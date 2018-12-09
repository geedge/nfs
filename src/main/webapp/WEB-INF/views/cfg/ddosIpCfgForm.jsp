<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	//initCommIpVal();
	/* $("#cancel").on("click",function(){
		window.history.back();
	});*/
	var ddosProtocol=$("#antiddosProtocol").val();
	if(ddosProtocol==null||ddosProtocol==""){
		$("#bpsThreadshold").attr("disabled","true");
		$("#ppsThreadshold").attr("disabled","true");
	}
	$("#antiddosProtocol").on("change",function(){
		var ddosProtocol=$("#antiddosProtocol").val();
		if(ddosProtocol==null||ddosProtocol==""){
			$("#bpsThreadshold").attr("disabled","true");
			$("#ppsThreadshold").attr("disabled","true");
			$("#bpsThreadshold").val("");
			$("#bpsThreadshold").attr("placeholder","Bit Per Second");
			$("#ppsThreadshold").val("");
			$("#ppsThreadshold").attr("placeholder","Packet Per Second");
		}else{
			$("#bpsThreadshold").removeAttr("disabled");
			$("#ppsThreadshold").removeAttr("disabled");
		}
	})
	
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
	}); 
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#protocolId").val($(".action:checked").attr("protocolId"));
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
				$("div[for='bpsThreadshold']").empty();
				$("div[for='ppsThreadshold']").empty();
			if(($("#antiddosProtocol").val()!="")&&(!($("#bpsThreadshold").val().trim()>0)&&!($("#ppsThreadshold").val().trim()>0))){
				$("div[for='bpsThreadshold']").append("<label id='level-error' class='error'><spring:message code='input'/></label>");
				$("div[for='ppsThreadshold']").append("<label id='level-error' class='error'><spring:message code='input'/></label>");
				top.$.jBox.tip("<spring:message code='one_more_greater_zero'/>", "<spring:message code='info'/>");
				return;
			}
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
                      <form id="cfgFrom" action="${ctx}/manipulation/ddos/saveOrUpdate" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
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
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
								            <div class="col-md-6">
								                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
								        </div>
								    </div>
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
																	<c:if test="${_cfg.serviceId==service.serviceId || (_cfg.serviceId==null && satus.index==0)}">checked</c:if>>
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
								<div>
									<div class="row doLog">
										 <div class="col-md-6">
											<div class="form-group">
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
                   					 </div>
								</div>
								<h3 class="form-section">
									<spring:message code="action_region" />
									<small></small>
								</h3>
								
								
								
								<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
											<!--  目前支持TCP_SYN, DNS, NTP，单选。 -->
								            <label class="control-label col-md-3"><spring:message code="antiddos_protocol"/></label>
								            <div class="col-md-6">
								            	 <select id="antiddosProtocol" name="antiddosProtocol" class="selectpicker show-tick form-control">
													<option value=""><spring:message code="select"/></option>
													<c:forEach items="${fns:getDictList('ANTIDDOS_PROTOCOL')}" var="dic">
														<option value="${dic.itemCode}" <c:if test="${_cfg.antiddosProtocol eq dic.itemCode}">selected</c:if>><spring:message code="${dic.itemValue}"/></option>
													</c:forEach>
								                 </select>
								            </div>
								            <div for="antiddosProtocol"></div>
								        </div>
								    </div><!--  目前支持TCP_SYN, DNS, NTP，单选。 -->
								    <div class="col-md-6">
									      <div class="form-group">
									          <label class="control-label col-md-3"><spring:message code="bps_threadshold"/></label>
									          <div class="col-md-6">
									              <input id="bpsThreadshold" class="form-control digits" placeholder="Bit Per Second" maxlength="18" type="text" name="bpsThreadshold" value="${_cfg.bpsThreadshold}">
									          </div>
									          <div for="bpsThreadshold"></div>
									      </div>
									  </div>
									  <div class="col-md-6">
									      <div class="form-group">
									          <label class="control-label col-md-3"><spring:message code="pps_threadshold"/></label>
									          <div class="col-md-6">
									              <input id="ppsThreadshold" class="form-control digits" placeholder="Packet Per Second"  maxlength="18" type="text" name="ppsThreadshold" value="${_cfg.ppsThreadshold}">
									          </div>
									          <div for="ppsThreadshold"></div>
									      </div>
									  </div>
									  
								</div>
							<!-- desc and action -->
                           <%@include file="/WEB-INF/include/form/dDosIpInfo.jsp" %>
                           <br>
							<%@include file="/WEB-INF/include/form/areaInfo.jsp"%>
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
</body>
</html>