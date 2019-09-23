/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : gamesever

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-09-23 10:24:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bag
-- ----------------------------
DROP TABLE IF EXISTS `bag`;
CREATE TABLE `bag` (
  `un_id` int(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `item_str` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  PRIMARY KEY (`un_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bag
-- ----------------------------
INSERT INTO `bag` VALUES ('1', '背包栏', '100', '{6887237653851410432:{\"count\":492,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6887240352932171776:{\"count\":472952,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6887240349836775424:{\"count\":107,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}');
INSERT INTO `bag` VALUES ('6', '背包栏', '100', '{6887237653851410432:{\"count\":298,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6887240349836775424:{\"count\":96,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6887240352932171776:{\"count\":814,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}');
INSERT INTO `bag` VALUES ('8', '背包栏', '100', '{6898069155891777536:{\"count\":1,\"durable\":30,\"id\":6898069155891777536,\"itemInfo\":{\"describe\":\"屠龙之刃，暴击之王。\",\"durable\":30,\"id\":11001,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},9:{\"description\":\"对于玩家:5点耐力延长1%的技能持续时间;对于装备:1点耐力表示为装备1点耐性\",\"id\":9,\"name\":\"耐力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":4,\\\"value\\\":20},{\\\"id\\\":9,\\\"value\\\":20}]\",\"level\":5,\"name\":\"铁血长剑\",\"part\":2,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0},6898029714485678080:{\"count\":200,\"durable\":0,\"id\":6898029714485678080,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6898085176740548608:{\"count\":1,\"durable\":30,\"id\":6898085176740548608,\"itemInfo\":{\"describe\":\"戴上至尊之盔吧!\",\"durable\":30,\"id\":21002,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},6:{\"description\":\"5点智力增加1点防御力\",\"id\":6,\"name\":\"智力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":6,\\\"value\\\":20},{\\\"id\\\":4,\\\"value\\\":20}]\",\"level\":5,\"name\":\"天命战盔\",\"part\":0,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0},6898085140568870912:{\"count\":18,\"durable\":0,\"id\":6898085140568870912,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}');
INSERT INTO `bag` VALUES ('9', '背包栏', '100', '{6898371941011427328:{\"count\":1364,\"durable\":0,\"id\":6898371941011427328,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6898373954189266944:{\"count\":7,\"durable\":0,\"id\":6898373954189266944,\"itemInfo\":{\"buff\":3,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6898376418649051136:{\"count\":1,\"durable\":0,\"id\":6898376418649051136,\"itemInfo\":{\"describe\":\"戴上至尊之盔吧!\",\"durable\":30,\"id\":21002,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},6:{\"description\":\"5点智力增加1点防御力\",\"id\":6,\"name\":\"智力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":6,\\\"value\\\":20},{\\\"id\\\":4,\\\"value\\\":20}]\",\"level\":5,\"name\":\"天命战盔\",\"part\":0,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0},6900298252223123456:{\"count\":37,\"durable\":0,\"id\":6900298252223123456,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6900897164961124352:{\"count\":1,\"durable\":28,\"id\":6900897164961124352,\"itemInfo\":{\"describe\":\"屠龙之刃，暴击之王。\",\"durable\":30,\"id\":11001,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},9:{\"description\":\"对于玩家:5点耐力延长1%的技能持续时间;对于装备:1点耐力表示为装备1点耐性\",\"id\":9,\"name\":\"耐力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":4,\\\"value\\\":20},{\\\"id\\\":9,\\\"value\\\":20}]\",\"level\":5,\"name\":\"铁血长剑\",\"part\":2,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0}}');
INSERT INTO `bag` VALUES ('10', '背包栏', '100', null);
INSERT INTO `bag` VALUES ('11', '背包栏', '100', '{6900538987706978304:{\"count\":213,\"durable\":0,\"id\":6900538987706978304,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6900541158947164160:{\"count\":1,\"durable\":30,\"id\":6900541158947164160,\"itemInfo\":{\"describe\":\"屠龙之刃，暴击之王。\",\"durable\":30,\"id\":11001,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},9:{\"description\":\"对于玩家:5点耐力延长1%的技能持续时间;对于装备:1点耐力表示为装备1点耐性\",\"id\":9,\"name\":\"耐力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":4,\\\"value\\\":20},{\\\"id\\\":9,\\\"value\\\":20}]\",\"level\":5,\"name\":\"铁血长剑\",\"part\":2,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0},6900625469092270080:{\"count\":1,\"durable\":30,\"id\":6900625469092270080,\"itemInfo\":{\"describe\":\"戴上至尊之盔吧!\",\"durable\":30,\"id\":21002,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":20},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":40},6:{\"description\":\"5点智力增加1点防御力\",\"id\":6,\"name\":\"智力\",\"typeId\":2,\"value\":20}},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":40},{\\\"id\\\":6,\\\"value\\\":20},{\\\"id\\\":4,\\\"value\\\":20}]\",\"level\":5,\"name\":\"天命战盔\",\"part\":0,\"price\":10,\"repairPrice\":1,\"type\":1},\"level\":5,\"locationIndex\":0}}');
INSERT INTO `bag` VALUES ('12', '背包栏', '100', '{6900894975450550272:{\"count\":296,\"durable\":0,\"id\":6900894975450550272,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},6900897164956930048:{\"count\":9,\"durable\":0,\"id\":6900897164956930048,\"itemInfo\":{\"buff\":7,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}');

-- ----------------------------
-- Table structure for guild
-- ----------------------------
DROP TABLE IF EXISTS `guild`;
CREATE TABLE `guild` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `member_size` int(11) DEFAULT NULL,
  `warehouse_str` text,
  `warehouse_size` int(11) DEFAULT NULL,
  `donate_str` text,
  `join_request_str` text,
  `member_str` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of guild
-- ----------------------------
INSERT INTO `guild` VALUES ('10', '散人天堂', '1', '27', '{1000:{\"count\":100,\"durable\":0,\"id\":6900538987706978304,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '50', '{11:{\"donateNum\":100,\"name\":\"fafa\",\"unId\":11}}', '{}', '{11:{\"change\":false,\"guildAuth\":1,\"level\":21,\"name\":\"fafa\",\"right\":\"会长\",\"unId\":11}}');

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_un_id` int(20) NOT NULL,
  `recevier_un_id` int(20) NOT NULL,
  `time` bigint(20) DEFAULT NULL,
  `keep_time` bigint(20) NOT NULL DEFAULT '259200000' COMMENT '3天保留',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `items_str` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `is_read` bit(1) NOT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------
INSERT INTO `mail` VALUES ('1', '-1', '1', '1566469686252', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":1,\"durable\":100,\"id\":6890875886426525696,\"itemInfo\":{\"describe\":\"一指呼风唤雨\",\"durable\":100,\"id\":1008,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":200},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":4,\\\"value\\\":200},{\\\"id\\\":5,\\\"value\\\":200}]\",\"level\":1,\"name\":\"君临至尊戒指\",\"part\":3,\"price\":1000,\"repairPrice\":16,\"type\":1},\"level\":1,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('2', '1', '6', '1566531842995', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":1,\"durable\":100,\"id\":6890875886426525696,\"itemInfo\":{\"describe\":\"一指呼风唤雨\",\"durable\":100,\"id\":1008,\"itemRoleAttribute\":{4:{\"id\":4,\"name\":\"攻击力\",\"typeId\":2,\"value\":200},5:{\"description\":\"1点力量增加1点HP攻击力\",\"id\":5,\"name\":\"力量\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":4,\\\"value\\\":200},{\\\"id\\\":5,\\\"value\\\":200}]\",\"level\":1,\"name\":\"君临至尊戒指\",\"part\":3,\"price\":1000,\"repairPrice\":16,\"type\":1},\"level\":1,\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('3', '1', '6', '1566542889849', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('4', '6', '1', '1566543120244', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家22');
INSERT INTO `mail` VALUES ('5', '1', '6', '1566543419799', '-1', '送给我的小哥哥', '送你10瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('6', '1', '6', '1566543741102', '-1', '送给我的小哥哥', '送你10瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('7', '1', '6', '1566543771101', '-1', '送给我的小哥哥', '送你10瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('8', '1', '6', '1566544211541', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '玩家1');
INSERT INTO `mail` VALUES ('9', '-1', '1', '1566544307517', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('10', '-1', '1', '1566544355067', '-1', '送给我的小哥哥', '送你100瓶血瓶', '[{\"count\":10,\"durable\":0,\"id\":6887240349836775424,\"itemInfo\":{\"buff\":106,\"describe\":\"加hp\",\"durable\":-1,\"id\":1004,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"红药\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('11', '-1', '6', '1566901620194', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('12', '-1', '1', '1567064686554', '-1', '面对面交易邮件', '面对面交易失败, 这是您的物品。', '[{\"count\":30,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('13', '-1', '6', '1567064666774', '-1', '面对面交易邮件', '面对面交易失败, 这是您的物品。', '[null]', '', '系统');
INSERT INTO `mail` VALUES ('14', '-1', '1', '1567065912299', '-1', '面对面交易邮件', '面对面交易失败, 这是您的物品。', '[{\"count\":30,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('15', '-1', '1', '1567066308313', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893378311020482560,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('16', '-1', '1', '1567066549605', '-1', '面对面交易邮件', '面对面交易失败, 这是您的物品。', '[{\"count\":90,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('17', '-1', '1', '1567067339111', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893382641400287232,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('18', '-1', '1', '1567067343233', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893382658693402624,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('19', '-1', '1', '1567067344139', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893382662493442048,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('20', '-1', '1', '1567067597748', '-1', '面对面交易邮件', '面对面交易失败, 这是您的物品。', '[{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('21', '-1', '1', '1567067915256', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":1302,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('22', '-1', '6', '1567067915255', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('23', '-1', '6', '1567068235802', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('24', '-1', '1', '1567068236155', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":200,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('25', '-1', '6', '1567068379706', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('26', '-1', '1', '1567068383277', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":200,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('27', '-1', '1', '1567068492228', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893387477923532800,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('28', '-1', '1', '1567068493402', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893387482851840000,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('29', '-1', '1', '1567068494869', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893387489000689664,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('30', '-1', '1', '1567068495872', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893387493207576576,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('31', '-1', '1', '1567068496632', '-1', '商店购买物品', '这是你在商店购买的物品', '[{\"count\":100,\"durable\":-1,\"id\":6893387496399441920,\"itemInfo\":{\"buff\":6,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('32', '-1', '6', '1567071630233', '-1', '交易栏一口价邮件', '一口价竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1900,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('33', '-1', '1', '1567071629751', '-1', '交易栏一口价邮件', '一口价竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('34', '-1', '1', '1567072259306', '-1', '交易栏一口价邮件', '一口价竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1900,\"durable\":0,\"id\":6887240352932171776,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('35', '-1', '6', '1567072258981', '-1', '交易栏一口价邮件', '一口价竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('43', '9', '6', '1568686026116', '259200000', '送给我的小哥哥', '送你2000元宝', '[{\"count\":2000,\"durable\":0,\"id\":6898371941011427328,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '\0', '小鹿送我一枝花儿');
INSERT INTO `mail` VALUES ('44', '-1', '6', '1568778568288', '259200000', '交易栏竞拍邮件', '竞拍交易成功, 这是您此次交易的物品。', '[null]', '\0', '系统');
INSERT INTO `mail` VALUES ('45', '-1', '9', '1568780149271', '-1', '交易栏竞拍邮件', '竞拍交易失败, 系统自动返回交易物品, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372608,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":146,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('46', '-1', '9', '1568780411001', '-1', '交易栏竞拍邮件', '竞拍交易失败, 系统自动返回交易物品, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372608,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":146,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('47', '9', '11', '1568780746397', '-1', '送给我的小哥哥', '送你2000元宝', '[{\"count\":2000,\"durable\":0,\"id\":6898371941011427328,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '小鹿送我一枝花儿');
INSERT INTO `mail` VALUES ('49', '-1', '9', '1568789729540', '-1', '交易栏竞拍邮件', '竞拍交易失败, 系统自动返回交易物品, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('50', '-1', '9', '1568796010213', '-1', '交易栏竞拍邮件', '竞拍交易失败, 系统自动返回交易物品, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('51', '-1', '11', '1568796482527', '259200000', '交易栏竞拍邮件', '竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}]', '\0', '系统');
INSERT INTO `mail` VALUES ('52', '-1', '9', '1568796482532', '-1', '交易栏竞拍邮件', '竞拍交易成功, 这是您此次交易的物品。', '[{\"count\":1999,\"durable\":0,\"id\":6900538987706978304,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('53', '-1', '9', '1568860269207', '259200000', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":2,\"durable\":0,\"id\":6900894975450550272,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '\0', '系统');
INSERT INTO `mail` VALUES ('54', '-1', '12', '1568860269220', '-1', '面对面交易邮件', '面对面交易成功, 这是您的物品。', '[{\"count\":200,\"durable\":0,\"id\":6898371941011427328,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}]', '', '系统');
INSERT INTO `mail` VALUES ('55', '-1', '9', '1568860581242', '259200000', '交易栏竞拍邮件', '竞拍交易失败, 系统自动返回交易物品, 这是您此次交易的物品。', '[{\"count\":1,\"durable\":100,\"id\":6900295141362372608,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":146,\"locationIndex\":0}]', '\0', '系统');

-- ----------------------------
-- Table structure for task_progress
-- ----------------------------
DROP TABLE IF EXISTS `task_progress`;
CREATE TABLE `task_progress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL,
  `un_id` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `begin_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `progress` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_progress
-- ----------------------------
INSERT INTO `task_progress` VALUES ('1', '3', '8', '4', '1568167808022', null, '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":3},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('2', '1', '8', '4', '1568175292234', '1568184682662', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":1},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('3', '3', '9', '4', '1568182820291', null, '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":3},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('4', '1', '9', '4', '1568183364847', '1568196338557', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":1},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('5', '4', '8', '4', '1568184691245', '1568187648862', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":6},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('6', '2', '8', '4', '1568187648871', '1568188510903', '[{\"condition\":{\"field\":1,\"goal\":2,\"target\":4},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('7', '5', '8', '1', '1568188510907', null, '[{\"condition\":{\"field\":6,\"goal\":50,\"target\":2},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('8', '14', '8', '1', '1568188510986', null, '[{\"condition\":{\"field\":2,\"goal\":1000,\"target\":1000},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('9', '4', '9', '4', '1568196338560', '1568256880833', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":6},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('10', '2', '9', '4', '1568256880847', '1568260433608', '[{\"condition\":{\"field\":1,\"goal\":2,\"target\":4},\"finished\":true,\"progressNum\":2}]');
INSERT INTO `task_progress` VALUES ('11', '5', '9', '4', '1568257948388', '1568258308772', '[{\"condition\":{\"field\":6,\"goal\":50,\"target\":2},\"finished\":true,\"progressNum\":55}]');
INSERT INTO `task_progress` VALUES ('12', '14', '9', '4', '1568257948411', '1568258373542', '[{\"condition\":{\"field\":2,\"goal\":1000,\"target\":1000},\"finished\":true,\"progressNum\":1000}]');
INSERT INTO `task_progress` VALUES ('13', '7', '9', '4', '1568258308774', '1568627520982', '[{\"condition\":{\"field\":3,\"goal\":1,\"target\":1},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('14', '11', '9', '4', '1568258308784', '1568687080002', '[{\"condition\":{\"field\":5,\"goal\":1,\"target\":3},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('15', '10', '9', '2', '1568627520995', null, '[{\"condition\":{\"field\":5,\"goal\":1,\"target\":2},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('16', '18', '9', '2', '1568627521021', null, '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":7},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('17', '22', '9', '2', '1568627521038', null, '[{\"condition\":{\"field\":3,\"goal\":2,\"target\":1},\"finished\":false,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('18', '9', '9', '4', '1568687080002', '1568704971488', '[{\"condition\":{\"field\":5,\"goal\":1,\"target\":1},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('19', '3', '10', '2', '1568728272194', null, '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":3},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('20', '3', '11', '4', '1568772887339', '1568773545027', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":3},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('21', '1', '11', '4', '1568773545031', '1568774062692', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":1},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('22', '4', '11', '4', '1568774062692', '1568792834932', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":6},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('23', '2', '11', '4', '1568792834936', '1568794163797', '[{\"condition\":{\"field\":1,\"goal\":2,\"target\":4},\"finished\":true,\"progressNum\":2}]');
INSERT INTO `task_progress` VALUES ('24', '5', '11', '1', '1568794163797', null, '[{\"condition\":{\"field\":6,\"goal\":50,\"target\":2},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('25', '14', '11', '1', '1568794163805', null, '[{\"condition\":{\"field\":2,\"goal\":1000,\"target\":1000},\"finished\":false,\"progressNum\":0}]');
INSERT INTO `task_progress` VALUES ('26', '3', '12', '4', '1568858173592', '1568858419120', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":3},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('27', '1', '12', '4', '1568858419122', '1568858941140', '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":1},\"finished\":true,\"progressNum\":1}]');
INSERT INTO `task_progress` VALUES ('28', '4', '12', '1', '1568858941144', null, '[{\"condition\":{\"field\":1,\"goal\":1,\"target\":6},\"finished\":false,\"progressNum\":0}]');

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` bigint(20) DEFAULT NULL,
  `keep_time` bigint(20) DEFAULT NULL,
  `owner_un_id` int(11) NOT NULL,
  `trade_model` int(11) DEFAULT NULL,
  `items_map_str` text,
  `item_info_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `finish` bit(1) NOT NULL,
  `success` bit(1) NOT NULL,
  `accept_un_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trade
-- ----------------------------
INSERT INTO `trade` VALUES ('1', '1567064493066', '10800000', '1', '1', '{1:{\"count\":30,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1004', '200', null, '', '\0', '6');
INSERT INTO `trade` VALUES ('2', '1567066431913', '10800000', '1', '1', '{1:{\"count\":90,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1004', '100', null, '', '\0', '6');
INSERT INTO `trade` VALUES ('3', '1567067248388', '10800000', '1', '1', '{1:{\"count\":80,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1004', '100', null, '\0', '\0', '6');
INSERT INTO `trade` VALUES ('4', '1567067486914', '10800000', '1', '1', '{1:{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1004', '200', null, '', '\0', '6');
INSERT INTO `trade` VALUES ('5', '1567067781862', '10800000', '1', '1', '{1:{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1000', '200', null, '', '', '6');
INSERT INTO `trade` VALUES ('6', '1567068193287', '10800000', '1', '1', '{1:{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1000', '200', null, '', '', '6');
INSERT INTO `trade` VALUES ('7', '1567068321524', '10800000', '1', '1', '{1:{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1000', '200', null, '', '', '6');
INSERT INTO `trade` VALUES ('8', '1567068553839', '10800000', '1', '1', '{1:{\"count\":100,\"durable\":0,\"id\":6887237653851410432,\"itemInfo\":{\"buff\":105,\"describe\":\"加mp\",\"durable\":-1,\"id\":1005,\"itemRoleAttribute\":{},\"level\":1,\"name\":\"蓝瓶\",\"part\":9,\"price\":5,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1000', '200', null, '\0', '\0', '6');
INSERT INTO `trade` VALUES ('9', '1567069699820', '180000', '6', '2', '{6:{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}}', null, null, '1888', '', '', '1');
INSERT INTO `trade` VALUES ('10', '1567071777450', '180000', '1', '2', '{1:{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}}', null, null, '1888', '', '', '6');
INSERT INTO `trade` VALUES ('11', '1567072580266', '180000', '6', '3', '{6:{\"count\":1,\"durable\":100,\"id\":6892687565888032768,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":34,\"locationIndex\":0}}', null, null, '1888', '', '', null);
INSERT INTO `trade` VALUES ('12', '1568779079677', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372608,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":146,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);
INSERT INTO `trade` VALUES ('13', '1568780913905', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);
INSERT INTO `trade` VALUES ('14', '1568788424220', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);
INSERT INTO `trade` VALUES ('15', '1568789762480', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);
INSERT INTO `trade` VALUES ('16', '1568794532540', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);
INSERT INTO `trade` VALUES ('17', '1568796063621', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372609,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":147,\"locationIndex\":0},11:{\"count\":1999,\"durable\":0,\"id\":6900538987706978304,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', null, null, '1888', '', '', '11');
INSERT INTO `trade` VALUES ('18', '1568860173848', '10800000', '12', '1', '{9:{\"count\":200,\"durable\":0,\"id\":6898371941011427328,\"itemInfo\":{\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0},12:{\"count\":2,\"durable\":0,\"id\":6900894975450550272,\"itemInfo\":{\"buff\":1000,\"describe\":\"元宝\",\"durable\":-1,\"id\":1000,\"itemRoleAttribute\":{},\"name\":\"元宝\",\"part\":9,\"price\":1,\"repairPrice\":-1,\"type\":3},\"locationIndex\":0}}', '1000', '200', null, '', '', '9');
INSERT INTO `trade` VALUES ('19', '1568860391285', '180000', '9', '3', '{9:{\"count\":1,\"durable\":100,\"id\":6900295141362372608,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":146,\"locationIndex\":0}}', null, null, '1888', '', '\0', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'kevins', '123', '156816969231911e921962846b49de8c53eb05dd982a75');
INSERT INTO `user` VALUES ('2', '佳鸿', '123', null);
INSERT INTO `user` VALUES ('3', 'kd', '123456', '15633455772273');
INSERT INTO `user` VALUES ('6', 'kevinstest', '123456', '15687122362676435cc8f7520f44cfbce8467d0ea6a482');
INSERT INTO `user` VALUES ('7', '光头', '123456', null);
INSERT INTO `user` VALUES ('8', 'annie', '123456', null);
INSERT INTO `user` VALUES ('10', 'test', '123456', null);
INSERT INTO `user` VALUES ('11', 'test/111', '123456', null);
INSERT INTO `user` VALUES ('12', 'test1//131', '123456', null);
INSERT INTO `user` VALUES ('13', 'te/s3///1', '123456', null);
INSERT INTO `user` VALUES ('19', '/0/1', '123', null);
INSERT INTO `user` VALUES ('20', '123', '123', null);
INSERT INTO `user` VALUES ('21', '131', '123', null);
INSERT INTO `user` VALUES ('22', '安妮', '520', '156877294813722392c131c1d0d4bb29fa06858132c3c41');
INSERT INTO `user` VALUES ('23', '安妮2', '520', null);
INSERT INTO `user` VALUES ('24', '凯撒大帝', '123', '1568196175004243c67ffc2920140c6918db1c340666e20');
INSERT INTO `user` VALUES ('25', '小鹿送我一枝花儿', '123', '1569205000166253585a3bfcb624a739a080b3c43e82fea');
INSERT INTO `user` VALUES ('26', '极光网络', '520', '156877162931326ff436cb7768943a5b87f4854a7493720');
INSERT INTO `user` VALUES ('27', 'gege', '123456', '1568772246514271848db388de84580bf1320cc65f123af');
INSERT INTO `user` VALUES ('28', 'baba', '123456', '156877249866928195a78306ab34fa3b752f4807c5420d3');
INSERT INTO `user` VALUES ('29', 'yeye', '123456', '156877237673529ebbecee5d30e49ae8182aa87596c7e31');
INSERT INTO `user` VALUES ('30', 'fafa', '123456', '1568802119766306f3768ac20834120a3a02bd284b2c79e');
INSERT INTO `user` VALUES ('31', '蔡徐坤', '123', '1569204955842319045473331fc4d35a49d82954c62ba62');

-- ----------------------------
-- Table structure for user_entity
-- ----------------------------
DROP TABLE IF EXISTS `user_entity`;
CREATE TABLE `user_entity` (
  `un_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` tinyint(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` int(11) NOT NULL DEFAULT '1',
  `type_id` int(11) NOT NULL,
  `sence_id` int(11) NOT NULL,
  `exp` int(11) unsigned NOT NULL DEFAULT '100',
  `equipments` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `money` int(11) NOT NULL DEFAULT '0',
  `guild_list_str` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `baby_level` int(4) NOT NULL DEFAULT '0',
  `baby_type` int(4) NOT NULL DEFAULT '0',
  `friend` text,
  PRIMARY KEY (`un_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_entity
-- ----------------------------
INSERT INTO `user_entity` VALUES ('1', '1', '玩家1', '1', '1', '1', '20900', '{0:{\"count\":1,\"durable\":70,\"id\":6887243819226632193,\"itemInfo\":{\"describe\":\"戴上至尊之盔吧!\",\"durable\":100,\"id\":1002,\"itemRoleAttribute\":{},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":400},{\\\"id\\\":6,\\\"value\\\":200},{\\\"id\\\":4,\\\"value\\\":200}]\",\"level\":30,\"name\":\"踏碎苍茫大地头盔\",\"part\":0,\"price\":1000,\"repairPrice\":30,\"type\":1},\"level\":26,\"locationIndex\":0},1:{\"count\":1,\"durable\":100,\"id\":6887584564508758016,\"itemInfo\":{\"describe\":\"周身万法不侵!\",\"durable\":100,\"id\":1003,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":200},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":200}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":200},{\\\"id\\\":8,\\\"value\\\":200}]\",\"level\":34,\"name\":\"奔雷天干地煞甲\",\"part\":1,\"price\":1000,\"repairPrice\":20,\"type\":1},\"level\":137,\"locationIndex\":0},4:{\"count\":1,\"durable\":100,\"id\":6887576192023662594,\"itemInfo\":{\"describe\":\"护腰\",\"durable\":100,\"id\":1007,\"itemRoleAttribute\":{3:{\"description\":\"指的是物抗\",\"id\":3,\"name\":\"防御\",\"typeId\":2,\"value\":100},8:{\"description\":\"指的是魔抗\",\"id\":8,\"name\":\"魔法抗性\",\"typeId\":2,\"value\":100}},\"jsonStr\":\"[{\\\"id\\\":3,\\\"value\\\":100},{\\\"id\\\":8,\\\"value\\\":100}]\",\"level\":35,\"name\":\"万法无上无量腰\",\"part\":4,\"price\":1000,\"repairPrice\":15,\"type\":1},\"level\":39,\"locationIndex\":0}}', '0', '[]', '0', '0', null);
INSERT INTO `user_entity` VALUES ('5', '3', '玩家3', '1', '0', '1', '100', null, '0', null, '0', '0', null);
INSERT INTO `user_entity` VALUES ('6', '22', '玩家22', '1', '4', '1', '7700', '{2:{\"count\":1,\"durable\":94,\"id\":6887243819226632199,\"itemInfo\":{\"describe\":\"\",\"durable\":100,\"id\":1001,\"itemRoleAttribute\":{},\"jsonStr\":\"[{\\\"id\\\":5,\\\"value\\\":400},{\\\"id\\\":4,\\\"value\\\":200},{\\\"id\\\":9,\\\"value\\\":200}]\",\"level\":25,\"name\":\"无尽之刃\",\"part\":2,\"price\":1000,\"repairPrice\":10,\"type\":1},\"level\":26,\"locationIndex\":0}}', '0', '[]', '0', '0', '{}');
INSERT INTO `user_entity` VALUES ('8', '24', '凯撒大帝', '1', '3', '3', '100', '{}', '0', '[]', '0', '0', null);
INSERT INTO `user_entity` VALUES ('9', '25', '小鹿送我一枝花儿', '1', '3', '1', '15350', '{}', '0', '[]', '0', '0', '{}');
INSERT INTO `user_entity` VALUES ('10', '26', '极光网络', '1', '0', '1', '100', '{}', '0', '[]', '0', '0', '{}');
INSERT INTO `user_entity` VALUES ('11', '30', 'fafa', '1', '2', '3', '2150', '{}', '0', '[10]', '0', '0', '{}');
INSERT INTO `user_entity` VALUES ('12', '31', '蔡徐坤', '1', '4', '1', '850', '{}', '0', '[]', '0', '0', '{}');
