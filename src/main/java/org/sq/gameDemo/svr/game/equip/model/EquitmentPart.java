package org.sq.gameDemo.svr.game.equip.model;

public enum EquitmentPart {

    /**
     * 头:0
     * 身体:1
     * 手:2
     * 手指:3
     * 腰:4
     * 脚:5
     * 背包:9
     */
    HEAD(0, "头部"),
    BODY(1, "身体"),
    HADN(2, "手"),
    FINGER(3, "手指"),
    WAIST(4, "腰"),
    FOOT(5, "脚"),
    PACKAGE(9, "背包"),

    ;
    private String part;
    private Integer code;

    EquitmentPart(Integer code, String part) {
        this.part = part;
        this.code = code;
    }

    public String getPart() {
        return part;
    }

    public Integer getCode() {
        return code;
    }

    public static String getPartByCode(Integer code) {
        for (EquitmentPart par : EquitmentPart.values()) {
            if(par.getCode().equals(code)) {
                return par.getPart();
            }
        }
        return null;
    }
}
