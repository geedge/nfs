<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title></title>
<script type="text/javascript">
	$(document) .ready(function() {
		$("#cfgFrom").validate({
				submitHandler : function(form) {
					top.$.jBox.tip("The system is being maintained and temporarily inaccessible.",'loading',{opacity:0.5,persistent:true});
					form.submit();
				},
		})
	})
</script>
</head>

<body>
	<form id="cfgFrom" action="${ctx}/config/synchronization/configSync?cmd=1" method="post" class="form-horizontal">
	<div class="page-content">
 		<h3 class="page-title">
			<c:forEach items="${fns:getDictList('currrent_sync_status') }" var="dict">
				${dict.itemDesc }
			</c:forEach>
		</h3>
		<h2 class="page-title">
			<c:forEach items="${fns:getDictList('currrent_sync_status') }" var="dict">
				同步状态：
				<c:if test="${dict.itemValue eq '0' }">开始</c:if>
				<c:if test="${dict.itemValue eq '1' }">初始化</c:if>
				<c:if test="${dict.itemValue eq '2' }">进行中</c:if>
				<c:if test="${dict.itemValue eq '3' }">结束</c:if>
				<c:if test="${dict.itemValue eq '-1' }">服务端同步失败</c:if>
				<c:if test="${dict.itemValue eq '-2' }">界面端同步失败</c:if>
			</c:forEach>
		</h2>
		<button id="save" type="submit" class="btn green">
			配置同步
		</button>
	</div>
	</form>
</body>
</html>