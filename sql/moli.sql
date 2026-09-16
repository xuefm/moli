/*
 Navicat Premium Dump SQL

 Source Server         : 虚拟机
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : 192.168.200.101:3306
 Source Schema         : moli

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 15/09/2026 17:44:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_account`;
CREATE TABLE `sys_account`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `login_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `login_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(加密)',
  `login_time` bigint NULL DEFAULT NULL COMMENT '最后登录时间(时间戳)',
  `enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除标识-0:未删除,1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_account
-- ----------------------------
INSERT INTO `sys_account` VALUES ('2099781347181707266', 'admin', '$2a$10$7mP8VAIkKLIx.mUHtIZHxunFRTy6x9g6Rp7gSaWJZQ6nCJFpqU8Oq', NULL, 1, 0, '2026-09-15 16:44:20', 'root', '2026-09-15 16:44:20', 'root');

-- ----------------------------
-- Table structure for sys_account_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_account_role`;
CREATE TABLE `sys_account_role`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `account_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号id',
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色id',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除标识-0:未删除,1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_role`(`account_id` ASC, `role_id` ASC) USING BTREE COMMENT '账号角色唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统账号 系统角色 关联表(多对多)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_account_role
-- ----------------------------
INSERT INTO `sys_account_role` VALUES ('2099781347202678786', '2099781347181707266', '2099781412457660417', 0, '2026-09-15 16:44:20', 'root', '2026-09-15 16:44:20', 'root');

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `type` int NOT NULL DEFAULT 1 COMMENT '类型 0组 1接口',
  `level` int NULL DEFAULT 1 COMMENT '层级',
  `superior_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源标题',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源code',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除标识-0:未删除,1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE COMMENT 'code唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('2099781525590622210', 0, 1, NULL, '账号管理', 'account', 1, 0, '2026-09-15 16:45:02', 'root', '2026-09-15 16:45:02', 'root');
INSERT INTO `sys_resource` VALUES ('2099782119457931266', 1, 2, '2099781525590622210', '账号查询', 'account:select', 1, 0, '2026-09-15 16:47:24', 'root', '2026-09-15 16:47:24', 'root');
INSERT INTO `sys_resource` VALUES ('2099782395459911682', 0, 1, NULL, '角色管理', 'role', 1, 0, '2026-09-15 16:48:30', 'root', '2026-09-15 16:48:30', 'root');
INSERT INTO `sys_resource` VALUES ('2099782477534052354', 1, 2, '2099782395459911682', '角色查询', 'role:select', 1, 0, '2026-09-15 16:48:49', 'root', '2026-09-15 16:48:49', 'root');
INSERT INTO `sys_resource` VALUES ('2099782586334298113', 0, 1, NULL, '资源管理', 'resource', 1, 0, '2026-09-15 16:49:15', 'root', '2026-09-15 16:49:15', 'root');
INSERT INTO `sys_resource` VALUES ('2099782668433604609', 1, 2, '2099782586334298113', '资源查询', 'resource:select', 1, 0, '2026-09-15 16:49:35', 'root', '2026-09-15 16:49:35', 'root');
INSERT INTO `sys_resource` VALUES ('2099783338058432513', 1, 2, '2099781525590622210', '创建账号', 'account:insert', 1, 0, '2026-09-15 16:52:15', 'root', '2026-09-15 16:52:15', 'root');
INSERT INTO `sys_resource` VALUES ('2099783441607409666', 1, 2, '2099781525590622210', '更新账号', 'account:update', 1, 0, '2026-09-15 16:52:39', 'root', '2026-09-15 16:52:39', 'root');
INSERT INTO `sys_resource` VALUES ('2099788340822827010', 1, 2, '2099782395459911682', '创建角色', 'role:insert', 1, 0, '2026-09-15 17:12:07', 'root', '2026-09-15 17:12:07', 'root');
INSERT INTO `sys_resource` VALUES ('2099788449543380994', 1, 2, '2099782395459911682', '更新角色', 'role:update', 1, 0, '2026-09-15 17:12:33', 'root', '2026-09-15 17:12:33', 'root');
INSERT INTO `sys_resource` VALUES ('2099788647749410817', 1, 2, '2099782586334298113', '创建资源', 'resource:insert', 1, 0, '2026-09-15 17:13:20', 'root', '2026-09-15 17:13:20', 'root');
INSERT INTO `sys_resource` VALUES ('2099788740988788737', 1, 2, '2099782586334298113', '更新资源', 'resource:update', 1, 0, '2026-09-15 17:13:43', 'root', '2026-09-15 17:13:43', 'root');
INSERT INTO `sys_resource` VALUES ('2099795889160753154', 1, 2, '2099782586334298113', '删除资源', 'resource:delete', 1, 0, '2026-09-15 17:42:07', 'root', '2026-09-15 17:42:07', 'root');
INSERT INTO `sys_resource` VALUES ('2099796050247192577', 1, 2, '2099781525590622210', '删除账号', 'account:delete', 1, 0, '2026-09-15 17:42:45', 'root', '2026-09-15 17:42:45', 'root');
INSERT INTO `sys_resource` VALUES ('2099796134028414977', 1, 2, '2099782395459911682', '删除角色', 'role:delete', 1, 0, '2026-09-15 17:43:05', 'root', '2026-09-15 17:43:05', 'root');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色code',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除标识-0:未删除,1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2099781412457660417', 'admin', 'admin', 1, 0, '2026-09-15 16:44:35', 'root', '2026-09-15 16:44:35', 'root');

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色id',
  `resource_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源id',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除标识-0:未删除,1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_resource`(`role_id` ASC, `resource_id` ASC) USING BTREE COMMENT '角色权限唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色 系统资源 关联表(多对多)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('2099784965586796545', '2099781412457660417', '2099782119457931266', 0, '2026-09-15 16:58:43', 'root', '2026-09-15 16:58:43', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099784965649711105', '2099781412457660417', '2099783338058432513', 0, '2026-09-15 16:58:43', 'root', '2026-09-15 16:58:43', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099784965649711106', '2099781412457660417', '2099783441607409666', 0, '2026-09-15 16:58:43', 'root', '2026-09-15 16:58:43', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099784965649711107', '2099781412457660417', '2099782477534052354', 0, '2026-09-15 16:58:43', 'root', '2026-09-15 16:58:43', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099784965649711108', '2099781412457660417', '2099782668433604609', 0, '2026-09-15 16:58:43', 'root', '2026-09-15 16:58:43', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099789560891334658', '2099781412457660417', '2099788340822827010', 0, '2026-09-15 17:16:58', 'root', '2026-09-15 17:16:58', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099789560891334659', '2099781412457660417', '2099788449543380994', 0, '2026-09-15 17:16:58', 'root', '2026-09-15 17:16:58', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099789560891334660', '2099781412457660417', '2099788647749410817', 0, '2026-09-15 17:16:58', 'root', '2026-09-15 17:16:58', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099789560954249218', '2099781412457660417', '2099788740988788737', 0, '2026-09-15 17:16:58', 'root', '2026-09-15 17:16:58', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099795935537172482', '2099781412457660417', '2099795889160753154', 0, '2026-09-15 17:42:18', 'root', '2026-09-15 17:42:18', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099796429907202049', '2099781412457660417', '2099796050247192577', 0, '2026-09-15 17:44:16', 'root', '2026-09-15 17:44:16', 'root');
INSERT INTO `sys_role_resource` VALUES ('2099796429970116609', '2099781412457660417', '2099796134028414977', 0, '2026-09-15 17:44:16', 'root', '2026-09-15 17:44:16', 'root');

SET FOREIGN_KEY_CHECKS = 1;
