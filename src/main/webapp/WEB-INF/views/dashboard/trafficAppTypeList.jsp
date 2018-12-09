<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>app类型</title>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/dashboard.css">
 <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/pagination.css">
 
 <script src="${pageContext.request.contextPath}/static/pages/scripts/jquery.pagination.js"></script>
 <script src="${ctxStatic }/global/plugins/tableExport-3.3.13/xlsx.core.js"></script>
 <script src="${ctxStatic }/global/plugins/tableExport-3.3.13/FileSaver.js"></script>
 <script src="${ctxStatic }/global/plugins/tableExport-3.3.13/tableexport.js"></script>
</head>
<body>
<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/traffic/appTypeList'"><i class="fa fa-refresh"></i></button>
	<button type="button" class="btn btn-default" onClick="javascript:window.location='${ctx}/dashboard/logChart'"><i class="fa fa-history"></i></button>
	</div>
	<h3 class="page-title">
		<spring:message code="App"></spring:message>
	</h3>
	<h5 class="page-header"></h5>
	<div class="row">
		<div id="chart"  style="width:98%;height: 510px; -moz-user-select: none; position: relative;"></div>
	</div>
	<div class="row pull-right">
		<button type="button" class="btn btn-default" id="export-btn"><i class="fa fa-download"> <spring:message code="export"/></i></button>
	</div>
	<br>
	<br>
    <div class="row">
       <table id="contentTable" class="table table-active table-striped table-bordered table-condensed text-nowrap">
         <thead>
           <tr>
             <th class="tl"><spring:message code="app"/></th>
             <th class="tl"><spring:message code="link_num"/></th>
             <th class="tl"><spring:message code="percentage"/> (<spring:message code="link_num"/>)</th>
             <th class="tl"><spring:message code="packets"/></th>
             <th class="tl"><spring:message code="percentage"/> (<spring:message code="packets"/>)</th>
             <th class="tl"><spring:message code="Gbyte"/></th>
             <th class="tl"><spring:message code="percentage"/> (<spring:message code="Gbyte"/>)</th>
           </tr>
         </thead>
         <tbody id="tableData"></tbody>
       </table>
		<div id="page"><div class="M-box" style="float: right"></div></div>
        <div class="none-data"><i class="fa fa-warning font-red-flamingo"></i>&nbsp;&nbsp;<spring:message code="noneData"/></div>
