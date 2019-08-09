package org.sq.gameDemo.svr.game.bag.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j
public class ItemInfoCache {

    @Autowired
    private BagService bagService;

    @Value("${excel.itemInfo}")
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

        for (ItemInfo itemInfo : itemInfoList) {
            List<RoleAttribute> list = JsonUtil.reSerializableJson(itemInfo.getJsonStr(), RoleAttribute.class);
            put(itemInfo.getId(), itemInfo);
        }
        itemInfos = itemInfoCache.asMap().values().stream().collect(Collectors.toList());
        log.info("物品信息ItemInfo加载完毕");
    }

    public static List<ItemInfo> getItemInfos() {
        return itemInfos;
    }

    public ItemInfo get(Integer id) {
        ItemInfo itemInfo = itemInfoCache.getIfPresent(id);
        if(itemInfo != null && !itemInfo.getJsonStr().isEmpty() && !itemInfo.getItemRoleAttribute().isEmpty()) {
            bagService.bindItemAttr(itemInfo);
        }
        return itemInfo;
    }

    public void put(Integer id, ItemInfo value) {
        itemInfoCache.put(id,value);
    }

    public Map<Integer,ItemInfo> itemInfoMap() {
        return itemInfoCache.asMap();
    }
    
}
