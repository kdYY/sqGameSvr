package org.sq.gameDemo.svr.eventManage.handler;

import io.netty.channel.Channel;
import org.checkerframework.checker.nullness.Opt;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */

@Component
public class LevelEventHandler {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SenceService senceService;

    {
        EventBus.subscribe(LevelEvent.class, this::levelUp);
    }


    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            byte[] bytes = MessageProto.Msg.newBuilder()
                    .setContent("恭喜你升了一级，目前等级是" + levelEvent.getLevel())
                    .build()
                    .toByteArray();
            playerCache.getChannelByPlayerId(player.getId()).writeAndFlush(ProtoBufUtil.getBroadCastEntity(bytes));
        });
    }



}
