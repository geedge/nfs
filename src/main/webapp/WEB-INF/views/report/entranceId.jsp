<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script type="text/javascript">
$(function(){
});
</script>
	<table id="entranceIdTable" class="table table-striped table-bordered table-condensed text-nowrap">
		<thead>
			<tr>
				<th><spring:message code="entrance" /></th>
				<th><spring:message code="log_total" /></th>
				<c:forEach items="${titles}" var="title">
					<th>${title}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<script type="text/javascript">
			var trContent = "";
			<c:forEach items="${datas}" var="data">
			var flag=false;
				trContent += "<tr>";
				<c:forEach items="${data.key}" var="entranceId" varStatus="status">
				trContent += "<td>";
				<c:forEach items="${fns:getDictList('ENTRANCE')}" var="dic">
					<c:if test="${dic.itemCode eq entranceId}">
					trContent += "<spring:message code="${dic.itemValue}"/>";
					flag=true;
					</c:if>
				</c:forEach>
				if(!flag){
					trContent +="${entranceId}";
					}
					trContent += "</td>";
				</c:forEach>
				<c:forEach items="${data.value}" var="cloumn" varStatus="status">
					trContent += "<td>${cloumn}</td>";
				</c:forEach>
				trContent += "</tr>";
			</c:forEach>
			$('tbody').remove();
			$('#entranceIdTable').append("<tbody></tbody>");
			$('tbody').html(trContent);
			pagination();
		</script>
		</tbody>
	</table>
	<div class="page">${page}</div>