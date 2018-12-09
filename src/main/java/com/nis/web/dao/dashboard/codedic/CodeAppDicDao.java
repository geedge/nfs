package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeAppDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeAppDicDao {
    List<CodeAppDic> getCodeDicList();
}