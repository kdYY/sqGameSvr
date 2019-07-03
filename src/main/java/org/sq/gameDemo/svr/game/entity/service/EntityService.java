package org.sq.gameDemo.svr.game.entity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.PoiUtil;
import org.sq.gameDemo.svr.game.entity.model.Entity;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@Service
public class EntityService {

    @Value("${excel.entity}")
    private String entityFileName;

    //存储所有的实体信息
    private List<Entity> entities;

    @PostConstruct
    public void initalEntity() {
        try {
            entities = PoiUtil.readExcel(entityFileName, 0, Entity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
