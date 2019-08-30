package org.sq.gameDemo.svr.eventManage.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sq.gameDemo.svr.eventManage.Event;
import org.sq.gameDemo.svr.game.guild.model.Guild;

/**
 * 公会满人 物品满了
 */
@Data
@AllArgsConstructor
public class GuildFullEvent extends Event {
    Guild guild;

}
