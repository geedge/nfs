<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
	$(function() {

		$("#urlInfo,#urlBtn").on('click',function(){
			$("#cfgFile").trigger("click");
		});
		
		/* $(".action").on("change",function(){
			$("#serviceId").val($(this).attr("serviceId"));
		});
		$("#serviceId").val($(".action:checked").attr("serviceId"));//初始化*/
		
		$("#cfgFile").on('change',function(){
			$("#urlInfo").val($("#cfgFile").val());
		});

		$("#cfgFrom") .validate( {
			submitHandler : function(form) {
				loading('onloading...');
				form.submit();
			},
			errorContainer : "#messageBox",
			errorPlacement : function(error, element) {
				$(element).parents(".form-group").find( "div[for='" + element.attr("name") + "']").append(error);
			},
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
						<form id="cfgFrom" action="${ctx}/proxy/fileStrategy/saveOrUpdate" enctype="multipart/form-data" method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId" value="608">
							<div class="form-body">
							<div class="row">
									<div class="col-md-6">
										<div class="form-group ">
											<label class="control-label col-md-3"><font color="red">*</font>
											<spring:message code="reply_file" /></label>
											<div class="col-md-6">
												<input id="cfgFile" name="cfgFile" type="file" style="width: 330px; display: none" />
												<div class="input-group">
													<input id="urlInfo" name="urlInfo" readonly="readonly" data-msg-required="" placeholder="<spring:message code="select_file"/>" class="required form-control"
														style="background-color: transparent" aria-required="true"
														type="text" value="${_cfg.url }">
													<div class="input-group-btn">
														<a id="urlBtn" class="btn btn-default btn-search"
															href="javascript:" style=""><i class="fa fa-search"></i></a>
													</div>
												</div>
											</div>
											<div for="fileUrl"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font color="red">*</font><spring:message code="file_desc" /></label>
											<div class="col-md-6">
												<input class="form-control required" type="text" name="fileDesc" value="${_cfg.fileDesc}">
											</div>
											<div for="fileDesc"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
												<font color="red">*</font><spring:message code="content_type" />
											</label>
											<div class="col-md-6">
												<select name="contentType" data-live-search="true" data-live-search-placeholder="search" class="selectpicker form-control required">
													<option value=""><spring:message code="select"/></option>
													<c:forEach items="${fns:getDictList('CONTENT_TYPE')}" var="dict">
														<option value="${dict.itemCode}"  <c:if test="${dict.itemCode==_cfg.contentType}">selected</c:if>>${dict.itemCode}</option>
													</c:forEach>
												</select>
											</div>
											<div for="contentType"></div>
										</div>
									</div>
									
									<div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="action"/></label>
								            <div class="col-md-6">
								            	<c:forEach items="${serviceList}" var="service" varStatus="satus">
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
								<input type="hidden" name="isAreaEffective" value="0">
								<%@include file="/WEB-INF/include/form/basicInfo.jsp"%>
							</div>
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
						</form>
						<!-- END FORM-->
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>