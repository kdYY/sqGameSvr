package org.sq.gameDemo.svr.game.characterEntity.model;

import org.sq.gameDemo.svr.game.buff.model.Buff;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;
import java.util.Map;

/**
 * 场景上的一个单位，规范角色的必有的属性方法
 */
public interface Character {

    //角色id
    Long getId();

    //角色名
    String getName();

    //角色血量
    Long getHp();
    void setHp(Long hp);

    //角色mp
    Long getMp();
    void setMp(Long mp);

    //角色状态，生存 or 死亡
    Integer getState();
    void setState(Integer state);

    //角色正在使用的技能
    Map<Integer, Skill> getSkillInUsedMap();
    void setSkillInUsedMap(Map<Integer, Skill> skillMap);


    /** 角色的当前buff*/
    List<Buff> getBufferList();
    void setBufferList(List<Buff> bufferList);



    //角色的场景id
    Integer getSenceId();
    void setSenceId(Integer senceId);

}
