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
	var action=$(".action:checked");
	if(!action){
		action=$("input[name='action']");
	}
	var regionCode=action.attr("regionCode");
	//initCommIpVal();
	var serviceType=$("input[name='cfgRegionCode']").attr("serviceType");
	if(serviceType){
		if(serviceType.indexOf("ipaddr")>-1&&$("input[name$='action']:checked").val()==32){
			$("select[name$='protocol'] option").each(function(){
				if($(this).attr("value")==1){
					$(this).removeAttr("disabled");
				}
			});
			$("select[name$='protocol']").selectpicker("refresh");
		}
		if(serviceType&&serviceType.indexOf("ipaddr")>-1&&$("input[name$='action']").val()==32){
			$("select[name$='protocol'] option").each(function(){
				if($(this).attr("value")==1){
					$(this).removeAttr("disabled");
				}
			});
			$("select[name$='protocol']").selectpicker("refresh");
		}
		if(serviceType.indexOf("ipmulitiplex")>-1){
			$(".ipmulitiplex").removeClass("hidden");
			$("input[name$='isAreaEffective']").each(function(){
				if($(this).val()==0){
					$(this).click();
				}
			})
			$(".areaInfo").addClass("hidden");
		}else{
			$(".areaInfo").removeClass("hidden");
			$(".ipmulitiplex").addClass("hidden");
		}
		if(serviceType.indexOf("ratelimit")>-1){
			if($("input[name$='action']").val()==64||$("input[name$='action']:checked").val()==64){
				$(".ratelimit").removeClass("hidden");
			}else{
				$(".ratelimit").addClass("hidden");
			}
		}else{
			$(".ratelimit").addClass("hidden");
		}
	}
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
		var actionCode=$(this).val();
		var regionCode=$(this).attr("regionCode");
		if(!regionCode){
			regionCode=$("input[name='action']").attr("regionCode");
		}
		if(regionCode){
			if(regionCode.indexOf(",")==-1){
				$("#cfgRegionCode"+regionCode).attr("name","cfgRegionCode");	
				$("#cfgType"+regionCode).attr("name","cfgType");
				$("[id^='cfgRegionCode']").each(function(){
					if($(this).attr("id")==("cfgRegionCode"+regionCode)){
						$(this).attr("name","cfgRegionCode");
					}else{
						$(this).removeAttr("name");
					}
				});
				$("[id^='cfgType']").each(function(){
					if($(this).attr("id")==("cfgType"+regionCode)){
						$(this).attr("name","cfgType");
					}else{
						$(this).removeAttr("name");
					}
				});
			}else{
				var _region='';
				var regionArr=regionCode.split(',');
				$("input[name$='cfgRegionCode'][regionType='1']").each(function(){
					for(var re in regionArr){
						if($(this).val()==regionArr[re]){
							serviceType=$(this).attr("serviceType");
							_region=regionArr[re];
							break;
						}
					}
				});
				if(_region!=''){
					regionCode=_region;
				}
			}
			var ipPortShow=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPortShow"),
	     		ipType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipType"),
	     		ipPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPattern"),
	     		portPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("portPattern"),
	     		direction=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("direction"),
	     		protocol=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("protocol"),
	     		regionType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("regionType");
				//ip，端口处理
				if(ipPortShow){
					if(ipPortShow.indexOf('1')>-1){//展示源IP,端口
						$("input[name$='srcIpAddress']").parents(".form-group").removeClass("hidden");
						$("input[name$='srcPort']").parents(".form-group").removeClass("hidden");
					}else{
						$("input[name$='srcIpAddress']").parents(".form-group").addClass("hidden");
						$("input[name$='srcPort']").parents(".form-group").addClass("hidden");
					}
					if(ipPortShow.indexOf('2')>-1){//展示目的IP,端口
						$("input[name$='destIpAddress']").parents(".form-group").removeClass("hidden");
						$("input[name$='destPort']").parents(".form-group").removeClass("hidden");
					}else{
						$("input[name$='destIpAddress']").parents(".form-group").addClass("hidden");
						$("input[name$='destPort']").parents(".form-group").addClass("hidden");
					}
				}
				if(ipType){
					var arr=ipType.split(',');
					$("select[name$='ipType'] option").each(function(){
						var has=false;
						for(var type in arr){
							if($(this).val()==arr[type]){
								has=true;
								if($(this).attr("disabled")){
									$(this).removeAttr("disabled")
								}
								break;
							}
						}
						if(!has){
							$(this).attr("disabled",true);
						}
					});
					$("select[name$='ipType']").selectpicker("refresh");
				}
				if(ipPattern){
					arr=ipPattern.split(',');
					$("select[name$='ipPattern'] option").each(function(){
						var has=false;
						for(var type in arr){
							if($(this).val()==arr[type]){
								has=true;
								if($(this).attr("disabled")){
									$(this).removeAttr("disabled")
								}
								break;
							}
						}
						if(!has){
							$(this).attr("disabled",true);
						}
					});
					$("select[name$='ipPattern']").selectpicker("refresh");
				}
				if(portPattern){
					arr=portPattern.split(',');
					$("select[name$='portPattern'] option").each(function(){
						var has=false;
						for(var type in arr){
							if($(this).val()==arr[type]){
								has=true;
								if($(this).attr("disabled")){
									$(this).removeAttr("disabled")
								}
								break;
							}
						}
						if(!has){
							$(this).attr("disabled",true);
						}
					});
					$("select[name$='portPattern']").selectpicker("refresh");
				}
				if(direction){
					arr=direction.split(',');
					$("select[name$='direction'] option").each(function(){
						var has=false;
						for(var type in arr){
							if($(this).val()==arr[type]){
								has=true;
								if($(this).attr("disabled")){
									$(this).removeAttr("disabled")
								}
								break;
							}
						}
						if(!has){
							$(this).attr("disabled",true);
						}
					});
					$("select[name$='direction']").selectpicker("refresh");
				}
				
				if(protocol){
					arr=protocol.split(',');
					$("select[name$='protocol'] option").each(function(){
						var has=false;
						for(var type in arr){
							if($(this).val()==arr[type]){
								has=true;
								if($(this).attr("disabled")){
									$(this).removeAttr("disabled")
								}
								break;
							}
						}
						if(!has){
							$(this).attr("disabled",true);
						}
					});
					$("select[name$='protocol']").selectpicker("refresh");
				}
				
			//处理自定义字段
			var serviceType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("serviceType");
			if(serviceType){
				if(serviceType.indexOf("ipaddr")>-1){
					if($(this).val()==32){
						$("select[name$='protocol'] option[value='1']").removeAttr("disabled")
					}else{
						$("select[name$='protocol'] option[value='1']").attr("disabled",true);
					}
					$("select[name$='protocol']").selectpicker("refresh");
				}
				if(serviceType.indexOf("ipmulitiplex")>-1){
					$(".ipmulitiplex").removeClass("hidden");
				}else{
					$(".ipmulitiplex").addClass("hidden");
				} 
				if(serviceType.indexOf("ratelimit")>-1){
					if($(this).val()==64){
						$(".ratelimit").removeClass("hidden");
					}else{
						$(".ratelimit").addClass("hidden");
					}
				}else{
					$(".ratelimit").addClass("hidden");
				}
				if($(this).val()==0x20||$(this).val()==0x60){
					$("input[name$='isAreaEffective']").each(function(){
						if($(this).val()==0){
							$(this).click();
						}
					})
				}
			}
		}
	});
	$("#ipCfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
			loading('<spring:message code="onloading"/>');
			//禁用隐藏域的表单
			var serviceType=$("input[name='cfgRegionCode']").attr("serviceType");
			var action=$("input[name='action']").val();
			if(serviceType.indexOf("ipmulitiplex")==-1){
				$("[name='dnsStrategyId'").removeAttr("name");
				$("[name='irType'").removeAttr("name");
			}
			if(serviceType.indexOf("ratelimit")==-1){
				$("[name='ratelimit'").removeAttr("name");
			}else if(64!=action){
				$("[name='ratelimit'").removeAttr("name");
			}
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
                      <form id="ipCfgFrom" action="${ctx}${urlPrefix}/saveOrUpdate" method="post" class="form-horizontal">
                          <div class="form-body row">
                            <input name="cfgName" type="hidden" value="${cfgName}"/>
                            <input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                            <input type="hidden" name="compileId" value="${_cfg.compileId}">
                            <input type="hidden" name="functionId" value="${_cfg.functionId}">
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
	               				<%@include file="/WEB-INF/include/form/ipInfo.jsp" %>
	           			   		<div class="row ipmulitiplex hidden">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group"/></label>
								            <div class="col-md-6">
								            	<select id="dnsStrategyId" name="dnsStrategyId" class="selectpicker show-tick form-control required">
				               						<option value="" <c:if test="${empty _cfg.dnsStrategyId }">selected</c:if>><spring:message code="select"/></option>
				               						<c:forEach items="${policyGroups }" var="policyGroup">
														<option value="${policyGroup.groupId}" <c:if test="${_cfg.dnsStrategyId==policyGroup.groupId }">selected</c:if>><spring:message code="${policyGroup.groupName}"/></option>
							              			</c:forEach>
								                </select>
								            </div>
								            <div for="dnsStrategyId"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ir_type"/></label>
								            <div class="col-md-6">
								            	<select name="irType" class="selectpicker show-tick form-control required">
								                  <option value=""><spring:message code="select"/></option>
								                  <c:forEach items="${fns:getDictList('IR_TYPE')}" var="irTypeC">
														<option value="${irTypeC.itemCode}" <c:if test="${_cfg.irType==irTypeC.itemCode}">selected</c:if>><spring:message code="${irTypeC.itemValue}"/></option>
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
								            	<input class="form-control required number" range="[0,1]" type="text" name="ratelimit" value="${_cfg.ratelimit}">
								            </div>
								            <div for="ratelimit"></div>
								        </div>
								        
								    </div>
								</div>
							<%@include file="/WEB-INF/include/form/areaInfo.jsp" %>
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