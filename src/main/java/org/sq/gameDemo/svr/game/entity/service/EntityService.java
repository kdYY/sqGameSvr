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
import org.sq.gameDemo.svr.game.scene.service.SenceService;

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


    public void transformEntityTypeProto(EntityTypeProto.EntityTypeResponseInfo.Builder builder) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (EntityType entityType : entitieTypes) {
            builder.addEntityType(
                    (EntityTypeProto.EntityType) ProtoBufUtil.transformProtoReturnBean(EntityTypeProto.EntityType.newBuilder(), entityType)
            );
        }
    }

    public EntityType getEntityTypeById(int typeId) {
        return  SenceService.getSingleByCondition(entitieTypes, o->o.getId() == typeId);
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
