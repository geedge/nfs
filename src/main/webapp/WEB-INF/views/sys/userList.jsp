<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			top.$.jBox.confirm("确认要导出用户数据吗？", "系统提示", function(v, h, f) {
				if (v == "ok") {
					$("#searchForm").attr("action", "${ctx}/sys/user/export");
					$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			top.$('.jbox-body .jbox-icon').css('top', '55px');
		});
		$("#btnImport").click(function() {
			$.jBox($("#importBox").html(), {
				title : "导入数据",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
			});
		});

	});

	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/sys/user/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<div class="page-content">
		<div id="importBox" class="hide">
			<form id="importForm" action="${ctx}/sys/user/import" method="post"
				enctype="multipart/form-data" class="form-search"
				style="padding-left: 20px; text-align: center;"
				onsubmit="loading('正在导入，请稍等...');">
				<br /> <input id="uploadFile" name="file" type="file"
					style="width: 330px" /><br />
				<br /> <input id="btnImportSubmit" class="btn btn-primary"
					type="submit" value="   导    入   " /> <a
					href="${ctx}/sys/user/import/template"><spring:message code="template"/>下载模板</a>
			</form>
		</div>

		<shiro:hasPermission name="sys:user:edit">
			<div class="theme-panel hidden-xs hidden-sm">
				<button type="button" class="btn btn-primary"
					onClick="javascript:window.location='${ctx}/sys/user/form'">
					<i class="fa fa-plus"></i>
					<spring:message code="add" />
				</button>
			</div>
		</shiro:hasPermission>

		<h3 class="page-title">
			<spring:message code="userManage"></spring:message>
		</h3>

		<h5 class="page-header"></h5>

		<!-- 筛选搜索内容栏默认隐藏-->
		<div class="row">
			<div class="col-md-12">
				<form:form id="searchForm" modelAttribute="sysUser"
					action="${ctx}/sys/user/list" method="post" class="form-search ">
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden"
						value="${page.pageSize}" />
					<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />

					<div class="col-md-12">
						<div class="row">
							 <div class="col-md-3">
								<div class="input-group">
									<div class="input-group-btn">
										<span class="form-control"><spring:message code="login_name"/></span>
	                             	</div>
									<form:input path="loginId" htmlEscape="false" maxlength="50"
										class="form-control" />
								</div>
							 </div>
							<div class="col-md-3">
								<div class="input-group">
									<div class="input-group-btn">
										<span class="form-control"><spring:message code="name"/></span>
	                             	</div>
									<form:input path="name" htmlEscape="false" maxlength="50"
										class="form-control" />
								</div>
							</div>
							<div class="pull-left">
							<span>&nbsp;</span>
									<button type="submit" class="btn blue" onclick="return page();">
										<i class="fa fa-search"></i>
										<spring:message code="search" />
									</button>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<sys:message content="${message}"  />
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					
					<%--<th><spring:message code="group" /></th> --%>
					<th class="sort-column login_id"><spring:message code="login_name" /></th>
					<th class="sort-column name"><spring:message code="name" /></th>
					 <th><spring:message code="organizer" /></th>
					<c:if test="${fns:getUser().identity eq 1}">
						<th><spring:message code="identify_mark" /></th>
					</c:if>
					<%-- <th><spring:message code="mail" /></th> --%>
					<th><spring:message code="create_time" /></th>
					<%--<th>角色</th> --%>
					<shiro:hasPermission name="sys:user:edit">
						<th><spring:message code="operation" /></th>
					</shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						
						<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginId}</a></td>
						<td>${user.name}</td>
						<td>${user.company.name }</td>
						<c:if test="${fns:getUser().identity eq 1}">
							<td>
								<c:if test="${user.identity eq 0 }"><spring:message code="ordinary_man"/></c:if>
								<c:if test="${user.identity eq 1 }"><spring:message code="manager"/></c:if>
							</td>
						</c:if>
						<td><fmt:formatDate value="${user.createTime}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<shiro:hasPermission name="sys:user:edit"></shiro:hasPermission>
						<td>
							<c:if test="${fns:getUser().identity eq 1 or (fns:getUser().identity eq 0 and user.identity eq 0)}">
								<a href="${ctx}/sys/user/form?id=${user.id}"><spring:message code="edit" /></a> <a
								href="${ctx}/sys/user/delete?id=${user.id}"
								onclick="return confirmx('<spring:message code="sure_delete"/>', this.href)"><spring:message code="delete" /></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="page">${page}</div>
	</div>

</body>
</html>