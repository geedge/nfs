<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>

	<title>数据选择</title>
	<link href="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
	<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
	<%--解决ztree add小图标丢失 --%>
	<style type="text/css">
	.ztree li span.button.add{margin-right:2px;background-position:-144px 0;vertical-align:top;*vertical-align:middle}
	</style>
	<script type="text/javascript">
		var key, lastValue = "", nodeList = [], type = getQueryString("type", "${url}");
		var tree, setting = {
				view:{
					addHoverDom: addHoverDom,
					removeHoverDom: removeHoverDom,
					selectedMulti:false,
					dblClickExpand:false,
					addDiyDom:addDiyDom
				},
				edit: {
					enable: true,
					showRemoveBtn: false,
					showRenameBtn: false
				},
				check:{enable:"${checked}",chkboxType: { "Y" : "${checkedPS}", "N" : "${unCheckedPS}" },nocheckInherit:true},
				async:{enable:(type==6),url:"${ctx}/sys/user/treeData",autoParam:["id=officeId"]},
				data:{simpleData:{enable:true}},
				callback:{
					beforeEditName:false,
					beforeRename:beforeRename,
					beforeRemove:false,
					//beforeClick: function(treeId, treeNode){
						//if("${checked}" == "true"){
							//tree.checkNode(treeNode, !node.checked, true, true);
							//tree.expandNode(treeNode, true, false, false);
						//}
					//},
					onClick:function(event, treeId, treeNode){
						tree.expandNode(treeNode);
					},onCheck: function(e, treeId, treeNode){
						var nodes = tree.getCheckedNodes(true);
						for (var i=0, l=nodes.length; i<l; i++) {
							tree.expandNode(nodes[i], true, false, false);
						}
						return false;
					},onAsyncSuccess: function(event, treeId, treeNode, msg){
						var nodes = tree.getNodesByParam("pId", treeNode.id, null);
						for (var i=0, l=nodes.length; i<l; i++) {
							try{tree.checkNode(nodes[i], treeNode.checked, true);}catch(e){}
							//tree.selectNode(nodes[i], false);
						}
						selectCheckNode();
					},onDblClick: function(){//<c:if test="${!checked}">
						top.$.jBox.getBox().find("button[value='ok']").trigger("click");
						//$("input[type='text']", top.mainFrame.document).focus();//</c:if>
					},
					onRename:onRename,
					onRemove:false
				}
			};
		function expandNodes(nodes) {
			if (!nodes) return;
			for (var i=0, l=nodes.length; i<l; i++) {
				tree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				}
			}
		}
		$(document).ready(function(){
			$.get("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}&extId=${extId}&selectIds: $('#${id}Id').val()&isAll=${isAll}&module=${module}&t="
					+ new Date().getTime(), function(zNodes){
				// 初始化树结构
				//处理根节点国际化
				for(var i=0;i<zNodes.length;i++){
					var map = zNodes[i];
					if(map.id==0&&map.pId==0&&map.name=="root_node"){
						if('${title}'){
							map.name="<spring:message code='${title}' />";
						}else{
							map.name="<spring:message code='root_node' />";
						}
					}
				}
				tree = $.fn.zTree.init($("#tree"), setting, zNodes);
				// 默认展开一二级节点
				var nodes = tree.getNodesByParam("level", 1);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
					var nodeId=nodes[i].id;
					//处理特定服务业务类型的图标
					if(typeof nodeId=='string'&&nodeId.indexOf("businessType")==0){
						if(!nodes[i].isParent){//强制改变没有子节点的特定服务业务类型为父节点，让其改变样式
							nodes[i].isParent=true;
							tree.updateNode(nodes[i],false);
							tree.expandNode(nodes[i], true, false, false);
						}
					}
					/* if(nodes[i].user&&nodes[i].user=="${userId}"){
						nodes[i].icon="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/5.png";
						tree.updateNode(nodes[i],false);
					} */
				}
				nodes=tree.getNodesByParam("level", 0);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
				}
				//更换用户自己添加的节点的图标
				/* if("${enableAddBtn}"&&"${enableAddBtn}"=="true"){
					nodes=tree.getNodesByParam("level", 2);
					for(var i=0; i<nodes.length; i++) {
						if(nodes[i].user&&nodes[i].user=="${userId}"){
							nodes[i].icon="${pageContext.request.contextPath}/static/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/5.png";
							tree.updateNode(nodes[i],false);
						}
					}
				} */
				//异步加载子节点（加载用户）
				var nodesOne = tree.getNodesByParam("isParent", true);
				for(var j=0; j<nodesOne.length; j++) {
					tree.reAsyncChildNodes(nodesOne[j],"!refresh",true);
				}
				selectCheckNode();
			});
			key = $("#key");
			key.bind("focus", focusKey).bind("blur", blurKey).bind("change cut input propertychange", searchNode);
			key.bind('keydown', function (e){if(e.which == 13){searchNode();}});
			setTimeout("search();", "300");
		});
		
		// 默认选择节点
		function selectCheckNode(){
			var ids = "${selectIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", (type==3?"u_":"")+ids[i]);
				//如果节点id与排除id相同，不被选中
				if(node&&node.id!=parseInt("${extId}")){
					if("${checked}" == "true"){
						try{tree.checkNode(node, true, true);}catch(e){}
						tree.selectNode(node, false);
					}else{
						tree.selectNode(node, true);
					}
				}
				
			}
		}
	  	function focusKey(e) {
			if (key.hasClass("empty")) {
				key.removeClass("empty");
			}
		}
		function blurKey(e) {
			if (key.get(0).value === "") {
				key.addClass("empty");
			}
			searchNode(e);
		}
		
		//搜索节点
		function searchNode() {
			// 取得输入的关键字的值
			var value = $.trim(key.get(0).value);
			
			// 按名字查询
			var keyType = "name";<%--
			if (key.hasClass("empty")) {
				value = "";
			}--%>
			
			// 如果和上次一次，就退出不查了。
			if (lastValue === value) {
				return;
			}
			
			// 保存最后一次
			lastValue = value;
			
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				return;
			}
			hideAllNode(nodes);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			updateNodes(nodeList);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						tree.expandNode(nodes[i],false,false,false,false);
					}else{
						tree.expandNode(nodes[i],true,true,false,false);
					}
					tree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			tree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				
				//展开当前节点的父节点
				tree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					tree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					tree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				tree.showNode(nodeList[i].getParentNode());
				//展开根节点
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		
		// 开始搜索
		function search() {
			//$("#search").slideToggle(200);
			//$("#txt").toggle();
			$("#key").focus();
		}
		/* function beforeEditName(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.selectNode(treeNode);
			return confirm("<spring:message code='edit' />" + treeNode.name + "?");
		} */
		function beforeRename(treeId, treeNode, newName) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (newName.length == 0) {
				var tip="<spring:message code='can_not_null' />";
				alert("<spring:message code='${title}' />"+tip.replace("%s"," "));
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			return true;
		}
		/* function beforeRemove(treeId, treeNode) {
			return confirm("<spring:message code='remove' /> " + treeNode.name + "?");
		} */
		//在重命名节点时，调用后台存储，将返回结果更新
		function onRename(e, treeId, treeNode) {
			loading("<spring:message code='loading' />...");
			$.ajax({
				type:'post',
				async:true,
				cache:false,
				url:'${ctx}/specific/specificServiceCfg/ajaxSaveOrUpdateApp',
				data:{
					"specServiceId":treeNode.id,
					"specServiceCode":treeNode.serviceCode,
					"specServiceName":treeNode.name,
					"cfgType":treeNode.serviceType,
					"groupId":treeNode.groupId,
					"businessType":treeNode.pId.replace("businessType",""),
					"addFlag":treeNode.user
				},
				//dataType:"json",
				//contentType:"application/json; charset=utf-8",
				success:function(data){
					treeNode.id=data.id;
					treeNode.groupId=data.groupId;
					treeNode.specServiceCode=data.specServiceCode;
					treeNode.user=data.addFlag;
					var zTree = $.fn.zTree.getZTreeObj("tree");
					zTree.updateNode(treeNode);
					closeTip();
				},
				complete:function(XMLHttpRequest,status){//发生异常，关闭遮罩层
					if(status !="success"){
						closeTip();
					}
				}
			}); 
		}
		/* function onRemove(e, treeId, treeNode) {
			loading("<spring:message code='loading' />...");
			if(typeof treeNode=="string"){
				var zTree = $.fn.zTree.getZTreeObj("tree");
				zTree.removeNode(treeNode,false);
				closeTip();
			}else{
				$.ajax({
					type:'post',
					async:true,
					cache:false,
					url:'${ctx}/specific/specificServiceCfg/ajaxRemoveApp',
					data:{
						"specServiceId":treeNode.id
					},
					//dataType:"json",
					//contentType:"application/json; charset=utf-8",
					success:function(data){
						if(data){
							var zTree = $.fn.zTree.getZTreeObj("tree");
							var pId=treeNode.parentTId;
							try{zTree.removeNode(treeNode,false);}catch(e){}
							var node = tree.getNodeByTId(pId);
							if(node){
								if(!node.isParent){//强制改变没有子节点的特定服务业务类型为父节点，让其改变样式
									node.isParent=true;
									tree.updateNode(node,false);
								}
							}
						}
						closeTip();
					}
				}); 
			}
			
		} */
		//用户只能rename自己添加的节点
		/* function showRenameBtn(treeId, treeNode) {
			if(treeNode.isParent){
				return false;
			}
			if("${enableAddBtn}"&&"${enableAddBtn}"=="true"&&treeNode.user&&treeNode.user=="${userId}"){
				return true;
			}else{
				return false;
			}
		} */
		//用户只能remove自己添加的节点
		/* function showRemoveBtn(treeId, treeNode) {
			if(treeNode.isParent){
				return false;
			}
			if("${enableAddBtn}"&&"${enableAddBtn}"=="true"&&treeNode.user&&treeNode.user=="${userId}"){
				return true;
			}else{
				return false;
			}
		} */
		//只有在配置了enableAddBtn的界面上才会显示添加按钮
		function addHoverDom(treeId, treeNode) {
			//开启节点增加
			if("${enableAddBtn}"&&"${enableAddBtn}"=="true"){
				if(treeNode.isParent){
					//根节点不需要增加节点
					if(treeNode.pId==null||(treeNode.businessType&&treeNode.businessType!=-1)){
						return;
					}
					var sObj = $("#" + treeNode.tId + "_span");
					if (treeNode.editNameFlag || $("#addBtn_"+treeNode.id).length>0) return;
					var title="<spring:message code='add' /> <spring:message code='${title}' />";
					var addStr = "<span class='button add' id='addBtn_" + treeNode.id
						+ "' title='"+title+"' onfocus='this.blur();'></span>";
					sObj.after(addStr);
					var btn = $("#addBtn_"+treeNode.id);
					if (btn){
						btn.bind("click", function(){
							//区别人工添加，在ID前加上manual
							var id="manual"+new Date().getTime();
							var zTree = $.fn.zTree.getZTreeObj("tree"); //addSelectedNode
							zTree.addNodes(treeNode, {id:id, pId:treeNode.id,parentTId:treeNode.tId, name:"<spring:message code='new' /> <spring:message code='${title}' />",
								businessType:treeNode.id.replace("businessType",""),user:"${userId}",serviceType:treeNode.serviceType,groupId:"",
								serviceCode:""});
							var node = zTree.getNodeByParam("id", id,treeNode);
							if(node){
								zTree.updateNode(node);
								zTree.editName(node);
							}
							return;
						});
					}
				}
			}
		};
		function removeHoverDom(treeId, treeNode) {
			if("${enableAddBtn}"&&"${enableAddBtn}"=="true"){
				if(treeNode.isParent){
					$("#addBtn_"+treeNode.id).unbind().remove();
				}
			}
		};
		function addDiyDom(treeId, treeNode){
			if (treeNode.parentNode) return;
			if("${enableAddBtn}"&&"${enableAddBtn}"=="true"&&treeNode.user&&treeNode.user=="${userId}"){
				if(treeNode.tId){
					var aBtn=$("#"+treeNode.tId+"_a");
					var editStr = "<span class='btn edit' id='" +treeNode.tId+ "_editBtn' title='"+"<spring:message code='edit'/>"+"' treenode_edit'><span class='button edit'></span></span>";
					var removeStr = "<span class='btn remove' id='" +treeNode.tId+ "_removeBtn' title='"+"<spring:message code='remove'/>"+"' treenode_remove'><span class='button remove'></span></span>";
					aBtn.append(editStr);
					aBtn.append(removeStr);
					//添加重命名按钮绑定事件
					var editBtn = $("#"+treeNode.tId+"_editBtn");
					if(editBtn){
						editBtn.bind("click", function(){
							var zTree = $.fn.zTree.getZTreeObj("tree"); 
							//替代beforeEditName
							if(confirm("<spring:message code='edit' />" + treeNode.name + "?")){
								zTree.editName(treeNode);
							}
							return;
						});
					}
					//添加移除按钮绑定事件
					var removeBtn = $("#"+treeNode.tId+"_removeBtn");
					if(removeBtn){
						removeBtn.bind("click", function(){
							var zTree = $.fn.zTree.getZTreeObj("tree"); 
							var pId=treeNode.parentTId;
							//替代beforeRemoveName
							if(confirm("<spring:message code='remove' /> " + treeNode.name + "?")){
								loading("<spring:message code='loading' />...");
								if(typeof treeNode=="string"){
									zTree.removeNode(treeNode,false);
									var node = tree.getNodeByTId(pId);
									if(node){
										if(!node.isParent){//强制改变没有子节点的特定服务业务类型为父节点，让其改变样式
											node.isParent=true;
											tree.updateNode(node,false);
											tree.expandNode(node, true, false, false);
										}
									}
									closeTip();
								}else{
									$.ajax({
										type:'post',
										async:true,
										cache:false,
										url:'${ctx}/specific/specificServiceCfg/ajaxRemoveApp',
										data:{
											"specServiceId":treeNode.id
										},
										//dataType:"json",
										//contentType:"application/json; charset=utf-8",
										success:function(data){
											if(data){
												zTree.removeNode(treeNode,false);
												//修复父节点下没有子节点的时候其图标变成非文件夹
												var node = tree.getNodeByTId(pId);
												if(node){
													if(!node.isParent){
														node.isParent=true;
														tree.updateNode(node,false);
														tree.expandNode(node, true, false, false);
													}
												}
											}
											closeTip();
										}
									}); 
								}
							}
							return;
						});
					}
				}
			}
		}
	</script>
</head>
<body>
<!-- 
	<div style="position:absolute;right:8px;top:5px;cursor:pointer;" onclick="search();">
		<i class="icon-search"></i><label id="txt">搜索</label>
	</div> -->
	<c:if test="${enableSearch ne null and enableSearch==true}">
	<div id="search" class="form-search input-group" style="padding:10px 0 0 13px;">
		<label for="key" class="control-label" style="padding:5px 5px 3px 0;"><spring:message code="keywords"/>:</label>
		<input type="text" class="empty input-small" id="key" name="key" maxlength="50" style="width:170px;" placeholder="<spring:message code='search'/>...">
		<!-- <button class="btn" id="btn" onclick="searchNode()"> <i class="icon-search"></i> &nbsp;搜索 </button> -->
	</div>
	</c:if>
	<div id="tree" class="ztree" style="padding:15px 20px;"></div>
</body>