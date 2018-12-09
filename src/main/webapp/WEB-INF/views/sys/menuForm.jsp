<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
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

	<div class="theme-panel hidden-xs hidden-sm">
		
		<button type="button" class="btn btn-default" onclick="history.go(-1)">&nbsp;<spring:message code="back"/>&nbsp;</button>

	</div>
		
		
	<h3 class="page-title">
		<spring:message code="menuManage"/>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><shiro:hasPermission name="sys:menu:edit"><c:if test="${not empty menu.id}"><spring:message code="edit"/></c:if><c:if test="${empty menu.id}"><spring:message code="add"/></c:if></shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit"><spring:message code="show"/></shiro:lacksPermission></div>
						
				</div>
				
				<div class="portlet-body form">
					
				
					<div class="form-body">

						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/saveOrUpdate" method="post" class="form-horizontal">
							<form:hidden path="id"/>
							<sys:message content="${message}"/>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="parent_menu"></spring:message>:</label>
								<div class="col-md-4">
					                <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.code}"
										title="${menu.parent.code}" url="/sys/menu/treeData" extId="${menu.id}" cssClass="required form-control"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font> <spring:message code="menu"/>:</label>
								<div class="col-md-4">
									<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="unicode"/>:</label>
								<div class="col-md-4">
									<form:input path="code" htmlEscape="false" maxlength="50" class="required form-control"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="link"/>:</label>
								<div class="col-md-4">
									<form:input path="href" htmlEscape="false" maxlength="2000" class="form-control"/>
									<span class="help-inline"><spring:message code="click_menu_turn"/></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="aim"/>:</label>
								<div class="col-md-4">
									<form:input path="target" htmlEscape="false" maxlength="10" class="form-control"/>
									<span class="help-inline"><spring:message code="link_window_info"/></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="is_top_menu"/>:</label>
								<div class="col-md-4">
									
									<input type="radio" name="isTop"  value="1" <c:if test="${menu.isTop==1}">checked="checked"</c:if>><spring:message code="yes"/>
									<input type="radio" name="isTop" value="0" <c:if test="${empty menu.isTop or menu.isTop==0}">checked="checked"</c:if>><spring:message code="no"/>
									<span class="help-inline"><spring:message code="is_top_menu_info"/></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="function id"/>:</label>
								<div class="col-md-4">
									<form:input path="functionId" htmlEscape="false" maxlength="10" class="form-control"/>
									<span class="help-inline"><spring:message code="function_id"/></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="icon"/>:</label>
								<div class="col-md-4">
									<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="sort"/>:</label>
								<div class="col-md-4">
									<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits form-control"/>
									<span class="help-inline"><spring:message code="sort_info"/></span>
								</div>
							</div>
							
						
							
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="visible"/>:</label>
								<div class="col-md-4">
						           	<c:forEach items="${fns:getDictList('SYS_SHOW_HIDE')}" var="isShowC">
							           	<label class="radio-inline">
							           		<input type="radio" name="isShow" value="${isShowC.itemCode }" class="required areaControl"
							           			<c:if test="${menu.isShow==isShowC.itemCode}">checked</c:if>
							           		><spring:message code="${isShowC.itemValue }"/>
							           	</label>
			               			</c:forEach></br>
									<span class="help-inline"><spring:message code="is_show_sysmenu"/></span>
						           <div for="isShow"></div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="permission_mark"/>:</label>
								<div class="col-md-4">
									<form:input path="permission" htmlEscape="false" maxlength="100" class="form-control"/>
									<span class="help-inline"><spring:message code="control_permission_info"/></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="remarks"/>:</label>
								<div class="col-md-4">
									<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
								</div>
							</div>
			
							
							<div class="form-actions">
								<div class="row">
									<div class="col-md-offset-3 col-md-9">
										<shiro:hasPermission name="sys:menu:edit"><button type="submit" class="btn btn-circle blue">&nbsp;&nbsp;<spring:message code="submit"/>&nbsp;&nbsp;</button></shiro:hasPermission>
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