<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<style type="text/css">
		#items p input{
			margin-left:0px;
			width:80px;
		}
		#items p select{
			width:50px;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btn_add_item").click(function(){
				var itemCopy = $("#itemModel").clone();
				itemCopy.show();
				$("#items").append(itemCopy);	
			});
			
			//删除子项
			$("#items").delegate("button","click",function(){
				$(this).parent().remove();
			});
			
		    var $inputForm = $("#inputForm");
		    var $error = $('.alert-error', $inputForm);
            var $success = $('.alert-success', $inputForm);
            
            $inputForm.validate({
				rules: {
					'moduleName':{
						required:true
					},
					'mark':{
						required:true
					}
			
				},
				submitHandler: function(form){
					if(!validateItem()) {
						return false;
					}

					loading('onloading...');
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
		
		function validateItem(){

			var $itemsObject = $("#items >p:visible");
			if($itemsObject.length==0){
				$("#messageBox").text("<spring:message code='dict_saveitem_error'/>").show();
				return false;
				
			}else {
				
				var flag = true;
				$.each($itemsObject,function(i,obj){
					var code = $(obj).find(".itemCode").val();
					var value = $(obj).find(".itemValue").val();
					if(code==""||value==""){
						flag = false;
						return false;
					}
				});
				
				if(!flag){
					$("#messageBox").text("<spring:message code='dict_saveitem_nullerror'/>").show();
					return false;
				}
				
				//给每个对象设置name属性
				$.each($itemsObject,function(index,obj){
					$(obj).find(".itemCode").attr("name","dictItemList["+index+"].itemCode");
					$(obj).find(".itemValue").attr("name","dictItemList["+index+"].itemValue");
					$(obj).find(".itemDesc").attr("name","dictItemList["+index+"].itemDesc");
					$(obj).find(".itemStatus").attr("name","dictItemList["+index+"].status");
					$(obj).find(".itemType").attr("name","dictItemList["+index+"].type");
					
				});
			}
			
			return true;
			
		}
		
	</script>
</head>
<body>
<div class="page-content">
	<div class="row">
	
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default" onclick="history.go(-1)"><spring:message code="back"/></button>
	</div>
	<%-- <h3 class="page-title">
		<spring:message code="dictManage"/>
	</h3> --%>
		<div class="col-md-12">

	<%-- <ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dict/list">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${sysDataDictionaryName.id}">字典
			<shiro:hasPermission name="sys:dict:edit"></shiro:hasPermission>${not empty sysDataDictionaryName.id?'修改':'添加'}
			<shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission>
		</a></li>
	</ul><br/> --%>
	
	<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty sysDataDictionaryName.id}"><spring:message code="edit"/></c:if><c:if test="${empty sysDataDictionaryName.id}"><spring:message code="add"/></c:if></div>
				</div>
	<div class="portlet-body form">
	
	<form:form id="inputForm" modelAttribute="sysDataDictionaryName" action="${ctx}/sys/dict/saveOrUpdate" method="post" class="form-horizontal">
	
		<form:hidden path="id"/>
		<form:hidden path="revision"/>
		<sys:message content="${message}"/>
		
	
		<div class="alert alert-error hide">
			<button class="close" data-dismiss="alert"></button>
			<spring:message code="submit_error"/>
		</div>

		<div class="alert alert-success hide">

			<button class="close" data-dismiss="alert"></button>
			<spring:message code="form_validate"/>
		</div>
		<div class="form-body">							
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="module_name"/>:</label>
			<div class="col-md-4">
				<form:input path="moduleName" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="dict_mark"/>:</label>
			<div class="col-md-4">
				<form:input path="mark" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="entry"/></label>
			<div class="col-md-4">
				<span type="button" class="btn black" title="<spring:message code="click_add_entry"/>" id="btn_add_item">
					<i class="icon-plus"></i> <spring:message code="add_entry"/>
				</span>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
			</div>
			<div class="form-group">
			<div class="col-md-3"></div>
			 <div id="items" class="col-md-8">
               <p id="itemModel" style="display: none;margin-top: 3px;">
               		<spring:message code="mark"/> <input type="text"  class="itemCode" autocomplete="off"/>
               		   <spring:message code="value"/> <input type="text"  class="itemValue" autocomplete="off"/>
               		 <spring:message code="describe"/> <input type="text"  class="itemDesc" autocomplete="off"/>
               		  	<spring:message code="useable"/>
               		  	<select class="itemStatus noSelect2">
               		  		<option value="1"><spring:message code="yes"/></option>
               		  		<option value="0"><spring:message code="no"/></option>
               		  	</select>
					   <spring:message code="maintainable"/> 
						<select class="itemType noSelect2">
               		  		<option value="1"><spring:message code="yes"/></option>
               		  		<option value="0"><spring:message code="no"/></option>
               		  	</select>
					   <button type="button" class="btn red delItem" ><spring:message code="delete"/></button>
               </p>
               
            	<c:forEach items="${sysDataDictionaryName.dictItemList }" var="dictItem">
	            	 <p id="itemModel" style="margin-top: 3px;" >
	               		<spring:message code="mark"/> <input type="text"  class="itemCode" value="${dictItem.itemCode}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		   <spring:message code="value"/> <input type="text"  class="itemValue" value="${dictItem.itemValue}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		 <spring:message code="describe"/> <input type="text"  class="itemDesc" value="${dictItem.itemDesc}" autocomplete="off" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>/>
	               		  	<spring:message code="useable"/>
	               		  	<select class="itemStatus noSelect2" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>>
	               		  		<option value="1" <c:if test="${dictItem.status==1}">selected</c:if>><spring:message code="yes"/></option>
	               		  		<option value="0" <c:if test="${dictItem.status==0}">selected</c:if>><spring:message code="no"/></option>
	               		  	</select>
						   <spring:message code="maintainable"/>
							<select class="itemType noSelect2" <c:if test="${dictItem.type==0}">readonly="readonly"</c:if>>
	               		  		<option value="1" <c:if test="${dictItem.type==1}">selected</c:if>><spring:message code="yes"/></option>
	               		  		<option value="0" <c:if test="${dictItem.type==0}">selected</c:if>><spring:message code="no"/></option>
	               		  	</select>
	               		  	<c:if test="${dictItem.type==1}">
	               		  		<button type="button" class="btn red delItem" ><spring:message code="delete"/></button>
	               		  	</c:if>
	               </p>
	            </c:forEach>
               </div>
		</div>
		
		<div class="form-group">
			<label class="col-md-3 control-label"><spring:message code="desc"/>:</label>
			<div class="col-md-4">
				<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="200"  class="span6 form-control"/>
			</div>
		</div>
		
		<div id="messageBox" class="alert alert-error hide"></div>
		
		<div class="form-actions">
		<div class="row">
			<div class="col-md-offset-3 col-md-9">
			<shiro:hasPermission name="sys:dict:edit"></shiro:hasPermission>
			<button type="submit" class="btn btn-circle blue"><spring:message code="submit"></spring:message></button>
			<button type="button" class="btn btn-circle grey-salsa btn-outline" onclick="history.go(-1)"><spring:message code="cancel"></spring:message></button>
		</div>
		</div>
		</div>
		</div>
	</form:form>
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>