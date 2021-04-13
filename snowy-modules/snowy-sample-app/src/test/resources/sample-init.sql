CREATE TABLE `cloud_sample` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `sample_name` varchar(200) NOT NULL COMMENT '样本名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT='微服务样本' ROW_FORMAT = Dynamic;

INSERT INTO `cloud_sample`(`id`, `sample_name`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1265476890672672808, '微服务样本A-测试1', '2020-12-28 23:47:41', 1265476890672672808, '2020-12-28 23:47:45', 1265476890672672808);
INSERT INTO `cloud_sample`(`id`, `sample_name`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1275735541155614721, '微服务样本B-测试2', '2020-12-28 23:48:12', 1265476890672672808, '2020-12-28 23:48:31', 1265476890672672808);
INSERT INTO `cloud_sample`(`id`, `sample_name`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1280709549107552257, '微服务样本C-测试3', '2020-12-28 23:48:57', 1265476890672672808, '2020-12-28 23:49:02', 1265476890672672808);
