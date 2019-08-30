package org.sq.gameDemo.svr.game.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.Ref;
import org.sq.gameDemo.svr.common.ThreadManager;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.mail.service.MailService;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.transaction.manager.TransactionCache;
import org.sq.gameDemo.svr.game.transaction.model.OnlineTrade;
import org.sq.gameDemo.svr.game.transaction.model.Trade;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnlineTradeService {



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
     * 发起   交易
     */
    public void startTrade(Player itemOwner, Long accpeterId, Long auctionItemId, Integer autionCount, Integer accpetItemCount, Integer
            accpetItemInfoId) {
        OnlineTrade onlineTrade = createOnlineTrade(itemOwner, accpeterId, auctionItemId, autionCount, accpetItemCount, accpetItemInfoId);
        if(onlineTrade == null) {
            return;
        }
        //将交易放入缓存
        if(!tradeService.insertTrade(onlineTrade)) {
            log.info("onlineTrace插入失败");
            return;
        }
        transactionCache.putOnlineTrade(onlineTrade);
        senceService.notifyPlayerByDefault(onlineTrade.getAccpeter(), itemOwner.getName() + "向你发起交易，交易号为 id=" + onlineTrade.getId());
    }



    //接收者接受
    public void accpetTrade(Player accpeter, int tradeId) throws CustomException.SystemSendMailErrException {
        OnlineTrade onlineTrade = transactionCache.getOnlineTrade(tradeId);
        if(onlineTrade == null) {
            senceService.notifyPlayerByDefault(accpeter, "此交易已超时或者完成");
            return;
        }
        synchronized (onlineTrade) {
            if(tryAccpetOnlineTrade(accpeter, tradeId)) {
                onlineTrade.setSuccess(true);
                senceService.notifyPlayerByDefault(accpeter, "交易成功");
            } else {
                onlineTrade.setSuccess(false);
                senceService.notifyPlayerByDefault(accpeter, "交易失败");

            }
            onlineTrade.setFinish(true);
            sendTradeMail(onlineTrade);
            transactionCache.removeOnlineTrade(onlineTrade);
        }
        //更新数据库
        tradeService.updateTrace(onlineTrade);
    }


    //交易超时
    public void offTimeTrace(OnlineTrade onlineTrade) {
        synchronized (onlineTrade) {
            try {
                onlineTrade.setSuccess(false);
                onlineTrade.setFinish(true);
                transactionCache.removeOnlineTrade(onlineTrade);
                sendTradeMail(onlineTrade);
                tradeService.updateTrace(onlineTrade);
            } catch (CustomException.SystemSendMailErrException e) {
                e.printStackTrace();
            }
        }
    }


    //发送交易结束的邮件
    private void sendTradeMail(OnlineTrade onlineTrade) throws CustomException.SystemSendMailErrException {
        String title = "面对面交易邮件";
        String content = "面对面交易"
                + (onlineTrade.isSuccess() ? "成功":"失败")
                + (onlineTrade.isFinish() ? "":", 原因:交易超时")
                + ", 这是您的物品。";

        onlineTrade.setFinish(true);

        Item ownerItem = onlineTrade.getAutionItemMap().get(onlineTrade.getOwnerUnId());
        Item acceptItem = onlineTrade.getAutionItemMap().get(onlineTrade.getAcceptUnId());
        if(!onlineTrade.isSuccess()) {
            //mailService.sendMail(entityService.getSystemPlayer(), onlineTrade.getAcceptUnId(), title, content, acceptItem);
            Optional.ofNullable(ownerItem).ifPresent(
                    item -> mailService.sendMail(entityService.getSystemPlayer(), onlineTrade.getOwnerUnId(), title, content, ownerItem));
        } else {
            Optional.ofNullable(ownerItem).ifPresent(
                    item -> mailService.sendMail(entityService.getSystemPlayer(), onlineTrade.getAcceptUnId(), title, content, ownerItem));
            Optional.ofNullable(acceptItem).ifPresent(
                    item ->  mailService.sendMail(entityService.getSystemPlayer(), onlineTrade.getOwnerUnId(), title, content, acceptItem));
        }

    }




    //检测接收者是否进行交易成功
    private boolean tryAccpetOnlineTrade(Player accpeter, int tradeId) {
        OnlineTrade onlineTrade = transactionCache.getOnlineTrade(tradeId);

        Item itemInBag = bagService.findItem(accpeter, onlineTrade.getItemInfoId(), onlineTrade.getCount());
        //交易失败
        if(itemInBag == null) {
            return false;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemInBag, item);
        item.setCount(onlineTrade.getCount());
        if(!bagService.removeItem(accpeter, item.getId(), onlineTrade.getCount())) {
           return false;
        }
        onlineTrade.getAutionItemMap().put(accpeter.getUnId(), item);
//        onlineTrade.setAcceptUnId(accpeter.getUnId());
//        onlineTrade.setAccpeter(accpeter);
        //交易成功
        return true;
    }




    /**
     * 创建交易
     */
    private OnlineTrade createOnlineTrade(Player itemOwner, Long accpeterId, Long auctionItemId, Integer autionCount, Integer
                                          accpetItemCount, Integer
                                         accpetItemInfoId) {
        Player accpeter = entityService.getPlayer(accpeterId);
        Ref<ItemInfo> itemInfoRef = new Ref<>();
        if(!checkTransaction(itemOwner, accpeter, auctionItemId, autionCount, accpetItemInfoId, itemInfoRef)) {
            return null;
        }

        Item item = bagService.findItem(itemOwner, auctionItemId, autionCount);
        if(bagService.removeItem(itemOwner, auctionItemId, autionCount)) {
            Item tradeItem = new Item();
            BeanUtils.copyProperties(item, tradeItem);
            tradeItem.setCount(autionCount);
            return new OnlineTrade(itemOwner, accpeter, tradeItem, itemInfoRef.ref, accpetItemCount);
        } else {
            senceService.notifyPlayerByDefault(itemOwner, "物品移除失败");
            return null;
        }
    }

    /**
     * 检查能否发起交易
     */
    private boolean checkTransaction(Player itemOwner, Player accpeter, Long auctionItemId, Integer count, Integer
            accpetItemInfoId, Ref<ItemInfo> itemInfoRef) {
        if(accpeter == null) {
            senceService.notifyPlayerByDefault(itemOwner, "在线交易失败，交易对象不在线");
            return false;
        }

        if(accpeter == itemOwner) {
            senceService.notifyPlayerByDefault(itemOwner, "不能和自己交易");
            return false;
        }

        Item item = bagService.findItem(itemOwner, auctionItemId, count);
        if(item == null) {
            return false;
        }

        ItemInfo itemInfo = bagService.getItemInfo(accpetItemInfoId);
        if(itemInfo == null) {
            senceService.notifyPlayerByDefault(itemOwner, "期望交易的物品种类不存在，请检查accpetItemInfoId");
            return false;
        }
        itemInfoRef.ref = itemInfo;
        return true;

    }


    //查看玩家发起交易
    public List<OnlineTrade> getTrace(Player player) {
        return transactionCache.onlineAsMap().values().stream()
                .filter(trade -> trade.getOwnerUnId().equals(player.getUnId()))
                .collect(Collectors.toList());
    }

    //查看玩家可接收的
    public List<OnlineTrade> getTraceCanAccept(Player player) {
        return transactionCache.onlineAsMap().values().stream()
                .filter(trade -> trade.getAcceptUnId().equals(player.getUnId()))
                .collect(Collectors.toList());
    }

    /**
     * 加载面对面交易
     * @param playerCached
     */
    public void loadTrace(Player playerCached) {
        ThreadManager.dbTaskPool.execute(() -> {
            List<Trade> trades = tradeService.selectOnlineByAccpetUnId(playerCached.getUnId());
            for (Trade trade : trades) {
                OnlineTrade onlineTrade = new OnlineTrade();
                BeanUtils.copyProperties(trade, onlineTrade);
                Integer itemInfoId = onlineTrade.getItemInfoId();
                onlineTrade.setAccpertItemInfo(bagService.getItemInfo(itemInfoId));
                onlineTrade.setAuctionItemId(onlineTrade.getAutionItemMap().get(onlineTrade.getOwnerUnId()).getId());
                transactionCache.putOnlineTrade(onlineTrade);
            }

            trades.forEach(trade -> senceService.notifyPlayerByDefault(playerCached,
                            "你有一项面对面交易未完成，交易号为 id=" + trade.getId()));
        });
    }

    public List<Trade> getTraceHistory(Player player) {
        return tradeService.selectFinishedOnlineTrade(player.getUnId());
    }
}
