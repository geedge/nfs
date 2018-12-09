<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<script>
 	$(document).ready(function() {
 		if($("#functionId",parent.document).val()==632){
 			$("#fileAdd").show();
 		}else{
 			$("#fileAdd").hide();
 		}
 		$("#submitButton").on('click',function(){
 			var selectPic = "";
 			$("div[name='checkPic']").each(function(){
 	            if($(this).attr("ischecked")=="true")
 	            {
 	            	selectPic += $(this).attr("value")+"|";
 	            }
 	          });
 			if(selectPic==""){
 				top.$.jBox.tip("<spring:message code='one_more'/>", "<spring:message code='info'/>");
 				return false;
 			}else{
 				$("#srcPath",parent.document).val(selectPic);
 	 			//关闭此窗口
 				window.parent.window.jBox.close();
 			}
 			
 		});
 		$("div[name='checkPic']").on('click',function(){
 			if($(this).attr("ischecked")=="true"){
 				$(this).attr("ischecked","false");
 				$(this).find(".icon-check").css("display","none");
 			}else{
 				$(this).attr("ischecked","true");
 				$(this).find(".icon-check").css("display","block");
 			}
 		});
 		$(".checkAll").on('click',function(){
 			if($(this).hasClass("checkAll-cancel")){
 				$(this).addClass("checkAll-check");
 				$(this).removeClass("checkAll-cancel");
 				$("div[name='checkPic']").attr("ischecked","true");
 				$("div[name='checkPic']").find(".icon-check").css("display","block");
 			}else{
 				$(this).addClass("checkAll-cancel");
 				$(this).removeClass("checkAll-check");
 				$("div[name='checkPic']").attr("ischecked","false");
 				$("div[name='checkPic']").find(".icon-check").css("display","none");
 			}
 		})
 		
 	});
 	function addFile(){
 		$("#srcFile" , parent.document).trigger("click");
 	}
</script>
<style type="text/css">
.selectImgDiv .item{width:150px;height:150px;border:solid thin #aaa;float:left;margin:10px;cursor:pointer;overflow:hidden}
.selectImgDiv .item:hover{border:solid thin #09f}.selectImgDiv .item .img_show{width:100%;height:100%;overflow:hidden}
.selectImgDiv .item .img_show img{width:100%;height:100%}
.selectImgDiv .item:hover .img_show img{transform:scale(1.3);transition:all 1s ease 0s;-webkit-transform:scale(1.3);-webkit-transform:all 1s ease 0s}
.selectImgDiv .item .img_title{height:30px;text-align:center;font-size:16px;line-height:30px;background-color:rgba(102,102,102,.6);position:relative;color:#fff;font-weight:700}
.selectImgDiv .item:hover .img_title{top:-30px}
.selectImgDiv .item .img_isCheck{position:relative;top:-168px;text-align:right}
.selectImgDiv .item .img_isCheck i{font-size:30px;display:none;color:#fdfdfd;padding-top:3px;}
.checkAll{font-size:30px;display:none;color:#6f3}
/* @font-face{
	font-family:iconfont;
	src:url(../font/iconfont.eot?t=1495283771116);
	src:url(../font/iconfont.eot?t=1495283771116#iefix) format('embedded-opentype'),url(../font/iconfont.woff?t=1495283771116) format('woff'),url(../font/iconfont.ttf?t=1495283771116) format('truetype'),url(../font/iconfont.svg?t=1495283771116#iconfont) format('svg')
} */
.iconfont{
	background-color: #6f3;
    width: 25px;
    height: 25px;
    float: right;
    margin-right: 5px;
} 
.icon-check:before{content:"\2714"}
.checkAll-check:before{content:"\2611"}
.checkAll-cancel:before{content:"\2610"}
</style>
</head>
<body>

<div class="page-content">

		<div class="col-md-12">
			<div class="portlet">
				<div class="portlet-body">
					<div class="row" style="margin-top:10px">
						<input type="hidden" id="picFilePath" name="picFilePath" value="${picFilePath }"/>
						<div class="col-md-12">
							<div class="pull-right">
							<button type="button" id="fileReload" class="btn btn-default" style="margin-right:10px" onClick="window.location.reload()" value="111"><i class="fa fa-refresh"></i> <spring:message code="refresh"/></button>
							<button type="button" id="fileAdd" onClick="addFile()" class="btn btn-primary" style="margin-right:10px"><i class="fa fa-plus"></i> <spring:message code="add"/></button>
							<button type="button" id="submitButton" class="btn btn-warning" style="margin-right:10px">
									<i class="fa fa-plus"></i>
									<spring:message code="ok"></spring:message></button>
							</div>
							<div class="pull-right">
									<i class="checkAll checkAll-cancel" style="display:block;font-style:normal;margin:-5px 8px 0px 0px"></i>
							</div>
							
						</div>
	
					</div>
					<div class="container" style="margin-top:30px">
						<%-- <table id="contentTable" class="table table-striped table-bordered table-condensed text-nowrap">
							<thead>
								<tr>
									<th><input type="checkbox" class="i-checks" id="checkAll"></th>
									<th><spring:message code="index"/></th>
									<th><spring:message code="thumbnail"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${fileList}" var="file" varStatus="status" step="1">
									<tr>
									<td><input type="checkbox" name="checkPic" ${file.checked } class="i-checks" id="${status.index+1}" value="${file.picName }"></td>
									<td>${status.index+1}</td>
									<td>
										<a href="${pageContext.request.contextPath}/${file.picUrl }" target="_blank">
											<img alt="" src="${pageContext.request.contextPath}/${file.picUrl }" width="100px" height="100px">
										</a>
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table> --%>
						<div class="selectImgDiv" id="selectItemDiv">
							<c:forEach items="${fileList}" var="file" varStatus="status" step="1">
								<div class="item i-checks" name="checkPic" id="checkPic${status.index }" ischecked="${file.checked }" value="${file.picName }">
									<div class="img_show">
										<img alt="" src="${pageContext.request.contextPath}/${file.picUrl }" width="120px" height="120px">
									</div>
									<div class="img_title">
										${file.picName }
									</div>
									<div class="img_isCheck">
										<i class="iconfont icon-check" style="<c:if test='${file.checked }'>display:block</c:if>"></i>
									</div>
								</div>
							</c:forEach>
						</div>
						<%-- <c:forEach items="${fileList}" var="file" varStatus="status" step="1">
						
						<input type="checkbox" name="checkPic" ${file.checked } class="i-checks" id="${status.index+1}" value="${file.picName }">
						<a href="${pageContext.request.contextPath}/${file.picUrl }" target="_blank">
							<img alt="" src="${pageContext.request.contextPath}/${file.picUrl }" width="120px" height="120px">
						</a>
						
						<c:if test="${(status.index+1) mod 8 eq 0 }">
						<br><br>
						</c:if>
						</c:forEach> --%>
					</div>
				</div>
			</div>
</div>
</div>
</body>
</html>