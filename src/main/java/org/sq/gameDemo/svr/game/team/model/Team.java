package org.sq.gameDemo.svr.game.team.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.TeamPt;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 队伍类
 */
@Data
public class Team {
    //组队id
    private Long id;
    private String name;
    @ProtoField(Ignore = true)
    private Long captainId;

    //<玩家id 玩家>
    @ProtoField(TargetName = "member", Function = "addMember", TargetClass = TeamPt.Team.Builder.class)
    Map<Long, String> playerInTeam = new ConcurrentSkipListMap<>();

    /**
     * 做member的注入
     * @param builder
     * @throws Exception
     */
    public void addMember(TeamPt.Team.Builder builder) throws Exception {
        playerInTeam.entrySet().stream().forEach(entry -> {
            TeamPt.Member.Builder member = TeamPt.Member.newBuilder();
            member.setId(entry.getKey());
            member.setName(entry.getValue());
            builder.addMember(member);
        });
    }
    //队伍人数上限
    private Integer limitedSize;

}
