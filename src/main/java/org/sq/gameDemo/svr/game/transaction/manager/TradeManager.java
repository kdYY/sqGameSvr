package org.sq.gameDemo.svr.game.transaction.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.transaction.model.OnlineTrade;
import org.sq.gameDemo.svr.game.transaction.service.DealTradeService;
import org.sq.gameDemo.svr.game.transaction.service.OnlineTradeService;
import org.sq.gameDemo.svr.game.updateDB.UpdateDB;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;



@Component
@Slf4j
public class TradeManager {

    @Autowired
    private TransactionCache transactionCache;
    @Autowired
    private OnlineTradeService onlineTradeService;
    @Autowired
    private DealTradeService dealTradeService;

//    private static ThreadFactory tradeCheckFactory = new ThreadFactoryBuilder()
//            .setNameFormat("transaction-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
//    /** 一个线程处理 */
//    private static ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(tradeCheckFactory);


    @PostConstruct
    private void init() {
        log.info("开始轮询交易状态");
        UpdateDB.dbTaskPool.scheduleWithFixedDelay(this::refreshTrace, 5000, 10000, TimeUnit.MILLISECONDS);
    }

    /**
     * 刷新交易
     */
    public void refreshTrace() {
        try {
            Map<Integer, OnlineTrade> onlineAsMap = transactionCache.onlineAsMap();
            onlineAsMap.values().stream()
                    .filter(onlineTrade -> onlineTrade.getStartTime() + onlineTrade.getKeepTime() <= System.currentTimeMillis())
                    .forEach(onlineTrade -> onlineTradeService.offTimeTrace(onlineTrade));
            transactionCache.dealAsMap().values().stream()
                    .filter(trade -> trade.getStartTime() + trade.getKeepTime() <= System.currentTimeMillis())
                    .forEach(trade -> dealTradeService.offTime(trade));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
