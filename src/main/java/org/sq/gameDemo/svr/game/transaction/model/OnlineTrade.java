package org.sq.gameDemo.svr.game.transaction.model;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper=true)
public class OnlineTrade extends Trade{

    @ProtoField(Ignore = true)
    private Player itemOwner;
    @ProtoField(Ignore = true)
    private Player accpeter;
    @ProtoField(TargetName = "accpertItemInfo", Function = "addItemInfoPt", TargetClass = TradePt.Trade.Builder.class)
    private ItemInfo accpertItemInfo;

    private Long auctionItemId;
    /**
     * 做item中ItemInfo的注入
     * @param builder
     * @throws Exception
     */
    public void addItemInfoPt(TradePt.Trade.Builder builder) throws Exception {
        builder.setAccpertItemInfo((ItemInfoPt.ItemInfo) ProtoBufUtil.transformProtoReturnBean(ItemInfoPt.ItemInfo.newBuilder(), this
                .getAccpertItemInfo()));
    }

    public OnlineTrade() {super();}

    public OnlineTrade(Player itemOwner, Player accpeter, Item auctionItem, ItemInfo accpertItemInfo, Integer count) {

        super.setOwnerUnId(itemOwner.getUnId());
        super.setAcceptUnId(accpeter.getUnId());
        super.setKeepTime(3 * 60 * 60 * 1000L);
        super.setStartTime(System.currentTimeMillis());
        super.setTradeModel(TradeModel.ONINE_TRADE.getCode());

        super.setItemInfoId(accpertItemInfo.getId());

        super.setCount(count);

        this.itemOwner = itemOwner;
        this.accpeter = accpeter;
        this.setAccpertItemInfo(accpertItemInfo);
        this.auctionItemId = auctionItem.getId();
        super.getAutionItemMap().put(itemOwner.getUnId(), auctionItem);
    }


}
