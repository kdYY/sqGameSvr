package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Npc;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Data
@AllArgsConstructor
public class ConversationEvent extends Event {
    Player player;
    Npc npc;
}
