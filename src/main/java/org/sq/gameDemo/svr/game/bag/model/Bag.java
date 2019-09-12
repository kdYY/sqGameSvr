package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Data
public class Bag {


    private Integer unId;
    String name;


    //背包容量
    Integer size;
    //存库用
    String itemStr;

    public String getItemStr() {
       synchronized (itemBar) {
           if(itemBar != null && itemBar.size() > 0) {
               itemStr = JsonUtil.serializableJson(itemBar);
           }
       }
        return itemStr;
    }

    //物品集合
    @ProtoField(Ignore = true)
    Map<Long, Item> itemBar = new ConcurrentSkipListMap<>();

    @ProtoField(TargetName = "item", TargetClass = ItemPt.Item.class)
    private List<Item> itemList;

    public List<Item> getItemList() {
        if(itemBar.size() > 0) {
            itemList = itemBar.values().stream().collect(Collectors.toList());
        } else {
            itemList = new ArrayList<>();
        }
        return itemList;
    }

    private Bag() {
    }

    public Bag(Integer userEntityId, String name, Integer size) {
        this.unId = userEntityId;
        this.name = name;
        this.size = size;
    }


    public Bag(Integer userEntityId, Integer size) {
        this.unId = userEntityId;
        this.size = size;
    }
}
