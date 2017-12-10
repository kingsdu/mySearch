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
    
    <title>文件搜索</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<style type="text/css">
		a {
			text-decoration:none;
			border:none;
			color:#05497A
		}
		a:hover{
			text-decoration:none;
			color:#ccc
		}
	</style>
  </head>
  
  <body style="margin-top:0px">
  	<center>
	    <div style="width:800px;margin-top:0x;height:900px;background-color:aliceblue">
	    	<!-- 网页头部 -->
	    	<div style="width:800px;height:100px;background-image:url('./images/logo.jpg')">
	    		<div style="width:100px;height:15px;font-size:12px;float:right;margin-top:80px;">
	  	 			<a href="./manager/main.jsp">后台管理</a>
	  	 		</div>
	    		<div style="width:100px;height:15px;font-size:12px;float:right;margin-top:80px;">
	  	 			<a href="#">首页</a>
	  	 		</div>
	    	</div>
	    	
	    	<!-- 搜索栏 -->
	    	<div style="width:800px;height:50px;padding-top:15px;">
	    		<form action="FileDocumentAction_doQuery.action" method="post">
		    		<input type="text" name="queryString" size="50px" value="${queryString } "/>
	    			<input type="submit" name="submit" value="search file" />
    			</form>
	    	</div>
	    	
	    	<!-- 搜索结果的显示 -->
	    	<div style="width:800px;height:600px;text-align:left">
	    		<c:forEach  items="${searchList}" var="map">
	    		<div style="width:798px;height:15px;padding:5px 0px 5px 0px;;font-size:14px;border:0px #BFDCE8 solid">
	    			<a href="FileDocumentAction_viewSwf.action?swfName=${map.swfName }" target="blank">${map.fdName }</a>
	    		</div>
	    		<div style="width:800px;height:5px;">
	    		
	    		</div>
	    		</c:forEach>
	    	</div>
	    	
	    	<!-- 分页 -->
	    	<div style="width:800px;height:15px;margin-top:15px;font-size:12px;">
	    		共有<font color="blue">${pageCount }</font>页&nbsp;&nbsp;
			            第<font color="blue">${pageNow }</font>页&nbsp;&nbsp;
		         <c:url var="url_pre" value="FileDocumentAction_doQuery.action">   
		         	<c:param name="pageNow" value="${pageNow-1 }"></c:param>   
		         	<c:param name="queryString" value="${queryString}"></c:param>   
		         </c:url>   
		  
			     <c:url var="url_next" value="FileDocumentAction_doQuery.action" >   
			         <c:param name="pageNow" value="${pageNow+1 }"></c:param> 
		         	 <c:param name="queryString" value="${queryString}"></c:param>   
			     </c:url>   
			     
			     
			     <a href="${url_pre}">上一页</a>
			     <a href="${url_next}">下一页</a>
	    	</div>
	    	
	    	<!-- 页脚 -->
	    	<div style="margin-top:10px;">
	    		<jsp:include page="foot.jsp"></jsp:include>
	    	</div>
	    	<div style="height:10px;">
	    	
	    	</div>
	    </div>
  	</center>
  </body>
</html>
