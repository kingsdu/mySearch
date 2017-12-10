package com.mySearch.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.mySearch.domain.FileDocument;
import com.mySearch.service.FileDocumentService;
import com.retrieve.api.FullTextIndexParams;
import com.retrieve.api.FullTextResult;
import com.retrieve.api.FullTextSearchParams;
import com.retrieve.api.FullTextService;
import com.retrieve.spi.LuceneService;
import com.retrieve.util.DateUtils;
import com.retrieve.util.StringUtils;

public class FileDocumentAction extends BaseAction{

	private static final long serialVersionUID = 7799130341904364716L;

	private FileDocumentService fileDocumentService;
	
	private File upload;
	
	private String uploadFileName;
	
	private FileDocument fileDocument;
	
	private String fdId;
	private String swfName;
	
	private String queryString;
	private List searchList = new ArrayList();
	
	private List<FileDocument> fileDocumentAllListPa = new ArrayList<FileDocument>();
	
	private int pageNow = 1;
	private int pageCount;

	
	public void setFileDocumentService(FileDocumentService fileDocumentService) {
		this.fileDocumentService = fileDocumentService;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	public FileDocument getFileDocument() {
		return fileDocument;
	}

	public void setFileDocument(FileDocument fileDocument) {
		this.fileDocument = fileDocument;
	}
	
	

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	

	public List<FileDocument> getFileDocumentAllListPa() {
		return fileDocumentAllListPa;
	}

	public void setFileDocumentAllListPa(List<FileDocument> fileDocumentAllListPa) {
		this.fileDocumentAllListPa = fileDocumentAllListPa;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List getSearchList() {
		return searchList;
	}

	public void setSearchList(List searchList) {
		this.searchList = searchList;
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	
	public String getSwfName() {
		return swfName;
	}

	public void setSwfName(String swfName) {
		this.swfName = swfName;
	}

	public String upload(){
		HttpServletRequest request = null;
		try {
			request = ServletActionContext.getRequest();
			request.setCharacterEncoding("utf-8");
			
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String path = realPath+"/upload/";
			
			InputStream is = new FileInputStream(this.getUpload());
			String fullPdfPath = path+this.getUploadFileName();
			OutputStream os = new FileOutputStream(fullPdfPath);
			byte[] b = new byte[1024];
			int size = is.read(b);
			while(size>0){
				os.write(b, 0, size);
				size = is.read(b);
			}
			is.close();
			os.close();
			
			Date date = new Date();
			String swfName = date.getTime()+".swf";
			String fullSwfName = path+swfName;
			//pdf转换成swf文件
			Runtime rt = Runtime.getRuntime();
			String command = "E:\\programFiles\\swftools\\pdf2swf "+fullPdfPath+
					" -o " + fullSwfName +" -T 9";
			rt.exec(command);
			
			this.fileDocument = new FileDocument();
			this.fileDocument.setCreateTime(DateUtils.getCurrentYMDHMS());
			this.fileDocument.setFdName(this.getUploadFileName());
			this.fileDocument.setStatus(-1);
			this.fileDocument.setSwfName(swfName);
			this.fileDocumentService.addObject(this.fileDocument);
			
			List<FileDocument> list = (List)this.fileDocumentService.getAllObject(FileDocument.class, "fdId", false);
			int fileId = 0;
			if(list != null && list.size() > 0){
				FileDocument fDocument = list.get(0);
				fileId = fDocument.getFdId();
			}
			
			//创建索引
			String indexPath = realPath+"/indexPath/";
			FullTextService searchService = this.fileDocumentService.getSearchService("lucene", 
					"fileDocument", "writer", LuceneService.class.getName(), indexPath, "");
			FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
			List<Map<String,Object>> indexData = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("docfullid", fileId+"");
			map.put("fdName", this.uploadFileName);
			map.put("swfName", this.fileDocument.getSwfName());
			indexData.add(map);
			fullTextIndexParams.setIndexData(indexData);
			
			searchService.doIndex(fullTextIndexParams);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "upload";
	}
	
	public String searchAllFileDocumentPa(){
		int pageSize = 10;
		int pageRow = this.fileDocumentService.getRow();
		
		if(pageRow%pageSize == 0){
			this.pageCount = pageRow/pageSize;
		}else{
			this.pageCount = pageRow/pageSize + 1;
		}
		
		if(this.pageNow < 1){
			this.pageNow = 1;
		}
		
		if(this.pageNow > this.pageCount){
			this.pageNow = this.pageCount;
		}
		this.fileDocumentAllListPa = this.fileDocumentService.fileDocumentPagination(pageSize, this.pageNow);
		return "searchAllFileDocumentPa";
	}
	
	public String delFileDocument(){
		if(StringUtils.isNotEmpty(this.fdId)){
			FileDocument fd = new FileDocument();
			HttpServletRequest request = null;
			try {
				request = ServletActionContext.getRequest();
				request.setCharacterEncoding("utf-8");
				
				String realPath = request.getSession().getServletContext().getRealPath("/");
				String path = realPath+"/upload/";
				
				int id = Integer.parseInt(this.fdId);
				fd = (FileDocument)this.fileDocumentService.getObjectById(FileDocument.class, id);
				
				String sourceName = fd.getFdName();
				String swfName = fd.getSwfName();
				File sourceFile = new File(path+sourceName);
				File swfFile = new File(path+swfName);
				//删除文件
				sourceFile.delete();
				swfFile.delete();
				
				this.fileDocumentService.deleteObject(fd);
				
				//删除索引
				String indexPath = realPath+"/indexPath";
				FullTextService searchService = this.fileDocumentService.getSearchService("lucene", 
						"fileDocumentDel", "writer", LuceneService.class.getName(), indexPath, "");
				FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
				fullTextIndexParams.setId(this.fdId);
				searchService.deleteIndex(fullTextIndexParams);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "delFileDocument";
	}
	
	public String doQuery(){
		HttpServletRequest request = null;
		try {
			request = ServletActionContext.getRequest();
			request.setCharacterEncoding("utf-8");
			
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String indexPath = realPath+"/indexPath";
			
			FullTextService searchService = this.fileDocumentService.getSearchService("lucene", 
					"fileDocumentSearch", "search", LuceneService.class.getName(), indexPath, "");
			
			FullTextSearchParams fullTextSearchParams = new FullTextSearchParams();
			fullTextSearchParams.setQueryWord(this.queryString);
			fullTextSearchParams.setReturnNums(1000);
			
			List<String> assignmentFields = new ArrayList<String>();
			assignmentFields.add("fdName");
			fullTextSearchParams.setAssignmentFields(assignmentFields);
			
			String[] viewFields = new String[]{"fdName","swfName","docfullid"};
			fullTextSearchParams.setViewFields(viewFields);
			
			fullTextSearchParams.setViewNums(150);
			fullTextSearchParams.setIsHighlight(true);
			fullTextSearchParams.setPreHighlight("<font color='red'>");
			fullTextSearchParams.setPostHighlight("</font>");
			
			FullTextResult result = searchService.doQuery(fullTextSearchParams);
			long numFound = result.getNumFound();
			List tempList = result.getResultList();
			
			int pageRow = tempList.size();
			int pageSize = 10;
			
			if(pageRow%pageSize == 0){
				this.pageCount = pageRow/pageSize;
			}else{
				this.pageCount = pageRow/pageSize+1;
			}
			
			if(this.pageNow < 1){
				this.pageNow = 1;
			}
			
			if(this.pageNow > this.pageCount){
				this.pageNow = this.pageCount;
			}
			
			if(tempList != null && tempList.size() > 0){
				this.searchList = this.fileDocumentService.listPagination(this.pageNow, pageSize, tempList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "doQuery";
	}
	
	public String viewSwf(){
		return "viewSwf";
	}
}
