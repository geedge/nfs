<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
var ipInfoDiv ;
$(function(){
	// 初始clone
	ipInfoDiv = $(".ipDiv").clone();
	if($(".ipDiv").hasClass("hidden")){
		$(".ipDiv").remove();
	}
	$(".addDiv").removeClass("hidden");
	if('${fn:length(serviceList)}'>1){
		$("#serviceId").val($(".action:checked").attr("serviceId"));
		$("#protocolId").val($(".action:checked").attr("protocolId"));
	}
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
	
	$("select[name$='ipType']").on("change",function(){
		switchIpInfo(this);
	}); 
	$("select[name$='ipPattern']").on("change",function(){
		switchIpInfo(this); 
	});
	
	ipInfoDiv.removeClass("ipDiv");
	ipInfoDiv.removeClass("hidden");
	resetIndex();
});

var resetDiv = function(obj){
	// 添加 or 删除
	if($(obj).hasClass("addDiv")){
		$(".ipInfo").last().after(ipInfoDiv.clone());
	}else{
		if($(".ipInfo").length == 1){
			top.$.jBox.tip("<spring:message code='one_more'/>");
			return;
		}else{
			$(obj).parents(".ipInfo:first").remove();
		}
	}
	
	$(".selectpicker").selectpicker("render");// 重新渲染
	$("select[name$='ipType']").on("change",function(){
		switchIpInfo(this); 
	});
	$("select[name$='ipPattern']").on("change",function(){
		switchIpInfo(this); 
	});
	resetIndex();
}

