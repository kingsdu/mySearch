package com.mySearch.domain;

import java.util.Date;

/**
 * @Description: TODO
 * @author: DU 
 * @date: 2017-9-30  
 */
public class News {

	private int newsId;
	private String newsTitle;
	private String sourceUrl;
	private String filePath;
	private String newsTime;
	private String sourceNet;
	private User createBy;
	private Date createTime;
	private String mark;
	private Column column;
	private String content;
	private String module;

	
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getNewsId() {
		return newsId;
	}
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getNewsTime() {
		return newsTime;
	}
	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}
	public String getSourceNet() {
		return sourceNet;
	}
	public void setSourceNet(String sourceNet) {
		this.sourceNet = sourceNet;
	}
	public User getCreateBy() {
		return createBy;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}

	//	@Override
	//	public String toString() {
	//		return "\nTITLE:\n" + newsTitle + "\nTIME:\n" + newsTime + "\nCONTENT:\n" + getContentSrc();
	//	}


}
