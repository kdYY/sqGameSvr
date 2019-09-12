package org.sq.gameDemo.svr.game.team.model;

import lombok.Data;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Data
public class TeamRequest {
    Long sponsorId;
    Long invitedId;
}
