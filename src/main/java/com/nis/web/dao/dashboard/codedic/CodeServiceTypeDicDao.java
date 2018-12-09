package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeServiceTypeDic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface CodeServiceTypeDicDao {
	List<CodeServiceTypeDic> getCodeDicList();

}