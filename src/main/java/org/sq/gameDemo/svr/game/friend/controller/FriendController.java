package org.sq.gameDemo.svr.game.friend.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.FriendPt;
import org.sq.gameDemo.common.proto.TeamPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.friend.model.Friend;
import org.sq.gameDemo.svr.game.friend.service.FriendService;
import org.sq.gameDemo.svr.game.team.model.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private EntityService entityService;

    /**
     * 添加好友
     */
    @OrderMapping(OrderEnum.ADD_FRIEND)
    public void addFriend(MsgEntity msgEntity,
                               @ReqParseParam FriendPt.FriendRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        friendService.addFriend(player, requestInfo.getUnId());
    }

    /**
     * 展示好友列表
     */
    @OrderMapping(OrderEnum.SHOW_FRIEND)
    public MsgEntity showfriend(MsgEntity msgEntity,
                           @RespBuilderParam FriendPt.FriendResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<Friend> friendList = friendService.getFriendList(player);
        try {
            getFriendResponsePt(builder, friendList);
            builder.setResult(Constant.SUCCESS);
        } catch (Exception e) {
            builder.setResult(Constant.SVR_ERR);
            e.printStackTrace();
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    private void getFriendResponsePt(FriendPt.FriendResponseInfo.Builder builder, List<Friend> friendList) throws Exception {
        for (Friend friend : friendList) {
            builder.addFriend(ProtoBufUtil.transformProtoReturnBuilder(FriendPt.Friend.newBuilder(), friend));
        }
    }
}
