package org.sq.gameDemo.svr.game.characterEntity.model;

import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

/**
 * 野怪类
 */
public class Monster extends SenceEntity implements Character {
    //野怪攻击目标
    @ProtoField(Ignore = true)
    Character attackTarget;

    //野怪攻击速度
    private Integer attackSpeed = 1000;


}
