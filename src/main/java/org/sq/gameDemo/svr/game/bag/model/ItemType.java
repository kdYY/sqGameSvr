package org.sq.gameDemo.svr.game.bag.model;

public enum ItemType {


    //消耗品
    CONSUMABLE_ITEM(1),

    //装备
    EQUIT_ITEM(2),

    //可叠加
    CAN_BE_STACKED(3),

    ;
    private Integer type;

    ItemType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
