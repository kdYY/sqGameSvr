package org.sq.gameDemo.svr.game.characterEntity.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Buff;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * buff缓存
 */
@Slf4j
@Component
public class BuffCache {

    @Value("${excel.buff}")
    private String fileName;

    public static Cache<Integer,Buff> buffCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.debug(cacheObj.getKey() + "基础角色被移除, 原因是" + cacheObj.getCause())
            ).build();


    //存储所有角色类型
    private static List<Buff> buffs;

    @PostConstruct
    private void init() throws Exception {

        List<Buff> buffList = PoiUtil.readExcel(fileName, 0, Buff.class);
        for (Buff type : buffList) {
            put(type.getId(), type);
        }
        buffs = buffCache.asMap().values().stream().collect(Collectors.toList());
        log.info("基础角色类型Buff加载完毕");
    }

    public static List<Buff> getAllBuffs() {
        return buffs;
    }

    public Buff get(Integer id) {
        return buffCache.getIfPresent(id);
    }

    public void put(Integer typeId, Buff value) {
        buffCache.put(typeId,value);
    }

    public Map<Integer,Buff> buffMap() {
        return buffCache.asMap();
    }

}
