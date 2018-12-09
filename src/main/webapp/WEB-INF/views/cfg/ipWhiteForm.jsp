<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>来函信息</title>

</head>
<body>

<div class="page-content">

	<!-- <h3 class="page-title">
		Managed Datatables <small>managed datatable samples</small>
	</h3> -->
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
                  <div class="portlet-title">
                      <div class="caption">
                          <i class="fa fa-gift"></i>IP管控白名单-新增 </div>
                      <div class="tools">
                          <a href="javascript:;" class="collapse" data-original-title="" title=""> </a>
                          <a href="#portlet-config" data-toggle="modal" class="config" data-original-title="" title=""> </a>
                          <a href="javascript:;" class="reload" data-original-title="" title=""> </a>
                          <a href="javascript:;" class="remove" data-original-title="" title=""> </a>
                      </div>
                  </div>
                  <div class="portlet-body form">
                      <!-- BEGIN FORM-->
                      <form action="#" class="form-horizontal">
                          <div class="form-body">
                          
                              <%@include file="/WEB-INF/include/form/ip6T.jsp" %>
                              
                        
                              <%@include file="/WEB-INF/include/form/basicInfo.jsp" %>
                              
                            
                          </div>
                          <div class="form-actions">
                              <div class="row">
                                  <div class="col-md-6">
                                      <div class="row">
                                          <div class="col-md-offset-3 col-md-8">
                                              <button type="submit" class="btn green">保存</button>
                                              <button type="button" class="btn default">Cancel</button>
                                          </div>
                                      </div>
                                  </div>
                                  <div class="col-md-6"> </div>
                              </div>
                          </div>
                      </form>
                      <!-- END FORM-->
                  </div>
              </div>
		</div>
	</div>
</div>
</body>
</html>