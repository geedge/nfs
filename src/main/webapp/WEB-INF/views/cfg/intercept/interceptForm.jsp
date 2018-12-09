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
						
						$(".action").on("change", function() {
								 switchAction($("input[name=action]:checked").val());
							   	//拦截根据action切换动作部分
							   	setInterceptDefaultInfo("");
								$("#serviceId").val($(this).attr("serviceId"));
								$("#protocolId").val($(this).attr("protocolId"));
								openSingleBoxsolid();
								
						  });
						setInterceptDefaultInfo($("input[name='cfgId']").val());
						openSingleBoxsolid();
						$("#serviceId").val($(".action:checked").attr("serviceId"));
						$("#protocolId").val($(".action:checked").attr("protocolId"));
						$(".ratelimitAction").find("input[name='userRegion1']").on("change",function(){
							switchRateLimitType();
						});
						$(".monitAction").find("select[name='userRegion1']").on("change",function(){
							selectCert(this);
						});
						selectCert();
						$("#cfgFrom").validate(
								{
									errorPlacement : function(error, element) {
										$(element).parents(".form-group").find(
												"div[for='"
														+ element.attr("name")
														+ "']").html("");
										$(element).parents(".form-group").find(
												"div[for='"
														+ element.attr("name")
														+ "']").append(error);
									},
									submitHandler : function(form) {
										var flag = true;
										var actionValue=$("input[name=action]:checked").val();
										
										flag=validDomain(actionValue);
										if(!flag){
											return;
										}
										
										//代表所有业务都隐藏了，提示必须增加一种业务数据
										if($(".boxSolid").length ==$(".boxSolid.hidden").length){
											top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
											return;
										}
										
										//代表所有区域都隐藏了，提示必须增加个区域信息
										if($("input[name='isAreaEffective']:checked").val()==1 && $(".container-fluid:visible").size()==0){
											if($("#areaIsp").hasClass("hidden")) $("#areaIp").find(".glyphicon-plus").click();
											if($("#areaIp").hasClass("hidden")) $("#areaIsp").find(".glyphicon-plus").click();
											top.$.jBox.tip("<spring:message code='one_more_area'/>", "<spring:message code='info'/>");
											return;
										}
										//校验tagInput的空和十六进制
										flag=validateTagInputKeyword();
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
											var action=$("input[name='action']:checked").val();
											if(action==1){ //监测 
												$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".replaceAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".whiteListAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
											}else if(action==64){//限速
												$(".monitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".replaceAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".whiteListAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												var rateLimitType=$("input[name='userRegion1']checked").val();
												if(rateLimitType == 0){//丢包率
													$(".bandwith").find("input[name='userRegion3']").attr("disabled",true);
												}
												if(rateLimitType == 1){//带宽
													$(".droprate").find("select[name='userRegion2']").attr("disabled",true);
												}
											}else if(action==80){//替换
												$(".monitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".whiteListAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
											}else {//白名单
												$(".monitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
												$(".replaceAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
											}
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
	
	//限流方式
	var switchRateLimitType=function(){
		//0 丢包率 1 带宽
		var rateLimitType=$(".ratelimitAction").find("input[name='userRegion1']:checked").val();
		if(rateLimitType == 0){
			$(".droprate").removeClass("hidden");
			$(".bandwith").addClass("hidden");
		}
		if(rateLimitType == 1){
			$(".droprate").addClass("hidden");
			$(".bandwith").removeClass("hidden");
		}
		
	}
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
	//单域默认展开且不可关闭
	var openSingleBoxsolid = function() {
		var boxsolidLenth=$(".boxSolid").not(".intercept_replace_pkt_bin_div").prev(".form-section:visible").length;
		if(boxsolidLenth == 1){
			$(".boxSolid").prev(".form-section:visible").eq(0).find("span[title=add]").click();
			$(".boxSolid").prev(".form-section:visible").eq(0).next(".boxSolid").find("span[title=remove]").addClass("hidden");
		}
	}
	//实体证书提示域名信息
	var selectCert=function(){
		var cert=$(".monitAction").find("select[name='userRegion1']").val();
		var keyringType=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("keyringType");
		if(cert != '' && keyringType == 'end-entity'){
			var san=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("san");
			var cn=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("cn");
			if(san.indexOf(cn) > -1){
				$("#certDomain").attr("title",san);
			}else{
				$("#certDomain").attr("title",san+""+cn);
			}
			$("#certDomain").removeClass("hidden");
		}else{
			$("#certDomain").addClass("hidden");
		}
	}
	var validDomain=function(actionValue){
		var flag=false; 
		if(actionValue == 1){
			var cert=$(".monitAction").find("select[name='userRegion1']").val();
			var keyringType=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("keyringType");
			if(cert != '' && keyringType == 'end-entity'){
				var cn=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("cn");
				if(cn !='' && cn != null){
					var cnReg = new RegExp('^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\'+cn.replace("*","")+')+$');
					var san=$(".monitAction").find("select[name='userRegion1']").find("option[value='"+cert+"']").attr("san");
					if(san != null && san !=''){
						$("input[name$='cfgRegionCode'").each(function(){
							var serviceType=$(this).attr("serviceType");
							var prefixName=$(this).attr("name").split("cfgRegionCode")[0];
							if(serviceType == "intercept_domain"){
								var domain=$("input[name='"+prefixName+"cfgKeywords']").val();
								var domain=domain.trim();
								if(!flag){
									for (var i=0;i<san.split(",").length;i++) {
										if(san.split(",")[i] != null && san.split(",")[i] != '' && san.split(",")[i].trim() !=''){
											if(flag){
												break;
											}
											var sanStr=san.split(",")[i].trim();
											if(sanStr.indexOf("*") >-1){
												var sanReg= new RegExp('^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\'+sanStr.replace("*","")+')+$');
												if(sanReg.exec(domain) != null){
													flag=true;
												}
											}else{
												if(sanStr == domain){ //完全匹配
													flag=true;
												}
											}
										}
									}
									
									if(!flag){
										if(cn.indexOf("*") > -1){
											if(cnReg.exec(domain) != null){
												flag=true;
											}
										}else{
											if(cn == domain){ //完全匹配
												flag=true;
											}
										}
									}
								}else{
									flag=true;
								}
							}
							if(!flag){
								$("div[for='"+prefixName+"cfgKeywords']").html("");
								$("div[for='"+prefixName+"cfgKeywords']").html("<label class='error'>"+$("#certNotMatchDomain").text()+"</label>");
							}else{
								$("div[for='"+prefixName+"cfgKeywords']").html("");
							}
						});
					}else{
						flag=false;
						$("input[name$='cfgRegionCode'").each(function(){
							var serviceType=$(this).attr("serviceType");
							var prefixName=$(this).attr("name").split("cfgRegionCode")[0];
							if(serviceType == "intercept_domain"){
								if(error ==null || error.trim() == ''){
									$("div[for='"+prefixName+"cfgKeywords']").html("");
									$("div[for='"+prefixName+"cfgKeywords']").html("<label class='error'>"+$("#certNotMatchDomain").text()+"</label>");
								}
							}
							
						});
					}
				}else{
					flag=false;
					$("input[name$='cfgRegionCode'").each(function(){
						var serviceType=$(this).attr("serviceType");
						var prefixName=$(this).attr("name").split("cfgRegionCode")[0];
						var error=$("div[for='"+prefixName+"cfgKeywords']").html();
						if(serviceType == "intercept_domain"){
							if(error ==null || error.trim() == ''){
								$("div[for='"+prefixName+"cfgKeywords']").html("");
								$("div[for='"+prefixName+"cfgKeywords']").html("<label class='error'>"+$("#certNotMatchDomain").text()+"</label>");
							}
						}
						
					});
				}
			}else{
				flag=true;
			}
		}else{
			flag=true;
		}
		return flag;
		
	}
	var setInterceptDefaultInfo=function(cfgId){
		var action=$("input[name='action']:checked").val();
		var interceptRatelimitIp="";
		var interceptReplacePktBin="";
		var protocolNamePrefix="";
		var serviceType="";
		//查找当前业务中的serviceRegionType，是否存在intercept_ratelimit_ip
		$("input[name$='cfgRegionCode']").each(function(){
			//替换动作非tcp
			var serviceRegionTypeValue=$(this).attr("serviceType");
			serviceType=serviceRegionTypeValue;
			var serviceRegionTypeName=$(this).attr("name");
			if(serviceRegionTypeName.indexOf(".") >-1){
				serviceRegionTypeName=serviceRegionTypeName.substring(0,serviceRegionTypeName.indexOf(".")+1);
			}else{
				serviceRegionTypeName="";
			}
			if(serviceRegionTypeValue == 'intercept_ratelimit_ip'){
				interceptRatelimitIp=serviceRegionTypeValue;
				protocolNamePrefix=serviceRegionTypeName;
			}
			if(serviceRegionTypeValue == 'intercept_replace_pkt_bin'){
				interceptReplacePktBin=serviceRegionTypeValue;
			}
		});
		if(action==1){ 
			//监测
			$(".monitAction").removeClass("hidden");
			$(".ratelimitAction").addClass("hidden");
			$(".replaceAction").addClass("hidden");
			$(".whiteListAction").addClass("hidden");
			if(interceptRatelimitIp == 'intercept_ratelimit_ip'){
				$("select[name='"+protocolNamePrefix+"protocol']").find("option").remove();
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
				//$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='0' selected>"+$.validator.messages.all+"</option>");
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='6' selected>TCP</option>");
				//$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='17'>UDP</option>");
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
			}
			if(interceptReplacePktBin == 'intercept_replace_pkt_bin'){
				$("."+interceptReplacePktBin).addClass("hidden");
				$("."+interceptReplacePktBin+"_div").addClass("hidden");
			}
		}else if(action==64){
			//限速
			$(".monitAction").addClass("hidden");
			$(".ratelimitAction").removeClass("hidden");
			$(".replaceAction").addClass("hidden");
			$(".whiteListAction").addClass("hidden");
			if(interceptRatelimitIp == 'intercept_ratelimit_ip'){	
				$("select[name='"+protocolNamePrefix+"protocol']").find("option").remove();
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='0' selected>"+$.validator.messages.all+"</option>");
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='6'>TCP</option>");
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='17'>UDP</option>");
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
				switchRateLimitType();
			}
			if(interceptReplacePktBin == 'intercept_replace_pkt_bin'){
				$("."+interceptReplacePktBin).addClass("hidden");
				$("."+interceptReplacePktBin+"_div").addClass("hidden");
			}
		}else if(action==80){
			//替换
			$(".monitAction").addClass("hidden");
			$(".ratelimitAction").addClass("hidden");
			$(".replaceAction").removeClass("hidden");
			$(".whiteListAction").addClass("hidden");
			if(interceptRatelimitIp == 'intercept_ratelimit_ip'){
				$("select[name='"+protocolNamePrefix+"protocol']").find("option").remove();
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='17' selected>UDP</option>")
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='1' >ICMP</option>")
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
			}
			if(interceptReplacePktBin == 'intercept_replace_pkt_bin' && cfgId == ""){
				$("."+interceptReplacePktBin).removeClass("hidden");
				$("."+interceptReplacePktBin).find("span").removeClass("hidden");
			}
		}else if(action==128){
			$(".monitAction").addClass("hidden");
			$(".ratelimitAction").addClass("hidden");
			$(".replaceAction").addClass("hidden");
			$(".whiteListAction").removeClass("hidden");
			if(interceptRatelimitIp == 'intercept_ratelimit_ip'){
				$("select[name='"+protocolNamePrefix+"protocol']").find("option").remove();
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
				//$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='0' selected>"+$.validator.messages.all+"</option>")
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='6' selected>TCP</option>")
				$("select[name='"+protocolNamePrefix+"protocol']").append("<option value='17'>UDP</option>")
				$("select[name='"+protocolNamePrefix+"protocol']").selectpicker("refresh");
			}
			if(interceptReplacePktBin == 'intercept_replace_pkt_bin'){
				$("."+interceptReplacePktBin).addClass("hidden");
				$("."+interceptReplacePktBin+"_div").addClass("hidden");
			}
		}
		
	}
</script>
</head>
<body>
			<!-- fucntion_ID 200-IP 201-DOMAIN -->
			<c:if test="${_cfg.functionId eq 200}">
				<c:set var="interceptType" value="Ip"/>
			</c:if>
			<c:if test="${_cfg.functionId eq 201}">
				<c:set var="interceptType" value="Domain"/>
			</c:if>
			<c:if test="${_cfg.functionId eq 212}">
				<c:set var="interceptType" value="Domain"/>
			</c:if>
	<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
	<span id="certNotMatchDomain" style="display:none"><spring:message code="cert_not_match_domain"></spring:message></span>
	<!-- Domain and certificate information do not match! -->
	<div class="page-content">
	 		<h3 class="page-title">
				<spring:message code="${_cfg.menuNameCode}"></spring:message>
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
						<form id="cfgFrom" action="${ctx}/proxy/intercept/${fn:toLowerCase(interceptType) }/saveOrUpdate"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
							<input type="hidden" id="regionSize" name="regionSize" value="${fn:length(regionList)}">
							<input type="hidden" id="doLog" name="doLog" value="1">

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
																value="${service.action }" class="required action"
																<c:if test="${_cfg.action==service.action || (_cfg.action==null && satus.index==0)}">checked</c:if>>
																<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
																	<c:if test="${(dict.itemCode eq service.action) && (service.action eq 1) }">
																			<spring:message code="intercept"/>
																	</c:if>
																	<c:if test="${(dict.itemCode eq service.action) && (service.action eq 128) }">
																			<spring:message code="bypass"/>
																	</c:if>
																	<c:if test="${(dict.itemCode eq service.action) && (service.action ne 1 && service.action ne 128 ) }">
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
								<%-- <div class="row interceptDoLog">
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
								</div> --%>
								<!-- dolog end-->
								
								<%--   action 为1 监测时 可以选择证书--%>
								<div class="row monitAction hidden">
								<h4 class="form-section">
											<spring:message code="action_region" />
											<small></small>
										</h4>
									<!-- 域名拦截增加拦截强度 -->
									<c:if test="${_cfg.functionId eq 201}">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><font color="red">*</font>
														<spring:message code="intercept_intensity" />
												</label>
												<div class="col-md-6">
													<select name="userRegion5"
													class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('INTERCEPT_DOMAIN_INTENSITY')}" var="intensityC">
															<option value="${intensityC.itemCode}" <c:if test="${_cfg.userRegion5==intensityC.itemCode || (empty _cfg.userRegion5 && intensityC.itemCode eq 1)}">selected</c:if>><spring:message code="${intensityC.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="userRegion5"></div>
											</div>
										</div>
									</c:if>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="intercept_file_strategy" /></label>
											<div class="col-md-6">
												<select name="userRegion1" class="selectpicker show-tick form-control">
													<option value="" <c:if test="${empty _cfg.userRegion1}">selected</c:if> ><spring:message code="selected"/></option>
													<c:forEach items="${certificateList}" var="certificate">
														<option value="${certificate.compileId}" 
															cn="${certificate.cn}"
															san="${certificate.altName}"
															keyringType="${certificate.keyringType}"
															<c:if test="${_cfg.userRegion1 eq fn:trim(certificate.compileId)}">
															selected
															</c:if>
															>
															${certificate.cfgDesc}
														</option>
														
													</c:forEach>
								                </select>
											</div>
											&nbsp;<span id="certDomain" title="" 
							            class="hidden" data-icon="&#xe08b;" style="cursor:default"></span>
											 <div for="userRegion1"></div>
										</div>
									</div>
								</div>
								<%--   action 为64 限速时 --%>
								<div class="row ratelimitAction hidden">
								<h4 class="form-section">
											<spring:message code="action_region" />
											<small></small>
										</h4>
							   		 <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="limit_rate_type"/></label>
								            <div class="col-md-6">
								            	<label class="radio-inline"> 
													<input type="radio"
														name="userRegion1" value="0"
														<%-- <c:if test="${(userRegion1 eq 0) || (empty userRegion1)}"> --%>
															checked
														<%-- </c:if> --%>
														class="required" > 
													<spring:message	code="ratelimit" />
												</label>
								            	<%-- <label class="radio-inline"> 
													<input type="radio"
														name="userRegion1" value="1"
														<c:if test="${(userRegion1 eq 1)}">
															checked
														</c:if>
														class="required" > 
													<spring:message	code="bandwith" />
												</label> --%>
								            </div>
								            <div for="userRegion1"></div>
								        </div>
							   		 </div>
							   		 <div class="col-md-6 droprate">
								        <div class="form-group ">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ratelimit"/></label>
								            <div class="col-md-6">
								            	<select name="userRegion2" data-live-search="true" class="selectpicker form-control required">
													<option value="" ><spring:message code="selected"/></option>
													<c:forEach items="${fns:getDictList('RATE_LIMIT')}" var="ratelimitC">
														<option value="${ratelimitC.itemCode}" <c:if test="${_cfg.userRegion2==ratelimitC.itemCode || (_cfg.userRegion2==null && ratelimitC.itemCode eq '0.1')}">selected</c:if>><spring:message code="${ratelimitC.itemValue}"/></option>
													</c:forEach>
												</select>
								            </div>
								            <div for="userRegion2"></div>
								        </div>
							   		 </div>
							   		 <%-- <div class="col-md-6 bandwith">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="bandwith"/></label>
								            <div class="col-md-6">
								            	<input class="form-control required number" type="text" name="userRegion3" value="${_cfg.userRegion3 }">
								            </div>
								            <div for="userRegion3"></div>
								        </div>
							   		 </div> --%>
								</div> 
								<%--action 为80 替换时 可以选择替换动作--%>
								<div class="row replaceAction hidden">
									<h4 class="form-section">
											<spring:message code="action_region" />
											<small></small>
										</h4>
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="control-label col-md-3"><font color="red">*</font>
															<spring:message code="replace_zone" />
													</label>
													<div class="col-md-6">
														<select name="userRegion1"
														class="selectpicker show-tick form-control required">
															<option value="" ><spring:message code="selected"/></option>
															<c:forEach items="${fns:getDictList('INTERCEPT_REPLACE_ZONE')}" var="replaceZoneC">
																<option value="${replaceZoneC.itemCode}" <c:if test="${_cfg.userRegion1==replaceZoneC.itemCode || _cfg.userRegion1==null}">selected</c:if>><spring:message code="${replaceZoneC.itemValue}"/></option>
															</c:forEach>
														</select>
													</div>
													<div for="userRegion1"></div>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-6">
												<div class="form-group ">
													<label class="control-label col-md-3"><font color="red">*</font>
														<spring:message code="replaced_content" /></label>
													<div class="col-md-6">
														<input class="form-control required" type="text"
															name="userRegion2"
															value="${_cfg.userRegion2 }">
													</div>
													<div for="userRegion2"></div>
												</div>
											</div>
											<div class="col-md-6">
												<div class="form-group ">
													<label class="control-label col-md-3"><font color="red">*</font>
														<spring:message code="replace_content" /></label>
													<div class="col-md-6">
														<input class="form-control required" type="text"
															name="userRegion3"
															value="${_cfg.userRegion3 }">
													</div>
													<div for="userRegion3"></div>
												</div>
											</div>
										</div>
								</div>
								<%--   action 为128 白名单时 --%>
								<c:if test="${_cfg.functionId eq 201}">
								<div class="row whiteListAction hidden">
								<%-- <h4 class="form-section">
											<spring:message code="action_region" />
											<small></small>
										</h4>
									<!-- 域名拦截增加拦截强度 -->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><font color="red">*</font>
														<spring:message code="intercept_intensity" />
												</label>
												<div class="col-md-6">
													<select name="userRegion5"
													class="selectpicker show-tick form-control required">
														<c:forEach items="${fns:getDictList('INTERCEPT_DOMAIN_INTENSITY')}" var="intensityC">
															<option value="${intensityC.itemCode}" <c:if test="${_cfg.userRegion5==intensityC.itemCode || (empty _cfg.userRegion5 && intensityC.itemCode eq 1)}">selected</c:if>><spring:message code="${intensityC.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="userRegion5"></div>
											</div>
										</div> --%>
								</div>
								</c:if>
								<c:set var="ipCfgIndex" value="0"></c:set>
								<c:set var="complexCfgIndex" value="0"></c:set>
								<c:set var="domainCfgIndex" value="0"></c:set>
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
										<c:if test="${region.configServiceType eq 'intercept_replace_pkt_bin'}">
											<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
											<h4 class="form-section ${region.configServiceType }">
												<spring:message code="${region.configRegionValue}" />
												<small> <span
													class="glyphicon glyphicon-plus ${tabName}Add"
													onClick="addContent(this,'${tabName}')" title="add"></span></small>
											</h4>
											<c:set var="cfgName" value="interceptPktBinList[${complexCfgIndex}]"></c:set>
											<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
											<c:choose>
												<c:when test="${fn:length(_cfg.interceptPktBinList)>0}">
													<c:set var="isBreak" value="false" ></c:set>
													<c:forEach items="${_cfg.interceptPktBinList}" var="cfg">
														<c:choose>
															<c:when test="${region.configRegionValue eq cfg.cfgType and !isBreak}">
																<div class="row boxSolid ${region.configServiceType }_div  ${tabName}${status.index}">
																	<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																</div>
																<c:set var="isBreak" value="true" ></c:set>
																<c:set var="complexCfgIndex" value="${complexCfgIndex+1 }"></c:set>
															</c:when>
														</c:choose>
													</c:forEach>
													<c:if test="${!isBreak}">
														<div class="row boxSolid ${region.configServiceType }_div ${tabName}${status.index} hidden disabled">
															<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
														</div>
														<c:set var="complexCfgIndex" value="${complexCfgIndex+1 }"></c:set>
													</c:if>
												</c:when>
												<c:otherwise>
													<div class="row boxSolid  ${region.configServiceType }_div ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="complexCfgIndex" value="${complexCfgIndex+1 }"></c:set>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${region.configServiceType eq 'intercept_domain'}">
											<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
											<h4 class="form-section ${region.configServiceType }">
												<spring:message code="${region.configRegionValue}" />
												<small> <span
													class="glyphicon glyphicon-plus ${tabName}Add"
													onClick="addContent(this,'${tabName}')" title="add"></span></small>
											</h4>
											<c:set var="cfgName" value="httpUrlList[${domainCfgIndex}]"></c:set>
											<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
											<c:choose>
												<c:when test="${fn:length(_cfg.httpUrlList)>0}">
													<c:set var="isBreak" value="false" ></c:set>
													<c:forEach items="${_cfg.httpUrlList}" var="cfg">
														<c:choose>
															<c:when test="${region.configRegionValue eq cfg.cfgType and !isBreak}">
																<div class="row boxSolid ${region.configServiceType }_div  ${tabName}${status.index}">
																	<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																</div>
																<c:set var="isBreak" value="true" ></c:set>
																<c:set var="domainCfgIndex" value="${domainCfgIndex+1 }"></c:set>
															</c:when>
														</c:choose>
													</c:forEach>
													<c:if test="${!isBreak}">
														<div class="row boxSolid ${region.configServiceType }_div ${tabName}${status.index} hidden disabled">
															<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
														</div>
														<c:set var="domainCfgIndex" value="${domainCfgIndex+1 }"></c:set>
													</c:if>
												</c:when>
												<c:otherwise>
													<div class="row boxSolid  ${region.configServiceType }_div ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="domainCfgIndex" value="${domainCfgIndex+1 }"></c:set>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:if>
								</c:forEach>
								<br>
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
						</form>
						<!-- END FORM-->
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>