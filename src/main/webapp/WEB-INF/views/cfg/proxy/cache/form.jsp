<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<style type="text/css">
.boxSolid {
	border: 1px solid #eeeeee
}
</style>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".switch").bootstrapSwitch({
							/* onText:"<spring:message code='ON'/>",
							offText:"<spring:message code='OFF'/>", */
							onColor:"info",
							offColor:"default",
							size:"small",
							onInit: function() {
								if($(this).val()==''){
									$(this).val(0);
								}
							},
							onSwitchChange:function(event,state){
								if(state==true){
									$(this).val("1");
								}else{
									$(this).val("0");
								}
							}
						});
						/* $("#no_revalidate").each(function(){
							$(this).on("switchChange.bootstrapSwitch",function(event,state){
								if(state){
									$(this).val(1);
								}else{
									$(this).val(0);
								}
							})
						}); */
						$("#ignore_qs").tagsInput({
							width:$("#ignore_qs").find(".form-control").width(),
						   	   defaultText:'please input ignore query string in URL',
						   	   'delimiter':',',//特殊字符串分隔与表达式的多关键词
						   		maxCount:0,
						   	   onAddTag:function(tag,size){
							   		var reg = new RegExp(/\t|\r|\n|,/);
									if (tag.match(reg)) {
										$(this).parents(".col-md-6").next("div").html("<label class='error'><spring:message code='ignore_qs_error'/></label>");
									}else{
										$(this).parents(".col-md-6").next("div").html("");
									}
						   	   },
						   		onRemoveTag:function(tag,size){
						   		   $(this).parents(".col-md-6").next("div").html("");
						   	   }
						});
						$(".tagsinput").popover({
							animation:true,
							container:'body',
							placement:'right',
							html:true,
							trigger:"hover",
							title:"",
							content:function(){
								var content = $("#tagsinputTip").text();
							    return content;
							}
						});
						//initCommIpVal();
						/* $("#cancel").on("click", function() {
							window.history.back();
						});*/
						$(".action").on("change", function() {
							$("#serviceId").val($(this).attr("serviceId"));
							$("#protocolId").val($(this).attr("protocolId"));
							if($(this).val()==128){
// 								$("#doLog").val(0);
// 								$("input:radio[name='doLog']").attr("disabled",true);
								$(".cacheAction").hide();
							}else{
// 								$("input:radio[name='doLog']").attr("disabled",false);
								$(".cacheAction").show();
							}
						}); 
						$(".action:checked").change();
						
						$("#serviceId").val($(".action:checked").attr("serviceId"));
						$("#protocolId").val($(".action:checked").attr("protocolId"));

						$(".cache_key").on("click",function(){
							if($(this).val()=="include_cookie"){
								if($(this).is(":checked")){
									$(".cookie").removeClass("hidden");
								}else{
									$(".cookie").addClass("hidden");
								}
							}else if($(this).val()=="ignore_query_string"){
								if($(this).is(":checked")){
									$(".ignore_qs").removeClass("hidden");
								}else{
									$(".ignore_qs").addClass("hidden");
								}
							}
						});
						//$(".cache_key").click();
						
						$("#cfgFrom").validate(
								{
									errorPlacement : function(error, element) {
										if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
											error.appendTo(element.parent().parent());
										} else {
											if($(element).parents().hasClass("tagsinput")){
												$(element).parents(".col-md-6").next("div").append(error);
											}else{
												error.appendTo(element.parent().parent());
											}
										}
									},
									submitHandler : function(form) {
										var flag = true;
										var subscribeId=0;

										if($("#ignore_qs").parents(".col-md-6").next("div").html()!=''){
											$("#ignore_qs").parents(".col-md-6").next("div").html("<label class='error'><spring:message code='ignore_qs_error'/></label>");
											return;
										}
										if($("#pinning_time_unit").val()=='h'){
											if(parseInt($("#pinning_time").val())>24){
												$("div[for='pinning_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}else if($("#pinning_time_unit").val()=='m'){
											if(parseInt($("#pinning_time").val())>1440){
												$("div[for='pinning_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}else if($("#pinning_time_unit").val()=='s'){
											if(parseInt($("#pinning_time").val())>86400){
												$("div[for='pinning_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}
										if($("#inactive_time_unit").val()=='h'){
											if(parseInt($("#inactive_time").val())>24){
												$("div[for='inactive_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}else if($("#inactive_time_unit").val()=='m'){
											if(parseInt($("#inactive_time").val())>1440){
												$("div[for='inactive_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}else if($("#inactive_time_unit").val()=='s'){
											if(parseInt($("#inactive_time").val())>86400){
												$("div[for='inactive_time']").append("<label class='error'><spring:message code='cache_time_error'/></label>");
												return ;
											}
										}
										if($("#max_cache_size_unit").val()=='t'){
											if(parseInt($("#max_cache_size").val())>1024){
												$("div[for='max_cache_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}else if($("#max_cache_size_unit").val()=='g'){
											if(parseInt($("#max_cache_size").val())>1048576){
												$("div[for='max_cache_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}else if($("#max_cache_size_unit").val()=='m'){
											if(parseInt($("#max_cache_size").val())>1073741824){
												$("div[for='max_cache_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}
										if($("#max_cache_obj_size_unit").val()=='t'){
											if(parseInt($("#max_cache_obj_size").val())>1024){
												$("div[for='max_cache_obj_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}else if($("#max_cache_obj_size_unit").val()=='g'){
											if(parseInt($("#max_cache_obj_size").val())>1048576){
												$("div[for='max_cache_obj_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}else if($("#max_cache_obj_size_unit").val()=='m'){
											if(parseInt($("#max_cache_obj_size").val())>1073741824){
												$("div[for='max_cache_obj_size']").append("<label class='error'><spring:message code='cache_size_error'/></label>");
												return ;
											}
										}
										//存在隐藏的subscribeId，不算进域配置
										if($(".boxSolid.hidden").hasClass("subscribeId")){
											subscribeId=1;
										}
										var boxSolidLenth=$(".boxSolid").length;
										if($(".boxSolid").hasClass("subscribeId")){
											boxSolidLenth=boxSolidLenth-1;
										}
										//代表所有业务都隐藏了，提示必须增加一种业务数据
										if(boxSolidLenth  ==($(".boxSolid.hidden").length-subscribeId)){
											top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
											return;
										}
										//代表所有区域都隐藏了，提示必须增加个区域信息
										if($("input[name='isAreaEffective']:checked").val()==1 && $(".container-fluid:visible").size()==0){
											if($("#areaIsp").hasClass("hidden")) $("#areaIp").find(".glyphicon-plus").click();
											if($("#areaIp").hasClass("hidden")) $("#areaIsp").find(".glyphicon-plus").click();
											top.$.jBox.tip("<spring:message code='one_more_area'/>", "<spring:message code='info'/>");
											return;
										}
										$(".boxSolid:visible").find("input[name$='cfgKeywords']").each(function(){
											if($(this).val()==''){
												$(this).parents(".form-group").find(
														"div[for='"
																+ $(this).attr("name")
																+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$("#keywordError").text()+"</label>");
												flag = false;
											}
										});
										//keywords非空校验完成校验二进制字符串
										$(".boxSolid:visible").find("select[name$='isHexbin']").each(function(){
											var isHexbin=$(this).val();
											if(isHexbin == 1){ //十六进制 
												var keywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","cfgKeywords")+"']").val();
												keywords=keywords.replace("***and***","")	
											if(keywords != ''){
													if(!(/^([0-9|a-f|A-F]*)$/.test(keywords))){
														$(this).parents(".boxSolid").find(
																"div[for='"
																		+ $(this).attr("name").replace("isHexbin","cfgKeywords")
																		+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$.validator.messages.hexCheck+"</label>");
														flag = false;
													}
												}						
											}
										});
										var flag1=validateInvisibleCharTag();
										if(flag){
											flag=flag1;
										}
										if(flag){
											//将disable属性的元素删除
											$(".disabled").each(function(){
												$(this).remove();
											});
											$("input[name$='exprType']").attr("disabled",false);
											
											if($("#pinning_time").val()==''){
												$("#pinning_time").val(0+$("#pinning_time_unit").val());
											}else{
												$("#pinning_time").val($("#pinning_time").val()+$("#pinning_time_unit").val());
											}
											if($("#inactive_time").val()==''){
												$("#inactive_time").val(0+$("#inactive_time_unit").val());
											}else{
												$("#inactive_time").val($("#inactive_time").val()+$("#inactive_time_unit").val());
											}
											if($("#max_cache_size").val()==''){
												$("#max_cache_size").val(0+$("#max_cache_size_unit").val());
											}else{
												$("#max_cache_size").val($("#max_cache_size").val()+$("#max_cache_size_unit").val());
											}
											if($("#max_cache_obj_size").val()==''){
												$("#max_cache_obj_size").val(0+$("#max_cache_obj_size_unit").val());
											}else{
												$("#max_cache_obj_size").val($("#max_cache_obj_size").val()+$("#max_cache_obj_size_unit").val());
											}
											if($("#min_use").val()==''){
												$("#min_use").val(0);
											}
											$(".userRegion").each(function(){
												$(this).attr("name","userRegion['"+$(this).attr("id")+"']");
												if($(this).is(":checkbox") && $(this).hasClass("switch")){
													$(this).attr("checked","checked");
												}
											});
											loading('onloading...');
											form.submit();
										}else{
											return;
										}
										
									},
									errorContainer : "#messageBox",
								});
						//业务内容增加
						/* $("span[title='add']").on("click", function() {
							$(".ipPort0").removeClass("hidden");
						}); */

					});

	//业务窗口打开
	var addContent = function(obj, contentClassName) {
		var showDiv = $(obj).parent().parent().next();
		$(showDiv).removeClass("hidden").removeClass(
				"disabled");
		$(obj).addClass("hidden");
	}

	//业务窗口关闭
	var delContent = function(contentClassName, addBtnClassName) {
		$("." + contentClassName).addClass("hidden").addClass("disabled");
		$("." + addBtnClassName).removeClass("hidden");
	}
</script>
</head>
<body>
	<span id="keywordError" style="display:none"><spring:message code="required"></spring:message></span>
	<span id="tagsinputTip" style="display:none"><spring:message code="multiple_keywords_tip"></spring:message></span>
	<div class="page-content">
		<h3 class="page-title">
			<spring:message code="${_cfg.menuNameCode }"></spring:message>
		</h3>
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${empty _cfg.cfgId}">
								<spring:message code="add"></spring:message>
							</c:if>
							<c:if test="${not empty _cfg.cfgId}">
								<spring:message code="edit"></spring:message>
							</c:if>
						</div>
					</div>
					<div class="portlet-body form">
						<!-- BEGIN FORM-->
						<form id="cfgFrom" action="${ctx}/proxy/cache/saveOrUpdate"
							method="post" class="form-horizontal">
							<input type="hidden" name="cfgId" id="cfgId" value="${_cfg.cfgId}">
							<input type="hidden" name="compileId" value="${_cfg.compileId}">
							<input type="hidden" name="functionId" value="${_cfg.functionId}">
							<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
							<input type="hidden" id="doLog" name="doLog" value="1">
							<div class="form-body">

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="config_describe" /></label>
											<div class="col-md-6">
												<input class="form-control" type="text" name="cfgDesc"
													value="${_cfg.cfgDesc}">
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="action" /></label>
											<div class="col-md-6">
												<c:forEach items="${serviceList}" var="service"
													varStatus="satus">
													<label class="radio-inline"> <c:if
															test="${_cfg.functionId eq service.functionId}">
															<input type="radio" name="action"
																serviceId="${service.serviceId }"
																protocolId="${service.protocolId }"
																regionCode="${service.regionCode }"
																value="${service.action }" class="required action"
																<c:if test="${_cfg.action==service.action || (_cfg.action==null && satus.index==0)}">checked</c:if>>
																<spring:message code="${service.actionCode }"/>
																<%-- <c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
																	<c:if test="${dict.itemCode eq service.action }">
																		<c:set var="action" value="${dict.itemCode }"></c:set>
																		<spring:message code="${dict.itemValue }"/>
																	</c:if>
																</c:forEach> --%>
														</c:if>
													</label>
												</c:forEach>
											</div>
											<div for="action"></div>
										</div>
									</div>
								</div>
								<!-- 记录日志begin -->
								<%-- <div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message code="do_log" /></label>
											<c:forEach items="${fns:getDictList('DO_LOG') }" var="dict">
														<c:choose>
															<c:when test="${dict.itemCode eq _cfg.doLog}">
																<label class="radio-inline">
															<input type="radio" id="doLog" name="doLog" checked value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
																</label>
															</c:when>
															<c:otherwise>
																<label class="radio-inline">
																	<input type="radio" id="doLog" name="doLog" value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/>
																</label>
															</c:otherwise>
														</c:choose>
											</c:forEach>
										</div>
									</div>
								</div> --%>
								<!-- 记录日志end -->
								<h4 class="form-section cacheAction">
									<spring:message code="action_region" />
									<small></small>
								</h4>
								<div class="row cacheAction">
									<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">
															<spring:message code="cache_key" />
													</label>
													<div class="col-md-6">
														<%-- <select name="userRegion['cache_key']" id="cache_key"
														class="selectpicker show-tick form-control" multiple>
															<c:forEach items="${fns:getDictList('CACHE_KEY')}" var="cacheKey">
																<option value="${cacheKey.itemCode}" <c:if test="${_cfg.userRegion.cacke_key eq cacheKey.itemCode  || (empty _cfg.userRegion.cache_key && cacheKey.itemCode eq 'ignore_qs')}">selected</c:if>><spring:message code="${cacheKey.itemCode}"/></option>
															</c:forEach>
														</select> --%>
														<div class="mt-checkbox-inline">
															 <c:forEach items="${fns:getDictList('CACHE_KEY')}"  var="dict">
																 	 <label class="mt-checkbox">
																 	 	<input type="checkbox" class="cache_key"  
																 	 	<c:if test="${!empty _cfg.userRegion.cache_key.cookie and dict.itemCode eq 'include_cookie' }"> checked</c:if>
																 	 	<c:if test="${!empty _cfg.userRegion.cache_key.ignore_qs and dict.itemCode eq 'ignore_query_string' }"> checked</c:if>
																 	 	value="${dict.itemCode }" /> 
																 	 	<spring:message code="${dict.itemValue }" />
																	 	<span></span>
																 	 </label>
															 </c:forEach>
														 </div> 
													</div>
											</div>
									</div>
							</div>
							<div class="row cacheAction">
									<div class="col-md-6 cookie <c:if test='${empty _cfg.userRegion.cache_key.cookie }'>hidden</c:if>">
										<div class="form-group">
											<label class="control-label col-md-3">
											<spring:message code="include_cookie" />
											</label>
											<div class="col-md-6">
											<input id="cookie" name="cookie" class="form-control invisibleChar" type="text" value="${_cfg.userRegion.cache_key.cookie}">
										 	</div>
											<div for="cookie"></div>
										</div>
									</div>
									<%-- <c:set var="propertyName" >ignore_qs</c:set> --%>
									<div class="col-md-6 ignore_qs <c:if test='${empty _cfg.userRegion.cache_key.ignore_qs }'>hidden</c:if>">
										<div class="form-group">
											<label class="control-label col-md-3">
												<spring:message code="ignore_query_string" />
											</label>
											<div class="col-md-6">
											<input id="ignore_qs" name="ignore_qs" class="form-control tags" type="text"  
											value="${fn:replace(fn:replace(_cfg.userRegion.cache_key.ignore_qs, '[',''),']','')}">
										 	</div>
											<div for="ignore_qs"></div>
										</div>
									</div>
									
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="no_revalidate" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"   id="no_revalidate" name="no_revalidate" class="switch userRegion" 
								            		value="${_cfg.userRegion['no_revalidate'] }"  <c:if test="${_cfg.userRegion['no_revalidate'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['no_revalidate']" id="no_revalidate"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('CACHE_NO_REVALIDATE')}" var="dict">
														<option value="${dict.itemCode}" 
														<c:if test="${_cfg.userRegion.no_revalidate eq dict.itemCode  
														|| (empty _cfg.userRegion.no_revalidate && dict.itemCode eq 0)}">selected</c:if>>
														<spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select>  --%>
											</div>
											<div for="no_revalidate"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="cache_dyn_url" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"  id="cache_dyn_url" name="cache_dyn_url" class="switch userRegion" 
								            		value="${_cfg.userRegion['cache_dyn_url']}"  <c:if test="${_cfg.userRegion['cache_dyn_url'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['cache_dyn_url']" id="cache_dyn_url"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('CACHE_DYN_URL')}" var="dict">
														<option value="${dict.itemCode}" <c:if test="${_cfg.userRegion.cache_dyn_url eq dict.itemCode  || (empty _cfg.userRegion.cache_dyn_url && dict.itemCode eq 0)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select> --%>
											</div>
											<div for="userRegion['cache_dyn_url']"></div>
										</div>
									</div>
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="cache_cookied_cont" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"  id="cache_cookied_cont" name="cache_cookied_cont" class="switch userRegion" 
								            		value="${_cfg.userRegion['cache_cookied_cont'] }"  <c:if test="${_cfg.userRegion['cache_cookied_cont'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['cache_cookied_cont']" id="cache_cookied_cont"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('CACHE_COOKIED_CONT')}" var="dict">
														<option value="${dict.itemCode}" <c:if test="${_cfg.userRegion.cache_cookied_cont eq dict.itemCode  || (empty _cfg.userRegion.cache_cookied_cont && dict.itemCode eq 0)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select> --%>
											</div>
											<div for="cache_cookied_cont"></div>
										</div>
											
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
												<spring:message code="ignore_req_nocache" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"  id="ignore_req_nocache"  name="ignore_req_nocache" class="switch userRegion" 
								            		value="${_cfg.userRegion['ignore_req_nocache'] }"  <c:if test="${_cfg.userRegion['ignore_req_nocache'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['ignore_req_nocache']" id="ignore_req_nocache"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('IGNORE_REQ_NOCACHE')}" var="dict">
														<option value="${dict.itemCode}" <c:if test="${_cfg.userRegion.ignore_req_nocache eq dict.itemCode  || (empty _cfg.userRegion.ignore_req_nocache && dict.itemCode eq 0)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select> --%>
											</div>
											<div for="ignore_req_nocache"></div>
										</div>
										
									</div>
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="ignore_res_nocache" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"  id="ignore_res_nocache" name="ignore_res_nocache" class="switch userRegion" 
								            		value="${_cfg.userRegion['ignore_res_nocache'] }"  <c:if test="${_cfg.userRegion['ignore_res_nocache'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['ignore_res_nocache']" id="ignore_res_nocache"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('IGNORE_REQ_NOCACHE')}" var="dict">
														<option value="${dict.itemCode}" <c:if test="${_cfg.userRegion.ignore_res_nocache eq dict.itemCode  || (empty _cfg.userRegion.ignore_res_nocache && dict.itemCode eq 0)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select> --%>
											</div>
											<div for="ignore_res_nocache"></div>
										</div>
											
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
												<spring:message code="force_caching" />
											</label>
											<div class="col-md-6">
												<div class="bootstrap-switch">
								            		<input type="checkbox"  id="force_caching" name="force_caching" class="switch userRegion" 
								            		value="${_cfg.userRegion['force_caching'] }"  <c:if test="${_cfg.userRegion['force_caching'] eq 1}">checked</c:if>/>
								            	</div>
												<%-- <select name="userRegion['force_caching']" id="force_caching"
														class="selectpicker show-tick form-control">
													<c:forEach items="${fns:getDictList('FORCE_CACHING')}" var="dict">
														<option value="${dict.itemCode}" <c:if test="${_cfg.userRegion.force_caching eq dict.itemCode  || (empty _cfg.userRegion.force_caching && dict.itemCode eq 0)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
													</c:forEach>
												</select> --%>
											</div>
											<div for="force_caching"></div>
										</div>
										
									</div>
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="min_use" />
											</label>
											<div class="col-md-6">
												<input id="min_use" class="form-control digits userRegion"  name="min_use" placeholder="0"  type="text" value="${_cfg.userRegion.min_use}">
											</div><div for="min_use"></div>
										</div>
											
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
												<spring:message code="pinning_time" />
											</label>
											<div class="col-md-6">
												<div class="input-group">
													<input type="input"  id="pinning_time"  name="pinning_time"  placeholder="0"  class="form-control input-small digits userRegion"  
													value="${fn:substring(_cfg.userRegion.pinning_time,0, fn:length(_cfg.userRegion.pinning_time)-1) }" />
													<select id="pinning_time_unit"
															class="selectpicker show-tick form-control input-small">
														<c:forEach items="${fns:getDictList('CACHE_TIME_UNIT')}" var="dict">
															<option value="${dict.itemCode}" <c:if test="${fns:endsWith(_cfg.userRegion.pinning_time,dict.itemCode)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="pinning_time"></div>
												<%-- <div class="input-group">
													<input id="pinning_time_hour" class="form-control input-xsmall"  type="text" />													
													<span class="input-group-btn">
													<button class="btn" type="button">h</button>
													</span>
													<input id="pinning_time_min"  class="form-control input-xsmall" type="text"  />
													<span class="input-group-btn">
													<button class="btn" type="button">m</button>
													</span>
													<input id="pinning_time_sec"  class="form-control input-xsmall" type="text"  value="${fn:replace(_cfg.userRegion.pinning_time, 's', '') }" />
													<span class="input-group-btn">
													<button class="btn" type="button">s</button>
													</span>
												</div> --%>
											</div>
										</div>
										
									</div>
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="max_cache_size" />
											</label>
											<div class="col-md-6">
												<div class="input-group">
													<input type="input"  id="max_cache_size" name="max_cache_size"  placeholder="unlimited"   class="form-control input-small digits userRegion" 
													 value="${fn:substring(_cfg.userRegion.max_cache_size,0, fn:length(_cfg.userRegion.max_cache_size)-1) }" />
													<select id="max_cache_size_unit"
															class="selectpicker show-tick form-control input-small">
														<c:forEach items="${fns:getDictList('CACHE_SIZE_UNIT')}" var="dict">
															<option value="${dict.itemCode}" <c:if test="${fns:endsWith(_cfg.userRegion.max_cache_size,dict.itemCode)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
														</c:forEach>
													</select>
												</div>
												<div for="max_cache_size"></div>
												<%-- <div class="input-group">
													<input id="max_cache_size_g" class="form-control input-small"  type="text" />													
													<span class="input-group-btn">
													<button class="btn" type="button">G</button>
													</span>
													<input id="max_cache_size_m"  class="form-control input-small" type="text"  value="${fn:replace(_cfg.userRegion.max_cache_size, 'm', '')}" />													<span class="input-group-btn">
													<button class="btn" type="button">M</button>
													</span>
												</div> --%>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
												<spring:message code="inactive_time" />
											</label>
											<div class="col-md-6">
												<div class="input-group">
													<input type="input" id="inactive_time"  placeholder="0"  name="inactive_time"  class="form-control input-small digits userRegion" 
													 value="${fn:substring(_cfg.userRegion.inactive_time,0, fn:length(_cfg.userRegion.inactive_time)-1) }" />
													<select id="inactive_time_unit"
															class="selectpicker show-tick form-control input-small">
														<c:forEach items="${fns:getDictList('CACHE_TIME_UNIT')}" var="dict">
															<option value="${dict.itemCode}" <c:if test="${fns:endsWith(_cfg.userRegion.inactive_time,dict.itemCode)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
														</c:forEach>
													</select>
													<!-- <a href="#" style="margin-left:5px" data-original-title="" class="tooltips" data-flag="false" data-html="true" data-placement="top">
														<i class="icon-info"></i>
													</a> -->
												</div>
												<div for="inactive_time"></div>
												<%-- <div class="input-group">
													<input id="inactive_time_hour" class="form-control input-xsmall"  type="text" />
													<span class="input-group-btn">
													<button class="btn" type="button">h</button>
													</span>
													<input id="inactive_time_min" class="form-control input-xsmall" type="text"  />
													<span class="input-group-btn">
													<button class="btn" type="button">m</button>
													</span>
													<input id="inactive_time_sec" class="form-control input-xsmall" type="text"  value="${fn:replace(_cfg.userRegion.inactive_time, 's','')}" />
													<span class="input-group-btn">
													<button class="btn" type="button">s</button>
													</span>
												</div> --%>
												<%-- <input id="inactive_time" name="userRegion['inactive_time']" class="form-control" type="text" value="${_cfg.userRegion.inactive_time}"> --%>
											</div>
										</div>
										
									</div>
								</div>
								<div class="row cacheAction">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3">
													<spring:message code="max_cache_obj_size" />
											</label>
											<div class="col-md-6">
												<div class="input-group">
													<input type="input"  id="max_cache_obj_size"  name="max_cache_obj_size" placeholder="unlimited"   class="form-control input-small digits userRegion" 
													value="${fn:substring(_cfg.userRegion.max_cache_obj_size, 0,fn:length(_cfg.userRegion.max_cache_obj_size)-1) }" />
													<select id="max_cache_obj_size_unit"
															class="selectpicker show-tick form-control input-small">
														<c:forEach items="${fns:getDictList('CACHE_SIZE_UNIT')}" var="dict">
															<option value="${dict.itemCode}" <c:if test="${fns:endsWith(_cfg.userRegion.max_cache_obj_size,dict.itemCode)}">selected</c:if>><spring:message code="${dict.itemValue}"/></option>
														</c:forEach>
													</select>
													<!-- <a href="#" style="margin-left:5px" data-original-title=" Inactive time specifies how long an item can remain in the cache without being accessed. In this example, a file that has not been requested for 60 minutes is automatically deleted from the cache by the cache manager process, regardless of whether or not it has expired" class="tooltips" data-flag="false" data-html="true" data-placement="top">
														<i class="icon-info"></i>
													</a> -->
												</div>
												<div for="max_cache_obj_size"></div>
											</div>
										</div>
									</div>
								</div>
								<br>
								<c:set var="strCfgIndex" value="0"></c:set>
								<c:forEach items="${regionList}" var="region" varStatus="status">
									<c:if test="${region.regionType eq 2 }">
										<c:set var="tabName" value="${region.configRegionValue}Tab"></c:set>
										<h4 class="form-section">
											<spring:message code="${region.configRegionValue}" />
											<small> <span
												class="glyphicon glyphicon-plus ${tabName}Add"
												onClick="addContent(this,'${tabName}')" title="add"></span></small>
										</h4>
										<c:set var="cfgName" value="stringList[${strCfgIndex}]"></c:set>
										<c:choose>
												<c:when test="${fn:length(_cfg.stringList)>0}">
													<c:set var="isBreak" value="false" ></c:set>
													<c:forEach items="${_cfg.stringList}" var="cfg">
														<c:choose>
															<c:when test="${region.configRegionValue eq cfg.cfgType and !isBreak}">
																<div class="row boxSolid ${tabName}${status.index}">
																	<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
																</div>
																<c:set var="isBreak" value="true" ></c:set>
																
																	<c:set var="strCfgIndex" value="${strCfgIndex+1 }"></c:set>
															</c:when>
														</c:choose>
													</c:forEach>
													<c:if test="${!isBreak}">
														<div class="row boxSolid ${tabName}${status.index} hidden disabled">
															<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
														</div>
																
														<c:set var="strCfgIndex" value="${strCfgIndex+1 }"></c:set>
														
													</c:if>
												</c:when>
												<c:otherwise>
													<div class="row boxSolid ${tabName}${status.index} hidden disabled">
														<%@include file="/WEB-INF/views/cfg/complexCfgForm.jsp"%>
													</div>
													<c:set var="strCfgIndex" value="${strCfgIndex+1 }"></c:set>
												</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								<%@include file="/WEB-INF/include/form/areaInfo.jsp"%>
								<br>
								<%@include file="/WEB-INF/include/form/basicInfo.jsp"%>
								<div class="form-actions">
									<div class="row">
										<div class="col-md-6">
											<div class="row">
												<div class="col-md-offset-3 col-md-8">
													<button id="save" type="submit" class="btn green">
														<spring:message code="submit" />
													</button>
													<button id="cancel" type="button" class="btn default">
														<spring:message code="cancel" />
													</button>
												</div>
											</div>
										</div>
										<div class="col-md-6"></div>
									</div>
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