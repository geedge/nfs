<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	/* $("input[type=checkbox]").bootstrapSwitch();
	$("input[type=checkbox]").each(function(){
		$(this).on("switchChange.bootstrapSwitch",function(event,state){
			if(state){
				$(this).val(1);
			}else{
				$(this).val(0);
			}
		})
	}); */
	if($("#oldAction").val()==1&&$(".action:checked").attr("id")!="actionOff"){
		$(".level").removeClass("hidden");
		$(".level").find("select[name='level']").find("option").eq(0).attr("value","");
		$(".level").find("select[name='level']").val("100");
		$(".level").find("select[name='level']").selectpicker("refresh");
	}else{
		$(".level").addClass("hidden");
		$(".level").find("select[name$='level']").find("option").eq(0).attr("value","0");
		$(".level").find("select[name$='level']").val("0");
		$(".level").find("select[name$='level']").selectpicker("refresh");
	}
	$("input[name~=action]").on("change",function(){
		$("#serviceId").val($(this).attr("serviceId"));
		if($(".action:checked").val()==1){
			$(".level").removeClass("hidden");
			$(".level").find("select[name$='level']").find("option").eq(0).attr("value","");
			$(".level").find("select[name$='level']").val("100");
			$(".level").find("select[name$='level']").selectpicker("refresh");
		}else{
			$(".level").addClass("hidden");
			$(".level").find("select[name$='level']").find("option").eq(0).attr("value","0");
			$(".level").find("select[name$='level']").val("0");
			$(".level").find("select[name$='level']").selectpicker("refresh");
		}
		if($("#level").is(":hidden")){
			$("input[name$='level']").val(0);
		}
		if($(".action:checked").attr("id")=="actionOff"){
				$(".level").addClass("hidden");
				$(".level").find("select[name$='level']").find("option").eq(0).attr("value","0");
				$(".level").find("select[name$='='level']").val("0");
				$(".level").find("select[name$='level']").selectpicker("refresh");
			}
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#save").click(function(){
		if($(".action:checked").attr("id")=="actionOff"){
			$("#isValid").val(0);
		}
		if($(".action:checked").attr("id")=="actionOn"){
			$("#isValid").val(1);
		}
		
	})
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
			loading('onloading...');
			form.submit();
		},
		errorContainer: "#messageBox",
	});

});
</script>
</head>
<body>
<div class="page-content">
 	<h3 class="page-title">
		<spring:message code="av_sample_video_porn_control"></spring:message>
	</h3>
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
					</div>
                  </div>
                  <div class="portlet-body form">
                  	  <sys:message content="${message}" />
                  	  <shiro:hasPermission name="avSignSample:preset">
                  	  	<form id="presetFrom" action="${ctx}/ntc/av/sample/auditAvSignSample" method="get" class="form-horizontal">
	                      <input type="hidden" name="functionId" value="${functionId }">
	                      <input type="hidden" name="cfgs[0].functionId" value="${functionId }">
	                      <input type="hidden" name="cfgs[0].isValid" value="1">
	                      <input type="hidden" name="cfgs[0].isAudit" value="1">
	                      <input type="hidden" name="cfgs[0].isAreaEffective" value="0">
	                      <input type="hidden" name="cfgs[0].description" value="0">
	                      <input type="hidden" name="cfgs[0].level" value="0">
	                      <input type="hidden" name="preset" value="1">
	                      <c:if test="${fn:length(serviceList)>0 and fn:length(cfgs)eq 0}">
	                      	<input type="hidden" name="cfgs[0].serviceId" value="${serviceList[0].serviceId}">
                   			<input type="hidden" name="cfgs[0].action" value="${serviceList[0].action}">
                   			<div class="form-body">
		             	 	 <!-- BEGIN FORM-->
		                     <div class="row">
								<div class="col-md-6">
									 <div class="form-group">
				                      	 <div class="" style="background-color: #FFFFFF">
				                             <div class="row">
				                                 <div class="col-md-8">
				                                     <div class="row">
				                                         <div class="col-md-offset-8 col-md-8">
				                                             <button id="preset" type="submit" class="btn-danger"><spring:message code="preset"/></button>
				                                         </div>
				                                     </div>
				                                 </div>
				                             </div>
				                         </div>
						               </div>
									</div>
								</div>
							</div>
	                      </c:if>
	                    </form>
                  	  </shiro:hasPermission>
                  	 		
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/ntc/av/sample/auditAvSignSample" method="post" class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${functionId}">
                          <div class="form-body">
                          	 <table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap hidden">
							 <tbody>	
							 <c:choose>
							  	<c:when test="${fn:length(cfgs)>0 }">
							 		<c:forEach items="${cfgs}" var="_cfg" varStatus="status">
							  		<input type="hidden" name="cfgs[${status.index }].cfgId" value="${_cfg.cfgId }">
									<input type="hidden" id="serviceId" name="cfgs[${status.index }].serviceId" value="${_cfg.serviceId }">
			                       	<input type="hidden" name="cfgs[${status.index }].compileId" value="${_cfg.compileId}">
			                       	<input type="hidden" name="cfgs[${status.index }].isValidOld" value="${_cfg.isValid}">
			                       	<input type="hidden" id="oldAction" name="cfgs[${status.index }].actionOld" value="${_cfg.action}">
			                       	<input type="hidden" id="isValid" name="cfgs[${status.index }].isValid" value="1">
			                       	<input type="hidden" id="isAudit" name="cfgs[${status.index }].isAudit" value="0">
		                              <div class="row">
									    	<div class="col-md-6">
		      									<div class="form-group">
		      										<label class="control-label col-md-3"><spring:message code="is_audit"/></label>
		      										<div class="col-md-6" style="margin-top:8px">
													<c:choose>
														<c:when test="${_cfg.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
														<c:when test="${_cfg.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
														<c:when test="${_cfg.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
														<c:when test="${_cfg.isAudit eq '3'}"><span class="label label-warning"><spring:message code="cancel_approved"></spring:message></span></c:when>
													</c:choose>
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
										</div>
										<div class="row">
											<div class="col-md-6">
										        <div class="form-group ">
										           <div id="level" class="form-group level hidden" >
											            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="harm_level"/></label>
											            <div class="col-md-6">
											            	 <select name="cfgs[${status.index }].level" data-live-search="true" class="selectpicker form-control required">
																	<option value="0" ><spring:message code="selected"/></option>
																	<c:forEach items="${fns:getDictList('CONFIDENCE_INTERVAL')}" var="configdenceC">
																		<option value="${configdenceC.itemCode}" <c:if test="${_cfg.level==configdenceC.itemCode || (_cfg.level==null && configdenceC.itemCode eq 100)}">selected</c:if>><spring:message code="${configdenceC.itemValue}"/></option>
																	</c:forEach>
																</select>
											            </div>
											        </div>
											            
											            <div for="cfgs[${status.index }].level"></div>
										        </div>
										    </div>
										</div>
										<tr>
											<td><input type="checkbox" checked="true" class="i-checks" id="${_cfg.cfgId}" value="${_cfg.isAudit}"></td>
										</tr>
									</c:forEach>
							 	</c:when>
							 	<c:otherwise>
							 	</c:otherwise>
							 </c:choose>
							</tbody></table>
                          </div>
                          <div class="form-actions">
                              <div class="row">
                                  <div class="col-md-6">
                                      <div class="row">
                                          <div class="col-md-offset-3 col-md-8">
                                          	  <shiro:hasPermission name="avSignSample:config">
                                              <button id="save" type="submit" class="btn green"><spring:message code="edit"/></button>
                                              </shiro:hasPermission>
                                              <shiro:hasPermission name="avSignSample:confirm">
					                            <div class="btn-group">
					                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
					                                    <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
					                                    <i class="fa fa-angle-down"></i>
					                                </button>
					                                <ul class="dropdown-menu pull-right">
					                                    <li><sys:delRow url="${ctx}/ntc/av/sample/auditAvAudioSignSample?isAudit=1&isValid=1&functionId=${functionId }" id="contentTable" label="approved"></sys:delRow></li>
												  		<li><sys:delRow url="${ctx}/ntc/av/sample/auditAvAudioSignSample?isAudit=2&isValid=0&functionId=${functionId }" id="contentTable" label="unapproved"></sys:delRow></li>
												  		<li><sys:delRow url="${ctx}/ntc/av/sample/auditAvAudioSignSample?isAudit=3&isValid=0&functionId=${functionId }" id="contentTable" label="cancelPass"></sys:delRow></li>            
					                       		  </ul>
					                    		</div>
		                        			</shiro:hasPermission>
                                              <%-- <button id="cancel" type="button" class="btn default"><spring:message code="cancel"/></button> --%>
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