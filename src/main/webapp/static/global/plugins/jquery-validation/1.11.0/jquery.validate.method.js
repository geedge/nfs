/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
(function ($) {
	$.extend($.validator.messages, {
		required: "必填信息",
		remote: "请修正该信息",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
		minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
		rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: $.validator.format("请输入一个最大为 {0} 的值"),
		min: $.validator.format("请输入一个最小为 {0} 的值")
	});
}(jQuery));

jQuery.validator.addMethod("hexCheck", function(value, element) {
	return this.optional(element) || /^([0-9|a-f|A-F]*)$/.test(value);   
}, $.validator.messages.hexCheck);
jQuery.validator.addMethod("ip", function(value, element) {
	return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
}, "请输入合法的IP地址");

//IPv4  IPv6 
jQuery.validator.addMethod("ipv4v6", function(value, element) {
	return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256))||/^\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}(:|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:)|((:[0-9A-Fa-f]{1,4}){1,2}))))(%.+)?\s*$/.test(value);   
}, "请输入合法的IP地址如IPv4 0.0.0.0或IPv6 ::");

jQuery.validator.addMethod("abc",function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9_]*$/.test(value);
},"请输入字母数字或下划线");

jQuery.validator.addMethod("username",function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9][a-zA-Z0-9_]{2,19}$/.test(value);
},"3-20位字母或数字开头，允许字母数字下划线");

jQuery.validator.addMethod("noEqualTo",function(value, element, param) {
	return value != $(param).val();
},"请再次输入不同的值");

//真实姓名验证
jQuery.validator.addMethod("realName", function(value, element) {
    return this.optional(element) || /^[\u4e00-\u9fa5]{2,30}$/.test(value);
}, "姓名只能为2-30个汉字");

// 字符验证
jQuery.validator.addMethod("userName", function(value, element) {
    return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "登录名只能包括中文字、英文字母、数字和下划线");

// 手机号码验证
jQuery.validator.addMethod("mobile", function(value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/.test(value));
}, "请正确填写您的手机号码");

