package org.sq.gameDemo.svr.game.drop.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.drop.model.ItemDropConf;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 物品掉落配置缓存
 */
@Component
@Slf4j
public class ItemDropCache {
    @Value("${excel.itemDropConf}")
    private String fileName;

    public static Cache<Integer,ItemDropConf> itemDropConfCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.info(cacheObj.getKey() + "角色属性被移除, 原因是" + cacheObj.getCause())
            ).build();


    /**
     * 初始化物品掉落配置
     * @throws Exception
     */
    @PostConstruct
    private void init() throws Exception {

        List<ItemDropConf> itemDropConfList = PoiUtil.readExcel(fileName, 0, ItemDropConf.class);
        for (ItemDropConf itemDrop : itemDropConfList) {
            String holderDropJson = itemDrop.getHolderDrop();
            List list = JsonUtil.reSerializableJson(holderDropJson, ItemDropConf.HolderDrop.class);
            itemDrop.getHolderDropList().addAll(list);
            put(itemDrop.getItemInfoId(), itemDrop);
        }
        log.info("物品掉落表配置加载完毕");
    }

    public ConcurrentMap<Integer,ItemDropConf> getMap() {
        return itemDropConfCache.asMap();
    }


    public void put(Integer id, ItemDropConf value) {
        itemDropConfCache.put(id,value);
    }

}
