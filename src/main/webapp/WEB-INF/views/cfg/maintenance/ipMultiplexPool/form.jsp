<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	/* $("select[name='protocol']").parents(".form-group").addClass("hidden");
	$("select[name='direction']").parents(".form-group").addClass("hidden"); */
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
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
			loading('onloading...');
			form.submit();
		},
		errorContainer: "#messageBox"
	});
});

</script>
</head>
<body>

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
                        <c:if test="${empty _cfg.cfgId}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${!empty _cfg.cfgId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/maintenance/ipMultiplexPoolCfg/saveOrUpdate" method="post" class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="1">
                       	<input type="hidden" id="areaType" name="areaType" value="1">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode"
                   				 isMaat="${region.isMaat}"
		                   		 serviceType="${region.configServiceType}"
		                   		 ipPortShow="${region.configIpPortShow}"
		                   		 ipType="${region.configIpType}"
		                   		 ipPattern="${region.configIpPattern}"
		                   		 portPattern="${region.configPortPattern}"
		                   		 direction="${region.configDirection}"
		                   		 protocol="${region.configProtocol}"
		                   		 regionType="${region.regionType}"
                   				 value="${region.configRegionCode}">
                   			</c:if>
                   		</c:forEach>
                          <div class="form-body">
                              <div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
								            <div class="col-md-6">
								                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
										    <div for="cfgDesc"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group"> 
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group"/></label>
								            <div class="col-md-6">
								            	<select name="policyGroup" class="selectpicker show-tick form-control required">
													<option value="0" <c:if test="${empty _cfg.policyGroup }">selected</c:if>><spring:message code="select"/></option>
					               					<c:forEach items="${policyGroups}" var="policyGroup">
					               							<option value="${policyGroup.groupId}" <c:if test="${_cfg.policyGroup==policyGroup.groupId }">selected</c:if>><spring:message code="${policyGroup.groupName}"/></option>
							              			</c:forEach>
								                </select>
								            </div>
								              <div for="policyGroup"></div>
								        </div>
								    </div>
								    <div class="col-md-6 hidden">
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
								</div>
								<div class="row ipInfo">
									<div class="row">
									    <div class="col-md-6 hidden" >
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
									            <div class="col-md-6">
									                <select name="ipType"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
															<option value="${ipTypeC.itemCode}" <c:if test="${_cfg.ipType==ipTypeC.itemCode || (_cfg.ipType==null && ipTypeC.itemCode==4)}">selected</c:if>><spring:message code="${ipTypeC.itemValue}"/></option>
														</c:forEach>
													</select>
									            </div>
											    <div for="ipType"></div>
									        </div>
									    </div>
									     <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
									            <div class="col-md-6">
									               <select name="protocol"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
															<option value="${protocolC.itemCode}" <c:if test="${_cfg.protocol==protocolC.itemCode || (_cfg.protocol==null && protocolC.itemCode==0)}">selected</c:if>><spring:message code="${protocolC.itemValue}"/></option>
														</c:forEach>
													</select>
									            </div>
											    <div for="protocol"></div>
									        </div>
									    </div> 
									   <div class="col-md-6">
											<div class="form-group ">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="server_ip" /></label>
												<div class="col-md-6">
													<input class="form-control required ipCheck ipUnique" type="text"
														name="destIpAddress"
														value="${_cfg.destIpAddress}"
														ctx="${ctx}">
												</div>
												<div for="destIpAddress"></div>
											</div>
										</div>
									</div>
									<div class="row">
									    <div class="col-md-6 ">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
									            <div class="col-md-6">
									               <input class="form-control required digits" name="port" range="[0,65535]" type="text" value="${_cfg.port}">
									            </div>
											    <div for="port"></div>
									        </div>
									    </div>
									   <div class="col-md-6 hidden">
											<div class="form-group ">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="direction" /></label>
													<div class="col-md-6">
														<select name="direction"
															class="selectpicker show-tick form-control required">
															<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
																<option value="${directionC.itemCode}" <c:if test="${_cfg.direction==directionC.itemCode || (_cfg.direction==null && directionC.itemCode==1)}">selected</c:if>><spring:message code="${directionC.itemValue}"/></option>
															</c:forEach>
														</select>
													</div>
												<div for="direction"></div>
											</div>
										</div>
									</div>
									 
							</div>
							<div class="row  hidden">
								<div class="col-md-6">
									<div class="form-group ">
										<label class="control-label col-md-3"><font color="red">*</font>
											<spring:message code="location" /></label>
											<div class="col-md-6">
												<select name="location"
													class="selectpicker show-tick form-control required">
													<c:forEach items="${fns:getDictList('IP_LOCATION')}" var="locationC">
														<option value="${locationC.itemCode}" <c:if test="${_cfg.location==locationC.itemCode || (_cfg.location==null && locationC.itemCode==0)}">selected</c:if>><spring:message code="${locationC.itemValue}"/></option>
													</c:forEach>
												</select>
											</div>
										<div for="location"></div>
									</div>
								</div>
							</div>
                            <%@include file="/WEB-INF/include/form/basicInfo.jsp" %>
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