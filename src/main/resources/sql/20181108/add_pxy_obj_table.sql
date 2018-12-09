INSERT INTO function_service_dict
   ()
VALUES
   (152, 571, 0, 1, 'monit', 640, 'PXY_OBJ_TRUSTED_CA_CERT', '', 1, null, "2018/10/06 14:21:00",null , "2018/10/06 14:21:00", '', 0);

INSERT INTO function_region_dict
   ()
VALUES
   (603, 571, '', 1, 'PXY_OBJ_TRUSTED_CA_CERT', '可信证书配置', 1, 2, 6, null, "2018/10/06 14:21:00", null, "2018/10/06 14:21:00", '', '', '', '', '', '', '', '', '', '', '', 1, 0);

INSERT INTO sys_menu
   (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`)
VALUES
   (1157, 1121, '0,1,86,717,1121,', 'PXY_OBJ_TRUSTED_CA_CERT', '可信证书', 3, '/proxy/intercept/strateagy/trustedCertList', '', '', 1, 'proxy:trustedCert:config', '1', "2018/11/6 14:58:41", '1', "2018/11/6 14:58:41", '', 1, '', 0, 0, 571);

INSERT INTO sys_menu
   (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`)
VALUES
   (1158, 1122, '0,1,150,750,1122,', 'PXY_OBJ_TRUSTED_CA_CERT', '可信证书', 420, '/proxy/intercept/strateagy/trustedCertList', '', '', 1, 'proxy:trustedCert:confirm', '1', "2018/11/6 16:58:48", '1', "2018/11/6 17:00:57", '', 1, '', 0, 0, );

INSERT INTO sys_menu
   (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`)
VALUES
   (1159, 1123, '0,1,151,865,1123,', 'PXY_OBJ_TRUSTED_CA_CERT', '可信证书', 32, '/proxy/intercept/strateagy/trustedCertList', '', '', 1, 'proxy:trustedCert:audit', '1', "2018/11/6 17:00:16", '1', "2018/11/6 17:00:16", '', 1, '', 0, 0, );


#add pxy_obj_trusted_ca_cert
CREATE TABLE `pxy_obj_trusted_ca_cert` (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '证书ID',
  `cfg_desc` varchar(128) DEFAULT '' COMMENT '证书名称',
  `cfg_type` varchar(64) DEFAULT NULL,
  `cert_file` varchar(1024) DEFAULT NULL COMMENT '证书路径',
  `action` int(11) DEFAULT NULL COMMENT '阻断',
  `is_valid` int(11) DEFAULT NULL COMMENT '0无效，1有效，-1删除;1）	未审核时配置可删除;2）	审核通过，此字段置1;3）	取消审核通过，此字段置0',
  `is_audit` int(11) DEFAULT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）	审核未通过，配置可修改;2）	审核通过，配置不可删除，只能取消审核通过',
  `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `audit_time` datetime DEFAULT NULL,
  `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
  `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
  `is_area_effective` int(11) NOT NULL DEFAULT 0 COMMENT '0否，1是',
  `classify` varchar(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
  `attribute` varchar(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
  `lable` varchar(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
  `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
  `function_id` int(11) NOT NULL,
  `cfg_region_code` int(11) DEFAULT NULL,
  `compile_id` int(11) NOT NULL,
  `issuer` varchar(1000) DEFAULT '' COMMENT '发者颁',
  `cancel_request_id` int(11) DEFAULT NULL COMMENT '取消审核来函',
  PRIMARY KEY (`cfg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4;

#add pxy_obj_trusted_ca_crl
CREATE TABLE `pxy_obj_trusted_ca_crl` (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'crlID',
  `cfg_desc` varchar(128) DEFAULT '' COMMENT '证书路径',
  `cfg_type` varchar(64) DEFAULT NULL,
  `crl_file` varchar(1024) DEFAULT NULL,
  `cert_id` bigint(20) DEFAULT 0 COMMENT '证书id',
  `action` int(11) DEFAULT NULL COMMENT '阻断',
  `is_valid` int(11) DEFAULT NULL COMMENT '0无效，1有效，-1删除;1）	未审核时配置可删除;2）	审核通过，此字段置1;3）	取消审核通过，此字段置0',
  `is_audit` int(11) DEFAULT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）	审核未通过，配置可修改;2）	审核通过，配置不可删除，只能取消审核通过',
  `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `audit_time` datetime DEFAULT NULL,
  `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
  `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
  `is_area_effective` int(11) NOT NULL DEFAULT 0 COMMENT '0否，1是',
  `classify` varchar(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
  `attribute` varchar(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
  `lable` varchar(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
  `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
  `function_id` int(11) NOT NULL,
  `cfg_region_code` int(11) DEFAULT NULL,
  `compile_id` int(11) NOT NULL,
  `issuer` varchar(128) DEFAULT NULL COMMENT '发者颁',
  `cancel_request_id` int(11) DEFAULT NULL COMMENT '取消审核来函',
  PRIMARY KEY (`cfg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;
   