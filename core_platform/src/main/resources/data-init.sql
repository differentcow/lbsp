-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.1.36-community - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 db_lbsp 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_lbsp` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db_lbsp`;
-- 数据导出被取消选择。

-- 导出  表 db_lbsp.preferential_category 结构
CREATE TABLE IF NOT EXISTS `preferential_category` (
  `preferential_id` int(11) NOT NULL COMMENT '优惠ID',
  `category_id` int(11) NOT NULL COMMENT '类别ID',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`preferential_id`,`category_id`)
) COMMENT='优惠类别' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 导出  表 db_lbsp.function_operate 结构
CREATE TABLE IF NOT EXISTS `function_operate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能ID(oper_id)',
  `code` varchar(50) COMMENT '编码' COLLATE 'utf8_unicode_ci',
  `name` varchar(100) NOT NULL COMMENT '功能名称' COLLATE 'utf8_unicode_ci',
  `url` varchar(255) NOT NULL COMMENT '功能别名' COLLATE 'utf8_unicode_ci',
  `base_url` varchar(255) DEFAULT NULL COMMENT '基础路径' COLLATE 'utf8_unicode_ci',
  `method` varchar(20) DEFAULT NULL COMMENT 'HTTP方法' COLLATE 'utf8_unicode_ci',
  `path_param` int(11) COMMENT 'url参数总数',
  `page_id` varchar(50) DEFAULT NULL COMMENT '所属页面' COLLATE 'utf8_unicode_ci',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT='功能资源操作' COLLATE='utf8_unicode_ci' ENGINE=InnoDB;
-- 数据导出被取消选择。

-- 导出  表 db_lbsp.page_operate 结构
CREATE TABLE IF NOT EXISTS `page_operate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '页面ID(oper_id)',
  `code` varchar(50) NOT NULL COMMENT '编码' COLLATE 'utf8_unicode_ci',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父ID' COLLATE 'utf8_unicode_ci',
  `parent_code` varchar(50) DEFAULT NULL COMMENT '父编码' COLLATE 'utf8_unicode_ci',
  `name` varchar(100) NOT NULL COMMENT '页面名称' COLLATE 'utf8_unicode_ci',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT='页面资源操作' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.parameter 结构
CREATE TABLE IF NOT EXISTS `parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '名称(值)' COLLATE 'utf8_unicode_ci',
  `code` varchar(50) NOT NULL COMMENT '编码(键)' COLLATE 'utf8_unicode_ci',
  `type` varchar(50) NOT NULL COMMENT '类型' COLLATE 'utf8_unicode_ci',
  `type_meaning` varchar(80) NOT NULL COMMENT '类型名称' COLLATE 'utf8_unicode_ci',
  `status` varchar(10) DEFAULT NULL COMMENT '状态' COLLATE 'utf8_unicode_ci',
  `create_user` int(11) DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `update_user` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) COMMENT='参数' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.privilege 结构
CREATE TABLE IF NOT EXISTS `privilege` (
  `privilege_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `privilege_master` varchar(50) COMMENT '编码' COLLATE 'utf8_unicode_ci',
  `privilege_master_value` int(11) NOT NULL COMMENT '功能名称',
  `privilege_access` varchar(255) DEFAULT NULL COMMENT '功能别名' COLLATE 'utf8_unicode_ci',
  `privilege_access_value` int(11) DEFAULT NULL COMMENT '描述',
  `privilege_operation` varchar(2) DEFAULT NULL COMMENT 'E:enabled，D:disabled' COLLATE 'utf8_unicode_ci',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`privilege_id`)
) COMMENT='授权' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL COMMENT '角色ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称' COLLATE 'utf8_unicode_ci',
  `status` int(11) NOT NULL COMMENT '状态(0:无效,1:有效)',
  `func_count` int(11) COMMENT '此角色包含功能数',
  `user_count` int(11) COMMENT '此角色使用用户数',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='角色' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。

