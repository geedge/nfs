<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "<spring:message code="equal_password"/>"}
				},
				submitHandler: function(form){
					loading('<spring:message code="loading"/>');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code="enter_error"/>");
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/info"><spring:message code="userInfo"/></a></li>
		<li class="active"><a href="${ctx}/sys/user/modifyPwd"><spring:message code="updatePwd"/></a></li>
	</ul><br/>
	<div class="col-md-12">
		<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
							<i class="fa fa-gift"></i>
					<spring:message code="updatePwd" />
				</div>
		</div>
	<div class="portlet-body form">	
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-body">
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="old_password"/>:</label>
			<div class="col-md-4">
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="new_password"/>:</label>
			<div class="col-md-4">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="sure_new_password"/>:</label>
			<div class="col-md-4">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control" equalTo="#newPassword"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
		<div class="row">
				<div class="col-md-offset-3 col-md-9">
			<button id="btnSubmit" type="submit" class="btn btn-primary">
				<spring:message code="submit"></spring:message>
			</button>
			<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="cancel"/></button>
		</div>
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