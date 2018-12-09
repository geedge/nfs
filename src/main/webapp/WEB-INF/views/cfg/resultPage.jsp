<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>结果跳转页</title>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout("javascript:window.location.href='${ctx}/cfg/${cfgType}/list?serviceId=${serviceId}&action=${action}&cfgName=${cfgName}&audit=${audit}&mainTable=${mainTable}'",1000);
});
</script>
</head>
<body>
<div class="page-content">
<sys:message content="${message}"/>
</div>
</body>
</html>