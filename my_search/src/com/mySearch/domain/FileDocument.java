package com.mySearch.domain;

import java.util.Date;

public class FileDocument {

	private int fdId;
	private String fdName;
	private String swfName;
	private int status;
	private Date createTime;
	private User createBy;
	private String mark;
	
	public int getFdId() {
		return fdId;
	}
	public void setFdId(int fdId) {
		this.fdId = fdId;
	}
	public String getFdName() {
		return fdName;
	}
	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	public String getSwfName() {
		return swfName;
	}
	public void setSwfName(String swfName) {
		this.swfName = swfName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	
}
