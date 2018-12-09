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
<c:if test="${fn:length(_cfg.ipPortList)>0}">
	<div id="ipTitle${index}" onclick="switchSubCfgTabInfo('ip',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
			<spring:message code='http_ip_title' />
			<i id="ip${index}" class="fa" name="tabFlag${index}"></i>
		
	</div>
</c:if>
<c:if test="${fn:length(_cfg.httpUrlList)>0}">
	<div id="urlTitle${index}" onclick="switchSubCfgTabInfo('url',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_url_title' />
		<i id="url${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(_cfg.httpReqHdrList)>0}">
	<div id="reqHdrTitle${index}" onclick="switchSubCfgTabInfo('reqHdr',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_req_hdr_title' />
		<i id="reqHdr${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(_cfg.httpReqBodyList)>0}">
	<div id="reqBodyTitle${index}" onclick="switchSubCfgTabInfo('reqBody',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_req_body_title' />
		<i id="reqBody${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(_cfg.httpResHdrList)>0}">
	<div id="resHdrTitle${index}" onclick="switchSubCfgTabInfo('resHdr',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_res_hdr_title' />
		<i id="resHdr${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(_cfg.httpResBodyList)>0}">
	<div id="resBodyTitle${index}" onclick="switchSubCfgTabInfo('resBody',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='http_res_body_title' />
		<i id="resBody${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
<c:if test="${fn:length(_cfg.ntcSubscribeIdCfgList)>0}">
	<div id="subscribeIdTitle${index}" onclick="switchSubCfgTabInfo('subscribeId',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
		<spring:message code='NTC_SUBSCRIBE_ID' />
		<i id="resBody${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:if>
</div>

<c:forEach items="${_cfg.ipPortList}" var="cfg">
<div id="ipInfo${index}" class="content" name="subCfg${index}">
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
						<div class="col-md-4">
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
						<div class="col-md-4">
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
</c:forEach>
<c:forEach items="${_cfg.httpUrlList}" var="cfg">
<div id="urlInfo${index}" class="content content${region[0]}" name="subCfg${index}">
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
<c:forEach items="${_cfg.httpReqHdrList}" var="cfg">
<div id="reqHdrInfo${index}" class="content content${region[0]}" name="subCfg${index}">
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
<c:forEach items="${_cfg.httpReqBodyList}" var="cfg">
<div id="reqBodyInfo${index}" class="content content${region[0]}" name="subCfg${index}">
	<div class="row">
		<div class="col-md-8">
			<div class="form-group">
				<label><spring:message code='keywords' />: ${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
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
<c:forEach items="${_cfg.httpResHdrList}" var="cfg">
<div id="resHdrInfo${index}" class="content content${region[0]}" name="subCfg${index}">
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
<c:forEach items="${_cfg.httpResBodyList}" var="cfg">
<div id="resBodyInfo${index}" class="content content${region[0]}" name="subCfg${index}">
	<div class="row">
		<div class="col-md-8">
			<div class="form-group">
				<label><spring:message code='keywords' />: ${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
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
<c:forEach items="${_cfg.ntcSubscribeIdCfgList}" var="cfg">
<div id="subscribeIdInfo${index}" class="content content${region[0]}" name="subCfg${index}">
	<div class="row">
		<div class="col-md-8">
			<div class="form-group">
				<label><spring:message code='keywords' />: ${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
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

</html>