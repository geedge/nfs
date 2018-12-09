<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>数据概览</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/data_text.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/pages/css/dashboard.css">
    <script src="${pageContext.request.contextPath}/static/pages/scripts/dashboard.js"></script>

    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    
</head>

<body>
<!--content开始-->
<div class="data_content">
<!--header开始-->
<!--    <div class="header-title">
        <div class="title-left fl"></div>
        <div class="title-center fl"><a href="javascript:window.location.reload()"><i class="fa fa-line-chart"></i>&nbsp;网络流量分析</a></div>
        <div class="title-right fr"></div>
</div> -->
	<div class="header">
    <div class="data_info">
        <div class="info_1 fl_fc">
            <div class="text_1">
                <div class="fr_fc">
                 <div class="fl_visual">
                	<a href="${ctx}/dashboard/traffic/bandwidthList"><i class="fa fa-cloud-download"></i></a>
                 </div>
                    <div class="fl_fc">
						<p>
                        <label data-original-title="<spring:message code="traffic"/>" class="tooltips" data-flag="false" data-html="true" data-placement="top"> 
                         <!-- 网络带宽 --><spring:message code="traffic"/>
                        </label>
                        </p>
                 			<a href="${ctx}/dashboard/traffic/bandwidthList"><p class="csNum">In <span class="c2sNum">0</span> | Out <span class="s2cNum">0</span> </p>
                        	<span class="numberRun4">0</span > <span class="numberRun4-unit">bps</span></a>
                    </div>
                </div>
                <div class="fr_fc">
                	<div class="fl_visual">
                		<a href="${ctx}/dashboard/traffic/trafficBlockList?searchAction=block"><i class="fa fa-ban"></i></a>
                	 </div>
                    <div class="fl_fc">
						<p style="margin-left: 0px;width:100%">
                        <label data-original-title="<spring:message code="action_reject"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top"> 
                         <!-- 阻断链接数 --><spring:message code="action_reject"/>
                        </label>
                         </p>
                        <a href="${ctx}/dashboard/traffic/trafficBlockList?searchAction=block"><p class="numberRun1">0</p></a>
                    </div>
                </div>
            </div>
        </div>
      <div class="info_2 fl_fc">
            <div class="text_2">
             <div class="fr_fc">
             		<div class="fl_visual">
                		<a href="${ctx}/dashboard/traffic/trafficBlockList?searchAction=monitor"><i class="fa fa-eye"> </i></a>
                	 </div>
                    <div class="fl_fc">
                        <p data-original-title="<spring:message code="action_monit"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top"> 
                         <!-- 监测链接数 --><spring:message code="action_monit"/></p>
                        <a href="${ctx}/dashboard/traffic/trafficBlockList?searchAction=monitor"><p class="numberRun2">0</p></a>
                    </div>
                </div>
            	<div class="fr_fc">
            		<div class="fl_visual">
                		<i class="fa fa-trash"> </i>
                	 </div>
                    <div class="fl_fc">
<%--                      <a href="${ctx}/dashboard/ipActiveList"> --%>
                        <p data-original-title="<spring:message code="action_drop"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top"> 
                         <!-- 丢弃链接数 --><spring:message code="action_drop"/></p>
                        <p class="numberRun3">0</p>
                    </div>
                </div>
                <div class="fr_fc">
                	<div class="fl_visual">
                		<i class="fa fa-recycle"> </i>
                	 </div>
                    <div class="fl_fc">
<!--                     <a href="javacript:;"> -->
                        <p data-original-title="<spring:message code="action_loop"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top">
							<!-- 回流链接数 --><spring:message code="action_loop"/></p>
                        <p class="numberRun">0</p>
                    
                    </div>
                </div>
                
                 <div class="fr_fc">
                	<div class="fl_visual">
                		<i class="fa fa-link"> </i>
                	 </div>
                    <div class="fl_fc">
<!--                     <a href="javacript:;"> -->
                        <p data-original-title="<spring:message code="new_link"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top">
							<!-- 新建链接数 --><spring:message code="new_link"/></p>
                        <p class="numberRun5">0</p>
                    
                    </div>
                </div>
                 <div class="fr_fc">
                	<div class="fl_visual">
                		<i class="fa fa-heartbeat"> </i>
                	 </div>
                    <div class="fl_fc">
