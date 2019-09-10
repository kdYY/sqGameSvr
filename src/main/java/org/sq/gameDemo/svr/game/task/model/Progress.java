package org.sq.gameDemo.svr.game.task.model;

import lombok.Data;
import org.sq.gameDemo.svr.game.task.model.config.FinishCondition;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Progress {
    private FinishCondition condition;

    private AtomicInteger progressNum = new AtomicInteger(0);

    public boolean finished = false;

    public Progress(FinishCondition condition) {
        this.condition = condition;
    }

    public Progress() {
    }
}
