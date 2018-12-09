<%
response.setStatus(500);

// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
if (ex != null){
	LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
}
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
// 编译错误信息
StringBuilder sb = new StringBuilder(msgProp!=null?msgProp.getProperty("exception_info")+":\n":"Exception Infomation:\n");
if (ex != null) {
	sb.append(Exceptions.getStackTraceAsString(ex));
} else {
	sb.append(msgProp!=null?msgProp.getProperty("unkown_error")+":\n\n":"Unkown Error Infomation:\n");
}

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page import="com.nis.web.security.Servlets"%>
<%@page import="com.nis.util.Exceptions"%>
<%@page import="com.nis.util.StringUtils"%>
<%@page import="java.util.Properties"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page import="com.nis.util.Configurations"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>500 - <spring:message code="system_error"/></title>
	<%@include file="/WEB-INF/include/header.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>500-<spring:message code="system_error"/>.</h1></div>
		<div class="errorMessage">
			<spring:message code="exception_info"/>：<%=ex==null?(msgProp!=null?msgProp.getProperty("exception_info")+":\n":"Exception Infomation:\n")+"\n":StringUtils.toHtml(ex.getMessage())%> <br/> <br/>
			<!-- 请点击“查看详细信息”按钮，将详细错误信息发送给系统管理员，谢谢！ -->
			<spring:message code="click_detail"/><br/> <br/>
			<a href="javascript:" onclick="history.go(-1);" class="btn"><spring:message code="return_lastpage"/></a> &nbsp;
			<a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn"><spring:message code="show_detail"/></a>
		</div>
		<div class="errorMessage hide">
			<%=StringUtils.toHtml(sb.toString())%> <br/>
			<a href="javascript:" onclick="history.go(-1);" class="btn"><spring:message code="return_lastpage"/></a> &nbsp;
			<a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn"><spring:message code="hidden_detail"/></a>
			<br/> <br/>
		</div>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div>
</body>
</html>
<%
} out = pageContext.pushBody();
%>