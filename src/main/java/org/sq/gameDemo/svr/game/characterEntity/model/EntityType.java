package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.sq.gameDemo.common.proto.EntityTypeProto;
import org.sq.gameDemo.common.proto.SkillPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.skills.model.Skill;
import org.sq.gameDemo.svr.game.skills.service.SkillCache;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色类型，战士，法师...
 */
@Data
public class EntityType {
    private int id;
    private String name;

    //魔法攻击
    private int baseMPAttack;
    //物理攻击
    private int baseHPAttack;
    //防御值
    private int defense;

    @ProtoField(Ignore = true)
    private String skillStr;

    @ProtoField(TargetName = "skill", TargetClass = SkillPt.Skill.class)
    private List<Skill> skillList = new ArrayList<>();

    @ProtoField(TargetName = "type", Function = "getType", TargetClass = EntityTypeProto.EntityType.Builder.class)
    private String typeName; //AP型， AD型


    public void getType(EntityTypeProto.EntityType.Builder builder) {
        builder.setType(Constant.AP.equals(this.getTypeName()) ? EntityTypeProto.AttackType.AP : EntityTypeProto.AttackType.AD);
    }


    /**
     * 获取角色技能
     * @return
     */
    public List<Skill> getSkillList() {
        if(skillList.size() <= 0 && !this.skillStr.equals("")) {
            Arrays.stream(skillStr.split(",")).forEach(skillId -> {
                Optional.ofNullable(SkillCache.skillCache.getIfPresent(Integer.valueOf(skillId)))
                        .ifPresent(skill -> skillList.add(skill));
            });

        }
        return skillList;
    }

    @ProtoField(Ignore = true)
    private Map<Integer, Skill> skillMap;

    public Map<Integer, Skill> getSkillMap() {

        if(Objects.isNull(skillMap) && !this.skillStr.equals("")) {
            this.skillMap = this.getSkillList().stream().collect(Collectors.toMap(Skill::getId, skill -> skill));
        }
        return this.skillMap;
    }
}
