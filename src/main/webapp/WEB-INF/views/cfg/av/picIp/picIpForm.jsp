<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<style type="text/css">
.boxSolid {
	border: 1px solid #eeeeee
}
</style>
<script type="text/javascript">
$(function(){
	//initCommIpVal();
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
		var action=$("input[name='action']:checked").val();
	 	switchAction(action);
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#protocolId").val($(".action:checked").attr("protocolId"));
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
			var flag = true;
			
			if(flag){
				//将disable属性的元素删除
				$(".disabled").each(function(){
					$(this).remove();
				});
				$("input[name$='exprType']").attr("disabled",false);
				loading('onloading...');
				form.submit();
			}else{
				return;
			}
		},
		errorContainer: "#messageBox",
		errorPlacement : function(error, element) {
			$(element).parents(".form-group").find(
					"div[for='" + element.attr("name") + "']")
					.append(error);
		},
	});
});
//业务窗口打开
var addContent = function(obj, contentClassName) {
	var showDiv = $(obj).parent().parent().next();
	$(showDiv).removeClass("hidden").removeClass("disabled");
	$(obj).addClass("hidden");
}

//业务窗口关闭
var delContent = function(contentClassName, addBtnClassName) {
	$("." + contentClassName).addClass("hidden").addClass("disabled");
	$("." + addBtnClassName).removeClass("hidden");
}
</script>
</head>
<body>
<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
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
                      <form id="cfgFrom" action="${ctx}/ntc/av/savePicIp" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="protocolId" name="protocolId" value="${_cfg.protocolId}">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<c:if test="${region.configServiceType ne 'subscribe_id'}">
                   				<input type="hidden" name="${cfgName}.configServiceType" value="${region.configServiceType}">
                   				<input type="hidden" id="cfgType${region.configRegionCode}" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" id="cfgRegionCode${region.configRegionCode}"
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
							<!-- desc and action -->
                           <%@include file="/WEB-INF/include/form/ipInfo.jsp" %>
                           
                           <!-- SubscribeId -->
                           <c:set var="subscribeIdCfgIndex" value="0"></c:set>
                           <c:forEach items="${regionList}" var="region" varStatus="status">
                           		<c:if test="${region.regionType eq 2 }">
                           			<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus ${tabName}Add"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName" value="ntcSubscribeIdCfgList[${subscribeIdCfgIndex}]"></c:set>
										<c:set var="regionValue" value="${region.configRegionValue}"></c:set>
										<c:choose>
											<c:when test="${fn:length(_cfg.ntcSubscribeIdCfgList)>0}">
												<c:set var="isBreak" value="false" ></c:set>
												<c:forEach items="${_cfg.ntcSubscribeIdCfgList}" var="cfg">
													<c:choose>
														<c:when test="${region.configRegionValue eq cfg.cfgType and !isBreak}">
															<div class="row boxSolid subscribeId ${tabName}${status.index}">
																<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
															</div>
															<c:set var="isBreak" value="true" ></c:set>
															<c:set var="subscribeIdCfgIndex" value="${subscribeIdCfgIndex+1 }"></c:set>
														</c:when>
													</c:choose>
												</c:forEach>
												<c:if test="${!isBreak}">
													<div class="row boxSolid ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="subscribeIdCfgIndex" value="${subscribeIdCfgIndex+1 }"></c:set>
												</c:if>
											</c:when>
											<c:otherwise>
												<div class="row boxSolid subscribeId ${tabName}${status.index} hidden disabled">
													<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
												</div>
												<c:set var="subscribeIdCfgIndex" value="${subscribeIdCfgIndex+1 }"></c:set>
											</c:otherwise>
										</c:choose>
                           		</c:if>
                           </c:forEach>
                           
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