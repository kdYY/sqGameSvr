package org.sq.gameDemo.svr.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.eventManage.Event;

import java.util.concurrent.*;

@Slf4j
@Component
public class TimeTaskManager {

    private static ThreadFactory scheduledThreadPoolFactory = new ThreadFactoryBuilder()
            .setNameFormat("scheduledThreadPool-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    private static ScheduledExecutorService ScheduledThreadPool =
            Executors.newScheduledThreadPool( Runtime.getRuntime().availableProcessors()*2+1,scheduledThreadPoolFactory);


    /**
     *  单线程循环执行器
     */
    private static ThreadFactory singleThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("singleThread-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    public static ScheduledExecutorService singleThreadSchedule =  Executors.newSingleThreadScheduledExecutor(singleThreadFactory);





    /**
     *  设置定时任务
     * @param delay 延迟执行时间，单位毫秒
     * @param callback 任务
     * @return
     */
    public static Future<Event> scheduleByCallable(long delay, Callable<Event> callback) {
        return ScheduledThreadPool.schedule(callback,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  设置单线程定时任务
     * @param delay 延迟执行时间，单位毫秒
     * @param runnable 任务
     * @return
     */
    public static Future singleThreadSchedule(long delay, Runnable runnable) {
        return singleThreadSchedule.schedule(runnable,delay, TimeUnit.MILLISECONDS);
    }



    /**
     *  设置多线程定时任务
     * @param delay 延迟执行时间，单位毫秒
     * @param runnable 任务
     * @return
     */
    public static Future schedule(long delay, Runnable runnable) {
        return ScheduledThreadPool.schedule(runnable,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  按固定的周期执行任务
     * @param initDelay 延时开始第一次任务的时间
     * @param delay     执行间隔
     * @param runnable 任务
     * @return
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(long initDelay , long delay , Runnable runnable) {
        return ScheduledThreadPool.scheduleAtFixedRate(runnable,initDelay,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  按照固定的延迟执行任务（即执行完上一个再执行下一个）
     * @param initDelay 延时开始第一次任务的时间
     * @param delay     执行间隔
     * @param runnable 任务
     *
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(long initDelay , long delay , Runnable runnable) {
        return ScheduledThreadPool.scheduleWithFixedDelay(runnable,initDelay,delay, TimeUnit.MILLISECONDS);
    }



    class SingleThreadSceduleExecutor extends SingleThreadEventExecutor {

        protected SingleThreadSceduleExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp) {
            super(parent, executor, addTaskWakesUp);
        }

        @Override
        protected void run() {

        }
    }
}
