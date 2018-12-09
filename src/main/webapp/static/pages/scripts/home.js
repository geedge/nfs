var home = function () {
	
	var decorator_init = function() {
		
		//解决sitemesh3中body属性无法加载问题
		if($("#metaId")[0] && $("#metaId").val() != "")  
	        $("body").attr("id",$("#metaId").val());  
        if($("#metaClass")[0] && $("#metaClass").val() != "")  
            $("body").attr("class",$("#metaClass").val());  
 
	}
	
	
	//菜单选择触发事件
	var menu_action_init = function() {

		//顶部导航顶级菜单事件
		$(".mega-menu li a").click(function(e){
			
			var $this = $(this);
			var next_ul = $this.next("ul");
			var prev_ul = $this.parents("ul");
			var menuId = "#menu_" + $this.attr("id");
			
			if(next_ul.length) { 
				return false; 
			}
			
			if(prev_ul.hasClass("dropdown-menu")) {
				$this = prev_ul;
			}
			
			$(".mega-menu li.active").removeClass("active")
			$this.parent("li").addClass("active");
			
			if ($(menuId).length){
				$(".page-sidebar>.accordion").addClass("hide");
				$(menuId).removeClass("hide");
			}
			
			//默认点击左侧第一个有链接的菜单，显示其功能界面
			/*$(menuId).find("li").removeClass("open active");
			$(menuId).find("li > a > .arrow").removeClass("open");*/
			$(menuId).find('li > .sub-menu').slideUp();
			var $firstLi1 = $(menuId).find("li > a[onclick]:first");
			$firstLi1.trigger("click");
			$firstLi1.parent().parent().slideDown();
			
			//点击顶部菜单，默认展开左侧菜单，如果左侧菜单已有被选中的则忽视
			/*var left_menu_lis = $(menuId).find("li");
			if(!(left_menu_lis.hasClass("active") ||
					left_menu_lis.hasClass("open"))) {
				 var $li1 = $(menuId).find("li:first");
				 var $li2 = $(menuId).find("li:first").find("li:first");
				
				 var hasSubMenu1 =  $li1.children().hasClass('sub-menu');
				 if(hasSubMenu1) {
					 $li1.addClass('open');
					 $li1.find('> a > .arrow').addClass('open');
					 $li1.find('> .sub-menu').slideDown();

					 var hasSubMenu2 =  $li2.children().hasClass('sub-menu');
					 if(hasSubMenu2) {
						 $li2.addClass('open');
						 $li2.find('> a > .arrow').addClass('open');
						 $li2.find('> .sub-menu').slideDown();
						 
					 }
				 }
				 
			}*/
			

		})	 

		
	} 
	
	
    return {
        init: function () {
        	decorator_init();
        	menu_action_init();
        },
        setIframeHeight: function (iframe) {
        	
        	if (iframe) {
        		var iframeWin = iframe.contentWindow;
        		if (iframeWin.document.body) {
        			iframe.height = iframeWin.document.body.clientHeight;
        		}
        	}

        }

    };

}();



jQuery(document).ready(function() {
	
	home.init();

});