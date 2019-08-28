package org.sq.gameDemo.svr.game.transaction.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;
import org.sq.gameDemo.svr.game.transaction.model.OnlineTrade;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TransactionCache {

    @Autowired
    private static Cache<Integer, DealTrade> dealCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + " deal 被移除, 原因是" + notification.getCause())
                )
            .build();

    @Autowired
    private static Cache<Integer, OnlineTrade> onlineTradeCache = CacheBuilder.newBuilder()
            .removalListener(
               notification -> System.out.println(notification.getKey() + " onlineTradeCache 被移除, 原因是" + notification.getCause())
            )
            .build();

    @PostConstruct
    public void init() {

    }

    public void putDeal(DealTrade dealTrade) {
        dealCache.put(dealTrade.getId(), dealTrade);
    }

    public void putOnlineTrade(OnlineTrade onlineTrade) {
        onlineTradeCache.put(onlineTrade.getId(), onlineTrade);
    }

    public void removeDeal(DealTrade dealTrade) {
        dealCache.invalidate(dealTrade.getId());
    }

    public void removeOnlineTrade(OnlineTrade onlineTrade) {
        onlineTradeCache.invalidate(onlineTrade.getId());
    }

    public DealTrade getDealInCache(Integer id) {
        return dealCache.getIfPresent(id);
    }

    public OnlineTrade getOnlineTrade(Integer id) {
        return onlineTradeCache.getIfPresent(id);
    }

    public Map<Integer, OnlineTrade> onlineAsMap() {
        return onlineTradeCache.asMap();
    }

    public Map<Integer, DealTrade> dealAsMap() {
        return dealCache.asMap();
    }

}
