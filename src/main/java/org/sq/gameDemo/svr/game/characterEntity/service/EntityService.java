package org.sq.gameDemo.svr.game.characterEntity.service;

import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.dao.SenceEntityCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.copyScene.model.CopyScene;
import org.sq.gameDemo.svr.game.equip.service.EquitService;
import org.sq.gameDemo.svr.game.buff.service.BuffService;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.UserEntityMapper;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.friend.service.FriendService;
import org.sq.gameDemo.svr.game.guild.service.GuildService;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.skills.service.SkillService;
import org.sq.gameDemo.svr.game.task.service.TaskService;
import org.sq.gameDemo.svr.game.transaction.service.OnlineTradeService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EntityService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private  SenceEntityCache senceEntityCache;
    @Autowired
    private UserService userService;
    @Autowired
    private SenceService senceService;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private EntityTypeCache entityTypeCache;
    @Autowired
    private SkillService skillService;
    @Autowired
    private RoleAttributeService attributeService;
    @Autowired
    private BabyService babyService;
    @Autowired
    private BagService bagService;
    @Autowired
    private EquitService equitService;
    @Autowired
    private BuffService buffService;
    @Autowired
    private MailService mailService;
    @Autowired
    private OnlineTradeService onlineTradeService;
    @Autowired
    private GuildService guildService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FriendService friendService;

    /**
     * 用户登录
     * @param channel
     * @param builder
     * @param loginUser
     */
    public void playerLogin(Channel channel, UserProto.ResponseUserInfo.Builder builder, User loginUser) throws Exception {


        if(Objects.isNull(loginUser)) {
            builder.setContent("no this user, please register");
            builder.setResult(404);//用户缺失
            return;
        }
        //同客户端不能登录2个账号
        if(isClientOnline(channel)) {
            builder.setContent("当前客户端已经有用户登录");
            builder.setResult(222);
            return;
        }
        //账号不能同时在线
        if(isPlayerOnline(loginUser)) {
            builder.setContent("当前账号已经有用户登录");
            builder.setResult(222);
            return;
        }

        Integer userId = loginUser.getId();
        //进行token加密
        String token = tokenEncryp(userId);
        //从数据库更新,并更新缓存信息
        userService.updateTokenByUserId(userId, token);
        UserCache.updateUserCache(userId, loginUser, channel);
        //将token信息返回
        builder.setToken(token);

        //如果已经绑定角色的用户，获取上次保存的玩家角色，返回上次所在场景地
        if(hasBindRole(userId)) {
            //初始化好玩家并加入场景
            Player player = getInitedPlayer(userId, channel);

            //将角色增加进场景
            senceService.addPlayerInSence(player);

            String lastSence = senceService.getSenceBySenceId(player.getSenceId()).getName();
            builder.setContent(lastSence);
        } else {
            throw new CustomException.BindRoleInSenceException("login game server success, please bind your role，enter \"help\" to get help message");
        }
    }


    /**
     *     角色创建
     */
    public Player playerCreate(int typeId, Integer userId, Channel channel) throws CustomException.BindRoleInSenceException{
        Player player = playerCache.getPlayerByChannel(channel);
        if(Objects.nonNull(player) && userId.equals(player.getUserId())) {
            //老用户
            throw new CustomException.BindRoleInSenceException("角色只能绑定一次");
        }
        ;
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userService.getUser(userId));
        userEntity.setTypeId(typeId);
        userEntity.setUserId(userId);
        userEntity.setSenceId(Constant.DEFAULT_SENCE_ID);
        //数据库角色类型数据增加
        addUserEntity(userEntity);
        //返回初始化完毕的玩家
        Player initedPlayer = getInitedPlayer(userId, channel);
        senceService.addPlayerInSence(initedPlayer);
        //初始化新手任务
        taskService.getNewPlayerTask(initedPlayer);

        return initedPlayer;
    }

    /**
     * 获取userEntity
     * @param unId
     * @return
     */
    public String getUserEntityGuildStr(Integer unId) {
        return userEntityMapper.getUserEntityGuildStr(unId);
    }

    /**
     * 初始化player
     * @param userId
     * @param channel
     * @return
     */
    public Player getInitedPlayer(int userId, Channel channel) {
        UserEntity usrEntity = userEntityMapper.getUserEntityByUserId(userId);
        Player playerCached = new Player();
        BeanUtils.copyProperties(usrEntity,playerCached);
        //初始化playerId
        playerCached.setId(ConcurrentSnowFlake.getInstance().nextID());
        //初始化玩家
        initPlayer(playerCached);

        playerCache.putChannelPlayer(channel, playerCached);
        playerCache.savePlayerChannel(playerCached.getId(), channel);
        playerCache.putUnIdPlayer(usrEntity.getUnId(), playerCached);

        mailService.loadMail(playerCached);
        onlineTradeService.loadTrace(playerCached);
        guildService.loadGuild(playerCached);
        taskService.loadTaskProgress(playerCached);
        friendService.loadFriend(playerCached);
        return playerCached;
    }

    /**
     * 玩家是否在线
     * @param channel
     * @return
     */
    public boolean hasPlayer(Channel channel) {
        return Objects.nonNull(playerCache.getPlayerByChannel(channel));
    }

    /**
     * 数据库插入用户数据
     * @param entity
     */
    public void addUserEntity(UserEntity entity) {
        userEntityMapper.insertSelective(entity);
    }

    /**
     * 初始化玩家数据
     * @param player
     */
    public void initPlayer(Player player) {

        //初始化状态
        player.setState(CharacterState.LIVE.getCode());
        //初始化等级
        player.setLevel(player.getExp()/100);
        //加载指定角色属性
        attributeService.bindRoleAttr(player);

        bagService.bindBag(player);

        equitService.bindEquip(player);

        skillService.bindSkill(player);

        if(player.getTypeId().equals(JobType.SUMMONER.getType())) {
            babyService.bindBaby(player);
        }
        //计算最终的攻击力
        computeAttack(player);


        buffService.buffAffecting(player, buffService.getBuff(105));
        buffService.buffAffecting(player, buffService.getBuff(106));


    }

    /**
     * 计算玩家战力
     * @param player
     */
    public void computeAttack(Player player) {
        Map<Integer, RoleAttribute> roleAttributeMap = player.getRoleAttributeMap();
        AtomicLong totalAttack = new AtomicLong();
        AtomicLong finalTotalAttack = totalAttack;
        roleAttributeMap.values().forEach(attr -> {
            finalTotalAttack.addAndGet(Optional.ofNullable(attr.getValue()).orElse(30));
        });

        long finalAttack = finalTotalAttack.get() * player.getLevel();
        /**
         * AP英雄的增益
         */
        EntityType type = EntityTypeCache.getAllEntityTypes().get(player.getTypeId());
        if(Objects.nonNull(type) && Constant.AP.equals(type.getTypeName())) {
            finalAttack +=  Math.round(
                    Optional.ofNullable(roleAttributeMap.get(5).getValue() * Constant.hpAttackIncreaseRate)
                            .orElse(100 * Constant.hpAttackIncreaseRate)
            );
        }
        player.setAttack(finalAttack);
    }


    /**
     * 同客户端是否已经登录账号
     * @param channel
     * @return
     */
    private boolean isClientOnline(Channel channel) {
        return Objects.nonNull(playerCache.getPlayerByChannel(channel));
    }

    /**
     * 账号是否已经被登录
     * @param user
     * @return
     */
    private boolean isPlayerOnline(User user) {
        return Objects.nonNull(UserCache.getUserChannelByUserId(user.getId()));
    }

    //注册同时已经bind了的
    public boolean hasBindRole(int userId) {
        return Objects.nonNull(userEntityMapper.getUserEntityByUserId(userId));
    }

    //token鉴权
    private String tokenEncryp(int userId) {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(userId) + UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 将entityType转换为proto
     */
    public void transformEntityTypeProto(EntityTypeProto.EntityTypeResponseInfo.Builder builder) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (EntityType entityType : entityTypeCache.getAllEntityTypes()) {
            builder.addEntityType(
                    (EntityTypeProto.EntityType) ProtoBufUtil.transformProtoReturnBean(EntityTypeProto.EntityType.newBuilder(), entityType)
            );
        }
    }

    /**
     * 判断是否被攻击者杀死
     * @return
     */
    public boolean playerIsDead(Player player, Character attacker) {
        synchronized (player) {
            if(Objects.nonNull(player) && player.getHp() <= 0) {
                player.setDeadStatus();


                if(senceService.getSenecMsgById(player.getSenceId()) instanceof CopyScene) {
                    senceService.notifyPlayerByDefault(player, "你被 id: " + attacker.getId() +
                            ", name:" + attacker.getName() + "杀死了");
                    return true;
                }
                if(attacker != null && attacker instanceof Monster) {
                    ((Monster)attacker).setTarget(null);
                }
                //这里应该做成第一个被杀，通知

                senceService.notifyPlayerByDefault(player, "你被 id: " + attacker.getId() +
                        ", name:" + attacker.getName() + "杀死了, "+ Constant.RELIVE_TIME + "秒后回主城");

                try {
                    ThreadManager.threadPoolSchedule(
                            Constant.RELIVE_TIME, () -> {
                                relivePlayer(player);
                                //添加到起源之地
                                if(!player.getSenceId().equals(Constant.RELIVE_SCENE)) {
                                    try {
                                        senceService.moveToSence(player, 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                senceService.notifyPlayerByDefault(player, "玩家复活，恭喜你复活了");
                            }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            }

            return false;
        }

    }

    //获取角色类型
    public EntityType getType(Integer typeId) {
        return entityTypeCache.get(typeId);
    }

    //获取系统管理员
    public Player getSystemPlayer() {
        return playerCache.getPlayerByUnId(Constant.SYSTEM_UNID);
    }

    //找到玩家
    public Player getPlayer(Channel channel) {
        return playerCache.getPlayerByChannel(channel);
    }
    //找到玩家
    public Player getPlayer(Long playerId) {
        Channel channel = playerCache.getChannelByPlayerId(playerId);
        if(channel == null) {
            return null;
        }
        return playerCache.getPlayerByChannel(channel);
    }
    //找到玩家
    public Player getPlayer(Integer unId) {
        return playerCache.getPlayerByUnId(unId);
    }
    //复活玩家
    public void relivePlayer(Player player) {
        if(player.getB_Hp() > 0 && player.getB_Mp() > 0) {
            player.setHp(player.getB_Hp());
            player.setMp(player.getB_Mp());
        } else {
            senceService.notifyPlayerByDefault(player, "复活失败");
        }

    }

    public UserEntity findUserEntity(Integer unId) {
        return userEntityMapper.selectByPrimaryKey(unId);
    }

    public Integer getUnIdByName(String name) {
        UserEntityExample entityExample = new UserEntityExample();
        entityExample.createCriteria().andNameEqualTo(name);
        List<UserEntity> userEntities = userEntityMapper.selectByExample(entityExample);
        if(userEntities != null && userEntities.size() == 1) {
            return userEntities.get(0).getUnId();
        } else {
            return null;
        }
    }

    //已经进行异步
    public void updateUserEntityGuildStr(int unId, String guildListStr) {
        userEntityMapper.updateGuildStr(unId, guildListStr);
    }

    /**
     * 获取怪物
     */
    public SenceEntity getSenceEntity(Integer id) {
        return senceEntityCache.get((long)id);
    }

    public List<Player> getAllPlayer() {
        return playerCache.getAllPlayerCache().values().stream().collect(Collectors.toList());
    }
}
