package org.sq.gameDemo.svr.game.entity.model;

public class EntityVo {
    private Entity entity;
    private Integer num;


    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "EntityVo{" +
                "entity=" + entity +
                ", num=" + num +
                '}';
    }
}
