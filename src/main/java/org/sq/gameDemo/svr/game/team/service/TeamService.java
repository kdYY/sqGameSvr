package org.sq.gameDemo.svr.game.team.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.team.dao.TeamCache;
import org.sq.gameDemo.svr.game.team.model.Team;

@Service
public class TeamService {

    @Autowired
    private SenceService senceService;
    @Autowired
    private TeamCache teamCache;

    /**
     * 通知队伍成员
     * @param team
     * @param word
     */
    public void notifyTeam(Team team, String word) {
        for (Player player : team.getPlayerInTeam().values()) {
            senceService.notifyPlayerByDefault(player, word);
        }
    }

    /**
     * 创建队伍
     */
    public Team createTeam(Player player) {
        Team team = new Team();
        team.setCaptainId(player.getId());
        team.setId(ConcurrentSnowFlake.getInstance().nextID());
        team.setLimitedSize(5);
        team.getPlayerInTeam().put(player.getId(), player);
        teamCache.putTeam(team.getId(), team);
        return team;
    }


    /**
     * 入队
     */


    /**
     * 发起邀请
     */


    /**
     * 接受邀请
     */


    /**
     * 解散队伍
     */


}
