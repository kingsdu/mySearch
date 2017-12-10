package com.mySearch.config;

import org.apache.struts2.ServletActionContext;

public class mySearch_ConstantsParams {

	public static final String SERVLETPATH = ServletActionContext.getServletContext().getRealPath("");

	public static final String ROOTID = "0";

	public static final String URLNAMEID = "1";

	public static final String TOMCATID = "2";

	public static final String SHEDULETAG = "3";

	public static final String RECOVERGZ_All = "4";

	public static final String RECOVERGZ_A = "5";
	
	public static final String HTTPHEAD = "6";
	
	public static final String RECOVERFILENAME = "recoverALL.txt";
	
	public static final String A_RECOVERFILENAME = "recover.gz";
	
	public static final String ORDERNAME = "recover-path";
	
	public static final String RECOVERNAME = "recover.gz";

	public static final String NULLVALUE = "";
	
	public static final String FILESEPARATOR = "/";
	
	public static final String MIRROR = "mirror";
	
	public static final String LOGS = "logs";
}

