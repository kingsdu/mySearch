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
    
    <title>添加栏目</title>
    
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
  	 	<form action="ColumnAction_addColumn.action" method="post">
		<table style="margin-top:20px;margin-left:20px;font-size:12px">
			<tr>
				<td>
					父栏目:
				</td>
				<td>
					<select name="column.columnId" style="width:150px">
					  	<c:forEach items="${columnList}" var="column" >
							<option value="${column.columnId }">${column.columnName }</option>
				 		</c:forEach> 
					</select>
				</td>
			</tr>
			
			<tr>
				<td>栏目名称:</td>
				<td><input name="column.columnName" class="easyui-validatebox" required="true" /></td>
			</tr>
			<tr>
				<td>
					索引名称:
				</td>
				<td>
					<select name="index.indexId" style="width:150px">
					  	<c:forEach items="${indexList}" var="index" > 
							<option value="${index.indexId }">${index.indexName }</option>
				 		</c:forEach> 
					</select>
				</td>
			</tr>
			<tr>
				<td>备注:</td>
				<td><textarea name="column.mark" class="easyui-validatebox" style="width:150px;height:80px;"></textarea></td>
			</tr>
		</table>
			<div style="margin-top:10px;margin-left:80px;">
				<input type="submit" value="添加" ><input type="reset" value="清空" >
			</div>
  	 	</form>
	</div>
  </body>
</html>
