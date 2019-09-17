package org.sq.gameDemo.svr.game.chat;


import com.google.common.base.Strings;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SenceService senceService;

    @OrderMapping(OrderEnum.CHAT)
    public void chatsingle(MsgEntity msgEntity,
                        @ReqParseParam MessageProto.MsgRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        if(Strings.isNullOrEmpty(requestInfo.getContent())) {
            senceService.notifyPlayerByDefault(player, "内容不能为空");
            return;
        }
        chatService.chatSingle(player, requestInfo.getTargetId(), requestInfo.getContent());
    }


    @OrderMapping(OrderEnum.TALK_TO_WORD)
    public void talkToWord(MsgEntity msgEntity,
                           @ReqParseParam MessageProto.MsgRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        if(Strings.isNullOrEmpty(requestInfo.getContent())) {
            senceService.notifyPlayerByDefault(player, "内容不能为空");
            return;
        }
        chatService.chatInWord(player, requestInfo.getContent());
    }
}
