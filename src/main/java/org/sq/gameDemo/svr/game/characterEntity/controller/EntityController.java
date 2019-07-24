package org.sq.gameDemo.svr.game.characterEntity.controller;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Npc;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Controller
public class EntityController {
    @Autowired
    private SenceService senceService;
    @Autowired
    private EntityService entityService;

    @Autowired
    private PlayerCache playerCache;


    /**
     * 获取所有角色
     * @param msgEntity
     * @return
     * @throws Exception
     */
    @OrderMapping(OrderEnum.GetRole)
    public MsgEntity getRoles(MsgEntity msgEntity) throws Exception {
        EntityTypeProto.EntityTypeResponseInfo.Builder builder = EntityTypeProto.EntityTypeResponseInfo.newBuilder();
        try {
            entityService.transformEntityTypeProto(builder);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 绑定角色
     * @param msgEntity
     * @throws Exception
     */
    @OrderMapping(OrderEnum.BindRole)
    public void bindRole(MsgEntity msgEntity) throws Exception {

        SenceMsgProto.SenceMsgResponseInfo.Builder builder = SenceMsgProto.SenceMsgResponseInfo.newBuilder();
        Channel channel = msgEntity.getChannel();
        try {
            byte[] data = msgEntity.getData();
            UserEntityProto.UserEntityRequestInfo requestInfo = UserEntityProto.UserEntityRequestInfo.parseFrom(data);
            int typeId = requestInfo.getTypeId();
            Integer userId = UserCache.getUserIdByChannel(channel);

            //创建玩家角色
            UserEntity player = entityService.playerCreate(typeId, userId, channel);

            //返回场景信息
            getUserSenceMsg(builder, player.getSenceId());

            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (customException.BindRoleInSenceException bindException) {
            builder.setContent("角色只能绑定一次");
            builder.setResult(111);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {

            msgEntity.setData(builder.build().toByteArray());
            channel.writeAndFlush(msgEntity);
        }
    }





    private void getUserSenceMsg(SenceMsgProto.SenceMsgResponseInfo.Builder builder, int senceId) throws Exception {
        //场景，场景中的角色信息
        GameScene sence = senceService.getSenceBySenceId(senceId);
        builder.setSence(GameScene.transformProto(sence));
        senceService.transformEntityResponseProto(builder, sence.getId());
    }

    /**
     获取场景信息
     *
     */
    @OrderMapping(OrderEnum.Aoi)
    public MsgEntity aoi(MsgEntity msgEntity) {

        SenceMsgProto.SenceMsgResponseInfo.Builder builder = SenceMsgProto.SenceMsgResponseInfo.newBuilder();
        try {
            byte[] data = msgEntity.getData();
            SenceMsgProto.SenceMsgRequestInfo requestInfo = SenceMsgProto.SenceMsgRequestInfo.parseFrom(data);
            // TODO 优化UserCache结构
            // Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            Player player = playerCache.getPlayerByChannel(msgEntity.getChannel());
            //获取场景，场景中的角色信息
            getUserSenceMsg(builder, player.getSenceId());
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        }
    }

    /**
     获取场景信息
     *
     */
    @OrderMapping(OrderEnum.Move)
    public MsgEntity move(MsgEntity msgEntity) throws Exception {
        SenceMsgProto.SenceMsgResponseInfo.Builder builder = SenceMsgProto.SenceMsgResponseInfo.newBuilder();
        byte[] data = msgEntity.getData();
        SenceProto.RequestSenceInfo requestInfo = SenceProto.RequestSenceInfo.parseFrom(data);
        String content = "";
        try {

            int newSenceId = requestInfo.getSenceId();
            //判断场景id是否有效
            if(senceService.getSenceBySenceId(newSenceId) == null) {
                throw new customException.RemoveFailedException("场景id不存在");
            }

            Player player = playerCache.getPlayerByChannel(msgEntity.getChannel());

            if(newSenceId == player.getSenceId()) {
                //不能移动到原来的场景
                throw new customException.BindRoleInSenceException();
            }
            //从场景中移除并获取
            senceService.removePlayerAndGet(player.getUserId(), msgEntity.getChannel());
            //改变用户的所在地
            player.setSenceId(newSenceId);
            //进行重新绑定
            senceService.addPlayerInSence(player, msgEntity.getChannel());
            //修改用户的状态并进行数据库用户场景id更新
            getUserSenceMsg(builder, newSenceId);

        } catch (customException.BindRoleInSenceException bindException) {
            content = "你已经在这个场景中，输入getMap获取地图";
            builder.setResult(111);
        } catch (customException.RemoveFailedException re) {
            content = re.getMessage();
            builder.setResult(222);
        } catch (Exception e) {
            e.printStackTrace();
            content = "移动失败，系统err";
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {
            builder.setContent(content);
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        }
    }

    /**
     * 获取指定npc语录
     * @param msgEntity
     * @return
     * @throws Exception
     */
    @OrderMapping(OrderEnum.TalkWithNpc)
    public MsgEntity talkWithNpc(MsgEntity msgEntity) throws Exception {
        NpcPt.NpcRespInfo.Builder builder = NpcPt.NpcRespInfo.newBuilder();
        try {
            NpcPt.NpcReqInfo requestInfo =  NpcPt.NpcReqInfo.parseFrom(msgEntity.getData());
            int npcId = requestInfo.getId();

            Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            Player player = entityService.getInitedPlayer(userId, msgEntity.getChannel());
            Npc npc = senceService.getNpcBySenceIdAndId(player.getSenceId(), npcId);
            if(npc == null) {
                builder.setContent("无此npc, npc编号出错");
                builder.setResult(Constant.ORDER_ERR);
            } else {
                NpcPt.Npc npcProto =
                        ProtoBufUtil.transformProtoReturnBuilder(NpcPt.Npc.newBuilder(), npc).build();
                builder.addNpc(npcProto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            builder.setContent("系统err");
            builder.setResult(Constant.SVR_ERR);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        }
    }





}
