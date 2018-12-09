<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title></title>
<style type="text/css">
.tabbable-custom > .tab-content { 
	margin-top:-121px;
} 
.dropdown-menu{
	margin-bottom:150px;
}
</style>
	<script type="text/javascript">
	var validateForm;
	 	$(document).ready(function() {
			//校验叶子节点有下级不得更改为叶子节点
			/*  jQuery.validator.addMethod("leafChange",function(value,element){
				var flagLeafChange=false;
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/specific/specificServiceCfg/ajaxLeafChange',
					data:{parent:"${specificServiceCfg.specServiceId}",newIsLeaf:$("#isLeaf option:selected").val()},
					success:function(data){
						flagLeafChange=data;
					}
				});
				return flagLeafChange;
			},"<spring:message code='leafChange'/>");  */
			 
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				 rules:{
					 /* specServiceCode:{
						 remote:{
							 url:"${ctx}/specific/specificServiceCfg/isCodeNotRepeat",
							 type:"post",
							 //dataType:"json",
							 //contentType:"application/json; charset=utf-8",
							 data:{
								 parentId:function(){
									 return $("[name='parent.specServiceId']").val();
								 },
								 specServiceCode:function(){
									 return $("#specServiceCode").val();
								 },
								 cfgType:function(){
									 return $("[name='cfgType']").val();
								 },
								 specServiceId:'${specificServiceCfg.specServiceId}'	
							 }
						 }
					 } */
				},
				messages: {
					/* specServiceCode:{
						remote:'<spring:message code="repeat"/>'
					} */
					
				}, 
				
				submitHandler: function(form){
					if(!validateForm.form()) {
						return false;
					}
					loading('<spring:message code="submitting"/>');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
				}
			});
			//if("${specificServiceCfg.parentType}"){
			//	$("[name=cfgType]").each(function(){
			//		$(this).attr("parent-type","${specificServiceCfg.parentType}");
			//	});
			//}
			if(!$("[name='cfgType']").val()){
				$("[name='cfgType']").val(1);
				$(".cfgType").each(function(){
					if($(this).attr("cfgType")!=1){
						$(this).addClass("hidden");
						$(this).find("[name='parent.specServiceId']").removeAttr("name");
						$(this).find("[name='parent.specServiceName']").removeAttr("name");
						$(this).find("[name='businessType']").removeAttr("name");
						$(this).find("[name='specServiceCode']").removeAttr("name");
					}else{
						var cfgType=$(this).attr("cfgType");
						if("${specificServiceCfg.parent.specServiceId}"&& "${specificServiceCfg.parent.specServiceId}"!="0"){
							$("#businessType"+cfgType).parents(".businessType").addClass("hidden");
						}else{
							$("#businessType"+cfgType).parents(".businessType").removeClass("hidden");
						}
					}
				});
				$(".protocol_code").html('<font color="red">*</font><spring:message code="app_code"/>:');
				//$("input[name='specServiceCode']").attr("range","[300001,310000]");
				$(".protocol_name").html('<font color="red">*</font><spring:message code="app_name"/>:');
				$(".protocol_desc").html('<spring:message code="app_desc"/>:');
			}else{
				$(".cfgType").each(function(){
					if($(this).attr("cfgType")!=$("[name='cfgType']").val()){
						$(this).addClass("hidden");
						$(this).find("[name='parent.specServiceId']").removeAttr("name");
						$(this).find("[name='parent.specServiceName']").removeAttr("name");
						$(this).find("[name='businessType']").removeAttr("name");
						$(this).find("[name='specServiceCode']").removeAttr("name");
					}else{
						var cfgType=$(this).attr("cfgType");
						if("${specificServiceCfg.parent.specServiceId}"&& "${specificServiceCfg.parent.specServiceId}"!="0"){
							$("#businessType"+cfgType).parents(".businessType").addClass("hidden");
						}else{
							$("#businessType"+cfgType).parents(".businessType").removeClass("hidden");
						}
						
					}
				});
				if($("[name='cfgType']").val()==1){
					$(".protocol_code").html('<font color="red">*</font><spring:message code="app_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[300001,310000]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="app_name"/>:');
					$(".protocol_desc").html('<spring:message code="app_desc"/>:');
				}else if($("[name='cfgType']").val()==2){
					$(".protocol_code").html('<font color="red">*</font><spring:message code="tunnel_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[0,2147483647]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="tunnel_name"/>:');
					$(".protocol_desc").html('<spring:message code="tunnel_desc"/>:');
				}else{
					$(".protocol_code").html('<font color="red">*</font><spring:message code="protocol_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[10001,20000]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="protocol_name"/>:');
					$(".protocol_desc").html('<spring:message code="protocol_desc"/>:');
				}
			}
			$(".tabs-left li").on("click",function(){
				var cfgType=$(this).attr("cfgType");
				$("[name=cfgType]").val(cfgType);
				$(".cfgType").each(function(){
					if($(this).attr("cfgType")==cfgType){
						$("#specificServiceCfg"+cfgType+"Id").attr("name","parent.specServiceId");
						$("#specificServiceCfg"+cfgType+"Name").attr("name","parent.specServiceName");
						$("#businessType"+cfgType).attr("name","businessType");
						$("#specServiceCode"+cfgType).attr("name","specServiceCode");
						if($(this).hasClass("businessType")){
							var parentId=$("#specificServiceCfg"+cfgType+"Id").val();
							if(parentId!=0){
								$(this).addClass("hidden");
							}else{
								$(this).removeClass("hidden");
							}
						}else{
							$(this).removeClass("hidden");
						}
						
					}else{
						$(this).addClass("hidden");
						$(this).find("[name='parent.specServiceId']").removeAttr("name");
						$(this).find("[name='parent.specServiceName']").removeAttr("name");
						$(this).find("[name='businessType']").removeAttr("name");
					}
				});
				if(cfgType==1){
					$(".protocol_code").html('<font color="red">*</font><spring:message code="app_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[300001,310000]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="app_name"/>:');
					$(".protocol_desc").html('<spring:message code="app_desc"/>:');
				}else if(cfgType==2){
					$(".protocol_code").html('<font color="red">*</font><spring:message code="tunnel_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[0,2147483647]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="tunnel_name"/>:');
					$(".protocol_desc").html('<spring:message code="tunnel_desc"/>:');
				}else{
					$(".protocol_code").html('<font color="red">*</font><spring:message code="protocol_code"/>:');
					//$("input[name='specServiceCode']").attr("range","[10001,20000]");
					$(".protocol_name").html('<font color="red">*</font><spring:message code="protocol_name"/>:');
					$(".protocol_desc").html('<spring:message code="protocol_desc"/>:');
				}
			});
			$("#specificServiceCfg1Id,#specificServiceCfg2Id,#specificServiceCfg3Id").on("change",function(){
				var cfgType=$(this).parents(".cfgType").attr("cfgType");
				if($(this).val()==0){
					$("#businessType"+cfgType).parents(".businessType").removeClass("hidden");
				}else{
					$("#businessType"+cfgType).selectpicker("val","");
					$("#businessType"+cfgType).parents(".businessType").addClass("hidden");
				}
				if(!$(this).val()||$(this).val()==0){
					$("#isLeaf").val(0);
				}else{
					$("#isLeaf").val(1);
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
		<spring:message code="specific_service_cfg"/>
	</h3>
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-gift"></i><c:if test="${not empty specificServiceCfg.specServiceId}"><spring:message code='edit'/></c:if><c:if test="${empty specificServiceCfg.specServiceId}"><spring:message code='add'/></c:if></div>
					<div class="tools">
						<!-- <a href="javascript:;" class="collapse"> </a>
						<a href="#portlet-config" data-toggle="modal" class="config"> </a>
						<a href="javascript:;" class="reload"> </a>
						<a href="javascript:;" class="remove"> </a> -->
					</div>
				</div>
				<div class="portlet-body form">
						<!-- BEGIN FORM-->	
						<form:form id="inputForm" modelAttribute="specificServiceCfg" action="${ctx}/specific/specificServiceCfg/saveOrUpdate" method="post" class="form-horizontal">
							<input name="specServiceId" type="hidden" value="${specificServiceCfg.specServiceId}"/>
							<input name="cfgType" type="hidden" value="${specificServiceCfg.cfgType}"/>
							<form:hidden path="isValid"  class="form-control"/>
							<div class="form-body">
								<div class="portlet-body flip-scroll">
									<div class="row-fluid">
										<div class="span12">
											<div class="tabbable tabbable-custom tabs-left">
												<ul class="nav nav-tabs tabs-left">
													<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
													<li class="<c:if test="${(specificServiceCfg.specServiceId ==null and dict.itemCode==1) or specificServiceCfg.cfgType eq dict.itemCode}">active</c:if>" cfgType="${dict.itemCode}" >
														<a data-toggle="tab" href="#">
															<spring:message code="${dict.itemValue}"/>
														</a>
													</li>
													</c:forEach>
												</ul>
												<div class="tab-content">
													<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
														<c:set var="fatherName"><spring:message code="${dict.itemValue}"/></c:set>
														<div class="form-group cfgType" cfgType="${dict.itemCode}">
															<label class="col-md-3 control-label"><spring:message code="root_node"/>:</label>
															<div class="col-md-4">
																<c:if test="${empty specificServiceCfg.specServiceId}">
															    <sys:treeselect id="${'specificServiceCfg'.concat(dict.itemCode)}" name="parent.specServiceId" value="${specificServiceCfg.parent.specServiceId}" labelName="parent.specServiceName" labelValue="${fatherName}" title="${dict.itemValue}"
															    url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=${dict.itemCode}&specific=true" extId="${specificServiceCfg.specServiceId}" cssClass="required form-control"/>	
																</c:if>
																<c:if test="${not empty specificServiceCfg.specServiceId}">
																 <sys:treeselect id="${'specificServiceCfg'.concat(dict.itemCode)}" name="parent.specServiceId" value="${specificServiceCfg.cfgType eq dict.itemCode?specificServiceCfg.parent.specServiceId:0}" labelName="parent.specServiceName" labelValue="${(specificServiceCfg.cfgType eq dict.itemCode and specificServiceCfg.parent.specServiceId ne '0')?fns:getBySpecServiceId(specificServiceCfg.parent.specServiceId).specServiceName:fatherName}"
																title="${dict.itemValue}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=${dict.itemCode}&specific=true" extId="${specificServiceCfg.specServiceId}" cssClass="required form-control"/>	
																</c:if>
															</div>
															<div for="parent.specServiceName"></div>
														</div>
														<div class="form-group cfgType businessType
														<c:if test='${specificServiceCfg.parent.specServiceId ne null or specificServiceCfg.parent.specServiceId ne 0}'>hidden</c:if>" cfgType="${dict.itemCode}">
															<c:if test="${dict.itemCode==1}">
																<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="app_business_type"/>:</label>
																<div class="col-md-4">
																	<select id="businessType1" name="businessType" class="selectpicker form-control required" data-live-search="true" data-live-search-placeholder="search">
																		<option value=""><spring:message code="select"/></option>
						                                                <c:forEach items="${fns:getDictList('APP_BUSINESS_TYPE')}" var="dict">
																		  <option value="${dict.itemCode}"
																		  <c:if test="${dict.itemCode==specificServiceCfg.businessType}">selected</c:if>
																		  ><spring:message code="${dict.itemValue}"/></option>
																		</c:forEach>				                    
																	</select>
																</div>
															</c:if>
															<c:if test="${dict.itemCode==3}">
																<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="basic_protocol_business_type"/>:</label>
																<div class="col-md-4">
																<select id="businessType3" name="businessType"  class="selectpicker form-control required" data-live-search="true" data-live-search-placeholder="search">
					                                                <option value=""><spring:message code="select"/></option>
					                                                <c:forEach items="${fns:getDictList('BASIC_PROTOCOL_BUSINESS_TYPE')}" var="dict">
																	  <option value="${dict.itemCode}"
																	  <c:if test="${dict.itemCode==specificServiceCfg.businessType}">selected</c:if>
																	  ><spring:message code="${dict.itemValue}"/></option>
																	</c:forEach>				                    
																</select>
																</div>
															</c:if>
															<c:if test="${dict.itemCode==2}">
																<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="tunnel_behavior_business_type"/>:</label>
																<div class="col-md-4">
																<select id="businessType2" name="businessType" class="form-control required" data-live-search="true" data-live-search-placeholder="search">
					                                                <option value=""><spring:message code="select"/></option>
					                                                <c:forEach items="${fns:getDictList('TUNNEL_BEHAV_BUSINESS_TYPE')}" var="dict">
																	  <option value="${dict.itemCode}"
																	  <c:if test="${dict.itemCode==specificServiceCfg.businessType or (specificServiceCfg.parent.specServiceId eq null or specificServiceCfg.parent.specServiceId eq 0)}">selected</c:if>
																	  ><spring:message code="${dict.itemValue}"/></option>
																	</c:forEach>				                    
																</select>
																</div>
															</c:if>
															<div for="businessType"></div>
														</div>
														<%-- <c:if test="${dict.itemCode==1}">
														<div class="form-group cfgType">
															<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="app_code"/>:</label>
															<div class="col-md-4">
																<input id="specServiceCode${dict.itemCode}" name="specServiceCode" maxlength="50" range="[300001,310000]" class="form-control required digits specServiceCodeCheck" value="${specificServiceCfg.specServiceCode}" ctx="${ctx}"/>
															</div>
															<div for="specServiceCode"></div>
														</div> 
														</c:if>
														<c:if test="${dict.itemCode==3}">
														<div class="form-group cfgType">
															<label class="col-md-3 control-label protocol_code"><font color="red">*</font><spring:message code="protocol_code"/>:</label>
															<div class="col-md-4">
																<input id="specServiceCode${dict.itemCode}" name="specServiceCode" maxlength="50" range="[10001,20000]" class="form-control required digits specServiceCodeCheck" value="${specificServiceCfg.specServiceCode}" ctx="${ctx}"/>
															</div>
															<div for="specServiceCode"></div>
														</div> 
														</c:if>
														<c:if test="${dict.itemCode==2}">
														<div class="form-group cfgType">
															<label class="col-md-3 control-label protocol_code"><font color="red">*</font><spring:message code="tunnel_code"/>:</label>
															<div class="col-md-4">
																<input id="specServiceCode${dict.itemCode}" name="specServiceCode" maxlength="50" range="[0,2147483647]" class="form-control required digits specServiceCodeCheck" value="${specificServiceCfg.specServiceCode}" ctx="${ctx}"/>
															</div>
															<div for="specServiceCode"></div>
														</div> 
														</c:if> --%>
													</c:forEach>
												    <div class="form-group">
														<label class="col-md-3 control-label protocol_code"><font color="red">*</font><spring:message code="protocol_code"/>:</label>
														<div class="col-md-4">
															<input id="specServiceCode" name="specServiceCode" maxlength="50" range="[0,2147483647]" class="form-control required digits specServiceCodeCheck" value="${specificServiceCfg.specServiceCode}" ctx="${ctx}"/>
														</div>
														<div for="specServiceCode"></div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label protocol_name"><font color="red">*</font><spring:message code="protocol_name"/>:</label>
														<div class="col-md-4">
															<form:input path="specServiceName" htmlEscape="false" maxlength="64" class="form-control required noBlankSpace"/>
														</div>
														<div for="specServiceName"></div>
													</div> 
												 	<div class="form-group">
														<label class="col-md-3 control-label hidden"><spring:message code="group_id"/>:</label>
														<div class="col-md-4">
															<form:input path="groupId" htmlEscape="false" maxlength="50" range="[0,2147483647]" class="form-control digits hidden" placeholder="0"/>
														</div>
														<div for="groupId"></div>
													</div> 
													
													 <%-- <div class="form-group hidden">
														<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="is_leaf"/>:</label>
														<div class="col-md-4">
															<form:select path="isLeaf"  class="form-control leafChange">
						                                                <c:forEach items="${fns:getDictList('INT_YES_NO')}" var="dict">
																		  <form:option value="${dict.itemCode}"><spring:message code="${dict.itemValue}"/></form:option>
																		</c:forEach>				                    
															</form:select>
														</div>
													</div>  --%> 
													<c:if test="${not empty specificServiceCfg and specificServiceCfg.isLeaf ne null}">
														<input type="hidden" name="isLeaf" id="isLeaf" value="${specificServiceCfg.isLeaf}">
													</c:if>
													<c:if test="${empty specificServiceCfg or specificServiceCfg.isLeaf eq null}">
														<input type="hidden" name="isLeaf" id="isLeaf" value="0">
													</c:if>
													 <%-- <div class="form-group">
														<label class="col-md-3 control-label"><font color="red">*</font><spring:message code="cfg_type"/>:</label>
														<div class="col-md-4">
															<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
																<label class="radio-inline"><form:radiobutton path="cfgType" class="required checkParent" value="${dict.itemCode}"/><spring:message code="${dict.itemValue}"/></label>
															</c:forEach>
														</div>
													</div> --%>
													 <div class="form-group">
														<label class="col-md-3 control-label protocol_desc"><spring:message code="protocol_desc"/>:</label>
														<div class="col-md-4">
															<form:textarea path="specServiceDesc" htmlEscape="false" maxlength="64" class="form-control"/>
														</div>
														<div for="specServiceDesc"></div>
													</div>
													<div class="form-actions">
														<div class="row">
															<div class="col-md-offset-3 col-md-9">
																<shiro:hasPermission name="specific:service:edit"><button type="submit" class="btn btn-circle blue"><spring:message code="submit"/></button></shiro:hasPermission>
																<button type="button" class="btn btn-circle grey-salsa btn-outline" id="cancel" ><spring:message code="cancel"/></button>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
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
</body>
</html>