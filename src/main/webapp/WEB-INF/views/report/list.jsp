<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<script src="${ctxStatic }/global/plugins/tableExport-3.3.13/xlsx.core.js"></script>
<script src="${ctxStatic }/global/plugins/tableExport-3.3.13/FileSaver.js"></script>
<script src="${ctxStatic }/global/plugins/tableExport-3.3.13/tableexport.js"></script>
<title><spring:message code="${bean.cfgName}"></spring:message></title>
<%@ include file="/WEB-INF/include/form/pageGroup.jsp"%>
<script>
var pato=1;
var size=30;
$(document).ready(function() {
		//筛选功能初始化
		filterActionInit();
		//日期类型初始化
		changeReportType("${bean.reportType}");
		$("#reportType").change(function(){
			changeReportType($("#reportType").val());
		});
		//reset
		$("#resetBtn").on("click",function(){
			$("select.selectpicker").each(function(){
				$(this).selectpicker('val',$(this).find('option:first').val());
				$(this).find("option").attr("selected",false);
				$(this).find("option:first").attr("selected",true);
			});
			$(".Wdate").attr("value",'');
			$("#searchForm")[0].reset();
		});
		if(!"${bean.reportBusinessType}"){//default
			$("#reportBusinessType").val("lwhh_report");
			ajaxReport("/report/ajaxNtcLwhhReport","#lwhh");
		}else if("${bean.reportBusinessType}"=="label_report"){
			ajaxReport("/report/ajaxNtcTagReport","#label");
		}else if("${bean.reportBusinessType}"=="lwhh_report"){
			ajaxReport("/report/ajaxNtcLwhhReport","#lwhh");
		}else if("${bean.reportBusinessType}"=="src_ip_report"){
			ajaxReport("/report/ajaxNtcSrcipDomesticReport","#srcIp");
		}else if("${bean.reportBusinessType}"=="attr_type_report"){
			ajaxReport("/report/ajaxNtcXzReport","#attrType");
		}else if("${bean.reportBusinessType}"=="dest_ip_report"){
			ajaxReport("/report/ajaxNtcDestIpReport","#destIp");
		}else if("${bean.reportBusinessType}"=="isp_report"){
			ajaxReport("/report/ajaxNtcIspReport","#entranceId");
		}
		$("[name='reportType']").each(function(){
			var type='${bean.reportType}';
			if(type && type==$(this).val()){
				$(this).parent(".btn").button("toggle");
			}
		});
		$("a[data-toggle='tab']").on("click",function(){
			$("#reportBusinessType").val($(this).data("bussiness"));
			if(!$(this).parent("li").hasClass("active")){
				if($(this).data("bussiness")=="label_report"){
					ajaxReport("/report/ajaxNtcTagReport","#label");
				}else if($(this).data("bussiness")=="lwhh_report"){
					ajaxReport("/report/ajaxNtcLwhhReport","#lwhh");
				}else if($(this).data("bussiness")=="src_ip_report"){
					ajaxReport("/report/ajaxNtcSrcipDomesticReport","#srcIp");
				}else if($(this).data("bussiness")=="attr_type_report"){
					ajaxReport("/report/ajaxNtcXzReport","#attrType");
				}else if($(this).data("bussiness")=="dest_ip_report"){
					ajaxReport("/report/ajaxNtcDestIpReport","#destIp");
				}else if($(this).data("bussiness")=="isp_report"){
					ajaxReport("/report/ajaxNtcIspReport","#entranceId");
				}
			}
		});
		//ajaxServiceLogTotal();
		$("#export-btn").click(function(){
			totaltb(1,-1);
			var te = $(".in table").tableExport({
				headings:true,
				footers:true,
				formats:["xlsx"],
				fileName: getXlsxFileName(),
				bootstrap:false
			});
			$("#myexport").click();
			$("caption").remove();
			//totaltb(pato,size); 
		});
		
		
		var height=document.documentElement.clientHeight;
		if($('.table-responsive')){
			$('.table-responsive').css({'height':height-166+'px'});
			//style.height=height-130+'px';
		}
		
		var li=document.getElementById("liwhite").offsetWidth;
	    var lipx=Number(li/6);
	    $(".lireport").css({'width':lipx+'px','text-overflow':'ellipsis','overflow':'hidden'});
	});
	function getXlsxFileName() {
		var cfgName = $("[name=cfgName2]").val() + "_";
		if ("${bean.serviceId}") {
			var action = $("[name=serviceId]").find('[value=' + "${bean.serviceId}" + ']').text() + "_";
		} else {
			var action = "";
		}
		var reportType = $("[name=reportType]").find('[value=' + "${bean.reportType}" + ']').text() + "_";
		var reportTime = "${bean.reportTime}";
		return cfgName + action + reportType + reportTime;
	}

