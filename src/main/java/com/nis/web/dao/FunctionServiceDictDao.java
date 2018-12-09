package com.nis.web.dao;

import java.util.List;

import com.nis.domain.FunctionServiceDict;

@MyBatisDao
public interface FunctionServiceDictDao {

	List<FunctionServiceDict> getList(FunctionServiceDict entity);
}
