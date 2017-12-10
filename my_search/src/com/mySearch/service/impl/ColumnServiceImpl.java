package com.mySearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.domain.Column;
import com.mySearch.service.ColumnService;

public class ColumnServiceImpl extends BaseDaoImpl implements ColumnService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public List<Column> columnPagination(int pageSize, int pageNow) {
		List<Column> result = new ArrayList<Column>();
		try {
			String queryString = "from Column order by columnId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getRow() {
		String queryString = "select count(*) from Column";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
	
}
