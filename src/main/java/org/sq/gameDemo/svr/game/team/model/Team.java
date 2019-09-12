package org.sq.gameDemo.svr.game.team.model;

import lombok.Data;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Team {
    //组队id
    private Long id;

    private Long captainId;

    Map<Long, Player> playerInTeam = new ConcurrentHashMap<>();

    //队伍人数上限
    private Integer limitedSize;

}
