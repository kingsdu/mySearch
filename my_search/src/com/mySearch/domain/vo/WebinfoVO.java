package com.mySearch.domain.vo;

import java.util.Date;

public class WebinfoVO {

	private int webId;
	private String webName;
	private String webUrl;
	private Date createTime;
	private Date preCrawlTime;
	private String resultUrl;
	private String columnName;
	
	
	public int getWebId() {
		return webId;
	}
	public void setWebId(int webId) {
		this.webId = webId;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPreCrawlTime() {
		return preCrawlTime;
	}
	public void setPreCrawlTime(Date preCrawlTime) {
		this.preCrawlTime = preCrawlTime;
	}
	public String getResultUrl() {
		return resultUrl;
	}
	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	
	
	
	
}
