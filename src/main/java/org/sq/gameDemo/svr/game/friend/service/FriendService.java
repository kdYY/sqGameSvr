package org.sq.gameDemo.svr.game.friend.service;

import com.alibaba.fastjson.TypeReference;
import org.apache.poi.ss.formula.functions.PPMT;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.FirstAddFriendEvent;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.friend.model.Friend;
import org.sq.gameDemo.svr.game.guild.model.Member;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.sq.gameDemo.common.OrderEnum.SHOW_FRIEND;

@Service
public class FriendService {

    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    /**
     * 添加好友
     */
    public void addFriend(Player player, Integer unId) {
        Player find = entityService.getPlayer(unId);
        if(find == null) {
            UserEntity userEntity = entityService.findUserEntity(unId);
            if(userEntity == null) {
                senceService.notifyPlayerByDefault(player, "玩家不存在");
                return;
            } else {
                BeanUtils.copyProperties(userEntity, find);
            }
        }
        Friend friend = new Friend(unId, find.getName(), entityService.getType(find.getTypeId()).getTypeName());
        player.getFriendMap().put(unId, friend);
        //player.setFriend(JsonUtil.serializableJson(player.getFriendMap()));

        senceService.notifyPlayerByDefault(player, "添加好友成功, 使用" + SHOW_FRIEND.getOrder() + ", 查看好友列表");
        EventBus.publish(new FirstAddFriendEvent(player));
    }

    /**
     * 加载玩家朋友列表
     */
    public void loadFriend(Player player) {
        JsonUtil.setMap(player.getFriendMap(), player.getFriend(), new TypeReference<Map<Integer, Friend>>(){});
    }


    /**
     * 查看好友列表
     */
    public List<Friend> getFriendList(Player player) {
        return player.getFriendMap().values().stream().collect(Collectors.toList());
    }



}
