package org.sq.gameDemo.svr.eventManage.event;

import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Data
public class PlayerDeadEvent extends Event {
    private Character attacter;
    private Player player;

    public PlayerDeadEvent(Character attacter, Player player) {
        this.attacter = attacter;
        this.player = player;
    }
}
