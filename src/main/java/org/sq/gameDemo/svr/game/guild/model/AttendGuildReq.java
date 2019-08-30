package org.sq.gameDemo.svr.game.guild.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.ConcurrentSnowFlake;

@Data
public class AttendGuildReq {
    private Long requestTime;
    private Integer unId;
    private boolean agree;

    public AttendGuildReq(Integer unId) {
        this.unId = unId;
        this.requestTime = System.currentTimeMillis();
        agree = false;
    }
}
