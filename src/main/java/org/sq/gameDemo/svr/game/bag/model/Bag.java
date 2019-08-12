package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Bag {


    private Long userEntityId;
    String name;


    //背包格子数
    Integer size;
    //存库用
    String itemStr;

    public String getItemStr() {
        if(itemBar != null && itemBar.size() > 0) {
            itemStr = JsonUtil.serializableJson(itemBar);
        }
        return itemStr;
    }

    //物品集合

    @ProtoField(Ignore = true)
    Map<Long, Item> itemBar = new LinkedHashMap<>();

    private Bag() {
    }

    public Bag(Long userEntityId, String name, Integer size) {
        this.userEntityId = userEntityId;
        this.name = name;
        this.size = size;
    }


    public Bag(Long playerId, Integer size) {
        this.userEntityId = playerId;
        this.size = size;
    }
}
