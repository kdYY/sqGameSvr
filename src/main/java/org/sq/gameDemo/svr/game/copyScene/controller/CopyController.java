package org.sq.gameDemo.svr.game.copyScene.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.model.CopySceneConfig;
import org.sq.gameDemo.svr.game.copyScene.service.CopySceneService;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CopyController {

    @Autowired
    private CopySceneService copySceneService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SenceService senceService;

    @OrderMapping(OrderEnum.SHOW_COPY_SENCE)
    public void showAllCopyModel(MsgEntity msgEntity) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        StringBuffer buffer = new StringBuffer();
        buffer.append("副本模板有以下:\r\n");
        List<CopySceneConfig> collect = copySceneService.getAllCopySceneModel().stream().sorted(Comparator.comparing(CopySceneConfig::getId)).collect(Collectors.toList());
        for (CopySceneConfig model : collect) {
            buffer.append("id:" + model.getId() + ", name:" + model.getName() + "\r\n");
        }

        buffer.append("使用" + OrderEnum.ENTER_NEW_COPY.getOrder() + " id=1 指令进入新副本吧！");

        senceService.notifyPlayerByDefault(player, buffer.toString());
    }

    @OrderMapping(OrderEnum.SHOW_EXIST_COPY_SENCE)
    public void showExistCopySence(MsgEntity msgEntity) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        StringBuffer buffer = new StringBuffer();
        buffer.append("存在的副本场景有以下:");
        List<SenceConfigMsg> allExistCopyScene = copySceneService.getAllExistCopyScene();
        if(allExistCopyScene.size() <= 0) {
            senceService.notifyPlayerByDefault(player, "(空)");
            return;
        }
        for (SenceConfigMsg sence : allExistCopyScene) {
            buffer.append("senceId:"
                    + ((CopyScene)sence).getSenceId()
                    + ", name:" + ((CopyScene)sence).getName()
                    + "剩余可进人数:" + (((CopyScene)sence).getLimit() - ((CopyScene)sence).getPlayerList().size()) + "\r\n");
        }

        buffer.append("使用" + OrderEnum.ENTER_COPY.getOrder() + " senceId=#{senceId} 指令进入副本吧！");

        senceService.notifyPlayerByDefault(player, buffer.toString());
    }

    /**
     * 进入新的副本
     */
    @OrderMapping(OrderEnum.ENTER_NEW_COPY)
    public MsgEntity enterNewCopySence(MsgEntity msgEntity,
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

    @OrderMapping(OrderEnum.ENTER_COPY)
    public MsgEntity enterCopy(MsgEntity msgEntity,
                                       @ReqParseParam SenceMsgProto.SenceMsgRequestInfo requestInfo,
                                       @RespBuilderParam SenceMsgProto.SenceMsgResponseInfo.Builder builder) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            CopyScene copyScene = copySceneService.enterExistCopyScene(requestInfo.getSenceId(), player);
            if(copyScene != null) {
                senceService.transformEntityRespPt(builder, copyScene.getSenceId());
                builder.setResult(Constant.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
        }
        return msgEntity;
    }
}
