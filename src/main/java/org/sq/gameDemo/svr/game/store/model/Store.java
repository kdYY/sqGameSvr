package org.sq.gameDemo.svr.game.store.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.ItemInfoPt;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Store {
    private Integer id;
    private String name;

    @ProtoField(Ignore = true)
    private String goods;

    @ProtoField(Ignore = true)
    @ExcelFeild(Ignore = true)
    private Map<Integer, ItemInfo> goodsMap = new ConcurrentHashMap<>();

    @ExcelFeild(Ignore = true)
    @ProtoField(TargetClass = ItemInfoPt.ItemInfo.class, TargetName = "itemInfo")
    private List<ItemInfo> itemInfoList = new CopyOnWriteArrayList<>();

}
