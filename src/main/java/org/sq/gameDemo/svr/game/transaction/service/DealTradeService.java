package org.sq.gameDemo.svr.game.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.Ref;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.transaction.manager.TransactionCache;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;
import org.sq.gameDemo.svr.game.transaction.model.TradeModel;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DealTradeService {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TransactionCache transactionCache;

    @Autowired
    private EntityService entityService;

    @Autowired
    private SenceService senceService;

    @Autowired
    private BagService bagService;

    @Autowired
    private MailService mailService;

    /**
     * 参与竞拍
     */
    public void accpetTrace(Player accpeter, Integer tradeId, Integer price) throws CustomException.SystemSendMailErrException {
        Item itemInBag = bagService.findItem(accpeter, Constant.YUAN_BAO, price);
        if(itemInBag == null) {
            return;
        }
        DealTrade dealInCache = transactionCache.getDealInCache(tradeId);
        synchronized (dealInCache) {
            Integer nowMaPrice = tradeService.getMaxPriceInTrade(dealInCache);
            if(price < nowMaPrice) {
                senceService.notifyPlayerByDefault(accpeter, "您出的元宝数量低于该物品目前的价格price=" + dealInCache.getPrice());
                return;
            }
            if(!bagService.removeItem(accpeter, itemInBag.getId(), price)) {
                return;
            }

            Item tradeItem = dealInCache.getAutionItemMap().get(accpeter.getUnId());
            if(tradeItem != null) {
                tradeItem.setCount(tradeItem.getCount() + price);
            } else{
                Item item = new Item();
                BeanUtils.copyProperties(itemInBag, item);
                item.setCount(price);
                dealInCache.getAutionItemMap().put(accpeter.getUnId(), item);

            }
            //竞拍
            if(dealInCache.getTradeModel().equals(TradeModel.AT_AUCTION.getCode())) {
                senceService.notifyPlayerByDefault(accpeter, "竞拍成功");
            }
            //一口价
            else {
                senceService.notifyPlayerByDefault(accpeter, "购买成功");
                dealInCache.setFinish(true);
                dealInCache.setSuccess(true);
                transactionCache.removeDeal(dealInCache);
                dealInCache.setAcceptUnId(accpeter.getUnId());
                sendDealMail(dealInCache);
                tradeService.updateTrace(dealInCache);
            }

        }

    }

    /**
     * 发送交易邮件
     * @param dealInCache
     * @throws CustomException.SystemSendMailErrException
     */
    private void sendDealMail(DealTrade dealInCache) throws CustomException.SystemSendMailErrException {
        String title = "交易栏" + (dealInCache.getTradeModel().equals(TradeModel.BID.getCode())? "一口价" : "竞拍" ) + "邮件";
        String content = "一口价竞拍交易"
                + (dealInCache.isSuccess() ? "成功":"失败, 系统自动返回交易物品")
                + (dealInCache.isFinish() ? "":", 原因:交易超时")
                + ", 这是您此次交易的物品。";
        //竞拍获胜者设置
        if(dealInCache.isFinish()
                && dealInCache.isSuccess()
                && dealInCache.getTradeModel().equals(TradeModel.AT_AUCTION.getCode())) {
            //找出最高价unid
            Integer maxPriceInTrade = tradeService.getMaxPriceInTrade(dealInCache);
            Integer acceptUnId = dealInCache.getAutionItemMap().entrySet().stream()
                    .filter(entry -> entry.getValue().getCount().equals(maxPriceInTrade))
                    .findFirst()
                    .get()
                    .getKey();
            dealInCache.setAcceptUnId(acceptUnId);
        } else {
            dealInCache.setFinish(true);
        }
        Integer ownerUnId = dealInCache.getOwnerUnId();
        Integer acceptUnId = dealInCache.getAcceptUnId();
        Item ownerItem = dealInCache.getAutionItemMap().get(ownerUnId);
        Item acceptItem = dealInCache.getAutionItemMap().get(acceptUnId);
        if(dealInCache.isSuccess()) {
            //物品给最高价提供者
            mailService.sendMail(entityService.getSystemPlayer(), acceptUnId, title, content, ownerItem);
            //最高价给owner
            mailService.sendMail(entityService.getSystemPlayer(), ownerUnId, title, content, acceptItem);

            //竞拍中 其余的返回
            if(dealInCache.getTradeModel().equals(TradeModel.AT_AUCTION.getCode())) {
                List<Map.Entry<Integer, Item>> collect = dealInCache.getAutionItemMap().entrySet().stream().filter(entry -> !(entry.getKey().equals(ownerUnId) || entry.getKey()
                        .equals(acceptUnId))).collect(Collectors.toList());
                for (Map.Entry<Integer, Item> itemEntry : collect) {
                    mailService.sendMail(entityService.getSystemPlayer(), itemEntry.getKey(), title, content, itemEntry.getValue());
                }
            }
        } else {
            mailService.sendMail(entityService.getSystemPlayer(), acceptUnId, title, content,ownerItem);
            //竞拍不成功的时候 一口价没人买 竞拍超时没人拍
        }


}


    /**
     * 发起交易
     */
    public void startTrace(Player itemOwner,  Long auctionItemId, Integer count, Integer price, Integer tradeModel) {
        Ref<Item> itemref = new Ref<>();
        DealTrade dealTrade = createDeal(itemOwner, auctionItemId, count, price, tradeModel, itemref);
        if(dealTrade == null) {
            return;
        }
        if(!tradeService.insertTrade(dealTrade)) {
            log.info("dealTrace插入失败");
            return;
        }
        transactionCache.putDeal(dealTrade);

        senceService.notifyPlayerByDefault(itemOwner, "物品已放入交易栏，交易号id="+ dealTrade.getId()  + "， 使用showDeal (id=#{交易号})查看交易栏吧");
        senceService.notifyAllSence(itemOwner,
                itemref.ref.getItemInfo().getName()
                        +" (level=" + itemref.ref.getLevel() + "), "
                        + (tradeModel.equals(TradeModel.BID.getCode())?"一口价":"拍卖底价") + price
                        + ", 已放入交易栏，交易号id="+ dealTrade.getId()
                        + "使用showDeal " + "id="+ dealTrade.getId()+" 查看交易栏吧");

    }


    /**
     * 创建拍卖
      * @return
     */
    private DealTrade createDeal(Player itemOwner,  Long auctionItemId, Integer count, Integer price, Integer
            tradeModel, Ref<Item> itemRef) {
        if(!tryCreateDeal(itemOwner, auctionItemId, count, price, itemRef, tradeModel)) {
            return null;
        }
        if(bagService.removeItem(itemOwner, auctionItemId, count)) {
            Item tradeItem = new Item();
            BeanUtils.copyProperties(itemRef.ref, tradeItem);
            tradeItem.setCount(count);
            return new DealTrade(itemOwner.getUnId(), tradeItem, price, tradeModel);
        } else {
            senceService.notifyPlayerByDefault(itemOwner, "物品移除失败");
            return null;
        }
    }

    /**
     * 检测是否创建交易
     */
    private boolean tryCreateDeal(Player itemOwner, Long auctionItemId, Integer count, Integer price, Ref<Item> itemRef, Integer
            tradeModel) {
        Item item = bagService.findItem(itemOwner, auctionItemId, count);
        if(item == null) {
            return false;
        }
        if(price > item.getItemInfo().getPrice() * 2 || price < item.getItemInfo().getPrice() / 2) {
            senceService.notifyPlayerByDefault(itemOwner, "起拍价不能大于原物品价格的2倍, 也不能小于原物品价格的一半");
            return false;
        }
        if(!(tradeModel.equals(TradeModel.BID.getCode()) || tradeModel.equals(TradeModel.AT_AUCTION.getCode()))) {
            senceService.notifyPlayerByDefault(itemOwner, "tradeModel参数错误，2为一口价， 3为拍卖");
            return false;
        }

        itemRef.ref = item;
        return true;
    }


    /**
     * 拍卖到期操作
     */
    public synchronized void offTime(DealTrade trade)  {
        try {
            transactionCache.removeDeal(trade);
            if(trade.getTradeModel().equals(TradeModel.AT_AUCTION.getCode()) && trade.getAutionItemMap().size() != 0) {
                trade.setSuccess(true);
                trade.setFinish(true);
            }
            sendDealMail(trade);
        } catch (CustomException.SystemSendMailErrException e) {
            e.printStackTrace();
        }
        tradeService.updateTrace(trade);
    }

}
