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
						<form:form id="inputForm" modelAttribute="sysDictInfo" action="${ctx}/basics/sysDictInfo/saveOrUpdate" method="post" class="form-horizontal">
							<form:hidden path="sysDictId"/>
							<sys:message content="${message}"/>
							<c:if test="${specType == null or specType!=3 }">
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="superior_config"/>:</label>
								<div class="col-md-4">
								  <c:set var="fatherName"><spring:message code="root_node"/></c:set>
								  <input value="${sysDictInfo.parent.sysDictId == 0?fatherName:fns:getSysDictInfoById(sysDictInfo.parent.sysDictId).itemValue}" maxlength="50" class="form-control" readonly="readonly"/>
							    </div>
							</div>
							</c:if>
							<%-- <div class="form-group">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> 配置ID:</label>
								<div class="col-md-4">
								  <input value="${sysDictInfo.sysDictId}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> --%>
							<div class="form-group">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> <spring:message code="item_type"/>:</label>
								<div class="col-md-4">
								  <input value=<spring:message code="${fns:getDictLabel('SYS_DICT_ITM_TYPE',sysDictInfo.itemType,'0')}"/> maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_code"/>:</label>
								<div class="col-md-4">
								  <input value="${sysDictInfo.itemCode}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_value"/>:</label>
								<div class="col-md-4">
								  <input value="${sysDictInfo.itemValue}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<c:if test="${specType == null or specType!=3 }">
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
								<div class="col-md-4">
								  <input value=<spring:message code="${fns:getDictLabel('INT_YES_NO',sysDictInfo.isLeaf,'0')}"/> maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							</c:if>
							<%-- <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font>层级:</label>
								<div class="col-md-4">
								  <input value="${sysDictInfo.levelNo}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div> --%>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="creator"/>:</label>
								<div class="col-md-4">
								  <input value="${fns:getUserById(sysDictInfo.sysDictCreator.id).name}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="create_time"/>:</label>
								<div class="col-md-4">
                                  <input value="<fmt:formatDate value='${sysDictInfo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" maxlength="50" class="form-control" readonly="readonly" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="editor"/>:</label>
								<div class="col-md-4">
								  <input value="${fns:getUserById(sysDictInfo.sysDictEditor.id).name}" maxlength="50" class="form-control" readonly="readonly"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red"></font><spring:message code="edit_time"/>:</label>
								<div class="col-md-4">
                                  <input value="<fmt:formatDate value='${sysDictInfo.editTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" maxlength="50" class="form-control" readonly="readonly" />
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