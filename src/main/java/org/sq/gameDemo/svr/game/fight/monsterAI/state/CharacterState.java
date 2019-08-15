package org.sq.gameDemo.svr.game.fight.monsterAI.state;

/**
 * 怪物状态枚举
 */
public enum CharacterState {
    LIVE(1, "存活"),
    ATTACKING(2, "受到攻击"),
    IS_REFRESH(3, "死亡，正在刷新"),
    COPY_DEAD(3, "副本怪物死亡状态")
    ;
    private Integer code;
    private String descr;

    CharacterState(Integer code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    public static CharacterState getStateByCode(Integer state) {
        for (CharacterState characterState : CharacterState.values()) {
            if(characterState.getCode().equals(state)) {
                return characterState;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
