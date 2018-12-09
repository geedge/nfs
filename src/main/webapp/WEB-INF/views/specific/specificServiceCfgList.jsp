<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<link href="${ctxStatic}/global/plugins/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
	<script src="${ctxStatic}/global/plugins/treeTable/jquery.treeTable.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/pages/scripts/dict.js" type="text/javascript"></script>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	/** 
	 处理全选、全取消
	**/
	 function selectAll(){
	  	 if($("#selAll").prop("checked")){
                $("input[name='check']:checkbox").each(function(){
                $(this).prop("checked",true);
					});

			
		}else{
               $("input[name='check']:checkbox").each(function(){
              	 $(this).removeProp("checked");
					});
		} 
	} 
	/*
	系统通用方法，根据参数来决定处理的url和参数
	*/
	function cmd(){
		var url=arguments[0];
		var mulitId="";
		jQuery("#treeTable").find(":checkbox:checked[name='check']").each(function(){
		  if(jQuery(this).val()!=""){
		    mulitId+=jQuery(this).val()+",";
		  }
		});
		if(mulitId!=""){
			confirmx("<spring:message code='confirm_message'/>", url+"?mulitId="+mulitId);
		}else{
			top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
		}
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
			confirmx("<spring:message code='confirm_message'/>", url+"?specServiceId="+cked.val());
			}
	}
	function casec(){
		if($("#intype").attr("name")=="specServiceId"||$("#intype").attr("name")=="groupId"){
			 if(isNaN($("#intype").val())){
				 $("#showError").show();
				 return false;
			 }else if($("#intype").val()>2147483647){
						 $("#showError").show();
						 return false;
			 }else{
						$("#showError").hide();
						return true;
			 }
		}else{
			$("#showError").hide();
			 return true;
		}
	}
	
	//查询
 	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/specific/specificServiceCfg/list");
		if(!casec()){
			return false;
			}
		$("#searchForm").submit();
		return false;
	}
		$(document).ready(function() {
			
			//reset
			$("#resetBtn").on("click",function(){
				$("#cfgType").val("");
				$("#cfgType").selectpicker('refresh');
				$(".Wdate").attr("value",'');
				$("#searchForm")[0].reset();
			});

			//设定显示总条数
			$("#showTotalCount").text('${showTotalCount}');
			//条件回传
			$("#seltype").find("option[value=${searchType==null?11:searchType}]").attr("selected",true);
			$("#intype").attr("name",$("#seltype").find("option:selected").val());
			$("#intype").val("${searchContent}");
			//筛选功能初始化
	 		filterActionInit();
	 		$("#seltype").change(function(){
				$("#intype").val("");
				$("#intype").attr("placeholder","<spring:message code='input'/>"+$(this).find("option:selected").text());
				$("#intype").attr("name",$(this).find("option:selected").val());
				$("#showError").hide();
			});
			$("#treeTable").treeTable({expandLevel : 3}).show();	
			$("#cfgType").change(function(){
				page();
			});
		});
		
	</script>
	<style type="text/css">
	.dropdown-menu {
    min-width: 70px;
}
	</style>
