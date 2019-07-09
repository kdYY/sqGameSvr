package org.sq.gameDemo.svr.game.scene.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.proto.SenceProto;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.PoiUtil;
import org.sq.gameDemo.svr.game.entity.model.Entity;
import org.sq.gameDemo.svr.game.entity.model.EntityVo;
import org.sq.gameDemo.svr.game.entity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.model.GameScene;
import org.sq.gameDemo.svr.game.scene.model.SenceData;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    //存储单体场景信息
    private List<GameScene> gameScenes;
    //存储所有场景
    private List<SenceData> senceData;
    //
    @Value("${excel.entity}")
    private String entityFileName;

    //存储所有的实体信息
    private List<Entity> entities;

    @PostConstruct
    private void initalSence() {
        try {
            gameScenes = PoiUtil.readExcel(senceFileName, 0, GameScene.class);
            senceData = PoiUtil.readExcel(sencedataFileName, 0, SenceData.class);
            entities = PoiUtil.readExcel(entityFileName, 0, Entity.class);
            //{"1":10,"3":10, "4":20}
            senceData.forEach(data -> {
                JSONObject jsonObject = JsonUtil.parseObject(data.getJsonStr());

                List<EntityVo> entityVos = new ArrayList<>();
                jsonObject.keySet().forEach(key -> {
                    EntityVo entityVo = new EntityVo();
                    entityVo.setEntity(findEntity(entities, Integer.valueOf(key)));
                    entityVo.setNum(jsonObject.getIntValue(key));
                    entityVos.add(entityVo);
                });
                data.setEntitys(entityVos);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SenceData getSenecDataById(int senceId) {
        for (SenceData senceData : senceData) {
            if(senceData.getSenceId() == senceId) {
                return senceData;
            }
        }
        return null;
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
    public SenceProto.Sence getSence() {
        return null;
    }



}
