package com.mySearch.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Index {

	private int indexId;
	private int businessId;
	private String indexName;
	private Date indexTime;
	private String indexPath;
	private String sourcePath;
	private String type;//lucene solr
	private String action;//add update delete
	private int status;//索引状态
	private String sqlSentence;
	private User createBy;
	private Date createTime;
	private String mark;
	private Set<Column> columns = new HashSet<Column>();
	
	
	public int getBusinessId() {
		return businessId;
	}
	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}
	public int getIndexId() {
		return indexId;
	}
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public Date getIndexTime() {
		return indexTime;
	}
	public void setIndexTime(Date indexTime) {
		this.indexTime = indexTime;
	}
	public String getIndexPath() {
		return indexPath;
	}
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSqlSentence() {
		return sqlSentence;
	}
	public void setSqlSentence(String sqlSentence) {
		this.sqlSentence = sqlSentence;
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
	public Set<Column> getColumns() {
		return columns;
	}
	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}
	
	
	
}
