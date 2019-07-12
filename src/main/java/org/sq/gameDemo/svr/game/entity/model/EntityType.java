package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.EntityTypeProto;

@Data
public class EntityType {
    private int id;
    private String name;



    @Override
    public String toString() {
        return "EntityType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static EntityTypeProto.EntityType transform(EntityType entityType) {
        return EntityTypeProto.EntityType.newBuilder()
                .setId(entityType.getId())
                .setName(entityType.getName())
                .build();
    }
}
