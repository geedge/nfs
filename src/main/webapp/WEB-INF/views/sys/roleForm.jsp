<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	
	<link href="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
	
	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")}
				},
				messages: {
					name: {remote: "<spring:message code='role_unique'/>"}
				},
				submitHandler: function(form){
					var ids = [], nodes = tree.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					$("#menuIds").val(ids);
				
					loading('onloading...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='role_error_info'/>");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
			// 用户-菜单
			var zNodes=[
					<c:forEach items="${menuList}" var="menu">{id:"${menu.id}", pId:"${not empty menu.parent.id?menu.parent.id:0}", name:"<c:if test='${not empty menu.parent.id}'><spring:message code="${menu.code}"/></c:if><c:if test='${empty menu.parent.id}'><spring:message code='permission_list'/></c:if>"},
		            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 不选择父节点
			tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
			// 默认选择节点
			var ids = "${role.menuIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			tree.expandAll(true);
			//隐藏没有分配父节点权限，却有子节点权限的叶子节点
			$("#menuTree").children("li:not(:eq(0))").hide();
		});
		
	</script>
</head>
<body>


<div class="page-content">

	<div class="theme-panel hidden-xs hidden-sm">
		
		<button type="button" class="btn btn-default" onclick="history.go(-1)">&nbsp;<spring:message code="back"/>&nbsp;</button>

	</div>
		
		
	<h3 class="page-title">
		<spring:message code="roleManage"/>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><shiro:hasPermission name="sys:role:edit"><c:if test="${not empty role.id}"><spring:message code="edit"/></c:if><c:if test="${empty role.id}"><spring:message code="add"/></c:if></shiro:hasPermission><shiro:lacksPermission name="sys:role:edit"><spring:message code="show"/></shiro:lacksPermission></div>
				</div>
				
				<div class="portlet-body form">
					
					<div class="form-body">

						<!-- BEGIN FORM-->
							<form:form id="inputForm" modelAttribute="sysRole" action="${ctx}/sys/role/saveOrUpdate" method="post" class="form-horizontal">
								<form:hidden path="id"/>
								<spring:message code="${message}"/>
								
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="role_name"/>:</label>
									<div class="col-md-4">
										<input id="oldName" name="oldName" type="hidden" value="${role.name}">
										<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
									</div>
										<span class="help-inline"><font color="red">*</font> </span>
								</div>
								
								
								<c:choose>
									<c:when test="${fns:getUser().identity eq 1}">
										<div class="form-group">
											<label class="col-md-3 control-label"><spring:message code="role_type"/>:</label>
											<div class="col-md-4">
												<!-- <form:input path="roleType" htmlEscape="false" maxlength="50" class="required"/>
												<span class="help-inline" title="activiti有3种预定义的组类型：security-role、assignment、user 如果使用Activiti Explorer，需要security-role才能看到manage页签，需要assignment才能claim任务">
													工作流组用户组类型（security-role：管理员、assignment：可进行任务分配、user：普通用户）</span> -->
												<form:select path="roleType" class="form-control">
													<%-- <form:option value="assignment"><spring:message code="task_allot"/></form:option> --%>
													<form:option value="security-role"><spring:message code="manager"/></form:option>
													<form:option value="user"><spring:message code="ordinary_man"/></form:option>
												</form:select>
												<%-- <span class="help-inline" title="activiti有3种预定义的组类型：security-role、assignment、user 如果使用Activiti Explorer，需要security-role才能看到manage页签，需要assignment才能claim任务">
													<spring:message code="role_type_info"/></span> --%>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="roleType" value="user"/>
									</c:otherwise>
								</c:choose>
								
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="data_range"/>:</label>
									<div class="col-md-4">
										<form:select path="dataScope" class="form-control">
											<form:options items="${fns:getDictList('SYS_DATA_SCOPE')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
										</form:select>
										<%-- <span class="help-inline"><spring:message code="data_range_info"/></span> --%>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="role_permission"/>:</label>
									<div class="col-md-4">
										<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
										<form:hidden path="menuIds"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="remarks"/>:</label>
									<div class="col-md-4">
										<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
									</div>
								</div>
					
								
									
								<div class="form-actions">
									<div class="row">
										<div class="col-md-offset-3 col-md-9">
											<shiro:hasPermission name="sys:role:edit"><button type="submit" class="btn btn-circle blue">&nbsp;&nbsp;<spring:message code="submit"/>&nbsp;&nbsp;</button></shiro:hasPermission>
											<button type="button" class="btn btn-circle grey-salsa btn-outline" onclick="history.go(-1)"><spring:message code="cancel"/></button>
										</div>
									</div>
								</div>
								
								
							</form:form>
						
						<!-- END FORM-->
					</div>
					
				</div>
				
			</div>
		</div>
	</div>
</div>




</body>
</html>