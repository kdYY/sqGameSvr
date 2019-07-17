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
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
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
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    @OrderMapping(OrderEnum.Register)
    public MsgEntity register(MsgEntity msgEntity) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();;
        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);

            UserProto.User user = requestUserInfo.getUser();

            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());

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
                    UserCache.addUserMap(userSave.getId(), userSave);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    @OrderMapping(OrderEnum.Login)
    public void login(MsgEntity msgEntity) {
        byte[] data = msgEntity.getData();
        UserProto.RequestUserInfo requestUserInfo = null;
        UserProto.ResponseUserInfo.Builder builder = UserProto.ResponseUserInfo.newBuilder();

        try {
            requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);
            UserProto.User user = requestUserInfo.getUser();
            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());

            User loginUser = userService.getUser(user);
            if(loginUser == null) {
                builder.setContent("no this user, please register");
                builder.setResult(404);//用户缺失
            } else {
                Integer userId = loginUser.getId();
                String token = tokenEncryp(userId);
                builder.setToken(token);
                UserCache.updateUserToken(userId, token);
                //从数据库更新
                userService.updateTokenByUserId(userId, token);

                UserCache.updateChannelCache(msgEntity.getChannel(), userId);
                //如果是老用户，获取上次保存的UserEntity，找到对应场景
                UserEntity userEntity = entityService.getUserEntityByUserId(userId);
                if(userEntity != null) {
                    UserCache.addChannelInGroup(userEntity.getSenceId(), msgEntity.getChannel(), userEntity.getNick() + "已经上线！");
                    String lastSence = senceService.getSenceBySenceId(userEntity.getSenceId()).getName();
                    builder.setContent(lastSence);
                } else {
                    throw new customException.BindRoleInSenceException("login success, 请绑定角色，help获取帮助信息");
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        } catch (customException.BindRoleInSenceException e1) {
            builder.setResult(404);
            builder.setContent(e1.getMessage());
        }
        msgEntity.setData(builder.build().toByteArray());
        msgEntity.getChannel().writeAndFlush(msgEntity);
    }



    // 重连检测
    @OrderMapping(OrderEnum.CheckToken)
    public MsgEntity checkToken(MsgEntity msgEntity) throws Exception {
        UserProto.ResponseUserInfo.Builder builder = UserProto.ResponseUserInfo.newBuilder();
        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestUserInfo = UserProto.RequestUserInfo.parseFrom(data);
            String userToken = requestUserInfo.getToken();


            builder.setMsgId(requestUserInfo.getMsgId());
            builder.setTime(requestUserInfo.getTime());
            //Integer userId = UserCache.tokenUserMap.get(userToken);
            User user = userService.getUserByToken(userToken);
            if(userToken != null && !userToken.equals("") && user != null) {

                UserCache.updateChannelCache(msgEntity.getChannel(), user.getId());
                UserEntity userEntity = entityService.getUserEntityByUserId(user.getId());
                if(userEntity != null) {
                    UserCache.addChannelInGroup(userEntity.getSenceId(), msgEntity.getChannel(), userEntity.getNick() + "已经上线！");
                    String lastSence = senceService.getSenceBySenceId(userEntity.getSenceId()).getName();
                    builder.setContent(lastSence);
                } else {
                    builder.setContent("\r\nlogin success, \r\nbind your Role \r\n");
                }
                builder.setToken(userToken);
                //builder.setSence(SenceProto.Sence.newBuilder().setId(1).setName("起源之地").build());
            } else {
                builder.setContent("reconnect fail, please relogin, enter \"help\" to get Help");
                builder.setResult(404);
                builder.setToken("");
            }
            return msgEntity;
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
            return msgEntity;
        }finally {
            msgEntity.setData(builder.build().toByteArray());

        }

    }


    @OrderMapping(OrderEnum.GetRole)
    public MsgEntity getRoles(MsgEntity msgEntity) throws Exception {
        EntityTypeProto.EntityTypeResponseInfo.Builder builder = EntityTypeProto.EntityTypeResponseInfo.newBuilder();
        try {
            entityService.transformEntityTypeProto(builder);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        }
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