var  reportDate=0; 
	function changeReportType(reportType) {
		$("#intype").remove();
		var	reportTime = "${bean.reportTime }";
	    var  time=getNowFormatDate();
	    if(reportDate > 0){
	    	if(reportTime.length < 10){
	    		reportTime =time;
	    	}
	    }
	    reportDate ++;
		if(reportType == 1){//需删除元素后重新添加，直接更改onclick会出现缓存问题
			$("#reportTypeDiv").append('<input id="intype" name="reportTime" class="form-control input-medium Wdate" type="text" value="' + reportTime + '" readonly="readonly" onclick=WdatePicker({dateFmt:"yyyy-MM-dd",isShowClear:true});>');
		} else if(reportType == 2){
			$("#reportTypeDiv").append('<input id="intype" name="reportTime" class="form-control input-medium Wdate" type="text" value="' + reportTime.substring(0, 7) + '" readonly="readonly" onclick=WdatePicker({dateFmt:"yyyy-MM",isShowClear:true});>');
		} else if(reportType == 3){
			$("#reportTypeDiv").append('<input id="intype" name="reportTime" class="form-control input-medium Wdate" type="text" value="' + reportTime.substring(0, 4) + '" readonly="readonly" onclick=WdatePicker({dateFmt:"yyyy",isShowClear:true});>');
		}
	}
	
//获取当前时间	
 function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }	
	
var ajaxServiceLogTotal=function(){
	var services=[];
	var objs=[];
	var serviceIndex=0;
	$(".service").each(function(){
		if(serviceIndex%2==0){
			$(this).addClass("btn-primary");
		}else{
			$(this).addClass("btn-default");
		}
		objs.push($(this));
		services.push($(this).data("service"));
		serviceIndex++;
	});
	var timeout=$.validator.messages.timeout;
	var failed=$.validator.messages.failed;
	$.ajax({
		type:'post',
		timeout:10000,//超时时间设置，查询接口时间过长超时
		url:'${ctx}/report/ajaxNtcServiceReport',
		data:{
			"services":services.join(","),
			"functionId":$("#functionId").val(),
			"reportType":1/* $('[name="reportType"]').val() ,
			"reportTime":$('[name="reportTime"]').val()*/
		},
		dataType:'json',
		async:true,
		success:function(data,textStatus){//处理返回结果
			if(textStatus=="success"){
				for(var i=0;i<services.length;i++){
					var target=objs[i].find("span");
					for(var da in data){
						if(data[da].service==services[i]){
							target.html(data[da].sum);
						}
					}
				}
			}
		},
		complete:function(XMLHttpRequest,status){//超时设置
			if(status=="timeout"){
				for(var i=0;i<services.length;i++){
					var target=objs[i].find("span");
					target.html(timeout);
				}
			}else if(status !="success"){
				for(var i=0;i<services.length;i++){
					var target=objs[i].find("span");
					target.html(failed);
				}
			}
		}
	});
}	
var ajaxReport=function(url,target){
	loading('<spring:message code="onloading"/>');
	$.ajax({
		type:'post',
		async:false,
		cache:false,
		url:'${ctx}'+url,// /report/ajaxNtcTagReport
		data:{
			"serviceId":$('[name="serviceId"]').val(),
			"reportType":$('[name="reportType"]').val(),
			"reportTime":$('[name="reportTime"]').val(),
			"functionId":$('[name="functionId"]').val()
		},
		dataType:"html",
		success:function(data){
			$(target).html(data);//#label
			closeTip();
		}
	}); 
};
//自定义列设置事件
function customColumnClick(){
	var submit = function(v,h,f) {
		var abVisCols = [true];
		top.$(".customColumnList input[type='checkbox']").each(function(){
			abVisCols[$(this).val()] = $(this).prop("checked");
		});
		var key = getLocationPathName()+"_visCols";
		Cookies.set(key,JSON.stringify(abVisCols),{expires:7});
		customColumnInit();
		
		return true;	
	}
	var html = "<div class='customColumnList'>";
	$(".active .table tr th").each(function(colIndex,obj){	
		
		if(colIndex==0) { //第一列th含 checkbox ,结束本次循环, 下标从1开始反之0开始
			var ckVal = $(this).find("input[type='checkbox']").val();
			if(typeof(ckVal) != "undefined") {
				return true;	
			}
		}
		if($(this).hasClass("cfgDesc")){
			return true;	
		}
		var checked  = ($(this).is(":visible") ? "checked='checked'":"");
		var ckbox_html = "<input type='checkbox' "+checked+" value='"+colIndex+"'/>";
		html+="<div class='col-md-6'><label>"+ckbox_html+" "+$(this).html()+"</label></div>";
	})
	html +="</div>";
	top.$.jBox(html,{title:"<i class='fa fa-wrench'></i> Custom Columns",showIcon:false,opacity:0.3,submit:submit});
}

