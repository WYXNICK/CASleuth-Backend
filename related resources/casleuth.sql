/*
 Navicat Premium Data Transfer

 Source Server         : igem
 Source Server Type    : MySQL
 Source Server Version : 100328
 Source Host           : 43.133.192.56:3306
 Source Schema         : casleuth

 Target Server Type    : MySQL
 Target Server Version : 100328
 File Encoding         : 65001

 Date: 08/10/2023 22:38:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cas_model
-- ----------------------------
DROP TABLE IF EXISTS `cas_model`;
CREATE TABLE `cas_model`  (
  `fasta_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `guide_seq` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `index` int NOT NULL,
  `model_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`fasta_id`, `index`, `model_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for virus
-- ----------------------------
DROP TABLE IF EXISTS `virus`;
CREATE TABLE `virus`  (
  `virus_id` int NOT NULL AUTO_INCREMENT COMMENT '病毒id',
  `accession` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `organism_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '病毒名称',
  `isolate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `species` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `family` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `length` int NOT NULL,
  `segment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `geo_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `virus_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`virus_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12436 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
