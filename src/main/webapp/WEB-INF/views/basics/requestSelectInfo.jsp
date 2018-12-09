<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script type="text/javascript">
function changesel(){
		if($("#requestIsAudit").val()!=''&&$("#requestIsAudit").val()!=null){
		$(".errorBlock").hide()
	} 
}
</script>
<div class="row"
	style="scrolling:auto;z-index:999;position:relative;height: 25px; font-size: 18px; text-align: center; margin-top: 30px;line-height: 30px;">
		<div class="form-group" style="margin-left:5px">
			<div class="col-md-12">
			<label class=" "><font color="red">*</font> <spring:message code="letter"/> </label>
				<select id="requestIsAudit" name="requestId" onchange="changesel()"
					class="selectpicker field required form-control input-medium " data-live-search="true"
					data-live-search-placeholder="search">
					<option value=""><spring:message code="select" /></option>
					<c:forEach items="${requestInfos}" var="requestInfo">
						<option value="${requestInfo.id}"><spring:message
								code="${requestInfo.requestTitle}"></spring:message></option>
					</c:forEach>

				</select>
			</div>

		</div>
</div>
<!--/row-->