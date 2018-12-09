<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	switchIpType($("select[name$='ipType']"));
	areaControlInit();
	$("input[name='isAreaEffective']").on('change',function(){
		var val=$(this).val();
		if(val==1){
			$(".areaType").removeClass("hidden");
			if($("input[name='areaType']:checked").val()==1){//areaISP
				$("#areaIsp").removeClass("hidden");
			}else if($("input[name='areaType']:checked").val()==0){//areaIp
				$("#areaIp").removeClass("hidden");	
			}
		}else{
			$(".areaType").addClass("hidden");
			$("#areaIp").addClass("hidden");
			$("#areaIsp").addClass("hidden");
		}
	});
	$("select[name$='ipType']").on("change",function(){
		switchIpType($(this));
	});
	$("input[name='areaType']").on('change',function(){
		var val=$(this).val();
		if($(this).is(":visible")){
			if(val==0){
				$("#areaIp").removeClass("hidden");
				$("#areaIsp").addClass("hidden");
			}else{
				$("#areaIsp").removeClass("hidden");
				$("#areaIp").addClass("hidden");
			}
		}else{
			$("#areaIsp").addClass("hidden");
			$("#areaIp").addClass("hidden");
		}
	});
	hiddenlevel();
	$(".action").on("change",function(){
		$("#serviceId").val($(this).attr("serviceId"));
		hiddenlevel();
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
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

function hiddenlevel(){
	var action=$("input[name='action']:checked").val();
	if(action==16){
		$(".leveL").addClass("hidden");
		$("input[name='level']").val(0);
	}else {
		$(".leveL").removeClass("hidden");
		$("input[name='level']").val('');
	}
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
                      <form id="cfgFrom" action="${ctx}/ntc/av/sample/saveAudioSignSample" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
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
							        </div>
							    </div>
							    <div class="col-md-6">
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
							        <div class="form-group ">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="sample_sign"/></label>
							            <div class="col-md-6">
							            	 <input class="form-control required" type="text" name="description" value="${_cfg.description }">
							            </div>
							            <div for="description"></div>
							        </div>
							    </div>
							      <div class="col-md-6 leveL">
							        <div class="form-group ">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="harm_level"/></label>
							            <div class="col-md-6">
							            	 <input class="form-control required digest number" range="[0,100]" type="text" name="level" value="${_cfg.level }">
							            </div>
							            <div for="level"></div>
							        </div>
							    </div>
							    
							</div>
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
                           <%--  <%@include file="/WEB-INF/include/form/areaInfo.jsp" %> --%>
                           <input type="hidden" name="isAreaEffective" value="0">
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