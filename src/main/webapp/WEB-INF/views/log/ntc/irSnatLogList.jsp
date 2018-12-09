<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>
	IP<spring:message code="log"></spring:message>
</title>
<script>
	$(document).ready(function() {
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
		//筛选功能
		filterActionInit();
		
		//异步获取voip相关信息
		$("span[id^=open]").click(function(){
			var openId=$(this).attr("id");
			var closeId=$(this).attr("id").replace("open","close");
			var index=$(this).attr("id").replace("open","");
			$("#"+openId).hide();
			$("#"+closeId).show();
			var compileId=$(this).attr("compileId");
// 			var cfgId=$(this).attr("cfgId");
			if($("#"+openId).parent().parent().next("tr").hasClass("child")){
				$("#"+openId).parent().parent().next("tr").show();
			}else{
				$.ajax({
					type:'post',
					async:false,
					url:'${ctx}/ntc/iplist/ajaxSubList',
					data:{"compileId":compileId,"index":index},
					dataType:"html",
					success:function(data){
						var subTab="<tr class='child'>"+
					 	"<td colspan='"+($(".table tr").eq(0).children("th").length-1)+"'>";
						var html="";
						html+="<div class='row'>";
						html = html+data;
						subTab=subTab+html;
						subTab+="</td>";
						subTab+="</tr>";
						$("#"+openId).parent().parent().after(subTab);
						$("div[name='tabTitle"+index+"']").get(0).click();
					}
				});
			}
			
		});
		
		$("span[id^=close]").on("click",function(){
			var closeId=$(this).attr("id");
			var openId=$(this).attr("id").replace("close","open");
			$("#"+closeId).hide();
			$("#"+openId).show();
			$("#"+closeId).parent().parent().next("tr").hide();
		});
	});
