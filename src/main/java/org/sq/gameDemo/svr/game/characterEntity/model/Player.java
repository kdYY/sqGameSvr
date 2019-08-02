package org.sq.gameDemo.svr.game.characterEntity.model;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.TimedTaskManager;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 玩家类
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Player extends UserEntity implements Character {


    /**
     *  等级，根据经验计算得出
     */
    private Integer level;

    /**
     * 根据配置进行计算
     */
    private Long hp;
    private Long mp;

    /**
     *  玩家战力,根据base技能属性进行计算
     */
    private Long attack;

    @ProtoField(Ignore = true)
    private Map<Integer,RoleAttribute> roleAttributeMap = new ConcurrentHashMap<>();

    /**
     *  经验增加, 影响等级 需要加锁，场景线程在读取经验， 同时事件总线线程可能在增加经验
     * @param exp 经验
     */
    public synchronized void addExp(Integer exp) {
        this.setExp(this.getExp() + exp);

        int newLevel = this.getExp() / Constant.LEVEL_DIVISOR;

        // 如果等级发生变化，进行提示
        if (newLevel != this.getLevel()) {
            EventBus.publish(new LevelEvent(this, newLevel));
            this.setLevel(newLevel);
        }
    }


}
