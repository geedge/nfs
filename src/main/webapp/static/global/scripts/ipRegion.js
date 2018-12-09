/**
 * 处理IP域值
 */
;(function($){
	$.extend({
		ipcommon:function(){
			return {};
		}
	});
	$.extend($.ipcommon, {
		ipType:[],
		ipPattern:[],
		portPattern:[],
		protocol:[],
		direction:[]
	});
})(jQuery);
$(function(){
	initCommIpVal();
});
window.$.ipcommon=$.ipcommon;
/**
 * ip默认选项处理
 */
var initCommIpVal=function(){
	var action,regionCode,serviceType,ipPortShow,ipType,
	ipPattern,portPattern,direction,protocol,regionType;
	if($("input[name$='action']:checked").length>0){
		regionCode=$("input[name$='action']:checked").attr("regionCode");
		action=$("input[name$='action']:checked").val();
	}else{
		regionCode=$("input[name$='action']").attr("regionCode");
		action=$("input[name$='action']").val();
	}
	if(regionCode){
		if(regionCode.indexOf(",")==-1){
			$("#cfgRegionCode"+regionCode).attr("name","cfgRegionCode");	
			$("#cfgType"+regionCode).attr("name","cfgType");
		}else{
			var _region='';
			var regionArr=regionCode.split(',');
			$("input[name$='cfgRegionCode'][regionType='1']").each(function(){
				for(var re in regionArr){
					if($(this).val()==regionArr[re]){
						_region=regionArr[re];
						break;
					}
				}
			});
			if(_region!=''){
				regionCode=_region;
			}
		}
	}
	if(regionCode){//IP域大于1个，根据action获取ip属性
		ipPortShow=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPortShow"),
		ipType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipType"),
		ipPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPattern"),
		portPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("portPattern"),
		direction=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("direction"),
		protocol=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("protocol"),
		regionType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("regionType");
	}else{//IP域只有一个，获取regionType为1的
		ipPortShow=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipPortShow"),
		ipType=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipType"),
		ipPattern=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipPattern"),
		portPattern=$("input[name$='cfgRegionCode'][regionType='1']").attr("portPattern"),
		direction=$("input[name$='cfgRegionCode'][regionType='1']").attr("direction"),
		protocol=$("input[name$='cfgRegionCode'][regionType='1']").attr("protocol"),
		regionType=$("input[name$='cfgRegionCode'][regionType='1']").attr("regionType");
	}
	//ip，端口处理源ip展示、源端口展示、目的ip展示、目的端口展示1、2、3、4
	if(ipPortShow){
		if(ipPortShow.indexOf('1')>-1){//展示源IP
			$("input[name$='srcIpAddress']").parents(".form-group").removeClass("hidden");
		}else{
			$("input[name$='srcIpAddress']").parents(".form-group").addClass("hidden");
		}
		if(ipPortShow.indexOf('2')>-1){//展示源端口
			$("input[name$='srcPort']").parents(".form-group").removeClass("hidden");
		}else{
			$("input[name$='srcPort']").parents(".form-group").addClass("hidden");
		}
		if(ipPortShow.indexOf('3')>-1){//展示目的IP
			$("input[name$='destIpAddress']").parents(".form-group").removeClass("hidden");
		}else{
			$("input[name$='destIpAddress']").parents(".form-group").addClass("hidden");
		}
		if(ipPortShow.indexOf('4')>-1){//展示目的端口
			$("input[name$='destPort']").parents(".form-group").removeClass("hidden");
		}else{
			$("input[name$='destPort']").parents(".form-group").addClass("hidden");
		}
	}
	//ip类型处理
	$.ipcommon.ipType=$("select[name$='ipType'] option");
	if(ipType){
		var arr=ipType.split(',');
		$("select[name$='ipType'] option").each(function(){
			var has=false;
			for(var type in arr){
				if($(this).val()==arr[type]){
					has=true;
					if($(this).attr("disabled")){
						$(this).removeAttr("disabled")
					}
					break;
				}
			}
			if(!has){
				$(this).remove();
			}
		});
	}
	//ip格式处理
	$.ipcommon.ipPattern=$("select[name$='ipPattern'] option");
	if(ipPattern){
		arr=ipPattern.split(',');
		$("select[name$='ipPattern'] option").each(function(){
			var has=false;
			for(var type in arr){
				if($(this).val()==arr[type]){
					has=true;
					if($(this).attr("disabled")){
						$(this).removeAttr("disabled")
					}
					break;
				}
			}
			if(!has){
				$(this).remove();
			}
		});
	}
	//端口格式处理
	$.ipcommon.portPattern=$("select[name$='portPattern'] option");
	if(portPattern){
		arr=portPattern.split(',');
		$("select[name$='portPattern'] option").each(function(){
			var has=false;
			for(var type in arr){
				if($(this).val()==arr[type]){
					has=true;
					if($(this).attr("disabled")){
						$(this).removeAttr("disabled")
					}
					break;
				}
			}
			if(!has){
				$(this).remove();
			}
		});
	}
	//方向处理
	$.ipcommon.direction=$("select[name$='direction'] option");
	if(direction){
		arr=direction.split(',');
		$("select[name$='direction'] option").each(function(){
			var has=false;
			for(var type in arr){
				if($(this).val()==arr[type]){
					has=true;
					if($(this).attr("disabled")){
						$(this).removeAttr("disabled")
					}
					break;
				}
			}
			if(!has){
				$(this).remove();
			}
		});
	}
	//协议处理
	$.ipcommon.protocol=$("select[name$='protocol'] option");
	if(protocol){
		arr=protocol.split(',');
		$("select[name$='protocol'] option").each(function(){
			var has=false;
			for(var type in arr){
				if($(this).val()==arr[type]){
					has=true;
					if($(this).attr("disabled")){
						$(this).removeAttr("disabled")
					}
					break;
				}
			}
			if(!has){
				$(this).remove();
			}
		});
	}
}
/**
 * ip切换默认值处理
 */
