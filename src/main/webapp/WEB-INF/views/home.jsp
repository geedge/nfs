<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/pages/img/logo.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${fns:getStringProperty('productName','NIS')}</title>

<script src="${pageContext.request.contextPath}/static/pages/scripts/home.js" type="text/javascript"></script>


<script type="text/javascript">
	$(document).ready(function() {
		sessionStorage.setItem("log_total","${log_total}");
		sessionStorage.setItem("log_time_start","${log_time_start}");
		sessionStorage.setItem("log_time_range","${log_time_range}");
		window.frames['mainFrame'].location="${ctx}/dashboard/logChart";
		//国际化切换
		var lang = "${cookie.Language.value }".toLowerCase();
		var $obj;
		if(lang=="" || lang.indexOf("en")!=-1) {
			$obj = $("#en");
		}else if(lang.indexOf("cn")!=-1) {
			$obj = $("#cn");
		}else if(lang.indexOf("ru")!=-1) {
			$obj = $("#ru");
		}
		$("#lang_text").html($obj.html());
		$obj.parent("li").addClass("hide");
		
		// === Search input typeahead === //
		$('#searchText').typeahead({
			source: function(query,process) {
				
				var sourceArray = new Array();
				$(".accordion:visible a[target='mainFrame']").each(function(){
					
					sourceArray.push($(this).parents("li").attr("menu-name")+"_"+$(this).parents("li").attr("menu-id"));
				})

				return process(sourceArray)
			},
			setValue:function(item){
			},
			items: 8,
			matcher:function(term){
				var mod1 = pinyin.getPY_Header(term);
				var mod2 = pinyin.getPY_All(term);
				var mod3 = term;
				var s1 = mod1.toUpperCase().indexOf(this.query.toUpperCase())!=-1;
				var s2 = mod2.toUpperCase().indexOf(this.query.toUpperCase())!=-1;
				var s3 = mod3.toUpperCase().indexOf(this.query.toUpperCase())!=-1;
				
				return (s1||s2||s3);
			}
		});
		
		
		$("#searchText").click(function(){
			$(this).val("");
		});

	});

//页面跳转函数 level:级别，1顶级，2有子级。name:菜单名称，有多级#间隔。url：访问路径。object：点击元素对象。
function page_turn(id, functionId,level, name, url,obj){
	var $object = $("#menu_"+id);//点击当前级别
	$(".page-sidebar li").filter(".active,.open").removeClass("active open");//删除选中样式
	
	var $header = $(".page-breadcrumb");//添加头部信息
	$(".page-breadcrumb").empty();

	
	var parent_li = $object.parents("li");
	var parent_parent_li = $object.parents("li").parents("li");
						 
	var breadcrumb = $(".accordion:visible").attr("menu-name");
	if(level==1){//一级菜单
		breadcrumb += "#"+$object.attr("menu-name");
	}else if(level==2){//二级菜单
		parent_li.addClass("active open");
		breadcrumb += "#"+parent_li.attr("menu-name")+"#"+$object.attr("menu-name");
	}else if(level==3){//三级
		parent_li.addClass("active open");
		parent_parent_li.addClass("active open");
		breadcrumb += "#"+parent_parent_li.attr("menu-name")+"#"+parent_li.attr("menu-name")+"#"+$object.attr("menu-name");
	}
	$object.addClass("active");	
	
	
	
	$header.append("<li><a href='index.html'><spring:message code='home'></spring:message></a> <i class='fa fa-circle'></i></li>");
	var breadcrumbs = breadcrumb.split("#");
	for(var i=0;i<breadcrumbs.length;i++) {
		var circle="";
		if(i!=breadcrumbs.length-1) {
			circle = " <i class='fa fa-circle'></i>";
		}
		$header.append("<li><a href='javascript:void(0);'>"+breadcrumbs[i]+"</a>"+circle+"</li>");
	}
	


    App.scrollTo()
    //日志增加遮罩层
// 	if(url.search("/nis/log/")!=-1){
// 		loading();
// 	}else{
	    App.startPageLoading({animate:true});
// 	}
//     if(url.search("/nis/dashboard/")!=-1){
//     	$('.page-sidebar .sidebar-toggler').click();
// 	}
    
	//调入页面
	if(url.indexOf("?")>0){
		url = url+"&functionId="+functionId;
	}else{
		url = url+"?functionId="+functionId;
	}
	window.frames['mainFrame'].location=url;
     
    $("#mainFrame").load(function(){
    	 App.stopPageLoading();
    });
}


