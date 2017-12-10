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
    
    <title>创建索引</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/viewIndex.js"></script>
	
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
      <table width="1000" class="viewList">
		  <tr class="tablehead">
		    <td>索引名称</td>
		    <td style="width:150px">索引路径</td>
		    <td style="width:150px">文件路径</td>
		    <td>索引工具</td>
		    <td>索引动作</td>
		    <td>索引状态</td>
		    <td style="width:150px">索引时间</td>
		    <td style="width:150px">操作</td>
		  </tr>
		  <c:forEach items="${indexList}" var="index">
		  <tr>
		    <td>
		    	${index.indexName}
			</td>
		    <td>
		    	${index.indexPath }
			</td>
		    <td>
		    	${index.sourcePath }
			</td>
		    <td>
		    	${index.type }
			</td>
		    <td>
		    	${index.action }
			</td>
		    <td>
		    	<c:if test="${index.status> 0}">已索引</c:if>
		    	<c:if test="${index.status < 1}">未索引</c:if>
			</td>
		    <td>
		    	${index.indexTime }
			</td>
		    <td>
		    	<input type="hidden" name="indexId" value="${index.indexId}" />
		    	<a href="javascript:$.treeLink('IndexAction_createIndex?indexIdStr=${index.indexId}');" >创建索引</a>
			</td>
		  </tr>
		  </c:forEach>	
		</table>
		<div style="width:1000px;margin-left:400px;margin-top:10px">
			共有<font color="blue">${pageCount }</font>页&nbsp;&nbsp;
			第<font color="blue">${pageNow }</font>页&nbsp;&nbsp;	
			 <c:url var="url_pre" value="javascript:$.treeLink('IndexAction_viewIndex?pageType=pre');">   
	         </c:url> 
	         
	         <c:url var="url_next" value="javascript:$.treeLink('IndexAction_viewIndex?pageType=next');">   
	         </c:url> 			
             <a href="${url_pre}">上一页</a>
		     <a href="${url_next}">下一页</a>
		</div>
  	</div>
  </body>
</html>
