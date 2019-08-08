package org.sq.gameDemo.svr.game.bag.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j
public class ThingInfoCache {


    @Value("${excel.thingInfo}")
    private String fileName;

    public static Cache<Integer,ItemInfo> itemInfoCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.debug(cacheObj.getKey() + "物品被移除, 原因是" + cacheObj.getCause())
            ).build();


    //存储所有角色类型
    private static List<ItemInfo> itemInfos;

    @PostConstruct
    private void init() throws Exception {

        List<ItemInfo> itemInfoList = PoiUtil.readExcel(fileName, 0, ItemInfo.class);
        for (ItemInfo type : itemInfoList) {
            put(type.getId(), type);
        }
        itemInfos = itemInfoCache.asMap().values().stream().collect(Collectors.toList());
        log.info("基础角色类型ThingInfo加载完毕");
    }

    public static List<ItemInfo> getItemInfos() {
        return itemInfos;
    }

    public ItemInfo get(Integer typeId) {
        return itemInfoCache.getIfPresent(typeId);
    }

    public void put(Integer typeId, ItemInfo value) {
        itemInfoCache.put(typeId,value);
    }

    public Map<Integer,ItemInfo> thingInfoMap() {
        return itemInfoCache.asMap();
    }
    
}
