<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			if($(element).parents().hasClass("tagsinput")){
				$(element).parents(".col-md-6").next("div").append(error);
			}else{
				$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
			}
		},
		submitHandler: function(form){
			var flag = true;
			//代表所有业务都隐藏了，提示必须增加一种业务数据
			if($(".boxSolid").length ==$(".boxSolid.hidden").length){
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
				return;
			}
			$(".boxSolid:visible").find("input[name$='cfgKeywords']").each(function(){
				if($(this).val()==''){
					$(this).parents(".form-group").find(
							"div[for='"
									+ $(this).attr("name")
									+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$("#keywordError").text()+"</label>");
					flag = false;
				}
			})
			//keywords非空校验完成校验二进制字符串
			$(".boxSolid:visible").find("select[name$='isHexbin']").each(function(){
				var isHexbin=$(this).val();
				if(isHexbin == 1){ //十六进制 
					var keywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","cfgKeywords")+"']").val();
					keywords=keywords.replace("***and***","")	
				if(keywords != ''){
						if(!(/^([0-9|a-f|A-F]*)$/.test(keywords))){
							$(this).parents(".boxSolid").find(
									"div[for='"
											+ $(this).attr("name").replace("isHexbin","cfgKeywords")
											+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$.validator.messages.hexCheck+"</label>");
							flag = false;
						}
					}						
				}
			});
			if(flag){
				//将disable属性的元素删除
				$(".disabled").each(function(){
					$(this).remove();
				});
				$("input[name$='exprType']").attr("disabled",false);
				$("#appCode").val($("#specServiceIdId").val());
				loading('onloading...');
				form.submit();
			}else{
				return;
			}
		},
		errorContainer: "#messageBox",
	});
});
//业务窗口打开
var addContent = function(obj, contentClassName) {
	var showDiv = $(obj).parent().parent().next();
	$(showDiv).removeClass("hidden").removeClass(
			"disabled");
	/* $("."+contentClassName+"0").find("input,select").each(function(){
		$(this).removeAttr("disabled");
	}); */
	$("select[name$='portPattern']").parents(".port").removeClass("hidden");
	$("input[name$='destIpAddress']").parents(".destPort").removeClass("hidden");
	$(".moreBtn").data("click-times",2);
	$(obj).addClass("hidden");
}

