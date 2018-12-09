<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	$("#privateKeyFileInfo,#uploadPrivateKeyFile").on('click', function() {
		$("#privateKeyFileI").trigger("click");
	});
	$("#publicKeyFileInfo,#uploadPublicKeyFile").on('click', function() {
		$("#publicKeyFileI").trigger("click");
	});
	$("#publicKeyFileI").on('change', function() {
		$("#publicKeyFileInfo").val($("#publicKeyFileI").val());
		publicFileValidate();
	});
	$("#privateKeyFileI").on('change', function() {
		$("#privateKeyFileInfo").val($("#privateKeyFileI").val());
		privateFileValidate();
	});
	switchKeyringType();
	$("select[name=keyringType]").on('change', function() {
		switchKeyringType();
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
			
			
			var publicFile = $("#publicKeyFileI").val();
			var privateFile = $("#privateKeyFileI").val();
			var publicKeyFile = $("#publicKeyFile").val();
			var privateKeyFile = $("#privateKeyFile").val();
			
			if(publicFile!=''){
				if(!publicFileValidate()){
					 return false;
				}
				
			}
			if(privateFile!=''){
				if(!privateFileValidate()){
					 return false;
				}
			}
			
			if((publicFile==null||publicFile=="") && (publicKeyFile==null || publicKeyFile=="")){
				$("div[for='publicKeyFileI']").append("<label id='level-error' class='error' for='publicKeyFileI'><spring:message code='required'></spring:message></label>");
				return false;
			}else if((privateFile==null || privateFile=="") && (privateKeyFile==null || privateKeyFile=="")){
				$("div[for='privateKeyFileI']").append("<label id='level-error' class='error' for='privateKeyFileI'><spring:message code='required'></spring:message></label>");
				return false;
			}else{
				loading('onloading...');
				form.submit();
			}
		},
		errorContainer: "#messageBox"
	});
	
});
var switchCfgType=function(){
	var cfgType=$("input[name='cfgType']:checked").val()
	if(cfgType =='ip'){ //不允许选择实体证书
		$("select[name=keyringType]").find("option[value='end-entity']").attr("disabled",true);
		$("select[name=keyringType]").selectpicker("refresh");
	}else{
		$("select[name=keyringType]").find("option[value='end-entity']").removeAttr("disabled");
		$("select[name=keyringType]").selectpicker("refresh");
	}
}
var switchKeyringType=function(){
	var keyringType=$("select[name=keyringType]").val();
	var expireAfter=$("input[name=expireAfter]").val();
	if(keyringType!=''){
		if(keyringType=='root'){
			$("#publicInfo").empty();
			$("#publicInfo").append("&nbsp;&nbsp;<label ><i class='fa fa-info-circle'></i>&nbsp;<spring:message code='root_certificate'/></label>");
		}else{
			$("#publicInfo").empty();
			$("#publicInfo").append("&nbsp;&nbsp;<label ><i class='fa fa-info-circle'></i>&nbsp;<spring:message code='intermediate_entity_certificate'/></label>");
		}
	}else{
		$("#publicInfo").empty();
// 		$("#publicInfo").append("&nbsp;&nbsp;<label style='color:#ea5200'><i class='fa fa-info-circle'></i>&nbsp;<spring:message code='select_certificate'/></label>");
	}
	
	
	if(keyringType =='end-entity'){ //实体证书不允许输入，并且默认0
		$("input[name=expireAfter]").val(0);
		$(".expireAfter").addClass("hidden");
	}else{
		if(expireAfter == ''){
			$("input[name=expireAfter]").val(30);
		}
		$(".expireAfter").removeClass("hidden");
	}
}

