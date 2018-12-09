<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
 	$(document).ready(function() {
 		//搜索框提示语初始化
		if("${cfg.groupId}"){
			$("#intype").val("${cfg.groupId}");
		}else{
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
		}
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
		});
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
		<%-- <shiro:hasPermission name="avContUrl:config"> --%>
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/groupArea/groupAreaForm'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message>
		</button>
		<%--</shiro:hasPermission>--%>
	</div>

	<h3 class="page-title">
		<spring:message code="area_group_manage"/>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/cfg/groupArea/groupAreaList" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								 <div class="pull-left">
									<div class="input-group">
                                         <div class="input-group-btn">
                                         
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
												<form:option value="areaCode"><spring:message code="area"></spring:message></form:option>
											</form:select>
									
                                         </div>
                                         
                                         <input id="intype" class="form-control input-medium" type="hidden" value="">
                                  		 <div class="input-group-btn">
	                                  		 <form:select id="" path="areaCode" class="selectpicker select2 input-medium" data-live-search="true" data-live-search-placeholder="search">
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${areas}" var="area">
														<form:option value="${area.itemCode }"><spring:message code="${area.itemValue }"/></form:option>
													</c:forEach>
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
							 		<%-- <shiro:hasPermission name="avContUrl:config"> --%>
							 			<sys:delRow url="${ctx}/cfg/groupArea/groupAreaForm" id="contentTable" label="update"></sys:delRow>
                                        <sys:delRow url="${ctx}/cfg/groupArea/delete?isValid=-1" id="contentTable" label="delete"></sys:delRow>
                                  <%--  </shiro:hasPermission> --%>
                               		
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
								</div>
							</div> 
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<sys:message content="${message}" type="${messageTypes }"/>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th><spring:message code="group_name"/></th>
									<th><spring:message code="area"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
									<tr>
									<td><input type="checkbox" class="i-checks" id="${cfg.id}" value="0" "></td>
									<td>
										<%-- <a href="javascript:;" data-original-title="${cfg.groupName}"
										 class="tooltips" data-flag="false" data-html="true" data-placement="top">
											${fns:abbr(cfg.groupName,20)}
										</a> --%>
										<c:forEach items="${policyGroups }" var="policyGroup">
		               							<c:if test="${cfg.groupId==policyGroup.groupId }"><spring:message code="${policyGroup.groupName}"/></c:if>
				              			</c:forEach>
									</td>
									<td>
										<c:forEach items="${areas}" var="area">
											<c:if test="${cfg.areaCode==area.itemCode }"><spring:message code="${area.itemValue}"/></c:if>
										</c:forEach>
				               		</td>
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

</body>
</html>