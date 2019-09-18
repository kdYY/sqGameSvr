package org.sq.gameDemo.svr.game.guild.service;


import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.GuildPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.Ref;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.GuildFullEvent;
import org.sq.gameDemo.svr.eventManage.event.PlayerAddGuildEvent;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemType;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.guild.dao.GuildCache;
import org.sq.gameDemo.svr.game.guild.dao.GuildMapper;
import org.sq.gameDemo.svr.game.guild.model.*;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.updateDB.UpdateDB;

import java.sql.SQLOutput;
import java.util.*;
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
    @Autowired
    private MailService mailService;

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
        Guild guild = new Guild(name, Constant.GUILD_DEFAULT_WAREHOUSE_SIZE, Constant.GUILD_DEFAULT_MEMBER_SIZE);
        if(guildMapper.insert(guild) > 0) {
            guildCache.put(guild);
            player.getGuildList().add(guild.getId());
            guild.getMemberMap().put(player.getUnId(),
                    new Member(player.getUnId(), player.getName(), player.getLevel(), GuildAuth.CHAIRMAN.getName(), GuildAuth.CHAIRMAN.getAuthCode()));
            updateGuild(guild);
            senceService.notifyPlayerByDefault(player, "创建公会成功，可以使用showGuild id=" + guild.getId() + "查看公会哦");
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
    public List<Guild> findGuild(Player player) {
        return guildCache.asMap().values()
                .stream()
                .filter(guild -> guild.getMemberMap().size() < guild.getMemberSize())
                .filter(guild -> !player.getGuildList().contains(guild.getId()))
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
        if(player.getGuildList().contains(guildId)) {
            senceService.notifyPlayerByDefault(player, "你已经在此公会中，使用showGuildCanAttend查看可加入的公会列表");
            return;
        }
        AttendGuildReq attendGuildReq = guild.getPlayerJoinRequestMap().get(player.getUnId());
        long now = System.currentTimeMillis();
        if(attendGuildReq != null && now - attendGuildReq.getRequestTime() < 5000) {
            senceService.notifyPlayerByDefault(player, "已经申请加入此公会，不要频繁重复申请");
            return;
        }
        if(guild.getMemberMap().size() >= guild.getMemberSize()) {
            senceService.notifyPlayerByDefault(player, "公会" + guild.getName() + " 人数已满，使用showGuildCanAttend查看可加入的公会列表");
            EventBus.publish(new GuildFullEvent(guild));
            return;
        }
        if(attendGuildReq == null) {
            guild.getPlayerJoinRequestMap().put(player.getUnId(), new AttendGuildReq(player.getUnId(), player.getName()));
        } else {
            attendGuildReq.setRequestTime(now);
        }
        updateGuild(guild);
        senceService.notifyPlayerByDefault(player, "申请加入" + guild.getName() + " 公会成功，等待同意通过, 申请号为" + player.getUnId());
        guild.getMemberMap().entrySet().stream()
                .filter(entry -> entry.getValue().getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode()))
                .findFirst()
                .ifPresent(entry -> {
                    Player chairMan = entityService.getPlayer(entry.getKey());
                    if(chairMan != null) {
                        senceService.notifyPlayerByDefault(chairMan, "公会(id=" + guild.getId() + ",name=" + guild.getName
                                () +") 有玩家申请入会，使用 showGuildReq id=" + guild.getId() + "查看入会申请");
                    }
                });
        EventBus.publish(new PlayerAddGuildEvent(player));
    }

    /**
     * 更新公会数据
     * @param guild
     */
    private void updateGuild(Guild guild) {
        //ThreadManager.dbTaskPool.execute(() -> guildMapper.updateByPrimaryKey(guild));
    }

    public synchronized void updateGuildDB() {
        for (Guild guild : guildCache.asMap().values()) {
            guild.setMemberStr(JsonUtil.getJsonStr(guild.getMemberMap()));
            guild.setDonateStr(JsonUtil.getJsonStr(guild.getDonateMap()));
            guild.setWarehouseStr(JsonUtil.getJsonStr(guild.getWarehouseMap()));
            guild.setJoinRequestStr(JsonUtil.getJsonStr(guild.getPlayerJoinRequestMap()));
            guildMapper.updateByPrimaryKeySelective(guild);
        }
    }
    /**
     * 获取新成员的权限
     * @param guild
     * @return
     */
    private GuildAuth getNewMemberAuth(Guild guild) {

        //获取每个权限下分布的人数
        ConcurrentMap<Integer, Long> collect = guild.getMemberMap()
                .values().stream()
                .map(Member::getGuildAuth)
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));

