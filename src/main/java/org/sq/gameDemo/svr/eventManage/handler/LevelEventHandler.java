package org.sq.gameDemo.svr.eventManage.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */
@Slf4j
@Component
public class LevelEventHandler {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SenceService senceService;

    {
        EventBus.registe(LevelEvent.class, this::levelUp);
        log.info("角色升级事件注册成功");
    }


    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            player.setLevel(levelEvent.getNewlevel());

            playerCache.getChannelByPlayerId(player.getId()).writeAndFlush(
                    ProtoBufUtil.getBroadCastDefaultEntity("恭喜你升了一级，目前等级是" + player.getLevel()));
        });
    }



}
