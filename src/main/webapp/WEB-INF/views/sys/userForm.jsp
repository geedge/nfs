<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<script type="text/javascript">
		$(document).ready(function() {
			
			
			$("#no").focus();
			
			$("#inputForm").validate({
				rules: {
					loginId: {remote: "${ctx}/sys/user/checkLoginName?oldLoginId=" + encodeURIComponent('${user.loginId}')},
					'office.name': {officeIsValid: true },
					'entity.name': {required: true }
				},
				messages: {
					loginId: {remote: "<spring:message code='name_existed'/>"},
					'office.name': {officeIsValid: "<spring:message code='company_range'/>"},
					confirmNewPassword: {equalTo: "<spring:message code='equal_password'/>"}
				},
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
			
			
			 var isValid;//是否有效
			   $.validator.addMethod(
					"officeIsValid", 
					function(value, element,param) { 
						var companyId=$("#companyId").val();
						var officeId=$("#officeId").val();
						if(param){
						
							$.ajax({
		  						type:'post',
		 						url:'${ctx}/sys/user/checkOffice',
		 						data:{officeId:officeId, companyId:companyId},
		 						dataType:'text',
		  						async:false,
		  						success:function (data,textStatus){	
									if(textStatus=="success"){
										if(data=='true'){
											isValid= true;
										}else {
											isValid= false;
										}
									}
								}
							});
							
							return isValid;
						}
					}
				);
			
		});
	</script>
</head>
<body>
<div class="page-content">

<div class="row">
<!-- 	<ul class="nav nav-tabs"> -->
<%-- 		<li><a href="${ctx}/sys/user/list">用户列表</a></li> --%>
<%-- 		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户 --%>
<%-- 			<shiro:hasPermission name="sys:user:edit"></shiro:hasPermission>${not empty user.id?'修改':'添加'} --%>
<%-- 			<shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission> --%>
<!-- 		</a></li> -->
<!-- 	</ul><br/> -->
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>
	</div>
	<h3 class="page-title">
		<spring:message code="userManage"/>
	</h3>
	
	<div class="col-md-12">
	<div class="portlet box blue">
		<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty user.id}"><spring:message code="edit"/></c:if><c:if test="${empty user.id}"><spring:message code="add"/></c:if></div>
		</div>
	
	<div class="portlet-body form">
	<form:form id="inputForm" modelAttribute="sysUser" action="${ctx}/sys/user/saveOrUpdate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<spring:message code="${message}"/>
		<div class="form-body">
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="name_image"/>:</label>
			<div class="col-md-4">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="login_name"/>:</label>
			<div class="col-md-4">
				<input id="oldLoginId" name="oldLoginId" type="hidden" value="${user.loginId}">
				<c:if test="${not empty user.id}" var="exisitUser">
					<form:input path="loginId" htmlEscape="false" maxlength="50" readonly="true" class="required userName form-control"/>
				</c:if>
				
				<c:if test="${!exisitUser}">
					<form:input path="loginId" htmlEscape="false" maxlength="50" class="required userName form-control"/>
				</c:if>
			</div>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	 	<input type="hidden" name="company.id" value="0"/>
		<input type="hidden" name="entity.id" value="0"/>
		<input type="hidden" name="office.id" value="0"/>
		<%-- <div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="organizer"/>:</label>
			<div class="col-md-4">
                <sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="company" url="/sys/office/treeData" cssClass="form-control" notAllowSelectRoot="true"/>
			</div>
		</div> --%>
		
		<%-- <div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="organizer"/>:</label>
			<div class="col-md-4">
                <sys:treeselect id="entity" name="entity.id" value="${user.entity.id}" labelName="entity.name" labelValue="${user.entity.name}"
					title="organization" url="/sys/office/treeData?type=2" cssClass="required form-control" notAllowSelectRoot="true"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="owner_group"/>:</label>
			<div class="col-md-4">
                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="group" url="/sys/office/treeData?type=3" cssClass="required form-control" notAllowSelectRoot="true"/>
			</div>
		</div> --%>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="name"/>:</label>
			<div class="col-md-4">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="password"/>:</label>
			<div class="col-md-4">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.id?'required':''} form-control"/>
			</div>
				<c:if test="${empty user.id}"><span class="help-inline"><font color="red">*</font> </span></c:if>
				<c:if test="${not empty user.id}"><span class="help-inline"><spring:message code="nochange_blank"/></span></c:if>
		</div>
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="confirm_password"></spring:message>:</label>
			<div class="col-md-4">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword" class="form-control"/>
				<c:if test="${empty user.id}"></c:if>
			</div>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<c:choose>
			<c:when test="${fns:getUser().identity eq 1}">
				<div class="form-group">
					<label class="col-md-3 control-label"><spring:message code="identify_mark"></spring:message>:</label>
					<div class="col-md-4 ">
						<form:select path="identity" class="selectpicker select2 form-control">
							<form:option value="0"><spring:message code="ordinary_man"/></form:option>
							<form:option value="1"><spring:message code="manager"/></form:option>
						</form:select>
						<span class="help-inline"><font><spring:message code="manager_info"/></font></span>
					</div>
						
				</div>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="identity" value="0"/>
			</c:otherwise>
		</c:choose>
		<input type="hidden" name="email" value="0"/>
		<%-- <div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="mail"/>:</label>
			<div class="col-md-4">
				<form:input path="email" htmlEscape="false" maxlength="100" class="required email form-control"/>
			</div>
			<span class="help-inline"><font color="red">*</font> </span>
		</div> --%>
		
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="role"/>:</label>
			<div class="col-md-8">
				
				<div class="mt-checkbox-inline">
			
					 <c:forEach items="${allRoles}" var="role">
					 	<c:if test="${fns:getUser().identity eq 1 or (fns:getUser().identity eq 0 and role.roleType eq 'user' )}">
						 	 <label class="mt-checkbox">
						 	 	<form:checkbox path="roleIdList" value="${role.id }" class="required" /> ${role.name }
							 	<span></span>
						 	 </label>
					 	 </c:if>
					 </c:forEach>
					
					 <span class="help-inline "><font color="red">*</font> </span>
				 </div> 
				
			</div>
		</div>
		
                                
		
		<c:if test="${not empty user.id}">
			<div class="form-group">
				<label class="col-md-3 control-label"><spring:message code="create_time"/>:</label>
				<div class="col-md-4">
					<label class="lbl form-control"><fmt:formatDate value="${user.createTime}" type="both" dateStyle="full"/></label>
				</div>
			</div>
			
		</c:if>
		</div>
		<div class="form-actions">
		<div class="row">
		<div class="col-md-offset-3 col-md-9">
			<shiro:hasPermission name="sys:user:edit"></shiro:hasPermission>
			<c:if test="${fns:getUser().id ne user.id }">
			<button type="submit" class="btn blue"><spring:message code="submit"></spring:message></button>
			</c:if>
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