package org.sq.gameDemo.svr.game.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;
import org.sq.gameDemo.svr.game.transaction.service.DealTradeService;

import java.util.List;

@Controller
public class DealTradeController {

    @Autowired
    private DealTradeService dealTradeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private BagService bagService;
    @Autowired
    private SenceService senceService;

    /**
     * 发起在线交易
     */
    @OrderMapping(OrderEnum.START_DEAL_TRADE)
    public void startDealTrade(MsgEntity msgEntity,
                             @ReqParseParam TradePt.TradeRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
            dealTradeService.startTrace(player,  requestInfo.getAuctionItemId()
                    , requestInfo.getAutionCount(), requestInfo.getPrice(), requestInfo.getTradeModel());
    }

    /**
     * 接收在线交易
     * @param msgEntity
     * @param requestInfo
     */
    @OrderMapping(OrderEnum.ACCEPT_DEAL_TRADE)
    public void accpetTrade(MsgEntity msgEntity,
                        @ReqParseParam TradePt.TradeRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            dealTradeService.accpetTrace(player, requestInfo.getId(), requestInfo.getPrice());
        } catch (CustomException.SystemSendMailErrException e) {
            e.printStackTrace();
        }
//        return msgEntity;
    }

    /**
     * 查看交易栏上的物品
     * @param msgEntity
     * @param builder
     */
    @OrderMapping(OrderEnum.GET_DEAL)
    public MsgEntity getDealTrade(MsgEntity msgEntity,
                               @RespBuilderParam TradePt.TradeResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            List<DealTrade> trades = dealTradeService.getTrace(player);
            for (DealTrade trade : trades) {
                builder.addTrade((TradePt.Trade) ProtoBufUtil.transformProtoReturnBean(TradePt.Trade.newBuilder(), trade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgEntity;
    }

    /**
     * 查看交易栏上可购买的物品
     * @param msgEntity
     * @param builder
     */
    @OrderMapping(OrderEnum.GET_DEAL_CAN_BUY)
    public MsgEntity getDealTradeCanBuy(MsgEntity msgEntity,
                                  @RespBuilderParam TradePt.TradeResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            List<DealTrade> trades = dealTradeService.getTraceCanBuy(player);
            for (DealTrade trade : trades) {
                builder.addTrade((TradePt.Trade) ProtoBufUtil.transformProtoReturnBean(TradePt.Trade.newBuilder(), trade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgEntity;
    }
}
