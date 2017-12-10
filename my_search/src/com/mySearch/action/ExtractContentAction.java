package com.mySearch.action;

import java.io.File;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mySearch.domain.News;
import com.mySearch.service.WebinfoService;
import com.mySearch.utils.WebExtractorUtils;
import com.retrieve.util.FileUtils;
import com.retrieve.util.StringUtils;


public class ExtractContentAction {
	
	private WebinfoService webinfoService;
	
	//web抽取,入库
	public void extractWeb(String path){
		//获取初始路径		
		String inputPath = path + File.separator + "mirror";
		StringUtils su = new StringUtils(inputPath);
		List<String> allPath = su.allPathResult;
		for(String p : allPath){
			StringBuilder html = FileUtils.readHtml(p);	
			int position = p.indexOf("mirror")+6;
			String sourceUrl = p.substring(position);
			sourceUrl = "http:/"+sourceUrl.replace("\\", "/");
			Document doc = Jsoup.parse(html.toString(), sourceUrl);
			WebExtractorUtils exct = new WebExtractorUtils(doc);
			Element contentElement = null;
			try {
				News news = new News();
				contentElement = exct.computeScoreSecond();
				news.setContent(contentElement.toString());
				news.setSourceUrl(sourceUrl);
				news.setNewsTitle(exct.getTitle(contentElement));
				news.setNewsTime(exct.getTime(contentElement));
				webinfoService.addObject(news);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	public WebinfoService getWebinfoService() {
		return webinfoService;
	}

	public void setWebinfoService(WebinfoService webinfoService) {
		this.webinfoService = webinfoService;
	}

}
