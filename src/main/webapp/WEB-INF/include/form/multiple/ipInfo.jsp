<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<h3 class="form-section">
<spring:message code="${tableInfo.tableDesc}"/>
<small><span style="margin-left:10px"><input type="checkbox" id="ipSelect" value="ipCfg"
<c:if test="${_cfg.ipCfg[tableInfo.tableName]!= null and _cfg.ipCfg[tableInfo.tableName].cfgId != null}">checked</c:if>
/> <spring:message code="optional"/></span></small></h3>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
            <div class="col-md-6">
            	<input  class="form-control" type="text" name="ipCfg[${tableInfo.tableName}].cfgDesc" value="${_cfg.ipCfg[tableInfo.tableName].cfgDesc}">
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="ip_type"/></label>
            <div class="col-md-6">
            	 <select name="ipCfg[${tableInfo.tableName}].ipType" class="ipType selectpicker show-tick form-control required">
                   <option value="4" <c:if test="${_cfg.ipCfg[tableInfo.tableName].ipType==4}">selected</c:if> >V4</option>
                   <option value="6" <c:if test="${_cfg.ipCfg[tableInfo.tableName].ipType==6}">selected</c:if>>V6</option>
                </select>
            </div>
            <div for="ipCfg[${tableInfo.tableName}].ipType"></div>
        </div>
    </div>
</div>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_ip"/></label>
            <div class="col-md-6">
            	<input class="form-control required ipCheck" type="text" name="ipCfg[${tableInfo.tableName}].srcIp" value="${_cfg.ipCfg[tableInfo.tableName].srcIp}">
            </div>
		    <div for="ipCfg[${tableInfo.tableName}].srcIp"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_address_mask"/></label>
            <div class="col-md-6">
            <input class="form-control required" type="text" name="ipCfg[${tableInfo.tableName}].srcIpMask" value="${_cfg.ipCfg[tableInfo.tableName].srcIpMask}">
            </div>
            <div for="ipCfg[${tableInfo.tableName}].srcIpMask"></div>
            <div for="srcIpMask"></div>
        </div>
    </div>
</div>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port"/></label>
            <div class="col-md-6">
            <input class="form-control required" range="[0,65535]" type="text" name="ipCfg[${tableInfo.tableName}].srcPort" value="${_cfg.ipCfg[tableInfo.tableName].srcPort}">
            </div>
            <div for="ipCfg[${tableInfo.tableName}].srcPort"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="client_port_mask"/></label>
            <div class="col-md-6">
            <input class="form-control required" type="text" name="ipCfg[${tableInfo.tableName}].srcPortMask" range="[0,65535]" value="${_cfg.ipCfg[tableInfo.tableName].srcPortMask}">
            </div>
            <div for="ipCfg[${tableInfo.tableName}].srcPortMask"></div>
        </div>
    </div>
</div>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_ip"/></label>
            <div class="col-md-6">
            <input class="form-control required ipCheck" type="text" name="ipCfg[${tableInfo.tableName}].dstIp" value="${_cfg.ipCfg[tableInfo.tableName].dstIp}">
            </div>
		    <div for="ipCfg[${tableInfo.tableName}].dstIp"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_address_mask"/></label>
            <div class="col-md-6">
            <input class="form-control required" type="text" name="ipCfg[${tableInfo.tableName}].dstIpMask" value="${_cfg.ipCfg[tableInfo.tableName].dstIpMask}">
            </div>
		   <div for="ipCfg[${tableInfo.tableName}].dstIpMask"></div>
        </div>
    </div>
</div>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port"/></label>
            <div class="col-md-6">
            <input class="form-control required" type="text" name="ipCfg[${tableInfo.tableName}].dstPort" value="${_cfg.ipCfg[tableInfo.tableName].dstPort}" range="[0,65535]">
            </div>
		    <div for="ipCfg[${tableInfo.tableName}].dstPort"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="server_port_mask"/></label>
            <div class="col-md-6">
            <input class="form-control required" type="text" name="ipCfg[${tableInfo.tableName}].dstPortMask" range="[0,65535]" value="${_cfg.ipCfg[tableInfo.tableName].dstPortMask}">
            </div>
            <div for="ipCfg[${tableInfo.tableName}].dstPortMask"></div>
        </div>
    </div>
</div>
<div class="row ipCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="direction"/></label>
            <div class="col-md-6">
       		<select name="ipCfg[${tableInfo.tableName}].direction" class="selectpicker show-tick form-control required" title=<spring:message code="select"/>>
               <option value="" ><spring:message code="select"/></option>
               <option value="0" <c:if test="${_cfg.ipCfg[tableInfo.tableName].direction==0}">selected</c:if>><spring:message code="twoway"/></option>
               <option value="1" <c:if test="${_cfg.ipCfg[tableInfo.tableName].direction==1}">selected</c:if>><spring:message code="oneway"/></option>
             </select>
                <%-- <input class="form-control" type="text" name="direction" value="${_cfg.ipCfg.direction}"> --%>
            </div>
       		<div for="ipCfg[${tableInfo.tableName}].direction"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="protocol"/></label>
            <div class="col-md-6">
       		<select name="ipCfg[${tableInfo.tableName}].protocol" class="selectpicker show-tick form-control" required title=<spring:message code="select"/>>
               <option value=""><spring:message code="select"/></option>
               <option value="6" <c:if test="${_cfg.ipCfg[tableInfo.tableName].protocol==6}">selected</c:if>>TCP</option>
               <option value="17" <c:if test="${_cfg.ipCfg[tableInfo.tableName].protocol==17}">selected</c:if>>UDP</option>
               <option value="0" <c:if test="${_cfg.ipCfg[tableInfo.tableName].protocol==0}">selected</c:if>><spring:message code="arbitrary"/></option>
            </select>
            </div>
            <div for="ipCfg[${tableInfo.tableName}].protocol"></div>
        </div>
    </div>
   	<input type="hidden" name="ipCfg[${tableInfo.tableName}].protocolId" value="${_cfg.ipCfg[tableInfo.tableName].protocolId}">
	<input name="ipCfg[${tableInfo.tableName}].cfgId" type="hidden" value="${_cfg.ipCfg[tableInfo.tableName].cfgId}"/>
</div>
