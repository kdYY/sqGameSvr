package org.sq.gameDemo.svr.game.task.model.config.condition;

/**
 * 功能性提升任务类型中target的代表含义
 */
public enum LevelTarget {

    EQUIPALL(1, "全身装备等级"),
    PLAYER(2, "人物等级"),
    BABY(3, "宝宝星级"),

    ;
    int code;
    String meaning;

    LevelTarget(int code, String meaning) {
        this.code = code;
        this.meaning = meaning;
    }

    public int getCode() {
        return code;
    }
}
