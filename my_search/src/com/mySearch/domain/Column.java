package com.mySearch.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Column {

	private int columnId;
	private int parentId;
	private String columnName;
	private Date createTime;
	private User createBy;
	private String mark;
	private Set<Index> indexs = new HashSet<Index>();
	private Set<News> news = new HashSet<News>();
	private Set<WebInfo> webinfo = new HashSet<WebInfo>();
	
	
	public Set<WebInfo> getWebinfo() {
		return webinfo;
	}
	public void setWebinfo(Set<WebInfo> webinfo) {
		this.webinfo = webinfo;
	}
	public int getColumnId() {
		return columnId;
	}
	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public User getCreateBy() {
		return createBy;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Set<Index> getIndexs() {
		return indexs;
	}
	public void setIndexs(Set<Index> indexs) {
		this.indexs = indexs;
	}
	public Set<News> getNews() {
		return news;
	}
	public void setNews(Set<News> news) {
		this.news = news;
	}

}
