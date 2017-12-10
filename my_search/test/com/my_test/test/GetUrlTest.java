package com.my_test.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class GetUrlTest extends TestCase{

	public static ArrayList<String> aList = new ArrayList<String>();
	public static ArrayList<String> bList = new ArrayList<String>();

	private static int count = 0;
	public static final String patternString1 = "[^\\s]*((<\\s*[aA]\\s+(href\\s*=[^>]+\\s*)>)(.*)</[aA]>).*";
	public static final String patternString2 = ".*(<\\s*[aA]\\s+(href\\s*=[^>]+\\s*)>(.*)</[aA]>).*";
	public static final String patternString3 = ".*href\\s*=\\s*(\"|'|)http://.*";
	public static final String patternString4 = "<\\s*[aA]\\s+(href\\s*=[^>]+\\s*)>";
	public static final String patternString5 = "((http://|https://)\\s*[.shtml]+)$";
	//	public static final String patternString6 = "<\\s*[aA][\\s\\S]+(href\\s*=\\s*(\"|'|)(http|https)://[^>]+\\s*)>";//sina   "<\\s*[aA]\\s+(href\\s*=\\s*(\"|'|)(http|https)://[^>]+\\s*)>";
	public static final String patternString6 = "<\\s*[aA][\\s\\S]+(href\\s*=\\s*(\"|'|)(http|https)://[^>]+\\s*)>";//sina   "<\\s*[aA]\\s+(href\\s*=\\s*(\"|'|)(http|https)://[^>]+\\s*)>";
	public static final String patternString7 = "<\\s*[aA][\\s\\S]+(href\\s*=\\s*(\"|'|)((http|https)://[^\"]+(\")))";//sina    "<\\s*[aA]\\s+(href\\s*=\\s*(\"|'|)((http|https)://[^>]+(.html|.shtml)))";
	public static final String patternString8 = "<a(.*)href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])(.*)>";//Extractor_Cnpc
	public static final String patternString9 = "(http://|https://)+((\\w|\\.|\\/|-|=|\\?|&)+)+";//抽取特征
	//	String context =           "https://news.sina.com.cn/s/wh/2017-09-17/doc-ifykynia7720477.shtml";
	//(http://|https://)+((\\w|\\.)+|\\/|-+|=|\\?|&)+
	public static final String tag = "(\\w|\\.)+";//抽取特征
	public static Pattern TAG = Pattern.compile(tag,
			Pattern.DOTALL);

	public static Pattern pattern1 = Pattern.compile(patternString1,
			Pattern.DOTALL);
	public static Pattern pattern2 = Pattern.compile(patternString2,
			Pattern.DOTALL);
	public static Pattern pattern3 = Pattern.compile(patternString3,
			Pattern.DOTALL);
	public static Pattern pattern4 = Pattern.compile(patternString4,
			Pattern.DOTALL);
	public static Pattern pattern5 = Pattern.compile(patternString5,
			Pattern.DOTALL);
	public static Pattern pattern6 = Pattern.compile(patternString6,
			Pattern.DOTALL);
	public static Pattern pattern7 = Pattern.compile(patternString7,
			Pattern.DOTALL);
	public static Pattern pattern8 = Pattern.compile(patternString8,
			Pattern.DOTALL);
	public static Pattern pattern9 = Pattern.compile(patternString9,
			Pattern.DOTALL);

	//	private static final String A_HERF = "<a(.*)href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])(.*)>";
	//	private static final String A_HERF = ".*href\\s*=\\s*(\"|'|)http://.*";
	//	private static final String NEWS_YANGTZEU = "http://news.sina.com.cn/(.*)/(.*)/(.*)/(.*).shtml";
	private static final String NEWS_CNPC = "http://news.cnpc.com.cn/system/[\\d]+/[\\d]+/[\\d]+/[\\d]+.shtml";
	private static final String NEWS_SINA = "http://news.sina.com.cn/(.*)/(.*)/(.*)/(.*).shtml";
	//http://news.sina.com.cn/s/wh/2016-08-25/doc-ifxvixeq0439030.shtml"
	/**
	 * @Description: 读取txt，抽取出符合要求的html
	 * @return:
	 * @date: 2017-9-17  
	 */
	public static void readText_old(){
		String path = "E:\\test\\sina_news.html";
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = br.readLine())!=null){
				extract_old(s);
			}
			br.close();
			//			System.out.println("find "+count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}


	public static void readText_optimize(){
		String path = "E:\\test\\sina_news.html";
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = br.readLine())!=null){
				extract_opt_1(s);
			}
			br.close();
			//			System.out.println("find "+count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}


	/**
	 * @Description: 输出结果file
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void outputFile_old(List<String> outList){
		//		String path = "E:\\test\\output.txt";//row
		String path = "E:\\test\\output_res.txt";	
		try {
			File file = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i=0;i<outList.size();i++){
				bw.append(outList.get(i));
				bw.newLine();//换行
				bw.flush();//需要及时清
			}
			bw.append(String.valueOf(outList.size()));
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}




	/**
	 * @Description: 输出结果file
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void outputFile_optimize(List<String> outList){
		//		String path = "E:\\test\\output1.txt";//row
		String path = "E:\\test\\output_res1.txt";
		try {
			File file = new File(path);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i=0;i<outList.size();i++){
				bw.append(outList.get(i));
				bw.newLine();//换行
				bw.flush();//需要及时清
			}
			bw.append(String.valueOf(outList.size()));
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}


	/**
	 * @Description: 
	 * @return:
	 * @date: 2017-9-19  
	 */
	public static void outputFile_optimize_String(String url){
		String path = "E:\\test\\output_res1.txt";
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			bw = new BufferedWriter(new FileWriter(file,true));
			bw.append(url);
			bw.newLine();
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * @Description: 一次查找
	 * @return:
	 * @date: 2017-9-17  
	 */
	public void getLinkInHTML_pattern3(String context){
		Matcher matcher = null;
		if (context != null && context.length() > 28) {
			matcher = pattern3.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.find()) {
				String	bString = matcher.group(0);
				System.out.println(bString);
				count++;
			}
		}
	}


	/**
	 * @Description: 二次查找
	 * @return:
	 * @date: 2017-9-17  
	 */
	public void getLinkInHTML_pattern1(String context){
		Matcher matcher = null;
		if (context != null && context.length() > 28) {
			matcher = pattern1.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.find()) {
				String	bString = matcher.group(matcher.groupCount() - 3);
				System.out.println(bString);
				count++;
			}
		}
	}


	/**
	 * @Description: 抽取出标签特征（一种方式过滤）
	 * 1   判断是否是a标签
	 * 2 结尾是否html
	 * 3 层次
	 * 4 正则判断层次的内容
	 * 5 去除?所带参数
	 * 6 精准限定url每层的内容
	 * @return:
	 * @date: 2017-9-17  
	 */
	public void getHTML_1(){
		Matcher matcher = null;
		String context = "<a href='http://news.sina.com.cn/o/2017-09-17/doc-ifykyfwq7982015.shtml' target='_blank' suda-uatrack='key=newschina_index_2014&value=news_link_5'>120年不漏水 港珠澳大桥隐藏的“秘密”曝光</a>";
		if (context != null && context.length() > 28) {
			matcher = pattern1.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.find()) {
				String	bString = matcher.group(matcher.groupCount() - 3);// 这个group包含所有符合正则的字符串
				String	aString = matcher.group(matcher.groupCount() - 2);// 这个group包含url的html标签
				String url1 = matcher.group(matcher.groupCount() - 1);// 最后一个group就是url
				String url0 = matcher.group(matcher.groupCount());// 最后一个group就是url

				System.out.println("matcher.groupCount():" + matcher.groupCount()); 
				System.out.println(bString);
				System.out.println(aString);
				System.out.println(url1);
				System.out.println(url0);
				System.out.println(matcher.group());
			}
		}
	}





	/**
	 * @Description: 抽取出标签特征（两种方式过滤）
	 * @return:
	 * @date: 2017-9-17  
	 */
	public static void getHTML_2(){
		Matcher matcher = null;
		String context = "<li><a data-param='?_f=index_news_0' href='http://www.sohu.com/a/192735893_123753' target='_blank' title='一名日籍商人涉嫌从事间谍活动在大连被捕'>";
		if (context != null && context.length() > 28) {
			matcher = pattern3.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.matches()) {
				matcher = pattern6.matcher(context);
				while (matcher != null && matcher.find()) {
					String url1 = matcher.group();
					System.out.println(url1);
				}
			}
		}
	}


	/**
	 * @Description: 截取出需要部分
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void test01(String context){
		Matcher matcher = null;
		//		String var = "<a href='http://news.sina.com.cn/o/2017-09-17/doc-ifykyfwq7982015.shtml' target='_blank' suda-uatrack='key=newschina_index_2014&value=news_link_5'>120年不漏水 港珠澳大桥隐藏的“秘密”曝光</a>";
		if (context != null && context.length() > 28) {
			matcher = pattern3.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.find()) {
				String url0 = matcher.group(matcher.groupCount()-1);// 最后一个group就是url
				aList.add(url0);
				//				if(url0!=null){
				//					if(url0.indexOf(" ")!=-1){
				//						String [] url = url0.split(" ");
				//						System.out.println(url[0]);
				//						count++;
				//					}else{
				//						if(url0.indexOf(">")!=-1){
				//							String [] url = url0.split(">");
				//							System.out.println(url[0]);
				//							count++;
				//						}
				//					}
				//				}
			}
		}

	}

	/**
	 * @Description: 通过两层循环，取出网页中的链接<>
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void extract_opt_1(String context){
		Matcher matcher = null;
		if (context != null && context.length() > 28) {
			matcher = pattern3.matcher(context);
			// 确定句子里包含有链接
			if (matcher != null && matcher.matches()) {
				matcher = pattern6.matcher(context);
				while (matcher != null && matcher.find()) {
					String url0 = matcher.group();// 最后一个group就是url
					matcher = pattern7.matcher(url0);
					if (matcher != null && matcher.find()) {
						String url = matcher.group(matcher.groupCount()-2);// 最后一个group就是url
						url = url.replace("\"", "");//替换",清除前后双引号
						if(url.matches(NEWS_SINA)){//和正则进行匹配							
							outputFile_optimize_String(url.toString());
						}
					}
				}
			}
		}
	}



	/**
	 * @Description: 提取输出url的特征
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void extract_opt_2(){
		readText_optimize();
		Matcher matcher = null;
		for(String context:aList){
			if (context != null && context.length() > 28) {
				matcher = pattern7.matcher(context);
				// 确定句子里包含有链接
				if (matcher != null && matcher.find()) {
					String url0 = matcher.group(matcher.groupCount()-2);// 最后一个group就是url
					url0 = url0.replace("\"", "");//替换",清除前后双引号
					if(url0.matches(NEWS_SINA)){//和正则进行匹配
						//						//1 最终抓取的链接;2 分析的内容;3常量
						bList.add(url0);
					}
				}
			}
		}
	}




	/**
	 * @Description: 抽取出的最初的url
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void extract_old(String context){
		Matcher matcher = null;
		String url = null;
		if (context != null && context.length() > 28) {
			matcher = pattern8.matcher(context);
			while(matcher.find()){
				url = matcher.group(2);
				//				aList.add(url);//测试row	
				url = url.replace("\"", "");
				if(url.matches(NEWS_SINA)){
					aList.add(url);
				}
			}
		}
	}















	/**
	 * @Description:已经使用的中石油的url获取方式测试
	 * @return:
	 * @date: 2017-9-18  
	 */
	public static void testInfore(String context){
		Matcher matcher = null;
		String url = null;
		if (context != null && context.length() > 28) {
			matcher = pattern8.matcher(context);
			while(matcher.find()){
				url = matcher.group(2);//先将url截取出来
				url = url.replace("\"", "");//替换",清除前后双引号
				if(url.matches(NEWS_CNPC)){//和正则进行匹配
					//1 最终抓取的链接;2 分析的内容;3常量
					System.out.println("url is  "+url);
					count++;
				}
			}
		}
	}



	/**
	 * @Description:读取url，抽取特征
	 * 1 判断url是否合法
	 * 2 遍历出url的层数
	 * 3 获取第一层字符，完全匹配
	 * 4 获取html的结尾，完全匹配
	 * @return:
	 * @date: 2017-9-18  http://news.sina.com.cn/c/nd/2017-09-18/doc-ifykywuc6667944.shtml
	 */
	public static void extractorFeature(){
		String context = "http://news.sina.com.cn/o/2017-09-21/doc-ifymenmt6016781.shtml";
		                //http://news.sina.com.cn/(.*)/(.*)/(.*).shtml([^\/]*)
		Matcher matcher = null;
		String finalRes = "http://";
		String findIndex = "/";
		//1 判断url是否合法
		if (context != null && context.length() > 12) {
			matcher = pattern9.matcher(context);
			if (matcher != null && matcher.matches()) {
				String url = matcher.group(2);//去除协议名称
				//判断是否有/
				if(url!=null && url.indexOf(findIndex)!=-1){
					String start = null;
					int count = 0;
					int index = 0;
					boolean first = true;
					while ((index = url.indexOf(findIndex, index)) != -1) {		
						if(first){
							start = url.substring(0, index);
							first = false;
						}
						index = index + findIndex.length();
						count++;
					}
					index = 0;
					String end = null;
					if(url.lastIndexOf(".")!=-1){
						int lastIndex = url.lastIndexOf(".");
						String temp = url.substring(lastIndex, url.length());
						if(".htm".equals(temp)||".html".equals(temp)||".shtml".equals(temp)||".shtm".equals(temp)){
							end = temp;
						}
					}
					if(start!=null){
						finalRes += start;
					}
					for(int i=0;i<count;i++){
						finalRes+="/([^/]*)";
					}
					if(end!=null){
						finalRes+= end;
					}
					System.out.println(finalRes);
				}else{
					System.out.println("no or /");
				}		
			}else{
				System.out.println("false");
			}
		}else{
			System.out.println("bad");
		}

	}



	/**
	 * @Description: 读取XML
	 * @date: 2017-9-20  
	 */
	public static String getXMLValue(){
		String key = "0";
		System.out.println(ServletActionContext.getServletContext().getRealPath("")+ File.separator + "properties.xml");
		File xmlFile = new File(ServletActionContext.getServletContext().getRealPath("")+ File.separator + "properties.xml");
		
		String result = null;
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(xmlFile);

			//利用XPath查找所有节点中，属性名 id 值为参数中id值的节点元素
			Element currElement = (Element)doc.selectSingleNode("//*[@id='"+key+"']");
			result = currElement.attributeValue("url");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static void updateXML(String key,String value){
		File xmlFile = new File(ServletActionContext.getServletContext().getRealPath("")+ File.separator + "properties.xml");
		try {
			//从文件读取XML，输入文件路径，返回XML文档
			SAXReader reader = new SAXReader();
			Document doc = reader.read(xmlFile);

			//利用XPath查找所有节点中，属性名 id 值为参数中menuId值的节点元素
			Element currElement = (Element)doc.selectSingleNode("//*[@id='"+key+"']");	
			Attribute urlAttr = currElement.attribute("name");		
			urlAttr.setValue(value);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @Description: 检测url是否匹配
	 * @return:
	 * @date: 2017-9-22  
	 */
	public static void testMatchs(){
		List<String> urls = new ArrayList<String>();
		String context1 = "http://news.sina.com.cn/o/2017-09-21/doc-ifymenmt6016781.shtml";
		String context2 = "http://news.sina.com.cn/c/gat/2017-09-22/doc-ifymesii4966142.shtml";
		String context3 = "http://news.sina.com.cn/zl/zatan/blog/2015-07-16/10024083/1505576400/59bd45d00102vu9o.shtml";
		String context4 = "http://news.sina.com.cn/zl/59bd45d00102vu9o.shtml";
		urls.add(context1);
		urls.add(context2);
		urls.add(context3);
		urls.add(context4);
		Matcher matcher = null;
		//http://news.sina.com.cn/(.*)/(.*)/(.*).shtml//http://news.sina.com.cn/(.*)/(.*)/([^/]*).shtml";//"http://news.sina.com.cn/([^/]*)/([^/]*)/([^/]*).shtml";
		String pattern = "http://news.sina.com.cn/(.*)/(.*)/(.*).shtml";
	    Pattern pattern11 = Pattern.compile(pattern,
				Pattern.DOTALL);
	    
	    for(String url:urls){
	    	matcher = pattern11.matcher(url);
			if (matcher != null && matcher.matches()) {
				String u = matcher.group();
				System.out.println(u);
	    	}else{
	    		System.out.println("aa");
	    	}
	    } 
	}
	
	
	public static void getSheduleTag(){
		List<String> urls = new ArrayList<String>();
		String context1 = "http://news.sina.com.cn/o/2017-09-21/doc-ifymenmt6016781.shtml";
		String context2 = "http://fiance.sina.com.cn/c/gat/2017-09-22/doc-ifymesii4966142.shtml";
		String context3 = "http://nasas.sina.com.cn/zl/zatan/blog/2015-07-16/10024083/1505576400/59bd45d00102vu9o.shtml";
		String context4 = "http://newsads.sina.com.cn/zl/59bd45d00102vu9o.shtml";
		urls.add(context1);
		urls.add(context2);
		urls.add(context3);
		urls.add(context4);
		Matcher matcher = null;
		//http://news.sina.com.cn/(.*)/(.*)/(.*).shtml//http://news.sina.com.cn/(.*)/(.*)/([^/]*).shtml";//"http://news.sina.com.cn/([^/]*)/([^/]*)/([^/]*).shtml";
		String pattern = "http://((\\w|\\.|\\/|-|=|\\?|&)+)+";
	    Pattern pattern11 = Pattern.compile(pattern,
				Pattern.DOTALL);
	    for(String url:urls){
	    	matcher = pattern11.matcher(url);
			if (matcher != null && matcher.matches()) {
				String u = matcher.group(1);
				u = u.substring(0, u.indexOf("."));
				System.out.println(u);
	    	}else{
	    		System.out.println("aa");
	    	}
	    } 
	}
	
	
	public static Boolean updataXMLByName(){
		String path = "E:/test/hetritrix/output/sina/11/order.xml";
		String name = "recover-path";
		String value = "111";
		File xmlFile = new File(path);
		
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		try {
			Document doc = reader.read(xmlFile);
			// 找到设置时间的xml位置
			List listModTime = doc.selectNodes("//controller/string/@name");
			Iterator iterModTime = listModTime.iterator();
			while (iterModTime.hasNext()) {
				Attribute attribute = (Attribute) iterModTime.next();
				if (attribute.getValue().equals(name)) {
					Element element = (Element) attribute.getParent();
					element.setText(value);
				}
			} 
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML字符集编码，防止乱码
			format.setEncoding("UTF-8");
			//将Document中的内容写入文件中
			XMLWriter writer=new XMLWriter(new FileOutputStream(xmlFile),format); 
			writer.write(doc);  
			writer.close();  
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}



	public static void main(String args[]){
		long start = System.currentTimeMillis(); // 记录起始时间
		//		readText();
		//		outputFile(aList);
		//				test03();
		//				outputFile(bList);
		//				getHTML_2();
		//old
		//		readText_old();
		//		outputFile_old(aList);
		//opt
		//		extract_opt_2();
		//		readText_optimize();
		//		outputFile_optimize(bList);

		//		readText_optimize();

//		getSheduleTag();
		updataXMLByName();

		//		System.out.println("success");
		long end = System.currentTimeMillis();       // 记录结束时间
		System.out.println(end-start);              // 相减得出运行时间
	}

}





