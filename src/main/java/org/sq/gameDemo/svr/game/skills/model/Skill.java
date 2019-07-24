package org.sq.gameDemo.svr.game.skills.model;


import lombok.Data;

/**
 * 角色技能类
 */
@Data
public class Skill {

    private Integer id;

    private String name;

    /**
     * 技能类型:
     * 1 自身
     * 2 单个队友和自身同时
     * 3 群体队友和自身同时
     * 4 单个敌人
     * 5 群体敌人
     */
    private Integer skillRange;

    /**
     * 技能冷却时间
     */
    private Long cd;

    /**
     * 需要的魔法值
     */
    private Long mpNeed;

    /**
     * 造成的伤害
     */
    private Long hurt;

    /**
     * 治疗量
     */
    private Long heal;

    /**
     * 技能等级
     */
    private Integer grade;

    /**
     * buff类型，暂无
     */
    private Integer buff;

    /**
     * 技能描述
     */
    private String   description;
}

