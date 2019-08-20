package org.sq.gameDemo.svr.game.bag.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.roleAttribute.model.RoleAttribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ItemInfo {

    private Integer id;

    private String name;

    private Integer buff;

    @ProtoField(Ignore = true)
    private Integer level;
    private Integer type;

    @ProtoField(TargetName = "part", Function = "transformPart", TargetClass = ItemInfoPt.ItemInfo.Builder.class)
    private Integer part;

    public void transformPart(ItemInfoPt.ItemInfo.Builder builder) {
        builder.setPart(ItemInfoPt.EquipPart.forNumber(this.getPart()));
    }

    private Integer price;

    private String describe;

    private Long durable;

    private String jsonStr;


    @ProtoField(Ignore = true)
    private Integer repairPrice;

    @ProtoField(Ignore = true)
    @ExcelFeild(Ignore = true)
    private Map<Integer, RoleAttribute> itemRoleAttribute = new ConcurrentHashMap<>();

}
