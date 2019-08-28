package org.sq.gameDemo.svr.game.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.transaction.service.OnlineTradeService;

@Controller
public class OnlineTradeController {

    @Autowired
    private OnlineTradeService onlineTradeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private BagService bagService;
    @Autowired
    private SenceService senceService;

    /**
     * 获取邮件内容
     */
    @OrderMapping(OrderEnum.START_ONLINE_TRADE)
    public void startOnlineTrade(MsgEntity msgEntity,
                             @ReqParseParam TradePt.TradeRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
            onlineTradeService.startTrade(player, requestInfo.getAccpeterId(),  requestInfo.getAuctionItemId()
                    , requestInfo.getAutionCount(), requestInfo.getAccpetCount(), requestInfo.getItemInfoId());
//        return msgEntity;
    }


    @OrderMapping(OrderEnum.ACCEPT_ONLINE_TRADE)
    public void accpetTrade(MsgEntity msgEntity,
                        @ReqParseParam TradePt.TradeRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            onlineTradeService.accpetTrade(player, requestInfo.getId());
        } catch (CustomException.SystemSendMailErrException e) {
            e.printStackTrace();
        }
//        return msgEntity;
    }

}
