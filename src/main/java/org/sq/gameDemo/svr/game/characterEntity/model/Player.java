package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.observer.ObserverController;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.friend.model.Friend;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;
import org.sq.gameDemo.svr.game.task.model.TaskProgress;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 玩家类
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Player extends UserEntity implements Character {

    private ObserverController observerController = new ObserverController(this);

    private Long id;
    /**
     *  等级，根据经验计算得出
     */
    private Integer level;


    /**
     * 根据配置进行计算,同时hp mp等级变动影响
     */
    private Long hp;
    private Long mp;

    private Long B_Hp = 0L;
    private Long B_Mp = 0L;

    /**
     * 设置hp的波动范围
     * @param hp
     */
    public synchronized void setHp(Long hp) {

        if(hp > this.getB_Hp() ) {
            hp = this.getB_Hp();
        }
        if(hp < 0) {
            hp = 0L;
        }
        this.hp = hp;

    }

    /**
     * 设置mp的波动范围
     * @param mp
     */
    public synchronized void setMp(Long mp) {
        if(mp > this.getB_Mp()) {
            mp = this.getB_Mp();
        }
        if(mp < 0) {
            mp = 0L;
        }
        this.mp = mp;
    }

    @Override
    public void onDie(Character lastAttacker) {
        observerController.onDie(lastAttacker);
    }

    /**
     *  玩家战力,根据base技能属性进行计算
     */
    private Long attack;

    @ProtoField(Ignore = true)
    private Map<Integer,RoleAttribute> roleAttributeMap = new ConcurrentHashMap<>();

    /**
     * 经验增加, 影响等级 需要加锁，场景线程在读取经验， 同时事件总线线程可能在增加经验
     * @param exp 经验
     */
    public synchronized void addExp(Integer exp) {
        this.setExp(this.getExp() + exp);

        int newLevel = this.getExp() / Constant.LEVEL_DIVISOR;

        // 如果等级发生变化，进行提示
        if (newLevel != this.getLevel()) {
            EventBus.publish(new LevelEvent(this, newLevel));
        }
    }


    /**
     * 玩家身上的buff
     */
    //@ProtoField(TargetClass = BuffPt.Buff.class, TargetName = "buff")
    @ProtoField(Ignore = true)
    private List<Buff> bufferList = new CopyOnWriteArrayList<>();


    // 装备<部位，装备>
    @ProtoField(Ignore = true)
    private Map<Integer, Item> equipmentBar = new ConcurrentHashMap<>();


    // 背包栏 默认大小为16
    @ProtoField(Ignore = true)
    private Bag bag = new Bag(this.getUnId(), "背包栏",100) ;


    public void setDeadStatus(Character lastAttacker) {
        setState(CharacterState.IS_REFRESH.getCode());
        setHp(0L);
        setMp(0L);
        onDie(lastAttacker);
    }


    //baby
    @ProtoField(Ignore = true)
    private Baby baby;

    @ProtoField(Ignore = true)
    private List<Integer> guildList = new CopyOnWriteArrayList<>();

    //<taskId, taskProgress>
    @ProtoField(Ignore = true)
    private Map<Integer, TaskProgress> taskProgressMap = new ConcurrentHashMap<>();

    // 玩家当前的队伍id
    private Long teamId;

    @ProtoField(Ignore = true)
    private Map<Integer, Friend> friendMap = new ConcurrentHashMap<>();
}
