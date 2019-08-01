package org.sq.gameDemo.svr.eventManage.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.MonsterBeAttackedEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterDeadEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */

@Component
@Slf4j
public class MonsterDeadEventHandler {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SenceService senceService;

    {
        EventBus.registe(MonsterDeadEvent.class, this::monsterDead);
        log.info("怪物死亡事件注册成功");
    }

    private  void monsterDead(MonsterDeadEvent monsterDeadEvent) {
        //玩家增加经验， 检查任务进度，掉落物品，触发幸运大爆 ....
        Character attacter = monsterDeadEvent.getAttacter();
        if(attacter instanceof Player) {

            Player player = (Player) attacter;
            Monster targetMonster = monsterDeadEvent.getTargetMonster();

            player.addExp(Constant.MONSTER_EXP);
            //设置死亡状态
            targetMonster.setDeadStatus();

            playerCache.getChannelByPlayerId(attacter.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity(targetMonster.getName()
                            + "(id=" + targetMonster.getId()
                            +")被你杀死了, 经验增加"
                            + Constant.MONSTER_EXP
                            + ",当前exp="
                            + player.getExp()
                    )
            );

        }
    }



}
