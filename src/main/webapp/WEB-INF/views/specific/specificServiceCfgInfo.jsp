<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/pages/css/dictInfo.css" />
<script type="text/javascript" src="${ctxStatic}/pages/scripts/dict.js"></script>
	
</head>
<body>


<div class="page-content">

	<div class="theme-panel hidden-xs hidden-sm">
		
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>

	</div>
		
		
	<h3 class="page-title">
		<spring:message code="specific_service_cfg"/>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><spring:message code="show"/></div>
					<div class="tools">
					</div>
				</div>
				
				<div class="portlet-body form">
					
				
					<div class="form-body">
						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="specificServiceCfg"  class="form-horizontal">
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="cfg_type" />:</label>
								<div class="col-md-4">
									<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
										<label class="radio-inline"><input type="radio" name="cfgType" class="required" value="${dict.itemCode}" disabled
										<c:if test="${dict.itemCode==specificServiceCfg.cfgType}">checked</c:if>
										readonly="readonly"/><spring:message code="${dict.itemValue}"/></label>
									</c:forEach>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="superior_config"/>:</label>
								<div class="col-md-4">
								<c:set var="fatherName"><spring:message code="root_node"/></c:set>
								 <input value="${specificServiceCfg.parent.specServiceId == 0?fatherName:fns:getBySpecServiceId(specificServiceCfg.parent.specServiceId).specServiceName}" maxlength="50" class="form-control" readonly="readonly"/>
					             </div>
							</div>
						    <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="protocol_id" />:</label>
								<div class="col-md-4">
									<input id="specServiceId" name="specServiceId" maxlength="50" class="form-control" value="${specificServiceCfg.specServiceId}" readonly="readonly"/>
								</div>
							</div>  
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="protocol_name" />:</label>
								<div class="col-md-4">
									<form:input path="specServiceName" htmlEscape="false" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> 
						 	<%-- <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="group_id" />:</label>
								<div class="col-md-4">
									<form:input path="groupId" htmlEscape="false" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>  --%>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
								<div class="col-md-4">
					                  <input value="${fns:getDictLabel('SYS_YES_NO',specificServiceCfg.isLeaf,'0')}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> 
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="operate_time"/>:</label>
								<div class="col-md-4">
                                  <input value="<fmt:formatDate value='${specificServiceCfg.opTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" maxlength="50" class="form-control" readonly="readonly" />
								</div>
							</div> 
							 <div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="protocol_desc" />:</label>
								<div class="col-md-4">
									<form:textarea path="specServiceDesc" htmlEscape="false" maxlength="2000" class="form-control" readonly="readonly"/>
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