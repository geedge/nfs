<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html>
    <!--<![endif]-->
    <!-- BEGIN HEAD -->

    <head>
        <meta charset="utf-8" />
        <title>Login</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/pages/img/logo.ico" />
        <!-- BEGIN GLOBAL MANDATORY STYLES -->
        <link href="${pageContext.request.contextPath}/static/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/static/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
        <!-- END GLOBAL MANDATORY STYLES -->
        <!-- BEGIN THEME GLOBAL STYLES -->
        <link href="${pageContext.request.contextPath}/static/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="${pageContext.request.contextPath}/static/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
        <!-- END THEME GLOBAL STYLES -->
        <!-- BEGIN PAGE LEVEL STYLES -->
        <link href="${pageContext.request.contextPath}/static/pages/css/login.css" rel="stylesheet" type="text/css" />
        <!-- END PAGE LEVEL STYLES -->
        <!-- BEGIN THEME LAYOUT STYLES -->
        <!-- END THEME LAYOUT STYLES -->
        <link rel="shortcut icon" href="favicon.ico" /> </head>
    <!-- END HEAD -->

    <body class="">
        <div class="page-login">
            <div class="page-logo">
        		<img src="${pageContext.request.contextPath}/static/pages/img/logo1.png" alt="logo" />
            </div>
            <div class="page-body">
                <div class="login-head"> <spring:message code='login'/> </div>
                <div class="login-body">
                    <div class="pull-left login-avatar-block">
                        <img src="${pageContext.request.contextPath}/static/pages/img/login-02.png" class="login-avatar"> </div>
                    <form id="loginForm" class="login-form pull-left" action="${pageContext.request.contextPath }/login" method="post">
						<div class="form-group">
                            <input id="username" name="username" type="text" class="form-control placeholder-no-fix " value="${username}" placeholder="<spring:message code='fill_loginName'/>"/>
                        </div>
                        <div class="form-group">
                        	<input type="password"  class="form-control placeholder-no-fix "  id="password" name="password" placeholder="<spring:message code='fill_loginPassWord'/>"/>
                        </div>
                        
                      <%--  <c:if test="${not empty isValidateCodeLogin or isValidateCodeLogin==true}">
				             <li class="yzm ${ (empty isValidateCodeLogin or isValidateCodeLogin==false) ?'hide':''  }">
					   	 		<span><input id="captcha" name="captcha" class="required" type="text" placeholder="验证码"/></span><cite><img alt="未获取验证码" src="${pageContext.request.contextPath}/captcha-image" id="captacha_image" class="mid"/></cite> 
					    	</li>
				             
						 </c:if>
					    
					    
					    <li><input  type="submit" class="loginbtn" value="登录"/>
					    	<label><input  id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} type="checkbox" />记住我（公共场所慎用）</label>
					    </li> --%>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn red uppercase"><spring:message code='login'/></button>
                        </div>
                    </form>
                </div>
                <div class="login-bottom">
                    <!-- <a href="">Not Amanda Smith?</a> -->
                </div>
                

            </div>
            
            <p>
				<div id="messageBox" class="alert alert-danger ${empty message ? 'hide' : ''}">
					<label id="loginError" class="error"><spring:message code="${message}"></spring:message></label>
				</div>
			</p>
				
		
             
        </div>
        
        		
         <div class="page-footer-custom"> 
         		CEIEC All Rights Reserved，CEIEC &copy;
           		<%-- Copyright &copy; 2015-${fns:getStringProperty('copyrightYear','2015')} <a href="${pageContext.request.contextPath}">${fns:getStringProperty('productName','NIS')}</a> - Powered By <a href="${pageContext.request.contextPath}" target="_blank">NIS</a> ${fns:getStringProperty('version','1.0.0')} --%>
         </div>
         
         
        <!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/static/global/plugins/respond.min.js"></script>
<script src="${pageContext.request.contextPath}/static/global/plugins/excanvas.min.js"></script> 
<![endif]-->
        <!-- BEGIN CORE PLUGINS -->
        <script src="${pageContext.request.contextPath}/static/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/js.cookie.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/static/global/plugins/jquery-validation/1.11.0/jquery.validate.method.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/static/global/scripts/jeesite.js" type="text/javascript"></script>
        <!-- END CORE PLUGINS -->
        <!-- BEGIN THEME GLOBAL SCRIPTS -->
        <script src="${pageContext.request.contextPath}/static/global/scripts/app.min.js" type="text/javascript"></script>
        <!-- END THEME GLOBAL SCRIPTS -->
        <!-- BEGIN THEME LAYOUT SCRIPTS -->
        <!-- END THEME LAYOUT SCRIPTS -->
        
        <script type="text/javascript">
            $(document).ready(function(){
      
   
            	$("#loginForm").validate({
    				rules: {
    					username: { required: true},
                        password: { required: true},
                        captcha: {remote: "${pageContext.request.contextPath}/validateCode"}
    				},
    				messages: {
    					username: {required: '<spring:message code="fill_loginName"/>...'},password: {required: '<spring:message code="fill_loginPassWord"/>...'},
    					captcha: {remote: '<spring:message code="captcha_error"/>...', required: '<spring:message code="enter_captcha"/>...'}
    				},
    				errorLabelContainer: "#messageBox",
    				errorPlacement: function(error, element) {
    					error.appendTo($("#loginError").parent());
    				} 
    			});
                
             // 如果在框架或在对话框中，则弹出提示并跳转到首页
        		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
        			top.$.jBox.confirm("<spring:message code='login_timeout'/>","<spring:message code='info'/>",function(v,h,f){
    				
    				if(v=="ok"){
    					top.location = "${pageContext.request.contextPath }";
    				}else{
    					top.location = "${pageContext.request.contextPath }";
        				}
    			},{buttonsFocus:1});
    			top.$('.jbox-body .jbox-icon').css('top','55px');
        		}
                
                
            });
        </script>
    </body>

</html>