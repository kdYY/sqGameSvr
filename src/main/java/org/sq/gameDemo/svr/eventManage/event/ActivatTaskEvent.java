package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.List;

/**
 * 任务激活事件
 */
@Data
@AllArgsConstructor
public class ActivatTaskEvent extends Event {
    Player player;
    List<Integer> taskId;
}
