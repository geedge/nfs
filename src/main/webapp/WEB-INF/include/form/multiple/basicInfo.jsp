 <%@ page contentType="text/html;charset=UTF-8"%>
 <%@ include file="/WEB-INF/include/taglib.jsp"%>
 <h3 class="form-section"><spring:message code="basic_config"/></h3>
<div class="row">
   <div class="col-md-6">
       <div class="form-group">
           <label class="control-label col-md-3"><font color="red">*</font><spring:message code="letter"/></label>
           <div class="col-md-6">
               <select name="requestId" data-live-search="true" data-live-search-placeholder="search" class="selectpicker form-control required">
               	<option value=""><spring:message code="select"/></option>
               	<c:forEach items="${requestInfos}" var="requestInfo">
               		<option value="${requestInfo.id}"
               		 <c:if test="${requestInfo.isValid==0 or requestInfo.isAudit==3}">disabled="disabled"</c:if>
               		 <c:if test="${requestInfo.id==_cfg.requestId}">selected</c:if>>${requestInfo.requestTitle}</option>
               	</c:forEach>
               </select>
           </div>
           <div for="requestId"></div>
       </div>
   </div>
   <!--/span-->
   <div class="col-md-6">
       <div class="form-group">
           <label class="control-label col-md-3"><spring:message code="classification"/></label>
           <div class="col-md-6">
           	 <select name="classify" multiple class="selectpicker form-control" title=<spring:message code="select"/>>
               	<c:forEach items="${fls}" var="fl">
               		<c:choose>
               			<c:when test="${_cfg.classify==null or _cfg.classify==''}">
               				<option value="${fl.serviceDictId}">${fl.itemValue}</option>
               			</c:when>
               			<c:otherwise>
               			<option value="${fl.serviceDictId}"
               				<c:forEach items="${fn:split(_cfg.classify,',')}" var="_classify">
		               			<c:if test="${fl.isValid==0}">disabled="disabled"</c:if>
		               			<c:if test="${fn:trim(fl.serviceDictId) eq _classify}">selected</c:if>
               				</c:forEach>
               			>${fl.itemValue}</option>	
               			</c:otherwise>
               		</c:choose>
               	</c:forEach>
             </select>
           </div>
       </div>
   </div>
   <!--/span-->
</div>

 <div class="row">
     <div class="col-md-6">
         <div class="form-group">
             <label class="control-label col-md-3"><spring:message code="attribute"/></label>
             <div class="col-md-6">
                <select name="attribute" multiple class="selectpicker form-control" title=<spring:message code="select"/>>
               		<c:forEach items="${xzs}" var="xz">
	               		<c:choose>
	               			<c:when test="${_cfg.attribute==null or _cfg.attribute==''}">
	               				<option value="${xz.serviceDictId}">${xz.itemValue}</option>
	               			</c:when>
	               			<c:otherwise>
	               				<c:forEach items="${fn:split(_cfg.attribute,',')}" var="_attribute">
	               				<option value="${xz.serviceDictId}"
	               				<c:if test="${xz.isValid==0}">disabled="disabled"</c:if>
	               		 		<c:if test="${fn:trim(xz.serviceDictId) eq _attribute}">selected</c:if>
	               				>${xz.itemValue}</option>
	               				</c:forEach>
	               			</c:otherwise>
	               		</c:choose>
               		</c:forEach>
             	</select>
             </div>
         </div>
     </div>
     <div class="col-md-6">
         <div class="form-group">
             <label class="control-label col-md-3"><spring:message code="label"/></label>
             <div class="col-md-6">
                <select name="lable" multiple class="selectpicker form-control" title=<spring:message code="select"/>>
               		<c:forEach items="${lables}" var="lable">
	               		<c:choose>
	               			<c:when test="${_cfg.lable==null or _cfg.lable==''}">
	               				<option value="${lable.serviceDictId}">${lable.itemValue}</option>
	               			</c:when>
	               			<c:otherwise>
	               				<c:forEach items="${fn:split(_cfg.lable,',')}" var="_lable">
	               					<option value="${lable.serviceDictId}"
			               			<c:if test="${lable.isValid==0}">disabled="disabled"</c:if>
				               		<c:if test="${fn:trim(lable.serviceDictId) eq _lable}">selected</c:if>
				               		>${lable.itemValue}</option>
	               				</c:forEach>
	               			</c:otherwise>
	               		</c:choose>
	               		
	               		</c:forEach>
             	</select>
             </div>
         </div>
     </div>
 </div>