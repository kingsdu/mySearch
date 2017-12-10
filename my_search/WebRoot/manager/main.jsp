<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>后台主页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/manager/js/main.js"></script>
	
	<%
		String type = request.getParameter("type");
		if("addIndex".equals(type)){
	 %>
	 	<script type="text/javascript">
	 		$(document).ready(function(){
				$('#main').load("./manager/addIndex.jsp");
			});
	 	</script>
	<%
		}else if("addColumn".equals(type)){
	 %>
		 <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("ColumnAction_getAllColumn");
				});
		 </script>
	 <%
	 	}else if("addNews".equals(type)){
	  %>
	  	<script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("NewsAction_getAllColumn");
				});
		 </script>
	  <%
	  	}else if("delNews".equals(type)){
	   %>
	   	<script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("NewsAction_searchAllNewsPa");
				});
		 </script>
	   <%
	   	}else if("addLocal".equals(type)){
	    %>
	    <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("FileDocumentAction_searchAllFileDocumentPa.action");
				});
		 </script>
	    <%
	    }else if("delFileDocument".equals(type)){
	     %>
	     <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("FileDocumentAction_searchAllFileDocumentPa.action");
				});
		 </script>
	     <%
	     }else if("addWebInfo".equals(type)){
	      %>
	      <script type="text/javascript">
		 		$(document).ready(function(){
					$('#main').load("CrawlAction_getAllWebinfo.action");
				});
		 </script>
	      <%
	     }
	      %>

	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/demo.css">
	
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
  
  <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:80px;background:#B3DFDA;padding:10px">
		垂直搜索引擎
	</div>
	
	<div data-options="region:'west',split:true,title:'业务管理'" style="width:208px;padding:0px;">
		<div class="easyui-accordion" style="width:200px;height:480px;">
		   <div id="selectzero" title="采集设置" data-options="selected:true" style="padding:10px;">
				<p>
					<a href="javascript:$.treeLink('CrawlAction_searchAllWebinfo');" >整站采集</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('CrawlAction_updateXML');" >主题采集</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('CrawlAction_getXML');"  >区域爬取</a>
				</p>
				<p>
					<a href="#nowhere" >定时采集</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('CrawlAction_getAllWebinfo');" >新增网站</a>
				</p>
				<p>
					<a href="#nowhere" >数据清洗</a>
				</p>
			</div>
			<div id="selectone" title="数据来源管理" data-options="selected:true" style="padding:10px;">
				<p>
					<a href="javascript:$.treeLink('NewsAction_getAllColumn');" >网络数据</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('NewsAction_searchAllNewsPa');" >网络数据管理</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('FileDocumentAction_searchAllFileDocumentPa');" >本地数据</a>
				</p>
			</div>
			<div id="selecttwo" title="索引管理" style="padding:10px">
				<p>
					<a href="javascript:$.treeLink('./manager/addIndex.jsp');" >配置索引</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('IndexAction_viewIndex');" >创建索引</a>
				</p>
			</div>
			<div title="栏目管理" style="padding:10px">
				<p>
				 	<a href="javascript:$.treeLink('ColumnAction_getAllColumn');" >添加栏目</a>
				</p>
				<p>
					<a href="javascript:$.treeLink('ColumnAction_searchAllColumnPa');" >栏目管理</a>
				</p>
			</div>
			<div title="用户管理" style="padding:10px">
			</div>
			<div title="角色管理" style="padding:10px">
			</div>
			<div title="权限管理" style="padding:10px">
			</div>
		</div>
	</div>
	
	<div data-options="region:'south',border:false" style="height:30px;background:#A9FACD;padding:5px;">
		<div>
			<a href="#">首页</a>
		</div>
	</div>
	<div id="main" data-options="region:'center',title:' '">
	
	</div>
  </body>
</html>
