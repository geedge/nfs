<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
 	$(document).ready(function() {
 		//筛选功能初始化
		filterActionInit();
		$("#isAudit").change(function(){
			page();
		});
		$("#appId").change(function(){
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
			$("#keywords").attr("value",'');
			$("#searchForm")[0].reset();
		});
		//全选及取消
		$("#checkAll").change(function(){
			if($("#checkAll").prop("checked")){
				$("input.i-checks").prop("checked",true);
			}else{
				$("input.i-checks").prop("checked",false);
			}
		});
 	});
	//查询
 	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	//修改配置
	function updateCfg(){
		var cked = $('tbody tr td input.i-checks:checkbox:checked');
		if(cked.val()==1){
			top.$.jBox.tip("<spring:message code='has_approved'/>", "<spring:message code='info'/>");
			return;
		}else if(cked.val()==3){
			top.$.jBox.tip("<spring:message code='cancel_approved'/>", "<spring:message code='info'/>");
			return;
		}
		if(cked.length==1){
			window.location = "${ctx}/cfg/app/updateForm?serviceId="+$("#serviceId").val()+"&action="+
			$("#action").val()+"&cfgId="+cked.attr("id")+"&cfgName="+$("#cfgName").val();
		}else{
			top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
			return;
		}
	}
</script>
</head>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<%-- <button type="button" class="btn btn-default" onclick="location='${ctx}/cfg/app/list?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}'"><spring:message code="refresh"></spring:message></button> --%>
		<c:if test="${audit==0}">
		<shiro:hasPermission name="cfg:add">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/app/form?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}'"><spring:message code="add"></spring:message></button>
		</shiro:hasPermission>
		</c:if>
		
	</div>

	<h3 class="page-title">
		<spring:message code="${cfg.menuNameCode }"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/cfg/app/list" method="post" class="form-search">
							<input id="cfgName" name="cfgName" type="hidden" value="${cfgName}"/>
							<input id="action" name="action" type="hidden" value="${action}"/>
							<input id="serviceId" name="serviceId" type="hidden" value="${serviceId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<c:set var="state"><spring:message code='state'/></c:set>
									<form:select path="isAudit" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all_states"/></form:option>
										<form:option value="0"><spring:message code="created"></spring:message></form:option>
										<form:option value="1"><spring:message code="approved"></spring:message></form:option>
										<form:option value="2"><spring:message code="unapproved"></spring:message></form:option>
									</form:select>
								</div>
								<div class="pull-left">
									<form:select path="appId" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="select"/></form:option>
										<c:forEach items="${protocolList}" var="app">
						               		<form:option value="${app.specServiceId}">
						               			${app.specServiceName}
						               		</form:option>
						               	</c:forEach>													
									</form:select>
								</div>
								<%-- <div class="pull-left">
									<spring:message code="config_describe"></spring:message>
                                    <form:input path="cfgDesc"/>
								</div> --%>
								<%-- <button type="button" class="btn btn-default btn-sm" onclick="return page()">
								<i class="fa fa-edit"></i><spring:message code="search"></spring:message>
								</button> --%>
							<%-- 	<div class="pull-left">
								<c:set var="sortI18n"><spring:message code="sort"/></c:set>
									<select name="orderBy" class="selectpicker select2 input-small">
										<option value="" <c:if test="${empty page.orderBy}">selected</c:if> ><spring:message code="${sortI18n}"/></option>
										<option value="${page.alias}.create_time asc" <c:if test="${fn:contains(page.orderBy,'create_time asc') }">selected</c:if> ><spring:message code="createTime_asc"/></option>
										<option value="${page.alias}.create_time desc" <c:if test="${fn:contains(page.orderBy , 'create_time desc') }">selected</c:if> ><spring:message code="createTime_desc"/></option>
										<option value="${page.alias}.edit_time asc" <c:if test="${fn:contains(page.orderBy , 'edit_time asc') }">selected</c:if> ><spring:message code="editTime_asc"/></option>
										<option value="${page.alias}.edit_time desc" <c:if test="${fn:contains(page.orderBy , 'edit_time desc') }">selected</c:if> ><spring:message code="editTime_desc"/></option>
										<option value="${page.alias}.audit_time asc" <c:if test="${fn:contains(page.orderBy , 'audit_time asc') }">selected</c:if> ><spring:message code="auditTime_asc"/></option>
										<option value="${page.alias}.audit_time desc" <c:if test="${fn:contains(page.orderBy , 'audit_time desc') }">selected</c:if> ><spring:message code="auditTime_desc"/></option>
									</select>
								</div> --%>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" onClick="resetx()"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> 筛选  <i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
								 
								 	<c:if test="${audit==0}">
										<shiro:hasPermission name="cfg:edit">
											 <button type="button" class="btn btn-default" onclick="updateCfg()">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"/> </button>
							      		</shiro:hasPermission>
										<shiro:hasPermission name="cfg:delete">
											<sys:delRow url="${ctx}/cfg/app/deleteCfg?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}" id="contentTable" label="delete"></sys:delRow>
											<%-- <button type="button" class="btn btn-default" onclick="deleteCfg()">
                                            <i class="fa fa-remove"></i> <spring:message code="delete"/> </button> --%>
							      		</shiro:hasPermission>
									</c:if>
									<c:if test="${audit==1}">
										<shiro:hasPermission name="cfg:confirm">
											<div class="btn-group">
	                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                                <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                                <i class="fa fa-angle-down"></i>
	                                            </button>
	                                            <ul class="dropdown-menu pull-right">                                            	
	                                                	<li><sys:delRow url="${ctx}/cfg/app/auditCfg?serviceId=${serviceId}&action=${action}&auditState=1&cfgName=${cfgName}" id="contentTable" label="approved"></sys:delRow></li>
														<li><sys:delRow url="${ctx}/cfg/app/auditCfg?serviceId=${serviceId}&action=${action}&auditState=2&cfgName=${cfgName}" id="contentTable" label="unapproved"></sys:delRow></li>
														<li><sys:delRow url="${ctx}/cfg/app/auditCfg?serviceId=${serviceId}&action=${action}&auditState=3&cfgName=${cfgName}" id="contentTable" label="cancelPass"></sys:delRow></li>
	                                            </ul>
	                                        </div>		
                                        </shiro:hasPermission>				        		
						        		<%-- <shiro:hasPermission name="cfg:confirm">
						        			<button type="button" class="btn btn-default" onclick="auditCfg(1)">
                                            <i class="fa fa-check"></i> <spring:message code="approved"/> </button>
                                            <button type="button" class="btn btn-default" onclick="auditCfg(2)">
                                            <i class="fa fa-remove"></i> <spring:message code="unapproved"/> </button>
                                            <button type="button" class="btn btn-default" onclick="auditCfg(3)">
                                            <i class="fa fa-undo"></i> <spring:message code="cancel"/> </button>
						        		</shiro:hasPermission> --%>
							        </c:if>
                                 	<a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                     </a>
                                 </div>
							</div>
							<!-- /搜索内容与操作按钮栏 -->
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
								
									<div class="col-md-3">
											
										<div class="form-group">
	                                            <label class="control-label"><spring:message code='request_number'/></label>
	                                            <c:set var="select"><spring:message code='select'/></c:set>
	                                            <form:select path="requestId" class="selectpicker form-control" title="${select}" data-live-search="true" data-live-search-placeholder="search">
													<c:forEach items="${requestInfos}" var="requestInfo" >
														<form:option value="${requestInfo.id}"><spring:message code="${requestInfo.requestTitle}"></spring:message></form:option>
													</c:forEach>
												</form:select>
											</div>
                                          
                                        </div>
		                                        
                                        <div class="col-md-3">
									
											<div class="form-group">
	                                            <label class="control-label"><spring:message code='type'/></label>
	                                            <c:set var="select"><spring:message code='select'/></c:set>
	                                            <form:select path="classify" class="selectpicker form-control" title="${select}" data-live-search="true" data-live-search-placeholder="search">
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
	                                            <form:select path="attribute" class="selectpicker form-control" title="${select}" data-live-search="true" data-live-search-placeholder="search">
													<c:forEach items="${xzs}" var="xz" >
														<form:option value="${xz.serviceDictId}"><spring:message code="${xz.itemValue}"></spring:message></form:option>
													</c:forEach>
												</form:select>
											</div>
                                          
                                        </div>
		                                        
                                        <div class="col-md-3">
									
											<div class="form-group">
	                                            <label class="control-label"><spring:message code='label'/></label>
	                                          	<c:set var="select"><spring:message code='select'/></c:set>
	                                            <form:select path="lable" class="selectpicker  form-control" title="${select }" data-live-search="true" data-live-search-placeholder="search">
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
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th><spring:message code="config_describe"/></th>
									<th><spring:message code="protocol_id"/></th>
									<th><spring:message code="protocol_name"/></th>
									<c:if test="${isContainFeaturesCfg}">
									<th><spring:message code="features_config"/></th>
									</c:if>
									<th><spring:message code="block_type"/></th>
									<!-- <th>业务id</th> -->
									<th><spring:message code="letter"/></th>
									<!-- <th>编译id</th> -->
									<th><spring:message code="whether_area_block"/></th>
									<th><spring:message code="classification"/></th>
									<th><spring:message code="attribute"/></th>
									<th><spring:message code="label"/></th>
									<!-- <th>区域生效id</th> -->
									<th><spring:message code="valid_identifier"/></th>
									<th><spring:message code="is_audit"/></th>
									<th><spring:message code="creator"/></th>
									<th><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"/></th>
									<th><spring:message code="audit_time"/></th>
									<th><spring:message code="operation"></spring:message></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
									<tr>
									<td>
										<input type="checkbox" class="i-checks" id="${cfg.cfgId}" value="${cfg.isAudit}">
									</td>
									<td>${cfg.cfgDesc }</td>
									<td>${cfg.appId }</td>
									<td>${cfg.appName }</td>
									
									<c:if test="${isContainFeaturesCfg}">
									<td>
										<a href="${ctx}/cfg/app/showFeaturesCfg?serviceId=${cfg.serviceId}&compileId=${cfg.compileId }&action=${cfg.action}&cfgId=${cfg.cfgId}&cfgName=${cfgName}">
											<spring:message code="detail"></spring:message>
										</a>
									</td>
									</c:if>								
									<td>
										<c:if test="${1 eq cfg.action }"><spring:message code="block"/></c:if>
										<c:if test="${2 eq cfg.action }"><spring:message code="monitor"/></c:if>
										<c:if test="${5 eq cfg.action }"><spring:message code="block_white_list"/></c:if>
										<c:if test="${6 eq cfg.action }"><spring:message code="monitor_white_list"/></c:if>
										<c:if test="${7 eq cfg.action }"><spring:message code="block_monitor_white_list"/></c:if>
										<c:if test="${8 eq cfg.action }"><spring:message code="grey_list"/></c:if>
									</td>
									<%-- <td>${cfg.serviceId }</td> --%>
									<td>${cfg.requestName }</td>
									<%-- <td>${cfg.compileId }</td> --%>
									<td>
										<c:if test="${cfg.isAreaEffective==0}"><spring:message code="no"/></c:if>
										<c:if test="${cfg.isAreaEffective==1}"><spring:message code="yes"/></c:if>
									</td>
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
									<td title="${classify }">
										${fns:abbr(classify,20)}
									</td>
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
									<td title="${attribute }">
										${fns:abbr(attribute,20)}
									</td>
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
									<td title="${lableInfo }">
										${fns:abbr(lableInfo,20)}
									</td>
									<%-- <td>${cfg.areaEffectiveIds }</td> --%>
									<td>
										<c:if test="${cfg.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${cfg.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${cfg.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${cfg.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '3'}"><span indexTable="${cfg.indexTable}" data-placement="right" data-original-title="<spring:message code='letter_cancel_info'/>: " class="label le-ca-fo label-warning tooltips" data-icon="&#xe01e;"> <spring:message code="cancel_approved"/></span></c:when>
										</c:choose>
									</td>
									<td>${cfg.creatorName }</td>
									<td><fmt:formatDate value="${cfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.editorName }</td>
									<td><fmt:formatDate value="${cfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.auditorName }</td>
									<td><fmt:formatDate value="${cfg.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>
										<div class="btn-group btn-xs">
									      <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"></spring:message><span class="caret"></span></a>
									      <ul class="dropdown-menu btn-xs">
									      <c:if test="${audit==0}">
									      	<c:choose>
									      		<c:when test="${cfg.isAudit eq '2'}">
									      			<shiro:hasPermission name="cfg:edit">
									      			<li><a href="${ctx}/cfg/app/updateForm?serviceId=${cfg.serviceId}&action=${cfg.action}&cfgId=${cfg.cfgId}&cfgName=${cfgName}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      			</shiro:hasPermission>
									      		</c:when>
									      		<c:when test="${cfg.isAudit eq '0'}">
									      			<shiro:hasPermission name="cfg:edit">
									      			<li><a href="${ctx}/cfg/app/updateForm?serviceId=${cfg.serviceId}&action=${cfg.action}&compileId=${cfg.compileId}&cfgId=${cfg.cfgId}&cfgName=${cfgName}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      			</shiro:hasPermission>
									      			<shiro:hasPermission name="cfg:delete">
									      			<li><a href="${ctx}/cfg/app/deleteCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&compileId=${cfg.compileId}&ids=${cfg.cfgId}&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="delete"></spring:message></a></li>
									      			</shiro:hasPermission>
									      		</c:when>
									      	</c:choose>
									      </c:if>
									      <c:if test="${audit==1}">
									      	<c:choose>
									        	<c:when test="${cfg.isAudit eq '1'}">
									        		<shiro:hasPermission name="cfg:confirm">
											        <li><a href="${ctx}/cfg/app/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&compileId=${cfg.compileId}&ids=${cfg.cfgId}&auditState=3&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="cancel"></spring:message></a></li>
											        </shiro:hasPermission>
									        	</c:when>
									        	<c:when test="${cfg.isAudit eq '0'}">
									        		<shiro:hasPermission name="cfg:confirm">
									        		<li><a href="${ctx}/cfg/app/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&compileId=${cfg.compileId}&ids=${cfg.cfgId}&auditState=1&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="approved"></spring:message></a></li>
									        		</shiro:hasPermission>
									        		<shiro:hasPermission name="cfg:confirm">
									        		<li><a href="${ctx}/cfg/app/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&compileId=${cfg.compileId}&ids=${cfg.cfgId}&auditState=2&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="unapproved"></spring:message></a></li>
									        		</shiro:hasPermission>
									        	</c:when>
								        	</c:choose>
									      </c:if>
									      </ul>
									    </div>
									</td>
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
<style>
	.input-medium {
    width: 200px !important;
}
	.Wdate {
    border: #c2cad8 1px solid;
    height: 26px;
}

.dropdown-menu {
    min-width: 50px;
}

</style>
</body>
</html>