<!--                     <a href="javacript:;"> -->
                        <p data-original-title="<spring:message code="active_link"/>" 
                         class="tooltips" data-flag="false" data-html="true" data-placement="top">
							<!-- 活跃链接数 --><spring:message code="active_link"/></p>
                        <p class="numberRun6">0</p>
                    
                    </div>
                </div>
                
        </div>
    </div>
</div>
</div>
<!--header结束-->
<!-- 第一行 -->
    <div class="data_main">
        <div class="main_left fl">
            <div class="left_1">
                <div class="main_title">
                <spring:message code="traffic_ipactive_chart"/>&nbsp;&nbsp;<a href="${ctx}/dashboard/ipActiveList"><i class="fa fa-line-chart"></i></a> <a href="javascipt:void(0)" onclick="ipActiveList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
               <!-- 活跃IP图 --> <div id="chart_main" style="width:100%;height:400px;"></div>
            </div>
        </div>
        <!-- 协议类型图 -->
        <div class="main_center fl">
            <div class="center_text">
                <div class="main_title">
                    <spring:message code="traffic_protocol_chart"/>&nbsp;&nbsp;<a href="${ctx}/dashboard/traffic/protocolTypeList"><i class="fa fa-line-chart"></i></a>
                    <a href="javascipt:void(0)" onclick="protocolList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
                <div id="chart_1" class="" style="width:100%;height: 400px;"></div>
            </div>
        </div>
        <!-- 活跃端口 -->
        <div class="main_right fr">
            <div class="right_1">
                <div class="main_title_port">
                    <spring:message code="active_port"/>&nbsp;&nbsp; <!-- <a href="#"><i class="fa fa-list-ul"></i></a> -->
                    <a href="javascipt:void(0)" onclick="portActiveList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
                
                 <div class="main_table_port">
                    <table id="contentTable">
                        <thead>
                        <tr>
                            <th><spring:message code="port"/></th>
                            <th><spring:message code="visits"/></th>
                            <th><spring:message code="trend"/></th>
                        </tr>
                        </thead>
                        <tbody id="tbodyDataPort">
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
        
    </div>
    
<!-- 第2行 -->
    <div class="data_main1">
        <div class="main_left1 fl">
            <div class="left_1">
                <div class="main_title1">
                	<spring:message code="topic"/>&nbsp;&nbsp;<%-- <a href="${ctx}/dashboard/ipActiveList"><i class="fa fa-list-ul"></i></a> --%>
	              	<a href="javascipt:void(0)" onclick="topicAndDomainList();return false;"><i class="fa fa-refresh"></i></a>
	             </div>
	                <!-- 网站主题分类图 --><div id="chart_topic" style="width:100%;height:400px;"></div>
                 
                 
            </div>
        </div>
        
          <div class="main_center1 fl">
            <div class="bottom_web fl">
	            <div class="main_title_web">
                    <spring:message code="traffic_website_list"/>&nbsp;&nbsp;<%-- <a href="${ctx}/dashboard/webTypeList"><i class="fa fa-list-ul"></i></a> --%>
                    <a href="javascipt:void(0)" onclick="websiteList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
	             <div class="main_table_web">
                    <table>
                        <thead>
                        <tr>
                            <th><spring:message code="ranking"/></th>
                            <th><spring:message code="website"/></th>
                            <th><spring:message code="trend"/></th>
                        </tr>
                        </thead>
                        <tbody id="tbodyData2">
                        </tbody>
                    </table>
                </div>
	        </div>
        </div>
	        <div class="main_right1 fl">
            	<div class="bottom_web fl">
				    <%-- <div class="main_title_web">
				        <spring:message code="traffic_website_type_chart"/>
				    </div> --%>
				    <div class="main_table_web">
				        <!-- http网站分类图 --><div id="chart_4" class="" style="width:100%;height: 400px;"></div>
				    </div>
				</div>
        </div>
    </div>
    <!-- 第三行-->
    <div class="data_main1">
        <div class="main_left1 fl">
            <div class="left_1">
                <div class="main_title1">
                <spring:message code="traffic_app_chart"/>&nbsp;&nbsp;<a href="${ctx}/dashboard/traffic/appTypeList"><i class="fa fa-line-chart"></i></a>
                <a href="javascipt:void(0)" onclick="appTypeList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
                 <!--app应用图  --><div id="chart_3" class="" style="width:100%;height: 420px;"></div>
            </div>
        </div>
        
          <div class="main_center1 fl">
            <div class="bottom_web fl">
	            <div class="main_title_web">
                   <!-- 终端 -->BS &nbsp;&nbsp;<!-- <a href="#"><i class="fa fa-list-ul"></i></a> -->
	                <a href="javascipt:void(0)" onclick="uaSelectChange();return false;"><i class="fa fa-refresh"></i></a>
                </div>
	             <div class="main_table_web">
                    <table>
	                    <thead>
	                    <tr>
	                        <th><spring:message code="ranking"/></th>
	                        <th>
								 <div class="choice">
	                    			<select name="uaSelect" id="uaSelect">
	                       				<option value="system"><spring:message code="os"/></option>
	                        			<option value="browser"><spring:message code="browser"/></option>
	                    			</select>
	                			</div>
							</th>
	                        <th><spring:message code="trend"/></th>
	                    </tr>
	                    </thead>
	                    <tbody id="tbodyData1">
	                    </tbody>
	                </table>
                </div>
	        </div>
        </div>
	        <div class="main_right1 fl">
            	<div class="bottom_web fl">
				    <%-- <div class="main_title_web">
				        <spring:message code="traffic_website_type_chart"/>
				    </div> --%>
				    <div class="main_table_web">
				        <!-- ua终端图 --><div id="chart_2" class="" style="width:100%;height: 400px;"></div>
				    </div>
				</div>
        </div>
        
    </div>
    
	<!-- 第4行 -->
