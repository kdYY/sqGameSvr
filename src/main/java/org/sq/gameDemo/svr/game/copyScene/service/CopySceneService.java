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
import org.sq.gameDemo.svr.game.fight.monsterAI.MonsterAIService;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class CopySceneService {

    private AtomicInteger ramSenceId = new AtomicInteger(5);

    @Autowired
    private CopySceneConfCache copySceneConfCache;

    @Autowired
    private SenceService senceService;

    @Autowired
    private SenceEntityCache senceEntityCache;

    @Autowired
    private PlayerCache playerCache;

    @Autowired
    private MonsterAIService monsterAIService;


    /**
     * 实例化副本
     * @param copyScenceId
     * @return
     */
    private CopyScene getInitedCopyScene(Integer copyScenceId) {
        CopyScene copyScene = new CopyScene();
        int senceId = ramSenceId.addAndGet(1);
        CopySceneConfig config = copySceneConfCache.get(copyScenceId);

        if(config == null) {
            return null;
        }

        SenceConfig.tmpCommonConf bossConf = JsonUtil.reJson(config.getBoss(), SenceConfig.tmpCommonConf.class);
        Monster bossMonster = senceService.getInitedMonster(senceEntityCache.get((long) bossConf.getId()), senceId, bossConf.getLevel());

        List<SenceConfig.tmpCommonConf> tmpCommonConfList = JsonUtil.reSerializableJson(config.getMonsters(), SenceConfig.tmpCommonConf.class);

        List<Monster> monsterList = new CopyOnWriteArrayList();
        for (SenceConfig.tmpCommonConf tmpCommonConf : tmpCommonConfList) {
            SenceEntity senceEntity = senceEntityCache.get((long) tmpCommonConf.getId());
            if(senceEntity.getTypeId().equals(Constant.Monster)) {
                for (int i = 0; i < tmpCommonConf.getNum(); i++) {
                    Monster monster = senceService.getInitedMonster(senceEntity, senceId, tmpCommonConf.getLevel());
                    //重要
                    monster.setRefreshTime(-1L);
                    monsterList.add(monster);
                }
            }
        }
        copyScene.setId(config.getId());
        copyScene.setSenceId(senceId);
        copyScene.setBoss(bossMonster);
        copyScene.setMonsterList(monsterList);
        copyScene.setPlayerList(new CopyOnWriteArrayList());
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
        ScheduledFuture<?> future = copyScene.getSingleThreadSchedule().scheduleWithFixedDelay( () -> {
            if(copyScene == null || copyScene.isGarbage()) {
                return;
            }
            if(copyScene.getBoss() == null && copyScene.getMonsterList().size() == 0) {
                copyScene.getPlayerList().forEach(
                        player -> {
                            senceService.notifyPlayerByDefault(player, "恭喜挑战副本成功，正在返回原来场景..");
                            exitCopyScene(player);
                        }
                );
            } else {
                copyScene.getMonsterList().stream().filter(monster -> monster.getState().equals(CharacterState.LIVE)).forEach(
                        monster -> copyScene.getPlayerList().stream().findAny().ifPresent(player -> monster.setTarget(player))
                );

                //刷新攻击
                monsterAIService.monsterAttacking(copyScene.getBoss());
               copyScene.getMonsterList()
                        .stream()
                        .filter(monster -> monster.getState().equals(CharacterState.ATTACKING.getCode()))
                        .forEach(monster -> monsterAIService.monsterAttacking(monster));
            }
        }, 2000,60, TimeUnit.MILLISECONDS);

        // 提前通知
        TimeTaskManager.threadPoolSchedule(copyScene.getMaxTime() - Constant.COPY_RIGHT_NOTIFY_BEFORE_TIME,
                () -> notifyCopyScene(copyScene, "副本(id:"+ copyScene.getSenceId() + ", name:" + copyScene.getName()
                        +") 将于十秒后关闭，赶紧打掉boss --- 充钱买礼包打得更快！")
        );

        // 挑战时间到
        TimeTaskManager.threadPoolSchedule(copyScene.getMaxTime(), () -> {
            destroyInstance(copyScene, future);
        });
        return future;
    }

    private void notifyCopyScene(CopyScene copyScene, String content) {
        Optional.ofNullable(copyScene).ifPresent(scene -> {
            scene.getPlayerList().forEach(
                    player ->  senceService.notifyPlayerByDefault(player, content));
        });
    }

    public void exitCopyScene(Player player) {
        CopyScene copyScene = (CopyScene) senceService.getSenecMsgById(player.getSenceId());
        try {
            senceService.moveToSence(player,
                    copyScene.getBeforeSenceIdMap().get(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //销毁副本
    private void destroyInstance(CopyScene copyScene, Future future) {
        Optional.ofNullable(copyScene).ifPresent(
                scene -> {
                    //将玩家移动到原来场景
                    CopyScene finalScene = scene;
                    //清缓存
                    senceService.getSenceCache().invalidate(scene);
                    scene.getPlayerList().forEach(player -> exitCopyScene(player));
                    future.cancel(false);
                    scene.inGarbage();
                    scene.getSingleThreadSchedule().shutdown();
                    scene = null;
                }
        );

    }


    /**
     * 玩家进入已存在的副本
     * @param copyScene
     * @param player
     */
    private void enterCopyScene(CopyScene copyScene, Player player) throws Exception {
        if(copyScene == null) {
            senceService.notifyPlayerByDefault(player, "无此副本场景");
            return;
        }

        if(copyScene.isGarbage()) {
            senceService.notifyPlayerByDefault(player, "副本场景被回收了");
            return;
        }
        if(copyScene.getPlayerList().size() >= copyScene.getLimit()) {
            senceService.notifyPlayerByDefault(player, "副本(id:" + copyScene.getSenceId() + ", name:"
                    + copyScene.getName() + "人数已满, 重新开一个吧");
            return;
        }
        notifyCopyScene(copyScene, "副本任务即将开始");
        //保存玩家原来的场景id
        copyScene.getBeforeSenceIdMap().put(player.getId(), player.getSenceId());
        //移动到副本场景
        senceService.moveToSence(player, copyScene.getSenceId());
        copyScene.getPlayerList().forEach( pl -> {
            senceService.notifyPlayerByDefault(player, pl.getName() + "进入 : (id:" + pl.getId()
                    + ") 进入副本 (id:" + copyScene.getSenceId() + ", name:" + copyScene.getName() + ", 剩余挑战时间: "
                    + (copyScene.getMaxTime() - (System.currentTimeMillis()  - copyScene.getStartTime())) + "), "
                    + "还可以进入的玩家个数 count:" + (copyScene.getLimit() - copyScene.getPlayerList().size()));
        });
        //怪物攻击
        if(copyScene.getLimit().equals(1)) {
            copyScene.getBoss().setTarget(player);
            copyScene.getMonsterList().forEach(monster -> {
                monster.setTarget(player);
            });
        } else {
            //第一个进入场景的吸引boss仇恨
            if(copyScene.getBoss().getTarget() != null) {
                copyScene.getBoss().setTarget(player);
            }

            copyScene.getMonsterList().stream().filter(monster -> monster.getTarget() != null).findAny().ifPresent(
                    hit -> hit.setTarget(player)
            );
            //所有玩家都进来了，无目标的群怪随机攻击
            if(copyScene.getPlayerList().size() == copyScene.getLimit()) {
                copyScene.getMonsterList().stream().filter(monster -> monster.getTarget() != null).forEach( mon -> {
                    mon.setTarget(copyScene.getPlayerList().stream().findAny().get());
                });
            }
        }
    }



    /**
     * 玩家进入新的副本
     * @param copySceneId
     * @param player
     */
    public CopyScene enterNewCopyScene(Integer copySceneId, Player player) throws Exception {
        CopyScene copyScene = getInitedCopyScene(copySceneId);
        //将场景保存到缓存
        senceService.getSenceCache().put(copyScene.getSenceId(), copyScene);
        enterCopyScene(copyScene, player);
        return copyScene;
    }


    /**
     * 玩家进入已存在的副本
     * @param copySceneId
     * @param player
     */
    public void enterExistCopyScene(Integer copySceneId, Player player) throws Exception {
        CopyScene copyScene = findCopyScene(copySceneId);
        //将场景保存到缓存
        enterCopyScene(copyScene, player);
    }

    /**
     * 找到副本场景
     * @param copySceneId
     * @return
     */
    private CopyScene findCopyScene(Integer copySceneId) {
        SenceConfigMsg senecMsgById = senceService.getSenecMsgById(copySceneId);
        if(senecMsgById instanceof CopyScene) {
            return (CopyScene) senecMsgById;
        }
        return null ;
    }

}
