package org.sq.gameDemo.svr.game.bag.service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.game.bag.dao.BagMapper;
import org.sq.gameDemo.svr.game.bag.dao.ItemInfoCache;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.model.ItemType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.*;
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

        for (int i = 0; i < count; i++) {
            Optional.ofNullable(buffService.getBuff(itemInfo.getBuff()))
                    .ifPresent(buff -> buffService.buffAffecting(player, buff));
        }
        return removeItem(player, item.getId(), count);
    }

    /**
     * 创建物品条目， 怪物爆出物品装备的时候
     */
    public Item createItem(Integer itemInfoId, Integer count, Integer level) {
        ItemInfo itemInfo = getItemInfo(itemInfoId);

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
        Optional.ofNullable(bagMapper.selectByPrimaryKey(player.getUnId())).ifPresent(
                bag -> {
                    Bag playerBag = player.getBag();
                    if(bag.getItemStr().isEmpty()) {
                        playerBag.setItemBar(new LinkedHashMap<>());
                    } else {
                        Map<Long, Item> itemMap = JsonUtil.reSerializableJson(
                                bag.getItemStr(), new TypeReference<Map<Long, Item>>() {});
                        playerBag.setItemBar(itemMap);
                    }
                    //player.setBag(playerBag);
                }
        );
        if(player.getBag().getUnId() == null) {
            player.getBag().setUnId(player.getUnId());
        }

    }

    /**
     * 更新数据库中玩家的背包信息
     * @param player
     */
    public void updateBagInDB(Player player) {
        Bag bag = bagMapper.selectByPrimaryKey(player.getUnId());
        if(bag == null) {
            bagMapper.insert(player.getBag());
        } else {
            bagMapper.updateByPrimaryKey(player.getBag());
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
        //可以叠加
        if(itemOptional.isPresent() && item.getItemInfo().getType().equals(ItemType.CAN_BE_STACKED.getType())) {
            Item itemFind = itemOptional.get();
            itemFind.setCount(itemFind.getCount() + item.getCount());
            senceService.notifyPlayerByDefault(player, item.getItemInfo().getName() + "物品 *" + item.getCount() + "已经放入背包");
        }
        //不可叠加的时候
        else if(bag.getSize() > bag.getItemBar().keySet().size()) {
            bag.getItemBar().put(item.getId(), item);
            senceService.notifyPlayerByDefault(player, item.getItemInfo().getName() + "物品 *" + item.getCount() + "已经放入背包");
        }
        //背包已经满了
        else {
            senceService.notifyPlayerByDefault(player, "背包已满，请整理背包");
            return false;
        }
        return true;
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
            Item item = itemMap.get(itemId);
            String itemName = item.getItemInfo().getName();
            if(item == null) {
                senceService.notifyPlayerByDefault(player, itemName + "物品不存在");
                return false;
            }

            if(item.getCount() < count) {
                senceService.notifyPlayerByDefault(player, "物品数量不足");
                return false;
            }

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
            Map<Long, Item> newItemMap = new LinkedHashMap<>();

            for (ItemType itemType : ItemType.values()) {
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

        senceService.notifyPlayerByDefault(player, "背包整理完毕");
    }


    @Data
    public static class ItemRoleAttri {
        Integer id;
        Integer value;

        //RoleAttribute roleAttribute;
    }
}
