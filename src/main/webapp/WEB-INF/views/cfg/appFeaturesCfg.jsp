<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<script type="text/javascript">
$(function(){
	$("#cfgFrom").find("input,select,radio").attr("disabled",true);

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
                        <spring:message code="show"></spring:message>
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
						    <c:if test="${fn:length(featuresList)>0}">	
						    	<c:set var="index1" value="0"/>
						    	<c:set var="index2" value="0"/>					    	 
						    	 <c:forEach items="${featuresList }" var="features"  >
						    	 	<c:if test="${features.tableType eq 2}">
						    	 	<h3 class="form-section"><spring:message code="${features.tableDesc}"/></h3>
						    	 	<input type="hidden" name="strFeaturesCfgList[${index1}].tableName" value="${features.tableName }"/>
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
															<option value="0" <c:if test="${_cfg.strFeaturesCfgList[index1].matchMethod==0 || empty _cfg.strFeaturesCfgList[index1].matchMethod}">selected</c:if>><spring:message code="substring_match"></spring:message></option>   
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
							    		<input type="hidden" name="complexFeaturesList[${index2}].compileId" value="${_cfg.complexFeaturesList[index2].compileId }"/>
						    	 		<div class="row">
						    	 			<div class="col-md-6">
										        <div class="form-group">
										            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_area"/></label>
										            <div class="col-md-6">
										            	<c:set var="districtValue" value="noSelect"></c:set>
										            	<select name="complexFeaturesList[${index2}].district" class="required selectpicker select2 form-control" title="<spring:message code="select"/>" >
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
										            		><spring:message code="and_expression"/>
										            	</label>
										            	<label class="radio-inline">
										            		<input type="radio" class="required" name="complexFeaturesList[${index2}].exprType" value="0"
										            		<c:if test="${_cfg.complexFeaturesList[index2].exprType==0}">checked</c:if>
										            		><spring:message code="null_expression"/>
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
                            <input type="hidden" name="action" value="${_cfg.action}">  
                          </div>
                          <div class="form-actions">
                              <div class="row">
                                  <div class="col-md-6">
                                      <div class="row">
                                          <div class="col-md-offset-3 col-md-8">
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