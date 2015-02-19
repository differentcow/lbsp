-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.5.20 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

-- 导出 jr_esl 的数据库结构
CREATE DATABASE IF NOT EXISTS `db_lbsp` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db_lbsp`;

-- 数据导出被取消选择。


-- 导出  表 jr_esl.interface 结构
CREATE TABLE IF NOT EXISTS `interface` (
  `id` varchar(50) NOT NULL COMMENT '接口ID',
  `code` varchar(255) NOT NULL COMMENT '接口编码',
  `name` varchar(255) NOT NULL COMMENT '接口名称',
  `url` varchar(255) NOT NULL COMMENT '接口资源路径',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `method` varchar(10) DEFAULT NULL COMMENT 'http方法',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_user` varchar(50) DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 jr_esl.interface_app 结构
CREATE TABLE IF NOT EXISTS `application` (
  `id` varchar(50) NOT NULL COMMENT '外接应用ID',
  `code` varchar(255) NOT NULL COMMENT '外接应用编码',
  `name` varchar(255) NOT NULL COMMENT '外接应用名称',
  `auth_code` varchar(255) NOT NULL COMMENT '授权码（系统自动生成）',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_user` varchar(255) DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.interface_link 结构
CREATE TABLE IF NOT EXISTS `interface_app` (
  `app_id` varchar(50) NOT NULL COMMENT '外接应用ID',
  `interface_id` varchar(50) NOT NULL COMMENT '接口ID',
  `start_date` timestamp NULL DEFAULT NULL COMMENT '接口开放起始日期',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '接口开放结束日期',
  `status` varchar(2) DEFAULT '0' COMMENT '状态(0:永久开放接口,1:按时间开放接口)',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `create_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`app_id`,`interface_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.operate 结构
CREATE TABLE IF NOT EXISTS `page_operate` (
  `id` varchar(50) NOT NULL COMMENT '页面ID(oper_id)',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父ID',
  `parent_code` varchar(50) DEFAULT NULL COMMENT '父编码',
  `name` varchar(100) NOT NULL COMMENT '页面名称',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面资源操作表';

-- 导出  表 jr_esl.operate 结构
CREATE TABLE IF NOT EXISTS `function_operate` (
  `id` varchar(50) NOT NULL COMMENT '功能ID(oper_id)',
  `code` varchar(50) DEFAULT '0' COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '功能名称',
  `url` varchar(255) NOT NULL COMMENT '功能别名',
  `base_url` varchar(255) DEFAULT NULL COMMENT '基础路径',
  `method` varchar(20) DEFAULT NULL COMMENT 'HTTP方法',
  `path_param` int(11) DEFAULT 0 COMMENT 'url参数总数',
  `page_id` varchar(50) DEFAULT NULL COMMENT '所属页面',
  `sort_index` int(11) DEFAULT NULL COMMENT '排序',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能资源操作表';

-- 数据导出被取消选择。

-- 导出  表 jr_esl.operate 结构
CREATE TABLE IF NOT EXISTS `privilege` (
  `privilege_id` varchar(50) NOT NULL COMMENT 'ID',
  `privilege_master` varchar(50) DEFAULT '0' COMMENT '编码',
  `privilege_master_value` varchar(100) NOT NULL COMMENT '功能名称',
  `privilege_access` varchar(255) DEFAULT NULL COMMENT '功能别名',
  `privilege_access_value` varchar(1000) DEFAULT NULL COMMENT '描述',
  `privilege_operation` varchar(2) DEFAULT NULL COMMENT 'E:enabled，D:disabled',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权表';

-- 导出  表 jr_esl.parameter 结构
CREATE TABLE IF NOT EXISTS `parameter` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `name` varchar(255) NOT NULL COMMENT '名称(值)',
  `code` varchar(255) NOT NULL COMMENT '编码(键)',
  `type` varchar(50) NOT NULL COMMENT '类型',
  `type_meaning` varchar(255) NOT NULL COMMENT '类型名称',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_date` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` varchar(50) NOT NULL COMMENT '角色ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `status` int(11) NOT NULL COMMENT '状态(0:无效,1:有效)',
  `func_count` int(11) DEFAULT '0' COMMENT '此角色包含功能数',
  `user_count` int(11) DEFAULT '0' COMMENT '此角色使用用户数',
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 数据导出被取消选择。

-- 导出  表 jr_esl.role_parent 结构
CREATE TABLE IF NOT EXISTS `role_parent` (
  `child_id` varchar(50) NOT NULL COMMENT '子角色',
  `parent_id` varchar(50) NOT NULL DEFAULT '' COMMENT '父角色',
  `operator` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`child_id`,`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.task 结构
CREATE TABLE IF NOT EXISTS `task` (
  `id` varchar(50) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 jr_esl.task_queue 结构
CREATE TABLE IF NOT EXISTS `task_queue` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `task_name` varchar(250) DEFAULT NULL COMMENT '任务名称',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '时间表达式',
  `cron_text` varchar(255) DEFAULT NULL COMMENT '时间表达式文本',
  `create_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  `task_status` varchar(20) DEFAULT NULL COMMENT '状态',
  `task_class` varchar(255) DEFAULT NULL COMMENT '工作任务对应的具体任务类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(50) NOT NULL,
  `account` varchar(255) NOT NULL,
  `username` varchar(80) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `security_key` varchar(255) DEFAULT NULL,
  `security_time` timestamp NULL DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `update_user` varchar(50) DEFAULT NULL,
  `login_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `auth_code` varchar(20) DEFAULT NULL COMMENT '修改密码时的验证码',
  `auth_time` timestamp NULL DEFAULT NULL COMMENT '申请修改密码时的时间（30分钟后时效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。

-- 导出  表 jr_esl.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  `create_date` timestamp NULL DEFAULT NULL,
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------------------超级管理员账号--------------------------------
delete from `user` where id = '000000000001';
INSERT INTO `user` (`id`, `account`, `username`, `email`, `password`, `security_key`, `security_time`, `create_date`, `last_update_date`, `create_user`, `status`, `update_user`, `login_time`, `auth_code`, `auth_time`) VALUES
('000000000001', 'root', 'root', 'different_cow@163.com', '6f9c6152b28c181909013e60b808ace4bdca8f0662f2f139e042ee9d109c7118383e4163471c9ec9', NULL, NULL, now(), now(), '000000000001', 1, '000000000001', NULL, NULL, NULL);
-- ----------------------------------------超级管理员角色--------------------------------
delete from `role` where id = '000000000001';
INSERT INTO `role` (`id`, `name`, `status`, `func_count`, `user_count`, `create_date`, `last_update_date`, `create_user`, `update_user`) VALUES
('000000000001', '管理员', 1, 1, 1, now(), now(), '000000000001','000000000001');
-- ----------------------------------------超级管理员角色账户关联--------------------------------
delete from `user_role` where user_id = '000000000001' and role_id='000000000001';
INSERT INTO `user_role` (`user_id`, `role_id`, `create_date`, `operator`) VALUES('000000000001', '000000000001', now(), '000000000001');
-- -----------------------------------------页面资源--------------------------------------
delete from page_operate;
insert into page_operate(id,code,parent_id,parent_code,`name`,sort_index,create_date,last_update_date,create_user,update_user) VALUES
('000000000001','personalfile',NULL,NULL,'个人档案',1,now(),now(),'000000000001','000000000001'),
('000000000002','personalinfo','000000000001','personalfile','修改个人资料',1,now(),now(),'000000000001','000000000001'),
('000000000003','personalpwd','000000000001','personalfile','修改密码',2,now(),now(),'000000000001','000000000001'),
('000000000004','interadmin',NULL,NULL,'接口管理',2,now(),now(),'000000000001','000000000001'),
('000000000005','interreg','000000000004','interadmin','接口信息',1,now(),now(),'000000000001','000000000001'),
('000000000006','interapp','000000000004','interadmin','外接应用',2,now(),now(),'000000000001','000000000001'),
('000000000007','interopen','000000000004','interadmin','开放接口',3,now(),now(),'000000000001','000000000001'),
('000000000008','sysadmim',NULL,NULL,'系统功能',3,now(),now(),'000000000001','000000000001'),
('000000000009','param','000000000008','sysadmim','参数设置',1,now(),now(),'000000000001','000000000001'),
('000000000010','task','000000000008','sysadmim','工作任务',2,now(),now(),'000000000001','000000000001');

-- -----------------------------------------功能操作资源----------------------------------
delete from function_operate;
insert into function_operate(id,code,`name`,page_id,url,base_url,method,path_param,sort_index,create_date,last_update_date,create_user,update_user)
VALUES
('000000000001','viewparam','查看参数','000000000009','/param/lst',NULL,'GET',0,1,now(),now(),'000000000001','000000000001'),
('000000000002','viewparam','查看参数','000000000009','/param/types',NULL,'GET',0,2,now(),now(),'000000000001','000000000001'),
('000000000003','viewparam','查看参数','000000000009','/param/{id}',NULL,'GET',1,3,now(),now(),'000000000001','000000000001'),
('000000000004','delparam','删除参数','000000000009','/param/del',NULL,'DELETE',0,4,now(),now(),'000000000001','000000000001'),
('000000000005','addparam','增加参数','000000000009','/param/add',NULL,'POST',0,5,now(),now(),'000000000001','000000000001'),
('000000000006','modifyparam','修改参数','000000000009','/param/upt',NULL,'PUT',0,6,now(),now(),'000000000001','000000000001'),
('000000000007','viewpersonal','查看个人资料','000000000001','/userInfo/me',NULL,'GET',0,1,now(),now(),'000000000001','000000000001'),
('000000000008','modifypersonal','修改个人资料','000000000001','/userInfo/upt/me',NULL,'PUT',0,2,now(),now(),'000000000001','000000000001'),
('000000000009','modifypwd','修改密码','000000000001','/userInfo/new',NULL,'PUT',0,3,now(),now(),'000000000001','000000000001');

-- -----------------------------------------关联操作资源----------------------------------
delete from `privilege` where privilege_master = 'role' and  privilege_master_value =  '000000000001';
insert into privilege(privilege_id,privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_date,last_update_date,create_user,update_user)
select CONCAT('page_',id),'role','000000000001', 'page',id,'E',now(),now(),'000000000001','000000000001' from page_operate;
insert into privilege(privilege_id,privilege_master,privilege_master_value,privilege_access,privilege_access_value,privilege_operation,create_date,last_update_date,create_user,update_user)
select CONCAT('func_',id),'role','000000000001', 'function',id,'E',now(),now(),'000000000001','000000000001' from function_operate;