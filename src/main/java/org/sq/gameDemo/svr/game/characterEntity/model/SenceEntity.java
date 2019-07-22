package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//场景实体
@Data
public class SenceEntity{
    private Integer id;
    private String name;
    private Integer senceId;
    private Integer typeId;//角色类型
    private Integer state;

    private Long hp;
    private Long mp;
    private Long attack;
    private Long refreshTime; //刷新时间

    private String skillStr;
    // 当前使用技能的集合
    Map<Integer, Skill> skillMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    private Character target;


    //buff集合 可不可覆盖?
    //掉落的东西
    //待领任务或者奖励


    //@ProtoField(TargetClass = String.class, TargetName = "npcWord")
    private String npcWord;
//    public SenceEntity(Integer id, Integer typeId, int state) {
//        this.id = id;
//        this.typeId = typeId;
//        this.state = state;
//    }
}
