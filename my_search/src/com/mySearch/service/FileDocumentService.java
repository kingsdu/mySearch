package com.mySearch.service;

import java.util.List;

import com.mySearch.dao.BaseDao;
import com.mySearch.domain.FileDocument;

public interface FileDocumentService extends BaseDao{

	public List<FileDocument> fileDocumentPagination(int pageSize,int pageNow);
	
	public int getRow();
}
