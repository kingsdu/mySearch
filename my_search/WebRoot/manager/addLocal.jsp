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
    
    <title>添加本地数据</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/viewNews.js"></script>
	<style type="text/css">
		.viewList tr td {
			text-align:center;
			width:150px;
			font-size:12px;
			border:1px solid #BFDCE8;
		}
		
		.viewList .tablehead td {
			text-align:center;
			width:150px;
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
  	<div style="margin-left:30px;margin-top:30px">
  	 	<form action="FileDocumentAction_upload" method="post" enctype="multipart/form-data">
  			<input type="file" name="upload" />
  			<input type="submit" value="上传" />
  		</form>
	</div>
	
	<div style="margin-top:30px;margin-left:30px">
  		<table class="viewList">
  			<tr class="tablehead">
  				<td style="width:200px;">
  					上传文件名称
  				</td>
  				<td>
  					展现文件名称
  				</td>
  				<td>
  					创建时间
  				</td>
  				<td>
  					操作
  				</td>
  			</tr>	
  			<c:forEach items="${fileDocumentAllListPa}" var="fileDocument">
  			<tr>
  				<td>
  					${fileDocument.fdName }
  				</td>
  				<td>
  					${fileDocument.swfName }
  				</td>
  				<td>
  					${fileDocument.createTime }
  				</td>
  				<td style="width:150px;">
  					<a href="FileDocumentAction_delFileDocument.action?fdId=${fileDocument.fdId }">删除</a>
  				</td>
  			</tr>
  			</c:forEach>	
  		</table>
	  	<div style="width:850px;margin-top:10px;font-size:10pt;text-align:center">
	   		共有<font color="blue">${pageCount }</font>页&nbsp;&nbsp;
		            第<font color="blue">${pageNow }</font>页&nbsp;&nbsp;
	         <c:url var="url_pre" value="javascript:$.treeLink('FileDocumentAction_searchAllFileDocumentPa.action?');">   
	         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
	         </c:url>   
	  
		     <c:url var="url_next" value="javascript:$.treeLink('FileDocumentAction_searchAllFileDocumentPa.action?');" >   
		         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		     </c:url>   
		     
		     
		     <a href="${url_pre}">上一页</a>
		     <a href="${url_next}">下一页</a>
	     </div>
  	</div>
  </body>
</html>
