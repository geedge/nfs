<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>来函信息</title>

</head>

<body>

<div class="page-content">
	<shiro:hasPermission name="cfg:requestInfo:config">
		<div class="theme-panel hidden-xs hidden-sm">
			<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/request/form'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"/></button>
		</div>
	</shiro:hasPermission>
	<h3 class="page-title">
		<spring:message code="requestInfo"></spring:message>
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
	
				<div class="portlet-body">

					<div class="row"  >
						<form:form id="searchForm" modelAttribute="requestInfo" action="${ctx}/cfg/request/list" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${requestInfo.isFilterAction }"/>
							
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
									
									<div class="pull-left">
										<form:select path="isAudit" class="selectpicker select2 input-small" >
											<form:option value=""><spring:message code="all_states"/></form:option>
											<form:option value="0"><spring:message code="created"></spring:message></form:option>
											<form:option value="1"><spring:message code="approved"></spring:message></form:option>
										</form:select>
									</div>
									
									<div class="pull-left">	
										<div class="input-group">
                                            <div class="input-group-btn">
                                                <form:select path="seltype" class="selectpicker select2 input-small" >
													<form:option value="requestNumber"><spring:message code="title"></spring:message></form:option>
													<form:option value="requestTitle"><spring:message code="request_number"></spring:message></form:option>
													<form:option value="requestContent"><spring:message code="content"></spring:message></form:option>
											</form:select>
									
                                         </div>
                                         
                                         <input id="intype" class="form-control input-medium" type="text" value="">
                                            
                                        </div>
	                               </div>
	                                
	                               <div class="pull-left">
	                               		 <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
										 <button type="button" class="btn btn-default" onClick="resetx()"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                     <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
									</div>

									<div class="pull-right">
									<shiro:hasPermission name="cfg:requestInfo:config">
                                        <button type="button" class="btn btn-default" onClick="edit()">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"></spring:message></button>
                                            <sys:delRow url="${ctx}/cfg/request/delete" id="contentTable" label="delete"></sys:delRow>
                                      <!--   <button type="button" class="btn btn-default">
                                            <i class="fa fa-download"></i> 导出</button> -->
                        			</shiro:hasPermission>
                        			<shiro:hasPermission name="cfg:requestInfo:confirm">
	                                        <div class="btn-group">
	                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                                <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                                <i class="fa fa-angle-down"></i>
	                                            </button>
	                                            <ul class="dropdown-menu pull-right">
	                                                <li><sys:delRow url="${ctx}/cfg/request/requestExamine" id="contentTable" label="approved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/request/requestExamineNo" id="contentTable" label="unapproved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/request/requestCancelExamine" id="contentTable" label="cancelPass"></sys:delRow></li>            
	                                            </ul>
	                                        </div>
                        			</shiro:hasPermission>
                                        <a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                         </a>
                                    </div>
							
								
							</div>
							<!-- /搜索内容与操作按钮栏-->
							
							
							
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
								
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label><spring:message code="request_time"/>：</label>
                                            <input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate" data-options="buttons:buttons"
												value="<fmt:formatDate value="${requestInfo.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
												value="<fmt:formatDate value="${requestInfo.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label><spring:message code="edit_time"/>：</label>
                                            <input id="dobeginDate" name="dobeginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${requestInfo.dobeginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="doendDate" name="doendDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${requestInfo.doendDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                                          
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
									<th><spring:message code="Id"></spring:message></th>
									<th><spring:message code="request_number"></spring:message></th>
									<th><spring:message code="request_organization"></spring:message></th>
									<th><spring:message code="request_time"></spring:message></th>
									<th><spring:message code="state"></spring:message></th>
									<th><spring:message code="title"></spring:message></th>
									<th><spring:message code="content"></spring:message></th>
									<th><spring:message code="special_task"></spring:message></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column createTime"><spring:message code="create_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column editTime"><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"></spring:message></th>
									<th class="sort-column auditTime"><spring:message code="audit_time"></spring:message></th>
						
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${page.list}" var="requestInfo">
								<tr>
									<td><input type="checkbox" class="i-checks" id="${requestInfo.id}" value="${requestInfo.isAudit}"></td>
									<td>${requestInfo.id }</td>
									<td>${requestInfo.requestTitle }</td><!--requestTitle当成来函函号使用 -->
									<td>${requestInfo.requestOrg }</td>
									<td><fmt:formatDate value="${requestInfo.requestTime }" pattern="yyyy-MM-dd"/></td>
									<td>
										<c:choose>
											<c:when test="${requestInfo.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${requestInfo.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${requestInfo.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${requestInfo.isAudit eq '3'}"><span class="label label-warning"><spring:message code="cancel_approved"></spring:message></span></c:when>
										</c:choose>
									</td>
									<td>${requestInfo.requestNumber }</td><!--requestTitle当成来函标题使用 -->
									<td>${requestInfo.requestContent }</td>
									<td>${requestInfo.taskName }</td>
									<td>${requestInfo.creatorName }</td>
									<td><fmt:formatDate value="${requestInfo.createTime }" pattern="yyyy-MM-dd"/></td>
									<td>${requestInfo.editorName }</td>
									<td><fmt:formatDate value="${requestInfo.editTime }" pattern="yyyy-MM-dd"/></td>
									<td>${requestInfo.auditorName }</td>
									<td><fmt:formatDate value="${requestInfo.auditTime }" pattern="yyyy-MM-dd"/></td>
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

<script>
 	$(document).ready(function() {
 	    
		if("${requestInfo.requestTitle}"){
			$("#intype").val("${requestInfo.requestTitle}");
		}
		if("${requestInfo.requestNumber}"){
			$("#intype").val("${requestInfo.requestNumber}");
		}
		if("${requestInfo.requestContent}"){
			$("#intype").val("${requestInfo.requestContent}");
		}else{
			$("#intype").attr("placeholder","<spring:message code='input'/>"+$("#seltype").find("option:selected").text());
		}
 		//筛选功能初始化
 		filterActionInit();
 		
		$("#isAudit").change(function(){
			page();
		});
		
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/>"+$(this).find("option:selected").text());
		});
		//全选及取消
		$("#checkAll").change(function(){
			if($("#checkAll").prop("checked")){
				$("input.i-checks").prop("checked",true);
			}else{
				$("input.i-checks").prop("checked",false);
			}
		});
		
	
		
 	});
 	
 	
 	function resetx(){
//  		$("#searchForm").reset();
//  		$(':input','#searchForm')
//  		.not(':button,:submit,:reset,:hidden')
//  		.val('')
//  		.removeAttr('checked')
//  		.removeAttr('selected');
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#searchForm")[0].reset();
			$("#intype").attr("placeholder","<spring:message code='input'/>"+"<spring:message code='title'/>");
 		
 	}
 	
 	
	//查询
 	function page(n,s){
 		$("#intype").attr("name",$("#seltype").val());
		
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/cfg/request/list");
		$("#searchForm").submit();
		return false;
	}
	//编辑
	function edit(){
		var cked = $('tbody tr td input.i-checks:checkbox:checked');
		if(cked.val()==1){
			top.$.jBox.tip("<spring:message code='has_approved'/>", "<spring:message code='info'/>");
			return;
		}
		if(cked.val()==3){
			top.$.jBox.tip("<spring:message code='cancel_approved'/>", "<spring:message code='info'/>");
			return;
		}
		if(cked.length==1){
			window.location = "${ctx}/cfg/request/form?id="+cked.attr("id");
		}else{
			top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
			return;
		}
	}

</script>

</body>
</html>
