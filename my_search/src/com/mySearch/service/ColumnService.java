package com.mySearch.service;

import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.domain.Column;


public interface ColumnService extends BaseDao{

	public List<Column> columnPagination(int pageSize,int pageNow);

	public int getRow();
}
