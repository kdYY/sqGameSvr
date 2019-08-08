/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : gamesever

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-08-08 15:52:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bag
-- ----------------------------
DROP TABLE IF EXISTS `bag`;
CREATE TABLE `bag` (
  `player_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `item_str` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bag
-- ----------------------------

-- ----------------------------
-- Table structure for order_rule
-- ----------------------------
DROP TABLE IF EXISTS `order_rule`;
CREATE TABLE `order_rule` (
  `order_name` varchar(255) NOT NULL,
  `rule` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_rule
-- ----------------------------
INSERT INTO `order_rule` VALUES ('aoi', 'entityController:aio', '打印出当前场景所有的实体信息，是否包含自己');
INSERT INTO `order_rule` VALUES ('errOrder', 'userController:errOrder', '错误指令');
INSERT INTO `order_rule` VALUES ('login', 'userController:login', '用户登录');
INSERT INTO `order_rule` VALUES ('loginOut', 'userController:loginOut', '用户登出');
INSERT INTO `order_rule` VALUES ('move', 'senceController:move', 'move <村子> | 移动到于当前场景相邻的另一个场景');
INSERT INTO `order_rule` VALUES ('register', 'userController:register', '用户注册');
INSERT INTO `order_rule` VALUES ('site', 'userController:site', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'kevins', '123', '156501567941917862581e9e2f4ac0bf6358c347413f83');
INSERT INTO `user` VALUES ('2', '佳鸿', '123', null);
INSERT INTO `user` VALUES ('3', 'kd', '123456', '15633455772273');
INSERT INTO `user` VALUES ('6', 'kevinsets', '123456', null);
INSERT INTO `user` VALUES ('7', '光头', '123456', null);
INSERT INTO `user` VALUES ('8', 'annie', '123456', null);
INSERT INTO `user` VALUES ('10', 'test', '123456', null);
INSERT INTO `user` VALUES ('11', 'test111', '123456', null);
INSERT INTO `user` VALUES ('12', 'test1131', '123456', null);
INSERT INTO `user` VALUES ('13', 'tes31', '123456', null);
INSERT INTO `user` VALUES ('19', '12', '123', null);
INSERT INTO `user` VALUES ('20', '123', '123', null);
INSERT INTO `user` VALUES ('21', '131', '123', null);
INSERT INTO `user` VALUES ('22', '安妮', '520', '1565015679418221f53536936644b498a020e3f0442a095');
INSERT INTO `user` VALUES ('23', '安妮2', '520', null);

-- ----------------------------
-- Table structure for user_entity
-- ----------------------------
DROP TABLE IF EXISTS `user_entity`;
CREATE TABLE `user_entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` tinyint(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` tinyint(11) NOT NULL DEFAULT '1',
  `type_id` tinyint(11) NOT NULL,
  `sence_id` tinyint(11) NOT NULL,
  `exp` int(11) unsigned NOT NULL DEFAULT '100',
  `equipments` varchar(255) DEFAULT NULL,
  `money` int(11) NOT NULL DEFAULT '0',
  `guild_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_entity
-- ----------------------------
INSERT INTO `user_entity` VALUES ('1', '1', '玩家1', '1', '1', '1', '100', null, '0', null);
INSERT INTO `user_entity` VALUES ('5', '3', '玩家3', '1', '0', '1', '100', null, '0', null);
INSERT INTO `user_entity` VALUES ('6', '22', '玩家22', '1', '4', '3', '100', null, '0', null);
