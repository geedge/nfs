<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<link href="${ctxStatic}/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
	<link href="${ctxStatic}/global/plugins/bootstrap/css/bootstrap2.css" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/global/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var selectedIds = "${selectIds}";
		var pre_ids = new Array();
		var ids = new Array();
		if (selectedIds) {
			pre_ids = "${selectIds}".split(",");
			ids = "${selectIds}".split(",");
		} 
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("officeTree" == treeId) {
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree" == treeId) {
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
			};
			if("selectedTree"  == treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					top.$.jBox.tip("<spring:message code='old_remove_error'/>", 'info');
				}
			}
		};
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "【${role.name}】<spring:message code='role_no_new'/>";
					}else{
						tips = "<spring:message code='selected_clear_success'/>";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("<spring:message code='cancel_clear'/>", 'info');
			    }
			    return true;
			};
			tips="【${role.name}】<spring:message code="sure_remove_role"/>";
			top.$.jBox.confirm(tips, "<spring:message code='sure_clear'/>", submit);
		};
	</script>
</head>
<body>
	<div id="assignRole" class="row-fluid span12">
		<div class="span4" style="border-right: 1px solid #A8A8A8;">
			<p><spring:message code="owner_group"/>：</p>
			<div id="officeTree" class="ztree"></div>
		</div>
		<div class="span3">
			<p><spring:message code="be_selected_staff"/>：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="span3" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p><spring:message code="selected_staff"/>：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>
