package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//角色实体
@Data
public class UserEntity {
    @ProtoField(Ignore = true)
    private Integer id;

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



    // 当前使用技能的集合
    private Map<Integer, Skill> skillMap = new ConcurrentHashMap<>();

    // 当前攻击对象
    private Character target;


}
