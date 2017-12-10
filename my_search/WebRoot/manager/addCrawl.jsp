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
    
    <title>新增网站</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<%--     <script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/jquery-1.9.1.js"></script> --%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/addCrawl.js"></script>
	
	<style type="text/css">
	    .addCrawl_main{
	        margin-top:30px;
	        margin-left:20px;
		    font-family:arial; 
	    }
		.addCrawlList li{
			list-style-type:none;
			margin-top:20px;
		}
	</style>
	</head>

  
  <body>
     <div class="addCrawl_main">
      <form action="CrawlAction_CreateCrawl.action" method="post">
      <ul class="addCrawlList"><li>
      <span>网站类别(必选)
      <select name="column.columnId" style="margin-top:10px; width:100px">
         <c:forEach items="${columnList}" var="column" >
			<option value="${column.columnId }">${column.columnName }</option>
	     </c:forEach> 
      </select>
      </span>
      <button type="button" id="addCategory" name="addCategory" onclick="addCate()">新增类别</button>
      </li>
      <li>
      <span id="addInput" name="addInput" style="display:none">网站类别
      <input type="text" placeholder="请输入网站类别" />
      </span>
      </li>
      <li>
      <span>网站名称<input type="text" name="webinfo.webName" placeholder="请输入网站名称"/></span>
      </li>
      <li>
      <span>网站地址(可多个)
      <input type="text" name="webinfo.webUrl" style="width:200px" placeholder="请复制正确地址" />
      </span>
      </li>
      <li>
      <span>爬取页面地址
      <input type="text" name="webinfo.crawlURL" style="width:200px" placeholder="请复制正确地址" />
      </span>
      </li>
      <li>
      <span>爬取输出目录
      <input type="text" name="webinfo.resultUrl" style="width:200px" placeholder="请选择输出地址" />
      </span>
      </li>  
      <li>
      <span>网站关键词(可多个)
      <input type="text" name="webinfo.mark" style="width:200px" placeholder="请输入网站的关键词" />
      </span>
      </li>  
      <li>
	  <div style="margin-top:10px;margin-left:200px">
		    <input type="submit" value="添加" >
		    <input type="reset" value="清空" >
      </div>
      </li>
      </ul>
  	  </form>
  	</div>
  </body>
</html>
