package org.sq.gameDemo.svr.game.bag.service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.game.bag.model.EquitmentPart;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.model.ItemType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.*;
import java.util.stream.Collectors;

@Log4j
@Service
public class EquitService {

    @Autowired
    private BagService bagService;

    @Autowired
    private RoleAttributeService roleAttriService;

    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    /**
     * 玩家绑定装备
     * @param player
     */
    public void bindEquip(Player player) {
        if(!Strings.isNullOrEmpty(player.getEquipments())) {
            Map<Integer, Item> equipMap = JsonUtil.reSerializableJson(player.getEquipments(), new TypeReference<Map<Integer, Item>>() {});
            Optional.ofNullable(equipMap).ifPresent(map -> {
                    player.setEquipmentBar(map);
                    map.values().stream().forEach(item -> roleAttriService.bindEquipmentAttrToPlayer(player, item));
                    //计算战力
                    entityService.computeAttack(player);
                }
            );
        }

    }

    /**
     * 脱去装备
     * @param player
     * @param equip
     */
    public void removeEquip(Player player, Item equip) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(Objects.nonNull(equip) && equipmentBar.containsKey(equip.getItemInfo().getPart())) {
            roleAttriService.removeEquitAttrToPlayer(player, equip);
            //计算战力
            entityService.computeAttack(player);
            senceService.notifyPlayerByDefault(player, "脱去" + equip.getItemInfo().getName() + ", 目前战力" + player.getAttack());
            return;
        }
        senceService.notifyPlayerByDefault(player, equip.getItemInfo().getName() + "脱去失败, 该装备不存在");

    }

    /**
     * 穿上装备
     * @param player
     * @param equip
     */
    public void addEquip(Player player, Item equip) {

        Item equipExist = player.getEquipmentBar().get(equip.getItemInfo().getPart());

        String lastEquipName = equipExist.getItemInfo().getName();
        String equipName = equip.getItemInfo().getName();

        if(equipExist != null) {
            roleAttriService.removeEquitAttrToPlayer(player, equipExist);

            Integer level = equipExist.getLevel();
            Integer nowLevel = equip.getLevel();
            if(level > nowLevel) {
                senceService.notifyPlayerByDefault(player,
                        lastEquipName + "被脱下，等级为" + level + "; 等级比新穿上的" + equipName + "高");
            }

        }
        //绑定新装备属性
        roleAttriService.bindEquipmentAttrToPlayer(player, equip);
        //计算战力
        entityService.computeAttack(player);
        senceService.notifyPlayerByDefault(player, "穿上" + equipName + ", 目前战力" + player.getAttack());
    }


    /**
     * 物品是否属于装备类别
     */
    public boolean isEquip(Item item) {
        if(!item.getItemInfo().getType().equals(ItemType.EQUIT_ITEM.getType())) {
            return false;
        }
        return true;
    }

    /**
     * 获取玩家身体部位上的装备
     * @param player
     * @param part
     * @return
     */
    public Item getEquipByPart(Player player, int part) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        return equipmentBar.get(part);
    }

    /**
     * 检查武器损耗度
     * @param player
     * @return
     */
    public boolean equipCanUse(Player player) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(equipmentBar.size() != 0) {
            List<Item> badEquip = equipmentBar.values().stream().filter(equip -> equip.getDurable() <= 0).collect(Collectors.toList());
            if(badEquip.size() > 0) {
                senceService.notifyPlayerByDefault(player, "武器损耗不能使用啦, 花费金额来修复吧！ 损耗情况\r\n ");
                showEquip(player);
                return false;
            }
        }
        return true;
    }

    public void showEquip(Player player) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(equipmentBar.size() == 0) {
            senceService.notifyPlayerByDefault(player, "身上没有任何装备，穷的一丿");
            return;
        }
        Collection<Item> equips = equipmentBar.values();
        StringBuffer stringBuffer = new StringBuffer();
        for (Item item : equips) {
            ItemInfo itemInfo = item.getItemInfo();
            stringBuffer.append(EquitmentPart.getPartByCode(itemInfo.getPart()) + ":" + itemInfo.getName() + ", 耐力度:" + item.getDurable());
            stringBuffer.append("\r\n");
        }
        senceService.notifyPlayerByDefault(player, stringBuffer.toString());
    }

    /**
     * 修复武器持久度
     */

    public void repairEquip(Player player, Item equip, int durableNum) {

        ItemInfo itemInfo = equip.getItemInfo();
        if(!itemInfo.getType().equals(ItemType.EQUIT_ITEM.getType())) {
            senceService.notifyPlayerByDefault(player, itemInfo.getName() + " 不属于装备");
            return;
        }
        if(equip.getDurable() == itemInfo.getDurable()) {
            senceService.notifyPlayerByDefault(player, itemInfo.getName() + "没有被损耗，不需要修理");
            return;
        }
        if(durableNum + equip.getDurable() > itemInfo.getDurable() ) {
            senceService.notifyPlayerByDefault(player, "耐力值超出装备最高耐力值，请适当减少耐力数值");
            return;
        }
        //够不够钱
        int spend = durableNum * itemInfo.getRepairPrice();
        if(spend > player.getMoney()) {
            senceService.notifyPlayerByDefault(player, "修理需要" + spend + ", 不够钱啦，去杀怪赚钱吧!");
            return;
        }

        equip.setDurable(equip.getDurable() + durableNum);
        player.setMoney(player.getMoney() - spend);
        senceService.notifyPlayerByDefault(player, "修理成功," + equip.getItemInfo().getName() + "耐力值提升" + durableNum + "\r\n");
        showEquip(player);

    }



}
