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
    
    <title>添加新闻</title>
    
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
  	<div style="margin-left:30px;margin-top:30px">
  	 	<form action="NewsAction_addNews" method="post">
		    <table style="margin-top:20px;margin-left:20px;font-size:12px">
			  <tr>
			    <td>栏目名称：</td>
			    <td>
			    	<select name="column.columnId" style="width:150px">
					  	<c:forEach items="${columnList}" var="column" >
					  		<option selected="selected" value="-1">--请选择-- </option>
							<option value="${column.columnId }">${column.columnName }</option>
				 		</c:forEach> 
					</select>
			    </td>
			    <td>新闻标题：</td>
			    <td><input name="news.newsTitle" class="easyui-validatebox" required="true" /></td>
			  </tr>
			  <tr>
			    <td>来源URL：</td>
			    <td>
			    	<input name="news.sourceUrl" class="easyui-validatebox" required="true" />
			    </td>
			    <td>内容文件路径：</td>
			    <td>
			    	<input name="news.filePath" class="easyui-validatebox" required="true" />
			    </td>
			  </tr>
			  <tr>
			    <td>新闻时间：</td>
			    <td>
			    	<input name="news.newsTime" class="easyui-validatebox" required="true" />
			    </td>
			    <td>来源网站：</td>
			    <td>
			    	<input name="news.sourceNet" class="easyui-validatebox" required="true" />
				</td>
			  </tr>
			  <tr>
			    <td>备注：</td>
			    <td>
			    	<textarea rows="6" cols="40" name="news.mark" value=""></textarea>
			    </td>
			  </tr>
			</table>
			<div style="margin-top:10px;margin-left:180px;">
				<input type="submit" value="添加" ><input type="reset" value="清空" >
			</div>
  	 	</form>
	</div>
  </body>
</html>