</head>
<body>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<%-- <button type="button" class="btn btn-default" onclick="javascript:window.location='${ctx}/specific/specificServiceCfg/list'"><spring:message code="refresh"></spring:message></button> --%>
	<shiro:hasPermission name="specific:service:add">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/specific/specificServiceCfg/form'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
	</shiro:hasPermission>
	</div>
	
	<h3 class="page-title">
		<spring:message code="specific_service_cfg"/>
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
	
				<div class="portlet-body">

					<div class="row"  >
						<form:form id="searchForm" modelAttribute="specificServiceCfg" action="${ctx}/specific/specificServiceCfg/list" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${specificServiceCfg.isFilterAction}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							
							<!-- 搜索内容与操作按钮栏 -->
			 				<div class="col-md-12">
									<div class="pull-left">
	                       				 <form:select path="cfgType" class="selectpicker select2 input-middle" >
	                       				 	<%-- <form:option value=""><spring:message code='cfg_type'/></form:option> --%>
	                       				 	<c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
	                       				 	<form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}'/></form:option>
	                       				 	</c:forEach>
	                       				 </form:select>
	                       			</div>
									<div class="pull-left">	
										<div class="input-group">
                                           <div class="input-group-btn">
                                                <select id="seltype"  class="selectpicker select2 input-middle" >
                                                	<c:if test="${specificServiceCfg.cfgType==1}">
                                                	<option value="specServiceName"><spring:message code="app_name"/>
                                                	</c:if>
                                                	<c:if test="${specificServiceCfg.cfgType==3}">
                                                	<option value="specServiceName"><spring:message code="protocol_name"/>
                                                	</c:if>
                                                	<c:if test="${specificServiceCfg.cfgType==2}">
                                                	<option value="specServiceName"><spring:message code="tunnel_name"/>
                                                	</c:if>
													</option>
												</select>
                                            </div>
                                            <c:if test="${specificServiceCfg.cfgType==1}">
                                            <input id="intype" class="form-control input-medium" placeholder="<spring:message code='input'/> <spring:message code='app_name'/>" type="text" onchange="casec()" maxlength="60">
                                    		</c:if>
                                            <c:if test="${specificServiceCfg.cfgType==2}">
                                            <input id="intype" class="form-control input-medium" placeholder="<spring:message code='input'/> <spring:message code='tunnel_name'/>" type="text" onchange="casec()" maxlength="60">
                                    		</c:if>
                                            <c:if test="${specificServiceCfg.cfgType==3}">
                                            <input id="intype" class="form-control input-medium" placeholder="<spring:message code='input'/> <spring:message code='protocol_name'/>" type="text" onchange="casec()" maxlength="60">
                                    		</c:if>
                                            
                                        </div>
	                               </div>
	                               <div class="pull-left" style="margin-top:10px;display:none" id="showError">
									<label class="error"><spring:message code='input_integer'/></label>
									</div>
									 <div class="pull-left">
									 <shiro:hasPermission name="specific:service:view">
	                               		 <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
									</shiro:hasPermission>
										 <button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                     <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
									</div>
									

									<div class="pull-right">
									<shiro:hasPermission name="specific:service:edit">
                                        <button type="button" class="btn btn-default" onclick="cmdEdit('${ctx}/specific/specificServiceCfg/form')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"/></button>
                                    </shiro:hasPermission>  
                                    <shiro:hasPermission name="specific:service:del">        
                                        <button type="button" class="btn btn-default" onclick="cmd('${ctx}/specific/specificServiceCfg/delete')">
                                            <i class="fa fa-trash"></i> <spring:message code="delete"/></button>
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
                                            <label><spring:message code="operate_time"/>：</label>
                                            <input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceCfg.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                        </div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${specificServiceCfg.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                        </div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
                                            <label><spring:message code="business_type"/>：</label>
	                                          <form:select path="businessType" class="selectpicker select2 input-middle" data-live-search="true" data-live-search-placeholder="search">
	                                          	<form:option value=""><spring:message code='business_type'/></form:option>
	                                          	<c:if test="${specificServiceCfg.cfgType==1}">
	                                          		<c:forEach items="${fns:getDictList('APP_BUSINESS_TYPE') }" var="dict">
			                       				 	<form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}'/></form:option>
			                       				 	</c:forEach>
	                                          	</c:if>
	                                          	<c:if test="${specificServiceCfg.cfgType==2}">
	                                          		<c:forEach items="${fns:getDictList('TUNNEL_BEHAV_BUSINESS_TYPE') }" var="dict">
			                       				 	<form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}'/></form:option>
			                       				 	</c:forEach>
	                                          	</c:if>
	                                          	<c:if test="${specificServiceCfg.cfgType==3}">
	                                          		<c:forEach items="${fns:getDictList('BASIC_PROTOCOL_BUSINESS_TYPE') }" var="dict">
			                       				 	<form:option value="${dict.itemCode}"><spring:message code='${dict.itemValue}'/></form:option>
			                       				 	</c:forEach>
	                                          	</c:if>
		                       				 </form:select>
                                        </div>
									</div>
								</div>
							</div>
							
							<!-- /筛选搜索内容栏    结束-->
						</form:form>

					</div>
					
					
				
					
					
				<sys:message content="${message}"/>
					 <div class="table-responsive"> 
					
						<table id="treeTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
								   <th><input type="checkbox" class="ckboxs" id="selAll" onclick="selectAll()"></th>
