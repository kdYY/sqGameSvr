package org.sq.gameDemo.svr.game.mail.model;

import lombok.Data;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.NpcPt;
import org.sq.gameDemo.svr.common.JsonUtil;
import org.sq.gameDemo.svr.common.poiUtil.ExcelFeild;
import org.sq.gameDemo.svr.common.protoUtil.ProtoField;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class Mail {

    private Integer id;

    private String senderName;
    private String recevierName;
    @ProtoField(Ignore = true)
    private Integer senderUnId;
    @ProtoField(Ignore = true)
    private Integer recevierUnId;


    //发送时间
    private Long time;

    //保留时长
    private Long keepTime;

    //标题
    private String title;

    //内容
    private String content;

    //物品json字符串

    @ProtoField(Ignore = true)
    private String itemsStr = "{}";

    public String getItemsStr() {
        if(rewardItems.size() != 0) {
            itemsStr = JsonUtil.serializableJson(rewardItems);
        }
        return itemsStr;
    }

    //物品集
    @ProtoField(TargetClass = ItemPt.Item.class, TargetName = "item")
    private List<Item> rewardItems = new ArrayList<>();

    //是否已读
    private Boolean isRead = false;

    public Mail() {
    }

    public Mail(Integer senderUnId, String senderName, Integer recevierUnId,String title, String content, List<Item> rewardItems) {
        this.senderUnId = senderUnId;
        this.senderName = senderName;
        this.recevierUnId = recevierUnId;
        this.time = System.currentTimeMillis();
        this.title = title;
        this.content = content;
        this.rewardItems = rewardItems;
    }
}
