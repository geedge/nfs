<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>
	<spring:message code="traffic_ip_active"></spring:message>
</title>
</head>

<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/ipActiveList'"><i class="fa fa-refresh"></i></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/logChart'"><i class="fa fa-history"></i></button>
	</div>
	<h3 class="page-title">
		<spring:message code="traffic_ipactive_hour_trend"></spring:message>
	</h3>
	
	<div id="chart" style="width:95%;height:350px;"></div>
	
	<div class="col-md-12">
		<div class="portlet">
			<div class="portlet-body">
				<div class="row" >
					<form:form id="searchForm" modelAttribute="entry" action="${ctx}/traffic/ipActiveList" method="post" class="form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
				</div>
				<sys:message content="${message}"/>
				<h3 class="page-title">
					<spring:message code="traffic_ipactive_hour_max"></spring:message>
				</h3>
				<div class="pull-right">
					<button type="button" onClick="javascript:window.location='${ctx}/dashboard/export'" class="btn btn-default"><i class="fa fa-download"> <spring:message code="export"/></i></button> 
				</div>
				<br>
				<br>
				<div class="" align="center">
					<table id="contentTable"class="table table-striped table-bordered table-condensed text-nowrap">
						<thead>
							<tr>
								<th><spring:message code="ip_addr"/></th>
								<th><spring:message code="area_id"/></th>
								<th><spring:message code="link_num"/></th>
								<th><spring:message code="c2s_pkt_num"/></th>
								<th><spring:message code="s2c_pkt_num"/></th>
								<th><spring:message code="c2s_byte_num"/></th>
								<th><spring:message code="s2c_byte_num"/></th>
								<th><spring:message code="stat_time"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="entry" items="${ipActiveList }" varStatus="status">
								<tr>
									<td>${entry.ipAddr }</td>
									<td>
										<c:forEach items="${fns:getDictList('AREA_NAME')}" var="dic">
										<c:choose>
											<c:when test="${dic.itemCode eq entry.areaId}">
												<spring:message code="${dic.itemValue}"/>
											</c:when>
										</c:choose>
										</c:forEach>
									</td>
									<td>${entry.linkNum }</td>
									<td>${entry.c2sPktNum }</td>
									<td>${entry.s2cPktNum }</td>
									<td>${entry.c2sByteLen }</td>
									<td>${entry.s2cByteLen }</td>
									<td>${entry.statTime }</td>
<%-- 									<td><fmt:formatDate value="${entry.statTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
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
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/series-label.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/offline-exporting.js"></script>
<%-- <script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/themes/grid.js"></script> --%>
<script type="text/javascript">
$(document).ready(function(){
	ipActiveMinuteList()
	
});

function showIpActiveChart(rs){
	var data=new Array();
	var xData=new Array();
		xData=rs[0].statTime;
	var series=new Array();
	$(rs).each(function(i, d) {
	series.push({
		name: d.ipAddr,
		data: d.linkNum
	});
	})
	var chart = Highcharts.chart('chart', {
		exporting: {
			filename:'IP-Active',
			scale:1,
			sourceWidth: 1280,
			sourceHeight: 500,
		},
	    title: {
	        text: null
	    },
		xAxis: {
			type:'category',
			categories: xData,
			title: {
					text: 'time',
					align:'high',
			},
// 		labels:{
// 			formatter:function(){
// 				if(this.value.length>15){
// 					return '<span>'+this.value.substring(10,20)+"..."+'</span>'
// 				}else{
// 					return this.value
// 				}
// 			},
// 		}
			
	},
	    yAxis: {
	        title: {
	            text: 'bytes'
	        }
	    },
	    credits:{//是否有highcharts水印
	        enabled:false
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	    series: series,
	});
}
	//活跃IP一小时间隔五分钟统计
	function ipActiveMinuteList(){
		loading();
		$.ajax({
			url: '${ctx}/dashboard/ipActiveMinuteList',
			type : "get" ,
			dataType:"json",
			cache:false,async:true,
			success:function (rs) {
				showIpActiveChart(rs);
				closeTip();
			},  
			error: function(data, textStatus, errorThrown){
				closeTip();
//	 			warning_prompt("获取实时列表数据失败！",1500);
			},
			
		});
		 
	  }
</script>
</body>
</html>