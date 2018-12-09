<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<script type="text/javascript">
</script>
</head>
<%-- <c:forEach items="${regionList}" var="regionDistrict">
	<c:if
		test="${ipPort.functionId eq regionDistrict.functionId and regionDistrict.regionType eq 1}"> --%>
		<!-- regionDistrict.regionType==1表示IP类配置 -->
		<input type="hidden" name="${cfgName}.cfgType" value="${region.configRegionValue }">
		<input type="hidden" name="${cfgName}.cfgRegionCode" 
		 isMaat="${region.isMaat}"
   		 serviceType="${region.configServiceType}"
   		 ipPortShow="${region.configIpPortShow}"
   		 ipType="${region.configIpType}"
   		 ipPattern="${region.configIpPattern}"
   		 portPattern="${region.configPortPattern}"
   		 direction="${region.configDirection}"
   		 protocol="${region.configProtocol}"
   		 regionType="${region.regionType}"
		value="${region.configRegionCode }">
	<%-- </c:if>
</c:forEach> --%>
<input type="hidden" name="${cfgName}.protocolId" id="protocolId" value="">
<div class="row">
	<div class="pull-right">
		<span class="glyphicon glyphicon-remove pull-right" title="remove"
			onClick="delContent('${tabName}${status.index}','${tabName}Add');" />
	</div>
</div>
<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="ip_type" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.ipType"
					class="selectpicker show-tick form-control required">
					<c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
						<option value="${ipTypeC.itemCode}" <c:if test="${ipPort.ipType==ipTypeC.itemCode || (ipPort.ipType==null && ipTypeC.itemCode==4)}">selected</c:if>><spring:message code="${ipTypeC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="${cfgName}.ipType"></div>
		</div>
	</div>
</div>
<div class="row ip">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="ip_pattern" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.ipPattern"
					class="selectpicker show-tick form-control required">
					<c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
						<option value="${ipPatternC.itemCode}" <c:if test="${ipPort.ipPattern==ipPatternC.itemCode || (ipPort.ipPattern==null && ipPatternC.itemCode==3)}">selected</c:if>><spring:message code="${ipPatternC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="${cfgName}.ipPattern"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="client_ip" /></label>
			<div class="col-md-6">
				<input class="form-control required ipCheck" type="text"
					name="${cfgName}.srcIpAddress"
					value="${ipPort.srcIpAddress}">
			</div>
			<div for="${cfgName}.srcIpAddress"></div>
		</div>
	</div>
</div>
<div class="row hidden port">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="port_pattern" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.portPattern"
					class="selectpicker show-tick form-control required">
					<c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
						<option value="${portPatternC.itemCode}" <c:if test="${ipPort.portPattern==portPatternC.itemCode || (ipPort.portPattern==null && portPatternC.itemCode==1)}">selected</c:if>><spring:message code="${portPatternC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="${cfgName}.portPattern"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="client_port" /></label>
			<div class="col-md-6">
				<input class="form-control required portCheck" type="text"
					name="${cfgName}.srcPort"
					value="${ipPort.srcPort}">
			</div>
			<div for="${cfgName}.srcPort"></div>
		</div>
	</div>
</div>
<div class="row hidden destPort">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="server_ip" /></label>
			<div class="col-md-6">
				<input class="form-control required ipCheck" type="text"
					name="${cfgName}.destIpAddress"
					value="${ipPort.destIpAddress}">
			</div>
			<div for="${cfgName}.destIpAddress"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="server_port" /></label>
			<div class="col-md-6">
				<input class="form-control required portCheck" type="text"
					name="${cfgName}.destPort"
					value="${ipPort.destPort}">
			</div>
			<div for="${cfgName}.destPort"></div>
		</div>
	</div>
</div>
<input type="hidden" name="${cfgName}.protocol" value="0">
<input type="hidden" name="${cfgName}.direction" value="0">
<div class="row">
	<button type="button" class="btn btn-red-hollow center-block"
		onClick="more(this);" data-click-times="0">
		<spring:message code="show_more" />
	</button>
</div>
<br>

</html>