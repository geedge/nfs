<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>

<script type="text/javascript">
var validateForm;
function selectP(){
		 $("label[for='specServiceId']").hide();
}
	$(document).ready(function() {
		$("select[name='ipType']").on("change",function(){
			var type=$(this).val();
			if(4==type){
				$("input[name='srcIpMask']").attr("placeholder","<spring:message code='arbitrary'/> : 0.0.0.0");
				$("input[name='dstIpMask']").attr("placeholder","<spring:message code='arbitrary'/> : 0.0.0.0");
			}
			if(6==type){
				$("input[name='srcIpMask']").attr("placeholder","<spring:message code='arbitrary'/> : \"::\"");
				$("input[name='dstIpMask']").attr("placeholder","<spring:message code='arbitrary'/> : \"::\"");
			}
		});
				//specServiceId校验,处理校验不能及时显示
			   jQuery.validator.addMethod("specServiceIdSelected",function(value, element) {
					var selectedValue=$(".filter-option").text();
					if(selectedValue=="<spring:message code='select' />"){
						return false;
					}else{
						return true;
						}
					}, "请选择协议名称");		
				
				$("#name").focus();
				validateForm = $("#inputForm")
						.validate(
								{
									rules : {
										specServiceId:{
											specServiceIdSelected:true
											}
										},
									messages : {
										specServiceId:{
										    specServiceIdSelected:'<spring:message code="required"/>'
									        }
										},

									submitHandler : function(form) {
										if (!validateForm.form()) {
											return false;
										}
										loading('<spring:message code="submitting"/>');
										form.submit();
									},
									errorContainer : "#messageBox",
									/* errorPlacement : function(error, element) {
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
									} */
									errorPlacement: function(error,element){
										$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
									}
								});
			});
</script>
</head>
<body>
<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
	<c:if test="${dict.itemValue eq 'social_app'}"><c:set var="app" value="${dict.itemCode}"/></c:if>
</c:forEach>

	<div class="page-content">

		<div class="theme-panel hidden-xs hidden-sm">

			<button type="button" class="btn btn-default"
				onclick="history.go(-1)">
				<spring:message code="back" />
			</button>

		</div>


		<h3 class="page-title">
			<spring:message code="agreement_ip_configuration" />
		</h3>
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${not empty specificServiceHostCfg.hostId}"><spring:message code='edit'/></c:if><c:if test="${empty specificServiceHostCfg.hostId}"><spring:message code='add'/></c:if></div>
						</div>
						<div class="tools"></div>
					</div>
					<div class="portlet-body form">
							<!-- BEGIN FORM-->
							<form:form id="inputForm" modelAttribute="specificServiceHostCfg" action="${ctx}/specific/specificServiceHostCfg/saveOrUpdate" method="post" class="form-horizontal" >
								<div class="form-body row">
								<form:hidden path="hostId" class="form-control" />
								<sys:message content="${message}" />
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><font color="red">*</font><spring:message code='protocol_name' />:</label>
									            <c:set var="spec_service_id"></c:set>
									           <div class="col-md-6">
									            <sys:treeselect id="specServiceId" name="specServiceId" value="${specificServiceCfg.parent.specServiceId}" 
												 labelName="parent.specServiceName" 
												 labelValue="${empty specificServiceHostCfg.specServiceId?spec_service_id:fns:getBySpecServiceId(specificServiceHostCfg.specServiceId).specServiceName}"
												 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=${app}" extId=""
												 cssClass="form-control required"/>
												<div for="parent.specServiceName"></div>
												</div>
										</div>
										
									</div>
									
									<div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><font color="red">*</font><spring:message code='direction' />:</label>
											<div class="col-md-6">
												<form:select name="direction" path="direction" class="select2 form-control required" >
												    <form:option value=""><spring:message code='select' /></form:option>
													<c:forEach items="${fns:getDictList('DIRECTION')}" var="dict">
									                   <form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}' /></form:option>
									                </c:forEach>
												</form:select>
												<div for="direction"></div>
											</div>
										</div>
									</div>
								</div>

								<div class="row">
								   <div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><font color="red">*</font><spring:message code='protocol' />:</label>
											<div class="col-md-6">
												<form:select name="protocol" path="protocol" class="select2 form-control required" >
												    <form:option value=""><spring:message code='select' /></form:option>
													<c:forEach items="${fns:getDictList('PROTOCOL')}" var="dict">
									                   <form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}' /></form:option>
									                </c:forEach>
												</form:select>
												<div for="protocol"></div>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><font color="red">*</font><spring:message code='ip_type' />:</label>
										    <div class="col-md-6">
												   <form:select path="ipType" class="select2 form-control required" >	
	                                                	<c:forEach items="${fns:getDictList('IP_TYPE')}" var="dict">
										                   <form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}' /></form:option>
										                </c:forEach>
										           </form:select>
									        </div>
											<div class="col-md-6" style="display:none;">
											    <input name="isAudit" value="1"/>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="ip_pattern" /></label>
											<div class="col-md-6">
												<form:select path="ipPattern"
													class="select2 form-control required">
													<c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
														<option value="${ipPatternC.itemCode}" ><spring:message code="${ipPatternC.itemValue}"/></option>
													</c:forEach>
												</form:select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><font color="red">*</font><spring:message code='server_ip' />:</label>
											<div class="col-md-6">
												<form:input class="form-control required ipCheck" type="text" path="destIpAddress" />
											<div for="destIpAddress"></div>
											</div>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="port_pattern" /></label>
											<div class="col-md-6">
												<form:select path="portPattern"
													class="select2 form-control required">
													<c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
														<option value="${portPatternC.itemCode}" ><spring:message code="${portPatternC.itemValue}"/></option>
													</c:forEach>
												</form:select>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group ">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="server_port" /></label>
											<div class="col-md-6">
												<form:input class="form-control required portCheck" type="text"
													path="destPort" />
											<div for="destPort"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-actions">
									<div class="row">
										<div class="col-md-offset-3 col-md-9">
											<shiro:hasPermission name="specific:serviceIp:edit"><button type="submit" class="btn btn-circle blue"><spring:message code="submit" /></button></shiro:hasPermission>
											<button type="button" class="btn btn-circle grey-salsa btn-outline" onclick="history.go(-1)"> <spring:message code="cancel" /></button>
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
</body>
</html>