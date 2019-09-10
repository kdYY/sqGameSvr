package org.sq.gameDemo.svr.eventManage.event;

import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Data
public class PlayerPKEvent extends Event {
    private Player attacter;
    private Player player;

    public PlayerPKEvent(Player attacter, Player player) {
        this.attacter = attacter;
        this.player = player;
    }
}
