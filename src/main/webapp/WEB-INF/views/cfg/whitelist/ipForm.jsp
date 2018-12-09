<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="ip_whitelist"></spring:message></title>

<style type="text/css">
.boxSolid {
	border: 1px solid #eeeeee
}
</style>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						/*initCommIpVal();
						 $("#cancel").on("click", function() {
							window.history.back();
						});*/
						$(".glyphicon-plus").first().click();
						$(".boxSolid .glyphicon-remove").first().addClass("hidden");
						$(".protocol").addClass("hidden");
						$(".action").on("change", function() {
							$("#serviceId").val($(this).attr("serviceId"));
							$("#protocolId").val($(this).attr("protocolId"));
							switchAction($(this).val());
						}); 
						$("#serviceId").val($(".action:checked").attr("serviceId"));
						$("#protocolId").val($(".action:checked").attr("protocolId"));
						$("select[name$='protocol']").parents(".form-group").addClass("hidden");
						$("select[name$='direction']").parents(".form-group").addClass("hidden");
						$("#cfgFrom").validate(
								{
									errorPlacement : function(error, element) {
										$(element).parents(".form-group").find(
												"div[for='"
														+ element.attr("name")
														+ "']").append(error);
									},
									submitHandler : function(form) {
										//loading('onloading...');
										var flag = true;
										//存在隐藏的subscribeId，不算进域配置
										if($(".boxSolid[class~='subscribeId']").length>0){
											if($(".boxSolid.hidden[class~='subscribeId']").is(":hidden")){
												if(($(".boxSolid").length) == ($(".boxSolid.hidden").length)){
													top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
													return;
												}
											}
											if(!$(".boxSolid.hidden[class~='subscribeId']").is(":hidden")){
												if(($(".boxSolid").length) == ($(".boxSolid.hidden").length) + 1){
													top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
													return;
												}
											}
											//代表所有业务都隐藏了，提示必须增加一种业务数据
										}else{
											if(($(".boxSolid").length) == ($(".boxSolid.hidden").length)){
												top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
												return;
											}
										}
										//代表所有区域都隐藏了，提示必须增加个区域信息
										if($("input[name='isAreaEffective']:checked").val()==1 && $(".container-fluid:visible").size()==0){
											if($("#areaIsp").hasClass("hidden")) $("#areaIp").find(".glyphicon-plus").click();
											if($("#areaIp").hasClass("hidden")) $("#areaIsp").find(".glyphicon-plus").click();
											top.$.jBox.tip("<spring:message code='one_more_area'/>", "<spring:message code='info'/>");
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
										});
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
										var flag1=validateInvisibleCharTag();
										if(flag){
											flag=flag1;
										}
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
									errorContainer : "#messageBox",
								});
						//业务内容增加
						/* $("span[title='add']").on("click", function() {
							$(".ipPort0").removeClass("hidden");
						}); */

					});

	//业务窗口打开
	var addContent = function(obj, contentClassName) {
		var showDiv = $(obj).parent().parent().next();
		$(showDiv).removeClass("hidden").removeClass(
				"disabled");
		/* $("."+contentClassName+"0").find("input,select").each(function(){
			$(this).removeAttr("disabled");
		}); */
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
						<form id="cfgFrom" action="${ctx}/ntc/whitelist/ip/save"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<!-- <input type="hidden" name="protocol" value="0">
							<input type="hidden" name="direction" value="0"> -->
							<input type="hidden" name="doLog" value="0" >
							<input type="hidden" name="isAreaEffective" value="0" >
							<input type="hidden" id="serviceId" name="serviceId"
								value="${_cfg.serviceId}">

							<div class="form-body">


								<div class="row">
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
									<div class="col-md-6">
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
								<!-- dolog begin-->
								<%--
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
								 --%>
								<!-- dolog end-->
								<c:set var="ipCfgIndex" value="0"></c:set>
								<c:set var="strCfgIndex" value="0"></c:set>
								<c:set var="subscribeIdIndex" value="0"></c:set>
								<c:forEach items="${regionList}" var="region" varStatus="status">
									<c:if test="${region.regionType eq 1 }">
										<!--ip info-->
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus  ${tabName}Add"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName" value="ipPortList[${ipCfgIndex}]"></c:set>
										<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
										<c:choose>
											<c:when test="${fn:length(_cfg.ipPortList)>0 and ipCfgIndex<fn:length(_cfg.ipPortList) }">
												<c:forEach items="${_cfg.ipPortList}" var="ipPort">
												<c:if test="${region.configRegionValue eq ipPort.cfgType }">
													<div class="row boxSolid ${tabName}${status.index}">
														<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
													</div>	
													<c:set var="ipCfgIndex" value="${ipCfgIndex+1}"></c:set>		
												</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<div class="row boxSolid ${tabName}${status.index} hidden disabled">
													<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
												</div>
												<c:set var="ipCfgIndex" value="${ipCfgIndex+1 }"></c:set>
											</c:otherwise>
										</c:choose>
									<!--/ip info-->
									</c:if>
									<c:if test="${region.regionType eq 2 }">
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus ${tabName}Add"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:if test="${region.configServiceType eq 'subscribe_id' }">
											<c:set var="cfgName" value="ntcSubscribeIdCfgList[${subscribeIdIndex}]"></c:set>
										</c:if>
										<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
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
																<c:set var="subscribeIdIndex" value="${subscribeIdIndex+1 }"></c:set>
														</c:when>
													</c:choose>
												</c:forEach>
												<c:if test="${!isBreak}">
													<div class="row boxSolid subscribeId ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:if test="${region.configServiceType eq 'subscribe_id' }">
																<c:set var="subscribeIdIndex" value="${subscribeIdIndex+1 }"></c:set>
															</c:if>
												</c:if>
											</c:when>
											<c:otherwise>
												<div class="row boxSolid subscribeId ${tabName}${status.index} hidden disabled">
													<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
												</div>
												<c:if test="${region.configServiceType eq 'subscribe_id' }">
													<c:set var="subscribeIdIndex" value="${subscribeIdIndex+1 }"></c:set>
												</c:if>
												<c:if test="${region.configServiceType ne 'subscribe_id' }">
													<c:set var="strCfgIndex" value="${strCfgIndex+1 }"></c:set>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								<br>
								<input type="hidden" name="requestId" value="0"/>
								<input type="hidden" name="classify" value="0"/>
								<input type="hidden" name="attribute" value="0"/>
								<input type="hidden" name="lable" value="0"/>
								<%--@include file="/WEB-INF/include/form/areaInfo.jsp"--%>
								<%--@include file="/WEB-INF/include/form/basicInfo.jsp"--%>
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


							</div>
						</form>
						<%-- END FORM--%>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>