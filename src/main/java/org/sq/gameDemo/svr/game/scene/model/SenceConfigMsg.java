package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.SenceEntityProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoObject;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class  SenceConfigMsg {
    @ProtoObject(Ignore = true)
    private int senceId;

    @ProtoObject(Ignore = true)
    @JSONField(serialize = false)
    private String jsonStr;

    @ProtoObject(TargetClass = EntityTypeProto.EntityType.class)
    private List<EntityType> entityTypes;
    @ProtoObject(TargetClass = SenceEntityProto.SenceEntity.class)
    private List<SenceEntity> senceEntities;
    @ProtoObject(TargetClass = UserEntityProto.UserEntity.class)
    private List<UserEntity> userEntities;

}
