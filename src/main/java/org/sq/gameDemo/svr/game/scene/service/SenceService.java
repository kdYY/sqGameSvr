package org.sq.gameDemo.svr.game.scene.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //存储单体场景信息
    private List<GameScene> gameScenes;

    //
    private List<SenceConfigMsg> senceConfigMsgList;

    //<typeId, 怪物>
    private Map<Integer, List<SenceEntity>> map = new HashMap<>();

    //存储所有初始化的实体信息
    private List<EntityType> entities;

    @PostConstruct
    private void initalSence() {
        try {
            gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
            senceConfigMsgList = PoiUtil.readExcel(sencedataFileName, 0, SenceConfigMsg.class);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public SenceConfigMsg getSenecMsgById(int senceId) {
        for (SenceConfigMsg msg : senceConfigMsgList) {
            if(msg.getSenceId() == senceId) {
                return msg;
            }
        }
        return null;
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

        int i=0;
        for (UserEntity userEntity : findSence.getUserEntities()) {
            builder.setUserEntity(i++, entityService.transformSenceUserEntityProto(userEntity));
        }
        i=0;
        for (SenceEntity senceEntity : findSence.getSenceEntities()) {
            builder.setSenceEntity(i++, entityService.transformSenceEntityProto(senceEntity));
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


    public void addUsetEntityInSence(int senceId, UserEntity userEntity) {
        senceConfigMsgList.forEach(msg -> {
            if(msg.getSenceId() == senceId) {
                msg.getUserEntities().add(userEntity);
            }
        });
    }

    public GameScene addUsetEntityAndGetSence(int senceId, UserEntity userEntity) {
        for (SenceConfigMsg msg : senceConfigMsgList) {
            if (msg.getSenceId() == senceId) {
                msg.getUserEntities().add(userEntity);
                return getSenceBySenceId(senceId);
            }
        }
        return null;
    }


}
