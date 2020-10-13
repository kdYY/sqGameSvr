package org.sq.gameDemo.observer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 事件管理器
 */
public class ObserverController {

    private AbstractCreature owner;

    private final Map<Class<? extends IObserver>, ObserverSupport<?>> observerSupportMap = new ConcurrentHashMap<>();

    private AtomicInteger version = new AtomicInteger(1);

    public ObserverController(AbstractCreature owner) {
        this.owner = owner;
    }

    /**
     * 获取事件代理 如果不存在则创建
     * @param listenerClass 事件接口
     * @param <L> 事件接口泛型
     * @return 代理
     */
    @SuppressWarnings("uncheck")
    public <L extends IObserver> ObserverSupport<L> getOrCreateSupport(Class<L> listenerClass) {
        ObserverSupport<?> support = observerSupportMap.get(listenerClass);
        if(support == null) {
            synchronized (observerSupportMap) {
                support = observerSupportMap.get(listenerClass);
                if(support == null) {
                    support = new ObserverSupport<>(listenerClass);
                    observerSupportMap.put(listenerClass, support);
                }
            }
        }
        return (ObserverSupport<L>) support;
    }

    /**
     * 获取代理
     * @param listenerClass
     * @param <L>
     * @return
     */
    @SuppressWarnings("uncheck")
    public <L extends IObserver> ObserverSupport<L> getMayNullSupport(Class<L> listenerClass) {
        ObserverSupport<?> support = observerSupportMap.get(listenerClass);
        return (ObserverSupport<L>) support;
    }

    public <L extends IObserver> void fire(Class<L> listenerClass, Consumer<L> consumer) {
        fire(listenerClass, consumer, null);
    }

    private <L extends IObserver> void fire(Class<L> listenerClass, Consumer<L> consumer, Runnable afterFire) {
        ObserverSupport<L> observerSupport = getMayNullSupport(listenerClass);
        if(observerSupport == null || observerSupport.isEmpty()) {
            //没有观察者
            if(afterFire != null) {
                afterFire.run();
            }
            return;
        }
        int version = this.version.get();
        singleThreadSchedule.execute(() -> {

        });

    }

    /** 单独一个线程异步处理事件处理，同时保证了事件的循序 */
    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("event-%d").build();
    private static ExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);

}
