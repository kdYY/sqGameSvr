package org.sq.gameDemo.svr.game.bag.service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.CollectorEvent;
import org.sq.gameDemo.svr.game.bag.dao.BagMapper;
import org.sq.gameDemo.svr.game.bag.dao.ItemInfoCache;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.model.ItemType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.equip.service.EquitService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BagService {

    @Autowired
    private ItemInfoCache itemInfoCache;

    @Autowired
    private RoleAttributeService roleAttributeService;

    @Autowired
    private BuffService buffService;

    @Autowired
    private BagMapper bagMapper;

    @Autowired
    private SenceService senceService;

    @Autowired
    private EquitService equitService;

    /**
     * 物品和拥有对应的属性进行绑定
     * @param itemInfo
     */
    public void bindItemAttr(ItemInfo itemInfo) {
        List<ItemRoleAttri> attriList = JsonUtil.reSerializableJson(itemInfo.getJsonStr(), ItemRoleAttri.class);

        Optional.ofNullable(attriList).ifPresent(attris -> {
            attris.forEach(attri -> {
                RoleAttribute roleAttr = roleAttributeService.getRoleAttr(attri.getId());
                RoleAttribute roleAttribute = new RoleAttribute();
                BeanUtils.copyProperties(roleAttr, roleAttribute);
                roleAttribute.setValue(attri.getValue());
                itemInfo.getItemRoleAttribute().put(roleAttr.getId(), roleAttribute);
            });
        });
    }

    /**
     * 获取所有物品展示
     * @return
     */
    public List<ItemInfo> showAllItemInfo() {
        return itemInfoCache.itemInfoMap().values().stream().collect(Collectors.toList());
    }


    /**
     * 获取物品信息
     * @param id
     * @return
     */
    public ItemInfo getItemInfo(Integer id) {
        return itemInfoCache.get(id);
    }

    /**
     * 使用背包中的物品
     * @param player
     * @param itemId
     * @return
     */
    public boolean useItem(Player player, Long itemId, Integer count) {
        Bag bag = player.getBag();
        Item item = bag.getItemBar().get(itemId);
        if(item == null  || item.getItemInfo().getBuff() == null) {
            senceService.notifyPlayerByDefault(player, "该物品不属于非装备消耗型物品");
            return false;
        }
        if(item.getCount() < count) {
            senceService.notifyPlayerByDefault(player, "物品数量不足");
            return false;
        }
        ItemInfo itemInfo = itemInfoCache.get(item.getItemInfo().getId());
        senceService.notifyPlayerByDefault(player, "开始使用" + itemInfo.getName() );
        if(itemInfo.getBuff().equals(Constant.EXPITEM_BUFF)) {
            player.addExp(count);
        } else {
            for (int i = 0; i < count; i++) {
                Optional.ofNullable(buffService.getBuff(itemInfo.getBuff()))
                        .ifPresent(buff -> buffService.buffAffecting(player, buff));
            }
        }
        return removeItem(player, item.getId(), count);
    }

    /**
     * 创建物品条目， 怪物爆出物品装备的时候
     */
    public Item createItem(Integer itemInfoId, Integer count, Integer level) {
        ItemInfo itemInfo = getItemInfo(itemInfoId);
        return createItem(itemInfo, count,level);
    }

    public Item createItem(ItemInfo itemInfo, Integer count, Integer level) {
        if (Objects.isNull(itemInfo)) {
            return null;
        }
        if(itemInfo.getType().equals(ItemType.EQUIT_ITEM.getType())) {
            return new Item(ConcurrentSnowFlake.getInstance().nextID(), count, itemInfo, level);
        } else {
            return new Item(ConcurrentSnowFlake.getInstance().nextID(), count, itemInfo);
        }

    }

    /**
     * 将用户和背包进行绑定
     * @param player
     */
    public void bindBag(Player player) {

        Bag bag = bagMapper.selectByPrimaryKey(player.getUnId());
        if(bag != null) {
            if(!Strings.isNullOrEmpty(bag.getItemStr())) {
                Map<Long, Item> itemMap = JsonUtil.reSerializableJson(bag.getItemStr(), new TypeReference<Map<Long, Item>>() {});
                player.getBag().getItemBar().putAll(itemMap);
            }
        } else {
            player.getBag().setUnId(player.getUnId());
        }


    }

    /**
     * 更新数据库中玩家的背包信息
     * @param player
     */
    public void updateBagInDB(Player player) {
//        Bag bag = bagMapper.selectByPrimaryKey(player.getUnId());
//        ThreadManager.dbTaskPool.execute(() -> {
//            if(bag == null) {
//                bagMapper.insert(player.getBag());
//            } else {
//                bagMapper.updateByPrimaryKey(player.getBag());
//            }
//        });

    }
    public void updateBag(Player player) {
        Bag bag = bagMapper.selectByPrimaryKey(player.getUnId());
        Bag playerBag = player.getBag();
        playerBag.setUnId(player.getUnId());
        if(bag == null) {
            bagMapper.insert(player.getBag());
        } else {
            bagMapper.updateByPrimaryKeySelective(playerBag);
        }
    }

    /**
     * 放置物品进去背包
     */
    public boolean addItemInBag(Player player, Item item) {
        Bag bag = player.getBag();

        if(Objects.isNull(item)) {
            return false;
        }

        Integer itemInfoId = item.getItemInfo().getId();

        Optional<Item> itemOptional = bag.getItemBar().values()
                .stream()
                .filter(filterItem -> filterItem.getItemInfo().getId().equals(itemInfoId))
                .findFirst();

        Integer bagSize = getBagCurrentSize(bag);

        //可以叠加
        if(itemOptional.isPresent() && item.getItemInfo().getType().equals(ItemType.CAN_BE_STACKED.getType())) {
            Item itemFind = itemOptional.get();
            itemFind.setCount(itemFind.getCount() + item.getCount());
            senceService.notifyPlayerByDefault(player, item.getItemInfo().getName() + "物品 *" + item.getCount() + "已经放入背包");
        }
        //不可叠加的时候
        else if(bag.getSize() > bagSize) {
            bag.getItemBar().put(item.getId(), item);
            senceService.notifyPlayerByDefault(player, item.getItemInfo().getName() + "物品 *" + item.getCount() + "已经放入背包");
        }
        //背包已经满了
        else {
            senceService.notifyPlayerByDefault(player, "背包已满，请清除背包");
            return false;
        }
        EventBus.publish(new CollectorEvent(player, item));
        updateBagInDB(player);
        return true;
    }

    /**
     * 计算目前背包容量
     */
    private Integer getBagCurrentSize(Bag bag) {
        ConcurrentMap<Integer, Integer> collect = bag.getItemBar().values()
                .stream()
                .filter(item -> !item.getItemInfo().getId().equals(Constant.YUAN_BAO))
                .collect(Collectors.groupingByConcurrent(bagItem -> bagItem.getItemInfo().getType(), Collectors.summingInt(Item::getCount)));
        int result = 0;

        for (Integer type : collect.keySet()) {
            if(type.equals(ItemType.CAN_BE_STACKED.getType())) {
               result += (collect.get(type) > 100 ? (collect.get(type)/100) : 1);
            } else {
                result += collect.get(type);
            }
        }

        return result;
    }

    /**
     * 移除物品
     * @param player
     * @param itemId
     * @return
     */
    public boolean removeItem(Player player, Long itemId, Integer count) {
        Bag bag = player.getBag();
        Map<Long, Item> itemMap = bag.getItemBar();
        if(!itemMap.isEmpty()) {
            Item item = findItem(player, itemId, count);
            String itemName = item.getItemInfo().getName();

            //非装备 同时数量够减
            if(!equitService.isEquip(item) && item.getCount() > count) {
                item.setCount(item.getCount() - count);
            }else {
                if(!itemMap.remove(itemId, item)) {
                    senceService.notifyPlayerByDefault(player, itemName + "移除失败");
                    return false;
                }
            }

            senceService.notifyPlayerByDefault(player, itemName + " * " + count + "从背包中移除");
            updateBagInDB(player);
            return true;
        }
        return false;
    }


    /**
     * 整理背包
     */
    public void tidyBag(Player player) {
        Bag bag = player.getBag();

        synchronized (bag) {
            Map<Long, Item> itemMap = bag.getItemBar();
            Map<Long, Item> newItemMap = new ConcurrentSkipListMap<>();

            for (ItemType itemType : ItemType.getTypeQueue()) {
                Map<Long, Item> collect = itemMap.values()
                        .stream()
                        .filter(item -> item.getItemInfo().getType().equals(itemType.getType()))
                        .collect(Collectors.toMap(item -> item.getId(), item -> item));

                if(!collect.isEmpty()) {
                    newItemMap.putAll(collect);
                }
            }

            bag.setItemBar(itemMap);
        }
        updateBagInDB(player);
        senceService.notifyPlayerByDefault(player, "背包整理完毕");

    }

    /*寻找背包中物品*/
    public Item findItem(Player player, Long itemId, Integer count) {
        Bag bag = player.getBag();
        Map<Long, Item> itemMap = bag.getItemBar();
        Item itemInBag = null;
        if(!itemMap.isEmpty()) {
            itemInBag = itemMap.get(itemId);
            if(itemInBag == null) {
                senceService.notifyPlayerByDefault(player, "id=" + itemId + "物品不存在");
                return null;
            }

            if(itemInBag.getCount() < count) {
                senceService.notifyPlayerByDefault(player, "id=" + itemId + "物品数量不足");
                return null;
            }
        } else {
            senceService.notifyPlayerByDefault(player, "背包为空");
        }
        return itemInBag;
    }

    /*寻找背包中物品*/
    public Item findItem(Player player, Integer iteminfoId, Integer count) {
        Item itemInBag = findItem(player, iteminfoId);
        if(itemInBag != null && itemInBag.getCount() < count) {
            senceService.notifyPlayerByDefault(player, "iteminfoId=" + iteminfoId + "物品数量不足");
            return null;
        }
        return itemInBag;

    }
    /*寻找背包中物品*/
    public Item findItem(Player player, Integer iteminfoId) {
        Bag bag = player.getBag();
        Map<Long, Item> itemMap = bag.getItemBar();

        Item itemInBag = null;
        if(!itemMap.isEmpty()) {
            Optional<Item> find = player.getBag().getItemBar().values().stream().filter(item -> item.getItemInfo().getId().equals(iteminfoId)).findFirst();
            if(!find.isPresent()) {
                senceService.notifyPlayerByDefault(player, "iteminfoId=" + iteminfoId + "物品不存在");
                return null;
            }
            itemInBag = find.get();
        } else {
            senceService.notifyPlayerByDefault(player, "背包为空");
        }
        return itemInBag;

    }


    @Data
    public static class ItemRoleAttri {
        Integer id;
        Integer value;

        //RoleAttribute roleAttribute;
    }
}
