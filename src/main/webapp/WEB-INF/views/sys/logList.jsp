<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>日志管理</title>
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	 	<%-- <ul class="nav nav-tabs"> 
	 		<li class="active"><a href="${ctx}/sys/log/">日志列表</a></li> 
	 	</ul>  --%>
	<div class="page-content">
		<h3 class="page-title">
			<spring:message code="system_log"></spring:message>
		</h3>
		<h5 class="page-header"></h5>
		<div class="row">
			<div class="col-md-12">
				<div class="portlet">
					<div class="portlet-body">
						<div class="row">
							<form:form id="searchForm" action="${ctx}/sys/log" method="post" class="breadcrumb form-search" style="background-color:#fff" >
								<input id="pageNo" name="pageNo" type="hidden"
									value="${page.pageNo}" />
								<input id="pageSize" name="pageSize" type="hidden"
									value="${page.pageSize}" />
								<div class="col-md-12" >
											<div class="pull-left">
												<label><spring:message code="log_menu" />：</label> <input
													id="title" name="title" type="text" maxlength="50"
													class="form-control" value="${sysLog.title}" />
											</div>

											<div class="pull-left">
												<label><spring:message code="user" />：</label> <input
													id="createBy" name="createBy" type="text" maxlength="50"
													class="form-control" value="${sysLog.createBy}" />
											</div>
											<div class="pull-left">
												<label><spring:message code="URL" />：</label> <input
													id="requestUri" name="requestUri" type="text"
													maxlength="50" class="form-control"
													value="${sysLog.requestUri}" />
										</div>
											<div class="pull-left">
												<label><spring:message code="log_date" />：</label> <input
													id="beginDate" name="beginDate" type="text"
													readonly="readonly" maxlength="20"
													class="form-control Wdate"
													value="<fmt:formatDate value="${sysLog.beginDate}" pattern="yyyy-MM-dd"/>"
													onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
										</div>
											<div class="pull-left">
												<label><spring:message code="" />&nbsp;</label> <input
													id="endDate" name="endDate" type="text" readonly="readonly"
													maxlength="20" class="form-control Wdate"
													value="<fmt:formatDate value="${sysLog.endDate}" pattern="yyyy-MM-dd"/>"
													onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
											</div>
											<div class="pull-left">
												<label for="exception"><input id="exception" name="exception" type="checkbox" ${sysLog.exception eq '1'?' checked':''} value="1" /> <spring:message code="log_exception" /></label>
												<button type="submit" class="btn blue form-control"><i class="fa fa-search"></i> <spring:message code="search" /></button>
										</div>

										
								</div>
							</form:form>
						</div>
						<sys:message content="${message}" />
						<div class="">
						<br/>
							<table  id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
								<thead>
									<tr>
										<th><spring:message code="log_menu" /></th>
										<th><spring:message code="log_user" /></th>
										<th><spring:message code="URL" /></th>
										<th><spring:message code="log_submission" /></th>
										<th><spring:message code="log_ip" /></th>
										<th><spring:message code="operate_time" /></th>
								</thead>
								<tbody>
									<%
										request.setAttribute("strEnter", "\n");
										request.setAttribute("strTab", "\t");
									%>
									<c:forEach items="${page.list}" var="log">
										<tr>
											<td>${log.title}</td>
											<td>${log.createBy}</td>
											<td><strong>${log.requestUri}</strong></td>
											<td>${log.method}</td>
											<td>${log.remoteAddr}</td>
											<td><fmt:formatDate value="${log.createDate}"
													type="both" /></td>
										</tr>
										<c:if test="${not empty log.exception}">
											<tr>
												<td colspan="8"
													style="word-wrap: break-word; word-break: break-all;">
													<%-- 					用户代理: ${log.userAgent}<br/> --%> <%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
													<spring:message code="log_exception_info" />：<br />
													${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
							<div class="page">${page}</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>