package org.sq.gameDemo.svr.game.scene.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.MonsterStateManager;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SenceRefreshManager {

    @Autowired
    private SenceService senceService;

    @Autowired
    private MonsterStateManager monsterStateManager;

    private static ThreadFactory monsterAIThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("monster-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 一个线程处理 */
    static ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(monsterAIThreadFactory);


    @PostConstruct
    private void init() {
        log.info("开始轮询场景实体状态");
        //考虑使用场景对应的线程来做，就不用担心线程安全问题.....
        singleThreadSchedule.scheduleWithFixedDelay(this::refreshMonsterState, 1000, 60, TimeUnit.MILLISECONDS);
    }

    /**
     * 刷新非副本场景
     */
    private void refreshMonsterState() {
        senceService.getSenceCache().asMap().values().stream().filter(msg -> !(msg instanceof CopyScene) ).forEach(
                senceCnfMsg -> {
                    List<Monster> monsterList = senceCnfMsg.getMonsterList();
                    if(senceCnfMsg instanceof CopyScene) {
                        monsterStateManager.refreshMonsterState(((CopyScene)senceCnfMsg).getBoss());
                    }
                    monsterList.forEach(monsterStateManager::refreshMonsterState);
                }
        );

    }
}
