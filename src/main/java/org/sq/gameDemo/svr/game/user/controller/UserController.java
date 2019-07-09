/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.model.UserExample;
import org.sq.gameDemo.svr.game.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b><code>UserController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/3 22:34.
 *
 * @author
 * @since SpringBootDemo ${PROJECT_VERSION}
 */
@Controller
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private EntityService entityService;

    @OrderMapping(OrderEnum.Register)
    public MsgEntity register(MsgEntity msgEntity) throws Exception {
        byte[] data = msgEntity.getData();
        UserProto.User user = UserProto.User.parseFrom(data);

        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(user.getMsgId());
        builder.setTime(user.getTime());
        User userSave = new User();
        userSave.setName(user.getName());
        userSave.setPassword(user.getPassword());
        if(userService.userNameExist(user.getName())) {
            builder.setContent("name exist, try again");
        } else {
            int isSuccess = userService.addUser(userSave);
            if(isSuccess <= 0) {
                builder.setContent("fail, try again");
            } else {
                builder.setContent("success");
                UserCache.addUserMap(userSave.getId(), userService.getUser(userSave.getId()));
            }
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    @OrderMapping(OrderEnum.Login)
    public MsgEntity login(MsgEntity msgEntity) throws Exception {
        byte[] data = msgEntity.getData();
        UserProto.User user = UserProto.User.parseFrom(data);

        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(user.getMsgId());
        builder.setTime(user.getTime());

        User loginUser = userService.getUser(user);
        if(loginUser == null) {
            builder.setContent("no this user, please register");
        } else {
            builder.setContent("login success, \r\nhi! welcome to 起源之地!! \r\nnow construct your role~\r\n" + entityService.getEntitieListString());

            String token = tokenEncryp(loginUser.getId());
            builder.setToken(token);
            UserCache.updateUserToken(user.getId(), token);
            UserCache.updateChannelCache(msgEntity.getChannel(), user.getId());
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }



    // 重连检测
    @OrderMapping(OrderEnum.CheckToken)
    public MsgEntity checkToken(MsgEntity msgEntity) throws Exception {
        byte[] data = msgEntity.getData();
        UserProto.User user = UserProto.User.parseFrom(data);
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(user.getMsgId());
        builder.setTime(user.getTime());
        String userToken = user.getToken();
        if(userToken != null && userToken.length() != 0 && UserCache.tokenUserMap.get(userToken) != null) {
            UserCache.updateChannelCache(msgEntity.getChannel(), user.getId());
            //TODO 上次离开的地方
            builder.setContent("\r\nlogin success, \r\nhi! welcome back to 起源之地!! \r\n");
        } else {
            builder.setContent("login fail, please relogin, enter \"help\" to get Help");
            builder.setToken("");
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }


    @OrderMapping(OrderEnum.GetRole)
    public MsgEntity getRoles(MsgEntity msgEntity) throws Exception {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setContent(entityService.getEntitieListString());
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    @OrderMapping(OrderEnum.ErrOrder)
    public MsgEntity errOrder() {
        return errOrder(new MsgEntity());
    }

    private MsgEntity errOrder(MsgEntity msgEntity) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent("無此指令");
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }



    private String tokenEncryp(int userId) {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(userId);
    }
}
