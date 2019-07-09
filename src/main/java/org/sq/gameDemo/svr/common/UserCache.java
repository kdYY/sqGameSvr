package org.sq.gameDemo.svr.common;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.user.model.User;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserCache {

    //<Token, UserId>
    public static Map<String, Integer> tokenUserMap = new HashMap<>();
    //<Channel, UserId>
    public static Map<Channel,Integer> channelUserIdMap = new ConcurrentHashMap<>();
    //<UserId, User>
    public static Map<Integer, User> userMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

    }


    public static void updateUserToken(int userId, String token) {
        updateKey(tokenUserMap, token, userId);
    }

    public static void updateChannelCache(Channel newChannel, int userId) {
        updateKey(channelUserIdMap, newChannel, userId);
    }

    public static Integer getUserIdByChannel(Channel channel) {
        return channelUserIdMap.get(channel);
    }

    public static Integer getUserIdByToken(String token) {
        return tokenUserMap.get(token);
    }

    private static void updateKey(Map map, Object newKey, Object value) {
        Object overTimeKey = getKeyByValue(value, map);
        if(overTimeKey != null && !overTimeKey.equals(value)) {
            map.remove(overTimeKey);
            map.put(newKey, value);
        }
    }

    private static Object getKeyByValue(Object value, Map map) {
        for (Object key : map.keySet()) {
            if(map.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    public static void addUserMap(int userId, User user) {
        userMap.put(userId, user);
    }

    public static User getUserById(int userId) {
        return userMap.get(userId);
    }

}
