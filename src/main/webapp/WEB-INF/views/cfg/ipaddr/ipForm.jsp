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
						var asnRegionCode,isAsn=0;
						var regionCodes=$(".action:checked").attr("regionCode").split(",");
						$("input[id^='cfgRegionCode']").each(function(){
							if(7==$(this).attr("regionType")){
								asnRegionCode=$(this).val();
							}
						});
						for(var ind in regionCodes){
							if(regionCodes[ind]==asnRegionCode){
								isAsn=1;
								break;
							}
						}
						//hide actions
						if(isAsn==1){
							hideAction(0,asnRegionCode);
						}else{
							hideAction(1,asnRegionCode);
						}
						if(isAsn==0){
							$("[class~='glyphicon-plus'][class~='ipPortList']").click();
							$("[class~='boxSolid'][class~='asn']").find(".glyphicon-remove").click();
							/*if(action==32){
							 $(".subscribeId").find(".glyphicon-remove").click();
							$(".subscribeId").prev("h4").addClass("hidden");
							}*/
							var regionCodes=$(".action:checked").attr("regionCode").split(",");
							$("h4[class*='ipPortListTitle']").each(function(){
								var has=false;
								for(var ind in regionCodes){
									if($(this).hasClass("ipPortListTitle"+regionCodes[ind])){
										has=true;
									}
								}
								if(!has){
									$(this).addClass("hidden");
								}
							});
							processAction(isAsn,$(".ipPortList"));
						}else{
							$("[class~='glyphicon-plus'][class~='asn']").click();
							$("[class~='boxSolid'][class~='ipPortList']").find(".glyphicon-remove").click();
							processAction(isAsn,$(".asn"));
						}
						//隐藏显示asn
						if($("[name='action']:checked").val()==32||$("[name='action']:checked").val()==64){
							console.log(111);
							$("h4.form-section.asn").addClass("hidden");
						}else{
							$("h4.form-section.asn").removeClass("hidden");
						}
						$(".glyphicon-plus").on("click",function(){
							if($(this).hasClass("ipPortList")){
								isAsn=0;
								hideAction(1,asnRegionCode);
								$("[class~='boxSolid'][class~='asn']").find(".glyphicon-remove").click();
							}
							if($(this).hasClass("asn")){
								isAsn=1;
								hideAction(0,asnRegionCode);
								$("[class~='boxSolid'][class~='ipPortList']").find(".glyphicon-remove").click();
							}
						});
						//处理asn id
						$(".asn").find("input[name='userRegion5']").val($(".asn").find("select[name='userRegion4'] option:selected").attr("asnId"));
						$(".asn").find("select[name='userRegion4']").on("change",function(){
							var asnId=$(this).find("option[value='"+$(this).val()+"']").attr("asnId");
							$(this).parents(".row").find("input[name='userRegion5']").val(asnId);
						});
						$(".action").on("change", function() {
							$("#serviceId").val($(this).attr("serviceId"));
							$("#protocolId").val($(this).attr("protocolId"));
							switchAction($(this).val());
							var regioncode=$(this).attr("regioncode");
							var hasRegioncode=false;
							var arr=regioncode.split(',');
							var cfgType,cfgRegionCode,i18nCode;
							for(var re in arr){
								if($("[id='cfgRegionCode"+arr[re]+"']")&&$("[id='cfgRegionCode"+arr[re]+"']").attr("regionType")==1){
									regioncode=arr[re];
									cfgType=$("[id='cfgType"+arr[re]+"']").val();
									i18nCode=$("[id='cfgType"+arr[re]+"']").attr("i18nCode");
									cfgRegionCode=$("[id='cfgRegionCode"+arr[re]+"']").val();
									
								}
							}
							$("h4[class*='ipPortListTitle']").each(function(){
								$(this).find(".ipPortTitle").html(i18nCode);
							});
							$("input[id^='cfgRegionCode']").each(function(){
								if($(this).attr("id")!=("cfgRegionCode"+cfgRegionCode)){
									$(this).removeAttr("name");
									$("#cfgType"+$(this).val()).removeAttr("name");
								}else{
									$(this).attr("name",'cfgRegionCode');
									$("#cfgType"+$(this).val()).attr("name",'cfgType');
								}
							}); 
							var tabInfo=$(".ipPortList");
							if(tabInfo){
								tabInfo.find("[name$='cfgType']").val(cfgType);
								tabInfo.find("[name$='cfgRegionCode']").val(cfgRegionCode);
							}
							if(isAsn==0){
								changeIPVal(tabInfo);
							}
							processAction(isAsn,tabInfo);
							/* if(tabInfo){
								tabInfo.find("[name$='cfgType']").val(cfgType);
								tabInfo.find("[name$='cfgRegionCode']").val(cfgRegionCode);
								//丢弃回流没有subscribeId
								 if($(this).val()==32||$(this).val()==96){
									$(".subscribeId").find(".glyphicon-remove").click();
									$(".subscribeId").prev("h4").addClass("hidden");
								}else{
									$(".subscribeId").prev("h4").removeClass("hidden");
								} 
								//处理ICMP
								if($(this).val()==32){
									$.ipcommon.protocol.each(function(){
										if($(this).attr("value")==1){
											tabInfo.find("select[name$='protocol']").append($(this));
										}
									})
									tabInfo.find("select[name$='protocol']").selectpicker("refresh");
								}else{
									tabInfo.find("select[name$='protocol'] option[value='1']").remove();
									tabInfo.find("select[name$='protocol']").selectpicker("refresh");
								}
								
							} */
							//隐藏显示asn
							if($(this).val()==32||$(this).val()==64){
								$("h4.form-section.asn").addClass("hidden");
							}else{
								$("h4.form-section.asn").removeClass("hidden");
							}
						}); 
						$("#serviceId").val($(".action:checked").attr("serviceId"));
						$("#protocolId").val($(".action:checked").attr("protocolId"));
						$(".ratelimitAction").find("input[name='userRegion1']").on("change",function(){
							switchRateLimitType();
						})
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
										var subscribeId=0;
										//存在隐藏的subscribeId，不算进域配置
										if($(".boxSolid.hidden").hasClass("subscribeId")){
											subscribeId=1;
										}
										//代表所有业务都隐藏了，提示必须增加一种业务数据
										/* var boxSolidLenth=$(".boxSolid").length;
										if($(".boxSolid").hasClass("subscribeId")){
											boxSolidLenth=boxSolidLenth-1;
										}
										if(boxSolidLenth  ==($(".boxSolid.hidden").length-subscribeId)){
											top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
											return;
										} */
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
											if(isAsn==0){
												$("input[name='userRegion4']").attr("disabled",true);
												$("input[name='userRegion5']").attr("disabled",true);
											}
											$("input[name$='userRegion1']").attr("disabled",false);
											$("input[name$='exprType']").attr("disabled",false);
											//将disable属性的元素删除
											$(".disabled").each(function(){
												$(this).remove();
											});
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

