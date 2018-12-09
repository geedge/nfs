<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/pages/css/dictInfo.css" />
<script type="text/javascript" src="${ctxStatic}/pages/scripts/dict.js"></script>
	<title><spring:message code="${cfgName}"></spring:message></title>
</head>
<body>


<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>
	</div>
		
		
 	<h3 class="page-title">
		<spring:message code="user_info"/>
	</h3> 
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><spring:message code='show'/></div>
					<div class="tools">
						</div>
				</div>
				<div class="portlet-body form">
					<div class="form-body">
						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="user" action="#" method="post" class="form-horizontal">
							<sys:message content="${message}"/>
							<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="user_name" /></label>
									<div class="col-md-4">
										 <input value="${user.userName}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-3">
										<spring:message code="config_time" /></label>
									<div class="col-md-4">
										 <input value="${user.createOn}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="edit_time" /></label>
									<div class="col-md-4">
										 <input value="${user.updatedOn}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-3">
										<spring:message code="outgoing_unicast_packets" /></label>
									<div class="col-md-4">
										 <input value="${user.outgoingUnicastPackets}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="outgoing_broadcast_packets" /></label>
									<div class="col-md-4">
										 <input value="${user.outgoingBroadcastPackets}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-3">
										<spring:message code="outgoing_broadcast_total_size" /></label>
									<div class="col-md-4">
										 <input value="${user.outgoingBroadcastTotalSize}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="incoming_unicast_packets" /></label>
									<div class="col-md-4">
										 <input value="${user.incomingUnicastPackets}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-3">
										<spring:message code="incoming_unicast_total_size" /></label>
									<div class="col-md-4">
										 <input value="${user.incomingUnicastTotalSize}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="incoming_broadcast_packets" /></label>
									<div class="col-md-4">
										 <input value="${user.incomingBroadcastPackets}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-3">
										<spring:message code="incoming_broadcast_total_size" /></label>
									<div class="col-md-4">
										 <input value="${user.incomingBroadcastTotalSize}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-4">
										<spring:message code="num_logins" /></label>
									<div class="col-md-4">
										 <input value="${user.numberOfLogins}" maxlength="50" class="form-control" readonly="readonly"/>
									</div>
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