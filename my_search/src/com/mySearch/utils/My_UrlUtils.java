package com.mySearch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class My_UrlUtils {

	public static String getSheduleTag(String crawlUrl) {
		Matcher matcher = null;
		String pattern = "http://((\\w|\\.|\\/|-|=|\\?|&)+)+";
		Pattern pattern11 = Pattern.compile(pattern,
				Pattern.DOTALL);
		matcher = pattern11.matcher(crawlUrl);
		if (matcher != null && matcher.matches()) {
			String u = matcher.group(1);
			return u.substring(0, u.indexOf("."));
		}
		return null;
	}
	
	public static void main(String[] args){      
		String site = "http://news.cnpc.com.cn/system/2017/11/07/001667442.shtml";
		System.err.println(getSheduleTag(site));
	}
	
}
