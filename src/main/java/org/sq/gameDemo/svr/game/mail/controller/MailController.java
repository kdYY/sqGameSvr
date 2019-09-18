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
import java.util.HashMap;
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
                Item itemInBag = bagService.findItem(player, item.getId(), item.getCount());
                if(itemInBag == null) {
                    return;
                }
            }

            mailService.playerSendMail(player, title, content, recevierName, itemSendList, itemIdList);

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
            transformMail(msgEntity, builder, mail);
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
        Mail mail = mailService.getMail(player, requestInfo.getId());
        transformMail(msgEntity, builder, mail);
        builder.setResult(Constant.SUCCESS);
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

    /**
     * 收取邮件
     */
    @OrderMapping(OrderEnum.RECEIVE_MAIL)
    public void receieveMail(MsgEntity msgEntity,
                             @ReqParseParam MailPt.MailRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        Mail mail = mailService.getMailInCache(player, requestInfo.getId());
        if(mail != null) {
            mailService.getMailItem(player, mail);
        }else {
            senceService.notifyPlayerByDefault(player, "id="+ requestInfo.getId() + " 的邮件不存在");
        }
    }

    /**
     * 转换proto
     * @param msgEntity
     * @param builder
     * @param mail
     */
    private void transformMail(MsgEntity msgEntity, @RespBuilderParam MailPt.MailResponseInfo.Builder builder, Mail mail) {
        try {
            if(mail != null) {
                builder.addMail((MailPt.Mail) ProtoBufUtil.transformProtoReturnBean(MailPt.Mail.newBuilder(), mail));
                builder.setResult(Constant.SUCCESS);
            } else {
                builder.setResult(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            builder.setResult(Constant.SVR_ERR);
        }
    }


}
