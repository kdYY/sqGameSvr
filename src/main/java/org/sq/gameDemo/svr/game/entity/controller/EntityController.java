package org.sq.gameDemo.svr.game.entity.controller;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.CustomException.BindRoleInSenceExistException;
import org.sq.gameDemo.svr.common.CustomException.RemoveFailedException;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

@Controller
public class EntityController {
    @Autowired
    private UserService userService;
    @Autowired
    private SenceService senceService;
    @Autowired
    private EntityService entityService;


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

            Integer userId = UserCache.getUserIdByChannel(channel);

            UserEntity userEntity = senceService.getUserEntityByUserId(userId, defaultSenceId);
            if(userEntity != null) {
                //老用户
                throw new BindRoleInSenceExistException();
            }

            int typeId = requestInfo.getTypeId();
            //进行角色-场景id，类型id 初始化绑定
            UserEntity entity = new UserEntity();
            entity.setNick("玩家" + userId);
            entity.setSenceId(defaultSenceId);
            entity.setState(1);
            entity.setTypeId(typeId);
            entity.setUserId(userId);
            senceService.bindUserEntityInSence(entity);
            //数据库角色类型数据增加
            entityService.addUserEntity(entity);
            //返回场景信息
            getUserSenceMsg(builder, defaultSenceId);


            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (BindRoleInSenceExistException bindException) {
            builder.setContent("角色只能绑定一次");
            builder.setResult(111);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        } finally {

            msgEntity.setData(builder.build().toByteArray());
            channel.writeAndFlush(msgEntity);
        }
    }

    private void getUserSenceMsg(SenceMsgProto.SenceMsgResponseInfo.Builder builder, int senceId) {
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
            Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            UserEntity userEntity = senceService.getUserEntityByUserId(userId);
            //获取场景，场景中的角色信息
            getUserSenceMsg(builder, userEntity.getSenceId());
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
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
        try {
            byte[] data = msgEntity.getData();
            SenceProto.RequestSenceInfo requestInfo = SenceProto.RequestSenceInfo.parseFrom(data);
            int newSenceId = requestInfo.getSenceId();
            //判断场景id是否有效


            Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            UserEntity userEntity = senceService.getUserEntityByUserId(userId);
            if(newSenceId == userEntity.getSenceId()) {
                //不能移动到原来的场景
                throw new BindRoleInSenceExistException();
            }
            //从场景中移除并获取
            senceService.removeUserEntity(userEntity);
            //改变用户的所在地
            userEntity.setSenceId(newSenceId);
            //进行重新绑定
            senceService.bindUserEntityInSence(userEntity);
            //修改用户的状态并进行数据库用户场景id更新
            getUserSenceMsg(builder, newSenceId);
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        } catch (BindRoleInSenceExistException bindException) {
            builder.setContent("你已经在这个场景中，输入getMap获取地图");
            builder.setResult(111);
        } catch (RemoveFailedException re) {
            builder.setContent("移动失败，系统繁忙");
            builder.setResult(222);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setContent("移动失败，系统err");
            builder.setResult(500);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        }
    }

    public static final int defaultSenceId = 1;
}
