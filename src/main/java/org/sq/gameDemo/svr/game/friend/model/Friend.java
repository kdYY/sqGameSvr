package org.sq.gameDemo.svr.game.friend.model;

import lombok.Data;

@Data
public class Friend {
    private Integer unId;
    private String name;
    private String typeName;
    private boolean isOnline = false;

    public Friend() {
    }

    public Friend(Integer unId, String name, String typeName) {
        this.unId = unId;
        this.name = name;
        this.typeName = typeName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}
