<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<script type="text/javascript">
</script>
</head>
<div class="row">
	<div class="pull-right">
		<span class="glyphicon glyphicon-remove pull-right" title="remove"
			onClick="delContent('${tabName}${status.index}','${tabName}Add');" />
	</div>
</div>
<br>
<div class="row">
	<div class="col-md-6">
		<input type="hidden" name="userRegion5" value="${_cfg.userRegion5}">
        <div class="form-group">
            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="group"/></label>
	            <div class="col-md-6">
	            	<select name="userRegion4" class="selectpicker show-tick form-control required" data-live-search="true" data-live-search-placeholder="search">
             					<c:forEach items="${policyGroups }" var="policyGroup">
							<option asnId="${policyGroup.asnId}" value="${policyGroup.groupId}" <c:if test="${asn==policyGroup.groupId }">selected</c:if>><spring:message code="${policyGroup.groupName}"/></option>
              			</c:forEach>
	                </select>
	            </div>
	            <div for="userRegion4"></div>
        </div>
    </div>
</div>
<br>
</html>