var changeIPVal=function(obj){
	var action,regionCode,serviceType,ipPortShow,ipType,
	ipPattern,portPattern,direction,protocol,regionType,o;
	o=$(obj);
	if($("input[name$='action']:checked").length>0){
		regionCode=$("input[name$='action']:checked").attr("regionCode");
		action=$("input[name$='action']:checked").val();
	}else{
		regionCode=$("input[name$='action']").attr("regionCode");
		action=$("input[name$='action']").val();
	}
	if(regionCode){
		if(regionCode.indexOf(",")==-1){
			$("#cfgRegionCode"+regionCode).attr("name","cfgRegionCode");	
			$("#cfgType"+regionCode).attr("name","cfgType");
		}else{
			var _region='';
			var regionArr=regionCode.split(',');
			$("input[name$='cfgRegionCode'][regionType='1']").each(function(){
				for(var re in regionArr){
					if($(this).val()==regionArr[re]){
						_region=regionArr[re];
						break;
					}
				}
			});
			if(_region!=''){
				regionCode=_region;
			}
		}
	}
	if(regionCode){//IP域大于1个，根据action获取ip属性
		ipPortShow=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPortShow"),
		ipType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipType"),
		ipPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("ipPattern"),
		portPattern=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("portPattern"),
		direction=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("direction"),
		protocol=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("protocol"),
		regionType=$("input[name$='cfgRegionCode'][value='"+regionCode+"']").attr("regionType");
	}else{//IP域只有一个，获取regionType为1的
		ipPortShow=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipPortShow"),
		ipType=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipType"),
		ipPattern=$("input[name$='cfgRegionCode'][regionType='1']").attr("ipPattern"),
		portPattern=$("input[name$='cfgRegionCode'][regionType='1']").attr("portPattern"),
		direction=$("input[name$='cfgRegionCode'][regionType='1']").attr("direction"),
		protocol=$("input[name$='cfgRegionCode'][regionType='1']").attr("protocol"),
		regionType=$("input[name$='cfgRegionCode'][regionType='1']").attr("regionType");
	}
	if(o){
		//ip，端口处理源ip展示、源端口展示、目的ip展示、目的端口展示1、2、3、4
		if(ipPortShow){
			if(ipPortShow.indexOf('1')>-1){//展示源IP
				$("input[name$='srcIpAddress']").parents(".form-group").removeClass("hidden");
			}else{
				$("input[name$='srcIpAddress']").parents(".form-group").addClass("hidden");
			}
			if(ipPortShow.indexOf('2')>-1){//展示源端口
				$("input[name$='srcPort']").parents(".form-group").removeClass("hidden");
			}else{
				$("input[name$='srcPort']").parents(".form-group").addClass("hidden");
			}
			if(ipPortShow.indexOf('3')>-1){//展示目的IP
				$("input[name$='destIpAddress']").parents(".form-group").removeClass("hidden");
			}else{
				$("input[name$='destIpAddress']").parents(".form-group").addClass("hidden");
			}
			if(ipPortShow.indexOf('4')>-1){//展示目的端口
				$("input[name$='destPort']").parents(".form-group").removeClass("hidden");
			}else{
				$("input[name$='destPort']").parents(".form-group").addClass("hidden");
			}
		}
		//ip类型处理
		if(ipType){
			var arr=ipType.split(',');
			var ipTypeVal=o.find("select[name$='ipType'] option:selected").attr("value");
			o.find("select[name$='ipType']").empty();
			$.ipcommon.ipType.each(function(){
				var has=false;
				for(var type in arr){
					if($(this).val()==arr[type]){
						has=true;
						o.find("select[name$='ipType']").append($(this));
						break;
					}
				}
			});
			o.find("select[name$='ipType']").selectpicker("refresh");
			if(o.find("select[name$='ipType'] option[value='"+ipTypeVal+"']").length>0){
				o.find("select[name$='ipType']").selectpicker("val",ipTypeVal);
			}else{
				if(o.find("select[name$='ipType'] option[value=4]").length>0){
					o.find("select[name$='ipType']").selectpicker("val",4);
				}else{
					o.find("select[name$='ipType']").get(0).selectedIndex=0;
					o.find("select[name$='ipType']").selectpicker("refresh");
				}
			}
			
		}
		//ip格式处理
		if(ipPattern){
			arr=ipPattern.split(',');
			var ipPatternVal=o.find("select[name$='ipPattern'] option:selected").attr("value");
			o.find("select[name$='ipPattern']").empty();
			$.ipcommon.ipPattern.each(function(){
				var has=false;
				for(var type in arr){
					if($(this).val()==arr[type]){
						has=true;
						o.find("select[name$='ipPattern']").append($(this));
						break;
					}
				}
			});
			o.find("select[name$='ipPattern']").selectpicker("refresh");
			if(o.find("select[name$='ipPattern'] option[value='"+ipPatternVal+"']").length>0){
				o.find("select[name$='ipPattern']").selectpicker("val",ipPatternVal);
			}else{
				if(o.find("select[name$='ipPattern'] option[value=1]").length>0){
					o.find("select[name$='ipPattern']").selectpicker("val",1);
				}else{
					o.find("select[name$='ipPattern']").get(0).selectedIndex=0;
					o.find("select[name$='ipPattern']").selectpicker("refresh");
				}
			}
		}
		//端口格式处理
		if(portPattern){
			arr=portPattern.split(',');
			var portPatternVal=o.find("select[name$='portPattern'] option:selected").attr("value");
			o.find("select[name$='portPattern']").empty();
			$.ipcommon.portPattern.each(function(){
				var has=false;
				for(var type in arr){
					if($(this).val()==arr[type]){
						has=true;
						o.find("select[name$='portPattern']").append($(this));
						break;
					}
				}
			});
			o.find("select[name$='portPattern']").selectpicker("refresh");
			if(o.find("select[name$='portPattern'] option[value='"+portPatternVal+"']").length>0){
				o.find("select[name$='portPattern']").selectpicker("val",portPatternVal);
			}else{
				if(o.find("select[name$='portPattern'] option[value=1]").length>0){
					o.find("select[name$='portPattern']").selectpicker("val",1);
				}else{
					o.find("select[name$='portPattern']").get(0).selectedIndex=0;
					o.find("select[name$='portPattern']").selectpicker("refresh");
				}
			}
		}
		//方向处理
		if(direction){
			arr=direction.split(',');
			var directionVal=o.find("select[name$='direction'] option:selected").attr("value");
			o.find("select[name$='direction']").empty();
			$.ipcommon.direction.each(function(){
				var has=false;
				for(var type in arr){
					if($(this).val()==arr[type]){
						has=true;
						o.find("select[name$='direction']").append($(this));
						break;
					}
				}
			});
			o.find("select[name$='direction']").selectpicker("refresh");
			if(o.find("select[name$='direction'] option[value='"+directionVal+"']").length>0){
				o.find("select[name$='direction']").selectpicker("val",directionVal);
			}else{
				if(o.find("select[name$='direction'] option[value=0]").length>0){
					o.find("select[name$='direction']").selectpicker("val",0);
				}else{
					o.find("select[name$='direction']").get(0).selectedIndex=0;
					o.find("select[name$='direction']").selectpicker("refresh");
				}
			}
		}
		//协议处理
		if(protocol){
			arr=protocol.split(',');
			var protocolVal=o.find("select[name$='protocol'] option:selected").attr("value");
			o.find("select[name$='protocol']").empty();
			$.ipcommon.protocol.each(function(){
				var has=false;
				for(var type in arr){
					if($(this).val()==arr[type]){
						has=true;
						o.find("select[name$='protocol']").append($(this));
						break;
					}
				}
			});
			o.find("select[name$='protocol']").selectpicker("refresh");
			if(o.find("select[name$='protocol'] option[value='"+protocolVal+"']").length>0){
				o.find("select[name$='protocol']").selectpicker("val",protocolVal);
			}else{
				if(o.find("select[name$='protocol'] option[value=0]").length>0){
					o.find("select[name$='protocol']").selectpicker("val",0);
				}else{
					o.find("select[name$='protocol']").get(0).selectedIndex=0;
					o.find("select[name$='protocol']").selectpicker("refresh");
				}
			}
		}
	}
}