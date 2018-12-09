<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>来函信息</title>

</head>
<body>

<div class="page-content">
	<div class="row">
	<h3 class="page-title">
		<spring:message code="requestInfo"/>
	</h3>
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty requestInfo.id}"><spring:message code="edit"/></c:if><c:if test="${empty requestInfo.id}"><spring:message code="add"/></c:if></div>
				</div>
				
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form:form action="${ctx}/cfg/request/saveOrUpdate" modelAttribute="requestInfo" class="form-horizontal" id="inputForm" method="post" >
					<sys:message content="${message}"/>
					<input type="hidden"  name="id" value="${requestInfo.id}"/>
						<div class="form-body">
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="request_number"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" maxlength="512" name="requestTitle" value="${requestInfo.requestTitle}"><!--requestTitle当成来函函号使用 -->
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="request_organization"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" maxlength="128" name="requestOrg" value="${requestInfo.requestOrg}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="request_time"></spring:message>:</label>
								<div class="col-md-4">
									<input id="requestTime" name="requestTime" type="text" readonly="readonly" maxlength="20" class="form-control input-medium Wdate required"
									value="<fmt:formatDate value="${requestInfo.requestTime}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="title"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" name="requestNumber" maxlength="512" value="${requestInfo.requestNumber}"><!--requestTitle当成来函标题使用 -->
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="content"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" name="requestContent" maxlength="4000" value="${requestInfo.requestContent}">
								</div>
							</div>
							<div class="form-group last">
								<label class="col-md-3 control-label"><th><spring:message code="special_task"/>:</label>
								<div class="col-md-4">
									<form:select path="taskId" class="selectpicker select2">
										<form:option value=""><spring:message code="select"/></form:option>
										<c:forEach items="${taskInfos}" var="taskInfo">
											<form:option value="${taskInfo.id}">${taskInfo.taskName}</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</div>
						<div class="form-actions">
							<div class="row">
								<div class="col-md-offset-3 col-md-9">
									<button type="submit" class="btn btn-circle blue"><spring:message code="submit"></spring:message></button>
									<button type="button" class="btn btn-circle grey-salsa btn-outline" onclick="history.go(-1)"><spring:message code="cancel"></spring:message></button>
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
<script type="text/javascript">
		 $(document).ready(function() {
	 		
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('onloading...');
					form.submit();
				},
				errorContainer: "#messageBox",
			});
		});  
	</script>
</body>
</html>