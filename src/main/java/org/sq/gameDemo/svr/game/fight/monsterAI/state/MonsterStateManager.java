package org.sq.gameDemo.svr.game.fight.monsterAI.state;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.TimedTaskManager;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 怪物状态管理器
 */
@Slf4j
@Component
public class MonsterStateManager {

    @Autowired
    private SenceService senceService;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillCache skillCache;


    private boolean check = true;

    HashMap<CharacterState, MonsterStateHandle> stateHandleMap = new HashMap<>();

    {
        stateHandleMap.put(CharacterState.MOVE1, this::move);
        stateHandleMap.put(CharacterState.ATTACK1, this::attack);
        stateHandleMap.put(CharacterState.DEADBYATTACT1, this::deadByAttack);
        stateHandleMap.put(CharacterState.IS_REFRESH1, this::refreshMonster);
    }

    private static ThreadFactory monsterAIThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("monster-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 一个线程处理 */
    ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(monsterAIThreadFactory);


   // @PostConstruct
    private void init() {
        singleThreadSchedule.execute(
                () ->{
                    List<Integer> allSenceId = senceService.getAllSenceId();
                    log.info("开始轮询怪物状态");
                    while(true) {
                        allSenceId.stream().forEach(
                                senceId -> {
                                    if(senceId.equals(3)) {
                                        System.out.println("11");
                                    }
                                    List<Monster> monsterList = senceService.getSenecMsgById(senceId).getMonsterList();
                                    monsterList.forEach(monster -> {
                                        try {
                                            stateHandleMap.get(CharacterState.getStateByCode(monster.getState())).handle(monster);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                        });

                    }
                });
    }


    /**
     * 怪物死亡状态
     * @param monster
     */
    private void deadByAttack(Monster monster) {
        if(monster.getHp() <= 0 && monster.getState() != CharacterState.IS_REFRESH1.getCode()) {
            //死亡通知
            UserCache.broadcastChannelGroupBysenceId(monster.getSenceId(), CharacterState.DEADBYATTACT1.getDescr());
            monster.setTarget(null);
            //通知刷新
            monster.setState(CharacterState.IS_REFRESH1.getCode());
        }
    }

    /**
     * 怪物刷新状态
     * @param monster
     */
    private void refreshMonster(Monster monster) {
        SenceConfigMsg senecMsgById = senceService.getSenecMsgById(monster.getSenceId());
        TimedTaskManager.schedule(monster.getRefreshTime(),
                ()->{
                    senecMsgById.getSingleThreadSchedule().execute(
                            () -> {
                                UserCache.broadcastChannelGroupBysenceId(senecMsgById.getSenceId(),
                                        "id=" + monster.getId() + "(" + monster.getName() + ")已经刷新");
                                monster.setState(CharacterState.MOVE1.getCode());
                            }
                    );
                });
    }


    /**
     * 怪物移动状态
     * @param monster
     */
    private void move(Monster monster) throws InterruptedException {
        if(monster.getTarget() == null) {
            UserCache.broadcastChannelGroupBysenceId(monster.getSenceId(),
                    "id:" + monster.getId() + "(" + monster.getName() + ") " + CharacterState.MOVE1.getDescr());
        } else {
            if(monster.getHp() > 0) {
                monster.setState(CharacterState.ATTACK1.getCode());
            } else {
                monster.setState(CharacterState.DEADBYATTACT1.getCode());
            }
        }
    }

    /**
     * 怪物攻击状态
     * @param monster
     */
    private void attack(Monster monster)  {
        Character target = monster.getTarget();
        if(target != null) {
            //攻击同场景内的玩家
            if(target instanceof UserEntity && ((Player) target).getSenceId().equals(monster.getSenceId())) {
                Skill skill = skillCache.get(Integer.valueOf(monster.getSkillStr()));
                skillService.characterUseSkillAttack(monster, monster.getTarget(), skill, senceService.getSenecMsgById(monster.getSenceId()));
            }
        }
    }




}