-- 导出  表 db_lbsp.feedback 结构
CREATE TABLE IF NOT EXISTS `feedback` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`title` VARCHAR(80) NULL DEFAULT NULL COMMENT '标题' COLLATE 'utf8_unicode_ci',
	`content` VARCHAR(1024) NOT NULL COMMENT '内容' COLLATE 'utf8_unicode_ci',
	`anonymous` VARCHAR(50) NULL DEFAULT NULL COMMENT '匿名提交人（记录手机号码等唯一信息）' COLLATE 'utf8_unicode_ci',
	`customer_id` INT(11) NULL DEFAULT NULL COMMENT '注册用户ID',
	`create_user` INT(11) NULL DEFAULT NULL COMMENT '提交人（注册用户）',
	`create_time` BIGINT(20) NOT NULL COMMENT '提交时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='意见反馈'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.advert 结构
CREATE TABLE IF NOT EXISTS `advert` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`title` VARCHAR(100) NOT NULL COMMENT '标题' COLLATE 'utf8_unicode_ci',
	`type` CHAR(2) NOT NULL DEFAULT 'L' COMMENT '类型(L-链接,C-代码)' COLLATE 'utf8_unicode_ci',
	`event` TEXT NOT NULL COMMENT '广告事件' COLLATE 'utf8_unicode_ci',
	`pic_path` VARCHAR(255) NOT NULL COMMENT '图片路径' COLLATE 'utf8_unicode_ci',
	`description` VARCHAR(255) NULL COMMENT '描述/简介' COLLATE 'utf8_unicode_ci',
	`customer` VARCHAR(255) NOT NULL COMMENT '客户' COLLATE 'utf8_unicode_ci',
	`status` INT(2) NULL COMMENT '状态(0-禁用,1-启用)',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='广告'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.advert_statistics 结构
CREATE TABLE IF NOT EXISTS `advert_statistics` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`ad_id` INT(11) NOT NULL COMMENT '广告ID',
	`anonymous_user` VARCHAR(50) NULL DEFAULT NULL COMMENT '点击的用户（匿名用户,记录手机唯一信息）' COLLATE 'utf8_unicode_ci',
	`customer_id` INT(11) NULL DEFAULT NULL COMMENT '注册用户ID',
	`create_user` INT(11) NULL DEFAULT NULL COMMENT '点击的用户（注册用户）',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='广告统计'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.category 结构
CREATE TABLE IF NOT EXISTS `category` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR(50) NOT NULL COMMENT '类别名称' COLLATE 'utf8_unicode_ci',
	`parent_id` INT(11) NULL COMMENT '父类别',
	`depth` INT(3) NULL COMMENT '深度(1-min)',
	`status` INT(2) NULL COMMENT '状态(0-禁用,1-启用)',
	`priority` INT(5) NULL COMMENT '优先级(1-max)',
	`query_code` VARCHAR(255) NULL DEFAULT NULL COMMENT '查询编码(树形ID记录1-2-2)' COLLATE 'utf8_unicode_ci',
	`create_user` INT(11) NOT NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='分类'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.collection 结构
CREATE TABLE IF NOT EXISTS `collection` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`mark` VARCHAR(1024) NOT NULL COMMENT '收藏关联ID(可存储多个,eg:1,2,54,6)' COLLATE 'utf8_unicode_ci',
	`type` CHAR(2) NOT NULL DEFAULT 'S' COMMENT '收藏类型(S-商户，G-商品)' COLLATE 'utf8_unicode_ci',
	`page` INT(11) NULL DEFAULT NULL COMMENT '分页(mark因为太多而采取分页)',
	`count` INT(11) NOT NULL DEFAULT '0' COMMENT '一页当前数量',
	`customer_id` INT(11) NOT NULL DEFAULT '0' COMMENT '注册用户ID',
	`create_user` INT(11) NOT NULL COMMENT '收藏人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '收藏时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='收藏'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.shop 结构
