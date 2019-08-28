package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class Item {
    private long id;

    private Integer count; // 武器不能叠加

    private Integer locationIndex = 0;


    /**武器耐久度 默认是-1*/
    private Long durable = -1L;

    public void setDurable(Long durable) {
        if(durable <= 0) {
            durable = 0L;
        }
        this.durable = durable;
    }


    private Integer level;

    public Item() {}

    //装备
    public Item(Long id, Integer count, ItemInfo itemInfo, Integer level) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
        //设置磨损度
        this.durable = itemInfo.getDurable();
        this.level = level;
    }

    //物品
    public Item(Long id, Integer count, ItemInfo itemInfo) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
    }

    @ProtoField(TargetName = "itemInfo", Function = "addItemInfoPt", TargetClass = ItemPt.Item.Builder.class)
    private ItemInfo itemInfo;


    /**
     * 做item中ItemInfo的注入
     * @param builder
     * @throws Exception
     */
    public void addItemInfoPt(ItemPt.Item.Builder builder) throws Exception {
        builder.setItemInfo((ItemInfoPt.ItemInfo) ProtoBufUtil.transformProtoReturnBean(ItemInfoPt.ItemInfo.newBuilder(), this.getItemInfo()));
    }
}
