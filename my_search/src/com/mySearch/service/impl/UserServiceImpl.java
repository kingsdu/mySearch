package com.mySearch.service.impl;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.service.UserService;

public class UserServiceImpl extends BaseDaoImpl implements UserService {
	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
}
