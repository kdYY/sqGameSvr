package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 *场景角色单位
 */
@Data
public class UserEntity {
    private Integer unId;

    /**
     * 玩家昵称
     */
    private String name;

    /**
     * 从属用户
     */
    private Integer userId;

    /**
     * 角色状态
     */
    private Integer state;

    /**
     * 角色类型
     */
    private Integer typeId;

    /**
     * 从属场景Id
     */
    private Integer senceId;

    /**
     * 经验值
     */
    private Integer exp;

    /**
     * 装备
     */
    private String equipments = "{}";

    
    /**
     * 元宝
     */
    private Integer money;


    /**
     * 公会id
     */
    private Integer guildId;

    /**
     * 宝宝等级 0代表没有宝宝
     */
    private Integer babyLevel = 0;

    /**
     * 宝宝类型
     * */
    private Integer babyType = 0;

    // 当前使用技能的集合
    @ProtoField(Ignore = true)
    private Map<Integer, Skill> skillInUsedMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    @ProtoField(Ignore = true)
    private Character target;

    //延迟释放的技能
    @ProtoField(Ignore = true)
    private Map<Skill, Future> skillInEffectingMap = new ConcurrentHashMap<>();

}