<!-- 								   <th>序号</th> -->
								   <th><spring:message code="business_type"/></th>
								   <th>
									   <c:if test="${specificServiceCfg.cfgType==1}">
									   <spring:message code="app_code"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==2}">
									   <spring:message code="tunnel_code"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==3}">
									   <spring:message code="protocol_code"/>
									   </c:if>
								   </th>
								   <th>
								   	   <c:if test="${specificServiceCfg.cfgType==1}">
									   <spring:message code="app_name"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==2}">
									   <spring:message code="tunnel_name"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==3}">
									   <spring:message code="protocol_name"/>
									   </c:if>
								   </th>
								   <th>
								   	<c:if test="${specificServiceCfg.cfgType==1}">
									   <spring:message code="app_desc"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==2}">
									   <spring:message code="tunnel_desc"/>
									   </c:if>
									   <c:if test="${specificServiceCfg.cfgType==3}">
									   <spring:message code="protocol_desc"/>
									</c:if>
								  </th>
								   <%-- <th><spring:message code="group_id"/></th> --%>
								  <%--  <th><spring:message code="is_leaf"/></th> --%>
								   <th><spring:message code="cfg_type"/></th>
								   <th class="sort-column op_time"><spring:message code="operate_time"/></th>
  							    </tr>
							</thead>
							<tbody>
							<c:forEach items="${list}" var="specificServiceCfg" varStatus="ss">
											<tr id="${specificServiceCfg.specServiceId}" pId="${specificServiceCfg.parent.specServiceId ne 0?specificServiceCfg.parent.specServiceId:0}">
												<td><input type="checkbox" class="ckbox" name="check" value="${specificServiceCfg.specServiceId}"></td>
<%-- 												<td>${specificServiceCfg.showSequence}</td> --%>
												<td title="${specificServiceCfg.businessType}">
												<c:if test="${specificServiceCfg.cfgType==1}">
													<c:forEach items="${fns:getDictList('APP_BUSINESS_TYPE') }" var="dict">
														<c:if test="${specificServiceCfg.businessType==dict.itemCode}">
																<spring:message code="${dict.itemValue}"/>
														</c:if>
													</c:forEach>
												</c:if>
												<c:if test="${specificServiceCfg.cfgType==2}">
													<c:forEach items="${fns:getDictList('TUNNEL_BEHAV_BUSINESS_TYPE') }" var="dict">
														<c:if test="${specificServiceCfg.businessType==dict.itemCode}">
																<spring:message code="${dict.itemValue}"/>
														</c:if>
													</c:forEach>
												</c:if>
												<c:if test="${specificServiceCfg.cfgType==3}">
													<c:forEach items="${fns:getDictList('BASIC_PROTOCOL_BUSINESS_TYPE') }" var="dict">
														<c:if test="${specificServiceCfg.businessType==dict.itemCode}">
																<spring:message code="${dict.itemValue}"/>
														</c:if>
													</c:forEach>
												</c:if>
												</td>
												<td nowrap><i class="icon-icon-tablet"></i><%--<a href="${ctx}/specific/specificServiceCfg/form?specServiceId=${specificServiceCfg.specServiceId}&doAction=0">--%>${specificServiceCfg.specServiceCode}<%--</a>--%></td>
												<td title="${specificServiceCfg.specServiceName}">${specificServiceCfg.specServiceName}</td>
												<td title="${specificServiceCfg.specServiceDesc}">${fns:abbr(specificServiceCfg.specServiceDesc,15)}</td>
												<%-- <td>${specificServiceCfg.groupId }</td> --%>
												<%-- <td><spring:message code="${fns:getDictLabel('INT_YES_NO',specificServiceCfg.isLeaf,'0')}"></spring:message></td> --%>
												<td><c:forEach items="${fns:getDictList('SPECIFIC_SERVICE_CFG_TYPE') }" var="dict">
													<c:if test="${dict.itemCode==specificServiceCfg.cfgType}"><spring:message code="${dict.itemValue}"/></c:if>
													</c:forEach>
												</td>
												<td><fmt:formatDate value="${specificServiceCfg.opTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<%-- <td>
													<div class="btn-group btn-xs">
													    <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"/><span class="caret"></span></a>
													      <ul class="dropdown-menu btn-xs">
													         <li><a href="${ctx}/specific/specificServiceCfg/form?mulitId=${specificServiceCfg.specServiceId}&doAction=0"><spring:message code="show"/></a></li>
													         <shiro:hasPermission name="sys:dict:edit">
														        <li><a href="${ctx}/specific/specificServiceCfg/form?mulitId=${specificServiceCfg.specServiceId}" onclick="return confirmx('您确认要修改该项配置吗？', this.href)"><spring:message code="edit"/></a></li>
													         </shiro:hasPermission>
													         <shiro:hasPermission name="sys:dict:edit">
													       	 	<li><a href="${ctx}/specific/specificServiceCfg/delete?mulitId=${specificServiceCfg.specServiceId}" onclick="return confirmx('您确认要删除该项配置吗？', this.href)"><spring:message code="delete"/></a></li>
															 </shiro:hasPermission>
													    
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