package org.sq.gameDemo.svr.game.scene.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.copyScene.service.CopySceneService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SenceService {

    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceEntityCache senceEntityCache;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SkillService skillService;


    @Value("${excel.sence}")
    private String senceFileName;

    @Value("${excel.sencedata}")
    private String sencedataFileName;
    //存储场景
    private List<GameScene> gameScenes;

    //<场景id,场景信息>
    /** 缓存不过期 **/
    private static Cache<Integer, SenceConfigMsg> senceIdAndSenceMsgMap = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "场景信息被移除, 原因是" + notification.getCause())
            ).build();



    @PostConstruct
    private void initalSence() throws Exception {
        gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
        List<SenceConfig> senceConfList = PoiUtil.readExcel(sencedataFileName, 0, SenceConfig.class);

        senceConfList.forEach(config -> {
            SenceConfigMsg senceConfigMsg = getInitedSence(config);
            senceIdAndSenceMsgMap.put(config.getSenceId(), senceConfigMsg);
        });

    }


    public Cache<Integer, SenceConfigMsg> getSenceCache() {
        return senceIdAndSenceMsgMap;
    }

    private SenceConfigMsg getInitedSence(SenceConfig config) {
        List<SenceConfig.tmpCommonConf> tmpCommonConfList = JsonUtil.reSerializableJson(config.getJsonStr(), SenceConfig.tmpCommonConf.class);

        ArrayList<Monster> monsterListinSence = new ArrayList<>();
        ArrayList<Npc> npclistinsence = new ArrayList<>();
        for (SenceConfig.tmpCommonConf tmpCommonConf : tmpCommonConfList) {
            SenceEntity senceEntity = senceEntityCache.get((long) tmpCommonConf.getId());
            if(senceEntity.getTypeId().equals(Constant.Monster)) {
                for (int i = 0; i < tmpCommonConf.getNum(); i++) {
                    Monster monster = getInitedMonster(senceEntity, config.getSenceId(), tmpCommonConf.getLevel());
                    monsterListinSence.add(monster);
                }
            }
            if(senceEntity.getTypeId().equals(Constant.NPC)) {
                for (int i = 0; i < tmpCommonConf.getNum(); i++) {
                    Npc npc = getInitedNpc(senceEntity, config.getSenceId());
                    npclistinsence.add(npc);
                }
            }
        }
        SenceConfigMsg senceConfigMsg = new SenceConfigMsg();
        senceConfigMsg.setSenceId(config.getSenceId());
        senceConfigMsg.setMonsterList(monsterListinSence);
        senceConfigMsg.setNpcList(npclistinsence);
        return senceConfigMsg;
    }

    private Npc getInitedNpc(SenceEntity senceEntity, Integer senceId) {
        Npc npc = new Npc();
        BeanUtils.copyProperties(senceEntity, npc);
        npc.setId(ConcurrentSnowFlake.getInstance().nextID());
        npc.setSenceId(senceId);
        return npc;
    }

    public Monster getInitedMonster(SenceEntity senceEntity, Integer senceId, Integer level) {
        Monster monster = new Monster();
        BeanUtils.copyProperties(senceEntity, monster);
        monster.setId(ConcurrentSnowFlake.getInstance().nextID());
        monster.setEntityTypeId(senceEntity.getId());
        monster.setSenceId(senceId);
        ConcurrentMap<Integer, Skill> collect = Arrays.stream(monster.getSkillStr().trim().split(","))
                .map(Integer::valueOf)
                .filter(str -> skillService.getSkill(str) != null)
                .map(skillService::getSkill)
                .collect(Collectors.toConcurrentMap(
                        skill -> skill.getId(),
                        skill -> {
                            Skill monsterSkill = new Skill();
                            BeanUtils.copyProperties(skill, monsterSkill);
                            monsterSkill.setHurt(skill.getHurt() * level);
                            monsterSkill.setMpNeed(0L);
                            return monsterSkill;
                        }
                        ));
        monster.setSkillInUsedMap(collect);
        monster.setLevel(level);
        return monster;
    }

    /**
     * 根据场景id获取场景中的所有角色信息
     * @param senceId
     * @return
     */
    public SenceConfigMsg getSenecMsgById(int senceId) {
        return senceIdAndSenceMsgMap.getIfPresent(senceId);
    }

    /**
     * 将场景信息序列化成ResponseEntityInfo
     * @param builder
     * @param senceId
     */
    public void transformEntityRespPt(SenceMsgProto.SenceMsgResponseInfo.Builder builder, int senceId) throws Exception{
        SenceConfigMsg findSence = null;
        if((findSence = getSenecMsgById(senceId)) == null) {
            throw new CustomException.NoSuchSenceException("没有此场景");
        }
        if(findSence instanceof CopyScene) {
            ProtoBufUtil.transformProtoReturnBean(builder, (CopyScene)findSence);
        } else {
            ProtoBufUtil.transformProtoReturnBuilder(builder, findSence);
        }
    }

    /**
     * 根据场景id获取场景
     * @param senceId
     * @return
     */
    public GameScene getSenceBySenceId(int senceId) {
        return getSingleByCondition(gameScenes, gameScene -> gameScene.getId() == senceId);
    }




    /**
     *     將玩家角色加入场景，并广播通知
     */
    public void addPlayerInSence(Player player) throws CustomException.BindRoleInSenceException {
        SenceConfigMsg msg = senceIdAndSenceMsgMap.getIfPresent(player.getSenceId());
        Optional.ofNullable(msg).ifPresent(senceConfigMsg -> {
            List<Player> playerList = senceConfigMsg.getPlayerList();
            if(Objects.isNull(playerList)) {
                playerList = new CopyOnWriteArrayList<>();
                senceConfigMsg.setPlayerList(playerList);
            }
            playerList.add(player);
            //广播通知
            notifySenceByDefault(player.getSenceId(), player.getName() + "进入场景!");
        });

    }



    /**
     * /remove掉场景中的player，同时广播通知
     */
    public Player removePlayerAndGet(Player player) throws CustomException.RemoveFailedException {
        //玩家在非副本场景 (不能判断senceId)
//        if(player.getCopySceneId() == null || player.getCopySceneId().intValue() <= 0) {
            SenceConfigMsg senceConfigMsg = senceIdAndSenceMsgMap.getIfPresent(player.getSenceId());
            List<Player> playerList = senceConfigMsg.getPlayerList();
            if(!playerList.remove(player)) {
                throw new CustomException.RemoveFailedException("移动失败");
            }
            notifySenceByDefault(player.getSenceId(),player.getName() + "离开场景!");
//        }
        //玩家在副本场景
//        else {
//            copySceneService.removeFromCopyScene(player);
//        }

        return player;
    }

    public Npc getNpcInSence(Integer senceId, Long npcId) {
        return getSingleByCondition(
                senceIdAndSenceMsgMap.getIfPresent(senceId).getNpcList(),
                o -> o.getId().equals(npcId));
    }

    public static <T> T getSingleByCondition(List<T> list, Function<T,Boolean> function) {
        if(Objects.isNull(list) || list.size() == 0) {
            return null;
        }
        Optional<T> first = list.stream().filter(o -> function.apply(o)).findFirst();
        if(first.equals(Optional.empty())) {
            return null;
        }
        return first.get();
    }

    /**
     * 从场景移除，加入新场景
     * @param player
     * @param newSenceId
     * @throws Exception
     */
    public void moveToSence(Player player, int newSenceId) throws Exception{
        //场景id相同且不在副本中
        if(newSenceId == player.getSenceId() ) {
            //不能移动到原来的场景
            throw new CustomException.BindRoleInSenceException();
        }
        //从场景中移除并获取
        removePlayerAndGet(player);
        //改变用户的所在地
        player.setSenceId(newSenceId);
        //进行重新绑定
        addPlayerInSence(player);
    }


    public void notifyPlayerByDefault(Character attacter, String content) {
        Channel channel = null;
        if(attacter instanceof Player) {
            channel = playerCache.getChannelByPlayerId(attacter.getId());
        }
        if(attacter instanceof Monster
                && ((Monster)attacter).getTarget() instanceof Player) {
            channel = playerCache.getChannelByPlayerId(((Monster)attacter).getTarget().getId());
        }
        Optional.ofNullable(channel).ifPresent(ch -> ch.writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity(content)));
    }

    public void notifySenceByDefault(Integer senceId, String content) {
        getSenecMsgById(senceId).getPlayerList().forEach(
                player -> {
                    Channel channel = playerCache.getChannelByPlayerId(player.getId());
                    Optional.ofNullable(channel).ifPresent(ch -> ch.writeAndFlush(ProtoBufUtil.getBroadCastDefaultEntity(content)));
                }
        );


    }


    public boolean removeMonster(Monster targetMonster) {
        SenceConfigMsg sence = getSenecMsgById(targetMonster.getSenceId());
        if(sence instanceof CopyScene && ((CopyScene) sence).getBoss().getId().equals(targetMonster.getId())) {
            ((CopyScene) sence).setBoss(null);
            return true;
        } else {
            return getSenecMsgById(targetMonster.getSenceId()).getMonsterList().remove(targetMonster);
        }

    }

    public boolean enterMonsterSence(Monster monster) {

        return getSenecMsgById(monster.getSenceId()).getMonsterList().add(monster);
    }
}
