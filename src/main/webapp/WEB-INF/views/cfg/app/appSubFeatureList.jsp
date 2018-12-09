<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<script>
	 $(document).ready(function() {
		 //$("div[name='tabTitle"+index+"']").get(0).click();
	 })
</script>
<style type="text/css">
</style>
</head>

<%-- <c:if test="${fn:length(ipPortList)>0}">
	<div id="ipTitle${index}" onclick="switchSubCfgTabInfo('ip',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
			<spring:message code='APP_IP_RANGE' />
			<i id="ip${index}" class="fa" name="tabFlag${index}"></i>
		
	</div>
</c:if>
<c:if test="${fn:length(strList)>0}">
	<div id="strTitle${index}" onclick="switchSubCfgTabInfo('str',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_url_title' />
		<i id="str${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(complexList)>0}">
	<div id="complexTitle${index}" onclick="switchSubCfgTabInfo('complex',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_req_hdr_title' />
		<i id="complex${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if> --%>

<c:forEach items="${tabList}" var="region" varStatus="regionStatus">
	<div id="${region.regionValue}_${regionStatus.index}Title${index}" onclick="switchSubCfgTabInfo('${region.regionValue}_${regionStatus.index}',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
			<spring:message code='${region.regionValue}_${regionStatus.index}' />
			<i id="${region.regionValue}_${regionStatus.index}${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:forEach>

</div>
<c:forEach items="${tabList}" var="region" varStatus="regionStatus">
	
	<c:if test="${region.regionType eq 0 }">
		${region.regionValue}_${regionStatus.index}
	</c:if>
	<c:if test="${region.regionType eq 1 }">
		<c:forEach items="${region.ipRangeList}" var="cfg">
			<c:if test="${region.regionValue eq cfg.cfgType }">
				<div id="${region.regionValue}_${regionStatus.index}Info${index}" class="content" name="subCfg${index}">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='ip_type'/>:</label>
								<label>
									<c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
										<c:if test="${cfg.ipType==ipTypeC.itemCode}"><spring:message code="${ipTypeC.itemValue }"/></c:if>
									</c:forEach>
								</label>
							</div>
						</div>
						</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='ip_pattern'/>:</label>
								<label>
									<c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
										<c:if test="${cfg.ipPattern==ipPatternC.itemCode}"><spring:message code="${ipPatternC.itemValue }"/></c:if>
									</c:forEach>
								</label>
							</div>
						</div>
						<div class="col-md-4 hidden">
							<div class="form-group">
								<label><spring:message code='client_ip'/>:</label>
								<label>
									${cfg.srcIpAddress}
								</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='port_pattern'/>:</label>
								<label>
									<c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
										<c:if test="${cfg.portPattern eq portPatternC.itemCode}"><spring:message code="${portPatternC.itemValue }"/></c:if>
									</c:forEach>
								</label>
							</div>
						</div>
						<div class="col-md-4 hidden">
							<div class="form-group">
								<label><spring:message code='client_port'/>:</label><label>${cfg.srcPort }</label>
							</div>
						</div>
						</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='server_ip'/>:</label><label>${cfg.destIpAddress }</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='server_port'/>:</label><label>${cfg.destPort }</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='direction'/>:</label>
								<label>
									<c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
										<c:if test="${cfg.direction eq directionC.itemCode}"><spring:message code="${directionC.itemValue }"/></c:if>
									</c:forEach>
								</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label><spring:message code='protocol'/>:</label>
								<label>
									<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
										<c:if test="${cfg.protocol eq protocolC.itemCode}"><spring:message code="${protocolC.itemValue }"/></c:if>
									</c:forEach>
								</label>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			</c:forEach>
	</c:if>
	<c:if test="${region.regionType eq 2 }">
		<c:forEach items="${region.strList}" var="cfg">
			<div id="${region.regionValue}_${regionStatus.index}Info${index}" class="content content${region[0]}" name="subCfg${index}">
				<div class="row">
					<div class="col-md-8">
						<div class="form-group">
							<label><spring:message code='URL' />: ${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
						</div>
					</div>
					
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label><spring:message code='expression_type' />:</label>
							<label>
								<c:choose>
									<c:when test="${cfg.exprType eq 1}"><spring:message code='and_expression'/></c:when>
									<c:when test="${cfg.exprType eq 0}"><spring:message code='null_expression'/></c:when>
								</c:choose>
							</label>
						</div>
					</div>
					</div>
					<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label><spring:message code='match_method' />:</label>
							<label>
								<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
									<c:if test="${cfg.matchMethod==matchMethodC.itemCode }"><spring:message code="${matchMethodC.itemValue}"/></c:if>
								</c:forEach>   
							</label>
						</div>
					</div>
					</div>
					<c:if test="${cfg.isHexbin==0 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="not_hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_insenstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
								<c:if test="${cfg.isHexbin==1 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_insenstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
								<c:if test="${cfg.isHexbin==2 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="not_hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_senstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
			</div>
			
			</c:forEach>
	</c:if>
	
	<c:if test="${region.regionType eq 3 }">
		<c:forEach items="${region.complexList}" var="cfg">
			<div id="${region.regionValue}_${regionStatus.index}Info${index}" class="content content${region[0]}" name="subCfg${index}">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label><spring:message code='district' />: ${cfg.district }</label>
						</div>
					</div>
					</div>
					<div class="row">
					<div class="col-md-8">
						<div class="form-group">
							<label class="keyword-disinline"><spring:message code='keywords' />: ${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-8">
						<div class="form-group">
							<label><spring:message code='expression_type' />:</label>
							<label>
								<c:choose>
									<c:when test="${cfg.exprType eq 1}"><spring:message code='and_expression'/></c:when>
									<c:when test="${cfg.exprType eq 0}"><spring:message code='null_expression'/></c:when>
								</c:choose>
							</label>
						</div>
					</div>
					</div>
					<div class="row">		
					<div class="col-md-8">
						<div class="form-group">
							<label><spring:message code='match_method' />:</label>
							<label>
								<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
									<c:if test="${cfg.matchMethod==matchMethodC.itemCode }"><spring:message code="${matchMethodC.itemValue}"/></c:if>
								</c:forEach>
							</label>
						</div>
					</div>
					</div>
				<c:if test="${cfg.isHexbin==0 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="not_hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_insenstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
								<c:if test="${cfg.isHexbin==1 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_insenstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
								<c:if test="${cfg.isHexbin==2 }">
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_hex"/>:</label>
												<label>
													<spring:message code="not_hex"/>
												</label>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<label><spring:message code="is_case_insenstive"/>:</label>
												<label>
													<spring:message code="case_senstive"/>
												</label>
											</div>
										</div>
									</div>
								</c:if>
				</div>
			</c:forEach>
	</c:if>
</c:forEach>


</html>