$(document).ready(function(){
	$("tr").mouseover(function(){
		$(this).addClass("over");
	}).mouseout(function(){
		$(this).removeClass("over");
	});
});




function startCrawl(){
	$.ajax({  
		type : "POST",  
		url : "CrawlAction_startCrawl",  
		datatype:'json',
		success : function(data) {  
		},
		error:function(){
			alert("请求链接失败！");
		}
	});
}


function Tips(){
	var rows = document.getElementById("crawlInfo").rows; 
	var ids = document.getElementsByName("webinfo.webIds");
	debugger;
	for(var i=0;i<ids.length;i++) 
	{ 
		if(ids[i].checked){ 
			break;
		}
		if(i>ids.length-1){
			alert("请选择采集网站");
			return;
		}
	}
	document.getElementById("formid").submit();
	alert("已开始爬取，请耐心等待");
}


