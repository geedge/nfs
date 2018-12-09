<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>机构管理</title>

<style type="text/css">
	#officeContent {
		margin-left: 10px;
		margin-top: -10px;
	}
</style>
<link href="${ctxStatic}/global/plugins/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<script src="${ctxStatic}/global/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/global/scripts/mustache.min.js" type="text/javascript"></script>
</head>
<body>
<div class="page-content-body">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/sys/office/form?itType=${itType}'">
			<i class="fa fa-plus"></i>
			<spring:message code="add"></spring:message></button>
	</div>
	
	<h3 class="page-title">
	<spring:message code="officeManage"></spring:message>
	</h3>
	
	<h5 class="page-header"></h5>

	<sys:message content="${message}"/>

	
	<div class="row">
          <div class="col-md-2">
          	
          	<div class="portlet light bordered">
           	<div class="portlet-title">
           	
                     <div class="caption">
                         <i class="icon-list  font-blue-sharp"></i>
                         <span class="caption-subject font-blue-sharp bold uppercase">组织机构</span>
                     	   
                     </div>
                     <div class="actions">
                        <a class="accordion-toggle"><i class="icon-refresh" onclick="refreshTree();"></i></a>
                     </div>
		    </div>
		    
               <div id="ztree" class="ztree portlet-body"></div>
          	</div>
          	
          </div>
          <div class="col-md-10">
         		 <div id="officeContent"></div>
          </div>
    </div>
</div>
	<script type="text/javascript">
		
	$(document).ready(function(){
		refreshTree();
	});
	
	 var setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			callback:{onClick:function(event, treeId, treeNode){
					var id = treeNode.pId ? treeNode.id : 1;

					var pageContentBody = $('#officeContent');
					var url = "${ctx}/sys/office/list?id="+id+"&parentIds="+treeNode.pIds;
					
					App.startPageLoading({animate:true});
					
				    $.ajax({
		                type: "GET",
		                cache: false,
		                url: url,
		                dataType: "html",
		                success: function (res) {
		                	App.stopPageLoading();
		                    pageContentBody.html(res);
		                },
		                error: function (xhr, ajaxOptions, thrownError) {
		                    pageContentBody.html('<h4>Could not load the requested content.</h4>');
		                    App.stopPageLoading();
		                }
		            });
				    
					  
				}
			}
		};
		
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
				var tree = $.fn.zTree.init($("#ztree"), setting, data);
				var nodes = tree.getNodesByParam("level", 0);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
				}
				var secondNodes = tree.getNodesByParam("level", 1);
				 $("#"+secondNodes[0].tId+"_a").click();
				
			});
		}
		
		 
		
	</script>

</body>


	
</html>