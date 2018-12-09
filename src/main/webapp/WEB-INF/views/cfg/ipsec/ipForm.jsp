<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	if($("input[name='action']:checked").val()==0x20||$("input[name='action']:checked").val()==0x60){
		$("#cfgRegionCode").val($("#cfgRegionCodeCallback").val());	
		$("#cfgType").val($("#cfgTypeCallback").val());	
	}else{
		$("#cfgRegionCode").val($("#cfgRegionCodeMaat").val());	
		$("#cfgType").val($("#cfgTypeMaat").val());	
	}
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
		if($(this).val()==0x20||$(this).val()==0x60){
			$("#cfgRegionCode").val($("#cfgRegionCodeCallback").val());	
			$("#cfgType").val($("#cfgTypeCallback").val());	
			$("input[name='isAreaEffective']").each(function(){
				if($(this).val()==0){
					$(this).click();
				}
			})
		}else{
			$("#cfgRegionCode").val($("#cfgRegionCodeMaat").val());	
			$("#cfgType").val($("#cfgTypeMaat").val());	
		}
		
	});
	if('${fn:length(serviceList)}'>1){
		$("#serviceId").val($(".action:checked").attr("serviceId"));
		$("#protocolId").val($(".action:checked").attr("protocolId"));
	}
	
	if($("[name='action']:checked").val()!="${dropAction}"){
		$(".ipsec").addClass("hidden").addClass("disabled");
		$("#protocolHidden").removeAttr("disabled");
	}
	$("[name='action']").on("click",function(){
		if($(this).val()=="${dropAction}"){
			$(".ipsec").removeClass("hidden").removeClass("disabled");
			$("#protocolHidden").attr("disabled",true);
		}else{
			$(".ipsec").addClass("hidden").addClass("disabled");
			$("#protocolHidden").removeAttr("disabled");
		}
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
                      <form id="ipCfgFrom" action="${ctx}${urlPrefix}/saveOrUpdate" method="post" class="form-horizontal">
                          <div class="form-body row">
                            <input name="cfgName" type="hidden" value="${cfgName}"/>
                            <input type="hidden" id="protocolHidden"name="protocol" value="0" disabled>
                            <input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                            <input type="hidden" name="compileId" value="${_cfg.compileId}">
                            <input type="hidden" name="functionId" value="${_cfg.functionId}">
                            <input type="hidden" id="cfgType" name="cfgType" value="${_cfg.cfgType}">
	                   		<input type="hidden" id="cfgRegionCode" name="cfgRegionCode" value="${_cfg.cfgRegionCode}">
                            <c:if test="${fn:length(serviceList)==1}">
			            	<c:forEach items="${serviceList}" var="service">
                				<input type="hidden" name="action" value="${service.action }">
			            	    <input type="hidden" id="serviceId" name="serviceId" value="${service.serviceId}">
			            	    <input type="hidden" id="protocolId" name="protocolId" value="${service.protocolId}">
			            	</c:forEach>
							</c:if>
							<c:if test="${fn:length(serviceList)>1}">
							 <input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
							 <input type="hidden" id="protocolId" name="protocolId" value="${_cfg.protocolId}">
							</c:if>
                            <!-- 配置域类型 -->
                   			<c:forEach items="${regionList}" var="region">
	                   			<c:if test="${_cfg.functionId eq region.functionId}">
	                   				<c:if test="${region.isMaat==1}"><!-- is maat -->
	                   				<input type="hidden" id="cfgTypeMaat" value="${region.configRegionValue}">
	                   				<input type="hidden" id="cfgRegionCodeMaat" value="${region.configRegionCode}">
	                   				</c:if>
	                   				<c:if test="${region.isMaat==2}">
	                   				<input type="hidden" id="cfgTypeCallback" value="${region.configRegionValue}">
	                   				<input type="hidden" id="cfgRegionCodeCallback" value="${region.configRegionCode}">
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
							<!-- desc and action -->
							<c:forEach items="${fns:getDictList('SPECIAL_FUNCTION_ID')}" var="sfi">
								<c:if test="${sfi.itemCode==_cfg.functionId}">
								<c:set var="specialProtocol" value="${sfi.itemValue}"/>
								</c:if>
               				</c:forEach>
           			   		<%@include file="/WEB-INF/include/form/ipInfo.jsp" %>
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
<script type="text/javascript">

</script>
</html>