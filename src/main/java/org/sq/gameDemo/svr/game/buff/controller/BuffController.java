package org.sq.gameDemo.svr.game.buff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.BuffPt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Controller
public class BuffController {

    @Autowired
    private BuffService buffService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SenceService senceService;

    @OrderMapping(OrderEnum.USE_BUFF)
    public void useBuff(MsgEntity msgEntity,
                        @ReqParseParam BuffPt.BuffReqInfo requestInfo) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        Buff buff = buffService.getBuff(requestInfo.getBuffId());
        requestInfo.getTargetId();
        if(buff == null) {
            senceService.notifyPlayerByDefault(player, "没有此buff");
            return;
        }
        senceService.notifyPlayerByDefault(player, "开始使用" + buff.getName());
        buffService.buffAffecting(player, buff);
    }
}
