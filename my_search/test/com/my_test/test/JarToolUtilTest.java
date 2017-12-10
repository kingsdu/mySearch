package com.my_test.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

public class JarToolUtilTest extends TestCase{
	/** 
     * 获取jar绝对路径 
     *  
     * @return 
     */  
    public static String getJarPath()  
    {  
        File file = getFile();  
        if (file == null)  
            return null;  
        return file.getAbsolutePath();  
    }  
      
    /** 
     * 获取jar目录 
     *  
     * @return 
     */  
    public static String getJarDir()  
    {  
        File file = getFile();  
        if (file == null)  
            return null;  
        return getFile().getParent();  
    }  
      
    /** 
     * 获取jar包名 
     *  
     * @return 
     */  
    public static String getJarName()  
    {  
        File file = getFile();  
        if (file == null)  
            return null;  
        System.out.println(getFile().getName());
        return getFile().getName();  
    }  
      
    public void getUserDir(){
    	String userDir = System.getProperty("user.dir");
    	userDir+= File.separator+"config"+File.separator+ "search.properties";
    	BufferedReader br = null;
    	try {
			File f = new File(userDir);
		    br = new BufferedReader(new FileReader(f));
			String s = null;
			while((s = br.readLine())!=null){
				System.out.println(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

    }
    /** 
     * 获取当前Jar文件 
     *  
     * @return 
     */  
    private static File getFile()  
    {  
        // 关键是这行...  
        String path = JarToolUtilTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();  
        try  
        {  
            path = java.net.URLDecoder.decode(path, "UTF-8"); // 转换处理中文及空格 
            System.out.println("path is "+path);
        }  
        catch (java.io.UnsupportedEncodingException e)  
        {  
            return null;  
        }  
        return new File(path);  
    }  
}
