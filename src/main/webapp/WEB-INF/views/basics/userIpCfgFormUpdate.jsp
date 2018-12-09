<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="asn_ip_configuration"></spring:message></title>
<script type="text/javascript">
$(function(){
	$("input[name='destPort']").parents(".form-group").addClass("hidden");
	$("select[name='portPattern']").parents(".form-group").addClass("hidden");
	$("select[name='protocol']").parents(".form-group").addClass("hidden");
	$("select[name='direction']").parents(".form-group").addClass("hidden");
	$("select[name$='ipType']").parents(".form-group").addClass("hidden");
	$("select[name$='ipPattern']").parents(".form-group").addClass("hidden");
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
			loading('onloading...');
			form.submit();
		},
		errorContainer: "#messageBox",
	});

});
</script>
</head>
<body>

<div class="page-content">

	<h3 class="page-title">
		<spring:message code="vpn_ip"></spring:message>
	</h3>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
						<spring:message code="edit"></spring:message>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/basics/vpn/update" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                   		<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="0">
                   		<input type="hidden" id="requestId" name="requestId" value="${_cfg.requestId}">
                   		<input type="hidden" id="protocolId" name="protocolId" value="${_cfg.protocolId}">
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
                          <!-- desc and action -->
                              <div class="row">
                              <div class="col-md-6">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="vpn_ip"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required ipCheck" type="text" name="destIpAddress" value="${_cfg.destIpAddress}">
								            </div>
								            <div for="destIpAddress"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="desc"/></label>
								            <div class="col-md-6">
								                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
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
							<!-- desc and action -->
                           <%-- <%@include file="/WEB-INF/include/form/complexIpInfo.jsp" %> --%>
                           <div class="row ipInfo">
								<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
								            <div class="col-md-6">
								            	 <select name="ipType" class="selectpicker show-tick form-control required">
								                   <c:forEach items="${fns:getDictList('WHITELIST_IPTYPE')}" var="ipTypeC">
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
								</div>
								<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
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
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required ipCheck" type="text" name="srcIpAddress" value="${_cfg.srcIpAddress}">
								            </div>
								            <div for="srcIpAddress"></div>
								        </div>
								    </div>
								</div>
								<div class="row  port">
								    <div class="col-md-6">
								        <div class="form-group">
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
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required portCheck" type="text" name="srcPort" value="${_cfg.srcPort}">
								            </div>
								            <div for="srcPort"></div>
								        </div>
								    </div>
								</div>
								<div class="row  destPort">
								     <div class="col-md-6">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required portCheck" type="text" name="destPort" value="${_cfg.destPort}">
								            </div>
								            <div for="destPort"></div>
								        </div>
								    </div>
								</div>
								<div class="row  protocol">
									<div class="col-md-6">
										<div class="form-group ">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="protocol" /></label>
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
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="direction" /></label>
											<div class="col-md-6">
												<select name="direction"
													class="selectpicker show-tick form-control required">
													<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
														<option value="${directionC.itemCode}" <c:if test="${_cfg.direction==directionC.itemCode || (_cfg.direction==null && directionC.itemCode==0)}">selected</c:if>><spring:message code="${directionC.itemValue}"/></option>
													</c:forEach>
												</select>
											</div>
											<div for="direction"></div>
										</div>
									</div>
								</div>
								<%-- <div class="row">
									<button type="button" class="btn btn-red-hollow center-block"
										onClick="more(this);" data-click-times="0">
										<spring:message code="show_more" />
									</button>
								</div> --%>
							</div>
                           <%--  <%@include file="/WEB-INF/include/form/basicInfo.jsp" %> --%>
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