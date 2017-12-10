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
    
    <title>显示文件</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/flexpaper_flash.js"></script>
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
	    	</div>
	    	
	    	
	    	<div style="width:800px;height:650px;padding-top:15px;">
	    		 <a id="viewerPlaceHolder" style="width:800px;height:650px;display:block"></a>
	        
		        <script type="text/javascript"> 
					var fp = new FlexPaperViewer(	
							 'FlexPaperViewer',
							 'viewerPlaceHolder', { config : {
							 SwfFile : escape('upload/${swfName}'),
							 Scale : 0.6, 
							 ZoomTransition : 'easeOut',
							 ZoomTime : 0.5,
							 ZoomInterval : 0.2,
							 FitPageOnLoad : true,
							 FitWidthOnLoad : false,
							 FullScreenAsMaxWindow : false,
							 ProgressiveLoading : false,
							 MinZoomSize : 0.2,
							 MaxZoomSize : 5,
							 SearchMatchAll : false,
							 InitViewMode : 'SinglePage',
							 
							 ViewModeToolsVisible : true,
							 ZoomToolsVisible : true,
							 NavToolsVisible : true,
							 CursorToolsVisible : true,
							 SearchToolsVisible : true,
	  						
	  						 localeChain: 'en_US'
							 }});
		        </script>            
	    	</div>
	    	
	    	
	    	<!-- 页脚 -->
	    	<div style="margin-top:30px;">
	    		<jsp:include page="foot.jsp"></jsp:include>
	    	</div>
	    	<div style="height:10px;">
	    	
	    	</div>
	    </div>
  	</center>
  </body>
</html>
