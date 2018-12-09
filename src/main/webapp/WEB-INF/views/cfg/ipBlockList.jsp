<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title>来函信息</title>

</head>
<body>

<div class="page-content">
	<div class="theme-panel hidden-xs hidden-sm">
		<button type="button" class="btn btn-default">&nbsp;刷新&nbsp;</button>

		<button type="button" class="btn btn-primary"
			onClick="javascript:window.location='${ctx}/cfg/ip/ipBlockForm'">&nbsp;新增&nbsp;</button>
	</div>

	<h3 class="page-title">
		IP管控详情
	</h3>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-cogs"></i>IP管控详情
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse" data-original-title=""
							title=""> </a> <a href="#portlet-config" data-toggle="modal"
							class="config" data-original-title="" title=""> </a> <a
							href="javascript:;" class="reload" data-original-title=""
							title=""> </a> <a href="javascript:;" class="remove"
							data-original-title="" title=""> </a>
					</div>
				</div>
				<div class="portlet-body">


					<div class="row" >


						<div class="col-md-12">


							<select>
								<option>标题</option>
								<option>来函函号</option>
								<option>任务</option>
							</select> &nbsp; <input> &nbsp; <select>
								<option>来函时间</option>
								<option>操作时间</option>
							</select> &nbsp; <input>

							<button type="button" class="btn btn-default btn-sm">
								<i class="fa fa-edit"></i> 搜索
							</button>
							&nbsp;&nbsp; 排序: <select>
								<option>默认排序</option>
								<option>按来函时间顺序</option>
								<option>按来函时间倒序</option>
								<option>按操作时间顺序</option>
								<option>按操作时间倒序</option>
							</select>
						</div>


					</div>


					<div class="table-responsive">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>序号</th>
									<th>来函函号</th>
									<th>来函单位</th>
									<th>来函时间</th>
			
						
									<th>任务</th>
									<th>操作员</th>
									<th>操作时间</th>
									<th>标题</th>
									<th>内容</th>
									<th>备注</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td>20181720函号</td>
									<td>来函单位 </d>
									<td>2018-01-06 21:57:03</td>
							
									<td>七五专项</td>
									<td>Admin</td>
									<td>2018-01-06 21:57:03</td>
									<td>标题</td>
									<td>test</td>
									<td>test</td>
								</tr>
								<tr>
									<td>2</td>
									<td>20181720函号</td>
									<td>来函单位 </d>
									<td>2018-01-06 21:57:03</td>
						
									<td>七五专项</td>
									<td>Admin</td>
									<td>2018-01-06 21:57:03</td>
									<td>标题</td>
									<td>test</td>
									<td>test</td>
								</tr>
								<tr>
									<td>3</td>
									<td>20181720函号</td>
									<td>来函单位 </d>
									<td>2018-01-06 21:57:03</td>
					
									<td>七五专项</td>
									<td>Admin</td>
									<td>2018-01-06 21:57:03</td>
									<td>标题</td>
									<td>test</td>
									<td>test</td>
								</tr>
								<tr>
									<td>4</td>
									<td>20181720函号</td>
									<td>来函单位 </d>
									<td>2018-01-06 21:57:03</td>
							
									<td>七五专项</td>
									<td>Admin</td>
									<td>2018-01-06 21:57:03</td>
									<td>标题</td>
									<td>test</td>
									<td>test</td>
								</tr>

								<tr>
									<td>5</td>
									<td>20181720函号</td>
									<td>来函单位 </d>
									<td>2018-01-06 21:57:03</td>
								
									<td>七五专项</td>
									<td>Admin</td>
									<td>2018-01-06 21:57:03</td>
									<td>标题</td>
									<td>test</td>
									<td>test</td>
								
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
</div>
</body>
</html>