package org.sq.gameDemo.svr.game.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.StorePt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.store.model.Store;
import org.sq.gameDemo.svr.game.store.service.StoreService;

@Controller
public class StoreController {


    @Autowired
    private StoreService storeService;
    @Autowired
    private EntityService entityService;


    /**
     * 展示商店
     */
    @OrderMapping(OrderEnum.SHOW_STORE)
    public MsgEntity showStore(MsgEntity msgEntity,
                             @RespBuilderParam StorePt.StoreResponseInfo.Builder builder) {
        Store store = storeService.showStore(1);
        try {
            builder.setStore((StorePt.Store) ProtoBufUtil.transformProtoReturnBean(StorePt.Store.newBuilder(), store));
            msgEntity.setData(builder.build().toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
        }
        return msgEntity;
    }


    /**
     * 购买商店物品
     */
    @OrderMapping(OrderEnum.BUY_SHOP_ITEM)
    public void buyGood(MsgEntity msgEntity,
                               @ReqParseParam StorePt.StoreRequestInfo requestInfo) {
        try {
            storeService.buyGood(entityService.getPlayer(msgEntity.getChannel()), requestInfo.getItemInfoId(), requestInfo.getCount(),
                    requestInfo.getId());
        } catch (CustomException.SystemSendMailErrException e) {
            e.printStackTrace();
        }
    }

}
