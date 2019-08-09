package org.sq.gameDemo.svr.game.bag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.BagPt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Controller
public class BagController {

    @Autowired
    private  BagService bagService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    /**
     * 玩家使用背包中的物品
     * @param msgEntity
     * @param requestInfo
     * @param builder
     */
    @OrderMapping(OrderEnum.USE_ITEM)
    public void useItem(MsgEntity msgEntity,
                        @ReqParseParam BagPt.BagReqInfo requestInfo,
                        @RespBuilderParam BagPt.BagRespInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        if(bagService.useItem(player, requestInfo.getItemId())) {
            senceService.notifyPlayerByDefault(player, "物品使用完毕");
        }
    }


    /**
     * 玩家移除背包中的物品
     * @param msgEntity
     * @param requestInfo
     * @param builder
     */
    @OrderMapping(OrderEnum.REMONVE_ITEM)
    public void removeItem(MsgEntity msgEntity,
                           @ReqParseParam BagPt.BagReqInfo requestInfo,
                           @RespBuilderParam BagPt.BagRespInfo.Builder builder) {

    }



}