<%--     <div class="data_bottom">
	        <div class="bottom_1 fl">
	            <div class="main_title">
	                <!-- 终端 -->BS &nbsp;&nbsp;<!-- <a href="#"><i class="fa fa-list-ul"></i></a> -->
	                <a href="javascipt:void(0)" onclick="uaSelectChange();return false;"><i class="fa fa-refresh"></i></a>
	            </div>
	            <div class="main_table">
	                <table>
	                    <thead>
	                    <tr>
	                        <th><spring:message code="ranking"/></th>
	                        <th>
								 <div class="choice">
	                    			<select name="uaSelect" id="uaSelect">
	                       				<option value="system"><spring:message code="os"/></option>
	                        			<option value="browser"><spring:message code="browser"/></option>
	                    			</select>
	                			</div>
							</th>
	                        <th><spring:message code="trend"/></th>
	                    </tr>
	                    </thead>
	                    <tbody id="tbodyData1">
	                    <tr>
	                        <td>1</td>
	                        <td>windows</td>
	                        <td>0</td>
	                    </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>
        
        <div class="bottom_center fl">
            <div class="bottom_2 fl">
                <div class="main_title">
                    <spring:message code="BS"/>
                </div>
                <div class="main_table">
                    <!-- ua终端图 --><div id="chart_2" class="" style="width:100%;height: 400px;"></div>
                </div>
            </div>
            <div class="bottom_3 fl">
                <div class="main_title">
                    <spring:message code="traffic_website_list"/>&nbsp;&nbsp;<a href="${ctx}/dashboard/webTypeList"><i class="fa fa-list-ul"></i></a>
                </div>
                <div class="main_table">
                    <table>
                        <thead>
                        <tr>
                            <th><spring:message code="ranking"/></th>
                            <th><spring:message code="website"/></th>
                            <th><spring:message code="trend"/></th>
                        </tr>
                        </thead>
                        <tbody id="tbodyData2">
                        <tr>
                            <td>1</td>
                            <td>baidu.com</td>
                            <td>0</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
       
	        <div class="bottom_3 fl">
	                <div class="main_title">
	                <spring:message code="traffic_app_chart"/>&nbsp;&nbsp;<!-- <a href="#"><i class="fa fa-list-ul"></i></a> -->
                <a href="javascipt:void(0)" onclick="appTypeList();return false;"><i class="fa fa-refresh"></i></a>
                </div>
                 <!--app应用图  --><div id="chart_3" class="" style="width:100%;height: 420px;"></div>
	        </div>
	        </div>
    </div> --%>

    </div>





