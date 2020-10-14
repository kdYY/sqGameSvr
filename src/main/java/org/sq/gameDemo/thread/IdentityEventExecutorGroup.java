package org.sq.gameDemo.thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

import io.netty.util.concurrent.ScheduledFuture;

/**
 * 任务调度器
 */
public class IdentityEventExecutorGroup {

    private static final ThreadLocal<Integer> dispatcherCode = new ThreadLocal<>();
    private static final String EXECUTOR_NAME_PREFIX = "Identity-dispatcher";
    private static EventExecutor[] childrens;

    /**
     * 初始化
     * @param nThreads
     */
    public synchronized static void init(int nThreads) {
        if(childrens == null) {
            childrens = new EventExecutor[nThreads];
            ThreadFactory threadFactory = new DefaultThreadFactory(EXECUTOR_NAME_PREFIX);
            for (int i = 0; i < nThreads; i++) {
                EventExecutor eventExecutors = new EventExecutor(i + 1, null, threadFactory, true);
                childrens[i] = eventExecutors;
                int dispatcherCode = i;
                addTask(i, "setDispatcherCode", () -> {
                    IdentityEventExecutorGroup.dispatcherCode.set(dispatcherCode);
                });
            }
        }
    }

    public static void shutdown() {
        for (EventExecutor children : childrens) {
            children.shutdownGracefully();
        }
    }

    /**
     * 添加同步任务
     * @param task
     * @return
     */
    public static Future<?> addTask(AbstractDispatcherHashCodeRunable task) {
        checkName(task.name());
        EventExecutor eventExecutor = hashExecutor(task.getDispatcherHashCode());
        task.submit(eventExecutor.getIndex(), false);
        return eventExecutor.addTask(task);
    }

    public static Future<?> addTask(int dispatcherCode, String name, Runnable runnable) {
        checkName(name);
        return addTask(new AbstractDispatcherHashCodeRunable() {

            @Override
            public void doRun() {
                runnable.run();
            }

            @Override
            public int getDispatcherHashCode() {
                return dispatcherCode;
            }

            @Override
            public String name() {
                return name;
            }
        });
    }

    /**
     * 添加延时任务
     */
    public static ScheduledFuture<?> addScheduleTask(AbstractDispatcherHashCodeRunable task, long delay, TimeUnit unit) {
        checkName(task.name());
        EventExecutor eventExecutors = hashExecutor(task.getDispatcherHashCode());
        task.submit(eventExecutors.getIndex(), true);
        return eventExecutors.addScheduleTask(task, delay, unit);
    }


    public static ScheduledFuture<?> addScheduleTask(int dispatcherHashCode, String name, long delay, TimeUnit unit, Runnable runnable) {
        checkName(name);
        return addScheduleTask(new AbstractDispatcherHashCodeRunable() {
            @Override
            public void doRun() {
                runnable.run();
            }

            @Override
            public int getDispatcherHashCode() {
                return dispatcherHashCode;
            }

            @Override
            public String name() {
                return name;
            }
        }, delay, unit);
    }

    /**
     * 添加定时器任务
     * 该任务按照周期执行
     */
    public static ScheduledFuture<?> addScheduleAtFixedRate(AbstractDispatcherHashCodeRunable task, long initialDelay, long period,
                                                            TimeUnit
                                                            unit) {
        checkName(task.name());
        EventExecutor eventExecutors = hashExecutor(task.getDispatcherHashCode());
        task.submit(eventExecutors.getIndex(), true);
        return eventExecutors.addScheduleAtFixedRate(task, initialDelay, period, unit);
    }


    public static ScheduledFuture<?> addScheduleAtFixedRate(int dispatcherHashCode, String name, long initialDelay, long period,  TimeUnit unit, Runnable runnable) {
        checkName(name);
        return addScheduleAtFixedRate(new AbstractDispatcherHashCodeRunable() {
            @Override
            public void doRun() {
                runnable.run();
            }

            @Override
            public int getDispatcherHashCode() {
                return dispatcherHashCode;
            }

            @Override
            public String name() {
                return name;
            }
        }, initialDelay, period, unit);
    }

    /**
     * 添加定时器任务
     * 该任务按照延迟时间执行
     */
    public static ScheduledFuture<?> addScheduleAtFixedDelay(AbstractDispatcherHashCodeRunable task, long initialDelay, long delay,
                                                             TimeUnit unit) {
        checkName(task.name());
        EventExecutor eventExecutors = hashExecutor(task.getDispatcherHashCode());
        task.submit(eventExecutors.getIndex(), true);
        return eventExecutors.addScheduleAtFixedDelay(task, initialDelay, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedDelay(int dispatcherHashCode, String name, long initialDelay, long delay, TimeUnit
            unit, Runnable runnable) {
        checkName(name);
        return addScheduleAtFixedDelay(new AbstractDispatcherHashCodeRunable() {
            @Override
            public void doRun() {
                runnable.run();
            }

            @Override
            public int getDispatcherHashCode() {
                return dispatcherHashCode;
            }

            @Override
            public String name() {
                return name;
            }
        }, initialDelay, delay, unit);
    }

    /**
     * 根据分发码从池中找到对应的Executor
     * @param dispatcherHashCode
     * @return
     */
    private static EventExecutor hashExecutor(int dispatcherHashCode) {
        return childrens[adjustDispatchCode(dispatcherHashCode)];
    }

    private static int adjustDispatchCode(int dispatcherHashCode) {
        return Math.abs(dispatcherHashCode % childrens.length);
    }

    private static void checkName(String name) {
        if(StringUtils.isEmpty(name)) {
            throw new NullPointerException("name is null!");
        }
    }

    /**
     * 获取线程数量
     * @return
     */
    public static int getThreadNum() {
        return childrens.length;
    }

    /**
     * 等待线程池起那么提交的业务全部执行完成
     */
    public static void blockWaitRunOver() {
        int threadNum = getThreadNum();
        CountDownLatch runNum = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            addTask(i, "checkSyncTaskRunOver", runNum::countDown);
        }

        while (runNum.getCount() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 检测当前线程是否在业务线程池中, 如果不是，抛出异常
     */
    public static void checkInIdentityExecutorGroup() {
        Assert.isTrue(isInIdentityExecutorGroup(), "run in IdentityEventExecutorGroup");
    }
    /**
     * 检测当前线程是否在业务线程池中, 如果是，抛出异常
     */
    public static void checkNotInIdentityExecutorGroup() {
        Assert.isTrue(!isInIdentityExecutorGroup(), "run in IdentityEventExecutorGroup");
    }

    private static boolean isInIdentityExecutorGroup() {
        return Thread.currentThread().getName().startsWith(EXECUTOR_NAME_PREFIX);
    }


}
