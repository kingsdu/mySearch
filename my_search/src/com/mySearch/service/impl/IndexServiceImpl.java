package com.mySearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.domain.Index;
import com.mySearch.service.IndexService;

public class IndexServiceImpl extends BaseDaoImpl implements IndexService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	
	public List<Index> indexPagination(int pageSize, int pageNow) {
		List<Index> result = new ArrayList<Index>();
		try {
			String queryString = "from Index order by indexId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public int getRow() {
		String queryString = "select count(*) from Index";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
	
	
}
