package org.sq.gameDemo.svr.game.mail;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;

@Component
@Slf4j
public class MailItemCache {
    private static Cache<Integer, CopySceneConfig> copySceneConf = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "场景信息被移除, 原因是" + notification.getCause())
            ).build();
    public CopySceneConfig get(Integer id) {
        return copySceneConf.getIfPresent(id);
    }

    public void put(Integer id, CopySceneConfig value) {
        copySceneConf.put(id,value);
    }
}
