package org.sq.gameDemo.svr.game.transaction.model;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.Item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class Trade {
    private Integer id;

    private Long startTime;
    private Long keepTime;

    private Integer ownerUnId;
    private Integer acceptUnId;

    private Integer tradeModel;

    //物品json字符串
    @ProtoField(Ignore = true)
    private String itemsMapStr;

    private Integer count;
    private Integer itemInfoId;
    //价格
    private Integer price;


    private boolean finish = false;
    private boolean success = false;

    //proto工具类使用 之后改良
    public boolean getFinish() {
        return finish;
    }

    //proto工具类使用
    public boolean getSuccess() {
        return success;
    }


    public boolean isFinish() {
        return finish;
    }

    public boolean isSuccess() {
        return success;
    }


    //<<用户UnId, Item>>
    @ProtoField(TargetName = "item", Function = "addItemPt", TargetClass =TradePt.Trade.Builder.class)
    private Map<Integer, Item> autionItemMap = new ConcurrentHashMap<>();

    /**
     * 做trade中Item的注入
     * @param builder
     * @throws Exception
     */
    public void addItemPt(TradePt.Trade.Builder builder) throws Exception {
        List<Item> collect = this.getAutionItemMap().values().stream().collect(Collectors.toList());
        for (Item item : collect) {
            builder.addItem((ItemPt.Item) ProtoBufUtil.transformProtoReturnBean(ItemPt.Item.newBuilder(), item));
        }
    }

    public Map<Integer, Item> getAutionItemMap() {
        if(autionItemMap.size() == 0 && !Strings.isNullOrEmpty(itemsMapStr)) {
            this.autionItemMap  = JsonUtil.reSerializableJson(itemsMapStr, new TypeReference<ConcurrentHashMap<Integer,  Item>>(){});
        }
        return autionItemMap;
    }

    public String getItemsMapStr() {
        if(autionItemMap != null && Strings.isNullOrEmpty(itemsMapStr)) {
            itemsMapStr = JsonUtil.serializableJson(this.autionItemMap);
        }
        return itemsMapStr;
    }
}
