<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<table id="tagTable" class="table table-striped table-bordered table-condensed text-nowrap">
<thead>
	<tr>
		<th><spring:message code="label"/></th>
		<th><spring:message code="log_total"/></th>
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
			<c:forEach items="${labels}" var="label">
				<c:if test="${label.serviceDictId==data.key}">
					trContent += "<td>${label.itemValue}</td>";
				</c:if>
			</c:forEach>
			<c:forEach items="${data.value}" var="cloumn" varStatus="status">
				trContent += "<td>${cloumn}</td>";
			</c:forEach>
		 	trContent += "</tr>";
		</c:forEach>
		$('tbody').remove();
		$('#tagTable').append("<tbody></tbody>");
		$('tbody').html(trContent);
		pagination();
	</script>
</tbody>
</table>
<div class="page"></div>