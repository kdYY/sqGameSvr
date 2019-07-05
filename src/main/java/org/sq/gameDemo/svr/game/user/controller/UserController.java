/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.controller;

import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto2;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

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


    @OrderMapping(OrderEnum.Site)
    public MsgEntity listUser() {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setCmdCode(OrderEnum.Site.getOrderCode());
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent(userService.listUser().stream().map(User::toString).collect(Collectors.joining()));
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    @OrderMapping(OrderEnum.ErrOrder)
    public MessageProto2.Msg errOrder() {
        MessageProto2.Msg.Builder builder = MessageProto2.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent("無此指令");
        return builder.build();
    }

}
