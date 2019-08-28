package org.sq.gameDemo.svr.game.bag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.BagPt;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
     */
    @OrderMapping(OrderEnum.SHOW_ITEMINFO)
    public MsgEntity showAllItemInfo(MsgEntity msgEntity,
                                @RespBuilderParam ItemInfoPt.ItemInfoResponseInfo.Builder builder) {
        List<ItemInfo> itemInfos = bagService.showAllItemInfo();
        for (ItemInfo itemInfo : itemInfos) {
            try {
                builder.addItemInfo(ProtoBufUtil.transformProtoReturnBuilder(ItemInfoPt.ItemInfo.newBuilder(), itemInfo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 玩家使用背包中的物品
     * @param msgEntity
     * @param requestInfo
     */
    @OrderMapping(OrderEnum.USE_ITEM)
    public void useItem(MsgEntity msgEntity,
                        @ReqParseParam ItemPt.ItemRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        if(bagService.useItem(player, requestInfo.getId(), requestInfo.getCount())) {
            senceService.notifyPlayerByDefault(player, "物品使用完毕");
        } else {
            senceService.notifyPlayerByDefault(player, "该物品不能直接使用");
        }
    }


    /**
     * 玩家移除背包中的物品
     * @param msgEntity
     * @param requestInfo
     */
    @OrderMapping(OrderEnum.REMONVE_ITEM)
    public MsgEntity removeItem(MsgEntity msgEntity,
                           @ReqParseParam ItemPt.ItemRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());

        bagService.removeItem(player, requestInfo.getId(), requestInfo.getCount());
        return msgEntity;
    }


    /**
     * 展示背包
     */
    @OrderMapping(OrderEnum.TIDY_BAG)
    public MsgEntity tidyBag(MsgEntity msgEntity,
                           @ReqParseParam BagPt.BagReqInfo requestInfo,
                           @RespBuilderParam BagPt.BagRespInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        bagService.tidyBag(player);
        showBag(msgEntity, requestInfo, builder);
        return msgEntity;
    }


    @OrderMapping(OrderEnum.SHOW_BAG)
    public MsgEntity showBag(MsgEntity msgEntity,
                             @ReqParseParam BagPt.BagReqInfo requestInfo,
                             @RespBuilderParam BagPt.BagRespInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Bag bag = player.getBag();
        try {
            builder.setBag((BagPt.Bag) ProtoBufUtil.transformProtoReturnBean(BagPt.Bag.newBuilder(), bag));

            builder.setResult(Constant.SUCCESS);
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
            builder.setContent("服务端异常");
        } finally {
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        }
        return msgEntity;
    }

}
