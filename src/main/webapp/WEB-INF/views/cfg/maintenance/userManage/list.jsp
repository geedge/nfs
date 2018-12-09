<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>

<script>
	 $(document).ready(function() {
		 if("${cfg.userName}"){
				$("#intype").val("${cfg.userName}");
			} else if("${cfg.remarks}"){
				$("#intype").val("${cfg.remarks}");
			} else  if("${cfg.serverIp}"){
				$("#intype").val("${cfg.serverIp}");
			} else{
				$("#intype").attr("placeholder","<spring:message code='input'/> "+$("#seltype").find("option:selected").text());
			}
		$("#seltype").change(function(){
			$("#intype").attr("placeholder","<spring:message code='input'/> "+$(this).find("option:selected").text());
		});
		//筛选功能初始化
		filterActionInit();
		$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#level").attr("value",'');
			$("#searchForm")[0].reset();
		});
		$("span[id^=close]").on("click",function(){
			var closeId=$(this).attr("id");
			var openId=$(this).attr("id").replace("close","open");
			$("#"+closeId).hide();
			$("#"+openId).show();
			$("#"+closeId).parent().parent().next("tr").hide();
		});
		
		if($("#exportType").val() != null && $("#exportType").val() != ""){
			 if($("#intype").val() != null && $("#intype").val() != ""){
				$("#exportValue").val($("#intype").val());
		  } 
		}

	}); 
	 
	
	 function dictDetail(serverIp,userName){
		$.ajax({
  			type:'post',
			url:'${ctx}/maintenance/userManage/userInfo',
			data:{"serverIp":serverIp,"userName":userName},
			dataType:'json',
			async:false,
			success:function(data){
				$("#userTable tbody").empty("");
					var html = ""
						html = "<div style='width:98%;overflow: auto;margin-top: 10px;height: 417px;margin-left: 1%;'>"
						html+="<table class='table table-bordered table-condensed text-nowrap' style='width: 100%;'>";
	      			  	html+="<thead>";
	      			  	html+="<th><spring:message code="user_name"/></th><th><spring:message code="vpn_ip"/></th><th><spring:message code="create_time"/></th><th><spring:message code="last_login"/></th><th><spring:message code="num_logins"/></th>";
	      			  	html+="<th><spring:message code="outgoing_unicast_packets"/></th><th><spring:message code="outgoing_unicast_total_size"/></th>";
	      			  	/* html+="<th><spring:message code="outgoing_broadcast_packets"/></th><th><spring:message code="outgoing_broadcast_total_size"/></th>"; */
	      			  	html+="<th><spring:message code="incoming_unicast_packets"/></th><th><spring:message code="incoming_unicast_total_size"/></th>";
	      			  	/* html+="<th><spring:message code="incoming_broadcast_packets"/></th><th><spring:message code="incoming_broadcast_total_size"/></th>"; */
	      			  	html+="</thead>";
	      			   	html+="<tbody>";
	      			   var user=data.user;
		      			   	for(i=0;i<user.length;i++){
		      			   		html+="<tr>";
		      			   		if(i==0){
		      			   		html+="<td class='taskStatus' rowspan='"+(user.length-1)+"'>"+userName;
		      					html+="</td>";
		      			   		}
		      					html+="<td class='taskStatus'>"+user[i].serverIp+"</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Create_on==null?"":user[i].Create_on);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Updated_on==null?"":user[i].Updated_on);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Number_of_Logins==null?"":user[i].Number_of_Logins);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Outgoing_Unicast_Packets==null?"":user[i].Outgoing_Unicast_Packets);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Outgoing_Unicast_Total_Size==null?"":user[i].Outgoing_Unicast_Total_Size);
		      					html+="</td>";
		      					/* html+="<td class='taskStatus'>"+(data[i].Outgoing_Broadcast_Packets==null?"":data[i].Outgoing_Broadcast_Packets);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(data[i].Outgoing_Broadcast_Total_Size==null?"":data[i].Outgoing_Broadcast_Total_Size);
		      					html+="</td>"; */
		      					html+="<td class='taskStatus'>"+(user[i].Incoming_Unicast_Packets==null?"":user[i].Incoming_Unicast_Packets);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(user[i].Incoming_Unicast_Total_Size==null?"":user[i].Incoming_Unicast_Total_Size);
		      					html+="</td>";
		      					/* html+="<td class='taskStatus'>"+(data[i].Incoming_Broadcast_Packets==null?"":data[i].Incoming_Broadcast_Packets);
		      					html+="</td>";
		      					html+="<td class='taskStatus'>"+(data[i].Incoming_Broadcast_Total_Size==null?"":data[i].Incoming_Broadcast_Total_Size);
		      					html+="</td>"; */
		      			   		html+="</tr>";
		      			   	}
		      				html+="</tbody>";
		      			 	html+="</table>";  
		      			 	html+="</div>";
					top.$.jBox(html,{width: $(document).width()*0.6,height: 480,title:"<spring:message code="user_info"/>", buttons:{"<spring:message code="close"/>":true}});
					if(data.message !=null && data.message !=""){
						var mes="<spring:message code="user"/> "+userName+" "+data.message+" <spring:message code="cgi_failed"/> <spring:message code="info_failed"/>";
						top.$.jBox.tip(mes);
					}
		 }
	  });
	} 
	
