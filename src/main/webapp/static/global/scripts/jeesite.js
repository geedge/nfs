/*!
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * 
 * 通用公共方法
 * @author ThinkGem
 * @version 2014-4-29
 */
$(document).ready(function() {

	try{
		// 链接去掉虚框
		$("a").bind("focus",function() {
			if(this.blur) {this.blur()};
		});
		
		//界面中存在<a class="setfields">自定义列按钮，初始化自定义列功能 
		if($("a").hasClass("setfields")) {
			customColumnInit();
			
			//绑定自定义按钮事件
			$(".setfields").click(function(){
				customColumnClick();
			});
		}

		
	}catch(e){
		// blank
	}
});


 function getContextPath() {
    var pathName = window.location.pathname;
    var index = pathName.substring(1).indexOf("/");
    var result =  pathName.substring(0,index+1);
    return result;
}
 
 function getProjectPath() {
    var pathName = window.location.pathname;
    var pathNames = pathName.substring(1).split("/");
    var result = "/"+pathNames[0]+"/"+pathNames[1];
    return result;
}


// 引入js和css文件
function include(id, path, file){
	if (document.getElementById(id)==null){
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++){
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
            var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
            document.write("<" + tag + (i==0?" id="+id:"") + attr + link + "></" + tag + ">");
        }
	}
}

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
	    url = window.location.search;
    }else{	
    	url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}

//获取字典标签
function getDictLabel(data, value, defaultValue){
	for (var i=0; i<data.length; i++){
		var row = data[i];
		if (row.itemCode == value){
			return row.itemValue;
		}
	}
	return defaultValue;
}

// 打开一个窗体
function windowOpen(url, name, width, height){
	var top=parseInt((window.screen.height-height)/2,10),left=parseInt((window.screen.width-width)/2,10),
		options="location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes,"+
		"resizable=yes,scrollbars=yes,"+"width="+width+",height="+height+",top="+top+",left="+left;
	window.open(url ,name , options);
}

// 恢复提示框显示
function resetTip(){
	top.$.jBox.tip.mess = null;
}

// 关闭提示框
function closeTip(){
	top.$.jBox.closeTip();
}

//显示提示框
function showTip(mess, type, timeout, lazytime){
	resetTip();
	setTimeout(function(){
		top.$.jBox.tip(mess, (type == undefined || type == '' ? 'info' : type), {opacity:0, 
			timeout:  timeout == undefined ? 2000 : timeout});
	}, lazytime == undefined ? 500 : lazytime);
}

// 显示加载框
function loading(mess){
	if (mess == undefined || mess == ""){
		mess = "OnLoading...";
	}
	resetTip();
	top.$.jBox.tip(mess,'loading',{opacity:0.5,persistent:true});
}

