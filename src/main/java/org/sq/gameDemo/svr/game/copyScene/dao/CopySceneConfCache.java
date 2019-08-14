package org.sq.gameDemo.svr.game.copyScene.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;


import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class CopySceneConfCache {


    @Value("${excel.copySceneConfig}")
    private String fileName;
//<场景id,场景信息>
    /** 缓存不过期 **/
    private static Cache<Integer, CopySceneConfig> copySceneConf = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "场景信息被移除, 原因是" + notification.getCause())
            ).build();


    @PostConstruct
    private void initalSence() throws Exception {
        List<CopySceneConfig> scenceConfigs = PoiUtil.readExcel(fileName, 0, CopySceneConfig.class);

        scenceConfigs.forEach(config -> {
            copySceneConf.put(config.getId(), config);
        });
        log.info("副本任务配置表加载完毕");
    }


    public CopySceneConfig get(Integer id) {
        return copySceneConf.getIfPresent(id);
    }

    public void put(Integer id, CopySceneConfig value) {
        copySceneConf.put(id,value);
    }

}
