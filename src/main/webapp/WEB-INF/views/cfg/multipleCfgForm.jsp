<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	switchIpType($("select[name$='ipType']"));
	areaControlInit();
	$("[name^='ipCfg']").attr("disabled",true);
	$("[name^='stringCfg']").attr("disabled",true);
	$("[name^='numCfg']").attr("disabled",true);
	$("[name^='complexCfg']").attr("disabled",true);
	if($("#ipSelect").is(":checked")){
		$("[name^='ipCfg']").removeProp("disabled");
		//解决select插件disbale后无法恢复
		$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
		$(".ipCfg").find(".bootstrap-select button").removeClass("disabled");
		$(".ipCfg").addClass("in");
	}
	$("[name='cfgSelect']:checked").each(function(){
		var row=$(this).parents(".form-section").next();
		if(row.hasClass("complexCfg")){
			$("[name^='complexCfg']").removeProp("disabled");
			//解决select插件disbale后无法恢复
			$(".complexCfg").find(".bootstrap-select").removeClass("disabled");
			$(".complexCfg").find(".bootstrap-select").removeClass("disabled");
			$(".complexCfg").find(".bootstrap-select button").removeClass("disabled");
			$(".complexCfg").addClass("in");
		}else if(row.hasClass("stringCfg")){
			$("[name^='stringCfg']").removeProp("disabled");
			//解决select插件disbale后无法恢复
			$(".stringCfg").find(".bootstrap-select").removeClass("disabled");
			$(".stringCfg").find(".bootstrap-select").removeClass("disabled");
			$(".stringCfg").find(".bootstrap-select button").removeClass("disabled");
			$(".stringCfg").addClass("in");
		}else if(row.hasClass("ipCfg")){
			$("[name^='ipCfg']").removeProp("disabled");
			//解决select插件disbale后无法恢复
			$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
			$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
			$(".ipCfg").find(".bootstrap-select button").removeClass("disabled");
			$(".ipCfg").addClass("in");
		}
	});
	$("input[name='isAreaEffective']").on('change',function(){
		var val=$(this).val();
		if(val==1){
			$(".areaType").removeClass("hidden");
			if($("input[name='areaType']:checked").val()==1){//areaISP
				$("#areaIsp").removeClass("hidden");
			}else if($("input[name='areaType']:checked").val()==0){//areaIp
				$("#areaIp").removeClass("hidden");	
			}
		}else{
			$(".areaType").addClass("hidden");
			$("#areaIp").addClass("hidden");
			$("#areaIsp").addClass("hidden");
		}
	});
	$("input[name='areaType']").on('change',function(){
		var val=$(this).val();
		if($(this).is(":visible")){
			if(val==0){
				$("#areaIp").removeClass("hidden");
				$("#areaIsp").addClass("hidden");
			}else{
				$("#areaIsp").removeClass("hidden");
				$("#areaIp").addClass("hidden");
			}
		}else{
			$("#areaIsp").addClass("hidden");
			$("#areaIp").addClass("hidden");
		}
	});
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
		submitHandler: function(form){
			var len=$("[name='cfgSelect']:checked").length;
			if(len==0){
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
				return;
			}
			form.submit();
		},
		errorContainer: "#messageBox"
	});
	$("[name='cfgSelect']").change(function(){
		var row=$(this).parents(".form-section").next();
		if($(this).prop("checked")){
			if(row.hasClass("complexCfg")){
				$("[name^='complexCfg']").removeProp("disabled");
				//解决select插件disbale后无法恢复
				$(".complexCfg").find(".bootstrap-select").removeClass("disabled");
				$(".complexCfg").find(".bootstrap-select").removeClass("disabled");
				$(".complexCfg").find(".bootstrap-select button").removeClass("disabled");
				$(".complexCfg").addClass("in");
			}else if(row.hasClass("stringCfg")){
				$("[name^='stringCfg']").removeProp("disabled");
				//解决select插件disbale后无法恢复
				$(".stringCfg").find(".bootstrap-select").removeClass("disabled");
				$(".stringCfg").find(".bootstrap-select").removeClass("disabled");
				$(".stringCfg").find(".bootstrap-select button").removeClass("disabled");
				$(".stringCfg").addClass("in");
			}else if(row.hasClass("ipCfg")){
				$("[name^='ipCfg']").removeProp("disabled");
				//解决select插件disbale后无法恢复
				$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
				$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
				$(".ipCfg").find(".bootstrap-select button").removeClass("disabled");
				$(".ipCfg").addClass("in");
			}
		}else{
			if(row.hasClass("complexCfg")){
				$(".complexCfg").removeClass("in");
				$("[name^='complexCfg']").attr("disabled",true);
			}else if(row.hasClass("stringCfg")){
				$(".stringCfg").removeClass("in");
				$("[name^='stringCfg']").attr("disabled",true);
			}else if(row.hasClass("ipCfg")){
				$(".ipCfg").removeClass("in");
				$("[name^='ipCfg']").attr("disabled",true);
				
			}
		}
	});
	$("#ipSelect").change(function(){
		if($(this).prop("checked")){
			$("[name^='ipCfg']").removeProp("disabled");
			//解决select插件disbale后无法恢复
			$(".ipCfg").find(".bootstrap-select").removeClass("disabled");
			$(".ipCfg").find(".bootstrap-select button").removeClass("disabled");
			$(".ipCfg").addClass("in");
		}else{
			$(".ipCfg").removeClass("in");
			$("[name^='ipCfg']").attr("disabled",true);
		}
	});
	
	$(".district").on("change",function(){
		var text=$(this).find("option:selected").text().toLowerCase();
		var other=$(this).parent("div").siblings(".otherValue");
		if("others"==text){
			other.prop("type","text");
			$(this).find("option").each(function(){
				if(other.val()==$(this).text()){
					other.val("");
					return false;
				}
			});
		}else{
			other.prop("type","hidden");
		}
	});
	$(".otherValue").on("change",function(){
		var val=$(this).val();
		if($(this).is(":visible")){
			$(this).prev("div").find("option").each(function(){
				if("others"==$(this).text().toLowerCase()){
					$(this).prop("value",val);
					if($(this).prop("selected")){
						$(this).parent("select.selectpicker").selectpicker('val',val);
					}
				}
			});
		}
		
	});
	$(".ipType").on("change",function(){
		var tableName=$(this).attr("name").replace(".ipType","");
		switchIpType($(".ipType"));
	}); 		

});
</script>
</head>
<body>

