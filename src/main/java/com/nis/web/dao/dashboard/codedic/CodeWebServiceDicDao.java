package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeWebServiceDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeWebServiceDicDao {
    List<CodeWebServiceDic> getCodeDicList();

}