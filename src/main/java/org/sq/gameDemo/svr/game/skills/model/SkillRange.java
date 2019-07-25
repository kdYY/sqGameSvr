package org.sq.gameDemo.svr.game.skills.model;

/**
 * 技能作用范围
 */
public enum SkillRange {


    /**
     * 作用在自身
     */
    SELF(1),


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
    int typeId;

    SkillRange(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
