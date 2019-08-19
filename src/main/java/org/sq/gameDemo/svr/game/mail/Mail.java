package org.sq.gameDemo.svr.game.mail;

import lombok.Data;
import org.sq.gameDemo.svr.game.bag.model.Item;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.util.List;

@Data
public class Mail {

    private Player sender;
    private Long time;
    private String title;
    private String content;
    private List<Item> rewardItems;




}
