package org.sq.gameDemo.svr.game.characterEntity.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.SenceEntity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class SenceEntityCache {
    @Value("${excel.senceEntity}")
    private String fileName;

    public static Cache<Long, SenceEntity> senceEntityCache = CacheBuilder.newBuilder()
            .removalListener(
                    cacheObj -> log.info(cacheObj.getKey() + "基础角色被移除, 原因是" + cacheObj.getCause())
            ).build();

    @PostConstruct
    private void init() throws Exception {
        List<SenceEntity> senceEntityList = PoiUtil.readExcel(fileName, 0, SenceEntity.class);
        for (SenceEntity senceEntity : senceEntityList) {
            put(senceEntity.getId(), senceEntity);
        }
        log.info("非玩家角色加载完毕");
    }


    public SenceEntity get(Long id) {
        return senceEntityCache.getIfPresent(id);
    }

    public void put(Long id, SenceEntity value) {
        senceEntityCache.put(id,value);
    }


    public ConcurrentMap asMap () {
        return senceEntityCache.asMap();
    }
}
