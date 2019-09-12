package org.sq.gameDemo.svr.game.characterEntity.model;


import com.google.common.base.Strings;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.task.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Npc类
 */
public class Npc extends SenceEntity implements Character {

    @ProtoField(Ignore = true)
    public List<Integer> taskList = new ArrayList<>();

    public List<Integer> getTaskList() {
        if(!Strings.isNullOrEmpty(getTaskStr())) {
            taskList.addAll(JsonUtil.reSerializableJson(getTaskStr(), Integer.class));
        }
        return taskList;
    }
}
