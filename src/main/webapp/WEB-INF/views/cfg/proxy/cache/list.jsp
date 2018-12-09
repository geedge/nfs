<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<script>
	 $(document).ready(function() {
		//搜索框提示语初始化
		if("${cfg.cfgDesc}"){
			$("#intype").val("${cfg.cfgDesc}");
		}else if("${cfg.compileId}"){
			$("#intype").val("${cfg.compileId}");
		}else if("${cfg.strCfg.cfgKeywords}"){
			$("#intype").val("${cfg.strCfg.cfgKeywords}");
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
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#level").attr("value",'');
			$("#searchForm")[0].reset();
		});
		//异步获取子配置相关信息
		$("span[id^=open]").click(function(){
			var openId=$(this).attr("id");
			var closeId=$(this).attr("id").replace("open","close");
			var index=$(this).attr("id").replace("open","");
			$("#"+openId).hide();
			$("#"+closeId).show();
			//var compileId=$(this).attr("compileId");
			var cfgId=$(this).attr("cfgId");
			if($("#"+openId).parent().parent().next("tr").hasClass("child")){
				$("#"+openId).parent().parent().next("tr").show();
			}else{
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/proxy/cache/ajaxCacheSubList',
					data:{"cfgId":cfgId,"index":index},
					dataType:"html",
					success:function(data){
						var subTab="<tr class='child'>"+
					 	"<td style='border-right: 1px solid #FFFFFF;'>"+
					 		"<input type='checkbox' hidden='hidden'/>"+
					 	"</td>"+
					 	"<td colspan='"+($(".table tr").eq(0).children("th").length-1)+"'>";
						var html="";
						html+="<div class='row'>";
						html = html+data;
						subTab=subTab+html;
						subTab+="</td>";
						subTab+="</tr>";
						$("#"+openId).parent().parent().after(subTab);
						$("div[name='tabTitle"+index+"']").get(0).click();
					}
				});
			}
			
		});
		
		$("span[id^=close]").on("click",function(){
			var closeId=$(this).attr("id");
			var openId=$(this).attr("id").replace("close","open");
			$("#"+closeId).hide();
			$("#"+openId).show();
			$("#"+closeId).parent().parent().next("tr").hide();
		});
		$(".action").on("change", function() {
			$("#serviceId").val($(this).attr("serviceId"));
			$("#protocolId").val($(this).attr("protocolId"));
		});
	}); 
	
</script>

</head>
<body>

