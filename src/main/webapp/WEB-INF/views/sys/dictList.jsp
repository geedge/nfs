<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>字典管理</title>
<style type="text/css">
	
	.revisionBox {
		margin: 5px;
	}
	
	span.label.label-success{
		width: 250px;
	}
	.popover {
	  max-width: 800px;
	}
	
	.btn.purple-stripe {
	    border-left: 3px solid #852B99;
	}

</style>
<script type="text/javascript">

	$(document).ready(function() {
		
		$(".revision").popover('destroy');
		$(".revision").popover({
			animation:true,
			html:true,
			placement:'left',
			title:'<spring:message code="edit_record"/>',
			content:function(){
				var html = []; 
				var content = $(this).attr("data-content");
				if(!(content=="" || content==null)){
					var strs = content.split("|");
  					for(i=0;i<strs.length;i++){
  						if(!(strs[i]=="" || strs[i]==null)){
  						 	html.push('<span class="label label-success">'+strs[i]+'</span>'); 
  							html.push('\t');
  							if((i+1)%2==0){
  							 	html.push('<br/>'); 
  							}
  						}
  					}
				}else {
					html.push('<span class="label label-success"><spring:message code="nochange"/></span>'); 
				}
				return html.join('');
				
			}			
		});
	});
	
	function dictDetail(dictId){
		$.ajax({
  			type:'post',
			url:'${ctx}/sys/dict/loadDataDict',
			data:{"id":dictId},
			dataType:'json',
			async:false,
			success:function(data,textStatus){
				if(textStatus=="success"){

					var revisions = []; 
      					var content = data.revision;
      					if(!(content=="" || content==null)){
      						var strs = content.split("|");
	      					for(i=0;i<strs.length;i++){
	      						if(!(strs[i]=="" || strs[i]==null)){
	      						 	revisions.push('<span class="label label-success" style="width: 250px;">'+strs[i]+'</span>'); 
	      							 revisions.push('\t');
	      							 if((i+1)%2==0){
	      							 	 revisions.push('<br/>'); 
	      							 }
	      						}
	      					}
      					}else {
      						revisions.push('<span class="label label-success"><spring:message code="nochange"/></span>'); 
      					}
      					
      					
					
					
					var html = ""
      					html+="<div style='font-size: 14px;'>";
		      			html+="<table class='table table-bordered table-condensed'>";
		      			html+="<tbody>";
		      			
		      			html+="<tr>";
		      			html+="<th><spring:message code="module_name"/>：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.moduleName;
		      			html+="</td>";
						html+="</tr>";
		      			html+="<tr>";
		      			html+="<th><spring:message code="dict_mark"/>：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.mark;
		      			html+="</td>";
						html+="</tr>";
		      			
						
						html+="<tr>";
		      			html+="<th><spring:message code="run_state"/>：";
		      			html+="</th>";
		      			html+="<td colspan='3'>";
		      			 	html+="<table class='table table-bordered table-condensed'>";
		      			  	html+="<thead>";
		      			  		html+="<th><spring:message code="mark"/></th><th><spring:message code="value"/></th><th><spring:message code="desc"/></th><th><spring:message code="is_useable"/></th><th><spring:message code="is_maintain"/></th>";
		      			   	html+="</thead>";
		      			   	html+="<tbody>";
		      			   	for(i=0;i<data.dictItemList.length;i++){
		      			   		html+="<tr>";
		      			   		html+="<td class='taskStatus'>"+data.dictItemList[i].itemCode;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+data.dictItemList[i].itemValue;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+data.dictItemList[i].itemDesc;
		      					html+="</td>";
		      					html+="<td class='taskStatus'>";
		      					if(data.dictItemList[i].status==1){
		      						html+="<span class='in-progress'><spring:message code="yes"/></span>";
		      					}else {
		      						html+="<span class='pending'><spring:message code="no"/></span>";
		      					}	
		      					html+="</td>";
		      					html+="<td class='taskStatus'>";
		      					if(data.dictItemList[i].type==1){
		      						html+="<span class='in-progress'><spring:message code="yes"/></span>";
		      					}else {
		      						html+="<span class='pending'><spring:message code="no"/></span>";
		      					}	
		      					html+="</td>";
		      			   		html+="</tr>";
		      			   		
		      			   		
		      			   	}
		      			   	html+="</tbody>";
		      			 	html+="</table>";
                		 
		      			html+="</td>";
						html+="</tr>";
						
						
						
						html+="<tr>";
		      			html+="<th><spring:message code="create_time"/>：";
		      			html+="</th>";
		      			html+="<td>"+data.createTime;
		      			html+="</td>";
		      			html+="<th><spring:message code="last_edit_time"/>：";
		      			html+="</th>";
		      			html+="<td>"+data.modifyTime;
		      			html+="</td>";
						html+="</tr>";
						
						html+="<tr>";
		      			html+="<th><spring:message code="desc"/>：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+data.remark;
		      			html+="</td>";
						html+="</tr>";
						
						
						html+="<tr>";
		      			html+="<th><spring:message code="dict_edit_record"/>：";
		      			html+="</th>";
		      			html+="<td colspan='3'>"+revisions.join('');
		      			html+="</td>";
						html+="</tr>";
						html+="</tbody>";	              
						html+="</table>";
						html+="</div>";  
						
						top.$.jBox(html,{width: $(document).width()*0.6,height: 480,title:"<spring:message code="dict_detail"/>", buttons:{"关闭":true}});
						
			
				
							
				}
					 
			}
	  
	  });

	}	
	
	
	function page(n,s){
		if(n) $("#pageNo").val(n);
		if(s) $("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/sys/dict/list");
		$("#searchForm").submit();
    	return false;
    }
	
	
	
