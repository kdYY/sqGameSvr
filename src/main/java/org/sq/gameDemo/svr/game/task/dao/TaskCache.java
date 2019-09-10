package org.sq.gameDemo.svr.game.task.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.task.model.Task;
import org.sq.gameDemo.svr.game.task.model.config.FinishCondition;
import org.sq.gameDemo.svr.game.task.model.config.TaskReward;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TaskCache {
    @Value("${excel.task}")
    private String fileName;

    public static Cache<Integer,Task> taskCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.info(cacheObj.getKey() + "基础角色被移除, 原因是" + cacheObj.getCause())
            ).build();


    @PostConstruct
    private void init() throws Exception {

        List<Task> taskList = PoiUtil.readExcel(fileName, 0, Task.class);
        for (Task type : taskList) {
            put(type.getId(), type);
        }
        log.info("任务成就表加载完毕");
    }

    public Map<Integer,Task> asMap() {
        return taskCache.asMap();
    }

    public Task get(Integer id) {
        return taskCache.getIfPresent(id);
    }

    public void put(Integer id, Task value) {
        taskCache.put(id,value);
    }

}