var resetIndex = function(){
	// 设置索引
	$(".ipInfo").not(".hidden").each(function(index,element){
		var ind = index;
		$(this).find("input,select").each(function(){
			var oldName = $(this).attr("name");
			var newName = oldName.replace(oldName.match(/\[(\S*)\]/)[1],ind);
			$(this).attr("name",newName);
			$(this).parents(".form-group").find("div[for='"+oldName+"']").attr("for",newName);
		});
	});
}
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
                      <form:form id="cfgFrom" modelAttribute="_cfg" action="${ctx}/maintenance/ipMultiplexPoolCfg/snatSaveOrUpdate" method="post" class="form-horizontal">
                       	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
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
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="address_pool"/></label>
								            <div class="col-md-6">
								            	<!-- 地址池名称 -->
								                <input class="form-control required addrPoolUnique" type="text" name="addrPoolName" value="${_cfg.addrPoolName}" autocomplete="off" ctx="${ctx}">
								            </div>
								            <div for="addrPoolName"></div>
								        </div>
								    </div>
								    <div class="col-md-6 hidden">
								        <div class="form-group"> 
								            <label class="control-label col-md-3"><spring:message code="action"/></label>
								            <div class="col-md-6">
								            	<c:forEach items="${serviceList}" var="service" varStatus="satus">
													<label class="radio-inline"> 
														<c:if test="${_cfg.functionId eq service.functionId}">
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
								   <div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><spring:message code="desc"/>:</label>
											<div class="col-md-6">
												<form:textarea path="description" htmlEscape="false" maxlength="128" class="form-control" />
											</div>
										</div>
									</div>
								</div>
								
								<h4 class="form-section">
									<spring:message code="address_pool_ip_configuration" />
									<small>
										<span class="glyphicon glyphicon-plus addDiv"
										onClick="resetDiv(this)" title="add"></span>
									</small>
								</h4>
								<div class="row ipInfo boxSolid ipDiv <c:if test="${!empty _cfg.ipCfgs}">hidden</c:if>">							
									<input type="hidden" name="ipCfgs[0].protocolId" value="0">
	                           		<div class="row">
										<div class="pull-right">
											<span class="glyphicon glyphicon-remove pull-right" title="remove"
												onClick="resetDiv(this);" />
										</div>
									</div>
									<div class="row">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].ipType" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
															<option value="${ipTypeC.itemCode}" <c:if test="${ipTypeC.itemCode==4 }">selected</c:if> >
																<spring:message code="${ipTypeC.itemValue}"/>
															</option>
								               			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].ipType"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="protocol" /></label>
												<div class="col-md-6">
													<select name="ipCfgs[0].protocol"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
															<option value="${protocolC.itemCode}">
																<spring:message code="${protocolC.itemValue}"/>
															</option>
														</c:forEach>
													</select>
												</div>
												<div for="ipCfgs[0].protocol"></div>
											</div>
										 </div>
									</div>
									
									<div class="row ip">
										<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].ipPattern" class="selectpicker show-tick form-control required">
									                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
															<option value="${ipPatternC.itemCode}">
																<spring:message code="${ipPatternC.itemValue}"/>
															</option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].ipPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip"/></label>
									            <div class="col-md-6">
									            	<input class="form-control required ipCheck" type="text" name="ipCfgs[0].srcIpAddress">
										        </div>
												<div for="ipCfgs[0].srcIpAddress"></div>
											</div>
									    </div>
									</div>
									
									<div class="row port">
										<div class="col-md-6">
									        <div class="form-group hidden">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].portPattern" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
															<option value="${portPatternC.itemCode}">
																<spring:message code="${portPatternC.itemValue}"/>
															</option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].portPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="ipCfgs[0].srcPort">
									            </div>
									            <div for="ipCfgs[0].srcPort"></div>
									        </div>
									    </div>
									</div>
									<div class="row destPort">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required ipCheck" type="text" name="ipCfgs[0].destIpAddress">
									            </div>
									            <div for="ipCfgs[0].destIpAddress"></div>
									        </div>
									    </div>
									     <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="ipCfgs[0].destPort">
									            </div>
									            <div for="ipCfgs[0].destPort"></div>
									        </div>
									    </div>
									</div>
									<div class="row protocol">
										<div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="direction" /></label>
												<div class="col-md-6">
													<select name="ipCfgs[0].direction"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
															<option value="${directionC.itemCode}">
																<spring:message code="${directionC.itemValue}"/>
															</option>
														</c:forEach>
													</select>
												</div>
												<div for="ipCfgs[0].direction"></div>
											</div>
										</div>
									</div>
							</div>
								
								<c:forEach items="${_cfg.ipCfgs }" var="ipCfg" varStatus="index">
									<div class="row ipInfo boxSolid">
									<input type="hidden" name="ipCfgs[0].protocolId" value="0">
									<div class="row">
										<div class="pull-right">
											<span class="glyphicon glyphicon-remove pull-right" title="remove"
												onClick="resetDiv(this);" />
										</div>
									</div>
									<div class="row">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].ipType" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
															<option value="${ipTypeC.itemCode}" 
																<c:if test="${ipCfg.ipType == ipTypeC.itemCode}">
																	selected
																</c:if>>
																<spring:message code="${ipTypeC.itemValue}"/>
															</option>
								               			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].ipType"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="protocol" /></label>
												<div class="col-md-6">
													<select name="ipCfgs[0].protocol"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
															<option value="${protocolC.itemCode}"
																<c:if test="${ipCfg.protocol == protocolC.itemCode}">
																	selected
																</c:if>>
																<spring:message code="${protocolC.itemValue}"/>
															</option>
														</c:forEach>
													</select>
												</div>
												<div for="ipCfgs[0].protocol"></div>
											</div>
										</div>
									</div>
									
									<div class="row ip">
										<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].ipPattern" class="selectpicker show-tick form-control required">
									                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
															<option value="${ipPatternC.itemCode}" 
																<c:if test="${ipCfg.ipPattern == ipPatternC.itemCode}">
																	selected
																</c:if>>
																<spring:message code="${ipPatternC.itemValue}"/>
															</option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].ipPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip"/></label>
									            <div class="col-md-6">
									            	<input class="form-control required ipCheck" type="text" name="ipCfgs[0].srcIpAddress" value="${ipCfg.srcIpAddress }">
										        </div>
												<div for="ipCfgs[0].srcIpAddress"></div>
											</div>
									    </div>
									</div>
									
									<div class="row port">
										<div class="col-md-6 hidden">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
									            <div class="col-md-6">
									            	 <select name="ipCfgs[0].portPattern" class="selectpicker show-tick form-control required">
									                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
															<option value="${portPatternC.itemCode}" 
																<c:if test="${ipCfg.portPattern == portPatternC.itemCode}">
																	selected
																</c:if>>
																<spring:message code="${portPatternC.itemValue}"/>
															</option>
								              			</c:forEach>
									                 </select>
									            </div>
									            <div for="ipCfgs[0].portPattern"></div>
									        </div>
									    </div>
									    <div class="col-md-6">
									        <div class="form-group ">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="ipCfgs[0].srcPort" value="${ipCfg.srcPort }">
									            </div>
									            <div for="ipCfgs[0].srcPort"></div>
									        </div>
									    </div>
									</div>
									<div class="row destPort">
									    <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required ipCheck" type="text" name="ipCfgs[0].destIpAddress" value="${ipCfg.destIpAddress }">
									            </div>
									            <div for="ipCfgs[0].destIpAddress"></div>
									        </div>
									    </div>
									     <div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
									            <div class="col-md-6">
									                 <input class="form-control required portCheck" type="text" name="ipCfgs[0].destPort" value="${ipCfg.destPort }">
									            </div>
									            <div for="ipCfgs[0].destPort"></div>
									        </div>
									    </div>
									</div>
									<div class="row protocol">
										<div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="direction" /></label>
												<div class="col-md-6">
													<select name="ipCfgs[0].direction"
														class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
															<option value="${directionC.itemCode}" 
																<c:if test="${ipCfg.direction == directionC.itemCode}">
																	selected
																</c:if>>
																<spring:message code="${directionC.itemValue}"/>
															</option>
														</c:forEach>
													</select>
												</div>
												<div for="ipCfgs[0].direction"></div>
											</div>
										</div>
									</div>
							</div>
								</c:forEach>
								
						<br>
						<input name="isAreaEffective" type="hidden" value="0">
						<%-- <%@include file="/WEB-INF/include/form/areaInfo.jsp"%> --%>
						<br>
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
                      </form:form>
                      <!-- END FORM-->
                  </div>
              </div>
		</div>
	</div>
</div>
</body>
</html>