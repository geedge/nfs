<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>菜单管理</title>

<link
	href="${ctxStatic}/global/plugins/treeTable/themes/vsStyle/treeTable.min.css"
	rel="stylesheet" type="text/css" />
<script
	src="${ctxStatic}/global/plugins/treeTable/jquery.treeTable.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treeTable({
			expandLevel : 3
		}).show();
		$("input[name=sorts]").each(function(){
			 $(this).keydown(function(event){
					if(event.keyCode==13){
						$.ajax({
							type:'post',
							async:false,
							url:'${ctx}/sys/menu/updateSort',
							data:{"menuId":$(this).attr("menuId"),"sort":$(this).val()},
							success:function(data){
								if(data){
									top.$.jBox.tip("<spring:message code='update_success'/>", "<spring:message code='info'/>");
								}else{
									top.$.jBox.tip("<spring:message code='update_failed'/>", "<spring:message code='info'/>");
								}	
							}
						}
						);
					}
				})
		}); 
	});
	function updateSort() {
		loading('onloading...');
		$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
		$("#listForm").submit();
	}
	/* $("input[name=sorts]").each(function(){
		 $(this).bind("keyup",function(event){
					alert($(this).attr("menuId"));
					alert($(this).val());
				if(event.keyCode==13){
				}
			})
	});  */
	
</script>
</head>
<body>
	<div class="page-content">
		<div class="theme-panel hidden-xs hidden-sm">

			<shiro:hasPermission name="sys:menu:edit">
				<button type="button" class="btn btn-primary"
					onClick="javascript:window.location='${ctx}/sys/menu/form'">
					&nbsp;
					<spring:message code="add" />
					&nbsp;
				</button>
			</shiro:hasPermission>

		</div>

		<h3 class="page-title">
			<spring:message code="menuManage" />
		</h3>

		<h5 class="page-header"></h5>

		<div class="row">
			<div class="col-md-12">
				<div class="portlet">

					<div class="portlet-body">

						<div class="row">

							<div class="col-md-12">

								<sys:message content="${message}" />
								<form id="listForm" method="post">
									<table id="treeTable"
										class="table table-striped table-bordered table-condensed">
										<thead>
											<tr>
												<th><spring:message code="menu_name" /></th>
												<th><spring:message code="unicode" /></th>
												<th><spring:message code="functionId" /></th>
												<th><spring:message code="link" /></th>
												<th style="text-align:center;"><spring:message
														code="sort" /></th>
												<th><spring:message code="visible" /></th>
												<th><spring:message code="permission_mark" /></th>
												<shiro:hasPermission name="sys:menu:edit">
													<th><spring:message code="operation" /></th>
												</shiro:hasPermission>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${list}" var="menu">
												<tr id="${menu.id}"
													pId="${menu.parent.id ne 1?menu.parent.id:0}">
													<td nowrap><i
														class="${not empty menu.icon?menu.icon:' hide'}  font-blue-sharp"></i>
														&nbsp;<a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></td>
													<td>${menu.code}</td>
													<td>${menu.functionId}</td>
													<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
													<td style="text-align:center;"><shiro:hasPermission
															name="sys:menu:edit">
															<input type="hidden" name="ids" value="${menu.id}" />
															<input name="sorts" type="text" value="${menu.sort}" menuId="${menu.id}"
																style="width:50px;margin:0;padding:0;text-align:center;border: 1px solid #ddd;">
														</shiro:hasPermission>
														<shiro:lacksPermission name="sys:menu:edit">
														${menu.sort}
													</shiro:lacksPermission></td>
													<td><c:choose>
															<c:when test="${menu.isShow eq '1'}">
																<spring:message code="display" />
															</c:when>
															<c:otherwise>
																<spring:message code="hide" />
															</c:otherwise>
														</c:choose></td>
													<td title="${menu.permission}">${fns:abbr(menu.permission,30)}</td>
													<shiro:hasPermission name="sys:menu:edit">
														<td nowrap><a
															href="${ctx}/sys/menu/form?id=${menu.id}"><spring:message
																	code="edit" /></a> <a
															href="${ctx}/sys/menu/delete?id=${menu.id}"
															onclick="return confirmx('<spring:message code="delete_all_child"/>', this.href)"><spring:message
																	code="delete" /></a> <a
															href="${ctx}/sys/menu/form?parent.id=${menu.id}"><spring:message
																	code="add_child_menu"></spring:message></a></td>
													</shiro:hasPermission>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>

							</div>
						</div>
					</div>

				</div>
			</div>

		</div>

	</div>
</body>
</html>