</script>
</head>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	</div>
	<h3 class="page-title">
		<spring:message code="snat_multiplex"/>
	</h3>
	<h5 class="page-header"></h5>
	<div class="col-md-12">
		<div class="portlet">
			<div class="portlet-body">
				<div class="row" >
					<form:form id="searchForm" modelAttribute="log" action="${ctx}/log/ntc/irSnatLogs/list" method="post" class="form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="functionId" name="functionId" type="hidden" value="${log.functionId}"/>
						<input id="isLogTotalSearch" name="isLogTotalSearch" type="hidden" value="${log.isLogTotalSearch}"/>
						<!-- 筛选按钮展开状态-->
						<input id="isFilterAction" name="isFilterAction" type="hidden" value="${log.isFilterAction }"/>
						<sys:tableSort id="orderBy" name="orderBy.
						" value="${page.orderBy}" callback="page();" />
						<!-- 搜索内容与操作按钮栏 -->
						<div class="col-md-12">
							<div class="pull-left">
									<form:select path="service" class="selectpicker select2 input-small">
									<form:option value=""><spring:message code="action"/></form:option>
									<c:forEach items="${serviceList}" var="service"
											varStatus="satus">
										<c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
											<c:if test="${(dict.itemCode eq service.action) && (service.action ne 128) && (service.action ne 32) && (service.action ne 96) && (service.serviceId ne 37)&& (service.serviceId ne 149)  }">  
												<form:option value="${service.serviceId}"><spring:message code="${dict.itemValue}"/></form:option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</form:select>
							</div>
							<div class="pull-left">
								<div class="input-group">
                                	<div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="begin_date"/></span>
                                   	</div>
                                   	<input name="searchFoundStartTime" type="text" readonly="readonly" maxlength="20" class="form-control Wdate input-medium"
										value="${log.searchFoundStartTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                          		</div> 
                            </div>
							<div class="pull-left">
								<div class="input-group">
                                    <div class="input-group-btn">
										<span class="selectpicker form-control" ><spring:message code="end_date"/></span>
                                   	</div>
                                    <input name="searchFoundEndTime" type="text" readonly="readonly" maxlength="20" class="form-control Wdate input-medium"
										value="${log.searchFoundEndTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
                          		</div> 
                            </div>
							<div class="pull-left">
								<button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
								<button type="button" class="btn btn-default" id="resetBtn"> <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
                                <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/><i class="fa fa-angle-double-down"></i></button>
							</div>
							<div class="pull-right">
                               	<a class="btn btn-icon-only btn-default setfields tooltips" 
                                     	data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                    <i class="icon-wrench"></i>
                                </a>
                            </div>
						</div>
						<!-- 搜索内容与操作按钮栏 -->
						
						<!-- 筛选搜索内容栏默认隐藏-->
						<div class="col-md-12 filter-action-select-panle hide" >
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
                                		<label class="control-label"><spring:message code='transport_layer_protocol'/>：</label>
                                       	<form:select path="transProto" class="selectpicker form-control">
											<form:option value=""><spring:message code="select"/></form:option>
											<c:forEach items="${fns:getDictList('LOG_PROTOCOL')}" var="protocol" >
												<form:option value="${protocol.itemCode}"><spring:message code="${protocol.itemValue}"></spring:message></form:option>
											</c:forEach>
										</form:select>
									</div>
                                </div>
                                   
								<div class="col-md-2">
									<div class="form-group">
                                		<label class="control-label"><spring:message code="direction"/>：</label>
                                       	<form:select path="direction" class="selectpicker form-control">
											<form:option value=""><spring:message code="select"/></form:option>
											<c:forEach items="${fns:getDictList('LOG_DIRECTION')}" var="direction" >
												<form:option value="${direction.itemCode}"><spring:message code="${direction.itemValue}"></spring:message></form:option>
											</c:forEach>
										</form:select>
									</div>
                                </div>
								
								<div class="col-md-2">
									<div class="form-group">
										<label><spring:message code="entrance"/>：</label>
										<form:select path="entranceId" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="search">
											<form:option value=""><spring:message code="select"/></form:option>
											<c:forEach items="${fns:getDictList('ENTRANCE')}" var="entrance" >
												<form:option value="${entrance.itemCode}"><spring:message code="${entrance.itemValue}"></spring:message></form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<div class="col-md-2">
									<div class="form-group">
										<label><spring:message code="clj_ip"></spring:message>：</label>
										<input name="capIp" type="text" class="form-control" value="${log.capIp }"/>
									</div>
								</div>
								
								<div class="col-md-2">
									<div class="form-group">
										<label><spring:message code="clientip"></spring:message>：</label>
										<input name="sIp" type="text" class="form-control" value="${log.sIp }"/>
									</div>
								</div>
								
								<div class="col-md-2">
									<div class="form-group">
										<label><spring:message code="serverip"></spring:message>：</label>
										<input name="dIp" type="text" class="form-control" value="${log.dIp }"/>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<label><spring:message code="cfg_id"></spring:message>：</label>
									<input name="cfgId" type="text" class="form-control logCfgId number" value="${log.cfgId }"/>
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
								<th class="sort-column cfg_id "  isVisible="false" ><spring:message code="cfg_id"/></th>
								<th class="sort-column service" isVisible="false"><spring:message code="action"/></th>
								<th class="sort-column found_time"><spring:message code="found_time"/></th>
								<th class="sort-column recv_time" isVisible="false"><spring:message code="recv_time"/></th>
								<th class="sort-column entrance_id" isVisible="false"><spring:message code="entrance_id"/></th>
								
								<th class="sort-column nat_ip"><spring:message code="nat_ip"/></th>
								<th class="sort-column creat_time"><spring:message code="creat_time"/></th>
								<th class="sort-column close_time"><spring:message code='close_time'/></th>
								<th class="sort-column c2s_pkt_num"><spring:message code="c2s_pkt_num"/></th>
								<th class="sort-column s2c_pkt_num"><spring:message code="s2c_pkt_num"/></th>
								<th class="sort-column c2s_byte_num"><spring:message code="c2s_byte_num"/></th>
								<th class="sort-column s2c_byte_num"><spring:message code="s2c_byte_num"/></th>


								<th class="sort-column cap_ip" isVisible="false"><spring:message code="clj_ip"/></th>
								<th class="sort-column trans_proto"><spring:message code="protocol_type"/></th>
								<th class="sort-column addr_type"><spring:message code='addr_type'/></th>
								<th class="sort-column d_ip"><spring:message code="server_ip"/></th>
								<th class="sort-column s_ip"><spring:message code="client_ip"/></th>
								<th class="sort-column d_port"><spring:message code="server_port"/></th>
								<th class="sort-column s_port"><spring:message code="client_port"/></th>
								<th class="sort-column device_id" isVisible="false"><spring:message code="deviceid"/></th>
								<th class="sort-column link_id" isVisible="false"><spring:message code="link_id"/></th>
								<th class="sort-column encap_type"><spring:message code="encap_type"/></th>
								<th class="sort-column direction"><spring:message code="direction"/></th>
								<th class="sort-column inner_smac"><spring:message code="inner_smac"/></th>
								<th class="sort-column inner_dmac"><spring:message code="inner_dmac"/></th>
								<th class="sort-column stream_dir"><spring:message code="stream_type"/></th>
								
								<th class="sort-column addr_list"><spring:message code="nest_addr_list"/></th>
								<th class="sort-column server_locate"><spring:message code='server_locate'/></th>
								<th class="sort-column client_locate"><spring:message code='client_locate'/></th>
								<th class="sort-column s_asn" isVisible="false"><spring:message code='s_asn'/></th>
								<th class="sort-column d_asn" isVisible="false"><spring:message code='d_asn'/></th>
								<th class="sort-column s_subscribe_id" isVisible="false"><spring:message code='s_subscribe_id'/></th>
								<th class="sort-column d_subscribe_id" isVisible="false"><spring:message code='d_subscribe_id'/></th>
								<th class="sort-column user_region" isVisible="false"><spring:message code='user_region'/></th>
								<c:if test="${fns:getUser().isAdmin()}"><th class="sort-column scene_file"><spring:message code='scene_file'/></th></c:if>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="log" items="${page.list }" varStatus="status">
								<tr>
									<td>
									<%-- <span id="open${status.index}" class="log-open-cfg" compileId="${log.cfgId }"> ▷ </span><span  style="display: none" id="close${status.index}" > ▼ </span> --%>
										<a href="javascript:void(0)" name="viewLogInfo"><i class="icon-book-open"></i></a>
									</td>
									<td>${log.cfgId }</td>
									<td>
										<c:set var="actions">${log.action }</c:set>
										<c:forEach items="${fns:getDictList('SERVICE_ACTION')}" var="dict">
											<c:if test="${dict.itemCode eq log.action}">
												<c:set var="actions">${dict.itemValue}</c:set>
											</c:if>
										</c:forEach>
										<spring:message code="${actions}"/>
									</td>
									<td>${log.foundTime }</td>
									<td>${log.recvTime }</td>
									<td>
										<c:set var="entrances">${log.entranceId }</c:set>
										<c:forEach items="${fns:getDictList('ENTRANCE')}" var="dic">
											<c:if test="${dic.itemCode eq log.entranceId}">
												<c:set var="entrances">${dic.itemValue}</c:set>
											</c:if>
										</c:forEach>
										<spring:message code="${entrances}"/>
									</td>
									
									<td>${log.natIp }</td>
									<td>${log.creatTime }</td>
									<td>${log.closeTime }</td>
									<td>${log.c2sPktNum }</td>
									<td>${log.s2cPktNum }</td>
									<td>${log.c2sByteNum }</td>
									<td>${log.s2cByteNum }</td>
									
									<td title="${log.capIp }">${fns:abbr(log.capIp, 42)}</td>
									<td>
										<c:set var="transProtos">${log.transProto }</c:set>
										<c:forEach items="${fns:getDictList('LOG_PROTOCOL')}" var="dic">
											<c:if test="${dic.itemCode eq log.transProto}">
												<c:set var="transProtos">${dic.itemValue}</c:set>
											</c:if>
										</c:forEach>
										<spring:message code="${transProtos}"/>
									</td>
									<td>
									<c:set var="addrTypes">${log.addrType }</c:set>
										<c:forEach items="${fns:getDictList('IP_TYPE')}" var="dic">
											<c:if test="${log.addrType==dic.itemCode}">
											<c:set var="addrTypes">${dic.itemValue}</c:set>
											</c:if>
				               			</c:forEach>
				               			<spring:message code="${addrTypes}"/>
									</td>
									<td title="${log.dIp }">${fns:abbr(log.dIp, 42)}</td>
									<td title="${log.sIp }">${fns:abbr(log.sIp, 42)}</td>
									<td>${log.dPort }</td>
									<td>${log.sPort }</td>
									<td>
										<c:set var="deviceIds">${log.deviceId }</c:set>
										<c:forEach items="${fns:getDictList('DEVICE')}" var="device">
											<c:if test="${device.itemCode eq log.deviceId}">
											<c:set var="deviceIds">${device.itemValue}</c:set>
											</c:if>
					                	</c:forEach>
					                	<spring:message code="${deviceIds}"/>
									</td>
									<td>
										<c:set var="linkIds">${log.linkId }</c:set>
										<c:forEach items="${fns:getDictList('LINK')}" var="link">
											<c:if test="${link.itemCode eq log.linkId}">
											<c:set var="linkIds">${link.itemValue}</c:set>
											</c:if>
					                	</c:forEach>
					                	<spring:message code="${linkIds}"/>
									</td>
									<td>
										<c:set var="encapTypes">${log.encapType }</c:set>
										<c:forEach items="${fns:getDictList('ENCAP_TYPE')}" var="encapType">
											<c:if test="${encapType.itemCode eq log.encapType}">
											<c:set var="encapTypes">${encapType.itemValue}</c:set>
											</c:if>
					                	</c:forEach>
					                	<spring:message code="${encapTypes}"/>
									</td>
									<td>
									<c:set var="directions">${log.direction }</c:set>
										<c:forEach items="${fns:getDictList('LOG_DIRECTION')}" var="direction">
											<c:if test="${direction.itemCode eq log.direction}">
											<c:set var="directions">${direction.itemValue}</c:set>
											</c:if>
					                	</c:forEach>
					                	<spring:message code="${directions}"/>
									</td>

									<td>${log.innerSmac }</td>
									<td>${log.innerDmac }</td>
									<td>
										<c:set var="streamDirs">${log.streamDir }</c:set>
										<c:forEach items="${fns:getDictList('LOG_STREAM_TYPE')}" var="streamType">
											<c:if test="${streamType.itemCode eq log.streamDir}">
											<c:set var="streamDirs">${streamType.itemValue}</c:set>
											</c:if>
					                	</c:forEach>
					                	<spring:message code="${streamDirs}"/>
									</td>
									<td>${log.addrList }</td>
									<td>${log.serverLocate}</td>
									<td>${log.clientLocate}</td>
									<td>${log.sAsn}</td>
									<td>${log.dAsn}</td>
									<td>${log.sSubscribeId}</td>
									<td>${log.dSubscribeId}</td>
									<td>${log.userRegion}</td>
									<c:if test="${fns:getUser().isAdmin()}"><td>${log.sceneFile}</td></c:if>
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