</script>
</head>
<body>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-primary" onClick="javascript:window.location='${ctx}/maintenance/userManage/form'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"/></button>
	</div>
	<h3 class="page-title">
		<spring:message code="user_manage"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" >
						<form:form id="searchForm" modelAttribute="cfg" action="${ctx}/maintenance/userManage/list" method="post" class="form-search">
							<input id="exportType" type="hidden" value="${cfg.seltype}"/>
							<input id="exportValue" type="hidden" value=""/>
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
						callback="page();" />
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${cfg.isFilterAction }"/>
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								<div class="pull-left">
									<div class="input-group">
                                         <div class="input-group-btn">
                                        	<form:select path="seltype" class="selectpicker select2 input-small" >
												<form:option value="userName"><spring:message code="user_name"></spring:message></form:option>
												<form:option value="serverIp"><spring:message code="vpn_ip"></spring:message></form:option>
												<form:option value="remarks"><spring:message code="desc"></spring:message></form:option>
											</form:select>
									
                                         </div>
                                         
                                         <input id="intype" class="form-control input-medium" type="text" value="">
                                  		 <div class="input-group-btn">
											 <form:select path="isValid" class="selectpicker select2 input-small" >
													<form:option value=""><spring:message code="select"/></form:option>
													<form:option value="1"><spring:message code="yes"/></form:option>
													<form:option value="0"><spring:message code="no"/></form:option>
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
							 		<shiro:hasPermission name="website:ssl:config">
							 			<sys:delRow url="${ctx}/maintenance/userManage/form" id="contentTable" label="update"></sys:delRow>
                                        <sys:delRow url="${ctx}/maintenance/userManage/delete?isValid=-1" id="contentTable" label="delete"></sys:delRow>
                                    </shiro:hasPermission>
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
									<th><spring:message code="user_name"/></th>
									<th><spring:message code="vpn_ip"/></th>
									<th><spring:message code="desc"/></th>
									<th><spring:message code="creator"/></th>
									<th class="sort-column a.create_time"><spring:message code="config_time"/></th>
									<th><spring:message code="editor"/></th>
									<th class="sort-column a.edit_time"><spring:message code="edit_time"/></th>
									<th width="15%"><spring:message code="operation"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list }" var="indexCfg" varStatus="status" step="1">
								<tr>
									<td>
									<input type="checkbox" class="i-checks child-checks" id="${indexCfg.id}"  isValid="${cfg.isValid}"  value="0" >
									</td>
								    <td>${indexCfg.userName }</td>
								    <c:set var="serverIp" value="${fn:substring(indexCfg.serverIp,0,(fn:length(indexCfg.serverIp)-1)) }"></c:set>
									<td class="no_substr" title="${serverIp }"><a href="javascript:dictDetail('${serverIp}','${indexCfg.userName}')">
									<c:if test="${fn:length(serverIp)>28}">
										${fn:substring(serverIp,0,28)}..
									</c:if>
									<c:if test="${fn:length(serverIp)<=28}">
										${serverIp }
									</c:if>
									</a></td>
									<td>${indexCfg.remarks }</td>
								    <td>${indexCfg.creatorName }</td>
									<td><fmt:formatDate value="${indexCfg.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${indexCfg.editorName }</td>
									<td><fmt:formatDate value="${indexCfg.editTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>
										<a href="javascript:showVpnIp('${indexCfg.id}','${serverIp}')"><i class="fa fa-plus"></i> <spring:message code="vpn_ip"/></a>
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
     <!-- vpn服务器Ip模板，start -->
<%@include file="/WEB-INF/views/cfg/maintenance/userManage/addIpModal.jsp" %>
</body>
</html>