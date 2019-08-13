package org.sq.gameDemo.svr.game.equip.controller;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.equip.service.EquitService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Map;
import java.util.Optional;

@Controller
public class EquipController {

    @Autowired
    private EquitService equitService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SenceService senceService;

    /**
     * 穿戴背包中的装备
     * @param msgEntity
     * @param requestInfo
     */
    @OrderMapping(OrderEnum.ADD_EQUIP)
    public MsgEntity addEquip(MsgEntity msgEntity,
                           @ReqParseParam ItemPt.ItemRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Map<Long, Item> itemBar = player.getBag().getItemBar();
        Item wanaEquip = itemBar.get(requestInfo.getId());

        String content = null;
        if(wanaEquip == null) {
            content = "背包中无此装备";
        } else if(!equitService.isEquip(wanaEquip)) {
            content = "该" + wanaEquip.getItemInfo().getName() + "不属于装备";
        }

        if(!Strings.isNullOrEmpty(content)) {
            senceService.notifyPlayerByDefault(player, content);
            return msgEntity;
        }
        equitService.addEquip(player, wanaEquip);
        return msgEntity;
    }



    /**
     * 玩家脱掉身上的装备
     * @param msgEntity
     * @param requestInfo 指定身体部位进行脱装备
     */
    @OrderMapping(OrderEnum.REMOVE_EQUIP)
    public MsgEntity removeEquip(MsgEntity msgEntity,
                         @ReqParseParam ItemPt.ItemRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Item equip = equitService.getEquipByPart(player, requestInfo.getPartValue());

        if(equip == null) {
            senceService.notifyPlayerByDefault(player, "无穿戴此装备");
            return msgEntity;
        }
        equitService.removeEquip(player, equip);
        return msgEntity;
    }

    @OrderMapping(OrderEnum.SHOW_EQUIP)
    public MsgEntity showEquip(MsgEntity msgEntity,
                          @ReqParseParam ItemPt.ItemRequestInfo requestInfo,
                          @RespBuilderParam ItemPt.ItemResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            equitService.showEquip(player, builder);
            msgEntity.setData(builder.build().toByteArray());
            return msgEntity;
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
            builder.setContent("服务端异常");
        } finally {
            builder.setMsgId(requestInfo.getMsgId())
                    .setTime(requestInfo.getTime());
        }
        return msgEntity;
    }


    @OrderMapping(OrderEnum.REPAIR_EQUIP)
    public void repairEquip(MsgEntity msgEntity,
                            @ReqParseParam ItemPt.ItemRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Optional<Item> find;
        find = player.getEquipmentBar().values().stream().filter(equip -> equip.getId() == requestInfo.getId()).findFirst();
        if(!find.isPresent()) {
            find = player.getBag().getItemBar().values().stream().filter(equip -> equip.getId() == requestInfo.getId()).findFirst();
            if(!find.isPresent()) {
                senceService.notifyPlayerByDefault(player, "装备不存在");
                return;
            }
        } else {
            int repairValue = requestInfo.getRepairValue();
            equitService.repairEquip(player, find.get(), repairValue <= 0 ?  (find.get().getItemInfo().getDurable()) : repairValue);
        }
    }



}
