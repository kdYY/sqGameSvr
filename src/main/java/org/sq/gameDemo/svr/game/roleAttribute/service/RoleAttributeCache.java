package org.sq.gameDemo.svr.game.roleAttribute.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class RoleAttributeCache {
    @Value("${excel.roleAttribute}")
    private String fileName;

    public static Cache<Integer,RoleAttribute> roleAttributeCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.debug(cacheObj.getKey() + "角色属性被移除, 原因是" + cacheObj.getCause())
            ).build();



    @PostConstruct
    private void init() throws Exception {

        List<RoleAttribute> roleAttributeList = PoiUtil.readExcel(fileName, 0, RoleAttribute.class);
        for (RoleAttribute roleAttr : roleAttributeList) {
            put(roleAttr.getId(), roleAttr);
        }
        log.info("角色基础属性配置表加载完毕");
    }


    public RoleAttribute get(Integer id) {
        return roleAttributeCache.getIfPresent(id);
    }

    public void put(Integer id, RoleAttribute value) {
        roleAttributeCache.put(id,value);
    }
}
