package org.sq.gameDemo.svr.game.fight.monsterAI.state;

/**
 * 怪物状态枚举
 */
public enum CharacterState {

    MOVE1(1, "怪物在区域内漫无目的移动..."),
    ATTACK1(2, "攻击"),
    DEADBYATTACT1(3, "被打死"),
    IS_REFRESH1(4, "死亡，正在刷新")
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
