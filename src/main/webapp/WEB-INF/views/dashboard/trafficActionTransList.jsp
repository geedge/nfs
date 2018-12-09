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
	<button type="button" class="btn btn-default" onClick="javascript:window.location.reload()"><i class="fa fa-refresh"></i></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/logChart'"><i class="fa fa-history"></i></button>
	</div>
	<h3 class="page-title">
		<spring:message code="${searchAction}"></spring:message>
	</h3>
	
	<div id="chart" style="width:97%;height:550px;"></div>
	
	<input id="searchAction" name="searchAction" type="hidden" value="${searchAction}"/>
			
</div>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/series-label.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/offline-exporting.js"></script>
<%-- <script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/themes/grid.js"></script> --%>
<script type="text/javascript">
$(document).ready(function(){
	actionTransAjax($("#searchAction").val());
});
// 局点信息
function showActionTransChart(rs){
	var data=new Array();
	var xData=new Array();
		xData=rs[0].statTime;
	var series=new Array();
	$(rs).each(function(i, d) {
		var entrance="";
		if(d!=null&&d.entranceId==1){
			entrance="Astana";
		}
		if(d!=null&&d.entranceId==2){
			entrance="Alamty";
		}
	series.push({
		name: entrance,
		data: d.count
	});
	})
	var chart = Highcharts.chart('chart', {
		exporting: {
			filename:'Action-Trans',
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
		},
	    yAxis: {
	        title: {
	            text: null
	        }
	    },
	    credits:{//是否有highcharts水印
	        enabled:false
	    },
// 	    legend: {
// 	        layout: 'vertical',
// 	        align: 'right',
// 	        verticalAlign: 'middle'
// 	    },
	    series: series,
	});
}
	// 动作一小时，间隔五分钟统计数据
	function actionTransAjax(searchAction){
		loading();
		$.ajax({
			url: "${ctx}/dashboard/traffic/actionEntranceTrans?searchAction="+searchAction,
			type : "get" ,
			dataType:"json",
			cache:false,
			async:true,
			success:function (rs) {
				showActionTransChart(rs);
				closeTip();
			},  
			error: function(data, textStatus, errorThrown){
				closeTip();
			},
			
		});
		 
	  }
</script>
</body>
</html>