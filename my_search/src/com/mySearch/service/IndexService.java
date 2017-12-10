package com.mySearch.service;

import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.domain.Index;


public interface IndexService extends BaseDao{

	public List<Index> indexPagination(int pageSize,int pageNow);

	public int getRow();
}
