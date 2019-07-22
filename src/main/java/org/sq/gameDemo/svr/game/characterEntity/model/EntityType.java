package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import java.util.List;

@Data
public class EntityType {
    private int id;
    private String name;

    //魔法攻击
    private int baseMPAttack;
    //物理攻击
    private int baseHPAttack;
    //防御值
    private int defense;

    @ProtoField(Ignore = true)
    private String skillStr;

    //@ProtoField(TargetName = "skill", TargetClass = SkillProto.class)
    private List<Skill> skillList;

    @ProtoField( TargetName = "type", Function = "getType", TargetClass = EntityTypeProto.EntityType.Builder.class)
    private String typeName; //AP型， AD型


    public void getType(EntityTypeProto.EntityType.Builder builder) {
        builder.setType(Constant.AP.equals(this.getTypeName()) ? EntityTypeProto.AttackType.AP : EntityTypeProto.AttackType.AD);
    }
}
