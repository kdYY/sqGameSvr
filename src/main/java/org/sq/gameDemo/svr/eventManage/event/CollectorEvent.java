package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
@Data
@AllArgsConstructor
public class CollectorEvent extends Event {
    Player player;
    Item item;
}
