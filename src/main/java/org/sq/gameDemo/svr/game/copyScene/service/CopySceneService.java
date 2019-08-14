package org.sq.gameDemo.svr.game.copyScene.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.SenceEntity;
import org.sq.gameDemo.svr.game.copyScene.dao.CopySceneConfCache;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.MonsterStateHandle;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CopySceneService {

    @Autowired
    private CopySceneConfCache copySceneConfCache;

    @Autowired
    private SenceService senceService;

    @Autowired
    private SenceEntityCache senceEntityCache;

    @Autowired
    private PlayerCache playerCache;


    /**
     * 实例化副本
     * @param copyScenceId
     * @return
     */
    private CopyScene getInitedCopyScene(Integer copyScenceId) {
        CopyScene copyScene = new CopyScene();
        int senceId = copyScene.hashCode();
        CopySceneConfig config = copySceneConfCache.get(copyScenceId);

        if(config == null) {
            return null;
        }

        SenceConfig.tmpCommonConf bossConf = JsonUtil.reJson(config.getBoss(), SenceConfig.tmpCommonConf.class);
        Monster bossMonster = senceService.getInitedMonster(senceEntityCache.get((long) bossConf.getId()), senceId, bossConf.getLevel());

        List<SenceConfig.tmpCommonConf> tmpCommonConfList = JsonUtil.reSerializableJson(config.getMonsters(), SenceConfig.tmpCommonConf.class);

        ArrayList<Monster> monsterList = new ArrayList<>();
        for (SenceConfig.tmpCommonConf tmpCommonConf : tmpCommonConfList) {
            SenceEntity senceEntity = senceEntityCache.get((long) tmpCommonConf.getId());
            if(senceEntity.getTypeId().equals(Constant.Monster)) {
                for (int i = 0; i < tmpCommonConf.getNum(); i++) {
                    Monster monster = senceService.getInitedMonster(senceEntity, senceId, tmpCommonConf.getLevel());
                    monsterList.add(monster);
                }
            }
        }
        copyScene.setId(config.getId());
        copyScene.setSenceId(senceId);
        copyScene.setBoss(bossMonster);
        copyScene.getMonsterList().addAll(monsterList);
        copyScene.setName(config.getName());
        copyScene.setLimit(config.getLimit());
        copyScene.setStartTime(System.currentTimeMillis());
        copyScene.setMaxTime(config.getTime());

        // 进入场景检查线程
        Future future = null;
        try {
            future = checkCopyScene(copyScene);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //graceful close
            if(future != null && copyScene != null) {
                destroyInstance(copyScene, future);
                log.debug("副本任务进行中，出现异常，进行关闭");
            }
        }

        return copyScene;
    }

    /**
     * 检查副本状况
     * @param copyScene
     * @return
     * @throws Exception
     */
    private Future checkCopyScene(CopyScene copyScene) throws Exception {
        ScheduledFuture<?> future = copyScene.getCopyScenceSingleThread().scheduleWithFixedDelay( () -> {
            //如果玩家全部掉线,回收场景
        }, 20,60, TimeUnit.MILLISECONDS);


        // 提前通知
        TimeTaskManager.threadPoolSchedule(copyScene.getMaxTime() - 10000,
                () -> Optional.ofNullable(copyScene).ifPresent(scene -> {
                    scene.getPlayerList().forEach(
                            player ->  senceService.notifyPlayerByDefault(player,
                                    "副本将于十秒后关闭，赶紧打掉boss --- 充钱买礼包打得更快！"));
                })
        );
        // 挑战时间到
        TimeTaskManager.threadPoolSchedule(copyScene.getMaxTime(), () -> {
            destroyInstance(copyScene, future);
        });
        return future;
    }

    //销毁副本
    private void destroyInstance(CopyScene copyScene, Future future) {
        Optional.ofNullable(copyScene).ifPresent(
                scene -> {
                    //将玩家移动到原来场景
                    CopyScene finalScene = scene;
                    //清缓存
                    senceService.getSenceIdAndSenceMsgMap().invalidate(scene);
                    scene.getPlayerList().forEach(player ->
                            senceService.moveToSence(player,
                                player.getSenceId(),
                                playerCache.getChannelByPlayerId(player.getId())));

                    future.cancel(false);
                    scene.inGarbage();
                    scene = null;
                }
        );

    }


    /**
     * 玩家进入已存在的副本
     * @param copyScene
     * @param player
     */
    public void enterCopyScene(CopyScene copyScene, Player player) {

        if(copyScene.getPlayerList().size() >= copyScene.getLimit()) {
            senceService.notifyPlayerByDefault(player, "副本(id:" + copyScene.getId() + ", name:"
                    + copyScene.getName() + "人数已满");
            return;
        }
    }



    /**
     * 玩家进入新的副本
     * @param copySceneId
     * @param player
     */
    public void enterNewCopyScene(Integer copySceneId, Player player) {
        CopyScene copyScene = getInitedCopyScene(copySceneId);

        if(copyScene == null) {
            senceService.notifyPlayerByDefault(player, "无此副本场景");
            return;
        }
        senceService.getSenceIdAndSenceMsgMap().put(copyScene.getSenceId(), copyScene);
        //移动到副本场景
        senceService.moveToSence(player, copyScene.getSenceId(), playerCache.getChannelByPlayerId(player.getId()));
        copyScene.getPlayerList().forEach( pl -> {
            senceService.notifyPlayerByDefault(player, pl.getName() + "(id:" + pl.getId()
                    + ") 进入副本 (id:" + copyScene.getId() + ", name:" + copyScene.getName() + ")");
        });
        //怪物攻击
        if(copyScene.getLimit().equals(1)) {
            copyScene.getBoss().setTarget(player);
            copyScene.getBoss().setState(CharacterState.ATTACKING.getCode());
            copyScene.getMonsterList().forEach(monster -> {
                monster.setTarget(player);
                monster.setState(CharacterState.ATTACKING.getCode());
            });
        }
    }

}
