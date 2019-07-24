/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
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
@Slf4j
@Controller
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    /**
     * 玩家注册
     * @param msgEntity
     * @return
     */
    @OrderMapping(OrderEnum.Register)
    public MsgEntity register(MsgEntity msgEntity) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();;
        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);
            UserProto.User userProto = requestUserInfo.getUser();

            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());
            userService.userRegister(builder, userProto);

        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }



    /**
     * 玩家登录
     * @param msgEntity
     */
    @OrderMapping(OrderEnum.Login)
    public void login(MsgEntity msgEntity) {
        UserProto.ResponseUserInfo.Builder builder = UserProto.ResponseUserInfo.newBuilder();
        Channel channel = msgEntity.getChannel();
        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);
            UserProto.User user = requestUserInfo.getUser();
            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());
            entityService.playerLogin(channel, builder, user.getId());
        } catch (customException.BindRoleInSenceException e1) {
            builder.setResult(404);
            builder.setContent(e1.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
            channel.writeAndFlush(msgEntity);
        }
    }


    /**
     * / 重连检测
     * @param msgEntity
     * @return
     * @throws Exception
     */
    @OrderMapping(OrderEnum.CheckToken)
    public MsgEntity checkToken(MsgEntity msgEntity) throws Exception {
        UserProto.ResponseUserInfo.Builder builder = UserProto.ResponseUserInfo.newBuilder();

        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);
            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());
            Channel channel = msgEntity.getChannel();
            String userToken = requestUserInfo.getToken();

            userService.checkUserToken(channel, builder, userToken);

            return msgEntity;
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
            return msgEntity;
        }finally {
            msgEntity.setData(builder.build().toByteArray());
        }

    }

    @OrderMapping(OrderEnum.Exit)
    public void loginOut(MsgEntity msgEntity) {
        Channel channel = msgEntity.getChannel();
        UserCache.removeChannle(channel);
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent("期待下次回来~ 再见");
        msgEntity.setData(builder.build().toByteArray());
        channel.writeAndFlush(msgEntity);
        channel.close();
        log.info("玩家退出登陆");
    }



    @OrderMapping(OrderEnum.ErrOrder)
    public MsgEntity errOrder() {
        MsgEntity msgEntity = new MsgEntity();
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent("無此指令");
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }






}
