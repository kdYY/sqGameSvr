package org.sq.gameDemo.svr.game.drop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.drop.dao.ItemDropCache;
import org.sq.gameDemo.svr.game.drop.model.ItemDropConf;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class DropService {

    @Autowired
    private ItemDropCache itemDropCache;
    @Autowired
    private BagService bagService;

    public List<Item> getDropItems(Player player, Monster monster) {
        List<Item> dropItem = new ArrayList<>();
        Integer level = player.getLevel();
        //找到满足指定等级下的所有物品
        List<ItemDropConf> itemDropConfs = itemDropCache.getMap().values()
                                            .stream()
                                            .filter(o -> o.getMinPickerLevel() < level && level > o.getMaxPickerLevel())
                                            .collect(Collectors.toList());
        //找到每个物品下指定怪物等级的掉落配置
        for (ItemDropConf itemDrop : itemDropConfs) {
            Optional<ItemDropConf.HolderDrop> first = itemDrop.getHolderDropList().stream()
                                                            .filter(holderDrop -> holderDrop.getMin() < monster.getLevel()
                                                                && holderDrop.getMax() >= monster.getLevel()).findFirst();
            if(first.isPresent()) {
                ItemDropConf.HolderDrop holderDrop = first.get();
                //得到(1,maxCount)随机数量
                int itemCount = getRamdonNumInRange(1, holderDrop.getMaxCount());
                //得到(玩家level - 5, 玩家level)随机等级
                int itemLevel = getRamdonNumInRange(level - 5, level);
                //按照概率区间得到物品id
                int itemInfoId = aimItem(itemDropConfs, holderDrop.getProb());

                dropItem.add(bagService.createItem(itemInfoId, itemCount, itemLevel));
            }
        }

        return dropItem;
    }

    // 放大倍数
    private static final int mulriple = 100;

    public int aimItem(List<ItemDropConf> itemDropConfs, Integer prob) {
        int lastScope = 0;
        // 洗牌，打乱物品次序
        Collections.shuffle(itemDropConfs);
        Map<Integer, int[]> itemScopes = new HashMap<Integer, int[]>();
        for (ItemDropConf itemDropConf : itemDropConfs) {
            int itemInfoId = itemDropConf.getItemInfoId();
            // 划分区间
            int currentScope = lastScope + prob * mulriple;
            itemScopes.put(itemInfoId, new int[] { lastScope + 1, currentScope });

            lastScope = currentScope;
        }

        // 获取1-1000000之间的一个随机数
        int luckyNumber = new Random().nextInt(mulriple);
        int luckyPrizeId = 0;
        // 查找随机数所在的区间
        if ((null != itemScopes) && !itemScopes.isEmpty()) {
            Set<Map.Entry<Integer, int[]>> entrySets = itemScopes.entrySet();
            for (Map.Entry<Integer, int[]> m : entrySets) {
                int key = m.getKey();
                if (luckyNumber >= m.getValue()[0] && luckyNumber <= m.getValue()[1]) {
                    luckyPrizeId = key;
                    break;
                }
            }
        }

        return luckyPrizeId;
    }


    public int getRamdonNumInRange(int min, int max) {
        return new Random().nextInt(max)%(max-min+1) + min;
    }
}
