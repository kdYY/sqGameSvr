package org.sq.gameDemo.svr.game.task.model.config.condition;

/**
 * 任务完成条件中的条件域含义
 */
public enum FinishField {

    ENTITY_TYPE(1, "匹配场景实体类型id"),
    ITEMINFO_ID(2, "匹配物品类型id"),
    COPY_SCENE_ID(3, "匹配副本场景类型id"),
    ALL_EQUIP_LEVEL(4, "所有装备等级之和"),
    PLAYER_LEVEL(6, "人物等级"),

    FIRST(5, "首次完成"),

    ;
    int field;
    String meaning;

    FinishField(int field, String meaning) {
        this.field = field;
        this.meaning = meaning;
    }

    public int getField() {
        return field;
    }
}
