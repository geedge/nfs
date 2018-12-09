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
	$(document)
			.ready(
					function() {
						//initCommIpVal();
						//$("#cancel").on("click", function() {
						//	window.history.back();
						//});
						$(".action").on("change", function() {
							$("#serviceId").val($(this).attr("serviceId"));
							$("#protocolId").val($(this).attr("protocolId"));
						});
						$("#serviceId").val($(".action:checked").attr("serviceId"));
						$("#protocolId").val($(".action:checked").attr("protocolId"));
						if($("#cfgId").val()!=""){
							if($("#httpReqCfgNum").val()>0){
								$("a[for='httpReqCfg']").parent().addClass("active");
								$("a[for='httpResCfg']").parent().removeClass("active");
								$(".httpResCfg").addClass("hidden").addClass("disabled");
								$(".httpReqCfg").removeClass("hidden").removeClass("disabled");
							}
							if($("#httpResCfgNum").val()>0){
								$("a[for='httpResCfg']").parent().addClass("active");
								$("a[for='httpReqCfg']").parent().removeClass("active");
								$(".httpReqCfg").addClass("hidden").addClass("disabled");
								$(".httpResCfg").removeClass("hidden").removeClass(
								"disabled");
							}
						}else{
							$(".glyphicon-plus").first().click();
							$(".httpResCfg").addClass("hidden").addClass("disabled");
							$(".httpReqCfg").removeClass("hidden").removeClass(
							"disabled");
						}
						$(".configType").on("click", function() {
							var region = ($(this).find("a").attr("for"));
							if(region=="httpReqCfg"){
								$(".httpResCfg").addClass("hidden").addClass("disabled");
								$(".httpReqCfg").removeClass("hidden").removeClass(
								"disabled");
							}else{
								$(".httpReqCfg").addClass("hidden").addClass("disabled");
								$(".httpResCfg").removeClass("hidden").removeClass(
								"disabled");
							}
							
						});
						$("#cfgFrom").validate(
								{
									errorPlacement : function(error, element) {
										if($(element).parents().hasClass("tagsinput")){
											$(element).parents(".col-md-6").next("div").append(error);
										}else{
											$(element).parents(".form-group").find(
													"div[for='"
															+ element.attr("name")
															+ "']").append(error);
										}
									},
									submitHandler : function(form) {
										//loading('onloading...');
										var flag = true;
										//代表所有业务都隐藏了，提示必须增加一种业务数据
										if(!$(".httpReqCfg").hasClass("hidden")){
											if($(".httpReqCfg").find(".boxSolid").length==$(".httpReqCfg").find(".boxSolid.hidden").length){
												top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
												return;
											}
										}
										if(!$(".httpResCfg").hasClass("hidden")){
											if($(".httpResCfg").find(".boxSolid").length==$(".httpResCfg").find(".boxSolid.hidden").length){
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
		$("." + contentClassName + "0").removeClass("hidden").removeClass(
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
						<form id="cfgFrom" action="${ctx}/ntc/website/saveHttpCfg"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" id="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId"
								value="${_cfg.serviceId}">
							<input type="hidden" id="httpReqCfgNum" value="${httpReqCfgNum}">
							<input type="hidden" id="httpResCfgNum" value="${httpResCfgNum}">

							<div class="form-body">

								<div class="portlet-body flip-scroll">
									<div class="row-fluid">
										<div class="span12">

											<div class="tabbable tabbable-custom tabs-left">

												<!-- Only required for left/right tabs -->
												<ul class="nav nav-tabs tabs-left">

													<li class="active configType"><a href="#http_req_tab"
														data-toggle="tab" for="httpReqCfg"
														data_dldk="<spring:message code="http_req_control"/>"><spring:message
																code="http_req_control" /></a></li>
													<li class="configType"><a href="#http_res_tab" data-toggle="tab" for="httpResCfg"
														data_dldk="<spring:message code="http_res_control"/>"><spring:message
																code="http_res_control" /></a></li>


												</ul>

												<div class="tab-content">
													<div class="row">
														<div class="col-md-6">
															<div class="form-group">
																<label class="control-label col-md-3"><spring:message
																		code="config_describe" /></label>
																<div class="col-md-6">
																	<input class="form-control" type="text" name="cfgDesc" multiple
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
													
													<div class="row">
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
													<c:forEach items="${regionList}" var="region" varStatus="status">
														<c:if test="${region.regionType eq 1 }">
															<!--ip info-->
															<div class="httpReqCfg">
																<c:set var="tabName" value="httpIpTab"></c:set>
																<h4 class="form-section" >
																	<spring:message code="http_ip_title" />
																	<small> <span
																		class="glyphicon glyphicon-plus  ${tabName}Add"
																		onClick="addContent(this,'${tabName}')" title="add"></span></small>
																</h4>
																
																<c:forEach items="${_cfg.ipPortList}" var="ipPort"
																	varStatus="status">
																	<c:set var="cfgName" value="ipPortList[${status.index}]"></c:set>
																	<div class="row boxSolid ${tabName}${status.index} 
																		<c:if test="${empty ipPort.cfgId}">hidden disabled </c:if>" >
																		 <%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
																	</div>
																	<c:set var="ipCfgIndex" value="${ipCfgIndex+1}"></c:set>
																</c:forEach>
															</div>
															<!--/ip info-->
														</c:if>
													
														<c:if test="${region.regionType eq 2 }">
															<c:if test="${region.configRegionValue eq _cfg.httpUrl.cfgType }">
																<!--url info-->
																<div class="httpReqCfg">
																	<c:set var="tabName" value="httpUrlTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="http_url_title" />
																		<small> <span
																			class="glyphicon glyphicon-plus ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	
																	<%-- <c:set var="regionValue" value="${_cfg.httpUrl.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.httpUrlList}" var="cfg"
																	varStatus="status">
																		<c:set var="cfgName" value="httpUrlList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} 
																		<c:if test="${empty cfg.cfgId}">hidden disabled </c:if>" >
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
																<!--/url info-->
															</c:if>
															<c:if test="${region.configRegionValue eq _cfg.httpReqBody.cfgType }">
																<!--request body info-->
																<div class="httpReqCfg">
																	<c:set var="tabName" value="httpReqBodyTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="http_req_body_title" />
																		<small> <span
																			class="glyphicon glyphicon-plus  ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	
																	<%-- <c:set var="regionValue" value="${_cfg.httpReqBody.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.httpReqBodyList}" var="cfg"
																	varStatus="status">
																		<c:set var="cfgName" value="httpReqBodyList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} <c:if test="${empty cfg.cfgId}">hidden disabled </c:if>" >
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
																<!--/request body info-->
															</c:if>
															<c:if test="${region.configRegionValue eq _cfg.httpResBody.cfgType }">
																<!--response body info-->
																<div class="httpResCfg">
																	<c:set var="tabName" value="httpResBodyTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="http_res_body_title" />
																		<small> <span
																			class="glyphicon glyphicon-plus ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	
																	<%-- <c:set var="regionValue" value="${_cfg.httpResBody.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.httpResBodyList}" var="cfg"
																	varStatus="status">
																		<c:set var="cfgName" value="httpResBodyList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} <c:if test="${empty cfg.cfgId}">hidden disabled </c:if>" >
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
															</c:if>
														</c:if>
														<c:if test="${region.regionType eq 3 }">
															<c:if test="${region.configRegionValue eq _cfg.httpReqHdr.cfgType }">
																<!--request header info-->
																<div class="httpReqCfg">
																	<c:set var="tabName" value="httpReqHdrTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="http_req_hdr_title" />
																		<small> <span
																			class="glyphicon glyphicon-plus  ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	
																	<%-- <c:set var="regionValue" value="${_cfg.httpReqHdr.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.httpReqHdrList}" var="cfg"
																		varStatus="status">
																		<c:set var="cfgName" value="httpReqHdrList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} <c:if test="${empty cfg.cfgId}">hidden disabled </c:if>">
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
																<!--/request header info-->
															</c:if>
															<c:if test="${region.configRegionValue eq _cfg.httpResHdr.cfgType }">
																<!--response header info-->
																<div class="httpResCfg">
																	<c:set var="tabName" value="httpResHdrTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="${region.configRegionValue}" />
																		<small> <span
																		class="glyphicon glyphicon-plus  ${tabName}Add"
																		onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	<%-- <c:set var="regionValue" value="${_cfg.httpResHdr.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.httpResHdrList}" var="cfg"
																		varStatus="status">
																		<c:set var="cfgName" value="httpResHdrList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} <c:if test="${empty cfg.cfgId}">hidden disabled </c:if>">
	
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
																<!--/response header info-->
															</c:if>
															<c:if test="${region.configRegionValue eq _cfg.ntcSubscribeIdCfg.cfgType }">
																<!--response header info-->
																<div class="subscribeId">
																	<c:set var="tabName" value="subscribeIdTab"></c:set>
																	<h4 class="form-section" >
																		<spring:message code="${region.configRegionValue}" />
																		<small> <span
																		class="glyphicon glyphicon-plus  ${tabName}Add"
																		onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																	<%-- <c:set var="regionValue" value="${_cfg.httpResHdr.cfgType}"></c:set> --%>
																	<c:forEach items="${_cfg.ntcSubscribeIdCfgList}" var="cfg"
																		varStatus="status">
																		<c:set var="cfgName" value="ntcSubscribeIdCfgList[${status.index}]"></c:set>
																		<div class="row boxSolid ${tabName}${status.index} <c:if test="${empty cfg.cfgId}">hidden disabled </c:if>">
	
																			<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																		</div>
																	</c:forEach>
																</div>
																<!--/response header info-->
															</c:if>
														</c:if>
													</c:forEach>
													<%@include file="/WEB-INF/include/form/areaInfo.jsp"%>
													<br>
													<%@include file="/WEB-INF/include/form/basicInfo.jsp"%>
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
											</div>
										</div>
									</div>
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