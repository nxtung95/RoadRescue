/*
 Navicat Premium Data Transfer

 Source Server         : LOCAL_HOME
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : road_rescue

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 06/10/2023 00:30:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `f_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `l_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timestamp` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`customer_id`) USING BTREE,
  UNIQUE INDEX `contact_num`(`contact_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for otp
-- ----------------------------
DROP TABLE IF EXISTS `otp`;
CREATE TABLE `otp`  (
  `otp_id` int NOT NULL AUTO_INCREMENT,
  `mobile_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `otp_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `timestamp` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `expire_timestamp` timestamp(0) NULL DEFAULT NULL,
  `is_used` tinyint NULL DEFAULT NULL COMMENT '0: not use, 1: used',
  `wrong_attempts` int NULL DEFAULT NULL,
  PRIMARY KEY (`otp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of otp
-- ----------------------------

-- ----------------------------
-- Table structure for service_provider
-- ----------------------------
DROP TABLE IF EXISTS `service_provider`;
CREATE TABLE `service_provider`  (
  `service_provider_id` int NOT NULL AUTO_INCREMENT,
  `service_provider_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `availability_status` enum('open','closed') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `avg_rating` float NOT NULL,
  `timestamp` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`service_provider_id`) USING BTREE,
  UNIQUE INDEX `contact_num`(`contact_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of service_provider
-- ----------------------------

-- ----------------------------
-- Table structure for vehicle
-- ----------------------------
DROP TABLE IF EXISTS `vehicle`;
CREATE TABLE `vehicle`  (
  `plate_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `make_id` int NOT NULL,
  `year` int NOT NULL,
  `model_id` int NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `owner_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`plate_num`) USING BTREE,
  INDEX `owner_id`(`owner_id`) USING BTREE,
  INDEX `vehicle_fk_2`(`make_id`) USING BTREE,
  INDEX `vehicle_fk_3`(`model_id`) USING BTREE,
  CONSTRAINT `vehicle_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `vehicle_fk_2` FOREIGN KEY (`make_id`) REFERENCES `vehicle_make` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `vehicle_fk_3` FOREIGN KEY (`model_id`) REFERENCES `vehicle_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vehicle
-- ----------------------------

-- ----------------------------
-- Table structure for vehicle_make
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_make`;
CREATE TABLE `vehicle_make`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `make_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vehicle_make
-- ----------------------------
INSERT INTO `vehicle_make` VALUES (1, 'Toyota');
INSERT INTO `vehicle_make` VALUES (2, 'Honda');
INSERT INTO `vehicle_make` VALUES (3, 'Mercedes');

-- ----------------------------
-- Table structure for vehicle_model
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_model`;
CREATE TABLE `vehicle_model`  (
  `id` int NOT NULL,
  `make_id` int NULL DEFAULT NULL,
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_model_1`(`make_id`) USING BTREE,
  CONSTRAINT `fk_model_1` FOREIGN KEY (`make_id`) REFERENCES `vehicle_make` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vehicle_model
-- ----------------------------
INSERT INTO `vehicle_model` VALUES (1, 1, 'Corolla Cross');
INSERT INTO `vehicle_model` VALUES (2, 1, 'Camry');
INSERT INTO `vehicle_model` VALUES (3, 2, 'Civic');
INSERT INTO `vehicle_model` VALUES (4, 2, 'CRV');
INSERT INTO `vehicle_model` VALUES (5, 3, 'C200');
INSERT INTO `vehicle_model` VALUES (6, 3, 'S300');

SET FOREIGN_KEY_CHECKS = 1;
