-- ----------------------------
-- Table structure for proc_exec_log
-- ----------------------------
CREATE TABLE `proc_exec_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proc_name` varchar(200) DEFAULT NULL,
  `table_name` varchar(200) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=449 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for statistics_tables
-- ----------------------------
CREATE TABLE `statistics_tables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tab_name` varchar(200) NOT NULL,
  `is_valid` int(11) NOT NULL,
  `description` varchar(200) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

INSERT INTO `statistics_tables` VALUES ('1', 'app_byte_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('2', 'app_domain_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('3', 'app_features_index', '0', '');
INSERT INTO `statistics_tables` VALUES ('4', 'app_http_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('5', 'app_id_cfg', '0', '');
INSERT INTO `statistics_tables` VALUES ('6', 'app_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('7', 'app_policy_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('8', 'asn_keyword_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('9', 'av_cont_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('10', 'av_cont_url_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('11', 'av_file_sample_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('12', 'av_pic_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('13', 'av_pic_url_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('14', 'av_sign_sample_cfg', '0', '');
INSERT INTO `statistics_tables` VALUES ('15', 'av_voip_account_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('16', 'av_voip_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('17', 'byte_features_cfg', '0', '');
INSERT INTO `statistics_tables` VALUES ('18', 'complex_keyword_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('19', 'ddos_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('20', 'dns_domain_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('21', 'dns_ip_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('22', 'dns_res_strategy', '1', '');
INSERT INTO `statistics_tables` VALUES ('23', 'file_digest_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('24', 'ftp_keyword_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('25', 'http_body_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('26', 'http_req_head_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('27', 'http_res_head_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('28', 'http_url_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('29', 'ip_multiplex_pool_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('30', 'ip_port_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('31', 'l2tp_url_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('32', 'mail_keyword_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('33', 'multiple_cfg_relation', '0', '');
INSERT INTO `statistics_tables` VALUES ('34', 'num_boundary_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('35', 'policy_group_info', '0', '');
INSERT INTO `statistics_tables` VALUES ('36', 'pptp_url_cfg', '1', '');
INSERT INTO `statistics_tables` VALUES ('37', 'ssl_keyword_cfg', '1', '');

-- ----------------------------
-- Table structure for cfg_num_statistics
-- ----------------------------
CREATE TABLE `cfg_num_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `cfg_state` int(11) NOT NULL COMMENT '0未审核，1已审核，2审核未通过，3审核取消，-1删除',
  `cfg_type` varchar(128) DEFAULT NULL COMMENT '配置类型，与业务配置表中相同',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5888338 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for request_num_statistics
-- ----------------------------
CREATE TABLE `request_num_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL COMMENT '来函信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5913175 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Procedure structure for exec_procs
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `exec_procs`()
BEGIN
	call proc_statistics_request();
	call proc_statistics_config();
END;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for proc_statistics_config
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_statistics_config`()
BEGIN
	DECLARE ntime VARCHAR(40);/*当前时间*/
	DECLARE tabName VARCHAR(500);
	DECLARE description VARCHAR(500);
	DECLARE deleteSql VARCHAR(500);
	DECLARE done INT;/*游标标识*/
	DECLARE flag INT;/*循环标识*/
	DECLARE proc_log_table VARCHAR(100);/*存储过程日志表*/
	DECLARE proc_name VARCHAR(100);/*存储过程名称*/
	DECLARE icursor CURSOR FOR SELECT  tab_name FROM statistics_tables where is_valid=1;
	DECLARE CONTINUE HANDLER FOR NOT found SET done=1;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	SET done=0;
	SET proc_log_table='proc_exec_log';
	SET proc_name='proc_statistics_config';
	SET ntime=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%k:%S');
	set @deleteSql := 'delete from cfg_num_statistics';
	PREPARE  execs FROM @deleteSql;
	EXECUTE   execs;
	DEALLOCATE PREPARE execs;
	COMMIT;
	OPEN icursor;
		loop_iloop:LOOP
			FETCH icursor INTO tabName;
			SET description=tabName;
			set @descriptionStart=concat(description,'表统计start');
			/*统计当前配置表数据到统计表中start*/
			set @v_log_sql1 := concat('insert into ',proc_log_table,'(proc_name,table_name,log_time,description) values(?,?,?,?)');
			PREPARE  execs FROM @v_log_sql1;
			EXECUTE   execs using proc_name,proc_log_table,ntime,@descriptionStart;
			DEALLOCATE PREPARE execs;
			COMMIT;

			set @insert_statistics_sql :=concat('insert into cfg_num_statistics(function_id,service_id,action,cfg_type,cfg_state) select function_id,service_id,action,cfg_type,if(is_audit=3,3,if(is_audit=2,2,if(is_audit=1,1,if(is_valid=0,0,if(is_valid,-1,-1))))) cfg_state from ',tabName);
			PREPARE  execs FROM @insert_statistics_sql;
			EXECUTE   execs;
			DEALLOCATE PREPARE execs;
			COMMIT;

			set @descriptionEnd=concat(description,'表统计end');
			set @v_log_sql2 := concat('insert into ',proc_log_table,'(proc_name,table_name,log_time,description) values(?,?,?,?)');
			PREPARE  execs FROM @v_log_sql2;
			EXECUTE   execs using proc_name,proc_log_table,ntime,@descriptionEnd;
			DEALLOCATE PREPARE execs;
			COMMIT;

			/*统计当前配置表数据到统计表中end*/
			IF done=1 THEN
				LEAVE loop_iloop;
				ELSE
					SET flag=0;
			END IF;
			IF flag=0 THEN
				SET done=0;
			END IF;
			END LOOP;
	CLOSE icursor;
	COMMIT;
END;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for proc_statistics_request
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_statistics_request`()
BEGIN
	DECLARE ntime VARCHAR(40);/*当前时间*/
	DECLARE tabName VARCHAR(500);
	DECLARE description VARCHAR(500);
	DECLARE deleteSql VARCHAR(500);
	DECLARE done INT;/*游标标识*/
	DECLARE flag INT;/*循环标识*/
	DECLARE proc_log_table VARCHAR(100);/*存储过程日志表*/
	DECLARE proc_name VARCHAR(100);/*存储过程名称*/
	DECLARE icursor CURSOR FOR SELECT  tab_name FROM statistics_tables where is_valid=1;
	DECLARE CONTINUE HANDLER FOR NOT found SET done=1;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	SET done=0;
	SET proc_log_table='proc_exec_log';
	SET proc_name='proc_statistics_request';
	SET ntime=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%k:%S');
	set @deleteSql := 'delete from request_num_statistics';
	PREPARE  execs FROM @deleteSql;
	EXECUTE   execs;
	DEALLOCATE PREPARE execs;
	COMMIT;
	OPEN icursor;
		loop_iloop:LOOP
			FETCH icursor INTO tabName;
			SET description=tabName;
			set @descriptionStart=concat(description,'表request统计start');
			/*统计当前配置表数据到统计表中start*/
			set @v_log_sql1 := concat('insert into ',proc_log_table,'(proc_name,table_name,log_time,description) values(?,?,?,?)');
			PREPARE  execs FROM @v_log_sql1;
			EXECUTE   execs using proc_name,proc_log_table,ntime,@descriptionStart;
			DEALLOCATE PREPARE execs;
			COMMIT;

			set @insert_statistics_sql :=concat('insert into request_num_statistics(function_id,service_id,request_id) select function_id,service_id,request_id from ',tabName,' where request_id <> 0');
			PREPARE  execs FROM @insert_statistics_sql;
			EXECUTE   execs;
			DEALLOCATE PREPARE execs;
			COMMIT;

			set @descriptionEnd=concat(description,'表request统计end');
			set @v_log_sql2 := concat('insert into ',proc_log_table,'(proc_name,table_name,log_time,description) values(?,?,?,?)');
			PREPARE  execs FROM @v_log_sql2;
			EXECUTE   execs using proc_name,proc_log_table,ntime,@descriptionEnd;
			DEALLOCATE PREPARE execs;
			COMMIT;

			/*统计当前配置表数据到统计表中end*/
			IF done=1 THEN
				LEAVE loop_iloop;
				ELSE
					SET flag=0;
			END IF;
			IF flag=0 THEN
				SET done=0;
			END IF;
			END LOOP;
	CLOSE icursor;
	COMMIT;
END;;
DELIMITER ;


--增加事件，每分钟执行一次 exec_procs存储过程
