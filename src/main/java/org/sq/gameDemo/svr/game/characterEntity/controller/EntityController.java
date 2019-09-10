package org.sq.gameDemo.svr.game.characterEntity.controller;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.model.Npc;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.service.CopySceneService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;
import java.util.Optional;

@Controller
public class EntityController {
    @Autowired
    private SenceService senceService;
    @Autowired
    private EntityService entityService;

    @Autowired
    private PlayerCache playerCache;

    @Autowired
    private CopySceneService copySceneService;



    /**
     * 获取所有角色
     * @param msgEntity
     * @return
     * @throws Exception
     */
    @OrderMapping(OrderEnum.GetRole)
    public MsgEntity getRoles(MsgEntity msgEntity,
                              @RespBuilderParam EntityTypeProto.EntityTypeResponseInfo.Builder builder) throws Exception {
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
    public void bindRole(MsgEntity msgEntity,
                         @ReqParseParam UserEntityProto.UserEntityRequestInfo requestInfo,
                         @RespBuilderParam SenceMsgProto.SenceMsgResponseInfo.Builder builder) throws Exception {

        Channel channel = msgEntity.getChannel();
        try {
            int typeId = requestInfo.getTypeId();
            Integer userId = UserCache.getUserIdByChannel(channel);

            //创建玩家角色
            UserEntity player = entityService.playerCreate(typeId, userId, channel);

            //返回场景信息
            getUserSenceMsg(builder, player.getSenceId());

            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (CustomException.BindRoleInSenceException bindException) {
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
        if(sence != null) {
            builder.setSence((SenceProto.Sence) ProtoBufUtil.transformProtoReturnBean(SenceProto.Sence.newBuilder(), sence));
        }
        senceService.transformEntityRespPt(builder, senceId);
    }

    /**
     获取场景信息
     *
     */
    @OrderMapping(OrderEnum.Aoi)
    public MsgEntity aoi(MsgEntity msgEntity,
                         @ReqParseParam SenceMsgProto.SenceMsgRequestInfo requestInfo,
                         @RespBuilderParam SenceMsgProto.SenceMsgResponseInfo.Builder builder) {

        try {
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
    public MsgEntity move(MsgEntity msgEntity,
                          @ReqParseParam SenceProto.RequestSenceInfo requestInfo,
                          @RespBuilderParam SenceMsgProto.SenceMsgResponseInfo.Builder builder) throws Exception {
        String content = "";
        try {

            int newSenceId = requestInfo.getSenceId();
            //判断场景id是否有效
            if(senceService.getSenceBySenceId(newSenceId) == null) {
                throw new CustomException.RemoveFailedException("场景id不存在");
            }

            Channel channel = msgEntity.getChannel();
            Player player = playerCache.getPlayerByChannel(channel);
            senceService.moveToSence(player, newSenceId);
            //修改用户的状态并进行数据库用户场景id更新
            getUserSenceMsg(builder, newSenceId);

        } catch (CustomException.BindRoleInSenceException bindException) {
            content = "你已经在这个场景中，输入getMap获取地图";
            builder.setResult(111);
        } catch (CustomException.RemoveFailedException re) {
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
    public MsgEntity talkWithNpc(MsgEntity msgEntity,
                                 @ReqParseParam NpcPt.NpcReqInfo requestInfo,
                                 @RespBuilderParam NpcPt.NpcRespInfo.Builder builder) throws Exception {
        try {
            long npcId = requestInfo.getId();
            Player player = entityService.getPlayer(msgEntity.getChannel());
            Npc npc = senceService.getTalkNpc(player, player.getSenceId(), npcId);
            if(npc == null) {
                builder.setContent("无此npc, npcId出错");
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


    @OrderMapping(OrderEnum.ShowPlayer)
    public MsgEntity showPlayer(MsgEntity msgEntity,
                              @RespBuilderParam PlayerPt.PlayerRespInfo.Builder builder) throws Exception {
        try {
            Player player = playerCache.getPlayerByChannel(msgEntity.getChannel());
            Optional.ofNullable(player.getSkillInUsedMap().values()).ifPresent(
                    skillList -> skillList.forEach(skill -> {
                            try {
                                builder.addSkill((SkillPt.Skill) ProtoBufUtil.transformProtoReturnBean(SkillPt.Skill.newBuilder(), skill));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
            );
            builder.addPlayer(ProtoBufUtil.transformProtoReturnBuilder(PlayerPt.Player.newBuilder(), player));
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);//服务端异常
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

}
