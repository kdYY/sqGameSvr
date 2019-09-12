package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.task.model.TaskProgress;

@Data
@AllArgsConstructor
public class TaskFinishedEvent extends Event {
    Player player;
    TaskProgress taskProgress;
}
