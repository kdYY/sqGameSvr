package org.sq.gameDemo.svr.game.characterEntity.service;

import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.*;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.PlayerCache;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.common.UserCache;
import org.sq.gameDemo.svr.common.customException.customException;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.characterEntity.dao.UserEntityMapper;
import org.sq.gameDemo.svr.game.characterEntity.model.EntityType;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntityExample;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.roleAttribute.service.RoleAttributeService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.user.model.User;
import org.sq.gameDemo.svr.game.user.service.UserService;

import javax.annotation.PostConstruct;
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
    private RoleAttributeService attributeService;

    @Value("${excel.entity}")
    private String entityFileName;

    //存储所有的实体信息
    private List<EntityType> entitieTypes;

    @PostConstruct
    public void init() {
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


    //获取数据库中UserEntity实体
    public List<UserEntity> getUserEntityList() {
        return userEntityMapper.selectByExample(new UserEntityExample());
    }


    /**
     * 获取场景中初始化完毕的player
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
            //将角色增加进场景
            senceService.addPlayerInSence(playerCached, channel);

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
        //初始化场景id
        player.setSenceId(defaultSenceId);
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
        if(Constant.AP.equals(entitieTypes.get(player.getTypeId()).getTypeName())) {
           totalAttack +=  Math.round(
                   Optional.ofNullable(roleAttributeMap.get(5).getValue() * Constant.hpAttackIncreaseRate)
                           .orElse(100 * Constant.hpAttackIncreaseRate)
                    );
        }
        player.setAttack(totalAttack);
    }

    /**
     * 用户登录
     * @param channel
     * @param builder
     * @param user
     */
    public void playerLogin(Channel channel, UserProto.ResponseUserInfo.Builder builder, UserProto.User user) {
        User loginUser = userService.getUser(user);

        if(Objects.isNull(loginUser)) {
            builder.setContent("no this user, please register");
            builder.setResult(404);//用户缺失
            return;
        }

        Integer userId = loginUser.getId();
        //进行token加密
        String token = tokenEncryp(userId);
        //从数据库更新
        userService.updateTokenByUserId(userId, token);
        //更新缓存信息
        UserCache.updateUserToken(userId, token);
        //将token信息返回
        builder.setToken(token);
        //更新channel
        UserCache.updateChannelCache(channel, userId);

        //如果已经绑定角色的用户，获取上次保存的玩家角色，返回上次所在场景地
        if(hasPlayer(channel)) {
            //进行广播
            Player player = getInitedPlayer(userId, channel);
            MessageProto.Msg.Builder msgbuilder = MessageProto.Msg.newBuilder();
            msgbuilder.setContent(player.getName() + "已经上线!");
            UserCache.addChannelInGroup(player.getSenceId(), channel, msgbuilder.build().toByteArray());

            String lastSence = senceService.getSenceBySenceId(player.getSenceId()).getName();
            builder.setContent(lastSence);
        } else {
            throw new customException.BindRoleInSenceException("login game server success, please bind your role，enter \"help\" to get help message");
        }
    }

    //token鉴权
    private String tokenEncryp(int userId) {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(userId) + UUID.randomUUID().toString().replaceAll("-","");
    }

    public static final int defaultSenceId = 1;

    //角色创建
    public Player playerCreate(int typeId, Integer userId, Channel channel) throws customException.BindRoleInSenceException{
        Player player = playerCache.getPlayerByChannel(channel);
        if(Objects.nonNull(player) || userId.equals(player.getUserId())) {
            //老用户
            throw new customException.BindRoleInSenceException("角色只能绑定一次");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(Constant.DefaultPlayerName + userId);
        userEntity.setTypeId(typeId);
        userEntity.setUserId(userId);
        userEntity.setSenceId(defaultSenceId);
        //数据库角色类型数据增加
        addUserEntity(userEntity);
        //返回初始化完毕的玩家
        return getInitedPlayer(userId, channel);
    }

    public void playerOnline(Player player, Channel channel) {
        MessageProto.Msg.Builder msgbuilder = MessageProto.Msg.newBuilder();
        msgbuilder.setContent(player.getName() + "已经上线！");
        UserCache.addChannelInGroup(player.getSenceId(), channel, msgbuilder.build().toByteArray());
    }
}
