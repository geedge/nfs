package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.UserManage;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface UserManageDao extends CrudDao<UserManage>{
	void saveInfo(UserManage entity);
	void updateInfo(UserManage entity);
	UserManage getUserByLoginName(@Param("userName") String userName);
	List<UserManage> findList(UserManage entity);
	UserManage getUserById(@Param("id") String id);
	
	List<UserManage> findUsers();
}
