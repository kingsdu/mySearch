package org.archive.crawler.postprocessor;

import org.archive.crawler.datamodel.CandidateURI;

import com.mySearch.config.mySearch_ConstantsParams;
import com.retrieve.util.XMLHandleUtils;

public class Cnpc_FrontierScheduler extends FrontierScheduler{

	private static final long serialVersionUID = 2449342312089802082L;

	public Cnpc_FrontierScheduler(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected void schedule(CandidateURI caUri) {
		//拿到的URL的限制
//		if(caUri.toString().contains(readCrawlUrl())){
			getController().getFrontier().schedule(caUri);
//		}
	}
	
	
	/**
	 * @Description: 获取CrawlUrl
	 * @return:
	 * @date: 2017-9-22  
	 */
	protected static String readCrawlUrl(){
//		String path = Thread.currentThread().getContextClassLoader().getResource("properties.xml").getPath();
//		String sheduleTag = XMLHandleUtils.getXML(path, mySearch_ConstantsParams.SHEDULETAG);
//		return sheduleTag;	
		return "news";
	}
	
	
	public static void main(String[] args) {
		String url = "http://www.sinopecnews.com.cn";
		if(url.contains("news")){
			System.out.println("aaaa");
		}
	}

}
