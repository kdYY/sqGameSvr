/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.model.UserExample;
import org.sq.gameDemo.svr.game.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

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


    @OrderMapping(OrderEnum.Register)
    public MsgEntity register(MsgEntity msgEntity) throws Exception {
        byte[] data = msgEntity.getData();
        UserProto.User user = UserProto.User.parseFrom(data);

        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(user.getMsgId());
        builder.setTime(user.getTime());

        if(userService.userNameExist(user.getName())) {
            builder.setContent("name exist, try again");
        } else {
            int userId = userService.addUser(user);
            if(userId <= 0) {
                builder.setContent("fail, try again");
            } else {
                builder.setContent("success");
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
            builder.setContent("login success");
            builder.setToken(tokenEncryp(loginUser.getId()));
        }

        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }



    private MsgEntity errOrder(MsgEntity msgEntity) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent("無此指令");
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    @OrderMapping(OrderEnum.ErrOrder)
    public MsgEntity errOrder() {
        return errOrder(new MsgEntity());
    }

    private String tokenEncryp(int userId) {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(userId);
    }
}
