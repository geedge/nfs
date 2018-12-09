<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('onloading...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("<spring:message code='enter_error'/>");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
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
				<li class="active"><a href="${ctx}/sys/user/info"><spring:message code="userInfo"/></a></li>
				<li><a href="${ctx}/sys/user/modifyPwd"><spring:message code="updatePwd"/></a></li>
			</ul>
			<br />
			<div class="col-md-12">
				<div class="portlet box blue">

					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<spring:message code="userInfo" />
						</div>
					</div>
					<div class="portlet-body form">
						<form:form id="inputForm" modelAttribute="user"
							action="${ctx}/sys/user/info" method="post"
							class="form-horizontal form-striped">
							<sys:message content="${message}" />
							<div class="form-body">
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="name_image"/>:</label>
									<div class="col-md-4">
										<form:hidden id="nameImage" path="photo" htmlEscape="false"
											maxlength="255" class="input-xlarge" />
										<sys:ckfinder input="nameImage" type="images"
											uploadPath="/photo" selectMultiple="false" maxWidth="100"
											maxHeight="100" />
									</div>
								</div>

								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="name"/>:</label>
									<div class="col-md-4">
										<form:input path="name" htmlEscape="false" maxlength="50"
											class="required form-control" readonly="true" />
									</div>
								</div>
							 	<div class="form-group" hidden="hidden">
									<label class="col-md-3 control-label"><spring:message code="mail"/>:</label>
									<div class="col-md-4">
										<form:input path="email" htmlEscape="false" value="0" maxlength="50"
											class="email form-control" />
									</div>
								</div>
								<%--
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="department"/>:</label>
									<div class="col-md-4">
										<label class="lbl control-label">${user.office.name}</label>
									</div>
								</div> --%>


								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message code="role_name"/>:</label>
									<div class="col-md-4">
										<label class="lbl control-label">${user.roleNames}</label>
									</div>
								</div>
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
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>