package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeProtocolTypeDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeProtocolTypeDicDao {
    List<CodeProtocolTypeDic> getCodeDicList();

}