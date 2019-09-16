package org.sq.gameDemo.svr.game.updateDB;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.guild.service.GuildService;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.task.service.TaskService;
import org.sq.gameDemo.svr.game.transaction.service.TradeService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class UpdateDB {

    @Autowired
    private BagService bagService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MailService mailService;
    @Autowired
    private GuildService guildService;
    @Autowired
    private TradeService tradeService;

    private static ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat("dbTaskPool-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    public static ScheduledExecutorService dbTaskPool = Executors.newSingleThreadScheduledExecutor(factory);

    @PostConstruct
    private void init() {
        log.info("启动数据库刷新器");
        dbTaskPool.scheduleWithFixedDelay(this::refreshDB, 10000, 60000, TimeUnit.MILLISECONDS);
    }
    /**
     * 刷新数据库
     */
    private void refreshDB() {
        List<Player> allPlayer = entityService.getAllPlayer();
        for (Player player : allPlayer) {
            bagService.updateBag(player);
            log.info("定时更新背包数据库");
            taskService.updateTask(player);
            log.info("定时更新任务数据库");
        }
        mailService.updateMailDB();
        log.info("定时更新邮件数据库");
        guildService.updateGuildDB();
        log.info("定时更新公会数据库");
        tradeService.updateTraceDB();
        log.info("定时更新交易数据库");
    }
}
