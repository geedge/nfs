<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<link href="${ctxStatic}/global/plugins/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
	<script src="${ctxStatic}/global/plugins/treeTable/jquery.treeTable.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/pages/scripts/dict.js" type="text/javascript"></script>
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
			confirmx("<spring:message code='confirm_message'/>", url+"&mulitId="+mulitId);
		}else{
			top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
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
			confirmx("<spring:message code='confirm_message'/>", url+"&serviceDictId="+cked.val());
			}
	}
	//查询
 	function page(n,s){
 		$("#intype").attr("name",$("#seltype").val());
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/basics/serviceDictInfo/list?itType=${itType}");
		$("#searchForm").submit();
		return false;
	}
		$(document).ready(function() {
			
			//设定显示总条数
			$("#showTotalCount").text('${showTotalCount}');
			//设定selected，设定name  设定value
			
				$("#seltype").find("option[value='${searchType==null?11:searchType}']").attr("selected",true);
				$("#intype").attr("name",$("#seltype").find("option:selected").val());
				$("#intype").val('${searchContent}');
			
				//reset
				$("#resetBtn").on("click",function(){
					$(".Wdate").attr("value",'');
					$("#itemType").val("");
					$("#searchForm")[0].reset();
					$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text().trim()); 
				});
			
			//筛选功能初始化
	 		filterActionInit();
	 		
			$("#isAudit").change(function(){
				page();
			});
			
			$("#seltype").change(function(){
				$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text().trim());
			});
			$("#treeTable").treeTable({expandLevel : 3}).show();	
			
		});
		
	</script>
	<style type="text/css">
	.dropdown-menu {
    min-width: 70px;
}
	</style>
