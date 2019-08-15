package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.common.proto.BuffPt;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 野怪类
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Monster extends SenceEntity implements Character {
    Long entityTypeId;

    //野怪攻击速度
    private Integer attackSpeed = 1000;

    /**
     * 设置怪物死亡状态
     */
    public void setDeadStatus() {
        if(isDead()) {
            this.setTarget(null);
            if(this.getRefreshTime() <= 0) {
                this.setState(CharacterState.COPY_DEAD.getCode());
            } else {
                this.setState(CharacterState.IS_REFRESH.getCode());
                this.setDeadTime(System.currentTimeMillis());
            }
        }
    }

    public boolean isDead() {
        return this.getHp() <= 0;
    }

    /**
     * 仇恨时间
     */
    //private Long hatredTime;

    @ProtoField(TargetClass = BuffPt.Buff.class, TargetName = "buff")
    private List<Buff> bufferList = new CopyOnWriteArrayList<>();

}
