<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/pages/css/dictInfo.css" />
	<title>配置管理</title>
</head>
<body>


<div class="page-content">

	<div class="theme-panel hidden-xs hidden-sm">
		
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>

	</div>
		
		
	<h3 class="page-title">
			<spring:message code="configuration_manage"/>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><spring:message code="show"/></div>
				</div>
				
				<div class="portlet-body form">
					
				
					<div class="form-body">

						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="serviceDictInfo" action="${ctx}/basics/serviceDictInfo/saveOrUpdate" method="post" class="form-horizontal">
							<form:hidden path="serviceDictId"/>
							<sys:message content="${message}"/>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="superior_config"/>:</label>
								<div class="col-md-4">
								<c:set var="fatherName"><spring:message code="root_node"/></c:set>
								  <input value="${serviceDictInfo.parent.serviceDictId == 0?fatherName:fns:getServiceDictInfoById(serviceDictInfo.parent.serviceDictId).itemValue}" maxlength="50" class="form-control" readonly="readonly"/>
							    </div>
							</div>
							<%-- <div class="form-group">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> 配置ID:</label>
								<div class="col-md-4">
								  <input value="${serviceDictInfo.serviceDictId}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> --%>
							<div class="form-group">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> <spring:message code="item_type"/>:</label>
								<div class="col-md-4">
								  <input value=<spring:message code="${fns:getDictLabel('SERVICE_DICT_ITM_TYPE',serviceDictInfo.itemType,'0')}"/> maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
						<%-- 	<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_code"/>:</label>
								<div class="col-md-4">
								  <input value="${serviceDictInfo.itemCode}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> --%>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_value"/>:</label>
								<div class="col-md-4">
								  <input value="${serviceDictInfo.itemValue}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
								<div class="col-md-4">
								  <input value=<spring:message code="${fns:getDictLabel('INT_YES_NO',serviceDictInfo.isLeaf,'0')}"/>  maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<%-- <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font>层级:</label>
								<div class="col-md-4">
								  <input value="${serviceDictInfo.levelNo}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> --%>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="creator"/>:</label>
								<div class="col-md-4">
								  <input value="${fns:getUserById(serviceDictInfo.serviceDictCreator.id).name}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="create_time"/>:</label>
								<div class="col-md-4">
                                  <input value="<fmt:formatDate value='${serviceDictInfo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" maxlength="50" class="form-control" readonly="readonly" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="editor"/>:</label>
								<div class="col-md-4">
								  <input value="${fns:getUserById(serviceDictInfo.serviceDictEditor.id).name}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="edit_time"/>:</label>
								<div class="col-md-4">
                                  <input value="<fmt:formatDate value='${serviceDictInfo.editTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" maxlength="50" class="form-control" readonly="readonly" />
                             </div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="desc"/>:</label>
								<div class="col-md-4">
									<form:textarea path="itemDesc" htmlEscape="false" maxlength="2000" class="form-control" readonly="true"/>
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