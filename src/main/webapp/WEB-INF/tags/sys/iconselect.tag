<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<i id="${id}Icon" class="${not empty value?value:' hide'}"></i>&nbsp;<label id="${id}IconLabel">${not empty value?value:'null'}</label>&nbsp;
<input id="${id}" name="${name}" type="hidden" value="${value}"/><a id="${id}Button" href="javascript:" class="btn"><spring:message code='select'/></a>&nbsp;&nbsp;
<script type="text/javascript">
	$("#${id}Button").click(function(){
		top.$.jBox.open("iframe:${ctx}/tag/iconselect?value="+$("#${id}").val(), "<spring:message code='select_icon'/>", 1200, $(top.document).height()-180, {
			top:'8%',
            buttons:{"<spring:message code='ok'/>":"ok", "<spring:message code='clear'/>":"clear", "<spring:message code='close'/>":true}, submit:function(v, h, f){
                if (v=="ok"){
                	var icon = h.find("iframe")[0].contentWindow.$("#icon").val();
                	$("#${id}Icon").attr("class", icon);
	                $("#${id}IconLabel").text(icon);
	                $("#${id}").val(icon);
                }else if (v=="clear"){
	                $("#${id}Icon").attr("class", "hide");
	                $("#${id}IconLabel").text("<spring:message code='nothing'/>");
	                $("#${id}").val("");
                }
            }, loaded:function(h){
                $(".jbox-content", top.document).css("overflow-y","hidden");
            }
        });
	});
</script>