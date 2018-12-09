<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script type="text/javascript">
$(function(){
	var ifSubmit=true;
	var sfd = $("#functionId").val();
	var fileType=[];
	if(sfd==28||sfd==630){
		fileType=["mp4","flv","asf","wmv","avi","mpeg","mov","dat","m4v","m4p","m4b","webm","ogv","wav","mp3"];
	}
	if(sfd==29){
		fileType=["mp4","flv","asf","wmv","avi","mpeg","mov","dat","m4v","m4p","m4b","webm","ogv"];
	}
	if(sfd==30||sfd==632||sfd==631){
		fileType=["bmp","jpg","tiff","raw","gif"];
	}
	if(sfd==31){
		fileType=["mp4","flv","asf","wmv","avi","mpeg","mov","dat","m4v","m4p","m4b","webm","ogv","wav","mp3"];
	}
	$("#fileNoteInfo").attr("title","<spring:message code='select_file'/>: "+fileType);
	switchIpType($("select[name$='ipType']"));
	areaControlInit();
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
	$("select[name$='ipType']").on("change",function(){
		switchIpType($(this));
	});
	$("#uploadSrc,#srcFileInfo").on('click',function(){
		$("#srcFile").trigger("click");
		
	});
	
	$("#srcFile").on('change',function(){
		if(sfd!=632&&sampleFileValidate(fileType,$("#srcFile").val())){
			//样例文件类型正确进行后续处理
			$("#srcFileInfo").val($("#srcFile").val());
			var videoToPicture = $("#videoToPicture").val();
			if(videoToPicture=="true"){
				
		    	var fd = new FormData($('#cfgFrom')[0]);
		    	$.ajax({
		    		url: "${ctx}/ntc/av/sample/videoToPicture",
		    		type: "POST",
		    		processData: false,
		    		contentType: false,
		    		data: fd,
		    		async: true,  
		            cache: false, 
		            beforeSend: function(){
		            	loading('onloading...');
		            },
		    		success: function(data) {
		    			top.$.jBox.closeTip();
		    			if(data.status==1){
		    				$("#picPath").val(data.picFilePath);
		    				$("#srcPath").val(data.srcPath);
		    				$("#showPicture").removeClass("hidden");
			    			var url = "${ctx}/ntc/av/sample/selectVedioPicture?picFilePath="+encodeURIComponent(data.picFilePath);
			    			//openPicWindow(url);
		    			}else{
// 		    				alert(data.msg);
		    				top.$.jBox.tip(data.msg, "<spring:message code='info'/>");
		    				return;
		    			}
		    			
		    		},
					error:function(jqXHR, textStatus, errorThrown){ 
						top.$.jBox.closeTip();
						//alert(jqXHR.responseText);
						top.$.jBox(jqXHR.responseText,{ 
							width: $(document).width()*0.9,
							height:$(document).height()*0.8,
							top: '10%',
						    draggable:false,
							title: "Error Info", buttons:{ "close":true} 
						});
						return false;
					} 
		    	});
			}
		}
			// 人脸识别
			if(sfd==632){
				 var files = $(this)[0].files;
				 if(files.length>0){
					 for(var i = 0;i < files.length;i ++) {
			                var file = files[i];
			                var faceName = file.name;
			              var vals=sampleFileValidate(fileType,faceName)
			              if (!vals){
			            	  ifSubmit=false;
			            	  return;
			              }else{
			            	  ifSubmit=true;
			              }
					 }
				 }else{
					 ifSubmit=false;
				 }
				if(ifSubmit){
				$("#srcFileInfo").val($("#srcFile").val());
				var srcFacePath =$("#srcPath").val();
				var faceFilePath =$("#faceFilePath").val();
				var fd = new FormData($('#cfgFrom')[0]);
		   		$.ajax({
		    		url: "${ctx}/ntc/av/sample/faceToPicture?faceFilePath="+encodeURIComponent(faceFilePath),
		    		type: "POST",
		    		processData: false,
		    		contentType: false,
		    		data: fd,
		    		async: true,  
		            cache: false, 
		            beforeSend: function(){
		            	loading('onloading...');
		            },
		    		success: function(data) {
		    			top.$.jBox.closeTip();
			    			$.jBox.close();
		    			if(data.status==1){
		    				$("#picPath").val(data.picFilePath);
		    				$("#srcPath").val(data.srcPath);
		    				$("#faceFilePath").val(data.faceFilePath);
		    				$("#showPicture").removeClass("hidden");
			    			var url = "${ctx}/ntc/av/sample/selectVedioPicture?picFilePath="+encodeURIComponent(data.picFilePath);
			    			//openPicWindow(url);
			    			if($.jBox.getBox().length>0){
			    				openPicWindow(url);
			    			}
		    			}else{
		    				top.$.jBox.tip(data.msg, "<spring:message code='info'/>");
		    				return;
		    			}
		    			
		    		},
					error:function(jqXHR, textStatus, errorThrown){ 
						top.$.jBox.closeTip();
						$.jBox.close();
						//alert(jqXHR.responseText);
						top.$.jBox(jqXHR.responseText,{ 
							width: $(document).width()*0.9,
							height:$(document).height()*0.8,
							top: '10%',
						    draggable:false,
							title: "Error Info", buttons:{ "close":true} 
						});
						return false;
					} 
		    	});
				}
			}
	});
	/* $("#uploadSample,#sampleFileInfo").on('click',function(){
		$("#sampleFile").trigger("click");
	});
	$("#sampleFile").on('change',function(){
		$("#sampleFileInfo").val($("#sampleFile").val());
	}); */
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
	
	hiddenlevel();
	$(".action").on("change",function(){
		$("#serviceId").val($(this).attr("serviceId"));
		hiddenlevel();
	});
	$("#serviceId").val($(".action:checked").attr("serviceId"));
	$("#cfgFrom").validate({		
		submitHandler: function(form){
			var srcFile = $("#srcFile").val();
			
			if(srcFile!=''){
				if(!ifSubmit){
					$("#fileErrorNote").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
					 return false;
				}else{
					if(!sampleFileValidate(fileType,srcFile)){
						return false;
					}
				}
				
			}
			//var sampleFile = $("#sampleFile").val();
			var srcUrl = $("#srcUrl").val();
			var srcPath = $("#srcPath").val();
			//var sampleUrl = $("#sampleUrl").val();
			var videoToPicture = $("#videoToPicture").val();
			if(videoToPicture=="true"&&sfd!=632){
				if((srcUrl==null||srcUrl=="") && (srcPath==null || srcPath=="")){//srcUrl判断修改时未重新上传文件，srcPath判断修改时重新上传文件但未选择关键帧图片
					$("div[for='srcFileInfo']").append("<label id='level-error' class='error' for='srcFile'><spring:message code='keyframe_pic_required'></spring:message></label>");
					return false;
				}else if(srcFile!=null && srcFile!=""){//选择视频文件
					if(srcPath==null || srcPath==""){//单位选择关键帧图片
						$("div[for='srcFileInfo']").append("<label id='level-error' class='error' for='srcFile'><spring:message code='keyframe_pic_required'></spring:message></label>");
						return false;
					}
				}
			}else{
				if((srcUrl==null||srcUrl=="") && (srcFile==null || srcFile=="")){
					$("div[for='srcFileInfo']").append("<label id='level-error' class='error' for='srcFile'><spring:message code='required'></spring:message></label>");
					return false;
				}
			}
			loading('onloading...');
			//如果是新增配置，则校验是否配置量已达上限
			var flag = true;
			if($("input[name='cfgId']").val()==""){
				var serviceId = $("#serviceId").val();
				flag = validateEffectiveRuleLimit("${ctx}",serviceId);
			}
			if(flag){
				form.submit();
			}
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error,element){
			$(element).parents(".form-group").find("div[for='"+element.attr("name")+"']").append(error);
		},
	});
	$("#showPicture").on('click',function(){
		var srcPath = $("#srcPath").val()
		var url = "${ctx}/ntc/av/sample/selectVedioPicture?picFilePath="+encodeURIComponent($("#picPath").val())+"&srcPath="+encodeURIComponent(srcPath);
		openPicWindow(url);
	})
});

