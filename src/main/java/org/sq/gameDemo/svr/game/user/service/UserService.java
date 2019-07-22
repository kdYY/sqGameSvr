/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.UserProto;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.dao.UserMapper;
import org.sq.gameDemo.svr.game.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.user.model.UserExample;

import java.util.List;
import java.util.Objects;

/**
 * <b><code>UserService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/3 22:31.
 * @since SpringBootDemo ${PROJECT_VERSION}
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;


    public User getUser(UserProto.User user){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(user.getName()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0) {
            return users.get(0);
        }
        return null;
    }

    public List<User> listUser(){
        return userMapper.selectByExample(new UserExample());
    }

    @Transactional
    public int addUser(User user) throws Exception{
        return userMapper.insertSelective(user);
    }

    public User getUserName(String name) {
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size() != 0) {
            return userList.get(0);
        }
        return null;
    }

    public boolean userNameExist(String name) {
        if(getUserName(name) != null) {
            return true;
        }
        return false;
    }


    public void updateTokenByUserId(Integer userId, String token) {
        userMapper.updateTokenByUserId(userId, token);
    }

    public User getUserByToken(String userToken) {
        UserExample example = new UserExample();
        example.createCriteria().andTokenEqualTo(userToken);
        List<User> users = userMapper.selectByExample(example);
        if(users != null && users.size() != 0) {
            return users.get(0);
        }
        return null;
    }

    public void playerRegister(MessageProto.Msg.Builder builder, UserProto.User proto) throws Exception {
        User userSave = new User();
        userSave.setName(proto.getName());
        userSave.setPassword(proto.getPassword());

        if(userNameExist(proto.getName())) {
            builder.setContent("name exist, try again");
        } else {
            int isSuccess = addUser(userSave);
            if(isSuccess <= 0) {
                builder.setContent("fail, try again");
            } else {
                builder.setContent("success");
                UserCache.addUserMap(userSave.getId(), userSave);
            }
        }
    }

    public void checkUserToken(Channel channel, UserProto.ResponseUserInfo.Builder builder, String userToken) {
        User user = getUserByToken(userToken);
        if(userToken != null && !userToken.equals("") && user != null) {

            UserCache.updateChannelCache(channel, user.getId());

            if(entityService.hasPlayer(channel)) {
                Player player = entityService.getInitedPlayer(user.getId(), channel);
                entityService.playerOnline(player, channel);
                String lastSence = senceService.getSenceBySenceId(player.getSenceId()).getName();
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
    }

    public void userOffLine(Channel channel) {
        Integer userId = UserCache.getUserIdByChannel(channel);
        UserEntity userEntity = senceService.removePlayerAndGet(userId, channel);
        UserCache.removeChannle(channel, userId);
        log.info(userEntity.getName() + "下线");
    }

}