CREATE TABLE IF NOT EXISTS `shop` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`shop_name` VARCHAR(20) NOT NULL DEFAULT 'O' COMMENT '类别名称' COLLATE 'utf8_unicode_ci',
	`contact_user` VARCHAR(20) NOT NULL COMMENT '联系人' COLLATE 'utf8_unicode_ci',
	`contact_phone` VARCHAR(20) NOT NULL COMMENT '商铺电话' COLLATE 'utf8_unicode_ci',
	`verify_pic_path` VARCHAR(255) NOT NULL COMMENT '营业执照图片路径' COLLATE 'utf8_unicode_ci',
	`description` VARCHAR(255) NULL DEFAULT NULL COMMENT '描述/简介' COLLATE 'utf8_unicode_ci',
	`status` INT(2) NULL DEFAULT NULL COMMENT '状态(0-禁用,1-启用,2-审核)',
	`pic_path` VARCHAR(255) NOT NULL COMMENT '商铺Logo路径' COLLATE 'utf8_unicode_ci',
	`latitude` DOUBLE NULL DEFAULT NULL COMMENT '纬度',
	`longitude` DOUBLE NULL DEFAULT NULL COMMENT '经度',
	`area_code` VARCHAR(50) NULL DEFAULT NULL COMMENT '区域编码' COLLATE 'utf8_unicode_ci',
	`sell_no` VARCHAR(255) NOT NULL COMMENT '营业号' COLLATE 'utf8_unicode_ci',
	`customer_id` INT(11) NULL DEFAULT NULL COMMENT '注册用户ID',
	`create_user` INT(11) NULL DEFAULT NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='商铺'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.preferential 结构
CREATE TABLE IF NOT EXISTS `preferential` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`title` VARCHAR(80) NULL COMMENT '标题' COLLATE 'utf8_unicode_ci',
	`type` CHAR(2) NOT NULL DEFAULT 'O' COMMENT '类别(O-减价优惠,A-活动优惠)' COLLATE 'utf8_unicode_ci',
	`was_price` VARCHAR(10) NULL COMMENT '原价' COLLATE 'utf8_unicode_ci',
	`now_price` VARCHAR(10) NULL COMMENT '现价' COLLATE 'utf8_unicode_ci',
	`off` VARCHAR(10) NULL COMMENT '折扣' COLLATE 'utf8_unicode_ci',
	`description` VARCHAR(255) NULL COMMENT '描述(当类别为活动优惠时,必填项)' COLLATE 'utf8_unicode_ci',
	`status` INT(2) NULL COMMENT '状态(0-禁用,1-启用)',
	`shop_id` INT(11) NOT NULL COMMENT '商铺ID',
	`pic_path` VARCHAR(255) NOT NULL COMMENT '图片路径' COLLATE 'utf8_unicode_ci',
	`start_time` BIGINT(20) NOT NULL COMMENT '起始时间',
	`end_time` BIGINT(20) NOT NULL COMMENT '结束时间',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='优惠'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.customer 结构
CREATE TABLE IF NOT EXISTS `customer` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`type` CHAR(2) NOT NULL DEFAULT 'N' COMMENT '类别(N-注册用户,B-商户)' COLLATE 'utf8_unicode_ci',
	`account` VARCHAR(20) NOT NULL COMMENT '账号' COLLATE 'utf8_unicode_ci',
	`name` VARCHAR(20) NOT NULL COMMENT '名称(默认为account)' COLLATE 'utf8_unicode_ci',
	`password` VARCHAR(50) NOT NULL COMMENT '密码' COLLATE 'utf8_unicode_ci',
	`status` INT(2) NULL DEFAULT NULL COMMENT '状态(0-禁用,1-启用)',
	`mobile` VARCHAR(50) NULL DEFAULT NULL COMMENT '手机号码' COLLATE 'utf8_unicode_ci',
	`email` VARCHAR(255) NULL DEFAULT NULL COMMENT '电子邮箱' COLLATE 'utf8_unicode_ci',
	`key` VARCHAR(255) NULL DEFAULT NULL COMMENT '用户唯一信息（相关手机）' COLLATE 'utf8_unicode_ci',
	`gender` TINYINT(4) NULL DEFAULT NULL COMMENT '男:1女:0',
	`create_user` INT(11) NULL DEFAULT NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='注册用户'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;

-- 导出  表 db_lbsp.comment 结构
CREATE TABLE IF NOT EXISTS `comment` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`preferential_id` INT(11) NOT NULL COMMENT '优惠信息ID',
	`content` VARCHAR(1024) NOT NULL COMMENT '评论内容' COLLATE 'utf8_unicode_ci',
	`anonymous_user` VARCHAR(50) NULL COMMENT '匿名用户唯一信息' COLLATE 'utf8_unicode_ci',
	`customer_id` INT(11) NULL COMMENT '注册用户ID',
	`status` TINYINT(4) NULL DEFAULT '1' COMMENT '状态(1-整除,0-删除)',
	`create_user` INT(11) NULL DEFAULT NULL COMMENT '评论人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '评论时间',
	`update_user` INT(11) NULL DEFAULT NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`)
)
COMMENT='评论'
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;


-- 导出  表 db_lbsp.role_parent 结构
CREATE TABLE IF NOT EXISTS `role_parent` (
  `child_id` int(11) NOT NULL COMMENT '子角色',
  `parent_id` int(11) NOT NULL COMMENT '父角色',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`child_id`,`parent_id`)
) COMMENT='父子角色' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.task 结构
CREATE TABLE IF NOT EXISTS `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) NOT NULL COLLATE 'utf8_unicode_ci',
  `name` varchar(255) DEFAULT NULL COLLATE 'utf8_unicode_ci',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='任务' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.task_queue 结构
