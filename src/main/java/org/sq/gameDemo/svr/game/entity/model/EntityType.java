package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

@Data
public class EntityType {
    private int id;
    private String name;

    private int baseMP;
    private int baseHP;

    @ProtoField( TargetName = "type", Function = "getType", TargetClass = EntityTypeProto.EntityType.Builder.class)
    private String typeName; //AP型， AD型


    public void getType(EntityTypeProto.EntityType.Builder builder) {
        builder.setType("AP".equals(this.getTypeName()) ? EntityTypeProto.AttackType.AP : EntityTypeProto.AttackType.AD);
    }
}