// 警告对话框
function alertx(mess, closed){
	top.$.jBox.info(mess, 'Waring', {closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}

// 确认对话框
function confirmx(mess, href, closed){
	top.$.jBox.confirm(mess,'System Prompt',function(v,h,f){
		if(v=='ok'){
			if (typeof href == 'function') {
				href();
			}else{
				resetTip(); //loading();
				location = href;
			}
		}
	},{buttonsFocus:1, closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

// 提示输入对话框
function promptx(title, lable, href, closed){
	top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + lable + "：<input type='text' id='txt' name='txt'/></div>", {
			title: title, submit: function (v, h, f){
	    if (f.txt == '') {
	        top.$.jBox.tip("Please input" + lable + "。", 'error');
	        return false;
	    }
		if (typeof href == 'function') {
			href();
		}else{
			resetTip(); //loading();
			location = href + encodeURIComponent(f.txt);
		}
	},closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	return false;
}

// 添加TAB页面
function addTabPage(title, url, closeable, $this, refresh){
	top.$.fn.jerichoTab.addTab({
        tabFirer: $this,
        title: title,
        closeable: closeable == undefined,
        data: {
            dataType: 'iframe',
            dataLink: url
        }
    }).loadData(refresh != undefined);
}

// cookie操作
function cookie(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
}

// 数值前补零
function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

// 转换为日期
function strToDate(date){
	return new Date(date.replace(/-/g,"/"));
}

// 日期加减
function addDate(date, dadd){  
	date = date.valueOf();
	date = date + dadd * 24 * 60 * 60 * 1000;
	return new Date(date);  
}

//截取字符串，区别汉字和英文
function abbr(name, maxLength){  
 if(!maxLength){  
     maxLength = 20;  
 }  
 if(name==null||name.length<1){  
     return "";  
 }  
 var w = 0;//字符串长度，一个汉字长度为2   
 var s = 0;//汉字个数   
 var p = false;//判断字符串当前循环的前一个字符是否为汉字   
 var b = false;//判断字符串当前循环的字符是否为汉字   
 var nameSub;  
 for (var i=0; i<name.length; i++) {  
    if(i>1 && b==false){  
         p = false;  
    }  
    if(i>1 && b==true){  
         p = true;  
    }  
    var c = name.charCodeAt(i);  
    //单字节加1   
    if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {  
         w++;  
         b = false;  
    }else {  
         w+=2;  
         s++;  
         b = true;  
    }  
    if(w>maxLength && i<=name.length-1){  
         if(b==true && p==true){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         if(b==false && p==false){  
             nameSub = name.substring(0,i-3)+"...";  
         }  
         if(b==true && p==false){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         if(p==true){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         break;  
    }  
 }  
 if(w<=maxLength){  
     return name;  
 }  
 return nameSub;  
}



/*筛选按钮初始化*/
function filterActionInit() {
	
	var isFilterAction = $("#isFilterAction").val();
	if(isFilterAction=="true") {
		fiterPanleShow();
	}else {
		fiterPanleHide();
	}
	
	$("#filter-btn").click(function(){
		if($("#isFilterAction").val()=="true") {
			$("#isFilterAction").val("false");
			fiterPanleHide();
		}else {
			$("#isFilterAction").val("true");
			fiterPanleShow();
		}
	});
}

function tzheight(){
	var hei=document.documentElement.clientHeight;
	hei=hei-91;
	var rowheigth=0;
	if($('.row')){
		 rowheigth=$('.row').height();
		 hei=hei-rowheigth;
	}
	if($("#messageBox")){
		  var messageH=$("#messageBox").height();
		     if(messageH > 0){
		    	 hei=hei-messageH-52;
		     }
		}
	if($('.table-responsive')){
		$('.table-responsive').css({'height':hei+'px'});
	}
}

function fiterPanleShow() {
	$("#filter-btn").find("i").removeClass("fa-angle-double-down").addClass("fa-angle-double-up");
	//$(".btn-search").addClass("hide");
	$(".filter-action-select-panle").removeClass("hide");
	tzheight();
}

function fiterPanleHide() {
	$("#filter-btn").find("i").removeClass("fa-angle-double-up").addClass("fa-angle-double-down");
	//$(".btn-search").removeClass("hide");
	$(".filter-action-select-panle").addClass("hide");
	tzheight();
}


//location.pathname 属性返回url的路径名，如果存在 //替换为/
function getLocationPathName() {
	return window.location.pathname.replace("\//","\/");
}

//自定义列设置事件
function customColumnClick(){

	
	var submit = function(v,h,f) {
		var abVisCols = [true];
		top.$(".customColumnList input[type='checkbox']").each(function(){
			abVisCols[$(this).val()] = $(this).prop("checked");
		});

		var key = getLocationPathName()+"_visCols";
		Cookies.set(key,JSON.stringify(abVisCols),{expires:7});
		customColumnInit();
		
		return true;	
	}
	
	
	var html = "<div class='customColumnList'>";
	$(".table tr th").each(function(colIndex,obj){	
		
		if(colIndex==0) { //第一列th含 checkbox ,结束本次循环, 下标从1开始反之0开始
			var ckVal = $(this).find("input[type='checkbox']").val();
			if(typeof(ckVal) != "undefined") {
				return true;	
			}
		}
		/*if($(this).hasClass("cfgDesc")){
			return true;	
		}*/
		var checked  = ($(this).is(":visible") ? "checked='checked'":"");
		var ckbox_html = "<input type='checkbox' "+checked+" value='"+colIndex+"'/>";
		html+="<div class='col-md-6'><label>"+ckbox_html+" "+$(this).html()+"</label></div>";
	})
	html +="</div>";
	
	top.$.jBox(html,{title:"<i class='fa fa-wrench'></i> Custom Columns",showIcon:false,opacity:0.3,submit:submit});
	
}

//自定义列初始化，先取cookie 后取table tr th
function customColumnInit() {
	
	var key = getLocationPathName()+"_visCols";
	var abVisCols = Cookies.get(key);
	if(abVisCols != null && abVisCols.length > 0) {
	
		abVisCols = $.parseJSON(abVisCols);
		
		$(abVisCols).each(function(indexCol,col){
			if(col) {
 	    		//选中则显示
 	            showColumn(indexCol);
 	    	}else {
 	    		 hideColumn(indexCol);
 	    	}
		})	
	}else {
		
		$(".table tr th").each(function(){
 	    	
 	    	var indexCol = $(this).index();
 	    	if($(this).attr("isVisible")=="false") {
 	            hideColumn(indexCol);
 	    	}else {
 	    		//选中则显示
 	            showColumn(indexCol);
 	    	}
 	    	
 	    });
	}
	
}



//隐藏索引列
function hideColumn(index) {
    $(".table tr").each(function() {
        $(this).find("th:eq(" + index + ")").hide();
        $(this).find("td:eq(" + index + ")").hide();
    })

}
//显示索引列
function showColumn(index) {

    $(".table tr").each(function() {
        $(this).find("th:eq(" + index + ")").show();
        $(this).find("td:eq(" + index + ")").show();
    })

}



