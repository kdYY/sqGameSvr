package org.sq.gameDemo.svr.common;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class UserCache {

    @Autowired
    private UserService userService;
    //<Token, UserId>
    public static Map<String, Integer> tokenUserMap = new HashMap<>();
    //<Channel, UserId>
    public static Map<Channel,Integer> channelUserIdMap = new ConcurrentHashMap<>();
    //<UserId, User>
    public static Map<Integer, User> userMap = new ConcurrentHashMap<>();
    //<SenceId, Channel>
    public static Map<Integer, List<Channel>> senceChannelGroupMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        List<User> userList = userService.listUser();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
    }

    /**
     *
     * @param senceId
     * @param channel
     * @param msg 要广播话语
     */
    public static void addChannelInGroup(Integer senceId, Channel channel, String msg) {
        List<Channel> channelList = senceChannelGroupMap.get(senceId);
        if(channelList == null) {
            channelList = new CopyOnWriteArrayList<>();
        }
        channelList.add(channel);
        //同时广播
        broadcastChannelGroupBysenceId(channelList, msg);
    }

    public static void moveChannelInGroup(Integer senceId, Channel channel, String msg) {
        List<Channel> channelList = senceChannelGroupMap.get(senceId);
        if(channelList == null) {
            throw new customException.RemoveFailedException("场景id不存在");
        }
        channelList.remove(channel);
        //同时广播
        broadcastChannelGroupBysenceId(channelList, msg);
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
        }
        map.put(newKey,value);

    }

    private static Object getKeyByValue(Object value, Map map) {
        if(map == null || map.size() == 0) {
            return null;
        }
        return  map.keySet().stream().filter(key -> {
            Object o = map.get(key);
            return map.get(key).equals(value);
        }).findFirst().get();
    }

    public static void addUserMap(int userId, User user) {
        userMap.put(userId, user);
    }

    public static User getUserById(int userId) {
        return userMap.get(userId);
    }

    public static void broadcastChannelGroupBysenceId(List<Channel> channelGroup, String msg) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setContent(msg);
        builder.setCmd(OrderEnum.BroadCast.getOrder());
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setData(builder.build().toByteArray());

        channelGroup.forEach(channel -> {
            msgEntity.setChannel(channel);
            channel.writeAndFlush(msgEntity);
        });
    }

}
