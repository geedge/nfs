<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>专项任务</title>

</head>
<body>

<div class="page-content">
	<div class="row">
 	<h3 class="page-title">
		<spring:message code="special_tasks"/>
	</h3>
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty taskInfo.id}"><spring:message code="edit"/></c:if><c:if test="${empty taskInfo.id}"><spring:message code="add"/></c:if></div>
				</div>
				
				<div class="portlet-body form">
					<!-- BEGIN FORM-->
					<form:form action="${ctx}/basics/taskInfo/saveOrUpdate" modelAttribute="taskInfo" class="form-horizontal" id="inputForm" method="post" >
					<sys:message content="${message}"/>
					<input type="hidden"  name="id" value="${taskInfo.id}"/>
						<div class="form-body">
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="task_name"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" name="taskName" maxlength="64" value="${taskInfo.taskName}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="task_org"></spring:message>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control required" name="taskOrg" maxlength="128" value="${taskInfo.taskOrg}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="task_time"></spring:message>:</label>
								<div class="col-md-4">
									<input id="taskTime" name="taskTime" type="text" readonly="readonly" maxlength="20" class="form-control input-medium Wdate required"
									value="<fmt:formatDate value="${taskInfo.taskTime}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
								</div>
							</div>
							<div class="form-group last">
								<label class="col-md-3 control-label"><th><spring:message code="desc"/>:</label>
								<div class="col-md-4">
									<input type="text" class="form-control" maxlength="512" name="taskDesc" value="${taskInfo.taskDesc}">
								</div>
							</div>
						</div>
						<div class="form-actions">
							<div class="row">
								<div class="col-md-offset-3 col-md-9">
									<button type="submit" class="btn btn-circle green"><spring:message code="submit"></spring:message></button>
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
<style>
	.input-medium {
    width: 200px !important;
}
	.Wdate {
	border: #c2cad8 1px solid;
    height: 34px;
}
	
</style>
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