//增加对文件后缀名验证
function publicFileValidate(){
	var keyringType=$("select[name=keyringType]").val();
    var flag=false;    //状态，检测文件后缀用
    	var arr=["der","cer","crt","pem","p12"];//使用是什么格式的后缀der、cer、crt、pem
    if(keyringType=='root'){
   		arr=["der","cer","crt","pem"];
    }else{
    	arr=["p12"];
    }
    var cFile=$("#publicKeyFileI").val();//文件的值
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
     	$("div[for='publicKeyFileInfo']").empty();
     if(!flag){
     // ("文件名不合法");
        $("div[for='publicKeyFileInfo']").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
         return false;
     }
     return true;
}
function privateFileValidate(){
    var flag=false;    //状态，检测文件后缀用
    var arr=["key","p12","pem"];//使用是什么格式的后缀pem、key、p12
    var cFile=$("#privateKeyFileI").val();//文件的值
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
     	$("div[for='privateKeyFileInfo']").empty();
     if(!flag){
     // ("文件名不合法");
        $("div[for='privateKeyFileInfo']").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
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
                      <form id="cfgFrom" action="${ctx}/proxy/intercept/strateagy/saveOrUpdate" method="post" enctype="multipart/form-data"  class="form-horizontal">
                       	<input type="hidden" name="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="compileId" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" id="cfgId" name="cfgId" value="${_cfg.cfgId}">
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
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="keyring_name"/></label>
								            <div class="col-md-6">
								                <input class="form-control required" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
								            </div>
										    <div for="cfgDesc"></div>
								        </div>
								    </div>
								 <div class="col-md-6">
								        <div class="form-group">
								            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="keyring_type"/></label>
								            <div class="col-md-6">
								                 <select name="keyringType" class="selectpicker show-tick form-control required">
													<option value="" <c:if test="${empty cfg.keyringType}">selected</c:if>><spring:message code="select"/></option>
													<c:forEach items="${fns:getDictList('INTERCEPT_CERTIFICATE_TYPE')}" var="keyringType">
															<option value="${keyringType.itemCode}" <c:if test="${_cfg.keyringType==keyringType.itemCode}">selected</c:if> ><spring:message code="${keyringType.itemValue}"/></option>
													</c:forEach>
													</select>
								            </div>
										    <div for="keyringType"></div>
								        </div>
								    </div>
								   
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font> <spring:message code="public_key_file" /></label>
											<div class="col-md-6">
												<input id="publicKeyFileI" name="publicKeyFileI" type="file"
													style="width: 330px; display: none" />
												<div class="input-group">
													<input id="publicKeyFileInfo" name="publicKeyFileInfo" readonly="readonly"
														data-msg-required=""
														placeholder="<spring:message code="select_file"/>"
														class="required form-control"
														style="background-color: transparent" aria-required="true"
														type="text" value="${_cfg.publicKeyFile }">

													<div class="input-group-btn">
														<a id="uploadPublicKeyFile" class="btn btn-default btn-search"
															href="javascript:" style=""><i class="fa fa-search"></i></a>
													</div>
													<input id="publicKeyFile" name="publicKeyFile" type="hidden" value="${_cfg.publicKeyFile }"/>
												</div>
											</div>
											<div id="publicInfo"></div>
											<div for="publicKeyFileInfo"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font> <spring:message code="private_key_file" /></label>
											<div class="col-md-6">
												<input id="privateKeyFileI" name="privateKeyFileI" type="file"
													style="width: 330px; display: none" />
												<div class="input-group">
													<input id="privateKeyFileInfo" name="privateKeyFileInfo" readonly="readonly"
														data-msg-required=""
														placeholder="<spring:message code="select_file"/>"
														class="required form-control"
														style="background-color: transparent" aria-required="true"
														type="text" value="${_cfg.privateKeyFile }">

													<div class="input-group-btn">
														<a id="uploadPrivateKeyFile" class="btn btn-default btn-search"
															href="javascript:" style=""><i class="fa fa-search"></i></a>
													</div>
													<input id="privateKeyFile" name="privateKeyFile" type="hidden" value="${_cfg.privateKeyFile }"/>
												</div>
											</div>
											&nbsp;&nbsp;<label ><i class='fa fa-info-circle'></i>&nbsp;<spring:message code='private_certificate'/></label>
											<div for="privateKeyFileInfo"></div>
										</div>
									</div>
								</div>
								<div class="row ">
									<div class="col-md-6 expireAfter">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="expire_after"/></label>
									            <div class="col-md-6">
									            	 <input class="form-control required number" type="text" min="1" name="expireAfter" value="${_cfg.expireAfter}">
									            </div>
											    <div for="expireAfter"></div>
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
								
								<div class="row <c:if test="${isAdd }">hidden</c:if>">
									<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message code="certificate_subject"/></label>
									            <div class="col-md-6">
									            	<label style="padding-top:7px">${_cfg.subject}</label>
									            </div>
									        </div>
									</div>
								   
									<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><spring:message code="certificate_validity"/></label>
									            <label class=" col-md-6" style="padding-top:7px">${_cfg.notBeforeTime }-${_cfg.notAfterTime }</label>
									        </div>
								    </div>
								</div>
								<div class="row hidden">
									<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="public_key_algo"/></label>
									            <div class="col-md-6">
									            	 <input class="form-control required" type="text" name="publicKeyAlgo" value="${_cfg.publicKeyAlgo}">
									            </div>
											    <div for="publicKeyAlgo"></div>
									        </div>
								    </div>
									<div class="col-md-6">
									        <div class="form-group">
									            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="crl"/></label>
									            <div class="col-md-6">
									            	 <input class="form-control required" type="text" name="crl" value="${_cfg.crl}">
									            </div>
											    <div for="crl"></div>
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