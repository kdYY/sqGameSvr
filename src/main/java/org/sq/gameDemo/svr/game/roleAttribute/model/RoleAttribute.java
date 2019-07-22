package org.sq.gameDemo.svr.game.roleAttribute.model;

import lombok.Data;

@Data
public class RoleAttribute {
    //ID
    Integer id;

    //属性名
    String name;

    //属性值
    Integer value;

    //种类
    Integer typeId;

    //描述
    String description;


}
