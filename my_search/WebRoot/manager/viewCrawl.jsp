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
    
    <title>爬取设置</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/viewCrawl.js"></script>
	<style type="text/css">
		.viewCrwalSite tr td{
			text-align:center;
/* 			width:160px;
			height: 35px; */
			font-size:14px;
			border:1px solid #BFDCE8;
		}
		
		.viewCrwalSite .tablehead td {
			text-align:center;
			width:150px;
			height: 50px;
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
    <div style="margin-top:30px;margin-left:20px">
     <form action="CrawlAction_startCrawl.action" method="post" id="formid">
  		<table class="viewCrwalSite" id="crawlInfo">
  			<tr class="tablehead">
  				<td style="width:80px;">
  					选择
  				</td>
  				<td style="width:120px;">
  					采集类别
  				</td>
  				<td style="width:120px;">
  				          采集网站名称
  				</td>
  				<td style="width:200px;">
  					网站地址
  				</td>
  				<td>
  					任务创建时间
  				</td>
  				<td>
  					上次采集时间
  				</td>
  				<td>
  					操作
  				</td>
  			</tr>	
  			<c:forEach items="${webinfoAllListPa}" var="webinfoPa">
  			<tr>
  			    <td>
  			       <input name="webinfo.webIds" type="checkbox" value="${webinfoPa.webId}"/>
  			    </td>
  				<td>
  				   ${webinfoPa.columnName}
  				</td>
  				<td>
  				   ${webinfoPa.webName}
  				</td>
  				<td>
  				   ${webinfoPa.webUrl}
  				</td>
  				<td>
  				   ${webinfoPa.createTime}
  				</td>
  				<td>
  					${webinfoPa.resultUrl}
  				</td>
  				<td>
  					<a href="CrawlAction_deleteCrawl?webinfo.webId=${webinfoPa.webId}" >删除</a>
  				</td>
  			</tr>
  			</c:forEach>	
  		</table>
  		<div style="margin-top:15px;">
  		   <button type="button" onclick="Tips()">采集选中网站</button>
  		</div>
  	    </form>
	  	<div style="width:850px;margin-top:10px;font-size:10pt;text-align:center">
	   		共有<font color="blue">${pageCount}</font>页&nbsp;&nbsp;
		            第<font color="blue">${pageNow}</font>页&nbsp;&nbsp;
	         <c:url var="url_pre" value="javascript:$.treeLink('CrawlAction_searchAllWebinfo.action?');">   
	         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
	         </c:url>   
	  
		     <c:url var="url_next" value="javascript:$.treeLink('CrawlAction_searchAllWebinfo.action?');" >   
		         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		     </c:url>   
		     <a href="${url_pre}">上一页</a>
		     <a href="${url_next}">下一页</a>
	     </div>
  	</div>
  </body>
</html>
