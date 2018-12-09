package com.nis.web.dao;

import java.util.List;

import com.nis.domain.FunctionRegionDict;

@MyBatisDao
public interface FunctionRegionDictDao {
	List<FunctionRegionDict> getList(FunctionRegionDict entity);
}
