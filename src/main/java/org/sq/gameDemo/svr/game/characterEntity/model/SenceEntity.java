package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sq.gameDemo.common.proto.BuffPt;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.fight.monsterAI.state.CharacterState;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/**
 * 场景非角色单位
 */
@Data
public class SenceEntity{
    private Long id;
    private String name;
    private Integer senceId;
    private Integer typeId;//npc、monster
    private Integer state;
    private Long hp;
    private Long mp;
    private Long attack;
    private Long refreshTime; //刷新时间
    private String npcWord;
    @ProtoField(Ignore = true)
    private String taskStr;

    @ProtoField(Ignore = true)
    private String skillStr;

    // 当前使用技能的集合
    @ProtoField(Ignore = true)
    Map<Integer, Skill> skillInUsedMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    @ProtoField(Ignore = true)
    private Character target;

    /**
     * 根据目标变动改变怪物状态
     * @param target
     */
    public void setTarget(Character target) {
        if(target == null) {
            this.setState(CharacterState.LIVE.getCode());
        } else {
            this.setState(CharacterState.ATTACKING.getCode());
        }
        this.target = target;
    }

    // 死亡时间
    @ProtoField(Ignore = true)
    private long deadTime;

    public synchronized void setHp(Long hp) {
            if(hp <= 0) {
                hp = 0L;
            }
            this.hp = hp;
    }

    public synchronized void setMp(Long mp) {
            if(mp <= 0) {
                mp = 0L;
            }
            this.mp = mp;
    }


    //buff
    @ProtoField(TargetClass = BuffPt.Buff.class, TargetName = "buff")
    private List<Buff> bufferList = new CopyOnWriteArrayList<>();

    @ExcelFeild(Ignore = true)
    private Integer level;


    //延迟释放的技能
    @ProtoField(Ignore = true)
    private Map<Skill, Future> skillInEffectingMap = new ConcurrentHashMap<>();
}
