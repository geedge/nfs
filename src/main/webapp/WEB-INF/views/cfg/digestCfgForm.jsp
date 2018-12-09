<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>

</head>
	<%-- <c:forEach items="${regionList}" var="regionDistrict">
		<c:if
		test="${_cfg.functionId eq regionDistrict.functionId 
		and regionDistrict.regionType eq 5 
		and regionValue eq regionDistrict.configRegionValue}"> --%>
		<!-- regionDistrict.regionType==5表示文件摘要类配置 -->
		<input type="hidden" name="${cfgName}.cfgType" value="${region.configRegionValue }">
		<input type="hidden" name="${cfgName}.cfgRegionCode" value="${region.configRegionCode }">
	<%-- </c:if>
</c:forEach> --%>

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
				<spring:message code="file" /></label>
			<div class="col-md-6">
				<input id="file" name="file" type="file" style="width: 330px;display:none" />
					<div class="input-group">
						 <input id="fileInfo" name="fileInfo" readonly="readonly" data-msg-required="" 
							 placeholder="<spring:message code="select_file"/>" class="required form-control" style=" background-color:transparent" 
							 aria-required="true" type="text" value="${digestCfg.digest }">
					
						<div class="input-group-btn">
					 	<a id="uploadSrc" class="btn btn-default btn-search" href="javascript:" style=""><i class="fa fa-search"></i></a>
						</div>
				   </div>
           	 	   <input id="rawLen" name="${cfgName}.rawLen" type="hidden" value="${digestCfg.rawLen }"/>
           	 	   <input id="digest" name="${cfgName}.digest" type="hidden" value="${digestCfg.digest }"/>
			</div>
			<div for="fileInfo"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
			<spring:message code="cfds_level" /></label>
			<div class="col-md-6">
				<input class="form-control required digits" range="[1,10]" type="text"
					name="${cfgName}.cfdsLevel"
					value="${digestCfg.cfdsLevel}">
			</div>
			<div for="${cfgName}.cfdsLevel"></div>
		</div>
	</div>

</div>
</html>