//合计table中某一段数据
 function totaltb(paNo,paSize){
 /* 	if(paSize>0){
		size=paSize;
		pato=paNo;
	}  */
	 $('#total').remove();
	 var reportBusinessType=$("#reportBusinessType").val()
	 var tableId="tagTable";
	 if(reportBusinessType=="label_report"){
		 tableId="tagTable";
	 }else if(reportBusinessType=="lwhh_report"){
		 tableId="lwhhTable";
	}else if(reportBusinessType=="src_ip_report"){
		tableId="contentTable1";
	}else if(reportBusinessType=="attr_type_report"){
		tableId="attrTypeTable";
	}else if(reportBusinessType=="dest_ip_report"){
		tableId="destIpTable";
	}else if(reportBusinessType=="isp_report"){
		tableId="entranceIdTable";
	}
	//统计数据 
	var tb=document.getElementById(tableId);
	var rows=tb.rows;
 	var list=new Array(); 
 	list.splice(0,list.length);
 	var num=0;
 	var row=1;
 	var s=1;
 	if(reportBusinessType=="src_ip_report"){
 		s=2;
 	}
 	
 	/* if(paNo > 1){
 	   row=(paNo-1)*paSize+1;	
 	} */
 	for (var i = 1; i < rows.length; i++) {
		for (var j = s; j <rows[i].cells.length ; j++) {
			if(list[j-s]!=null){
				list[j-s]=parseInt(rows[i].cells[j].innerHTML)+parseInt(list[j-s]);
			}else{
				list[j-s]=parseInt(rows[i].cells[j].innerHTML);
		   }
		}
	/* 	num++;
		if(num == paSize){
			break;
			} */
    	 }
 	   if(rows.length  > 1){
 		 var trtotal="";
 		if(reportBusinessType=="src_ip_report"){
		    trtotal+="<td colspan='2' style='text-align: left;' ><spring:message code='report_total' /></td>";
 		}else{
 			 trtotal+="<td style='text-align: left;'><spring:message code='report_total' /></td>";
 		} 
	 	for (var k = 0; k < list.length; k++) {
			trtotal+="<td>"+list[k]+"</td>";
		}
		    trtotal+=""
	 	$('#total').remove();
		$('#'+tableId+" tbody").prepend("<tr id='total'></tr>");
		$('#total').html(trtotal);
 	  } 
}	  
 window.onload=function (){
		setTimeout(function (){
			var height=document.documentElement.clientHeight;
			if($('.table-responsive')){
				$('.table-responsive').css({'height':height-186+'px'});
			} 	
		},510);
		
		
		 var reportBusinessType=$("#reportBusinessType").val()
		 var tableId="tagTable";
		 if(reportBusinessType=="lwhh_report"){
			 tableId="lwhhTable";
		}else if(reportBusinessType=="label_report"){
			 tableId="tagTable";
		 } else if(reportBusinessType=="src_ip_report"){
			tableId="contentTable1";
		}else if(reportBusinessType=="attr_type_report"){
			tableId="attrTypeTable";
		}else if(reportBusinessType=="dest_ip_report"){
			tableId="destIpTable";
		}else if(reportBusinessType=="isp_report"){
			tableId="entranceIdTable";
		}
		var tableCont = document.querySelector('.table-responsive');
		function scrollHandle (e){
		    console.log(this);
		    var scrollTop = this.scrollTop;
		    $('th').css('transform','translateY(' + scrollTop + 'px)');
		    $('thead tr').css('z-index','2147482468');
	        $('thead tr').css('position','relative');
	        $('th').css('border','1px solid rgb(255, 255, 255)');
	        $('th').css('background-clip','padding-box');
		 }
		tableCont.addEventListener('scroll',scrollHandle);
		$('#'+tableId).attr("margin-top",'0px');	
		
	}
 window.onresize=function (){
		var height=document.documentElement.clientHeight;
		if($('.table-responsive')){
			$('.table-responsive').css({'height':height-186+'px'});
		}
	    var li=document.getElementById("liwhite").offsetWidth;
	    var lipx=Number(li/6);
	    $(".lireport").css({'width':lipx+'px','text-overflow':'ellipsis','overflow':'hidden'});
	}
