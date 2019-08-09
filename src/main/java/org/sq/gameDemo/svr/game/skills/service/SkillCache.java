package org.sq.gameDemo.svr.game.skills.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.common.poiUtil.PoiUtil;
import org.sq.gameDemo.svr.game.skills.model.Skill;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SkillCache {
    @Value("${excel.skill}")
    private String fileName;

    public static Cache<Integer,Skill> skillCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    cacheObj -> log.debug(cacheObj.getKey() + "基础角色被移除, 原因是" + cacheObj.getCause())
            ).build();


    //存储所有角色类型
    private List<Skill> skills;

    @PostConstruct
    private void init() throws Exception {

        List<Skill> skillList = PoiUtil.readExcel(fileName, 0, Skill.class);
        for (Skill type : skillList) {
            put(type.getId(), type);
        }
        skills = skillCache.asMap().values().stream().collect(Collectors.toList());
        log.info("基础角色表加载完毕");
    }

    public List<Skill> getAllSkills() {
        return skills;
    }

    public Skill get(Integer id) {
        return skillCache.getIfPresent(id);
    }

    public void put(Integer id, Skill value) {
        skillCache.put(id,value);
    }

    public Map<Integer,Skill> skillMap() {
        return skillCache.asMap();
    }
}
