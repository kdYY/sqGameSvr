package org.sq.gameDemo.svr.game.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.bag.model.ItemInfo;
import org.sq.gameDemo.svr.game.bag.service.BagService;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.scene.service.SenceService;
import org.sq.gameDemo.svr.game.store.dao.StoreCache;
import org.sq.gameDemo.svr.game.store.model.Store;

import java.io.UTFDataFormatException;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreCache storeCache;
    @Autowired
    private SenceService senceService;
    @Autowired
    private BagService bagService;

    public Store showStore(Integer storeId) {
        return storeCache.get(storeId);
    }

    /**
     * 使用元宝购买物品
     * @param player
     * @param storeId
     */
    public void buyGood(Player player, Integer itemInfoId, Integer count, Integer storeId) {
        Store store = storeCache.get(storeId);
        if(store == null ) {
            senceService.notifyPlayerByDefault(player, "该商店(id="+ storeId +")编号不存在");
            return;
        }
        ItemInfo itemInfo = store.getGoodsMap().get(itemInfoId);
        if(itemInfo == null) {
            senceService.notifyPlayerByDefault(player, "该商店货物(id="+ itemInfoId +") 不存在");
            return;
        }

        int needMoney = itemInfo.getPrice() * count;

        Optional<Item> money = player.getBag().getItemBar().values().stream()
                .filter(item -> item.getItemInfo().getId().equals(Constant.YUAN_BAO)).findFirst();
        if(money.isPresent() && money.get().getCount() >= needMoney) {
            Item item = bagService.createItem(itemInfo.getId(), count, itemInfo.getLevel());
            bagService.addItemInBag(player, item);
            bagService.removeItem(player, money.get().getId(), needMoney);
        } else {
            senceService.notifyPlayerByDefault(player, "元宝不足， 该商店货物(id="+ itemInfo.getId() +") * " + count + "需要 元宝 * " +
                    needMoney);
            return;
        }

    }



}
