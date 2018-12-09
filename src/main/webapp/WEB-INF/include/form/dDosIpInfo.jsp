<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<c:forEach items="${regionList}" var="region" varStatus="status">
									<c:if test="${region.regionType eq 1 }">
										<!--ip info-->
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
										</h4>
<div class="row ipInfo">

	<div class="row">
	    <div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
	            <div class="col-md-6">
	            	 <select name="ipType" class="selectpicker show-tick form-control required">
	                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipTypeC">
							<option value="${ipTypeC.itemCode}" 
									<c:if test="${_cfg.ipType==ipTypeC.itemCode 
										|| (_cfg.ipType==null && ipTypeC.itemCode==4)}">
										selected
									</c:if>
									>
							<spring:message code="${ipTypeC.itemValue}"/>
							</option>
               			</c:forEach>
	                 </select>
	            </div>
	            <div for="ipType"></div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
	            <div class="col-md-6">
           				<select name="protocol" class="selectpicker show-tick form-control required">
           					<c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
								<option value="${protocolC.itemCode}" <c:if test="${_cfg.protocol==protocolC.itemCode || (_cfg.protocol==null && protocolC.itemCode==0)}">selected</c:if>><spring:message code="${protocolC.itemValue}"/></option>
            				</c:forEach>
           				</select>
	                <input type="hidden" name="protocolId" value="0">
	            </div>
	            <div for="protocol"></div>
	        </div>
	    </div>
	</div>
	<div class="row">
	    <div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
	            <div class="col-md-6">
	            	 <select name="ipPattern" class="selectpicker show-tick form-control required">
	                  <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
							<option value="${ipPatternC.itemCode}" <c:if test="${_cfg.ipPattern==ipPatternC.itemCode || (_cfg.ipPattern==null && ipPatternC.itemCode==3)}">selected</c:if>><spring:message code="${ipPatternC.itemValue}"/></option>
              			</c:forEach>
	                 </select>
	            </div>
	            <div for="ipPattern"></div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
	            <div class="col-md-6">
	                 <input class="form-control required ipCheck" type="text" name="destIpAddress" value="${_cfg.destIpAddress}">
	            </div>
	            <div for="destIpAddress"></div>
	        </div>
	    </div>
	</div>
	<c:choose>
		<c:when test="${_cfg.cfgId==null}">
			<div class="row hidden port">
		</c:when>
		<c:otherwise>
			<div class="row port">
		</c:otherwise>
	</c:choose>
	    <div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
	            <div class="col-md-6">
	            	 <select name="portPattern" class="selectpicker show-tick form-control required">
	                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
							<option value="${portPatternC.itemCode}" <c:if test="${_cfg.portPattern==portPatternC.itemCode || (_cfg.portPattern==null && portPatternC.itemCode==1)}">selected</c:if>><spring:message code="${portPatternC.itemValue}"/></option>
              			</c:forEach>
	                 </select>
	            </div>
	            <div for="portPattern"></div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
	            <div class="col-md-6">
	                 <input class="form-control required portCheck" type="text" name="destPort" value="${_cfg.destPort}">
	            </div>
	            <div for="destPort"></div>
	        </div>
	    </div>
	</div>
	<c:choose>
		<c:when test="${_cfg.cfgId==null}">
			<div class="row hidden destPort">
		</c:when>
		<c:otherwise>
			<div class="row destPort">
		</c:otherwise>
	</c:choose>
		
	    <div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
	            <div class="col-md-6">
	            	<select name="direction" class="selectpicker show-tick form-control required" >
	                   <c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
							<option value="${directionC.itemCode}" <c:if test="${_cfg.direction==directionC.itemCode || (_cfg.direction==null && directionC.itemCode==0)}">selected</c:if>><spring:message code="${directionC.itemValue}"/></option>
              			</c:forEach>
	                 </select>
	            </div>
	            <div for="direction"></div>
	        </div>
	    </div>
	</div>
	<c:if test="${_cfg.cfgId==null}">
		<div class="row">
		<button type="button" class="btn btn-red-hollow center-block" onClick="more(this);" data-click-times="0"><spring:message code="show_more"/></button>
		</div>
	</c:if>
	<div class="row destPort">
	    <div class="col-md-6">
	        <div class="form-group">
	            <div class="col-md-6">
	                 <input type="hidden" name="srcIpAddress" value="${_cfg.srcIpAddress}">
	            </div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group ">
	            <div class="col-md-6">
	                 <input type="hidden" name="srcPort" value="${_cfg.srcPort}">
	            </div>
	        </div>
	    </div>
	</div>
</div>
</c:if>
</c:forEach>