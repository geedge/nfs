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
			//var cfgId=$("#cfgId").val();
			var message="";
			/* if(flag){
				if($("#isAdd").val() == 'true'){
					$.ajax({
						type:'post',
						async:false,
						url:'${ctx}/maintenance/dnsResStrategy/validCfgId',
						data:{"cfgId":cfgId},
						success:function(data){
								if(data){
									flag=false;
									//message="策略号已存在";
									message="<spring:message code='policy_number_value_unique'/>";
								}
						}
					
					});
				}
			} */
			/* if(flag){
				$("#cfgId").removeClass("error");
				$("#cfgId").attr("aria-invalid","false");
			}else{
				$("div[for=cfgId]").html("<label for=\"cfgId\" class=\"error\" id=\"cfgId-error\">"+message+"</label>");
				$("#cfgId").addClass("error");
				$("#cfgId").attr("aria-invalid","true");
			} */
			if(flag){
				var maxTtl=$("[name=maxTtl]").val();
				maxTtl = parseInt(maxTtl);
				var minTtl=$("[name=minTtl]").val();
				minTtl = parseInt(minTtl);
				if(minTtl>maxTtl){
					message="<spring:message code='max_shouldnot_less_than_min'/>";
					$("div[for=maxTtl]").html("<label for=\"maxTtl\" class=\"error\" id=\"maxTtl-error\">"+message+"</label>");
					flag=false;
				}
			}
			if(flag){
				loading('onloading...');
				form.submit();
			}
		},
		errorContainer: "#messageBox"
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
                        <c:if test="${isAdd eq true}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${isAdd eq false}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/maintenance/dnsResStrategy/saveOrUpdate" method="post" class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="0">
                       	<input type="hidden" id="isAdd" name="isAdd" value="${isAdd}">
                       	
                       	<input type="hidden" id="cfgId" name="cfgId" value="${_cfg.cfgId}">
                       	
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   			</c:if>
                   		</c:forEach>
                          <div class="form-body">
                              <div class="row">
								   <%--  <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="policy_number"/></label>
								            <div class="col-md-6">
								                <input class="form-control required number" <c:if test="${!isAdd}" >readonly="true"</c:if>  type="text" id="cfgId" min="101" name="cfgId" value="${_cfg.cfgId}">
								            </div>
									    <div for="cfgId"></div>
								        </div>
								    </div> --%>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="policy_name"/></label>
								            <div class="col-md-6">
								                <input class="form-control required" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
										    <div for="cfgDesc"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="min_ttl"/></label>
								            <div class="col-md-2">
								                <input class="form-control required number"  placeholder="<spring:message code="ttl_unit" />" type="text" name="minTtl" value="${_cfg.minTtl}" min="0">
								            </div>
								            <div class="col-md-1" style="text-align: center;vertical-align: center">
								                -
								            </div>
								            <div class="col-md-2">
								                <input class="form-control required number"  placeholder="<spring:message code="ttl_unit" />" type="text" name="maxTtl" value="${_cfg.maxTtl}" min="0">
								            </div>
										    <div for="minTtl"></div>
										    <div for="maxTtl"></div>
								        </div>
								    </div>
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="action"/></label>
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
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group"/></label>
								            <div class="col-md-6">
								            	<select name="resGroup1Id" class="selectpicker show-tick form-control required">
													<option value=""><spring:message code="select"/></option>
					               					<c:forEach items="${policyGroups }" var="policyGroup">
														<option value="${policyGroup.groupId}" <c:if test="${_cfg.resGroup1Id==policyGroup.groupId }">selected</c:if>><spring:message code="${policyGroup.groupName}"/></option>
							              			</c:forEach>
								                </select>
								            </div>
								            <div for="resGroup1Id"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="res_group_num"/></label>
								            <div class="col-md-6">
								                <input class="form-control number required" range=[1,128] type="text" name="resGroup1Num" value="${_cfg.resGroup1Num}">
								            </div>
										    <div for="resGroup1Num"></div>
								        </div>
								    </div>
								</div>
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