</script>
</head>
<body>
	<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-primary" onClick="javascript:window.location='${ctx}/sys/dict/form'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"/></button>
	</div>
	
	<h3 class="page-title">
		<spring:message code="dictManage"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
	<div class="row">
	<div class="col-md-12">
	<div class="portlet">
	<div class="portlet-body">
	<div class="row"  >
	<form:form id="searchForm" action="${ctx}/sys/dict/list" method="post" class="form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<!-- 搜索内容与操作按钮栏 -->
		<div class="col-md-12 filter-action-select-panle" style="background-color:transparent">
			<div class="row">
				<div class="col-md-2">
							
					 <div class="form-group">
					 	<label><spring:message code="module_name"/>：</label>
					 	<input id="moduleName" name="moduleName" type="text" maxlength="50" class="form-control" value="${sysDataDictionaryName.moduleName}"/>
					 </div>		
				</div>	
				
				<div class="col-md-2">
							
					 <div class="form-group">
					 	<label><spring:message code="dict_mark"/>：</label>
					 	<input id="mark" name="mark" type="text" maxlength="50" class="form-control" value="${sysDataDictionaryName.mark}"/>
					 </div>		
				</div>	
				
				<div class="col-md-2">
							
					 <div class="form-group">
					 	<label><spring:message code="create_time"/>：</label>
					 	<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
							value="<fmt:formatDate value="${sysDataDictionaryName.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
					 </div>		
				</div>	
				
				<div class="col-md-2">
							
					 <div class="form-group">
					 	<label>&nbsp;</label>
					 	<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control Wdate"
							value="<fmt:formatDate value="${sysDataDictionaryName.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
					 </div>		
				</div>	
				
				<div class="col-md-1">
					<div class="form-group">
						<label>&nbsp;</label>
						<button type="submit" class="btn blue form-control"><i class="fa fa-search"></i><spring:message code="search" /></button>
					
					</div>
				</div>
	
			</div>
		</div>
	</form:form>
	</div>
	
	<sys:message content="${message}"  />
	
	<table id="contentTable1" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column module_name" width="15%"><spring:message code="module_name"/></th>
				<th class="sort-column mark"  width="20%"><spring:message code="dict_mark"/></th>
				<th class="sort-column create_time" width="15%"><spring:message code="create_time"/></th>
				<th  width="17%"><spring:message code="desc"/></th>
				<th  width="18%"><spring:message code="edit_record"/></th>
				<shiro:hasPermission name="sys:dict:edit">
				<th width="15%"><spring:message code="operation"/></th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="dict">
			<tr>
				
				<td>${dict.moduleName}</td>
				<td>${dict.mark}</td>
				<td><fmt:formatDate value="${dict.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td title="${dict.remark }">${fns:abbr(dict.remark,15)}</td>
				<td>
					<button class="btn revision popovers purple-stripe" data-content="${dict.revision}"><spring:message code="edit_record_view"/></button>
				</td>
				
				<td>
	
					<div class="btn-group">
				      <a class="btn btn-primary" href="#"><i class="icon-cogs"></i> <spring:message code="operation"/></a>
				      <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
				      <ul class="dropdown-menu">
				      
			        	<li><a href="javascript:dictDetail('${dict.id}')"><i class="icon-list"></i> <spring:message code="detail"/></a></li>
			        	<li class="divider"></li>
				       
				        <shiro:hasPermission name="sys:dict:edit">
					        <li><a href="${ctx}/sys/dict/form?id=${dict.id}" onclick="return confirmx('<spring:message code="dict_edit_info"/>', this.href)"><i class="fa fa-edit"></i> <spring:message code="edit"/></a></li>
					        <li class="divider"></li>
				        </shiro:hasPermission>
				        <shiro:hasPermission name="sys:dict:edit">
				       	 	<li><a href="${ctx}/sys/dict/delete?id=${dict.id}" onclick="return confirmx('<spring:message code="dict_delete_info"/>', this.href)"><i class="icon-trash"></i> <spring:message code="delete"/></a></li>
						</shiro:hasPermission>
				    
				      </ul>
				    </div>
				    
				</td>
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
	</div>
</body>
</html>