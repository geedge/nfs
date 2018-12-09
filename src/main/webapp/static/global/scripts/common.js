$(function(){
//	var leff =$("span[class~='le-ca-fo']").attr("data-original-title")
//	getConfigSyncStatus();
	$("#contentTable").not(".logTb").find("tbody tr").each(function(i){
		if($(this).find("input[type='checkbox']").attr("value")==3){
			var ids = $(this).find("input").attr("id");
			var resetLeff =$(this).find("span[class~='le-ca-fo']");
			var indexTableName = $("#contentTable tbody tr td span[class~='le-ca-fo']").attr("indexTable");
			var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.lastIndexOf("/nis")+4);
			if(ids!=null&&indexTableName!=null){
				$.ajax({
					type:'post',
					url:pathName+'/basics/serviceDictInfo/requestCancleInfoNumber',
					data:{"ids":ids,"indexTable":indexTableName},
					dataType:'json',
					success:function(data){//处理返回结果
						//根据表名跟cfgid获取 requestNumber；
						if(data.requestTitle!=null){
							resetLeff.attr("data-original-title",data.requestTitle)
						}else{
							resetLeff.attr("data-original-title","")
						}
					}
				});
			}
		}
	})
	
	
	
	//增加描述新增时的文字长度限制
	$("form input[name='cfgDesc']").attr("maxlength","128");
	$("form  input[name$='cfgKeywords']").attr("maxlength","1024");
	$("form  input[class~='domainCheck']").attr("maxlength","1024");
	//截取过长的文字 使用id选择器;例如:tab对象->tr->td对象. 排除日志table .logTb  
	$("#contentTable").find("td").not(":has(a)").each(function(i,element){
	     //获取td当前对象的文本,如果长度大于25;
		if(!$(element).find(".tooltips").length>0){
			var tdclass= $(this).attr("class");//no_substr 不进行截取
	    	if(tdclass!='no_substr'){	
		     if($(this).text().trim().length>28){  
		        //给td设置title属性,并且设置td的完整值.给title属性.
		    	var strTitle = $(this).text().trim().replace(/[\r\n]/g,"").replace(/\s+/g, " ");
	    		//解决火狐title不能自动换行
	    		var count = Math.floor(strTitle.length/62);
	    		for(var i=1;i<=count;i++){
	    			strTitle=strTitle.substring(0,i*62-1)+"\n"+strTitle.substring(i*62-1);
	    		}
				$(this).attr("title",strTitle);  
	            //获取td的值,进行截取。赋值给text变量保存.  
				var text=$(this).text().trim().substring(0,25)+"...";
	            //重新为td赋值;  
	            $(this).text(text);  
	     	}
	      }
		}
	  });
	
	$("input[name$='isCaseSenstive']").on("change",function(){
		setIsHexBin(this);
	});
	setHexCaseSenstive();
	$("select[name$='isHexbin']").each(function(){
		setIsHexBin(this);
	});
	$("input[name$='isHex']").on("change",function(){
		setIsHexBin(this);
	});
	$("a[name=viewLogInfo]>i").on("click",function(){
		var html = "<div class='logInfo'>";
		$(this).parents("tr").find("td").each(function(index,element){
			if(index >0){
				var text="";
				if($(element).find(".tooltips").length > 0){
					text=$(element).find(".tooltips").attr("data-original-title").trim();
					
				}else{
					text=$(element).text().trim()
				}
				if(!$(element).find(".tooltips").length>0&&typeof($(element).attr("title"))!="undefined"&&$(element).attr("title")!=null&&$(element).attr("title").length > 0){
					text=$(element).attr("title").trim();
				
				}
				//特殊字符转义
				text=text.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
				
				html+="<div class='row col-md-12'>";
				html+="<div class='col-md-4'><label>"+$(".table tr th").eq(index).text().trim()+":</label></div>";
				html+="<div class='col-md-6' style='width:330px;display:block;word-break:break-all;word-wrap:break-word;'><label>"+text+"</label></div>";
				html+="</div>";
				
			}
		})
		html +="</div>";
		
		top.$.jBox(html,{height:450,width:600,title:"<i class='icon-book-open'></i> Log Info",showIcon:false,opacity:0.5});
	});
	//表格中的tooltips鼠标点击复制完整的内容
	$("td>.tooltips").not(".addrPool").on("click",function(){
		//$(this).attr("data-clipboard-action","copy");
		$(this).attr("data-clipboard-text",$(this).attr("data-original-title"));
		var clipboard = new ClipboardJS('td >.tooltips');
		clipboard.on('success', function(e) {
			top.$.jBox.tip("Copyied!",'copy',{opacity:0.5,persistent:false});
	        console.log(e);
	        e.clearSelection();
	    });
	    clipboard.on('error', function(e) {
	        console.log(e);
	    });
	});
	//表格中的title鼠标点击复制完整内容
	$("table.logTb td[title]").on("click",function(){
		//$(this).attr("data-clipboard-action","copy");
		$(this).attr("data-clipboard-text",$(this).attr("title"));
		var clipboard = new ClipboardJS('td[title]');
		clipboard.on('success', function(e) {
			top.$.jBox.tip("Copyied!",'copy',{opacity:0.5,persistent:false});
//	        console.log(e);
	        e.clearSelection();
	    });
	    clipboard.on('error', function(e) {
	        console.log(e);
	    });
	});
	var tree2 = $("select[name=lableTest]").treeMultiselect({
        searchable: true,
        hideSidePanel:true,
        startCollapsed:true
      });
	var tree1 = $("select[name=classifyTest]").treeMultiselect({
		searchable: true,
        hideSidePanel:true,
        startCollapsed:true
	});
	//全选及取消
	$("#checkAll").change(function(){
		if($("#checkAll").prop("checked")){
			$("input.i-checks").prop("checked",true);
		}else{
			$("input.i-checks").prop("checked",false);
		}
	});
	
	$("select[name$='ipType']").each(function(){
		switchIpInfo(this);
	})
	$("select[name$='exprType']").each(function(){
		switchIpInfo(this);
	})
	$("select[name$='ipType']").on("change",function(){
		switchIpInfo(this);
	});
	$("select[name$='ipPattern']").on("change",function(){
		switchIpInfo(this);
	});
	$("select[name$='portPattern']").on("change",function(){
		switchIpInfo(this);
	});
	
	switchAction($("input[name='action']:checked").val());
	
	$(".glyphicon-plus").on("click",function(){
		var obj=$(this).parents(".form-section").next(".container-fluid").clone();
		var len=$(this).parents("div").find(".container-fluid:visible").size();
		if($(this).parent().parent().parent().attr("id")=="areaIp"){
			obj.find("[id^='_areaCfg']").each(function(){
				var name=$(this).attr("id").replace("_areaCfg.","");
				$(this).removeAttr("id");
				$(this).attr("name","areaCfg["+len+"]."+name);
				$(this).parents(".form-group").find("div[for^='_areaCfg']").attr("for","areaCfg["+len+"]."+name);
				$(this).selectpicker("refresh");
			});
			obj.removeClass("hidden");
			switchIpInfo(obj.find("select[name$='ipType']"));
			obj.find("select[name$='ipType']").on("change",function(){
				switchIpInfo($(this));
			});
			obj.find("select[name$='ipPattern']").on("change",function(){
				switchIpInfo($(this));
			});
			obj.appendTo("#areaIp");
			$("#areaIsp").find(".container-fluid").each(function(){
				if(!$(this).hasClass("hidden")) $(this).remove();
			});
			if(len==0){
				$("#areaIp").find(".container-fluid:visible").eq(0).find("span[title=remove]").addClass("hidden");
			}
		}else if($(this).parent().parent().parent().attr("id")=="areaIsp"){
			obj.find("[name^='areaCode']").each(function(){
				$(this).attr("name","areaIsp["+len+"].area");
				$(this).parents(".form-group").find("div[for^='areaCode']").attr("for","areaIsp["+len+"].area");
				$(this).selectpicker("refresh");
			});
			obj.find("[name^='ispCode']").each(function(){
				$(this).attr("name","areaIsp["+len+"].isp");
				$(this).selectpicker("refresh");
			});
			obj.removeClass("hidden");
			obj.appendTo("#areaIsp");
			$("#areaIp").find(".container-fluid").each(function(){
				if(!$(this).hasClass("hidden")) $(this).remove();
			});
			if(len==0){
				$("#areaIsp").find(".container-fluid:visible").eq(0).find("span[title=remove]").addClass("hidden");
			}
		}
	});
	$("input[name='areaType']").on('change',function(){
		var val=$(this).val();
		if($(this).is(":visible")){
			if(val==0){
				$("#areaIp").removeClass("hidden");
				$("#areaIsp").addClass("hidden");
				if($("#areaIp").find(".container-fluid:visible").size() <1){
					$("#areaIp").find(".glyphicon-plus").click();
				}
				$("#areaIp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).removeAttr("disabled");
						$(this).removeClass("disabled");
					})
				});
				$("#areaIsp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			}else{
				$("#areaIsp").removeClass("hidden");
				$("#areaIp").addClass("hidden");
				if($("#areaIsp").find(".container-fluid:visible").size() <1){
					$("#areaIsp").find(".glyphicon-plus").click();
				}
				$("#areaIsp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).removeAttr("disabled");
						$(this).removeClass("disabled");
					})
				});
				$("#areaIp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			}
		}else{
			$("#areaIsp").addClass("hidden");
			$("#areaIp").addClass("hidden");
			$("#areaIsp").find(".container-fluid").each(function(){
				$(this).find("input,select,div,button").each(function(){
					$(this).attr("disabled","disabled");
				})
			});
			$("#areaIp").find(".container-fluid").each(function(){
				$(this).find("input,select,div,button").each(function(){
					$(this).attr("disabled","disabled");
				})
			});
		}
	});
	$("input[name='isAreaEffective']").on('change',function(){
		var val=$(this).val();
		if(val==1){
			$(".areaType").find("input,select,div,button").each(function(){
				$(this).removeAttr("disabled");
				$(this).removeClass("disabled");
			});
			$(".areaType").removeClass("hidden");
			if($("input[name='areaType']:checked").val()==1){//areaISP
				$("#areaIsp").removeClass("hidden");
				if($("#areaIsp").find(".container-fluid:visible").size() <1){
					$("#areaIsp").find(".glyphicon-plus").click();
				}
				$("#areaIsp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).removeAttr("disabled");
						$(this).removeClass("disabled");
					})
				});
				$("#areaIp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			}else if($("input[name='areaType']:checked").val()==0){//areaIp
				$("#areaIp").removeClass("hidden");	
				if($("#areaIp").find(".container-fluid:visible").size() <1){
					$("#areaIp").find(".glyphicon-plus").click();
				}
				$("#areaIp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).removeAttr("disabled");
						$(this).removeClass("disabled");
					})
				});
				$("#areaIsp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			}else{
				$(".areaType").find("[value='1']").prop("checked",true);
				$("#areaIsp").removeClass("hidden");
				if($("#areaIsp").find(".container-fluid:visible").size() <1){
					$("#areaIsp").find(".glyphicon-plus").click();
				}
				$("#areaIsp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).removeAttr("disabled");
						$(this).removeClass("disabled");
					})
				});
				$("#areaIp").find(".container-fluid").each(function(){
					$(this).find("input,select,div,button").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			}
			
		}else{
			$(".areaType").addClass("hidden");
			$("#areaIp").addClass("hidden");
			$("#areaIsp").addClass("hidden");
			
			$(".areaType").find("input,select,div,button").each(function(){
				$(this).attr("disabled","disabled");
			});
			$("#areaIsp").find(".container-fluid").each(function(){
				$(this).find("input,select,div,button").each(function(){
					$(this).attr("disabled","disabled");
				})
			});
			$("#areaIp").find(".container-fluid").each(function(){
				$(this).find("input,select,div,button").each(function(){
					$(this).attr("disabled","disabled");
				})
			});
		}
	});
	areaControlInit();
	/*=====关键字与表达式处理 开始=====*/
	var tagsInputSettings="#tags";
	var options;
	$("input[name$='cfgKeywords']").each(function(){
		var tagsId = $(this).attr("id");
		var objNamePrefix = $(this).attr("name").split("cfgKeywords")[0];	
		var isTags = $(this).hasClass("tags");//有tags样式的关键字输入框才需处理
		if(typeof(tagsId)!=='undefined' && tagsId.indexOf("tags_")!=-1 && isTags){
			// 表达式类型初始
			if($("input:radio[name='"+objNamePrefix+"exprType'][checked=checked]").val()==1){
				options = $("select[name='"+objNamePrefix+"matchMethod']").find("option[value!=0]");
				$("select[name='"+objNamePrefix+"matchMethod']").find("option[value!=0]").remove();
			}else{
				options = $("select[name='"+objNamePrefix+"matchMethod']").find("option[value!=0]");
			}
			//表单中如果有关键字内容可能输入多个关键字的情况，根据输入关键字个数确定表达式选中情况，不允许手动选中
			$("input:radio[name='"+objNamePrefix+"exprType']").attr("disabled",true);
			tagsInputSettings = tagsInputSettings+",#"+tagsId;
		}
		
	});
	$(tagsInputSettings).tagsInput({
   	   width:$(tagsInputSettings).find(".form-control").width(),
   	   defaultText:'please input keywords',
   	   'delimiter':'***and***',//特殊字符串分隔与表达式的多关键词
   	   maxCount:4,
   	   onAddTag:function(tag,size){
	   		var reg = new RegExp(/\t|\r|\n/);
			if (tag.match(reg)) {
				$(this).parents(".col-md-6").next("div").html("<label class='error'>"+$.validator.messages.hasInvisibleChar.replace("{0}","'"+tag+"'")+"</label>");
			}else{
				$(this).parents(".col-md-6").next("div").html("");
			}
			//var keywordValue = "";
   		   	var objNamePrefix = $(this).attr("name").split("cfgKeywords")[0];
				/*$("span[class='tag']").each(function(){
					keywordValue = keywordValue+"***iie***"+$(this).find("span").text().trim();
				});
				$(this).prev("input[name$='cfgKeywords']").val(keywordValue);*/
   		   exprTypeChecked(objNamePrefix,size,options);
   	   },
   	   onRemoveTag:function(tag,size){
   		   $(this).parents(".col-md-6").next("div").html("");
   		   //var keywordValue = "";
   		   var objNamePrefix = $(this).attr("name").split("cfgKeywords")[0];
	   		/*$("span[class='tag']").each(function(){
				keywordValue = keywordValue+"***iie***"+$(this).find("span").text().trim();
			});
			$(this).prev("input[name$='cfgKeywords']").val(keywordValue);*/
   		    exprTypeChecked(objNamePrefix,size,options);
   	   }
   });
   $(".tagsinput").popover({
		animation:true,
		container:'body',
		placement:'right',
		html:true,
		trigger:"hover",
		title:"",
		content:function(){
			var content = $("#tagsinputTip").text();
		    return content;
		}
	});
   /*=====关键字与表达式处理 结束=====*/
  /*$("input[name$='cfgKeywords']").each(function(){
		if($(this).hasClass("tags")){
			if($(this).val()==''){
				$(this).parents(".form-group").find(
						"div[for='"
								+ $(this).attr("name")
								+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$("#keywordError").text()+"</label>");
				flag = false;
				return;
			}
		}
	})*/
   
   //处理增强字符串配置的匹配区域
   $(".district").each(function(){
		if($(this).siblings(".otherValue").val()!=''){
			if($(this).val()!=$(this).siblings(".otherValue").val()){
				$(this).find("option[value=others]").attr("selected","selected");
				$(this).siblings(".otherValue").prop("type","text");
			}
		}
		
	});
   $("input[name$='exprType']").on("change",function(){
	   setDefaultMatchMethod(this);
	});
   $("input[name$='exprType']:checked").each(function(){
	   //setDefaultMatchMethod(this);
   });
   $(".action").on("change", function() {
		$("#serviceId").val($(this).attr("serviceId"));
		$("#protocolId").val($(this).attr("protocolId"));
		switchAction($(this).val());
  });
   // 校验搜索条件里的时间选框，不允许起始时间晚于终止时间
   $("#searchForm").validate({
       rules:{
           "searchFoundStartTime": {
           },
           "searchFoundEndTime": {
               compareDate: "[name=searchFoundStartTime]"
           },
           "search_create_time_start": {
           },
           "search_create_time_end": {
               compareDate: "[name=search_create_time_start]"
           },
           "search_edit_time_start": {
           },
           "search_edit_time_end": {
               compareDate: "[name=search_edit_time_start]"
           },
           "search_audit_time_start": {
           },
           "search_audit_time_end": {
               compareDate: "[name=search_audit_time_start]"
           },
           "beginDate": {
           },
           "endDate": {
               compareDate: "[name=beginDate]"
           },
           "dobeginDate": {
           },
           "doendDate": {
               compareDate: "[name=dobeginDate]"
           },
           "editBeginDate": {
           },
           "editEndDate": {
        	   compareDate: "[name=editBeginDate]"
           },
           "capIp":{
        	   ipv4v6:true
           },
           "sIp":{
        	   ipv4v6:true
           },
           "dIp":{
        	   ipv4v6:true
           }
       },
       submitHandler: function(form){
    	   loading('onloading');
    	   form.submit();
       }
   });
  /* $("#ipCfgFrom input, #cfgForm input, #cfgFrom input").change(function(){
	  var s = $(this).val();
	  var ns = s.replace(/\t|\r|\n/mg, "");
	  $(this).val(ns);
   });*/
   $("input[name*='userRegion'][type='text']").addClass("invisibleChar");
   $("#cancel").on("click",function(){
		window.history.back();
		return false;
   });
   //去掉首尾空格
   $("input,textarea").on("blur",function(){
	   if($(this).attr("type")!="file"){
		   var val=$(this).val();
		   $(this).val(val.trim());
	   }
   });
   /*var log_total=sessionStorage.getItem("log_total");
   var log_time_start=sessionStorage.getItem("log_time_start");
   var log_time_range=sessionStorage.getItem("log_time_range");
   var log_5_minutes=$.validator.messages.log_5_minutes;
   var log_1_hour=$.validator.messages.log_1_hour;
   $('th').each(function(){
	   if(log_time_start){
		   if($(this).text()==log_5_minutes){
			   $(this).text(log_total);
		   }else if($(this).text()==log_1_hour){
			   $(this).text(log_total);
		   }
	   }else if(log_time_range){
		   if(log_time_range==300000){
			   if($(this).text()==log_total){
				   $(this).text(log_5_minutes);  
			   }else if($(this).text()==log_1_hour){
				   $(this).text(log_5_minutes);  
			   }
		   }else if(log_time_range=3600000){
			   if($(this).text()==log_total){
				   $(this).text(log_1_hour); 
			   }else if($(this).text()==log_5_minutes){
				   $(this).text(log_1_hour); 
			   }
		   }
	   }
	   
   });*/
   // 初始
   if($("#seltype").val() == "action"){
	   $("#intype").hide();// 隐藏输入框
	   if($("#actionSelect").find("option").length < 3){
		   $("#actionSelect").find("option[value='']").remove();
	   }
	   $("#isValid").selectpicker("hide");// 隐藏下拉框
   }else if($("#seltype").val() == "isValid"){
	   $("#intype").hide();// 隐藏输入框
	   $("#actionSelect").selectpicker("hide");// 隐藏下拉框
   }else{
	   $("#actionSelect").selectpicker("hide");// 隐藏下拉框
	   $("#isValid").selectpicker("hide");// 隐藏下拉框
   }
   // 改变
   $("#seltype").on("change",function() {
	   if($(this).val() == "action"){
		   if($("#actionSelect").find("option").length < 3){
			   $("#actionSelect").find("option[value='']").remove();
		   }
		   $("#intype").hide(); // 隐藏输入框
		   $("#intype").val("");// 清空input条件
		   $("#actionSelect").find("option").removeAttr("selected",false);
		   $("#actionSelect").selectpicker("refresh");
		   $("#actionSelect").selectpicker("show");// 显示下拉框
		   $("#isValid").find("option:first").attr("selected",true);// 清空action条件
		   $("#isValid").selectpicker("hide");// 隐藏下拉框
	   }else if($(this).val() == "isValid"){
		   $("#intype").hide();// 隐藏输入框
		   $("#intype").val("");// 清空input条件
		   $("#isValid").find("option").removeAttr("selected",false);
		   $("#isValid").selectpicker("refresh");
		   $("#isValid").selectpicker("show");// 显示下拉框
		   $("#actionSelect").find("option:first").attr("selected",true);// 清空action条件
		   $("#actionSelect").selectpicker("hide");// 隐藏下拉框
	   }else{
		   $("#intype").show(); // 显示输入框
		   $("#intype").val("");// 清空input条件
		   $("#actionSelect").find("option:first").attr("selected",true);// 清空action条件
		   $("#actionSelect").selectpicker("hide");// 隐藏下拉框
		   $("#isValid").find("option:first").attr("selected",true);// 清空action条件
		   $("#isValid").selectpicker("hide");// 隐藏下拉框
	   }
   });
   
   // 重置操作（增加）
   $("#resetBtn").on("click",function(){
	   // action查询条件放最上面的情况
	   if($("#seltype").find("option:first").val() == "action"){
		   $("#intype").hide();
		   $("#actionSelect").selectpicker("show");
		   return false;
	   }
	   if($("#seltype").find("option:first").val() == "isValid"){
		   $("#intype").hide();
		   $("#isValid").selectpicker("show");
		   return false;
	   }
	   if($("#seltype").val() == "action"){
		   $("#intype").show();
		   $("#intype").attr("placeholder",$.validator.messages.input+$("#seltype").find("option:first").text());
		   $("#actionSelect").selectpicker("hide");
	   }else if($("#seltype").val() == "isValid"){
		   $("#intype").show();
		   $("#intype").attr("placeholder",$.validator.messages.input+$("#seltype").find("option:first").text());
		   $("#isValid").selectpicker("hide");
	   }else{
		   $("#intype").attr("placeholder",$.validator.messages.input+$("#seltype").find("option:first").text());
	   }
	   
   });
   /*$("#classifyId,#attributeId,#lableId").on("change",function(){
	   if($(this).val()==0 || $(this).val()==''){
		   $(this).val(0);
	   }
   });*/
   //配置修改时已经展开的域上的加号隐藏
   $(".boxSolid:visible").each(function(){
	   $(this).prev("h4").find(".glyphicon-plus").addClass("hidden");
   });
   //IP域修改时默认展开,不显示show more
   if($("input[name='compileId']")&&$("input[name='compileId']").val()!=0&&($(".btn-red-hollow").parents(".boxSolid").is(":visible")||$("input[name$='cfgRegionCode']").length==1)){
		$(".port").removeClass("hidden").removeClass("disabled");
		$(".destPort").removeClass("hidden").removeClass("disabled");
		$(".protocol").removeClass("hidden").removeClass("disabled");
		$(".btn-red-hollow").addClass("hidden");
   }
   
	$("#messageBox .close").on("click",function(){
		heightDiv(1); 
	})
	//协议端口加入验证
	$("input[name$='srcPort']").addClass("protocolPort");
	$("input[name$='destPort']").addClass("protocolPort");
	$("select[name$='protocol']").addClass("protocolPort");
});
window.onload=function(){
	$("span[id^=open]").click(function(){
		//关键字
		$("div[class='content content2']").find("label:lt(1)").addClass("keyword-disinline");//page.css 关键词换行
		//增强字符串
		$("div[class='content content3']").find("label:lt(1)").addClass("keyword-disinline");//page.css 关键词换行
		//摘要
		$("div[class='content content5']").find("label:lt(1)").addClass("keyword-disinline");//page.css 关键词换行
	})
	// 配置id的搜索下拉框选择切换
	if ($("#searchForm #seltype").val() == 'compileId') {
		$("#searchForm #intype").addClass("number");
	} else {
		$("#searchForm #intype").removeClass("number");
	}
	// reset时去掉number
	$("#resetBtn").on("click", function() {
		if ($("#searchForm #seltype").val() == 'compileId') {
			$("#searchForm #intype").addClass("number");
		} else {
			$("#searchForm #intype").removeClass("number");
		}
	});
	// 切换时为compileId加上number
	$("#searchForm #seltype").on("change", function() {
		if ($("#searchForm #seltype").val() == 'compileId') {
			$("#searchForm #intype").addClass("number");
		} else {
			$("#searchForm #intype").removeClass("number");
		}
	});
	
	
	// 日志查询IP类型增加格式提示
	$("#searchForm [name='capIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='sIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='dIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='rtpDIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='rtpSIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='sipDIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	$("#searchForm [name='sipSIp']").attr("placeholder","IPv4 0.0.0.0 or IPv6 ::");
	// 动态调整日志页搜索框的样式，防止validate的错误提示导致样式错乱
	var igHeight = $("#searchForm").find(".col-md-12").find(".pull-left").find(".input-group").height();
	$("#searchForm").find(".col-md-12").find(".pull-left").find(".input-group").bind("DOMNodeInserted",function(e) {
		var newIgHeight = $("#searchForm").find(".col-md-12").eq(0).height();
		$("#searchForm").find(".col-md-12").find(".pull-left").find(".input-group").find(".input-group-btn").css("padding-bottom", (newIgHeight-igHeight)+"px");
	});
	
	setTimeout(function (){
		heightDiv(); 	
	},500);
	
	 if($('.table-responsive')){
		 var tableCont = document.querySelector('.table-responsive');
		  if(tableCont !=null && tableCont!=''){
		    function scrollHandle (e){
		        var scrollTop = this.scrollTop;
		        $('th').css('transform','translateY(' + scrollTop + 'px)');
			    //$('thead tr').css('z-index','2147482468');
		        $('thead tr').css('position','relative');
		        $('th').css('border','1px solid rgb(255, 255, 255)');
		        $('th').css('background-clip','padding-box');
		    }
		    tableCont.addEventListener('scroll',scrollHandle);
			$('#contentTable').attr("margin-top",'0px');
		  }
	 }
}

//0 非十六进制 大小不敏感 1 十六进制，大小写不敏感 2非十六进制 大小写敏感
var setIsHexBin=function(obj){
	var profix="";
	if($(obj).attr("name").indexOf(".") > -1){
		profix=$(obj).attr("name").substring(0,$(obj).attr("name").indexOf(".")+1);
	}
	if($(obj).attr("name").indexOf("isHexbin") > -1){
		var isHexbin=$(obj).val();
		if(isHexbin == 0){
			$("input[name='"+profix+"isHex'][value=0]").prop("checked",true);
			$("input[name='"+profix+"isHex'][value=1]").prop("checked",false);
			$("input[name='"+profix+"isCaseSenstive'][value=0]").prop("checked",true);
			$("input[name='"+profix+"isCaseSenstive'][value=1]").prop("checked",false);
		}else if(isHexbin == 1){
			$("input[name='"+profix+"isHex'][value=1]").prop("checked",true);
			$("input[name='"+profix+"isHex'][value=0]").prop("checked",false);
			$("input[name='"+profix+"isCaseSenstive'][value=0]").prop("checked",true);
			$("input[name='"+profix+"isCaseSenstive'][value=1]").prop("checked",false);
			$("input[name='"+profix+"isCaseSenstive'][value=1]").parent().addClass("hidden");
		}else if(isHexbin == 2){
			$("input[name='"+profix+"isHex'][value=0]").prop("checked",true);
			$("input[name='"+profix+"isHex'][value=1]").prop("checked",false);
			$("input[name='"+profix+"isCaseSenstive'][value=1]").prop("checked",true);
			$("input[name='"+profix+"isCaseSenstive'][value=0]").prop("checked",false);
		}
		if(isHexbin == 1){ //十六进制
			console.log($("input[name='"+profix+"cfgKeywords']").val());
			$("input[name='"+profix+"cfgKeywords']").addClass("hexCheck");
			$("input[name='"+profix+"domain']").addClass("hexCheck");
		}else{
			$("input[name='"+profix+"cfgKeywords']").removeClass("hexCheck");
			$("input[name='"+profix+"domain']").removeClass("hexCheck");
		}
		return;
	}
	var isHex=$("input[name='"+profix+"isHex']:checked").val();
	//如果选择十六进制，则必须发小写不敏感
	if(isHex == 1){
		$("input[name='"+profix+"isCaseSenstive'][value=0]").prop("checked",true);
		$("input[name='"+profix+"isCaseSenstive'][value=1]").prop("checked",false);
		$("input[name='"+profix+"isCaseSenstive'][value=1]").parent().addClass("hidden");
	}else{
		var configHex=$("input[name='"+profix+"configHex']").val();
		configHex=","+configHex+",";
		if(configHex.indexOf(",2,") > -1){
			$("input[name='"+profix+"isCaseSenstive'][value=1]").parent().removeClass("hidden");
		}
	}
	var isCaseSenstive=$("input[name='"+profix+"isCaseSenstive']:checked").val();
	//设置isHexbin下拉框的值
	if(isHex == 0 && isCaseSenstive==0){
		$("select[name='"+profix+"isHexbin']").find("option").prop("selected","");
		$("select[name='"+profix+"isHexbin']").find("option[value=0]").prop("selected","selected");
	}
	if(isHex == 1 && isCaseSenstive==0){
		$("select[name='"+profix+"isHexbin']").find("option").prop("selected","");
		$("select[name='"+profix+"isHexbin']").find("option[value=1]").prop("selected","selected");
	}
	if(isHex == 0 && isCaseSenstive==1){
		$("select[name='"+profix+"isHexbin']").find("option").prop("selected","");
		$("select[name='"+profix+"isHexbin']").find("option[value=2]").prop("selected","selected");
	}
	if($("select[name='"+profix+"isHexbin']").val() == ""){
		$("select[name='"+profix+"isHexbin']").find("option[value=0]").prop("selected","selected");
	}
	if(isHex == 1){ //十六进制
		console.log($("input[name='"+profix+"cfgKeywords']").val());
		$("input[name='"+profix+"cfgKeywords']").addClass("hexCheck");
		$("input[name='"+profix+"domain']").addClass("hexCheck");
	}else{
		$("input[name='"+profix+"cfgKeywords']").removeClass("hexCheck");
		$("input[name='"+profix+"domain']").removeClass("hexCheck");
	}
	
}
//与表达式时，只允许为子串匹配
var setDefaultMatchMethod=function (obj){
	var exprType = $(obj).val();
	if(exprType == 1){
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").find("option").removeAttr("selected");
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").find("option[value=0]").attr("selected","selected");
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").find("option[value!=0]").attr("disabled",true);
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").selectpicker("refresh");
	}else{
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").find("option").removeAttr("disabled");
		$(obj).parents(".row").parent(".row").find("select[name$='matchMethod']").selectpicker("refresh");
	}
}
var switchIpType=function(obj){
	var type=$(obj).val();
	var row=$(obj).parents('.row');
	var ipPattern=row.siblings().find("input[name$='ipPattern']");
	var portPattern=row.siblings().find("input[name$='portPattern']");
	var srcIp=row.siblings().find("input[name$='srcIpAddress']");
	var dstIp=row.siblings().find("input[name$='destIpAddress']");
	var srcPort=row.siblings().find("input[name$='srcPort']");
	var dstPort=row.siblings().find("input[name$='destPort']");
	if(4==type){
		if(!$(srcIp).val()){
			$(srcIp).val("0.0.0.0");
		}else if($(srcIp).val()=="::"){
			$(srcIp).val("0.0.0.0");
		}
		if(!$(dstIp).val()){
			$(dstIp).val("0.0.0.0");
		}else if($(dstIp).val()=="::"){
			$(dstIp).val("0.0.0.0");
		}
		if(!$(srcPort).val()){
			$(srcPort).val("0");
		}
		if(!$(dstPort).val()){
			$(dstPort).val("0");
		}
	}
	if(6==type){
		if(!$(srcIp).val()){
			$(srcIp).val("::");
		}else if($(srcIp).val()=="0.0.0.0"){
			$(srcIp).val("::");
		}
		if(!$(dstIp).val()){
			$(dstIp).val("::");
		}else if($(dstIp).val()=="0.0.0.0"){
			$(dstIp).val("::");
		}
		if(!$(srcPort).val()){
			$(srcPort).val("0");
		}
		if(!$(dstPort).val()){
			$(dstPort).val("0");
		}
	}
}
var switchAction=function(action){
	/********************dns reject时选择策略**********************/
	if(action == 16){ //reject
		$(".policy").find("input,select,div,button").each(function(){
			$(this).removeAttr("disabled");
			$(this).removeClass("hidden");
			$(this).removeClass("disabled");
		})
	}else{
		$(".policy").find("input,select,div,button").each(function(){
			$(this).attr("disabled","disabled");
			$(this).addClass("hidden");
		})
	}
	
	/*************************action切换时，隐藏白名单和drop的是否记录日志*****************************/
	//drop whitelist
/*	if(action == 32 || action==128 || action==96){
		$(".doLog").addClass("hidden");
		$("input[name=doLog][value=0]").prop("checked",true);
		$(".interceptDoLog").find("input[name=doLog][value=0]").prop("checked",true);
	}else{
		//如果表单非修改时，选中默认值
		if(!$("input[name='compileId']").val()){
			$(".doLog").removeClass("hidden");
			$("input[name=doLog][value=1]").prop("checked",true);
			$(".interceptDoLog").find("input[name=doLog][value=1]").prop("checked",true);
		}
	}
	if(action == 1){//监测时隐藏doLog中不记录日志的选项
		if($("input[name=doLog][value=0]")){
			$("input[name=doLog][value=0]").parent(".radio-inline").addClass("hidden");
			if($("input[name=doLog][value=0]").is(":checked")){
				$("input[name=doLog][value=0]").removeAttr("checked");
				$("input[name=doLog][value=1]").prop("checked",true);
				$(".interceptDoLog").find("input[name=doLog][value=1]").prop("checked",true);
			}
		}
	}else{
		$("input[name=doLog][value=0]").parent(".radio-inline").removeClass("hidden");
	}
	if(action !=128 && action !=32 &&action !=96){
		$(".doLog").removeClass("hidden");
	}*/
}
//ipType、ipPattern、portPattern选项变化时调用此方法，添加默认值
var switchIpInfo=function(obj){
	var ipType="";
	var ipPattern="";
	var portPattern="";
	
	var row=$(obj).parents('.row').parent('.row');
	ipType=row.find("select[name$='ipType']").val();
	ipPattern=row.find("select[name$='ipPattern']").val();
	portPattern=row.find("select[name$='portPattern']").val();
	var srcIp=row.find("input[name$='srcIpAddress']");
	var destIp=row.find("input[name$='destIpAddress']");
	var srcPort=row.find("input[name$='srcPort']");
	var destPort=row.find("input[name$='destPort']");
	
	var srcIpV4Default=new Array();
	srcIpV4Default[0]="0.0.0.0/32"; //subnet
	srcIpV4Default[1]="0.0.0.0-0.0.0.0";//ip_range
	srcIpV4Default[2]="0.0.0.0"; //ip
	
	var destIpV4Default=new Array();
	destIpV4Default[0]="0.0.0.0/32"; //subnet
	destIpV4Default[1]="0.0.0.0-0.0.0.0";//ip_range
	destIpV4Default[2]="0.0.0.0"; //ip
	
	
	var srcIpV6Default=new Array();
	srcIpV6Default[0]="::/128"; //subnet
	srcIpV6Default[1]="::-::";//ip_range
	srcIpV6Default[2]="::"; //ip
	
	var destIpV6Default=new Array();
	destIpV6Default[0]="::/128"; //subnet
	destIpV6Default[1]="::-::";//ip_range
	destIpV6Default[2]="::"; //ip
	
	var portDefault=new Array();
	portDefault[0]="0";//port
	portDefault[1]="0/65535";//port_mask
	
	//IPv4设置默认值
	if(4==ipType){
		if(!$(srcIp).val()){
			if(ipPattern==1){
				$(srcIp).val(srcIpV4Default[0]);
			}else if(ipPattern==2){
				$(srcIp).val(srcIpV4Default[1]);
			}else if(ipPattern==3){
				$(srcIp).val(srcIpV4Default[2]);
			}else{
				$(srcIp).val(srcIpV4Default[2]);
			}
		}else{
			if($.inArray($(srcIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV4Default) > -1
					|| $.inArray($(srcIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV6Default) > -1){ //是ipV4的默认值其中一个
				if(ipPattern==1){
					$(srcIp).val(srcIpV4Default[0]);
				}else if(ipPattern==2){
					$(srcIp).val(srcIpV4Default[1]);
				}else if(ipPattern==3){
					$(srcIp).val(srcIpV4Default[2]);
				}else{
					$(srcIp).val(srcIpV4Default[2]);
				}
			}
		}
		if(!$(destIp).val()){
			if(ipPattern==1){
				$(destIp).val(destIpV4Default[0]);
			}else if(ipPattern==2){
				$(destIp).val(destIpV4Default[1]);
			}else if(ipPattern==3){
				$(destIp).val(destIpV4Default[2]);
			}else{
				$(destIp).val(destIpV4Default[2]);
			}
		}else{
			if($.inArray($(destIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(destIp).val(),destIpV4Default) > -1
					|| $.inArray($(destIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(destIp).val(),destIpV6Default) > -1){
				if(ipPattern==1){
					$(destIp).val(destIpV4Default[0]);
				}else if(ipPattern==2){
					$(destIp).val(destIpV4Default[1]);
				}else if(ipPattern==3){
					$(destIp).val(destIpV4Default[2]);
				}else{
					$(destIp).val(destIpV4Default[2]);
				}
			}
		}
	}
	if(6==ipType){
		if(!$(srcIp).val()){
			if(ipPattern==1){
				$(srcIp).val(srcIpV6Default[0]);
			}else if(ipPattern==2){
				$(srcIp).val(srcIpV6Default[1]);
			}else if(ipPattern==3){
				$(srcIp).val(srcIpV6Default[2]);
			}else{
				$(srcIp).val(srcIpV6Default[2]);
			}
		}else{
			if($.inArray($(srcIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV4Default) > -1
					|| $.inArray($(srcIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV6Default) > -1){ //是ipV6的默认值其中一个
				if(ipPattern==1){
					$(srcIp).val(srcIpV6Default[0]);
				}else if(ipPattern==2){
					$(srcIp).val(srcIpV6Default[1]);
				}else if(ipPattern==3){
					$(srcIp).val(srcIpV6Default[2]);
				}else{
					$(srcIp).val(srcIpV6Default[2]);
				}
			}
		}
		if(!$(destIp).val()){
			if(ipPattern==1){
				$(destIp).val(destIpV6Default[0]);
			}else if(ipPattern==2){
				$(destIp).val(destIpV6Default[1]);
			}else if(ipPattern==3){
				$(destIp).val(destIpV6Default[2]);
			}else{
				$(destIp).val(srcIpV6Default[2]);
			}
		}else{
			if($.inArray($(destIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(destIp).val(),destIpV4Default) > -1
					|| $.inArray($(destIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(destIp).val(),destIpV6Default) > -1){ //是ipV6的默认值其中一个
				if(ipPattern==1){
					$(destIp).val(destIpV6Default[0]);
				}else if(ipPattern==2){
					$(destIp).val(destIpV6Default[1]);
				}else if(ipPattern==3){
					$(destIp).val(destIpV6Default[2]);
				}else{
					$(destIp).val(srcIpV6Default[2]);
				}
			}
		}
	}
	if(46==ipType){
		if(!$(srcIp).val()){
			if(ipPattern==1){
				$(srcIp).val(srcIpV4Default[0]);
			}else if(ipPattern==2){
				$(srcIp).val(srcIpV4Default[1]);
			}else if(ipPattern==3){
				$(srcIp).val(srcIpV4Default[2]);
			}else{
				$(srcIp).val(srcIpV4Default[2]);
			}
		}else{
			if($.inArray($(srcIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV4Default) > -1
					|| $.inArray($(srcIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV6Default) > -1){ 
				if(ipPattern==1){
					$(srcIp).val(srcIpV4Default[0]);
				}else if(ipPattern==2){
					$(srcIp).val(srcIpV4Default[1]);
				}else if(ipPattern==3){
					$(srcIp).val(srcIpV4Default[2]);
				}else {
					$(srcIp).val(srcIpV4Default[2]);
				}
			}
		}
		if(!$(destIp).val()){
			if(ipPattern==1){
				$(destIp).val(destIpV6Default[0]);
			}else if(ipPattern==2){
				$(destIp).val(destIpV6Default[1]);
			}else if(ipPattern==3){
				$(destIp).val(destIpV6Default[2]);
			}else{
				$(destIp).val(destIpV6Default[2]);
			}
		}else{
			if($.inArray($(destIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(destIp).val(),destIpV4Default) > -1
					|| $.inArray($(destIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(destIp).val(),destIpV6Default) > -1){ 
				if(ipPattern==1){
					$(destIp).val(destIpV6Default[0]);
				}else if(ipPattern==2){
					$(destIp).val(destIpV6Default[1]);
				}else if(ipPattern==3){
					$(destIp).val(destIpV6Default[2]);
				}else{
					$(destIp).val(destIpV6Default[2]);
				}
			}
		}
	}
	if(64==ipType){
		if(!$(srcIp).val()){
			if(ipPattern==1){
				$(srcIp).val(srcIpV6Default[0]);
			}else if(ipPattern==2){
				$(srcIp).val(srcIpV6Default[1]);
			}else if(ipPattern==3){
				$(srcIp).val(srcIpV6Default[2]);
			}else{
				$(srcIp).val(srcIpV6Default[2]);
			}
		}else{
			if($.inArray($(srcIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV4Default) > -1
					|| $.inArray($(srcIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(srcIp).val(),destIpV6Default) > -1){ 
				if(ipPattern==1){
					$(srcIp).val(srcIpV6Default[0]);
				}else if(ipPattern==2){
					$(srcIp).val(srcIpV6Default[1]);
				}else if(ipPattern==3){
					$(srcIp).val(srcIpV6Default[2]);
				}else{
					$(srcIp).val(srcIpV6Default[2]);
				}
			}
		}
		if(!$(destIp).val()){
			if(ipPattern==1){
				$(destIp).val(srcIpV4Default[0]);
			}else if(ipPattern==2){
				$(destIp).val(srcIpV4Default[1]);
			}else if(ipPattern==3){
				$(destIp).val(srcIpV4Default[2]);
			}else{
				$(destIp).val(srcIpV4Default[2]);
			}
		}else{
			if($.inArray($(destIp).val(),srcIpV4Default) > -1 
					|| $.inArray($(destIp).val(),destIpV4Default) > -1
					|| $.inArray($(destIp).val(),srcIpV6Default) > -1 
					|| $.inArray($(destIp).val(),destIpV6Default) > -1){ 
				if(ipPattern==1){
					$(destIp).val(destIpV4Default[0]);
				}else if(ipPattern==2){
					$(destIp).val(destIpV4Default[1]);
				}else if(ipPattern==3){
					$(destIp).val(destIpV4Default[2]);
				}else{
					$(destIp).val(destIpV4Default[2]);
				}
			}
		}
	}
	if(10==ipType){
		if(!$(srcIp).val()){
			if(ipPattern==1){
				$(srcIp).val(srcIpV4Default[0]);
			}else if(ipPattern==2){
				$(srcIp).val(srcIpV4Default[1]);
			}else if(ipPattern==3){
				$(srcIp).val(srcIpV4Default[2]);
			}else{
				$(srcIp).val(srcIpV4Default[2]);
			}
		}
		if(!$(destIp).val()){
			if(ipPattern==1){
				$(destIp).val(destIpV4Default[0]);
			}else if(ipPattern==2){
				$(destIp).val(destIpV4Default[1]);
			}else if(ipPattern==3){
				$(destIp).val(destIpV4Default[2]);
			}else{
				$(destIp).val(destIpV4Default[2]);
			}
		}
	}
	//设置源端口默认值
	if(!$(srcPort).val()){
		if(portPattern==1){
			$(srcPort).val(portDefault[0]);
		}else if(portPattern==2){
			$(srcPort).val(portDefault[1]);
		}else{
			$(srcPort).val(portDefault[0]);
		}
	}else{
		if($.inArray($(srcPort).val(),portDefault) > -1){ //是ipV4的默认值其中一个
			if(portPattern==1){
				$(srcPort).val(portDefault[0]);
			}else if(portPattern==2){
				$(srcPort).val(portDefault[1]);
			}else{
				$(srcPort).val(portDefault[0]);
			}
		}
	}
	//设置目的端口默认值
	if(!$(destPort).val()){
		if(portPattern==1){
			$(destPort).val(portDefault[0]);
		}else if(portPattern==2){
			$(destPort).val(portDefault[1]);
		}else{
			$(destPort).val(portDefault[0]);
		}
	}else{
		if($.inArray($(destPort).val(),portDefault) > -1){ //是port的默认值其中一个
			if(portPattern==1){
				$(destPort).val(portDefault[0]);
			}else if(portPattern==2){
				$(destPort).val(portDefault[1]);
			}else{
				$(destPort).val(portDefault[0]);
			}
		}
	}
}
var areaControlInit=function(){
	if($("input[name='isAreaEffective']:checked").val()==1){
		$(".areaType").removeClass("hidden");
		if($("input[name='areaEffectiveIds']").val()){
			$(".areaType").find("[value='1']").prop("checked",true);
			$("#areaIsp").removeClass("hidden");
			$("#areaIp").addClass("hidden");
		}else{
			$(".areaType").find("[value='0']").prop("checked",true);
			$("#areaIp").removeClass("hidden");
			$("#areaIsp").addClass("hidden");
		} 
	}else{
		$("input[name='areaEffectiveIds']").val("");
		$("input[name='isAreaEffective']").find("[value='0']").prop("checked",true);
	}
}
//查询
var page=function(n,s){
	if($("#intype").val() != null && $("#intype").val() != ""){
		$("#intype").attr("name",$("#seltype").val());
	}
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

//show more 添加更多属性
var more=function(obj){
	var clickTimes=$(obj).data("click-times");
	if(clickTimes==0){
		$(".port").removeClass("hidden").removeClass("disabled");
		$(obj).data("click-times",clickTimes+1);
	}
	if(clickTimes==1){
		$(".destPort").removeClass("hidden").removeClass("disabled");
		$(obj).data("click-times",clickTimes+1);
	}
	if(clickTimes==2){
		$(".protocol").removeClass("hidden").removeClass("disabled");
		$(obj).data("click-times",clickTimes+1);
	}
	var rows=$(obj).parent(".row").siblings(".row:hidden");
	if(rows.size()==0){
		$(obj).addClass("hidden");
	}
}

var viewAreaInfo=function(path,areaEffectiveIds,compileId){
	$.ajax({
		type:'post',
		url:path+'/ntc/av/area/ajaxAreaEffictiveInfo',
		data:{"areaEffectiveIds":areaEffectiveIds,"compileId":compileId},
		dataType:'json',
		async:false,
		success:function(data,textStatus){
			if(textStatus=="success"){
				var html = "";
				var title="";
				if(data.areaIsps.length > 0){
					title=$.validator.messages.area;//+" "+$.validator.messages.isp;
					html+="<table class='table table-striped table-bordered table-condensed' style='margin-left: 10px; width: 96%;margin-top: 15px;'>";
      			  	html+="<thead>";
      			  		html+="<th style='width:50%;padding-right:0px'>"+$.validator.messages.area+"</th>" +
      			  			  "<th style='width:50%;padding-right:0px'>"+$.validator.messages.isp+"</th>";
      			   	html+="</thead>";
      			   	html+="<tbody>";
      			   	for(i=0;i<data.areaIsps.length;i++){
      			   		html+="<tr>";
      			   		html+="<td>"+data.areaIsps[i].areaName;
      					html+="</td>";
      					html+="<td>"+data.areaIsps[i].ispName;
      					html+="</td>";
      			   		html+="</tr>";
      			   	}
      			   	html+="</tbody>";
      			 	html+="</table>";
				}
				if(data.areaIps.length > 0){
					title=$.validator.messages.area+" ip";
					html+="<table class='table table-striped table-bordered table-condensed' style='margin-left: 10px; width: 96%;margin-top: 15px;'>";
					html+="<thead>";
					html+="<th style='width:20%;padding-right:0px'>"+$.validator.messages.ip_type+"</th>" +
						  "<th style='width:20%;padding-right:0px'>"+$.validator.messages.ip_pattern+"</th>" +
						  "<th style='width:60%;padding-right:0px'>"+$.validator.messages.client_ip+"</th>";
					html+="</thead>";
					html+="<tbody>";
					for(i=0;i<data.areaIps.length;i++){
						html+="<tr>";
						html+="<td>";
						if(data.areaIps[i].ipType==4){
							html+=$.validator.messages.ipv4;
						}
						if(data.areaIps[i].ipType==6){
							html+=$.validator.messages.ipv6;
						}
						if(data.areaIps[i].ipType==46){
							html+=$.validator.messages.over4;
						}
						if(data.areaIps[i].ipType==64){
							html+=$.validator.messages.over6;
						}
						if(data.areaIps[i].ipType==10){
							html+=$.validator.messages.all;
						}
						html+="</td>";
						html+="<td>";
						if(data.areaIps[i].ipPattern==1){
							html+=$.validator.messages.ip_subnet;
						}
						if(data.areaIps[i].ipPattern==2){
							html+=$.validator.messages.ip_range;
						}
						if(data.areaIps[i].ipPattern==3){
							html+="IP";
						}
						html+="</td>";
						html+="<td>"+data.areaIps[i].srcIpAddress;
						html+="</td>";
						html+="</tr>";
					}
					html+="</tbody>";
					html+="</table>";
				}
				top.$.jBox(html,{width: $(document).width()*0.4,height: 400,title:title, buttons:false});
			}
				 
		}
  
  });
}

//删除区域IP
function delAreaIp(obj){ 
	var thisObj=$(obj);
	thisObj.parents(".container-fluid").remove();
	var len=$("#areaIp").find(".container-fluid:visible").size();
	//调整角标
	if(len>0){
		len--;
		$("#areaIp").find(".container-fluid:visible").each(function(){
			$(this).find("[name^='areaCfg']").each(function(){
				var name=$(this).attr("name");
				name=name.substring(name.lastIndexOf(".")+1,name.length);
				$(this).attr("name","areaCfg["+len+"]."+name);
				$(this).parents(".form-group").find("div[for^='areaCfg']").attr("for","areaCfg["+len+"]."+name);
			});
			len--;
		});
	}
}
//删除区域ISP
function delAreaIsp(obj){ 
	var thisObj=$(obj);
	thisObj.parents(".container-fluid").remove();
	var len=$("#areaIsp").find(".container-fluid:visible").size();
	if(len>0){
		len--;
		$("#areaIsp").find(".container-fluid:visible").each(function(){
			$(this).find("[name^='areaIsp']").each(function(){
				var name=$(this).attr("name");
				name=name.substring(name.lastIndexOf(".")+1,name.length);
				$(this).attr("name","areaIsp["+len+"]."+name);
				$(this).parents(".form-group").find("div[for^='areaIsp']").attr("for","areaIsp["+len+"]."+name);
			});
			len--;
		});
	}
}
//切换配置列表中显示子配置的选项卡样式
function switchSubCfgTabInfo(flag,index){
	$("div[name='subCfg"+index+"']").hide();
	$("div[id='"+flag+"Info"+index+"']").show();
	$("div[name='tabTitle"+index+"']").removeClass("badge-info");
	$("div[id='"+flag+"Title"+index+"']").addClass("badge-info");
	$("i[name='tabFlag"+index+"']").addClass("fa-angle-double-up");
	$("i[name='tabFlag"+index+"']").removeClass("fa-angle-double-down");
	$("i[id='"+flag+index+"']").addClass("fa-angle-double-down");
	$("i[id='"+flag+index+"']").removeClass("fa-angle-double-up");
}
function isLicit(str,regStr) {//判断是否为合格字符 //s 包括空格回车等特殊字符
	regStr = App.isEmpty(regStr)?"^[_0-9a-zA-Z\u4e00-\u9fa5\.@&]*$":regStr;
	var re = new RegExp(regStr);
    //var re = "^[_0-9a-zA-Z\.@]*$";
    return re.test(str);
}
function exprTypeChecked(objNamePrefix,size,options){
	if(size>1) {
		// 与表达式
		$("input:radio[name='"+objNamePrefix+"exprType'][value=1]").prop("checked",true);
		$("select[name='"+objNamePrefix+"matchMethod']").find("option[value!=0]").remove();
		$("select[name='"+objNamePrefix+"matchMethod']").selectpicker("refresh");
	}else {
		// 无表达式
		$("input:radio[name='"+objNamePrefix+"exprType'][value=0]").prop("checked",true);
		if($("select[name='"+objNamePrefix+"matchMethod']").find("option").length == 1){
			$("select[name='"+objNamePrefix+"matchMethod']").append(options.clone());
		}
		$("select[name='"+objNamePrefix+"matchMethod']").find("option").removeAttr("selected",false);
		$("select[name='"+objNamePrefix+"matchMethod']").find("option[value=0]").attr("selected",true);
		$("select[name='"+objNamePrefix+"matchMethod']").selectpicker("refresh");
		
	}
}
function validateDataIsLicit(){
	var fdfz = $.trim($("#tags_1").val()).replace(/,/g,"");
    return isLicit(fdfz);
}
//导入文件提示框
var toImport=function (cfgRegionCode,cfgType){
	$("#cfgRegionCode").val(cfgRegionCode);
	$("#cfgType").val(cfgType);
	if(cfgType){//针对要下发到不同cfgType的处理
		$('.radio-inline').children('span').each(function(){
			if($(this).hasClass(cfgType)){
				$(this).find("input").removeAttr("disabled");
				if($('.radio-inline').children('span').size()==1){
					$(this).find("input").click();
				}
			}else{
				$(this).find("input").attr("disabled",true).attr("checked",false);
			}
		})
	}
	$("#import_modal").modal({
		backdrop:"static",
		keyboard:false,
		show:true
	});
}
//下载模板
var downLoadXLS=function(){
	var pathName=window.document.location.pathname.substring(0,window.document.location.pathname.lastIndexOf("/"));
	window.location =pathName+"/import/template?functionId="+$("#functionId").val()+"&cfgRegionCode="+$("#cfgRegionCode").val();
}
//下载模板
var openSelct=function(obj){
	$(obj).parent().parent().find(".tree-multiselect").removeClass("hidden");
}
var validateTagInputKeyword=function(){
	var flag=true;
	$(".boxSolid:visible").find("input[name$='cfgKeywords']").each(function(){
		if($(this).val()==''){
			$(this).parents(".form-group").find(
					"div[for='"
							+ $(this).attr("name")
							+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$("#keywordError").text()+"</label>");
			return false;
		}
	});
	if(flag){
		//keywords非空校验完成校验二进制字符串
		$(".boxSolid:visible").find("select[name$='isHexbin']").each(function(){
			var isHexbin=$(this).val();
			var configMultiKeywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","configMultiKeywords")+"']").val();
			if(isHexbin == 1 && configMultiKeywords==1){ //十六进制 
				var keywords=$("input[name$='"+$(this).attr("name").replace("isHexbin","cfgKeywords")+"']").val();
				keywords=keywords.replace("***and***","")	
			if(keywords != ''){
					if(!(/^([0-9|a-f|A-F]*)$/.test(keywords))){
						$(this).parents(".boxSolid").find(
								"div[for='"
										+ $(this).attr("name").replace("isHexbin","cfgKeywords")
										+ "']").html("<label id=\"cfgKeywordsError\" class=\"error\">"+$.validator.messages.hexCheck+"</label>");
						flag = false;
					}
				}					
			}
		});
	}
	return flag;
}
var validateInvisibleCharTag=function(){
	var hasInvisibleCharTags=[];
	var reg = new RegExp(/\t|\r|\n/);
	$(".tagsinput").find(".tag").each(function(){
		var text=$(this).children("span").text();
		if (text.match(reg)) {
			hasInvisibleCharTags.push("'"+text.trim()+"'");
		}
	});
	if(hasInvisibleCharTags.length==1){
		$(".tagsinput").parents(".col-md-6").next("div").html("<label class='error'>"+$.validator.messages.hasInvisibleChar.replace("{0}",hasInvisibleCharTags.join(","))+"</label>");
		return false;
	}else if(hasInvisibleCharTags.length>=1){
		$(".tagsinput").parents(".col-md-6").next("div").html("<label class='error'>"+$.validator.messages.haveInvisibleChar.replace("{0}",hasInvisibleCharTags.join(","))+"</label>");
		return false;
	}
	return true;
}
var setHexCaseSenstive=function(){
	$("input[name$='configHex']").each(function(){
		var configHex=$(this).val();
		var configNamePrefix=$(this).attr("name").split("configHex")[0];	;
		if(configHex != ''){
			$(this).parent().find("input[name='"+configNamePrefix+"isHex'][value=1]").parent().addClass("hidden");
			$(this).parent().find("input[name='"+configNamePrefix+"isHex'][value=0]").parent().addClass("hidden");
			$(this).parent().find("input[name='"+configNamePrefix+"isCaseSenstive'][value=0]").parent().addClass("hidden");
			$(this).parent().find("input[name='"+configNamePrefix+"isCaseSenstive'][value=1]").parent().addClass("hidden");
			if(configHex.indexOf("0")>-1){//非十六进制大小写不敏感
				$(this).parent().find("input[name='"+configNamePrefix+"isHex'][value=0]").parent().removeClass("hidden");
				$(this).parent().find("input[name='"+configNamePrefix+"isCaseSenstive'][value=0]").parent().removeClass("hidden");
			}
			if(configHex.indexOf("1")>-1){//十六进制大小写不敏感
				$(this).parent().find("input[name='"+configNamePrefix+"isHex'][value=1]").parent().removeClass("hidden");
				$(this).parent().find("input[name='"+configNamePrefix+"isCaseSenstive'][value=0]").parent().removeClass("hidden");
			}
			if(configHex.indexOf("2")>-1){//非十六进制大小写敏感
				$(this).parent().find("input[name='"+configNamePrefix+"isHex'][value=0]").parent().removeClass("hidden");
				$(this).parent().find("input[name='"+configNamePrefix+"isCaseSenstive'][value=1]").parent().removeClass("hidden");
			}
		}
		
	});
}
var validateEffectiveRuleLimit=function(path,serviceId){
	var flag = true;
	$.ajax({
		url: path+"/configure/statistics/getEffectiveCfgNum",
		type: "POST",
		data:{"serviceId":serviceId},
		async: false, //表单提交前验证是否可提交，所以此参数必须为false
		success: function(data) {
			if(data.status==0){
				top.$.jBox.closeTip();
				alertx(data.msg);
    			flag=false;
			}
		},
		error:function(jqXHR, textStatus, errorThrown){ 
			top.$.jBox.closeTip();
			alertx(errorThrown);
			flag=false;
		} 
	});
	return flag;
}
//打开帮助文档页面
function openHelp(url){
	top.$.jBox("iframe:"+url,{ 
		width: $(document).width()*0.9,
		height:$(document).height()*0.8,
		top: '10%',
	    draggable:false,
		title: "online help", buttons:{ "close":true} 
	});
}
//查询系统当前是否在进行配置同步，如果正在同步，界面不允许配置操作
function getConfigSyncStatus(){
	var currentStatus = getSyncStatus();
	//3成功，-1服务端同步失败，-2界面端同步失败
	if(currentStatus!=3 && currentStatus!=-1 && currentStatus!=-2){
		$.jBox.defaults = {
		        id:'syncStatusTip',
		        zIndex: 9999
	    };
		var tipId = top.$.jBox.tip($.validator.messages.config_sync_tip,'loading',{opacity:0.5,persistent:true});
		var t1 = setInterval(function() {
			currentStatus = getSyncStatus();
			if(currentStatus==3 || currentStatus==-1 || currentStatus==-2){//3表示同步完成，-1表示服务端同步失败，-2表示界面端同步失败
				window.clearInterval(t1); 
				top.$.jBox.closeTip('syncStatusTip');
			}
		},5000);
	}
}
function getSyncStatus(){
	//获取当前是否有配置同步任务
	var currentStatus = 3;
	var pathName = window.document.location.pathname.substring(0,window.document.location.pathname.lastIndexOf("/nis")+4);
	$.ajax({
		type:'get',
		async:false,
		url:pathName+'/config/synchronization/getStatus',
		dataType:"json",
		success:function(data){
			currentStatus = data.status;
		}
	});
	return currentStatus;
}
function heightDiv(type){
	var hei=document.documentElement.clientHeight;
	hei=hei-91;
	var rowheigth=0;
	if($('.row')){
		 rowheigth=$('.row').height();
		 hei=hei-rowheigth;
	}
	if(type != 1){
	if($("#messageBox")){
	  var messageH=$("#messageBox").height();
	     if(messageH > 0){
	    	 hei=hei-messageH-52;
	     }
	  }
	}
	if($('.table-responsive')){
		$('.table-responsive').css({'height':hei+'px'});
	}
}
window.onresize=heightDiv;

