<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script type="text/javascript">
$(function(){
	setSelectIndex();
	$("#addAreaIsp").on("click",function(){
		setSelectIndex();
	});
});
var setSelectIndex = function(){
	$("select[areaIsp='area']").each(function(index,item){
		var name = this.name;
		var index = name.substring(name.indexOf("[")+1,name.indexOf("]"));
		$(this).attr("index",index);
	});
}
</script>
<p class="border-top border-top-blue-sharp"></p>
<h3 class="form-section"><spring:message code="effect_range"/></h3> 
<div class="row areaInfo">
	<div class="col-md-6">
       <div class="form-group">
           <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_area_block"/></label>
           <div class="col-md-6">
           <label class="radio-inline">
           		<input type="radio" name="isAreaEffective" value="0" class="required"
           			<c:if test="${empty _cfg.isAreaEffective or _cfg.isAreaEffective==0}">checked</c:if>
           		><spring:message code="all"/>
           	</label>
           	<label class="radio-inline">
           		<input type="radio" name="isAreaEffective" value="1" class="required"
           			<c:if test="${_cfg.isAreaEffective==1}">checked</c:if>
           		><spring:message code="selective"/>
           	</label>           	
           </div>
           <div for="isAreaEffective"></div>
       </div>
   </div>
   <div class="hidden areaType col-md-6">
       <div class="form-group">
           <label class="control-label col-md-3"><font color="red">*</font><spring:message code="area_control_type"/></label>
           <div class="col-md-6">
           	<label class="radio-inline">
           		<input type="radio" name="areaType" value="1" class="required"><spring:message code="area"/> 
           	</label>
           	<%-- <label class="radio-inline">
           		<input type="radio" name="areaType" value="0" class="required"><spring:message code="area"/> ip
           	</label> --%>
           </div>
           <div for="areaType"></div>
           <input class="form-control" type="hidden" name="areaEffectiveIds" value="${_cfg.areaEffectiveIds}">
        </div>
    </div>
</div>
<div class="hidden" id="areaIsp">
<h4 id="addAreaIsp" class="form-section"><spring:message code="area"/> <%-- <spring:message code="isp"/> --%><small><span class="glyphicon glyphicon-plus" title="add"></span></small></h4>
	<div class="container-fluid hidden row">
		<div class="row">
			<div class="pull-right">
				<span class="glyphicon glyphicon-remove pull-right" title="remove" onClick="delAreaIsp(this);"/>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
		        <div class="form-group">
		        	<label class="control-label col-md-3"><font color="red">*</font><spring:message code="area"/></label>
					<div class="col-md-6">
						<select name="areaCode" areaIsp="area" class="form-control required areaUnique" data-live-search="true" data-live-search-placeholder="search" >
							<option value="" ><spring:message code="select"/></option>
							<c:forEach items="${areas}" var="area">
								<option value="${area.itemCode}">${area.itemValue}</option>
							</c:forEach>
						</select>
					</div>
					<div for="areaCode"></div>
				</div>
			</div>
			<div class="col-md-6 ">
		        <div class="form-group">
		        	<label class="control-label col-md-3"><spring:message code="isp"/></label>
					<div class="col-md-6">
						<select name="ispCode" class="form-control" data-live-search="true" data-live-search-placeholder="search" >
							<option value="" ><spring:message code="select"/></option>
							<c:forEach items="${isps}" var="isp">
								<option value="${isp.itemCode}">${isp.itemValue}</option>
							</c:forEach>
						</select>
					</div>
					<div for="ispCode"></div>
				</div>
			</div>
		</div>
	</div>
	<c:forEach items="${_areaIsps}" var="areaIsp" varStatus="status">
		<div class="container-fluid">
			<div class="row">
				<div class="pull-right">
				<span class="glyphicon glyphicon-remove <c:if test="${status.index==0 }">hidden</c:if>" title="remove" onClick="delAreaIsp(this);"/>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
			        <div class="form-group">
			        	<label class="control-label col-md-3"><font color="red">*</font><spring:message code="area"/></label>
						<div class="col-md-6">
							<select name="areaIsp[${status.index}].area" areaIsp="area" class="selectpicker form-control required areaUnique" data-live-search="true" data-live-search-placeholder="search" >
								<option value="" ><spring:message code="select"/></option>
								<c:forEach items="${areas}" var="area">
									<option value="${area.itemCode}"
									<c:if test="${area.itemCode==areaIsp.area}">selected</c:if>
									>${area.itemValue}</option>
								</c:forEach>
							</select>
						</div>
						<div for="areaIsp[${status.index}].area"></div>
					</div>
				</div>
				<div class="col-md-6 ">
			        <div class="form-group">
			        	<label class="control-label col-md-3"><spring:message code="isp"/></label>
						<div class="col-md-6">
							<select name="areaIsp[${status.index}].isp" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search" >
								<option value="" ><spring:message code="select"/></option>
								<c:forEach items="${isps}" var="isp">
									<option value="${isp.itemCode}"
									<c:if test="${isp.itemCode==areaIsp.isp}">selected</c:if>
									>${isp.itemValue}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
	<!-- <div for="ispArea" ><label class="error">Area already exists.</label></div> -->
	<%--<span id="rangeError" style="display:none"><spring:message code="area_existed"/></span> --%>