<!--content结束-->
<!-- 列表显示 -->
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/data.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/drilldown.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/no-data-to-display.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/sunburst.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/series-label.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/js/offline-exporting.js"></script>
<script src="${pageContext.request.contextPath}/static/pages/scripts/echart.js"></script>

<%-- <script src="${pageContext.request.contextPath}/static/global/plugins/highcharts/themes/grid.js"></script> --%>
<style>
	tr.select-row-tr:HOVER {
	background-color: #5d6062 !important;
	cursor: pointer;
}
	tr.select-row-tr:ACTIVE {
	background-color: #5d6062 !important;
}
.activeColor{
	background-color: #5d6062 !important;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//动态显示数据
	ajaxinfo();
	setInterval(function(){
	 ajaxinfo();
	},30000);
	
	protocolList();//协议统计
	ipActiveList();//活跃IP
	portActiveList();//活跃端口
	appTypeList();//应用类型
	systemList();//操作系统列表
	websiteList();//http网站列表
	topicAndDomainList();//主题网站域名
	

	uaSelectChange();
	//当选择改变时触发
	$('#uaSelect').change(function(){
		uaSelectChange();
	});


});
//获取数据info 
function ajaxinfo(){
// 	var c2s = $(".c2sNum").text().trim();
// 	var s2c = $(".s2cNum").text().trim();
	$.ajax({
		url:'${ctx}/dashboard/traffic/info',
		type : "get" ,
		dataType:'json',
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (data){
			var loopConnNum =loopConnNum= data.loopConnNum;
			var rejectNum =data.rejectNum;
			var monitorNum =data.monitorNum;
			var dropConnNum =data.dropConnNum;
			var newUniConnNum=data.newUniConnNum;
			var liveConnNum=data.liveConnNum;
			
			if(loopConnNum==null||loopConnNum==""){
				loopConnNum=0;
			}
			if(rejectNum==null||rejectNum==""){
				rejectNum=0;
			}
			if(monitorNum==null||monitorNum==""){
				monitorNum=0;
			}
			if(dropConnNum==null||dropConnNum==""){
				dropConnNum=0;
			}
			if(newUniConnNum==null||newUniConnNum==""){
				newUniConnNum=0;
			}
			if(liveConnNum==null||liveConnNum==""){
				liveConnNum=0;
			}
			dataScroll(".numberRun",Math.round(loopConnNum));
			dataScroll(".numberRun1",Math.round(rejectNum));
			dataScroll(".numberRun2",Math.round(monitorNum));
			dataScroll(".numberRun3",Math.round(dropConnNum));
			dataScroll(".numberRun5",Math.round(newUniConnNum));
			dataScroll(".numberRun6",Math.round(liveConnNum));
			
			//计算带宽进出口流量百分比
// 			$("#c2sColor").removeClass();
// 			$("#s2cColor").removeClass();
			var inoctetsNum=Math.round(data.inoctetsNum);
			var inoctetsNumK=inoctetsNum/1024;
			var inoctetsNumM=inoctetsNumK/1024;
			var inoctetsNumG=inoctetsNumM/1024;
			var outoctetsNum=Math.round(data.outoctetsNum);
			var outoctetsNumK=outoctetsNum/1024;
			var outoctetsNumM=outoctetsNumK/1024;
			var outoctetsNumG=outoctetsNumM/1024;
			var unit="bps";
			var bandwidth=inoctetsNum+outoctetsNum;
			if(inoctetsNumK>1||outoctetsNumK>1){
				inoctetsNum=inoctetsNumK.toFixed(2);
				outoctetsNum=outoctetsNumK.toFixed(2);
				unit="Kbps";
				};
			if(inoctetsNumM>1||outoctetsNumM>1){
				inoctetsNum=inoctetsNumM.toFixed(2);
				outoctetsNum=outoctetsNumM.toFixed(2);
				unit="Mbps";
				};
			if(inoctetsNumG>1||outoctetsNumG>1){
				inoctetsNum=inoctetsNumG.toFixed(2);
				outoctetsNum=outoctetsNumG.toFixed(2);
				unit="Gbps";
				};
			$(".c2sNum").text(inoctetsNum);
			$(".s2cNum").text(outoctetsNum);
			
			$(".numberRun4-unit").text(" "+unit);
			
			bandwidth=parseFloat(inoctetsNum)+parseFloat(outoctetsNum);
			dataScroll2(".numberRun4",bandwidth);
			/* if(data.inoctetsNum!=c2s){
			$(".c2sNum").text(data.c2sNum);
			$(".s2cNum").text(100-data.c2sNum);
			if(data.c2sNum>c2s){
			//c2s增长 fa fa-arrow-up
			$("#c2sColor").addClass("fa fa-arrow-up");
			$("#c2sColor").attr({style:"color:green"});
			$("#s2cColor").addClass("fa fa-arrow-down");
			$("#s2cColor").attr({style:"color:red"});
			}
			//c2s下降
			if(data.c2sNum<c2s){
			$("#c2sColor").addClass("fa fa-arrow-down");
			$("#c2sColor").attr({style:"color:red"});
			$("#s2cColor").addClass("fa fa-arrow-up");
			$("#s2cColor").attr({style:"color:green"});
				}
			} */
		},
	});
}
//动态显示数据
function dataScroll(index,nums){
	if(typeof nums=='undefined'||nums==null||nums==""){
		nums=0;
	}
	var num=nums;
	  var numRun = $(index).numberAnimate({num:1, speed:3000, symbol:","});
	    numRun.resetData(nums);
}
function dataScroll2(index,nums){
	if(typeof nums=='undefined'||nums==null||nums==""){
		nums=0;
	}
	var num=nums;
	  var numRun = $(index).numberAnimate({num:1, speed:3000, symbol:",",dot:2});
	    numRun.resetData(nums);
}

