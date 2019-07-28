package org.sq.gameDemo.svr.game.skills.model;

import java.util.Arrays;

/**
 * 技能作用范围
 */
public enum SkillRange {


    /**
     * 作用在自身
     */
    Self(1),


    /**
     * 作用在单个友方和自身
     */
    SingleFriendAndSelf(2),


    /**
     * 作用在多个友方和自身
     */
    FriendsAndSelf(3),


    /**
     * 作用在单个敌方
     */
    SingleEnemy(4),


    /**
     * 作用在群体敌方
     */
    Enemys(5),


    ;
    int rangeId;

    SkillRange(int typeId) {
        this.rangeId = typeId;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public static SkillRange getSkillRangeByRangeId(int typeId) {
       return Arrays.stream(SkillRange.values()).filter(t -> t.getRangeId() == typeId).findFirst().orElse(null);
    }
}
