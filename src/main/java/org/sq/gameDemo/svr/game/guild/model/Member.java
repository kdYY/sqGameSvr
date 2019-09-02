package org.sq.gameDemo.svr.game.guild.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

@Data
@AllArgsConstructor
public class Member {
    @ProtoField(Ignore = true)
    Integer unId;
    String name;
    Integer level;
    String right;
    @ProtoField(Ignore = true)
    Integer guildAuth;

    public Member() {
    }
}
