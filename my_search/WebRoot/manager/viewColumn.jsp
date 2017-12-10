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
    
    <title>栏目管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<style type="text/css">
		.viewList tr td {
			text-align:center;
			width:100px;
			font-size:12px;
			border:1px solid #BFDCE8;
		}
		
		.viewList .tablehead td {
			text-align:center;
			width:100px;
			font-size:12px;
			border:1px solid #BFDCE8;
			background-color:#95bce2;
		}
		.over{
			background:#95bce2;
		}
	</style>
  </head>
  
  <body>
    <div style="margin-top:30px;margin-left:30px">
  		<table class="viewList">
  			<tr class="tablehead">
  				<td>
  					栏目名称
  				</td>
  				<td style="width:150px;">
  					父栏目
  				</td>
  				<td>
  					索引名称
  				</td>
  				<td style="width:150px;">
  					操作
  				</td>
  			</tr>	
  			<c:forEach items="${columnAllListPa}" var="column">
  			<tr>
  				<td>
  					${column.columnName }
  				</td>
  				<td>
  					${column.pcolumnName }
  				</td>
  				<td>
  					${column.indexName }
  				</td>
  				<td style="width:150px;">
  					<input type="hidden" id="columnId" value="${column.columnId }" />
  					 删除&nbsp;&nbsp; 修改
  				</td>
  			</tr>
  			</c:forEach>	
  		</table>
	  	<div style="width:550px;margin-top:10px;font-size:10pt;text-align:center">
	   		共有<font color="blue">${pageCount }</font>页&nbsp;&nbsp;
		            第<font color="blue">${pageNow }</font>页&nbsp;&nbsp;
	         <c:url var="url_pre" value="javascript:$.treeLink('ColumnAction_searchAllColumnPa.action?');">   
	         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
	         </c:url>   
	  
		     <c:url var="url_next" value="javascript:$.treeLink('ColumnAction_searchAllColumnPa.action?');" >   
		         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		     </c:url>   
		     
		     
		     <a href="${url_pre}">上一页</a>
		     <a href="${url_next}">下一页</a>
	     </div>
  	</div>
  </body>
</html>
