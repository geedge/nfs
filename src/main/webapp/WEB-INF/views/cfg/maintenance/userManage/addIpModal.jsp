<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>

<script type="text/javascript">
function showVpnIp(id,serverIp){
	$("#vpn_modal").modal({
		backdrop:"static",
		keyboard:false,
		show:true
	});
	$("#serverIpId").val(serverIp);
	$("#serverIpName").val(serverIp);
	$("#userId").val(id);
}

var saveIp=function(){
	if($("#serverIpId").val()==""){
		 top.$.jBox.tip("<spring:message code='vpn_ip'/> <spring:message code='required'/>","");
		return ;
	}
	$("#vpn_modal").modal('hide');
	$("#importForm1").submit();
}
</script> 
<div class="modal fade" id="vpn_modal" tabindex="-1" role="dialog" aria-labelledby="mo" aria-hidden="true">
	<form id="importForm1" action="${ctx}/maintenance/userManage/save" method="post" enctype="multipart/form-data" style="margin-top: 10%;" class="form-horizontal"
		onsubmit="loading('<spring:message code='loading'/>');">
		<input type="hidden"  id="importTip" value="<spring:message code='import_tip_excel'/>">
		<div class="modal-dialog" role="document" style="width:700px;">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">
						<spring:message code="vpn_ip" />
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
				<input type="hidden"  id="userId"  name="id" />
				<div class="form-group" style="margin-top: 15px;">
					<label class="control-label col-md-3">
					 <spring:message code="vpn_ip" />
					</label>
					<div class="col-md-6">
						<sys:treeselect id="serverIp" name="serverIp"
													value="${user.serverIp}" labelName="user.serverIp"
													notAllowSelectParent="true" enableSearch="true"
													notAllowSelectRoot="true"   checkedPS="ps"  unCheckedPS="ps"
													labelValue="${fn:substring(user.serverIp,0,(fn:length(user.serverIp)-1)) }" title="vpn_ip"
													url="/maintenance/userManage/treeData?isLeafShow=false&cfgType=3&specific=true" extId="0"
													checked="true"   cssClass="form-control required" />
					</div>
					<span class="help-inline"><font color="red">*</font> </span>
					<div for="user.serverIp"></div>
				</div>
					<div class="modal-footer">
					<button type="button" class="btn red" onclick="saveIp()">
						<spring:message code="ok" />
					</button>
					<button type="button" class="btn" data-dismiss="modal">
						<spring:message code="close" />
					</button>
				</div>
			</div>
		</div>
	</form>
</div>