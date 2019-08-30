package org.sq.gameDemo.svr.game.guild.model;


import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.model.UserEntity;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Data
public class Guild {
    private Integer id;

    private String name;

    private Integer level;

    //创建时间
    private Long time;

    private Integer warehouseSize;

    private Integer memberSize;


    private String memberStr;

    private String warehouseStr;


    private String joinRequestStr;

    private String donateStr;

    // 成员 <unId, 权限>
    private Map<Integer, Integer> memberMap = new ConcurrentHashMap<>();


    // 公会仓库
    private Map<Integer, Item> warehouseMap =  new ConcurrentSkipListMap<>();

    // 捐献列表<UnId, 物品id>
    private Map<Integer, Long> donateMap = new ConcurrentHashMap<>();

    // 请求加入公会的列表
    private Map<Integer, AttendGuildReq> playerJoinRequestMap =  new ConcurrentSkipListMap<>();

    public String getDonateStr() {
        return JsonUtil.getJsonStr(donateMap, donateStr);
    }

    public Map<Integer, Long> getDonateMap() {
        return JsonUtil.getMap(donateMap, donateStr);
    }

    public Map<Integer, Integer> getMemberMap() {
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

    public Guild(String name, Integer chairManUnId, Integer warehouseSize, Integer memberSize) {
        this.name = name;
        this.level = 1;
        this.time = System.currentTimeMillis();
        this.memberMap.put(chairManUnId, GuildAuth.CHAIRMAN.getAuthCode());
        this.warehouseSize = warehouseSize;
        this.memberSize = memberSize;
    }
}
