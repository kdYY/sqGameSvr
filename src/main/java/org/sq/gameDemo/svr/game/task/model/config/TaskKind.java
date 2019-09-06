package org.sq.gameDemo.svr.game.task.model.config;

public enum TaskKind {
    /**
     * 成就: 0
     * 新手: 1
     * 主线: 2
     * 日常: 3
     * 支线: 4
     */
    ACHIEVE(0),
    NOVICE(1),
    MAINLINE(2),
    DAILY(3),
    BRANCHLINE(4)
    ;
    private Integer kind;
    TaskKind(Integer kind) {
        this.kind = kind;
    }
}
