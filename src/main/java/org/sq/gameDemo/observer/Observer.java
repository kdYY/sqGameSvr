package org.sq.gameDemo.observer;

import java.lang.ref.WeakReference;

public class Observer<L> {
    /**
     * 弱引用的support 让观察者失效后可以被gc
     */
    private WeakReference<ObserverSupport<L>> supportWeakReference;
    /**
     * 观察者接口实例
     */
    private L o;
    /**
     * 根据版本号有序地向下触发执行
     */
    private int version;

    /**
     * 如果为null 则手动控制失效
     */
    private ObserverInvalidType invalidType;


    Observer(ObserverSupport<L> support, L o, ObserverInvalidType invalidType, int version) {
        this.supportWeakReference = new WeakReference<>(support);
        this.o = o;
        this.invalidType = invalidType;
        this.version = version;
    }


    /**
     * 取消监听
     */
    public void cancel() {
        ObserverSupport<L> support = supportWeakReference.get();
        if(support == null) {
            return;
        }
        support.remove(this);
    }


    public L getO() {
        return o;
    }

    public int getVersion() {
        return version;
    }

    public ObserverInvalidType getInvalidType() {
        return invalidType;
    }
}