//切换下拉列表-操作系统与浏览器
function uaSelectChange(){
	var ua = $('#uaSelect').val();
	if(ua=="system"){
		systemList();
	}
	if(ua=="browser"){
		browserList();
	}
}

//协议类型统计
function protocolList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/protocol',
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
		  if(rs!=null&&rs.length>0&&rs[0].error!=null){
			  top.$.jBox.tip("<spring:message code='request_service_failed'/>", "<spring:message code='info'/>");
				return;
			}else{
				echart_1(rs);
			}
				
			closeTip(); 
		}, 
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
//活跃IP统计
function ipActiveList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/ipActive',
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			if(rs!=null&&rs.length>0&&rs[0].error!=null){
				  top.$.jBox.tip("<spring:message code='request_service_failed'/>", "<spring:message code='info'/>");
					return;
				}else{
					echart_main(rs);
				}
					
		closeTip();
		}, 
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
//活跃端口统计
function portActiveList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/portActive',  
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		beforeSend: function () {
			
			var msg = "OnLoading...";
			var trLen = $("#tbodyDataPort tr").length;
			if(trLen<=0) {
				msg = "OnLoading...";
			}else {
				$("#tbodyDataPort").html("")
			}

			var tr = "<tr class='frist rowData'>";
			tr += "<td class='list_c1' colspan='3' align='center' style='color:#fff'>"+msg+"</td>";
			
			$("#tbodyDataPort").prepend(tr);
		},
		success:function (rs) { 
			rs.reverse();
			  $("#tbodyDataPort").html("");
				var n=rs.length;
				if(rs!=null&&rs.length>0&&rs[0].error!=null){
					$("#tbodyDataPort").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'><spring:message code='request_service_failed'/></td><td></td></tr>");
				}else{
					if(n>0){
		
					   $(rs).each(function(i, itemObj) { 
						   var count= itemObj.sum;
						   var preCount = itemObj.preSum;
							var tr = "<tr class='frist rowData'>";
							tr += "<td class='list_c1' style='color:#fff' title='"+itemObj.port+"'>"+itemObj.port+"</td>";
							tr += "<td class='list_c2' style='color:#fff'>"+count+"</td>";
							if(preCount<count){
								tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-up' style='color:red'/>"+"</td></tr>";
							}else if(preCount>count){
								tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-down' style='color:green'/>"+"</td></tr>";
							}else{
								tr += "<td class='list_c3'>"+"--"+"</td></tr>";
							}
							$("#tbodyDataPort").prepend(tr);
					   })
					}else{
						 $("#tbodyDataPort").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'>No Data</td><td></td></tr>");
					}
				}
			
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }

