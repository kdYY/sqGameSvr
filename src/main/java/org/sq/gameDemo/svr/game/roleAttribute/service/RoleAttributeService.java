package org.sq.gameDemo.svr.game.roleAttribute.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import java.util.Map;
import java.util.Optional;

@Service
public class RoleAttributeService {

    @Autowired
    private RoleAttributeCache roleAttrCache;


    public void bindRoleAttr(Player player) {
        Map<Integer, RoleAttribute> attributeMap = player.getRoleAttributeMap();
        for (Integer id : RoleAttributeCache.roleAttributeCache.asMap().keySet()) {
            RoleAttribute cachedRoleAttr = roleAttrCache.get(id);
            if(cachedRoleAttr.getTypeId().equals(1)) {
                if(cachedRoleAttr.getName().contains("HP")) {
                    Optional.ofNullable(cachedRoleAttr).ifPresent( attr -> player.setHp(Long.valueOf(attr.getValue())));
                } else if (cachedRoleAttr.getName().contains("MP")) {
                    Optional.ofNullable(cachedRoleAttr).ifPresent( attr -> player.setMp(Long.valueOf(attr.getValue())));
                }
                continue;
            }
            RoleAttribute attribute = new RoleAttribute();
            BeanUtils.copyProperties(cachedRoleAttr, attribute);
            attributeMap.put(id, attribute);
        }



    }


}
