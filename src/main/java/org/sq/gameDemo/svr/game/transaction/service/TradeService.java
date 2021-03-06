package org.sq.gameDemo.svr.game.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.transaction.dao.TradeMapper;
import org.sq.gameDemo.svr.game.transaction.manager.TransactionCache;
import org.sq.gameDemo.svr.game.transaction.model.*;
import org.sq.gameDemo.svr.game.updateDB.UpdateDB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TradeService {
    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private TransactionCache transactionCache;

    /**
     * 插入交易
     * @param trade
     * @return
     */
    protected boolean insertTrade(Trade trade) {
        trade.setItemsMapStr(JsonUtil.serializableJson(trade.getAutionItemMap()));
        return tradeMapper.insertSelective(trade) > 0 ? true : false;
    }

    /**
     * 更新交易数据
     */
    public void updateTradeDB(Trade trade) {
        trade.setItemsMapStr(JsonUtil.serializableJson(trade.getAutionItemMap()));
        UpdateDB.dbTaskPool.execute(()->tradeMapper.updateByPrimaryKey(trade));
    }

    public void updateTraceDB() {
        for (DealTrade trade : transactionCache.dealAsMap().values()) {
            trade.setItemsMapStr(JsonUtil.serializableJson(trade.getAutionItemMap()));
            tradeMapper.updateByPrimaryKeySelective(trade);
        }
        for (OnlineTrade trade : transactionCache.onlineAsMap().values()) {
            trade.setItemsMapStr(JsonUtil.serializableJson(trade.getAutionItemMap()));
            tradeMapper.updateByPrimaryKeySelective(trade);
        }
    }

    /**
     * 查询交易
     * @param acceptUnId
     * @return
     */
    public List<Trade> selectOnlineByAccpetUnId(Integer acceptUnId) {
        TradeExample tradeExample = new TradeExample();
        tradeExample.createCriteria().andAcceptUnIdEqualTo(acceptUnId)
                .andTradeModelEqualTo(TradeModel.ONINE_TRADE.getCode())
                .andFinishEqualTo(false);
        return  tradeMapper.selectByExample(tradeExample);
    }


    //获取交易中需要的最高价
    public Integer getMaxPriceInTrade(Trade trade) {
        Integer maxPrice = trade.getPrice();
        if(trade.getTradeModel().equals(TradeModel.BID.getCode())) {
            return maxPrice;
        }
        if(trade.getAutionItemMap().size() == 1) {
            return maxPrice;
        }
        Optional<Integer> max = trade.getAutionItemMap().values().stream()
                .map(item -> item.getCount())
                .max(Comparator.comparingInt(Integer::intValue));
        if(max.isPresent()) {
            return max.get();
        } else {
            return maxPrice;
        }
    }

    /**
     * 获取交易栏为交易数据
     * @return
     */
    public List<Trade> getAllUnFinishDealTrade() {
        TradeExample tradeExample = new TradeExample();
        tradeExample.createCriteria().andTradeModelNotEqualTo(TradeModel.ONINE_TRADE.getCode()).andFinishEqualTo(false);
        return  tradeMapper.selectByExample(tradeExample);

    }

    public List<Trade> selectDealHistory(Integer unId) {
        return  tradeMapper.selectDealHistory(unId);
    }

    public List<Trade> selectFinishedOnlineTrade(Integer unId) {
        return  tradeMapper.selectOnlineTradeHistory(unId);
    }
}
