<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="agreement_ip_configuration" /></title>
<script src="${pageContext.request.contextPath}/static/global/scripts/app.js" type="text/javascript"></script>
<script>
 	$(document).ready(function() {
 		//筛选功能初始化
 		filterActionInit();
 		
		$("#direction").change(function(){
			page();
		});
		$("#protocol").change(function(){
			page();
		});
		//全选及取消
		$("#checkAll").change(function(){
			if($("#checkAll").prop("checked")){
				$("input.i-checks").prop("checked",true);
			}else{
				$("input.i-checks").prop("checked",false);
			}
		});
		$("#btnImport").click(function(){
			$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
				bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
		
		
		$("button[data-dismiss='modal']").click(function (){
			
			window.location ="${ctx}/specific/specificServiceHostCfg/list";
		})
 	});
 	function resetx(){
 		$(':input','#searchForm')
 		.not(':button,:submit,:reset,:hidden')
 		.val('')
 		.removeAttr('checked')
 		.removeAttr('selected');
 		$("select.selectpicker").each(function(){
			$(this).selectpicker('val',$(this).find('option:first').val());
			$(this).find("option").attr("selected",false);
			$(this).find("option:first").attr("selected",true);
		});
 		$("#specServiceId").attr("value","");
 		$("#srcIp").attr("value","");
 		$("#dstIp").attr("value","");
 		
 	}
 	//修改
	function cmdEdit(){
		var cked = $("tbody tr td input[name='check']:checkbox:checked");
		if(cked.length<1){
			top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			}
		if(cked.length>1){
			top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
			}
		if(cked.length==1){
			var url=arguments[0];
			confirmx("<spring:message code='confirm_message'/>", url+"?hostId="+cked.val());
			}
	}
	//删除
	function delAll(){
		var mulitId="";
		jQuery("#contentTable").find(":checkbox:checked[name='check']").each(function(){
			  if(jQuery(this).val()!=""){
			    mulitId+=jQuery(this).val()+",";
			  }
			});
			if(mulitId!=""){
				confirmx("<spring:message code='confirm_message'/>", '${ctx}/specific/specificServiceHostCfg/delete?mulitId='+mulitId);
			}else{
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			}
	}
	//查询
 	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/specific/specificServiceHostCfg/list");
		$("#searchForm").submit();
		return false;
	}
	
 	//导入文件提示框
	function toImport(){
		$("#import_modal").modal({
			backdrop:"static",
			keyboard:false,
			show:true
		});
	}
	//导入配置
	function ajaxImp(){
		
		/*var fileName = $(".fileupload-preview", $("#importForm1")).text();
	 	var $error = $('.alert-error', $("#importForm1"));
	 	if(App.isEmpty(fileName)){
	 		
	 		$("span",$error).html("请选择xls或者xlsx格式文件进行导入...");
	 		$error.removeClass("hide");
	 		$error.addClass("show");
        	return false;
        	
	 	}else if(fileName.lastIndexOf("\.")==-1||fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xls' &&fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xlsx'){
	 		$("span",$error).html("导入的文件后缀，必须为xls或者xlsx...");
	 		$error.removeClass("hide");
	 		$error.addClass("show");
        	return false;
	 	}
	 	$("#import_modal").modal('hide');//导入文件隐藏
		$("#importForm1").submit();
	 	*/
	 	
	 	$("#importForm1").submit();
	 	
		/* $("#importForm1").ajaxSubmit({
			//clearForm:true,
			resetForm:true,
			dataType:'json',
			type:'post',
			beforeSubmit:function(formData,jqForm,options){
			 	var fileName = $(".fileupload-preview", jqForm).text();
			 	var $error = $('.alert-error', jqForm);
			 	$error.hide();
			 	if(App.isEmpty(fileName)){
			 		$("span",$error).html("请选择xls或者xlsx格式文件进行导入...");
                	
			 		$error.removeClass("hide");
			 		$error.addClass("show");
                	return false;
			 	}else if(fileName.lastIndexOf("\.")==-1||fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xls' &&fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xlsx'){
			 		$("span",$error).html("导入的文件后缀，必须为xls或者xlsx...");
			 		$error.removeClass("hide");
			 		$error.addClass("show");
                	return false;
			 	}
			 	$("#import_modal").modal('hide');//导入文件隐藏
			 	
				return true;
			},
			success:function(data,statusText){
				if(statusText='success'){
					if(data!=null&&data.length>0){
						alert(data[0].errorMessage);
					
					}
				}
			}
			
		}); */
		
	}
	function downLoadXLS(){
		window.location ="${ctx}/specific/specificServiceHostCfg/import/template";
	}
</script>
</head>

<body>
<div class="modal fade" id="import_modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
<form id="importForm1" action="${ctx}/specific/specificServiceHostCfg/import/" method="post"
			enctype="multipart/form-data" class="form-horizontal"
			onsubmit="loading('<spring:message code='loading'/>');">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel"><spring:message code="import"/></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="alert alert-error hide">
			<button class="close" data-dismiss="alert"></button>
			<span></span>
		</div>
		<div class="row">
		<div class="col-md-12">
		<div class="form-group">
			<label class="col-md-2 control-label" style="margin-top:5px;"><spring:message code="chooseFile"/>：</label>
			<div class="controls">

				<div class="fileupload fileupload-new" data-provides="fileupload">

					<div class="input-append">

						<div class="uneditable-input">
							<i class="fa fa-fa fa-file"></i> 
							<span class="fileupload-preview"></span>
						  </div>
						<span class="btn btn-file ">
							<button type="button" class="btn fileupload-new"><spring:message code="add"/></button>
							<button type="button" class="btn fileupload-exists"><spring:message code="edit"/></button>
							<input type="file" class="default" id="uploadFile" name="file"/>
							<input type="hidden" name="flag" value="${flag }"/>
						</span>
						<button type="button" class="btn red fileupload-exists" data-dismiss="fileupload" style="margin-left:-1px;"><spring:message code="remove"/></a>
						<button type="button" onclick="downLoadXLS()" class="btn black" style="margin-left:-1px"><spring:message code="download"/></button>
					</div>
				</div>
			</div>
			</div>
			</div>
	  </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn red" onclick="ajaxImp()"><spring:message code="ok"/></button>
        <button type="button" class="btn" data-dismiss="modal"><spring:message code="close"/></button>
      </div>
    </div>
  </div>
  </form>
</div>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<shiro:hasPermission name="specific:serviceIp:add">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/specific/specificServiceHostCfg/form'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"/></button>
			<%--  <button type="button" class="btn btn-primary green" data-toggle="modal" data-target="#exampleModal" onClick="toImport();">
		  	<i class="fa fa-upload"></i>
		  	<spring:message code="import"/></button> --%>
		</button>	
	</shiro:hasPermission>
	</div>
	
	<h3 class="page-title">
		<spring:message code="agreement_ip_configuration" />
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
	
				<div class="portlet-body">

					<div class="row"  >
						<form:form id="searchForm" modelAttribute="specificServiceHostCfg" action="${ctx}/specific/specificServiceHostCfg/list" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${specificServiceHostCfg.isFilterAction }"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								   <div class="pull-left">
									<c:set var="spec_service_id"><spring:message code="protocol_name"/></c:set>
									 <sys:treeselect id="specServiceId" name="specServiceId" value="${specificServiceCfg.parent.specServiceId}" 
									 labelName="parent.specServiceName" 
									 labelValue="${empty specificServiceHostCfg.specServiceId?spec_service_id:fns:getBySpecServiceId(specificServiceHostCfg.specServiceId).specServiceName}"
									 title="${spec_service_id}" url="/specific/specificServiceCfg/treeData?isLeafShow=false&cfgType=1" extId=""
									 cssClass="form-control input-small"/>	
						
								   </div>
								   <div class="pull-left" style="margin-right:0px;">
										<form:select path="protocol" class="selectpicker select2 input-small" >
											<form:option value=""><spring:message code="protocol"/></form:option>
											   <c:forEach items="${fns:getDictList('SPEC_PROTOCOL')}" var="dict">
											      <form:option value="${dict.itemCode}"><spring:message code="${dict.itemValue}"/></form:option>
											   </c:forEach>
										</form:select>
								   </div>
								   <div class="pull-left">
					  			      <c:set var = "condition_dstIp"><spring:message code="server_ip" /></c:set>
									  <form:input path="destIpAddress"  placeholder="${condition_dstIp}" class="form-control input-small"/>
								   </div>
								   
								<div class="pull-left">
									  <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									  <button type="button" class="btn btn-default" onClick="resetx()"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                  <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
								</div>
									<div class="pull-right">
									<shiro:hasPermission name="specific:serviceIp:edit">
									<button type="button" class="btn btn-default" onclick="cmdEdit('${ctx}/specific/specificServiceHostCfg/form')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"></spring:message></button>
                                    </shiro:hasPermission>
                                     <shiro:hasPermission name="specific:serviceIp:del">       
                                             <button type="button" class="btn btn-default" onclick="delAll()">
                                            <i class="fa fa-trash"></i><spring:message code="delete"/></button>
                                    </shiro:hasPermission>
                                        <a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title="<spring:message code='custom_columns'/>" href="javascript:;">
                                             <i class="icon-wrench"></i>
                                         </a>
                                    </div>
							
								
							</div>
							<!-- /搜索内容与操作按钮栏-->
							
							
							
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="config_time"/>：</label>
											<input name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value='${specificServiceHostCfg.beginDate}' pattern='yyyy-MM-dd HH:mm:ss'/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceHostCfg.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="edit_time"/>：</label>
											<input name="editBeginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceHostCfg.editBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="editEndDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceHostCfg.editEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="audit_time"/>：</label>
											<input name="auditBeginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceHostCfg.auditBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="auditEndDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceHostCfg.auditEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
								</div>
							
							
							
							</div>
							
							<!-- /筛选搜索内容栏    结束-->
						
						</form:form>

					</div>
					
					
				
					
					
				<sys:message content="${message}"/>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th><spring:message code="protocol_name" /></th>
									<th><spring:message code="ip_type" /></th>
									<th><spring:message code="server_ip" /></th>
									<th><spring:message code="server_port" /></th>
									<th><spring:message code="direction" /></th>
									<th><spring:message code="protocol" /></th>
									<%-- <th><spring:message code="is_audit" /></th> --%>
									<th><spring:message code="creator" /></th>
									<th class="sort-column create_time"><spring:message code="config_time" /></th>
									<th><spring:message code="editor" /></th>
									<th class="sort-column edit_time"><spring:message code="edit_time" /></th>
									<th isVisible="false"><spring:message code="auditor" /></th>
									<th isVisible="false" class="sort-column audit_time"><spring:message code="audit_time" /></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${page.list}" var="specificServiceHostCfg">
								<tr>
									<td><input type="checkbox" class="i-checks" name="check" id="${specificServiceHostCfg.hostId}" value="${specificServiceHostCfg.hostId}"></td>
									<td>${fns:getBySpecServiceId(specificServiceHostCfg.specServiceId).specServiceName }</td>
									<td>
										<c:forEach items="${fns:getDictList('IP_TYPE')}" var="ipType">
											<c:if test="${ipType.itemCode eq specificServiceHostCfg.ipType}">${ipType.itemValue}</c:if>
						                </c:forEach>
									</td>
									<td>${specificServiceHostCfg.destIpAddress }</td>
									<td>${specificServiceHostCfg.destPort }</td>
									<td><spring:message code='${fns:getDictLabel("DIRECTION",specificServiceHostCfg.direction,"0")}' /></td>
									<td><spring:message code='${fns:getDictLabel("PROTOCOL",specificServiceHostCfg.protocol,"0")}' /></td>
									<td>${fns:getUserById(specificServiceHostCfg.creator.id).name}</td>
									<td><fmt:formatDate value="${specificServiceHostCfg.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${fns:getUserById(specificServiceHostCfg.editor.id==null?0:specificServiceHostCfg.editor.id).name}</td>
									<td><fmt:formatDate value="${specificServiceHostCfg.editTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${fns:getUserById(specificServiceHostCfg.auditor.id==null?0:specificServiceHostCfg.auditor.id).name}</td>
									<td><fmt:formatDate value="${specificServiceHostCfg.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<div class="page">${page}</div>	
					</div>
				</div>
			</div>
	</div>
	</div>
</body>
</html>
