<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>

</head>
<%-- <c:forEach items="${regionList}" var="regionDistrict">
	<c:if
		test="${cfg.functionId eq regionDistrict.functionId 
		and regionDistrict.regionType eq 3 
		and regionValue eq regionDistrict.configRegionValue}"> --%>
		<!-- regionDistrict.regionType==3表示增强字符串类配置 -->
		<input type="hidden" name="${cfgName}.cfgType" value="${region.configRegionValue }">
		<input type="hidden" name="${cfgName}.cfgRegionCode" serviceType="${region.configServiceType }" value="${region.configRegionCode }">
		<input type="hidden" name="${cfgName}.configMultiKeywords" value="${region.configMultiKeywords }">
		<input type="hidden" name="${cfgName}.configServiceType" value="${region.configServiceType }">
		<input type="hidden" name="${cfgName}.configHex" value="${region.configHex }">
<%-- 	</c:if>
</c:forEach> --%>

<div class="row">
	<div class="pull-right">
		<span class="glyphicon glyphicon-remove pull-right" title="remove"
			onClick="delContent('${tabName}${status.index}','${tabName}Add');" />
	</div>
</div>
<br>
<c:if test="${!empty region.configDistrict }">
	<div class="row">
		<div class="col-md-6">
			<div class="form-group">
				<label class="control-label col-md-3"><font color="red">*</font>
					<spring:message code="district" /></label>
				<div class="col-md-6">
					<select name="${cfgName}.district"
						class="selectpicker show-tick form-control required district" onchange="changeDistrict($(this))">
							<c:forEach items="${fn:split(region.configDistrict,',')}"
								var="_district">
								<option value="${_district }"
									<c:if test="${cfg.district==_district}">selected</c:if>>${_district }</option>
							</c:forEach>
					</select>
					<input type="hidden" name="${cfgName}.districtShowName"  maxlength="64"  placeholder="<spring:message code='please_input'/> <spring:message code='custom_region'/>" class="otherValue form-control invisibleChar" value="${cfg.district}"/>
				</div>
				<div for="${cfgName}.districtShowName"></div>
				<div for="${cfgName}.district"></div>
			</div>
		</div>
	</div>
</c:if>
<div class="row">
	<c:if test="${isP2pHashCfg}">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><spring:message code="p2p_hash_type"/></label>
			<div class="col-md-6">
				<select name="userRegion1" class="selectpicker show-tick form-control">
				<option value=""><spring:message code="select"/></option>
				<c:forEach items="${fns:getDictList('P2P_HASH_TYPE')}" var="hashType">
						<option value="${hashType.itemCode}" <c:if test="${_cfg.userRegion1==hashType.itemCode}">selected</c:if> ><spring:message code="${hashType.itemValue}"/></option>
				</c:forEach>
				</select>
			</div>
			<div for="userRegion1"></div>
		</div>
	</div>
	</c:if>
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
			<c:choose>
				<c:when test="${fn:containsIgnoreCase(region.configServiceType,'domain') }">
					<spring:message code="domain" /> 
				</c:when>
				<c:when test="${fn:containsIgnoreCase(region.configServiceType,'url') }">
					<spring:message code="URL" />
				</c:when>
				<c:otherwise>
					<spring:message code="keywords" />
				</c:otherwise>
			</c:choose>
			</label>
			
			<!-- 此配置的关键词可以输入多个关键词 -->
			<c:if test="${region.configMultiKeywords eq 1}">
				<div class="col-md-6">
					<input class="form-control required tags" type="text" id="tags_${tabName}"
						name="${cfgName}.cfgKeywords"
						value="${cfg.cfgKeywords}">
				</div>
			</c:if>
			<!-- 此配置的关键词不允许输入多个关键词 -->
			<c:if test="${(region.configMultiKeywords eq 0) or (empty region.configMultiKeywords)}">
				<div class="col-md-6">
					
					<input class="form-control required invisibleChar
						<c:if test="${fn:containsIgnoreCase(region.configServiceType,'domain') }"> domainCheck </c:if>
						" 
							type="text"
						name="${cfgName}.cfgKeywords"
						value="${cfg.cfgKeywords}">
				</div>
			</c:if>
			
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
					<c:if test="${!empty region.configExprType}">
						<c:forEach var="exprType" items="${fn:split(region.configExprType,',')}" varStatus="stat" >
							<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC" >
								<c:if test="${exprTypeC.itemCode eq exprType}">
									<label class="radio-inline"> 
										<input type="radio"
											name="${cfgName}.exprType" value="${exprType }"
											class="required"
											<c:if test="${stat.index == 0 }"> checked </c:if>
										> 
										<spring:message	code="${exprTypeC.itemValue }" />
									</label>
								</c:if>
							</c:forEach>
						</c:forEach>
					</c:if>
					<c:if test="${empty region.configExprType}">
						<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC">
								<c:if test="${exprTypeC.itemCode eq 0}">
									<label class="radio-inline"> 
										<input type="radio"
											name="${cfgName}.exprType" value="${exprTypeC.itemCode }"
											class="required" checked > 
										<spring:message	code="${exprTypeC.itemValue }" />
									</label>
								</c:if>
							</c:forEach>
					</c:if>
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
						<c:if test="${!empty region.configMatchMethod}">
							<c:forEach var="matchMethod" items="${fn:split(region.configMatchMethod,',')}">
								<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
									<c:if test="${matchMethodC.itemCode eq matchMethod}">
										<option value="${matchMethodC.itemCode}" 
										<c:if test="${cfg.matchMethod eq matchMethod || (empty cfg.matchMethod && matchMethod eq 0)}">
										selected
										</c:if>
										>
										<spring:message code="${matchMethodC.itemValue}"/>
										</option>
									</c:if>
								
								</c:forEach> 
							</c:forEach>
						</c:if>
						<c:if test="${empty region.configMatchMethod}">
							<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
									<c:if test="${matchMethodC.itemCode eq 0}">
										<option value="${matchMethodC.itemCode}" 
										selected
										>
										<spring:message code="${matchMethodC.itemValue}"/>
										</option>
									</c:if>
								
								</c:forEach> 
						</c:if>
					</select>
				</div>
				<div for="${cfgName}.matchMethod"></div>
			</div>
		</div>
