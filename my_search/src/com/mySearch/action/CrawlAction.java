package com.mySearch.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.frontier.RecoveryJournal;
import org.archive.crawler.settings.XMLSettingsHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mySearch.config.mySearch_ConstantsParams;
import com.mySearch.domain.Column;
import com.mySearch.domain.WebInfo;
import com.mySearch.domain.vo.WebinfoVO;
import com.mySearch.service.WebinfoService;
import com.mySearch.utils.BloomFilter_utils;
import com.mySearch.utils.WebExtractorUtils;
import com.retrieve.config.ConstantParams;
import com.retrieve.util.DataBaseUtils;
import com.retrieve.util.DateUtils;
import com.retrieve.util.FileUtils;
import com.retrieve.util.StringUtils;
import com.retrieve.util.XMLHandleUtils;

public class CrawlAction extends BaseAction{

	private static final long serialVersionUID = -317900846387678273L;

	private WebInfo webinfo;
	private Column column;
	private List<WebInfo> webinfoList = new ArrayList<WebInfo>();
	private List<Column> columnList = new ArrayList<Column>();
	private List<WebinfoVO> webinfoAllListPa = new ArrayList<WebinfoVO>();
	private WebinfoService webinfoService;
	private int pageCount;	
	private int pageNow;
	
	public boolean crawlingFlag = true;


	/**
	 * @Description: 获取采集网站
	 * @return:
	 * @date: 2017-9-17  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getAllWebinfo(){
		this.webinfoList = (List)webinfoService.getAllObject("WebInfo", "webId", false);
		this.columnList = (List)webinfoService.getAllObject("Column", "columnId", false);
		return "getAllWebinfo";
	}


	/**
	 * @Description: 
	 * 1  增加爬取的网站
	 * 2  信息写入数据库
	 * @return:
	 * @date: 2017-9-16  
	 */
	public String CreateCrawl(){
		//生成存放order.xml的文件夹
		String heritrix_path;
		String website_path = null;
		String maxTaskVersion = null;//存放最大文件夹编号
		if(this.webinfo.getResultUrl()!=null){
			heritrix_path = this.webinfo.getResultUrl();
		}else{
			heritrix_path = StringUtils.getConfigParam(ConstantParams.HERITRIX_OUTPUT, "E:/test/hetritrix/output", ConstantParams.PROPERTIES_NAME);
		}
		//heritrix_path路径下没有文件，属于第一次爬取，则创建文件夹和文件
		if(!new File(heritrix_path + File.separator + this.webinfo.getWebName()).isDirectory()){
			FileUtils.createFirstFile(heritrix_path + File.separator + this.webinfo.getWebName() + File.separator + ConstantParams.FIRST);
		}
		maxTaskVersion = FileUtils.getMaxFiles(heritrix_path + File.separator + this.webinfo.getWebName());
		if(this.webinfo.getWebName()==null || maxTaskVersion== null){
			return null;
		}
		website_path = heritrix_path + File.separator + this.webinfo.getWebName() + File.separator + maxTaskVersion; 	
		//信息写入数据库
		int columnId = 0;
		if(this.column != null){
			columnId = this.column.getColumnId();
		}
		if(columnId > 0){
			Column columnObject = (Column)this.webinfoService.getObjectById(Column.class, columnId);
			this.webinfo.setColumn(columnObject);
		}
		//数据入库
		this.webinfo.setCreateTime(DateUtils.getCurrentYMDHMS());
		this.webinfo.setResultUrl(website_path);
		this.webinfoService.addObject(this.webinfo);		
		return "addWebInfo";
	}


	/**
	 * @Description: 获取爬取新闻列表
	 * @return:
	 * @date: 2017-9-17  
	 */
	public String searchAllWebinfo(){
		int pageSize = 10;
		int pageRow = this.webinfoService.getRow();
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
		//获取webinfo的信息
		List<WebInfo> tempList = this.webinfoService.webInfoPagination(pageSize, this.pageNow);
		WebinfoVO webinfovo = null;
		for(WebInfo webinfo : tempList){
			webinfovo = new WebinfoVO();
			webinfovo.setWebId(webinfo.getWebId());
			webinfovo.setWebName(webinfo.getWebName());
			webinfovo.setCreateTime(webinfo.getCreateTime());
			webinfovo.setPreCrawlTime(webinfo.getPreCrawlTime());
			webinfovo.setWebUrl(webinfo.getWebUrl());
			webinfovo.setResultUrl(webinfo.getResultUrl());
			//获取column类别信息
			Column column = (Column) this.webinfoService.getObjectById(Column.class, webinfo.getColumn().getColumnId());
			webinfovo.setColumnName(column.getColumnName());
			this.webinfoAllListPa.add(webinfovo);
		}
		return "searchAllwebinfoPa";
	}

	

