package org.sq.gameDemo.svr.game.copyScene.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.BuffPt;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.service.CopySceneService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Controller
public class CopyController {

    @Autowired
    private CopySceneService copySceneService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SenceService senceService;

    @OrderMapping(OrderEnum.ENTER_COPY)
    public MsgEntity enterCopySence(MsgEntity msgEntity,
                        @ReqParseParam SenceMsgProto.SenceMsgRequestInfo requestInfo,
                        @RespBuilderParam SenceMsgProto.SenceMsgResponseInfo.Builder builder) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            CopyScene copyScene = copySceneService.enterNewCopyScene(requestInfo.getSenceId(), player);
            senceService.transformEntityRespPt(builder, copyScene.getSenceId());
            builder.setResult(Constant.SUCCESS);//服务端异常
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
        }
        return msgEntity;

    }
}
