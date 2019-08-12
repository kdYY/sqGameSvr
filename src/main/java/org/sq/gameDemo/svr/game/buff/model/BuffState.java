package org.sq.gameDemo.svr.game.buff.model;

import org.sq.gameDemo.svr.game.characterEntity.model.Character;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public enum BuffState {

    /**
     * 1 无特殊效果
     */
    NONE_EFFECT(1, true),

    /**
     * * 2 不断伤害
     */
    CONTINUOUS_INJURY(2, false),

    /**
     *  3 减速
     */
    SLOW_DOWN(3, false),

    /**
     *  4 解除负面效果
     */
    PURIFY(4, true),

    /**
     *  5 眩晕
     */
    DAZE(5, false),
    ;
    private Integer effectState;
    //是否是增益效果
    private Boolean isGain;
    BuffState(Integer effectState, Boolean isGain) {
        this.effectState = effectState;
        this.isGain = isGain;
    }

    public Integer getEffectState() {
        return effectState;
    }

    public Boolean getGain() {
        return isGain;
    }
}
