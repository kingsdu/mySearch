package com.mySearch.service;

import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.domain.WebInfo;


public interface WebinfoService extends BaseDao{

	public  List<WebInfo>  webInfoPagination(int pageSize,int pageNow);

	public int getRow();

}
