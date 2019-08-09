package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

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

    private Long intervalTime;
}
