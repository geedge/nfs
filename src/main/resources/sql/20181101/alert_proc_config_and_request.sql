-- MySQL dump 10.16  Distrib 10.2.14-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: gwall_dev
-- ------------------------------------------------------
-- Server version	10.2.14-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'gwall_dev'
--
/*!50003 DROP PROCEDURE IF EXISTS `exec_procs` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `exec_procs`()
BEGIN
	call proc_statistics_config();
	call proc_statistics_request();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_statistics_config` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_statistics_config`()
BEGIN

	DECLARE ntime VARCHAR(40);/*当前时间*/

	DECLARE otime VARCHAR(40);/*上次统计时间*/

	DECLARE nRow VARCHAR(40);/*本次统计条数*/

	DECLARE tabName VARCHAR(500);

	DECLARE description VARCHAR(500);

	DECLARE deleteSql VARCHAR(500);

	DECLARE done INT;/*游标标识*/

	DECLARE flag INT;/*循环标识*/

	DECLARE t_error INT;/*错误标识*/

	DECLARE proc_log_table VARCHAR(100);/*存储过程日志表*/

	DECLARE proc_name VARCHAR(100);/*存储过程名称*/

	DECLARE icursor CURSOR FOR SELECT  tab_name FROM statistics_tables where is_valid=1;

	DECLARE CONTINUE HANDLER FOR NOT found SET done=1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION set t_error=1;

	select max(statistic_time) into otime from cfg_num_statistics;

	SET done=0;

	SET t_error=0;

	SET proc_log_table='proc_exec_log';

	SET proc_name='proc_statistics_config';

	SET ntime=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%i:%S');

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


			/*cfg_index_info只统计asn配置*/
			IF tabName='cfg_index_info' THEN

				set @insert_statistics_sql := concat('insert into cfg_num_statistics(statistic_time,function_id,service_id,action,compile_id,cfg_state) select  ','''',ntime,'''',',','function_id,service_id,action,compile_id,if(is_audit=3,3,if(is_audit=2,2,if(is_audit=1,1,if(is_valid=0,0,if(is_valid,-1,-1))))) cfg_state from ',tabName,' where service_id in(37,149)');

			ELSE
			/*cfg_index_info只统计asn配置*/
				set @insert_statistics_sql := concat('insert into cfg_num_statistics(statistic_time,function_id,service_id,action,compile_id,cfg_state) select  ','''',ntime,'''',',','function_id,service_id,action,compile_id,if(is_audit=3,3,if(is_audit=2,2,if(is_audit=1,1,if(is_valid=0,0,if(is_valid,-1,-1))))) cfg_state from ',tabName);

			END IF;

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

			/*异常退出loop*/
			IF t_error=1 THEN
				LEAVE loop_iloop;
			END IF;

			/*循环结束退出loop*/
			IF done=1 THEN

				LEAVE loop_iloop;

			ELSE

				SET flag=0;

			END IF;

			IF flag=0 THEN

				SET done=0;

			END IF;

		END LOOP loop_iloop;

	CLOSE icursor;
	 /*取出本次统计条数*/
	SELECT count(statistic_time) INTO nRow from cfg_num_statistics where statistic_time=ntime;

	IF t_error=1 THEN    /*如果异常清楚本次数据*/
		delete from cfg_num_statistics where statistic_time=ntime;
		COMMIT;
	ELSEIF nRow > 0 THEN /*判断本次统计是否有数据录入，如果有则删除上次统计数据，如果没有则不清除上次统计数据*/

		delete from cfg_num_statistics where statistic_time=otime;
		COMMIT;
	END IF;
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_statistics_request` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_statistics_request`()
BEGIN

	DECLARE otime VARCHAR(40);/*上次统计时间*/

	DECLARE ntime VARCHAR(40);/*当前统计时间*/

	DECLARE nRow VARCHAR(40);/*本次统计条数*/

	DECLARE tabName VARCHAR(500);

	DECLARE description VARCHAR(500);

	DECLARE deleteSql VARCHAR(500);

	DECLARE done INT;/*游标标识*/

	DECLARE flag INT;/*循环标识*/

	DECLARE t_error INT;/*错误标识*/

	DECLARE proc_log_table VARCHAR(100);/*存储过程日志表*/

	DECLARE proc_name VARCHAR(100);/*存储过程名称*/

	DECLARE icursor CURSOR FOR SELECT  tab_name FROM statistics_tables where is_valid=1;

	DECLARE CONTINUE HANDLER FOR NOT found SET done=1;

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION set t_error=1;

	select max(statistic_time) into otime from request_num_statistics;

	SET done=0;

	SET t_error=0;

	SET proc_log_table='proc_exec_log';

	SET proc_name='proc_statistics_request';

	SET ntime=DATE_FORMAT(SYSDATE(),'%Y-%m-%d %H:%i:%S');

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


			/*cfg_index_info只统计asn配置*/
			IF tabName='cfg_index_info' THEN
			
				set @insert_statistics_sql :=concat('insert into request_num_statistics(statistic_time,function_id,service_id,request_id,compile_id) select  ','''',ntime,'''',',','function_id,service_id,request_id,compile_id from ',tabName,' where is_valid <> -1 and is_audit <> 3','  and service_id in(37,149)');

			ELSE

				set @insert_statistics_sql :=concat('insert into request_num_statistics(statistic_time,function_id,service_id,request_id,compile_id) select  ','''',ntime,'''',',','function_id,service_id,request_id,compile_id from ',tabName,' where is_valid <> -1 and is_audit <> 3');

			END IF;
			

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

	  /*取出本次统计条数*/
	SELECT count(statistic_time) INTO nRow from request_num_statistics where statistic_time=ntime;

	IF t_error=1 THEN    /*如果异常清楚本次数据*/
		delete from request_num_statistics where statistic_time=ntime;
		COMMIT;
	ELSEIF nRow > 0 THEN /*判断本次统计是否有数据录入，如果有则删除上次统计数据，如果没有则不清除上次统计数据*/

		delete from request_num_statistics where statistic_time=otime;
		COMMIT;
	END IF;
	COMMIT;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-01 11:18:23
