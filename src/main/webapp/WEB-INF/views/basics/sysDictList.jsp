<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<link href="${ctxStatic}/global/plugins/treeTable/themes/vsStyle/treeTable.min.css" rel="stylesheet" type="text/css" />
	<script src="${ctxStatic}/global/plugins/treeTable/jquery.treeTable.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/pages/scripts/dict.js" type="text/javascript"></script>
	<script type="text/javascript">
	function reset(){
 		$("#searchForm").reset();
 	}

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
			var isinit = true;
			jQuery("#treeTable").find(":checkbox:checked[name='check']").each(function(){
			  if(jQuery(this).val()!=""){
				  if(jQuery(this).prop("placeholder")==1){
					  isinit = false;
				  }
			    mulitId+=jQuery(this).val()+",";
			  }
			});
			if(mulitId!=""){
				if(isinit){
					confirmx("<spring:message code='confirm_message'/>", url+"&mulitId="+mulitId);
				}else{
					top.$.jBox.tip("<spring:message code='initial_data_is_not_allowed_to_delete'/>", "<spring:message code='info'/>");
				}
			}else{
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			}
		}
		
		//sys修改
		function cmdSysEdit(){
			var cked = $("tbody tr td input[name='check']:checkbox:checked");
			if(cked.length<1){
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
				}
			if(cked.length>1){
				top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
				}
			if(cked.length==1){
				var url=arguments[0];
				if(cked.prop("placeholder")!=1){
					confirmx("<spring:message code='confirm_message'/>", url+"&sysDictId="+cked.val());
				}else{
					if(${fns:getUser().loginId=='admin'}){
						confirmx("<spring:message code='confirm_message'/>", url+"&sysDictId="+cked.val());
					}else{
						top.$.jBox.tip("<spring:message code='initial_data_is_not_allowed_to_edit.'/>", "<spring:message code='info'/>");
					}
					
				}
				
				}
		}
			

    	//查询
     	function page(n,s){
     		$("#intype").attr("name",$("#seltype").val());
    		$("#pageNo").val(n);
    		$("#pageSize").val(s);
    		$("#searchForm").attr("action","${ctx}/basics/sysDictInfo/list?itType=${itType}");
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
					$("select.selectpicker").each(function(){
						$(this).selectpicker('val',$(this).find('option:first').val());
						$(this).find("option").attr("selected",false);
						$(this).find("option:first").attr("selected",true);
					});
					
					$(".Wdate").attr("value",'');
					$("#searchForm")[0].reset();
					$("#itemType").val("");
				});
			
			//筛选功能初始化
	 		filterActionInit();
	 		
			$("#isAudit").change(function(){
				page();
			});
			
			$("#seltype").change(function(){
				$("#intype").val("");
				$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
				$("#intype").attr("name",$(this).find("option:selected").val());
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
<c:set var="permission_type">${fns:getPermissionByNo('SYS_DICT_ITM_TYPE',intArr)}</c:set>
	<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<%-- <button type="button" class="btn btn-default" onclick="javascript:window.location='${ctx}/basics/sysDictInfo/list?itType=${itType}'"><spring:message code="refresh"></spring:message></button> --%>
	<shiro:hasPermission name="basics:${permission_type}:add">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/basics/sysDictInfo/form?itType=${itType}'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
	</shiro:hasPermission>
	</div>
	
	<h3 class="page-title">
		<c:forEach items="${fns:getItemTypeByNo('SYS_DICT_ITM_TYPE',intArr)}" var="shows">
		<spring:message code="${shows}" />
	    </c:forEach>
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
	
				<div class="portlet-body">

					<div class="row"  >
						<form:form id="searchForm" modelAttribute="sysDictInfo" action="${ctx}/basics/sysDictInfo/list?itType=${itType}" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${sysDictInfo.isFilterAction}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							<!-- 搜索内容与操作按钮栏 -->
			 				<div class="col-md-12">
			 				
			 				           <c:if test="${intArr.size()>1}">
							 				  <div class="pull-left">	
							 				  <div class="input-group">
				                                 <div class="input-group-btn">
				                                   <form:select path="itemType" class="selectpicker select2 input-small">
				                                        <form:option value=""><spring:message code="item_type"/></form:option>
				                                        <c:forEach items="${fns:getDictList('SYS_DICT_ITM_TYPE')}" var="dict">
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
													<option value="itemCode"><spring:message code="item_code"></spring:message></option>
													<option value="itemValue"><spring:message code="config_content"></spring:message></option>
												</select>
                                            </div>
                                            
                                            <input id="intype" class="form-control input-medium" placeholder="<spring:message code='input'/> <spring:message code='item_code'></spring:message>" type="text" maxlength="60">
                                     
                                        </div>
	                               </div>
	                               
	                                <div class="pull-left">
	                               		 <button type="button" class="btn blue" onClick="page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
										 <button type="button" class="btn btn-default" onClick="resetx()" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                     <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
									</div>

									<div class="pull-right">
									<shiro:hasPermission name="basics:${permission_type}:edit">
                                        <button type="button" class="btn btn-default" onclick="cmdSysEdit('${ctx}/basics/sysDictInfo/form?itType=${itType}')">
                                            <i class="fa fa-edit"></i> <spring:message code="edit"/> </button>
                                    </shiro:hasPermission>         
                                    <shiro:hasPermission name="basics:${permission_type}:del">        
                                        <button type="button" class="btn btn-default" onclick="cmd('${ctx}/basics/sysDictInfo/delete?itType=${itType}')">
                                            <i class="fa fa-trash"></i> <spring:message code="delete"/> </button>
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
												value="<fmt:formatDate value="${sysDictInfo.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
												value="<fmt:formatDate value="${sysDictInfo.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
                                        </div>
			
									</div>
									
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label><spring:message code="edit_time"/>：</label>
                                            <input id="editBeginDate" name="editBeginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${sysDictInfo.editBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                        </div>
			
									</div>
									
									<div class="col-md-3">
										
										<div class="form-group">
                                            <label>&nbsp;</label>
                                            <input id="editEndDate" name="editEndDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
											value="<fmt:formatDate value="${sysDictInfo.editEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                                          
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
								   <th><input type="checkbox" id="selAll" class="ckboxs" onclick="selectAll()"></th>
								   <!-- <th>序号</th> -->
								   <th><spring:message code="item_code"/></th>
								   <th><spring:message code="config_content"/></th>
								   <th><spring:message code="desc"/></th>
								   <th><spring:message code="item_type"/></th>
								   <c:if test="${specType == null or specType!=3 }">
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
							<c:forEach items="${list}" var="sysDictInfo" varStatus="se">
											<tr id="${sysDictInfo.sysDictId}" pId="${sysDictInfo.parent.sysDictId ne 0?sysDictInfo.parent.sysDictId:0}">
												<td><input type="checkbox" class="ckbox" name="check" value="${sysDictInfo.sysDictId}" placeholder="${fns:getSysDictInfoById(sysDictInfo.sysDictId).isInitianlize}"></td>
												<%-- <td>${sysDictInfo.showSequence}</td> --%>
												<td nowrap><i class="icon-icon-tablet"></i><a href="${ctx}/basics/sysDictInfo/form?sysDictId=${sysDictInfo.sysDictId}&doAction=0">${sysDictInfo.itemCode}</a></td>
												<td>${sysDictInfo.itemValue}</td>
												<td title="${sysDictInfo.itemDesc}">${fns:abbr(sysDictInfo.itemDesc,15)}</td>
												<td><spring:message code='${fns:getDictLabel("SYS_DICT_ITM_TYPE",sysDictInfo.itemType,"0")}'/></td>
												<c:if test="${specType == null or specType!=3 }">
												<td><spring:message code='${fns:getDictLabel("INT_YES_NO",sysDictInfo.isLeaf,"0")}'/></td>
												</c:if>
												<td><c:if test="${sysDictInfo.sysDictCreator != null}">
												    ${fns:getUserById(sysDictInfo.sysDictCreator.id).name}
												    </c:if></td>
												<td><fmt:formatDate value="${sysDictInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<td><c:if test="${sysDictInfo.sysDictEditor != null}">
												    ${fns:getUserById(sysDictInfo.sysDictEditor.id).name}
												    </c:if></td>
												<td><fmt:formatDate value="${sysDictInfo.editTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												<%-- <td>
													<div class="btn-group btn-xs">
													    <a class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="operation"/><span class="caret"></span></a>
													      <ul class="dropdown-menu btn-xs">
													         <li><a href="${ctx}/basics/sysDictInfo/form?sysDictId=${sysDictInfo.sysDictId}&doAction=0&itType=${itType}"><spring:message code="show"/></a></li>
													         <shiro:hasPermission name="sys:dict:edit">
														        <li><a href="${ctx}/basics/sysDictInfo/form?sysDictId=${sysDictInfo.sysDictId}&itType=${itType}" onclick="return confirmx('您确认要修改该项配置吗？', this.href)"><spring:message code="edit"/></a></li>
													         </shiro:hasPermission>
													         <shiro:hasPermission name="sys:dict:edit">
													       	 	<li><a href="${ctx}/basics/sysDictInfo/delete?mulitId=${sysDictInfo.sysDictId}&itType=${itType}" onclick="return confirmx('您确认要删除该项配置吗？', this.href)"><spring:message code="delete"/></a></li>
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