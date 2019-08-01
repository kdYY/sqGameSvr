package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景非角色单位
 */
@Data
public class SenceEntity{
    private Long id;
    private String name;
    private Integer senceId;
    private Integer typeId;//角色类型
    private Integer state;
    private Long hp;
    private Long mp;
    private Long attack;
    private Long refreshTime; //刷新时间
    private String npcWord;

    @ProtoField(Ignore = true)
    private String skillStr;

    // 当前使用技能的集合
    @ProtoField(Ignore = true)
    Map<Integer, Skill> skillInUsedMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    @ProtoField(Ignore = true)
    private Character target;
    // 死亡时间
    @ProtoField(Ignore = true)
    private long deadTime;


    //buff集合 可不可覆盖?
    //掉落的东西
    //待领任务或者奖励

}
