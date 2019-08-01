package org.sq.gameDemo.svr.game.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.SkillPt;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

@Controller
public class FightController {


    @Autowired
    private FightService fightService;
    @Autowired
    private PlayerCache playerCache;

    @OrderMapping(OrderEnum.SkillAttack)
    public void kanguaiwu(MsgEntity msgEntity,
                          @ReqParseParam SkillPt.SkillReqInfo skillReqInfo) {
        Player player = playerCache.getPlayerByChannel(msgEntity.getChannel());
        fightService.skillAttackSingleMonster(player, skillReqInfo.getSkillId(), skillReqInfo.getTargetId());
    }

}
