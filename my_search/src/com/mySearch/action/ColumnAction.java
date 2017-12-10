package com.mySearch.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mySearch.domain.Column;
import com.mySearch.domain.Index;
import com.mySearch.domain.vo.ColumnVO;
import com.mySearch.service.ColumnService;
import com.mySearch.service.IndexService;
import com.retrieve.util.DateUtils;

public class ColumnAction extends BaseAction{

	private static final long serialVersionUID = -1244093642743940970L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getAllColumn(){
		this.columnList = (List)this.columnService.getAllObject("Column", "columnId", false);
		this.indexList = (List)this.indexService.getAllObject("Index", "indexId", false);
		return "getAllColumn";
	}
	
	public String addColumn(){
		int indexId = 0;
		if(this.index != null){
			indexId = this.index.getIndexId();
		}
		if(indexId > 0){
			//取出多个Index,存入column
			Set<Index> indexs = new HashSet<Index>();
			Index indexObject = (Index)this.indexService.getObjectById(Index.class, indexId);
			indexs.add(indexObject);
			this.column.setIndexs(indexs);
		}
		this.column.setCreateTime(DateUtils.getCurrentYMDHMS());
		this.column.setParentId(this.column.getColumnId());
		this.columnService.addObject(this.column);
		return "addColumn";
	}
	
	public String searchAllColumnPa(){
		int pageSize = 10;
		int pageRow = this.columnService.getRow();
		
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
		
		List<Column> tempList = this.columnService.columnPagination(pageSize, this.pageNow);
		ColumnVO columnVO = null;
		for(Column column : tempList){
			columnVO = new ColumnVO();
			columnVO.setColumnId(column.getColumnId());
			int pId = column.getParentId();
			String pcolumnName = "";
			if(pId > 0){
				Column tempColumn = (Column)this.columnService.getObjectById(Column.class, pId);
				if(tempColumn != null){
					pcolumnName = tempColumn.getColumnName();
				}
			}
			columnVO.setPcolumnName(pcolumnName);
			Set<Index> indexs = column.getIndexs();
			Iterator<Index> iter = indexs.iterator();
			String indexName = "";
			while(iter.hasNext()){
				indexName = iter.next().getIndexName();
			}
			columnVO.setIndexName(indexName);
			columnVO.setColumnName(column.getColumnName());
			this.columnAllListPa.add(columnVO);
		}
		
		return "searchAllColumnPa";
	}
	
	

	private ColumnService columnService;
	
	private IndexService indexService;
	
	private List<Column> columnList = new ArrayList<Column>();
	
	private List<Index> indexList = new ArrayList<Index>();
	
	private Column column;
	
	private Index index;
	
	private List<ColumnVO> columnAllListPa = new ArrayList<ColumnVO>();
	
	private int pageCount;
	
	private int pageNow;

	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}
	
	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public List<Index> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<Index> indexList) {
		this.indexList = indexList;
	}
	
	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}
	
	public List<ColumnVO> getColumnAllListPa() {
		return columnAllListPa;
	}

	public void setColumnAllListPa(List<ColumnVO> columnAllListPa) {
		this.columnAllListPa = columnAllListPa;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}
}
