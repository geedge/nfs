<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<div class="row stringInfo">
<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
<span id="urlError" style="display:none"><spring:message code="url_error"></spring:message></span>
<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
	<div class="row"> 
		<div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="key_word"/></label>
	            <div class="col-md-6">
	            	 <input class="form-control required tags" type="text" id="tags_cfgKeywords" name="cfgKeywords" value="${_cfg.cfgKeywords}">
	            </div>
	            <div for="cfgKeywords" class="col-md-6"></div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expression_type"/></label>
	            <div class="col-md-6">
	            	<label class="radio-inline">
	            		<input type="radio" name="exprType" value="1" class="required"
	            			<c:if test="${_cfg.exprType==1}">checked</c:if>
	            		><spring:message code="and_expression"/>
	            	</label>
	            	<label class="radio-inline">
	            		<input type="radio" name="exprType" value="0" class="required"
	            			<c:if test="${_cfg.exprType==0 || _cfg.exprType==null}">checked</c:if>
	            		><spring:message code="null_expression"/>
	            	</label>
	            </div>
	            <div for="exprType"></div>
	        </div>
	    </div>
	</div>
	<div class="row">
		<div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="match_method"/></label>
	            <div class="col-md-6">
	            	<select name="matchMethod" class="selectpicker select2 form-control required" >
						<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
							<option value="${matchMethodC.itemCode}" <c:if test="${_cfg.matchMethod==matchMethodC.itemCode || (_cfg.matchMethod==null && matchMethodC.itemCode==0)}">selected</c:if>><spring:message code="${matchMethodC.itemValue}"/></option>
               			</c:forEach>     
					</select>
	            </div>
	            <div for="matchMethod"></div>
	        </div>
	    </div>
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_hex"/></label>
	            <div class="col-md-6">
	            	<label class="radio-inline">
	            		<input type="radio" name="isHex" value="1" class="required"
	            			<c:if test="${_cfg.isHexbin eq 1}">checked</c:if>
	            		><spring:message code="hex"/>
	            	</label>
	            	<label class="radio-inline">
	            		<input type="radio" name="isHex" value="0" class="required"
	            			<c:if test="${_cfg.isHexbin ne 1}">checked</c:if>
	            		><spring:message code="not_hex"/>
	            	</label>
	            </div>
	            <div for="isHex"></div>
	        </div>
	    </div>
	    
	</div>
	<div class="row">
	    <div class="col-md-6">
	        <div class="form-group ">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="is_case_insenstive"/></label>
	            <div class="col-md-6">
	            	<label class="radio-inline">
	            		<input type="radio" name="isCaseSenstive" value="1" class="required"
	            			<c:if test="${_cfg.isHexbin==1}">checked</c:if>
	            		><spring:message code="case_senstive"/>
	            	</label>
	            	<label class="radio-inline">
	            		<input type="radio" name="isCaseSenstive" value="0" class="required"
	            			<c:if test="${_cfg.isHexbin ne 1}">checked</c:if>
	            		><spring:message code="case_insenstive"/>
	            	</label>
	            </div>
	            <div for="isCaseInsenstive"></div>
	        </div>
	    </div>
	</div>
	<div class="row hidden">
		<div class="col-md-6">
	        <div class="form-group">
	            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_hexbinary"/></label>
	            <div class="col-md-6">
	            	<select name="isHexbin" class="selectpicker select2 form-control required" >
						<c:forEach items="${fns:getDictList('WHETHER_HEXBINARY')}" var="isHexbinC">
							<option value="${isHexbinC.itemCode}" <c:if test="${_cfg.isHexbin==isHexbinC.itemCode || (_cfg.matchMethod==null && isHexbinC.itemCode==0)}">selected</c:if>><spring:message code="${isHexbinC.itemValue}"/></option>
               			</c:forEach>
					</select>
	            </div>
	            <div for="isHexbin"></div>
	        </div>
	    </div>
	    
	</div>
</div>
<!--/row-->