package org.sq.gameDemo.svr.eventManage;

@FunctionalInterface
public interface EventHandler<E extends Event> {
    public void handle(E event);
}
