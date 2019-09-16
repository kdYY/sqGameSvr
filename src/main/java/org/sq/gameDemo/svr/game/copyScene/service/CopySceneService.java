package org.sq.gameDemo.svr.game.copyScene.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.CopySceneFinishedEvent;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.dao.CopySceneConfCache;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;
import org.sq.gameDemo.svr.game.fight.monsterAI.MonsterAIService;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.team.model.Team;
import org.sq.gameDemo.svr.game.team.service.TeamService;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.sq.gameDemo.common.OrderEnum.ENTER_COPY;
import static org.sq.gameDemo.common.OrderEnum.SHOW_COPY_SENCE;

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

    @Autowired
    private TeamService teamService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BagService bagService;


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

        List<SenceConfig.tmpCommonConf> tmpCommonConfList =
                JsonUtil.reSerializableJson(config.getMonsters(), SenceConfig.tmpCommonConf.class);

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
        copyScene.setRewardExp(config.getExp());
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

    private void startCopyScene(List<Player> playerList, CopyScene copyScene) {
        for (Player player : playerList) {
            senceService.notifyPlayerByDefault(player, "warning !! you will be send to the copy !!");
            copyScene.getOwners().add(player.getUnId());
        }
        //开启计时检测线程
        copyScene.getSingleThreadSchedule().schedule(()-> {

            for (Player player : playerList) {
                if(player != null) {
                    senceService.notifyPlayerByDefault(player, " the copy active!! ");
                    try {
                        enterCopyScene(copyScene, player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, 3000, TimeUnit.MILLISECONDS);

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
            //挑战成功
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

        ArrayList<Player> players = new ArrayList<>();
        players.addAll(copyScene.getPlayerList());
        EventBus.publish(new CopySceneFinishedEvent(players, copyScene.getId()));
        Map<Integer, Long> damagePercentage = copyScene.getBossDamagePercentage();
        long count = 0L;
        for (Long damage : damagePercentage.values()) {
            count += damage;
        }
        if(count <= 0) {
            log.info("副本伤害计算有bug");
            return;
        }
        for (Map.Entry<Integer, Long> entry : damagePercentage.entrySet()) {
            Long getExp = (entry.getValue() / count) * copyScene.getRewardExp();
            Item item = bagService.createItem(Constant.EXP_ITEMINFO, getExp.intValue(), 1);
            mailService.sendMail(entityService.getSystemPlayer(), entry.getKey(), "副本挑战成功经验获奖", "这是您在副本中获取的物品", item);
        }
        copyScene.getPlayerList().forEach(
                player -> {
                    senceService.notifyPlayerByDefault(player, "恭喜挑战副本成功，正在返回原来场景..");
                    exitCopyScene(player, copyScene);
                }
        );
        destroyInstance(copyScene, copyScene.getFuture());

    }

    /**
     * 副本内怪物AI攻击
     */
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

    /**
     * 副本通知
     */
    private void notifyCopyScene(CopyScene copyScene, String content) {
        Optional.ofNullable(copyScene).ifPresent(scene -> {
            scene.getPlayerList().forEach(
                    player ->  senceService.notifyPlayerByDefault(player, content));
        });
    }

    /**
     * 离开副本
     */
    public void exitCopyScene(Player player, CopyScene copyScene) {
        try {
            senceService.moveToSence(player,
                    copyScene.getBeforeSenceIdMap().get(player.getId()));
            senceService.notifyPlayerByDefault(player, "使用" + ENTER_COPY.getOrder() + " senceId=" + copyScene.getId() + "继续挑战副本");
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
    private void enterCopyScene(CopyScene copyScene, Player player) throws Exception{
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

        //保存玩家原来的场景id
        copyScene.getBeforeSenceIdMap().put(player.getId(), player.getSenceId());
        //移动到副本场景
        senceService.moveToSence(player, copyScene.getSenceId());
        notifyCopyScene(copyScene, "副本任务即将开始");
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
                        && !((Player) boss.getTarget()).getTypeId().equals(JobType.WARRIOR.getType())) {
                    copyScene.getPlayerList().stream()
                           .filter(pl -> pl.getTypeId().equals(JobType.WARRIOR.getType()))
                           .findFirst()
                           .ifPresent(wairrior -> boss.setTarget(wairrior));
                }
            }

            copyScene.getMonsterList().stream().filter(monster -> monster.getTarget() == null).findAny().ifPresent(
                    hit -> hit.setTarget(player)
            );
            //所有玩家都进来了，无目标的群怪随机攻击
            if(copyScene.getPlayerList().size() == copyScene.getLimit()) {
                copyScene.getMonsterList().stream().filter(monster -> monster.getTarget() == null).forEach( mon -> {
                    mon.setTarget(copyScene.getPlayerList().stream().findAny().get());
                });
            }
        }
    }



    /**
     * 玩家进入新的副本 单人挑战副本
     * @param copySceneId
     * @param player
     */
    public CopyScene enterNewCopyScene(Integer copySceneId, Player player) throws Exception {
        CopySceneConfig config = copySceneConfCache.get(copySceneId);
        if(config == null ) {
            senceService.notifyPlayerByDefault(player, "副本id不存在, 使用" + SHOW_COPY_SENCE.getOrder() + "查看副本");
            return null;
        }
        if(config.getLimit().equals(1)) {
            return enterSingle(copySceneId, player);
        } else {
            return enterWithTeam(copySceneId, player);
        }
    }

    /**
     * 进入单人副本
     */
    private CopyScene enterSingle(Integer copySceneId, Player player) throws Exception {
        CopyScene copyScene = getInitedCopyScene(copySceneId);
        if(copyScene == null ) {
            senceService.notifyPlayerByDefault(player, "副本id不存在, 使用" + SHOW_COPY_SENCE.getOrder() + "查看副本");
            return null;
        }
        //将场景保存到缓存
        senceService.getSenceCache().put(copyScene.getSenceId(), copyScene);
        //开始检测场景
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        startCopyScene(players, copyScene);
        return copyScene;
    }
    /**
     * 玩家进入新的副本 组队挑战副本
     * @param copySceneId
     */
    private CopyScene enterWithTeam(Integer copySceneId, Player captain) throws Exception {
        Team team = teamService.getTeam(captain);
        if(team == null) {
            senceService.notifyPlayerByDefault(captain, "你还没加入队伍");
            return null;
        }
        if(!team.getCaptainId().equals(captain.getId())) {
            senceService.notifyPlayerByDefault(captain, "你8是队伍队长，不能开启该副本");
            return null;
        }
        CopySceneConfig config = copySceneConfCache.get(copySceneId);

        if(team.getPlayerInTeam().values().size() != config.getLimit()) {
            senceService.notifyPlayerByDefault(entityService.getPlayer(team.getCaptainId()), "人数不满足条件，该副本需要"+ config.getLimit() +" 个玩家进入," +
                    " 使用" + SHOW_COPY_SENCE.getOrder() + "查看副本详情");
            return null;
        }
        CopyScene copyScene = getInitedCopyScene(copySceneId);
        //将场景保存到缓存
        senceService.getSenceCache().put(copyScene.getSenceId(), copyScene);
        startCopyScene(team.getPlayerInTeam().keySet().stream().map(id -> entityService.getPlayer(id)).collect(Collectors.toList()), copyScene);
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
        if(copyScene != null && copyScene.getOwners().contains(player.getUnId())) {
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
    public List<SenceConfigMsg> getAllExistCopyScene(Player player) {
        return senceService.getSenceCache().asMap().values()
                .stream()
                .filter(sence -> sence instanceof CopyScene && ((CopyScene) sence).getOwners().contains(player.getUnId()))
                .collect(Collectors.toList());
    }
}
