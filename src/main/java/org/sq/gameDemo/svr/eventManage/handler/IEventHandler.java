package org.sq.gameDemo.svr.eventManage.handler;

import org.sq.gameDemo.svr.eventManage.Event;

@FunctionalInterface
public interface IEventHandler<E extends Event> {
    public void handle(E event);
}
