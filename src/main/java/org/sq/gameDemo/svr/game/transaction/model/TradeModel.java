package org.sq.gameDemo.svr.game.transaction.model;

import lombok.Data;

public enum TradeModel {

    ONINE_TRADE(1, "即时交易"),
    BID(2, "一口价"),
    AT_AUCTION(3, "拍卖"),


    ;
    private Integer code;
    private String model;

    TradeModel(Integer code, String model) {
        this.code = code;
        this.model = model;
    }

    public Integer getCode() {
        return code;
    }

    public String getModel() {
        return model;
    }
}
