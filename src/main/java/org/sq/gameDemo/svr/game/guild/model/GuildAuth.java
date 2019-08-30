package org.sq.gameDemo.svr.game.guild.model;

import java.util.LinkedList;
import java.util.Queue;

public enum GuildAuth {

    //会长，副会长，精英，普通会员
    CHAIRMAN(1, "会长", "默认是公会创建者", 1),
    VICE_CHAIRMAN(2, "副会长", "公会的第二把手", 1),
    ELITE(3, "精英", "公会的精英", 5),
    COMMON(4, "普通会员", "公会的成员", 20)

    ;
    private Integer authCode;
    private String name;
    private String describe;
    private Integer limitNum;
    private static Queue<GuildAuth> queue = new LinkedList<>();

    GuildAuth(Integer authCode, String name, String describe, Integer limitNum) {
        this.authCode = authCode;
        this.name = name;
        this.describe = describe;
        this.limitNum = limitNum;
    }

    public Integer getAuthCode() {
        return authCode;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public static Queue<GuildAuth> getQueue() {
        if(queue.size() == 0) {
            queue.add(GuildAuth.CHAIRMAN);
            queue.add(GuildAuth.VICE_CHAIRMAN);
            queue.add(GuildAuth.ELITE);
            queue.add(GuildAuth.COMMON);
        }
        return queue;
    }
}