<div class="page-content">
		<c:forEach items="${serviceList }" var="serviceDict">
			<c:if test="${serviceDict.functionId== cfg.functionId}">
				<c:set var="serviceName1" value="${serviceDict.serviceName }"/>
				<c:set var="action" value="${serviceDict.action }"/>
			</c:if>
		</c:forEach>
		<div class="theme-panel hidden-xs hidden-sm">
		
		<c:set var="configPermissionName" value="cache:policy:config"/>
		<c:set var="confirmPermissionName" value="cache:policy:confirm"/>
		<c:set var="actionPath" value="${ctx}/proxy/cache/list?functionId=${cfg.functionId}"></c:set>
		<shiro:hasPermission name="${configPermissionName }">
			<button type="button" class="btn btn-primary"
				onClick="javascript:window.location='${ctx}/proxy/cache/form?functionId=${cfg.functionId}'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
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
						<form:form id="searchForm" modelAttribute="cfg" action="${actionPath }" method="post" class="form-search">
							<input id="functionId" name="functionId" type="hidden" value="${cfg.functionId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
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
								
									<div class="input-group">
                                         <div class="input-group-btn">
                                         
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
												<form:option value="cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
												<form:option value="compileId"><spring:message code="cfg_id"></spring:message></form:option>
												<form:option value="strCfg.cfgKeywords"><spring:message code="keywords"></spring:message></form:option>
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
									 <button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/>  <i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
							 		<shiro:hasPermission name="${configPermissionName }">
							 			<sys:delRow url="${ctx}/proxy/cache/form" id="contentTable" label="update"></sys:delRow>
                                        <sys:delRow url="${ctx}/proxy/cache/delete?isValid=-1&functionId=${cfg.functionId }" id="contentTable" label="delete"></sys:delRow>
                                        <sys:delRow url="${ctx}/proxy/cache/exportHttp?functionId=${cfg.functionId }" id="contentTable" searchUrl="${actionPath }" label="export"></sys:delRow> 
                                    </shiro:hasPermission>
                               		<shiro:hasPermission name="${confirmPermissionName }">
	                                    <div class="btn-group">
	                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                            <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                            <i class="fa fa-angle-down"></i>
	                                        </button>
	                                        <ul class="dropdown-menu pull-right">
	                                            <li><sys:delRow url="${ctx}/proxy/cache/audit?isAudit=1&isValid=1&functionId=${cfg.functionId }" id="contentTable" label="approved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/proxy/cache/audit?isAudit=2&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="unapproved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/proxy/cache/audit?isAudit=3&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="cancelPass"></sys:delRow></li>            
				                            </ul>
				                         </div>
                                    </shiro:hasPermission>
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
							<c:forEach items="${serviceList}" var="service"
									varStatus="satus">
									<c:if
										test="${cfg.functionId eq service.functionId}">
										<c:set var="action" value="${service.action }"></c:set>
									</c:if>
								</c:forEach>
							
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<sys:message content="${message}"/>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th class="sort-column a.compile_id" style="display: none"><spring:message code="cfg_id"/></th>
								    <th class="sort-column a.cfg_desc cfgDesc"><spring:message code="config_describe"/></th>
									<th class="sort-column a.action"><spring:message code="block_type"/></th>
									<th><spring:message code="ignore_query_string"/></th>
									<th><spring:message code="include_cookie"/></th>
									<th><spring:message code="no_revalidate"/></th>
									<th><spring:message code="cache_dyn_url"/></th>
									<th><spring:message code="cache_cookied_cont"/></th>
									<th><spring:message code="ignore_req_nocache"/></th>
									<th><spring:message code="ignore_res_nocache"/></th>
									<th><spring:message code="force_caching"/></th>
									<th><spring:message code="min_use"/></th>
									<th><spring:message code="pinning_time"/></th>
									<th><spring:message code="max_cache_size"/></th>
									<th><spring:message code="inactive_time"/></th>
									<th><spring:message code="max_cache_obj_size"/></th>
									<th class="sort-column a.is_audit"><spring:message code="is_audit"/></th>
									<th><spring:message code="whether_area_block"/></th>
									<th><spring:message code="letter"/></th>
									<th><spring:message code="classification"/></th>
									<th><spring:message code="attribute"/></th>
									<th><spring:message code="label"/></th>
									<th class="sort-column a.is_valid"><spring:message code="valid_identifier"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column a.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column a.edit_time"><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"/></th>
									<th class="sort-column a.audit_time"><spring:message code="audit_time"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="indexCfg" varStatus="status" step="1">
								<tr>
									<td>
									<span id="open${status.index}" class="" compileId="${indexCfg.compileId}" cfgId="${indexCfg.cfgId}"> ▷ </span><span  style="display: none" id="close${status.index}" > ▼ </span>
									<input type="checkbox" class="i-checks child-checks" id="${indexCfg.cfgId}" value="${indexCfg.isAudit}">
									</td>
									<td>${indexCfg.compileId }</td>
								    <td>${indexCfg.cfgDesc }</td>
									<td>
										<c:forEach items="${serviceList}" var="service"
											varStatus="satus">
												<c:if test="${indexCfg.functionId eq service.functionId}">
														<c:if test="${indexCfg.action==service.action}"><spring:message code="${service.actionCode }"/></c:if>
												</c:if>
										</c:forEach>
									</td>
									<td>
										${indexCfg.userRegion.cache_key.ignore_qs}
									</td>
									<td>
										${indexCfg.userRegion.cache_key.cookie}
									</td>
									<td>
										<c:forEach items="${fns:getDictList('CACHE_NO_REVALIDATE') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.no_revalidate }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('CACHE_DYN_URL') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.cache_dyn_url }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('CACHE_COOKIED_CONT') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.cache_cookied_cont }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('IGNORE_REQ_NOCACHE') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.ignore_req_nocache }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('IGNORE_RES_NOCACHE') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.ignore_res_nocache }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${fns:getDictList('FORCE_CACHING') }" var="dict">
											<c:if test="${dict.itemCode eq indexCfg.userRegion.force_caching }">
												<spring:message code="${dict.itemValue }"/>
											</c:if>
										</c:forEach>
									</td>
									<td>
										${indexCfg.userRegion.min_use}
									</td>
									<td>
										${indexCfg.userRegion.pinning_time}
									</td>
									<td>
										${indexCfg.userRegion.max_cache_size}
									</td>
									<td>
										${indexCfg.userRegion.inactive_time}
									</td>
									<td>
										${indexCfg.userRegion.max_cache_obj_size}
									</td>
									<td>
										<c:choose>
											<c:when test="${indexCfg.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${indexCfg.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${indexCfg.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${indexCfg.isAudit eq '3'}"><span indexTable="${indexCfg.indexTable}" data-placement="right" data-original-title="<spring:message code='letter_cancel_info'/>: " class="label le-ca-fo label-warning tooltips" data-icon="&#xe01e;"> <spring:message code="cancel_approved"/></span></c:when>
										</c:choose>
									</td>
									<td>
										<c:if test="${indexCfg.isAreaEffective==0}"><spring:message code="all"/></c:if>
										<c:if test="${indexCfg.isAreaEffective==1}">
												<a href="javascript:viewAreaInfo('${ctx}','${indexCfg.areaEffectiveIds }','${indexCfg.compileId }')" >
													<spring:message code="selective"/>
												</a>
										</c:if>
									</td>
									<td>${indexCfg.requestName }</td>
									<td>
									<c:set var="classify"></c:set>
									<c:forEach items="${fn:split(indexCfg.classify,',')}" var="classifyId" varStatus="status">
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
									<c:forEach items="${fn:split(indexCfg.attribute,',')}" var="attributeId" varStatus="status">
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
									<c:forEach items="${fn:split(indexCfg.lable,',')}" var="lableId" varStatus="status">
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
								    <td>
										<c:if test="${indexCfg.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${indexCfg.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${indexCfg.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
								    <td>${indexCfg.creatorName }</td>
									<td><fmt:formatDate value="${indexCfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${indexCfg.editorName }</td>
									<td><fmt:formatDate value="${indexCfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${indexCfg.auditorName }</td>
									<td><fmt:formatDate value="${indexCfg.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="page" >${page}</div>
					</div>
					</div>
				</div>
			</div>
</div>

</body>
</html>