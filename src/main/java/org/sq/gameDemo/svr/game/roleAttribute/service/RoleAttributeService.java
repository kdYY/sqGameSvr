package org.sq.gameDemo.svr.game.roleAttribute.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleAttributeService {

    @Autowired
    private RoleAttributeCache roleAttrCache;
    @Autowired
    private EntityService entityService;

    /**
     * 物品增强玩家属性
     * @param player
     * @param equitment
     */
    public void bindEquipmentAttrToPlayer(Player player, Item equitment) {
        if(Objects.nonNull(equitment)) {

            Map<Integer, RoleAttribute> playerAttrMap = player.getRoleAttributeMap();
            Map<Integer, RoleAttribute> itemAttrMap = equitment.getItemInfo().getItemRoleAttribute();
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
                                    });
                        }
                );
            });
            player.getEquipmentBar().put(equitment.getItemInfo().getPart(), equitment);

            //设置装备耐久度

        }




    }


    /**
     * 移除装备玩家属性,重新计算战力
     */
    public void removeEquitAttrToPlayer(Player player, Item equitment) {
        Map<Integer, Item> equipmentBar = player.getEquipmentBar();
        if(equitment != null && equipmentBar.remove(equitment.getItemInfo().getPart(), equitment)) {
            Optional.ofNullable(equitment.getItemInfo().getItemRoleAttribute()).ifPresent(
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
                        player.setB_Hp(player.getB_Hp() + Long.valueOf(attr.getValue()));
                        if(player.getHp() <= 0) {
                            player.setHp(player.getB_Hp() * player.getLevel());
                        }
                    }
            );
        } else if (cachedRoleAttr.getName().contains("MP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent( attr -> {
                player.setB_Mp(player.getB_Mp() + Long.valueOf(attr.getValue()));
                if(player.getMp() <= 0) {
                    player.setMp(player.getB_Mp() * player.getLevel());
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
                        if(player.getHp() > player.getB_Hp()) {
                            player.setHp(player.getB_Hp() * player.getLevel());
                        }
                    }
            );
        } else if (cachedRoleAttr.getName().contains("MP")) {
            Optional.ofNullable(cachedRoleAttr).ifPresent(
                    attr -> {
                        player.setB_Mp(player.getB_Mp() - Long.valueOf(attr.getValue()));
                        if(player.getMp() > player.getB_Mp()) {
                            player.setMp(player.getB_Mp() * player.getLevel());
                        }
                    }
            );
        }
    }

    public RoleAttribute getRoleAttr(Integer id) {
        return roleAttrCache.get(id);
    }
}
