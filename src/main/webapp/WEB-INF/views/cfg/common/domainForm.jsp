<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
	$(function() {
		/* if ($("input[name='action']:checked").val() == 0x20
				|| $("input[name='action']:checked").val() == 0x60) {
			$("#cfgRegionCode").val($("#cfgRegionCodeCallback").val());
			$("#cfgType").val($("#cfgTypeCallback").val());
		} else {
			$("#cfgRegionCode").val($("#cfgRegionCodeMaat").val());
			$("#cfgType").val($("#cfgTypeMaat").val());
		} */
		$(".action").on("change", function() {
			$("#serviceId").val($(this).attr("serviceId"));
			$("#protocolId").val($(this).attr("protocolId"));
			/* if ($(this).val() == 0x20 || $(this).val() == 0x60) {
				$("#cfgRegionCode").val($("#cfgRegionCodeCallback").val());
				$("#cfgType").val($("#cfgTypeCallback").val());
				$("input[name='isAreaEffective']").each(function() {
					if ($(this).val() == 0) {
						$(this).click();
					}
				})
			} else {
				$("#cfgRegionCode").val($("#cfgRegionCodeMaat").val());
				$("#cfgType").val($("#cfgTypeMaat").val());
			} */

		});
		if ('${fn:length(serviceList)}' > 1) {
			$("#serviceId").val($(".action:checked").attr("serviceId"));
			$("#protocolId").val($(".action:checked").attr("protocolId"));
		}
		$("#cfgFrom").validate({
			errorPlacement: function(error,element){
				$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
			},
			submitHandler: function(form){
				loading('<spring:message code="onloading"/>');
				form.submit();
			},
			errorContainer: "#messageBox"
		});
	});
	
</script>
</head>
<body>
	<c:forEach items="${fns:getDictList('SPECIAL_FUNCTION_ID')}" var="sfi">
		<c:if test="${sfi.itemCode==_cfg.functionId}">
			<c:set var="specialFunctionId" value="${sfi.itemValue}"/>
		</c:if>
	</c:forEach>
	<div class="page-content">

 		<h3 class="page-title">
			<spring:message code="${_cfg.menuNameCode }"></spring:message>
		</h3>

		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${empty _cfg.cfgId}">
								<spring:message code="add"></spring:message>
							</c:if>
							<c:if test="${not empty _cfg.cfgId}">
								<spring:message code="edit"></spring:message>
							</c:if>
						</div>
					</div>
					<div class="portlet-body form">
						<!-- BEGIN FORM-->
						<form id="cfgFrom" action="${ctx}${urlPrefix}/saveOrUpdate" method="post"
							class="form-horizontal">
							<input type="hidden" name="cfgName" value="${cfgName}">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<c:if test="${fn:length(serviceList)==1}">
								<c:forEach items="${serviceList}" var="service">
									<input type="hidden" name="action" value="${service.action }">
									<input type="hidden" id="serviceId" name="serviceId"
										value="${service.serviceId}">
								</c:forEach>
							</c:if>
							<c:if test="${fn:length(serviceList)>1}">
								<input type="hidden" id="serviceId" name="serviceId"
									value="${_cfg.serviceId}">
							</c:if>
							<div class="form-body">
								<h3 class="form-section">
									<spring:message code="block_config" />
								</h3>
								<%-- <%@include file="/WEB-INF/include/form/stringInfo.jsp" %> --%>
								<div class="row stringInfo">
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><spring:message
														code="config_describe" /></label>
												<div class="col-md-6">
													<input class="form-control" type="text" name="cfgDesc"
														value="${_cfg.cfgDesc}">
												</div>
												<div for="cfgDesc"></div>
											</div>
										</div>
										<c:if test="${fn:length(serviceList)>1}">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message code="action"/></label>
									            <div class="col-md-6">
									            	<c:forEach items="${serviceList}" var="service"
														varStatus="satus">
														<label class="radio-inline"> <c:if
																test="${_cfg.functionId eq service.functionId}">
																<input type="radio" name="action"
																	serviceId="${service.serviceId }"
																	protocolId="${service.protocolId }"
																	value="${service.action }" class="required action"
																	<c:if test="${_cfg.action==service.action || (_cfg.action==null && satus.index==0)}">checked</c:if>>
																<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
																	<c:if test="${dict.itemCode eq service.action }">
																		<spring:message code="${dict.itemValue }"/>
																	</c:if>
																</c:forEach>
															</c:if>
														</label>
													</c:forEach>
									            </div>
									            <div for="action"></div>
									       </div>
									   </div>
								   </c:if>
								   <c:if test="${specialFunctionId ne null and specialFunctionId eq 'domainratelimit'}">
										    <div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ratelimit"/></label>
										            <div class="col-md-6">
										            	<input class="form-control required number" range="[0,1]" type="text" name="ratelimit" value="${_cfg.ratelimit }">
										            </div>
										            <div for="ratelimit"></div>
										        </div>
										    </div>
									</c:if>
									</div>
								</div>
								<c:forEach items="${regionList}" var="region">
		                   			<c:if test="${_cfg.functionId eq region.functionId}">
			                           <%@include file="/WEB-INF/include/form/complexInfo.jsp" %>
		                   			</c:if>
		                   		</c:forEach>
								<%@include file="/WEB-INF/include/form/areaInfo.jsp"%>
								<%@include file="/WEB-INF/include/form/basicInfo.jsp"%>
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-6">
										<div class="row">
											<div class="col-md-offset-3 col-md-8">
												<button id="save" type="submit" class="btn green">
													<spring:message code="submit" />
												</button>
												<button id="cancel" type="button" class="btn default">
													<spring:message code="cancel" />
												</button>
											</div>
										</div>
									</div>
									<div class="col-md-6"></div>
								</div>
							</div>
						</form>
						<!-- END FORM-->
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>