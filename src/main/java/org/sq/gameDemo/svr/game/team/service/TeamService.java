package org.sq.gameDemo.svr.game.team.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.team.dao.TeamCache;
import org.sq.gameDemo.svr.game.team.model.Team;

@Service
public class TeamService {

    @Autowired
    private SenceService senceService;
    @Autowired
    private TeamCache teamCache;
    @Autowired
    private EntityService entityService;

    /**
     * 通知队伍成员
     */
    public void notifyTeam(Team team, String word) {
        team.getPlayerInTeam().entrySet().stream()
                .filter(entry -> entityService.getPlayer(entry.getKey()) != null)
                .map(entry -> entityService.getPlayer(entry.getKey()))
                .forEach(player -> senceService.notifyPlayerByDefault(player, word));
    }

    /**
     * 创建队伍
     */
    public Team createTeam(Player player) {
        Team team = new Team();
        team.setCaptainId(player.getId());
        team.setId(ConcurrentSnowFlake.getInstance().nextID());
        team.setLimitedSize(5);
        team.setName(player.getName() + "的队伍");
        team.getPlayerInTeam().put(player.getId(), player.getName());
        teamCache.putTeam(team.getId(), team);
        player.setTeamId(team.getId());
        senceService.notifyPlayerByDefault(player, "队伍创建成功");
        return team;
    }

    /**
     * 发起邀请
     */
    public void sendTeamRequest(Player player, Long invitedId) {
        if(player.getTeamId() == null) {
            senceService.notifyPlayerByDefault(player, "你没有加入任何队伍");
            return;
        }

        Player invitedPlayer = entityService.getPlayer(invitedId);
        if(invitedPlayer == null) {
            senceService.notifyPlayerByDefault(player, "玩家不在线");
            return;
        }

        teamCache.putTeamRequest(invitedId, player.getId());
        senceService.notifyPlayerByDefault(player, "邀请成功");
        senceService.notifyPlayerByDefault(invitedPlayer, player.getName() + "邀请你入队, 使用enterTeam加入队伍");
    }

    /**
     * 接受邀请入队
     */
    public void enterTeam(Player player) {
        if(player.getTeamId() != null) {
            senceService.notifyPlayerByDefault(player, "你已经加入队伍, 退出队伍加入其它队伍使用 exitTeam 指令");
            return;
        }
        Long teamRequest = teamCache.getTeamRequest(player.getId());
        if(teamRequest == null) {
            senceService.notifyPlayerByDefault(player, "队伍邀请不存在或者已过期");
            return;
        }
        Player captain = entityService.getPlayer(teamRequest);
        Team team = teamCache.getTeam(captain.getTeamId());
        if(team == null) {
            senceService.notifyPlayerByDefault(player, "队伍不存在");
            return;
        }
        team.getPlayerInTeam().put(player.getId(), player.getName());
        player.setTeamId(team.getId());
        senceService.notifyPlayerByDefault(player,"已加入" + team.getName() + "队伍");
    }


    /**
     * 退出队伍
     */
    public boolean exitTeam(Player player) {
        Long teamId = player.getTeamId();
        if(teamId == null) {
            senceService.notifyPlayerByDefault(player, "你没有加入任何队伍");
            return false;
        }
        Team team = teamCache.getTeam(teamId);
        if(team == null) {
            senceService.notifyPlayerByDefault(player, "退出成功");
            player.setTeamId(null);
            return false;
        }
        team.getPlayerInTeam().remove(player.getId());
        senceService.notifyPlayerByDefault(player, "退出成功");
        player.setTeamId(null);
        if(player.getId().equals(team.getCaptainId())) {
            teamCache.removeTeam(teamId);
            senceService.notifyPlayerByDefault(player, "队伍已解散");
            notifyTeam(team, "你的队伍已解散");
        }
        return true;
    }

    /**
     * 获取队伍
     */
    public Team getTeam(Player player) {
        Long teamId = player.getTeamId();
        if(teamId == null) {
            senceService.notifyPlayerByDefault(player, "你没有加入任何队伍");
            return null;
        }
        Team team = teamCache.getTeam(teamId);
        if(team == null) {
            senceService.notifyPlayerByDefault(player, "队伍不存在");
            return null;
        }
        return team;
    }

    /**
     * 下线离开队伍
     * @param player
     */
    public void clearTeam(Player player) {
        exitTeam(player);
    }
}
