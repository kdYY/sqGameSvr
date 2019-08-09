package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;

@Data
public class Item {
    private long id;

    private Integer count;

    /** 默认的背包格子是0 */
    private Integer locationIndex = 0;

    private ItemInfo itemInfo;

    /**武器耐久度 默认是-1*/
    private Long durable = -1L;

    public void setDurable(Long durable) {
        if(durable <= 0) {
            durable = 0L;
        }
        this.durable = durable;
    }

    private Integer level;

    private Item() {}

    //装备
    public Item(Long id, Integer count, ItemInfo itemInfo, Integer level) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
        //设置磨损度
        if(itemInfo.getType().equals(ItemType.EQUIT_ITEM)) {
            this.durable = itemInfo.getDurable();
        }
        this.level = level;
    }

    //物品
    public Item(Long id, Integer count, ItemInfo itemInfo) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
    }

}