//业务窗口关闭
var delContent = function(contentClassName, addBtnClassName) {
	$("." + contentClassName).addClass("hidden").addClass("disabled");
	/* $("."+contentClassName).find("input,select").each(function(){
		$(this).attr("disabled","true");
	}); */
	$("." + addBtnClassName).removeClass("hidden");
}
</script>
</head>
<body>
	<span id="keywordError" style="display:none"><spring:message
			code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message
			code="multiple_keywords_tip"></spring:message></span>
	<div class="page-content">
		<c:forEach items="${serviceList}" var="service">
         <c:if test="${cfg.functionId eq service.functionId}">
            <c:set var="serviceName" value="${service.serviceName}"/>
         </c:if>
    </c:forEach>
 		<h3 class="page-title">
			<spring:message code="${_cfg.menuNameCode }"></spring:message>
		</h3>
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${empty _cfg.cfgId}">
								<spring:message code="add"></spring:message>
							</c:if>
							<c:if test="${not empty _cfg.cfgId}">
								<spring:message code="edit"></spring:message>
							</c:if>
						</div>
					</div>
					<div class="portlet-body form">
						<!-- BEGIN FORM-->
						<form id="cfgFrom" action="${ctx}/app/feature/saveAppMultiFeatureCfg"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}"> 
							<input type="hidden" id="appCode" name="appCode" value="${_cfg.appCode}"> 
							<input type="hidden" id="behavCode" name="behavCode" value="${_cfg.behavCode}"> 
							<input type="hidden" id="doLog" name="doLog" value="2">
							<!-- 配置域类型 -->
							<div class="form-body">
								<div class="row">
									<div class="col-md-6">
								        <div class="form-group">
								        	 <c:set var="spec_service_id"><spring:message code="social_app"/></c:set>
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="social_app"/></label>
								            <div class="col-md-6">
										       <sys:treeselect id="specServiceId" name="specServiceId" value="${_cfg.specServiceId}" enableSearch="true"
												 labelName="parent.specServiceName" notAllowSelectRoot="true" notAllowSelectParent="true" enableAddBtn="true"
												 labelValue="${empty _cfg.specServiceId?spec_service_id:fns:getBySpecServiceId(_cfg.specServiceId).specServiceName}"
												 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=1" extId=""
												 cssClass="form-control required"/>
								            </div>
								            <div for="parent.specServiceName"></div>
								        </div>
								    </div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="config_describe" /></label>
											<div class="col-md-6">
												<input class="form-control" type="text" name="cfgDesc"
													value="${_cfg.cfgDesc}">
											</div>
										</div>
									</div>
									<div class="col-md-6 hidden">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="action" /></label>
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
															<c:forEach items="${fns:getDictList('SERVICE_ACTION') }"
																var="dict">
																<c:if test="${dict.itemCode eq service.action }">
																	<spring:message code="${dict.itemValue }" />
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
								<c:if test="${fns:getUser().loginId eq 'admin' or fns:getUser().loginId eq 'appUser' }">
									<div class="row">
										<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message
														code="user_region" /></label>
												<div class="col-md-6">
													<input class="form-control" type="text" name="userRegion1"
														value="${_cfg.userRegion1}">
												</div>
												<div for="userRegion1"></div>
									        </div>
									    </div>
									</div>
								</c:if>
								<c:set var="ipCfgIndex" value="0"></c:set>
								<c:set var="complexCfgIndex" value="0"></c:set>
								<c:set var="stfCfgIndex" value="0"></c:set>
								<c:forEach items="${regionList}" var="region" varStatus="status">
									
									<c:if test="${region.regionType eq 1 }">
										<!--ip info-->
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus  ${tabName}Add <c:if test="${fn:length(regionList)==1  or status.index==0}"> hidden</c:if>"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName" value="ipPortList[${ipCfgIndex}]"></c:set>
										<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
										<c:choose>
											<c:when
												test="${fn:length(_cfg.ipPortList)>0 and ipCfgIndex<fn:length(_cfg.ipPortList) }">
												<c:forEach items="${_cfg.ipPortList}" var="ipPort">
													<c:if test="${region.configRegionValue eq ipPort.cfgType }">
														<div class="row boxSolid ${tabName}${status.index}">
														<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
														<input type="hidden" name="cfgRegionType" value="${region.regionType }">
														<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
															<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
														</div>
														<c:set var="ipCfgIndex" value="${ipCfgIndex+1}"></c:set>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<div
													class="row boxSolid ${tabName}${status.index} <c:if test="${fn:length(regionList)>1 and status.index>0 }"> hidden disabled</c:if>">
													<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
												<input type="hidden" name="cfgRegionType" value="${region.regionType }">
												<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
													<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
												</div>
												<c:set var="ipCfgIndex" value="${ipCfgIndex+1 }"></c:set>
											</c:otherwise>
										</c:choose>
										<!--/ip info-->
									</c:if>
									<c:if test="${region.regionType eq 3 }">
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus ${tabName}Add <c:if test="${fn:length(regionList)==1  or status.index==0}"> hidden</c:if>"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName" value="complexList[${complexCfgIndex}]"></c:set>
										<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
										<c:choose>
											<c:when test="${fn:length(_cfg.complexList)>0}">
												<c:set var="isBreak" value="false"></c:set>
												<c:forEach items="${_cfg.complexList}" var="cfg">
													<c:choose>
														<c:when
															test="${region.configRegionCode eq cfg.cfgRegionCode and !isBreak}">
															
															<div class="row boxSolid ${tabName}${status.index}">
															<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
															<input type="hidden" name="cfgRegionType" value="${region.regionType }">
															<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
																<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
															</div>
															<c:set var="isBreak" value="true"></c:set>
															<c:set var="complexCfgIndex"
																value="${complexCfgIndex+1 }"></c:set>
														</c:when>
													</c:choose>
												</c:forEach>
												<c:if test="${!isBreak}">
													<div class="row boxSolid ${tabName}${status.index} hidden disabled">
														<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
														<input type="hidden" name="cfgRegionType" value="${region.regionType }">
														<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="complexCfgIndex" value="${complexCfgIndex+1 }"></c:set>
												</c:if>
											</c:when>
											<c:otherwise>
												
												<div
													class="row boxSolid ${tabName}${status.index} <c:if test="${fn:length(regionList)>1  and status.index>0}"> hidden disabled</c:if>">
													<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
												<input type="hidden" name="cfgRegionType" value="${region.regionType }">
												<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
													<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
												</div>
												<c:set var="complexCfgIndex" value="${complexCfgIndex+1 }"></c:set>
											</c:otherwise>
										</c:choose>
									</c:if>
									<c:if test="${region.regionType eq 2 }">
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus ${tabName}Add <c:if test="${fn:length(regionList)==1  or status.index==0}"> hidden</c:if>"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName"
											value="strList[${stfCfgIndex}]"></c:set>
										<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
										<c:choose>
											<c:when test="${fn:length(_cfg.strList)>0}">
												<c:set var="isBreak" value="false"></c:set>
												<c:forEach items="${_cfg.strList}" var="cfg">
													<c:choose>
														<c:when
															test="${region.configRegionCode eq cfg.cfgRegionCode and !isBreak}">
															
															<div
																class="row boxSolid ${tabName}${status.index}">
																<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
															<input type="hidden" name="cfgRegionType" value="${region.regionType }">
															<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
															</div>
															<c:set var="isBreak" value="true"></c:set>
															<c:set var="stfCfgIndex"
																value="${stfCfgIndex+1 }"></c:set>
														</c:when>
													</c:choose>
												</c:forEach>
												<c:if test="${!isBreak}">
													<div
														class="row boxSolid ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="stfCfgIndex"
														value="${stfCfgIndex+1 }"></c:set>
												</c:if>
											</c:when>
											<c:otherwise>
												
												<div
													class="row boxSolid ${tabName}${status.index} <c:if test="${fn:length(regionList)>1  and status.index>0}"> hidden disabled</c:if>">
													<input type="hidden" name="cfgRegionValue" value="${region.configRegionValue }">
												<input type="hidden" name="cfgRegionType" value="${region.regionType }">
												<input type="hidden" name="cfgRegionCode1" value="${region.configRegionCode }">
													<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
												</div>
												<c:set var="stfCfgIndex" value="${stfCfgIndex+1 }"></c:set>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								<input name="isAreaEffective" type="hidden" value="0"> <input
									type="hidden" name="requestId" value="0" /> <input
									type="hidden" name="classify" value="0" /> <input type="hidden"
									name="attribute" value="0" /> <input type="hidden" name="lable"
									value="0" />
								<div class="form-actions">
									<div class="row">
										<div class="col-md-6">
											<div class="row">
												<div class="col-md-offset-3 col-md-8">
													<button id="save" type="submit" class="btn green">
														<spring:message code="submit" />
													</button>
													<button id="cancel" type="button" class="btn default">
														<spring:message code="cancel" />
													</button>
												</div>
											</div>
										</div>
										<div class="col-md-6"></div>
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