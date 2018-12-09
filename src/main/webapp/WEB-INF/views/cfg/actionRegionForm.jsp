<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<script type="text/javascript">

</script>
</head>
<!-- block -->
<c:if test="${action == 16}">
<h4 class="form-section">
	<spring:message code="action_region" />
	<small></small>
</h4>
<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3">
					<spring:message code="response_code" />
			</label>
			<div class="col-md-6">
				<select name="userRegion1"
				class="selectpicker show-tick form-control">
					<c:forEach items="${fns:getDictList('RESPONSE_CODE')}" var="responseCodeC">
						<option value="${responseCodeC.itemCode}" <c:if test="${_cfg.userRegion1 eq responseCodeC.itemCode  || (empty _cfg.userRegion1 && responseCodeC.itemCode eq 404)}">selected</c:if>>${responseCodeC.itemCode} <spring:message code="${responseCodeC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="userRegion1"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
				<div class="col-md-3">
					<select id="response" class="selectpicker select2 input-small pull-right">
						<option value="responseContent"><spring:message code="response_content"></spring:message></option>
						<option value="responseFile"><spring:message code="response_file"></spring:message></option>
					</select>
				</div>
				<div class="col-md-6">
					<input id="responseContent" name="userRegion2" class="form-control" type="text" value="${_cfg.userRegion2 }">
					<select id="responseFile" name="userRegion3" data-live-search="true" data-live-search-placeholder="search" class="selectpicker form-control required">
	               		<option value=""><spring:message code="select"/></option>
	               		<c:forEach items="${allProxyFileStrategys}" var="file">
	               			<c:if test="${file.isValid!=0 and file.isAudit!=3}">
	               				<option action="${file.action }" value="${file.compileId}" 
	               		 	<c:if test="${file.compileId==_cfg.userRegion3}">selected</c:if>>${file.fileDesc}</option>
	               		</c:if>
	               		</c:forEach>
	               </select>
				</div>
			<div for="userRegion2"></div>
			<script type="text/javascript">
				$(function(){
					/*//清除没用的 file 信息
					var action = $("input[name='action']").val();
					$("#responseFile option").each(function(i,n){
						var oa = $(this).attr("action");
						if(oa != action && i != 0){
							$(this).remove();
						}
					}); */
					//页面加载时触发初始化操作
					var file = $("#responseFile").val();
					if(file){
						$("#responseContent").hide();//隐藏输入框
						$("#response").val("responseFile");
						$("#responseFile").selectpicker("show");//隐藏选择框
					}else{
						$("#responseFile").selectpicker("hide");//隐藏选择框
						$("#response").val("responseContent");
						$("#responseContent").show();//隐藏输入框
					}
					
					$("#response").on("change",function(){
						var $this = $(this);
						var inputId = $this.val();
						$("#responseContent").val("");//清空
						$("#responseFile").selectpicker("val","");
						if("responseContent" == inputId){
							$("#responseContent").show();//显示输入框
							$("#responseFile").selectpicker("hide");//隐藏选择框
						}else{
							$("#responseContent").hide();//隐藏输入框
							$("#responseFile").selectpicker("show");//显示选择框
						}
					});
				});
			</script>
		</div>
	</div>
</div>
</c:if>
<c:if test="${action == 48}">
<h4 class="form-section">
	<spring:message code="action_region" />
	<small></small>