CREATE TABLE IF NOT EXISTS `task_queue` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `task_name` varchar(250) DEFAULT NULL COMMENT '任务名称' COLLATE 'utf8_unicode_ci',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述' COLLATE 'utf8_unicode_ci',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '时间表达式' COLLATE 'utf8_unicode_ci',
  `cron_text` varchar(255) DEFAULT NULL COMMENT '时间表达式文本' COLLATE 'utf8_unicode_ci',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  `task_status` varchar(2) DEFAULT NULL COMMENT '状态' COLLATE 'utf8_unicode_ci',
  `task_class` int(11) DEFAULT NULL COMMENT '工作任务对应的具体任务类',
  PRIMARY KEY (`id`)
) COMMENT='任务队列' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL COLLATE 'utf8_unicode_ci',
  `username` varchar(80) DEFAULT NULL COLLATE 'utf8_unicode_ci',
  `email` varchar(255) NOT NULL COLLATE 'utf8_unicode_ci',
  `password` varchar(255) NOT NULL COLLATE 'utf8_unicode_ci',
  `security_key` varchar(255) DEFAULT NULL COLLATE 'utf8_unicode_ci',
  `security_time` timestamp NULL DEFAULT NULL,
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  `status` int(11) DEFAULT '0',
  `login_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `auth_code` varchar(20) DEFAULT NULL COMMENT '修改密码时的验证码' COLLATE 'utf8_unicode_ci',
  `auth_time` timestamp NULL DEFAULT NULL COMMENT '申请修改密码时的时间（30分钟后时效）',
  PRIMARY KEY (`id`)
) COMMENT='用户' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
	`create_user` INT(11) NULL COMMENT '创建人ID',
	`create_time` BIGINT(20) NOT NULL COMMENT '创建时间',
	`update_user` INT(11) NULL COMMENT '更新者',
	`update_time` BIGINT(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`role_id`)
) COMMENT='用户角色' COLLATE 'utf8_unicode_ci' ENGINE=InnoDB;

-- ----------------------------------------超级管理员账号--------------------------------
delete from `user` where id = '1';
INSERT INTO `user` (`id`, `account`, `username`, `email`, `password`, `security_key`, `security_time`, `create_time`, `update_time`, `create_user`, `status`, `update_user`, `login_time`, `auth_code`, `auth_time`) VALUES
(1, 'root', 'root', 'different_cow@163.com', 'b7c1c40e68a6978d32ef5ce8fcba0efe8cfb94d59c6536e1066dea19522251b9230fba8645967e4b', NULL, NULL, (UNIX_TIMESTAMP(now()) * 1000), (UNIX_TIMESTAMP(now()) * 1000), 1, 1, 1, NULL, NULL, NULL);
-- ----------------------------------------超级管理员角色--------------------------------
delete from `role` where id = '1';
INSERT INTO `role` (`id`, `name`, `status`, `func_count`, `user_count`, `create_time`, `update_time`, `create_user`, `update_user`) VALUES
(1, '管理员', 1, 1, 1, (UNIX_TIMESTAMP(now()) * 1000), (UNIX_TIMESTAMP(now()) * 1000), 1,1);
-- ----------------------------------------超级管理员角色账户关联--------------------------------
delete from `user_role` where user_id = 1 and role_id=1;
INSERT INTO `user_role` (`user_id`, `role_id`, `create_time`, `create_user`,`update_user`,`update_time`) VALUES(1, 1, (UNIX_TIMESTAMP(now()) * 1000), 1, 1, (UNIX_TIMESTAMP(now()) * 1000));
-- -----------------------------------------页面资源--------------------------------------
delete from page_operate;
insert into page_operate(id,code,parent_id,parent_code,`name`,sort_index,create_time,update_time,create_user,update_user) VALUES
(1,'personalfile',NULL,NULL,'个人档案',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(2,'personalinfo',1,'personalfile','修改个人资料',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(3,'personalpwd',1,'personalfile','修改密码',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(4,'interadmin',NULL,NULL,'接口管理',3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(5,'interreg',4,'interadmin','接口信息',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(6,'interapp',4,'interadmin','外接应用',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(7,'interopen',4,'interadmin','开放接口',3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(8,'sysadmim',NULL,NULL,'系统功能',4,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(9,'param',8,'sysadmim','参数设置',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(10,'task',8,'sysadmim','工作任务',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(11,'lbsp',NULL,NULL,'LBSP',4,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(12,'comment',11,'lbsp','评论',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(13,'feedback',11,'lbsp','意见反馈',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1);

-- -----------------------------------------功能操作资源----------------------------------
delete from function_operate;
insert into function_operate(id,code,`name`,page_id,url,base_url,method,path_param,sort_index,create_time,update_time,create_user,update_user)
VALUES
(1,'viewparam','查看参数',9,'/param/lst',NULL,'GET',0,1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(2,'viewparam','查看参数',9,'/param/types',NULL,'GET',0,2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(3,'viewparam','查看参数',9,'/param/{id}',NULL,'GET',1,3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(4,'delparam','删除参数',9,'/param/del',NULL,'DELETE',0,4,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(5,'addparam','增加参数',9,'/param/add',NULL,'POST',0,5,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(6,'modifyparam','修改参数',9,'/param/upt',NULL,'PUT',0,6,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(7,'viewpersonal','查看个人资料',1,'/userInfo/me',NULL,'GET',0,1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(8,'modifypersonal','修改个人资料',1,'/userInfo/upt/me',NULL,'PUT',0,2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(9,'viewcomment','查看评论',12,'/comment/lst',NULL,'GET',0,1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(10,'viewcomment','查看评论',12,'/comment/{id}',NULL,'GET',1,2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(11,'modifycomment','更改评论状态',12,'/comment/upt',NULL,'PUT',0,3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(12,'delcomment','删除评论',12,'/comment/del',NULL,'DELETE',0,4,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(13,'viewfeedback','查看反馈',13,'/feedback/lst',NULL,'GET',0,1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(14,'viewfeedback','查看反馈',13,'/feedback/{id}',NULL,'GET',1,2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(15,'delfeedback','删除反馈',13,'/feedback/del',NULL,'DELETE',0,3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1);

-- -----------------------------------------关联操作资源----------------------------------
delete from `privilege` where privilege_master = 'role' and  privilege_master_value =  1;
insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)
select 'role',1, 'page',id,'E',(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1 from page_operate;
insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)
select 'role',1, 'function',id,'E',(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1 from function_operate;

-- ------------------2015/03/22 09:49:12 support content------------------

insert into page_operate(code,parent_id,parent_code,`name`,sort_index,create_time,update_time,create_user,update_user) VALUES('collection',11,'lbsp','收藏',2,1427032133107,1427032133107,1,1);

insert into function_operate(code,`name`,page_id,url,base_url,method,path_param,sort_index,create_time,update_time,create_user,update_user) VALUES ('viewcollection','查看收藏',14,'/collection/lst',NULL,'GET',0,1,1427032133107,1427032133107,1,1),('viewcollection','查看收藏',14,'/collection/{id}',NULL,'GET',1,2,1427032133107,1427032133107,1,1),('addcollection','添加收藏',14,'/collection/add',NULL,'POST',0,3,1427032133107,1427032133107,1,1),('delcollection','删除收藏',14,'/collection/del',NULL,'DELETE',0,4,1427032133107,1427032133107,1,1),('modifycollection','修改收藏',14,'/collection/upt',NULL,'PUT',0,5,1427032133107,1427032133107,1,1);

insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)  select 'role',1, 'page',id,'E',1427032133107,1427032133107,1,1 from page_operate where id = 14;

insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)  select 'role',1, 'function',id,'E',1427032133107,1427032133107,1,1 from function_operate where code in ('viewcollection','delcollection','modifycollection','addcollection');

-- ------------------2015/03/22 09:49:12 support content------------------