package org.sq.gameDemo.svr.game.entity.service;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.common.proto.UserProto;
import org.sq.gameDemo.svr.common.CustomException.BindRoleInSenceExistException;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class EntityService {



    @Autowired
    private UserService userService;
    @Autowired
    private SenceService senceService;

    @Value("${excel.entity}")
    private String entityFileName;

    //存储所有的实体信息
    private List<EntityType> entitieTypes;

    @PostConstruct
    public void initalEntity() {
        try {
            entitieTypes = PoiUtil.readExcel(entityFileName, 0, EntityType.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EntityType> getEntitieTypes() {
        return entitieTypes;
    }

    public String getEntitieListString() {
        StringBuilder builder = new StringBuilder();
        for (EntityType entityBaseType : entitieTypes) {
            builder.append(entityBaseType.toString() + "\r\n");
        }
        return builder.toString();
    }

    /**
     * 绑定角色
     * @param msgEntity
     * @throws Exception
     */
    @OrderMapping(OrderEnum.BindRole)
    public void bindRole(MsgEntity msgEntity) throws Exception {

        EntityProto.ResponseEntityInfo.Builder builder =EntityProto.ResponseEntityInfo.newBuilder();
        Channel channel = msgEntity.getChannel();
        try {
            byte[] data = msgEntity.getData();
            EntityProto.RequestEntityInfo requestEntityInfo = EntityProto.RequestEntityInfo.parseFrom(data);

            builder.setMsgId(requestEntityInfo.getMsgId())
                    .setTime(requestEntityInfo.getTime());

            int typeId = requestEntityInfo.getTypeId();
            Integer userId = UserCache.getUserIdByChannel(channel);
            User userSaved = UserCache.getUserById(userId);
            synchronized (userSaved) {
                //进行角色类型-用户绑定
                if (userSaved == null) {//一般情况下不可能为空...
                    User user = userService.getUser(userId);
                    user.setTypeId(typeId);
                    UserCache.addUserMap(userId, user);
                } else {
                    userSaved.setTypeId(typeId);
                }
                //进行角色-场景初始化绑定
                senceService.bindUserEntityInSence(userSaved.getSenceId(),
                        new UserEntity(typeId, "玩家" + userId, userId));
                //数据库角色类型数据更新
                userService.updateUser(userId, typeId);

            }
            //返回场景信息
            getUserSenceMsg(builder, userSaved);

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

    private void getUserSenceMsg(EntityProto.ResponseEntityInfo.Builder builder, User userCached) {
        Integer userCachedSenceid = userCached.getSenceId();

        //场景，场景中的角色信息
        GameScene sence = senceService.getSenceBySenceId(userCachedSenceid);
        builder.setSence(GameScene.transformProto(sence));
        senceService.transformEntityResponseProto(builder, sence.getId());
    }

    /**
     获取场景信息
     *
     */
    @OrderMapping(OrderEnum.Aoi)
    public MsgEntity aoi(MsgEntity msgEntity) throws Exception {
        EntityProto.ResponseEntityInfo.Builder builder =EntityProto.ResponseEntityInfo.newBuilder();
        try {
            byte[] data = msgEntity.getData();
            UserProto.RequestUserInfo requestInfo = UserProto.RequestUserInfo.parseFrom(data);
            Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            User userCached = UserCache.getUserById(userId);
            //获取场景，场景中的角色信息
            getUserSenceMsg(builder, userCached);
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
        EntityProto.ResponseEntityInfo.Builder builder =EntityProto.ResponseEntityInfo.newBuilder();
        try {
            byte[] data = msgEntity.getData();
            SenceProto.RequestSenceInfo requestInfo = SenceProto.RequestSenceInfo.parseFrom(data);
            int newSenceId = requestInfo.getSenceId();
            Integer userId = UserCache.getUserIdByChannel(msgEntity.getChannel());
            User userCached = UserCache.getUserById(userId);
            if(newSenceId == userCached.getSenceId()) {
                throw new BindRoleInSenceExistException("err");
            }
            synchronized (userCached) {
                //从场景中移除并获取
                UserEntity userEntity = senceService.removeUserEntityAndGet(userCached);
                //进行重新绑定
                senceService.bindUserEntityInSence(newSenceId, userEntity);
                //修改用户的状态并进行数据库用户场景id更新
                userCached.setSenceId(newSenceId);
                userService.updateUser(userCached);
            }
            //获取场景，场景中的角色信息
            getUserSenceMsg(builder, userCached);
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        }catch (BindRoleInSenceExistException bindException) {
            builder.setContent("角色只能绑定一次");
            builder.setResult(111);
        }  catch (Exception e) {
            e.printStackTrace();
            builder.setResult(500);//服务端异常
        } finally {
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        }
    }

    public void transformEntityTypeProto(EntityProto.ResponseEntityInfo.Builder builder) {
        for (EntityType entitieType : entitieTypes) {
            builder.addEntityType(
                    EntityType.transform(entitieType)
            );
        }
    }

    public EntityType getEntityTypeById(int typeId) {
        for (EntityType entitieType : entitieTypes) {
            if(entitieType.getId() == typeId) {
                return entitieType;
            }
        }
        return null;
    }

    public EntityProto.SenceEntity transformSenceEntityProto(SenceEntity senceEntity) {
        EntityType entityTypeById = getEntityTypeById(senceEntity.getTypeId());

        return EntityProto.SenceEntity.newBuilder()
                .setNum(senceEntity.getNum())
                .setState(senceEntity.getState())
                .addType( EntityType.transform(entityTypeById))
                .build();
    }

    public EntityProto.UserEntity transformSenceUserEntityProto(UserEntity userEntity) {
        EntityType entityTypeById = getEntityTypeById(userEntity.getTypeId());

        return EntityProto.UserEntity.newBuilder()
                .setNick(userEntity.getNick())
                .setTypeId(userEntity.getTypeId())
                .setUserId(userEntity.getUserId())
                .setState(userEntity.getState())
                .addType(EntityType.transform(entityTypeById))
                .build();
    }
}
