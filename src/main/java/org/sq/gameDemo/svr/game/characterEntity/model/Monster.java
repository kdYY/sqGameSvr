package org.sq.gameDemo.svr.game.characterEntity.model;

import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;

/**
 * 野怪类
 */
public class Monster extends SenceEntity implements Character {
    //野怪攻击目标
    @ProtoField(Ignore = true)
    Character attackTarget;

    //野怪攻击速度
    private Integer attackSpeed = 1000;

    public Integer getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(Integer attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setDeadStatus() {
        if(isDead()) {
            this.setTarget(null);
            this.setState(CharacterState.DEADBYATTACT1.getCode());
            this.setDeadTime(System.currentTimeMillis());
        }
    }

    public boolean isDead() {
        return this.getHp() <= 0;
    }


}
