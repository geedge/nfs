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
			$("input[name$='cfgKeywords']").each(function(){
				if($(this).val()==''){
					$(this).parents(".form-group").find(
							"div[for='"
									+ $(this).attr("name")
									+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$("#keywordError").text()+"</label>");
					flag = false;
				}
			})
			$("select[name$='isHexbin']").each(function(){
				var isHexbin=$(this).val();
				if(isHexbin == 1){ 
					var keywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","cfgKeywords")+"']").val();
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
</script>
</head>
<body>
<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
<div class="page-content">
	<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
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
                      <form id="cfgFrom" action="${ctx}/app/saveAppSslCfg" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="appCode" name="appCode" value="${_cfg.appCode}">
                       	<input type="hidden" id="behavCode" name="behavCode" value="${_cfg.behavCode}">
                       	<!-- 配置域类型 -->
                          <div class="form-body">
                          <!-- desc and action -->
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
											<label class="control-label col-md-3">
											<spring:message code="config_describe" /></label>
											<div class="col-md-6">
												<input class="form-control" type="text"
													name="cfgDesc"
													value="${_cfg.cfgDesc}">
											</div>
											<div for="cfgDesc"></div>
										</div>
								   </div>
								</div>
							<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" serviceType="${region.configServiceType }" value="${region.configRegionCode }">
								<input type="hidden" name="configMultiKeywords" value="${region.configMultiKeywords }">
								<input type="hidden" name="configServiceType" value="${region.configServiceType }">
								<input type="hidden" name="configHex" value="${region.configHex }">
								<div class="row">
								 <div class="row">
									<div class="col-md-6 hidden">
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
									<%-- <c:if test="${!empty region.configDistrict }"> --%>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="district" /></label>
												<div class="col-md-6">
													<select name="district" data-live-search="true" data-live-search-placeholder="search"
														class="selectpicker show-tick form-control required district" onchange="changeDistrict($(this))">
															<option value=""><spring:message code="select"/></option>
															<c:forEach items="${fns:getDictList('SSL_CERT_DISTRICT')}"
																var="dict">
																<option value="${dict.itemValue }"
																	<c:if test="${_cfg.district eq dict.itemValue}">selected</c:if>>${dict.itemValue }</option>
															</c:forEach>
													</select>
													<input type="hidden" name="districtShowName"  maxlength="64"  placeholder="<spring:message code='please_input'/> <spring:message code='custom_region'/>" class="otherValue form-control" value="${_cfg.district}"/>
												</div>
												<div for="district"></div>
											</div>
										</div>
									<%-- </c:if> --%>
									<!-- doLog -->
									<div class="col-md-6 hidden">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message code="do_log" /></label>
											<c:forEach items="${fns:getDictList('DO_LOG') }" var="dict">
												<c:if test="${dict.itemCode eq 2}">
													<input type="radio" name="doLog" checked value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
												</c:if>
												<%-- <c:choose>
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
												</c:choose> --%>
											</c:forEach>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
											<spring:message code="keywords" /></label>
											<!-- 此配置的关键词可以输入多个关键词 -->
											<c:if test="${region.configMultiKeywords eq 1}">
												<div class="col-md-6">
													<input class="form-control required tags" type="text" id="tags_${tabName}"
														name="cfgKeywords"
														value="${_cfg.cfgKeywords}">
												</div>
											</c:if>
											<!-- 此配置的关键词不允许输入多个关键词 -->
											<c:if test="${(region.configMultiKeywords eq 0) or (empty region.configMultiKeywords)}">
												<div class="col-md-6">
													<input class="form-control required invisibleChar" type="text"
														name="cfgKeywords"
														value="${_cfg.cfgKeywords}">
												</div>
											</c:if>
											
											<div for="cfgKeywords"></div>
										</div>
									</div>
								
								</div>
								<div class="row">
										<div class="col-md-6">
											<div class="form-group">
											
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="expression_type" /></label>
												<div class="col-md-6">
													<c:if test="${!empty region.configExprType}">
														<c:forEach var="exprType" items="${fn:split(region.configExprType,',')}">
															<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC">
																<c:if test="${exprTypeC.itemCode eq exprType}">
																	<label class="radio-inline"> 
																		<input type="radio"
																			name="exprType" value="${exprType }"
																			class="required"
																			<c:if test="${_cfg.exprType eq exprType || (empty _cfg.exprType && exprType eq 0)}">
																				checked
																			</c:if>
																		> 
																		<spring:message	code="${exprTypeC.itemValue }" />
																	</label>
																</c:if>
															</c:forEach>
														</c:forEach>
													</c:if>
													<c:if test="${empty region.configExprType}">
														<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC">
																<c:if test="${exprTypeC.itemCode eq 0}">
																	<label class="radio-inline"> 
																		<input type="radio"
																			name="exprType" value="${exprTypeC.itemCode }"
																			class="required" checked > 
																		<spring:message	code="${exprTypeC.itemValue }" />
																	</label>
																</c:if>
															</c:forEach>
													</c:if>
												</div>
												<div for="exprType"></div>
												
											</div>
										</div>
									
										<div class="col-md-6">
											<div class="form-group ">
												<label class="control-label col-md-3"><font color="red">*</font>
													<spring:message code="match_method" /></label>
												<div class="col-md-6">
													<select name="matchMethod"
														class="selectpicker select2 form-control required">
														<c:if test="${!empty region.configMatchMethod}">
															<c:forEach var="matchMethod" items="${fn:split(region.configMatchMethod,',')}">
																<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
																	<c:if test="${matchMethodC.itemCode eq matchMethod}">
																		<option value="${matchMethodC.itemCode}" 
																		<c:if test="${_cfg.matchMethod eq matchMethod || (empty _cfg.matchMethod && matchMethod eq 0)}">
																		selected
																		</c:if>
																		>
																		<spring:message code="${matchMethodC.itemValue}"/>
																		</option>
																	</c:if>
																
																</c:forEach> 
															</c:forEach>
														</c:if>
														<c:if test="${empty region.configMatchMethod}">
															<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
																	<c:if test="${matchMethodC.itemCode eq 0}">
																		<option value="${matchMethodC.itemCode}" 
																		selected
																		>
																		<spring:message code="${matchMethodC.itemValue}"/>
																		</option>
																	</c:if>
																
																</c:forEach> 
														</c:if>
													</select>
												</div>
												<div for="matchMethod"></div>
											</div>
										</div>
								</div>
								<div class="row">
										<c:if test="${!empty region.configHex}">
										    <div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" name="isHex" value="1" class="required"
										            		><spring:message code="hex"/>
										            	</label>
										            	<label class="radio-inline">
										            		<input type="radio" name="isHex" value="0" class="required"
										            		><spring:message code="not_hex"/>
										            	</label>
										            </div>
										            <div for="isHex"></div>
										        </div>
										    </div>
										    <div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_case_insenstive"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" name="isCaseSenstive" value="1" class="required"
										            		><spring:message code="case_senstive"/>
										            	</label>
										            	<label class="radio-inline">
										            		<input type="radio" name="isCaseSenstive" value="0" class="required"
										            		><spring:message code="case_insenstive"/>
										            	</label>
										            </div>
										            <div for="isCaseInsenstive"></div>
										        </div>
										    </div>
								  	</c:if>
								  	<c:if test="${empty region.configHex}"> 
										<div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" name="isHex" value="0" class="required" checked
										            		><spring:message code="not_hex"/>
										            	</label>
										            </div>
										            <div for="isHex"></div>
										        </div>
										    </div>
										    <div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_case_insenstive"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" name="isCaseSenstive" value="0" class="required"
										            			checked
										            		><spring:message code="case_insenstive"/>
										            	</label>
										            </div>
										            <div for="isCaseInsenstive"></div>
										        </div>
										    </div>
									</c:if>
								</div>
								<div class="row hidden">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font>
												<spring:message code="whether_hexbinary" /></label>
											<div class="col-md-6">
												<select name="isHexbin"
													class="selectpicker select2 form-control required">
													<c:if test="${!empty region.configHex}">
														<c:forEach var="isHexbin" items="${fn:split(region.configHex,',')}">
																<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
																	<c:if test="${isHexbinC.itemCode eq isHexbin}">
																		<option value="${isHexbinC.itemCode}" 
																		<c:if test="${_cfg.isHexbin eq isHexbin || (empty _cfg.isHexbin && isHexbin eq 0)}">
																		selected
																		</c:if>
																		>
																		<spring:message code="${isHexbinC.itemValue}"/>
																		</option>
																	</c:if>
																
																</c:forEach> 
														</c:forEach>
													</c:if>
													<c:if test="${empty region.configHex}"> 
													
														<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
															<c:if test="${isHexbinC.itemCode eq 0}">
																<option value="${isHexbinC.itemCode}"  selected >
																<spring:message code="${isHexbinC.itemValue}"/>
																</option>
															</c:if>
														</c:forEach> 
													
													</c:if>
												</select>
											</div>
											<div for="isHexbin"></div>
										</div>
									</div>
								</div>
							</div> 
							</c:if>
                   		</c:forEach>
                         <input name="isAreaEffective" type="hidden" value="0">
                           <%-- <%@include file="/WEB-INF/include/form/areaInfo.jsp" %> --%>
							<input type="hidden" name="requestId" value="0"/>
							<input type="hidden" name="classify" value="0"/>
							<input type="hidden" name="attribute" value="0"/>
							<input type="hidden" name="lable" value="0"/>
                          	<%--  <%@include file="/WEB-INF/include/form/basicInfo.jsp" %> --%>
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