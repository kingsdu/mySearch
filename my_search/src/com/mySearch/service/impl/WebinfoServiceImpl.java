package com.mySearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.domain.WebInfo;
import com.mySearch.service.WebinfoService;

public class WebinfoServiceImpl extends BaseDaoImpl implements WebinfoService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	
	public List<WebInfo> webInfoPagination(int pageSize, int pageNow) {
		List<WebInfo> result = new ArrayList<WebInfo>();
		try {
			String queryString = "from WebInfo order by webId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getRow() {
		String queryString = "select count(*) from WebInfo";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}


}
