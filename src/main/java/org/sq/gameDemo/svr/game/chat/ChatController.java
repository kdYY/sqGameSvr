package org.sq.gameDemo.svr.game.chat;


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

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private PlayerCache playerCache;

    @OrderMapping(OrderEnum.CHAT)
    public void chatsingle(MsgEntity msgEntity,
                        @ReqParseParam MessageProto.MsgRequestInfo requestInfo) {
        chatService.chatSingle(playerCache.getPlayerByChannel(msgEntity.getChannel()), requestInfo.getTargetId(), requestInfo.getContent());
    }


    @OrderMapping(OrderEnum.TALK_TO_WORD)
    public void talkToWord(MsgEntity msgEntity,
                           @ReqParseParam MessageProto.MsgRequestInfo requestInfo) {
        chatService.chatInWord(playerCache.getPlayerByChannel(msgEntity.getChannel()), requestInfo.getContent());
    }
}
