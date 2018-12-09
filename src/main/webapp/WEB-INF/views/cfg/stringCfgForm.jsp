<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<script type="text/javascript">
$(function(){
	
});
</script>
</head>
<%-- <c:forEach items="${regionList}" var="regionDistrict">
	<c:if
		test="${strCfg.functionId eq regionDistrict.functionId 
		and regionDistrict.regionType eq 2 
		and regionValue eq regionDistrict.configRegionValue}"> --%>
		<!-- regionDistrict.regionType==2表示字符串类配置 -->
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
			<spring:message code="keywords" /></label>
			<div class="col-md-6">
				<input class="form-control required tags" type="text" id="tags_${tabName}"
					name="${cfgName}.cfgKeywords"
					value="${strCfg.cfgKeywords}">
			</div>
			<div for="${cfgName}.cfgKeywords"></div>
		</div>
	</div>
	
</div>
<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="expression_type" /></label>
			<div class="col-md-6">
				<label class="radio-inline"> <input type="radio"
					name="${cfgName}.exprType" value="1"
					class="required" <c:if test="${strCfg.exprType==1}">checked</c:if>>
					<spring:message code="and_expression" />
				</label> <label class="radio-inline"> <input type="radio"
					name="${cfgName}.exprType" value="0"
					class="required"
					<c:if test="${strCfg.exprType==0 || strCfg.exprType==null}">checked</c:if>>
					<spring:message code="null_expression" />
				</label>
			</div>
			<div for="${cfgName}.exprType"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="match_method" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.matchMethod"
					class="selectpicker select2 form-control required">
					<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
						<option value="${matchMethodC.itemCode}" <c:if test="${strCfg.matchMethod==matchMethodC.itemCode || (strCfg.matchMethod==null && matchMethodC.itemCode==0)}">selected</c:if>><spring:message code="${matchMethodC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="${cfgName}.matchMethod"></div>
		</div>
	</div>
</div>
<div class="row">
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
	            <div class="col-md-6">
	            	<label class="radio-inline">
	            		<input type="radio" name="${cfgName}.isHex" value="1" class="required" 
	            			<c:if test="${(strCfg.isHexbin eq 1) || (region.configRegionValue eq 'NTC_P2P_HASH_BIN')}">checked disabled</c:if>
	            		><spring:message code="hex"/>
	            	</label>
	            	<label class="radio-inline">
	            		<input type="radio" name="${cfgName}.isHex" value="0" class="required" 
	            			<c:if test="${(strCfg.isHexbin ne 1) && (region.configRegionValue ne 'NTC_P2P_HASH_BIN')}">checked</c:if>
	            			<c:if test="${region.configRegionValue eq 'NTC_P2P_HASH_BIN'}">disabled</c:if>
	            		><spring:message code="not_hex"/>
	            	</label>
	            </div>
	            <div for="${cfgName}.isHex"></div>
	        </div>
	    </div>
	     <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_case_insenstive"/></label>
	            <div class="col-md-6">
	            	<label class="radio-inline">
	            		<input type="radio" name="${cfgName}.isCaseSenstive" value="1" class="required"
	            			<c:if test="${strCfg.isHexbin==1}">checked</c:if>
	            		><spring:message code="case_senstive"/>
	            	</label>
	            	<label class="radio-inline">
	            		<input type="radio" name="${cfgName}.isCaseSenstive" value="0" class="required"
	            			<c:if test="${strCfg.isHexbin ne 1}">checked</c:if>
	            		><spring:message code="case_insenstive"/>
	            	</label>
	            </div>
	            <div for="${cfgName}.isCaseInsenstive"></div>
	        </div>
	    </div>
	    
	</div>
	   
<div class="row hidden">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="whether_hexbinary" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.isHexbin"
					class="selectpicker select2 form-control required">
					<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
						<option value="${isHexbinC.itemCode}" <c:if test="${strCfg.isHexbin==isHexbinC.itemCode || (strCfg.isHexbin==null && isHexbinC.itemCode==0)}">selected</c:if>><spring:message code="${isHexbinC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="${cfgName}.isHexbin"></div>
		</div>
	</div>
	
</div>
</html>