</h4>
<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
					<spring:message code="redirect_response_code" />
			</label>
			<div class="col-md-6">
				<select id="redirectResponseCode" name="userRegion1"
				class="selectpicker show-tick form-control required">
					<c:forEach items="${fns:getDictList('REDIRECT_RESPONSE_CODE')}" var="redirectResponseCodeC">
						<option value="${redirectResponseCodeC.itemCode}" <c:if test="${_cfg.userRegion1==redirectResponseCodeC.itemCode || (_cfg.userRegion1==null && redirectResponseCodeC.itemCode==1)}">selected</c:if>>${redirectResponseCodeC.itemCode} <spring:message code="${redirectResponseCodeC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="userRegion1"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label id="responseLabel" class="control-label col-md-3 "><font color="red">*</font>
				<spring:message code="redirect_url" /></label>
			<div class="col-md-6">
				<input id="responseContent" name="userRegion2" class="form-control required httpCheck" type="text" value="${_cfg.userRegion2 }">
				<select id="responseFile" name="userRegion3" data-live-search="true" data-live-search-placeholder="search" class="selectpicker form-control required">
               		<option value=""><spring:message code="select"/></option>
               		<c:forEach items="${allProxyFileStrategys}" var="file">
               			<c:if test="${file.isValid!=0 and file.isAudit!=3}">
               				<option action="${file.action }" value="${file.compileId}" 
               		 			<c:if test="${file.compileId==_cfg.userRegion3}">selected</c:if>>${file.fileDesc}</option>
               			</c:if>
               		</c:forEach>
               </select>
			</div>
			<script type="text/javascript">
				$(function(){
					/* //清除没用的 file 信息
					var action = $("input[name='action']").val();
					$("#responseFile option").each(function(i,n){
						var oa = $(this).attr("action");
						if(oa != action && i != 0){
							$(this).remove();
						}
					}); */
					//页面加载时触发初始化操作
					var code = $("#redirectResponseCode").val();
					if(code == 200){//响应文件
						$("#responseContent").hide();//隐藏输入框
						$("#responseFile").selectpicker("show");//隐藏选择框
						$("#responseLabel").html('<font color="red">*</font><spring:message code="response_file" />');
					}else{
						$("#responseContent").show();//隐藏输入框
						$("#responseFile").selectpicker("hide");//隐藏选择框
						$("#responseLabel").html('<font color="red">*</font><spring:message code="redirect_url" />');
					}
					
					$("#redirectResponseCode").on("change",function(){
						var $this = $(this);
						var code = $this.val();
						if(code == 200){//重定向 应答码 为  200 时可以选择 应答文件
							$("#responseContent").hide();//隐藏输入框
							$("#responseFile").selectpicker("show");//隐藏选择框
							$("#responseLabel").html('<font color="red">*</font><spring:message code="response_file" />');
						}else{
							$("#responseContent").show();//隐藏输入框
							$("#responseFile").selectpicker("hide");//隐藏选择框
							$("#responseLabel").html('<font color="red">*</font><spring:message code="redirect_url" />');
						}
						$("#responseContent").val("");//清空
						$("#responseFile").selectpicker("val","");
					});
				});
			</script>

			<%-- <label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="redirect_url" /></label>
			<div class="col-md-6">
				<input class="form-control url required" type="text"
					name="userRegion2"
					value="${_cfg.userRegion2 }">
			</div> --%>
			<div for="userRegion2"></div>
			<div for="userRegion3"></div>
		</div>
	</div>
</div>
</c:if>
<c:if test="${action == 80}">
<h4 class="form-section">
	<spring:message code="action_region" />
	<small></small>
</h4>
<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label class="control-label col-md-3"><font color="red">*</font>
					<spring:message code="replace_zone" />
			</label>
			<div class="col-md-6">
				<select name="userRegion1"
				class="selectpicker show-tick form-control required">
					<c:forEach items="${fns:getDictList('CONTROL_REPLACE_ZONE')}" var="replaceZoneC">
						<option value="${replaceZoneC.itemCode}" <c:if test="${_cfg.userRegion1==replaceZoneC.itemCode || (_cfg.userRegion1==null && replaceZoneC.itemCode eq 'http_resp_body')}">selected</c:if>><spring:message code="${replaceZoneC.itemValue}"/></option>
					</c:forEach>
				</select>
			</div>
			<div for="userRegion1"></div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="replaced_content" /></label>
			<div class="col-md-6">
				<input class="form-control required" type="text"
					name="userRegion2"
					value="${_cfg.userRegion2 }">
			</div>
			<div for="userRegion2"></div>
		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group ">
			<label class="control-label col-md-3"><font color="red">*</font>
				<spring:message code="replace_content" /></label>
			<div class="col-md-6">
				<input class="form-control required" type="text"
					name="userRegion3"
					value="${_cfg.userRegion3 }">
			</div>
			<div for="userRegion3"></div>
		</div>
	</div>
</div>
</c:if>

</html>