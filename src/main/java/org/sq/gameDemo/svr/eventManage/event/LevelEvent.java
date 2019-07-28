package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class LevelEvent extends Event {

    private Player player;

    private Integer level;
}
