<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
 	$(document).ready(function() {
 		//搜索框提示语初始化
		if("${cfg.stringCfg.cfgKeywords}"){
			$("#intype").val("${cfg.stringCfg.cfgKeywords}");
		}else if("${cfg.stringCfg.cfgDesc}"){
			$("#intype").val("${cfg.stringCfg.cfgDesc}");
		}else if("${cfg.complexCfg.cfgDesc}"){
			$("#intype").val("${cfg.complexCfg.cfgDesc}");
		}else if("${cfg.complexCfg.district}"){
			$("#intype").val("${cfg.complexCfg.district}");
		}else if("${cfg.complexCfg.keywords}"){
			$("#intype").val("${cfg.complexCfg.keywords}");
		}else{
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
				if($(this).prop("name")!="mainTable"){
					$(this).selectpicker('val',$(this).find('option:first').val());
					$(this).find("option").attr("selected",false);
					$(this).find("option:first").attr("selected",true);
				}
			});
			$(".Wdate").attr("value",'');
			$("#keywords").attr("value",'');
			$("#searchForm")[0].reset();
		});
		$("select[name='mainTable']").on("change",function(){
			var val=$(this).val();
			var type=$(this).find("option:selected").data("type");
			$("#mainTableType").val(type);
			var desc=$(this).find("option:selected").data("desc");
			$("#mainTableDesc").val(desc);
			return page();
		});
 	});
	//查询
 	function page(n,s){
 		$("#intype").attr("name",$("#seltype").val());
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
 	var edit=function(url){
 		var cked = $('tbody tr td input.i-checks:checkbox:checked');
 		if(cked.val()==1){
 			top.$.jBox.tip("<spring:message code='has_approved'/>", "<spring:message code='info'/>");
 			return;
 		}
 		if(cked.length==1){
 			window.location = url+"&compileId="+cked.attr("id");
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
		<c:if test="${audit==0}">
		<shiro:hasPermission name="cfg:multiple:add">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/multiple/form?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&mainTable=${mainTable}'"><spring:message code="add"></spring:message></button>
		</shiro:hasPermission>
		</c:if>
	</div>

	<h3 class="page-title">
		<spring:message code="${cfgName}"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/cfg/multiple/list" method="post" class="form-search">
							<input id="mainTableType" name="mainTableType" type="hidden" value="${mainTableType}"/>
							<input id="mainTableDesc" name="mainTableDesc" type="hidden" value="${mainTableDesc}"/>
							<input id="cfgType" name="cfgType" type="hidden" value="${cfgType}"/>
							<input id="cfgName" name="cfgName" type="hidden" value="${cfgName}"/>
							<input id="action" name="action" type="hidden" value="${action}"/>
							<input id="serviceId" name="serviceId" type="hidden" value="${serviceId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<form:select path="isAudit" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="state"/></form:option>
										<form:option value="0"><spring:message code="created"></spring:message></form:option>
										<form:option value="1"><spring:message code="approved"></spring:message></form:option>
										<form:option value="2"><spring:message code="unapproved"></spring:message></form:option>
									</form:select>
								</div>
								<div class="pull-left">
									<form:select path="mainTable" class="selectpicker select2 input-small" >
                                          <c:forEach items="${tableInfos}" var="tableInfo">
                                          		<form:option value="${tableInfo.tableName}" data-type="${tableInfo.tableType}" data-desc="${tableInfo.tableDesc}"><spring:message code="${tableInfo.tableDesc}"></spring:message></form:option>
                                          </c:forEach>
                                    </form:select>
								</div>
								<div class="pull-left">
									<div class="input-group">
                                            <div class="input-group-btn">
                                           		<form:select path="seltype" class="selectpicker select2 input-small" >
													<c:if test="${mainTableType eq '1'}">
														<form:option value="ipCfg.cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
														<form:option value="ipCfg.srcIp"><spring:message code="client_ip"></spring:message></form:option>
														<form:option value="ipCfg.dstIp"><spring:message code="server_ip"></spring:message></form:option>
													</c:if>
													<c:if test="${mainTableType eq '2'}">
														<form:option value="stringCfg.cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
														<form:option value="stringCfg.cfgKeywords"><spring:message code="key_word"></spring:message></form:option>
													</c:if>
													<c:if test="${mainTableType eq '4'}">
														<form:option value="complexCfg.cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
														<form:option value="complexCfg.district"><spring:message code="match_area"></spring:message></form:option>
														<form:option value="complexCfg.keywords"><spring:message code="key_word"></spring:message></form:option>
													</c:if>
													
												<form:option value="action"><spring:message code="block_type"></spring:message></form:option>
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
										 </div>
                                        </div> 
								</div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" id="resetBtn" > <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/>  <i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
								 	<c:if test="${audit==0}">
								 		<shiro:hasPermission name="cfg:string:edit">
                                        <button type="button" class="btn btn-default" onClick="edit('${ctx}/cfg/multiple/updateForm?serviceId=${serviceId}&action=${action}&mainTable=${mainTable}&cfgName=${cfgName}')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"></spring:message></button>
                                         </shiro:hasPermission>
                                         <shiro:hasPermission name="cfg:multiple:delete">
                                            <sys:delRow url="${ctx}/cfg/multiple/delete?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&mainTable=${mainTable}" id="contentTable" label="delete"></sys:delRow>
                                      	 </shiro:hasPermission>
                                      <!--   <button type="button" class="btn btn-default">
                                            <i class="fa fa-download"></i> 导出</button> -->
                                    </c:if>
                                    <c:if test="${audit==1}">
                                    		<shiro:hasPermission name="cfg:multiple:confirm">
	                                        <div class="btn-group">
	                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                                <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                                <i class="fa fa-angle-down"></i>
	                                            </button>
	                                            <ul class="dropdown-menu pull-right">
	                                                <li><sys:delRow url="${ctx}/cfg/multiple/audit?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&mainTable=${mainTable}&isAudit=1" id="contentTable" label="approved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/multiple/audit?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&mainTable=${mainTable}&isAudit=2" id="contentTable" label="unapproved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/multiple/audit?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&mainTable=${mainTable}&isAudit=3" id="contentTable" label="cancelPass"></sys:delRow></li>            
	                                            </ul>
	                                        </div>
	                                        </shiro:hasPermission>
                                    </c:if>
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
									<c:choose>
										<c:when test="${action==5}">
										</c:when>
										<c:when test="${action==8}">
										</c:when>
										<c:otherwise>
											<div class="col-md-3">
												<div class="form-group">
		                                            <label class="control-label"><spring:message code='request_number'/></label>
		                                            <form:select path="requestId" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
														<form:option value=""><spring:message code='select'/></form:option>
														<c:forEach items="${requestInfos}" var="requestInfo" >
															<form:option value="${requestInfo.id}"><spring:message code="${requestInfo.requestTitle}"></spring:message></form:option>
														</c:forEach>
													</form:select>
												</div>
	                                        </div>
	                                        <div class="col-md-3">
												<div class="form-group">
		                                            <label class="control-label"><spring:message code='type'/></label>
		                                            <form:select path="classify" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
													<form:option value=""><spring:message code='select'/></form:option>
													<c:forEach items="${fls}" var="fl" >
														<form:option value="${fl.serviceDictId}"><spring:message code="${fl.itemValue}"></spring:message></form:option>
													</c:forEach>
												</form:select>
												</div>
	                                        </div>
											<div class="col-md-3">
												<div class="form-group">
		                                            <label class="control-label"><spring:message code='attribute'/></label>
		                                            <form:select path="attribute" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
														<form:option value=""><spring:message code='select'/></form:option>
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
														<form:option value=""><spring:message code='select'/></form:option>
														<c:forEach items="${lables}" var="lable" >
															<form:option value="${lable.serviceDictId}"><spring:message code="${lable.itemValue}"></spring:message></form:option>
														</c:forEach>
													</form:select>
												</div>
	                                          
	                                        </div>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="row">
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
							</div>
							
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<%-- <th><spring:message code="seq"/></th> --%>
									<%--遍历主表属性 --%>
									<c:choose>
										<%--增强字符串 --%>
										<c:when test="${mainTableType eq '4'}">
											<th><spring:message code="${mainTableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="match_area"/></th>
											<th><spring:message code="key_word"/></th>
										</c:when>
										<%--数值 --%>
										<c:when test="${mainTableType eq '3'}">
											<th><spring:message code="${mainTableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="数值下界"/></th>
											<th><spring:message code="数值上界"/></th>
										</c:when>
										<%--字符串 --%>
										<c:when test="${mainTableType eq '2'}">
											<th><spring:message code="${mainTableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="key_word"/></th>
										</c:when>
										<%--IP --%>
										<c:when test="${mainTableType eq '1'}">
											<th><spring:message code="${mainTableDesc}"/><spring:message code="config_describe"/></th>
											<th>IP<spring:message code="type"/></th>
											<th><spring:message code="client_ip"/></th>
											<th><spring:message code="client_address_mask"/></th>
											<th><spring:message code="client_port"/></th>
											<th><spring:message code="client_port_mask"/></th>
											<th><spring:message code="server_ip"/></th>
											<th><spring:message code="server_address_mask"/></th>
											<th><spring:message code="server_port"/></th>
											<th><spring:message code="server_port_mask"/></th>
											<th><spring:message code="direction"/></th>
											<th><spring:message code="protocol"/></th>
										</c:when>
									</c:choose>
									<%--遍历主表属性 --%>
									<%--遍历子表属性 --%>
									<c:forEach items="${tableInfos}" var="tableInfo" varStatus="status">
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '4'}">
											<th><spring:message code="${tableInfo.tableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="match_area"/></th>
											<th><spring:message code="key_word"/></th>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '3'}">
											<th><spring:message code="${tableInfo.tableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="数值下界"/></th>
											<th><spring:message code="数值上界"/></th>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq'2'}">
											<th><spring:message code="${tableInfo.tableDesc}"/><spring:message code="config_describe"/></th>
											<th><spring:message code="key_word"/></th>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '1'}">
											<th><spring:message code="${tableInfo.tableDesc}"/><spring:message code="config_describe"/></th>
											<th>IP<spring:message code="type"/></th>
											<th><spring:message code="client_ip"/></th>
											<th><spring:message code="client_address_mask"/></th>
											<th><spring:message code="client_port"/></th>
											<th><spring:message code="client_port_mask"/></th>
											<th><spring:message code="server_ip"/></th>
											<th><spring:message code="server_address_mask"/></th>
											<th><spring:message code="server_port"/></th>
											<th><spring:message code="server_port_mask"/></th>
											<th><spring:message code="direction"/></th>
											<th><spring:message code="protocol"/></th>
										</c:if>
									</c:forEach>
									<%--遍历子表属性 --%>
									<th class="sort-column r.action"><spring:message code="block_type"/></th>
									<th><spring:message code="letter"/></th>
									<th><spring:message code="whether_area_block"/></th>
									<th><spring:message code="classification"/></th>
									<th><spring:message code="attribute"/></th>
									<th><spring:message code="label"/></th>
									<th><spring:message code="valid_identifier"/></th>
									<th><spring:message code="is_audit"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"/></th>
									<th class="sort-column r.audit_time"><spring:message code="audit_time"/></th>
									<%-- <th><spring:message code="operation"></spring:message></th> --%>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="bean" varStatus="status" step="1">
									<tr>
									<td><input type="checkbox" class="i-checks" id="${bean.compileId}" value="${bean.isAudit}"></td>
									<%-- <td>${status.index+1 }</td> --%>
									<%--遍历主表值 --%>
									<c:choose>
										<%--增强字符串 --%>
										<c:when test="${mainTableType eq '4'}">
											<td>${bean.complexCfg[mainTable].cfgDesc }</td>
											<td>${bean.complexCfg[mainTable].district }</td>
											<td>${bean.complexCfg[mainTable].keywords }</td>
										</c:when>
										<%--数值 --%>
										<c:when test="${mainTableType eq '3'}">
											<td>${bean.numCfg[mainTable].cfgDesc }</td>
											<td>${bean.numCfg[mainTable].lowBounadry}</td>
											<td>${bean.numCfg[mainTable].upBounadry }</td>
										</c:when>
										<%--字符串 --%>
										<c:when test="${mainTableType eq '2'}">
											<td>${bean.stringCfg[mainTable].cfgDesc }</td>
											<td>${bean.stringCfg[mainTable].cfgKeywords }</td>
										</c:when>
										<%--IP --%>
										<c:when test="${mainTableType eq '1'}">
											<td>${bean.ipCfg[mainTable].cfgDesc }</td>
											<td>V${bean.ipCfg[mainTable].ipType }</td>
											<td>${bean.ipCfg[mainTable].srcIp }</td>
											<td>${bean.ipCfg[mainTable].srcIpMask }</td>
											<td>${bean.ipCfg[mainTable].srcPort }</td>
											<td>${bean.ipCfg[mainTable].srcPortMask }</td>
											<td>${bean.ipCfg[mainTable].dstIp }</td>
											<td>${bean.ipCfg[mainTable].dstIpMask }</td>
											<td>${bean.ipCfg[mainTable].dstPort }</td>
											<td>${bean.ipCfg[mainTable].dstPortMask }</td>
											<td>
											<c:if test="${bean.ipCfg[mainTable].direction==0}"><spring:message code="twoway"/></c:if>
											<c:if test="${bean.ipCfg[mainTable].direction==1}"><spring:message code="oneway"/></c:if>
											</td>
											<td>
											<c:if test="${bean.ipCfg[mainTable].protocol==6}"><spring:message code="TCP"/></c:if>	
											<c:if test="${bean.ipCfg[mainTable].protocol==17}"><spring:message code="UDP"/></c:if>	
											<c:if test="${bean.ipCfg[mainTable].protocol==0}"><spring:message code="arbitrary"/></c:if>	
											</td>
										</c:when>
									</c:choose>
									<%--遍历主表值 --%>
									<%--遍历子表值 --%>
									<c:forEach items="${tableInfos}" var="tableInfo" varStatus="status">
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '4'}">
											<c:choose>
											<c:when test="${bean.complexCfg[tableInfo.tableName]!=null}">
											<td>${bean.complexCfg[tableInfo.tableName].cfgDesc }</td>
											<td>${bean.complexCfg[tableInfo.tableName].district }</td>
											<td>${bean.complexCfg[tableInfo.tableName].keywords }</td>
											</c:when>
											<c:otherwise><td></td><td></td><td></td></c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '3'}">
											<c:choose>
											<c:when test="${bean.numCfg[tableInfo.tableName]!=null}">
											<td>${bean.numCfg[tableInfo.tableName].cfgDesc }</td>
											<td>${bean.numCfg[tableInfo.tableName].lowBounadry}</td>
											<td>${bean.numCfg[tableInfo.tableName].upBounadry }</td>
											</c:when>
											<c:otherwise><td></td><td></td><td></td></c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '2'}">
											<c:choose>
											<c:when test="${bean.stringCfg[tableInfo.tableName]!=null}">
												<td>${bean.stringCfg[tableInfo.tableName].cfgDesc }</td>
												<td>${bean.stringCfg[tableInfo.tableName].cfgKeywords }</td>
											</c:when>
											<c:otherwise><td></td><td></td></c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${tableInfo.tableName ne mainTable and tableInfo.tableType eq '1'}">
											<c:choose>
											<c:when test="${bean.ipCfg[tableInfo.tableName]!=null}">
												<td>${bean.ipCfg[tableInfo.tableName].cfgDesc }</td>
												<td>V${bean.ipCfg[tableInfo.tableName].ipType }</td>
												<td>${bean.ipCfg[tableInfo.tableName].srcIp }</td>
												<td>${bean.ipCfg[tableInfo.tableName].srcIpMask }</td>
												<td>${bean.ipCfg[tableInfo.tableName].srcPort }</td>
												<td>${bean.ipCfg[tableInfo.tableName].srcPortMask }</td>
												<td>${bean.ipCfg[tableInfo.tableName].dstIp }</td>
												<td>${bean.ipCfg[tableInfo.tableName].dstIpMask }</td>
												<td>${bean.ipCfg[tableInfo.tableName].dstPort }</td>
												<td>${bean.ipCfg[tableInfo.tableName].dstPortMask }</td>
												<td>
												<c:if test="${bean.ipCfg[tableInfo.tableName].direction==0}"><spring:message code="twoway"/></c:if>
												<c:if test="${bean.ipCfg[tableInfo.tableName].direction==1}"><spring:message code="oneway"/></c:if>
												</td>
												<td>
												<c:if test="${bean.ipCfg[tableInfo.tableName].protocol==6}"><spring:message code="TCP"/></c:if>	
												<c:if test="${bean.ipCfg[tableInfo.tableName].protocol==17}"><spring:message code="UDP"/></c:if>	
												<c:if test="${bean.ipCfg[tableInfo.tableName].protocol==0}"><spring:message code="arbitrary"/></c:if>	
												</td>
											</c:when>
											<c:otherwise>
												<td></td><td></td><td></td><td></td><td></td><td></td>
												<td></td><td></td><td></td><td></td><td></td><td></td>
											</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
									<%--遍历子表值 --%>
									<td>
										<c:if test="${1 eq bean.action }"><spring:message code="block"/></c:if>
										<c:if test="${2 eq bean.action }"><spring:message code="monitor"/></c:if>
										<c:if test="${5 eq bean.action }"><spring:message code="block_white_list"/></c:if>
										<c:if test="${6 eq bean.action }"><spring:message code="monitor_white_list"/></c:if>
										<c:if test="${7 eq bean.action }"><spring:message code="block_monitor_white_list"/></c:if>
										<c:if test="${8 eq bean.action }"><spring:message code="grey_list"/></c:if>
									</td>
									<%-- <td>${cfg.serviceId }</td> --%>
									<td>${bean.requestName }</td>
									<%-- <td>${cfg.compileId }</td> --%>
									<td>
										<c:if test="${bean.isAreaEffective==0}"><spring:message code="no"/></c:if>
										<c:if test="${bean.isAreaEffective==1}"><spring:message code="yes"/></c:if>
									</td>
									<c:set var="classify"></c:set>
									<c:forEach items="${fn:split(bean.classify,',')}" var="classifyId" varStatus="status">
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
									<c:forEach items="${fn:split(bean.attribute,',')}" var="attributeId" varStatus="status">
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
									<c:forEach items="${fn:split(bean.lable,',')}" var="lableId" varStatus="status">
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
									<%-- <td>${bean.areaEffectiveIds }</td> --%>
									<td>
										<c:if test="${bean.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${bean.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${bean.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${bean.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${bean.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${bean.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${bean.isAudit eq '3'}"><span indexTable="${bean.indexTable}" data-placement="right" data-original-title="<spring:message code='letter_cancel_info'/>: " class="label le-ca-fo label-warning tooltips" data-icon="&#xe01e;"> <spring:message code="cancel_approved"/></span></c:when>
										</c:choose>
									</td>
									<td>${bean.creatorName }</td>
									<td><fmt:formatDate value="${bean.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${bean.editorName }</td>
									<td><fmt:formatDate value="${bean.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${bean.auditorName }</td>
									<td><fmt:formatDate value="${bean.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<%-- <td>
										<div class="btn-group btn-xs">
									      <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"></spring:message><span class="caret"></span></a>
									      <ul class="dropdown-menu btn-xs">
									      <c:if test="${audit==0}">
									      	<c:choose>
									      		<c:when test="${bean.isAudit eq '2'}">
									      			<li><a href="${ctx}/cfg/multiple/updateForm?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      		</c:when>
									      		<c:when test="${bean.isAudit eq '0'}">
									      			<li><a href="${ctx}/cfg/multiple/updateForm?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      			<li><a href="${ctx}/cfg/multiple/deleteCfg?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="return confirm('sure？', this.href)"><spring:message code="delete"></spring:message></a></li>
									      		</c:when>
									      	</c:choose>
									      </c:if>
									      <c:if test="${audit==1}">
									      	<c:choose>
									        	<c:when test="${bean.isAudit eq '1'}">
											        <li><a href="${ctx}/cfg/multiple/auditCfg?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&isAudit=3&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="return confirm('sure？', this.href)"><spring:message code="cancel"></spring:message></a></li>
									        	</c:when>
									        	<c:when test="${bean.isAudit eq '0'}">
									        		<li><a href="${ctx}/cfg/multiple/auditCfg?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&isAudit=1&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="return confirm('sure？', this.href)"><spring:message code="approved"></spring:message></a></li>
									        		<li><a href="${ctx}/cfg/multiple/auditCfg?serviceId=${bean.serviceId}&action=${bean.action}&compileId=${bean.compileId}&isAudit=2&cfgName=${cfgName}&mainTable=${mainTable}&mainTableType=${mainTableType}&mainTableDesc=${mainTableDesc}" onclick="return confirm('sure？', this.href)"><spring:message code="unapproved"></spring:message></a></li>
									        	</c:when>
								        	</c:choose>
									      </c:if>
									      </ul>
									    </div>
									</td> --%>
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