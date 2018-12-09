<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<script>
function pagination() {

	pageNo = 1;
	count = $('tbody').children().size();// 总记录数
	//处理ajax时jquery选择器缓存的问题
/* 	dataLength = "${fn:length(datas)}";
	preCount = 0;
	if (dataLength < count) {
		preCount = count - dataLength;
		count = count - preCount;
		alert(count)
		//$('tbody').children().slice(0, preCount).remove();
	} */
	
	
	pageSize = 30; // 页面大小
	first = 1;// 首页索引
	last = Math.ceil(count/pageSize);// 尾页索引
	length = 8;// 显示页面长度
	slider = 1;// 前后显示页面长度
	funcParam = "";
	if (last == 0) {
		last++;
	}
	if (last < first) {
		last = first;
	}
	if (pageNo <= 1) {
		pageNo = first;
		firstPage = true;
	}
	if (pageNo >= last) {
		pageNo = last;
		lastPage = true;
	}
	if (pageNo < last - 1) {
		next = pageNo + 1;
	} else {
		next = last;
	}
	if (pageNo > 1) {
		prev = pageNo - 1;
	} else {
		prev = first;
	}
	if (pageNo < first) {
		pageNo = first;
	}

	if (pageNo > last) {
		pageNo = last;
	}
	toPage(1, pageSize, '');
}

//跳页
function toPage(pageNo, pageSize, funcParam) {
	prev = pageNo - 1;
	next = pageNo + 1;
	
	var inhtml = getFootHtml(pageNo);

	$(".page").html(inhtml);

	//隐藏该对象下面的所有子元素
	$('tbody').children().css('display', 'none');
	//显示元素
	$('tbody').children().slice((pageNo-1)*pageSize, pageNo*pageSize).css('display', '');
	totaltb(pageNo,pageSize);
}

//底部跳页按钮
function getFootHtml(pageNo) {
	var htm = "";
	if (count == 0) {
		htm = "<div class=\"none-data\"><i class=\"fa fa-warning font-red-flamingo\"></i>&nbsp;&nbsp;" + "<spring:message code="noneData"/>" + "</div>";
		return htm;
	}
	htm = "<ul>";
	if (pageNo == first) {
		htm += "<li class=\"disabled\"><a href=\"javascript:\">&#171; " + "<spring:message code="previousPage"/>" + "</a></li>";
	} else {
		htm += "<li><a href=\"javascript:\" onclick=\"toPage(" + prev + "," + pageSize + ",'" + funcParam + "');\">&#171; " + "<spring:message code="previousPage"/>" + "</a></li>";
	}
	
	var begin = pageNo - (length / 2);

	if (begin < first) {
		begin = first;
	}

	var end = begin + length - 1;

	if (end >= last) {
		end = last;
		begin = end - length + 1;
		if (begin < first) {
			begin = first;
		}
	}

	if (begin > first) {
		var i = 0;
		for (i = first; i < first + slider && i < begin; i++) {
			htm += "<li tmp=\"" + i + "\"><a href=\"javascript:\" onclick=\"toPage(" + i + "," + pageSize + ",'" + funcParam + "');\">" +  (i + 1 - first) + "</a></li>";
		}
		if (i < begin) {
			htm += "<li class=\"disabled\"><a href=\"javascript:\">...</a></li>";
		}
	}

	for (var i = begin; i <= end; i++) {
		if (i == pageNo) {
			htm += "<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first) + "</a></li>";
		} else {
			htm += "<li><a href=\"javascript:\" onclick=\"toPage(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>";
		}
	}

	if (last - end > slider) {
		htm += "<li class=\"disabled\"><a href=\"javascript:\">...</a></li>";
		end = last - slider;
	}

	for (var i = end + 1; i <= last; i++) {
		htm += "<li><a href=\"javascript:\" onclick=\"toPage(" + i + "," + pageSize + ",'" + funcParam + "');\">" + (i + 1 - first) + "</a></li>";
	}
	
	if (pageNo == last) {
		htm += "<li class=\"disabled\"><a href=\"javascript:\">" + "<spring:message code="nextPage"/>" + " &#187;</a></li>";
	} else {
		htm += "<li><a href=\"javascript:\" onclick=\"toPage(" + next + "," + pageSize + ",'" + funcParam + "');\">" + "" + "<spring:message code="nextPage"/>" + " &#187;</a></li>";
	}

	htm += "<li class=\"disabled controls\"><a href=\"javascript:\">" + "<spring:message code="current"/>" + " ";
 	htm += "<input type=\"text\" value=\"" + pageNo + "\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)";
	htm += "toPage(this.value," + pageSize + ",'" + funcParam + "');\" onclick=\"this.select();\"/> / ";
	/*htm += pageNo+" / ";*/
	htm += last+ " <spring:message code="page"/>" + "，";
	/* htm += "<input type=\"text\" value=\"" + last + "\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)";
	htm += "(toPage" + pageNo + ",this.value,'" + funcParam + "');\" onclick=\"this.select();\"/> " + "<spring:message code="page"/>" + "，"; */
	htm += "" + "<spring:message code="total"/>" + " <span id='showTotalCount'>" + count + " </span> " + "<spring:message code="count"/> " + "</a></li>";
	htm += "</ul>";
	return htm;
}
</script>