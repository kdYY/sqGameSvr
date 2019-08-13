package org.sq.gameDemo.svr.game.drop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.drop.dao.ItemDropCache;
import org.sq.gameDemo.svr.game.drop.model.ItemDropConf;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class DropPool {

    @Autowired
    private ItemDropCache itemDropCache;
    @Autowired
    private BagService bagService;

    public List<Item> getDropItems(Character attacter, Monster monster) {
        List<Item> dropItem = new ArrayList<>();
        int level = 0;
        if(attacter instanceof Player) {
             level = ((Player) attacter).getLevel();
        }

        //找到满足指定等级下的所有物品
        int finalLevel = level;
        List<ItemDropConf> itemDropConfs = itemDropCache.getMap().values()
                                            .stream()
                                            .filter(o -> o.getMinPickerLevel() <= finalLevel && finalLevel < o.getMaxPickerLevel())
                                            .collect(Collectors.toList());
        //找到每个物品下指定怪物等级的掉落配置
        for (ItemDropConf itemDrop : itemDropConfs) {
            Optional<ItemDropConf.HolderDrop> first = itemDrop.getHolderDropList().stream()
                                                            .filter(holderDrop -> holderDrop.getMin() < monster.getLevel()
                                                                && holderDrop.getMax() >= monster.getLevel()).findFirst();
            if(first.isPresent()) {
                ItemDropConf.HolderDrop holderDrop = first.get();
                //得到(0,maxCount)随机数量
                int itemCount = getRamdonNumInRange(0, holderDrop.getMaxCount());
                if(itemCount == 0) {
                    continue;
                }
                //得到(玩家level, 玩家level+5)随机等级
                int itemLevel = getRamdonNumInRange(level, level+5);
                //按照概率区间得到物品id
                int itemInfoId = aimItem(itemDropConfs,  holderDrop.getProb());
                //如果该物品被命中
                if(itemDrop.getItemInfoId().equals(itemInfoId)) {
                    Optional.ofNullable(bagService.createItem(itemInfoId, itemCount, itemLevel)).ifPresent(item -> dropItem.add(item));
                }

            }
        }

        return dropItem;
    }

    // 放大倍数
    private static final int mulriple = 1000;

    public int aimItem(List<ItemDropConf> itemDropConfs, int prob) {
        int lastScope = 0;
        // 洗牌，打乱物品次序
        Collections.shuffle(itemDropConfs);
        Map<Integer, int[]> itemScopes = new HashMap<Integer, int[]>();
        int[] luckRange = new int[itemDropConfs.size()];
        int i = 0;
        for (ItemDropConf itemDropConf : itemDropConfs) {
            int itemInfoId = itemDropConf.getItemInfoId();
            // 划分区间
            int currentScope = lastScope + prob * mulriple;
            itemScopes.put(itemInfoId, new int[] { lastScope + 1, currentScope });
            luckRange[i++] = currentScope;
            lastScope = currentScope;
        }

        // 获取1-100000之间的一个随机数
        int luckyNumber = 0;
        for (int luck : luckRange) {
            Random random = new Random();
            luckyNumber += random.nextInt(luck);
        }

        int luckyPrizeId = 0;
        // 查找随机数所在的区间
        if ((null != itemScopes) && !itemScopes.isEmpty()) {

            int finalLuckyNumber = luckyNumber / luckRange.length;
            luckyPrizeId = itemScopes.entrySet().stream()
                    .filter(entry -> finalLuckyNumber >= entry.getValue()[0] && finalLuckyNumber < entry.getValue()[1])
                    .findFirst().map(Map.Entry::getKey).orElse(0);
        }

        return luckyPrizeId;
    }


    public int getRamdonNumInRange(int min, int max) {
        return new Random().nextInt(max)%(max-min+1) + min;
    }


    public static void main(String[] args) {
        float a = 8 / 10;
        System.out.println(a);
    }
}
