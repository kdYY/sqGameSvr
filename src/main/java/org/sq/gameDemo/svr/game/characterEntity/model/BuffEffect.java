package org.sq.gameDemo.svr.game.characterEntity.model;

public enum BuffEffect {

    /**
     * 1 无特殊效果
     */
    NONE_EFFECT(1),

    /**
     * * 2 不断伤害
     */
    CONTINUOUS_INJURY(2),

    /**
     *  3 减速
     */
    SLOW_DOWN(3),

    /**
     *  4 解除负面效果
     */
    PURIFY(4),

    /**
     *  5 眩晕
     */
    DAZE(5),
    ;
    private Integer effectType;

    BuffEffect(Integer effectType) {
        this.effectType = effectType;
    }

    public Integer getEffectType() {
        return effectType;
    }
}
