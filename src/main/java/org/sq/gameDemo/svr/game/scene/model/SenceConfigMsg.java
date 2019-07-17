package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.SenceEntityProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.entity.model.EntityType;
import org.sq.gameDemo.svr.game.entity.model.SenceEntity;
import org.sq.gameDemo.svr.game.entity.model.UserEntity;

import java.util.List;

@Data
public class  SenceConfigMsg {
    @ProtoField(Ignore = true)
    private int senceId;

    @ProtoField(Ignore = true)
    @JSONField(serialize = false)
    private String jsonStr;

    @ProtoField(TargetClass = EntityTypeProto.EntityType.class)
    private List<EntityType> entityTypes;
    @ProtoField(TargetClass = SenceEntityProto.SenceEntity.class)
    private List<SenceEntity> senceEntities;
    @ProtoField(TargetClass = UserEntityProto.UserEntity.class)
    private List<UserEntity> userEntities;

}
