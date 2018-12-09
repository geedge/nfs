<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<%-- <h3 class="form-section"><spring:message code="block_config"/></h3> --%>
<c:forEach items="${regionList}" var="region" varStatus="status">
	<c:if test="${region.regionType eq 1 }">
		<h4 class="form-section">
			<spring:message code="${region.configRegionValue}" />
		</h4>
	</c:if>
</c:forEach>
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
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
	            <div class="col-md-6">
	                 <input class="form-control required ipCheck" type="text" name="srcIpAddress" value="${_cfg.srcIpAddress}">
	            </div>
	            <div for="srcIpAddress"></div>
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
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
	            <div class="col-md-6">
	                 <input class="form-control required portCheck" type="text" name="srcPort" value="${_cfg.srcPort}">
	            </div>
	            <div for="srcPort"></div>
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
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
	            <div class="col-md-6">
	                   <input class="form-control required ipCheck" type="text" name="destIpAddress" value="${_cfg.destIpAddress}">
	            </div>
	            <div for="destIpAddress"></div>
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
			<div class="row hidden protocol">
		</c:when>
		<c:otherwise>
			<div class="row protocol">
		</c:otherwise>
	</c:choose>
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
</div>

<%-- <c:choose>
	<c:when test="${action==5}">
		<input type="hidden" name="protocol" value="0">
		<input type="hidden" name="direction" value="0">
		<input type="hidden" name="isAreaEffective" value="0">
	</c:when>
	<c:when test="${action==8}">
		<div class="row">
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
		            <div class="col-md-6">
		            	<select name="direction" class="selectpicker show-tick form-control required">
		                   <option value=""><spring:message code="select"/></option>
		                   <option value="0" <c:if test="${_cfg.direction==0}">selected</c:if>><spring:message code="twoway"/></option>
		                   <option value="1" <c:if test="${_cfg.direction==1}">selected</c:if>><spring:message code="oneway"/></option>
		                 </select>
		            </div>
		            <div for="direction"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
		            <div class="col-md-6">
		            	<select name="protocol" class="selectpicker show-tick form-control required">
		                   <option value=""><spring:message code="select"/></option>
		                   <option value="6" <c:if test="${_cfg.protocol==6}">selected</c:if>>TCP</option>
		                   <option value="17" <c:if test="${_cfg.protocol==17}">selected</c:if>>UDP</option>
		                   <option value="0" <c:if test="${_cfg.protocol==0}">selected</c:if>><spring:message code="arbitrary"/></option>
		                </select>
		                <input class="form-control" type="text" name="protocol" value="${_cfg.protocol}">
		            </div>
		            <div for="protocol"></div>
		        </div>
		    </div>
		</div>
		<input type="hidden" name="isAreaEffective" value="0">
		<input type="hidden" name="areaEffectiveIds" value="">
	</c:when>
	<c:otherwise>
		<div class="row">
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
		            <div class="col-md-6">
		            	<select name="protocol" class="selectpicker show-tick form-control required">
		                   <option value=""><spring:message code="select"/></option>
		                   <option value="6" <c:if test="${_cfg.protocol==6}">selected</c:if>>TCP</option>
		                   <option value="17" <c:if test="${_cfg.protocol==17}">selected</c:if>>UDP</option>
		                   <option value="0" <c:if test="${_cfg.protocol==0}">selected</c:if>><spring:message code="arbitrary"/></option>
		                </select>
		                <input class="form-control" type="text" name="protocol" value="${_cfg.protocol}">
		            </div>
		            <div for="protocol"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
		            <div class="col-md-6">
		            	<select name="direction" class="selectpicker show-tick form-control required" >
		                   <option value=""><spring:message code="select"/></option>
		                   <option value="0" <c:if test="${_cfg.direction==0}">selected</c:if>><spring:message code="twoway"/></option>
		                   <option value="1" <c:if test="${_cfg.direction==1}">selected</c:if>><spring:message code="oneway"/></option>
		                 </select>
		            </div>
		            <div for="direction"></div>
		        </div>
		    </div>
		</div>
		<div class="row">
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_area_block"/></label>
		            <div class="col-md-6">
		            	<label class="radio-inline">
		            		<input type="radio" name="isAreaEffective" value="1" class="required"
		            			<c:if test="${_cfg.isAreaEffective==1}">checked</c:if>
		            			<c:if test="${action==2}">disabled</c:if>
		            		><spring:message code="yes"/>
		            	</label>
		            	<label class="radio-inline">
		            		<input type="radio" name="isAreaEffective" value="0" class="required"
		            			<c:if test="${_cfg.isAreaEffective==0}">checked</c:if>
		            			<c:if test="${action==2}">disabled</c:if>
		            		><spring:message code="no"/>
		            	</label>
		            	<c:if test="${action==2}">
		            		<input class="form-control" type="hidden" name="isAreaEffective" value="${_cfg.isAreaEffective}">
			            </c:if>
		            </div>
		            <div for="isAreaEffective"></div>
		        </div>
		    </div>
		    <div class="hidden areaType col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="area_control_type"/></label>
		            <div class="col-md-6">
		            	<label class="radio-inline">
		            		<input type="radio" name="areaType" value="1" class="required"><spring:message code="area"/> <spring:message code="isp"/>
		            	</label>
		            	<label class="radio-inline">
		            		<input type="radio" name="areaType" value="0" class="required"><spring:message code="area"/> ip
		            	</label>
		            </div>
		            <div for="areaType"></div>
		            <input class="form-control" type="hidden" name="areaEffectiveIds" value="${_cfg.areaEffectiveIds}">
		        </div>
		    </div>
		</div>
	</c:otherwise>
</c:choose> --%>
<!--/row-->