/**
 * 处理配置日志总量以及跳转日志界面
 */
$(function(){
	 //获取配置日志总数
	 getTotalLog();
});
var getTotalLog=function(){
	/*
		td需要配置属性有，audit,functionId，compileId，action
	    */
	   var data={};
	   //data.date=new Date();
	   data.compileIds=[];
	   data.serviceIds=[];
	   data.objs=[];
	   $("td[compileId]").each(function(){
		   	var audit=$(this).attr("audit");
		   	var compileId=$(this).attr("compileId");
		   	var serviceId=$(this).attr("serviceId");
		   	var functionId=$(this).attr("functionId");
		   	if(audit&&compileId&&functionId&&serviceId){
		   		if(audit == 1){
					var has=false;
					for(var i=0;i<data.serviceIds.length;i++){
						if(data.serviceIds[i]==$(this).attr("serviceId")){
							has=true;
						}
					}
					if(!has){
						data.serviceIds.push($(this).attr("serviceId"));
					}
					//$(this).attr("date",data.date.getTime());
					data.compileIds.push($(this).attr("compileId"));
					data.functionId=$(this).attr("functionId");
					data.objs.push($(this));
			   	}else{
			   		$(this).html("0");
			   	}
		   	}else{
		   		$(this).html("0");
		   	}
		});
	   if(data.compileIds.length>0){
		   GetLogTotal(data);
	   }
	   
}
var GetLogTotal=function(_data){
	var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
	var timeStamp;
	if(_data.date){
		timeStamp=_data.date.valueOf(); 
	}/*else{
		timeStamp=(new Date()).valueOf(); 
	}*/
	var totalTrs=$(_data.objs);
	var timeout=$.validator.messages.timeout;
	var failed=$.validator.messages.failed;
	var request=$.ajax({
		type:'post',
		timeout:10000,//超时时间设置，查询接口时间过长超时
		url:pathName+'/report/ajaxGetLogTotal',
		data:{"endTime":timeStamp,"serviceIds":_data.serviceIds.join(','),"functionId":_data.functionId,"compileIds":_data.compileIds.join(',')},
		dataType:'json',
		async:true,
		success:function(data,textStatus){//处理返回结果
			if(textStatus=="success"){
				totalTrs.each(function(){
					var hasLog=false;
					for(var i=0;i<data.length;i++){
						if($(this).attr("compileId")==data[i].compileId){
							$(this).attr("id",i+"logTotal");
							$(this).html(data[i].sum);
							$(this).parent("tr").find("td:eq(1)").html("<a href='javascript:;' onclick='toLogSearch("+i+")'>"+data[i].compileId+"<a>");
							hasLog=true;
						}
					}
					if(!hasLog){
						$(this).html("0");
					}
				})
			}
		},
		complete:function(XMLHttpRequest,status){//超时设置
			if(status=="timeout"){
				totalTrs.each(function(){
					$(this).html(timeout);
				})
			}else if(status !="success"){
				totalTrs.each(function(){
					$(this).html(failed);
				})
			}
		}
	});
}

var toLogSearch = function(index){
	// 保存配置界面URL
	$("#searchForm").each(function(){
		var cfgUrl = $(this).attr("action");
		sessionStorage.removeItem('cfgUrl');
		sessionStorage.setItem('cfgUrl',cfgUrl);
	});
	
	var td = document.getElementById(index+"logTotal");
	$("td[compileId]").each(function(){
		if($(this).attr("id") == index+"logTotal"){
			// 获取检索日志参数
			var serviceId=$(this).attr("serviceId");
			var compileId=$(this).attr("compileId");
			var functionId=$(this).attr("functionId");
			var date=$(this).attr("date");
			var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
			
			switchMenu("152");
			window.location.href = pathName+"/toLogSearch?service="+serviceId+"&functionId="+functionId+"&cfgId="+compileId+"&date="+date+"&isLogTotalSearch=yes";			
			
		}
	});
	
}

var switchMenu = function(topMenuId){
	// 顶部菜单切换	menuId： Policies/86 Logs/152
	$(".hor-menu ul li",parent.document).each(function(){
		var id = $(this).children("a").attr("id");
		if(id == topMenuId){
			$(this).addClass("active");
		}else{
			$(this).removeClass("active");
		}
	});
	
	// 保留左侧菜单特征
	var menusObj = $(".page-sidebar ul[class!='hide'] .active",parent.document);
	var firstMenuName = menusObj.attr("menu-name");
	var lastMenuName = $(menusObj,parent.document).last("li[class='nav-item active']").attr("menu-name");
	
	var middleMenu = new Array();
	$(menusObj,parent.document).find("li").each(function(){
		if($(this).hasClass("active")){
			middleMenu.push($(this).attr("menu-name"));
		}
	});
	
	// 左侧菜单切换
	$(".page-sidebar",parent.document).find("ul[id]").each(function(){
		var id = $(this).attr("id")
		if(id.indexOf(topMenuId) != -1){
			$(this).removeClass("hide");
			// 收回展开的菜单
			$(this).find("li").each(function(index){
				$(this).children("a").children("span").next().removeClass("open");
				$(this).children("ul").hide();
			});
			
			// 选中左侧菜单
			$(this).children("li").each(function(){
				if($(this).attr("menu-name") == firstMenuName){
					$(this).addClass("active open");// 选中主菜单
					$(this).children("a").children("span").next().addClass("open");//﹀
					$(this).children("ul").show();
					
					$(this).find("li").each(function(){
						if($.inArray($(this).attr("menu-name"),middleMenu) != -1){
							$(this).addClass("active open");
							$(this).children("a").children("span").next().addClass("open");
							$(this).children("ul").show();
						}
						if($(this).attr("menu-name") == lastMenuName){
							//$(this).addClass("active");
							$(this).children("a").trigger("click");
							return false;
						}
						
					});
					
				}
				
			});
		}else{
			$(this).addClass("hide");
		}
	});
	//loading('onloading');
}
