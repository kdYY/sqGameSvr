package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.SenceEntityProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoObject;
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

    @ProtoObject(TargetClass = SenceEntityProto.SenceEntity.class, TargetRepeatedName = "SenceEntity")
    private List<SenceEntity> senceEntities;
    @ProtoObject(TargetClass = UserEntityProto.UserEntity.class, TargetRepeatedName = "UserEntity")
    private List<UserEntity> userEntities;

}
