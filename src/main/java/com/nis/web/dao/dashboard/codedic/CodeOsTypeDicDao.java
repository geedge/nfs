package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeOsTypeDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeOsTypeDicDao {
    List<CodeOsTypeDic> getCodeDicList();

}