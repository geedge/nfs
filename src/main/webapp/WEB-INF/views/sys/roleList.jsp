<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
</head>
<body>

	<div class="page-content">
		<div class="theme-panel hidden-xs hidden-sm">
		

			<shiro:hasPermission name="sys:role:edit">
			<button type="button" class="btn btn-primary"
					onClick="javascript:window.location='${ctx}/sys/role/form'">&nbsp;<spring:message code="add"/>&nbsp;</button>
			</shiro:hasPermission>
				
		</div>
	
		<h3 class="page-title">
			<spring:message code="roleManage"/>
		</h3>
	
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-cogs"></i><spring:message code="date_list"/>
						</div>
					</div>
					<div class="portlet-body">
	
	
						<div class="row" >
	
	
						   <div class="col-md-12">
							
							
								<sys:message content="${message}"/>
								<table id="contentTable" class="table table-striped table-bordered table-condensed">
									<tr>
										<th><spring:message code="role_name"/></th>
										<th><spring:message code="data_range"/></th>
										<th><spring:message code="desc"/></th>
										<c:if test="${fns:getUser().identity eq 1}">
											<th><spring:message code="role_type"/></th>
										</c:if>
										<shiro:hasPermission name="sys:role:edit">
											<th><spring:message code="operation"/></th>
										</shiro:hasPermission>
									</tr>
									<c:forEach items="${list}" var="role">
										<c:if test="${fns:getUser().identity eq 1 or (fns:getUser().identity eq 0 and role.roleType eq 'user')}">
											<tr>
												<td><a href="form?id=${role.id}">${role.name}</a></td>
												<td>${fns:getDictLabel('SYS_DATA_SCOPE',role.dataScope,'无')}</td>
												<td title="${role.remark}">${fns:abbr(role.remark,30)}</td>
												<c:if test="${fns:getUser().identity eq 1}">
													<td>
														<c:if test="${role.roleType eq 'user' }"><spring:message code="ordinary_man"/></c:if>
														<c:if test="${role.roleType eq 'security-role' }"><spring:message code="manager"/></c:if>
													</td>
												</c:if>
												<shiro:hasPermission name="sys:role:edit"><td>
													<%--<a href="${ctx}/sys/role/assign?id=${role.id}"><spring:message code="allot"/></a> --%>
													<a href="${ctx}/sys/role/form?id=${role.id}"><spring:message code="edit"/></a>
													<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('<spring:message code="sure_delete"/>', this.href)"><spring:message code="delete"/></a>
												</td></shiro:hasPermission>	
											</tr>
										</c:if>
									</c:forEach>
								</table>
								
						    </div>
					   </div>
				   </div>
				   
				</div>
		    </div>
			 				
		</div>
		
	</div>
	
	
	

	
</body>
</html>