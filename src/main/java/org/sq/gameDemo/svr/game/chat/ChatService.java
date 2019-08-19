package org.sq.gameDemo.svr.game.chat;

import com.google.common.base.Strings;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.characterEntity.dao.PlayerCache;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

@Service
public class ChatService {

    @Autowired
    private PlayerCache playerCache;
    @Autowired
    private SenceService senceService;

    /**
     * 私信
     * @param sender
     * @param word
     */
    public void chatSingle(Player sender, Long recevierId, String word) {
        Channel receChannel = playerCache.getChannelByPlayerId(recevierId);
        if(receChannel == null) {
            senceService.notifyPlayerByDefault(sender, "对方离线");
            return;
        }
        if(Strings.isNullOrEmpty(word)) {
            senceService.notifyPlayerByDefault(sender, "不能发送空消息");
            return;
        }
        senceService.notifyPlayerByDefault(playerCache.getPlayerByChannel(receChannel),
                "id:" + sender.getId() + ", name:" + sender.getName() + "给你发消息: "+word);
    }

    /**
     * 世界喊话
     * @param sender
     * @param word
     */
    public void chatInWord(Player sender, String word) {
        senceService.notifyAllSence(sender, word);
    }




}