</script>
<style type="text/css">
li{
white-space:nowrap; 
}
</style>
</head>
<body>
<div class="page-content">
<%-- 	<div class="theme-panel hidden-xs hidden-sm">		
			<c:forEach items="${serviceList}" var="service" varStatus="status">
				      <c:if test="${(service.action ne 128)  && (service.serviceId ne 37) && (service.serviceId ne 149)}" >
					    <div data-service="${service.serviceId}" class="service btn">
							<spring:message code="total"/> 
							<c:forEach items="${fns:getDictList('SERVICE_ACTION')}" var="action">
								<c:if test="${service.functionId eq bean.functionId and action.itemCode eq service.action}">
									<spring:message code="${action.itemValue}"/>
								</c:if>
							</c:forEach>	
							<span>0</span>
						</div>
					</c:if>
			</c:forEach>
	</div> --%>
	<h3 class="page-title">
		<spring:message code="${bean.cfgName}"/>
		<small><spring:message code="report_list"/></small>
	</h3>
	<h5 class="page-header"></h5>
	<div class="col-md-12">
		<div class="portlet">
			<div class="portlet-body">
				<div class="row" >
					<form:form id="searchForm" modelAttribute="bean" action="${ctx}/report/list" method="post" class="form-search">
						<input id="functionId" name="functionId" type="hidden" value="${bean.functionId}"/>
						<input name="cfgName" type="hidden" value="${bean.cfgName}"/>
						
						<input name="cfgName2" type="hidden" value="<spring:message code="${bean.cfgName}"/>"/>
						<input name="serviceId2" type="hidden" value="<spring:message code="${bean.serviceId}"/>"/>
						
						<input id="reportBusinessType" name="reportBusinessType" type="hidden" value="${bean.reportBusinessType}"/>
						<!-- 筛选按钮展开状态-->
						<input id="isFilterAction" name="isFilterAction" type="hidden" value="${bean.isFilterAction }"/>
						<!-- 搜索内容与操作按钮栏 -->
						<div class="col-md-12">
							<div class="pull-left">
								<form:select path="serviceId" class="selectpicker select2 input-small">
									<form:option value=""><spring:message code="action"/></form:option>
										<c:forEach items="${serviceList}" var="service" >
										      <c:forEach items="${fns:getDictList('SERVICE_ACTION') }" var="dict">
													<c:if test="${(dict.itemCode eq service.action) && (service.action ne 128) && (service.serviceId ne 37)&& (service.serviceId ne 149)  }">  
														<form:option value="${service.serviceId}"><spring:message code="${dict.itemValue}"/></form:option>
													</c:if>
												</c:forEach>
										</c:forEach>
								</form:select>
							</div>
							<div class="pull-left">
                                 	<%-- <div class="btn-group" data-toggle="buttons">
                                 		<label class="btn btn-default">
                                 			<input type="radio" name="reportType" value="1" 
                                 			<c:if test="${bean.reportType==1}">checked</c:if>
                                 			><spring:message code="小时报"/>
                                 		</label>
                                 		<label class="btn btn-default">
                                 			<input type="radio" name="reportType" value="2"
                                 			<c:if test="${bean.reportType==2}">checked</c:if>
                                 			><spring:message code="日报"/>
                                 		</label>
                                 		<label class="btn btn-default">
                                 			<input type="radio" name="reportType" value="3"
                                 			<c:if test="${bean.reportType==3}">checked</c:if>
                                 			><spring:message code="月报"/>
                                 		</label>
                                 	</div> --%>
                                 	<div id="reportTypeDiv" class="input-group">
                                    	<div class="input-group-btn">
                                        	<form:select path="reportType" class="selectpicker select2 input-small" >
												<form:option value="1"><spring:message code="hour_of_day_report"/></form:option>
												<form:option value="2"><spring:message code="day_of_month_report"/></form:option>
												<form:option value="3"><spring:message code="month_report"/></form:option>
											</form:select>
                                        </div>
                                        <input id="intype" name="reportTime" class="form-control input-medium Wdate" type="text" value="${bean.reportTime }" readonly="readonly">
                                    </div>
							</div>
							<div class="pull-left">
								 <button type="button" class="btn blue" onClick="return page()"> <i class="fa fa-search"></i> <spring:message code="search"/>  </button>
								 <button type="button" class="btn btn-default" id="resetBtn" > <i class="fa fa-refresh"></i> <spring:message code="reset"/>  </button>
                                 <button type="button" class="btn btn-default" id="export-btn"><i class="fa fa-download"> <spring:message code="export"/></i></button>
                                 <%-- <button type="button" class="btn btn-default" id="filter-btn"> <spring:message code="filter"/><i class="fa fa-angle-double-down"></i></button> --%>
							</div>
							 <div class="pull-right">
                                	<a class="btn  btn-icon-only btn-default setfields tooltips" 
                                     		  data-container="body" data-placement="top" data-original-title=<spring:message code="custom_columns"/> href="javascript:;">
                                            <i class="icon-wrench"></i>
                                    </a>
                                </div>
						</div>
						<!-- 搜索内容与操作按钮栏 -->
						
						<!-- 筛选搜索内容栏默认隐藏-->
						<!-- <div class="col-md-12 filter-action-select-panle hide" >
						</div> -->
						<!-- /筛选搜索内容栏    结束-->
					</form:form>
				</div>
				
				<div id="liwhite">
					<sys:message content="${message}"/>
					<br>
					<ul class="nav nav-tabs nav-justified"  >
						<li
						<c:if test="${bean.reportBusinessType eq 'lwhh_report' or bean.reportBusinessType ==null}">
						class="active"
						</c:if> 
						><a class="lireport" data-bussiness="lwhh_report" data-toggle="tab" href="#lwhh" title="<spring:message code="lwhh_report"/>"  ><spring:message code="lwhh_report"/></a></li>
						<li 
						<c:if test="${bean.reportBusinessType eq 'label_report'}">
						class="active"
						</c:if>
						><a   class="lireport" data-bussiness="label_report" data-toggle="tab" href="#label"  title="<spring:message code="label_report"/>"><spring:message code="label_report"/></a></li>
						<li
						<c:if test="${bean.reportBusinessType eq 'attr_type_report'}">
						class="active"
						</c:if>
						><a class="lireport" data-bussiness="attr_type_report" data-toggle="tab" href="#attrType"   title="<spring:message code="attr_type_report"/>"  ><spring:message code="attr_type_report"/></a></li>
						<li 
						<c:if test="${bean.reportBusinessType eq 'src_ip_report'}">
						class="active"
						</c:if> 
						><a class="lireport" data-bussiness="src_ip_report" data-toggle="tab" href="#srcIp" title="<spring:message code="src_ip_report"/>"  ><spring:message code="src_ip_report"/></a></li>
						<li 
						<c:if test="${bean.reportBusinessType eq 'dest_ip_report'}">
						class="active"
						</c:if> 
						><a class="lireport" data-bussiness="dest_ip_report" data-toggle="tab" href="#destIp"  title="<spring:message code="dest_ip_report"/>" ><spring:message code="dest_ip_report"/></a></li>
						<li 
						<c:if test="${bean.reportBusinessType eq 'isp_report'}">
						class="active"
						</c:if>
						><a class="lireport" data-bussiness="isp_report" data-toggle="tab" href="#entranceId"  title="<spring:message code="isp_report"/>"><spring:message code="isp_report"/></a></li>
					</ul>
					<div class="tab-content table-responsive">
						<div id="lwhh" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'lwhh_report' or bean.reportBusinessType ==null}">
						 in active
						</c:if>"></div>
						<div id="label" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'label_report'}">
						 in active
						</c:if>"></div>
						<div id="attrType" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'attr_type_report'}">
						 in active
						</c:if>"></div>
						<div id="srcIp" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'src_ip_report'}">
						 in active
						</c:if>"></div>
						<div id="destIp" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'dest_ip_report'}">
						 in active
						</c:if>"></div>
						<div id="entranceId" class="tab-pane fade
						<c:if test="${bean.reportBusinessType eq 'isp_report'}">
						 in active
						</c:if>"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

</script>
</body>
</html>