function openPicWindow(url){
	$.jBox("iframe:"+url, {
		title: "",
	    top: '1%',
	    draggable:false,
	    width: $(document).width()*0.9,
	    height:$(document).height()*0.9,
	    buttons: { 'close': true }
	});
}

function hiddenlevel(){
	var action=$("input[name='action']:checked").val();
	if(action==16){
		$(".level").addClass("hidden");
		$("select[name='level']").find("option").eq(0).attr("value","0");
		$("select[name='level']").val("0");
		$("select[name='level']").selectpicker("refresh");
	}else {
		$(".level").removeClass("hidden");
		$("select[name='level']").find("option").eq(0).attr("value","");
		$("select[name='level']").val("100");
		$("select[name='level']").selectpicker("refresh");
	}
}

// 校验文件后缀名
function sampleFileValidate(fileType,fileName){
    var flag=false;    //状态，检测文件后缀用
    var arr=fileType;//使用是什么格式的后缀
    var cFile=fileName;//文件的值
    //取出上传文件的扩展名
    var index=cFile.lastIndexOf(".");
    var ext = cFile.substr(index+1).toLowerCase();
    //循环比较
     for(var i=0;i<arr.length;i++){
          if(ext == arr[i]){
           flag = true; //一旦找到合适的，立即退出循环
           break;
          }
     }
    //条件判断
     	$("#fileErrorNote").empty();
     if(!flag){
     // ("文件名不合法");
        $("#fileErrorNote").append("<label id='level-error' class='error'><spring:message code='file_in_wrong_format'/></label>");
        $.jBox.close();
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
                        <c:if test="${empty _cfg.cfgId}"><spring:message code="add"></spring:message></c:if>
						<c:if test="${not empty _cfg.cfgId}"><spring:message code="edit"></spring:message></c:if>
					</div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form id="cfgFrom" action="${ctx}/ntc/av/sample/saveFileSample" enctype="multipart/form-data" method="post" class="form-horizontal">
                      	<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
                       	<input type="hidden" name="compileId" value="${_cfg.compileId}">
                       	<input type="hidden" name="functionId" id="functionId" value="${_cfg.functionId}">
                       	<input type="hidden" id="serviceId" name="serviceId" value="${_cfg.serviceId}">
                       	<input type="hidden" name="picPath" id="picPath" value="">
                       	<!-- 配置域类型 -->
                   		<c:forEach items="${regionList}" var="region">
                   			<c:if test="${_cfg.functionId eq region.functionId}">
                   				<input type="hidden" name="cfgType" value="${region.configRegionValue}">
                   				<input type="hidden" name="cfgRegionCode" value="${region.configRegionCode}">
                   			</c:if>
                   		</c:forEach>
                          <div class="form-body">
                              <div class="row">
							    <div class="col-md-6">
							        <div class="form-group">
							            <label class="control-label col-md-3"><spring:message code="config_describe"/></label>
							            <div class="col-md-6">
							                <input class="form-control" type="text" name="cfgDesc" value="${_cfg.cfgDesc}">
							            </div>
							        </div>
							    </div>
							    
							        <div class="col-md-6">
							        <div class="form-group">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="action"/></label>
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
							        <div class="form-group ">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="src_file"/></label>
							            <div class="col-md-6">
							            	 <%-- <input class="form-control required" type="text" name="srcUrl" value="${_cfg.srcUrl }"> --%>
							            	 <c:if test="${_cfg.functionId != 632}">
							            		 <input id="srcFile" type="file" name="srcFile" style="width: 330px;display:none" />
							            	 </c:if>
							            	 <c:set var="videoToPicture" value="${fns:getDictLabel('VIDEO_TO_PICTURE', _cfg.functionId, 'false')}" />
							            	 <input type="hidden" name="videoToPicture" id="videoToPicture" value="${videoToPicture }">
							            	 <c:choose>
							            	 		
							            	 		<c:when test="${videoToPicture and _cfg.functionId != 632}">
							            	 			<input id="srcPath" name="srcPath" type="hidden" style="width: 330px;" />
							            	 			<div class="input-group">
								            	 			<input id="srcFileInfo" name="srcFileInfo" readonly="readonly" data-msg-required="" 
											            	 placeholder="<spring:message code="select_file"/>" class="required form-control" style=" background-color:transparent" 
											            	 aria-required="true" type="text" value="${_cfg.srcUrl }">
											            	 
											            	 <div class="input-group-btn">
													             <a id="uploadSrc" class="btn btn-default btn-search " href="javascript:" style=""><i class="fa fa-search"></i></a>
													         </div>
													        
												        </div>
												        <input id="srcUrl" name="srcUrl" type="hidden" value="${_cfg.srcUrl }"/>
															<button type="button" id="showPicture" class="btn btn-warning hidden ">
																<i class="fa btn-search"></i>
															<spring:message code="show"></spring:message></button>
							            	 		</c:when>
							            	 		
							            	 		<c:when test="${_cfg.functionId eq 632}">
							            	 			<input id="srcFile" type="file" name="srcFile" style="width: 330px;display:none" multiple/>
							            	 			<input id="srcPath" name="srcPath" type="hidden" style="width: 330px;"/>
							            	 			<input id="faceFilePath" name="faceFilePath" type="hidden" value="${faceFilePath}"/>
							            	 			<div class="input-group">
								            	 			<input id="srcFileInfo" name="srcFileInfo" readonly="readonly" data-msg-required=""
											            	 placeholder="<spring:message code="select_file"/>" class="required form-control" style=" background-color:transparent" 
											            	 aria-required="true" type="text" value="${_cfg.srcUrl }">
											            	 
											            	 <div class="input-group-btn">
													             <a id="uploadSrc" class="btn btn-default btn-search " href="javascript:" style=""><i class="fa fa-search"></i></a>
													         </div>
													        
												        </div>
												        <input id="srcUrl" name="srcUrl" type="hidden" value="${_cfg.srcUrl }"/>
															<button type="button" id="showPicture" class="btn btn-warning hidden ">
																<i class="fa btn-search"></i>
															<spring:message code="show"></spring:message></button>
							            	 		</c:when>
							            	 		
							            	 		<c:otherwise>
								            	 		<div class="input-group">
								            	 			<input id="srcFileInfo" name="srcFileInfo" readonly="readonly" data-msg-required="" 
											            	 placeholder="<spring:message code="select_file"/>" class="required form-control" style=" background-color:transparent" 
											            	 aria-required="true" type="text" value="${_cfg.srcUrl }">
											            	 
											            	 <div class="input-group-btn">
													             <a id="uploadSrc" class="btn btn-default btn-search" href="javascript:" style=""><i class="fa fa-search"></i></a>
													        </div>
													    <%-- <span id="srcFileInfo" style="margin-top:20px;margin-left:10px;">${_cfg.srcUrl }</span> --%>
										        		</div>
										        		<input id="srcUrl" name="srcUrl" type="hidden" value="${_cfg.srcUrl }"/>
							            	 		</c:otherwise>
							            	 	</c:choose>
							            </div>
							            &nbsp;<span id="fileNoteInfo" title="<spring:message code="select_file"/>" 
							            class="fileNoteInfo" data-icon="&#xe08b;" style="cursor:default"></span>
							            <div for="srcFileInfo"></div>
							            <div id="fileErrorNote"></div>
							        </div>
							    </div>
							    <%-- <div class="col-md-6">
							        <div class="form-group">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="sample_file"/></label>
							            <div class="col-md-6">
							                <input class="form-control required" type="text" name="sampleUrl" value="${_cfg.sampleUrl}">
							                <input id="sampleFile" name="sampleFile" type="file"style="width: 330px;display:none"  />
							                <div class="input-group">
								            	 <input id="sampleFileInfo" name="sampleFileInfo" readonly="readonly" data-msg-required="" 
								            	 placeholder="<spring:message code="select_file"/>" class="required form-control" style=" background-color:transparent" 
								            	 aria-required="true" type="text" value="${_cfg.sampleUrl }">
								            	 
								            	 <div class="input-group-btn">
										             <a id="uploadSample" class="btn btn-default btn-search" href="javascript:" style=""><i class="fa fa-search"></i></a>
										        </div>
										        <span id="srcFileInfo" style="margin-top:20px;margin-left:10px;">${_cfg.srcUrl }</span>
									        </div>
							                <button id="uploadSample" type="button" class="btn btn-default btn-search"><spring:message code="select_file"/></button>
							            	 <span id="sampleFileInfo">${_cfg.sampleUrl }</span>
							                <input id="sampleUrl" name="sampleUrl" type="hidden" value="${_cfg.sampleUrl }" />
							            </div>
							            <div for="sampleFileInfo"></div>
							        </div>
							    </div> --%>
								 <div class="col-md-6 level"  >
							        <div class="form-group ">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="harm_level"/></label>
							            <div class="col-md-6">
							            	 <select name="level" data-live-search="true" class="selectpicker form-control required">
													<option value="0" ><spring:message code="selected"/></option>
													<c:forEach items="${fns:getDictList('CONFIDENCE_INTERVAL')}" var="configdenceC">
														<option value="${configdenceC.itemCode}" <c:if test="${_cfg.level==configdenceC.itemCode || (_cfg.level==null && configdenceC.itemCode eq 100)}">selected</c:if>><spring:message code="${configdenceC.itemValue}"/></option>
													</c:forEach>
												</select>
							            </div>
							            <div for="level"></div>
							        </div>
							    </div>
							</div>
							<%-- <div class="row">
							    <div class="col-md-6">
							        <div class="form-group">
							            <label class="control-label col-md-3"><font color="red">*</font><spring:message code="action"/></label>
							            <div class="col-md-6">
							            	<c:forEach items="${serviceList}" var="service">
							                	<label class="radio-inline">
							                		<c:if test="${_cfg.functionId eq service.functionId}">
							                			<input type="radio" name="action" serviceId="${service.serviceId }" value="${service.action }" class="required action"
									            			<c:if test="${_cfg.action==service.action || _cfg.action==null}">checked</c:if>>
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
							</div> --%>
                           <%--  <%@include file="/WEB-INF/include/form/areaInfo.jsp" %> --%>
                           <input type="hidden" name="isAreaEffective" value="0">
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