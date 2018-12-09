<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>网站主题服务分类</title>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/dashboard.css">
</head>

	<style type="text/css">
	
	</style>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/webTypeList'"><spring:message code="refresh"/></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/logChart'"><spring:message code="back"/></button>
	</div>
	<h3 class="page-title">
		<spring:message code="网站分类"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/dashboard/ipActiveList">网站</a></li>
			<li><a href="${ctx}/dashboard/ipActiveList">主题</a></li>
			<li><a href="${ctx}/dashboard/ipActiveList">服务</a></li>
		</ul>
	
	
	
	
	
	<%-- 
	<embed width="700px" height="500px" src="${pageContext.request.contextPath}/static/global/plugins/highcharts/svg.svg"></embed>
	 --%>
	
	
	
	
	
	
	
	
	<!--  -->
	<div id="chart" style="width:100%;height:400px;"></div>
	
	<div class="col-md-12">
		<div class="portlet">
			<div class="portlet-body">
			
				<div class="row" >
					<form:form id="searchForm" modelAttribute="entry" action="${ctx}/dashboard/webTypeList" method="post" class="form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
				</div>
				<style>
					.show-two{
						display: inline-block;
					}
					.title-num{
						margin-right:8px;
						border-radius:40px !important;
						background-color: #f34b60;
						color:#fff;
						width:40px;
						text-align: center;
						font-size: 20px;
					}
					.tbody .tr:active{
						background: rgb(210, 232, 251, 0.8);
					}
				</style>
				<script type="text/javascript">
				$(function(){
					$("#contentTable tbody tr").bind('click',function(){
						$("tbody tr").removeClass("active");
						$(this).addClass("active");
						$("span[class='title-num']").html('&nbsp;'+($(this).index()+1)+'&nbsp;')
					});
				});
				</script>
				<sys:message content="${message}"/>
				<div class="table-responsive show-two">
					<table id="contentTable" style="width: 35%;" class="table table-hover table-striped table-bordered table-condensed text-nowrap">
						<thead>
							<tr>
								<th><spring:message code="sort"/></th>
								<th><spring:message code="网站"/></th>
<%-- 								<th><spring:message code="area_id"/></th> --%>
								<th><spring:message code="访问次数"/></th>
								<th><spring:message code="c2s_pkt_num"/></th>
								<th><spring:message code="s2c_pkt_num"/></th>
								<th><spring:message code="c2s_byte_len"/></th>
								<th><spring:message code="s2c_byte_len"/></th>
								<th><spring:message code="stat_time"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="entry" items="${page.list }" varStatus="status">
								<tr>
									<td>${entry.id }</td>
									<td>${entry.ipAddr }</td>
<%-- 									<td>${entry.areaId }</td> --%>
									<td>${entry.linkNum }</td>
									<td>${entry.c2sPktNum }</td>
									<td>${entry.s2cPktNum }</td>
									<td>${entry.c2sByteLen }</td>
									<td>${entry.s2cByteLen }</td>
									<td><fmt:formatDate value="${entry.statTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="page">${page}</div>
				</div>
				<div class="right-table" style="width: 30%;float: right;" >
				<h3>
					<span class="title-num">&nbsp;1 </span>
					<label>分类流量统计</label>
				</h3>
					<table id="contentTable2" class="table table-striped table-bordered table-condensed text-nowrap">
						<tbody>
								<tr><td><spring:message code="新闻"/></td><td>12131</td></tr>
								<tr><td><spring:message code="军事"/></td><td>241241</td></tr>
								<tr><td><spring:message code="娱乐"/></td><td>31412</td></tr>
								<tr><td><spring:message code="游戏"/></td><td>34655</td></tr>
								<tr><td><spring:message code="科技"/></td><td>37457</td></tr>
								<tr><td><spring:message code="。。"/></td><td>33457</td></tr>
								<tr><td><spring:message code="。。"/></td><td>38468</td></tr>
								<tr><td><spring:message code="。。"/></td><td>3367</td></tr>
								<tr><td><spring:message code="。。"/></td><td>33456</td></tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var chart = Highcharts.chart('chart', {
	    title: {
	        text: '网站分类'
	    },
	    yAxis: {
	        title: {
	            text: 'bps'
	        }
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
		xAxis:{
			type:'datetime',
			dateTimeLabelFormats:{
				day:'%Y-%m-%d %h'
				}
			},
	    plotOptions: {
	        series: {
	            pointStart: Date.UTC(2018, 8, 16,1),
	            pointInterval: 3600*1000,
// 	            pointIntervalUnit:'day'
	        }
	    },

	    series: [{
	        name: 'Installation',
	        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
	    }],

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }

	});
	
});

function showIpActiveChart(){
	
}

</script>
</body>
</html>