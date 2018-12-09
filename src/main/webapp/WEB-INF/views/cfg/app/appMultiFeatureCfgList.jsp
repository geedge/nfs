<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<script>
	 $(document).ready(function() {
		 $(".tooltips").tooltip();
		//搜索框提示语初始化
		if("${cfg.cfgDesc}"){
			$("#intype").val("${cfg.cfgDesc}");
		}else if("${cfg.compileId}"){
			$("#intype").val("${cfg.compileId}");
		}else if("${cfg.ipCfg.destIpAddress}"){
			$("#intype").val("${cfg.ipCfg.destIpAddress}");
		}else if("${cfg.strCfg.cfgKeywords}"){
			$("#intype").val("${cfg.strCfg.cfgKeywords}");
		}else if("${cfg.complexStrCfg.cfgKeywords}"){
			$("#intype").val("${cfg.complexStrCfg.cfgKeywords}");
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
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
 			$("#specServiceIdId").val("");
			$("#specServiceIdName").val("");
			$("#specServiceIdName").attr("placeholder",'<spring:message code="social_app"/>');
		});
		//异步获取voip相关信息
		$("span[id^=open]").click(function(){
			var openId=$(this).attr("id");
			var closeId=$(this).attr("id").replace("open","close");
			var index=$(this).attr("id").replace("open","");
			$("#"+openId).hide();
			$("#"+closeId).show();
			var compileId=$(this).attr("compileId");
			var functionId=$(this).attr("functionId");
			var cfgId=$(this).attr("cfgId");
			if($("#"+openId).parent().parent().next("tr").hasClass("child")){
				$("#"+openId).parent().parent().next("tr").show();
			}else{
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/app/feature/ajaxAppFeatureList',
					data:{"compileId":compileId,"cfgId":cfgId,"functionId":functionId,"index":index},
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
	<c:forEach items="${serviceList}" var="service">
         <c:if test="${cfg.functionId eq service.functionId}">
            <c:set var="serviceName" value="${service.serviceName}"/>
            <c:set var="configPermissionName" value="${service.serviceName}:config"/>
			<c:set var="confirmPermissionName" value="${service.serviceName}:confirm"/>
         </c:if>
    </c:forEach>
	<div class="theme-panel hidden-xs hidden-sm">
		<shiro:hasPermission name="${configPermissionName }">
			<button type="button" class="btn btn-primary"
				onClick="javascript:window.location='${ctx}/app/feature/multiFeatureCfgForm?functionId=${cfg.functionId}'">
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
						
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/app/feature/multiFeatureCfgList?functionId=${cfg.functionId}" method="post" class="form-search">
							<input id="functionId" name="functionId" type="hidden" value="${cfg.functionId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
							<input id="exportType" type="hidden" value="${cfg.seltype}"/>
							<input id="exportValue" type="hidden" value=""/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="exportType" type="hidden" value="${cfg.seltype}"/>
							<input id="exportValue" type="hidden" value=""/>
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
									<c:set var="spec_service_id"><spring:message code="social_app"/></c:set>
									<sys:treeselect id="specServiceId" name="specServiceId" value="${cfg.specServiceId}" enableSearch="true"
									 labelName="parent.specServiceName" notAllowSelectRoot="true" notAllowSelectParent="true" 
									 labelValue="${empty cfg.specServiceId?spec_service_id:fns:getBySpecServiceId(cfg.specServiceId).specServiceName}"
									 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=1" extId=""
									 cssClass="form-control input-small"/>
								</div>
								<div class="pull-left">
									<div class="input-group">
                                         <div class="input-group-btn">
                                         
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
												<form:option value="compileId"><spring:message code="cfg_id"></spring:message></form:option>
												<form:option value="cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
												<c:if test="${cfg.functionId ne 564 && cfg.functionId ne 567}">
													<form:option value="ipCfg.destIpAddress"><spring:message code="serverip"></spring:message></form:option>
												</c:if>
												<c:if test="${cfg.functionId ne 567}">
													<form:option value="complexStrCfg.cfgKeywords"><spring:message code="keywords"></spring:message></form:option>
												</c:if>
												<c:if test="${cfg.functionId eq 567}">
													<form:option value="strCfg.cfgKeywords"><spring:message code="keywords"></spring:message></form:option>
												</c:if>
												<form:option value="isValid"><spring:message code="valid_identifier"></spring:message></form:option>
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
							 		<shiro:hasPermission name="${configPermissionName }">
							 			<sys:delRow url="${ctx}/app/feature/multiFeatureCfgForm" id="contentTable" label="update"></sys:delRow>
                                        <sys:delRow url="${ctx}/app/feature/updateAppFeatureCfgValid?isValid=-1&functionId=${cfg.functionId }" id="contentTable" label="delete"></sys:delRow>
                                        <sys:delRow url="${ctx}/app/feature/exportFeature?functionId=${cfg.functionId }&specServiceId=${cfg.specServiceId}" searchUrl="${ctx}/app/feature/multiFeatureCfgList?functionId=${cfg.functionId}" id="contentTable" maxRow="10000" label="export"></sys:delRow>
                                    </shiro:hasPermission>
                               		<shiro:hasPermission name="${confirmPermissionName }">
	                                    <div class="btn-group">
	                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                            <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                            <i class="fa fa-angle-down"></i>
	                                        </button>
	                                        <ul class="dropdown-menu pull-right">
	                                            <li><sys:delRow url="${ctx}/app/feature/auditAppFeatureCfg?isAudit=1&isValid=1&functionId=${cfg.functionId }" id="contentTable" label="approved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/app/feature/auditAppFeatureCfg?isAudit=2&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="unapproved"></sys:delRow></li>
												<li><sys:delRow url="${ctx}/app/feature/auditAppFeatureCfg?isAudit=3&isValid=0&functionId=${cfg.functionId }" id="contentTable" label="cancelPass"></sys:delRow></li>            
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
								<div class="row hidden">
								
									<div class="col-md-3">
											
										<div class="form-group">
	                                            <label class="control-label"><spring:message code='request_number'/></label>
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
	                                            <label class="control-label"><spring:message code='type'/></label>
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
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th class="sort-column r.compile_id" style="display: none;"><spring:message code="cfg_id"/></th>
								    <th class="sort-column r.cfg_desc cfgDesc"><spring:message code="config_describe"/></th>
								    <th><spring:message code="social_app"/></th>
								    <th><spring:message code="is_audit"/></th>
									<th class="sort-column r.is_valid"><spring:message code="valid_identifier"/></th>
									<%-- <th><spring:message code="log_total"/></th> --%>
									<th><spring:message code="creator"/></th>
									<th class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"/></th>
									<th class="sort-column r.audit_time"><spring:message code="audit_time"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
								<tr>
									<td>
									<span id="open${status.index}" class="" compileId="${cfg.compileId}" functionId="${cfg.functionId}" cfgId="${cfg.cfgId}"> ▷ </span><span  style="display: none" id="close${status.index}" > ▼ </span>
									<input type="checkbox" class="i-checks child-checks" id="${cfg.cfgId}" value="${cfg.isAudit}">
									</td>
									<td>${cfg.compileId }</td>
								    <td>${cfg.cfgDesc }</td>
								    <td>${cfg.appName }</td>
								    <td>
										<c:choose>
											<c:when test="${cfg.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '3'}"><span class="label label-warning"><spring:message code="cancel_approved"></spring:message></span></c:when>
										</c:choose>
									</td>
								    <td>
										<c:if test="${cfg.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${cfg.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${cfg.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
									<%-- <td audit="${cfg.isAudit}" functionId="${cfg.functionId}" compileId="${cfg.compileId}" action="${cfg.action}" serviceId="${cfg.serviceId}"><div class="loading-total"></div></td> --%>
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
						<div class="page" >${page}</div>
					 </div>
					</div>
				</div>
			</div>
</div>

</body>
</html>