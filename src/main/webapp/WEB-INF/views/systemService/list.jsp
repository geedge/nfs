<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>系统业务类型管理列表</title>
	<%-- <link href="${pageContext.request.contextPath}/static/pages/css/systemService.css" rel="stylesheet" type="text/css"/> --%>
</head>

<body>
<script>
$(document).ready(function() {
	top.window.scrollTo(0, 0);
});

function page(n,s){
	if(n) $("#pageNo").val(n);
	if(s) $("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/systemService/list");
	$("#searchForm").submit();
	return false;
}

function deleteService(id) {
	if (id) {
		if (confirm("<spring:message code='confirm_message'/>")) {
			$("#serviceId").val(id);
			$("#searchForm").attr("action","${ctx}/systemService/delete");
			$("#searchForm").submit();
		}
	}
}

function checkNum(obj) {
	var reg = /^[0-9]*$/;
	if (!reg.test(obj.value)) {
		top.$.jBox.tip("请输入数字");
		obj.value=obj.value.replace(/[^\d]/g,'');
	}
}
</script>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default" onclick="javascript:window.location='${ctx}/systemService/list'"><spring:message code="refresh"></spring:message></button>
		<shiro:hasPermission name="system:service:edit">
			<button type="button" class="btn btn-primary" onClick="javascript:window.location='${ctx}/systemService/systemServiceform'"><spring:message code="add"></spring:message></button>
		</shiro:hasPermission>
	</div>

	<h3 class="page-title">
		<spring:message code="sysService_manage"></spring:message>
	</h3>

	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row">
						
						<form:form id="searchForm" modelAttribute="systemServiceInfo" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }"/>
							<input id="serviceId" name="id" type="hidden"/>
							<div class="col-md-12">
								<div class="pull-left">
									<form:select path="action" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all"/> <spring:message code="act"/></form:option>
										<form:option value="1"><spring:message code="block"/></form:option>
										<form:option value="2"><spring:message code="monitor"/></form:option>
										<form:option value="5"><spring:message code="block_white_list"/></form:option>
										<form:option value="6"><spring:message code="monitor_white_list"/></form:option>
										<form:option value="7"><spring:message code="block_monitor_white_list"/></form:option>
										<form:option value="8"><spring:message code="grey_list"/></form:option>
									</form:select>
								</div>
								<div class="pull-left">
									<form:select path="serviceType" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all"/> <spring:message code='type'/></form:option>
										<form:option value="1"><spring:message code="single_domain"/></form:option>
										<form:option value="2"><spring:message code="multi_domain"/></form:option>
										<form:option value="3"><spring:message code="special_service"/></form:option>
									</form:select>
								</div>
								<div class="pull-left">
									<input name="serviceName" class="form-control input-medium" placeholder="<spring:message code="service_name"/>" value="${systemServiceInfo.serviceName }"/>
								</div>
								<div class="pull-left">
									<input name="serviceId" class="form-control input-medium" placeholder="<spring:message code="service_id"/>" value="${systemServiceInfo.serviceId }" onkeyup="checkNum(this)"/>
								</div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
								</div>
							</div>
						</form:form>
					</div>
					<sys:message content="${message}"/>
					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th><spring:message code="service_name"/></th>
									<th style="width:8%"><spring:message code="service_id"/></th>
									<th><spring:message code="act"/></th>
									<th><spring:message code="type"/></th>
									<th><spring:message code="creator"></spring:message></th>
									<th style="width:15%"><spring:message code="create_time"></spring:message></th>
									<th><spring:message code="desc"></spring:message></th>
									<th><spring:message code="operation"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="ssi">
								<tr>
									<td>${ssi.serviceName }</td>
									<td>${ssi.serviceId }</td>
									<td>
										<c:if test="${1 eq ssi.action }"><spring:message code="block"/></c:if>
										<c:if test="${2 eq ssi.action }"><spring:message code="monitor"/></c:if>
										<c:if test="${5 eq ssi.action }"><spring:message code="block_white_list"/></c:if>
										<c:if test="${6 eq ssi.action }"><spring:message code="monitor_white_list"/></c:if>
										<c:if test="${7 eq ssi.action }"><spring:message code="block_monitor_white_list"/></c:if>
										<c:if test="${8 eq ssi.action }"><spring:message code="grey_list"/></c:if>
									</td>
									<td>
										<c:if test="${1 eq ssi.serviceType }"><spring:message code="single_domain"/></c:if>
										<c:if test="${2 eq ssi.serviceType }"><spring:message code="multi_domain"/></c:if>
										<c:if test="${3 eq ssi.serviceType }"><spring:message code="special_service"/></c:if>
									</td>
									<td>${ssi.creator.name }</td>
									<td><fmt:formatDate value="${ssi.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${ssi.serviceDesc }</td>
									<td>
										<shiro:hasPermission name="system:service:edit">
										<!-- 初始化数据admin能改不能删，其他用户不能进行任何操作 -->
										<c:choose>
											<c:when test="${ssi.currentUser.loginId eq 'admin' && ssi.isInitianlize == 1}">
												<span><a href="${ctx }/systemService/systemServiceform?id=${ssi.id }"><spring:message code="edit"></spring:message></a></span>
												<span title="<spring:message code='initial_data_is_not_allowed_to_delete'/>"><font color="#C0C0C0"><spring:message code="delete"></spring:message></font></span>
											</c:when>
											<c:when test="${ssi.currentUser.loginId ne 'admin' && ssi.isInitianlize == 1}">
												<span title="<spring:message code='initial_data_is_not_allowed_to_edit'/>"><font color="#C0C0C0"><spring:message code="edit"></spring:message></font></span>
												<span title="<spring:message code='initial_data_is_not_allowed_to_delete'/>"><font color="#C0C0C0"><spring:message code="delete"></spring:message></font></span>
											</c:when>
											<c:when test="${ssi.isInitianlize != 1}">
												<span><a href="${ctx }/systemService/systemServiceform?id=${ssi.id }"><spring:message code="edit"></spring:message></a></span>
												<span><a href="javascript:void(0)" onclick="deleteService('${ssi.id }')"><spring:message code="delete"></spring:message></a></span>
											</c:when>
										</c:choose>
										<c:if test="${ssi.currentUser.loginId eq 'admin' }"></c:if>
										</shiro:hasPermission>
									</td>
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