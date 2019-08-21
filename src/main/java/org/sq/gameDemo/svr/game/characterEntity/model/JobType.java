package org.sq.gameDemo.svr.game.characterEntity.model;

public enum JobType {

    WARRIOR(0, "战士"),
    Priest(1, "牧师"),
    MAGE(2, "法师"),

    ADC(3, "射手"),
    KNIGHT(4, "龙骑士"),
    SUMMONER(5, "召唤师")
    ;

    private Integer id;
    private String name;

    JobType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getType() {
        return id;
    }
}
