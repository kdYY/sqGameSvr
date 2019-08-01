package org.sq.gameDemo.svr.game.scene.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.MonsterPt;
import org.sq.gameDemo.common.proto.NpcPt;
import org.sq.gameDemo.common.proto.PlayerPt;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.dao.EntityTypeCache;
import org.sq.gameDemo.svr.game.characterEntity.model.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

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

    @ProtoField(Ignore = true)
    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("scene-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 通过一个场景一个线程处理器的方法保证每个场景的指令循序 */
    @ProtoField(Ignore = true)
    ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);

}
