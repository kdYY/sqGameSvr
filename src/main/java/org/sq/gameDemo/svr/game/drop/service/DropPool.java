package org.sq.gameDemo.svr.game.drop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.bag.model.Item;

import java.util.List;

/**
 * 物品掉落池 根据人物级别和怪物级别有一定概率掉落对应的物品
 */
@Component
public class DropPool {

    @Autowired
    private DropService dropService;



    public static DropPool dropPool = new DropPool();

    private DropPool() {}

    public List<Item> dropItem(Integer monLevel, Integer plyLevel) {
        //根据用户级别筛选小于等于plyLevel的可以掉落的物品， 根据怪物级别筛选出现的物品的数量
        return dropService.getDropItems(monLevel, plyLevel);
    }






}
