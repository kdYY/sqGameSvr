package org.sq.gameDemo.svr.game.store.dao;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.bag.dao.ItemInfoCache;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.store.model.Store;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class StoreCache {
    @Value("${excel.store}")
    private String fileName;

    @Autowired
    private ItemInfoCache itemInfoCache;

    private static Cache<Integer, Store> storeCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "邮件被移除, 原因是" + notification.getCause())
            ).build();

    @PostConstruct
    public void init() throws Exception {

        List<Store> storeList = PoiUtil.readExcel(fileName, 0, Store.class);

        for (Store store : storeList) {
            storeCache.put(store.getId(), store);
        }
        log.info("奖励邮件表加载完毕");

    }

    public Store get(Integer id) {
        Store store = storeCache.getIfPresent(id);
        if(store != null && store.getGoodsMap().size() == 0 && !Strings.isNullOrEmpty(store.getGoods())) {
            Arrays.stream(store.getGoods().split(",")).forEach(infoId -> {
                ItemInfo itemInfo = itemInfoCache.get(Integer.valueOf(infoId.trim()));
                store.getGoodsMap().put(itemInfo.getId(), itemInfo);
                store.getItemInfoList().add(itemInfo);
            });
        }
        return store;
    }

}