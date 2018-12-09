<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
	$(document).ready(function() {
		//搜索框提示语初始化
		if("${cfg.srcIpAddress}"){
			$("#intype").val("${cfg.srcIpAddress}");
		} else if("${cfg.destIpAddress}"){
			$("#intype").val("${cfg.destIpAddress}");
		} else if("${cfg.srcPort}"){
			$("#intype").val("${cfg.srcPort}");
		} else if("${cfg.destPort}"){
			$("#intype").val("${cfg.destPort}");
		} else if("${cfg.cfgDesc}"){
			$("#intype").val("${cfg.cfgDesc}");
		} else{
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
		}
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
		});
		//筛选功能初始化
		filterActionInit();
		$("#isAudit").change(function(){
			page();
		});
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
		
		if($("#exportType").val() != null && $("#exportType").val() != ""){
			 if($("#intype").val() != null && $("#intype").val() != ""){
				$("#exportValue").val($("#intype").val());
		  } 
		}
	});
</script>
</head>
<body>
<div class="page-content">
	<!-- 模板导入，start -->
	 <%@include file="/WEB-INF/include/excel/importIp.jsp" %>
	<!-- 模板导入，end -->
	<div class="theme-panel hidden-xs hidden-sm">
		<shiro:hasPermission name="${requiresPermissionPrefix.concat(':config')}">
			<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}${urlPrefix}/form?functionId=${cfg.functionId}&cfgName=${cfgName}'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
			<!-- 导入功能按钮 -->
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle green" data-toggle="dropdown">
				    <i class="fa fa-wrench"></i> <spring:message code="import"></spring:message>
				    <i class="fa fa-angle-down"></i>
				</button>
				<ul class="dropdown-menu pull-right">
					<c:forEach items="${serviceList}" var="service" varStatus="status">
					<c:if test="${fn:containsIgnoreCase(service.actionCode,'monit')}">
						<c:set var="monitCode" value="monit"/>
					</c:if>
					<c:if test="${fn:containsIgnoreCase(service.actionCode,'reject')}">
						<c:set var="rejectCode" value="reject"/>
					</c:if>
					<c:if test="${fn:containsIgnoreCase(service.actionCode,'drop')}">
						<c:set var="dropCode" value="monit"/>
					</c:if>
					<c:if test="${fn:containsIgnoreCase(service.actionCode,'ratelimit')}">
						<c:set var="ratelimitCode" value="monit"/>
					</c:if>
					<c:if test="${fn:containsIgnoreCase(service.actionCode,'loop')}">
						<c:set var="loopCode" value="monit"/>
						<c:if test="${service.action==96}"><c:set var="irCode" value="monit"/></c:if>
					</c:if>
					</c:forEach>
					<c:forEach items="${regionList}" var="region" varStatus="status">
						<li><a href="javascript:;" onClick="toImport('${region.configRegionCode}','${region.configRegionValue}');">
		  				<i class="fa fa-upload"></i>
		  				<c:choose>
		  					<c:when test="${monitCode!=null and rejectCode!=null and region.isMaat==1}">
		  						<spring:message code="reject_or_monit_ip"/>
		  					</c:when>
		  					<c:when test="${monitCode==null and rejectCode!=null and region.isMaat==1}">
		  						<spring:message code="reject_ip"/>
		  					</c:when>
		  					<c:when test="${monitCode!=null and rejectCode==null and region.isMaat==1}">
		  						<spring:message code="monit_ip"/>
		  					</c:when>
		  					<c:when test="${irCode!=null and region.isMaat==1}">
		  						<spring:message code="ir_ip"/>
		  					</c:when>
		  					<c:when test="${ratelimitCode!=null and region.isMaat==1}">
		  						<spring:message code="ratelimit_ip"/>
		  					</c:when>
		  					<c:when test="${dropCode!=null and loopCode!=null and region.isMaat==2}">
		  						<spring:message code="drop_or_loop_ip"/>
		  					</c:when>
		  					<c:when test="${dropCode!=null and loopCode==null and region.isMaat==2}">
		  						<spring:message code="drop_ip"/>
		  					</c:when>
		  					<c:when test="${loopCode!=null and dropCode==null and region.isMaat==2}">
		  						<spring:message code="loop_ip"/>
		  					</c:when>
		  					<c:otherwise>
		  						<spring:message code="${region.configRegionValue}"/>
		  					</c:otherwise>
		  				</c:choose>
		  				
		  				</a>
		  				</li>
					</c:forEach>   
				</ul>
			</div> 
		</shiro:hasPermission>
	</div>
	<h3 class="page-title">
		<spring:message code="${cfg.menuNameCode }"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}${urlPrefix}/list" method="post" class="form-search">
							<input id="functionId" name="functionId" type="hidden" value="${cfg.functionId}"/>
							<input name="cfgName" type="hidden" value="${cfgName}"/>
							<input id="exportType" type="hidden" value="${cfg.seltype}"/>
							<input id="exportValue" type="hidden" value=""/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<form:select path="isAudit" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all_states"/></form:option>
										<c:forEach items="${fns:getDictList('AUDIT_STATUS')}" var="auditC">
									       <c:if test="${auditC.itemCode ne 3}">
									       	<form:option value="${auditC.itemCode}">
									       		<spring:message code="${auditC.itemValue}"/>
									       	</form:option>
									       </c:if>
							            </c:forEach>
									</form:select>
								</div>
								<div class="pull-left">	
								
										<div class="input-group">
                                            <div class="input-group-btn">
                                            
                                           		<form:select path="seltype" class="selectpicker select2 input-small" >
													<form:option value="srcIpAddress"><spring:message code="client_ip"></spring:message></form:option>
													<form:option value="destIpAddress"><spring:message code="server_ip"></spring:message></form:option>
													<form:option value="srcPort"><spring:message code="client_port"></spring:message></form:option>
													<form:option value="destPort"><spring:message code="server_port"></spring:message></form:option>
													<form:option value="cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
													<form:option value="action"><spring:message code="block_type"></spring:message></form:option>
													<form:option value="isValid"><spring:message code="valid_identifier"></spring:message></form:option>
											</form:select>
									
                                         </div>
                                         
                                         <input id="intype" class="form-control input-medium" type="text" value="">
                                  		 <div class="input-group-btn">
	                                  		 <form:select id="actionSelect" path="action" class="selectpicker select2 input-small" >
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${serviceList}" var="service">
														<form:option value="${service.action }"><spring:message code="action_${service.actionCode }"/></form:option>
													</c:forEach>
											 </form:select>
											 <form:select path="isValid" class="selectpicker select2 input-small" >
													<form:option value=""><spring:message code="select"/></form:option>
													<form:option value="1"><spring:message code="yes"/></form:option>
													<form:option value="0"><spring:message code="no"/></form:option>
											 </form:select>
										 </div>
                                        </div> 
	                               </div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" id="resetBtn" > <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/><i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
								 		<shiro:hasPermission name="${requiresPermissionPrefix.concat(':config')}">
									 		<sys:delRow url="${ctx}${urlPrefix}/form?functionId=${cfg.functionId}&cfgName=${cfgName}" id="contentTable" label="update"></sys:delRow>
	                                        <sys:delRow url="${ctx}${urlPrefix}/delete?functionId=${cfg.functionId}&cfgName=${cfgName}" id="contentTable" label="delete"></sys:delRow>
                                        	<sys:delRow url="${ctx}${urlPrefix}/export?functionId=${cfg.functionId}" id="contentTable" label="export" maxRow="${page.maxExportSize}"></sys:delRow>
                                        </shiro:hasPermission>
                                   		<shiro:hasPermission name="${requiresPermissionPrefix.concat(':confirm')}">
	                                        <div class="btn-group">
	                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                                <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                                <i class="fa fa-angle-down"></i>
	                                            </button>
	                                            <ul class="dropdown-menu pull-right">
	                                                <li><sys:delRow url="${ctx}${urlPrefix}/audit?isAudit=1&isValid=1&functionId=${cfg.functionId}&cfgName=${cfgName}" id="contentTable" label="approved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}${urlPrefix}/audit?isAudit=2&isValid=0&functionId=${cfg.functionId}&cfgName=${cfgName}" id="contentTable" label="unapproved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}${urlPrefix}/audit?isAudit=3&isValid=0&functionId=${cfg.functionId}&cfgName=${cfgName}" id="contentTable" label="cancelPass"></sys:delRow></li>            
	                                            </ul>
	                                        </div>
                                        </shiro:hasPermission>
                        			
                                 	<a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                     </a>
                                 </div>
							</div>
							<!-- 搜索内容与操作按钮栏 -->
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
								
									<div class="col-md-3">
											
										<div class="form-group">
	                                            <label class="control-label"><spring:message code='letter'/></label>
	                                            <c:set var="select"><spring:message code='select'/></c:set>
	                                            <form:select path="requestId" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${requestInfos}" var="requestInfo" >
														<form:option value="${requestInfo.id}"><spring:message code="${requestInfo.requestTitle}"></spring:message></form:option>
													</c:forEach>
												</form:select>
											</div>
                                          
                                        </div>
		                                        
                                        <div class="col-md-3">
									
											<div class="form-group">
	                                            <label class="control-label"><spring:message code='classification'/></label>
	                                            <form:select path="classify" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
												<form:option value=""><spring:message code="select"/></form:option>
												<c:forEach items="${fls}" var="fl" >
													<form:option value="${fl.serviceDictId}"><spring:message code="${fl.itemValue}"></spring:message></form:option>
												</c:forEach>
											</form:select>
											</div>
                                          
                                        </div>
					
										<div class="col-md-3">
									
											<div class="form-group">
	                                            <label class="control-label"><spring:message code='attribute'/></label>
	                                            <c:set var="select"><spring:message code='select'/></c:set>
	                                            <form:select path="attribute" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${xzs}" var="xz" >
														<form:option value="${xz.serviceDictId}"><spring:message code="${xz.itemValue}"></spring:message></form:option>
													</c:forEach>
												</form:select>
											</div>
                                          
                                        </div>
		                                        
                                        <div class="col-md-3">
									
											<div class="form-group">
	                                            <label class="control-label"><spring:message code='label'/></label>
	                                            <form:select path="lable" class="selectpicker  form-control" data-live-search="true" data-live-search-placeholder="search">
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${lables}" var="lable" >
														<form:option value="${lable.serviceDictId}"><spring:message code="${lable.itemValue}"></spring:message></form:option>
													</c:forEach>
												</form:select>
											</div>
                                          
                                        </div>
									
								</div>
								<div class="row">
									
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="config_time"/>：</label>
											<input name="search_create_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value='${cfg.search_create_time_start}' pattern='yyyy-MM-dd HH:mm:ss'/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_create_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_create_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="edit_time"/>：</label>
											<input name="search_edit_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_edit_time_start}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_edit_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_edit_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="audit_time"/>：</label>
											<input name="search_audit_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_audit_time_start}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_audit_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_audit_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
								</div>
							</div>
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<sys:message content="${message}"/>
					<div class="table-responsive">
						
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<c:forEach items="${serviceList}" var="service">
								<c:if test="${service.action==64}">
									<c:set var="hasRatelimit" value="1"/>
								</c:if>
							</c:forEach>
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<%-- <th><spring:message code="seq"/></th> --%>
									<th class="sort-column r.compile_id" exportColumn="cfg_id" ><spring:message code="cfg_id"/></th>
									<th class="sort-column r.cfg_desc" exportColumn="config_describe" ><spring:message code="config_describe"/></th>
									<th exportColumn="ip_type"><spring:message code="ip_type"/></th>
									<th exportColumn="ip_pattern"><spring:message code="ip_pattern"/></th>
									<th exportColumn="port_pattern"><spring:message code="port_pattern"/></th>
									<th exportColumn="client_ip"><spring:message code="IP"/></th>
									<th exportColumn="client_port"><spring:message code="port"/></th>
									<th exportColumn="block_type" class="sort-column r.action"><spring:message code="block_type"/></th>
									<th exportColumn="direction"><spring:message code="direction"/></th>
									<th exportColumn="protocol"><spring:message code="protocol"/></th>
									<th exportColumn="group_name"><spring:message code="group"/></th>
									<th exportColumn="ir_type"><spring:message code="ir_type"/></th>
									<c:if test="${hasRatelimit==1}">
									<th exportColumn="ratelimit"><spring:message code="ratelimit"/></th>
									</c:if>
									<th exportColumn="letter"><spring:message code="letter"/></th>
									<th exportColumn="classification"><spring:message code="classification"/></th>
									<th exportColumn="attribute"><spring:message code="attribute"/></th>
									<th exportColumn="label"><spring:message code="label"/></th>
									<th class="sort-column r.is_valid" exportColumn="valid_identifier"><spring:message code="valid_identifier"/></th>
									<th exportColumn="is_audit"><spring:message code="is_audit"/></th>
									<%-- <th exportColumn="log_total"><spring:message code="log_total"/></th> --%>
									<th exportColumn="creator"><spring:message code="creator"/></th>
									<th exportColumn="config_time" class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th exportColumn="editor"><spring:message code="editor"/></th>
									<th exportColumn="edit_time" class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
									<th exportColumn="auditor"><spring:message code="auditor"/></th>
									<th exportColumn="audit_time" class="sort-column r.audit_time"><spring:message code="audit_time"/></th>
									<%-- <th><spring:message code="operation"></spring:message></th> --%>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
									<tr pId="${status.index}">
									<td><input type="checkbox" class="i-checks" id="${cfg.cfgId}" compileId="${cfg.compileId}" value="${cfg.isAudit}"></td>
									<%-- <td>${status.index+1 }</td> --%>
									<td>${cfg.compileId }</td>
									<td class="sort-column r.cfg_desc">${cfg.cfgDesc }</td>
									<td><c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipType">
										<c:if test="${ipType.itemCode==cfg.ipType}"><spring:message code="${ipType.itemValue}"/></c:if>
					                </c:forEach></td>
									<td><c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPattern">
										<c:if test="${ipPattern.itemCode==cfg.ipPattern}"><spring:message code="${ipPattern.itemValue}"/></c:if>
					                </c:forEach></td>
					                <td><c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPattern">
										<c:if test="${portPattern.itemCode==cfg.portPattern}"><spring:message code="${portPattern.itemValue}"/></c:if>
					                </c:forEach></td>
									<td title="${cfg.srcIpAddress }">${fns:abbr(cfg.srcIpAddress, 42)}</td>
									<td>${cfg.srcPort }</td>
									<td>
										<c:forEach items="${fns:getDictList('SERVICE_ACTION')}" var="serviceC">
									       <c:if test="${cfg.action eq serviceC.itemCode}"><spring:message code="${serviceC.itemValue }"/></c:if>
							            </c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
											<c:if test="${cfg.direction eq directionC.itemCode}"><spring:message code="${directionC.itemValue }"/></c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
											<c:if test="${cfg.protocol eq protocolC.itemCode}"><spring:message code="${protocolC.itemValue }"/></c:if>
										</c:forEach>
									</td>
									<td>${cfg.groupName }</td>
									<td>
										<c:forEach items="${fns:getDictList('IR_TYPE')}" var="irTypeC">
											<c:if test="${cfg.irType==irTypeC.itemCode}"><spring:message code="${irTypeC.itemValue}"/></c:if>
							             </c:forEach>
									</td>
									<c:if test="${hasRatelimit==1}">
									<td>
									<c:if test="${cfg.ratelimit eq null and cfg.action==64}">0%</c:if>
									<c:if test="${cfg.ratelimit ne null}">${cfg.ratelimit}%</c:if>
									</td>
									</c:if>
									<td>${cfg.requestName }</td>
									<td>
									<c:set var="classify"></c:set>
										<c:forEach items="${fn:split(cfg.classify,',')}" var="classifyId" varStatus="status">
											<c:forEach items="${fls}" var="fl">
												<c:if test="${classifyId eq fn:trim(fl.serviceDictId)}">
												<c:if test="${status.index+1 eq 1}">
													<c:set var="classify" value="${fl.itemValue}"></c:set>
												</c:if>
												<c:if test="${status.index+1 ne 1}">
													<c:set var="classify" value="${classify},${fl.itemValue}"></c:set>
												</c:if>
												</c:if>
											</c:forEach>
										</c:forEach>
										<a href="javascript:;" data-original-title="${classify}"
										 class="tooltips" data-flag="false" data-html="true" data-placement="top">
											${fns:abbr(classify,20)}
										</a>
									</td>
									<td>
									<c:set var="attribute"></c:set>
										<c:forEach items="${fn:split(cfg.attribute,',')}" var="attributeId" varStatus="status">
											<c:forEach items="${xzs}" var="xz">
												<c:if test="${attributeId eq fn:trim(xz.serviceDictId)}">
												<c:if test="${status.index+1 eq 1}">
													<c:set var="attribute" value="${xz.itemValue}"></c:set>
												</c:if>
												<c:if test="${status.index+1 ne 1}">
													<c:set var="attribute" value="${attribute},${xz.itemValue}"></c:set>
												</c:if>
												</c:if>
											</c:forEach>
										</c:forEach>
										<a href="javascript:;" data-original-title="${attribute}"
										 class="tooltips" data-flag="false" data-html="true" data-placement="top">
											${fns:abbr(attribute,20)}
										</a>
									</td>
									<td>
									<c:set var="lableInfo"></c:set>
										<c:forEach items="${fn:split(cfg.lable,',')}" var="lableId" varStatus="status">
											<c:forEach items="${lables}" var="lable">
												<c:if test="${lableId eq fn:trim(lable.serviceDictId)}">
												<c:if test="${status.index+1 eq 1}">
													<c:set var="lableInfo" value="${lable.itemValue}"></c:set>
												</c:if>
												<c:if test="${status.index+1 ne 1}">
													<c:set var="lableInfo" value="${lableInfo},${lable.itemValue}"></c:set>
												</c:if>
												</c:if>
											</c:forEach>
										</c:forEach>
										<a href="javascript:;" data-original-title="${lableInfo}"
										 class="tooltips" data-flag="false" data-html="true" data-placement="top">
											${fns:abbr(lableInfo,20)}
										</a>
									</td>
									<%-- <td>${cfg.areaEffectiveIds }</td> --%>
									<td>
										<c:if test="${cfg.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${cfg.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${cfg.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('AUDIT_STATUS')}" var="auditC">
									       <c:if test="${cfg.isAudit eq auditC.itemCode and auditC.itemValue eq 'created'}"><span data-audit="created" class="label label-danger"><spring:message code="created"></spring:message></span></c:if>
									       <c:if test="${cfg.isAudit eq auditC.itemCode and auditC.itemValue eq 'approved'}"><span data-audit="approved" class="label label-success"><spring:message code="approved"></spring:message></span></c:if>
									       <c:if test="${cfg.isAudit eq auditC.itemCode and auditC.itemValue eq 'unapproved'}"><span data-audit="unapproved" class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:if>
									       <c:if test="${cfg.isAudit eq auditC.itemCode and auditC.itemValue eq 'cancel_approved'}"><span data-audit="cancel" class="label label-warning"><spring:message code="cancel_approved"></spring:message></span></c:if>
							            </c:forEach>
									</td>
									<%-- <%-- <td audit="${cfg.isAudit}" functionId="${cfg.functionId}" compileId="${cfg.compileId}" action="${cfg.action}" serviceId="${cfg.serviceId}"><div class="loading-total"></div></td> --%> --%>
									<td>${cfg.creatorName }</td>
									<td><fmt:formatDate value="${cfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.editorName }</td>
									<td><fmt:formatDate value="${cfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.auditorName }</td>
									<td><fmt:formatDate value="${cfg.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="page">${page}</div>
					</div>
					</div>
				</div>
			</div>
</div>
</body>
</html>