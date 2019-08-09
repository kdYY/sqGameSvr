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
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.UserEntityMapper;
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
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private BagService bagService;



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

    /**
     * 获取用户，如果缓存没有，去数据库查
     * @param userToken
     * @return
     */
    public User getUserByToken(String userToken) {
        Integer userIdCache = UserCache.getUserIdByToken(userToken);
        if(Objects.nonNull(userIdCache)) {
            User userCache = UserCache.getUserById(userIdCache);
            if(userCache != null ) {
                return userCache;
            }
        }

        UserExample example = new UserExample();
        example.createCriteria().andTokenEqualTo(userToken);
        List<User> users = userMapper.selectByExample(example);
        if(users != null && users.size() != 0) {
            return users.get(0);
        }
        return null;
    }

    public void userRegister(MessageProto.Msg.Builder builder, UserProto.User proto) throws Exception {
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
            }
        }
    }

    public void checkUserToken(Channel channel, UserProto.ResponseUserInfo.Builder builder, String userToken) {

        if(userToken != null && !userToken.equals("")) {
            User user = getUserByToken(userToken);
            if(user == null) {
                builder.setResult(404);
                builder.setToken("");
            } else {
                try {
                    entityService.playerLogin(channel, builder, user);
                }  catch (CustomException.BindRoleInSenceException e1) {
                    builder.setContent(e1.getMessage());
                    builder.setResult(404);
                    builder.setToken("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            builder.setContent("reconnect fail, please relogin, enter \"help\" to get Help");
            builder.setResult(404);
            builder.setToken("");
        }

    }

    public void userOffLine(Channel channel) {
        Integer userId = UserCache.getUserIdByChannel(channel);
        Player player = senceService.removePlayerAndGet(userId, channel);
        //清除playerCache中的数据
        playerCache.removePlayerCache(channel);
        UserCache.removeChannle(channel, player.getUserId());
        UserEntity userEntity = userEntityMapper.getUserEntityByUserId(player.getUserId());
        userEntity.setSenceId(player.getSenceId());
        userEntityMapper.updateByPrimaryKeySelective(userEntity);
        bagService.updateBagInDB(player);
        log.info(player.getName() + "下线");
    }

    public User getUserById(Integer userId) {
        User user = null;
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(userId);

        List<User> userList = userMapper.selectByExample(userExample);
        if(userList != null && userList.size() >= 1) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    public User loginUser(UserProto.User user){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(user.getName()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0) {
            return users.get(0);
        }
        return null;
    }
}
