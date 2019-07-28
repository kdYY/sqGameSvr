package org.sq.gameDemo.svr.common;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.user.dao.UserMapper;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.model.UserExample;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class UserCache {

    @Autowired
    private UserMapper userMapper;
    //<Token, UserId>
    public static Map<String, Integer> tokenUserMap = new HashMap<>();
    //<Channel, UserId>
    public static Map<Channel,Integer> channelUserIdMap = new ConcurrentHashMap<>();
    //<UserId, User>
    public static Map<Integer, User> userMap = new ConcurrentHashMap<>();
    //<SenceId, Channel>
    public static Map<Integer, List<Channel>> senceChannelGroupMap = new ConcurrentHashMap<>();

    public static void removeChannle(Channel channel, Integer userId) {
        channelUserIdMap.remove(channel, userId);
    }

    public static void removeChannle(Channel channel) {
        channelUserIdMap.remove(channel);
    }


    @PostConstruct
    public void init() {
        List<User> userList = userMapper.selectByExample(new UserExample());
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
    }

    /**
     *
     * @param senceId
     * @param channel
     * @param msgByteArray 要广播话语
     */
    public static void addChannelInGroup(Integer senceId, Channel channel, byte[] msgByteArray) {
        List<Channel> channelList = senceChannelGroupMap.get(senceId);
        if(channelList == null) {
            channelList = new CopyOnWriteArrayList<>();
            senceChannelGroupMap.put(senceId, channelList);
        }
        channelList.add(channel);
        //同时广播
        broadCastPlayerGroup(channelList, msgByteArray);
    }

    public static void moveChannelInGroup(Integer senceId, Channel channel, String msg) {
        List<Channel> channelList = senceChannelGroupMap.get(senceId);
        if(channelList == null) {
            throw new customException.RemoveFailedException("场景id不存在");
        }
        channelList.remove(channel);
        //同时广播
        broadcastChannelGroupBysenceId(senceId, msg);
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
            return map.get(key).equals(value);
        }).findFirst();

    }

    public static void addUserMap(int userId, User user) {
        userMap.put(userId, user);
    }

    public static User getUserById(int userId) {
        return userMap.get(userId);
    }

    public static void broadcastChannelGroupBysenceId(Integer senceId, String msg) {
        List<Channel> channelGroup = senceChannelGroupMap.get(senceId);
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setContent(msg);

        broadCastPlayerGroup(channelGroup,builder.build().toByteArray());
    }



    public static void broadCastPlayerGroup(List<Channel> channelGroup, byte[] protoByte) {
        MsgEntity msgEntity = ProtoBufUtil.getBroadCastEntity(protoByte);
        channelGroup.forEach(channel -> {
            msgEntity.setChannel(channel);
            channel.writeAndFlush(msgEntity);
        });
    }

    public boolean isUserOnline(Channel channel) {
        return UserCache.getUserIdByChannel(channel) > 0;
    }

}
