<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/global/css/layout.css" type="text/css" media="screen">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/global/plugins/jquery-ui/jquery-ui.min.js"></script>

<title><spring:message code="traffic_user_behavior"></spring:message></title>
<style>
	td:hover{
		cursor: pointer;
	}
	.Wdate {
   		width: 200px !important;
	}
</style>
<script>
	$(document).ready(function() {
		
		// 获取查询类型
		if($("#searchBusinessType").val()==3){
			$(".userBtn").removeClass("active");
			$(".ipBtn").addClass("active");
			$(".httpReqCfg").addClass("hidden").addClass("disabled");
// 			$(".httpResCfg").removeClass("hidden").removeClass("disabled");
			$(".accountSearch").addClass("hidden").addClass("disabled");
			$(".nasIpSearch").removeClass("hidden").removeClass("disabled");
		}else {
			$(".nasIpSearch").addClass("hidden").addClass("disabled");
			$(".accountSearch").removeClass("hidden").removeClass("disabled");
			$(".httpResCfg").addClass("hidden").addClass("disabled");
			$(".httpReqCfg").removeClass("hidden").removeClass("disabled");
		}
		
		//筛选功能初始化
	$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#searchForm")[0].reset();
		}); 
		
		
		$(".configType").on("click", function() {
			var region = ($(this).find("a").attr("for"));
			if(region=="httpReqCfg"){
				$(".nasIpSearch").addClass("hidden").addClass("disabled");
				$(".httpResCfg").addClass("hidden").addClass("disabled");
				$(".httpReqCfg").removeClass("hidden").removeClass("disabled");
				$(".accountSearch").removeClass("hidden").removeClass("disabled");
			}else{
				$(".accountSearch").addClass("hidden").addClass("disabled");
				$(".httpReqCfg").addClass("hidden").addClass("disabled");
				$(".httpResCfg").removeClass("hidden").removeClass("disabled");
				$(".nasIpSearch").removeClass("hidden").removeClass("disabled");
			}
			
		});
		/*  */
		var aboutWidth = $(".about").width();
		$(".process-timeline").width(aboutWidth);
		var lastRightSpeed = 40; //控制拖动到最右边点是否对准时间刻度线上
		var processtimelineW = $(".process-timeline").width();
		var processrowLength = $(".process-body").find('.process-row').length;
		var processbodyNumber = processrowLength*150;
		$(".process-body").css({"width":processbodyNumber});
		if(processrowLength < 5) 
		{
			$("#draggable").draggable({
	            disabled: true
	        });;
			return;
		}
		$("#processLeft").click(function(){
			var processContentW = $(".process-content").width();
			var processBodyW = $(".process-body").width();
			var processBodyML = parseInt($(".ui-draggable").css("left"));
			if ( (Math.abs(processBodyML)+processtimelineW) >= processBodyW ) {
				return;
			}
			$(".ui-draggable").css({"left":processBodyML-50});
		});
		$("#processRight").click(function(){
			var processBodyML = parseInt($(".ui-draggable").css("left"));
			if (processBodyML == 0) {
				return;
			}
			$(".ui-draggable").css({"left":processBodyML+50});
		});

		$(document).keydown(function(event){
			if (event.keyCode == 39) {
			   $("#processLeft").click();
			}
			if (event.keyCode == 37) {
			   $("#processRight").click();
			}
		});

		$("#draggable").draggable({
			cursor: "move",
			axis: 'x',
			grid: [50, 20],
			stop: function(event, ui){
				var FleftNumber = parseInt($("#draggable").css("left"));
				if (FleftNumber > 0) {
					$("#draggable").animate({"left": 0}, 500);
					return;
				};
				var leftNumber = Math.abs(FleftNumber);
				leftNumber = leftNumber + processtimelineW;
				if (leftNumber > processbodyNumber) 
				{
					leftNumber = processbodyNumber;
					$("#draggable").animate({"left": -(leftNumber-processtimelineW + lastRightSpeed)}, 500);
				}
			}
		});
		
		});
	function searchList(){
		if($(".httpReqCfg").hasClass("hidden")){
			$("#searchBusinessType").val(3);
		}
		if($(".httpResCfg").hasClass("hidden")){
			$("#searchBusinessType").val(2);
		}
		
		$("#searchForm").attr("action","${ctx}/traffic/userBehavior");
		$("#searchForm").submit();
		return false;
	}
	function searchAccountForList(param){
		if($(".httpReqCfg").hasClass("hidden")){
			$("#searchBusinessType").val(3);
		}
		if($(".httpResCfg").hasClass("hidden")){
			$("#searchBusinessType").val(2);
		}
		$("#account").val(param);
		$("#searchForm").attr("action","${ctx}/traffic/userBehavior");
		$("#searchForm").submit();
		return false;
	}
	function searchNasIpForList(param){
		if($(".httpReqCfg").hasClass("hidden")){
			$("#searchBusinessType").val(3);
		}
		if($(".httpResCfg").hasClass("hidden")){
			$("#searchBusinessType").val(2);
		}
		$("#nasIp").val(param);
		$("#searchForm").attr("action","${ctx}/traffic/userBehavior");
		$("#searchForm").submit();
		return false;
	}
	
