package org.sq.gameDemo.svr.game.copyScene.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.dao.CopySceneConfCache;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;
import org.sq.gameDemo.svr.game.fight.monsterAI.MonsterAIService;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private EntityService entityService;

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

        List<SenceConfig.tmpCommonConf> tmpCommonConfList = JsonUtil.reSerializableJson(config.getMonsters(), SenceConfig.tmpCommonConf
                .class);

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
            copyScene.setFuture(future);
        } catch (Exception e) {
            e.printStackTrace();
            //graceful close
            if(future != null && copyScene != null) {
                destroyInstance(copyScene, future);
                log.info("副本任务进行中，出现异常，进行关闭");
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
            //副本没人 到达一定时间 自动关闭
            if(copyScene.getPlayerList().size() == 0) {
                if(copyScene.getGarbageThreshold().decrementAndGet() <= 0) {
                    destroyInstance(copyScene, copyScene.getFuture());
                }
                return;
            } else {
                copyScene.resetGarbageThreshold();
            }
            if(copyScene.getBoss() == null && copyScene.getMonsterList().size() == 0) {
                copySceneChallegeSuccess(copyScene);

            } else {
                copyScene.getMonsterList().stream().filter(monster -> monster.getState().equals(CharacterState.LIVE.getCode())).forEach(
                        monster -> copyScene.getPlayerList().stream().findAny().ifPresent(player -> monster.setTarget(player))
                );

                //刷新攻击
                copyMonsterAttacking(copyScene, copyScene.getBoss());
                List<Monster> collect = copyScene.getMonsterList()
                        .stream()
                        .filter(monster -> monster.getState().equals(CharacterState.ATTACKING.getCode()))
                        .collect(Collectors.toList());
                for (Monster monster : collect) {
                    copyMonsterAttacking(copyScene, monster);
                }
//                //如果有玩家死完，死亡玩家退出副本
//                copyScene.getPlayerList().stream().filter(player -> player.getHp() <= 0).forEach(
//                        player -> {
//
//                        }
//                );
            }
        }, 2000,Constant.COPY_CHECK_RATE_TIME, TimeUnit.MILLISECONDS);

        // 提前通知
        ThreadManager.threadPoolSchedule(copyScene.getMaxTime() - Constant.COPY_RIGHT_NOTIFY_BEFORE_TIME,
                () ->
                        notifyCopyScene(copyScene, "副本(id:"+ copyScene.getSenceId() + ", name:" + copyScene.getName()
                        +") 将于十秒后关闭，赶紧打掉boss --- 充钱买礼包打得更快！")
        );

        // 挑战时间到
        ThreadManager.threadPoolSchedule(copyScene.getMaxTime(), () -> {
            notifyCopyScene(copyScene, "副本 name:" + copyScene.getName()
                    +") 挑戰失敗! ! !充钱买礼包打得更快！");
            destroyInstance(copyScene, future);
        });
        return future;
    }

    /**
     * 挑战成功后的处理 经验按对场景怪物造成的伤害进行平分
     * @param copyScene
     */
    private void copySceneChallegeSuccess(CopyScene copyScene) {
        copyScene.getPlayerList().forEach(
                player -> {
                    senceService.notifyPlayerByDefault(player, "恭喜挑战副本成功，正在返回原来场景..");
                    exitCopyScene(player, copyScene);
                }
        );
        destroyInstance(copyScene, copyScene.getFuture());
        //TODO 发送邮件奖励

    }

    private void copyMonsterAttacking(CopyScene copyScene, Monster monster) {
        try {
            monsterAIService.monsterAttacking(monster);
        } catch (CustomException.PlayerAlreadyDeadException e) {
            Player player = (Player) monster.getTarget();
            senceService.notifyPlayerByDefault(player, " 副本 name:" + copyScene.getName()
                    +") 挑戰失敗! ! !充钱买礼包打得更快！");
            //复活
            entityService.relivePlayer(player);
            exitCopyScene(player, copyScene);
        }
    }

    private void notifyCopyScene(CopyScene copyScene, String content) {
        Optional.ofNullable(copyScene).ifPresent(scene -> {
            scene.getPlayerList().forEach(
                    player ->  senceService.notifyPlayerByDefault(player, content));
        });
    }

    public void exitCopyScene(Player player, CopyScene copyScene) {
        try {
            senceService.moveToSence(player,
                    copyScene.getBeforeSenceIdMap().get(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //销毁副本
    private void destroyInstance(CopyScene copyScene, Future future) {
        log.info("副本场景销毁(senceId=" + copyScene.getSenceId() + ", name=" + copyScene.getName());
        Future finalFuture = future;
        Optional.ofNullable(copyScene).ifPresent(
                scene -> {
                    //将玩家移动到原来场景
                    CopyScene finalScene = scene;
                    scene.getPlayerList().forEach(player -> exitCopyScene(player, finalScene));
                    //清缓存
                    senceService.getSenceCache().invalidate(scene.getSenceId());
                    scene.inGarbage();
                    scene.getSingleThreadSchedule().shutdown();
                    scene = null;
                    if(finalFuture != null) {
                        finalFuture.cancel(true);
                    }
                }
        );
        future = null;
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
        Monster boss = copyScene.getBoss();
        //怪物攻击
        if(copyScene.getLimit().equals(1)) {
            boss.setTarget(player);
            copyScene.getMonsterList().forEach(monster -> {
                monster.setTarget(player);
            });
        } else {
            //第一个进入场景的吸引boss仇恨
            if(boss.getTarget() == null) {
                boss.setTarget(player);
            } else {
                //判断怪物的目标是不是战士类型
                if(boss.getTarget() instanceof Player
                        && ((Player) boss.getTarget()).getTypeId().equals(JobType.WARRIOR.getType())) {
                    copyScene.getPlayerList().stream()
                           .filter(player1 -> player1.getTypeId().equals(JobType.WARRIOR.getType()))
                           .findFirst()
                           .ifPresent(wairrior -> boss.setTarget(wairrior));

                }
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
    public CopyScene enterExistCopyScene(Integer copySceneId, Player player) throws Exception {
        CopyScene copyScene = findCopyScene(copySceneId);
        //将场景保存到缓存
        if(copyScene != null) {
            enterCopyScene(copyScene, player);
            return copyScene;
        } else {
            senceService.notifyPlayerByDefault(player, "副本任务不存在");
            return null;
        }
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

    /**
     * 找到所有的副本模板
     * @return
     */
    public Collection<CopySceneConfig> getAllCopySceneModel() {
        return copySceneConfCache.asMap().values();
    }


    /**
     * 找到所有的存在的副本场景
     * @return
     */
    public List<SenceConfigMsg> getAllExistCopyScene() {
        return senceService.getSenceCache().asMap().values().stream().filter(sence -> sence instanceof CopyScene).collect(Collectors.toList());
    }
}
