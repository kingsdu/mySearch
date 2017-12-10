package org.archive.crawler.extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.io.ReplayCharSequence;
import org.archive.util.HttpRecorder;

import com.mySearch.config.mySearch_ConstantsParams;
import com.retrieve.util.StringUtils;
import com.retrieve.util.XMLHandleUtils;

public class Extractor_Cnpc extends Extractor{

	private static final long serialVersionUID = -2743922205448176772L;
	public static final String patternString1 = ".*href\\s*=\\s*(\"|'|)http://.*";
	public static final String patternString2 = "<\\s*[aA][\\s\\S]+(href\\s*=\\s*(\"|'|)(http|https)://[^>]+\\s*)>";
	public static final String patternString3 = "<\\s*[aA][\\s\\S]+(href\\s*=\\s*(\"|'|)((http|https)://[^\"]+(\")))";
	public static final String patternString4 = "<a(.*)href\\s*=\\s*(.*)>";//Extractor_Cnpc-row      "<a(.*)href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])(.*)>"
	public static final String patternString5 = "(http://|https://)+((\\w|\\.|\\/|-|=|\\?|&)+)+";//抽取特征


	private static String URL_REGEX  = null;

	public static Pattern pattern1 = Pattern.compile(patternString1,
			Pattern.DOTALL);
	public static Pattern pattern2 = Pattern.compile(patternString2,
			Pattern.DOTALL);
	public static Pattern pattern3 = Pattern.compile(patternString3,
			Pattern.DOTALL);
	public static Pattern pattern4 = Pattern.compile(patternString4,
			Pattern.CASE_INSENSITIVE);
	public static Pattern pattern5 = Pattern.compile(patternString5,
			Pattern.DOTALL);

	public Extractor_Cnpc(String name, String description) {
		super(name, description);
	}

	public Extractor_Cnpc(String name) {
		super(name, "Cnpc news extractor");
	}

	@Override
	protected void extract(CrawlURI curi) {
		String url = "";
		try {
			HttpRecorder hr = curi.getHttpRecorder();
			if(hr == null){
				throw new IOException("HttpRecorder is null");
			}
			ReplayCharSequence cs = hr.getReplayCharSequence();
			if(cs == null){
				return;
			}
			String context = cs.toString();
			Pattern pattern = Pattern.compile(patternString4, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(context);
			while(matcher.find()){
				url = matcher.group();//先将url截取出来
				writeUriIntoTxt(url,"F:\\data\\test\\output.txt");
				url = url.replace("\"", "");//替换",清除前后双引号
				URL_REGEX = extractor_Feature(readCrawlUrl());
				if(url.matches(URL_REGEX)){//和正则进行匹配
					curi.createAndAddLinkRelativeToBase(url, context, Link.NAVLINK_HOP);
					writeUriIntoTxt(url,getWriteUrlPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public static void main(String[] args) {
		extract_url();
		String url = "http://www.sinopecnews.com.cn/news/content/2017-11/07/content_1692856.shtml";
		extractor_Feature(url);
	}

	/**
	 * @Description: TODO
	 * @param:
	 * @return:
	 * @date: 2017-11-8  
	 */
	public static void extract_url(){
		String url = "http://www.sinopecnews.com.cn/news/content/2017-11/07/content_1692856.shtml";
		BufferedReader br = null;
		int i =0;
		try {
			br = new BufferedReader(new FileReader(new File("F:/data/test/aaa.html")));
			String line = "";
			while((line = br.readLine())!=null){
				Pattern pattern = Pattern.compile(patternString4,Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					i++;
					line = matcher.group(1);//先将url截取出来
					//				writeUriIntoTxt(url,"E:\\test\\myheritrix_output_config.txt");
					line = line.replace("\"", "");//替换",清除前后双引号
					//					System.out.println("line    "+line+"i   "+i);
					URL_REGEX = extractor_Feature(url);
					if(url.matches(URL_REGEX)){//和正则进行匹配
						//					curi.createAndAddLinkRelativeToBase(url, context, Link.NAVLINK_HOP);
						//					writeUriIntoTxt(url,getWriteUrlPath());
						System.out.println("line    "+line+"i   "+i);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 抽取出需要的url特征
	 * 1 抽取出其中的规则
	 * @return:String context
	 * @date: 2017-9-19  
	 */
	protected static String extractor_Feature(String context){
		//抽取规则
		Matcher matcher = null;
		String finalRes = "http://";
		String findIndex = "/";
		Pattern pattern = Pattern.compile(patternString5, Pattern.CASE_INSENSITIVE);//抽取所有html
		//1 判断url是否合法
		if (context != null && context.length() > 12) {
			matcher = pattern.matcher(context);
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
						//						finalRes+="/([^/]*)";精确模式，此模式，匹配出的url较少
						finalRes+="/(.*)";//大概模式
					}
					if(end!=null){
						finalRes+= end;
					}		    
					return finalRes;
				}else{
					System.out.println("no or /");
				}		
			}else{
				System.out.println("false");
			}
		}else{
			System.out.println("bad");
		}
		return finalRes;
	}



	protected static void writeUriIntoTxt(CharSequence uri,String path){
		BufferedWriter bw = null;
		try {
			if(path!=null){
				File file = new File(path);
				bw = new BufferedWriter(new FileWriter(file,true));
				if(uri!=null){
					bw.append(uri);
					bw.newLine();
					bw.flush();
				}	
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  		
	}



	/**
	 * @Description: 获取CrawlUrl
	 * @return:
	 * @date: 2017-9-22  
	 */
	protected static String readCrawlUrl(){
		String path = Thread.currentThread().getContextClassLoader().getResource("properties.xml").getPath();
		String crawlUrl = XMLHandleUtils.getXML(path, mySearch_ConstantsParams.URLNAMEID);
		return crawlUrl;	
	}


	/**
	 * @Description: 获取写出url的地址
	 * @return:
	 * @date: 2017-9-27  
	 */
	protected static String getWriteUrlPath(){
		String path = Thread.currentThread().getContextClassLoader().getResource("properties.xml").getPath();
		String urlPath = XMLHandleUtils.getXML(path, mySearch_ConstantsParams.RECOVERGZ_A);
		return urlPath;
	}






}
