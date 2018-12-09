<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$("#fileInfo,#uploadFile").on('click', function() {
		$("#file").trigger("click");
	});
	$("#file").on('change', function() {
		$("#fileInfo").val($("#file").val());
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
			
			
			var file = $("#file").val();
			var fileInfo = $("#fileInfo").val();
			if((file==null||file=="") && (fileInfo==null || fileInfo=="")){
				$("div[for='fileInfo']").append("<label id='level-error' class='error' for='file'><spring:message code='required'></spring:message></label>");
				return false;
			}else{
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
                      <form id="cfgFrom" action="${ctx}/app/appBuiltinFeatureFileSaveOrUpdate" method="post" enctype="multipart/form-data"  class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" id="cfgId" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="0">
                       	<input type="hidden" id="isAdd" name="isAdd" value="${isAdd}">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   			</c:if>
                   		</c:forEach>
                   		<c:forEach items="${serviceList}" var="service">
                   			<c:if test="${_cfg.functionId eq service.functionId}">
                   				<input type="hidden" name="serviceId" value="${service.serviceId}">
                   				<input type="hidden" name="action" value="${service.action}">
                   			</c:if>
                   		</c:forEach>
                          <div class="form-body">
                              <div class="row">
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
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="desc"/></label>
								            <div class="col-md-6">
								                <input class="form-control required" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
										    <div for="cfgDesc"></div>
								        </div>
								    </div>
								        <div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font> <spring:message code="file" /></label>
											<div class="col-md-6">
												<input id="file" name="file" type="file"
													style="width: 330px; display: none" />
												<div class="input-group">
													<input id="fileInfo" name="fileInfo" readonly="readonly"
														data-msg-required=""
														placeholder="<spring:message code="select_file"/>"
														class="required form-control"
														style="background-color: transparent" aria-required="true"
														type="text" value="${_cfg.filePath }">

													<div class="input-group-btn">
														<a id="uploadFile" class="btn btn-default btn-search"
															href="javascript:" style=""><i class="fa fa-search"></i></a>
													</div>
													<input id="filePath" name="filePath" type="hidden" value="${_cfg.filePath }"/>
												</div>
											</div>
											<div for="fileInfo"></div>
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