</script>
</head>
<body>
<div class="page-content">
<%-- 	<div class="theme-panel hidden-xs hidden-sm">
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/traffic/userBehavior'"><spring:message code="refresh"/></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.history.go(-1)"><spring:message code="back"/></button>
	</div> --%>
	<h3 class="page-title">
		<spring:message code="traffic_user_behavior"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
	<div class="row">
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<sys:message content="${message}"/>
					<!-- top -->
					<%--  <div class="row">
					 <div class="col-md-6">
					  <h4 class="ng-binding"><spring:message code="user_behavior_data"/></h4>
					 	 <div class="row">
					 		<div class="col-md-12">
							
							<c:if test="${empty accountList}">
								<div class="none-data"><i class="fa fa-warning font-red-flamingo"></i>&nbsp;&nbsp;<spring:message code="noneData"/></div>
							</c:if>
						</div>
						</div>
					</div>
					
						<div class="col-md-6">
						<h4 class="ng-binding"><spring:message code="ip_behavior_data"/></h4>
						<div class="row">
					 		<div class="col-md-12">
							<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
								<thead>
										<tr>
											<th><spring:message code="ip"/></th>
											<th><spring:message code="visits"/></th>
										</tr>
								</thead>
								<tbody>
		
										<c:forEach items="${nasIpList}" var="data">
											<tr>
												<td>${data.nasIp}</td>
												<td><fmt:formatNumber type="number" value="${data.num}" minFractionDigits="0"></fmt:formatNumber></td>
										 	</tr>
										</c:forEach>
		
								</tbody>
							</table>
							<c:if test="${empty nasIpList}">
								<div class="none-data"><i class="fa fa-warning font-red-flamingo"></i>&nbsp;&nbsp;<spring:message code="noneData"/></div>
							</c:if>			
						</div>
					</div>
						</div>
					</div> --%>
					<!-- top end -->
					
					<div class="row">
					<ul class="nav nav-tabs">
					<li class="userBtn active configType">
						<a data-toggle="tab" for="httpReqCfg" data_dldk="<spring:message code="user"/>">
						<spring:message code="user" /></a></li>
					<li class="ipBtn configType"><a data-toggle="tab" for="httpResCfg" data_dldk="<spring:message code="ip"/>">
						<spring:message code="ip"/></a></li>
					</ul>
					<br>
					<!-- searchform -->
						
					<div class="row" >
						<form:form id="searchForm" modelAttribute="log" action="${ctx}/traffic/userBehavior" method="post" class="form-search">
						<input id="searchBusinessType" name="searchBusinessType" type="hidden" value="${searchBusinessType}"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
				 				  <div class="input-group">
	                                 <div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="begin_date"/></span>
	                             	</div>
	                                 	<input id="searchFoundStartTime" name="searchFoundStartTime" type="text" readonly="readonly" class="form-control Wdate "
											value="${log.searchFoundStartTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
                        		</div>
								<div class="pull-left">	
				 				  <div class="input-group">
	                                 <div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="end_date"/></span>
	                             	</div>
	                                 	<input id="searchFoundEndTime" name="searchFoundEndTime" type="text" readonly="readonly" class="form-control Wdate "
											value="${log.searchFoundEndTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
                        		</div>
                        		<div class="pull-left accountSearch">
									<form:select path="account" class="selectpicker select2 input-medium"  data-live-search="true" data-live-search-placeholder="search">
										<form:option value=""><spring:message code="select"/> <spring:message code="user"/></form:option>
										<c:forEach items="${accountList}" var="acc" >
											<form:option value="${acc.account}">${acc.account}</form:option>
										</c:forEach>
									</form:select>
								</div>
								<div class="pull-left nasIpSearch">
									<form:select path="nasIp" class="selectpicker select2 input-medium"  data-live-search="true" data-live-search-placeholder="search">
										<form:option value=""><spring:message code="select"/> <spring:message code="ip"/></form:option>
										<c:forEach items="${nasIpList}" var="nIp">
											<form:option value="${nIp.nasIp}" >${nIp.nasIp}</form:option>
										</c:forEach>
									</form:select>
								</div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return searchList()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" id="resetBtn" > <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
								</div>
							</div>
							<!-- 搜索内容与操作按钮栏 -->
						</form:form>
					</div>
					
					<br>
					<!-- searchform end-->
					
					<div class="httpReqCfg">
						<div class="row">
						<c:if test="${!empty accountList}">
				  			<div class="col-md-1">
				            <div class=" " style="height: 500px;">
				                <div class="panel-body" style="max-height:460px;overflow-y:auto;overflow-x:hidden;">
