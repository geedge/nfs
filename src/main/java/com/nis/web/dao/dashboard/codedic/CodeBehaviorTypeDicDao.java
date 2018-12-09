package com.nis.web.dao.dashboard.codedic;

import java.util.List;

import com.nis.domain.dashboard.codedic.CodeBehaviorTypeDic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface CodeBehaviorTypeDicDao {
    List<CodeBehaviorTypeDic> getCodeDicList();

}