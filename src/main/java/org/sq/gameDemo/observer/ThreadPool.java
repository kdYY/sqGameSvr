package org.sq.gameDemo.observer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

public class ThreadPool {
    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("event-%d").build();

    public static ThreadFactory getSceneThreadFactory() {
        return sceneThreadFactory;
    }
}
