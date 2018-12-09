<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/pages/css/dictInfo.css" />
<script type="text/javascript" src="${ctxStatic}/pages/scripts/dict.js"></script>
	<title>配置管理</title>
	<script type="text/javascript">
	var validateForm;
		//上级选择数据类型自动改变
	 	function sysDictInfoTreeselectCallBack(){
			var parent = $("#sysDictInfoId").val();
			$.ajax({
				type:'post',
				async:true,
				cache:false,
				url:'${ctx}/basics/sysDictInfo/ajaxItemType',
				data:{parent:parent},
				success:function(data){
					$("#itemType").val(data);
					if(data==0){
						$("#itemType").prop("selectedIndex",0);
						}
				}
			});
		} 
	 	$(document).ready(function() {
	 		//类型选择与上级是否冲突
			jQuery.validator.addMethod("typeSame",function(value,element){
				var parentCondition = $("#sysDictInfoId").val();
				var childCondition = $("#itemType option:selected").val();
				var flag=false;
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/basics/sysDictInfo/ajaxType',
					data:{parent:parentCondition,child:childCondition},
					success:function(data){
						flag=data;
					}
				});
				return flag;
			},"<spring:message code='typeSame'/>");
			
			//校验叶子节点,有下级不得更改为叶子节点
			jQuery.validator.addMethod("leafChange",function(value,element){
				var flag=false;
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/basics/sysDictInfo/ajaxLeafChange',
					data:{parent:"${sysDictInfo.sysDictId}",newIsLeaf:$("#isLeaf option:selected").val()},
					success:function(data){
						flag=data;
					}
				});
				return flag;
			},"<spring:message code='leafChange'/>");
			
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					'itemCode':{
						remote:'${ctx}/basics/sysDictInfo/isItemCodeRepeat?oldItemCode=${sysDictInfo.itemCode}&itType=${itType}'
					},
					'itemType':{
						remote:'${ctx}/basics/sysDictInfo/ajaxChildrenType?parent=${sysDictInfo.sysDictId}'
					}
			
				},
				messages: {
					'itemCode':{
						remote:'<spring:message code="repeat"/>'
					},
					'itemType':{
						remote:'<spring:message code="typeChild"/>'
					}
					
				},
				
				submitHandler: function(form){
					if(!validateForm.form()){
						return false;
					}
					loading('<spring:message code="submitting"/>');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("<spring:message code='enter_error'/>");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>


<div class="page-content">

	<div class="theme-panel hidden-xs hidden-sm">
		
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>

	</div>
		
		
 	<h3 class="page-title">
	<c:if test="${itType==1 }">
		<spring:message code="area"/>
		<c:set var="title" value="area"></c:set>
	</c:if>
	<c:if test="${itType==2 }">
		<spring:message code="isp"/>
		<c:set  var="title" value="isp"></c:set>
	</c:if>
	</h3> 

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty sysDictInfo.sysDictId}"><spring:message code='edit'/></c:if><c:if test="${empty sysDictInfo.sysDictId}"><spring:message code='add'/></c:if></div>
					<div class="tools">
						<!-- <a href="javascript:;" class="collapse"> </a>
						<a href="#portlet-config" data-toggle="modal" class="config"> </a>
						<a href="javascript:;" class="reload"> </a>
						<a href="javascript:;" class="remove"> </a> -->
					</div>
				</div>
				
				<div class="portlet-body form">
					
				
					<div class="form-body">

						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="sysDictInfo" action="${ctx}/basics/sysDictInfo/saveOrUpdate?itType=${itType}" method="post" class="form-horizontal">
							<form:hidden path="sysDictId"/>
							<sys:message content="${message}"/>
							<c:if test="${specType == null or specType!=3 }">
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="superior_config"/>:</label>
								<div class="col-md-4">
								    <c:set var="fatherName"><spring:message code="root_node"/></c:set>
					                <sys:treeselect id="sysDictInfo" name="parent.sysDictId" value="${sysDictInfo.parent.sysDictId}" labelName="parent.itemValue" labelValue="${sysDictInfo.parent.sysDictId eq '0'?fatherName:fns:getSysDictInfoById(sysDictInfo.parent.sysDictId).itemValue}"
											title="${title }" 
										url="/basics/sysDictInfo/treeData?itType=${itType}" extId="${sysDictInfo.sysDictId}" cssClass="required form-control"/>
								</div>
							</div>
							</c:if>
							<div class="form-group">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> <spring:message code="item_type"/>:</label>
								<div class="col-md-4">
				                    <select id="itemType" name="itemType" class="form-control typeSame">
					                    <c:forEach items="${fns:getDictList('SYS_DICT_ITM_TYPE')}" var="dict">
					                      <c:forEach items="${intArr}" var="itTemp">
					                          <c:if test="${dict.itemCode eq itTemp}"> 
							                          <option value="${dict.itemCode}" <c:if test="${sysDictInfo.itemType eq dict.itemCode}">selected="selected"</c:if>><spring:message code="${dict.itemValue}"/></option>
							                        </c:if>
					                      </c:forEach> 
					                    </c:forEach>
				                    </select>
								</div>
							</div>
							 <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_code"/>:</label>
								<div class="col-md-4">
									<form:input path="itemCode" htmlEscape="false" maxlength="64" class="form-control required noBlankSpace"/>
								</div>
							</div> 
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="config_content"/>:</label>
								<div class="col-md-4">
									<form:input path="itemValue" htmlEscape="false" maxlength="64" class="form-control required noBlankSpace"/>
								</div>
							</div>
							<c:if test="${specType != null and specType==3 }">
							    <form:hidden path="isLeaf" value="1"/>
							</c:if>
							<c:if test="${specType == null or specType!=3}">
								<div class="form-group">
									<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
									<div class="col-md-4">
											<form:select path="isLeaf"  class="form-control leafChange">
												<c:forEach items="${fns:getDictList('INT_YES_NO')}" var="dict">
												  <form:option value="${dict.itemCode}"><spring:message code="${dict.itemValue}"/></form:option>
												</c:forEach>
						                    </form:select>
									</div>
								</div>
							</c:if>
							<div class="form-group">
								<label class="col-md-3 control-label"><spring:message code="desc"/>:</label>
								<div class="col-md-4">
									<form:textarea path="itemDesc" htmlEscape="false" maxlength="128" class="form-control"/>
								</div>
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-offset-3 col-md-9">
										<button type="submit" class="btn btn-circle blue"><spring:message code="submit"/></button>
										<button type="button" class="btn btn-circle grey-salsa btn-outline" onclick="history.go(-1)"><spring:message code="cancel"/></button>
									</div>
								</div>
							</div>
						</form:form>
						<!-- END FORM-->
					</div>
					
				</div>
				
			</div>
		</div>
	</div>
</div>


	
</body>
</html>