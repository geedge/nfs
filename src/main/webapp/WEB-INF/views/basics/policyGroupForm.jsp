<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	if($("[name=groupId]").val()){
		$("[name=groupType] option").each(function(){
			if("${_cfg.groupType}"!=$(this).attr("value")){
				$(this).attr("disabled",true);
			}
		});
		$("[name=groupType]").selectpicker("refresh");
	}
	/* $(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
	}); */
	
	if($("#groupType").val() == 4){
		$("#asnNo").parents(".form-group").removeClass("hidden");
	}
	$("#groupType").on("change",function(){
		if($(this).val() != 4){
			$("#asnNo").parents(".form-group").addClass("hidden");
		}else{
			$("#asnNo").parents(".form-group").removeClass("hidden");
		}
	});
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			if($(element).parents().hasClass("tagsinput")){
				$(element).parents(".col-md-6").next("div").append(error);
			}else{
				$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
			}
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
		<spring:message code="policy_group_manage"></spring:message>
	</h3>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
                        <c:if test="${empty _cfg.groupId}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${not empty _cfg.groupId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form:form id="cfgFrom" modelAttribute="_cfg" action="${ctx}/basics/policyGroup/saveOrUpdate" method="post" class="form-horizontal">
                      	<input type="hidden" name="groupId" value="${_cfg.groupId}">
                          <div class="form-body">
                              <div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								             <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group_name"/></label>
								            <div class="col-md-6">
								                <input class="form-control required" type="text" name="groupName" value="${_cfg.groupName}">
								            </div>
								            <div for="groupName"></div>
								        </div>
								    </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group_type"/></label>
								            <div class="col-md-6">
								            	<select id="groupType" name="groupType" class="selectpicker select2 form-control required" >
													<c:forEach items="${fns:getDictList('GROUP_TYPE')}" var="groupTypeC">
														<c:if test="${groupTypeC.itemCode ne 2 && groupTypeC.itemCode ne 3}">
															<option value="${groupTypeC.itemCode}" <c:if test="${_cfg.groupType==groupTypeC.itemCode || (_cfg.groupType==null && groupTypeC.itemCode==1)}">selected</c:if>><spring:message code="${groupTypeC.itemValue}"/></option>
							               				</c:if>
							               			</c:forEach>
												</select>
								            </div>
								            <div for="groupType"></div>
								       </div>
								   </div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="col-md-3 control-label"><spring:message code="desc"/>:</label>
											<div class="col-md-6">
												<form:textarea path="description" htmlEscape="false" maxlength="128" class="form-control" />
											</div>
										</div>
									</div>
									<div class="col-md-6">
								        <div class="form-group hidden">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="asn_no"/></label>
								            <div class="col-md-6">
								                <input id="asnNo" class="form-control required asnNoUnique number" type="text" name="asnNo" value="${_cfg.asnNo}" autocomplete="off" maxlength="15" ctx="${ctx }">
								            </div>
								            <div for="asnNo"></div>
								        </div>
								    </div>
								</div>
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
                      </form:form>
                      <!-- END FORM-->
                  </div>
              </div>
		</div>
	</div>
</div>
</body>
</html>