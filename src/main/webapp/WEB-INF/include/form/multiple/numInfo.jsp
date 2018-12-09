<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<h3 class="form-section">
<spring:message code="${tableInfo.tableDesc}"/>
<small><span class="glyphicon glyphicon-chevron-down" data-toggle="collapse" data-target=".numCfg" title="add"></span></small></h3>
<div class="row numCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
            <div class="col-md-6">
            	<input name="numCfg[${tableInfo.tableName}].cfgDesc" type="text" class="form-control" value="${_cfg.numCfg[tableInfo.tableName].cfgDesc}"/>
            </div>
            <div for="cfgDesc"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_area"/></label>
            <div class="col-md-6">
           		<input type="text" name="numCfg[${tableInfo.tableName}].lowBounadry" value="${_cfg.numCfg[tableInfo.tableName].lowBounadry}">
            </div>
            <div for="district"></div>
        </div>
    </div>
</div>
<div class="row numCfg collapse">
	<div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
            <div class="col-md-6">
           		<input type="text" name="numCfg[${tableInfo.tableName}].upBounadry" value="${_cfg.numCfg[tableInfo.tableName].upBounadry}">
            </div>
            <div for="keywords"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
            <div class="col-md-6">
       			<label class="radio-inline">
            		<input type="radio" name="numCfg[${tableInfo.tableName}].exprType" value="1"
            			<c:if test="${_cfg.numCfg[tableInfo.tableName].exprType==1}">checked</c:if>
            		><spring:message code="and"/>
            	</label>
            	<label class="radio-inline">
            		<input type="radio" name="numCfg[${tableInfo.tableName}].exprType" value="0"
            			<c:if test="${_cfg.numCfg[tableInfo.tableName].exprType==0}">checked</c:if>
            		><spring:message code="null"/>
            	</label>
            </div>
            <div for="isAreaEffective"></div>
        </div>
    </div>
</div>
<div class="row numCfg collapse">
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
            <div class="col-md-6">
   			<select name="numCfg[${tableInfo.tableName}].matchMethod" class="selectpicker select2 form-control" >
				<option value="" ><spring:message code="select"/></option>
				<option value="0" <c:if test="${_cfg.numCfg[tableInfo.tableName].matchMethod==0 }">selected</c:if>><spring:message code="substring_match"></spring:message></option>
				<option value="1" <c:if test="${_cfg.numCfg[tableInfo.tableName].matchMethod==1 }">selected</c:if>><spring:message code="right_match"></spring:message></option>
				<option value="2" <c:if test="${_cfg.numCfg[tableInfo.tableName].matchMethod==2 }">selected</c:if>><spring:message code="left_match"></spring:message></option>
				<option value="3" <c:if test="${_cfg.numCfg[tableInfo.tableName].matchMethod==3 }">selected</c:if>><spring:message code="exactly_match"></spring:message></option>
			</select>
            </div>
            <div for="matchMethod"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
            <div class="col-md-6">
   			<select name="numCfg[${tableInfo.tableName}].isHexbin" class="selectpicker select2 form-control" >
				<option value="" ><spring:message code="select"/></option>
				<option value="0" <c:if test="${_cfg.numCfg[tableInfo.tableName].isHexbin==0 }">selected</c:if>><spring:message code="case_insensitive_nohex"></spring:message></option>
				<option value="1" <c:if test="${_cfg.numCfg[tableInfo.tableName].isHexbin==1 }">selected</c:if>><spring:message code="hex_binary"></spring:message></option>
				<option value="2" <c:if test="${_cfg.numCfg[tableInfo.tableName].isHexbin==2 }">selected</c:if>><spring:message code="case_sensitive_nohex"></spring:message></option>
			</select>
            </div>
        </div>
    </div>
   	<input name="numCfg[${tableInfo.tableName}].cfgId" type="hidden" value="${_cfg.numCfg[tableInfo.tableName].cfgId}"/>
</div>
<!--/row-->