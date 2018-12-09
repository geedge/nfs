$(document).ready(function() {
	// 隱藏日誌詳情
//	$("table.logTb .icon-book-open").hide();
	// 界面鼠标悬停事件
/*	$("table.logTb").find("td").not(":has(a)").bind("mouseover", function(){
		var str = $(this).html(this.innerHTML.trim()).text();
		//解决火狐title不能自动换行
		var count = Math.floor(str.length/64);
		for(var i=1;i<=count;i++){
			str=str.substring(0,i*64-1)+"\n"+str.substring(i*64-1);
		}
		this.title=str;
	});
	var fontSize = 0;*/
/*	$("table.logTb th").each(function(j){
		// 判断是否支持currentStyle属性 是：IE  否：FF or Chrome
		var finalStyle = this.currentStyle ? this.currentStyle : document.defaultView.getComputedStyle(this , null);
		fontSize = (finalStyle.fontSize).replace("px","");
		// 设置界面标题宽度（不包括第一列）
		var px = getPixelsCount($(this).text(),fontSize)+32;
		this.setAttribute('width',px+'px');
		// URL列加宽
		var th = this;
		var thText = this.innerText;
		//将字符串过宽的列增加宽度
		$("table.logTb tbody tr").each(function(){
			$(this).find("td").each(function(i){
				var tdText = this.innerText;
				if((tdText.trim().length > thText.trim().length) && j==i){
					var px = getPixelsCount(thText,fontSize)+100;
					th.setAttribute('width',px+'px');
				}
			});
		});
	});*/
	
/*	var tdString = "";
	$("table.logTb tbody tr").each(function(){
		tdString = $(this).find('td:first').text();
	});
	
	$("table.logTb th:lt(1)").each(function(){
		// 设置界面第一列标题宽度
		var thString = $(this).text();
		if(tdString.trim().length < thString.length){
			var thPx = getPixelsCount(thString,fontSize)+32;
			this.setAttribute('width',thPx+'px');
		}else{
			var tdPx = getPixelsCount(tdString,fontSize)+45;
			this.setAttribute('width',tdPx+'px');
		}
	});*/
	
	// 添加由配置界面跳转到日志查询界面后的返回按钮
	var isLogTotalSearch = $("#isLogTotalSearch").val();
	if(isLogTotalSearch != undefined && isLogTotalSearch != ''){
		$(".theme-panel").html("<button type='button' onclick='back()' class='btn btn-primary'>"+$.validator.messages.go_back+"</button>");
	}
});
	
// 获取字符串对等的像素值
function getPixelsCount(str, strFontSize){
	// 字符个数
	var stringCharsCount = str.length;
	
	// 像素值
	var stringPixelsCount = 0;
		
	var element = document.createElement("span");
	element.style.fontSize = strFontSize; // 设置span的fontSize
	element.style.visibility = "hidden"; // 设置span不可见
	element.style.display = "inline-block";
	element.style.wordBreak = "break-all !important";
		
	document.body.appendChild(element);
	for(var i = 0; i < stringCharsCount; i++){
		if(str[i] == " "){
			element.innerHTML = "&nbsp;";
		}else{
			element.innerHTML = str[i];
		}
		stringPixelsCount += element.offsetWidth;
	}
		
	return stringPixelsCount;
}
function back(){
	// 获取配置界面URl并跳转
	var url = sessionStorage.getItem('cfgUrl');
	switchMenu("86");
	window.location.href = url;
}