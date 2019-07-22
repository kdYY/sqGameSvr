package org.sq.gameDemo.svr.game.scene.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.SenceEntityProto;
import org.sq.gameDemo.common.proto.UserEntityProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.*;

import java.util.List;

@Data
public class  SenceConfigMsg {
    @ProtoField(Ignore = true)
    private int senceId;

    @ProtoField(TargetClass = EntityTypeProto.EntityType.class)
    private List<EntityType> entityTypes;

    @ProtoField(TargetClass = SenceEntityProto.Monster.class)
    private List<Monster> monsterList;

    @ProtoField(TargetClass = UserEntityProto.Player.class)
    private List<Player> playerList;

    @ProtoField(TargetClass = UserEntityProto.Npc.class)
    private List<Npc> npcList;
}
