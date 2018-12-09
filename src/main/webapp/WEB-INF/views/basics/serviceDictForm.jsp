<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/pages/css/dictInfo.css" />
<script type="text/javascript" src="${ctxStatic}/pages/scripts/dict.js"></script>
	<title></title>
	<style type="text/css">
label.errorShow {
    background: url("${ctxStatic}/global/plugins/jquery-validation/1.11.0/images/unchecked.gif") no-repeat 0 0;
    padding-left: 18px;
    padding-bottom: 2px;
    font-weight: bold;
    color: #ea5200;
    margin-left: 10px;
}
</style>
	<script type="text/javascript">
	var validateForm;
		//上级选择数据类型自动改变
	 	function serviceDictInfoTreeselectCallBack(){
			var parent = $("#serviceDictInfoId").val();
			$.ajax({
				type:'post',
				async:true,
				cache:false,
				url:'${ctx}/basics/serviceDictInfo/ajaxItemType',
				data:{parent:parent},
				success:function(data){
					$("#itemTypeCheckChild").val(data);
					if(data==0){
						$("#itemTypeCheckChild").prop("selectedIndex",0);
						}
				}
			});
			
		errorShow();
			
		} 
		  function errorShow(){
			var flaelNoSure = false;
			 $.ajax({
				type:'post',
				async:false,
				url:'${ctx}/basics/serviceDictInfo/isLevelTotalSure',
				data:{parentId:$(".singleClass").val(),currentId:'${serviceDictInfo.serviceDictId}'},
				success:function(data){
					if(data){
						$(".errorShow").prop("style","display:none");
					}else{
						$(".errorShow").prop("style","display:block");
					}
					flaelNoSure = data;
				}
			});  
				return flaelNoSure;
			}  
	 	$(document).ready(function() {
		 	$("#serviceDictInfoId").on('onchange',errorShow);
	 		//类型选择与上级是否冲突
			jQuery.validator.addMethod("typeSame",function(value,element){
				var flagTypeSame=false;
				var parentCondition = $("#serviceDictInfoId").val();
				var childCondition = value;
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/basics/serviceDictInfo/ajaxType',
					data:{parent:parentCondition,child:childCondition},
					success:function(data){
						flagTypeSame=data;
					}
				});
				return flagTypeSame;
			},"<spring:message code='typeSame'/>");
			//与下级是否一致
			jQuery.validator.addMethod("childrenType",function(value,element){
				var flagChildTypeSame=false;
				var currentIdAsP=${serviceDictInfo.serviceDictId}
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/basics/serviceDictInfo/ajaxChildrenType',
					data:{parent:currentIdAsP,itemType:value},
					success:function(data){
						flagChildTypeSame=data;
					}
				});
				return flagChildTypeSame;
			},"<spring:message code='typeChild'/>");
			
			//校验叶子节点有下级不得更改为叶子节点
			jQuery.validator.addMethod("leafChange",function(value,element){
				var flagLeafChange=false;
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/basics/serviceDictInfo/ajaxLeafChange',
					data:{parent:"${serviceDictInfo.serviceDictId}",newIsLeaf:$("#isLeaf option:selected").val()},
					success:function(data){
						flagLeafChange=data;
					}
				});
				return flagLeafChange;
			},"<spring:message code='leafChange'/>");
			
			jQuery.validator.addMethod("ipchecks",function(value,element){
				var tel=/^(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)$/;
				 return this.optional(element) || (tel.test(value));
			},"<spring:message code='ip_check'/>");
			
			$("#name").focus();
			validateForm=$("#inputForm").validate({
				rules: {
					'itemCode':{
						remote:'${ctx}/basics/serviceDictInfo/isItemCodeRepeat?oldItemCode=${serviceDictInfo.itemCode}',
					}
				},
				messages: {   
					'itemCode':{
						remote:'<spring:message code="repeat"/>',
					}
				},
				
				submitHandler: function(form){
					var res = errorShow();
					if(!res){
						return false;
						}
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
			<spring:message code="classification"/>
			<c:set var="title" value="classification"></c:set>
		</c:if>
		<c:if test="${itType==2 }">
			<spring:message code="attribute"/>
			<c:set  var="title" value="attribute"></c:set>
		</c:if>
		<c:if test="${itType==3 }">
			<spring:message code="label"/>
			<c:set  var="title" value="label"></c:set>
		</c:if>
		<c:if test="${itType==5 }">
			<spring:message code="vpn_ip"/>
			<c:set  var="title" value="vpnIp"></c:set>
		</c:if>
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty serviceDictInfo.serviceDictId}"><spring:message code='edit'/></c:if><c:if test="${empty serviceDictInfo.serviceDictId}"><spring:message code='add'/></c:if></div>
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
						<form:form id="inputForm" modelAttribute="serviceDictInfo" action="${ctx}/basics/serviceDictInfo/saveOrUpdate?itType=${itType}" method="post" class="form-horizontal">
							<form:hidden path="serviceDictId"/>
							<sys:message content="${message}"/>
							<div class="form-group   <c:if test="${itType==5 }"> hidden </c:if> " >
								<label class="col-md-3 control-label"><spring:message code="superior_config"/>:</label>
								<div class="col-md-4">
									<c:if test="${itType==1 }">
										<c:set var="fatherName"><spring:message code="classification"/></c:set>
									</c:if>
									<c:if test="${itType==2 }">
										<c:set var="fatherName"><spring:message code="attribute"/></c:set>
									</c:if>
									<c:if test="${itType==3 }">
										<c:set var="fatherName"><spring:message code="label"/></c:set>
									</c:if>
									<c:if test="${itType==5 }">
										<c:set var="fatherName"><spring:message code="vpn_ip"/></c:set>
									</c:if>
					                <sys:treeselect id="serviceDictInfo" name="parent.serviceDictId" value="${serviceDictInfo.parent.serviceDictId}" labelName="parent.itemValue"  
					                labelValue="${serviceDictInfo.parent.serviceDictId eq '0'?fatherName:fns:getServiceDictInfoById(serviceDictInfo.parent.serviceDictId).itemValue}"
									title="${title }" url="/basics/serviceDictInfo/treeData?itType=${itType}" cssClass="required form-control"/>	
 								<label  class="errorShow" style="display: none;"><spring:message code="isLevelNoSure"/></label>
								</div>
							</div>
							<div class="form-group	 <c:if test="${itType==5 }"> hidden </c:if>">
								<label class="col-md-3 control-label radio-lable"><font color="red">*</font> <spring:message code="item_type"/>:</label>
								<div class="col-md-4">
				                    <select id="itemTypeCheckChild" name="itemType" class="form-control childrenType typeSame">
					                    <c:forEach items="${fns:getDictList('SERVICE_DICT_ITM_TYPE')}" var="dict">
					                      <c:forEach items="${intArr}" var="itTemp">
					                          <c:if test="${dict.itemCode eq itTemp}"> 
							                          <option value="${dict.itemCode}"  <c:if test="${serviceDictInfo.itemType eq dict.itemCode}">selected="selected"</c:if>><spring:message code="${dict.itemValue}"/></option>
							                        </c:if>
					                      </c:forEach> 
					                    </c:forEach>
				                    </select>
								</div>
							</div>
							 <%-- <div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="item_code"/>:</label>
								<div class="col-md-4">
									<form:input path="itemCode" htmlEscape="false" maxlength="64" class="form-control required noBlankSpace"/>
								</div>
							</div>  --%>
							<div class="form-group">
								<label class="col-md-3 control-label"><font color="red">*</font>
								<c:if test="${itType==5 }">
										<spring:message code="vpn_ip"/>:
								</c:if>
								<c:if test="${itType!=5 }">
										<spring:message code="config_content"/>:
								</c:if>
								</label>
								<div class="col-md-4">
								<c:if test="${itType==5 }">
										 <input class="form-control required ipchecks "  id="itemValue"  type="text" name="itemValue" value="${serviceDictInfo.itemValue}">
								</c:if>
								<c:if test="${itType!=5 }">
										<form:input path="itemValue" htmlEscape="false"  maxlength="64" class="form-control required "/>
								</c:if>
								</div>
							</div>
							<div class="form-group  <c:if test="${itType==5 }"> hidden </c:if>">
								<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
								<div class="col-md-4">
									<form:select path="isLeaf"  class="form-control leafChange">
					                  <c:forEach items="${fns:getDictList('INT_YES_NO')}" var="dict">
												  <form:option value="${dict.itemCode}"><spring:message code="${dict.itemValue}"/></form:option>
												</c:forEach>
				                    </form:select>
								</div>
							</div>
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