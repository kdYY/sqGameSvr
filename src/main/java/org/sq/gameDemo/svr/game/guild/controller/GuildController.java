package org.sq.gameDemo.svr.game.guild.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.GuildPt;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.guild.model.AttendGuildReq;
import org.sq.gameDemo.svr.game.guild.model.Guild;
import org.sq.gameDemo.svr.game.guild.service.GuildService;
import org.sq.gameDemo.svr.game.transaction.model.OnlineTrade;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
public class GuildController {


    @Autowired
    private GuildService guildService;
    @Autowired
    private EntityService entityService;

    /**
     * 创建公会
     */
    @OrderMapping(OrderEnum.CREATE_GUILD)
    public MsgEntity createGuild(MsgEntity msgEntity,
                                    @ReqParseParam GuildPt.GuildRequestInfo requestInfo,
                                    @RespBuilderParam GuildPt.GuildResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Guild guild = guildService.createGuild(player, requestInfo.getName());
        transformGuildPt(builder, guild);
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    //将guild为pt文件
    private void transformGuildPt(@RespBuilderParam GuildPt.GuildResponseInfo.Builder builder, Guild guild) {
        try {
            builder.addGuild(ProtoBufUtil.transformProtoReturnBuilder(GuildPt.Guild.newBuilder(), guild));

        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
        }
    }


    /**
     * 查询可加入的公会
     */
    @OrderMapping(OrderEnum.SHOW_GUILD_CAN_ATTEND)
    public MsgEntity showGuildCanAttend(MsgEntity msgEntity,
                                 @RespBuilderParam GuildPt.GuildResponseInfo.Builder builder) {
        for (Guild guild : guildService.findGuild()) {
            transformGuildPt(builder, guild);
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }


    /**
     * 申请加入公会
     */
    @OrderMapping(OrderEnum.APPLY_ATTEND_GUILD)
    public void applyAttendGuild(MsgEntity msgEntity,
                                 @ReqParseParam GuildPt.GuildRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        guildService.attendGuild(player, requestInfo.getGuildId());
    }


    /**
     *  showGuildReq 会长查看入会申请
     */
    @OrderMapping(OrderEnum.SHOW_GUILD_REQUEST)
    public MsgEntity showGuildRequest(MsgEntity msgEntity,
                                 @ReqParseParam GuildPt.GuildRequestInfo requestInfo,
                                 @RespBuilderParam GuildPt.GuildResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<AttendGuildReq> attendGuildReqs = guildService.showGuildReq(player, requestInfo.getGuildId());
        for (AttendGuildReq req : attendGuildReqs) {
            try {
                builder.addAttendReq(ProtoBufUtil.transformProtoReturnBuilder(GuildPt.AttendGuildReq.newBuilder(), req));
            } catch (Exception e) {
                e.printStackTrace();
                builder.setResult(Constant.SVR_ERR);
            }
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }


    /**
     * 同意申请
     */
    @OrderMapping(OrderEnum.AGREE_ATTEND_REQUEST)
    public void agreeAttendRequest(MsgEntity msgEntity,
                                 @ReqParseParam GuildPt.GuildRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        guildService.agreeEnterGuild(player, requestInfo.getGuildId(), requestInfo.getUnId());
    }

    /**
     * 退出公会
     */
    @OrderMapping(OrderEnum.EXIT_GUILD)
    public void exitGuild(MsgEntity msgEntity,
                                   @ReqParseParam GuildPt.GuildRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        guildService.exitGuild(player, requestInfo.getGuildId());
    }

    /**
     * 捐献物品
     */

    @OrderMapping(OrderEnum.DONATE_ITEM)
    public void donateItem(MsgEntity msgEntity,
                          @ReqParseParam GuildPt.GuildRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        guildService.donateItem(player, requestInfo.getGuildId(), requestInfo.getItemId(), requestInfo.getCount());
    }

    /**
     * 使用showGuildList查看已加入的公会列表
     */
    @OrderMapping(OrderEnum.SHOW_GUILDLIST)
    public MsgEntity showGuildRequest(MsgEntity msgEntity,
                                      @RespBuilderParam GuildPt.GuildResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<Guild> guildList = guildService.showGuildList(player);
        for (Guild guild : guildList) {
            transformGuildPt(builder, guild);
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 获取公会物品
     */
    @OrderMapping(OrderEnum.GET_GUILD_ITEM)
    public void getGuildItem(MsgEntity msgEntity,
                          @ReqParseParam GuildPt.GuildRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        guildService.getGuildItem(player, requestInfo.getGuildId(), requestInfo.getItemInfoId(), requestInfo.getCount());
    }


    /**
     * 使用showChairManGuild查看有会长权限的公会
     */
    @OrderMapping(OrderEnum.SHOW_CHAIRMAN_GUILD)
    public MsgEntity showChairManGuild(MsgEntity msgEntity,
                                      @RespBuilderParam GuildPt.GuildResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<Guild> guildList = guildService.showChairManGuildList(player);
        for (Guild guild : guildList) {
            transformGuildPt(builder, guild);
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }


}
