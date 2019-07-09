package org.sq.gameDemo.svr.game.scene.model;

import org.sq.gameDemo.svr.game.entity.model.EntityVo;

import java.util.List;
import java.util.Map;

public class SenceData {
    private int senceId;
    private String jsonStr;
    private List<EntityVo> entitys;

    public List<EntityVo> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<EntityVo> entitys) {
        this.entitys = entitys;
    }

    public int getSenceId() {
        return senceId;
    }

    public void setSenceId(int senceId) {
        this.senceId = senceId;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    @Override
    public String toString() {
        return "SenceData{" +
                "senceId=" + senceId +
                ", jsonStr='" + jsonStr + '\'' +
                ", entitys=" + entitys +
                '}';
    }
}
