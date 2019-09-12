package org.sq.gameDemo.svr.game.team.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.team.model.Team;

import java.util.concurrent.TimeUnit;

@Component
public class TeamCache {

    // 缓存不过期
    private static Cache<Long, Team> teamCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "队伍被移除, 原因是" + notification.getCause())
            ).build();

    /**
     *   组队请求的缓存,key是被邀请玩家的id,value是发起邀请的玩家id
     */
    private static Cache<Long,Long> teamRequestCache = CacheBuilder.newBuilder()
            // 设置三十秒后请求失效
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "组队请求被移除, 原因是" + notification.getCause())
            ).build();

    public Team getTeam(Long id) {
        return teamCache.getIfPresent(id);
    }

    public void putTeam(Long teamId, Team team) {
        teamCache.put(teamId,team);
    }


    public Long getTeamRequest(Long inviteeId) {
        return teamRequestCache.getIfPresent(inviteeId);
    }


    public void putTeamRequest(Long inviteeId, Long inviter){
        teamRequestCache.put(inviteeId,inviter);

    }

}
