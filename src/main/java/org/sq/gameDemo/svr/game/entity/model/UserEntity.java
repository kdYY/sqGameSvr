package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

//用户实体
@Data
public class UserEntity {
    @ProtoField(Ignore = true)
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
