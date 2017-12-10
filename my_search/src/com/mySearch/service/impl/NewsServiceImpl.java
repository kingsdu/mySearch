package com.mySearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.domain.News;
import com.mySearch.service.NewsService;

public class NewsServiceImpl extends BaseDaoImpl implements NewsService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public List<News> newsPagination(int pageSize, int pageNow) {
		List<News> result = new ArrayList<News>();
		try {
			String queryString = "from News order by newsId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getRow() {
		String queryString = "select count(*) from News";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
}
