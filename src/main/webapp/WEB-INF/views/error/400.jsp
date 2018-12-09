<%
response.setStatus(400);

// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
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
StringBuilder sb = new StringBuilder("(400)"+msgProp!=null?msgProp.getProperty("exception_info"):"Exception Infomation"+":\n");
if (ex != null) {
	if (ex instanceof BindException) {
		for (ObjectError e : ((BindException)ex).getGlobalErrors()){
			sb.append("☆" + e.getDefaultMessage() + "(" + e.getObjectName() + ")\n");
		}
		for (FieldError e : ((BindException)ex).getFieldErrors()){
			sb.append("☆" + e.getDefaultMessage() + "(" + e.getField() + ")\n");
		}
		LoggerFactory.getLogger("400.jsp").warn(ex.getMessage(), ex);
	}/* else if (ex instanceof ConstraintViolationException) {
		for (ConstraintViolation<?> v : ((ConstraintViolationException)ex).getConstraintViolations()) {
			sb.append("☆" + v.getMessage() + "(" + v.getPropertyPath() + ")\n");
		}
	} */ else {
		//sb.append(Exceptions.getStackTraceAsString(ex));
		sb.append("☆" + ex.getMessage());
	}
} else {
	sb.append(msgProp!=null?msgProp.getProperty("unkown_error"):"Unkown Error Infomation"+":\n\n");
}

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>
<%@page import="org.springframework.validation.BindException"%>
<%@page import="org.springframework.validation.ObjectError"%>
<%@page import="org.springframework.validation.FieldError"%>
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
	<title>400 - <spring:message code="request_error"/></title>
		<%@include file="/WEB-INF/include/header.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1><spring:message code="param_error"/><!-- 参数有误,服务器无法解析. --></h1></div>
		<div class="errorMessage">
			<%=StringUtils.toHtml(sb.toString())%> <br/>
		</div>
		<a href="javascript:" onclick="history.go(-1);" class="btn"><spring:message code="return_lastpage"/></a> &nbsp;
		<br/> <br/>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div>
</body>
</html>
<%
} out = pageContext.pushBody();
%>