function searchMenu() {
	
	if ($('body').hasClass("page-sidebar-closed")) {
        if ($('.sidebar-search').hasClass('open') === false) {
            if ($('.page-sidebar-fixed').size() === 1) {
                $('.page-sidebar .sidebar-toggler').click(); //trigger sidebar toggle button

            }
            $('.sidebar-search').addClass("open");
        } else {

          searchForm();
        }
    } else {
    	searchForm()
    }

    if ($('.sidebar-search').size() !== 0) {
        $('.sidebar-search .input-group').on('click', function(e){
            e.stopPropagation();
        });

        $('body').on('click', function() {
            if ($('.sidebar-search').hasClass('open')) {
                $('.sidebar-search').removeClass("open");
            }
        });
    }
}

function searchForm() {
	
	 var search_txt = $("#searchText").val();
		if(search_txt.indexOf('_')!=-1){
			search_txt = search_txt.substring(search_txt.lastIndexOf('_'));
			$("#menu"+search_txt+" >a").trigger('click');
		}else {
			alert('未找到该菜单！');
		} 
}

function changeFrameHeight() {

	var iframe = $("#mainFrame");
	var clientHeight = $(window).height();

	var headerHeight = $(".page-header").height();
	var barHeight = $(".page-bar").height();
	var footerHeight = $(".page-footer").height();
	var paddingHeight  =  20; 

	iframe.contents().find(".page-content").addClass("page-content-body");
	iframe.prop("height",clientHeight-headerHeight-barHeight-footerHeight-paddingHeight);
	menuHeight();
}
window.onresize=function(){
	changeFrameHeight();
	menuHeight();
}

function  menuHeight(){
	var height=document.documentElement.clientHeight;
	if($('.overhi')){
		$('.overhi').css({'height':height-82+'px'});
		$('.page-content-wrapper').css({'height':height-82+'px'});
	
	}
}
function refreshCache(cacheName){
	$.ajax({
		type:'post',
		url:'${ctx}/sys/dict/refreshCache',
		data:{"cacheName":cacheName},
		dataType:'text',
		async:false,
		success:function(data,textStatus){
			if(data=="success"){
				alert("缓存清理成功");
			}else{
				alert("缓存清理失败");
			}
		}
	})
}
</script>
<style type="text/css">
.bodyhi{
  overflow: hidden;
}
.overhi{
    overflow-y: auto;
    overflow-x: hidden;
}
#helpHref{
padding:16px 6px 13px 8px;
color:#c5c5c5
}
#helpHref:hover{
background:#3d3d3d;
}
</style>
</head>


