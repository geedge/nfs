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
	<div id="APP_IP_REANGETitle${index}"  onclick="switchSubCfgTabInfo('APP_IP_REANGE',${index})"
		class="col-md-1 tabInfo badge-info" name="tabTitle">
			<spring:message code='APP_IP_REANGE' />
			<i id="APP_IP_REANGE${index}" class="fa fa-angle-double-down" name="tabFlag${index}"></i>
	</div>
	</div>
	<div id="APP_IP_REANGEInfo${index}" class="content" name="subCfg${index}">
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
	<c:if test="${region[0] eq 1 }">
		<c:forEach items="${ipRangeList}" var="cfg">
			<c:if test="${region[1] eq cfg.cfgType }">
				<div id="${region[1]}Info${index}" class="content" name="subCfg${index}">
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
			</c:if>
			</c:forEach>
	</c:if>
</c:forEach>
</html>