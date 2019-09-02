package org.sq.gameDemo.svr.game.guild.model;


import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.sq.gameDemo.common.proto.GuildPt;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.PlayerPt;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Guild {
    private Integer id;

    private String name;

    private Integer level;

    //创建时间
    private Long time;

    private Integer warehouseSize;

    private Integer memberSize;

    @ProtoField(Ignore = true)
    private String memberStr;
    @ProtoField(Ignore = true)
    private String warehouseStr;
    @ProtoField(Ignore = true)
    private String joinRequestStr;
    @ProtoField(Ignore = true)
    private String donateStr;



    // 成员 <unId, 成员>
    @ProtoField(TargetName = "member", Function = "addMemberPt", TargetClass = GuildPt.Guild.Builder.class)
    private Map<Integer, Member> memberMap = new ConcurrentSkipListMap<>();

    private void addMemberPt(GuildPt.Guild.Builder builder) throws Exception {
        for (Member member : memberMap.values()) {
            builder.addMember(ProtoBufUtil.transformProtoReturnBuilder(GuildPt.Member.newBuilder(), member));
        }
    }

    // 公会仓库 <itemInfoId, item>
    @ProtoField(TargetName = "wareHouseitem", Function = "addItemPt", TargetClass = GuildPt.Guild.Builder.class)
    private Map<Integer, Item> warehouseMap =  new ConcurrentSkipListMap<>();

    private void addItemPt(GuildPt.Guild.Builder builder) throws Exception {
        for (Item item : warehouseMap.values()) {
            builder.addWareHouseitem(ProtoBufUtil.transformProtoReturnBuilder(ItemPt.Item.newBuilder(), item));
        }
    }

    // 捐献列表<UnId, 贡献值(物品*数量*元宝价值)>
    @ProtoField(TargetName = "donate", Function = "addDonate", TargetClass = GuildPt.Guild.Builder.class)
    private Map<Integer, Donate> donateMap = new ConcurrentSkipListMap<>();


    private void addDonate(GuildPt.Guild.Builder builder) throws Exception {
        for (Donate donate : donateMap.values()) {
            builder.addDonate(ProtoBufUtil.transformProtoReturnBuilder(GuildPt.Donate.newBuilder(), donate));
        }
    }

    // 请求加入公会的列表
    //<unId, attendReq>
    @ProtoField(TargetName = "attendReq", Function = "addAttendReq", TargetClass = GuildPt.Guild.Builder.class)
    private Map<Integer, AttendGuildReq> playerJoinRequestMap =  new ConcurrentSkipListMap<>();

    private void addAttendReq(GuildPt.Guild.Builder builder) throws Exception {
        for (AttendGuildReq attendGuildReq : playerJoinRequestMap.values()) {
            builder.addAttendReq(ProtoBufUtil.transformProtoReturnBuilder(GuildPt.AttendGuildReq.newBuilder(), attendGuildReq));
        }
    }

    public String getDonateStr() {
        return JsonUtil.getJsonStr(donateMap, donateStr);
    }

    public Map<Integer, Donate> getDonateMap() {
        return JsonUtil.getMap(donateMap, donateStr);
    }

    public Map<Integer, Member> getMemberMap() {
       return JsonUtil.getMap(memberMap, memberStr);
    }

    public String getMemberStr() {
        return JsonUtil.getJsonStr(memberMap, memberStr);
    }

    public String getWarehouseStr() {
        return JsonUtil.getJsonStr(warehouseMap, warehouseStr);
    }

    public String getJoinRequestStr() {
        return JsonUtil.getJsonStr(playerJoinRequestMap, joinRequestStr);
    }

    public Map<Integer, Item> getWarehouseMap() {
        return JsonUtil.getMap(warehouseMap, warehouseStr);

    }

    public Map<Integer, AttendGuildReq> getPlayerJoinRequestMap() {
        return JsonUtil.getMap(playerJoinRequestMap, joinRequestStr);
    }


    public Guild() {}

    public Guild(String name, Integer warehouseSize, Integer memberSize) {
        this.name = name;
        this.level = 1;
        this.time = System.currentTimeMillis();
        this.warehouseSize = warehouseSize;
        this.memberSize = memberSize;
    }
}
