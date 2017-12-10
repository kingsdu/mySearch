package com.my_test.test;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;


public class TestHeritrix_Extractor {
	
	
	public static String startCrawl(){
		//获取传入的抓取id
		//读取配置好的xml
		//启动爬取程序
		String taskpath = "E:\\test\\taskPath";
		String orderPath = taskpath + File.separator + "order.xml";
		File order = null;  
		XMLSettingsHandler orderHandler = null;//声明读取order.xml
		CrawlController controller = null; //控制
		try {
			//封装order.xml
			order = new File(orderPath);
			if(!order.exists()){
				System.out.println("配置文件找不到结束");
				System.exit(0);
			}
			//构造一个XMLSettingsHandler对象，将order.xml属性信息装入。
			orderHandler = new XMLSettingsHandler(order);		    
			orderHandler.initialize();

			//控制器加载
			controller = new CrawlController();
			controller.initialize(orderHandler);
			//调用requestCrawlStart();启动线程池和frontier
			controller.requestCrawlStart();    
			while(true){
				if(controller.isRunning()==false){
					break;
				}
				Thread.sleep(1000);
			}  
			controller.requestCrawlStop(); 
		} catch (InvalidAttributeValueException e) {
			e.printStackTrace();
			System.out.println("加载配置文件失败");
		} catch (InitializationException e) {
			e.printStackTrace();
			System.out.println("控制器加载配置失败");
		}catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("睡眠抛出异常");
		} 
		return null;
	}
	
	
	public static void main(String args[]){
		startCrawl();
	}
}
