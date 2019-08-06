package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import java.util.HashMap;
import java.util.Map;

@Data
public class ThingInfo {

    private Integer id;

    private String name;

    private Integer buffer;

    private  Integer level;

    private Integer type;

    private Integer part;

    private Integer price;

    private String describe;

    @ProtoField(Ignore = true)
    private String jsonStr;
    @ProtoField(Ignore = true)
    private Integer state;
    @ProtoField(Ignore = true)
    private Map<Integer, RoleAttribute> thingRoleAttribute = new HashMap<>();


}
