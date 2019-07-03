package org.sq.gameDemo.svr.game.scene.model;

public class GameScene {
    public int id;
    public String name;


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
        return "GameScene{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
