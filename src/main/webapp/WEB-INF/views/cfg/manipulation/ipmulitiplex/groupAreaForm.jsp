<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	
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
		<spring:message code="area_group_manage"></spring:message>
	</h3>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
                        <c:if test="${empty _cfg.id}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${not empty _cfg.id}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/cfg/groupArea/saveOrUpdate" method="post" class="form-horizontal">
                      	<input type="hidden" name="id" value="${_cfg.id}">
                          <div class="form-body">
                              <div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group_name"/></label>
								            <div class="col-md-6">
								            	 <select name="groupId" class="selectpicker show-tick form-control required" data-live-search="true" data-live-search-placeholder="search" >
													<option value="" <c:if test="${empty _cfg.groupId }">selected</c:if>><spring:message code="select"/></option>
					               					<c:forEach items="${policyGroups }" var="policyGroup">
					               							<option value="${policyGroup.groupId}" <c:if test="${_cfg.groupId==policyGroup.groupId }">selected</c:if>><spring:message code="${policyGroup.groupName}"/></option>
							              			</c:forEach>
												</select>
								            </div>
								            <div for="groupId"></div>
								        </div>
								   </div>
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="area"/></label>
								            <div class="col-md-6">
								            	 <select name="areaCode" class="selectpicker show-tick form-control required" data-live-search="true" data-live-search-placeholder="search" >
													<option value="" ><spring:message code="select"/></option>
													<c:forEach items="${areas}" var="area">
														<option value="${area.itemCode}" <c:if test="${_cfg.areaCode == area.itemCode }">selected</c:if> >${area.itemValue}</option>
													</c:forEach>
												</select>
								            </div>
								            <div for="areaCode"></div>
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
                      </form>
                      <!-- END FORM-->
                  </div>
              </div>
		</div>
	</div>
</div>
</body>
</html>