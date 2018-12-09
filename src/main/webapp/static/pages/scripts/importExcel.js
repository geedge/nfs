$(function(){
	//打开导入modal
	$(".import").click(function(){
		$("#import_modal").modal({
			backdrop:"static",
			keyboard:false,
			show:true
		});
	});
	$("#appIdId").on("change",function(){
		ajaxBehaviour($(this).val());
	});
	switchService();
	$("input[name='serviceDictId']").click(function(){
		switchService();
	});
	$("input[name='regionDictIds']").click(function(){
	});
	
});
/**
 * action动作切换
 */
function switchService(){
	var functionId=$("#functionId").val();
	if(functionId==5 || functionId==6){//IP配置特殊处理 //URL配置特殊处理
		var cfgRegionCodeS=$("input[name='serviceDictId']:checked").attr("cfgRegionCodeS");
		var cfgRegionCode=cfgRegionCodeS.split(",")[0];
		$("input[name='regionDictIds'][cfgregioncoder!='"+cfgRegionCode+"']").prop("checked",false);
		$("input[name='regionDictIds'][cfgRegionCodeR!='"+cfgRegionCode+"']").parents(".radio-inline").addClass("hidden");
		$("input[name='regionDictIds'][cfgRegionCodeR='"+cfgRegionCode+"']").prop("checked",true);
		$("input[name='regionDictIds'][cfgRegionCodeR='"+cfgRegionCode+"']").parents(".radio-inline").removeClass("hidden");
		if(cfgRegionCodeS.split(",").length <=1 ){
			$(".region").addClass("hidden");
		}else{
			$(".region").removeClass("hidden");
		}
	}else{
		if($("input[name='regionDictIds']").length <=1){
			$(".region").addClass("hidden");
		}else{
			$(".region").removeClass("hidden");
		}
	}
	if($("input[name='serviceDictId']").length <=1){
		$(".service").addClass("hidden");
	}else{
		$(".service").removeClass("hidden");
	}
	
}
/**
 * 模板下载
 * TODO 后期修改为传入regionDictId
 * */
function downLoadTemplate(path){
	var obj={
				"regionDictId":$("input[name='regionDictIds']:checked").val()
				,"serviceDictId":$("input[name='serviceDictId']:checked").val()
				,"requestId":$("select[name='requestId']").val()
			};
	var params = $.param(obj); 
	document.location.href = path+'/ntc/iplist/import/template?' + params;
	
}


var ajaxBehaviour=function(val){
	var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
	var request=$.ajax({
		type:'post',
		url:pathName+'/specific/specificServiceCfg/childrenList',
		data:{"parent":val},
		dataType:'json',
		async:true,
		success:function(data,textStatus){//处理返回结果
			if(textStatus=="success"){
				var html='<select name="behaviorId" data-live-search="true" class="selectpicker form-control">'
					+'<option value=""><spring:message code="select"/></option>';
				if(data.length>0){
						for(i=0;i<data.length;i++){
							html+='<option value="'+data[i].code+'"';
							html+='>'+data[i].name+'</option>';
	      			   	}
						html+='</select>';
						$("#behaviorId").html(html);
						$("[name='behaviorId']").selectpicker("refresh");
						$("[name='behaviorId']").selectpicker("render");
				}else{
					html+='</select>';
					$("#behaviorId").html(html);
					$("[name='behaviorId']").selectpicker("refresh");
					$("[name='behaviorId']").selectpicker("render");
				}
				
			}
		},
		complete:function(XMLHttpRequest,status){//超时设置
			if(status=="timeout"){
				var html='<select name="behaviorId" data-live-search="true" class="selectpicker form-control">'
					+'<option value=""><spring:message code="select"/></option></select>';
				$("#behaviorId").html(html);
				$("[name='behaviorId']").selectpicker("refresh");
				$("[name='behaviorId']").selectpicker("render");
			}
		}
	});
}
//导入配置
var importCfg=function(){
	if($("#serviceId").val()==""){
		alert("请选择action");
		return ;
	}
	var appFlag=true;
	if(($("#functionId").val() == 63)||($("#functionId").val() == 408)||($("#functionId").val() == 407)||
			($("#functionId").val() == 402)||($("#functionId").val() == 403)||($("#functionId").val() == 405)){
		var appIdValue=$("#appIdName").val();
		if(appIdValue == null || appIdValue ==''){
			appFlag=false;
		}
	}
	var fileName = $(".fileupload-preview", $("#importForm1")).text();
 	var $error = $('.alert-error', $("#importForm1"));
	if(!appFlag){
		$("span",$error).html($("#appError").val());
 		$error.removeClass("hide");
 		$error.addClass("show");
    	return false;
	}
 	
 	if(fileName==""){
 		$("span",$error).html($("#importTip").val());
 		$error.removeClass("hide");
 		$error.addClass("show");
    	return false;
    	
 	}else if(fileName.lastIndexOf("\.")==-1/*||fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xls'*/ ||fileName.substring(fileName.lastIndexOf("\.")+1).toLowerCase()!='xlsx'){
 		$("span",$error).html($("#importTip").val());
 		$error.removeClass("hide");
 		$error.addClass("show");
    	return false;
 	}
 	$("#import_modal").modal('hide');//导入文件隐藏
	$("#importForm1").submit();
}