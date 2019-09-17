package org.sq.gameDemo.svr.common;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.user.dao.UserMapper;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.model.UserExample;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用户缓存
 */
@Component
public class UserCache {

    @Autowired
    private UserMapper userMapper;
    //<Token, UserId>
    public static Map<String, Integer> tokenUserMap = new ConcurrentHashMap<>();
    //<Channel, UserId>
    public static Map<Channel,Integer> channelUserIdMap = new ConcurrentHashMap<>();
    //<UserId, User>
    public static Map<Integer, User> userMap = new ConcurrentHashMap<>();

    public static void removeUserIdChannel(Channel channel) {
        channelUserIdMap.remove(channel);
    }

    @PostConstruct
    public void init() {
        List<User> userList = userMapper.selectByExample(new UserExample());
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
    }


    private static void updateUserToken(int userId, String token) {
        updateKey(tokenUserMap, token, userId);

    }

    private static void updateChannelCache(Channel newChannel, int userId) {
        updateKey(channelUserIdMap, newChannel, userId);
    }

    public static Integer getUserIdByChannel(Channel channel) {
        return channelUserIdMap.get(channel);
    }

    public static Integer getUserIdByToken(String token) {
        return tokenUserMap.get(token);
    }

    public static Channel getUserChannelByUserId(Integer userId) {
        return (Channel) getKeyByValue(userId, channelUserIdMap).orElse(null);
    }

    private static void updateKey(Map map, Object newKey, Object value) {
        Optional overTimeKey = getKeyByValue(value, map);
        if(!overTimeKey.equals(Optional.empty()) && !value.equals(overTimeKey.get())) {
            map.remove(overTimeKey);
        }
        map.put(newKey,value);

    }

    private static Optional getKeyByValue(Object value, Map map) {
        if(map == null || map.size() == 0) {
            return Optional.empty();
        }
        return map.keySet().stream().filter(key -> {
            Object o = map.get(key);
            if(Objects.isNull(o)) {
                return false;
            }
            return map.get(key).equals(value);
        }).findFirst();

    }

    /**
     * 将用户注册到用户缓存中
     * @param userId
     * @param user
     */
    private static void registerUserInCache(int userId, User user) {
        userMap.put(userId, user);
    }

    public static void updateUserCache(int userId, User user, Channel channel) {
        updateUserToken(userId, user.getToken());
        updateChannelCache(channel, userId);
        if(userMap.get(userId) == null) {
            registerUserInCache(userId, user);
        }
    }


    public static User getUserById(int userId) {
        return userMap.get(userId);
    }
}
