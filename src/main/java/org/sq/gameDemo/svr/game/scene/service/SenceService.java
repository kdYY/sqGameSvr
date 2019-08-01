package org.sq.gameDemo.svr.game.scene.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.svr.common.*;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceConfig;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SenceService {

    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceEntityCache senceEntityCache;


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
            List<SenceConfig.tmpConf> tmpConfList = JsonUtil.reSerializableJson(config.getJsonStr(), SenceConfig.tmpConf.class);

            ArrayList<Monster> monsterListinSence = new ArrayList<>();
            ArrayList<Npc> npclistinsence = new ArrayList<>();
            for (SenceConfig.tmpConf tmpConf : tmpConfList) {
                SenceEntity senceEntity = senceEntityCache.get((long) tmpConf.getId());
                if(senceEntity.getTypeId().equals(Constant.Monster)) {
                    for (int i = 0; i < tmpConf.getNum(); i++) {
                        Monster monster = new Monster();
                        BeanUtils.copyProperties(senceEntity, monster);
                        monster.setId(ConcurrentSnowFlake.getInstance().nextID());
                        monster.setSenceId(config.getSenceId());
                        monsterListinSence.add(monster);
                    }
                }
                if(senceEntity.getTypeId().equals(Constant.NPC)) {
                    for (int i = 0; i < tmpConf.getNum(); i++) {
                        Npc npc = new Npc();
                        BeanUtils.copyProperties(senceEntity, npc);
                        npc.setId(ConcurrentSnowFlake.getInstance().nextID());
                        npc.setSenceId(config.getSenceId());
                        npclistinsence.add(npc);
                    }
                }
            }
            SenceConfigMsg senceConfigMsg = new SenceConfigMsg();
            senceConfigMsg.setSenceId(config.getSenceId());
            senceConfigMsg.setMonsterList(monsterListinSence);
            senceConfigMsg.setNpcList(npclistinsence);

            senceIdAndSenceMsgMap.put(config.getSenceId(), senceConfigMsg);
        });

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
    public void transformEntityResponseProto(SenceMsgProto.SenceMsgResponseInfo.Builder builder, int senceId) throws Exception{
        SenceConfigMsg findSence = null;
        if((findSence = getSenecMsgById(senceId)) == null) {
            throw new CustomException.NoSuchSenceException("没有此场景");
        }
        ProtoBufUtil.transformProtoReturnBuilder(builder, findSence);
    }

    /**
     * 根据场景id获取场景
     * @param senceId
     * @return
     */
    public GameScene getSenceBySenceId(int senceId) {
        return getSingleByCondition(gameScenes, gameScene -> gameScene.getId() == senceId);
    }

    public List<Integer> getAllSenceId() {
        return gameScenes.stream().map(GameScene::getId).collect(Collectors.toList());
    }



    /**
     *     將玩家角色加入场景，并广播通知
     */
    public void addPlayerInSence(Player player, Channel channel) throws CustomException.BindRoleInSenceException {
        SenceConfigMsg msg = senceIdAndSenceMsgMap.getIfPresent(player.getSenceId());
        Optional.ofNullable(msg).ifPresent(senceConfigMsg -> {
            List<Player> playerList = senceConfigMsg.getPlayerList();
            if(Objects.isNull(playerList)) {
                playerList = new ArrayList<>();
                senceConfigMsg.setPlayerList(playerList);
            }
            playerList.add(player);
            //广播通知
            //增加进channelGroup
            MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
            builder.setContent(player.getName() + "进来了!");
            // TODO 将channel放在player，广播也放在player
            UserCache.addChannelInGroup(player.getSenceId(), channel, builder.build().toByteArray());
        });

    }



    /**
     * /remove掉场景中的player，同时广播通知
     * @param usrId
     * @param channel
     * @throws CustomException.RemoveFailedException
     */
    public Player removePlayerAndGet(Integer usrId, Channel channel) throws CustomException.RemoveFailedException {
        if(!entityService.hasPlayer(channel)) {
            System.out.println("player找不到,出现脏数据");
        }
        Player player = entityService.getInitedPlayer(usrId, channel);
        Optional.ofNullable(senceIdAndSenceMsgMap.getIfPresent(player.getSenceId())).ifPresent(senceConfigMsg->{
            List<Player> playerList = senceConfigMsg.getPlayerList();
            if(!playerList.remove(player)) {
                throw new CustomException.RemoveFailedException("移动失败");
            }
            UserCache.moveChannelInGroup(player.getSenceId(), channel, player.getName() + "离开了!");

        });
        return player;
    }



    public Npc getNpcInSence(Integer senceId, Long npcId) {
        return getSingleByCondition(
                senceIdAndSenceMsgMap.getIfPresent(senceId).getNpcList(),
                o -> o.getId().equals(npcId));
    }

    public Character getMonsterBySenceIdAndId(Integer senceId, Long monsterId) {

        return getSingleByCondition(
                senceIdAndSenceMsgMap.getIfPresent(senceId).getMonsterList(),
                o -> o.getId().equals(monsterId));
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

}
