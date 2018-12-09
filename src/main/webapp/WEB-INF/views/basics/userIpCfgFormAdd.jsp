<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="asn_ip_configuration"></spring:message></title>
<script type="text/javascript">
var defaultIpInfo;
$(document).ready(function(){
	$("input[name$='destPort']").parents(".form-group").addClass("hidden");
	$("select[name$='portPattern']").parents(".form-group").addClass("hidden");
	$("select[name$='protocol']").parents(".form-group").addClass("hidden");
	$("select[name$='direction']").parents(".form-group").addClass("hidden");
	
	$("select[name$='ipType']").parents(".form-group").addClass("hidden");
	$("select[name$='ipPattern']").parents(".form-group").addClass("hidden");
	defaultIpInfo=$(".ipInfo").clone();
	
	$("#cfgFrom").validate({
		rules : {
			destIpAddress : {
				remote : "${ctx}/basics/vpn/checkIp?oldDestIpAddress="
						+ encodeURIComponent('${_cfg.ipReuseIpCfgs[0].destIpAddress}')
			},
		},
		messages : {
			destIpAddress : {
				remote : "<spring:message code='name_existed'/>"
			}
		},
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
var reSort=function(obj,index){
	$(obj).find("input,select").each(function(){
		var name=$(this).attr("name");
		if(name.indexOf("ipReuseIpCfgs[")>-1){
			var namePrefix="ipReuseIpCfgs[";
			var nameSubfix=name.substring(name.indexOf("]"));
			var nameNew=namePrefix+index+nameSubfix;
			if(nameNew!=name){
				$(this).attr("name",nameNew);
				$(this).parents(".form-group").find("div[for='"+name+"']").attr("for",nameNew);
			}
		}
	})
}
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
                        <spring:message code="add"></spring:message>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/basics/vpn/save" method="post" class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="0">
                   		<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="0">
                   		<input type="hidden" id="action" name="action" value="0">
                   		<input type="hidden" id="compileId" name="compileId" value="0">
                   		<input type="hidden" id="requestId" name="requestId" value="0">
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
								            <input id="oldDestIpAddress" name="oldDestIpAddress" type="hidden"
											value="${_cfg.ipReuseIpCfgs[0].destIpAddress}">
								                 <input class="form-control required ipCheck destIpAddress"  id="destIpAddress"  type="text" name="ipReuseIpCfgs[0].destIpAddress" value="${_cfg.ipReuseIpCfgs[0].destIpAddress}">
								            </div>
								            <div for="ipReuseIpCfgs[0].destIpAddress"></div>
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
								   
								</div>
							<!-- desc and action -->
<%-- 							<h4 class="form-section">
								<spring:message code="asn_ip_configuration" />
								<small> <span
									class="glyphicon glyphicon-plus  asnIpAdd"
									onClick="addContent(this,'ipInfo')" title="add"></span></small>
							</h4> --%>
                           <div class="row ipInfo boxSolid asnIp hidden disabled">
                           		<input type="hidden" name="ipReuseIpCfgs[0].protocolId" value="0">
      <!--                      		<div class="row">
									<div class="pull-right">
										<span class="glyphicon glyphicon-remove pull-right" title="remove"
											onClick="delContent(this,'ipInfo');" />
									</div>
								</div> -->
								<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
								            <div class="col-md-6">
								            	 <select name="ipReuseIpCfgs[0].ipType" class="selectpicker show-tick form-control required">
								                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
														<option value="${ipTypeC.itemCode}" 
																<c:if test="${_cfg.ipReuseIpCfgs[0].ipType==ipTypeC.itemCode 
																	|| (_cfg.ipReuseIpCfgs[0].ipType==null && ipTypeC.itemCode==4)}">
																	selected
																</c:if>
																>
														<spring:message code="${ipTypeC.itemValue}"/>
														</option>
							               			</c:forEach>
								                 </select>
								            </div>
								            <div for="ipReuseIpCfgs[0].ipType"></div>
								        </div>
								    </div>
								</div>
								<div class="row ip">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
								            <div class="col-md-6">
								            	 <select name="ipReuseIpCfgs[0].ipPattern" class="selectpicker show-tick form-control required">
								                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
														<option value="${ipPatternC.itemCode}" <c:if test="${_cfg.ipReuseIpCfgs[0].ipPattern==ipPatternC.itemCode || (_cfg.ipReuseIpCfgs[0].ipPattern==null && ipPatternC.itemCode==3)}">selected</c:if>><spring:message code="${ipPatternC.itemValue}"/></option>
							              			</c:forEach>
								                 </select>
								            </div>
								            <div for="ipReuseIpCfgs[0].ipPattern"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required ipCheck" type="text" name="ipReuseIpCfgs[0].srcIpAddress" value="${_cfg.ipReuseIpCfgs[0].srcIpAddress}">
								            </div>
								            <div for="ipReuseIpCfgs[0].srcIpAddress"></div>
								        </div>
								    </div>
								</div>
								<div class="row  port">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
								            <div class="col-md-6">
								            	 <select name="ipReuseIpCfgs[0].portPattern" class="selectpicker show-tick form-control required">
								                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
														<option value="${portPatternC.itemCode}" <c:if test="${_cfg.ipReuseIpCfgs[0].portPattern==portPatternC.itemCode || (_cfg.ipReuseIpCfgs[0].portPattern==null && portPatternC.itemCode==1)}">selected</c:if>><spring:message code="${portPatternC.itemValue}"/></option>
							              			</c:forEach>
								                 </select>
								            </div>
								            <div for="ipReuseIpCfgs[0].portPattern"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required portCheck" type="text" name="ipReuseIpCfgs[0].srcPort" value="${_cfg.ipReuseIpCfgs[0].srcPort}">
								            </div>
								            <div for="ipReuseIpCfgs[0].srcPort"></div>
								        </div>
								    </div>
								</div>
								<div class="row  destPort">
								     <div class="col-md-6">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required portCheck" type="text" name="ipReuseIpCfgs[0].destPort" value="${_cfg.ipReuseIpCfgs[0].destPort}">
								            </div>
								            <div for="ipReuseIpCfgs[0].destPort"></div>
								        </div>
								    </div>
								</div>
								<div class="row  protocol">
									<div class="col-md-6">
										<div class="form-group ">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="protocol" /></label>
											<div class="col-md-6">
												<select name="ipReuseIpCfgs[0].protocol"
													class="selectpicker show-tick form-control required">
													<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
														<option value="${protocolC.itemCode}" <c:if test="${_cfg.ipReuseIpCfgs[0].protocol==protocolC.itemCode || (_cfg.ipReuseIpCfgs[0].protocol==null && protocolC.itemCode==0)}">selected</c:if>><spring:message code="${protocolC.itemValue}"/></option>
													</c:forEach>
												</select>
											</div>
											<div for="ipReuseIpCfgs[0].protocol"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="direction" /></label>
											<div class="col-md-6">
												<select name="ipReuseIpCfgs[0].direction"
													class="selectpicker show-tick form-control required">
													<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
														<option value="${directionC.itemCode}" <c:if test="${_cfg.ipReuseIpCfgs[0].direction==directionC.itemCode || (_cfg.ipReuseIpCfgs[0].direction==null && directionC.itemCode==0)}">selected</c:if>><spring:message code="${directionC.itemValue}"/></option>
													</c:forEach>
												</select>
											</div>
											<div for="ipReuseIpCfgs[0].direction"></div>
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