<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<h3 class="form-section">
<spring:message code="${tableInfo.tableDesc}"/>
<small><span style="margin-left:10px"><input type="checkbox" name="cfgSelect" value="stringCfg"
<c:if test="${_cfg.stringCfg[tableInfo.tableName]!= null and _cfg.stringCfg[tableInfo.tableName].cfgId != null}">checked</c:if>
/> <spring:message code="optional"/></span></small>
</h3>
<div class="row stringCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
            <div class="col-md-6">
            	<input class="form-control" type="text" name="stringCfg[${tableInfo.tableName}].cfgDesc" value="${_cfg.stringCfg[tableInfo.tableName].cfgDesc}">
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
            <div class="col-md-6">
       			 <input class="form-control required" type="text" name="stringCfg[${tableInfo.tableName}].cfgKeywords" value="${_cfg.stringCfg[tableInfo.tableName].cfgKeywords}">
            </div>
            <div for="stringCfg[${tableInfo.tableName}].cfgKeywords"></div>
        </div>
    </div>
</div>
<div class="row stringCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
            <div class="col-md-6">
           		<label class="radio-inline">
            		<input type="radio" name="stringCfg[${tableInfo.tableName}].exprType" value="1"
            			<c:if test="${_cfg.stringCfg[tableInfo.tableName].exprType==1}">checked</c:if>
            		class="required"><spring:message code="and"/>
            	</label>
            	<label class="radio-inline">
            		<input type="radio" name="stringCfg[${tableInfo.tableName}].exprType" value="0"
            			<c:if test="${_cfg.stringCfg[tableInfo.tableName].exprType==0}">checked</c:if>
            		class="required"><spring:message code="null"/>
            	</label>
            </div>
            <div for="stringCfg[${tableInfo.tableName}].exprType"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
            <div class="col-md-6">
       		<select name="stringCfg[${tableInfo.tableName}].matchMethod" class="selectpicker select2 form-control required">
				<option value="" ><spring:message code="select"/></option>   
				<option value="0" <c:if test="${_cfg.stringCfg[tableInfo.tableName].matchMethod==0 }">selected</c:if>><spring:message code="substring_match"></spring:message></option>   
				<option value="1" <c:if test="${_cfg.stringCfg[tableInfo.tableName].matchMethod==1 }">selected</c:if>><spring:message code="right_match"></spring:message></option>       
				<option value="2" <c:if test="${_cfg.stringCfg[tableInfo.tableName].matchMethod==2 }">selected</c:if>><spring:message code="left_match"></spring:message></option>        
				<option value="3" <c:if test="${_cfg.stringCfg[tableInfo.tableName].matchMethod==3 }">selected</c:if>><spring:message code="exactly_match"></spring:message></option>     
			</select>
            </div>
            <div for="stringCfg[${tableInfo.tableName}].matchMethod"></div>
        </div>
    </div>
</div>
<div class="row stringCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
            <div class="col-md-6">
           		<select name="stringCfg[${tableInfo.tableName}].isHexbin" class="selectpicker select2 form-control required" >
					<option value="" ><spring:message code="select"/></option>
					<option value="0" <c:if test="${_cfg.stringCfg[tableInfo.tableName].isHexbin==0 }">selected</c:if>><spring:message code="case_insensitive_nohex"></spring:message></option>
					<option value="1" <c:if test="${_cfg.stringCfg[tableInfo.tableName].isHexbin==1 }">selected</c:if>><spring:message code="hex_binary"></spring:message></option>
					<option value="2" <c:if test="${_cfg.stringCfg[tableInfo.tableName].isHexbin==2 }">selected</c:if>><spring:message code="case_sensitive_nohex"></spring:message></option>
				</select>
            </div>
		    <div for="stringCfg[${tableInfo.tableName}].isHexbin"></div>
        </div>
    </div>
    <input name="stringCfg[${tableInfo.tableName}].cfgId" type="hidden" value="${_cfg.stringCfg[tableInfo.tableName].cfgId}"/>
</div>
<!--/row-->