package org.sq.gameDemo.svr.game.mail.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.game.mail.dao.MailCache;
import org.sq.gameDemo.svr.game.mail.model.Mail;
import org.sq.gameDemo.svr.game.mail.service.MailService;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MailCheckManager {

    @Autowired
    private MailCache mailCache;
    @Autowired
    private MailService mailService;


    private static ThreadFactory mailCheckThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("mail-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 一个线程处理 */
    static ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(mailCheckThreadFactory);


    @PostConstruct
    private void init() {
        log.info("开始轮询邮件状态");
        singleThreadSchedule.scheduleWithFixedDelay(this::refreshMail, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 刷新邮件
     */
    private void refreshMail() {
        Map<Integer, Mail> mailMap = mailCache.asMap();

        mailMap.values()
                .stream()
                .filter(mail -> !mail.getSenderUnId().equals(Constant.SYSTEM_UNID))
                .filter(mail -> mail.getTime() + mail.getKeepTime() <= System.currentTimeMillis())
                .forEach(mail -> {
                    mailService.returnItems(mail);
                });
    }
}
