package org.sq.gameDemo.svr.game.scene.service;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
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
    //所有的玩家集合
    private List<UserEntity> userEntityList;
    //<场景id,玩家集合>
    private Map<Integer, List<UserEntity>> senceIdAndUserEntityMap;
    //<场景id,场景信息>
    private Map<Integer, SenceConfigMsg> senceIdAndSenceMsgMap;
    //<场景id,怪物集合>
    private Map<Integer, List<SenceEntity>> senceIdAndNpcMap;
    //存储所有角色类型
    private List<EntityType> entityTypes;

    @PostConstruct
    private void initalSence() throws Exception {
        try {
            gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
            List<SenceConfigMsg> senceConfigMsgList = PoiUtil.readExcel(sencedataFileName, 0, SenceConfigMsg.class);
            entityTypes = PoiUtil.readExcel(entityFileName, 0, EntityType.class);
            userEntityList = entityService.getUserEntityList();
            senceIdAndUserEntityMap = userEntityList.stream().collect(Collectors.groupingBy(UserEntity::getSenceId));

            senceConfigMsgList.forEach(senceConfigMsg -> {
                List<SenceEntity> senceEntityList = JsonUtil.reSerializableJson(senceConfigMsg.getJsonStr(), SenceEntity.class);

                ArrayList<SenceEntity> monsterListInSence = new ArrayList<>();

                for (SenceEntity senceEntity : senceEntityList) {
                    for (int i = 0; i < senceEntity.getNum(); i++) {
                        SenceEntity monster = new SenceEntity();
                        monster.setId(i);
                        monster.setState(senceEntity.getState());
                        monster.setTypeId(senceEntity.getTypeId());
                        monster.setSenceId(senceConfigMsg.getSenceId());
                        monster.setNpcWord(senceEntity.getNpcWord());
                        monsterListInSence.add(monster);
                    }
                }

                senceConfigMsg.setSenceEntities(monsterListInSence);
                senceConfigMsg.setUserEntities(senceIdAndUserEntityMap.get(senceConfigMsg.getSenceId()));
                senceConfigMsg.setEntityTypes(entityTypes);
            });

            senceIdAndSenceMsgMap = senceConfigMsgList.stream()
                    .collect(Collectors.toMap(SenceConfigMsg::getSenceId, senceConfigMsg -> senceConfigMsg));

            senceIdAndNpcMap = senceConfigMsgList.stream()
                    .collect(Collectors.toMap(SenceConfigMsg::getSenceId,
                            senceConfigMsg -> senceConfigMsg.getSenceEntities()
                                    .stream()
                                    .filter(senceentity -> senceentity.getTypeId() == 1)
                                    .collect(Collectors.toList())));
        } catch (Exception e) {
            throw  e;
        }
    }

    public SenceConfigMsg getSenecMsgById(int senceId) {
        return senceIdAndSenceMsgMap.get(senceId);
    }

    /**
     * 将场景信息序列化成ResponseEntityInfo
     * @param builder
     * @param senceId
     */
    public void transformEntityResponseProto(SenceMsgProto.SenceMsgResponseInfo.Builder builder, int senceId) throws Exception{
        SenceConfigMsg findSence = null;
        if((findSence=getSenecMsgById(senceId)) == null) {
            throw new customException.NoSuchSenceException("没有此场景");
        }
        ProtoBufUtil.transformProtoReturnBuilder(builder, findSence);
    }

    public EntityType getEntityTypeById(int id) {
        return getSingleByCondition(entityTypes, entityType -> entityType.getId() == id);
    }

    public GameScene getSenceBySenceId(int senceId) {
        return getSingleByCondition(gameScenes, gameScene -> gameScene.getId() == senceId);
    }

    /**
     *     將玩家角色加入场景，并广播通知
     */
    public void addUserEntityInSence(UserEntity userEntity, Channel channel) throws customException.BindRoleInSenceException {
        SenceConfigMsg msg = senceIdAndSenceMsgMap.get(userEntity.getSenceId());
        List<UserEntity> userEntities = msg.getUserEntities();
        if(userEntities == null) {
            ArrayList<UserEntity> entities = new ArrayList<>();
            entities.add(userEntity);
            msg.setUserEntities(entities);

            senceIdAndUserEntityMap.put(msg.getSenceId(), entities);
        } else {
            userEntities.add(userEntity);
        }
        //广播通知
        //增加进channelGroup
        UserCache.addChannelInGroup(userEntity.getSenceId(), channel, userEntity.getNick() + "已经上线!");
    }



    /**
     * /从原来的remove掉，同时广播通知
     * @param userEntity
     * @param channel
     * @throws customException.RemoveFailedException
     */
    public UserEntity removeUserEntityAndGet(UserEntity userEntity, Channel channel) throws customException.RemoveFailedException {
        UserEntity userEntityFind = getUserEntityByUserId(userEntity.getUserId(), userEntity.getSenceId());
        List<UserEntity> userEntities = senceIdAndUserEntityMap.get(userEntity.getSenceId());
        synchronized (userEntities) {
            if(!userEntities.remove(userEntityFind)) {
                throw new customException.RemoveFailedException("移动失败");
            }
        }
        UserCache.moveChannelInGroup(userEntityFind.getSenceId(), channel, userEntity.getNick() + "已经下线!");
        return userEntityFind;
    }

    private boolean checkUserEntityExistInSence(int userId, List<UserEntity> userEntities) {
        if(getSingleByCondition(userEntities, (o -> o.getUserId() == userId)) != null) {
            return true;
        }
        return false;
    }




    public UserEntity getUserEntityByUserId(int userId, int senceId) {
        return getSingleByCondition(senceIdAndUserEntityMap.get(senceId), (entity -> entity.getUserId().equals(userId)));
    }

    public synchronized UserEntity getUserEntityByUserId(Integer userId) {
        return getSingleByCondition(userEntityList, (o -> o.getUserId().equals(userId)));
    }

    public synchronized SenceEntity getSenceEntityBySenceId(Integer senceId, Integer npcId) {
        return getSingleByCondition(senceIdAndNpcMap.get(senceId), o -> o.getId().equals(npcId));
    }

    public static <T> T getSingleByCondition(List<T> list, Function<T,Boolean> function) {
        if(list == null || list.size() == 0) {
            return null;
        }
        Optional<T> first = list.stream().filter(o -> function.apply(o)).findFirst();
        if(first.equals(Optional.empty())) {
            return null;
        }
        return first.get();
    }


}
