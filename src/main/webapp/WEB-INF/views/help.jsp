<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>

<title></title>
<link href="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	var setting = {check:{enable:false,nocheckInherit:true},view:{selectedMulti:false},
			data:{simpleData:{enable:true}},callback:{onClick:function(event, treeId, treeNode){
				showHelp(treeNode.helpHref);
	}}};
	// 用户-菜单
	var zNodes=[
			<c:forEach items="${menuList}" var="menu">
			{
				id:"${menu.id}", 
				helpHref:"${menu.menuBg}",
				pId:"${not empty menu.parent.id?menu.parent.id:0}", 
				name:"<c:if test='${not empty menu.parent.id}'><spring:message code="${menu.code}"/></c:if><c:if test='${empty menu.parent.id}'><spring:message code='permission_list'/></c:if>"
			},
            </c:forEach>];
	// 初始化树结构
	var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
	// 不选择父节点
	//tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
	// 默认选择节点
	/* var ids = "${role.menuIds}".split(",");
	for(var i=0; i<ids.length; i++) {
		var node = tree.getNodeByParam("id", ids[i]);
		try{tree.checkNode(node, true, false);}catch(e){}
	} */
	// 默认展开全部节点
	tree.expandAll(true);
	//隐藏没有分配父节点权限，却有子节点权限的叶子节点
	//$("#menuTree").children("li:not(:eq(0))").hide();
});
function showHelp(helpHref) {
	 if(helpHref!=''){
		var lang = "${cookie.Language.value }".toLowerCase();
	    if(lang=="" || lang.indexOf("en")!=-1) {
	    	helpHref += ".html";
		}else if(lang.indexOf("cn")!=-1) {
			helpHref += "_zh_CN.html";
		}else if(lang.indexOf("ru")!=-1) {
			helpHref += "_ru.html";
		}
	    $(".help").load("${pageContext.request.contextPath}"+helpHref);
	 }
};
</script>
</head>
<body>
	<div class="page-content">
 		<h3 class="page-title">
			<spring:message code="${online_help }"></spring:message>
		</h3>
		<div class="row">
			<div class="portlet-body">
				<div class="col-md-4">
					<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
				</div>
				<div class="col-md-8 help">

				</div>
				
				
			</div>
		</div>
	</div>
</body>
</html>
		
		

