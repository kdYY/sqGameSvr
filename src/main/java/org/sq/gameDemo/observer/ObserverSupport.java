package org.sq.gameDemo.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ObserverSupport<L> {

    /**
     * 监听的观察者接口
     */
    private Class<L> listenerClass;

    /**
     * 观察者与失效接口
     */
    private List<Observer<L>> observers;


    public ObserverSupport(Class<L> listenerClass) {
        this.listenerClass = listenerClass;
        this.observers = new CopyOnWriteArrayList<>();
    }

    /**
     * 添加观察者
     * @param o 观察者
     * @param invalidType 失效方式
     * @param version 版本号
     * @param <O> 类型约束
     * @return
     */
    public <O extends L> Observer<L> attach(O o, ObserverInvalidType invalidType, int version) {
        Observer<L> observer = new Observer<>(this, o, invalidType, version);
        observers.add(observer);
        return observer;
    }

    /**
     * 移除观察者
     * @param observer
     */
    public <O extends L> void remove(Observer<L> observer) {
        observers.remove(observer);
    }

    public void fire(int version, Consumer<L> consumer) {
        //抛出事件
        Observer[] observerCache = this.observers.toArray(new Observer[0]);
        int begin = ThreadLocalRandom.current().nextInt(0, observerCache.length);
        List<Observer> removes = new ArrayList<>(observers.size());
        for (int i = 0; i < observerCache.length; i++) {
            int index = (i + begin) % observerCache.length;
            Observer<L> observer = observerCache[index];
            if(observer.getVersion() >= version) {
                continue;
            }
            try {
                consumer.accept(observer.getO());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(observer.getInvalidType() == ObserverInvalidType.oneOff) {
                    removes.add(observer);
                }
            }
        }
        if(!removes.isEmpty()) {
            this.observers.removeAll(removes);
        }
    }


    /**
     * 移除对应失效条件的观察者
     * @param invalidType
     * @param version
     */
    public void tryRemoveInvalid(ObserverInvalidType invalidType, int version) {
        List<Observer> removes = new ArrayList<>(observers.size());
        for (Observer<L> observer : observers) {
            if(observer.getInvalidType() != invalidType) {
                continue;
            }
            if(observer.getVersion() >= version) {
                continue;
            }
            removes.add(observer);
        }
        if(removes.isEmpty()) {
            return;
        }
        this.observers.removeAll(removes);
    }


    /**
     * 是否没有观察者
     * @return
     */
    public boolean isEmpty() {
        return observers.isEmpty();
    }


    public Class<L> getListenerClass() {
        return listenerClass;
    }

    public static void main(String[] args) {
        for (;;) {
            int begin = ThreadLocalRandom.current().nextInt(0, 4);
            System.out.println(begin);
        }

    }

}
