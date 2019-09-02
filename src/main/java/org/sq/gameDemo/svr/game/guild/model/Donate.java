package org.sq.gameDemo.svr.game.guild.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Donate {
    private Integer unId;
    private String name;
    private Long donateNum;

    public Donate() {}
}
