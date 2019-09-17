package org.sq.gameDemo.svr.game.team.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.TaskPt;
import org.sq.gameDemo.common.proto.TeamPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.task.model.TaskProgress;
import org.sq.gameDemo.svr.game.team.model.Team;
import org.sq.gameDemo.svr.game.team.service.TeamService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private EntityService entityService;

    /**
     * 创建队伍
     */
    @OrderMapping(OrderEnum.CREATE_TEAM)
    public MsgEntity createTeam(MsgEntity msgEntity,
                              @RespBuilderParam TeamPt.TeamResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Team team = teamService.createTeam(player);
        getTeamResponsePt(builder, team);
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 邀请入队
     */
    @OrderMapping(OrderEnum.INVITE_TEAM)
    public void inviteTeam(MsgEntity msgEntity,
                           @ReqParseParam TeamPt.TeamRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        teamService.sendTeamRequest(player, requestInfo.getInvitedId());
    }

    /**
     * 退出队伍
     */
    @OrderMapping(OrderEnum.EXIT_TEAM)
    public void exitTeam(MsgEntity msgEntity) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        teamService.exitTeam(player);
    }


    /**
     * 查看队伍
     */
    @OrderMapping(OrderEnum.SHOW_TEAM)
    public MsgEntity showTeam(MsgEntity msgEntity,
                                @RespBuilderParam TeamPt.TeamResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Team team = teamService.getTeam(player);
        getTeamResponsePt(builder, team);
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 组队协议转换
     * @param builder
     * @param team
     */
    private void getTeamResponsePt(@RespBuilderParam TeamPt.TeamResponseInfo.Builder builder, Team team) {
        try {
            builder.addTeam(ProtoBufUtil.transformProtoReturnBuilder(TeamPt.Team.newBuilder(), team));
            builder.setResult(Constant.SUCCESS);
        } catch (Exception e) {
            builder.setResult(Constant.SVR_ERR);
            e.printStackTrace();
        }
    }

    /**
     * 接受邀请入队
     */
    @OrderMapping(OrderEnum.ENTER_TEAM)
    public void enterTeam(MsgEntity msgEntity) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        teamService.enterTeam(player);
    }
}
