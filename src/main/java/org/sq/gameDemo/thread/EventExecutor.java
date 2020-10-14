package org.sq.gameDemo.thread;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * kd
 */
public class EventExecutor extends SingleThreadEventExecutor {

    private int index;

    protected EventExecutor(int index, EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
        super(parent, threadFactory, addTaskWakesUp);
        this.index = index;
    }

    @Override
    protected void run() {
        //先做一次 再while判断
        do {
            Runnable task = takeTask();
            if(task != null) {
                task.run();
                updateLastExecutionTime();
            }
        } while (!confirmShutdown());
    }


    public ScheduledFuture<?> addScheduleTask(AbstractDispatcherHashCodeRunable task, long delay, TimeUnit unit) {
        return schedule(task, delay, unit);
    }

    public ScheduledFuture<?> addScheduleAtFixedRate(AbstractDispatcherHashCodeRunable task, long initialDelay, long period, TimeUnit unit) {
        return scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public ScheduledFuture<?> addScheduleAtFixedDelay(AbstractDispatcherHashCodeRunable task, long initialDelay, long delay, TimeUnit
            unit) {
        return scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedDelay(AbstractDispatcherHashCodeRunable task, long initialDelay, long delay, TimeUnit
            unit) {
        return scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }



    public Future<?> addTask(AbstractDispatcherHashCodeRunable task) {
        return submit(task);
    }

    public int getIndex() {
        return index;
    }
}
