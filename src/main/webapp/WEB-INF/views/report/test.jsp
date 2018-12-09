<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${bean.cfgName}"></spring:message></title>
<script>
	$(document).ready(function() {
		//筛选功能初始化
		filterActionInit();
		//reset
		$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#searchForm")[0].reset();
		});
	});
</script>
</head>
<body>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
			<div class="btn btn-primary">
			<spring:message code="阻断数据量"></spring:message>1000000
			</div>
			<div class="btn btn-default">
			<spring:message code="监测数据量"></spring:message>1000000
			</div>
	</div>
	<h3 class="page-title">
		<spring:message code="${bean.cfgName}"></spring:message>
		<small><spring:message code="report_list"/></small>
	</h3>
	<h5 class="page-header"></h5>
	<div class="row">
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="bean" action="${ctx}/report/test/list" method="post" class="form-search">
							<input id="functionId" name="functionId" type="hidden" value="${bean.functionId}"/>
							<input name="cfgName" type="hidden" value="${bean.cfgName}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${bean.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<form:select path="action" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="action"/></form:option>
										<c:forEach items="${fns:getDictList('SERVICE_ACTION')}" var="action" >
											<form:option value="${action.itemCode}" ><spring:message code="${action.itemValue}"/></form:option>
										</c:forEach>
									</form:select>
								</div>
								<div class="pull-left">
									<div class="input-group">
	                                	<div class="input-group-btn">
											<span class="form-control" ><spring:message code="begin_date"/></span>
	                                  	</div>
	                            		<input name="searchReportStartTime" type="text" readonly="readonly" maxlength="20" class="form-control Wdate input-medium"
											value="${bean.searchReportStartTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
	                         		</div> 
                            	</div>
								<div class="pull-left">
									<div class="input-group">
	                                   	<div class="input-group-btn">
											<span class="form-control" ><spring:message code="end_date"/></span>
	                                  	</div>
	                                    <input name="searchReportEndTime" type="text" readonly="readonly" maxlength="20" class="form-control Wdate input-medium"
											value="${cfg.searchReportEndTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
	                         		</div> 
	                            </div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" id="resetBtn" > <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <sys:delRow url="${ctx}/report/test/export?functionId=${cfg.functionId}" id="contentTable" label="export" maxRow="${page.maxExportSize}"></sys:delRow>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/><i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
                                 	<a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                     </a>
                                 </div>
							</div>
							<!-- 搜索内容与操作按钮栏 -->
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
							</div>
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<sys:message content="${message}"/>
					<div class="table-responsive">
						<br>
						<ul class="nav nav-tabs nav-justified">
							<!-- <li><a data-toggle="tab" href="#serviceType">业务类型</a></li> -->
							<li class="active"><a data-toggle="tab" href="#label">标签</a></li>
							<li><a data-toggle="tab" href="#attrType">性质</a></li>
							<li><a data-toggle="tab" href="#lwhh">来文函号</a></li>
							<li><a data-toggle="tab" href="#srcIp">境内源IP</a></li>
							<li><a data-toggle="tab" href="#destIp">各国家目的IP</a></li>
							<li><a data-toggle="tab" href="#entranceId">运营商局点</a></li>
						</ul>
						<div class="tab-content">
							<%@include file="/WEB-INF/views/report/label.jsp" %>
							<%@include file="/WEB-INF/views/report/attrType.jsp" %>
							<%@include file="/WEB-INF/views/report/lwhh.jsp" %>
							<%@include file="/WEB-INF/views/report/srcIp.jsp" %>
							<%@include file="/WEB-INF/views/report/destIp.jsp" %>
							<%@include file="/WEB-INF/views/report/entranceId.jsp" %>
						</div>
					</div>
					
				</div>
			</div>
</div>
</body>
</html>