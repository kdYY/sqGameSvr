package org.sq.gameDemo.svr.game.copyScene.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.sq.gameDemo.common.proto.MonsterPt;
import org.sq.gameDemo.common.proto.SenceMsgProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.Monster;
import org.sq.gameDemo.svr.game.scene.model.SenceConfigMsg;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 副本场景类
 */
@Data
public class CopyScene extends SenceConfigMsg {

    private String name;

    //副本场景id
    private Integer id;

    //<playerId , beforeSceneId>
    private Map<Long, Integer> beforeSenceIdMap = new ConcurrentHashMap<>();
    //boss
    @ProtoField(TargetName = "boss", Function = "buildBoss", TargetClass = SenceMsgProto.SenceMsgResponseInfo.Builder.class)
    private Monster boss;

    /**
     * 做item中ItemInfo的注入
     * @param builder
     * @throws Exception
     */
    public void buildBoss(SenceMsgProto.SenceMsgResponseInfo.Builder builder) throws Exception {
        builder.setBoss((MonsterPt.Monster) ProtoBufUtil.transformProtoReturnBean(MonsterPt.Monster.newBuilder(), this.getBoss()));
    }
    //副本开始时间戳
    private Long startTime;

    //副本允许容纳的玩家个数
    private Integer limit;

    //是否正在被回收
    @ProtoField(Ignore = true)
    private volatile boolean isGarbage;
    //副本限时
    private Long maxTime;

    @ProtoField(Ignore = true)
    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("scene-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 通过一个场景一个线程处理器的方法保证每个场景的指令循序 */
    @ProtoField(Ignore = true)
    ScheduledExecutorService copyScenceSingleThread = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);

    public void inGarbage() {

        this.setGarbage(true);
        if(this.getPlayerList() != null) {
            this.getPlayerList().clear();
            this.setPlayerList(null);
        }
        if(this.getMonsterList() != null) {
            this.getMonsterList().clear();
            this.setMonsterList(null);
        }
        if(this.getBeforeSenceIdMap() != null) {
            this.getBeforeSenceIdMap().clear();
            this.setBeforeSenceIdMap(null);
        }
        this.copyScenceSingleThread.shutdown();
        this.boss = null;

    }
}
