package org.sq.gameDemo.svr.game.buff.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;

import java.util.concurrent.Future;

/**
 * Buff类
 */
@Data
public class Buff {

    private Integer id;

    private String name;

    private Integer type;

    private Long cd;

    private Long mp;

    private Long hp;

    /**
     * 1 无特殊效果
     * 2 不断伤害
     * 3 减速
     * 4 解除负面效果
     * 5 眩晕
     */
    private Integer effect;


    private Long duration;

    /**
     * 记录buff开启时间
     */
    private Long startTime;

    /**
     * 作用时间间隔
     */
    private Long intervalTime;

    //记录buff作用者
    @ProtoField(Ignore = true)
    private Character character;
    /**
     * 记录buff的源头
     */
    @ProtoField(Ignore = true)
    private Future future;


    public void setFuture(Future future) {
        this.future = future;
    }

    @Override
    public String toString() {
        return "Buff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", cd=" + cd +
                ", mp=" + mp +
                ", hp=" + hp +
                ", effect=" + effect +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", intervalTime=" + intervalTime +
                ", character=" + character.getName() +
                '}';
    }
}
