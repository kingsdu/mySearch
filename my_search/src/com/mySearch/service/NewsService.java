package com.mySearch.service;

import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.domain.News;

public interface NewsService extends BaseDao{
	
	public List<News> newsPagination(int pageSize,int pageNow);
	
	public int getRow();

}
