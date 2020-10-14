package org.sq.gameDemo.observer;

import org.sq.gameDemo.observer.Impl.ICreateureDead;
import org.sq.gameDemo.svr.game.characterEntity.model.Character;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 事件管理器
 */
public class ObserverController {

    private Character owner;

    private final Map<Class<? extends IObserver>, ObserverSupport<?>> observerSupportMap = new ConcurrentHashMap<>();

    private AtomicInteger version = new AtomicInteger(1);

    public ObserverController(Character owner) {
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

    /**
     * 获取一个事件的代理对象， 用于触发事件
     * 该处理使用异步处理的方式
     * @param listenerClass
     * @param consumer
     * @param <L>
     */
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
            syncFireByVersion(version, listenerClass, consumer);
            if(afterFire != null) {
                afterFire.run();
            }
        });

    }

    /**
     * 获取一个事件的代理对象， 用于触发事件
     * 该处理使用同步处理的方式
     * @param listenerClass
     * @param consumer
     * @param <L>
     */
    private <L extends IObserver> void syncFire(Class<L> listenerClass, Consumer<L> consumer) {
        int version = this.version.get();
        syncFireByVersion(version, listenerClass, consumer);
    }

    private <L extends IObserver> void syncFireByVersion(int version, Class<L> listenerClass, Consumer<L> consumer) {
        ObserverSupport<L> support = getMayNullSupport(listenerClass);
        if(support == null || support.isEmpty()) {
            return;
        }
        support.fire(version, consumer);
    }


    /**
     * 添加永久有效的观察者, 自己维护观察者的生命周期
     * @param lClass
     * @param observer
     * @param <L>
     * @param <O>
     * @return
     */
    public <L extends IObserver, O extends L> Observer<L> attachForever(Class<L> lClass, O observer) {
        int version = this.version.getAndIncrement();
        return getOrCreateSupport(lClass).attach(observer, null, version);
    }

    /**
     * 添加观察者 在当前存活期间有效 对象死亡后失效
     * @param lClass
     * @param observer
     * @param <L>
     * @param <O>
     * @return
     */
    public <L extends IObserver, O extends L> Observer<L> attachWithAlive(Class<L> lClass, O observer) {
        int version = this.version.getAndIncrement();
        return getOrCreateSupport(lClass).attach(observer, ObserverInvalidType.alive, version);
    }

    /**
     * 添加观察者 监听一次后即失效 注意 该接口表示必然要触发之后 才会失效
     * @param lClass
     * @param observer
     * @param <L>
     * @param <O>
     * @return
     */
    public <L extends IObserver, O extends L> Observer<L> attachWithOneOff(Class<L> lClass, O observer) {
        int version = this.version.getAndIncrement();
        return getOrCreateSupport(lClass).attach(observer, ObserverInvalidType.oneOff, version);
    }


    /**
     * 尝试移除指定类型的观察者
     */
    private void tryRemoveInvalid(ObserverInvalidType invalidType, int version) {
        observerSupportMap.values().forEach(observerSupport -> observerSupport.tryRemoveInvalid(invalidType, version));
    }


    /**
     * 移除所有观察者
     */
    public void clear() {
        observerSupportMap.clear();
    }


    public void onDie(Character lastAttacker) {
        int version = this.version.get();
        // 抛出死亡事件后 移除死亡失效观察者
        fire(ICreateureDead.class,
                iCreateureDead -> iCreateureDead.onCreatureDead(owner, lastAttacker),
                () -> tryRemoveInvalid(ObserverInvalidType.alive, version));
    }

    //TODO 需要优化下->根据玩家的id去抛在玩家专属的线程里
    // 暂时这样暴力用
    /** 一个观察者实例 单独一个线程异步处理事件处理，同时保证了事件的循序 */
    private ExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(ThreadPool.getSceneThreadFactory());

}
