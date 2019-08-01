package org.sq.gameDemo.svr.eventManage.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.eventManage.EventBus;
import org.sq.gameDemo.svr.eventManage.event.LevelEvent;
import org.sq.gameDemo.svr.eventManage.event.MonsterBeAttackedEvent;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.Optional;

/**
 * @author gonefuture  gonefuture@qq.com
 * time 2019/1/2 15:32
 * @version 1.00
 * Description: 等级事件处理
 */

@Component
@Slf4j
public class MonsterBeAttackedEventHandler {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SenceService senceService;

    {
        EventBus.registe(MonsterBeAttackedEvent.class, this::monsterBeAttacked);
        log.info("怪物被攻击事件注册成功");
    }


    private  void monsterBeAttacked(MonsterBeAttackedEvent attackedEvent) {
        //怪物被攻击，开始计时仇恨有效
        attackedEvent.getTargetMonster().setTarget(attackedEvent.getAttacter());


        //仇恨有效期一过，设置怪物移动状态
    }



}
