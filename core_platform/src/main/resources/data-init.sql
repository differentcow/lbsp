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


-- 导出  表 db_lbsp.application 结构
CREATE TABLE IF NOT EXISTS `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '外接应用ID',
  `code` varchar(255) NOT NULL COMMENT '外接应用编码',
  `name` varchar(255) NOT NULL COMMENT '外接应用名称',
  `auth_code` varchar(255) NOT NULL COMMENT '授权码（系统自动生成）',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.function_operate 结构
CREATE TABLE IF NOT EXISTS `function_operate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能ID(oper_id)',
  `code` varchar(50) DEFAULT '0' COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '功能名称',
  `url` varchar(255) NOT NULL COMMENT '功能别名',
  `base_url` varchar(255) DEFAULT NULL COMMENT '基础路径',
  `method` varchar(20) DEFAULT NULL COMMENT 'HTTP方法',
  `path_param` int(11) DEFAULT '0' COMMENT 'url参数总数',
  `page_id` varchar(50) DEFAULT NULL COMMENT '所属页面',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能资源操作表';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.goods_attribute 结构
CREATE TABLE IF NOT EXISTS `goods_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) DEFAULT NULL COMMENT '属性名称',
  `value` varchar(128) DEFAULT NULL COMMENT '属性值',
  `goods_info_id` int(11) DEFAULT NULL COMMENT '商品表外键id',
  `create_time` bigint(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(11) DEFAULT NULL COMMENT '更新时间',
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.goods_image 结构
CREATE TABLE IF NOT EXISTS `goods_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_info_id` int(11) DEFAULT NULL COMMENT '商品表外键id',
  `image_path` varchar(256) DEFAULT NULL COMMENT '商品图片',
  `image_desc` varchar(256) DEFAULT NULL COMMENT '图片描述',
  `image_name` varchar(32) DEFAULT NULL COMMENT '图片名称',
  `create_time` bigint(11) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(11) DEFAULT NULL COMMENT '更新时间',
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品图片表(分表)';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.goods_info 结构
CREATE TABLE IF NOT EXISTS `goods_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(32) DEFAULT NULL COMMENT '商品名称',
  `introduction` varchar(512) DEFAULT NULL COMMENT '商品介绍',
  `version` varchar(32) DEFAULT NULL COMMENT '商品型号',
  `origin_price` decimal(6,2) DEFAULT NULL COMMENT '商品原价',
  `discount_price` decimal(6,2) DEFAULT NULL COMMENT '商品折扣价',
  `discount_rate` decimal(4,2) DEFAULT NULL COMMENT '折扣率',
  `seller_id` int(11) DEFAULT NULL COMMENT '所属商家id',
  `thumbnail_path` varchar(256) DEFAULT NULL COMMENT '缩略图',
  `deleted` int(2) DEFAULT NULL COMMENT '删除标志位(0->未删除,1->已删除)',
  `online_state` int(2) DEFAULT NULL COMMENT '上下线状态(0->下线,1->上线)',
  `signboard_state` int(2) DEFAULT NULL COMMENT '招牌状态',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `rank` int(3) DEFAULT NULL COMMENT '排序',
  `type` int(3) DEFAULT NULL COMMENT '商品类型',
  `goods_desc` varchar(512) DEFAULT NULL COMMENT '商品描述',
  `preferentia_info_id` int(11) DEFAULT NULL COMMENT '优惠id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品信息表';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.interface 结构
CREATE TABLE IF NOT EXISTS `interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '接口ID',
  `code` varchar(255) NOT NULL COMMENT '接口编码',
  `name` varchar(255) NOT NULL COMMENT '接口名称',
  `url` varchar(255) NOT NULL COMMENT '接口资源路径',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `method` varchar(10) DEFAULT NULL COMMENT 'http方法',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.interface_app 结构
CREATE TABLE IF NOT EXISTS `interface_app` (
  `app_id` varchar(50) NOT NULL COMMENT '外接应用ID',
  `interface_id` varchar(50) NOT NULL COMMENT '接口ID',
  `start_date` bigint(20) DEFAULT NULL COMMENT '接口开放起始日期',
  `end_date` bigint(20) DEFAULT NULL COMMENT '接口开放结束日期',
  `status` char(2) DEFAULT '0' COMMENT '状态(0:永久开放接口,1:按时间开放接口)',
  `create_user` int(11) DEFAULT '0',
  `update_user` int(11) DEFAULT '0',
  `update_time` bigint(20) DEFAULT NULL COMMENT '操作人',
  `create_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`app_id`,`interface_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.page_operate 结构
CREATE TABLE IF NOT EXISTS `page_operate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '页面ID(oper_id)',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父ID',
  `parent_code` varchar(50) DEFAULT NULL COMMENT '父编码',
  `name` varchar(100) NOT NULL COMMENT '页面名称',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面资源操作表';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.parameter 结构
