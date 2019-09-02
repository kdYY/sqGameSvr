package org.sq.gameDemo.svr.game.bag.model;

import java.util.LinkedList;
import java.util.Queue;

public enum ItemType {




    //装备
    EQUIT_ITEM(1),
    //消耗品
    CONSUMABLE_ITEM(2),
    //可叠加
    CAN_BE_STACKED(3),

    ;
    private Integer type;
    private static Queue<ItemType> typeQueue = new LinkedList();

    static {
        typeQueue.add(EQUIT_ITEM);
        typeQueue.add(CONSUMABLE_ITEM);
        typeQueue.add(CAN_BE_STACKED);
    }
    ItemType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static Queue<ItemType> getTypeQueue() {
        return typeQueue;
    }
}
