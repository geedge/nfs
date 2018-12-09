<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script type="text/javascript">
$(function(){
	if(!$(".district").val()&&$(".otherValue").val()){
		$(".district").find("option").each(function(){
			var text=$(this).text().toLowerCase();
			if("others"==text){
				$(this).attr("selected","selected");
				$(this).parent().siblings(".otherValue").prop("type","text");
			}
		});
	}
})
</script>
<h3 class="form-section"><spring:message code="${tableInfo.tableDesc}"/>
<small><span style="margin-left:10px"><input type="checkbox" name="cfgSelect" value="complexCfg"
<c:if test="${_cfg.complexCfg[tableInfo.tableName]!= null and _cfg.complexCfg[tableInfo.tableName].cfgId != null}">checked</c:if>
/> <spring:message code="optional"/></span></small>
</h3>
<div class="row complexCfg collapse">
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
            <div class="col-md-6">
            	<input name="complexCfg[${tableInfo.tableName}].cfgDesc" type="text" class="form-control" value="${_cfg.complexCfg[tableInfo.tableName].cfgDesc}"/>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_area"/></label>
            <div class="col-md-6">
   				<select name="complexCfg[${tableInfo.tableName}].district" class="district selectpicker select2 form-control required">
   					<option value=""><spring:message code='select'/></option>
   					<c:forEach items="${fns:getFeaturesDictData(tableInfo.tableName)}" var="district">
   						<option value="${district.itemValue}"
   						<c:if test="${_cfg.complexCfg[tableInfo.tableName].district eq district.itemValue}">selected</c:if>
   						>${district.itemValue}</option>
   					</c:forEach>
   				</select>
            	<input type="hidden" name="" placeholder="<spring:message code='please_input'/> <spring:message code='custom_region'/>" class="otherValue form-control" value="${_cfg.complexCfg[tableInfo.tableName].district}"/>
            </div>
		    <div for="complexCfg[${tableInfo.tableName}].district"></div>
        </div>
    </div>
</div>
<div class="row complexCfg collapse">
	<div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
            <div class="col-md-6">
            	<input name="complexCfg[${tableInfo.tableName}].keywords" type="text" class="form-control required" value="${_cfg.complexCfg[tableInfo.tableName].keywords}"/>
            </div>
		    <div for="complexCfg[${tableInfo.tableName}].keywords"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
            <div class="col-md-6">
         	<label class="radio-inline">
           		<input type="radio" name="complexCfg[${tableInfo.tableName}].exprType" value="1"
           			<c:if test="${_cfg.complexCfg[tableInfo.tableName].exprType==1}">checked</c:if>
           		class="required"/><spring:message code="and_expression"/>
           	</label>
           	<label class="radio-inline">
           		<input type="radio" name="complexCfg[${tableInfo.tableName}].exprType" value="0"
           			<c:if test="${_cfg.complexCfg[tableInfo.tableName].exprType==0}">checked</c:if>
           		class="required"/><spring:message code="null_expression"/>
           	</label>
            </div>
	        <div for="complexCfg[${tableInfo.tableName}].exprType"></div>
        </div>
    </div>
</div>
<div class="row complexCfg collapse">
    <div class="col-md-6">
        <div class="form-group ">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
            <div class="col-md-6">
         	<select name="complexCfg[${tableInfo.tableName}].matchMethod" class="selectpicker select2 form-control required" >
				<option value="" ><spring:message code="select"/></option>
				<option value="0" <c:if test="${_cfg.complexCfg[tableInfo.tableName].matchMethod==0 }">selected</c:if>><spring:message code="substring_match"></spring:message></option>
				<option value="1" <c:if test="${_cfg.complexCfg[tableInfo.tableName].matchMethod==1 }">selected</c:if>><spring:message code="right_match"></spring:message></option>
				<option value="2" <c:if test="${_cfg.complexCfg[tableInfo.tableName].matchMethod==2 }">selected</c:if>><spring:message code="left_match"></spring:message></option>
				<option value="3" <c:if test="${_cfg.complexCfg[tableInfo.tableName].matchMethod==3 }">selected</c:if>><spring:message code="exactly_match"></spring:message></option>
			</select>
            </div>
            <div for="complexCfg[${tableInfo.tableName}].matchMethod"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
            <div class="col-md-6">
   			<select name="complexCfg[${tableInfo.tableName}].isHexbin" class="selectpicker select2 form-control required" >
				<option value="" ><spring:message code="select"/></option>
				<option value="0" <c:if test="${_cfg.complexCfg[tableInfo.tableName].isHexbin==0 }">selected</c:if>><spring:message code="case_insensitive_nohex"></spring:message></option>
				<option value="1" <c:if test="${_cfg.complexCfg[tableInfo.tableName].isHexbin==1 }">selected</c:if>><spring:message code="hex_binary"></spring:message></option>
				<option value="2" <c:if test="${_cfg.complexCfg[tableInfo.tableName].isHexbin==2 }">selected</c:if>><spring:message code="case_sensitive_nohex"></spring:message></option>
			</select>
            </div>
			<div for="complexCfg[${tableInfo.tableName}].isHexbin"></div>
        </div>
    </div>
    <input name="complexCfg[${tableInfo.tableName}].cfgId" type="hidden" value="${_cfg.complexCfg[tableInfo.tableName].cfgId}"/>
</div>
<!--/row-->