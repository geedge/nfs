<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<script src="${pageContext.request.contextPath}/static/global/scripts/app.min.js" type="text/javascript"></script>
<script>
	 $(document).ready(function() {
		 //$("div[name='tabTitle"+index+"']").get(0).click();
	 })
</script>
<style type="text/css">
</style>
</head>

<c:forEach items="${tabList}" var="region" varStatus="regionStatus">
	<div id="${region[1]}Title${index}" onclick="switchSubCfgTabInfo('${region[1]}',${index})"
		class="col-md-1 tabInfo" name="tabTitle${index }">
			<spring:message code='${region[1]}' />
			<i id="${region[1]}${index}" class="fa" name="tabFlag${index}"></i>
	</div>
</c:forEach>
</div>
<c:forEach items="${tabList}" var="region">
	<c:if test="${region[0] eq 5 }">
		<c:set value="${_cfg}" var="cfg"></c:set>
			<c:if test="${region[1] eq cfg.cfgType }">
				<div id="${region[1]}Info${index}" class="content" name="subCfg${index}">
					<div class="row">
						<div class="col-md-8">
							<div class="form-group">
								<label><spring:message code='src_file' />:</label>
								<a href="javascript:;" data-original-title="${cfg.srcUrl}"
								 class="tooltips" data-flag="false" data-html="true" data-placement="top">
									${fns:abbr(cfg.srcUrl,100)}
								</a>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<div class="form-group">
								<label><spring:message code='sample_file' />:</label>
								<a href="${cfg.sampleUrl }" target="_blank" data-original-title="${cfg.sampleUrl }"
								 class="tooltips" data-flag="false" data-html="true" data-placement="top">
									${fn:substring(cfg.sampleUrl,0,100) }
								</a>
							</div>
						</div>
					</div>
				</div>
			</c:if>
	</c:if>
</c:forEach>
</html>