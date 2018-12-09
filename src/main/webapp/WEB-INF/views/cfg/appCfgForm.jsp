<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<script type="text/javascript">
$(function(){

	if(!$(".district").val()&&$(".otherValue").val()){
		$(".district").find("option").each(function(){
			var text=$(this).text().toLowerCase();
			if("others"==text){
				$(this).attr("selected","selected");
				$(this).parent().siblings(".otherValue").prop("type","text");
			}
		});
	}
	$(".district").on("change",function(){
		var text=$(this).find("option:selected").text().toLowerCase();
		var other=$(this).parent("div").siblings(".otherValue");
		if("others"==text){
			other.prop("type","text");
			$(this).find("option").each(function(){
				if(other.val()==$(this).text()){
					other.val("");
					return false;
				}
			});
		}else{
			other.prop("type","hidden");
		}
	});
	$(".otherValue").on("change",function(){
		var val=$(this).val();
		if($(this).is(":visible")){
			$(this).prev("div").find("option").each(function(){
				if("others"==$(this).text().toLowerCase()){
					$(this).prop("value",val);
					if($(this).prop("selected")){
						$(this).parent("select.selectpicker").selectpicker('val',val);
					}
				}
			});
		}
		
	});
	$("#save").on("click",function(){
		$("#cfgFrom").attr("action","${ctx}/cfg/app/saveOrUpdateCfg");
		$("#save").submit();
	});
	$("#cfgFrom").validate({
		submitHandler: function(form){
			//loading('onloading...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("<spring:message code='enter_error'/>");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		}
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
                      <!-- <div class="tools">
                          <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                          <a href="#portlet-config" data-toggle="modal" class="config" data-original-title="" title=""> </a>
                          <a href="javascript:;" class="reload" data-original-title="" title=""> </a>
                          <a href="javascript:;" class="remove" data-original-title="" title=""> </a>
                      </div> -->
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="#" method="post" class="form-horizontal">
                          <div class="form-body">
                          <h3 class="form-section"><spring:message code="block_config"/></h3>
                          	<div class="col-md-6">
						    	<div class="form-group">
						            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
						            <div class="col-md-6">
								        <select name="appId" title="<spring:message code="select"/>" data-live-search="true" data-live-search-placeholder="search" class="required selectpicker form-control">
						               	<c:forEach items="${protocolList}" var="app">
						               		<option value="${app.specServiceId}" <c:if test="${app.specServiceId eq _cfg.appId}">selected</c:if>>
						               			${app.specServiceName}
						               		</option>
						               	</c:forEach>
						               </select>
						            </div>
						         </div>
						    </div>
                            <div class="col-md-6">
						        <div class="form-group">
						            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
						            <div class="col-md-6">
						                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
						            </div>
						            <div for="cfgDesc"></div>
						        </div>
						    </div>
						    <c:if test="${isContainFeaturesCfg}">
						    	<input type="hidden" name="isAreaEffective" value="0"/>
						    </c:if>
						    <c:if test="${!isContainFeaturesCfg}">	
							    <div class="col-md-6">
							        <div class="form-group">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_area_block"/></label>
							            <div class="col-md-6">
							            	<label class="radio-inline">
							            		<input type="radio" class="required" name="isAreaEffective" value="1"
							            			<c:if test="${_cfg.isAreaEffective==1}">checked</c:if>
							            		><spring:message code="yes"/>
							            	</label>
							            	<label class="radio-inline">
							            		<input type="radio" class="required" name="isAreaEffective" value="0"
							            			<c:if test="${_cfg.isAreaEffective==0}">checked</c:if>
							            		><spring:message code="no"/>
							            	</label>
							                <%-- <input class="form-control" type="text" name="isAreaEffective" value="${_cfg.isAreaEffective}"> --%>
							            </div>
							            <div for="isAreaEffective"></div>
							        </div>
							    </div>
							    <div class="col-md-6">
							        <div class="form-group ">
							            <label class="control-label col-md-3"><spring:message code="area_effect_id"></spring:message></label>
							            <div class="col-md-6">
							                 <input class="form-control" type="text" name="areaEffectiveIds" value="${_cfg.areaEffectiveIds}">
							            </div>
							        </div>
							    </div>
						    </c:if>
						    <c:if test="${fn:length(featuresList)>0}">	
						    	<c:set var="index1" value="0"/>
						    	<c:set var="index2" value="0"/>					    	 
						    	 <c:forEach items="${featuresList }" var="features"  >
						    	 	<c:if test="${features.tableType eq 2}">
						    	 	<h3 class="form-section"><spring:message code="${features.tableDesc}"/></h3>
						    	 	<input type="hidden" name="strFeaturesCfgList[${index1}].tableName" value="${features.tableName }"/>
						    	 	<input type="hidden" name="strFeaturesCfgList[${index1}].cfgId" value="${_cfg.strFeaturesCfgList[index1].cfgId }"/>
						    	 	<input type="hidden" name="strFeaturesCfgList[${index1}].compileId" value="${_cfg.strFeaturesCfgList[index1].compileId }"/>
						    	 		<div class="row">
							    	 		<div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
										            <div class="col-md-6">
										                <input class="form-control required" type="text" name="strFeaturesCfgList[${index1}].cfgKeywords" value="${_cfg.strFeaturesCfgList[index1].cfgKeywords }">
										            </div>
										            <div for="cfgDesc"></div>
										        </div>
										    </div>
										    <div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" class="required" name="strFeaturesCfgList[${index1}].exprType" value="1"
										            		<c:if test="${_cfg.strFeaturesCfgList[index1].exprType==1}">checked</c:if>
										            		><spring:message code="and_expression"/>
										            	</label>
										            	<label class="radio-inline">
										            		<input type="radio" class ="required" name="strFeaturesCfgList[${index1}].exprType" value="0"
										            		<c:if test="${_cfg.strFeaturesCfgList[index1].exprType==0}">checked</c:if>
										            		><spring:message code="null_expression"/>
										            	</label>
										            </div>
										            <div for="exprType"></div>
										        </div>
										    </div>
						    	 		</div>
						    	 		<div class="row">
										    
										    <div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
										            <div class="col-md-6">
										            	<select name="strFeaturesCfgList[${index1}].matchMethod" class="required selectpicker select2 form-control" title="<spring:message code="select"/>" >
															<option value="0" <c:if test="${_cfg.strFeaturesCfgList[index1].matchMethod==0  || empty _cfg.strFeaturesCfgList[index1].matchMethod}">selected</c:if>><spring:message code="substring_match"></spring:message></option>   
															<option value="1" <c:if test="${_cfg.strFeaturesCfgList[index1].matchMethod==1 }">selected</c:if>><spring:message code="right_match"></spring:message></option>       
															<option value="2" <c:if test="${_cfg.strFeaturesCfgList[index1].matchMethod==2 }">selected</c:if>><spring:message code="left_match"></spring:message></option>        
															<option value="3" <c:if test="${_cfg.strFeaturesCfgList[index1].matchMethod==3 }">selected</c:if>><spring:message code="exactly_match"></spring:message></option>     
														</select>
										            </div>
										            <div for="matchMethod"></div>
										        </div>
										    </div>
										     <div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
										            <div class="col-md-6">
										            	<select name="strFeaturesCfgList[${index1}].isHexbin" class="required selectpicker select2 form-control" title="<spring:message code="select"/>" >
															<option value="0" <c:if test="${_cfg.strFeaturesCfgList[index1].isHexbin==0 }">selected</c:if>><spring:message code="case_insensitive_nohex"></spring:message></option>
															<option value="1" <c:if test="${_cfg.strFeaturesCfgList[index1].isHexbin==1 }">selected</c:if>><spring:message code="hex_binary"></spring:message></option>
															<option value="2" <c:if test="${_cfg.strFeaturesCfgList[index1].isHexbin==2 }">selected</c:if>><spring:message code="case_sensitive_nohex"></spring:message></option>
														</select>
										            </div>
										            <div for="isHexbin"></div>
										        </div>
										    </div>
										</div>
										<c:set var="index1" value="${index1+1 }"/>
						    	 	</c:if>
							    	<c:if test="${features.tableType eq 4}">
							    		<h3 class="form-section"><spring:message code="${features.tableDesc}"/></h3>
							    		
							    		<input type="hidden" name="complexFeaturesList[${index2}].tableName" value="${features.tableName }"/>
							    		<input type="hidden" name="complexFeaturesList[${index2}].cfgId" value="${_cfg.complexFeaturesList[index2].cfgId }"/>
							    		<input type="hidden" name="complexFeaturesList[${index2}].compileId" value="${_cfg.complexFeaturesList[index2].compileId }"/>
						    	 		<div class="row">
						    	 			<div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_area"/></label>
										            <div class="col-md-6">
										            	<c:set var="districtValue" value="noSelect"></c:set>
										            	<select name="complexFeaturesList[${index2}].district" class="district required selectpicker select2 form-control" title="<spring:message code="select"/>" >
										            		<c:forEach items="${fns:getFeaturesDictData(features.tableName) }" var="item">
																<option value="${item.itemValue }" <c:if test="${_cfg.complexFeaturesList[index2].district==item.itemValue }">selected</c:if>><spring:message code="${item.itemValue}"></spring:message></option>   
										            			<c:if test="${_cfg.complexFeaturesList[index2].district==item.itemValue }">
										            				<c:set var="districtValue" value="${item.itemValue }"></c:set>
										            			</c:if>
										            		</c:forEach>
										            		<c:if test="${districtValue eq 'noSelect' and not empty _cfg.cfgId}">
										            			<option value="${_cfg.complexFeaturesList[index2].district}" selected>
										            				${_cfg.complexFeaturesList[index2].district}
										            			</option>
										            		</c:if>
										            	</select>
										            	<input type="hidden" name="" placeholder="<spring:message code='please_input'/> <spring:message code='custom_region'/>" class="otherValue form-control" value="${_cfg.complexFeaturesList[index2].district}"/>
										                <%-- <input class="required form-control" type="text" name="complexFeaturesList[${index2}].district" value="${_cfg.complexFeaturesList[index2].district }"> --%>
										            </div>
										            <div for="cfgDesc"></div>
										        </div>
										    </div>
							    	 		<div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
										            <div class="col-md-6">
										                <input class="required form-control" type="text" name="complexFeaturesList[${index2}].keywords" value="${_cfg.complexFeaturesList[index2].keywords }">
										            </div>
										            <div for="cfgDesc"></div>
										        </div>
										    </div>
						    	 		</div>
						    	 		<div class="row">
										    <div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
										            <div class="col-md-6">
										            	<label class="radio-inline">
										            		<input type="radio" class="required" name="complexFeaturesList[${index2}].exprType" value="1"
										            		<c:if test="${_cfg.complexFeaturesList[index2].exprType==1}">checked</c:if>
										            		><spring:message code="and"/>
										            	</label>
										            	<label class="radio-inline">
										            		<input type="radio" class="required" name="complexFeaturesList[${index2}].exprType" value="0"
										            		<c:if test="${_cfg.complexFeaturesList[index2].exprType==0}">checked</c:if>
										            		><spring:message code="null"/>
										            	</label>
										            </div>
										            <div for="exprType"></div>
										        </div>
										    </div>
										    <div class="col-md-6">
										        <div class="form-group ">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
										            <div class="col-md-6">
										            	<select name="complexFeaturesList[${index2}].matchMethod" class="required selectpicker select2 form-control" title="<spring:message code="select"/>" >
															<option value="0" <c:if test="${_cfg.complexFeaturesList[index2].matchMethod==0 || empty _cfg.complexFeaturesList[index2].matchMethod}">selected</c:if>><spring:message code="substring_match"></spring:message></option>   
															<option value="1" <c:if test="${_cfg.complexFeaturesList[index2].matchMethod==1 }">selected</c:if>><spring:message code="right_match"></spring:message></option>       
															<option value="2" <c:if test="${_cfg.complexFeaturesList[index2].matchMethod==2 }">selected</c:if>><spring:message code="left_match"></spring:message></option>        
															<option value="3" <c:if test="${_cfg.complexFeaturesList[index2].matchMethod==3 }">selected</c:if>><spring:message code="exactly_match"></spring:message></option>     
														</select>
										            </div>
										            <div for="matchMethod"></div>
										        </div>
										    </div>
										</div>
						    	 		<div class="row">
										    <div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
										            <div class="col-md-6">
										            	<select name="complexFeaturesList[${index2}].isHexbin" class="required selectpicker select2 form-control" title="<spring:message code="select"/>" >
															<option value="0" <c:if test="${_cfg.complexFeaturesList[index2].isHexbin==0 }">selected</c:if>><spring:message code="case_insensitive_nohex"></spring:message></option>
															<option value="1" <c:if test="${_cfg.complexFeaturesList[index2].isHexbin==1 }">selected</c:if>><spring:message code="hex_binary"></spring:message></option>
															<option value="2" <c:if test="${_cfg.complexFeaturesList[index2].isHexbin==2 }">selected</c:if>><spring:message code="case_sensitive_nohex"></spring:message></option>
														</select>
										            </div>
										            <div for="isHexbin"></div>
										        </div>
										    </div>
										</div>
										<c:set var="index2" value="${index2+1 }"/>
						    	 	</c:if> 
						    	 </c:forEach>
	                            
						    </c:if>
						    
						    
                            <c:if test="${not empty _cfg.cfgId}">
                            	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                            </c:if>
                            <input type="hidden" name="audit" value="${audit}">  
                            <input type="hidden" name="serviceId" value="${_cfg.serviceId}">  
                            <input type="hidden" name="compileId" value="${_cfg.compileId}"> 
                            <input type="hidden" name="action" value="${_cfg.action}">  
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