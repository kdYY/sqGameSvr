package org.sq.gameDemo.svr.game.mail.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.ItemPt;
import org.sq.gameDemo.common.proto.MailPt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.common.protoUtil.ProtoBufUtil;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.mail.model.Mail;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private BagService bagService;
    @Autowired
    private SenceService senceService;

    /**
     * 发送邮件
     * @param msgEntity
     * @param requestInfo
     */
    @OrderMapping(OrderEnum.SEND_MAIL)
    public void sendMail(MsgEntity msgEntity,
                                    @ReqParseParam MailPt.MailRequestInfo requestInfo) {

        Player player = entityService.getPlayer(msgEntity.getChannel());
        try {
            String title = requestInfo.getMail().getTitle();
            String content = requestInfo.getMail().getContent();
            String recevierName = requestInfo.getMail().getRecevierName();
            List<Item> itemSendList = new ArrayList<>();
            List<ItemPt.Item> itemIdList = requestInfo.getItemList();

            for (ItemPt.Item item : itemIdList) {
                Item itemInBag = player.getBag().getItemBar().get(item.getId());
                if(itemInBag == null) {
                    senceService.notifyPlayerByDefault(player, "发送失败，id=" + itemInBag.getId()  +" 的物品不存在");
                    return;
                } else {
                    if(itemInBag.getCount() < item.getCount()) {
                        senceService.notifyPlayerByDefault(player, "发送失败，id=" + itemInBag.getId()  +" 的物品数量不足");
                        return;
                    }
                }
            }

            for (ItemPt.Item item : itemIdList) {
                Item itemInBag = player.getBag().getItemBar().get(item.getId());
                Item itemSend = new Item();
                BeanUtils.copyProperties(itemInBag, itemSend);
                itemSend.setCount(item.getCount());

                bagService.removeItem(player, itemInBag.getId(), item.getCount());
                itemSendList.add(itemSend);
            }

            mailService.sendMail(player, recevierName, title, content, itemSendList);

        } catch (Exception e) {
            e.printStackTrace();
        } catch (CustomException.SystemSendMailErrException e) {
            e.printStackTrace();
        }

    }

    /**
     * 展示邮件列表
     * @param msgEntity
     * @param builder
     * @return
     */
    @OrderMapping(OrderEnum.SHOW_ALL_MAIL)
    public MsgEntity showMailList(MsgEntity msgEntity,
                             @RespBuilderParam MailPt.MailResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<Mail> mailList = mailService.getMailList(player);
        mailList.forEach(mail -> {
            try {
                builder.addMail((MailPt.Mail) ProtoBufUtil.transformProtoReturnBean(MailPt.Mail.newBuilder(), mail));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 获取邮件内容
     */
    @OrderMapping(OrderEnum.GET_MAIL)
    public MsgEntity getMail(MsgEntity msgEntity,
                        @ReqParseParam MailPt.MailRequestInfo requestInfo,
                        @RespBuilderParam MailPt.MailResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Mail mail = mailService.getMail(player, requestInfo.getMail().getId());
        try {
            builder.addMail((MailPt.Mail) ProtoBufUtil.transformProtoReturnBean(MailPt.Mail.newBuilder(), mail));
            mail.setIsRead(true);
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }



    /**
     * 一键收取邮件
     */
    @OrderMapping(OrderEnum.RECEIVE_ALL_MAIL)
    public void receieveAllMail(MsgEntity msgEntity) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        mailService.getAllMailItem(player);
    }

}
