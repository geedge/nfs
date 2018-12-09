<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%> 
<script>
$(function () { 
	 var options;
	//模式动框打开之前做好配置列表
	$('#http_url_modal').on('show.bs.modal', function () {
		$("#httpUrl").find("tbody").remove();
		var checkboxes=$("#contentTable tbody tr td input.i-checks:checkbox");
		var compileIds=[],urls=[];
		$(checkboxes).filter(":checked").each(function(){
			compileIds.push($(this).attr("compileId"));
			urls.push($(this).attr("url"));
		})
	 	if(compileIds.length > 0){
	 		var httpUrlHtml="<tbody>";
	 		for ( var i = 0; i <compileIds.length; i++){
	 			httpUrlHtml=httpUrlHtml+"<tr><td></td><td></td><td></td>"
	 						+"<td></td><td></td><td></td><td></td>"
	 						+"<td></td><td></td><td></td>"
	 						+"</tr>";
			}	
	 		httpUrlHtml=httpUrlHtml+"</tbody>";
	 		$("#httpUrl").append(httpUrlHtml);
	 		for ( var i = 0; i <compileIds.length; i++){
	 			var prefixName="httpUrlList["+i+"].";
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(0).append(
	 					$("input[name='cfgDesc']").clone()
	 					.attr("name",prefixName+"cfgDesc")
	 					.removeClass("hidden")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(0).append(
	 					$("div[name='error']").clone()
	 					.attr("for",prefixName+"cfgDesc")
	 					);
	 			
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(1).append(
	 					$("input[name='cfgKeywords']").clone()
	 					.attr("name",prefixName+"cfgKeywords").attr("value",urls[i])
	 					);
	 			var url=urls[i]
 				if(url.length > 15){
 					url=url.substring(0,15)+"...";
	 			}
	 			//解决火狐title不能自动换行
	 			var count = Math.floor(urls[i].length/64);
	 			for(var j=1;j<=count;j++){
	 				urls[i]=urls[i].substring(0,j*64-1)+"\n"+urls[i].substring(j*64-1);
	 			}
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(1).attr("title",urls[i]);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(1)
	 					.append(url);
	 			
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(2).append(
	 					$("select[name='service']").clone()
	 					.attr("name",prefixName+"action")
	 					.removeClass("hidden")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(2).append(
	 					$("input[name='serviceId']").clone()
	 					.attr("name",prefixName+"serviceId")
	 					.attr("value","6")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(2).append(
	 					$("input[name='cfgType']").clone()
	 					.attr("name",prefixName+"cfgType")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(2).append(
	 					$("input[name='cfgRegionCode']").clone()
	 					.attr("name",prefixName+"cfgRegionCode")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(2).append(
	 					$("input[name='serviceId']").clone()
	 					.attr("name",prefixName+"functionId")
	 					.attr("value","6")
	 					);
	 			
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(3).append(
	 					$("select[name='doLog']").clone()
	 					.attr("name",prefixName+"doLog")
	 					.removeClass("hidden")
	 					);
	 			
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(4).append(
	 					$("select[name='exprType']").clone()
	 					.attr("name",prefixName+"exprType")
	 					.removeClass("hidden")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(5).append(
	 					$("select[name='matchMethod']").clone()
	 					.attr("name",prefixName+"matchMethod")
	 					.removeClass("hidden")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(6).append(
	 					$("input[name='isHexbin']").clone()
	 					.attr("name",prefixName+"isHexbin")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(7).append(
	 					$("input[name='requestId']").clone()
	 					.attr("name",prefixName+"requestId")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(7).append(
	 					$("input[name='classify']").clone()
	 					.attr("name",prefixName+"classify")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(7).append(
	 					$("input[name='attribute']").clone()
	 					.attr("name",prefixName+"attribute")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(7).append(
	 					$("input[name='lable']").clone()
	 					.attr("name",prefixName+"lable")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(8).append(
	 					$("input[name='isAreaEffective']").clone()
	 					.attr("name",prefixName+"isAreaEffective")
	 					);
	 			$("#httpUrl tbody tr").eq(i).find("td").eq(9).append(
	 					$("input[name='sourceCompileId']").clone()
	 					.attr("name",prefixName+"sourceCompileId")
	 					.attr("value",compileIds[i])
	 					);
	 			
	 			$("#httpUrl tr th").each(function(index){
	 				if($(this).hasClass("hidden")){
	 					$("#httpUrl tbody tr").eq(i).find("td").eq(index).addClass("hidden");
	 				}
	 			});
			}
	 		
	 		$("select").selectpicker("refresh");
	 		
	 		$("select[name$='.action']").each(function(){
	 			var action=$(this).val();
	 			var objNamePrefix=$(this).attr("name").split("action")[0];
	 			$(this).on("change", function() {
	 				action=$(this).val();
	 				if(action == 128){ //白名单
	 					$("input[name='"+objNamePrefix+"cfgType']").attr("value",$("#whiteUrlRegion").val());
	 					$("input[name='"+objNamePrefix+"cfgRegionCode']").attr("value",$("#whiteUrlRegion").attr("cfgRegionCode"));
	 				}else{
	 					$("input[name='"+objNamePrefix+"cfgType']").attr("value",$("#httpUrlRegion").val());
	 					$("input[name='"+objNamePrefix+"cfgRegionCode']").attr("value",$("#httpUrlRegion").attr("cfgRegionCode"));
	 				}
	 				if(action == 1){ //白名单
	 					$("select[name='"+objNamePrefix+"doLog']").attr("value",1);
	 					$("select[name='"+objNamePrefix+"doLog']").attr("disabled",true);
	 					$("select[name='"+objNamePrefix+"doLog']").selectpicker("refresh");
	 				}else{
	 					$("select[name='"+objNamePrefix+"doLog']").attr("disabled",false);
	 					$("select[name='"+objNamePrefix+"doLog']").selectpicker("refresh");
	 				}
					$("input[name='"+objNamePrefix+"serviceId']").attr("value",$(this).find("option:selected").attr("serviceId"));
				});
	 			if(action == 128){ //白名单
 					$("input[name='"+objNamePrefix+"cfgType']").attr("value",$("#whiteUrlRegion").val());
 					$("input[name='"+objNamePrefix+"cfgRegionCode']").attr("value",$("#whiteUrlRegion").attr("cfgRegionCode"));
 				}else{
 					$("input[name='"+objNamePrefix+"cfgType']").attr("value",$("#httpUrlRegion").val());
 					$("input[name='"+objNamePrefix+"cfgRegionCode']").attr("value",$("#httpUrlRegion").attr("cfgRegionCode"));
 				}
	 			if(action == 1){ //白名单
 					$("select[name='"+objNamePrefix+"doLog']").attr("value",1);
 					$("select[name='"+objNamePrefix+"doLog']").attr("disabled",true);
 					$("select[name='"+objNamePrefix+"doLog']").selectpicker("refresh");
 				}else{
 					$("select[name='"+objNamePrefix+"doLog']").attr("disabled",false);
 					$("select[name='"+objNamePrefix+"doLog']").selectpicker("refresh");
 				}
				$("input[name='"+objNamePrefix+"serviceId']").attr("value",$(this).find("option:selected").attr("serviceId"));
	 		});
	 		
	 		$("select[name$='.exprType']").each(function(){
	 			$(this).on("change",function(){
	 				var objNamePrefix=$(this).attr("name").split("exprType")[0];
	 				if($(this).val() == 1 ){
	 					if($("select[name='matchMethod']").find("option[value=1]").length > 0){
	 						options = $("select[name='matchMethod']").find("option[value=0]");
	 					 }else{
	 						options = $("select[name='matchMethod']").find("option");
	 					}
	 				}else{
	 					options = $("select[name='matchMethod']").find("option");
	 				}
	 				switchExprType(objNamePrefix,options);
		 		}); 
	 			var objNamePrefix=$(this).attr("name").split("exprType")[0];
	 			if($(this).val() == 1 ){
 					if($("select[name='matchMethod']").find("option[value=1]").length > 0){
 						options = $("select[name='matchMethod']").find("option[value=0]");
 					 }else{
 						options = $("select[name='matchMethod']").find("option");
 					}
 				}else{
 					options = $("select[name='matchMethod']").find("option");
 				}
	 			switchExprType(objNamePrefix,options);
	 		});
	 		
	 	}
    });
	
	
	$("#httpUrlForm").validate(
			{
				errorPlacement : function(error, element) {
						$(element).parents("td").find(
								"div[for='"
										+ element.attr("name")
										+ "']").append(error);
				},
				submitHandler : function(form) {
						loading('onloading...');
						form.submit();
						//移除顶部选中菜单
						window.parent.$(".hor-menu").find("li[class='active']").removeClass("active");
						//选中配置菜单
						window.parent.$(".hor-menu").find("a[id='86']").parent("li").addClass("active");
						/* window.parent.$(".page-container").find("ul[id='menu_152']").addClass("hide");
						window.parent.$(".page-container").find("ul[id='menu_86']").removeClass("hide");
						window.parent.$(".page-container").find("li[id='menu_1102']").addClass("active");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").attr("style","display:block");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").prev("a").find(".arrow").addClass("open");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").parent("li[class='nav-item']").prev("a").addClass("open").addClass("active");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").parent("li[class='nav-item']").parent("ul[class='sub-menu']").attr("style","display:block");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").parent("li[class='nav-item']").parent("ul[class='sub-menu']").prev("a").find(".arrow").addClass("open");
						window.parent.$(".page-container").find("li[id='menu_1102']").parent("ul[class='sub-menu']").parent("li[class='nav-item']").parent("ul[class='sub-menu']").parent("li[class='nav-item']").prev("a").addClass("open").addClass("active"); */
						window.parent.$("#menu_86").removeClass("hide");
						window.parent.$("#menu_152").addClass("hide");
						var left_menu_lis = window.parent.$("#menu_86").find("li");
						if(!(left_menu_lis.hasClass("active") ||
								left_menu_lis.hasClass("open"))) {
							 var $li1 = window.parent.$("#menu_86").find("li:first");
							 var $li2 = window.parent.$("#menu_86").find("li:first").find("li:first");
							
							 var hasSubMenu1 =  $li1.children().hasClass('sub-menu');
							 if(hasSubMenu1) {
								 $li1.addClass('open');
								 $li1.find('> a > .arrow').addClass('open');
								 $li1.find('> .sub-menu').slideDown();

								 var hasSubMenu2 =  $li2.children().hasClass('sub-menu');
								 if(hasSubMenu2) {
									 $li2.addClass('open');
									 $li2.find('> a > .arrow').addClass('open');
									 $li2.find('> .sub-menu').slideDown();
									 
								 }
							 }
							 
						}
						window.parent.$("#menu_1102").find("a").click();
						window.parent.$("#menu_1102").addClass("active");
				},
				errorContainer : "#messageBox",
			});
 });
//与表达式时，只允许为子串匹配
var switchExprType=function (objNamePrefix,options){
	$("select[name='"+objNamePrefix+"matchMethod']").find("option").remove();
	$("select[name='"+objNamePrefix+"matchMethod']").append(options.clone());
	$("select[name='"+objNamePrefix+"matchMethod']").selectpicker("refresh");
 }
</script>
<div class="modal fade" id="http_url_modal" tabindex="-1" role="dialog" aria-labelledby="mo" aria-hidden="true">
	<form id="httpUrlForm" action="${ctx}/ntc/whitelist/saveHttpUrlCfgs" method="post" class="form-horizontal">
		<div class="modal-dialog" role="document" style="width:100%;padding-left:100px;padding-right:100px;">
			<div class="modal-content" style="width:100%;">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<spring:message code="log_to_url" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="alert alert-error hide">
						<button class="close" data-dismiss="alert"></button>
						<span></span>
					</div>
						<div for="error"></div>
						<!-- http log option -->
						<select class="hidden selectpicker select2 form-control required input-small" name="doLog">
							<c:forEach items="${fns:getDictList('DO_LOG')}" var="doLogC">
								<option value="${doLogC.itemCode}" 
									<c:if test="${doLogC.itemCode eq 1}">
									selected
									</c:if>
								>
								<spring:message code="${doLogC.itemValue}"/>
								</option>
						</c:forEach>
						</select>
						<!-- http action info -->
						<select class="hidden selectpicker select2 form-control required input-small" name="service">
							<c:forEach items="${fns:getFunctionServiceDictList(6) }" var="service">
									<c:forEach items="${fns:getDictList('SERVICE_ACTION')}" var="dict">
											<c:if test="${dict.itemCode eq service.action}">
												<option value="${service.action}"
														serviceId="${service.serviceId }"
														functionId="${service.functionId }"
														protocolId="${service.protocolId }"
														regionCode="${service.regionCode }"
														<c:if test="${service.action eq 1}">
														selected
														</c:if>
														>
													<spring:message code="${dict.itemValue}"/>
												</option>
											</c:if>
									</c:forEach>
							</c:forEach>
						</select>
						<!-- http region info -->
						<c:forEach items="${fns:getFunctionRegionDictList(6) }" var="region">
							<c:if test="${region.configServiceType eq 'white_url'}">
								<input type='text' class='form-control hidden' name='whiteUrlRegion'  id='whiteUrlRegion' 
																							value="${region.configRegionValue }"
																							cfgRegionCode="${region.cfgRegionCode }"
																							>
							</c:if>
							<c:if test="${region.configServiceType eq 'http_url'}">
								<input type="hidden" name="cfgTypeInfo" id="region" 
												 value="${region.configRegionValue }"
												 configRegionValue="${region.configRegionValue }" 
							                     configServiceType="${region.configServiceType}"
												 configExprType="${region.configExprType }"
												 configMatchMethod="${region.configMatchMethod }"
												 configHex="${region.configHex }"
												 >
				    			<input type='text' class='form-control hidden' name='httpUrlRegion'  id='httpUrlRegion'
				    																 value="${region.configRegionValue }"
				    																 cfgRegionCode="${region.cfgRegionCode }">
								<input class='form-control hidden input-small'  type='text' name="cfgDesc" value="">
				    			<input type='text' class='form-control hidden' name='cfgKeywords' value="">
				    			<input type='text' class='form-control hidden' name='cfgType' value="">
				    			<input type='text' class='form-control hidden' name='functionId' value="6">
				    			<input type='text' class='form-control hidden' name='serviceId' value="">
				    			<input type='text' class='form-control hidden' name='requestId' value="0">
				    			<input type='text' class='form-control hidden' name='isAreaEffective' value="0">
				    			<input type='text' class='form-control hidden' name='sourceCompileId' value="">
				    			<input type='text'class="hidden"  name='isHexbin' value="0">
				    			<input type="hidden" name="classify" value="0"/>
								<input type="hidden" name="attribute" value="0"/>
								<input type="hidden" name="lable" value="0"/>
				    			<select name="exprType" class="hidden selectpicker select2 form-control required input-small">
								<c:if test="${!empty region.configExprType}">
									<c:forEach var="exprType" items="${fn:split(region.configExprType,',')}">
										<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC">
											<c:if test="${exprTypeC.itemCode eq exprType}">
												<option value="${exprTypeC.itemCode}" 
												<c:if test="${exprTypeC.itemCode eq 0}">
												selected
												</c:if>
												>
												<spring:message code="${exprTypeC.itemValue}"/>
												</option>
											</c:if>
										
										</c:forEach> 
									</c:forEach>
								</c:if>
								<c:if test="${empty region.configExprType}">
									<c:forEach items="${fns:getDictList('EXPRESSION_TYPE')}" var="exprTypeC">
											<c:if test="${exprTypeC.itemCode eq 0}">
												<option value="${exprTypeC.itemCode}" 
												selected
												>
												<spring:message code="${exprTypeC.itemValue}"/>
												</option>
											</c:if>
										</c:forEach> 
								</c:if>
							</select>
				    			<select name="matchMethod" class="hidden selectpicker select2 form-control required input-small">
								<c:if test="${!empty region.configMatchMethod}">
									<c:forEach var="matchMethod" items="${fn:split(region.configMatchMethod,',')}">
										<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
											<c:if test="${matchMethodC.itemCode eq matchMethod}">
												<option value="${matchMethodC.itemCode}" 
												<c:if test="${matchMethod eq 0}">
												selected
												</c:if>
												>
												<spring:message code="${matchMethodC.itemValue}"/>
												</option>
											</c:if>
										
										</c:forEach> 
									</c:forEach>
								</c:if>
								<c:if test="${empty region.configMatchMethod}">
									<c:forEach items="${fns:getDictList('MATCH_METHOD')}" var="matchMethodC">
											<c:if test="${matchMethodC.itemCode eq 0}">
												<option value="${matchMethodC.itemCode}" 
												selected
												>
												<spring:message code="${matchMethodC.itemValue}"/>
												</option>
											</c:if>
										</c:forEach> 
								</c:if>
							</select>
								
							</c:if>
						</c:forEach>
						<div style="overflow-x:auto;overflow-y:auto;margin:10px;max-height:450px;min-height:200px" >
							<table id="httpUrl" class="table table-striped table-bordered table-condensed text-nowrap">
								<thead>
									<tr>
										<th class="config_describe"><spring:message code="config_describe"/></th>
										<th class="cfg_keywords"><spring:message code="url"/></th>
										<th calss="block_type"><spring:message code="block_type"/></th>
										<th calss="do_log"><spring:message code="do_log"/></th>
										<th calss="expression_type"><spring:message code="expression_type"/></th>
										<th calss="match_method"><spring:message code="match_method"/></th>
										<th class="whether_hexbinary hidden"><spring:message code="whether_hexbinary"/></th>
										<th class="request_id hidden"><spring:message code="request_id"/></th>
										<th class="is_area_effective hidden"><spring:message code="is_area_effective"/></th>
										<th class="source_compile_id hidden"><spring:message code="source_compile_id"/></th>
									</tr>
								</thead>
							</table>
						</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn red">
						<spring:message code="ok" />
					</button>
					<button type="button" class="btn" data-dismiss="modal">
						<spring:message code="close" />
					</button>
				</div>
			</div>
		</div>
	</form>
</div>