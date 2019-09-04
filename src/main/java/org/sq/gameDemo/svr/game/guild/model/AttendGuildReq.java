package org.sq.gameDemo.svr.game.guild.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;

@Data
public class AttendGuildReq {
    private Long requestTime;
    private Integer unId;
    private boolean agree;
    private String name;

    public boolean isAgree() {
        return agree;
    }
    public boolean getAgree() {
        return agree;
    }


    public AttendGuildReq(){}
    public AttendGuildReq(Integer unId, String name) {
        this.name = name;
        this.unId = unId;
        this.requestTime = System.currentTimeMillis();
        agree = false;
    }
}
