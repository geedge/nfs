package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeBrowserTypeDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeBrowserTypeDicDao {
    List<CodeBrowserTypeDic> getCodeDicList();

}