<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white bodyhi">
	<!-- BEGIN HEADER -->
	<div class="page-header navbar navbar-fixed-top">

		<!-- BEGIN HEADER INNER -->
		<div class="page-header-inner ">
			<!-- BEGIN LOGO -->
			<div class="page-logo">
				<a href="index.html"> <img
					src="${pageContext.request.contextPath}/static/layouts/layout/img/logo2.png"
					alt="logo" class="logo-default" />
				</a>
				
				<div class="menu-toggler sidebar-toggler">
                     <span></span>
                 </div>
			</div>
			<!-- END LOGO -->


			<!-- BEGIN MEGA MENU -->
			<div class="hor-menu  hidden-sm hidden-xs">
				
				<ul class="nav navbar-nav mega-menu">
		           <c:forEach items="${fns:getMenuTreeList()}" var="menu" varStatus="idxStatus">
		           		
		           		<c:if test="${menu.isTop eq 1 && menu.isShow eq 1}">
	                  
				  			<li> 
					  			<c:choose>
					  				<c:when test="${not empty menu.children and menu.children[0].isTop eq 1 }">
					  				
					  					<a href="javascript:;" id="${menu.id}" data-hover="megamenu-dropdown" data-close-others="true"
					  						data-original-title="<spring:message code="${menu.code}"/>" class="tooltips" data-placement="right">
							  				<%-- ${menu.name } --%><%-- <spring:message code="${menu.code}"></spring:message> --%>
							  				<c:set var="menuName" ><spring:message code='${menu.code}'/></c:set>
							  				${fns:abbr(menuName,15)}
							  				<i class="fa fa-angle-down"></i>
						  				</a>
						  				
						  				<ul class="dropdown-menu pull-left">
				          					<c:forEach  items="${menu.children}" var="secondMenu" varStatus="secondStatus">
				          						<c:if test="${secondMenu.isTop eq 1 && secondMenu.isShow eq 1}">
				          							<%-- <c:if test="${secondStatus.index==2}">
				          								<li class="divider"></li>
				          							</c:if> --%>
				          							<li>
				          								<a href="javascript:;" id="${secondMenu.id}">
				          									<i class="${secondMenu.icon }"></i>&nbsp;
				          									<%-- ${secondMenu.name } --%><spring:message code="${secondMenu.code}"></spring:message>
				          								</a>
				          							</li> 
				          							<li class="divider"></li>
				          						</c:if>
				          						
				          					</c:forEach>
				          					
			          					</ul>
					  				</c:when>
					  				
					  				<c:otherwise>
					  					<a href="javascript:;" id="${menu.id}" data-original-title="<spring:message code="${menu.code}"/>" class="tooltips" data-placement="bottom">
							  				<%-- ${menu.name } --%><%-- <spring:message code="${menu.code}"></spring:message> --%>
							  				<c:set var="menuName" ><spring:message code='${menu.code}'/></c:set>
							  				${fns:abbr(menuName,15)}
						  				</a>
					  				</c:otherwise>
					  			</c:choose>
		          				
				  			</li>
				  		
				  	    </c:if>
          				
          			</c:forEach>
		           
	           </ul>
	           
			</div>
			<!-- END MEGA MENU -->


			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<c:if test="${fns:getUser().loginId eq 'admin'}">
						<li  class="dropdown  dropdown-user" id="cache">
							<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" >
	                         	<i class="fa fa-language"></i><span class="username username-hide-on-mobile" id="cache_text"> clear cache  </span>
	                         </a>
							<ul class="dropdown-menu">
								<li>
									<a href="javascript:refreshCache('functionServiceDict')">refresh function service</a>
								</li>
								<li>
									<a href="javascript:refreshCache('functionRegionDict')">refresh function region</a>
								</li>
								<li>
									<a href="javascript:refreshCache('asnNoCache')">refresh ASN no</a>
								</li>
							</ul>
						</li>
					</c:if>
					<!-- 在线帮助 -->
					<li class="dropdown  dropdown-user nav">
                         <a href="${ctx}/sys/help" id="helpHref"  target="mainFrame" >
                         	<i class="icon-question  font-sharp"></i>
                         	<span class="username username-hide-on-mobile" id="help">  
                         		<spring:message code="help" />
                         	</span>
                         </a>
                     </li>
                     <!-- 系统语言 -->
				     <li class="dropdown  dropdown-user" id="language">
				     	
				     	 
				
                         <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" >
                         	<i class="fa fa-language"></i><span class="username username-hide-on-mobile" id="lang_text">  </span>
                         </a>
                        
                         <ul class="dropdown-menu">
                    	   <li><a href="${pageContext.request.contextPath}/language?language=zh_CN" id="cn">&nbsp;简体中文&nbsp;</a></li>
                           <li><a href="${pageContext.request.contextPath}/language?language=en" id="en">&nbsp;English&nbsp;</a></li>
						   <li><a href="${pageContext.request.contextPath}/language?language=ru" id="ru">&nbsp;русский язык&nbsp;</a></li>
                         </ul>
                     </li> 

					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-user"><a href="javascript:;"
						class="dropdown-toggle" data-toggle="dropdown"
						data-close-others="true"> 
						<img alt="" class="img-circle" src=${fns:getUser().photo }/>
