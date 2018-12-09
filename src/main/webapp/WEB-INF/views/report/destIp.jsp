<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script type="text/javascript">
$(function(){
});
</script>
	<table id="destIpTable" class="table table-striped table-bordered table-condensed text-nowrap">
		<thead>
			<tr>
				<th><spring:message code="dest_country" /></th>
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
				trContent += "<tr>";
				<c:forEach items="${data.key}" var="destCountry" varStatus="status">
					trContent += "<td>${destCountry}</td>";
				</c:forEach>
				<c:forEach items="${data.value}" var="cloumn" varStatus="status">
					trContent += "<td>${cloumn}</td>";
				</c:forEach>
				trContent += "</tr>";
			</c:forEach>
			$('tbody').remove();
			$('#destIpTable').append("<tbody></tbody>");
			$('tbody').html(trContent);
			pagination();
		</script>
		</tbody>
	</table>
	<div class="page">${page}</div>