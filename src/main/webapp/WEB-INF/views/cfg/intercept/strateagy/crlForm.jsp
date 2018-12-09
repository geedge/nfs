<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%> 
<<script type="text/javascript">
$(function(){
	$("#crlFileInfo,#uploadCrlFile").on('click', function() {
		$("#crlFileI").trigger("click");
	});
	$("#crlFileI").on('change', function() {
		$("#crlFileInfo").val($("#crlFileI").val());
		/* crlFileValidate(); */
	});
});

//增加对文件后缀名验证
function crlFileValidate(){
    var flag=false;    //状态，检测文件后缀用
    var arr=["crl"];
    var cFile=$("#crlFileI").val();//文件的值
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
   	$("div[for='crlFileInfo']").empty();
   	$(".alert-error").addClass("hide");
     if(!flag){
     	// ("文件名不合法");
     	$("div[for='crlFileInfo']").empty();
   		$(".alert-error").removeClass("hide");
        $("div[for='crlFileInfo']").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
         return false;
     }
     return true;
}

var submitCrlFrom=function(){
	var crlFile = $("#crlFileI").val();
	/* if(crlFile!=''){
		if(!crlFileValidate()){
			 return false;
		}
	} */
	crlFile=$("#crlFileInfo").val();
	if((crlFile==null || crlFile=="")){
		$("div[for='crlFileInfo']").empty();
   		$(".alert-error").removeClass("hide");
        $("div[for='crlFileInfo']").append("<label id='level-error' class='error' for='crlFileI'><spring:message code='required'/></label>");
		return false;
	}else{
		$("div[for='crlFileInfo']").empty();
	   	$(".alert-error").addClass("hide");
		loading('onloading...');
		$("#crlForm").submit();
	}  
}
</script>
<div class="modal fade" id="add_crl_modal" tabindex="-1" role="dialog" aria-labelledby="mo" aria-hidden="true">
	<form id="crlForm" action="${ctx}/proxy/intercept/strateagy/addOrAuditCrl?importPath=${importPath}" method="post" enctype="multipart/form-data" class="form-horizontal"
		onsubmit="loading('<spring:message code='loading'/>');">
		<input type="hidden"  id="crlTip" value="<spring:message code='crl_tip'/>">
		
		<div class="modal-dialog" role="document" style="width:700px;">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<spring:message code="add_crl_file" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="alert alert-error hide">
						<!-- <button class="close" data-dismiss="alert"></button> -->
						<span><div for="crlFileInfo"></div></span>
					</div>
						<!-- 同主表cert配置信息 -->
						<input type="hidden" name="cfgDesc" value="">
						<input type="hidden" name="certId" value="">
						<input type="hidden" name="action" value="1">
						<input type="hidden" name="isValid" value="0">
						<input type="hidden" name="isAudit" value="0">
						<input type="hidden" name="functionId" value="${cfg.functionId }">
						<input type="hidden" name="serviceId" value="641">
						<input type="hidden" name="cfgType" value="PXY_OBJ_TRUSTED_CA_CRL">
						<input type="hidden" name="cfgRegionCode" value="">
						<input type="hidden" name="requestId" value="0">
						<input type="hidden" name="isAreaEffective" value="0">
						<input type="hidden" name="areaEffectiveIds" value="0">
						<input type="hidden" name="classify" value="0"/>
						<input type="hidden" name="attribute" value="0"/>
						<input type="hidden" name="lable" value="0"/>
						<input type="hidden" name="issuer" value=""/>
						<input type="hidden" name="cancelRequestId" value=""/>
				 		<!-- $(this).attr("crlFile"); -->
						
						</br>
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label col-md-3"><font
									color="red">*</font><spring:message code="CRL" /> <spring:message code="file" /></label>
								<div class="col-md-6">
									<input id="crlFileI" name="crlFileI" type="file"
										style="width: 330px; display: none" />
									<div class="input-group">
										<input id="crlFileInfo" name="crlFileInfo" readonly="readonly"
											data-msg-required=""
											placeholder="<spring:message code="select_file"/>"
											class="required form-control"
											style="background-color: transparent" aria-required="true"
											type="text" value="">

										<div class="input-group-btn">
											<a id="uploadCrlFile" class="btn btn-default btn-search"
												href="javascript:" style=""><i class="fa fa-search"></i></a>
										</div>
										<input id="crlFile" name="crlFile" type="hidden" value=""/>
									</div>
								</div>
								<div id="certInfo"></div>
							</div>
						</div>
				</div>
					<div class="modal-footer" style="border-top:0px">
					<button type="button" class="btn red" onclick="submitCrlFrom()">
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