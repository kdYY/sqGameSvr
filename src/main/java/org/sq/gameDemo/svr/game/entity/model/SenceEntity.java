package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoObject;

import java.util.List;

//场景实体
@Data
public class SenceEntity {
    private Integer id;
    private Integer senceId;
    private Integer typeId;
    private int num;
    private int state;

    @ProtoObject(TargetClass = String.class, TargetName = "npcWord")
    private List<String> npcWord;
//    public SenceEntity(Integer id, Integer typeId, int state) {
//        this.id = id;
//        this.typeId = typeId;
//        this.state = state;
//    }
}
