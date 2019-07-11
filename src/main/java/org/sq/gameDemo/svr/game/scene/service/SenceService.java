package org.sq.gameDemo.svr.game.scene.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.svr.common.CustomException.BindRoleInSenceExistException;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.user.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SenceService {

    @Autowired
    private EntityService entityService;

    @Value("${excel.sence}")
    private String senceFileName;

    @Value("${excel.sencedata}")
    private String sencedataFileName;

    @Value("${excel.entity}")
    private String entityFileName;
    //存储场景
    private List<GameScene> gameScenes;

    //场景信息


    //<typeId, 怪物>
    private Map<Integer, List<SenceEntity>> map = new HashMap<>();
    //<场景id,场景信息>
    private Map<Integer, SenceConfigMsg> senceMsgMap;

    //存储所有初始化的实体信息
    private List<EntityType> entities;

    @PostConstruct
    private void initalSence() {
        try {
            gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
            List<SenceConfigMsg> senceConfigMsgList = PoiUtil.readExcel(sencedataFileName, 0, SenceConfigMsg.class);
            entities = PoiUtil.readExcel(entityFileName, 0, EntityType.class);
            for (SenceConfigMsg senceMap : senceConfigMsgList) {
                List<SenceEntity> senceEntityList = JsonUtil.reSerializableJson(senceMap.getJsonStr(), SenceEntity.class);
                ArrayList<SenceEntity> senceEntities = new ArrayList<>();
                for (SenceEntity senceEntity : senceEntityList) {
                    ArrayList<SenceEntity> entitys = new ArrayList<>();
                    for (int i = 0; i < senceEntity.getNum(); i++) {
                        SenceEntity entity = new SenceEntity();
                        entity.setId(i);
                        entity.setState(senceEntity.getState());
                        entity.setTypeId(senceEntity.getTypeId());
                        entitys.add(entity);
                    }
                    map.put(senceEntity.getTypeId(), entitys);
                    senceEntities.addAll(entitys);
                }
                senceMap.setSenceEntities(senceEntities);

            }
            senceMsgMap = senceConfigMsgList.stream().collect(Collectors.toMap(SenceConfigMsg::getSenceId, senceConfigMsg -> senceConfigMsg));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public SenceConfigMsg getSenecMsgById(int senceId) {
        return senceMsgMap.get(senceId);
    }

    /**
     * 将场景信息序列化成ResponseEntityInfo
     * @param builder
     * @param senceId
     */
    public void transformEntityResponseProto(EntityProto.ResponseEntityInfo.Builder builder, int senceId) {
        SenceConfigMsg findSence = null;
        if((findSence=getSenecMsgById(senceId)) == null) {
            return;
        }

        for (UserEntity userEntity : findSence.getUserEntities()) {
            builder.addUserEntity(entityService.transformSenceUserEntityProto(userEntity));
        }
        for (SenceEntity senceEntity : findSence.getSenceEntities()) {
            builder.addSenceEntity(entityService.transformSenceEntityProto(senceEntity));
        }
    }


    public EntityType getEntityTypeById(int id) {
        for (EntityType entityBaseType : entities) {
            if(entityBaseType.getId() == id) {
                return entityBaseType;
            }
        }
        return null;
    }

    //创建用户 角色  根据id创建一个entity， 默认在场景 id = 1起源之地


    public GameScene getSenceBySenceId(int senceId) {
        for (GameScene gameScene : gameScenes) {
            if(gameScene.getId() == senceId) {
                return gameScene;
            }
        }
        return null;
    }

    //
    public void bindUserEntityInSence(int senceId, UserEntity userEntity) throws BindRoleInSenceExistException{
        SenceConfigMsg msg = senceMsgMap.get(senceId);
        List<UserEntity> userEntities = msg.getUserEntities();
        for (UserEntity entity : userEntities) {
            if(entity.getUserId() == userEntity.getUserId()) {
                throw new BindRoleInSenceExistException("该角色已经存在场景中");
            }
        }
        userEntities.add(userEntity);
    }

    public GameScene bindUserEntityAndGetSence(int senceId, UserEntity userEntity) {
        SenceConfigMsg msg = senceMsgMap.get(senceId);
        if(msg != null) {
            msg.getUserEntities().add(userEntity);
            return getSenceBySenceId(senceId);
        } else {
            return null;
        }

    }

    //添加到新的sence,同时广播通知
    public void moveToSence(UserEntity userEntity, int newSenceId) throws BindRoleInSenceExistException{
        bindUserEntityInSence(newSenceId, userEntity);
    }

    //从原来的remove掉，同时广播通知
    public UserEntity removeUserEntityAndGet(User user) {
        SenceConfigMsg msg = senceMsgMap.get(user.getSenceId());
        List<UserEntity> userEntities = msg.getUserEntities();
        for (UserEntity userEntity : userEntities) {
            if(userEntity.getUserId() == user.getId()) {
                userEntities.remove(userEntity);
                return userEntity;
            }
        }
        return null;
    }

}