<%-- 				                    <h5 class="ng-binding"><spring:message code="user_behavior_data"/></h5> --%>
				                   
				<!--                    		<div class="table-responsive"> -->
										<%-- <table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
											<thead>
												<tr>
													<th><spring:message code="ip"/></th>
													<th><spring:message code="stat_time"/></th>
													<th><spring:message code="visits"/></th>
												</tr>
											</thead>
										<tbody>
				
												<c:forEach items="${userList}" var="us">
													<tr>
														<td>${us.nasIp}</td>
														<td>${us.reportTime}</td>
														<td><fmt:formatNumber type="number" value="${us.num}" minFractionDigits="0"></fmt:formatNumber></td>
												 	</tr>
												</c:forEach>
				
										</tbody>
									</table> --%>
									<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed text-nowrap table-active">
										<thead>
												<tr>
													<th><spring:message code="account"/></th>
												</tr>
										</thead>
										<tbody>
												<c:forEach items="${accountList}" var="data">
													<tr>
														<td onClick="return searchAccountForList('${data.account}')"><a title="${data.account}">${data.account}</a></td>
												 	</tr>
												</c:forEach>
				
										</tbody>
									</table>
									
				<!-- 					</div> -->
				                </div>
				            </div>
				        </div>
				        
						<div class="col-md-11" >
				            <div class=" ">
				                <div class="panel-body" style="height:500px">
				                    <h5 class="ng-binding"><spring:message code="account"/> : ${searchAccount}</h5>
				                    <div id="trend" style="height: 400px; position: relative;" >
					<!-- ===================1============== -->	
											
					<div class="scrollMouse text-center">
						<span id="processLeft" style="display:inline-block"> <i class="fa fa-caret-left"></i> </span>
						<i class="icon icon-mouse"></i>
						<span id="processRight"  style="display:inline-block"> <i class="fa fa-caret-right"></i> </span>
					</div>
					<c:if test="${!empty userList}">
							<div class="about">
								<div class="about-body">
									<div class="about-content process-content">
											<!-- 鼠标点击 滚动 -->
										<div class="process-timeline draggable">
											<div id="draggable">
												<div class="process-body" width="auto">
												<c:forEach items="${userList}" var="us">
													<div class="process-row">
														<div class="process-time">
															<div class="time-con">
																<span class="year" title="<spring:message code="visits"/>: <fmt:formatNumber type='number' value='${us.num}' minFractionDigits='0'/>">${us.reportTime}</span>
															</div>
														</div>
														<div class="process-noyear" title="${us.nasIp}">
															${us.nasIp}
														</div>
													</div>
													</c:forEach>
												</div>
											</div>
										
										</div>
									</div>
								</div>
							</div>
						</c:if>					
															                    		
											<!-- ===================1============== -->					                    		

				                	</div>
				            	</div>
				       	 	</div>
						</div></c:if>
						<c:if test="${empty accountList}">
										<div class="none-data"><i class="fa fa-warning font-red-flamingo"></i>&nbsp;&nbsp;<spring:message code="noneData"/></div>
						</c:if>
					</div>
					</div>
					<!-- IP -->
					<div class="httpResCfg">
						<div class="row">
						<c:if test="${!empty nasIpList}">
				  			<div class="col-md-1">
				           <div class=" " style="height: 500px;">
				                <div class="panel-body" style="max-height:460px;overflow-y:auto;overflow-x:auto;">
