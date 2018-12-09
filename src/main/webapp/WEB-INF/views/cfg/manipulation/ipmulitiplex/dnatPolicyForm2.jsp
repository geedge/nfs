<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	if('${fn:length(serviceList)}'>1){
		$("#serviceId").val($(".action:checked").attr("serviceId"));
		$("#protocolId").val($(".action:checked").attr("protocolId"));
	}
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
	});
	$("#ipCfgFrom").validate({
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
						<c:if test="${not empty _cfg.cfgId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="ipCfgFrom" action="${ctx}/manipulation/ipmulitiplex/saveOrUpdateDnat" method="post" class="form-horizontal">
                          <div class="form-body row">
                            <input name="cfgName" type="hidden" value="${cfgName}"/>
                            <input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                            <input type="hidden" name="compileId" value="${_cfg.compileId}">
                            <input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input name="isAreaEffective" type="hidden" value="0">
                            
                            <c:if test="${fn:length(serviceList)==1}">
				            	<c:forEach items="${serviceList}" var="service">
	                				<input type="hidden" name="action" serviceId="${service.serviceId }" protocolId="${service.protocolId }" regionCode="${service.regionCode}" value="${service.action }">
				            		<input type="hidden" id="protocolId" name="protocolId" value="${service.protocolId}">
				            		<input type="hidden" id="serviceId" name="serviceId" value="${service.serviceId}">
				            	</c:forEach>
							</c:if>
							<c:if test="${fn:length(serviceList)>1}">
									<input type="hidden" id="protocolId" name="protocolId" value="${_cfg.protocolId}">
									<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
							</c:if>
                            <!-- 配置域类型 -->
                   			<c:forEach items="${regionList}" var="region">
	                            <input type="hidden" name="${cfgName}.configServiceType" value="${region.configServiceType}">
	                            <input type="hidden" id="cfgType${region.configRegionCode}" name="cfgType" value="${region.configRegionValue}">
		                   		<input type="hidden" id="cfgRegionCode${region.configRegionCode}" name="cfgRegionCode"
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
	                   		</c:forEach>
                   			<div class="form-body">
                          <!-- desc and action -->
                              <div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
								            <div class="col-md-6">
								                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
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
																	regionCode="${service.regionCode }"
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
								</div>
	               				<div class="row ipInfo">
									<div class="row">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
									            <div class="col-md-6">
									            	 <select name="ipType" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
															<option value="${ipTypeC.itemCode}" 
																	<c:if test="${_cfg.ipType==ipTypeC.itemCode 
																		|| (_cfg.ipType==null && ipTypeC.itemCode==4)}">
																		selected
																	</c:if>
																	>
															<spring:message code="${ipTypeC.itemValue}"/>
															</option>
								               			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipType"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol" /></label>
												<div class="col-md-6">
													<select name="protocol" class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
															<option value="${protocolC.itemCode}" <c:if test="${_cfg.protocol==protocolC.itemCode || (_cfg.protocol==null && protocolC.itemCode==0)}">selected</c:if>><spring:message code="${protocolC.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="protocol"></div>
											</div>
										</div>
									</div>
									<div class="row ip">
									    <div class="col-md-6">
									        <div class="form-group hidden">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="ipPattern" class="selectpicker show-tick form-control required">
									                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
															<option value="${ipPatternC.itemCode}" <c:if test="${_cfg.ipPattern==ipPatternC.itemCode || (_cfg.ipPattern==null && ipPatternC.itemCode==3)}">selected</c:if>><spring:message code="${ipPatternC.itemValue}"/></option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group hidden">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="portPattern" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
															<option value="${portPatternC.itemCode}" <c:if test="${_cfg.portPattern==portPatternC.itemCode || (_cfg.portPattern==null && portPatternC.itemCode==1)}">selected</c:if>><spring:message code="${portPatternC.itemValue}"/></option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="portPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="original_dest_ip"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required ipCheck" type="text" name="srcIpAddress" value="${_cfg.srcIpAddress}">
									            </div>
									            <div for="srcIpAddress"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="original_dest_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="srcPort" value="${_cfg.srcPort}">
									            </div>
									            <div for="srcPort"></div>
									        </div>
									    </div>
									</div>
									<div class="row port">
									    <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="translated_dest_ip"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required ipCheck" type="text" name="destIpAddress" value="${_cfg.destIpAddress}">
									            </div>
									            <div for="destIpAddress"></div>
									        </div>
									     </div>
									    <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="translated_dest_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="destPort" value="${_cfg.destPort}">
									            </div>
									            <div for="destPort"></div>
									        </div>
									    </div>
									</div>
									<%-- <div class="row destPort">
									     <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required ipCheck" type="text" name="destIpAddress" value="${_cfg.destIpAddress}">
									            </div>
									            <div for="destIpAddress"></div>
									        </div>
									     </div>
									     <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="destPort" value="${_cfg.destPort}">
									            </div>
									            <div for="destPort"></div>
									        </div>
									    </div>
									</div> --%>
									<div class="row protocol">
										<div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction" /></label>
												<div class="col-md-6">
													<select name="direction" class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
															<option value="${directionC.itemCode}"><spring:message code="${directionC.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="direction"></div>
											</div>
										</div>
									</div>
									<br>
								</div>
	           			   		<div class="row ipmulitiplex">
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ir_type"/></label>
								            <div class="col-md-6">
								            	<select name="irType" class="selectpicker show-tick form-control required">
								                 	<option value=""><spring:message code="select"/></option>
								                  	<c:forEach items="${fns:getDictList('IR_TYPE')}" var="irTypeC">
														<option value="${irTypeC.itemCode}" <c:if test="${1==irTypeC.itemCode}">selected</c:if>><spring:message code="${irTypeC.itemValue}"/></option>
							              			</c:forEach>
								                 </select>
								            </div>
								            <div for="irType"></div>
								        </div>
								    </div>
								</div>
								<div class="row ratelimit hidden">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ratelimit"/></label>
								            <div class="col-md-6">
								            	<input class="form-control required number" range="[0,100]" type="text" name="ratelimit" value="${_cfg.ratelimit}">
								            </div>
								            <div for="ratelimit"></div>
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
</div>
</body>
</html>