CREATE TABLE IF NOT EXISTS `parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '名称(值)',
  `code` varchar(50) NOT NULL COMMENT '编码(键)',
  `type` varchar(50) NOT NULL COMMENT '类型',
  `type_meaning` varchar(80) NOT NULL COMMENT '类型名称',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `create_user` int(11) DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `update_user` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.privilege 结构
CREATE TABLE IF NOT EXISTS `privilege` (
  `privilege_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `privilege_master` varchar(50) DEFAULT '0' COMMENT '编码',
  `privilege_master_value` int(11) NOT NULL COMMENT '功能名称',
  `privilege_access` varchar(255) DEFAULT NULL COMMENT '功能别名',
  `privilege_access_value` int(11) DEFAULT NULL COMMENT '描述',
  `privilege_operation` varchar(2) DEFAULT NULL COMMENT 'E:enabled，D:disabled',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权表';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL COMMENT '角色ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `status` int(11) NOT NULL COMMENT '状态(0:无效,1:有效)',
  `func_count` int(11) DEFAULT '0' COMMENT '此角色包含功能数',
  `user_count` int(11) DEFAULT '0' COMMENT '此角色使用用户数',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.role_parent 结构
CREATE TABLE IF NOT EXISTS `role_parent` (
  `child_id` int(11) NOT NULL COMMENT '子角色',
  `parent_id` int(11) NOT NULL COMMENT '父角色',
  `create_user` int(11) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`child_id`,`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.task 结构
CREATE TABLE IF NOT EXISTS `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.task_queue 结构
CREATE TABLE IF NOT EXISTS `task_queue` (
  `id` int(11) NOT NULL COMMENT 'ID',
  `task_name` varchar(250) DEFAULT NULL COMMENT '任务名称',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '时间表达式',
  `cron_text` varchar(255) DEFAULT NULL COMMENT '时间表达式文本',
  `create_user` int(11) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `update_user` int(11) DEFAULT NULL,
  `task_status` varchar(2) DEFAULT NULL COMMENT '状态',
  `task_class` int(11) DEFAULT NULL COMMENT '工作任务对应的具体任务类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `username` varchar(80) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `security_key` varchar(255) DEFAULT NULL,
  `security_time` timestamp NULL DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `update_user` int(11) DEFAULT NULL,
  `login_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `auth_code` varchar(20) DEFAULT NULL COMMENT '修改密码时的验证码',
  `auth_time` timestamp NULL DEFAULT NULL COMMENT '申请修改密码时的时间（30分钟后时效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 db_lbsp.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `create_user` int(11) DEFAULT NULL COMMENT '操作人',
  `update_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------------------超级管理员账号--------------------------------
delete from `user` where id = '1';
INSERT INTO `user` (`id`, `account`, `username`, `email`, `password`, `security_key`, `security_time`, `create_time`, `update_time`, `create_user`, `status`, `update_user`, `login_time`, `auth_code`, `auth_time`) VALUES
(1, 'root', 'root', 'different_cow@163.com', '6f9c6152b28c181909013e60b808ace4bdca8f0662f2f139e042ee9d109c7118383e4163471c9ec9', NULL, NULL, (UNIX_TIMESTAMP(now()) * 1000), (UNIX_TIMESTAMP(now()) * 1000), 1, 1, 1, NULL, NULL, NULL);
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
(4,'interadmin',NULL,NULL,'接口管理',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(5,'interreg',4,'interadmin','接口信息',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(6,'interapp',4,'interadmin','外接应用',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(7,'interopen',4,'interadmin','开放接口',3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(8,'sysadmim',NULL,NULL,'系统功能',3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(9,'param',8,'sysadmim','参数设置',1,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1),
(10,'task',8,'sysadmim','工作任务',2,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1);

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
(9,'modifypwd','修改密码',1,'/userInfo/new',NULL,'PUT',0,3,(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1);

-- -----------------------------------------关联操作资源----------------------------------
delete from `privilege` where privilege_master = 'role' and  privilege_master_value =  1;
insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)
select 'role',1, 'page',id,'E',(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1 from page_operate;
insert into privilege(privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_time,update_time,create_user,update_user)
select 'role',1, 'function',id,'E',(UNIX_TIMESTAMP(now()) * 1000),(UNIX_TIMESTAMP(now()) * 1000),1,1 from function_operate;