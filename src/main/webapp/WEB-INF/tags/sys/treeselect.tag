<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<%@ attribute name="selectDepartment" type="java.lang.String" required="false" description="不允许选择部门,自办部门,转办部门"%>
<%@ attribute name="showParentName" type="java.lang.String" required="false" description="显示父类名称"%>
<%@ attribute name="checkedPS" type="java.lang.String" required="false" description="复选框选中时是否关联父子节点，ps关联父子，p关联父，s关联子,都不关联为空"%>
<%@ attribute name="unCheckedPS" type="java.lang.String" required="false" description="复选框取消选中时是否关联父子节点，ps关联父子，p关联父，s关联子,都不关联为空"%>
<%@ attribute name="enableAddBtn" type="java.lang.Boolean" required="false" description="节点是否可编辑"%>
<%@ attribute name="enableSearch" type="java.lang.Boolean" required="false" description="是否开启节点搜索功能"%>
<%@ attribute name="labelEmptyValue" type="java.lang.String" required="false" description="标签不能为空的时候当清空选择时补填的值"%>
<div class="input-group">
	<input id="${id}Id" name="${name}" class="${cssClass} singleClass" type="hidden" value="${value}" />
	<input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="<spring:message code='${empty value?"":labelValue}'/>" data-msg-required="${dataMsgRequired}" placeholder="${(empty value or value eq extId)?labelValue:value}"
		class="${cssClass}" style="${cssStyle} background-color:transparent"/>
		
		<div class="input-group-btn">
             <a id="${id}Button" class="btn btn-default btn-search" href="javascript:" class="btn ${disabled} ${hideBtn ? 'hide' : ''}" style="${smallBtn?'padding:4px 2px;':''}"><i class="fa fa-search"></i></a>
        </div>
	
</div>
<script type="text/javascript">
	$("#${id}Button, #${id}Name").click(function(){
		// 是否限制选择，如果限制，设置为disablegetQueryString d
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&selectIds="+$("#${id}Id").val()+"&module=${module}&checked=${checked}&checkedPS=${checkedPS}&unCheckedPS=${unCheckedPS}&extId=${extId}&isAll=${isAll}&title=${title}&enableAddBtn=${enableAddBtn}&enableSearch=${enableSearch}", "<spring:message code='choose'/> <spring:message code='${title}'/>", 320, 420, {
			ajaxData:{selectIds: $("#${id}Id").val()},buttons:{"<spring:message code='ok'/>":"ok", "<spring:message code='clear'/>":"clear","<spring:message code='close'/>":true}, submit:function(v, h, f){
				if (v == "ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					var ids = [], names = [], nodes = [];
					if ("${checked}" == "true"){
						nodes = tree.getCheckedNodes(true);
					}else{
						nodes = tree.getSelectedNodes();
					}
					//wx提示，c:if标签前加了//注释,c:if标签的判断条件仍会生效
					for(var i=0; i<nodes.length; i++) {
						//<c:if test="${checked && notAllowSelectParent}">
						if (nodes[i].isParent && "${checkedPS}"!=''){
							continue; // 如果为复选框选择，并且父子节点有关联，则过滤掉父节点
						}//</c:if>
						//<c:if test="${notAllowSelectRoot}">
						if (nodes[i].level == 0){
							top.$.jBox.tip("<spring:message code='no_root'/>（"+nodes[i].name+"）<spring:message code='reselect'/>");
							return false;
						}//</c:if>
						//<c:if test="${notAllowSelectParent}">
						if (nodes[i].isParent){
							top.$.jBox.tip("<spring:message code='no_parent'/>（"+nodes[i].name+"）<spring:message code='reselect'/>");
							return false;
						}
						//wx: 带有business属性的节点特殊处理
						if(nodes[i].businessType&&nodes[i].businessType==-1){
							top.$.jBox.tip("<spring:message code='no_parent'/>（"+nodes[i].name+"）<spring:message code='reselect'/>");
							return false;
						}
						//</c:if>
						//<c:if test="${not empty module && selectScopeModule}">
						if (nodes[i].module == ""){
							top.$.jBox.tip("<spring:message code='no_public_model'/>（"+nodes[i].name+"）<spring:message code='reselect'/>");
							return false;
						}else if (nodes[i].module != "${module}"){
							top.$.jBox.tip("<spring:message code='no_outside_column'/>，<spring:message code='reselect'/>");
							return false;
						}//</c:if>
						//<c:if test="${selectDepartment}">
						if (nodes[i].name=="<spring:message code='send_org'/>" || nodes[i].name=="<spring:message code='department'/>" ||nodes[i].name=="<spring:message code='sendind_org'/>"){
							top.$.jBox.tip("<spring:message code='no_node'/>（"+nodes[i].name+"）<spring:message code='reselect'/>");
							return false;
						}//</c:if>
						if("${labelEmptyValue}"&&""!="${labelEmptyValue}"){
							if("${extId}"==nodes[i].id){
								continue;
							}
						}
						ids.push(nodes[i].id);
						//<c:if test="${showParentName}">
						if (nodes[i].id != null ){
						  	$.ajax({
								type:"post",
								async: false,
								url:"${ctx}/letter/process/queryParentName",
								data:{'deptId':nodes[i].id},
								dateType:"json",
								success:function(data){
									var data=eval('('+data+')');
									 names.push(data[0].parentName);
									 
								},
								
								error: function(e) { 
									names.push(nodes[i].name);
								} 
								
							});
						 
						}//</c:if>
						//<c:if test="${!showParentName}">
							names.push(nodes[i].name);
						//</c:if>
						//<c:if test="${!checked}">
						break; // 如果为非复选框选择，则返回第一个选择  
						//</c:if>
					}
					if(ids.length==0&&"${labelEmptyValue}"&&""!="${labelEmptyValue}"){
						ids.push("${labelEmptyValue}");
					}
					$("#${id}Id").val(ids.join(",").replace(/u_/ig,""));
					$("#${id}Id").change();//手动触发change事件，使Id的值得变化可以被监听到
					$("#${id}Name").val(names.join(","));
					if(!$("#${id}Name").val()&&'${value}'){//如果值不存在，但是value却有值，将placeholder清空
						$("#${id}Name").attr("placeholder","");
					}
				}else if (v == "clear"){
					if("${labelEmptyValue}"&&""!="${labelEmptyValue}"){
						$("#${id}Id").val("${labelEmptyValue}");
					}else{
						$("#${id}Id").val("");
					}
					$("#${id}Id").change();//手动触发change事件，使Id的值得变化可以被监听到
					$("#${id}Name").val("");
					$("#${id}Name").attr("placeholder","");
                }
				if(typeof ${id}TreeselectCallBack == 'function'){
					${id}TreeselectCallBack(v, h, f);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
				var language="${cookie.Language.value}";
				if(language=='ru_RU'){
					$(".jbox-title-panel", top.document).css("height","48px");
				}
				
			}
		});
	});
</script>