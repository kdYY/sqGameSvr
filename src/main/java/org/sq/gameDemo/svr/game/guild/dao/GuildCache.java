package org.sq.gameDemo.svr.game.guild.dao;

import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.guild.model.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class GuildCache {

    @Autowired
    private GuildMapper guildMapper;

    private static Cache<Integer, Guild> guildCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "公会被移除, 原因是" + notification.getCause())
            ).build();


    /**
     * 加载公会
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        List<Guild> guildList = guildMapper.selectByExample(new GuildExample());
        for (Guild guild : guildList) {
            JsonUtil.setMap(guild.getMemberMap(), guild.getMemberStr(), new TypeReference<Map<Integer, Member>>(){});
            JsonUtil.setMap(guild.getDonateMap(), guild.getDonateStr(), new TypeReference<Map<Integer, Donate>>(){});
            JsonUtil.setMap(guild.getPlayerJoinRequestMap(), guild.getJoinRequestStr(), new TypeReference<Map<Integer, AttendGuildReq>>(){});
            JsonUtil.setMap(guild.getWarehouseMap(), guild.getWarehouseStr(), new TypeReference<Map<Integer, Item>>(){});
            put(guild);
        }
    }

    public Guild get(Integer id) {
        return  guildCache.getIfPresent(id);
    }

    public void put(Guild guild) {

        guildCache.put(guild.getId(), guild);
    }

    public Map<Integer, Guild> asMap() {
        return guildCache.asMap();
    }

    public void remove(Guild guild) {
        guildCache.invalidate(guild.getId());
    }
    

}