//        Optional<GuildAuth> first = GuildAuth.getQueue().stream()
//                .filter(guildAuth -> collect.get(guildAuth.getAuthCode()) != null)
//                .filter(guildAuth -> collect.get(guildAuth.getAuthCode()) < guildAuth.getLimitNum())
//                .findFirst();
//
//        if(first.isPresent()) {
//            log.info("queue找新公会会员的权限: auth1=" + first.get().getAuthCode());
//        }

        for (GuildAuth guildAuth : GuildAuth.getQueue()) {
            if(collect.get(guildAuth.getAuthCode()) == null) {
                return guildAuth;
            } else {
                if(collect.get(guildAuth.getAuthCode()) < guildAuth.getLimitNum()) {
                    log.info("queue找新公会会员的权限: auth2=" + guildAuth.getAuthCode());
                    return guildAuth;
                }
            }
        }
        return GuildAuth.COMMON;
    }

    /**
     * 退出公会
     */
    public void exitGuild(Player player, Integer guildId) {
        List<Integer> guildList = player.getGuildList();
        if(!guildList.contains(guildId)) {
            senceService.notifyPlayerByDefault(player, "您没有加入此公会，使用showGuildList查看已加入的公会列表");
            return;
        }
        Guild guild = guildCache.get(guildId);
        if(guild == null) {
            senceService.notifyPlayerByDefault(player, "无此公会，使用showGuildList查看已加入的公会列表");
            return;
        }
        guildList.remove(guildId);
        if(guildList.contains(guildId)) {
            System.out.println("0000");
        }
        Member member = guild.getMemberMap().get(player.getUnId());
        guild.getMemberMap().remove(player.getUnId());
        //职位禅让制
        if(guild.getMemberMap().size() == 0) {
            //删除公会
            guildCache.remove(guild);
            deleteGuild(guild);
        } else {
            tidyMemberRight(guild, member);
            updateGuild(guild);
        }
        senceService.notifyPlayerByDefault(player, "退出"+ guild.getName() +"公会成功，使用showGuildList查看已加入的公会列表");

    }

    /**
     * 更正公会职位分布
     * @param guild
     * @param removeMan
     */
    private void tidyMemberRight(Guild guild, Member removeMan) {

        if(removeMan.getGuildAuth().equals(GuildAuth.COMMON.getAuthCode())) {
            return;
        }

        boolean next = false;
        int tidyAuthCode = 0;
        for (GuildAuth guildAuth : GuildAuth.getQueue()) {
            if(removeMan.getGuildAuth().equals(guildAuth.getAuthCode())) {
                next = true;
            } else {
                if(next) {
                    tidyAuthCode = guildAuth.getAuthCode();
                    break;
                }
            }
        }
        int finalTidyAuthCode = tidyAuthCode;
        //根据贡献最大升职位规则
        Optional<Member> max = guild.getMemberMap().values()
                .stream()
                .filter(member -> member.getGuildAuth().equals(finalTidyAuthCode) && guild.getDonateMap().get(member.getUnId()) != null)
                .max(Comparator.comparingLong(member -> guild.getDonateMap().get(member.getUnId()).getDonateNum()));

        if(!max.isPresent()) {
            max = guild.getMemberMap().values()
                    .stream()
                    .filter(member -> member.getGuildAuth().equals(finalTidyAuthCode))
                    .findAny();
        }

        if(max.isPresent()) {
            Member member = max.get();
            member.setGuildAuth(removeMan.getGuildAuth());
            member.setRight(removeMan.getRight());
            Player player = entityService.getPlayer(member.getUnId());
            if(player != null) {
                senceService.notifyPlayerByDefault(player,  "公会(id=" + guild.getId() + ",name="
                        + guild.getName() +") 根据公会禅让制， 你职位变为" + guild.getMemberMap().get(player.getUnId()).getRight());
            } else {
                member.setChange(true);
            }
        }


    }

    private void deleteGuild(Guild guild) {
        guildMapper.deleteByPrimaryKey(guild.getId());
    }

    /**
     * 加载玩家公会
     * @param playerCached
     */
    public void loadGuild(Player playerCached) {
        List<Integer> list = playerCached.getGuildList();
        if(list.size() == 0 && !Strings.isNullOrEmpty(playerCached.getGuildListStr())) {
            list.addAll(JsonUtil.reSerializableJson(playerCached.getGuildListStr(), new TypeReference<List<Integer>>(){}));
            List<Guild> guildList = list.stream().map(id -> guildCache.get(id)).collect(Collectors.toList());

            guildList.stream()
                    .filter(guild -> guild.getMemberMap().get(playerCached.getUnId()).getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode()))
                    .filter(guild -> guild.getPlayerJoinRequestMap().size() > 0)
                    .forEach(guild ->
                        senceService.notifyPlayerByDefault(playerCached,  "公会(id=" + guild.getId() + ",name=" + guild.getName()
                                +") 有玩家申请入会，使用 showGuildReq id=" + guild.getId() + "查看入会申请")
                    );

            guildList.stream()
                    .filter(guild -> guild.getMemberMap().get(playerCached.getUnId()).isChange())
                    .forEach(guild -> senceService.notifyPlayerByDefault(playerCached,  "公会(id=" + guild.getId() + ",name="
                            + guild.getName() +") 根据公会禅让制， 你职位变为" + guild.getMemberMap().get(playerCached.getUnId()).getRight()));
        }
    }

    /**
     * showGuildReq 查看指定公会入会申请
     */
    public List<AttendGuildReq> showGuildReq(Player player, Integer guildId) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(player, guildId, guild)) {
            return null;
        }
        if(!guild.getMemberMap().get(player.getUnId()).getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode())) {
            senceService.notifyPlayerByDefault(player, "您不是此公会的会长，没有权限查看申请，使用showChairManGuild查看有会长权限的公会列表");
            return null;
        }

        return  guild.getPlayerJoinRequestMap().values().stream().sorted(Comparator.comparingLong(AttendGuildReq::getRequestTime).reversed()).collect(Collectors.toList());
    }


    /**
     * 会长同意申请
     */
    public void agreeEnterGuild(Player chairMan, Integer guildId, Integer unId, boolean agree) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(chairMan, guildId, guild)) {
            return;
        }
        if(!guild.getMemberMap().get(chairMan.getUnId()).getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode())) {
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
        GuildAuth auth = getNewMemberAuth(guild);
        //获取userEntity 更新在线状态(如果该玩家在线)
        Player player = entityService.getPlayer(attendGuildReq.getUnId());
        if(agree) {
            UserEntity un = null;
            if(player == null) {
                un = entityService.findUserEntity(unId);
                guild.getMemberMap().put(attendGuildReq.getUnId(),
                        new Member(un.getUnId(), un.getName(), un.getExp() / 100, auth.getName(), auth.getAuthCode()));
                updatePlayerGuild(guildId, unId);
            } else{
                player.getGuildList().add(guildId);
                guild.getMemberMap().put(attendGuildReq.getUnId(),
                        new Member(player.getUnId(), player.getName(), player.getLevel(), auth.getName(), auth.getAuthCode()));
                senceService.notifyPlayerByDefault(player, "加入公会成功，使用showGuildList查看加入的公会列表");
            }

        } else {
            senceService.notifyPlayerByDefault(player, "公会(id=" + guild.getId() + ",name=" + guild.getName
                    () +") 拒绝你的入会申请");
        }


        guild.getPlayerJoinRequestMap().remove(unId);
        updateGuild(guild);
        senceService.notifyPlayerByDefault(chairMan, "已处理");

    }

    /**
     * 更新用户数据
     * @param guildId
     * @param unId
     */
    private void updatePlayerGuild(Integer guildId, Integer unId) {
        UpdateDB.dbTaskPool.execute(() ->{
            String guildStr = entityService.getUserEntityGuildStr(unId);
            List<Integer> guildList = JsonUtil.reSerializableJson(guildStr, new TypeReference<List<Integer>>() {});
            guildList.add(guildId);
            entityService.updateUserEntityGuildStr(unId, JsonUtil.serializableJson(guildList));
        });
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
        Integer wareHouseSize = getCurrentWareHouseSize(guild);
        if(wareHouseSize >= guild.getWarehouseSize()) {
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

            Donate donate = guild.getDonateMap().get(donater.getUnId());
            if(donate != null) {
                donate.setDonateNum(donate.getDonateNum() + item.getItemInfo().getPrice() * count);
            } else {
                donate = new Donate(donater.getUnId(), donater.getName(), Long.valueOf(item.getItemInfo().getPrice() * count));
                guild.getDonateMap().put(donater.getUnId(), donate);
            }
            senceService.notifyPlayerByDefault(donater, "捐献成功");
            updateGuild(guild);
        } else {
            senceService.notifyPlayerByDefault(donater, "捐献失败");
        }
    }

    /**
     * 计算目前背包容量
     */
    private Integer getCurrentWareHouseSize(Guild guild) {
        ConcurrentMap<Integer, Integer> collect = guild.getWarehouseMap().values()
                .stream()
                .filter(item -> !item.getItemInfo().getId().equals(Constant.YUAN_BAO))
                .collect(Collectors.groupingByConcurrent(bagItem -> bagItem.getItemInfo().getType(), Collectors.summingInt(Item::getCount)));
        int result = 0;

        for (Integer type : collect.keySet()) {
            if(type.equals(ItemType.CAN_BE_STACKED.getType())) {
                result += (collect.get(type) > 100 ? (collect.get(type)/100) : 1);
            } else {
                result += collect.get(type);
            }
        }

        return result;
    }

    /**
     * 检查公会
     */
    private boolean checkGuild(Player donater, Integer guildId, Guild guild) {
        if(guild == null) {
            senceService.notifyPlayerByDefault(donater, "无此公会，使用showGuildList查看已加入的公会列表");
            return false;
        }
        if(!donater.getGuildList().contains(guildId)) {
            senceService.notifyPlayerByDefault(donater, "您没有加入此公会，使用showGuildList查看已加入的公会列表");
            return false;
        }
        return true;
    }


    /**
     * 获取公会物品
     */
    public void getGuildItem(Player player, Integer guildId, Integer itemInfoId, Integer count) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(player, guildId, guild)) {
            return;
        }

        if(!guild.getMemberMap().get(player.getUnId()).getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode())) {
            senceService.notifyPlayerByDefault(player, "您不是此公会的会长，没有权限查看申请，使用showChairManGuild查看有会长权限的公会列表");
            return;
        }

        Ref<Item> itemRef = new Ref<>();
        if(!checkGuildWareHouse(player, guild, itemInfoId, count, itemRef)) {
            return;
        }

        //发邮件
        Item item = new Item();
        BeanUtils.copyProperties(itemRef.ref, item);
        if(itemRef.ref.getCount().equals(count)) {
            guild.getWarehouseMap().remove(itemRef.ref.getItemInfo().getId(), itemRef.ref);
        } else {
            itemRef.ref.setCount(itemRef.ref.getCount() - count);
        }
        item.setCount(count);

        mailService.sendMail(entityService.getSystemPlayer(), player.getUnId(),"公会物品获取", "这是您在公会获取的物品", item);
        updateGuild(guild);
    }

    /**
     * 检查公会背包
     */
    private boolean checkGuildWareHouse(Player player, Guild guild, Integer itemInfoId, Integer count, Ref<Item> refItem) {
        Map<Integer, Item> warehouseMap = guild.getWarehouseMap();
        Item item;
        if((item = warehouseMap.get(itemInfoId)) == null) {
            senceService.notifyPlayerByDefault(player, "无此物品，请检查itemInfoId");
            return false;
        }

        if(item.getCount()  < count) {
            senceService.notifyPlayerByDefault(player, "物品数量不足");
            return false;
        }

        refItem.ref = item;
        return true;
    }


    /**
     * 升级公会 增加人数限制， 物品限制 提升等级
     */


    /**
     * 使用showGuildList查看已加入的公会列表
     */
    public List<Guild> showGuildList(Player player) {
        List<Guild> guilds = new ArrayList<>();
        for (Integer guildId : player.getGuildList()) {
            guilds.add(guildCache.get(guildId));
        }
        return guilds;
    }


    /**
     * 使用showChairManGuildList查看已加入的公会列表
     */
    public List<Guild> showChairManGuildList(Player player) {
        List<Guild> guilds = new ArrayList<>();
        for (Integer guildId : player.getGuildList()) {
            Guild guild = guildCache.get(guildId);
            if(guild.getMemberMap().get(player.getUnId()).getGuildAuth().equals(GuildAuth.CHAIRMAN.getAuthCode())) {
                guilds.add(guild);
            }
        }
        return guilds;
    }


    public Guild showGuild(Player player, Integer guildId) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(player, guildId, guild)) {
            return null;
        }
        return guild;
    }


    public List<Item> showGuildBag(Player player, Integer guildId) {
        Guild guild = guildCache.get(guildId);
        if(!checkGuild(player, guildId, guild)) {
            return null;
        }
        return guild.getWarehouseMap().values().stream().collect(Collectors.toList());

    }
}
