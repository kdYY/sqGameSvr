package org.sq.gameDemo.svr.game.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.entity.dao.UserEntityMapper;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntityExample;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
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


    public void transformEntityTypeProto(EntityTypeProto.EntityTypeResponseInfo.Builder builder) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (EntityType entitieType : entitieTypes) {
            builder.addEntityType(
                    (EntityTypeProto.EntityType) ProtoBufUtil.transformProtoReturnBean(EntityTypeProto.EntityType.newBuilder(), entitieType)
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
                .setId(senceEntity.getId())
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


    public void addUserEntity(UserEntity entity) {
        userEntityMapper.insertSelective(entity);
    }
}
