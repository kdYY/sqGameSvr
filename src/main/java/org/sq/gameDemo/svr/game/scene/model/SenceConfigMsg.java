package org.sq.gameDemo.svr.game.scene.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.MonsterPt;
import org.sq.gameDemo.common.proto.NpcPt;
import org.sq.gameDemo.common.proto.PlayerPt;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;

import java.util.List;

/**
 * 场景信息存储类
 */
@Data
public class  SenceConfigMsg {
    @ProtoField(Ignore = true)
    private int senceId;

    @ProtoField(TargetClass = EntityTypeProto.EntityType.class, TargetName = "entityType")
    private List<EntityType> entityTypeList;

    @ProtoField(TargetClass = MonsterPt.Monster.class, TargetName = "monster")
    private List<Monster> monsterList;

    @ProtoField(TargetClass = PlayerPt.Player.class, TargetName = "player")
    private List<Player> playerList;

    @ProtoField(TargetClass = NpcPt.Npc.class, TargetName = "npc")
    private List<Npc> npcList;

    public List<EntityType> getEntityTypeList() {
        if(entityTypeList == null) {
            entityTypeList = EntityTypeCache.getAllEntityTypes();
        }
        return entityTypeList;
    }
}
