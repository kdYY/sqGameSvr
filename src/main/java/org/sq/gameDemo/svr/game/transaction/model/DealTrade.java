package org.sq.gameDemo.svr.game.transaction.model;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.Item;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Data
public class DealTrade extends Trade{

    public DealTrade() {
        super();
    }

    public DealTrade(Integer ownerUnId, Item auctionItem, Integer price, Integer tradeModel) {
        super.setOwnerUnId(ownerUnId);
        super.setKeepTime(3 * 60 * 1000L);
        super.setStartTime(System.currentTimeMillis());
        super.setTradeModel(tradeModel);
        super.setPrice(price);

        this.getAutionItemMap().put(ownerUnId, auctionItem);

    }



}
