<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						
						//用户名验证   
						jQuery.validator.addMethod("userCheck", function(value, element) {
						    var tel =/^[A-Za-z0-9]+$$/;
						    return this.optional(element) || (tel.test(value));
						}, "用户名仅支持英文大小写、数字");	
						$("#no").focus();
						
	$("#inputForm")
								.validate(
										{
											rules : {
												userName : {
													remote : "${ctx}/maintenance/userManage/checkLoginName?oldUserName="
															+ encodeURIComponent('${user.userName}'),
													userCheck : true
												},
											},
											messages : {
												userName : {
													remote : "<spring:message code='name_existed'/>"
												},
												newUserPwd : {
													equalTo : "<spring:message code='equal_password'/>"
												}
											},
											errorPlacement : function(error,element) {
												/* $("#messageBox").text("<spring:message code='enter_error'/>");
												if (element.is(":checkbox")|| element.is(":radio")|| element.parent().is(".input-append")) {
													error.appendTo(element.parent().parent());
												} else {
													error.insertAfter(element);
												} */
												$(element).parents(".form-group").find("div[for='"+ element.attr("name")+ "']").append(error);
											},
											submitHandler : function(form) {
												loading('onloading...');
												form.submit();
											},
											errorContainer : "#messageBox"
										});

					});
</script>
</head>
<body>
	<div class="page-content">

		<div class="row">
			<h3 class="page-title">
				<spring:message code="userManage" />
			</h3>
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${not empty user.id}">
								<spring:message code="edit" />
							</c:if>
							<c:if test="${empty user.id}">
								<spring:message code="add" />
							</c:if>
						</div>
					</div>

					<div class="portlet-body form">
						<form:form id="inputForm" modelAttribute="user"
							action="${ctx}/maintenance/userManage/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<spring:message code="${message}" />
							<div class="form-body">
								<div class="form-group">
									<c:set var="vpn_ip">
										<spring:message code="vpn_ip" />
									</c:set>
									<label class="control-label col-md-3"><spring:message
											code="vpn_ip" /></label>
									<span class="help-inline"><font color="red">*</font> </span>
									<div class="col-md-4">
										<sys:treeselect id="serverIp" name="serverIp"
											value="${user.serverIp}" labelName="user.serverIp"
											notAllowSelectParent="true" enableSearch="true"
											notAllowSelectRoot="true"   checkedPS="ps"  unCheckedPS="ps"
											labelValue="${fn:substring(user.serverIp,0,(fn:length(user.serverIp)-1)) }" title="vpn_ip"
											url="/maintenance/userManage/treeData?isLeafShow=false&cfgType=3&specific=true" extId="0"
											checked="true"   cssClass="form-control required" />
									</div>
									 <div for="user.serverIp"></div>
								</div>
								
								<div class="form-group hidden">
									<label class="col-md-3 control-label"><spring:message
											code="user_name" /></label>
											<span class="help-inline"><font color="red">*</font> </span>
									<div class="col-md-4">
										<select name="userType" data-live-search="true" class="selectpicker form-control required">
											<c:forEach items="${fns:getDictList('USER_TYPE')}" var="userTypes">
												<option value="${userTypes.itemCode}" <c:if test="${user.userType==userTypes.itemCode || (user.userType==null && userTypes.itemCode eq 'VPN')}">selected</c:if>><spring:message code="${userTypes.itemValue}"/></option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message
											code="user_name" /></label>
											<span class="help-inline"><font color="red">*</font> </span>
									<div class="col-md-4">
										<input id="oldUserName" name="oldUserName" type="hidden"
											value="${user.userName}">
										<%-- <c:if test="${not empty user.id}" var="exisitUser">
											<form:input path="userName" htmlEscape="false" maxlength="50"
												readonly="true" class="required   form-control" />
										</c:if> --%>
											<c:if test="${!(empty user.id)}">
											<form:input path="userName" readonly="true" htmlEscape="false" maxlength="50"
												class="required   form-control" />
											</c:if>
											<c:if test="${empty user.id}">
											<form:input path="userName" htmlEscape="false" maxlength="50"
												class="required   form-control" />
											</c:if>
											
									</div>
									<div for="userName"></div>
								</div>

								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message
											code="password" /></label>
									<c:if test="${empty user.id}">
										<span class="help-inline"><font color="red">*</font> </span>
									</c:if>
									<div class="col-md-4">
										<input id="userPwd" name="userPwd" type="password" value=""
											maxlength="50" minlength="3"
											class="${empty user.id?'required':''} form-control" />
									</div>
									<c:if test="${not empty user.id}">
										<span class="help-inline"><spring:message
												code="nochange_blank" /></span>
									</c:if>
									<div for="userPwd"></div>
								</div>
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message
											code="confirm_password"></spring:message></label>
									<span class="help-inline"><font color="red">*</font> </span>
									<div class="col-md-4">
										<input id="newUserPwd" name="newUserPwd" type="password"
											value="" maxlength="50" minlength="3" equalTo="#userPwd"
											class="form-control" />
										<c:if test="${empty user.id}"></c:if>
									</div>
									<div for="newUserPwd"></div>
								</div>
								<div class="form-group">
									<label class="col-md-3 control-label"><spring:message
											code="desc" /></label>
									<div class="col-md-4">
										<form:textarea path="remarks" htmlEscape="false" rows="3"
											maxlength="200" class="span6 form-control" />
									</div>
								</div>
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-6">
										<div class="row">
											<div class="col-md-offset-3 col-md-8">
												<button id="save" type="submit" class="btn green">
													<spring:message code="submit" />
												</button>
												<button id="cancel" type="button" class="btn default">
													<spring:message code="cancel" />
												</button>
											</div>
										</div>
									</div>
									<div class="col-md-6"></div>
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