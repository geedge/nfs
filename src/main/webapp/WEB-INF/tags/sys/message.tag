<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<style type="text/css">
.alert-error {
  background-color: #f2dede;
  border-color: #eed3d7;
  color: #b94a48;
}
.alert{
 margin-top: 20px;
 margin-bottom: 0px;
}
</style>
<script type="text/javascript">top.$.jBox.closeTip();</script>
<c:if test="${not empty content}">
	<c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if><c:if test="${empty type}"><c:set var="ctype" value="${(!fn:containsIgnoreCase(content,'失败') && !fn:containsIgnoreCase(content,'failed') && !fn:containsIgnoreCase(content,'Exception') && !fn:containsIgnoreCase(content,'null')&& !fn:containsIgnoreCase(content,'出错')&& !fn:containsIgnoreCase(content,'error'))?'success':'error'}"/></c:if>
	<div id="messageBox" class="alert alert-${ctype}" style="max-height:220px;overflow-y:auto;overflow-x:hidden;"><button data-dismiss="alert" class="close">×</button><spring:message code="${content}"/></div> 
	<script type="text/javascript">if(!top.$.jBox.tip.mess) {top.$.jBox.tip.mess=1;/* top.$.jBox.tip("<spring:message code='${content}'/>","${ctype}",{persistent:true,opacity:0}); */$("#messageBox").show();}</script>
</c:if>