</div>
<%-- <div class="hidden" id="areaIp">
<h4 class="form-section"><spring:message code="area"/> ip<small><span class="glyphicon glyphicon-plus" title="add"></span></small></h4>
	<div class="container-fluid hidden row">
		<div class="row">
			<input type="hidden" id="_areaCfg.isAreaEffective" value="${_areaCfg.isAreaEffective}">
			<input type="hidden" id="_areaCfg.cfgRegionCode" value="${_areaCfg.cfgRegionCode}">
			<input type="hidden" id="_areaCfg.cfgType" value="${_areaCfg.cfgType}">
			<input type="hidden" id="_areaCfg.functionId" value="0">
			<input id="_areaCfg.cfgId" type="hidden" value="${_areaCfg.cfgId}">
			<div class="pull-right">
			<span class="glyphicon glyphicon-remove" title="remove" onClick="delAreaIp(this);"/>
			</div>
		</div>
		<div class="row">
		    
		    <div class="col-md-6">
		    	<div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
		            <div class="col-md-6">
		            
		            	 <select id="_areaCfg.ipType" class="show-tick form-control required">
		                   <c:forEach items="${fns:getDictList('AREA_IPTYPE')}" var="ipTypeC">
								<option value="${ipTypeC.itemCode}" <c:if test="${_areaCfg.ipType==ipTypeC.itemCode || (_areaCfg.ipType==null && ipTypeC.itemCode==4)}">selected</c:if>><spring:message code="${ipTypeC.itemValue}"/></option>
	               			</c:forEach>
		                 </select>
		            </div>
		            <div for="_areaCfg.ipType"></div>
		        </div>
		    </div>
		</div>
		<div class="row">
		    <div class="col-md-6">
		    	<div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
		            <div class="col-md-6">
		            	 <select id="_areaCfg.ipPattern" class="show-tick form-control required">
		                   <c:forEach items="${fns:getDictList('IP_PATTERN')}" var="ipPatternC">
								<option value="${ipPatternC.itemCode}" <c:if test="${_areaCfg.ipPattern==ipPatternC.itemCode || (_areaCfg.ipPattern==null && ipPatternC.itemCode==3)}">selected</c:if>><spring:message code="${ipPatternC.itemValue}"/></option>
	              			</c:forEach>
		                 </select>
		            </div>
		            <div for="_areaCfg.ipPattern"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
		            <div class="col-md-6">
		                 <input id="_areaCfg.srcIpAddress" class="form-control required ipCheck areaIp" type="text" value="${_areaCfg.srcIpAddress}">
		            </div>
		            <div for="_areaCfg.srcIpAddress"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden">
			
		    <div class="col-md-6">
		    	<div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="port_pattern"/></label>
		            <div class="col-md-6">
		            	 <select id="_areaCfg.portPattern" class="show-tick form-control required">
		                   <c:forEach items="${fns:getDictList('PORT_PATTERN')}" var="portPatternC">
								<option value="${portPatternC.itemCode}" <c:if test="${_areaCfg.portPattern==portPatternC.itemCode || (_areaCfg.portPattern==null && portPatternC.itemCode==1)}">selected</c:if>><spring:message code="${portPatternC.itemValue}"/></option>
	              			</c:forEach>
		                 </select>
		            </div>
		            <div for="_areaCfg.portPattern"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
		            <div class="col-md-6">
		                 <input id="_areaCfg.srcPort" class="form-control required portCheck" type="text" value="${_areaCfg.srcPort}">
		            </div>
		            <div for="_areaCfg.srcPort"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden">
			<div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
		            <div class="col-md-6">
		                 <input id="_areaCfg.destIpAddress" class="form-control required ipCheck" type="text" value="${_areaCfg.destIpAddress}">
		            </div>
		            <div for="_areaCfg.destIpAddress"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
		            <div class="col-md-6">
		                 <input id="_areaCfg.destPort" class="form-control required portCheck" type="text" value="${_areaCfg.destPort}">
		            </div>
		            <div for="_areaCfg.destPort"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden">
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
		            <div class="col-md-6">
		            	<select id="_areaCfg.protocol" class="show-tick form-control required">
		                   <c:forEach items="${fns:getDictList('PROTOCOL')}" var="protocolC">
								<option value="${protocolC.itemCode}" <c:if test="${_areaCfg.protocol==protocolC.itemCode || (_areaCfg.protocol==null && protocolC.itemCode==0)}">selected</c:if>><spring:message code="${protocolC.itemValue}"/></option>
	              			</c:forEach>
		                </select>
		                <input type="hidden" id="_areaCfg.protocolId" value="${_areaCfg.protocolId}">
		            </div>
		            <div for="_areaCfg.protocol"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
		            <div class="col-md-6">
		            	<select id="_areaCfg.direction" class="show-tick form-control required" >
		                   <c:forEach items="${fns:getDictList('DIRECTION')}" var="directionC">
							<option value="${directionC.itemCode}" <c:if test="${_areaCfg.direction==directionC.itemCode || (_areaCfg.direction==null && directionC.itemCode==0)}">selected</c:if>><spring:message code="${directionC.itemValue}"/></option>
              			</c:forEach>
		                 </select>
		            </div>
		            <div for="_areaCfg.direction"></div>
		        </div>
		    </div>
		</div>
	</div>
	<input type="hidden" name="areaCfgIds" value="${areaCfgIds}">
	<c:forEach items="${areaCfgs}" var ="areaCfg" varStatus="status">
	<div class="container-fluid row">
		<div class="row">
			<input type="hidden" name="areaCfg[${status.index}].isAreaEffective" value="${areaCfg.isAreaEffective}">
			<input name="areaCfg[${status.index}].cfgId" type="hidden" value="${areaCfg.cfgId}">
			<div class="pull-right">
			<span class="glyphicon glyphicon-remove <c:if test="${status.index==0 }">hidden</c:if>" title="remove" onClick="delAreaIp(this);"/>
			</div>
		</div>
		<div class="row">
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
		            <div class="col-md-6">
		                 <select name="areaCfg[${status.index}].ipType" class="selectpicker show-tick form-control required">
		                   <c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipType">
							<option value="${ipType.itemCode}" <c:if test="${areaCfg.ipType==ipType.itemCode || (areaCfg.ipType==null && ipType.itemCode==4)}">selected</c:if> >${ipType.itemValue}</option>
                			</c:forEach>
		                 </select>
		            </div>
		            <div for="areaCfg[${status.index}].ipType"></div>
		        </div>
		    </div>
		</div>
		<div class="row">
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_pattern"/></label>
		            <div class="col-md-6">
		                 <select name="areaCfg[${status.index}].ipPattern" class="selectpicker show-tick form-control required">
		                   <option value="1" <c:if test="${areaCfg.ipPattern==1}">selected</c:if> ><spring:message code="ip_subnet"/></option>
		                   <option value="2" <c:if test="${areaCfg.ipPattern==2}">selected</c:if>><spring:message code="ip_range"/></option>
		                   <option value="3" <c:if test="${areaCfg.ipPattern==3 || areaCfg.ipPattern==null}">selected</c:if>>IP</option>
		                 </select>
		            </div>
		            <div for="areaCfg[${status.index}].ipPattern"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
		            <div class="col-md-6">
		                <input name="areaCfg[${status.index}].srcIpAddress" class="form-control required ipCheck areaIp" type="text" value="${areaCfg.srcIpAddress}">
		            </div>
		            <div for="areaCfg[${status.index}].srcIpAddress"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden" >
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port_mask"/></label>
		            <div class="col-md-6">
		                 <select name="areaCfg[${status.index}].portPattern" class="selectpicker show-tick form-control required">
		                   <option value="1" <c:if test="${areaCfg.portPattern==1}">selected</c:if> ><spring:message code="port"/></option>
		                   <option value="2" <c:if test="${areaCfg.portPattern==2}">selected</c:if>><spring:message code="port_mask"/></option>
		                 </select>
		            </div>
		            <div for="areaCfg[${status.index}].portPattern"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
		            <div class="col-md-6">
		                <input name="areaCfg[${status.index}].srcPort" class="form-control required portCheck" type="text" value="${areaCfg.srcPort}">
		            </div>
		            <div for="areaCfg[${status.index}].srcPort"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden">
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
		            <div class="col-md-6">
		                <input name="areaCfg[${status.index}].destIpAddress" class="form-control required ipCheck" type="text" value="${areaCfg.destIpAddress}">
		            </div>
		            <div for="areaCfg[${status.index}].destIpAddress"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
		            <div class="col-md-6">
		                <input name="areaCfg[${status.index}].destPort" class="form-control required portCheck" type="text" value="${areaCfg.destPort}">
		            </div>
		            <div for="areaCfg[${status.index}].destPort"></div>
		        </div>
		    </div>
		</div>
		<div class="row hidden" >
		    <div class="col-md-6">
		        <div class="form-group ">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
		            <div class="col-md-6">
		            	<select name="areaCfg[${status.index}].protocol" class="selectpicker show-tick form-control required">
		                   <option value=""><spring:message code="select"/></option>
		                   <option value="6" <c:if test="${areaCfg.protocol==6}">selected</c:if>>TCP</option>
		                   <option value="17" <c:if test="${areaCfg.protocol==17}">selected</c:if> >UDP</option>
		                   <option value="0" <c:if test="${areaCfg.protocol==0}">selected</c:if> ><spring:message code="arbitrary"/></option>
		                </select>
		                <input type="hidden" name="areaCfg[${status.index}].protocolId" value="${areaCfg.protocolId}">
		            </div>
		            <div for="areaCfg[${status.index}].protocol"></div>
		        </div>
		    </div>
		    <div class="col-md-6">
		        <div class="form-group">
		            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
		            <div class="col-md-6">
		            	<select name="areaCfg[${status.index}].direction" class="selectpicker show-tick form-control required" >
		                   <option value="0" <c:if test="${areaCfg.direction==0 || areaCfg.direction==null}">selected</c:if>><spring:message code="twoway"/></option>
		                   <option value="1" <c:if test="${areaCfg.direction==1 }">selected</c:if>><spring:message code="ingress"/></option>
		                   <option value="2" <c:if test="${areaCfg.direction==2 }">selected</c:if>><spring:message code="egress"/></option>
		                 </select>
		            </div>
		            <div for="areaCfg[${status.index}].direction"></div>
		        </div>
		    </div>
		</div>
	</div>
	</c:forEach>
</div> --%>
