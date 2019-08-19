package org.sq.gameDemo.svr.game.mail.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.mail.dao.MailMapper;
import org.sq.gameDemo.svr.game.mail.model.Mail;
import org.sq.gameDemo.svr.game.mail.model.MailExample;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MailService {

    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private SenceService senceService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private BagService bagService;


    /**
     * 发送带道具附件的邮件
     */
    public void sendMailWithItem(Player sender, String recevierName, String title, String content, List<Item>
            rewardItems) throws CustomException.SystemSendMailErrException {
        Integer recevierUnId = entityService.getUnIdByName(recevierName);
        if(recevierUnId == null ) {
            if(!sender.getName().equals(Constant.SYSTEM_MANAGER)) {
                senceService.notifyPlayerByDefault(sender, "邮件对象不存在，请检查id");
            } else {
                log.info("系统发送邮件失败, unid=" + recevierUnId + "不存在");
            }
            return;
        }

        Mail mail = createMail(sender, recevierUnId, title, content, rewardItems);
        if(mail == null) {
            return;
        }

        int id = mailMapper.insertSelective(mail);
        if(id >= 0) {
            Player reciever = entityService.getPlayer(mail.getRecevierUnId());

            if(reciever == null && entityService.hasUserEntity(mail.getRecevierUnId())) {
                //玩家不在线
                senceService.notifyPlayerByDefault(sender, "玩家不在线，离线发送成功...");
            } else {
                senceService.notifyPlayerByDefault(sender, "发送成功");
                senceService.notifyPlayerByDefault(reciever, sender.getName() + "给你发送了一封邮件(id=" + mail.getId() + "), 使用 showMail 查看邮件吧");
            }
        } else {
            senceService.notifyPlayerByDefault(sender, "发送失败，系统错误");
        }

    }

    /**
     * 发送不带道具的邮件
     */
    public void sendMailWithoutItem(Player sender, String recevierName, String title, String content) throws CustomException
            .SystemSendMailErrException {
        sendMailWithItem(sender, recevierName, title, content, new ArrayList<>());
    }

    /**
     * 创建邮件
     */
    private Mail createMail(Player sender, Integer recevierUnId, String title, String content, List<Item>
            rewardItems) throws CustomException.SystemSendMailErrException {
        if(sender.getName().equals(Constant.SYSTEM_MANAGER)) {
            log.info("系统发送邮件");
            return new Mail(Constant.SYSTEM_UNID, sender.getName(), recevierUnId, title, content, rewardItems);
        }
        return new Mail(sender.getUnId(), sender.getName(), recevierUnId, title, content, rewardItems);
    }

    /*
    * 邮件列表
    * */
    public List<Mail> getMailList(Player player) {
        MailExample mailExample = new MailExample();
        mailExample.createCriteria().andRecevierUnIdEqualTo(player.getUnId());
        List<Mail> mail = mailMapper.selectByExample(mailExample);
        if(mail != null && mail.size() != 0) {
            return mail;
        } else {
            senceService.notifyPlayerByDefault(player, "邮箱为空");
            return null;
        }
    }

    /**
     * 获取邮件
     */
    public Mail getMail(Player player, Integer mailId) {
        Mail mail = mailMapper.selectByPrimaryKey(mailId);
        if(mail != null) {
            return mail;
        } else {
            senceService.notifyPlayerByDefault(player, "该邮件不存在了");
            return null;
        }
    }


    /**
     * 一键收取邮件
     */
    public void getAllMailItem(Player player) {
        List<Mail> mailList = getMailList(player);
        if(mailList != null) {
            mailList.forEach(mail -> {
                mail.getRewardItems().forEach(item -> bagService.addItemInBag(player, item));
            });
        }
    }

}
