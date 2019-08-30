package org.sq.gameDemo.svr.game.guild.service;


import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.GuildFullEvent;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.guild.dao.GuildCache;
import org.sq.gameDemo.svr.game.guild.dao.GuildMapper;
import org.sq.gameDemo.svr.game.guild.model.AttendGuildReq;
import org.sq.gameDemo.svr.game.guild.model.Guild;
import org.sq.gameDemo.svr.game.guild.model.GuildAuth;
import org.sq.gameDemo.svr.game.guild.model.GuildExample;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GuildService {

    @Autowired
    private GuildCache guildCache;
    @Autowired
    private SenceService senceService;
    @Autowired
    private BagService bagService;
    @Autowired
    private GuildMapper guildMapper;
    @Autowired
    private EntityService entityService;

    /**
     * 创建公会
     */
    public Guild createGuild(Player player, String name) {
        if(isNameExist(name)) {
            senceService.notifyPlayerByDefault(player, "公会 名称已存在");
            return null;
        }
        Item item = bagService.findItem(player, Constant.YUAN_BAO, Constant.GUILD_PRICE);
        if(item == null) {
            senceService.notifyPlayerByDefault(player, "创建公会需要元宝 * 1888");
            return null;
        }
        if(!bagService.removeItem(player, item.getId(), Constant.GUILD_PRICE)) {
            return null;
        }
        Guild guild = new Guild(name, player.getUnId(), Constant.GUILD_DEFAULT_WAREHOUSE_SIZE, Constant.GUILD_DEFAULT_MEMBER_SIZE);
        if(guildMapper.insert(guild) > 0) {
            guildCache.put(guild);
            player.getGuildList().add(guild.getId());
            return guild;
        } else {
            senceService.notifyPlayerByDefault(player, "svr err, 创建公会失败");
            return null;
        }
    }

    /**
     * 公会名是否存在
     */
    private boolean isNameExist(String name) {
        Guild guild = searchGuild(name);
        if(guild != null) {
            return true;
        }
        return false;
    }

    /**
     * 根据姓名寻找公会
     */
    private Guild searchGuild(String name) {
        Optional<Guild> first = guildCache.asMap().values().stream().filter(guild -> guild.getName().equals(name)).findFirst();
        if(first.isPresent()) {
            return first.get();
        }
        return null;
    }

    /**
     * 查询可加入的公会
     * @return
     */
    public List<Guild> findGuild() {
        return guildCache.asMap().values()
                .stream().filter(guild -> guild.getMemberMap().size() < guild.getMemberSize())
                .collect(Collectors.toList());
    }

    /**
     * 玩家发起申请加入公会
     */
    public void attendGuild(Player player, int guildId) {

        Guild guild = guildCache.get(guildId);
        if(guild == null) {
            senceService.notifyPlayerByDefault(player, "无此公会，使用showGuildCanAttend查看可加入的公会列表");
            return;
        }
        //
        if(guild.getPlayerJoinRequestMap().get(player.getUnId()) != null) {
            senceService.notifyPlayerByDefault(player, "已经申请加入此公会，不要重复申请");
            return;
        }
        if(guild.getMemberMap().size() >= guild.getMemberSize()) {
            senceService.notifyPlayerByDefault(player, "公会" + guild.getName() + " 人数已满，使用showGuildCanAttend查看可加入的公会列表");
            EventBus.publish(new GuildFullEvent(guild));
            return;
        }
        guild.getPlayerJoinRequestMap().put(player.getUnId(), new AttendGuildReq(player.getUnId()));
        updateGuild(guild);
        senceService.notifyPlayerByDefault(player, "申请加入" + guild.getName() + " 公会成功，等待同意通过, 申请号为" + player.getUnId());

    }

    /**
     * 更新公会数据
     * @param guild
     */
    private void updateGuild(Guild guild) {
        ThreadManager.dbTaskPool.execute(() -> guildMapper.updateByPrimaryKey(guild));
    }

    /**
     * 获取新成员的权限
     * @param guild
     * @return
     */
    private Integer getNewMemberAuth(Guild guild) {

        ConcurrentMap<Integer, Long> collect = guild.getMemberMap()
                .values().stream()
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));

        Optional<GuildAuth> first = GuildAuth.getQueue().stream().filter(guildAuth -> collect.get(guildAuth.getAuthCode()) < guildAuth.getLimitNum()).findFirst();
        if(first.isPresent()) {
            log.info("queue找新公会会员的权限: auth1=" + first.get().getAuthCode());
        }

        for (GuildAuth guildAuth : GuildAuth.getQueue()) {
            if(collect.get(guildAuth.getAuthCode()) < guildAuth.getLimitNum()) {
                log.info("queue找新公会会员的权限: auth2=" + guildAuth.getAuthCode());
                return guildAuth.getAuthCode();
            }
        }
        return GuildAuth.COMMON.getAuthCode();
    }

    /**
     * 退出公会
     */
    public void exitGuild(Player player, int guildId) {
        if(!player.getGuildList().contains(guildId)) {
            senceService.notifyPlayerByDefault(player, "您没有加入此公会，使用showGuild查看已加入的公会列表");
            return;
        }
        Guild guild = guildCache.get(guildId);
        if(guild == null) {
            senceService.notifyPlayerByDefault(player, "无此公会，使用showGuild查看已加入的公会列表");
            return;
        }
        player.getGuildList().remove(guildId);
        guild.getMemberMap().remove(player.getUnId());
        updateGuild(guild);
        senceService.notifyPlayerByDefault(player, "退出"+ guild.getName() +"公会成功，使用showGuild查看已加入的公会列表");

    }

    /**
     * 加载玩家公会
     * @param playerCached
     */
    public void loadGuild(Player playerCached) {
        if(playerCached.getGuildList().size() == 0 && !Strings.isNullOrEmpty(playerCached.getGuildListStr())) {
            playerCached.setGuildList(JsonUtil.reSerializableJson(playerCached.getGuildListStr(), new TypeReference<List<Integer>>(){}));
            playerCached.getGuildList().stream()
                    .filter(guildId -> guildCache.get(guildId).getMemberMap().get(playerCached.getUnId()).equals(GuildAuth.CHAIRMAN.getAuthCode()))
                    .forEach(guildId -> {
                        senceService.notifyPlayerByDefault(playerCached,  "公会(id=" + guildId + ",name=" + guildCache.get(guildId).getName
                                () +") 有玩家申请入会，使用 showGuildReq id=" + guildId + "查看入会申请");
                        //guildCache.get(guildId).getPlayerJoinRequestMap().values().forEach();
                        //申请号reqId="+  +" 使用agreeEnter "
                    });
        }
    }

    /**
     * showGuildReq 查看指定公会入会申请
     */


    /**
     * 会长同意申请
     */
    public void agreeEnterGuild(Player chairMan, Integer guildId, Integer unId) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(chairMan, guildId, guild)) {
            return;
        }
        if(!guild.getMemberMap().get(chairMan.getUnId()).equals(GuildAuth.CHAIRMAN.getAuthCode())) {
            senceService.notifyPlayerByDefault(chairMan, "您不是此公会的会长，使用showChairManGuild查看有会长权限的公会列表");
            return;
        }


        AttendGuildReq attendGuildReq = guild.getPlayerJoinRequestMap().get(unId);
        if(attendGuildReq == null) {
            senceService.notifyPlayerByDefault(chairMan, "无此申请号，使用 showGuildReq id=" + guildId + "查看入会申请");
            return;
        }
        //同意申请
        //检查可以成为的公会成员权限
        Integer auth = getNewMemberAuth(guild);
        guild.getMemberMap().put(attendGuildReq.getUnId(), auth);
        updateGuild(guild);
        senceService.notifyPlayerByDefault(chairMan, "已同意");

        //获取userEntity 更新在线状态(如果该玩家在线)
        Player player = entityService.getPlayer(attendGuildReq.getUnId());
        if(player == null) {
            ThreadManager.dbTaskPool.execute(() ->{
                String guildStr = entityService.getUserEntityGuildStr(unId);
                List<Integer> guildList = JsonUtil.reSerializableJson(guildStr, new TypeReference<List<Integer>>() {});
                guildList.add(guildId);
                entityService.updateUserEntityGuildStr(unId, JsonUtil.serializableJson(guildList));
            });
        } else {
            player.getGuildList().add(guildId);
            senceService.notifyPlayerByDefault(player, "加入公会成功，使用showGuild查看加入的公会列表");
        }

    }
    /**
     * 捐献公会物品
     */
    public void donateItem(Player donater, Integer guildId, Long ItemId, Integer count) {

        Guild guild = guildCache.get(guildId);
        if(!checkGuild(donater, guildId, guild)) {
            return;
        }

        Item item = bagService.findItem(donater, ItemId, count);
        if(item == null) {
            return;
        }

        //检查公会背包是否已满
        if(guild.getWarehouseMap().size() >= guild.getWarehouseSize()) {
            senceService.notifyPlayerByDefault(donater, "公会背包已满");
            EventBus.publish(new GuildFullEvent(guild));
            return;
        }

        //捐献
        if(bagService.removeItem(donater, ItemId, count)) {
            Item Gitem = guild.getWarehouseMap().get(item.getItemInfo().getId());
            if(Gitem != null) {
                Gitem.setCount(Gitem.getCount() + count);
            } else {
                Item itemWanna = new Item();
                BeanUtils.copyProperties(item, itemWanna);
                itemWanna.setCount(count);
                guild.getWarehouseMap().put(item.getItemInfo().getId(), itemWanna);
            }
            senceService.notifyPlayerByDefault(donater, "捐献成功");

        } else {
            senceService.notifyPlayerByDefault(donater, "捐献失败");
        }
    }

    private boolean checkGuild(Player donater, Integer guildId, Guild guild) {
        if(guild == null) {
            senceService.notifyPlayerByDefault(donater, "无此公会，使用showGuild查看已加入的公会列表");
            return false;
        }
        if(!donater.getGuildList().contains(guildId)) {
            senceService.notifyPlayerByDefault(donater, "您没有加入此公会，使用showGuild查看已加入的公会列表");
            return false;
        }
        return true;
    }


    /**
     * 获取公会物品
     */



    /**
     * 升级公会 增加人数限制， 物品限制 提升等级
     */


    /**
     * 使用showGuild查看已加入的公会列表
     */
    public List<Guild> showGuild(Player player) {
        List<Guild> guilds = new ArrayList<>();
        for (Integer guildId : player.getGuildList()) {
            guilds.add(guildCache.get(guildId));
        }
        return guilds;
    }

}
