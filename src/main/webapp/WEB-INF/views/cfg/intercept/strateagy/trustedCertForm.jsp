<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$("#certFileInfo,#uploadCertFile").on('click', function() {
		$("#certFileI").trigger("click");
	});
	$("#certFileI").on('change', function() {
		$("#certFileInfo").val($("#certFileI").val());
		/* certFileValidate(); */
	});
	
	$("#cfgFrom").validate({
		errorPlacement: function(error,element){
			if($(element).parents().hasClass("tagsinput")){
				$(element).parents(".col-md-6").next("div").append(error);
			}else{
				$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
			}
		},
		submitHandler: function(form){
			var certFile = $("#certFileI").val();
			
			/* if(certFile!=''){
				if(!certFileValidate()){
					 return false;
				}
				
			} */
			certFile=$("#certFileInfo").val();
			if((certFile==null || certFile=="")){
				$("div[for='certFileI']").append("<label id='level-error' class='error' for='certFileI'><spring:message code='required'></spring:message></label>");
				return false;
			}else{
				loading('onloading...');
				form.submit();
			}  
		},
		errorContainer: "#messageBox"
	});
	
});
//增加对文件后缀名验证
function certFileValidate(){
    var flag=false;    //状态，检测文件后缀用
    var arr=["pem"];
    var cFile=$("#certFileI").val();//文件的值
    //取出上传文件的扩展名
    var index=cFile.lastIndexOf(".");
    var ext = cFile.substr(index+1).toLowerCase();
    //循环比较
     for(var i=0;i<arr.length;i++)
     {
          if(ext == arr[i])
          {
           flag = true; //一旦找到合适的，立即退出循环
           break;
          }
     }
    //条件判断
    	$("div[for='certFileInfo']").empty();
     if(!flag){
     // ("文件名不合法");
        $("div[for='certFileInfo']").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
         return false;
     }
     return true;
}
</script>
</head>
<body>

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
                        <c:if test="${isAdd eq true}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${isAdd eq false}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/proxy/intercept/strateagy/trustedCertSaveOrUpdate" method="post" enctype="multipart/form-data"  class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" id="cfgId" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" id="cfgType" name="cfgType" value="PXY_OBJ_TRUSTED_CA_CERT">
                       	<input type="hidden" id="isAreaEffective" name="isAreaEffective" value="0">
                       	<input type="hidden" id="isAdd" name="isAdd" value="${isAdd}">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   			</c:if>
                   		</c:forEach>
                   		<c:forEach items="${serviceList}" var="service">
                   			<c:if test="${_cfg.functionId eq service.functionId}">
                   				<input type="hidden" name="serviceId" value="${service.serviceId}">
                   				<input type="hidden" name="action" value="${service.action}">
                   			</c:if>
                   		</c:forEach>
                          <div class="form-body">
                              <div class="row">
								    <div class="col-md-6 hidden">
								        <div class="form-group">
								            <label class="control-label col-md-3"><spring:message code="action"/></label>
								            <div class="col-md-6">
								            	<c:forEach items="${serviceList}" var="service"
																		varStatus="satus">
																		<label class="radio-inline"> <c:if
																				test="${_cfg.functionId eq service.functionId}">
																				<input type="radio" name="action"
																					serviceId="${service.serviceId }"
																					protocolId="${service.protocolId }"
																					value="${service.action }" class="required action"
																					<c:if test="${_cfg.action==service.action || (_cfg.action==null && satus.index==0)}">checked</c:if>>
																					<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
																						<c:if test="${dict.itemCode eq service.action }">
																							<spring:message code="${dict.itemValue }"/>
																						</c:if>
																					</c:forEach>
																			</c:if>
																		</label>
																	</c:forEach>
								            </div>
								            <div for="action"></div>
								       </div>
								   </div>
								</div>
								<div class="row">
								 <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="cert_name"/></label>
								            <div class="col-md-6">
								                <input class="form-control required" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
										    <div for="cfgDesc"></div>
								        </div>
								    </div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font><spring:message code="certificate" /> <spring:message code="file" /></label>
											<div class="col-md-6">
												<input id="certFileI" name="certFileI" type="file"
													style="width: 330px; display: none" />
												<div class="input-group">
													<input id="certFileInfo" name="certFileInfo" readonly="readonly"
														data-msg-required=""
														placeholder="<spring:message code="select_file"/>"
														class="required form-control"
														style="background-color: transparent" aria-required="true"
														type="text" value="${_cfg.certFile }">

													<div class="input-group-btn">
														<a id="uploadCertFile" class="btn btn-default btn-search"
															href="javascript:" style=""><i class="fa fa-search"></i></a>
													</div>
													<input id="certFile" name="certFile" type="hidden" value="${_cfg.certFile }"/>
												</div>
											</div>
											<div id="certInfo"></div>
											<div for="certFileInfo"></div>
										</div>
									</div>
									<div class="col-md-6 <c:if test="${isAdd }">hidden</c:if>">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message code="issuer"/></label>
									            <div class="col-md-6">
									            	<label style="padding-top:7px">${_cfg.issuer}</label>
									            </div>
									        </div>
									</div>
								</div>
                            <%@include file="/WEB-INF/include/form/basicInfo.jsp" %>
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