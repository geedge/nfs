<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		var action=$("input[name='action']:checked").val();
		switchAction(action);
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			if($(element).parents().hasClass("tagsinput")){
				$(element).parents(".col-md-6").next("div").append(error);
			}else{
				$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
			}
		},
		submitHandler: function(form){
			var flag = true;
			if(flag){
				var maxTtl=$("[name=upBoundary]").val();
				maxTtl = parseInt(maxTtl);
				var minTtl=$("[name=lowBoundary]").val();
				minTtl = parseInt(minTtl);
				if(maxTtl > 4294967295){
					message="<spring:message code='app_tcp_max_min'/>";
					$("div[for=upBoundary]").html("<label for=\"upBoundary\" class=\"error\" id=\"upBoundary-error\">"+message+"</label>");
					flag=false;
				}
				if(minTtl>maxTtl){
					message="<spring:message code='max_shouldnot_less_than_min'/>";
					$("div[for=upBoundary]").html("<label for=\"upBoundary\" class=\"error\" id=\"upBoundary-error\">"+message+"</label>");
					flag=false;
				}
			}
			
			if(flag){
				$("#appCode").val($("#specServiceIdId").val());
				loading('onloading...');
				form.submit();
			}else{
				return;
			}
		},
		errorContainer: "#messageBox",
	});
});
</script>
</head>
<body>
<div class="page-content">
	<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
 	<h3 class="page-title">
		<spring:message code="${_cfg.menuNameCode }"></spring:message>
	</h3> 
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
                        <c:if test="${empty _cfg.cfgId}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${not empty _cfg.cfgId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/app/saveAppTcpCfg" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="appCode" name="appCode" value="${_cfg.appCode}">
                       	<input type="hidden" id="behavCode" name="behavCode" value="${_cfg.behavCode}">
                       	<!-- 配置域类型 -->
                          <div class="form-body">
                          <div class="row">
                          <!-- desc and action -->
                              <div class="row">
                              		<div class="col-md-6">
								        <div class="form-group">
								        	 <c:set var="spec_service_id"><spring:message code="social_app"/></c:set>
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="social_app"/></label>
								            <div class="col-md-6">
										       <sys:treeselect id="specServiceId" name="specServiceId" value="${_cfg.specServiceId}" enableSearch="true"
												 labelName="parent.specServiceName" notAllowSelectRoot="true" notAllowSelectParent="true" enableAddBtn="true"
												 labelValue="${empty _cfg.specServiceId?spec_service_id:fns:getBySpecServiceId(_cfg.specServiceId).specServiceName}"
												 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=1" extId=""
												 cssClass="form-control required"/>
								            </div>
								            <div for="parent.specServiceName"></div>
								        </div>
								    </div>
                              		
								    <div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
											<spring:message code="config_describe" /></label>
											<div class="col-md-6">
												<input class="form-control" type="text"
													name="cfgDesc"
													value="${_cfg.cfgDesc}">
											</div>
											<div for="cfgDesc"></div>
										</div>
								   </div>
								</div>
								 <div class="row">
									<div class="col-md-6 hidden">
										<div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="action"/></label>
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
								</div>	
								<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="min_tcp"/></label>
								            <div class="col-md-6">
								                <input class="form-control required number"  placeholder="<spring:message code="tcp_unit" />" type="text" name="lowBoundary" value="${_cfg.lowBoundary}" min="0">
								            </div>
										    <div for="lowBoundary"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="max_tcp"/></label>
								            <div class="col-md-6">
		 						                <input class="form-control required number" placeholder="<spring:message code="tcp_unit" />" type="text" name="upBoundary" value="${_cfg.upBoundary}" min="0">
								            </div>
										    <div for="upBoundary"></div>
								        </div>
								    </div>
								</div>
								<!-- dolog begin-->
								<div class="row " >
									<div class="col-md-6 hidden">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message code="do_log" /></label>
											<c:forEach items="${fns:getDictList('DO_LOG') }" var="dict">
												<c:if test="${dict.itemCode eq 2}">
													<input type="radio" name="doLog" checked value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
												</c:if>
												<%-- <c:choose>
													<c:when test="${dict.itemCode eq _cfg.doLog}">
														<label class="radio-inline">
															<input type="radio" name="doLog" checked value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
														</label>
													</c:when>
													<c:otherwise>
														<label class="radio-inline">
															<input type="radio" name="doLog" value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
														</label>
													</c:otherwise>
												</c:choose> --%>
											</c:forEach>
										</div>
									</div>
								</div>
								<!-- dolog end-->
								
								<c:forEach items="${regionList}" var="region">
                   				<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   				</c:if>
                   				<input type="hidden" name="exprType" value="0">
                   				<input type="hidden" name="matchMethod" value="0">
                   				<input type="hidden" name="isHexbin" value="0">
                   		       </c:forEach>
							</div>
							<input name="isAreaEffective" type="hidden" value="0">
							<input type="hidden" name="requestId" value="0"/>
							<input type="hidden" name="classify" value="0"/>
							<input type="hidden" name="attribute" value="0"/>
							<input type="hidden" name="lable" value="0"/>
                          </div>
                          <div class="form-actions">
                              <div class="row">
                                  <div class="col-md-6">
                                      <div class="row">
                                          <div class="col-md-offset-3 col-md-8">
                                              <button id="save" type="submit" class="btn green"><spring:message code="submit"/></button>
                                              <button id="cancel" type="button" class="btn default"><spring:message code="cancel"/></button>
                                          </div>
                                      </div>
                                  </div>
                                  <div class="col-md-6"> </div>
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