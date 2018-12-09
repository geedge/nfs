<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
 	$(document).ready(function() {
 		//搜索框提示语初始化
		if("${cfg.cfgKeywords}"){
			$("#intype").val("${cfg.cfgKeywords}");
		} else if("${cfg.cfgDesc}"){
			$("#intype").val("${cfg.cfgDesc}");
		} else{
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
		}
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
		});
 		//筛选功能初始化
		filterActionInit();
		$("#isAudit").change(function(){
			page();
		});
		//reset
		$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$("#cfgKeywords").attr("value",'');
			$(".Wdate").attr("value",'');
			$("#searchForm")[0].reset();
		});
 	});
 	var edit=function(url){
 		var cked = $('tbody tr td input.i-checks:checkbox:checked');
 		if(cked.val()==1){
 			top.$.jBox.tip("<spring:message code='has_approved'/>", "<spring:message code='info'/>");
 			return;
 		}
 		if(cked.length==1){
 			window.location = url+"&compileId="+cked.attr("id");
 		}else{
 			top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
 			return;
 		}
 	}
</script>
</head>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<%-- <button type="button" class="btn btn-default" onclick="location='${ctx}/cfg/string/list?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}'"><spring:message code="refresh"></spring:message></button> --%>
		<c:if test="${audit==0}">
			<shiro:hasPermission name="cfg:string:add">
			<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/string/form?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&audit=${audit}'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
			</shiro:hasPermission>
		</c:if>
	</div>

	<h3 class="page-title">
		<spring:message code="${cfgName}"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/cfg/string/list" method="post" class="form-search">
							<input id="cfgName" name="cfgName" type="hidden" value="${cfgName}"/>
							<input id="action" name="action" type="hidden" value="${action}"/>
							<input id="serviceId" name="serviceId" type="hidden" value="${serviceId}"/>
							<input id="audit" name="audit" type="hidden" value="${audit}"/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<form:select path="isAudit" class="selectpicker select2 input-small">
										<form:option value=""><spring:message code="all_states"/></form:option>
										<form:option value="0"><spring:message code="created"></spring:message></form:option>
										<form:option value="1"><spring:message code="approved"></spring:message></form:option>
										<form:option value="2"><spring:message code="unapproved"></spring:message></form:option>
									</form:select>
								</div>
								
								<div class="pull-left">
								
									<div class="input-group">
                                         <div class="input-group-btn">
                                         
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
                                        		<form:option value="cfgKeywords"><spring:message code="key_word"></spring:message></form:option>
												<form:option value="cfgDesc"><spring:message code="config_describe"></spring:message></form:option>
												<form:option value="action"><spring:message code="block_type"></spring:message></form:option>
											</form:select>
									
                                         </div>
                                         
                                         <input id="intype" class="form-control input-medium" type="text" value="">
                                  		 <div class="input-group-btn">
	                                  		 <form:select id="actionSelect" path="action" class="selectpicker select2 input-small" >
													<form:option value=""><spring:message code="select"/></form:option>
													<c:forEach items="${serviceList}" var="service">
														<form:option value="${service.action }"><spring:message code="action_${service.actionCode }"/></form:option>
													</c:forEach>
											 </form:select>
										 </div>
                                  
                                    </div> 
								</div>
								<div class="pull-left">
									 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									 <button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                 <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/>  <i class="fa fa-angle-double-down"></i></button>
								</div>
								 <div class="pull-right">
								 	<c:if test="${audit==0}">
								 		<shiro:hasPermission name="cfg:string:edit">
                                        <button type="button" class="btn btn-default" onClick="edit('${ctx}/cfg/string/updateForm?serviceId=${serviceId}&action=${action}&tableName=${tableName}&cfgName=${cfgName}')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"></spring:message></button>
                                         </shiro:hasPermission>
                                         <shiro:hasPermission name="cfg:string:delete">
                                            <sys:delRow url="${ctx}/cfg/string/delete?serviceId=${serviceId}&action=${action}&tableName=${tableName}&cfgName=${cfgName}" id="contentTable" label="delete"></sys:delRow>
                                      	 </shiro:hasPermission>
                                      <!--   <button type="button" class="btn btn-default">
                                            <i class="fa fa-download"></i> 导出</button> -->
                                    </c:if>
                                    <c:if test="${audit==1}">
                                    		<shiro:hasPermission name="cfg:string:confirm">
	                                        <div class="btn-group">
	                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                                                <i class="fa fa-wrench"></i> <spring:message code="examine"></spring:message>
	                                                <i class="fa fa-angle-down"></i>
	                                            </button>
	                                            <ul class="dropdown-menu pull-right">
	                                                <li><sys:delRow url="${ctx}/cfg/string/audit?serviceId=${serviceId}&action=${action}&tableName=${tableName}&cfgName=${cfgName}&isAudit=1" id="contentTable" label="approved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/string/audit?serviceId=${serviceId}&action=${action}&tableName=${tableName}&cfgName=${cfgName}&isAudit=2" id="contentTable" label="unapproved"></sys:delRow></li>
													<li><sys:delRow url="${ctx}/cfg/string/audit?serviceId=${serviceId}&action=${action}&tableName=${tableName}&cfgName=${cfgName}&isAudit=3" id="contentTable" label="cancelPass"></sys:delRow></li>            
	                                            </ul>
	                                        </div>
	                                        </shiro:hasPermission>
                                    </c:if>
                                 	<a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                     </a>
                                 </div>
							</div>
							<!-- /搜索内容与操作按钮栏 -->
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
									<c:choose>
										<c:when test="${action==5}">
										</c:when>
										<c:otherwise>
											<div class="col-md-3">
											
													<div class="form-group">
			                                            <label class="control-label"><spring:message code='request_number'/></label>
			                                            <form:select path="requestId" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
															<form:option value=""><spring:message code="select"/></form:option>
															<c:forEach items="${requestInfos}" var="requestInfo" >
																<form:option value="${requestInfo.id}"><spring:message code="${requestInfo.requestTitle}"></spring:message></form:option>
															</c:forEach>
														</form:select>
													</div>
		                                          
		                                        </div>
		                                        
		                                        <div class="col-md-3">
											
													<div class="form-group">
			                                            <label class="control-label"><spring:message code='type'/></label>
			                                            <form:select path="classify" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
														<form:option value=""><spring:message code="select"/></form:option>
														<c:forEach items="${fls}" var="fl" >
															<form:option value="${fl.serviceDictId}"><spring:message code="${fl.itemValue}"></spring:message></form:option>
														</c:forEach>
													</form:select>
													</div>
		                                          
		                                        </div>
												<div class="col-md-3">
													<div class="form-group">
			                                            <label class="control-label"><spring:message code='attribute'/></label>
			                                            <form:select path="attribute" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
															<form:option value=""><spring:message code="select"/></form:option>
															<c:forEach items="${xzs}" var="xz" >
																<form:option value="${xz.serviceDictId}"><spring:message code="${xz.itemValue}"></spring:message></form:option>
															</c:forEach>
														</form:select>
													</div>
		                                        </div>
		                                        <div class="col-md-3">
													<div class="form-group">
			                                            <label class="control-label"><spring:message code='label'/></label>
			                                            <form:select path="lable" class="selectpicker  form-control" data-live-search="true" data-live-search-placeholder="search">
															<form:option value=""><spring:message code="select"/></form:option>
															<c:forEach items="${lables}" var="lable" >
																<form:option value="${lable.serviceDictId}"><spring:message code="${lable.itemValue}"></spring:message></form:option>
															</c:forEach>
														</form:select>
													</div>
		                                        </div>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="row">
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="config_time"/>：</label>
											<input name="search_create_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value='${cfg.search_create_time_start}' pattern='yyyy-MM-dd HH:mm:ss'/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_create_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_create_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="edit_time"/>：</label>
											<input name="search_edit_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_edit_time_start}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_edit_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_edit_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="audit_time"/>：</label>
											<input name="search_audit_time_start" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_audit_time_start}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<input name="search_audit_time_end" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${cfg.search_audit_time_end}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
									</div>
								</div>
							</div> 
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<%-- <th><spring:message code="seq"/></th> --%>
									<th><spring:message code="config_describe"/></th>
									<th><spring:message code="key_word"/></th>
									<th class="sort-column r.action"><spring:message code="block_type"/></th>
									<c:choose>
										<c:when test="${action==5}">
										</c:when>
										<c:otherwise>
											<th><spring:message code="whether_area_block"/></th>
											<th><spring:message code="letter"/></th>
											<th><spring:message code="classification"/></th>
											<th><spring:message code="attribute"/></th>
											<th><spring:message code="label"/></th>
										</c:otherwise>
									</c:choose>
									<th><spring:message code="valid_identifier"/></th>
									<th><spring:message code="is_audit"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column r.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column r.edit_time"><spring:message code="edit_time"/></th>
									<th><spring:message code="auditor"/></th>
									<th class="sort-column r.audit_time"><spring:message code="audit_time"/></th>
									<%-- <th><spring:message code="operation"></spring:message></th> --%>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="cfg" varStatus="status" step="1">
									<tr>
									<td><input type="checkbox" class="i-checks" id="${cfg.compileId}" value="${cfg.isAudit}"></td>
									<%-- <td>${status.index+1 }</td> --%>
									<td>${cfg.cfgDesc }</td>
									<td>${cfg.cfgKeywords }</td>
									<td>
										<c:if test="${1 eq cfg.action }"><spring:message code="block"/></c:if>                   
										<c:if test="${2 eq cfg.action }"><spring:message code="monitor"/></c:if>                 
										<c:if test="${5 eq cfg.action }"><spring:message code="block_white_list"/></c:if>        
										<c:if test="${6 eq cfg.action }"><spring:message code="monitor_white_list"/></c:if>      
										<c:if test="${7 eq cfg.action }"><spring:message code="block_monitor_white_list"/></c:if>
										<c:if test="${8 eq cfg.action }"><spring:message code="grey_list"/></c:if>               
									</td>
									<c:choose>
										<c:when test="${action==5}">
										</c:when>
										<c:otherwise>
											<td>
												<c:if test="${cfg.isAreaEffective==0}"><spring:message code="no"/></c:if>
												<c:if test="${cfg.isAreaEffective==1}"><spring:message code="yes"/></c:if>
											</td>
											<td>${cfg.requestName }</td>
											<c:set var="classify"></c:set>
											<c:forEach items="${fn:split(cfg.classify,',')}" var="classifyId" varStatus="status">
												<c:forEach items="${fls}" var="fl">
													<c:if test="${classifyId eq fn:trim(fl.serviceDictId)}">
													<c:if test="${status.index+1 eq 1}">
														<c:set var="classify" value="${fl.itemValue}"></c:set>
													</c:if>
													<c:if test="${status.index+1 ne 1}">
														<c:set var="classify" value="${classify},${fl.itemValue}"></c:set>
													</c:if>
													</c:if>
												</c:forEach>
											</c:forEach>
											<td title="${classify }">
												${fns:abbr(classify,20)}
											</td>
											<c:set var="attribute"></c:set>
											<c:forEach items="${fn:split(cfg.attribute,',')}" var="attributeId" varStatus="status">
												<c:forEach items="${xzs}" var="xz">
													<c:if test="${attributeId eq fn:trim(xz.serviceDictId)}">
													<c:if test="${status.index+1 eq 1}">
														<c:set var="attribute" value="${xz.itemValue}"></c:set>
													</c:if>
													<c:if test="${status.index+1 ne 1}">
														<c:set var="attribute" value="${attribute},${xz.itemValue}"></c:set>
													</c:if>
													</c:if>
												</c:forEach>
											</c:forEach>
											<td title="${attribute }">
												${fns:abbr(attribute,20)}
											</td>
											<c:set var="lableInfo"></c:set>
											<c:forEach items="${fn:split(cfg.lable,',')}" var="lableId" varStatus="status">
												<c:forEach items="${lables}" var="lable">
													<c:if test="${lableId eq fn:trim(lable.serviceDictId)}">
													<c:if test="${status.index+1 eq 1}">
														<c:set var="lableInfo" value="${lable.itemValue}"></c:set>
													</c:if>
													<c:if test="${status.index+1 ne 1}">
														<c:set var="lableInfo" value="${lableInfo},${lable.itemValue}"></c:set>
													</c:if>
													</c:if>
												</c:forEach>
											</c:forEach>
											<td title="${lableInfo }">
												${fns:abbr(lableInfo,20)}
											</td>
										</c:otherwise>
									</c:choose>
									<td>
										<c:if test="${cfg.isValid==0}"><spring:message code="no"/></c:if>
										<c:if test="${cfg.isValid==1}"><spring:message code="yes"/></c:if>
										<c:if test="${cfg.isValid==-1}"><spring:message code="deleted"/></c:if>
									</td>
									<td>
										<c:choose>
											<c:when test="${cfg.isAudit eq '0'}"><span class="label label-danger"><spring:message code="created"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '1'}"><span class="label label-success"><spring:message code="approved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '2'}"><span class="label label-warning"><spring:message code="unapproved"></spring:message></span></c:when>
											<c:when test="${cfg.isAudit eq '3'}"><span indexTable="${cfg.indexTable}" data-placement="right" data-original-title="<spring:message code='letter_cancel_info'/>: " class="label le-ca-fo label-warning tooltips" data-icon="&#xe01e;"> <spring:message code="cancel_approved"/></span></c:when>
										</c:choose>
									</td>
									<td>${cfg.creatorName }</td>
									<td><fmt:formatDate value="${cfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.editorName }</td>
									<td><fmt:formatDate value="${cfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${cfg.auditorName }</td>
									<td><fmt:formatDate value="${cfg.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<%-- <td>
										<div class="btn-group btn-xs">
									      <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"></spring:message><span class="caret"></span></a>
									      <ul class="dropdown-menu btn-xs">
								      		<c:if test="${audit==0}">
									      	<c:choose>
									      		<c:when test="${cfg.isAudit eq '2'}">
									      			<li><a href="${ctx}/cfg/string/updateForm?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&cfgName=${cfgName}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      		</c:when>
									      		<c:when test="${cfg.isAudit eq '0'}">
									      			<li><a href="${ctx}/cfg/string/updateForm?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&cfgName=${cfgName}" onclick="javascript:return confirm('sure？', this.href)"><spring:message code="edit"></spring:message></a></li>
									      			<li><a href="${ctx}/cfg/string/deleteCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&cfgName=${cfgName}&compileId=${cfg.compileId}" onclick="return confirm('sure？', this.href)"><spring:message code="delete"></spring:message></a></li>
									      		</c:when>
									      	</c:choose>
									      </c:if>
									      <c:if test="${audit==1}">
									      	<c:choose>
									        	<c:when test="${cfg.isAudit eq '1'}">
											        <li><a href="${ctx}/cfg/string/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&isAudit=3&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="cancel"></spring:message></a></li>
									        	</c:when>
									        	<c:when test="${cfg.isAudit eq '0'}">
									        		<li><a href="${ctx}/cfg/string/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&isAudit=1&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="approved"></spring:message></a></li>
									        		<li><a href="${ctx}/cfg/string/auditCfg?serviceId=${cfg.serviceId}&action=${cfg.action}&tableName=${cfg.tableName}&cfgId=${cfg.cfgId}&isAudit=2&cfgName=${cfgName}" onclick="return confirm('sure？', this.href)"><spring:message code="unapproved"></spring:message></a></li>
									        	</c:when>
								        	</c:choose>
									      </c:if>
									      </ul>
									    </div>
									</td> --%>
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