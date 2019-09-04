package org.sq.gameDemo.svr.game.guild.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

@Data
public class Member {
    @ProtoField(Ignore = true)
    Integer unId;
    String name;
    Integer level;
    String right;
    @ProtoField(Ignore = true)
    Integer guildAuth;
    //职位是否发生变化
    @ProtoField(Ignore = true)
    boolean change = false;

    public Member() {
    }

    public Member(Integer unId, String name, Integer level, String right, Integer guildAuth) {
        this.unId = unId;
        this.name = name;
        this.level = level;
        this.right = right;
        this.guildAuth = guildAuth;
    }
}
