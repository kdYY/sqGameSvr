package org.sq.gameDemo.svr.game.entity.service;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.CustomException.BindRoleInSenceExistException;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.game.entity.dao.UserEntityMapper;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntityExample;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class EntityService {

    @Autowired
    private UserEntityMapper userEntityMapper;

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


    public void transformEntityTypeProto(EntityTypeProto.EntityTypeResponseInfo.Builder builder) {
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

    //实体转化为SenceEntity
    public SenceEntityProto.SenceEntity transformSenceEntityProto(SenceEntity senceEntity) {
        EntityType entityTypeById = getEntityTypeById(senceEntity.getTypeId());

        return SenceEntityProto.SenceEntity.newBuilder()
                .setNum(senceEntity.getNum())
                .setState(senceEntity.getState())
                .addType( EntityType.transform(entityTypeById))
                .build();
    }
    //实体转化为UserEntity
    public UserEntityProto.UserEntity transformUserEntityProto(UserEntity userEntity) {
        EntityType entityTypeById = getEntityTypeById(userEntity.getTypeId());

        return UserEntityProto.UserEntity.newBuilder()
                .setNick(userEntity.getNick())
                .setTypeId(userEntity.getTypeId())
                .setUserId(userEntity.getUserId())
                .setState(userEntity.getState())
                .addType(EntityType.transform(entityTypeById))
                .build();
    }

    //获取数据库中UserEntity实体
    public List<UserEntity> getUserEntityList() {
        return userEntityMapper.selectByExample(new UserEntityExample());
    }

    public UserEntity getUserEntityByUserId(int userId) {
        return userEntityMapper.getUserEntityByUserId(userId);
    }
}