<div class="page-content">

	<%-- <h3 class="page-title">
		<spring:message code="${cfgName}"></spring:message>
	</h3> --%>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                     <div class="caption">
                         <i class="fa fa-gift"></i>
                        <c:if test="${empty _cfg.compileId}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${not empty _cfg.compileId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/cfg/multiple/saveOrUpdateCfg" method="post" class="form-horizontal">
                          <input type="hidden" name="operator" value="${operator}">
                          <input type="hidden" name="mainTable" value="${_cfg.mainTable}">
                          <input type="hidden" name="mainTableType" value="${_cfg.mainTableType}">
                          <input type="hidden" name="audit" value="${audit}">  
                          <input type="hidden" name="serviceId" value="${_cfg.serviceId}">  
                          <input type="hidden" name="cfgName" value="${cfgName}">  
                          <input type="hidden" name="action" value="${_cfg.action}">  
                          <input type="hidden" name="compileId" value="${_cfg.compileId}">
                          <div class="form-body">
                          	<c:forEach items="${tableInfos}" var="tableInfo">
                          		<c:choose>
                          		<c:when test="${tableInfo.tableType eq '4'}">
                          			<%@include file="/WEB-INF/include/form/multiple/complexInfo.jsp" %>
                          		</c:when>
                          		<c:when test="${tableInfo.tableType eq '3'}">
                          			<%@include file="/WEB-INF/include/form/multiple/numInfo.jsp" %>
                          		</c:when>
                          		<c:when test="${tableInfo.tableType eq '2'}">
                          			<%@include file="/WEB-INF/include/form/multiple/stringInfo.jsp" %>
                          		</c:when>
                          		</c:choose>
                          	</c:forEach>
                          	<c:forEach items="${tableInfos}" var="tableInfo">
                          		<c:choose>
                          		<c:when test="${tableInfo.tableType eq '1'}">
                          			<%@include file="/WEB-INF/include/form/multiple/ipInfo.jsp" %>
                          		</c:when>
                          		</c:choose>
                          	</c:forEach>
                          	<div class="row">
								    <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="whether_area_block"/></label>
								            <div class="col-md-6">
								            	<label class="radio-inline">
								            		<input type="radio" name="isAreaEffective" value="1"
								            			<c:if test="${_cfg.isAreaEffective==1}">checked</c:if>
								            		class="required"><spring:message code="yes"/>
								            	</label>
								            	<label class="radio-inline">
								            		<input type="radio" name="isAreaEffective" value="0"
								            			<c:if test="${_cfg.isAreaEffective==0}">checked</c:if>
								            		class="required"><spring:message code="no"/>
								            	</label>
								            </div>
								            <div for="isAreaEffective"></div>
								        </div>
								    </div>
								    <div class="hidden areaType col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="area_control_type"/></label>
								            <div class="col-md-6">
								            	<label class="radio-inline">
								            		<input type="radio" name="areaType" value="1" class="required"><spring:message code="area"/> <spring:message code="isp"/>
								            	</label>
								            	<label class="radio-inline">
								            		<input type="radio" name="areaType" value="0" class="required"><spring:message code="area"/> ip
								            	</label>
								            </div>
								            <div for="areaType"></div>
								            <input class="form-control" type="hidden" name="areaEffectiveIds" value="${_cfg.areaEffectiveIds}">
								        </div>
								    </div>
								</div>
							  <%@include file="/WEB-INF/include/form/areaInfo.jsp" %>
                              <%@include file="/WEB-INF/include/form/multiple/basicInfo.jsp" %>
                          </div>
                          <div class="form-actions">
                              <div class="row">
                                  <div class="col-md-6">
                                      <div class="row">
                                          <div class="col-md-offset-3 col-md-8">
                                              <button id="save" type="submit" class="btn green"><spring:message code="submit"/></button>
                                              <button id="cancel" type="button" class="btn default"><spring:message code="cancel"/></button>
                                          </div>
                                      </div>
                                  </div>
                                  <div class="col-md-6"> </div>
                              </div>
                          </div>
                      </form>
                      <!-- END FORM-->
                  </div>
              </div>
		</div>
	</div>
</div>
</body>
</html>