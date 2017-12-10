<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>配置索引</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
     <div style="margin-top:30px;margin-left:30px">
      <form action="IndexAction_addIndex.action" method="post">
      <table width="600" border="0">
		  <tr>
		    <td>索引名称:</td>
		    <td><input name="index.indexName" class="easyui-validatebox" required="true" /></td>
		    <td>索引路径:</td>
		    <td><input name="index.indexPath" class="easyui-validatebox" required="true" /></td>
		  </tr>
		  <tr>
		    <td>文件路径:</td>
		    <td><input name="index.sourcePath" class="easyui-validatebox" required="true" /></td>
		    <td>索引工具:</td>
		    <td><input name="index.type" class="easyui-validatebox" required="true" /></td>
		  </tr>
		  <tr>
		    <td>查询语句:</td>
		    <td><input name="index.sqlSentence" class="easyui-validatebox" /></td>
		    <td>备注:</td>
		    <td><input name="index.mark" class="easyui-validatebox" /></td>
		  </tr>
		</table>
		<div style="margin-top:10px;margin-left:200px">
		    <input type="submit" value="添加" >
		    <input type="reset" value="清空" >
      	</div>
  	  </form>
  	</div>
  </body>
</html>
