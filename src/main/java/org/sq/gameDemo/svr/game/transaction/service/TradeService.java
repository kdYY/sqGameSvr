package org.sq.gameDemo.svr.game.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.transaction.dao.TradeMapper;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;
import org.sq.gameDemo.svr.game.transaction.model.Trade;
import org.sq.gameDemo.svr.game.transaction.model.TradeExample;
import org.sq.gameDemo.svr.game.transaction.model.TradeModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TradeService {
    @Autowired
    private TradeMapper tradeMapper;

    /**
     * 插入交易
     * @param trade
     * @return
     */
    protected boolean insertTrade(Trade trade) {
        return tradeMapper.insertSelective(trade) > 0 ? true : false;
    }

    /**
     * 更新交易数据
     */
    public void updateTrace(Trade trade) {
        ThreadManager.dbTaskPool.execute(()->tradeMapper.insertSelective(trade));
    }


    public List<Trade>  selectByAccpetUnId(Integer acceptUnId) {
        TradeExample tradeExample = new TradeExample();
        tradeExample.createCriteria().andAcceptUnIdEqualTo(acceptUnId).andFinishEqualTo(false);
        return  tradeMapper.selectByExample(tradeExample);
    }



    public Integer getMaxPriceInTrade(Trade trade) {
        Integer maxPrice = trade.getPrice();
        if(trade.getTradeModel().equals(TradeModel.BID.getCode())) {
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
}
