<%
response.setStatus(404);
//获取国际化信息
Properties msgProp = new Properties();
try {
	String language = LocaleContextHolder.getLocale().getLanguage();
	if (language.equals("zh_cn") || language.equals("zh")) {
		msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
	} else if (language.equals("ru")) {
		msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
	} else {
		msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
	}

} catch (Exception e) {
	msgProp = null;
}
// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print(msgProp!=null?msgProp.getProperty("notfound_page"):"Page does not exist");
}

//输出异常信息页面
else {
%>
<%@page import="com.nis.web.security.Servlets"%>
<%@page import="java.util.Properties"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page import="com.nis.util.Configurations"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>404 - <spring:message code="notfound_page"/></title>
	<%@include file="/WEB-INF/include/header.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>404-<spring:message code="notfound_page"/>.</h1></div>
		<div><a href="javascript:" onclick="history.go(-1);" class="btn"><spring:message code="return_lastpage"/></a></div>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div>
</body>
</html>
<%
out.print("<!--"+request.getAttribute("javax.servlet.forward.request_uri")+"-->");
} out = pageContext.pushBody();
%>