$(function(){
	var ids=[];
	$(".appCode").each(function(){
		if($(this).attr("id")&&$(this).attr("id")!=''){
			ids.push($(this).attr("id"));
		}
		//ids.push($(this).attr("id"));
	});
	$(".behavCode").each(function(){
		if($(this).attr("id")&&$(this).attr("id")!=''){
			ids.push($(this).attr("id"));
		}
	});
	if($(".appCode").length>0){
		var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
		$.ajax({
			type:'post',
			async:false,
			url:pathName+'/app/ajaxAppName',
			data:{"ids":ids.join(",")},
			success:function(data){
				if(data&&data.length==2){
					var apps=data[0];
					for(var ind in apps){
						$("[class='appCode'][id='"+ind+"']").html(apps[ind]);
					}
					var behavs=data[1];
					for(var ind in behavs){
						$("[class='behavCode'][id='"+ind+"']").html(behavs[ind]);
					}
				}
			}
		});
	}
	var asnNos=[];
	$(".configGroup").each(function(){
		if($(this).attr("asnNo")&&$(this).attr("asnNo")!=''){
			asnNos.push($(this).attr("asnNo"));
		}
	});
	if(asnNos.length>0){
		var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.indexOf("/nis")+4);
		$.ajax({
			type:'post',
			async:false,
			url:pathName+'/basics/asn/ajaxGroupName',
			data:{"ids":asnNos.join(",")},
			success:function(data){
				if(data){
					for(var ind in data){
						$("[class='configGroup'][asnNo='"+ind+"']").html(data[ind]);
					}
				}
			}
		});
	}
});