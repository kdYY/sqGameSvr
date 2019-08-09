package org.sq.gameDemo.svr.game.characterEntity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.TimeTaskManager;
import org.sq.gameDemo.svr.game.characterEntity.dao.BuffCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Buff;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Date;
import java.util.concurrent.Future;

@Slf4j
@Service
public class BuffService {

    @Autowired
    private BuffCache buffCache;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    public void buffAffecting(Character affecter, Buff buff) {
        Buff affectedBuff = new Buff();
        BeanUtils.copyProperties(buff, affectedBuff);

        //buff开始时间
        affectedBuff.setStartTime(System.currentTimeMillis());
        affecter.getBufferList().add(affectedBuff);

        Future cycleTask = TimeTaskManager.scheduleAtFixedRate(0, buff.getIntervalTime(),
                () -> {
                    if(affecter.getHp() >= 0) {
                        affecter.setHp(affecter.getHp() + buff.getHp());
                        affecter.setMp(affecter.getMp() + buff.getMp());
                        // 如果是玩家，进行通知
                        if (affecter instanceof Player) {
                            senceService.notifyPlayerByDefault(affecter,
                                    buff.getName() + "正作用在" + affecter.getName() + "身上");
                            // 检测玩家是否死亡
                            if(entityService.playerIsDead((Player) affecter,null)) {
                                //结束任务

                            }
                        }
                    }

                }
        );

        //如果不是永久性buff, 持续时间结束后停止
        if(affectedBuff.getDuration() != -1) {
            TimeTaskManager.scheduleByCallable(buff.getDuration(),
                    () -> {
                        cycleTask.cancel(true);
                        // 过期移除buffer
                        affecter.getBufferList().remove(buff);

                        // 如果是玩家，进行通知
                        if (affecter instanceof Player) {
                            senceService.notifyPlayerByDefault(affecter, buff.getName() + "取消作用，在" + affecter.getName() + "身上");
                            // 检测玩家是否死亡
                            entityService.playerIsDead((Player) affecter,null);
                        }
                        log.info(" buffer过期清除定时器 {}", new Date());
                        return null;
                    }
            );
        }
    }


    public Buff getBuff(Integer type) {
        return buffCache.get(type);
    }
}
