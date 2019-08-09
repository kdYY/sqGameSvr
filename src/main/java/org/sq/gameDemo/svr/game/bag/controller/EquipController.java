package org.sq.gameDemo.svr.game.bag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.BagPt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.EquitService;
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
    public void addEquip(MsgEntity msgEntity,
                           @ReqParseParam BagPt.BagReqInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Map<Long, Item> itemBar = player.getBag().getItemBar();
        Item wanaEquip = itemBar.get(requestInfo.getItemId());

        String content = null;
        if(wanaEquip == null) {
            content = "背包中无此装备";
        }

        if(!equitService.isEquip(wanaEquip)) {
            content = "该" + wanaEquip.getItemInfo().getName() + "不属于装备";
        }

        if(!content.isEmpty()) {
            senceService.notifyPlayerByDefault(player, content);
            return;
        }
        equitService.addEquip(player, wanaEquip);

    }



    /**
     * 玩家脱掉身上的装备
     * @param msgEntity
     * @param requestInfo 指定身体部位进行脱装备
     */
    @OrderMapping(OrderEnum.REMOVE_EQUIP)
    public void removeEquip(MsgEntity msgEntity,
                         @ReqParseParam BagPt.BagReqInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Item equip = equitService.getEquipByPart(player, requestInfo.getPart().getNumber());

        if(equip == null) {
            senceService.notifyPlayerByDefault(player, "无穿戴此装备");
            return;
        }
        equitService.removeEquip(player, equip);

    }

    @OrderMapping(OrderEnum.SHOW_EQUIP)
    public void showEquip(MsgEntity msgEntity) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        equitService.showEquip(player);
    }


    @OrderMapping(OrderEnum.REPAIR_EQUIP)
    public void repairEquip(MsgEntity msgEntity,
                            @ReqParseParam BagPt.BagReqInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Optional<Item> first = player.getEquipmentBar().values().stream().filter(equip -> equip.getId() == requestInfo.getItemId()).findFirst();
        if(!first.isPresent()) {
            equitService.repairEquip(player, first.get(), requestInfo.getDurable());
        } else {
            senceService.notifyPlayerByDefault(player, "装备不存在");
        }
    }



}
