<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%> 
<script type="text/javascript">
$(function(){
	//console.log("调用须知：调用本页面函数在common.js里，并且需要配置字典MAAT_SERVICE中的项")
});
</script>
<div class="modal fade" id="import_modal" tabindex="-1" role="dialog" aria-labelledby="mo" aria-hidden="true">
	<form id="importForm1" action="${ctx}${urlPrefix}/import?cfgName=${cfgName}"
		method="post" enctype="multipart/form-data" class="form-horizontal"
		onsubmit="loading('<spring:message code='loading'/>');">
		<input type="hidden" id="cfgRegionCode" name="cfgRegionCode" value="" />
		<input type="hidden" id="cfgType" name="cfgType" value=""/>
		<input type="hidden" id="serviceId" name="serviceId" value="">
		<input type="hidden" id="protocolId" name="protocolId" value="">
		<input type="hidden" id="functionId" name="functionId" value="${cfg.functionId}">
		<div class="modal-dialog" role="document" style="width:700px;">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<spring:message code="import" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="alert alert-error hide">
						<button class="close" data-dismiss="alert"></button>
						<span></span>
					</div>
						<div class="form-group">
							<label class="control-label col-md-3"> <spring:message
									code="action" />
							</label>
							<div class="col-md-6">
								<c:forEach items="${serviceList}" var="service"
									varStatus="satus">
									<label class="radio-inline">
										<c:if test="${cfg.functionId eq service.functionId}">
											<c:forEach items="${fns:getDictList('MAAT_SERVICE')}" var="serviceC">
												<c:if test="${service.serviceId==serviceC.itemCode}">
													<span class="${serviceC.itemValue}">
														<input type="radio" name="action" serviceId="${service.serviceId }" protocolId="${service.protocolId }"
															value="${service.action }" disabled class="required action">
														<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
															<c:if test="${dict.itemCode eq service.action}">
																<spring:message code="${dict.itemValue }"/>
															</c:if>
														</c:forEach>
													</span>
												</c:if>
		               						</c:forEach>
										</c:if>
									</label>
								</c:forEach>
							</div>
							<div for="action"></div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label class="col-md-2 control-label" style="margin-top:5px;"><spring:message
										code="chooseFile" />：</label>
								<div class="controls">

									<div class="fileupload fileupload-new"
										data-provides="fileupload">

										<div class="input-append">

											<div class="uneditable-input">
												<i class="fa fa-fa fa-file"></i> <span
													class="fileupload-preview"></span>
											</div>
											<span class="btn btn-file ">
												<button type="button" class="btn fileupload-new">
													<spring:message code="add" />
												</button>
												<button type="button" class="btn fileupload-exists">
													<spring:message code="edit" />
												</button> <input type="file" class="default" id="uploadFile"
												name="file" /> <input type="hidden" name="flag"
												value="${flag }" />
											</span>
											<button type="button" class="btn red fileupload-exists"
												data-dismiss="fileupload" style="margin-left:-1px;">
												<spring:message code="remove" />
												</a>
												<button type="button" onclick="downLoadXLS()"
													class="btn black" style="margin-left:-1px">
													<spring:message code="download" />
												</button>
										</div>
									</div>
								</div>
							</div>
						</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn red" onclick="importCfg()">
						<spring:message code="ok" />
					</button>
					<button type="button" class="btn" data-dismiss="modal">
						<spring:message code="close" />
					</button>
				</div>
			</div>
		</div>
	</form>
</div>