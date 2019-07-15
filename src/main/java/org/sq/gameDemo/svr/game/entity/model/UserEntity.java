package org.sq.gameDemo.svr.game.entity.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoObject;
import org.sq.gameDemo.svr.game.scene.model.GameScene;

//用户实体
@Data
public class UserEntity {
    @ProtoObject(Ignore = true)
    private Integer id;

    private Integer userId;

    private String nick;

    private Integer state;

    private Integer typeId;

    private Integer senceId;


    public static UserEntityProto.UserEntity transformProto(UserEntity userEntity) {
        return UserEntityProto.UserEntity.newBuilder()
                .setNick(userEntity.getNick())
                .setTypeId(userEntity.getTypeId())
                .setUserId(userEntity.getUserId())
                .setSenceId(userEntity.getSenceId())
                .setState(userEntity.getState())
                .build();
    }
}
