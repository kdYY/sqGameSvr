package org.sq.gameDemo.svr.game.scene.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.JsonUtil;
import org.sq.gameDemo.common.PoiUtil;
import org.sq.gameDemo.svr.game.entity.model.Entity;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceData;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SenceService {

    @Autowired
    private EntityService entityService;

    @Value("${excel.sence}")
    private String senceFileName;

    @Value("${excel.sencedata}")
    private String sencedataFileName;

    //存储所有的实体信息
    private List<GameScene> gameScenes;
    //存储实体和场景的映射关系
    private List<SenceData> senceData;


    private Map<GameScene,Map<Entity, Integer>> gameSceneMap = new HashMap<>();

    @PostConstruct
    private void initalSence() {
        try {
            gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
            senceData = PoiUtil.readExcel(sencedataFileName, 0, SenceData.class);

            senceData.forEach(map -> {
                JSONObject jsonObject = JsonUtil.parseObject(map.getJsonStr());
                Map<Entity, Integer> entityMap = new HashMap<>();
                jsonObject.keySet().forEach(key -> {
                    entityMap.put(findEntity(entityService.getEntities(), Integer.valueOf(key)),jsonObject.getIntValue(key));
                });
                gameSceneMap.put(findSence(gameScenes, map.getSenceId()),entityMap);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<GameScene, Map<Entity, Integer>> getGameSceneMap() {
        return gameSceneMap;
    }


    public static Entity findEntity(List<Entity> entityList, int id) {
        for (Entity entity : entityList) {
            if(entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    public static GameScene findSence(List<GameScene> gameScenes, Integer id) {
        for (GameScene sence : gameScenes) {
            if(sence.getId() == id) {
                return sence;
            }
        }
        return null;
    }

    //创建用户 角色  根据id创建一个entity， 默认在场景 id = 1起源之地

}