	/**
	 * @Description: 启动爬虫
	 * @return:
	 * @date: 2017-9-17  
	 */
	public String startCrawl(){
		//启动爬虫的初始化工作
		initCrawl();
		//获取传入的抓取id
		String wedIds = this.webinfo.getWebIds();
		String[] strsWebId = wedIds.split(",");
		for(String webid:strsWebId){
			if(beforeCrawl(webid)){
				//获取webinfo信息
				WebInfo webinfo = (WebInfo)webinfoService.getObjectById(WebInfo.class, Integer.valueOf(webid));	
				String resultUrl = webinfo.getResultUrl();
				//启动爬取程序
				String orderPath = resultUrl + File.separator + "order.xml";
				File order = null;  
				XMLSettingsHandler orderHandler=null;//声明读取order.xml
				CrawlController controller=null; //控制
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
					runnableMethod();
					//调用requestCrawlStart();启动线程池和frontier
					controller.requestCrawlStart();   
					while(crawlingFlag){
						if(controller.isRunning()==false){
							break;
						}
						Thread.sleep(1000);
					} 					
					controller.requestCrawlStop();
					endCrawl(webid);
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
			}
		}
		return "crawlOver";
	}
	
	
	public Boolean stopCrawl(){		 
		return crawlingFlag = false;  
	}
	
	
	public void runnableMethod(){
		TimerTask task = new TimerTask() {  
            @Override  
            public void run() {  
            	stopCrawl();
            }  
        };  
        Timer timer = new Timer();  
        long intevalPeriod = 1000 * 6300;//1h(1*1000 = 1s)
        timer.schedule(task, intevalPeriod);  
	}