//app应用类型统计
function appTypeList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/app',
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			if(rs!=null&&rs.length>0&&rs[0].error!=null){
				  top.$.jBox.tip("<spring:message code='request_service_failed'/>", "<spring:message code='info'/>");
					return;
				}else{
				echart_3(rs);
			}
				
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
//获取终端用户-操作系统列表 左下
function systemList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/osList',  
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		beforeSend: function () {
			
			var msg = "OnLoading...";
			var trLen = $("#tbodyData1 tr").length;
			if(trLen<=0) {
				msg = "OnLoading...";
			}else {
				$("#tbodyData1").html("")
			}

			var tr = "<tr class='frist rowData'>";
			tr += "<td class='list_c1' colspan='3' align='center' style='color:#fff'>"+msg+"</td>";
			
			$("#tbodyData1").prepend(tr);
		},
		success:function (rs) {  
		   $("#tbodyData1").html("");
		   rs.reverse();
			var n=rs.length;
			if(rs!=null&&rs.length>0&&rs[0].error!=null){
				$("#tbodyData1").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'><spring:message code='request_service_failed'/></td><td></td></tr>");
			}else{
				if(n>0){
	
				   $(rs).each(function(i, itemObj) {
					   var os="";
					   if(typeof(itemObj.osType)!="undefined"&&itemObj.osType!=null){
						   os=(itemObj.osType).toString();
						   if(os.length>18){
							   os=os.substring(0,18);
						   }
					   }
					   
					   var count= itemObj.count;
					   var preCount = itemObj.preCount;
						var tr = "<tr class='frist rowData select-row-tr' onclick='javascript:osClick(\""+itemObj.osType+"\",this);return false;'>";
						tr += "<td class='list_c1' style='color:#fff'>"+n+"</td>";
						tr += "<td class='list_c2' style='color:#fff' title='"+itemObj.osType+"'>"+os+"</td>";
						if(preCount<count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-up' style='color:red'/>"+"</td></tr>";
						}else if(preCount>count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-down' style='color:green'/>"+"</td></tr>";
						}else{
							tr += "<td class='list_c3'>"+"--"+"</td></tr>";
						}
						n=n-1;
						$("#tbodyData1").prepend(tr);
				   })
				}else{
					 $("#tbodyData1").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'>No Data</td><td></td></tr>");
				}
				//终端图-操作系统
				echart_2(rs.reverse());
			}
				
			closeTip();
		}, 
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
// 点击操作系统列表右侧显示 浏览器图
function osClick(osType,obj){
	//点击行变色
	$("#tbodyData1 tr").removeClass("activeColor");
	loading();
	$(obj).addClass("activeColor");
	
	$.ajax({
		url: '${ctx}/dashboard/browserChart',
		type : "get" ,
		data:{"osType":osType},
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			echart_5(rs.reverse());
			
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
}
//点击浏览器列表右侧显示 操作系统图
function bsClick(bsType,obj){
	$("#tbodyData1 tr").removeClass("activeColor");
	loading();
	$(obj).addClass("activeColor");
	$.ajax({
		url: '${ctx}/dashboard/systemChart',
		type : "get" ,
		data:{"bsType":bsType},
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			echart_2(rs.reverse());
			
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
}


//获取终端用户-浏览器列表
function browserList() {
	loading();
	$.ajax({
		url: '${ctx}/dashboard/bsList', 
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		beforeSend: function () {
			var msg = "OnLoading...";
			var trLen = $("#tbodyData1 tr").length;
			if(trLen<=0) {
				msg = "OnLoading...";
			}else {
				$("#tbodyData1").html("")
			}
			var tr = "<tr class='frist rowData'>";
			tr += "<td class='list_c1' colspan='3' align='center' style='color:#fff'>"+msg+"</td>";
			$("#tbodyData1").prepend(tr);
		},
		success:function (rs) { 
		   $("#tbodyData1").html("");
		   rs.reverse();
		   var n=rs.length;
		   if(rs!=null&&rs.length>0&&rs[0].error!=null){
				$("#tbodyData1").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'><spring:message code='request_service_failed'/></td><td></td></tr>");
			}else{
			   if(n>0){
				   $(rs).each(function(i, itemObj) {
					   var bs="";
					   if(typeof(itemObj.bsType)!="undefined"&&itemObj.bsType!=null){
						   bs=(itemObj.bsType).toString();
						   if(bs.length>18){
							   bs=bs.substring(0,18);
						   }
					   }
					   var count= itemObj.count;
					   var preCount = itemObj.preCount; 
						var tr = "<tr class='frist rowData select-row-tr' onclick='javascript:bsClick(\""+itemObj.bsType+"\",this);return false;'>";
						tr += "<td class='list_c1' style='color:#fff'>"+n+"</td>";
						tr += "<td class='list_c2' style='color:#fff' title='"+itemObj.bsType+"'>"+bs+"</td>";
						if(preCount<count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-up' style='color:red'/>"+"</td></tr>";
						}else if(preCount>count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-down' style='color:green'/>"+"</td></tr>";
						}else{
							tr += "<td class='list_c3'>"+"--"+"</td></tr>";
						}
						$("#tbodyData1").prepend(tr);
						n=n-1;
			   		})
			   	}else{
				   $("#tbodyData1").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'>No Data</td><td></td></tr>");
				}
		 		//终端图-浏览器
				echart_5(rs.reverse());
			}
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
//获取http网站table列表
function websiteList() {
	loading();
	$.ajax({
		url: '${ctx}/dashboard/websiteList',  
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		beforeSend: function () {
			var msg = "OnLoading...";
			var trLen = $("#tbodyData2 tr").length;
			if(trLen<=0) {
				msg = "OnLoading...";
			}else {
				$("#tbodyData2").html("")
			}

			var tr = "<tr class='frist rowData'>";
			tr += "<td class='list_c1' colspan='3' align='center' style='color:#fff'>"+msg+"</td>";
			
			$("#tbodyData2").prepend(tr);
		},
		success:function (rs) {
			rs.reverse();
		   $("#tbodyData2").html("");
		   var n=rs.length;
		   if(rs!=null&&rs.length>0&&rs[0].error!=null){
				$("#tbodyData2").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'><spring:message code='request_service_failed'/></td><td></td></tr>");
			}else{
			   if(n>0){
				   $(rs).each(function(i, itemObj) {
					   var web="";
					   if(typeof(itemObj.websiteService)!="undefined"&&itemObj.websiteService!=null){
							   web=(itemObj.websiteService).toString();
						   if(web.length>18){
							   web=web.substring(0,18);
						   }
					   }
						var tr = "<tr class='frist rowData select-row-tr' onclick='javascript:webClick(\""+itemObj.websiteServiceId+"\",this);return false;'>";
						tr += "<td class='list_c1' style='color:#fff'>"+n+"</td>";
						tr += "<td class='list_c2' style='color:#fff' title="+itemObj.websiteService+">"+web+"</td>";
						if(itemObj.preCount<itemObj.count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-up' style='color:red'/>"+"</td></tr>";
						}else if(itemObj.preCount>itemObj.count){
							tr += "<td class='list_c3'>"+"<i class='fa fa-arrow-down' style='color:green'/>"+"</td></tr>";
						}else{
							tr += "<td class='list_c3'>"+"--"+"</td></tr>";
						}
						
						$("#tbodyData2").prepend(tr);
						n=n-1;
				   })
			   }else{
				   $("#tbodyData2").prepend("<tr class='frist rowData'><td></td><td style='color:#fff'>No Data</td><td></td></tr>");
			}
		 	//网站统计图
			echart_4(rs.reverse());
			}
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
  }
//点击http网站列表-显示域名图
function webClick(websiteServiceId,obj){
	//点击行变色
	$("#tbodyData2 tr").removeClass("activeColor");
	loading();
	$(obj).addClass("activeColor");
	$.ajax({
		url: '${ctx}/dashboard/webTypeChart',
		type : "get" ,
		data:{"websiteServiceId":websiteServiceId},
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			echart_6(rs);
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
}
//网站主题
function topicAndDomainList(){
	loading();
	$.ajax({
		url: '${ctx}/dashboard/topicAndDomainList',
		type : "get" ,
		dataType:"json",
		cache:false,async:true,timeout:10000,//超时时间设置，查询接口时间过长超时
		success:function (rs) {
			//主题域名流量统计图
			echart_topic_domain(rs);
			closeTip();
		},
		complete:function(XMLHttpRequest,status){//超时设置
			closeTip();
		}
	});
}

</script>
</body>
</html>