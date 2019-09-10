package org.sq.gameDemo.svr.game.task.model.config;

public enum TaskType {

    /**
     * 任务类型
     * 1 杀怪类型
     * 2 功能性提升类型(装备，宠物，等级)
     * 3 收集类型
     * 4 通关副本类型
     * 5 对话类型
     * 6 赠送物品类型
     * 7 护送类型
     * 8 使用物品类型
     * 9 首次类型
     */
    KILLING(1, "杀怪类型"),
    FUNCTIONAL_UPGRADE(2, "功能性提升类型(装备，宠物，等级)"),
    COLLECTION(3, "收集类型"),
    CLEARANCE_COPY(4, "副本通关类型"),
    CONVERSATION(5, "对话类型"),
    FIRST(9, "首次类型"),


    ;
    private Integer typeId;
    private String describe;

    TaskType(Integer typeId, String describe) {
        this.typeId = typeId;
        this.describe = describe;
    }

    public Integer getTypeId() {
        return typeId;
    }
}
