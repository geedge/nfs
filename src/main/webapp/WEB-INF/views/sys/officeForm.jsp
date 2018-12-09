<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('onloading...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='enter_error'/>");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
					
				}
			});
		});
	</script>
</head>
<body>
<div class="page-content">
<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty office.id}"><spring:message code="edit"/></c:if><c:if test="${empty office.id}"><spring:message code="add"/></c:if></div>
				</div>
			<div class="portlet-body form">

<!-- 	<ul class="nav nav-tabs"> -->
<%-- 		<li><a href="${ctx}/sys/office/list?id=${office.parent.id}&parentIds=${office.parentIds}">机构列表</a></li> --%>
<%-- 		<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">机构<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li> --%>
<!-- 	</ul><br/> -->
	
	
	<form:form id="inputForm" modelAttribute="sysOffice" action="${ctx}/sys/office/saveOrUpdate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<spring:message code="${message}"/>
	<div class="form-body">
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="parent_org"/>:</label>
			<div class="col-md-4">
                <sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
					title="organization" url="/sys/office/treeData" extId="${office.id}" cssClass="form-control" allowClear="${office.currentUser.admin}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="belong_area"/>:</label>
			<div class="col-md-4">
                <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="area" url="/sys/area/treeData" cssClass="required form-control"  allowClear="${office.currentUser.admin}"/>
			</div>
					<span class="help-inline"><font color="red">*</font></span>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="org_name"/>:</label>
			<div class="col-md-4">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="org_coding"/>:</label>
			<div class="col-md-4">
				<form:input path="code" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="org_type"/>:</label>
			<div class="col-md-4">
				<form:select path="type" class="selectpicker select2 form-control">
					<form:options items="${fns:getDictList('SYS_OFFICE_TYPE')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="org_level"/>:</label>
			<div class="col-md-4">
				<form:select path="grade" class="selectpicker select2 form-control">
					<form:options items="${fns:getDictList('SYS_OFFICE_GRADE')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="org_duty_type"/>:</label>
			<div class="col-md-4">
				<form:select path="jobType" class="selectpicker select2 form-control">
					<form:options items="${fns:getDictList('SYS_JOB_TYPE')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="is_useable"/>:</label>
			<div class="col-md-4">
				<form:select path="useable" class="selectpicker select2">
					<form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><spring:message code="is_useable_info"/></span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="leader"/>:</label>
			<div class="col-md-4">
				 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="user" url="/sys/office/treeData?type=6" allowClear="true" notAllowSelectParent="true" cssClass="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="vice_leader"/>:</label>
			<div class="col-md-4">
				 <sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
					title="user" url="/sys/office/treeData?type=6" allowClear="true" notAllowSelectParent="true" cssClass="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="address"/>:</label>
			<div class="col-md-4">
				<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="post_code"/>:</label>
			<div class="col-md-4">
				<form:input path="zipCode" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="master"/>:</label>
			<div class="col-md-4">
				<form:input path="master" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="phone"/>:</label>
			<div class="col-md-4">
				<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="fax"/>:</label>
			<div class="col-md-4">
				<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="mail"/>:</label>
			<div class="col-md-4">
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="remarks"/>:</label>
			<div class="col-md-4">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge form-control"/>
			</div>
		</div>
		<c:if test="${empty office.id}">
			<div class="form-group">
				<label class="col-md-3 control-label"><spring:message code="add_level"/>:</label>
				<div class="col-md-4">
				<div class="mt-checkbox-inline">
					<form:checkboxes path="childDeptList" items="${fns:getDictList('SYS_OFFICE_COMMON')}" itemLabel="itemValue" itemValue="itemCode" htmlEscape="false"/>
					<span> </span>
				</div>
				</div>
			</div>
		</c:if>
		</div>
		<div class="form-actions">
		<div class="row">
			<div class="col-md-offset-3 col-md-9">
			<shiro:hasPermission name="sys:office:edit">
			<button type="submit" class="btn blue"><spring:message code="submit"></spring:message></button>
			</shiro:hasPermission>
			<button type="button" class="btn blue btn-outline" onclick="history.go(-1)"><spring:message code="cancel"></spring:message></button>
		</div>
		</div>
		</div>
	</form:form>
</div>
</div>
</div>
</div>
</div>
</body>
</html>