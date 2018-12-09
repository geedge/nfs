<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>
	<spring:message code="bandwith"></spring:message>
</title>
</head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/bandwidth.css">
   

<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/traffic/bandwidthList'"><i class="fa fa-refresh"></i></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/logChart'"><i class="fa fa-history"></i></button>
	</div>
	<h3 class="page-title">
		<spring:message code="traffic"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
	
	<div class="row">
	
<!--   <div class="col-md-3" style="padding-right: 1px; ">
            <div class="panel panel-default">
                <div class="panel-body" style="height: 500px;">
                    <h5 class="ng-binding">Bandwidth Data</h5>
                    <div id="deviceRank" class="drank hm-scroll">
                    <table>
                        <tbody>
                        ngRepeat: item in rankItems
                        <tr class="dtr active" onClick="rankItemChanged(id, name)" style="">
                            <td class="dtd1"><span class="ng-binding" style="">Total</span></td>
                            <td class="dtd2"><span class="tz" style="width: 100%; max-width: 100px;"></span></td>
                            <td class="dtd3"><span class="dy ng-binding1">234567891</span></td>
                        </tr>
                        end ngRepeat: item in rankItems
                        <tr class="dtr" onClick="rankItemChanged(id, name)" style="">
                            <td class="dtd1"><span class="ng-binding">c2s</span></td>
                            <td class="dtd2"><span class="tz tz-from" style="width: 86.55%; max-width: 100px;"></span></td>
                            <td class="dtd3"><span class="dy ng-binding2">123456789</span></td>
                        </tr>
                        end ngRepeat: item in rankItems
                        <tr class="dtr" onClick="rankItemChanged(id, name)" style="">
                            <td class="dtd1"><span class="ng-binding">s2c</span></td>
                            <td class="dtd2"><span class="tz tz-to" style="width: 5.59%; max-width: 100px;"></span></td>
                            <td class="dtd3"><span class="dy ng-binding3">1234</span></td>
                        </tr>
                    </tbody></table>
                    </div>
                </div>
            </div>
        </div> -->
        
		<div class="col-md-12">
						<select id="unitType" class="selectpicker select2 input-small">
							<option value="Gbps">Gbps</option>
							<option value="pps">pps</option>
							<option value="linkNumber"><spring:message code="link_num"/>/S</option>
						</select>
                	<br>
                    <div id="trend" style="height: 480px; position: relative;" >
	                    <div style="position: relative;">
	                    		<div id="chart" style="width:97%;height:480px;"></div>
	                    </div>
                	</div>
		</div>
</div>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/bandwidth.css" type="text/css" />
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/series-label.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/offline-exporting.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var unitType=$("#unitType").val();
	changeBandwidth(unitType);
	$("#unitType").on("change",function(){
		changeBandwidth($("#unitType").val());
	});
	
	setInterval(function(){
		changeBandwidth($("#unitType").val());
		},500000);// 五分钟调用一次
});
function ajaxBandwidth(){
	
}
//点击列表显示统计图
function clickTrShowChart(response){
	$("tbody .dtr").on("click",function(){
		$("tbody tr").removeClass("active");
		$(this).addClass("active");
		showBandwidthChart();//
	})
}
// 默认显示四条线 ip46,tcp,udp
function rankItemChanged(addrType,transType){
	loading();
	var result=null;
	$.ajax({
		url:"${ctx}/dashboard/traffic/bandwidthTrans",
		type:"get",
		data:{"addrType":addrType,"transType":transType},
		dataType:"json",
		async:false,
		timeout:10000,
		success:function (data){
			if(data!=null){
				result=(data)
			}
			closeTip();
		},
		error: function(data, textStatus, errorThrown){
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
	return result;
}
// 根据单位切换数据
function changeBandwidth(unitType){
	var ipv4=rankItemChanged(4,null);
	var ipv6=rankItemChanged(6,null);
	var transTcp=rankItemChanged(null,6);
	var transUdp=rankItemChanged(null,17);
	var xdata=ipv4.statTime;
	if(unitType=="Gbps"){
		ipv4data=ipv4.gbps;
		ipv6data=ipv6.gbps;
		tcpdata=transTcp.gbps;
		udpdata=transUdp.gbps;
	}
	if(unitType=="pps"){
		ipv4data=ipv4.pps;
		ipv6data=ipv6.pps;
		tcpdata=transTcp.pps;
		udpdata=transUdp.pps;
		
	}
	if(unitType=="linkNumber"){
		ipv4data=ipv4.linkNum;
		ipv6data=ipv6.linkNum;
		tcpdata=transTcp.linkNum;
		udpdata=transUdp.linkNum;
	}
	var series=new Array();
	series.push({
		name: "IPv4",
		data: ipv4data
	},{
		name: "IPv6",
		data: ipv6data
	},{
		name: "TCP",
		data: tcpdata
	},{
		name: "UDP",
		data: udpdata
	});
	showBandwidthChart(unitType,xdata,series);
}
/* 网络带宽时间维度趋势图 */
function showBandwidthChart(unitType,xdata,ydata){
	var chart = Highcharts.chart('chart', {
	    title: {
	        text: null
	    },
	    navigation: {
			buttonOptions: {
				y: -15,
				x: 5
			}
		},
	    exporting: {
			filename:'Bandwidth',
			scale:1,
			sourceWidth: 1280,
			sourceHeight: 550,
		},
	    yAxis: {
	        title: {
	            text: unitType
	        }
	    },
	    legend: {
	    	enabled:true
	    },
		xAxis:{
			type:'datetime',
			categories: xdata,
			},
	  	credits:{//是否有highcharts水印
	        enabled:false
	    },
	    series: ydata,

	});
	
}

</script>
</body>
</html>