// 电话号码验证
jQuery.validator.addMethod("simplePhone", function(value, element) {
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的电话号码");

// 电话号码验证     
jQuery.validator.addMethod("phone", function(value, element) {     
	var tel = /(^0[1-9]{1}\d{9,10}$)|(^1[3,5,8]\d{9}$)/g;     
	return this.optional(element) || (tel.test(value));     
}, "格式为:固话为区号(3-4位)号码(7-9位),手机为:13,15,18号段");	

// 邮政编码验证
jQuery.validator.addMethod("zipCode", function(value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

//QQ号码验证
jQuery.validator.addMethod("qq", function(value, element) {
    var tel = /^[1-9][0-9]{4,}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的QQ号码");
 
//校验身份证好
jQuery.validator.addMethod("card",function(value, element) {
	return this.optional(element) || checkIdcard(value);
},"请输入正确的身份证号码(15-18位)")

//验证身份证函数
function checkIdcard(idcard){
	idcard = idcard.toString();
	//var Errors=new Array("验证通过!","身份证号码位数不对!","身份证号码出生日期超出范围或含有非法字符!","身份证号码校验错误!","身份证地区非法!");
	var Errors=new Array(true,false,false,false,false);
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	var idcard,Y,JYM;
	var S,M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	//地区检验
	if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
	//身份号码位数及格式检验
	switch(idcard.length){
		case 15:
			if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
			} else {
				ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
			}
			if(ereg.test(idcard)) return Errors[0];
			else return Errors[2];
			break;
		case 18:
			//18 位身份号码检测
			//出生日期的合法性检查
			//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
				ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
			} else {
				ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
			}
			if(ereg.test(idcard)) {//测试出生日期的合法性
				//计算校验位
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
					+ parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6
					+ parseInt(idcard_array[9]) * 3 ;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y,1);//判断校验位
				if(M == idcard_array[17]) return Errors[0]; //检测ID的校验位
				else return Errors[3];
			}
			else return Errors[2];
			break;
		default:
			return Errors[1];
			break;
	}
} 


jQuery.validator.addMethod("regexPassword", function(value, element) {  
    return this.optional(element) || /^(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/.test(value);  
}, "一个大写，一个小写，一个符号");

//输入内容不得为空格
jQuery.validator.addMethod("noBlankSpace", function(value, element) {  
	return $.trim(value) != "";
}, "不能填写空格");

jQuery.validator.addMethod("compareDate", function(value, element, param) {  
	var startDate = $(param).val();
	if (startDate && value) {
		var date1 = new Date(startDate.replace(new RegExp(/-/gm), "/"));
		var date2 = new Date(value.replace(new RegExp(/-/gm), "/"));
		return date1 <= date2;
	}
	return true;
}, "结束时间不能早于开始时间");

jQuery.validator.addMethod("invisibleChar", function(value, element) {  
	var reg = new RegExp(/\t|\r|\n/);
	if (value.match(reg)) {
		return false;
	}
	return true;
}, "请输入可见字符");

//掩码校验，超过两位不得以0开头
jQuery.validator.addMethod("notStartZero",function(value, element) {
		if(value.length>1&&value.length<6){
			var str = value.substr(0,1);
			if(str == '0'){
				return false;
				}
			}
		return true;
		}, "请填写正确的数值");
//域名验证正则
jQuery.validator.addMethod("domainCheck",function(value, element) {
	var regexp=/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
	if(this.optional(element)||regexp.test(value)){
		return true;
	}
	return false;
});

//http、https 格式校验
jQuery.validator.addMethod("httpCheck",function(value, element) {
	var regexp=/^[hH][tT][tT][pP]([sS]?):\/\/(\S+\.)+\S{2,}$/;
	if(this.optional(element)||regexp.test(value)){
		return true;
	}
	return false;
});
/*协议端口校验
 * 规则TCP,UDP协议端口可以不为0
 * 其他协议端口必须为0
 * 端口号不为0时必须选择协议
 * */
jQuery.validator.addMethod("protocolPort",function(value, element) {
	var tcp=6,udp=17,protocolVal,srcPortVal,destPortVal;
	var name=$(element).attr('name');
	if(name.indexOf("protocol")>-1){
		protocolVal=value;
	}
	if(name.indexOf("srcPort")>-1){
		srcPortVal=value;
	}
	if(name.indexOf("destPort")>-1){
		destPortVal=value;
	}
	if(protocolVal){
		var srcPortElement=$("[name='"+name.replace("protocol","srcPort")+"']");
		srcPortVal=srcPortElement.val();
		var destPortElement=$("[name='"+name.replace("protocol","destPort")+"']");
		destPortVal=destPortElement.val();
	}else if(srcPortVal){
		var protocolElement=$("[name='"+name.replace("srcPort","protocol")+"']");
		protocolVal=protocolElement.val();
	}else if(destPortVal){
		var protocolElement=$("[name='"+name.replace("destPort","protocol")+"']");
		protocolVal=protocolElement.val();
	}
	//端口/掩码格式
	if(srcPortVal&&srcPortVal.indexOf("/")>-1){
		srcPortVal=srcPortVal.split("/")[0];
	}
	if(destPortVal&&destPortVal.indexOf("/")>-1){
		destPortVal=destPortVal.split("/")[0];
	}
	if(protocolVal==0){
		return true;
	}else if((protocolVal!=tcp&&protocolVal!=udp&&srcPortVal>0)||(protocolVal!=tcp&&protocolVal!=udp&&destPortVal>0)){
		$.validator.messages.protocolPort=$.validator.messages.protocolPort1;
		return false;
	}else{
		return true;
	}
});

//ip地址校验
jQuery.validator.addMethod("ipCheck",function(value, element) {
	var ipv4_ip_subnet_regexp=/^(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\/(3[0-2]|1[6-9]|2[0-9])$/;
	var ipv6_ip_subnet_regexp=/^((::)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3}))\/([2-9]|[1-9][0-9]|1[0-2][0-8])$/;
	var ipv4_ip_range_regexp=/^(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)-(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)$/;
	var ipv6_ip_range_regexp= /^((::)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3}))\-((::)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3}))$/;
	var ipv4_ip_regexp=/^(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(0|1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)$/;
	var ipv6_ip_regexp=/^((::)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])(\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])){3}))$/;
	var name=$(element).attr("name");
	if(value.length==0||value.trim().length==0){return true;}
	var ipTypeE,ipPatternE,srcIpAddressE,destIpAddressE;
	if(name.indexOf("srcIpAddress")>-1){
		srcIpAddressE=$(element);
		ipTypeE=$("select[name='"+name.replace("srcIpAddress","ipType")+"']");
		ipPatternE=$("select[name='"+name.replace("srcIpAddress","ipPattern")+"']");
		destIpAddressE=$("input[name='"+name.replace("srcIpAddress","destIpAddress")+"']");
	}else if(name.indexOf("destIpAddress")>-1){
		destIpAddressE=$(element);
		ipTypeE=$("select[name='"+name.replace("destIpAddress","ipType")+"']");
		ipPatternE=$("select[name='"+name.replace("destIpAddress","ipPattern")+"']");
		srcIpAddressE=$("input[name='"+name.replace("destIpAddress","srcIpAddress")+"']");
	}
	var typeInt=ipTypeE.val();
	var ipPattern=ipPatternE.val();
	var srcIpAddress=srcIpAddressE.val();
	var destIpAddress=destIpAddressE.val();
	var srcIpIsHidden=srcIpAddressE.is(":hidden");
	var destIpIsHidden=destIpAddressE.is(":hidden");
	var IPv4RangeDefault="0.0.0.0-0.0.0.0";
	if(ipPattern==1){//ip/掩码格式
		srcIpAddress=srcIpAddress.indexOf("/") >-1 ?srcIpAddress.split("/")[0] : srcIpAddress;
		destIpAddress=destIpAddress.indexOf("/") >-1 ?destIpAddress.split("/")[0] : destIpAddress;
	}else if (ipPattern==2){
		srcIpAddress=srcIpAddress.indexOf("-") >-1 ?srcIpAddress.split("-")[0] : srcIpAddress;
		destIpAddress=destIpAddress.indexOf("-") >-1 ?destIpAddress.split("-")[0] : destIpAddress;
	}
	if(srcIpIsHidden){
		if((srcIpAddress == destIpAddress)){
			$.validator.messages.ipCheck=$.validator.messages.destIpIsNotValue+destIpAddress;
			return false;
		}
	}else if(destIpIsHidden){
		if((srcIpAddress == destIpAddress)){
			$.validator.messages.ipCheck=$.validator.messages.srcIpIsNotValue+srcIpAddress;
			return false;
		}
	}else{
		if((srcIpAddress == destIpAddress)){
			$.validator.messages.ipCheck=$.validator.messages.srcIpNotEqDestIp+srcIpAddress
											+$.validator.messages.destIpNotEqDestIp+destIpAddress
													+$.validator.messages.IpNotEqDestIp;
			return false;
		}
	}
	
	if(typeInt==4){
		if(ipPattern){
			if(ipPattern==1){//ip/掩码格式
				if(this.optional(element)||(/^(\d+)\.(\d+)\.(\d+)\.(\d+)\/(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256 && RegExp.$5<=32))){
					if(ipv4_ip_subnet_regexp.test(value)){
						return true;
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
						return false;
					}
				}else if(RegExp.$5&&RegExp.$5>32){
					$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
					return false;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}
			}else if(ipPattern==2){//ip range
				//A类 0.0.0.0-127.255.255.255  默认掩码255.0.0.0
				//B类128.0.0.0-191.255.255.255 默认掩码255.255.0.0
				//C类192.0.0.0-223.255.255.255 默认掩码255.255.255.0
				if(ipv4_ip_range_regexp.test(value)){
					var startIp=value.split("-")[0];
					var endIp=value.split("-")[1];
					var startPart0=startIp.split(".")[0];
					var startPart1=endIp.split(".")[0];
					if(value==IPv4RangeDefault){
						return true;
					}
					if((startPart0<128&&startPart1<128)||(startPart0>127&&startPart1>127&&startPart0<192&&startPart1<192)
							||(startPart0>191&&startPart1>191&&startPart0<224&&startPart1<224)
							||(startPart0>223&&startPart1>223&&startPart0<240&&startPart1<240)
							||(startPart0>239&&startPart1>239&&startPart0<256&&startPart1<256)){//仅允许同段IP
						if(ipToNumber(startIp)<=ipToNumber(endIp)){//比较IP大小
							if((startIp.split(".")[0]!=endIp.split(".")[0])||
									(startIp.split(".")[1]!=endIp.split(".")[1])||
									(startIp.split(".")[2]!=endIp.split(".")[2])){
								$.validator.messages.ipCheck=$.validator.messages.netAddress;	
							}else{
								return true;
							}
						}else{
							$.validator.messages.ipCheck=$.validator.messages.ipRange1;
							return false;
						}
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipRange;
						return false;
					}
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipRange;
					return false;
				}
			}else if(ipPattern==3){//ip格式
				if(this.optional(element)||ipv4_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}
		}else{//兼容原来的验证
			if(this.optional(element)||ipv4_ip_regexp.test(value)){
				return true;
			}else{
				$.validator.messages.ipCheck=$.validator.messages.ip;
				return false;
			}
		}
	}else if(typeInt==6){
		if(ipPattern==1){//ip/掩码格式
			if(this.optional(element)||ipv6_ip_subnet_regexp.test(value)){
				return true;
			}else if(value.indexOf("/")==-1){
				$.validator.messages.ipCheck=$.validator.messages.ipMask;
				return false;
			}else{
				var ipMaskArr=value.split("/");
				if(ipMaskArr.length!=2){
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}else{
					if(!ipv6_ip_regexp.test(ipMaskArr[0])){
						$.validator.messages.ipCheck=$.validator.messages.ipPart;
						return false;
					//}else if(!/^(0|2|4|8|16|32|64|128)$/.test(ipMaskArr[1])){
					}else if(!/^([2-9]|[1-9][0-9]|1[0-2][0-8])$/.test(ipMaskArr[1])){
						$.validator.messages.ipCheck=$.validator.messages.ipv6MaskPart;
						return false;
					} else{
						$.validator.messages.ipCheck=$.validator.messages.ipMask;
						return false;
					}
				}
			}
		}else if(ipPattern==2){//ip range
			$.validator.messages.ipCheck=$.validator.messages.ipRange;
			if(ipv6_ip_range_regexp.test(value)){
				var ipArr=value.split("-");
				var startIp=fillIp(ipArr[0]);
				var endIp=fillIp(ipArr[1]);
				//默认比较前64位
				var exp1=/^[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*/;
				var startPre=String(startIp.match(exp1));
				var endPre=String(endIp.match(exp1));
				return startPre==endPre;
			}
			return false;
		}else if(ipPattern==3){//ip格式
			if(this.optional(element)||ipv6_ip_regexp.test(value)){
				return true;
			}else{
				$.validator.messages.ipCheck=$.validator.messages.ip;
				return false;
			}
		}else{//兼容原来的验证
			if(this.optional(element)||ipv6_ip_regexp.test(value)){
				return true;
			}else{
				$.validator.messages.ipCheck=$.validator.messages.ip;
				return false;
			}
		}
	}else if(typeInt==46){
		var name=$(element).attr("name");
		if(name.indexOf("srcIpAddress")>-1){
			if(ipPattern==1){//ip/掩码格式
				if(this.optional(element)||(/^(\d+)\.(\d+)\.(\d+)\.(\d+)\/(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256 && RegExp.$5<=32))){
					if(ipv4_ip_subnet_regexp.test(value)){
						return true;
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
						return false;
					}
				}else if(RegExp.$5&&RegExp.$5>32){
					$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
					return false;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}
			}else if(ipPattern==2){//ip range
				//A类 0.0.0.0-127.255.255.255  默认掩码255.0.0.0
				//B类128.0.0.0-191.255.255.255 默认掩码255.255.0.0
				//C类192.0.0.0-223.255.255.255 默认掩码255.255.255.0
				if(ipv4_ip_range_regexp.test(value)){
					var startIp=value.split("-")[0];
					var endIp=value.split("-")[1];
					var startPart0=startIp.split(".")[0];
					var startPart1=endIp.split(".")[0];
					if(value==IPv4RangeDefault){
						return true;
					}
					if((startPart0<128&&startPart1<128)||(startPart0>127&&startPart1>127&&startPart0<192&&startPart1<192)
							||(startPart0>191&&startPart1>191&&startPart0<224&&startPart1<224)
							||(startPart0>223&&startPart1>223&&startPart0<240&&startPart1<240)
							||(startPart0>239&&startPart1>239&&startPart0<256&&startPart1<256)){//仅允许同段IP
						if(ipToNumber(startIp)<=ipToNumber(endIp)){//比较IP大小
							if((startIp.split(".")[0]!=endIp.split(".")[0])||
									(startIp.split(".")[1]!=endIp.split(".")[1])||
									(startIp.split(".")[2]!=endIp.split(".")[2])){
								$.validator.messages.ipCheck=$.validator.messages.netAddress;	
							}else{
								return true;
							}
						}else{
							$.validator.messages.ipCheck=$.validator.messages.ipRange1;
							return false;
						}
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipRange;
						return false;
					}
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipRange;
					return false;
				}
			}else if(ipPattern==3){//ip
				if(this.optional(element)||ipv4_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}
		}else if(name.indexOf("destIpAddress")>-1){
			if(ipPattern==1){//ip/掩码格式
				if(this.optional(element)||ipv6_ip_subnet_regexp.test(value)){
					return true;
				}else if(value.indexOf("/")==-1){
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}else{
					var ipMaskArr=value.split("/");
					if(ipMaskArr.length!=2){
						$.validator.messages.ipCheck=$.validator.messages.ipMask;
						return false;
					}else{
						if(!ipv6_ip_regexp.test(ipMaskArr[0])){
							$.validator.messages.ipCheck=$.validator.messages.ipPart;
							return false;
						//}else if(!/^(0|2|4|8|16|32|64|128)$/.test(ipMaskArr[1])){
						}else if(!/^([2-9]|[1-9][0-9]|1[0-2][0-8])$/.test(ipMaskArr[1])){
							$.validator.messages.ipCheck=$.validator.messages.ipv6MaskPart;
							return false;
						} else{
							$.validator.messages.ipCheck=$.validator.messages.ipMask;
							return false;
						}
					}
				}
			}else if(ipPattern==2){//ip range
				$.validator.messages.ipCheck=$.validator.messages.ipRange;
				if(ipv6_ip_range_regexp.test(value)){
					var ipArr=value.split("-");
					var startIp=fillIp(ipArr[0]);
					var endIp=fillIp(ipArr[1]);
					//默认比较前64位
					var exp1=/^[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*/;
					var startPre=String(startIp.match(exp1));
					var endPre=String(endIp.match(exp1));
					return startPre==endPre;
				}
				return false;
			}else if(ipPattern==3){//ip格式
				if(this.optional(element)||ipv6_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}else{//兼容原来的验证
				if(this.optional(element)||ipv6_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}
		}
	}else if(typeInt==64){
		var name=$(element).attr("name");
		if(name.indexOf("srcIpAddress")>-1){
			if(ipPattern==1){//ip/掩码格式
				if(this.optional(element)||ipv6_ip_subnet_regexp.test(value)){
					return true;
				}else if(value.indexOf("/")==-1){
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}else{
					var ipMaskArr=value.split("/");
					if(ipMaskArr.length!=2){
						$.validator.messages.ipCheck=$.validator.messages.ipMask;
						return false;
					}else{
						if(!ipv6_ip_regexp.test(ipMaskArr[0])){
							$.validator.messages.ipCheck=$.validator.messages.ipPart;
							return false;
						//}else if(!/^(0|2|4|8|16|32|64|128)$/.test(ipMaskArr[1])){
						}else if(!/^([2-9]|[1-9][0-9]|1[0-2][0-8])$/.test(ipMaskArr[1])){
							$.validator.messages.ipCheck=$.validator.messages.ipv6MaskPart;
							return false;
						} else{
							$.validator.messages.ipCheck=$.validator.messages.ipMask;
							return false;
						}
					}
				}
			}else if(ipPattern==2){//ip range
				$.validator.messages.ipCheck=$.validator.messages.ipRange;
				if(ipv6_ip_range_regexp.test(value)){
					var ipArr=value.split("-");
					var startIp=fillIp(ipArr[0]);
					var endIp=fillIp(ipArr[1]);
					//默认比较前64位
					var exp1=/^[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*/;
					var startPre=String(startIp.match(exp1));
					var endPre=String(endIp.match(exp1));
					return startPre==endPre;
				}
				return false;
			}else if(ipPattern==3){//ip格式
				if(this.optional(element)||ipv6_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}else{//兼容原来的验证
				if(this.optional(element)||ipv6_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}
		}else if(name.indexOf("destIpAddress")>-1){
			if(ipPattern==1){//ip/掩码格式
				if(this.optional(element)||(/^(\d+)\.(\d+)\.(\d+)\.(\d+)\/(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256 && RegExp.$5<=32))){
					if(ipv4_ip_subnet_regexp.test(value)){
						return true;
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
						return false;
					}
				}else if(RegExp.$5&&RegExp.$5>32){
					$.validator.messages.ipCheck=$.validator.messages.ipMaskRange;
					return false;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}
			}else if(ipPattern==2){//ip range
				//A类 0.0.0.0-127.255.255.255  默认掩码255.0.0.0
				//B类128.0.0.0-191.255.255.255 默认掩码255.255.0.0
				//C类192.0.0.0-223.255.255.255 默认掩码255.255.255.0
				if(ipv4_ip_range_regexp.test(value)){
					var startIp=value.split("-")[0];
					var endIp=value.split("-")[1];
					var startPart0=startIp.split(".")[0];
					var startPart1=endIp.split(".")[0];
					if(value==IPv4RangeDefault){
						return true;
					}
					if((startPart0<128&&startPart1<128)||(startPart0>127&&startPart1>127&&startPart0<192&&startPart1<192)
							||(startPart0>191&&startPart1>191&&startPart0<224&&startPart1<224)
							||(startPart0>223&&startPart1>223&&startPart0<240&&startPart1<240)
							||(startPart0>239&&startPart1>239&&startPart0<256&&startPart1<256)){//仅允许同段IP
						if(ipToNumber(startIp)<=ipToNumber(endIp)){//比较IP大小
							if((startIp.split(".")[0]!=endIp.split(".")[0])||
									(startIp.split(".")[1]!=endIp.split(".")[1])||
									(startIp.split(".")[2]!=endIp.split(".")[2])){
								$.validator.messages.ipCheck=$.validator.messages.netAddress;	
							}else{
								return true;
							}
						}else{
							$.validator.messages.ipCheck=$.validator.messages.ipRange1;
							return false;
						}
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipRange;
						return false;
					}
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipRange;
					return false;
				}
			}else if(ipPattern==3){//ip
				if(this.optional(element)||ipv4_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;					
					return false;
				}
			}
		}
	}else if(typeInt==10){			
		if(ipPattern){
			if(ipPattern==1){//ip/掩码格式
				if(ipv4_ip_subnet_regexp.test(value)){
					return true;
				}else if(ipv6_ip_subnet_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ipMask;
					return false;
				}
			}else if(ipPattern==2){//ip range
				//A类 0.0.0.0-127.255.255.255  默认掩码255.0.0.0
				//B类128.0.0.0-191.255.255.255 默认掩码255.255.0.0
				//C类192.0.0.0-223.255.255.255 默认掩码255.255.255.0
				if(ipv4_ip_range_regexp.test(value)){
					var startIp=value.split("-")[0];
					var endIp=value.split("-")[1];
					var startPart0=startIp.split(".")[0];
					var startPart1=endIp.split(".")[0];
					if(value==IPv4RangeDefault){
						return true;
					}
					if((startPart0<128&&startPart1<128)||(startPart0>127&&startPart1>127&&startPart0<192&&startPart1<192)
							||(startPart0>191&&startPart1>191&&startPart0<224&&startPart1<224)
							||(startPart0>223&&startPart1>223&&startPart0<240&&startPart1<240)
							||(startPart0>239&&startPart1>239&&startPart0<256&&startPart1<256)){//仅允许同段IP
						if(ipToNumber(startIp)<=ipToNumber(endIp)){//比较IP大小
							if((startIp.split(".")[0]!=endIp.split(".")[0])||
									(startIp.split(".")[1]!=endIp.split(".")[1])||
									(startIp.split(".")[2]!=endIp.split(".")[2])){
								$.validator.messages.ipCheck=$.validator.messages.netAddress;	
							}else{
								return true;
							}
						}else{
							$.validator.messages.ipCheck=$.validator.messages.ipRange1;
							return false;
						}
					}else{
						$.validator.messages.ipCheck=$.validator.messages.ipRange;
						return false;
					}
				}else if(ipv6_ip_subnet_regexp.test(value)){
					var ipArr=value.split("-");
					var startIp=fillIp(ipArr[0]);
					var endIp=fillIp(ipArr[1]);
					//默认比较前64位
					var exp1=/^[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*:[0-9A-Fa-f]*/;
					var startPre=String(startIp.match(exp1));
					var endPre=String(endIp.match(exp1));
					if(startPre!=endPre){
						$.validator.messages.ipCheck=$.validator.messages.ipRange;
						return false;
					}else{
						return true;
					}
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
				
			}else if(ipPattern==3){//ip格式
				if(ipv4_ip_regexp.test(value)||ipv6_ip_regexp.test(value)){
					return true;
				}else{
					$.validator.messages.ipCheck=$.validator.messages.ip;
					return false;
				}
			}
		}else{//兼容原来的验证
			if(this.optional(element)||(/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256))){
				return true;
			}else{
				$.validator.messages.ipCheck=$.validator.messages.ip;
				return false;
			}
		}
	}else{
		$.validator.messages.ipCheck=$.validator.messages.ip;
		return false;
	}
}, $.validator.messages.ip);
//ip地址掩码校验
jQuery.validator.addMethod("ipMask",function(value, element) {
	if(value.length==0||value.trim().length==0){return true;}
	obj=value; 
	var typeInt=$(element).parents(".row").siblings().find("select[name$='ipType']").val();
	if(!typeInt){
		typeInt=$(element).parents(".row").find("select[name$='ipType']").val();
	}
	if(typeInt==4){
		if(obj=="255.255.255.255"){
			return true;
		}else{
			var exp=/^(254|252|248|240|224|192|128|0)\.0\.0\.0|255\.(254|252|248|240|224|192|128|0)\.0\.0|255\.255\.(254|252|248|240|224|192|128|0)\.0|255\.255\.255\.(254|252|248|240|224|192|128|0)$/; 
			var reg = obj.match(exp); 
			if(reg==null){return false;}else{return true} 
		}
	}else if(typeInt==6){
		if(obj=="FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"||obj==0||obj=="::"){
			return true;
		}else{return (obj > 0) && ((obj & (obj - 1)) == 0)};
	}
		}, $.validator.messages.ipMask);
jQuery.validator.addMethod("portCheck",function(value, element) {
		if(value.length==0||value.trim().length==0){return true;}
		var typeInt=$(element).parents(".row").parent(".row").find("select[name$='portPattern']").val();
		if(typeInt==1){//port 0~65535
			if(this.optional(element)||(/^(0|[1-9][0-9]{0,4})$/.test(value)&& RegExp.$1 <=65535)){
				return true;
			}else{
				$.validator.messages.portCheck=$.validator.messages.portRange
				return false;
			}
		}else if(typeInt==2){// port -mask 0~65535/0~65535
			if(this.optional(element)||(/^(0|[1-9][0-9]{0,4})\/(0|[1-9][0-9]{0,4})$/.test(value) && RegExp.$1 <=65535 && RegExp.$2 <=65535)){
				return true;
			}else{
				$.validator.messages.portCheck=$.validator.messages.portMaskRange
				return false;
			}
		}
	});
jQuery.validator.addMethod("chooseAreaOrIsp",function(value, element) {
	var isp="";
	if($(element).is(":visible")){
		isp=$(element).closest(".row").find("select[name$='.isp']").val()
	}
	if(value =='' && isp==""){
		$.validator.messages.chooseAreaOrIsp=$.validator.messages.chooseAreaOrIsp
		return false;
	}
	return true;
	
});
jQuery.validator.addMethod("areaControl",function(value, element) {
	var action=$(element).closest(".row").siblings(".row").find("input[name$=action]:radio:checked").val();
	if((action==0x20||action==0x60)&&value==1){
		$.validator.messages.areaControl=$.validator.messages.areaControl
		return false;
	}else{
		return true;
	}
});
jQuery.validator.addMethod("areaIp",function(value, element) {
	var typeInt=$(element).parents(".row").parent(".row").find("select[name$='ipType']").val();
	var ipPattern=$(element).parents(".row").parent(".row").find("select[name$='ipPattern']").val();
	var msg=$.validator.messages.areaIpPrefix;
	if(typeInt==4){
		if(ipPattern==3){
			if(value=='0.0.0.0'){
				$.validator.messages.areaIp=msg+'0.0.0.0';
				return false;
			}else{
				return true;
			}
		}else if(ipPattern==2){
			if(value=='0.0.0.0-0.0.0.0'){
				$.validator.messages.areaIp=msg+'0.0.0.0-0.0.0.0';
				return false;
			}else{
				return true;
			}
		}else if(ipPattern==1){
			if(value=='0.0.0.0/8'){
				$.validator.messages.areaIp=msg+'0.0.0.0/8';
				return false;
			}else{
				return true;
			}
		}
	}else if(typeInt==6){
		if(ipPattern==3){
			if(value=='::'){
				$.validator.messages.areaIp=msg+'::';
				return false;
			}else{
				return true;
			}
		}else if(ipPattern==2){
			if(value=='::-::'){
				$.validator.messages.areaIp=msg+'::-::';
				return false;
			}else{
				return true;
			}
		}else if(ipPattern==1){
			if(value=='::/64'){
				$.validator.messages.areaIp=msg+'::/64';
				return false;
			}else{
				return true;
			}
		}
	}
});
jQuery.validator.addMethod("checkParent",function(value, element) {
	var parentType=$(element).attr("parent-type");
	if(parentType&&parentType!=0&&parentType!=value){
		return false;
	}
	return true;
});
jQuery.validator.addMethod("specServiceCodeCheck",function(value, element) {
	var ctx=$(element).attr("ctx");
	var parentId= $("[name='parent.specServiceId']").val();
	var specServiceCode= $("#specServiceCode").val();
	var cfgType= $("[name='cfgType']").val();
	var specServiceId= $("[name='specServiceId']").val();
	var url=ctx+"/specific/specificServiceCfg/isCodeNotRepeat";
	var val=true;
	$.ajax({
		 url:url,
		 type:"post",
		 async:false,
		 data:{
			 parentId:parentId,
			 specServiceCode:specServiceCode,
			 cfgType:cfgType,
			 specServiceId:specServiceId
		 },
		 success:function(data){
			 val=data;
		 }
	});
	return val;
});
//ip v4转数字
var ipToNumber=function (ip){
	var num =0;
	if(ip==""){
		return num;
	}
	var aNum=ip.split(".");
	if(aNum.length !=4){
		return num;
	}
	num += parseInt(aNum[0]) << 24;
	num += parseInt(aNum[1]) << 16;
	num += parseInt(aNum[2]) << 8;
	num += parseInt(aNum[3]) << 0;
	num = num >>> 0;
	return num;
}
//ipv6 补全
var fillIp=function (ip){
	//var filledIp="";
	var exp=/:/ig;
	while(ip.match(exp).length<7){
		if(ip.indexOf("::")>-1){
			ip=ip.replace("::",":::");
		}else{
			ip=ip.replace(":","::");
		}
	}
	return ip;
}
// 地址池校验
jQuery.validator.addMethod("addrPoolUnique",function(value, element) {
	var ctx=$(element).attr("ctx");
	var cfgId= $("[name='cfgId']").val();
	var functionId= $("[name='functionId']").val();
	var url = ctx+"/maintenance/ipMultiplexPoolCfg/checkAddrPool";
	var result = true;
	$.ajax({
		type:'post',
		async:false,
		url: url,
		data:{"cfgId":cfgId,"functionId":functionId,"addrPoolName":value},
		success:function(data){
			result = data;
		}
	});
	return result;
});
// 策略分组管理asn号唯一
jQuery.validator.addMethod("asnNoUnique",function(value, element) {
	var ctx=$(element).attr("ctx");
	var groupId= $("[name='groupId']").val();
	var url = ctx+"/basics/policyGroup/checkAsnNo";
	var result = true;
	$.ajax({
		type:'post',
		async:false,
		url: url,
		data:{"groupId":groupId,"asnNo":value},
		success:function(data){
			result = data;
		}
	});
	return result;
});

jQuery.validator.addMethod("areaUnique",function(value,element) {
	var flag = true;
	var array = new Array();
	var index = $(element).attr("index");
	$("select[areaIsp='area']").each(function(){
		var code = $(this).val();
		if($(this).attr("index") < index){
			array.push(code);
		}
	});
	if($.inArray(value,array) != -1){
		flag = false;
	}
	return flag;
});