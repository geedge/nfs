<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>系统业务管理表单</title>
	<link href="${pageContext.request.contextPath}/static/pages/css/systemService.css" rel="stylesheet" type="text/css">
</head>
<body>
<script type="text/javascript">
	$(document).ready(function() {
		top.window.scrollTo(0, 0);
		if ("${systemServiceInfo.id}") {
			$("[name=childTableFlag]").val("1");
		}
		
		$("#inputForm").validate({
			rules: {
				'serviceId':{
					remote:"${ctx}/systemService/serviceIdValidate?id=" + encodeURIComponent($("#id").val())
				},
				'childTableFlag':{
					checkChild:true
				}
				
			},
			messages: {
				'serviceId':{
					remote:'<spring:message code="repeat"/>'+'serviceId'
				}
			},
			submitHandler: function(form){
				/* $("[for=childTableFlag]").css("display", "none");
				var flag = true;
				var tables = document.getElementsByClassName("child-required");
				if (tables.length == 0) {
					$("[for=childTableFlag]").text("<spring:message code='required'/>").css("display", "inline");
					if (!$("#serviceIdInput").val()) {
						$("[for=serviceId]").css("display", "inline");
					}
					return false;
				}
				for (var i = 0; i < tables.length; i++) {
					if (!tables[i].value || tables[i].value == '') {
						$("[for=childTableFlag]").text("<spring:message code='fill_all'/>").css("display", "inline");
						flag = false;
						break;
					}
				}
				
				if (!$("#serviceIdInput").val()) {
					$("[for=serviceId]").css("display", "inline");
					flag = false;
				}
				
				if (flag) {*/
					$("[disabled=disabled]").attr("disabled", false);
					$("#submitBtn").attr("disabled", true);//防止重复提交
					form.submit();
				/*} else {
					flag = true;
					return false;
				} */
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
		
		$.validator.addMethod(
			"checkChild", 
			function(value, element, params) {
				if (value && value == "1") {
					return true;
				} else {
					return false;
				}
			}, 
			"<spring:message code='fill_all'/>"
		);
		changeType($("#serviceType").val(), $("#serviceType"));
		
		//类型、动作不允许修改
		if ("${systemServiceInfo.id}") {
			$("#action").attr("disabled", true);
			$("#serviceType").attr("disabled", true);
			$("[name=tableType]").attr("disabled", true);
		}
	});
	
	window.onload = function() {
		$("[data-id=serviceType]").click(function() {
			now = $("#serviceType").val();
		});
	}
	
	function childValidate() {
		var flag = true;
		var c = $(".child-required, .child-required2.selectpicker");
		if (c.length == 0) {
			return false;
		}
		c.each(function(){
			if (!$(this).val()) {
				flag = false;
				return false;
			}
		});
		if (flag) {
			$("[name=childTableFlag]").val("1");
		} else {
			$("[name=childTableFlag]").val("0");
		}
	}
	
	function changeType(v, obj) {
		if ($(obj).val() == '') {
			$("#addBtn").attr("disabled", true);
		} else if ($(obj).val() == '1') {
			if ($(".table-list").length > 1) {
				alert('<spring:message code="single_service_message"/>');
				$(obj).val(v);
			} else if ($(".table-list").length == 1) {
				$("#addBtn").attr("disabled", true);
			} else if ($(".table-list").length == 0) {
				$("#addBtn").attr("disabled", false);
			}
		} else if ($(obj).val() == '2') {
			$("#addBtn").attr("disabled", false);
		} else if ($(obj).val() == '3') {
			if ($(".table-list").length > 1) {
				//alert('<spring:message code="special_service_message"/>');
				//$(obj).val(v);
			} else if ($(".table-list").length == 1) {
				//$("#addBtn").attr("disabled", true);
			} else if ($(".table-list").length == 0) {
				$("#addBtn").attr("disabled", false);
			}
		}
		
		autoServiceId();
	}
	
	//自动获取serviceId
	function autoServiceId() {
		if ("${systemServiceInfo.id}") {
			return false;
		}
		var type = $("[name=tableType]").val();
		var action = $("#action").val();
		var serviceIdPre;
		if ($("#serviceType").val() == "1" && action && type) {
			serviceIdPre = type + "0" + (action < 10 ? ("0" + action) : action);
		} else if ($("#serviceType").val() == "2" && action) {
			type = "60";
			serviceIdPre = type + (action < 10 ? ("0" + action) : action);
		} else if ($("#serviceType").val() == "3" && action) {
			type = "50";
			serviceIdPre = type + (action < 10 ? ("0" + action) : action);
		} else {
			serviceIdPre = "";
		}
		
		if (serviceIdPre) {
			$.ajax({
				type:"get",
				url:"${ctx}/systemService/newServiceId?serviceIdPre=" + serviceIdPre,
				dataType:"text",
				success: function(data) {
					if (data) {
						$("#serviceIdInput").val(data);
					}
				}
			})
		} else {
			$("#serviceIdInput").val("");
		}
		
		childValidate();
	}
	
	function addDatatable() {
		$("#datatables").append(
			'<div class="table-list col-md-10">' + 
				'<div class="table-item">' + 
					'<input type="hidden" name="sciId" value="0"/>' +
					'<input type="hidden" name="sciIsValid" value="1"/>' +
					'<span class="child-label col-md-3"><font color="red">*</font>'+'<spring:message code="table_name"/>'+':</span> ' +
					'<span><input class="child-required form-control child-input" onchange="childValidate()" name="tableName"/></span> ' +
				'</div>' +
				'<div class="table-item">' +
					'<span class="child-label col-md-3"><font color="red">*</font>'+'<spring:message code="table_type"/>'+':</span> ' + 
					'<span> ' +
						'<select class="child-required2 selectpicker select2" name="tableType" onchange="autoServiceId()" title="<spring:message code="select"/>"> ' +
							'<option value="1">IP</option>' +
							'<option value="2">'+'<spring:message code="string"/>'+'</option>' +
							'<option value="3">'+'<spring:message code="number"/>'+'</option>' +
							'<option value="4">'+'<spring:message code="strong_string"/>'+'</option>' +
						'</select>' +
					'</span> ' +
				'</div>' +
				'<div class="table-item">' +
					'<span class="child-label col-md-3"><font color="red">*</font>MAAT '+'<spring:message code="table_name"/>'+':</span> ' +
					'<span><input class="child-required form-control child-input" onchange="childValidate()" name="maatTable"/></span>' +
				'</div>' +
				'<div class="table-item">' +
					'<span class="child-label col-md-3">' + '<spring:message code="table_des"/>'+':</span> ' +
					'<span><input class="form-control child-input" name="tableDesc"/></span>' +
					'<button type="button" class="btn btn-danger del-button" onclick="del2(this)">'+'<spring:message code="delete"/>'+'</button>' +
				'</div>' +
			'</div>'
		);
		$("[name=tableType]").selectpicker('refresh');
		if ($("#serviceType").val() == '1') {
			$("#addBtn").attr("disabled", true);
		}
		
		childValidate();
	}
	
	function del(obj) {
		$(obj).parent().parent().find("[name=sciIsValid]").val("0");
		$(obj).parent().parent().css("display", "none").attr("class", "table-delete");
		changeType($("#serviceType").val(), $("#serviceType"));
	}
	
	function del2(obj) {
		$(obj).parent().parent().remove();
		changeType($("#serviceType").val(), $("#serviceType"));
	}
</script>
	


<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>
	</div>
		
	<h3 class="page-title">
		<spring:message code="sysService_manage"/>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i>
						<c:if test="${empty systemServiceInfo.id }"><spring:message code="add"/></c:if>
						<c:if test="${not empty systemServiceInfo.id }"><spring:message code="edit"/></c:if>
					</div>
				</div>
				
				<div class="portlet-body form">
					<div class="form-body">

						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="systemServiceInfo" action="${ctx}/systemService/save" method="post" class="form-horizontal">
							<form:hidden path="id" id="id"/>
							<sys:message content="${message}"/>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="service_name"/>:</label>
								<div class="col-md-4">
									<form:input path="serviceName" htmlEscape="false" maxlength="30" class="required form-control"/>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="act"/>:</label>
								<div class="col-md-4">
									<c:set var="select"><spring:message code='select'/></c:set>
									<form:select id="action" path="action" class="required form-control selectpicker select2" onchange="autoServiceId()" title="${select }">
										<form:option value="1"><spring:message code="block"/></form:option>
										<form:option value="2"><spring:message code="monitor"/></form:option>
										<form:option value="5"><spring:message code="block_white_list"/></form:option>
										<form:option value="6"><spring:message code="monitor_white_list"/></form:option>
										<form:option value="7"><spring:message code="block_monitor_white_list"/></form:option>
										<form:option value="8"><spring:message code="grey_list"/></form:option>
									</form:select>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="type"/>:</label>
								<div class="col-md-4">
									<form:select id="serviceType" path="serviceType" class="required form-control selectpicker select2" onchange="changeType(now, this)" title="${select }">
										<form:option value="1"><spring:message code="single_domain"/></form:option>
										<form:option value="2"><spring:message code="multi_domain"/></form:option>
										<form:option value="3"><spring:message code="special_service"/></form:option>
									</form:select>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="datebase_table"/>:</label>
								<div class="col-md-8" id="datatables">
									<shiro:hasPermission name="system:service:edit">
										<div>
											<button id="addBtn" type="button" class="btn btn-default" onclick="addDatatable()"><spring:message code="add"/></button>
											<input type="hidden" name="childTableFlag" value="${id }" class="required"/>
											<!-- <label for="childTableFlag" class="error" style="display:none"></label> --> 
										</div>
									</shiro:hasPermission>
									<c:forEach items="${systemServiceInfo.serviceConfigInfoList }" var="serviceConfigInfo">
										<div class="table-list col-md-10">
											<div class="table-item">
												<input type="hidden" name="sciId" value="${serviceConfigInfo.id }"/>
												<input type="hidden" name="sciIsValid" value="1"/>
												
												<span class="child-label col-md-3"><font color="red">*</font><spring:message code="table_name"/>:</span>
												<span><input class="child-required form-control child-input" onchange="childValidate()" name="tableName" value="${serviceConfigInfo.tableName }"/></span>
											</div>
											<div class="table-item">
												<span class="child-label col-md-3"><font color="red">*</font><spring:message code="table_type"/>:</span>
												<span>
													<select class="child-required2 selectpicker select2" name="tableType">
														<option value="1" <c:if test="${serviceConfigInfo.tableType eq 1}">selected="selected"</c:if>>IP</option>
														<option value="2" <c:if test="${serviceConfigInfo.tableType eq 2}">selected="selected"</c:if>><spring:message code="string"/></option>
														<option value="3" <c:if test="${serviceConfigInfo.tableType eq 3}">selected="selected"</c:if>><spring:message code="number"/></option>
														<option value="4" <c:if test="${serviceConfigInfo.tableType eq 4}">selected="selected"</c:if>><spring:message code="strong_string"/></option>
													</select>
												</span>
											</div>
											<div class="table-item">
												<span class="child-label col-md-3"><font color="red">*</font>MAAT <spring:message code="table_name"/>:</span>
												<span><input class="child-required form-control child-input" onchange="childValidate()" name="maatTable" value="${serviceConfigInfo.maatTable }"/></span>
											</div>
											<div class="table-item">
												<span class="child-label col-md-3"><spring:message code="table_des"/>:</span>
												<span><input class="form-control child-input child-desc" name="tableDesc" value="${serviceConfigInfo.tableDesc }"/></span>
												
												<button type="button" class="btn btn-danger del-button" onclick="del(this)" <c:if test="${serviceConfigInfo.isInitianlize eq 1 }">disabled="disabled" title="<spring:message code='initial_data_is_not_allowed_to_delete'/>"</c:if>><spring:message code="delete"/></button>
											</div>
										</div>
									</c:forEach>
								</div>
							</div>
								
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="service_id"/>:</label>
								<div class="col-md-4">
									<input id="serviceIdInput" class="form-control required" name="serviceId" value="${systemServiceInfo.serviceId }" readonly="readonly">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="describe"/>:</label>
								<div class="col-md-4">
									<form:textarea path="serviceDesc" htmlEscape="false" class="form-control"/>
								</div>
							</div>
							
							<div class="form-actions">
								<div class="row">
									<div class="col-md-offset-3 col-md-9">
										<shiro:hasPermission name="system:service:edit"><input id="submitBtn" type="submit" class="btn btn-circle blue" value=<spring:message code="submit"></spring:message>></shiro:hasPermission>
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
</div>


	
</body>
</html>