package org.sq.gameDemo.svr.game.characterEntity.dao;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2018/9/28 14:32
 * @version 1.00
 * Description: mmorpg
 */
@Slf4j
@Component
public class PlayerCache  {

    public static Cache<Channel, Player> channelPlayerCache = CacheBuilder.newBuilder()
            // 设置并发级别
            .concurrencyLevel(10)
            // 设置缓存容器的初始容量为100
            .initialCapacity(100)
            .maximumSize(5000)
            .recordStats()
            .removalListener(
                    cacheObj -> log.info("channelPlayerCache: " + cacheObj.getKey() + "玩家被移除，原因: " + cacheObj.getCause())
            )
            .build();

    public  static Cache<Long, Channel> IdChannelCache = CacheBuilder.newBuilder().build();
    public  static Cache<Integer, Player> unIdPlayerCache = CacheBuilder.newBuilder().build();

    @PostConstruct
    public void init() {
        Player system = new Player();
        system.setId(Constant.SYSTEM_ID);
        system.setUnId(Constant.SYSTEM_UNID);
        system.setName(Constant.SYSTEM_MANAGER);
        unIdPlayerCache.put(Constant.SYSTEM_UNID, system);
    }

    /**
     *  键为channel
     */
    public Player getPlayerByChannel(Channel channel) {
        return channelPlayerCache.getIfPresent(channel);
    }


    /**
     *  值为player
     */
    public void putChannelPlayer(Channel channel, Player player) {
        Channel old = getChannelByPlayerId(player.getId());
        Optional.ofNullable(old).ifPresent(o -> channelPlayerCache.invalidate(o));
        channelPlayerCache.put(channel,player);
    }

    /**
     *  值为player
     */
    public void putUnIdPlayer(Integer unId, Player player) {
        unIdPlayerCache.put(unId,player);
    }


    /**
     *  通过 channel清除玩家信息
     */
    public void removePlayerCache(Channel channel) {
        Optional.ofNullable(getPlayerByChannel(channel)).ifPresent(player -> {
            removePlayerChannel(player.getId());
            removeUnIdPlayerCache(player.getUnId());
            channelPlayerCache.invalidate(channel);
        });
    }

    private void removeUnIdPlayerCache(Integer unId) {
        unIdPlayerCache.invalidate(unId);
    }


    /**
     * 玩家id来保存ChannelHandlerContext
     */
    public void savePlayerChannel(Long playerId, Channel channel) {
        IdChannelCache.put(playerId, channel);
    }


    /**
     *  根据玩家id获取ChannelHandlerContext
     * @param playerId 玩家id
     */
    public Channel getChannelByPlayerId(Long playerId) {
        return IdChannelCache.getIfPresent(playerId);
    }

    public Player getPlayerByUnId(Integer unId) {
        return unIdPlayerCache.getIfPresent(unId);
    }

    public  void removePlayerChannel(Long playerId) {
        IdChannelCache.invalidate(playerId);
    }


    public Map<Channel, Player> getAllPlayerCache() {
        return channelPlayerCache.asMap();
    }

}

