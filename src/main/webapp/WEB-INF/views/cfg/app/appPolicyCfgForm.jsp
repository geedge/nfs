<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$(".ratelimitAction").find("input[name='userRegion1']").on("change",function(){
		switchRateLimitType();
	});
	$(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
		if($(".action:checked").val()==64){
			$(".ratelimitAction").removeClass("hidden");
		}else{
			$(".ratelimitAction").addClass("hidden");
		}
		if(($(".action:checked").val()==1) || ($(".action:checked").val()==16)){
			$(".subscribeId").show();
		}else{
			$(".subscribeId").hide();
		}
		if($(this).val()==1){
			$(".ipPortAdd").click();
		}
		switchAction($(this).val());
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#protocolId").val($(".action:checked").attr("protocolId"));
	if($(".action:checked").val()==64){
		$(".ratelimitAction").removeClass("hidden");
	}else{
		$(".ratelimitAction").addClass("hidden");
	}
	if(($(".action:checked").val()==1) || ($(".action:checked").val()==16)){
		$(".subscribeId").show();
	}else{
		$(".subscribeId").hide();
	}
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
			/* if($(".boxSolid").length ==$(".boxSolid.hidden").length){
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
				return;
			} */
			if($("[name=action]:checked").val()==1&&$(".ipPortAdd").parents(".form-section").next(".boxSolid").is(":hidden")){
				 top.$.jBox.tip("<spring:message code='ip_must_select'/>", "<spring:message code='info'/>");
				 return false;
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
			$("select[name$='isHexbin']").each(function(){
				var isHexbin=$(this).val();
				if(isHexbin == 1){ //十六进制 
					var keywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","cfgKeywords")+"']").val();
					keywords=keywords.replace("***and***","")
					if(!(/^([0-9|a-f|A-F]*)$/.test(keywords))){
						$(this).parents(".form-body").find(
								"div[for='"
										+ $(this).attr("name").replace("isHexbin","cfgKeywords")
										+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$.validator.messages.hexCheck+"</label>");
						flag = false;
					}					
				}
			});
			if(flag){
				//将disable属性的元素删除
				$(".disabled").each(function(){
					$(this).remove();
				});
				$("input[name='exprType']").attr("disabled",false);
				if($("[name='behavCode']")&&$("[name='behavCode']").val()!=""){
					$("input[name='exprType']").val(1);
				}
				if($(".ratelimitAction").is(":hidden")){
					$(".ratelimitAction").find("input[name^='userRegion'],select[name^='userRegion']").attr("disabled",true);
				}
				loading('onloading...');
				form.submit();
			}else{
				return;
			}
		},
		errorContainer: "#messageBox",
	});
	//if('${_cfg.behavCode}'){
		ajaxBehaviour($("#specServiceIdId").val());
	//}
	$("#specServiceIdId").on("change",function(){
		ajaxBehaviour($(this).val());
	});
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
var ajaxBehaviour=function(val){
	var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
	var request=$.ajax({
		type:'post',
		url:pathName+'/specific/specificServiceCfg/childrenList',
		data:{"parent":val},
		dataType:'json',
		async:true,
		success:function(data,textStatus){//处理返回结果
			if(textStatus=="success"){
				var html='<select name="behavCode" data-live-search="true" class="selectpicker form-control">'
					+'<option value=""><spring:message code="select"/></option>';
				if(data.length>0){
						for(i=0;i<data.length;i++){
							html+='<option value="'+data[i].code+'"';
							if('${_cfg.behavCode}'==data[i].code){
								html+=" selected";
							}
							html+='>'+data[i].name+'</option>';
	      			   	}
						html+='</select>';
						$("#behaviour").html(html);
						$("[name='behavCode']").selectpicker("refresh");
						$("[name='behavCode']").selectpicker("render");
				}else{
					html+='</select>';
					$("#behaviour").html(html);
					$("[name='behavCode']").selectpicker("refresh");
					$("[name='behavCode']").selectpicker("render");
				}
				
			}
		},
		complete:function(XMLHttpRequest,status){//超时设置
			if(status=="timeout"){
				var html='<select name="behavCode" data-live-search="true" class="selectpicker form-control">'
					+'<option value=""><spring:message code="select"/></option></select>';
				$("#behaviour").html(html);
				$("[name='behavCode']").selectpicker("refresh");
				$("[name='behavCode']").selectpicker("render");
			}
		}
	});
}
//业务窗口打开
var addContent = function(obj, contentClassName) {
	var showDiv = $(obj).parent().parent().next();
	$(showDiv).removeClass("hidden").removeClass(
			"disabled");
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
                      <form id="cfgFrom" action="${ctx}/app/saveAppPolicyCfg" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="exprType" name="exprType" value="0">
                       	<input type="hidden" id="matchMethod" name="matchMethod" value="0">
                       	<input type="hidden" id="isHexbin" name="isHexbin" value="0">
                       
                          <div class="form-body">
                          <!-- desc and action -->
                             
							<c:set var="ipCfgIndex" value="0"></c:set>
							<c:set var="subscribeIdCfgIndex" value="0"></c:set>
							<c:forEach items="${regionList}" var="region" varStatus="status">
								<c:if test="${region.configServiceType eq 'app_policy' }">
									<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   					<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   					<div class="row">
                   						<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
									            <div class="col-md-6">
									                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
									            </div>
									        </div>
									    </div>
									    <!-- doLog -->
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
									 <div class="row">
		                              		<div class="col-md-6">
										        <div class="form-group">
										        	<c:forEach items="${fns:getDictList('IS_SHOW_BUSINESS_TYPE') }" var="showBusinessType">
															<c:if test="${showBusinessType.itemCode==1 }">
															<c:set var="isShowBusinessType" value="true"/>
															</c:if>
															<c:if test="${showBusinessType.itemCode!=1 }">
															<c:set var="isShowBusinessType" value="false"/>
															</c:if>
													</c:forEach>
										        	 <c:set var="spec_service_id"><spring:message code="social_app"/></c:set>
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="social_app"/></label>
										            <div class="col-md-6">
												       <sys:treeselect id="specServiceId" name="specServiceId" value="${_cfg.specServiceId}" 
														 labelName="parent.specServiceName" notAllowSelectParent="true" enableSearch="true"
														 labelValue="${empty _cfg.specServiceId?spec_service_id:fns:getBySpecServiceId(_cfg.specServiceId).specServiceName}"
														 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=1&specific=${!isShowBusinessType}" extId="0"
														notAllowSelectRoot="true" cssClass="form-control required"/>
										            </div>
										            <div for="parent.specServiceName"></div>
										        </div>
										    </div>
										    <div class="col-md-6">
										    	<div class="form-group">
										    	 	<label class="control-label col-md-3"><spring:message code="behaviour_type"/></label>
										    	 	 <div class="col-md-6" id="behaviour">
										    	 	 <select name="behavCode" data-live-search="true" class="selectpicker form-control">
										    	 	 <option value=""><spring:message code="select"/></option>
										    	 	 </select>
										    	 	 </div>
										    	</div>
										    </div>
									</div>
									<div class="row">
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
										   <div class="col-md-6 hidden">
										        <div class="form-group">
										            <label class="control-label col-md-3">
										            	<input type="checkbox" name="userRegion4" class="child-checks" <%-- <c:if test="${_cfg.userRegion4 eq '1' }"> --%>checked<%-- </c:if> --%>   value="1">
										            </label>
										            <div class="col-md-6">
										            	<label class="control-label">
											            	<spring:message code="block_by_app_server_ip"/>
											            </label>
										            </div>
										            <div for="userRegion4"></div>
										       </div>
										   </div>
										  </div> 
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
																<c:forEach items="${fns:getDictList('APP_RATE_LIMIT')}" var="ratelimitC">
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
								</c:if>
								<!-- subscribeId配置 -->
								<div class="subscribeId" >
								<c:if test="${region.configServiceType eq 'subscribe_id'}">
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
										<c:when test="${fn:length(_cfg.ntcSubscribeIdCfgList)>0 and (!empty _cfg.ntcSubscribeIdCfgList[subscribeIdCfgIndex].cfgId)}">
											<c:set var="isBreak" value="false" ></c:set>
											<c:forEach items="${_cfg.ntcSubscribeIdCfgList}" var="cfg">
												<c:choose>
													<c:when test="${region.configRegionValue eq cfg.cfgType and !isBreak}">
														<div class="row boxSolid ${tabName}${status.index}">
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
											<div class="row boxSolid ${tabName}${status.index} hidden disabled">
												<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
											</div>
											<c:set var="subscribeIdCfgIndex" value="${subscribeIdCfgIndex+1 }"></c:set>
										</c:otherwise>
									</c:choose>
								</c:if>
								</div>
								<!-- IP配置 -->
								<c:if test="${region.regionType eq 1 }">
									<h4 class="form-section">
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<spring:message code="NTC_UNIVERSAL_IP" />
										<small> <span
														class="glyphicon glyphicon-plus  ${tabName}Add ipPortAdd"
														onClick="addContent(this,'${tabName}')" title="add"></span></small>
									</h4>
									<c:set var="cfgName" value="ipPortList[${ipCfgIndex}]"></c:set>
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