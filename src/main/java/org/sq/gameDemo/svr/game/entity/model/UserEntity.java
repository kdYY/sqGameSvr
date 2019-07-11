package org.sq.gameDemo.svr.game.entity.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.svr.game.scene.model.GameScene;

//用户实体
@Data
public class UserEntity {
    private int typeId;
    private String nick;//昵称
    private int userId;
    private int state = 1;
    public UserEntity(int typeId, String nick, int userId) {
        this.typeId = typeId;
        this.nick = nick;
        this.userId = userId;
    }

    public static EntityProto.UserEntity transformProto(UserEntity userEntity) {
        return EntityProto.UserEntity.newBuilder()
                .setNick(userEntity.getNick())
                .setTypeId(userEntity.getTypeId())
                .setUserId(userEntity.getUserId())
                .setState(userEntity.getState())
                .build();
    }
}
