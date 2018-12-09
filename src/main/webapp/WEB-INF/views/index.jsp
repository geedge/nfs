<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<meta name="format-detection" content="telephone=no,email=no,address=no"> 
	<style type="text/css">
	.statisticsRequest{
		padding:10px 0px 0px 10px;
		width:99%
	}
	.statisticsStatus{
		padding:10px 0px 0px 10px;
		width:99%
	}
	.page{
		padding:8px 0px 5px 0px;
	}
	.tableDiv{
		width:100%;
		overflow-x:auto;
	}
	.table {
	    margin-bottom: 0px;
	    margin-top: 0px;
	    max-width: 100%;
	    width: 100%;
	}
	.table > thead > tr > th {
		    vertical-align: bottom;
		    border-bottom: 2px solid #e7ecf1; }
		  .table > caption + thead > tr:first-child > th,
		  .table > caption + thead > tr:first-child > td,
		  .table > colgroup + thead > tr:first-child > th,
		  .table > colgroup + thead > tr:first-child > td,
		  .table > thead:first-child > tr:first-child > th,
		  .table > thead:first-child > tr:first-child > td {
		    border-top: 0; 
		    background-color: #E9E9E9;
			color: #333333;
			height: 38px;
			line-height: 38px;
			padding-top: 0px;
			padding-right: 10px;
			padding-bottom: 0px;
			padding-left: 10px;
		   }
		  .table > tbody + tbody {
		    border-top: 2px solid #D9D9D9; }
		  .table .table {
		    background-color: #fff; }
		
		.table-condensed > thead > tr > th,
		.table-condensed > thead > tr > td,
		.table-condensed > tbody > tr > th,
		.table-condensed > tbody > tr > td,
		.table-condensed > tfoot > tr > th,
		.table-condensed > tfoot > tr > td {
		  padding-left:10px; }
		
		.table-bordered {
		  border: 1px solid #D9D9D9; }
		  .table-bordered > thead > tr > th,
		  .table-bordered > thead > tr > td,
		  .table-bordered > tbody > tr > th,
		  .table-bordered > tbody > tr > td,
		  .table-bordered > tfoot > tr > th,
		  .table-bordered > tfoot > tr > td {
		    border: 1px solid #D9D9D9; }
		  .table-bordered > thead > tr > th,
		  .table-bordered > thead > tr > td {
		    border-bottom-width: 2px; }
	</style>
	<script type="text/javascript">
		$(function(){
			var hideTd=new Array();
			//如果来函统计数为0，去掉此列
			$(".statisticsRequest").find("table th").each(function(i){
				//第二列开始计算
				if(i > 0){
					var sum=0;
					$(".statisticsRequest").find("table tr").each(function(j){
						if(j>0){
							sum=sum+parseInt($(this).find("td").eq(i).text());
						}
					});
					if(sum < 1){
						hideTd.push(i);
					}
				}
			});
			
			for(var k = 0 ; k < hideTd.length; k++){
				$(".statisticsRequest").find("table tr").each(function(h){
					$(this).find("th").eq(hideTd[k]).hide();
					$(this).find("td").eq(hideTd[k]).hide();
				});
			}
			closeTip();
		});
	</script>
</head>
<title>index</title>
	</head>

	<body class="page-full-width">
		<div class="statisticsRequest">
		<form id="searchForm" action="${ctx}/configure/statistics/configureStateStatistics" >
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<font size="4"><i class="fa fa-cogs"> <spring:message code="letter_statistics_info"/>
								[<spring:message code="statistic_time"/>：${requestStatisticTime }]
								</i>
				</font> 
				<a style="color:#333333" href="javascript:page(${page.pageNo},${page.pageSize});" class="icon-refresh pull-right"> </a>
				<h5 class="page-header"></h5>
				<div class="tableDiv">
					<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
						<thead>
							<tr>
								<th><spring:message code="letter"/></th>
								<c:forEach items="${fns:getMenuCfgList() }" var="functionDict" varStatus="status" step="1">
										<th>
										<c:choose>
											<c:when test="${functionDict.functionId eq 8}">
												HTTP <spring:message code="${functionDict.code }"/>
											</c:when>
											<c:when test="${functionDict.functionId eq 35}">
												MAIL <spring:message code="${functionDict.code }"/>
											</c:when>
											<c:otherwise>
												<spring:message code="${functionDict.code }"/>
											</c:otherwise>
										</c:choose>
										</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list  }" var="requestStatistics" varStatus="statusRequest" step="1">
								<tr>
									<td>
										${requestStatistics['request']}
									</td>
									<c:forEach items="${fns:getMenuCfgList() }" var="functionDict" varStatus="status" step="1">
										<td>
										<c:set var="functionId" value="${functionDict.functionId}_${functionDict.functionId}"></c:set>
										${requestStatistics[functionId]}
										</<td>
								</c:forEach>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>			
				</div>
		</form>
		<div class="page">${page}</div>
	</div>
	<div class="statisticsStatus">
			<font size="4"><i class="fa fa-cogs"> <spring:message code="configure_statistics_info"/>
				[<spring:message code="statistic_time"/>：${configStatisticTime }]
			</i></font> <a style="color:#333333" href="javascript:page(${page.pageNo},${page.pageSize});" class="icon-refresh pull-right"> </a>
			<h5 class="page-header"></h5>
			<table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
					<thead>
						<tr>
							<th><spring:message code="service_name"/></th>
							<th><spring:message code="approved"/></th>
							<th><spring:message code="unapproved"/></th>
							<th><spring:message code="cancel_approved"/></th>
							<th><spring:message code="created"/></th>
							<th><spring:message code="deleted"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${configStatistics }" var="statistics" varStatus="status" step="1">
							<%-- <c:set var="serviceName" value=""></c:set>
							<c:forEach items="${serviceList }" var="serviceDict" varStatus="status" step="1">
								<c:if test="${serviceDict.serviceId eq statistics.service }">
									<c:set var="serviceName" value="${serviceDict.serviceName }"></c:set>
								</c:if>
							</c:forEach> --%>
							<c:set var="functionName" value=""></c:set>
							<c:forEach items="${fns:getMenuCfgList() }" var="functionDict" varStatus="status" step="1">
								<c:if test="${(functionDict.functionId eq statistics.functionId)}">
									<c:choose>
										<c:when test="${functionDict.functionId eq 8 && functionDict.code eq 'advanced'}">
											<c:set var="functionName" value="HTTP ${functionDict.code }"></c:set>
										</c:when>
										<c:when test="${functionDict.functionId eq 35 && functionDict.code eq 'advanced'}">
											<c:set var="functionName" value="MAIL ${functionDict.code }"></c:set>
										</c:when>
										<c:otherwise>
											<c:set var="functionName" value="${functionDict.code }"></c:set>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
							<%-- <c:if test="${!empty serviceName }"> --%>
							<c:if test="${!empty functionName }">
								<tr>
									<td>	
									<%-- <spring:message code="${serviceName }"/>${serviceName } --%>
									<spring:message code="${functionName }"/>
									</td>
									<td>
										${statistics.approved }
									</td>
									<td>
										${statistics.unapproved }
									</td>
									<td>
										${statistics.cancle_approved }
									</td>
									<td>
										${statistics.created }
									</td>
									<td>
										${statistics.deleted }
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
	</div>
	</body>
</html>
