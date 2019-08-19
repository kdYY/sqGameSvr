package org.sq.gameDemo.svr.game.drop.model;

import lombok.Data;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ItemDropConf {

    private Integer itemInfoId;

    //触发掉落物品的等拾取者级范围 譬如 玩家等级
    private Integer minPickerLevel;
    private Integer maxPickerLevel;

    private String holderDrop;

    @ExcelFeild(Ignore = true)
    private List<HolderDrop> holderDropList = new CopyOnWriteArrayList<>();



    @Data
    public static class HolderDrop {
        //触发掉落的拥有者最大最小等级 譬如 怪物等级
        Integer min;
        Integer max;

        //最大掉落数量
        Integer maxCount;

        //最大掉落概率
        Integer prob;

    }
}
