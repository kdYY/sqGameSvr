package org.sq.gameDemo.svr.game.scene.model;

import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.common.proto.SenceProto;

import java.lang.reflect.Method;

public class GameScene {
    private int id;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "场景信息:{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    //记得写个工具类...免得老是写这些恶心的方法...
    public static SenceProto.Sence transformProto(GameScene sence) {
        return SenceProto.Sence.newBuilder().setId(sence.getId()).setName(sence.getName()).build();
    }


}
