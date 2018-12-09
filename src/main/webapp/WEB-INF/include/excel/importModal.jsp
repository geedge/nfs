<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%> 
<script src="${pageContext.request.contextPath}/static/pages/scripts/importExcel.js" type="text/javascript"></script>
<div class="modal fade" id="import_modal" tabindex="-1" role="dialog" aria-labelledby="mo" aria-hidden="true">
	<form id="importForm1" action="${ctx}/ntc/iplist/import?importPath=${importPath}" method="post" enctype="multipart/form-data" class="form-horizontal"
		onsubmit="loading('<spring:message code='loading'/>');">
		<input type="hidden"  id="importTip" value="<spring:message code='import_tip_excel'/>">
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
					
						<input type="hidden" name="fucntionId" id="fucntionId" value="${cfg.functionId }">
						</br>
						<c:if test="${serviceList != null && serviceList.size() > 0}">
						
						<div class="form-group service">
							<label class="control-label col-md-3"> <spring:message
									code="action" />
							</label>
							<div class="col-md-6">
								<c:set var="serviceIndex" value="0"></c:set>
								<c:forEach items="${serviceList}" var="service" >
											<c:if test="${(service.serviceId ne 37) && (service.serviceId ne 149) && (service.isImport eq 1)}">
												<c:if test="${cfg.functionId eq service.functionId}">
													<label class="radio-inline"> 
														<input type="radio" name="serviceDictId"
																serviceId="${service.serviceId }" 
																functionId="${service.functionId }"
																cfgRegionCodeS="${service.regionCode }"
																protocolId="${service.protocolId }"
																action="${service.action }"
																value="${service.dictId }" 
																class="required action"
															<c:if test="${serviceIndex eq 0}">checked</c:if>>
															<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
																<c:if test="${dict.itemCode eq service.action }">
																	<spring:message code="${dict.itemValue }"/>
																</c:if>
															</c:forEach>
													</label>
													<c:set var="serviceIndex" value="${serviceIndex+1 }"></c:set>
												</c:if>
											</c:if>
										</c:forEach>
							</div>
							<div for="action"></div>
						</div>
						</c:if>
						<div class="form-group region">
							<label class="control-label col-md-3"> <spring:message
									code="cfg_type" />
							</label>
							<div class="col-md-6">
							<c:set var="regionIndex" value="0"></c:set>
								<c:forEach items="${regionList}" var="region" >
											<c:if test="${(cfg.functionId eq region.functionId) && (region.isImport eq 1)}">
												<label class="radio-inline">
												<span>
													<input type="radio" name="regionDictIds" class="required" 
															value="${region.dictId }"
															regionValue="${region.configRegionValue }"
															regionType="${region.regionType }" 
															isMaat="${region.isMaat }"
															cfgRegionCodeR="${region.configRegionCode }"
															configServiceType="${region.configServiceType }"
															configMultiKeywords="${region.configMultiKeywords }"
															configHex="${region.configHex }"
															configIpPortShow="${region.configIpPortShow}"
												   			configIpType="${region.configIpType}"
												   			configIpPattern="${region.configIpPattern}"
												   			configPortPattern="${region.configPortPattern}"
												   			configDirection="${region.configDirection}"
												   			configProtocol="${region.configProtocol}"
												   			<c:if test="${regionIndex eq 0}">checked</c:if>>
															<spring:message code="${region.configRegionValue }"/>
												</span>
												</label>
												<c:set var="regionIndex" value="${regionIndex+1 }"></c:set>
											</c:if>
								</c:forEach>
							</div>
							<div for="action"></div>
						</div>
						<!-- 600 asn ip -->
						<c:if test="${cfg.functionId eq 600 }">
							<div class="row hidden">
						</c:if>
						<c:if test="${cfg.functionId ne 600 }">
							<div class="row">
						</c:if>
					           <label class="control-label col-md-3"><font color="red">*</font><spring:message code="letter"/></label>
					           <div class="col-md-6">
					               <select name="requestId" data-live-search="true" data-live-search-placeholder="search" class="selectpicker form-control required">
					               		<%-- <option value=""><spring:message code="select"/></option> --%>
						               	<c:forEach items="${requestInfos}" var="requestInfo" varStatus="status">
						               		<c:if test="${requestInfo.isValid!=0 and requestInfo.isAudit!=3}">
						               			<option value="${requestInfo.id}" 
						               		 	<c:if test="${status.index==0 }">selected</c:if>>${requestInfo.requestTitle}</option>
						               		</c:if>
						               	</c:forEach>
					               </select>
					           </div>
					           <div for="requestId"></div>
						</div>
						</br>
						<!-- 63 app -->
						<c:if test="${cfg.functionId eq 63 or cfg.functionId eq 405 
						or cfg.functionId eq 402 or cfg.functionId eq 403 or cfg.functionId eq 520
						or cfg.functionId eq 563 or cfg.functionId eq 565 or cfg.functionId eq 564
						or cfg.functionId eq 566 or cfg.functionId eq 567}">
							<c:set var="cfgType" value="1"></c:set>
							<c:set var="spec_service_id"><spring:message code="social_app"/></c:set>
							<input type="hidden" name="appError" id="appError" value="${spec_service_id } <spring:message code="required" />">
						</c:if>
						<!-- 408 tunnel behavior -->
						<c:if test="${cfg.functionId eq 408 }">
							<c:set var="cfgType" value="2"></c:set>
							<c:set var="spec_service_id"><spring:message code="encrypted_tunnel_behavior"/></c:set>
							<input type="hidden" name="appError" id="appError" value="${spec_service_id } <spring:message code="required" />">
						</c:if>
						<!-- 407 basic -->
						<c:if test="${cfg.functionId eq 407 }">
							<c:set var="cfgType" value="3"></c:set>
							<c:set var="spec_service_id"><spring:message code="basic_protocol"/></c:set>
							<input type="hidden" name="appError" id="appError" value="${spec_service_id } <spring:message code="required" />">
						</c:if>
						
						<c:if test="${!empty cfgType }">
							<div class="row">
					        	<c:forEach items="${fns:getDictList('IS_SHOW_BUSINESS_TYPE') }" var="showBusinessType">
										<c:if test="${showBusinessType.itemCode==1 }">
										<c:set var="isShowBusinessType" value="true"/>
										</c:if>
										<c:if test="${showBusinessType.itemCode!=1 }">
										<c:set var="isShowBusinessType" value="false"/>
										</c:if>
								</c:forEach>
					            <label class="control-label col-md-3"><font color="red">*</font>${spec_service_id }</label>
					            <div class="col-md-6">
							       <sys:treeselect id="appId" name="appId" value="" 
									 labelName="parent.appIdName" notAllowSelectParent="true" enableSearch="true"
									 labelValue="${spec_service_id }"
									 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=${cfgType }&specific=${!isShowBusinessType}" extId="0"
									notAllowSelectRoot="true" cssClass="form-control required"/>
					            </div>
					            <div for="parent.appIdName"></div>
							</div>
							</br>
								<c:choose>
				    				<c:when test="${cfg.functionId eq 63 or cfg.functionId eq 408}">
				    				<div class="row">
				    				</c:when>
				    				<c:otherwise><div class="row hidden"></c:otherwise>
				    			</c:choose>
						    	 	<label class="control-label col-md-3"><spring:message code="behaviour_type"/></label>
						    	 	 <div class="col-md-6" id="behaviorId">
							    	 	 <select name="behaviorId" data-live-search="true" class="selectpicker form-control">
							    	 	 <option value=""><spring:message code="select"/></option>
							    	 	 </select>
						    	 	 </div>
								</div>
						</br>
						</c:if>
						<c:if test="${cfg.functionId eq 5 or cfg.functionId eq 34 or cfg.functionId eq 6}">
						<div class="row">
							 <label class="control-label col-md-3"><font color="red">*</font><spring:message code="examine"/></label>
							  <div class="col-md-6">
							  		<label class="radio-inline"> 
										<input type="radio" name="isSend" value="0" checked><spring:message code="no"/>
									</label>
							  		<label class="radio-inline"> 
										<input type="radio" name="isSend" value="1"><spring:message code="yes"/>
									</label>
							  </div>
						</div>
						</br>
						</c:if>
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
												name="files" /> <input type="hidden" name="flag"
												value="${flag }" />
											</span>
											<button type="button" class="btn red fileupload-exists"
												data-dismiss="fileupload" style="margin-left:-1px;">
												<spring:message code="remove" />
												</a>
												<button type="button" onclick="downLoadTemplate('${ctx}')"
													class="btn black" style="margin-left:-1px">
													<spring:message code="download" />
												</button>
										</div>
									</div>
								</div>
							</div>
						</div>
				</div>
				<c:if test="${cfg.functionId eq 600 }">
					<div class="modal-footer" style="border-top:0px">
				</c:if>
				<c:if test="${cfg.functionId ne 600 }">
					<div class="modal-footer">
				</c:if>
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