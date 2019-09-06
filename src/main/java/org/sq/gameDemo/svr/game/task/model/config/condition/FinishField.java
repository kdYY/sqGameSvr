package org.sq.gameDemo.svr.game.task.model.config.condition;

/**
 * 任务完成条件中的条件域含义
 */
public enum FinishField {

    ENTITY_TYPE(1, "匹配场景实体类型id"),
    ITEMINFO_ID(2, "匹配物品类型id"),
    COPY_SCENE_ID(3, "匹配副本场景类型id"),
    LEVEL(4, "满足各种等级"),
    FIRST(5, "首次完成"),

    ;
    int field;
    String meaning;

    FinishField(int field, String meaning) {
        this.field = field;
        this.meaning = meaning;
    }
}
