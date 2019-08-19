package org.sq.gameDemo.svr.game.mail.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class MailConf {
    private Integer id;

    //保留时长
    private Long keepTime;

    //标题
    private String title;

    //内容
    private String content;

    //物品json字符串
    private String itemsStr = "{}";
    @Data
    public static class MailItemConf {
        private int id;
        private int num;
        private int level;
    }
}
