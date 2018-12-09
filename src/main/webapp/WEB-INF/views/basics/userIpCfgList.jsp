<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="asn_ip_configuration"></spring:message></title>
<script>
 	$(document).ready(function() {
 		//搜索框提示语初始化
		if("${cfg.cfgDesc}"){
			$("#intype").val("${cfg.cfgDesc}");
		}else if("${cfg.destIpAddress}"){
			$("#intype").val("${cfg.destIpAddress}");
		}else{
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
		}
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
		});
 		//筛选功能初始化
		filterActionInit();
		//$("#isAudit").change(function(){
		//	page();
		//});
		//reset
		$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#description").attr("value",'');
			$("#searchForm")[0].reset();
		});
		
		if($("#exportType").val() != null && $("#exportType").val() != ""){
			 if($("#intype").val() != null && $("#intype").val() != ""){
				$("#exportValue").val($("#intype").val());
		  } 
		}
 	});
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
		
			<shiro:hasPermission name="asn:ip:config">
				<button type="button" class="btn btn-primary"
				onClick="javascript:window.location='${ctx}/basics/vpn/addForm?functionId=${cfg.functionId}'">
				<i class="fa fa-plus"></i>
				<spring:message code="add"></spring:message></button>
				<%-- <c:set var="regionImport" value="false"></c:set>
				<c:forEach items="${regionList}" var="region" >
					<c:if test="${(cfg.functionId eq region.functionId) && region.isImport eq 1}">
						<c:set var="regionImport" value="true"></c:set>
					</c:if>
				</c:forEach>
				<c:if test="${regionImport eq 'true'}">
					<button type="button" class="btn btn-primary import" >
						<i class="fa fa-plus"></i>
					<spring:message code="import"></spring:message></button>
				</c:if> --%>
			</shiro:hasPermission>
	</div> 

	<h3 class="page-title">
	<spring:message code="vpn_ip"></spring:message>
		<%-- <small><spring:message code="date_list"/></small> --%>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/basics/vpn/list?functionId=${cfg.functionId}" method="post" class="form-search">
							<input id="functionId" name="functionId" type="hidden" value="${cfg.functionId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
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
								<%--<div class="pull-left">
									 <c:set var="state"><spring:message code='group'/></c:set>
									<form:select path="isAudit" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all_states"/></form:option>
										<form:option value="0"><spring:message code="created"></spring:message></form:option>
										<form:option value="1"><spring:message code="approved"></spring:message></form:option>
										<form:option value="2"><spring:message code="unapproved"></spring:message></form:option>
										<form:option value="3"><spring:message code="cancel_approved"></spring:message></form:option>
									</form:select> 
								</div>--%>
								<div class="pull-left">
									<div class="input-group">
                                         <div class="input-group-btn">
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
                                        		<form:option value="destIpAddress"><spring:message code="vpn_ip"></spring:message></form:option>
												<form:option value="cfgDesc"><spring:message code="desc"></spring:message></form:option>
												<%-- <form:option value="isValid"><spring:message code="is_issued"></spring:message></form:option> --%>
											</form:select>
                                         </div>
                                         <input id="intype" class="form-control input-medium" type="text" value="">
                                  		 <div class="input-group-btn">
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
									 <button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/>  <i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
							 		<shiro:hasPermission name="asn:ip:config">
							 			<sys:delRow url="${ctx}/basics/vpn/updateForm?functionId=${cfg.functionId}" id="contentTable" label="update"></sys:delRow>
                                        <sys:delRow url="${ctx}/basics/vpn/delete?isValid=-1&functionId=${cfg.functionId }" id="contentTable" label="delete"></sys:delRow>
<%--                                    <sys:delRow url="${ctx}/basics/vpn/exportIp?functionId=${cfg.functionId }" searchUrl="${ctx}/basics/asn/list?functionId=${cfg.functionId}" id="contentTable" maxRow="10000" label="export"></sys:delRow>
 --%>                                    </shiro:hasPermission>
							 		<%-- <shiro:hasPermission name="asn:ip:confirm">
	                                    <div class="btn-group">
	                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                            <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                            <i class="fa fa-angle-down"></i>
	                                        </button>
	                                        <ul class="dropdown-menu pull-right">
	                                            <li><sys:delRow url="${ctx}/basics/asn/audit?isAudit=1&isValid=1&functionId=${cfg.functionId }" id="contentTable" label="approved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/basics/asn/audit?isAudit=2&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="unapproved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/basics/asn/audit?isAudit=3&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="cancelPass"></sys:delRow></li>            
				                            </ul>
				                         </div>
                                    </shiro:hasPermission> --%>
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
									
									<%-- <div class="col-md-2">
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
									</div> --%>
								</div>
								
							
							</div> 
							
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<sys:message content="${message}" type="${messageType }"/>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th><spring:message code="vpn_ip"/></th>
									<th><spring:message code="desc"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
									<tr>
									<td><input type="checkbox" class="i-checks" id="${cfg.cfgId}" isValid="${cfg.isValid}"  value="0"></td>
									<td>${cfg.destIpAddress }</td>
									<td>${cfg.cfgDesc }</td>
									<td>${cfg.creatorName }</td>
									<td><fmt:formatDate value="${cfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.editorName }</td>
									<td><fmt:formatDate value="${cfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
<c:set var="importPath" value="/basics/vpn/list?functionId=${cfg.functionId}"/>
<!-- 模板导入，start -->
<%@include file="/WEB-INF/include/excel/importModal.jsp" %>
</body>
</html>