var hideAction=function(isHide,regionCode){
	var action=$(".action:checked").val();
	var serviceId=$(".action:checked").attr("serviceId");
	$(".action").each(function(){
		var regionCodeArr=$(this).attr("regionCode").split(',');
		var has=false;
		for(var code in regionCodeArr){
			if(regionCode==regionCodeArr[code]){
				has=true;
				break;
			}
		}
		if(has){
			if(isHide==1){
				$(this).attr("disabled","disabled");
				$(this).parent(".radio-inline").addClass("hidden");
			}else{
				$(this).removeAttr("disabled");
				$(this).parent(".radio-inline").removeClass("hidden");
			}
		}else{
			if(isHide==1){
				$(this).removeAttr("disabled");
				$(this).parent(".radio-inline").removeClass("hidden");
			}else{
				$(this).attr("disabled","disabled");
				$(this).parent(".radio-inline").addClass("hidden");
			}
		}
	});
	if($("[class*='action'][value='"+action+"']:visible").length>0){
		$("[class*='action'][value='"+action+"']:visible").click();
	}else{
		$(".action:visible").get(0).click();
	}
	var reginosNew=$(".action:checked").attr("regioncode").split(",");
	$("input[id^='cfgRegionCode']").each(function(){
		var has=false;
		for(var code in reginosNew){
			if($(this).val()==reginosNew[code]){
				has=true;
				break;
			}
		}
		if(has){
			$(this).attr("name","cfgRegionCode");
			$("#cfgType"+$(this).val()).attr("name","cfgType");
		}else{
			$(this).removeAttr("name");
			$("#cfgType"+$(this).val()).removeAttr("name");
		}
	}); 
}
var processAction=function(configType,obj){
	var action=$(".action:checked").val();
	if(configType!=1){
		var o=$(obj)
		if(o){
			/*处理协议*/
			var selectedProtocol=o.find("select[name$='protocol'] option:selected").attr("value");
			if(action==32){
				//先清空，再添加
				/* o.find("select[name$='protocol']").empty();
				$.ipcommon.protocol.each(function(){
					if($(this).attr("value")==0||$(this).attr("value")==6||$(this).attr("value")==17||$(this).attr("value")==1||$(this).attr("value")==8||$(this).attr("value")==15){
						o.find("select[name$='protocol']").append($(this));
					}
				}) */
				$(".ratelimitAction").addClass("hidden");
				$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
			}else if(action==16){
				//先清空，再添加
				o.find("select[name$='protocol']").empty();
				$.ipcommon.protocol.each(function(){
					if($(this).attr("value")==6){
						o.find("select[name$='protocol']").append($(this));
					}
				})
				$(".ratelimitAction").addClass("hidden");
				$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
			}else if(action==64){
				//先清空，再添加
				/* o.find("select[name$='protocol']").empty();
				$.ipcommon.protocol.each(function(){
					if($(this).attr("value")==0||$(this).attr("value")==6||$(this).attr("value")==17){
						o.find("select[name$='protocol']").append($(this));
					}
				}) */
				$(".ratelimitAction").removeClass("hidden");
				var rateLimitType=$("input[name='userRegion1']:checked").val();
				if(rateLimitType == 0){//丢包率
					$(".bandwith").find("input[name='userRegion3']").attr("disabled",true);
					$(".droprate").find("select[name='userRegion2']").removeAttr("disabled");
					$(".droprate").find("select[name='userRegion2']").selectpicker("refresh");
				}
				if(rateLimitType == 1){//带宽
					$(".bandwith").find("input[name='userRegion3']").removeAttr("disabled");
					$(".droprate").find("select[name='userRegion2']").attr("disabled",true);
					$(".droprate").find("select[name='userRegion2']").selectpicker("refresh");
				}
			}else if(action==1){
				//先清空，再添加
				o.find("select[name$='protocol']").empty();
				$.ipcommon.protocol.each(function(){
					if($(this).attr("value")==0||$(this).attr("value")==6||$(this).attr("value")==17){
						o.find("select[name$='protocol']").append($(this));
					}
				})
				$(".ratelimitAction").addClass("hidden");
				$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
			}
			o.find("select[name$='protocol']").selectpicker("refresh");
			//切换后如果没找到选项，选任意协议
			if(o.find("select[name$='protocol'] option[value='"+selectedProtocol+"']").length>0){
				o.find("select[name$='protocol']").selectpicker("val",selectedProtocol);
			}else{
				if(o.find("select[name$='protocol'] option[value=0]").length>0){
					o.find("select[name$='protocol']").selectpicker("val",0);
				}else{
					o.find("select[name$='protocol']").get(0).selectedIndex=0;
					o.find("select[name$='protocol']").selectpicker("refresh");
				}
				
			}
		}
	}
	
}
var showHideIPSECProtocol=function(obj){
	var name=$(obj).attr("name");
	var userRegion1=$("select[name='"+name.replace("protocol","userRegion1")+"']");
	if($(obj).val()==8){
		if(userRegion1){
			userRegion1.removeAttr("disabled");
			userRegion1.parents(".ipsecProtocol").removeClass("hidden");
			userRegion1.selectpicker("refresh");
		}
	}else{
		if(userRegion1){
			userRegion1.attr("disabled","disabled");
			userRegion1.parents(".ipsecProtocol").addClass("hidden");
			userRegion1.selectpicker("refresh");
		}
	}
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
						<form id="cfgFrom" action="${ctx}/ntc/iplist/saveOrUpdate"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId"
								value="${_cfg.serviceId}">
							<c:forEach items="${regionList}" var="region">
	                            <input type="hidden" id="cfgType${region.configRegionCode}"
	                            	i18nCode="<spring:message code='${region.configRegionValue}' />"
	                             value="${region.configRegionValue}">
		                   		<input type="hidden" id="cfgRegionCode${region.configRegionCode}"
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
								<div class="portlet-body flip-scroll">
									<div class="row-fluid">
										<div class="span12">
											<%-- <div class="tabbable tabbable-custom tabs-left"> --%>
												<!-- Only required for left/right tabs -->
												<%-- <ul class="nav nav-tabs tabs-left">
													<li class="configType"><a href="#ip_tab" id="ipCfgTab"
														data-toggle="tab" for="ipaddr"
														data_dldk="<spring:message code="IP"/>"><spring:message
																code="IP" /></a></li>
													<li class="configType"><a href="#asn_tab" data-toggle="tab" for="asn" id="ansCfgTab"
														data_dldk="<spring:message code="ASN"/>"><spring:message
																code="ASN" /></a></li>
												</ul> --%>
												<div class="tab-content">
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
														<!-- dolog begin-->
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
														<div class="row ratelimitAction hidden">
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
																				<option value="${ratelimitC.itemCode}" <c:if test="${_cfg.userRegion2==ratelimitC.itemCode || (_cfg.userRegion2==null && ratelimitC.itemCode eq '0.001')}">selected</c:if>><spring:message code="${ratelimitC.itemValue}"/></option>
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
														<!-- dolog end-->
													<c:set var="ipCfgIndex" value="0"></c:set>
													<c:set var="strCfgIndex" value="0"></c:set>
													<c:set var="subscribeIdIndex" value="0"></c:set>
													<c:set var="asnIndex" value="0"></c:set>
													<c:forEach items="${regionList}" var="region" varStatus="status">
														<c:if test="${region.regionType eq 1 }">
															<c:if test="${ipCfgIndex==0}">
																	<!--ip info-->
																	<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
																	<h4 class="form-section ipPortListTitle${region.configRegionCode}">
																		<span class="ipPortTitle"><spring:message code="${region.configRegionValue}" /></span>
																		<small> <span
																			class="glyphicon glyphicon-plus ipPortList ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																	</h4>
																		<c:set var="cfgName" value="ipPortList[${ipCfgIndex}]"></c:set>
																		<%-- <c:set var="regionValue" value="${region.configRegionValue}"></c:set> --%>
																		<c:choose>
																			<c:when test="${fn:length(_cfg.ipPortList)>0 and ipCfgIndex<fn:length(_cfg.ipPortList) }">
																				<c:forEach items="${_cfg.ipPortList}" var="ipPort">
																				<c:if test="${region.configRegionValue eq ipPort.cfgType }">
																					<div class="row boxSolid ipPortList ${tabName}${status.index}">
																						<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
																					</div>	
																					<c:set var="ipCfgIndex" value="${ipCfgIndex+1}"></c:set>	
																				</c:if>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<div class="row boxSolid ipPortList ${tabName}${status.index} hidden disabled">
																					<%@include file="/WEB-INF/views/cfg/ipCfgForm.jsp"%>
																				</div>
																				<c:set var="ipCfgIndex" value="${ipCfgIndex+1 }"></c:set>
																			</c:otherwise>
																		</c:choose>
															</c:if>
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
														<c:if test="${region.regionType eq 7 }">
																<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
																<h4 class="form-section asn">
																		<spring:message code="${region.configRegionValue}" />
																		<small> <span
																			class="glyphicon glyphicon-plus asn ${tabName}Add"
																			onClick="addContent(this,'${tabName}')" title="add"></span></small>
																</h4>
																<c:choose>
																	<c:when test="${fn:length(fn:split(_cfg.userRegion4,','))>0}">
																		<c:set var="isBreak" value="false" ></c:set>
																		<c:forEach items="${fn:split(_cfg.userRegion4,',')}" var="asn">${_cfg.cfgType}
																			<c:choose>
																				<c:when test="${!isBreak}">
																					<div class="row boxSolid asn ${tabName}${status.index}">
																						<%@include file="/WEB-INF/views/cfg/ipaddr/asnForm.jsp"%>
																					</div>
																					<c:set var="isBreak" value="true" ></c:set>
																					<c:set var="asnIndex" value="${asnIndex+1 }"></c:set>
																				</c:when>
																			</c:choose>
																		</c:forEach>
																		<c:if test="${!isBreak}">
																			<div class="row boxSolid asn ${tabName}${status.index} hidden disabled">
																				<%@include file="/WEB-INF/views/cfg/ipaddr/asnForm.jsp"%>
																			</div>
																			<c:set var="asnIndex" value="${asnIndex+1 }"></c:set>
																		</c:if>
																	</c:when>
																	<c:otherwise>
																		<div class="row boxSolid asn ${tabName}${status.index} hidden disabled">
																			<%@include file="/WEB-INF/views/cfg/ipaddr/asnForm.jsp"%>
																		</div>
																		<c:set var="asnIndex" value="${asnIndex+1 }"></c:set>
																	</c:otherwise>
																</c:choose>
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
											<%-- </div> --%>
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