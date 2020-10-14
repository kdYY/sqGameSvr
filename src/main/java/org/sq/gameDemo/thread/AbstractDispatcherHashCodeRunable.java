package org.sq.gameDemo.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * kd
 * 任务分发标记器
 */
public abstract class AbstractDispatcherHashCodeRunable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDispatcherHashCodeRunable.class);

    private Consumer<AbstractDispatcherHashCodeRunable> runBefore;
    private Consumer<AbstractDispatcherHashCodeRunable> runAfter;

    /**
     * 是否为定时任务
     */
    private boolean scheduleTask;
    private int threadIndex;
    private long submitTime;

    /**
     * 执行的任务
     */
    abstract public void doRun();

    /**
     * 用于分发的编号
     */
    abstract public int getDispatcherHashCode();

    /**
     * 任务类型 同一种类型任务添加一种任务即可
     */
    public abstract String name();

    /**
     * @return 该任务纳秒时间
     */
    protected long timeoutNanoTime() {
        return TimeUnit.MILLISECONDS.toNanos(1);
    }

    public void submit(int threadIndex, boolean scheduleTask) {
        this.threadIndex = threadIndex;
        this.scheduleTask = scheduleTask;
        this.submitTime = System.nanoTime();
    }


    @Override
    final public void run() {
        long start = System.nanoTime();
        try {
            if(runBefore != null) {
                runBefore.accept(this);
            }
            doRun();
            if(runAfter != null) {
                runAfter.accept(this);
            }
        } catch (Throwable e) {
            //这里可以做监控 记录异常啊什么的
            LOGGER.error(String.format("任务执行错误! %s", this.getClass().getSimpleName()), e);
        } finally {
            long now = System.nanoTime();
            //这里可以做监控执行时间
            long submitNanoTime = submitTime;
            if(scheduleTask) {
                submitNanoTime = now;
            }
            LOGGER.info("{}执行中threadIndex={};startTime={};submitTime={}", name(), threadIndex, start, submitNanoTime);
            this.submitTime = now;
        }
    }

    public Consumer<AbstractDispatcherHashCodeRunable> getRunBefore() {
        return runBefore;
    }

    public void setRunBefore(Consumer<AbstractDispatcherHashCodeRunable> runBefore) {
        this.runBefore = runBefore;
    }

    public Consumer<AbstractDispatcherHashCodeRunable> getRunAfter() {
        return runAfter;
    }

    public void setRunAfter(Consumer<AbstractDispatcherHashCodeRunable> runAfter) {
        this.runAfter = runAfter;
    }
}
