package org.sq.gameDemo.svr.game.guild.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

@Data
@AllArgsConstructor
public class Donate {
    @ProtoField(Ignore = true)
    private Integer unId;
    private String name;
    private Long donateNum;

    public Donate() {}
}
