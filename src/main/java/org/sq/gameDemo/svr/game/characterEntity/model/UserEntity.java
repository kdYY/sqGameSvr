package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * 金币
     */
    private Integer money;


    /**
     * 公会id
     */
    private Integer guildId;

    // 当前使用技能的集合
    @ProtoField(Ignore = true)
    private Map<Integer, Skill> skillInUsedMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    @ProtoField(Ignore = true)
    private Character target;


}
