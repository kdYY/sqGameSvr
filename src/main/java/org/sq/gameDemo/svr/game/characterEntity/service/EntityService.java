package org.sq.gameDemo.svr.game.characterEntity.service;

import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.UserEntityMapper;
import org.sq.gameDemo.svr.game.characterEntity.model.EntityType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class EntityService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private SenceService senceService;
    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private EntityTypeCache entityTypeCache;

    @Autowired
    private RoleAttributeService attributeService;


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
        if(hasUserEntity(userId)) {
            //初始化好玩家并加入场景
            Player player = getInitedPlayer(userId, channel);

            //将角色增加进场景
            senceService.addPlayerInSence(player, channel);

            String lastSence = senceService.getSenceBySenceId(player.getSenceId()).getName();
            builder.setContent(lastSence);
        } else {
            throw new CustomException.BindRoleInSenceException("login game server success, please bind your role，enter \"help\" to get help message");
        }
    }


    //角色创建
    public Player playerCreate(int typeId, Integer userId, Channel channel) throws CustomException.BindRoleInSenceException{
        Player player = playerCache.getPlayerByChannel(channel);
        if(Objects.nonNull(player) && userId.equals(player.getUserId())) {
            //老用户
            throw new CustomException.BindRoleInSenceException("角色只能绑定一次");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(Constant.DefaultPlayerName + userId);
        userEntity.setTypeId(typeId);
        userEntity.setUserId(userId);
        userEntity.setSenceId(defaultSenceId);
        //数据库角色类型数据增加
        addUserEntity(userEntity);
        //返回初始化完毕的玩家
        Player initedPlayer = getInitedPlayer(userId, channel);
        senceService.addPlayerInSence(initedPlayer, channel);
        return initedPlayer;
    }


    public void transformEntityTypeProto(EntityTypeProto.EntityTypeResponseInfo.Builder builder) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (EntityType entityType : entityTypeCache.getAllEntityTypes()) {
            builder.addEntityType(
                    (EntityTypeProto.EntityType) ProtoBufUtil.transformProtoReturnBean(EntityTypeProto.EntityType.newBuilder(), entityType)
            );
        }
    }


    /**
     * 获取缓存中的player，若无，初始化player
     * @param userId
     * @param channel
     * @return
     */
    public Player getInitedPlayer(int userId, Channel channel) {
        //先从缓存中获取，如果没有，再从数据库查询，然后更新缓存
        Player playerCached = playerCache.getPlayerByChannel(channel);
        if(Objects.isNull(playerCached)) {
            UserEntity usrEntity = userEntityMapper.getUserEntityByUserId(userId);
            playerCached = new Player();
            BeanUtils.copyProperties(usrEntity,playerCached);
            //初始化玩家
            initPlayer(playerCached);

            playerCache.putChannelPlayer(channel, playerCached);
            playerCache.savePlayerChannel(playerCached.getId(), channel);
        }
        return playerCached;
    }

    public boolean hasPlayer(Channel channel) {
        return Objects.nonNull(playerCache.getPlayerByChannel(channel));
    }

    public void addUserEntity(UserEntity entity) {
        userEntityMapper.insertSelective(entity);
    }

    public void initPlayer(Player player) {
        //初始化playerId
        player.setId(ConcurrentSnowFlake.getInstance().nextID());
        //初始化状态
        player.setState(1);
        //初始化等级
        player.setLevel(player.getExp()/100);
        //加载指定角色属性
        attributeService.bindRoleAttr(player);
        //计算最终的攻击力
        computeAttack(player);
    }

    private void computeAttack(Player player) {
        Map<Integer, RoleAttribute> roleAttributeMap = player.getRoleAttributeMap();
        // 基础攻击力
        int basicAttack = Optional.ofNullable(roleAttributeMap.get(4).getValue()).orElse(30);
        // 力量
        int power  = Optional.ofNullable(roleAttributeMap.get(5).getValue()).orElse(100);

        long totalAttack = Long.valueOf(basicAttack + power);
        /**
         * AP英雄的增益
         */
        String typeName = "";
        EntityType type = EntityTypeCache.getAllEntityTypes().get(player.getTypeId());
        if(Objects.nonNull(type) && Constant.AP.equals(typeName)) {
            totalAttack +=  Math.round(
                    Optional.ofNullable(roleAttributeMap.get(5).getValue() * Constant.hpAttackIncreaseRate)
                            .orElse(100 * Constant.hpAttackIncreaseRate)
            );
        }
        player.setAttack(totalAttack);
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
    public boolean hasUserEntity(int userId) {
        return Objects.nonNull(userEntityMapper.getUserEntityByUserId(userId));
    }

    //token鉴权
    private String tokenEncryp(int userId) {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(userId) + UUID.randomUUID().toString().replaceAll("-","");
    }

    public static final int defaultSenceId = 1;
}