	/**
	 * @Description:将所有的抓取网站的标识初始化为0，在endCrawl后再将抓取过的置标识为1
	 * @param:
	 * @return:
	 * @date: 2017-11-14  
	 */
	private void initCrawl() {
		crawlingFlag = true;
		Connection conn = null;
		PreparedStatement pstmt = null;		
		Statement stmt = null;
		ResultSet rs = null;
		String sqlString = "UPDATE s_webinfo SET isIKDic = '0' WHERE webId IS NOT NULL";
		try {
			conn = DataBaseUtils.getConnection();
			pstmt = conn.prepareStatement(sqlString);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}


	/**
	 * @Description: 爬取之前的工作
	 * 1 将需要的xml信息写入到properitirs.xml
	 * 2 设置order.xml
	 * 3 若当前最新文件夹已经被抓取，则更新文件夹和数据库信息 
	 * @return:
	 * @date: 2017-9-22  
	 */
	private boolean beforeCrawl(String webid){
		WebInfo webinfo = (WebInfo)webinfoService.getObjectById(WebInfo.class, Integer.valueOf(webid));
		String webUrl = webinfo.getWebUrl();
		String resultUrl = webinfo.getResultUrl();
		//判断文件是否符合要求
		if(!FileUtils.judgeOrderOrSeeds(resultUrl)){
			System.out.println("resultUrl is error");
			return false;
		}
		//判断url是否符合规范
		if(!StringUtils.judgeUrls(webUrl)){
			System.out.println("webUrl is error");
			return false;
		}
		//将crawlUrl写入到xml中
		String servletPath = mySearch_ConstantsParams.SERVLETPATH + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "properties.xml";
		String orderPath = resultUrl + File.separator + "order.xml";
		if(!XMLHandleUtils.updateXML(servletPath,mySearch_ConstantsParams.URLNAMEID,webinfo.getCrawlURL())){
			System.out.println("CrawlURL写入xml失败");
		}
		//将tomcatPath写入到xml中
		if(!XMLHandleUtils.updateXML(servletPath,mySearch_ConstantsParams.TOMCATID,mySearch_ConstantsParams.SERVLETPATH)){
			System.out.println("TOMCATID写入xml失败");
		}
		//将sheduleTag写入到xml中
		//		if(!XMLHandleUtils.updateXML(servletPath,mySearch_ConstantsParams.SHEDULETAG,My_UrlUtils.getSheduleTag(webinfo.getCrawlURL()))){
		//			System.out.println("SHEDULETAG写入xml失败");
		//		}
		//将recoverAll.txt中的地址,写入到xml中。
		if(!XMLHandleUtils.updateXML(servletPath,mySearch_ConstantsParams.RECOVERGZ_All,resultUrl + File.separator + mySearch_ConstantsParams.RECOVERFILENAME)){
			System.out.println("RECOVERFILENAME写入xml失败");
		}
		//将recover2写入到xml中，存放该次爬取的url
		if(!XMLHandleUtils.updateXML(servletPath,mySearch_ConstantsParams.RECOVERGZ_A,resultUrl + File.separator +mySearch_ConstantsParams.LOGS + File.separator + mySearch_ConstantsParams.A_RECOVERFILENAME)){
			System.out.println("A_RECOVERFILENAME写入xml失败");
		}
		//修改recover-path
		if(!XMLHandleUtils.updataXMLByName(orderPath,mySearch_ConstantsParams.ORDERNAME,resultUrl + File.separator + mySearch_ConstantsParams.RECOVERNAME)){
			System.out.println("读取增量文件失败");
		}
		//写入种子文件
		FileUtils.writeStr(resultUrl + File.separator + "seeds.txt",webUrl);		

		String heritrix_path = null;//用户输入的输出路径
		if(resultUrl==null){
			return false;
		}
		heritrix_path = resultUrl;
		heritrix_path = heritrix_path.substring(0, heritrix_path.lastIndexOf("\\"));
		String maxTaskVersion = FileUtils.getMaxFiles(heritrix_path);//得到输入路径+webName下面的文件夹中最大的文件夹标号（最新）
		if(webinfo.getWebName()==null || maxTaskVersion== null){
			return false;
		}
		String website_path = heritrix_path + File.separator + maxTaskVersion; 
		File[] file = (new File(website_path)).listFiles();
		//判断是否含有4个要求文件
		if (file.length!=4 && FileUtils.judgeOrderOrSeeds(website_path)) {
			String newTaskVersion = String.valueOf(Integer
					.valueOf(maxTaskVersion) + 1);
			String strTargetFilepath = heritrix_path + File.separator + newTaskVersion;
			File fileOut = new File(strTargetFilepath);
			if(!fileOut.isDirectory()){
				(new File(strTargetFilepath)).mkdirs();
			}
			FileUtils.copySameFiles(website_path,strTargetFilepath);
			website_path = strTargetFilepath;
			maxTaskVersion = newTaskVersion;
			//更新数据库信息
			int columnId = 0;
			if(webinfo.getColumn() != null){
				columnId = webinfo.getColumn().getColumnId();
			}
			if(columnId > 0){
				Column columnObject = (Column)this.webinfoService.getObjectById(Column.class, columnId);
				webinfo.setColumn(columnObject);
			}
			//数据入库
			webinfo.setCreateTime(DateUtils.getCurrentYMDHMS());
			webinfo.setResultUrl(website_path);
			webinfoService.updateObject(webinfo);
		}
		return true;
	}


	/**
	 * @Description: 爬取之后的工作
	 * 1 重新生成recover.gz文件 
	 * 2 将更新之后的信息重新写入到下一次爬取的文件夹中
	 * 3 更新数据库信息
	 * @return:
	 * @date: 2017-9-22  
	 */
	private boolean endCrawl(String webid){
		//获取webinfo信息
		WebInfo webinfo = (WebInfo)webinfoService.getObjectById(WebInfo.class, Integer.valueOf(webid));
		//复制最新的文件夹，准备进行下次爬取
		String resultUrl = webinfo.getResultUrl();
		String crawlURL = webinfo.getCrawlURL();
		String recoverGzDir = resultUrl;
		//重新生成recover.gz文件
		genRecoverGZ(recoverGzDir);
		String heritrix_path = null;//用户输入的输出路径
		if(resultUrl==null){
			return false;
		}
		heritrix_path = resultUrl;
		//截取需要的部分
		heritrix_path = heritrix_path.substring(0, heritrix_path.lastIndexOf("\\"));
		String maxTaskVersion = FileUtils.getMaxFiles(heritrix_path);//得到输入路径+webName下面的文件夹中最大的文件夹标号（最新）
		if(webinfo.getWebName()==null || maxTaskVersion== null){
			return false;
		}
		String website_path = heritrix_path + File.separator + maxTaskVersion; 	
		//判断最新的文件夹是否被爬取过，爬取过，则复制order和seeds文件夹准备下次爬取
		File[] file = (new File(website_path)).listFiles();
		if (file.length!=4 && FileUtils.judgeOrderOrSeeds(website_path)) {
			String newTaskVersion = String.valueOf(Integer
					.valueOf(maxTaskVersion) + 1);
			String strTargetFilepath = heritrix_path + File.separator + newTaskVersion;
			File fileOut = new File(strTargetFilepath);
			if(!fileOut.isDirectory()){
				(new File(strTargetFilepath)).mkdirs();
			}
			FileUtils.copySameFiles(website_path,strTargetFilepath);
			website_path = strTargetFilepath;
			maxTaskVersion = newTaskVersion;
			int columnId = 0;
			//更新数据库信息
			if(webinfo.getColumn() != null){
				columnId = webinfo.getColumn().getColumnId();
			}
			if(columnId > 0){
				Column columnObject = (Column)this.webinfoService.getObjectById(Column.class, columnId);
				webinfo.setColumn(columnObject);
			}
			//数据入库
			webinfo.setCreateTime(DateUtils.getCurrentYMDHMS());
			webinfo.setResultUrl(website_path);
			webinfo.setIsIKDic(1);//本次抓取的网站
			webinfoService.updateObject(webinfo);
		}
		//清洗，入库
		extractWeb(crawlURL,resultUrl,webid);
		return true;
	}



	/**
	 * @Description: 生成recover.gz文件
	 * @return:
	 * @date: 2017-9-27  
	 */
	private Boolean genRecoverGZ(String recoverGzDir){
		String recover1 = beforeGenRecoverGZ();//做准备
		if(recover1==null){
			System.out.println("recover1 is error");
			return false;
		}

		RecoveryJournal recover = null; 

		String line=null; 
		InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null; 
		String sourceFileEncoding = "utf-8"; 

		try {
			sourceFile = new File(recover1); 
			recover = new RecoveryJournal(recoverGzDir,mySearch_ConstantsParams.RECOVERNAME);
			//读取文件内容  
			is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is,sourceFileEncoding);  
			br=new BufferedReader(isr);  

			//一行一行写入recover.gz文件  
			while((line=br.readLine())!=null){  
				if(!line.equals("")){  
					recover.writeLine(line);  
				}  
			}  	 
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{  
			try {  
				if(recover!=null){  
					recover.close();  
				}    
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}
		return false;  
	}




	/**
	 * @Description: 记录Heritrix生成的recover.gz文件
	 * @param:
	 * @return: recoverALL.txt文件
	 * @date: 2017-12-8  
	 */
	private static String beforeGenRecoverGZ_t(){
		BloomFilter_utils boom = new BloomFilter_utils(2000,24);
		String RECOVERGZ1 = "C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\3\\logs\\recover.gz";
		String RECOVERGZ2 = "C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\3\\recoverALL.txt";
		InputStream in = null;
		BufferedWriter bw = null;
		RandomAccessFile ra = null;
		String url = null;	
		try {
			//先读取RECOVERGZ_All中所有的url，建立BloomFliter（之前历史url）
			ra = new RandomAccessFile(new File(RECOVERGZ2),"r");
			while((url = ra.readLine())!=null){
				//建立BloomFliter
				if(!boom.contains(url)){
					boom.add(url);	
				}
			}
			url = null;
			in = new GZIPInputStream(new FileInputStream(RECOVERGZ1));  
			bw = new BufferedWriter(new FileWriter(RECOVERGZ2,true));
			Scanner sc = new Scanner(in);  
			while(sc.hasNextLine()){ 
				String url_temp = sc.nextLine();
				if(!boom.contains(url_temp)){
					bw.append(url_temp);
					bw.newLine();
					bw.flush();
				}
			}
			return RECOVERGZ2;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(bw!=null){
					bw.close();
				}
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return RECOVERGZ2;
	}

	/**
	 * @Description: 生成recover.gz文件之前，生成txt以生成recover.gz
	 * @return:
	 * @date: 2017-9-27  
	 */
	private String beforeGenRecoverGZ(){
		BloomFilter_utils boom = new BloomFilter_utils(2000,24);
		String path = Thread.currentThread().getContextClassLoader().getResource("properties.xml").getPath();
		String RECOVERGZ1 = XMLHandleUtils.getXML(path, mySearch_ConstantsParams.RECOVERGZ_All);//recoverAll.txt
		String RECOVERGZ2 = XMLHandleUtils.getXML(path, mySearch_ConstantsParams.RECOVERGZ_A);//log/recover.gz
		InputStream in = null;
		BufferedWriter bw = null;
		RandomAccessFile ra = null;
		String url = null;	
		try {
			//先读取RECOVERGZ_All中所有的url，建立BloomFliter（之前历史url）
			ra = new RandomAccessFile(new File(RECOVERGZ1),"r");
			while((url = ra.readLine())!=null){
				//建立BloomFliter
				if(!boom.contains(url)){
					boom.add(url);	
				}
			}
			url = null;
			//读取recoverGZ2（本次爬取的新闻）
			if(RECOVERGZ2 == null){
				return null;
			}
			in = new GZIPInputStream(new FileInputStream(RECOVERGZ2));  
			bw = new BufferedWriter(new FileWriter(RECOVERGZ1,true));
			Scanner sc = new Scanner(in);  
			while(sc.hasNextLine()){ 
				String temp_url = sc.nextLine();
				if(!boom.contains(temp_url)){
					bw.append(temp_url);
					bw.newLine();
					bw.flush();
				}
			}
			return RECOVERGZ1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(bw!=null){
					bw.close();
				}
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * @Description: 计算文件大小
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public double calFileSize(File file){
		if (file.exists()) {  
			if (!file.isDirectory()) {     
				return (double) file.length();
			} 
		}
		return 0;		
	}

	/**
	 * @Description: 删除指定后缀名称文件
	 * 遍历文件夹中所有文件，获取名称，删除指定名称或者指定后缀文件
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public void filterFile(File file){
		//判断文件是否存在     
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					filterFile(f);
				}
			}else {//如果是文件则直接返回其大小,以“兆”为单位   
				if(suffixDelFile(file)||calFileSize(file)<3000){
					file.delete();
				}    
			}      
		} else {     
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");       
		}  
	}


	/**
	 * @Description: 删除0kB的文件夹
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public void deleteZeroSize(File file){
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					deleteZeroSize(f);
				}			
			}     
		}
		if (file.exists()&&file.length()==0) {
			file.delete();//删除文件夹 
		} 
	}

	/**
	 * @Description: 判断需要删除的文件类型
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public boolean suffixDelFile(File file){
		if(file.getName().contains("index")||file.getName().contains("default")
				||file.getName().contains("gif") || file.getName().contains(".php") 
				||file.getName().endsWith(".jpeg") || file.getName().contains(".ico")
				|| file.getName().endsWith(".jpg") || file.getName().endsWith(".JPEG")
				|| file.getName().endsWith(".JPG") || file.getName().endsWith(".JSP")
				|| file.getName().endsWith(".jsp") || file.getName().endsWith(".ppt")
				|| file.getName().endsWith(".pptx") || file.getName().endsWith(".gif")
				|| file.getName().endsWith(".css") || file.getName().endsWith(".doc")
				|| file.getName().endsWith(".docx") || file.getName().endsWith(".zip")
				|| file.getName().endsWith(".png") || file.getName().endsWith(".js")
				|| file.getName().endsWith(".swf") || file.getName().endsWith(".xml")
				|| file.getName().endsWith(".xlsx") || file.getName().endsWith(".pdf")
				|| file.getName().endsWith(".xls") || file.getName().endsWith(".rar")
				|| file.getName().endsWith(".exe") || file.getName().endsWith(".txt")
				|| file.getName().endsWith(".mp4") || file.getName().endsWith(".wmv")
				|| file.getName().endsWith(".mp3") || file.getName().endsWith(".flv")
				|| file.getName().endsWith(".mpg") || file.getName().endsWith(".action")
				|| file.getName().endsWith(".aspx") || file.getName().endsWith(".bmp")
				|| file.getName().endsWith("4") || file.getName().endsWith("5")
				|| file.getName().endsWith("6") || file.getName().endsWith("7")
				|| file.getName().endsWith("8") || file.getName().endsWith("9")
				|| file.getName().endsWith("0") || file.getName().contains("mailto") 
				||file.getName().contains("b2b")||file.getName().contains("info")){
			return true;
		} 
		return false;		
	}


	/**
	 * @Description:爬取文件夹的处理
	 * 1 过滤文件夹中的文件，过滤规则  ：a、删除指定后缀或者名称文件（.jpg、.docx、.flv、index.html）;
	 *                       b、删除小于3kb的文件(无效文件);
	 *                       c、删除0kB的文件夹(加快抽取的速度);
	 * 2 采用文本标签路径比进行提取（无法提取到指定的全部信息，则跳过该条信息）。
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public void fileHandle(String path){
		filterFile(new File(path));		
		deleteZeroSize(new File(path));
	}





	/**
	 * @Description: web抽取,存入文本文件,入库
	 * @param:crawlURL:需要抓取的url、resultURL：结果存放地址、column：新闻所属栏目（区分不同的新闻）
	 * @return:
	 * @date: 2017-11-16  
	 */
	public void extractWeb(String crawlURL,String resultURL,String columnId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		//获取初始路径		
		boolean flag = false;
		String initPath = resultURL + File.separator + mySearch_ConstantsParams.MIRROR;
		//爬取文件过滤
		fileHandle(initPath);
		String inputPath = getExtractPath(crawlURL,resultURL);
		//文本抽取
		StringUtils su = new StringUtils(inputPath);
		List<String> allPath = su.allPathResult;
		for(String p : allPath){
			//判断是否为Index.html
			if(!flag){
				String index = p.substring(p.lastIndexOf("\\")+1,p.lastIndexOf("."));
				if(index.equals("index")){
					flag = true;
					continue;
				}
			}
			StringBuilder html = FileUtils.readHtml(p);	
			int position = p.indexOf("mirror")+6;
			String sourceUrl = p.substring(position);
			String rawTxtPath = p.substring(0,position);
			File f = new File(rawTxtPath + File.separator + "rawTxt");
			if(!f.isDirectory()){
				f.mkdirs();
			}
			String fileName = StringUtils.getFileNameFromPath(p)+".txt";
			//生成rawTxt文件夹
			String rawTxt = rawTxtPath + File.separator + "rawTxt" + File.separator + fileName;
			rawTxt = rawTxt.replace("\\", "/");//转换，否则无法存入数据"/"
			//生成sourceUrl	
			sourceUrl = "http:/"+sourceUrl.replace("\\", "/");
			Document doc = Jsoup.parse(html.toString(), sourceUrl);
			WebExtractorUtils exct = new WebExtractorUtils(doc);
			Element contentElement = null;
			String sqlString = "INSERT INTO s_news (newsTitle,sourceUrl,filePath,newsTime,content,module) VALUES ";
			StringBuilder stringBuilder = null;
			try {
				conn = DataBaseUtils.getConnection();
				contentElement = exct.getContentElement();			
				String title = exct.getTitle(contentElement);
				//标题不重复
				if(!isRepeat(title)){
					String content = contentElement.toString();
					content = content.replaceAll("\\\\", "\\\\\\\\");
					content = content.replaceAll("'", "\\\\\'");  //作用等于在单引号前面加上转义符号\
					String time = exct.getTime(contentElement);			
					if(StringUtils.isEmpty(content) || StringUtils.isEmpty(time) ||StringUtils.isEmpty(title)){
						continue;
					}
					int WebId = getModule(columnId);
					stringBuilder = new StringBuilder();
					stringBuilder.append("(");
					stringBuilder.append("'"+title+"'");
					stringBuilder.append(",");
					stringBuilder.append("'"+sourceUrl+"'");
					stringBuilder.append(",");
					stringBuilder.append("'"+rawTxt+"'");
					stringBuilder.append(",");
					stringBuilder.append("'"+time+"'");
					stringBuilder.append(",");
					stringBuilder.append("'"+content+"'");
					stringBuilder.append(",");
					stringBuilder.append("'"+WebId+"'");
					stringBuilder.append(")");

					pstmt = conn.prepareStatement(sqlString+stringBuilder.toString());
					//					System.out.println(stringBuilder);
					pstmt.execute();
					//维护索引表
					String sql = "SELECT LAST_INSERT_ID()";//获取最后插入的id,可以获取当前插入新闻的id
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					int id = 0;
					if(rs.next()){
						id = rs.getInt(1);
					}
					String insertSql = "insert into t_index (businessId,TYPE,action,INDEXEDDATE) values (?,?,?,?)";
					pstmt = conn.prepareStatement(insertSql);
					pstmt.setInt(1, id);//将获取的id插入索引表
					pstmt.setString(2, "news");
					pstmt.setString(3, "add");
					Date date = new Date();				
					pstmt.setObject(4, date);
					pstmt.execute();
					//文本文件(无格式content)
					StringUtils.string2File(title +"\n" +contentElement.text(), rawTxt);
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
	}


	/**
	 * @Description: 获取抓取的本地文件夹路径
	 * @param:crawlURL 爬取地址 ；resultURL 存储文件地址
	 * @return:
	 * 没做容错：1 属于文件夹部分的URL只有3位 或者 不属于文件夹的URL有4位 
	 * 
	 * @date: 2017-11-13  
	 */
	public static String getExtractPath(String crawlURL,String resultURL){
		resultURL = resultURL + mySearch_ConstantsParams.FILESEPARATOR + mySearch_ConstantsParams.MIRROR+mySearch_ConstantsParams.FILESEPARATOR;
		String finalPath = "";
		String regexHead = "http://|https://";
		String regexFile = "/";
		String regexNumber = "(.*?)\\d{4}(.*?)";
		String[] level_head = crawlURL.split(regexHead);
		String[] level_files = level_head[1].split(regexFile);
		if(level_files.length>2){
			for (int i = 0; i < level_files.length; i++) {		
				if(level_files[i].matches(regexNumber)){
					break;
				}else{
					finalPath+=level_files[i] + mySearch_ConstantsParams.FILESEPARATOR;
				}
			}
		}
		if(finalPath.length()>1){
			finalPath = finalPath.substring(0,finalPath.length()-1);
		}else{
			return null;
		}
		return resultURL+finalPath;
	}



	/**
	 * @Description: 删除url
	 * @return:
	 * @date: 2017-9-21  
	 */
	public String deleteCrawl(){
		int wedid = this.webinfo.getWebId();
		webinfoService.deleteObject(webinfoService.getObjectById(WebInfo.class, wedid));
		return "deleteInfo";
	}



	/**
	 * @Description: 获取crawlUrl,并写入到xml中
	 * @return:
	 * @date: 2017-9-22  
	 */
	public String getCrawlUrlToXML(){
		return null;
	}



	/*************************GET AND SET****************************************/

	public List<WebInfo> getWebinfoList() {
		return webinfoList;
	}
	public void setWebinfoList(List<WebInfo> webinfoList) {
		this.webinfoList = webinfoList;
	}
	public WebInfo getWebinfo() {
		return webinfo;
	}

	public void setWebinfo(WebInfo webinfo) {
		this.webinfo = webinfo;
	}

	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public List<Column> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public WebinfoService getWebinfoService() {
		return webinfoService;
	}
	public void setWebinfoService(WebinfoService webinfoService) {
		this.webinfoService = webinfoService;
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
	public List<WebinfoVO> getWebinfoAllListPa() {
		return webinfoAllListPa;
	}
	public void setWebinfoAllListPa(List<WebinfoVO> webinfoAllListPa) {
		this.webinfoAllListPa = webinfoAllListPa;
	}
	

	/***************************TEST*********************************/

	/**
	 * @Description: 删除指定后缀名称文件
	 * 遍历文件夹中所有文件，获取名称，删除指定名称或者指定后缀文件
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static void filterFile_test(File file){
		//判断文件是否存在     
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					filterFile_test(f);
				}
			}else {//如果是文件则直接返回其大小,以“兆”为单位   
				if(suffixDelFile_test(file)||calFileSize_test(file)<3000){
					file.delete();
				}    
			}      
		} else {     
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");       
		}  
	}


	public static void fileHandle_test(String path){
		filterFile_test(new File(path));		
		deleteZeroSize_test(new File(path));
	}


	/**
	 * @Description: 删除0kB的文件夹
	 * @param:
	 * @return:
	 * @date: 2017-11-13  
	 */
	public static void deleteZeroSize_test(File file){
		if (file.exists()) {     
			//如果是目录则递归计算其内容的总大小    
			if (file.isDirectory()) {     
				File[] children = file.listFiles();        
				for (File f : children){
					deleteZeroSize_test(f);
				}			
			}     
		}
		if (file.exists()&&file.length()==0) {
			file.delete();//删除文件夹 
		} 
	}

	/**
	 * @Description: 判断需要删除的文件类型
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static boolean suffixDelFile_test(File file){
		if(file.getName().contains("index")||file.getName().contains("default")
				||file.getName().contains("gif") || file.getName().contains(".php") 
				||file.getName().endsWith(".jpeg") || file.getName().contains(".ico")
				|| file.getName().endsWith(".jpg") || file.getName().endsWith(".JPEG")
				|| file.getName().endsWith(".JPG") || file.getName().endsWith(".JSP")
				|| file.getName().endsWith(".jsp") || file.getName().endsWith(".ppt")
				|| file.getName().endsWith(".pptx") || file.getName().endsWith(".gif")
				|| file.getName().endsWith(".css") || file.getName().endsWith(".doc")
				|| file.getName().endsWith(".docx") || file.getName().endsWith(".zip")
				|| file.getName().endsWith(".png") || file.getName().endsWith(".js")
				|| file.getName().endsWith(".swf") || file.getName().endsWith(".xml")
				|| file.getName().endsWith(".xlsx") || file.getName().endsWith(".pdf")
				|| file.getName().endsWith(".xls") || file.getName().endsWith(".rar")
				|| file.getName().endsWith(".exe") || file.getName().endsWith(".txt")
				|| file.getName().endsWith(".mp4") || file.getName().endsWith(".wmv")
				|| file.getName().endsWith(".mp3") || file.getName().endsWith(".flv")
				|| file.getName().endsWith(".mpg") || file.getName().endsWith(".action")
				|| file.getName().endsWith(".aspx") || file.getName().endsWith(".bmp")
				|| file.getName().endsWith("4") || file.getName().endsWith("5")
				|| file.getName().endsWith("6") || file.getName().endsWith("7")
				|| file.getName().endsWith("8") || file.getName().endsWith("9")
				|| file.getName().endsWith("0") || file.getName().contains("mailto") 
				||file.getName().contains("b2b")||file.getName().contains("info")){
			return true;
		} 
		return false;		
	}



	/**
	 * @Description: 计算文件大小
	 * @param:
	 * @return:
	 * @date: 2017-11-12  
	 */
	public static double calFileSize_test(File file){
		if (file.exists()) {  
			if (!file.isDirectory()) {     
				return (double) file.length();
			} 
		}
		return 0;		
	}


	//web抽取,存入文本文件,入库
	public static void testExtractWeb(String crawlURL,String resultURL,String webid){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		//获取初始路径		
		boolean flag = false;
		String initPath = resultURL + File.separator + mySearch_ConstantsParams.MIRROR;
		//爬取文件过滤
		fileHandle_test(initPath);		
		//获取抽取文件夹
		String inputPath = getExtractPath(crawlURL,resultURL);
		if(inputPath == null){
			return;
		}
		StringUtils su = new StringUtils(inputPath);
		List<String> allPath = su.allPathResult;
		for(String p : allPath){
			//判断是否为Index.html
			if(!flag){
				String index = p.substring(p.lastIndexOf("\\")+1,p.lastIndexOf("."));
				if(index.equals("index")){
					flag = true;
					continue;
				}
			}
			StringBuilder html = FileUtils.readHtml(p);	
			int position = p.indexOf("mirror")+6;
			String sourceUrl = p.substring(position);
			String rawTxtPath = p.substring(0,position);
			File f = new File(rawTxtPath + File.separator + "rawTxt");
			if(!f.isDirectory()){
				f.mkdirs();
			}
			String fileName = StringUtils.getFileNameFromPath(p)+".txt";
			//生成rawTxt文件夹
			String rawTxt = rawTxtPath + File.separator + "rawTxt" + File.separator + fileName;
			rawTxt = rawTxt.replace("\\", "/");//转换，否则无法存入数据"/"
			//生成sourceUrl	
			sourceUrl = "http:/"+sourceUrl.replace("\\", "/");
			Document doc = Jsoup.parse(html.toString(), sourceUrl);
			WebExtractorUtils exct = new WebExtractorUtils(doc);
			Element contentElement = null;
			//			Column column = getColumn_test();
			String sqlString = "INSERT INTO test_news (newsTitle,sourceUrl,filePath,newsTime,content,module) VALUES ";
			StringBuilder stringBuilder = null;
			try {
				conn = DataBaseUtils.getConnection();
				contentElement = exct.getContentElement();	
				String title = exct.getTitle(contentElement);
				//标题不重复
				//				if(!isRepeat(title)){
				String content = contentElement.toString();
				content = content.replaceAll("\\\\", "\\\\\\\\");
				content = content.replaceAll("'", "\\\\\'");  //作用等于在单引号前面加上转义符号\
				String time = exct.getTime(contentElement);			
				if(StringUtils.isEmpty(content) || StringUtils.isEmpty(time) ||StringUtils.isEmpty(title)){
					continue;
				}
				int WebId = getModule(webid);
				stringBuilder = new StringBuilder();
				stringBuilder.append("(");
				stringBuilder.append("'"+title+"'");
				stringBuilder.append(",");
				stringBuilder.append("'"+sourceUrl+"'");
				stringBuilder.append(",");
				stringBuilder.append("'"+rawTxt+"'");
				stringBuilder.append(",");
				stringBuilder.append("'"+time+"'");
				stringBuilder.append(",");
				stringBuilder.append("'"+content+"'");
				stringBuilder.append(",");
				stringBuilder.append("'"+WebId+"'");
				stringBuilder.append(")");

				pstmt = conn.prepareStatement(sqlString+stringBuilder.toString());
				//					System.out.println(stringBuilder);
				pstmt.execute();
				//维护索引表
				//					String sql = "SELECT LAST_INSERT_ID()";//获取最后插入的id,可以获取当前插入新闻的id
				//					stmt = conn.createStatement();
				//					rs = stmt.executeQuery(sql);
				//					int id = 0;
				//					if(rs.next()){
				//						id = rs.getInt(1);
				//					}
				//					String insertSql = "insert into t_index (businessId,TYPE,action,INDEXEDDATE) values (?,?,?,?)";
				//					pstmt = conn.prepareStatement(insertSql);
				//					pstmt.setInt(1, id);//将获取的id插入索引表
				//					pstmt.setString(2, "news");
				//					pstmt.setString(3, "add");
				//					Date date = new Date();				
				//					pstmt.setObject(4, date);
				//					pstmt.execute();
				//文本文件(无格式content)
				//					StringUtils.string2File(title +"\n" +contentElement.text(), rawTxt);
				//				}			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
		//		long endTime = System.currentTimeMillis();    //获取结束时间	
		//		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
	}


	public static int getModule(String webid){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int module = 0;

		String sql = "SELECT columnId FROM s_webInfo WHERE webId = "+webid;
		try {
			conn = DataBaseUtils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				module = (int)rs.getInt(1);
			}
			return module;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return 0;	
	}


	/**
	 * @Description: 检查列是否重复
	 * @param:
	 * @return:
	 * @date: 2017-11-30  
	 */
	public static Boolean isRepeat(String title){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(*) FROM s_news WHERE newsTitle = '"+title+"'";
		try {
			conn = DataBaseUtils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int id = 0;
			if(rs.next()){
				id = rs.getInt(1);
			}
			if(id!=0){
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return false;		
	}

	public static Column getColumn_test(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		Column column = null;
		String sql = "select * from s_column where columnId = '3'";

		try {
			column = new Column();
			conn = DataBaseUtils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				column.setColumnId(rs.getInt("columnId"));
				column.setColumnName(rs.getString("columnName"));
			}
			return column;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return column;
	}


	public static void getHead(){
		//http://news.cnpc.com.cn/
		String crawlUrl = "http://www.ceec.net.cn/art/2017/11/12/art_11016_1512669.html";//获取域名
		String regex = "/";
		if(crawlUrl.contains(regex)){
			crawlUrl = crawlUrl.substring(0, crawlUrl.indexOf(regex)-1);
		}
		System.out.println(crawlUrl);
	}


	public static void readRecover(){
		try {
			InputStream in = new GZIPInputStream(new FileInputStream(new File("G:\\data\\e430\\hetritrix\\output\\cnooc\\1\\logs\\recover.gz")));  
			Scanner sc = new Scanner(in);  
			while(sc.hasNextLine()){  		 
				System.out.println(sc.nextLine().toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}




	private static void GenRecoverGZ_t(){
		//		String path = Thread.currentThread().getContextClassLoader().getResource("properties.xml").getPath();
		//		String RECOVERGZ1 = "C:\\resource\\行业网站\\crawlData\\oilGas\\中海油新闻网\\2\\recoverALL.txt";
		//		String RECOVERGZ1 = "C:\\resource\\行业网站\\crawlData\\oilGas\\中海油新闻网\\2\\recoverALL.txt";
		String RECOVERGZ2 = "C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\3";
		String RECOVERGZ1 = beforeGenRecoverGZ_t();
		RecoveryJournal recover = null; 

		String line=null; 
		InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null; 
		String sourceFileEncoding = "utf-8"; 

		try {
			sourceFile = new File(RECOVERGZ1); 
			recover = new RecoveryJournal(RECOVERGZ2,mySearch_ConstantsParams.RECOVERNAME);
			//读取文件内容  
			is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is,sourceFileEncoding);  
			br=new BufferedReader(isr);  

			//一行一行写入recover.gz文件  
			while((line=br.readLine())!=null){  
				if(!line.equals("")){  
					recover.writeLine(line);  
				}  
			}  	 
		} catch (IOException e) {
			e.printStackTrace();
		}finally{  
			try {  
				if(recover!=null){  
					recover.close();  
				}    
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}
	}


	public static void main(String args[]){
		//		testExtractWeb("http://www.cnooc.com.cn/art/2017/11/8/art_191_2773291.html","G:/data/e430/hetritrix/output/cnooc/2");
		//		//		getExtractPath("a");
		//		//		testExtractWeb("F:/data/e430/hetritrix/output/cnpc_news/1");
		//		//initCrawl();
		//		//		GenRecoverGZ_t();
		//		beforeGenRecoverGZ_t();
		//		if(isRepeat("2020年我国散煤消费要减少2亿吨以上")){
		//			System.out.println("yes");
		//		}
		//		System.out.println(getModule("15"));
		//		testExtractWeb("http://www.sinopecnews.com.cn/news/content/2017-12/07/content_1695676.shtml","C:\\resource\\行业网站\\crawlData\\oilGas\\中石化新闻网\\1","11");
		//		testRegex();
//		GenRecoverGZ_t();
		fileHandle_test("C:\\resource\\行业网站\\crawlData\\oilGas\\中石油新闻中心\\3\\mirror");
	}

	public static void testRegex(){
		String url = "news--1111.html";
		String regexNumber = "(.*?)\\d{3}(.*?)";
		if(url.matches(regexNumber)){
			System.out.println("yes");
		}
	}


}
