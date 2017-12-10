package com.mySearch.domain;

import java.util.Date;

public class WebInfo {

	private int webId;
	private String webName;
	private String webUrl;
	private Date createTime;
	private Date preCrawlTime;
	private String resultUrl;
	private User createBy;
	private Column column;
	private String mark;
	private String crawlURL;
	private String webIds;
	private int isIKDic;
//	private Set<Column> indexs = new HashSet<Column>();

	
	public int getWebId() {
		return webId;
	}
	public int getIsIKDic() {
		return isIKDic;
	}
	public void setIsIKDic(int isIKDic) {
		this.isIKDic = isIKDic;
	}
	public String getWebIds() {
		return webIds;
	}
	public void setWebIds(String webIds) {
		this.webIds = webIds;
	}
	public String getCrawlURL() {
		return crawlURL;
	}
	public void setCrawlURL(String crawlURL) {
		this.crawlURL = crawlURL;
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
	public User getCreateBy() {
		return createBy;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
}
