package com.my_test.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.retrieve.api.FullTextResult;
import com.retrieve.api.FullTextSearchParams;
import com.retrieve.api.ServerFactory;
import com.retrieve.spi.SolrService;

public class TestFrame extends TestCase{
	
	SolrService solrService = null;
	
	public void beginService(String flag){
		Map<String,String> params = new HashMap<String,String>();
		params.put("type", "solr");
		params.put("serverName","test");
		params.put("flag", flag);
		params.put("className", SolrService.class.getName());
		params.put("indexPath", "H:/jar/solr-4.6/solr_home/test/data/index");
		ServerFactory serverFactory = new ServerFactory();
		solrService = (SolrService)serverFactory.beginService(params);
		solrService.setServerName("test");
	}


	
	public void test1(){
		//启动服务
		beginService("search");

		FullTextSearchParams fullTextSearchParams = new FullTextSearchParams();

		fullTextSearchParams.setQueryWord("中国");

//		fullTextSearchParams.setReturnNums(10);

		List<String> assignmentFields = new ArrayList<String>();
		assignmentFields.add("title");
		assignmentFields.add("contentWithP");
		fullTextSearchParams.setAssignmentFields(assignmentFields);
		
		String[] viewFields = new String[]{"title","contentWithP","pubTime"};
		fullTextSearchParams.setViewFields(viewFields);
		
		Map<String,Boolean> sortField = new HashMap<String,Boolean>();
		sortField.put("pubTime", false);
		fullTextSearchParams.setSortField(sortField);

		fullTextSearchParams.setViewNums(20);

		fullTextSearchParams.setIsHighlight(false);
		

//		Map<String,String> filterField = new HashMap<String,String>();
//		filterField.put("name", "百度");
//						fullTextSearchParams.setFilterField(filterField);

//		Map<String,Float> boost = new HashMap<String,Float>();
//		boost.put("name", 1.0f);
//		boost.put("testik", 9.0f);
//		fullTextSearchParams.setBoost(boost);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		FullTextResult result = solrService.doQuery(fullTextSearchParams);
		
		System.out.println(result.getNumFound());
		List list = result.getResultList();
		SolrDocumentList b = (SolrDocumentList)list;
		System.out.println("list size:"+list.size());		
		for(int i=0;i<10;i++){	
//			System.out.println(list.get(i));
			SolrDocument doc = b.get(i);
			System.out.println(sdf.format(doc.getFieldValue("pubTime")));
			System.out.println(doc.getFieldValue("title"));
			System.out.println(doc.getFieldValue("contentWithP"));
		}
		
		
//		for(int j=0;j<finList.size();j++){
//			System.out.println(finList.get("datetime"));
//		}

	}

}