</head>
<body>
<c:set var="permission_type">${fns:getPermissionByNo('SERVICE_DICT_ITM_TYPE',intArr)}</c:set>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
<%-- 		<button type="button" class="btn btn-default" onclick="javascript:window.location='${ctx}/basics/serviceDictInfo/list?itType=${itType}'"><spring:message code="refresh"></spring:message></button>	 --%>
			<shiro:hasPermission name="basics:${permission_type}:add">
			 <button type="button" class="btn btn-primary" onClick="javascript:window.location='${ctx}/basics/serviceDictInfo/form?itType=${itType}'"><i class="fa fa-plus"></i><spring:message code="add"></spring:message></button>
			</shiro:hasPermission>
	</div>
	
	<h3 class="page-title">
	    <c:forEach items="${fns:getItemTypeByNo('SERVICE_DICT_ITM_TYPE',intArr)}" var="shows">
	    <spring:message code="${shows}" />
	    </c:forEach>
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
	
				<div class="portlet-body">

					<div class="row"  >
						<form:form id="searchForm" modelAttribute="serviceDictInfo" action="${ctx}/basics/serviceDictInfo/list?itType=${itType}" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${serviceDictInfo.isFilterAction}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							<!-- 搜索内容与操作按钮栏 -->
			 				<div class="col-md-12">
			 				            <c:if test="${intArr.size()>1}">
							 				  <div class="pull-left">	
							 				  <div class="input-group">
				                                 <div class="input-group-btn">
				                                   <form:select path="itemType" class="selectpicker select2 input-small">
				                                        <form:option value=""><spring:message code="item_type"/></form:option>
				                                        <c:forEach items="${fns:getDictList('SERVICE_DICT_ITM_TYPE')}" var="dict">
											                <c:forEach items="${intArr}" var="itTemp">
												                <c:if test="${dict.itemCode eq itTemp}"> 
														            <form:option value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/></form:option>
													            </c:if>
												            </c:forEach> 
											             </c:forEach>
				                                    </form:select>
				                                    </div></div>
					                               </div>
					                          </c:if>     
									<div class="pull-left">	
										<div class="input-group">
                                            <div class="input-group-btn">
                                                <select id="seltype"  class="selectpicker select2 input-small" >
													<%-- <option value="itemCode"><spring:message code="item_code"></spring:message></option> --%>
													<option value="itemValue">
													 <c:if test="${itType!=5 }">
														<spring:message code="config_content"></spring:message>
													</c:if>
													<c:if test="${itType==5 }">
														<spring:message code="vpn_ip"></spring:message>
													</c:if>
													</option>
												</select>
                                            </div>
                                            <input id="intype" class="form-control input-medium" placeholder="<c:if test="${itType!=5 }"><spring:message code='input'/> <spring:message code='config_content'></spring:message></c:if><c:if test="${itType==5 }"><spring:message code='input'/> <spring:message code='vpn_ip'/></c:if>" type="text" maxlength="60">
                                        </div>
	                               </div>
	                               
	                                <div class="pull-left">
	                               		 <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
										 <button type="button" class="btn btn-default"  id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                     <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
									</div>

									<div class="pull-right">
									
									<shiro:hasPermission name="basics:${permission_type}:edit">
                                        <button type="button" class="btn btn-default" onclick="cmdEdit('${ctx}/basics/serviceDictInfo/form?itType=${itType}')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit" /></button>
                                    </shiro:hasPermission>  
                                    <shiro:hasPermission name="basics:${permission_type}:del">
                                        <button type="button" class="btn btn-default" onclick="cmd('${ctx}/basics/serviceDictInfo/delete?itType=${itType}')">
                                            <i class="fa fa-trash"></i><spring:message code="delete" /></button>
                                    </shiro:hasPermission>   
                                            
                                        <a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title="<spring:message code="custom_columns" />" href="javascript:;">
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
                                            <label><spring:message code="create_time"/>：</label>
                                            <input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate" data-options="buttons:buttons"
												value="<fmt:formatDate value="${serviceDictInfo.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
												value="<fmt:formatDate value="${serviceDictInfo.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label><spring:message code="edit_time"/>：</label>
                                            <input id="editBeginDate" name="editBeginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${serviceDictInfo.editBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="editEndDate" name="editEndDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${serviceDictInfo.editEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
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
								   <!-- <th>序号</th> -->
								   <%-- <th><spring:message code="item_code"/></th> --%>
									<c:if test="${itType==5 }">
										<th><spring:message code="vpn_ip"/></th>
									</c:if>
									<c:if test="${itType!=5 }">
										<th><spring:message code="config_content"/></th>
									</c:if>
								   <th><spring:message code="desc"/></th>
								   <c:if test="${itType!=5 }">
								   <th><spring:message code="item_type"/></th>
								   <th><spring:message code="is_leaf"/></th>
								   </c:if>
								   <th><spring:message code="creator"/></th>
								   <th class="sort-column create_time"><spring:message code="create_time"/>
								   </th><th><spring:message code="editor"/></th>
								   <th class="sort-column edit_time"><spring:message code="edit_time"/></th>
								   <%-- <shiro:hasPermission name="sys:menu:edit"><th><spring:message code="operation"/></th></shiro:hasPermission> --%>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${list}" var="serviceDictInfo" varStatus="se">
											<tr id="${serviceDictInfo.serviceDictId}" pId="${serviceDictInfo.parent.serviceDictId ne 0?serviceDictInfo.parent.serviceDictId:0}">
												<td><input type="checkbox" class="ckbox" name="check" value="${serviceDictInfo.serviceDictId}"></td>
												<%-- <td>${serviceDictInfo.showSequence}</td> --%>
												<%-- <td nowrap><i class="icon-icon-tablet"></i><a href="${ctx}/basics/serviceDictInfo/form?serviceDictId=${serviceDictInfo.serviceDictId}&doAction=0">${serviceDictInfo.itemCode}</a></td> --%>
												<td nowrap><i class="icon-icon-tablet"></i>
												   <c:if test="${itType!=5 }">
												   <a href="${ctx}/basics/serviceDictInfo/form?serviceDictId=${serviceDictInfo.serviceDictId}&doAction=0">${serviceDictInfo.itemValue}</a>
												   </c:if>
												   <c:if test="${itType==5 }">${serviceDictInfo.itemValue}</c:if>
												</td>
												<td title="${serviceDictInfo.itemDesc}">${fns:abbr(serviceDictInfo.itemDesc,15)}</td>
												<c:if test="${itType!=5 }">
												<td><spring:message code='${fns:getDictLabel("SERVICE_DICT_ITM_TYPE",serviceDictInfo.itemType,"0")}'/></td>
												<td><spring:message code='${fns:getDictLabel("INT_YES_NO",serviceDictInfo.isLeaf,"0")}'/></td>
												</c:if>
												<td><c:if test="${serviceDictInfo.serviceDictCreator != null}">
												    ${fns:getUserById(serviceDictInfo.serviceDictCreator.id).name}
												    </c:if></td>
												<td><fmt:formatDate value="${serviceDictInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<td><c:if test="${serviceDictInfo.serviceDictEditor != null}">
												    ${fns:getUserById(serviceDictInfo.serviceDictEditor.id).name}
												    </c:if></td>
												<td><fmt:formatDate value="${serviceDictInfo.editTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<%-- <td>
													<div class="btn-group btn-xs">
													    <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"/><span class="caret"></span></a>
													      <ul class="dropdown-menu btn-xs">
													         <li><a href="${ctx}/basics/serviceDictInfo/form?serviceDictId=${serviceDictInfo.serviceDictId}&doAction=0&itType=${itType}"><spring:message code="show"/></a></li>
													         <shiro:hasPermission name="sys:dict:edit">
														        <li><a href="${ctx}/basics/serviceDictInfo/form?serviceDictId=${serviceDictInfo.serviceDictId}&itType=${itType}" onclick="return confirmx('您确认要修改该项配置吗？', this.href)"><spring:message code="edit"/></a></li>
													         </shiro:hasPermission>
													         <shiro:hasPermission name="sys:dict:edit">
													       	 	<li><a href="${ctx}/basics/serviceDictInfo/delete?mulitId=${serviceDictInfo.serviceDictId}&itType=${itType}" onclick="return confirmx('您确认要删除该项配置吗？', this.href)"><spring:message code="delete"/></a></li>
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