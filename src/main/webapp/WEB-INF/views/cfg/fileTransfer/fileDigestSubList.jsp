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

<c:if test="${fn:length(tabList)==0}">
	<div id="NTC_UNIVERSAL_IPInfo${index}" class="content" name="subCfg${index}">
		<div class="row">
			<div class="col-md-4">
				<div class="form-group">
					<spring:message code='no_data' />
				</div>
			</div>
		</div>
	</div>
</c:if>

<c:forEach items="${tabList}" var="region" varStatus="regionStatus">
	<div id="${region[1]}Title${index}" onclick="switchSubCfgTabInfo('${region[1]}',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
			<spring:message code='${region[1]}' />
			<i id="${region[1]}${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:forEach>

</div>
<c:forEach items="${tabList}" var="region">
	<c:if test="${region[0] eq 2 }">
			<c:forEach items="${_cfg.ntcSubscribeIdCfgList}" var="cfg">
			<c:if test="${region[1] eq cfg.cfgType }">
				<div id="${region[1]}Info${index}" class="content content${region[0]}" name="subCfg${index}">
					<div class="row">
						<div class="col-md-8">
							<div class="form-group">
								<label><spring:message code='keywords' />: 
								${fn:replace(cfg.cfgKeywords, "***and***", " ")}</label>
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
			</c:if>
		</c:forEach>
	</c:if>
	
</c:forEach>
</html>