<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="renderer" content="webkit"><meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Expires" content="0"><meta http-equiv="Cache-Control" content="no-cache"><meta http-equiv="Cache-Control" content="no-store">
<meta name="format-detection" content="telephone=no,email=no,address=no"> 


<link href="${pageContext.request.contextPath}/static/global/plugins/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/static/global/plugins/simple-line-icons/simple-line-icons.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL PLUGINS -->

<link href="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/static/global/plugins/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
<!-- END PAGE LEVEL PLUGINS -->

<!-- BEGIN THEME GLOBAL STYLES -->
<link href="${pageContext.request.contextPath}/static/global/css/components.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="${pageContext.request.contextPath}/static/global/css/plugins.css" rel="stylesheet" type="text/css" />
<!-- END THEME GLOBAL STYLES -->

<!-- BEGIN THEME LAYOUT STYLES -->
<link href="${pageContext.request.contextPath}/static/layouts/layout/css/layout.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/static/layouts/layout/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${pageContext.request.contextPath}/static/layouts/layout/css/custom.css" rel="stylesheet" type="text/css" />

<link href="${pageContext.request.contextPath}/static/global/css/page.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/static/global/css/jquery.tagsinput.css" rel="stylesheet" type="text/css"  />
<!-- 文件导入 -->
<link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-fileupload/css/bootstrap-fileupload.css" rel="stylesheet" type="text/css" />

<link href="${pageContext.request.contextPath}/static/global/plugins/jquery-tree-multiselect/jquery.tree-multiselect.min.css" rel="stylesheet" type="text/css" />

<!-- 日志界面 -->
<link href="${pageContext.request.contextPath}/static/pages/css/pageLogs.css" rel="stylesheet" type="text/css" />

<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/static/global/plugins/respond.min.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<!-- BEGIN CORE PLUGINS -->
<script src="${pageContext.request.contextPath}/static/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/jquery.validate.method.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-jbox/2.3/jquery.jBox-2.3.src.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-typeahead/bootstrap3-typeahead.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/unicodes.js" type="text/javascript"></script>

<!-- END CORE PLUGINS -->


<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="${pageContext.request.contextPath}/static/global/scripts/app.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/scripts/jeesite.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->


<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${pageContext.request.contextPath}/static/layouts/layout/scripts/layout.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

<link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/js/bootstrap-select.min.js"></script>
<c:choose>
	<c:when test="${cookie.Language.value eq 'zh_CN'}">
	<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/js/i18n/defaults-zh_CN.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/localization/messages_zh.js"></script>
	</c:when>
	<c:when test="${cookie.Language.value eq 'en'}">
	<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/js/i18n/defaults-en_US.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/localization/messages_en.js"></script>
	</c:when>
	<c:when test="${cookie.Language.value eq 'ru_RU'}">
	<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/js/i18n/defaults-ru_RU.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/localization/messages_ru.js"></script>
	</c:when>
	<c:otherwise>
	<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/js/i18n/defaults-en_US.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/localization/messages_en.js"></script>
	</c:otherwise>
</c:choose>
<script src="${pageContext.request.contextPath}/static/global/scripts/common.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/scripts/jquery.tagsinput.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/scripts/app.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/scripts/ipRegion.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/scripts/pzLog.js" type="text/javascript"></script>
<!-- 文件导入 -->
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-fileupload/js/bootstrap-fileupload.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-fileupload/js/jquery.cookie.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-tree-multiselect/jquery.tree-multiselect.js" type="text/javascript"></script>

<!-- 日志界面 -->
<script src="${pageContext.request.contextPath}/static/pages/scripts/pageLogs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/static/pages/scripts/appNames.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/static/global/plugins/clipboard.js" type="text/javascript"></script>