<%-- 							src="${pageContext.request.contextPath}/static/pages/img/avatar.png" /> --%>
							<span class="username username-hide-on-mobile"> 
								${fns:getUser().name}</span> <i class="fa fa-angle-down"></i>
					</a>
						<ul class="dropdown-menu dropdown-menu-default">
							
							<li><a href="${ctx}/sys/user/info" target="mainFrame"><i class="icon-user"></i>&nbsp; <spring:message code="userInfo"></spring:message></a></li>
							<li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="icon-lock"></i>&nbsp; <spring:message code="updatePwd"></spring:message></a></li>
							<li><a href="${pageContext.request.contextPath }/logout" >
								<i class="icon-key"></i> <spring:message code="exit"></spring:message></a></li>
						</ul></li>
					<!-- END USER LOGIN DROPDOWN -->

				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->


		</div>
		<!-- END HEADER INNER -->
	</div>
	<!-- END HEADER -->


	<!-- BEGIN HEADER & CONTENT DIVIDER -->
	<div class="clearfix"></div>
	<!-- END HEADER & CONTENT DIVIDER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar-wrapper">
			<!-- BEGIN SIDEBAR -->
			<div class="page-sidebar navbar-collapse  overhi" >
			
				<ul
					class="page-sidebar-menu  page-header-fixed page-sidebar-menu-light"
					data-keep-expanded="false" data-auto-scroll="true"
					data-slide-speed="200" style="padding-top: 0px">
					<li class="sidebar-toggler-wrapper hide">
						<div class="sidebar-toggler">
							<span></span>
						</div> 
					</li>

					<li class="sidebar-search-wrapper">
						<form class="sidebar-search">
							<a href="javascript:;" class="remove"> <i class="icon-close"></i>
							</a>
							<div class="input-group">
								<input class="form-control" autocomplete="off"  spellcheck="false" placeholder="Search..." type="text" id="searchText">
								<span class="input-group-btn"> <a href="javascript:;"
									class="btn" onclick="searchMenu();"> <i class="icon-magnifier"></i>
								</a>
								</span>
							</div>
						</form> 
					</li>
				</ul>
				
				
				<%@include file="/WEB-INF/include/left_menu.jsp" %>
				
				
				<!-- END SIDEBAR MENU -->
			</div>
			<!-- END SIDEBAR -->
		</div>
		<!-- END SIDEBAR -->
		
		

		<div class="page-content-wrapper">
			<div class="page-content">
	
				<div class="page-bar">
					<ul class="page-breadcrumb">
						<li><a href="${ctx}/dashboard/logChart" target="mainFrame"><spring:message code="home"></spring:message></a> <i class="fa fa-circle"></i></li>
					</ul>
				</div>
				
		
				 <iframe id="mainFrame" name="mainFrame" width="100%" onload="changeFrameHeight()" frameborder="0" srcolling="no">	
				 	<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
								</tr>
							</tbody>
						</table>
			     </iframe>
				


			</div>
			<!-- END CONTENT BODY -->
		</div>
		<!-- END CONTENT -->

	</div>
	<!-- END CONTAINER -->


	<!-- BEGIN FOOTER -->
	<div class="page-footer" style="position: fixed;right: 0;left: 0;z-index: 1030;">
		<div class="page-footer-inner">
			CEIEC All Rights Reserved，CEIEC &copy;
		</div>
		<div class="scroll-to-top">
			<i class="icon-arrow-up"></i>
		</div>
	</div>
	<!-- END FOOTER -->
</body>


</html>