package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.EntityTypeProto;

@Data
public class EntityType {
    private int id;
    private String name;

    private int baseMP;
    private int baseHP;

    private String typeName; //AP型， AD型

    
}
