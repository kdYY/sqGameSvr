package org.sq.gameDemo.svr.game.roleAttribute.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.WearEquipEvent;
import org.sq.gameDemo.svr.game.bag.dao.ItemInfoCache;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleAttributeService {

    @Autowired
    private RoleAttributeCache roleAttrCache;
    @Autowired
    private ItemInfoCache itemInfoCache;
    @Autowired
    private SenceService senceService;

    /**
     * 物品增强玩家属性
     * @param player
     * @param equitment
     */
    public void bindEquipmentAttrToPlayer(Player player, Item equitment) {
        if(Objects.nonNull(equitment)) {

            Map<Integer, RoleAttribute> playerAttrMap = player.getRoleAttributeMap();
            Map<Integer, RoleAttribute> itemAttrMap = itemInfoCache.get(equitment.getItemInfo().getId()).getItemRoleAttribute();
            Optional.ofNullable(itemAttrMap).ifPresent(map -> {
                map.values().forEach(
                        attr -> {
                            if (attr.getTypeId().equals(1)) {
                                initPlayerHpAndMp(player, attr);
                            }
                            //增强玩家对应的属性值
                            Optional.ofNullable(attr.getId())
                                    .map(playerAttrMap::get)
                                    .ifPresent(playerAttr -> {
                                        playerAttr.setValue(playerAttr.getValue() + attr.getValue());
                                        senceService.notifyPlayerByDefault(player, attr.getName() + "提升↑" + attr.getValue());
                                    });
                        }
                );
            });
            player.getEquipmentBar().put(equitment.getItemInfo().getPart(), equitment);
        }




    }


    /**
     * 移除装备玩家属性,重新计算战力
     */
    public void removeEquitAttrToPlayer(Player player, Item equitment) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(equitment != null && equipmentBar.remove(equitment.getItemInfo().getPart(), equitment)) {
            Optional.ofNullable(itemInfoCache.get(equitment.getItemInfo().getId()).getItemRoleAttribute()).ifPresent(
                    equitAttrMap -> {
                        equitAttrMap.values()
                                    .forEach(attr -> {
                                        if (attr.getTypeId().equals(1)) {
                                            cutPlayerHpAndMp(player, attr);
                                        }
                                        RoleAttribute roleAttribute = player.getRoleAttributeMap().get(attr.getId());
                                        Optional.ofNullable(roleAttribute).ifPresent(
                                                attribute -> attribute.setValue(roleAttribute.getValue() - attr.getValue())
                                        );
                                    });
                    }
            );
            player.getEquipmentBar().remove(equitment.getItemInfo().getPart(), equitment);
        }
    }


    /**
     * 绑定玩家属性
     * @param player
     */
    public void bindRoleAttr(Player player) {
        Map<Integer, RoleAttribute> attributeMap = player.getRoleAttributeMap();
        for (Integer id : RoleAttributeCache.roleAttributeCache.asMap().keySet()) {
            RoleAttribute cachedRoleAttr = roleAttrCache.get(id);
            if(cachedRoleAttr.getTypeId().equals(1)) {
                initPlayerHpAndMp(player, cachedRoleAttr);
            }
            RoleAttribute attribute = new RoleAttribute();
            BeanUtils.copyProperties(cachedRoleAttr, attribute);
            attributeMap.put(id, attribute);
        }

    }

    /**
     * 根据属性增益设置玩家hp mp值
     * @param player
     * @param cachedRoleAttr
     */
    private void initPlayerHpAndMp(Player player, RoleAttribute cachedRoleAttr) {
        if(cachedRoleAttr.getName().contains("HP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent(
                    attr -> {
                        //玩家登陆初始化
                        if(player.getB_Hp() == null || player.getB_Hp() <= 0) {
                            player.setB_Hp(Long.valueOf(attr.getValue()) + player.getLevel() * 10);
                            player.setHp(player.getB_Hp());
                        }
                        //装备增益
                        else {
                            player.setB_Hp(player.getB_Hp() + Long.valueOf(attr.getValue()));
                        }
                    }
            );
        } else if (cachedRoleAttr.getName().contains("MP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent( attr -> {
                //玩家登陆初始化
                if(player.getB_Mp() == null || player.getB_Mp() <= 0) {
                    player.setB_Mp(Long.valueOf(attr.getValue()) + player.getLevel() * 5);
                    player.setMp(player.getB_Mp());
                }
                //装备增益
                else {
                    player.setB_Mp(player.getB_Mp() + Long.valueOf(attr.getValue()));
                }
            });
        }
    }

    /**
     * 根据物品属性减益设置hp mp值
     * @param player
     * @param cachedRoleAttr
     */
    private void cutPlayerHpAndMp(Player player, RoleAttribute cachedRoleAttr) {
        if(cachedRoleAttr.getName().contains("HP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent(
                    attr -> {
                        player.setB_Hp(player.getB_Hp() - Long.valueOf(attr.getValue()));
                        player.setHp(player.getHp());
                    }
            );
        } else if (cachedRoleAttr.getName().contains("MP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent(
                    attr -> {
                        player.setB_Mp(player.getB_Mp() - Long.valueOf(attr.getValue()));
                        player.setMp(player.getMp());
                    }
            );
        }
    }

    public RoleAttribute getRoleAttr(Integer id) {
        return roleAttrCache.get(id);
    }
}