</div>
<div class="row <c:if test="${fn:length(fn:split(region.configHex,',')) ==1}">hidden</c:if>">
		<c:if test="${!empty region.configHex}">
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
		            <div class="col-md-6">
		            	<!-- isP2pHashCfg: P2PHash配置只能是十六进制字符 -->
		            	<label class="radio-inline">
		            		<input type="radio" name="${cfgName}.isHex" value="1" class="required"
		            		><spring:message code="hex"/>
		            	</label>
		            	<label class="radio-inline">
		            		<input type="radio" name="${cfgName}.isHex" value="0" class="required"
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
		            		><spring:message code="case_senstive"/>
		            	</label>
		            	<label class="radio-inline">
		            		<input type="radio" name="${cfgName}.isCaseSenstive" value="0" class="required"
		            		><spring:message code="case_insenstive"/>
		            	</label>
		            </div>
		            <div for="${cfgName}.isCaseInsenstive"></div>
		        </div>
		    </div>
  	</c:if>
  	<c:if test="${empty region.configHex}"> 
		<div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
		            <div class="col-md-6">
		            	<label class="radio-inline">
		            		<input type="radio" name="${cfgName}.isHex" value="0" class="required" checked
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
		            		<input type="radio" name="${cfgName}.isCaseSenstive" value="0" class="required"
		            			checked
		            		><spring:message code="case_insenstive"/>
		            	</label>
		            </div>
		            <div for="${cfgName}.isCaseInsenstive"></div>
		        </div>
		    </div>
	</c:if>
</div>
<div class="row hidden">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="whether_hexbinary" /></label>
			<div class="col-md-6">
				<select name="${cfgName}.isHexbin"
					class="selectpicker select2 form-control required">
					<c:if test="${!empty region.configHex}">
						<c:forEach var="isHexbin" items="${fn:split(region.configHex,',')}">
								<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
									<c:if test="${isHexbinC.itemCode eq isHexbin}">
										<option value="${isHexbinC.itemCode}" 
										<c:if test="${cfg.isHexbin eq isHexbin || (empty isHexbin && isHexbin eq 0)}">
										selected
										</c:if>
										>
										<spring:message code="${isHexbinC.itemValue}"/>
										</option>
									</c:if>
								
								</c:forEach> 
						</c:forEach>
					</c:if>
					<c:if test="${empty region.configHex}"> 
					
						<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
							<c:if test="${isHexbinC.itemCode eq 0}">
								<option value="${isHexbinC.itemCode}"  selected >
								<spring:message code="${isHexbinC.itemValue}"/>
								</option>
							</c:if>
						</c:forEach> 
					
					</c:if>
				</select>
			</div>
			<div for="${cfgName}.isHexbin"></div>
		</div>
	</div>
</div>
</html>
<script type="text/javascript">
function changeDistrict(obj){
	if("others"==obj.val()){
		obj.parent().siblings(".otherValue").prop("type","text");
	}else{
		obj.parent().siblings(".otherValue").prop("type","hidden");
	}
}
</script>