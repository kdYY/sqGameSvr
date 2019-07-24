package org.sq.gameDemo.svr.game.characterEntity.model;

import org.sq.gameDemo.svr.game.skills.model.Skill;

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
    void setMp(Long hp);

    //角色状态，生存 or 死亡
    Integer getState();
    void setState(Integer state);

    //角色正在使用的技能
    Map<Integer, Skill> getSkillMap();
    void setSkillMap(Map<Integer, Skill> skillMap);



    //之后还有buff之类的

}
