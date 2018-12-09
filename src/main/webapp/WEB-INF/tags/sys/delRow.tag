<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="searchUrl" type="java.lang.String" required="false"%>
<%@ attribute name="label" type="java.lang.String" required="false"%>
<%@ attribute name="maxRow" type="java.lang.Long" required="false"%>
<c:choose>
	<c:when test="${label eq 'delete'}">
		<a href="javascript:void(0);" class="btn btn-default" onclick="del('${url}')" data-toggle="tooltip" data-placement="top">
			<i class="fa fa-trash"> <spring:message code="delete"/></i>
		</a>
	</c:when>
	<c:when test="${label eq 'update'}">
		<a href="javascript:void(0);" onclick="update('${url}')" class="btn btn-default" data-toggle="tooltip" data-placement="top">
			<i class="fa fa-check"></i> <spring:message code="edit"/>
		</a>
	</c:when>
	<c:when test="${label eq 'approved'}">
		<a href="javascript:void(0);" onclick="passOpt('${url}')"><i class="fa fa-check"></i> <spring:message code="approved"/></a>
	</c:when>
	<c:when test="${label eq 'unapproved'}">
		<a href="javascript:void(0);" onclick="noPassOpt('${url}')"><i class="fa fa-remove"></i> <spring:message code="unapproved"/></a>
	</c:when>
	<c:when test="${label eq 'cancelPass'}">
		<a href="javascript:void(0);" onclick="cancelPassOpt('${url}')"><i class="fa fa-undo"></i> <spring:message code="cancel_approved"/></a>
	</c:when>
	<c:when test="${label eq 'export'}">
		<a href="javascript:void(0);" class="btn btn-default" onclick="exportData('${url}',${fns:getStringProperty('maxExportSize',1000000)},'${searchUrl}')" data-toggle="tooltip" data-placement="top">
			<i class="fa fa-download"> <spring:message code="export"/></i>
		</a>
	</c:when>
