package org.sq.gameDemo.svr.game.characterEntity.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.EntityType;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EntityTypeCache {
    
    @Value("${excel.entityType}")
    private String fileName;

    public static Cache<Integer,EntityType> entityTypeCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.debug(cacheObj.getKey() + "基础角色被移除, 原因是" + cacheObj.getCause())
            ).build();


    //存储所有角色类型
    private List<EntityType> entityTypes;

    @PostConstruct
    private void init() throws Exception {

        List<EntityType> entityTypeList = PoiUtil.readExcel(fileName, 0, EntityType.class);
        for (EntityType type : entityTypeList) {
            put(type.getId(), type);
        }
        entityTypes = entityTypeCache.asMap().values().stream().collect(Collectors.toList());
        log.info("基础角色表加载完毕");
    }

    public List<EntityType> getAllEntityTypes() {
        return entityTypes;
    }

    public EntityType get(Integer id) {
        return entityTypeCache.getIfPresent(id);
    }

    public void put(Integer id, EntityType value) {
        entityTypeCache.put(id,value);
    }

    public Map<Integer,EntityType> entityTypeMap() {
        return entityTypeCache.asMap();
    }
}
