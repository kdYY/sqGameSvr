package org.sq.gameDemo.svr.game.characterEntity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Baby extends Monster {

    //主人
    private Player master;




}
