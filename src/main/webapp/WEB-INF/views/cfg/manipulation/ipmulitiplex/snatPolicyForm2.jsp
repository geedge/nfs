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
	
	// 获取地址池信息
	var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.lastIndexOf("/nis")+4);
	$.ajax({
		type:'get',
		url:pathName+'/manipulation/ipmulitiplex/ajaxGetAddrPools',
		dataType:'json',
		success:function(data){
			if(data != null){
				//$("#addrPoolSel").empty();
				$.each(data, function(i, n){
					if("${_cfg.addrPoolId}" == data[i].cfgId){
						var option = new Option(data[i].addrPoolName, data[i].cfgId, null, true);
					}else{
						var option = new Option(data[i].addrPoolName, data[i].cfgId);
					}
					$("#addrPoolSel")[0].options.add(option);
				});
				$("#addrPoolSel").selectpicker("refresh");
			}
		}
	});
	
	$("select[name$='ipType']").selectpicker();
	switchUserType($("select[name$='userType']"));
	$("select[name$='userType']").on("change",function(){
		switchUserType(this);
	});
	$("select[name$='ipType']").on("change",function(){
		switchIpInfo(this);
	}); 
	$("select[name$='ipPattern']").on("change",function(){
		switchIpInfo(this); 
	});
});

var switchUserType = function(obj){
	var type = $(obj).val();
	if(type == ''){
		$(".user").parents(".col-md-6").addClass("hidden");
		$(".clientIp").parents(".col-md-6").addClass("hidden");
		$(".ipPattern").parents(".col-md-6").addClass("hidden");
	}else if(type == 'VPN'){
		$(".user").parents(".col-md-6").removeClass("hidden");
		$(".clientIp").parents(".col-md-6").addClass("hidden");
		$(".ipPattern").parents(".col-md-6").addClass("hidden");
	}else if(type == 'SIPv4'){
		$(".user").parents(".col-md-6").addClass("hidden");
		$(".clientIp").parents(".col-md-6").removeClass("hidden");
		$(".ipPattern").parents(".col-md-6").removeClass("hidden");
		$("select[name$='ipType']").selectpicker("val","4");
	}else{
		$(".user").parents(".col-md-6").addClass("hidden");
		$(".clientIp").parents(".col-md-6").removeClass("hidden");
		$(".ipPattern").parents(".col-md-6").removeClass("hidden");
		$("select[name$='ipType']").selectpicker("val","6");
	}
	$("select[name$='ipType']").trigger("change");
	$("select[name$='ipType']").selectpicker("refresh");
	
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
						<c:if test="${not empty _cfg.cfgId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="ipCfgFrom" action="${ctx}${urlPrefix}/saveOrUpdateSnat" method="post" class="form-horizontal">
                          <div class="form-body row">
                            <input name="cfgName" type="hidden" value="${cfgName}"/>
                            <input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                            <input type="hidden" name="compileId" value="${_cfg.compileId}">
                            <input type="hidden" name="functionId" value="${_cfg.functionId}">
                            	<input type="hidden" name="translateParam" value="byconn=10">
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
								    <!-- dolog begin-->
									<!-- <div class="row doLog"> -->
										<div class="col-md-6">
											<div class="form-group hidden">
												<label class="control-label col-md-3"><spring:message code="do_log" /></label>
												<c:forEach items="${fns:getDictList('DO_LOG') }" var="dict">
													<c:choose>
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
													</c:choose>
												</c:forEach>
											</div>
										</div>
									<!-- </div> -->
									<!-- dolog end-->
								    
								    <div class="col-md-6">
										<div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="address_pool"/></label>
								            <div class="col-md-6">
								            	<select id="addrPoolSel" name="addrPoolId" class="selectpicker show-tick form-control required" data-live-search="true" data-live-search-placeholder="search">
				               						<option value="" ><spring:message code="select"/></option>
				               					</select>
								            </div>
								            <div for="addrPoolId"></div>
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
								<div class="row">
								<div class="row">
									<div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="user_type"/></label>
								            <div class="col-md-6">
								            	<select name="userType" class="selectpicker show-tick form-control required">
				               						<option value=""><spring:message code="select"/></option>
				               						<option value="VPN" <c:if test="${_cfg.userType eq 'VPN' }">selected</c:if>><spring:message code="policy_vpn_user"/></option>
				               						<option value="SIPv4" <c:if test="${_cfg.userType eq 'SIPv4' }">selected</c:if>><spring:message code="policy_sipv4_user"/></option>
				               						<option value="SIPv6" <c:if test="${_cfg.userType eq 'SIPv6' }">selected</c:if>><spring:message code="policy_sipv6_user"/></option>
								                </select>
								            </div>
								            <div for="userType"></div>
								        </div>
								    </div>
									<div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="user"/></label>
								            <div class="col-md-6">
								            	<select name="userName" class="selectpicker show-tick form-control required user" data-live-search="true" data-live-search-placeholder="search">
				               						<option value="" ><spring:message code="select"/></option>
				               						<c:forEach items="${users}" var="user">
														<option value="${user.userName}" <c:if test="${_cfg.userName eq user.userName }">selected</c:if>><spring:message code="${user.userName}"/></option>
							              			</c:forEach>
								                </select>
								            </div>
								            <div for="userName"></div>
								        </div>
								    </div>
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
								            <div class="col-md-6">
								            	 <select name="ipPattern" class="selectpicker show-tick form-control required ipPattern">
								                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
														<option value="${ipPatternC.itemCode}" <c:if test="${ipPatternC.itemCode == _cfg.ipPattern }">selected</c:if>>
															<spring:message code="${ipPatternC.itemValue}"/>
														</option>
							              			</c:forEach>
								                 </select>
								            </div>
								            <div for="ipPattern"></div>
								        </div>
								    </div>
								</div>
								
								<div class="row">
									<div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
								            <div class="col-md-6">
								            	 <select name="ipType" class="selectpicker show-tick form-control required">
								                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
														<option value="${ipTypeC.itemCode}" <c:if test="${ipTypeC.itemCode==4 }">selected</c:if> >
															<spring:message code="${ipTypeC.itemValue}"/>
														</option>
							               			</c:forEach>
								                 </select>
								            </div>
								            <div for="ipType"></div>
								        </div>
								    </div>
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip"/></label>
								            <div class="col-md-6">
								            	<input class="form-control required ipCheck clientIp" type="text" name="srcIpAddress" value="${_cfg.srcIpAddress }">
									        </div>
											<div for="srcIpAddress"></div>
										</div>
								    </div>
								</div>
								
								<div class="row dest">
									<div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
								            <div class="col-md-6">
								                 <input class="form-control required ipCheck" type="text" name="destIpAddress" >
								            </div>
								            <div for="destIpAddress"></div>
								        </div>
								    </div>
								</div>
								
	           			   		<div class="row ipmulitiplex">
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ir_type"/></label>
								            <div class="col-md-6">
								            	<select name="irType" class="selectpicker show-tick form-control required">
								                 	<option value=""><spring:message code="select"/></option>
								                  	<c:forEach items="${fns:getDictList('IR_TYPE')}" var="irTypeC">
														<option value="${irTypeC.itemCode}" <c:if test="${0==irTypeC.itemCode}">selected</c:if>><spring:message code="${irTypeC.itemValue}"/></option>
							              			</c:forEach>
								                 </select>
								            </div>
								            <div for="irType"></div>
								        </div>
								    </div>
								</div>
								</div>
							<%-- <br><%@include file="/WEB-INF/include/form/areaInfo.jsp"%> --%>
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