<%-- 				                    <h5 class="ng-binding"><spring:message code="ip_behavior_data"/></h5> --%>
				                    <div id="deviceRank" class="drank hm-scroll">
				                   
				<!--                    		<div class="table-responsive"> -->
										<%-- <table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
											<thead>
												<tr>
													<th><spring:message code="account"/></th>
													<th><spring:message code="stat_time"/></th>
													<th><spring:message code="visits"/></th>
												</tr>
											</thead>
										<tbody>
				
												<c:forEach items="${ipList}" var="it">
													<tr>
														<td>${it.account}</td>
														<td>${it.reportTime}</td>
														<td><fmt:formatNumber type="number" value="${it.num}" minFractionDigits="0"></fmt:formatNumber></td>
												 	</tr>
												</c:forEach>
				
										</tbody>
									</table> --%>
									<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed text-nowrap">
										<thead>
												<tr>
													<th><spring:message code="ip"/></th>
												</tr>
										</thead>
										<tbody>
				
												<c:forEach items="${nasIpList}" var="data">
													<tr>
														<td onClick="return searchNasIpForList('${data.nasIp}')"><a title="${data.nasIp}">${data.nasIp}</a></td>
												 	</tr>
												</c:forEach>
				
										</tbody>
									</table>
									
				<!-- 					</div> -->
				                    </div>
				                </div>
				            </div>
				        </div>
				        
						<div class="col-md-11" >
				            <div class=" ">
				                <div class="panel-body" style="height: 500px">
				                    <h5 class="ng-binding"><spring:message code="ip"/> : ${searchNasIp}</h5>
				                    <div id="trend" style="height: 400px; position: relative;" >
					  <!-- ===================2============== -->	
							<div class="scrollMouse text-center">
								<span id="processLeft" style="display:inline-block"> <i class="fa fa-caret-left"></i> </span>
								<i class="icon icon-mouse"></i>
								<span id="processRight"  style="display:inline-block"> <i class="fa fa-caret-right"></i> </span>
							</div>
							<!--  -->
							<c:if test="${!empty ipList}">
									<div class="about">
										<div class="about-body">
											<div class="about-content process-content">
													<!-- 鼠标点击 滚动 -->
												<div class="process-timeline draggable">
													<div id="draggable">
														<div class="process-body" width="auto">
														<c:forEach items="${ipList}" var="it">
															<div class="process-row">
																<div class="process-time">
																	<div class="time-con">
																		<span class="year" title="<spring:message code="visits"/>: <fmt:formatNumber type='number' value='${it.num}' minFractionDigits='0'/>">${it.reportTime}
																		</span>
																		
																	</div>
																</div>
																<div class="process-noyear" title="${it.account}">
																	${it.account}
																</div>
															</div>
															</c:forEach>
														</div>
													</div>
												
												</div>
											</div>
										</div>
									</div>
								</c:if>		
															                    		
											<!-- ===================2============== -->		
					                    </div>
				                	</div>
				            	</div>
				       	 	</div></c:if>	
				       	 	<c:if test="${empty nasIpList}">
										<div class="none-data"><i class="fa fa-warning font-red-flamingo"></i>&nbsp;&nbsp;<spring:message code="noneData"/></div>
							</c:if>	
						</div>
					</div>
					</div>
					<!-- IP end -->
					</div>
				</div>
			</div>
</div>
<!-- BEGIN PAGE LEVEL SCRIPTS -->

<!-- END PAGE LEVEL SCRIPTS -->
</body>
</html>