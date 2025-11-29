-- ==========================================
-- GitHub仓库服务 - 数据库初始化脚本
-- ==========================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `github_repo_service` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `github_repo_service`;

-- 2. 创建GitHub仓库信息表
DROP TABLE IF EXISTS `github_repo`;

CREATE TABLE `github_repo` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `full_name` VARCHAR(255) NOT NULL COMMENT '仓库全名 (owner/repo)',
  `owner` VARCHAR(100) NOT NULL COMMENT '仓库所有者',
  `repo_name` VARCHAR(155) NOT NULL COMMENT '仓库名称',
  `description` TEXT COMMENT '仓库描述',
  `clone_url` VARCHAR(500) COMMENT '克隆URL',
  `stars` INT(11) DEFAULT 0 COMMENT '星标数量',
  `forks` INT(11) DEFAULT 0 COMMENT 'Fork数量',
  `watchers` INT(11) DEFAULT 0 COMMENT '关注数量',
  `repo_size` INT(11) DEFAULT 0 COMMENT '仓库大小(KB)',
  `default_branch` VARCHAR(50) COMMENT '默认分支',
  `is_private` TINYINT(1) DEFAULT 0 COMMENT '是否私有仓库 0-否 1-是',
  `html_url` VARCHAR(500) COMMENT '仓库主页URL',
  `created_at` DATETIME COMMENT 'GitHub仓库创建时间',
  `updated_at` DATETIME COMMENT 'GitHub仓库更新时间',
  `pushed_at` DATETIME COMMENT 'GitHub仓库推送时间',
  `cache_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '缓存时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_full_name` (`full_name`) COMMENT '仓库全名唯一索引',
  KEY `idx_owner_repo` (`owner`, `repo_name`) COMMENT '所有者和仓库名联合索引',
  KEY `idx_cache_time` (`cache_time`) COMMENT '缓存时间索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GitHub仓库信息表';