</c:choose>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>
<script type="text/javascript">
$(document).ready(function() {
    $('#${id} thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('check');
    	});

    $('#${id} thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
    	  $('#${id} tbody tr td input.i-checks').iCheck('uncheck');
    	});
});
//删除
function del(url){
	var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
	
	if($(checkboxes).filter(":checked").length>0){
		if(validateAllNoAudit(checkboxes)){
			top.$.jBox.tip("<spring:message code='has_prohibit_delete'/>", "<spring:message code='info'/>");
			return;
		}else{
			var serviceGroupIds=[],ids=[],canDel=true;
			$(checkboxes).filter(":checked").each(function(){
				if($(this).attr("serviceGroupId")&&$(this).attr("isValid")){
					if($(this).attr("isValid")==1){//下发过的组的ip才需要判断是否是组内最后一个IP
						serviceGroupIds.push($(this).attr("serviceGroupId"));
					}
					ids.push($(this).attr("id"));
				}
				
			})
			if(serviceGroupIds.length>0){
				$.ajax({
 	 				type:'post',
 	 				url:'${ctx}/basics/asn/ajaxIsLast',
 	 				data:{"serviceGroupIds":serviceGroupIds.join(','),"ids":ids.join(',')},
 	 				async:false,
 	 				success:function(data,textStatus){//处理返回结果
 	 					if(data){
 	 						top.$.jBox.tip('<spring:message code="last_ip_in_asn_group"/>');
 	 						canDel=false;
 	 					}
 	 				}
 	 			});
			}
			
			if(canDel){
				doAll(checkboxes,url);
			}
		}
	}else{
		top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
		return;
	}
}
//修改配置
function update(url){
	var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
	var cked = $("tbody tr td input.i-checks:checkbox:checked");
	if($(checkboxes).filter(":checked").length==1){
		if(cked.val()==1){
			top.$.jBox.tip("<spring:message code='has_approved'/>", "<spring:message code='info'/>");
			return;
		}else if(cked.val()==3){
			top.$.jBox.tip("<spring:message code='cancel_approved'/>", "<spring:message code='info'/>");
			return;
		}
		//处理asnIp,asn组下发过的ip配置不允许修改
		if(cked.attr("serviceGroupId")&&cked.attr("isValid")){
			var serviceGroupId=cked.attr("serviceGroupId");
			var _isValid=cked.attr("isValid");
			if(_isValid==1){
				top.$.jBox.tip("<spring:message code='can_not_edit_issued_ans_ip'/>", "<spring:message code='info'/>");
				return;
			}
		}
		doAll(checkboxes,url);
	}else{
		top.$.jBox.tip("<spring:message code='check_one'/>", "<spring:message code='info'/>");
		return;
	}
}
//通过
function passOpt(url){
var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
	
	if($(checkboxes).filter(":checked").length>0){
		if(validateAllNoAudit(checkboxes)){
			top.$.jBox.tip("<spring:message code='has_prohibit_pass'/>", "<spring:message code='info'/>");
			return;
		}else{
					doAll(checkboxes,url);
		}
	}else{
		/* 	批量下发待测试
			if(url.indexOf("functionId") > -1){
			top.$.jBox.confirm("<spring:message code='confirm_message'/>","<spring:message code='approved_all'/>",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action",url);
					page();
				}
			});
		}else{ */
			top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			return;
		/* } */
	}
}
//未通过
function noPassOpt(url){
var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
	
	if($(checkboxes).filter(":checked").length>0){
		if(validateAllNoAudit(checkboxes)){
			top.$.jBox.tip("<spring:message code='has_prohibit_nopass'/>", "<spring:message code='info'/>");
			return;
			}else{
					doAll(checkboxes,url);
				}
			}else{
				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
				return;
		}
}
//配置取消
function cancelPassOpt(url){
	var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
	
	if($(checkboxes).filter(":checked").length>0){
		if(validateAllPass(checkboxes)){
			
			var addrPoolIds = [],canCancel = true;
			$(checkboxes).filter(":checked").each(function(){
				var addrPoolId = $(this).attr("addressPoolId");
				if(typeof addrPoolId != "undefined"){
					addrPoolIds.push(addrPoolId);
				}
			});
			
			// 地址池管理 （被策略引用的地址池不可取消）
			if(addrPoolIds.length > 0){
				$.ajax({
	 				type:'post',
	 				url:'${ctx}/manipulation/ipmulitiplex/ajaxIsUsed',
	 				data:{"addrPoolIds":addrPoolIds.join(',')},
	 				async:false,
	 				success:function(data){//处理返回结果
	 					if(data == false){
	 						top.$.jBox.tip('<spring:message code="address_pool_is_used"/>');
	 						canCancel=data;
	 					}
	 				}
	 			});
			}
			
			if(canCancel){
				doAll(checkboxes,url);
			}
		}else{
			top.$.jBox.tip("<spring:message code='hasnot_approved'/>", "<spring:message code='info'/>");
			return;
		}
		
	}else{
		/* 批量下发待测试
			if(url.indexOf("functionId") > -1){
			top.$.jBox.confirm("<spring:message code='confirm_message'/>","<spring:message code='cancle_all'/>",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action",url);
					page();
				}
			});
		}else{  */
			top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			return;
		/* }  */
	}
}
	function doAll(checkboxes,url){
		// var url = $(this).attr('data-url');
		  var str="";
		  var compileIdStr="";
		  var ids="";
		  var compileIds="";
		  var flag=false;//是否删除的为但是响应策略的无策略配置
		  checkboxes.each(function(){
		    if(true == $(this).is(':checked')){
		    	var id=$(this).attr("id");
		    	var serviceId=$(this).attr("id");
		    	if($(this).attr("serviceId")){
		    		  serviceId=$(this).attr("serviceId");
		      	}
		    	if(serviceId == 65 && id==0){
		    		flag=true;
		    	}
		      	str+=$(this).attr("id")+",";
		      	if($(this).attr("compileId")){
		    		  compileIdStr +=$(this).attr("compileId")+",";
		      	}
		    }
		  });
		  
		  
		  if(flag && url.indexOf("?isAudit=3") > 1){
			  top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			return;
		  }
		  
		  if(str.substr(str.length-1)== ','){
		    ids = str.substr(0,str.length-1);
		  }
		  if(compileIdStr.substr(compileIdStr.length-1)== ','){
			  compileIds = compileIdStr.substr(0,compileIdStr.length-1);
		  }
		  if(ids == ""){
			  //top.$.jBox.tip("不能选择公共模型（"+nodes[i].name+"）请重新选择。");
			  top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
			return;
		  }
		  if((url.indexOf("?isAudit=3") > 1)){
			  // 审核中区分是否有添加取消来函
			 var requestName=$("#ifCancelRequestInfo").attr("requestName");
			 var indexTable=$("#ifCancelRequestInfo").attr("indexTable");
			 	 // 有来函业务的取消审核
				if(requestName!=null&&indexTable!=null&&requestName.indexOf("null")==-1&&indexTable.indexOf("null")==-1){
					/****************取消审核时增加来函**************************  */
					$.jBox.open("iframe:${ctx}/cfg/request/requestSelectInfo", "<spring:message code='cancel_approved'/>",500, 400, { buttons: { '<spring:message code="ok"/>': 1, '<spring:message code="cancel"/>': 0 },
						submit: function (v, h, f) {
				    if (v == 0) {
				        return true; // close the window
				    } else {
				        h.find('.errorBlock').hide('fast', function () { $(this).remove(); });
				        var cancelRequestId = h.find("iframe")[0].contentWindow.requestIsAudit.value;
				        if (!cancelRequestId) {
				            $('<div class="errorBlock" style="display: none;font-size:18px;color:red;text-align:center;margin-top:10px"><spring:message code="required"/></div>').prependTo(h).show('fast');
				            return false;
				        }
								if(url.indexOf("?")>0){
									$.ajax({
					 	 				type:'post',
					 	 				url:'${ctx}/basics/serviceDictInfo/requestCancleInfoAjax',
					 	 				data:{"cancelRequestId":cancelRequestId,"ids":ids,"indexTable":indexTable},
					 	 				async:false,
					 	 				success:function(data,textStatus){//处理返回结果
					 	 					window.location = url+"&ids="+ids+"&compileIds="+compileIds;
					 	 				}
					 	 			});
								}else{
									window.location = url+"?ids="+ids+"&compileIds="+compileIds;
								} 
									if(url.indexOf("?isAudit") > 1){
										loading('<spring:message code="onloading"/>');
									}else if(url.indexOf("export") > 1){
										closeTip(); 
									}
				        
				    }

				    return true;
				},
								loaded:function(h){
									$(".jbox-content,top.document").css("overflow-y","hidden")
									$(".jbox-content,top.document.children").css("clear","both")
									$(".jbox-content,top.document.children").css("z-index","999999")
									$(".jbox-content,top.document.children").css("overflow;","auto")
									$(".jbox-content,top.document").css("width","90%")
								}
						}); 
					
					
					
					
					
					/******************************************  */
					}else{
						// 无来函选项的取消审核操作
						top.$.jBox.confirm("<spring:message code='confirm_message'/>","<spring:message code='info'/>",function(v,h,f){
							
							if(v=="ok"){
								if(url.indexOf("?")>0){
									window.location = url+"&ids="+ids+"&compileIds="+compileIds;
								}else{
									window.location = url+"?ids="+ids+"&compileIds="+compileIds;
								}
								if(url.indexOf("?isAudit") > 1){
									loading('<spring:message code="onloading"/>');
								}else if(url.indexOf("export") > 1){
									closeTip();
								}
								//$("#searchForm").submit();
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');	
					}
				//除取消审核之外的业务
			  }else{
				  
				top.$.jBox.confirm("<spring:message code='confirm_message'/>","<spring:message code='info'/>",function(v,h,f){
				
				if(v=="ok"){
					if(url.indexOf("?")>0){
						window.location = url+"&ids="+ids+"&compileIds="+compileIds;
					}else{
						window.location = url+"?ids="+ids+"&compileIds="+compileIds;
					}
					if(url.indexOf("?isAudit") > 1){
						loading('<spring:message code="onloading"/>');
					}else if(url.indexOf("export") > 1){
						closeTip();
					}
					//$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
	}
	//验证选择的配置，是否可删除或者审核未通过或者审核通过,只有未审核的配置可删除或审核未通过或审核通过
	function validateAllNoAudit(checkboxes){
		var flag = false;
		$(checkboxes).filter(":checked").each(function(){
			if($(this).val()!=0){
				flag = true;
				return;
			}		 	
		});
		return flag;
	}
	//验证选择的配置，是否有审核通过的
	function validatePass(checkboxes){
		var flag = false;
		$(checkboxes).filter(":checked").each(function(){
			if($(this).val()==1){
				flag = true;
				return;
			}		 	
		});
		return flag;
	}
	//验证选择的配置，是否全部为审核通过的
	function validateAllPass(checkboxes){
		var flag = true;
		$(checkboxes).filter(":checked").each(function(){
			if($(this).val()!=1){
				flag = false;
				return;
			}		 	
		});
		return flag;
	}
	//导出
	function exportData(url,maxRow,searchUrl){
		var column=[];
		$("#${id} ${value} thead tr th").each(function(){
			if($(this).is(":visible")){
				column.push($(this).text().trim());
			}
		});
		if($("#exportType").val() != null && $("#exportType").val() != ""){
		  if($("#exportValue").val() != null && $("#exportValue").val() != ""){
			url+="&"+$("#exportType").val()+"="+$("#exportValue").val();
		  }
		}
		if(column){
			url+="&columns="+column.toString();
		}
		var checkboxes=$("#${id} ${value} tbody tr td input.i-checks:checkbox");
		//导出选中数据
		if($(checkboxes).filter(":checked").length>0){ 
			doAll(checkboxes,url);
		//导出检索条件下所有数据
		}else{
			var totalCount = $("#showTotalCount").html() == undefined ? '' : $("#showTotalCount").html().trim();
			if(totalCount > maxRow){
				top.$.jBox.confirm("<spring:message code='export_confirm_message'/>"+maxRow+"!","<spring:message code='info'/>",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action",url);
						$("#searchForm").submit();
						closeTip();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}else{
				$("#searchForm").attr("action",url);
				$("#searchForm").submit();
				closeTip();
				$("#searchForm").attr("action",searchUrl);
			}
		}
	}
</script>