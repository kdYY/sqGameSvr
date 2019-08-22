package org.sq.gameDemo.svr.game.mail.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.SpringUtil;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.mail.model.Mail;
import org.sq.gameDemo.svr.game.mail.model.MailConf;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SystemMailConfCache {
    @Value("${excel.mail}")
    private String fileName;

    @Autowired
    private BagService bagService;

    private static Cache<Integer, MailConf> mailItemCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "邮件被移除, 原因是" + notification.getCause())
            ).build();

    @PostConstruct
    public void init() throws Exception {

        List<MailConf> mailConfs = PoiUtil.readExcel(fileName, 0, MailConf.class);

        for (MailConf mailConf : mailConfs) {
            List<MailConf.MailItemConf> list = JsonUtil.reSerializableJson(mailConf.getItemsStr(), MailConf.MailItemConf.class);
            if(list != null) {
                mailConf.setItemConfList(list);
            } else {
                mailConf.setItemConfList(new ArrayList<>());
            }
            mailItemCache.put(mailConf.getId(), mailConf);
        }
        log.info("奖励邮件表加载完毕");

    }

    public Mail get(Integer id) {
        MailConf mailConf = mailItemCache.getIfPresent(id);
        if(mailConf != null) {
            Mail mail = new Mail();
            BeanUtils.copyProperties(mailConf, mail);
            List<MailConf.MailItemConf> list = mailConf.getItemConfList();
            list.forEach(itemConf -> {
                mail.getRewardItems().add(bagService.createItem(itemConf.getId(), itemConf.getNum(), itemConf.getLevel()));
            });
            return mail;
        }
        return null;
    }

}
