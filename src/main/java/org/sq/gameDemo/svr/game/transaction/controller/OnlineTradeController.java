package org.sq.gameDemo.svr.game.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.StorePt;
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
import org.sq.gameDemo.svr.game.transaction.model.OnlineTrade;
import org.sq.gameDemo.svr.game.transaction.model.Trade;
import org.sq.gameDemo.svr.game.transaction.service.OnlineTradeService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
public class OnlineTradeController {

    @Autowired
    private OnlineTradeService onlineTradeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;
    @Autowired
    private BagService bagService;
    /**
     * 发起在线交易
     */
    @OrderMapping(OrderEnum.START_ONLINE_TRADE)
    public void startOnlineTrade(MsgEntity msgEntity,
                             @ReqParseParam TradePt.TradeRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
            onlineTradeService.startTrade(player, requestInfo.getAccpeterId(),  requestInfo.getAuctionItemId()
                    , requestInfo.getAutionCount(), requestInfo.getAccpetCount(), requestInfo.getItemInfoId());
//\\        return msgEntity;
    }

    /**
     * 接收在线交易
     * @param msgEntity
     * @param requestInfo
     */
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

    /**
     * 获取自己发起的所有在线交易
     * @param msgEntity
     * @param builder
     */
    @OrderMapping(OrderEnum.GET_ONLINE_TRADE)
    public MsgEntity getOnlineTrade(MsgEntity msgEntity,
                               @RespBuilderParam TradePt.TradeResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            List<OnlineTrade> trades = onlineTradeService.getTrace(player);
            for (OnlineTrade trade : trades) {
                builder.addTrade((TradePt.Trade) ProtoBufUtil.transformProtoReturnBean(TradePt.Trade.newBuilder(), trade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 获取自己还未处理的面对面交易
     * @param msgEntity
     * @param builder
     */
    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_CAN_RECEIVE)
    public MsgEntity getAcceptOnlineTrade(MsgEntity msgEntity,
                                    @RespBuilderParam TradePt.TradeResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            List<OnlineTrade> trades = onlineTradeService.getTraceCanAccept(player);
            for (OnlineTrade trade : trades) {
                builder.addTrade((TradePt.Trade) ProtoBufUtil.transformProtoReturnBean(TradePt.Trade.newBuilder(), trade));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }
    /**
     * 获取历史的面对面交易
     * @param msgEntity
     * @param builder
     */
    @OrderMapping(OrderEnum.GET_ONLINE_TRADE_HISTORY)
    public MsgEntity getOnlineTradeHistory(MsgEntity msgEntity,
                                          @RespBuilderParam TradePt.TradeResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            List<Trade> trades = onlineTradeService.getTraceHistory(player);
            transformOnlineTrade(builder, trades);
        } catch (Exception e) {
            e.printStackTrace();
        }
        msgEntity.setData(builder.build().toByteArray());

        return msgEntity;
    }

    private void transformOnlineTrade(TradePt.TradeResponseInfo.Builder builder, List<Trade> trades) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Trade trade : trades) {
            TradePt.Trade.Builder tradeBuilder = ProtoBufUtil.transformProtoReturnBuilder(TradePt.Trade.newBuilder(), trade);
            tradeBuilder.setAccpertItemInfo(
                    ProtoBufUtil.transformProtoReturnBuilder(ItemInfoPt.ItemInfo.newBuilder(), bagService.getItemInfo(tradeBuilder.getItemInfoId()))
            );
            builder.addTrade(tradeBuilder);
        }
    }

}
