<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>

<script type="text/javascript">
$(document).ready(function(){
	//筛选功能
	filterActionInit();
	//reset
	$("#resetBtn").on("click",function(){
		$("select.selectpicker").each(function(){
			$(this).selectpicker('val',$(this).find('option:first').val());
			$(this).find("option").attr("selected",false);
			$(this).find("option:first").attr("selected",true);
		});
		$(".Wdate").attr("value",'');
		$(':input','#searchForm')
		.not(':button,:submit,:reset,:hidden')
		.attr("value",'');
		$("#searchForm")[0].reset();
	});
	
});

</script>
</head>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	</div>
	<h3 class="page-title">
		<spring:message code="collect_voip"/>
	</h3>
	
	<h5 class="page-header"></h5>
		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row"  >
						<form:form id="searchForm" modelAttribute="log" action="${ctx}/log/ntc/ntcCollectVoipLogs" method="post" class="form-search">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<input id="functionId" name="functionId" type="hidden" value="${log.functionId}"/>
							<input id="isLogTotalSearch" name="isLogTotalSearch" type="hidden" value="${log.isLogTotalSearch}"/>
							<!-- 筛选按钮展开状态-->
							<input id="isFilterAction" name="isFilterAction" type="hidden" value="${log.isFilterAction }"/>
							
							<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
							
							<!-- 搜索内容与操作按钮栏 -->
							<div class="col-md-12">
								
							<div class="pull-left">	
				 				  <div class="input-group">
	                                 <div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="begin_date"/></span>
	                             	</div>
	                                 	<input id="searchFoundStartTime" name="searchFoundStartTime" type="text" readonly="readonly" class="form-control input-medium Wdate "
											value="${log.searchFoundStartTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
                        		</div>
							<div class="pull-left">	
				 				  <div class="input-group">
	                                 <div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="end_date"/></span>
	                             	</div>
	                                 	<input id="searchFoundEndTime" name="searchFoundEndTime" type="text" readonly="readonly" class="form-control input-medium Wdate "
											value="${log.searchFoundEndTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
										</div>
                        		</div>
	                               
	                               <div class="pull-left">
	                               		 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
										 <button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
	                                     <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"></spring:message>  <i class="fa fa-angle-double-down"></i></button>
									</div>

									<div class="pull-right">
                                        <a class="btn  btn-icon-only btn-default setfields tooltips" 
                                      		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                             <i class="icon-wrench"></i>
                                     </a>
                                    </div>
							</div>
							
							<!-- 筛选搜索内容栏默认隐藏-->
							<div class="col-md-12 filter-action-select-panle hide" >
								<div class="row">
									<div class="col-md-2">
										<div class="form-group">
										<label><spring:message code="voip_protocol"/>：</label>
										<c:set var="select"><spring:message code='select'/></c:set>
                                            <form:select path="voipProtocol" class="selectpicker form-control" >
												<form:option value=""><spring:message code="select"/></form:option>
												<c:forEach items="${fns:getDictList('VOIP_PROTOCOL')}" var="dict">
										 			<form:option value="${dict.itemCode}" ><spring:message code="${dict.itemValue}"/></form:option>
							             		</c:forEach>
											</form:select>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="clj_ip"/>：</label>
											<input id=capIp name="capIp" class="form-control ipv4v6" type="text" value="${log.capIp}"/>
									</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="rtp_d_ip"/>：</label>
											<input id="rtpDIp" name="rtpDIp" class="form-control ipv4v6" type="text" value="${log.rtpDIp}"/>
									</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="rtp_s_ip"/>：</label>
											<input id="rtpSIp" name="rtpSIp" class="form-control ipv4v6" type="text" value="${log.rtpSIp}"/>
									</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="sip_d_ip"/>：</label>
											<input id="sipDIp" name="sipDIp" class="form-control ipv4v6" type="text" value="${log.sipDIp}"/>
									</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label><spring:message code="sip_s_ip"/>：</label>
											<input id="sipSIp" name="sipSIp" class="form-control ipv4v6" type="text" value="${log.sipSIp}"/>
									</div>
									</div>
								</div>
							</div>
							<!-- /筛选搜索内容栏    结束-->
						</form:form>
					</div> 
					<sys:message content="${message}"/>
					<div class="table-responsive">
						<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap logTb">
						<thead>
							<tr>
								<th><spring:message code="log"/></th>
								<th class="sort-column pid"><spring:message code="pid"/></th>
								<th class="sort-column found_time"><spring:message code="found_time"/></th>
								<th class="sort-column recv_time"><spring:message code="recv_time"/></th>
								<th class="sort-column cap_ip"><spring:message code="clj_ip"/></th>
								<th class="sort-column voip_protocol"><spring:message code='voip_protocol'/></th>
								<th class="sort-column rtp_d_ip"><spring:message code="rtp_d_ip"/></th>
								<th class="sort-column rtp_s_ip"><spring:message code="rtp_s_ip"/></th>
								<th class="sort-column rtp_d_port"><spring:message code="rtp_d_port"/></th>
								<th class="sort-column rtp_s_port"><spring:message code="rtp_s_port"/></th>
								<th class="sort-column from_to_store_ip"><spring:message code="from_to_store_ip"/></th>
								<th class="sort-column from_to_store_url"><spring:message code="from_to_store_url"/></th>
								<th class="sort-column to_from_store_ip"><spring:message code="to_from_store_ip"/></th>
								<th class="sort-column to_from_store_url"><spring:message code="to_from_store_url"/></th>
								
								<th class="sort-column duation"><spring:message code='duation'/></th>
								<th class="sort-column sip_d_ip"><spring:message code='sip_d_ip'/></th>
								<th class="sort-column sip_s_ip"><spring:message code='sip_s_ip'/></th>
								<th class="sort-column sip_d_port"><spring:message code='sip_d_port'/></th>
								<th class="sort-column sip_s_port"><spring:message code='sip_s_port'/></th>
								<th class="sort-column call_id"><spring:message code='call_id'/></th>
								<th class="sort-column request_uri"><spring:message code='request_uri'/></th>
								<th class="sort-column calling_account"><spring:message code='calling_account'/></th>
								<th class="sort-column called_account"><spring:message code='called_account'/></th>
								
								<th class="sort-column contacts">Contacts</th>
								<th class="sort-column via">Via</th>
								<th class="sort-column route">Route</th>
								<th class="sort-column record_route">Record Route</th>
								<th class="sort-column user_agent">User Agent</th>
								<th class="sort-column server">Server</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="log" items="${page.list }" varStatus="status">
								<tr>
									<td>
										<a href="javascript:void(0)" name="viewLogInfo"><i class="icon-book-open"></i></a>
									</td>
									<td>${log.pid }</td>
									<td><fmt:formatDate value="${log.foundTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td><fmt:formatDate value="${log.recvTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td title="${log.capIp }">${fns:abbr(log.capIp, 42)}</td>
									<td>${log.voipProtocol }</td>
									<td>${log.rtpDIp }</td>
									<td>${log.rtpSIp }</td>
									<td>${log.rtpDPort }</td>
									<td>${log.rtpSPort }</td>
									<td>${log.fromToStoreIp }</td>
									<td>${log.fromToStoreUrl }</td>
									<td>${log.toFromStoreIp }</td>
									<td>${log.toFromStoreUrl }</td>
									<td>${log.duation }</td>
									<td>${log.sipDIp }</td>
									<td>${log.sipSIp }</td>
									<td>${log.sipDPort }</td>
									<td>${log.sipSPort }</td>
									<td>${log.callId }</td>
									<td>${log.requestUri }</td>
									<td>${log.callingAccount }</td>
									<td>${log.calledAccount }</td>
									<td>${log.contacts }</td>
									<td>${log.via }</td>
									<td>${log.route }</td>
									<td>${log.recordRoute }</td>
									<td>${log.userAgent }</td>
									<td>${log.server }</td>
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
