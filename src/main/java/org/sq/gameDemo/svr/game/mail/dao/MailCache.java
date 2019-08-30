package org.sq.gameDemo.svr.game.mail.dao;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.mail.model.Mail;
import org.sq.gameDemo.svr.game.mail.model.MailConf;
import org.sq.gameDemo.svr.game.mail.model.MailExample;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MailCache {

    @Autowired
    private MailMapper mailMapper;

    private static Cache<Integer, Mail> mailCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "邮件被移除, 原因是" + notification.getCause())
            ).build();


    public Mail get(Integer id) {
        return  mailCache.getIfPresent(id);
    }

    public void put(Mail mail) {

        mailCache.put(mail.getId(), mail);
    }

    public Map<Integer, Mail> asMap() {
        return mailCache.asMap();
    }

    public void remove(Mail mail) {
        mailCache.invalidate(mail.getId());
    }
}