</div> 
</div>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/series-label.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/offline-exporting.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	ajaxProtocolList();
	
});
function ajaxProtocolList(){
	loading();
	$.ajax({
        url: '${ctx}/dashboard/traffic/appList',
        type: 'get',
        dataType: "json",
        async:false,
		timeout:10000,
        success:function (data){
        	if(data!=null&&data.length>0&&data[0].error!=null){
				  top.$.jBox.tip("<spring:message code='request_service_failed'/>", "<spring:message code='info'/>");
					return;
				}
            fileData =data;
            getPageData(1,10);//初始化第一页的数据
            pageJuan(10);//初始化分页
            protocolTypeChart(data);// 初始化图
        	closeTip();
        },
        error: function(data, textStatus, errorThrown){
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
    });
}
// 导出列表
$("#export-btn").click(function(){
	getPageData(1,999999);// 设置导出页条数
	var te = $("#contentTable").tableExport({
		headings:true,
		footers:true,
		formats:["xlsx"],
		fileName:"App",
		bootstrap:false
	});
	$("#myexport").click();
	$("caption").remove();
	getPageData(1,10); 
});
/**
 * 获取本页数据
 * @param currentPage 当前页数 【初次查数据，默认第1页】
 */
var fileData;
function getPageData(currentPage,pageNumber){
    if (typeof (fileData) != "undefined" &&  fileData != null) {
        //计算每页数据起始和终止数据编号
//         var pageNumber = 10;
        var maxLength = currentPage * pageNumber - 1;
        var minLength = currentPage * pageNumber - pageNumber;
        var pageData = [];
        for (var i = minLength; i < fileData.length; i++) {
            if (maxLength < i) {
                break;
            } else {
                pageData.push(fileData[i]);
            }
        }
        htmlData(pageData);
    }else{
        //把空数据传到页面中去
        htmlData(fileData);
    }
}
// 处理接口数据
function htmlData(fileDataS){
    $("#tableData").html("");
    if(fileDataS == null){
		$(".none-data").show();
        $('.M-box').hide();
    }else{
        $('.none-data').hide();
        $('.M-box').show();
        $.each(fileDataS,function (index,data){
        	if(data!=null){
        	var totalLink = data.totalLink;
        	var totalPackets= data.totalPackets;
        	var totalGByte= data.totalGByte;
        	var linkper =0;
        	var packper=0;
        	var gbytper=0;
        	if(totalLink!=null&&totalLink!=0 ){
        		linkper=((data.linkNum/totalLink)*100).toFixed(2);
        	}
        	if(totalPackets!=null&&totalPackets!=0 ){
        		packper=((data.packets/totalPackets)*100).toFixed(2);
        	}
        	if(totalGByte!=null&&totalGByte!=0 ){
        		gbytper=((data.GByte/totalGByte)*100).toFixed(2);
        	}
            var html = "<tr>";
            	html+= "<td class='tc'>"+data.appName+"</td>";
            	html+= "<td class='tc'>"+data.linkNum+"</td>";
            	html+= "<td class='tc'>"+linkper+"%"+"</td>";
            	html+= "<td class='tc'>"+data.packets+"</td>";
            	html+= "<td class='tc'>"+packper+"%"+"</td>";
            	html+= "<td class='tc'>"+data.GByte+"</td>";
            	html+= "<td class='tc'>"+gbytper+"%"+"</td>";
                html+="</tr>"
        	}
            $("#tableData").append(html);
        });
    }
}

/**
 * 分页控件处理
 */
function pageJuan(showData) {
    if (typeof (fileData) != "undefined" &&  fileData != null) {
        var totalData = fileData.length;
//         var showData = 10;
        if(showData > totalData){
            showData = totalData;
        }
        $('.M-box').pagination({
            totalData: totalData,
            showData: showData,
            coping: true,
            callback: function (index) {
                //改变显示开始和结束数据编号
                getPageData(index.getCurrent(),showData);
            }
        });
    }
}
// 比例协议统计图
function protocolTypeChart(rs){
	   //终端用户 分操作系统与浏览器 
		var data=new Array();
		$(rs).each(function(i, d) {
			data.push({
				name: d.appName,
				y: parseInt(d.GByte),
			});
	   });
		// 创建图例
		var chart = Highcharts.chart('chart',{
				chart: {
		    			plotBackgroundColor:null,
		    			plotBorderWidth:null,
		    			plotShadow:false,
						type: 'pie'
				},
				navigation: {
	    			buttonOptions: {
	    				x:-25,
	    			}
	    		},
				exporting: {
	    			allowHTML:true,
	    			filename:"App",
	    			scale:1,
	    			sourceWidth: 1280,
	    			sourceHeight: 500,
	    		},
				noData:{
					style: {//设置字体颜色
						color: '#000',
					},	
	    		},
// 				legend:{// 底部平鋪图例
// 					width:1280,
// 	    	    	x:40,
// 	    	    	itemWidth:100,
// 	    	    	itemDistance:2,
// 	    	    	itemHoverStyle:{
// 	    	    		color:'#61D2F7',
// 	    	    	},
// 	    	    },
	    	    legend: {
// 	    			layout: 'vertical',
	    			align: 'right',
	    			verticalAlign: 'middle',
	    			width:380,
	    	    	itemWidth:100,
	    	    	itemDistance:2,
	    	    	itemHoverStyle:{
	    	    		color:'#61D2F7',
	    	    	},
	    		},
// 				colors:['#f36f8a', '#44A9A8', '#ffff43','#25f3e6','#964CEC','#32B0ED','#2b6ed7','#7278DD','#2DA9D8','#C66FE6'],
				title: {
						text: null,
				},
				plotOptions: {
						series: {
							dataLabels: {
								enabled: true,
								softConnector: true,   // 是否使用曲线
								formatter: function () {
				                    // 通过函数判断是否显示数据标签，为了防止数据标签过于密集
				                    return this.percentage > 1 ? '<b>' + this.point.name + ' :</b> '  +this.percentage.toFixed(1)+' %'  : null;
				                },
				                style: {
									color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
							}
							},
						},
						pie:{
							allowPointSelect: true,
							showInLegend: true,	
							point: {
								events: {
									mouseOver: function(e) { 
											this.slice();
									},
									// 鼠标移出时，收回突出显示
									mouseOut: function() {
											this.slice();
									},
									// 默认是点击突出，这里屏蔽掉
									click: function() {
											return false;
									}
								},
							},
						}
				},
				credits:{//是否有highcharts水印
			        enabled:false
			    },
			    tooltip: {
					headerFormat: '{series.name}<br>',
					pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
				},
				series: [{
						name: "App",
						colorByPoint: true,
						data: data
				}],
		});
}
</script>
</body>
</html>