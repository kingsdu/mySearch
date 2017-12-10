package com.mySearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.dao.impl.BaseDaoImpl;
import com.mySearch.domain.FileDocument;
import com.mySearch.service.FileDocumentService;

public class FileDocumentServiceImpl extends BaseDaoImpl implements FileDocumentService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public List<FileDocument> fileDocumentPagination(int pageSize, int pageNow) {
		List<FileDocument> result = new ArrayList<FileDocument>();
		try {
			String queryString = "from FileDocument order by fdId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getRow() {
		String queryString = "select count(*) from FileDocument";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}

}
