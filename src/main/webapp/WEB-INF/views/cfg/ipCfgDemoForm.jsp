<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
<title><spring:message code="${cfgName}"></spring:message></title>
<link
	href="${pageContext.request.contextPath}/static/global/plugins/bootstrap-select/css/bootstrap-select.min.css"
	rel="stylesheet" />
<script type="text/javascript">
	$(function() {
		switchIpType($("select[name$='ipType']"));
		areaControlInit();
		$("select[name='cfgType']").on('change', function() {
			var val = $(this).val();
			if (val == 1) {
				$("#srcIp").val("0.0.0.0/32");
			} else {
				$("#srcIp").val("0.0.0.0-0.0.0.0");
			}
		});
		$("input[name='isAreaEffective']").on('change', function() {
			var val = $(this).val();
			if (val == 1) {
				$(".areaType").removeClass("hidden");
				if ($("input[name='areaType']:checked").val() == 1) {//areaISP
					$("#areaIsp").removeClass("hidden");
				} else if ($("input[name='areaType']:checked").val() == 0) {//areaIp
					$("#areaIp").removeClass("hidden");
				}
			} else {
				$(".areaType").addClass("hidden");
				$("#areaIp").addClass("hidden");
				$("#areaIsp").addClass("hidden");
			}
		});
		$("input[name='areaType']").on('change', function() {
			var val = $(this).val();
			if ($(this).is(":visible")) {
				if (val == 0) {
					$("#areaIp").removeClass("hidden");
					$("#areaIsp").addClass("hidden");
				} else {
					$("#areaIsp").removeClass("hidden");
					$("#areaIp").addClass("hidden");
				}
			} else {
				$("#areaIsp").addClass("hidden");
				$("#areaIp").addClass("hidden");
			}
		});
		$("select[name$='ipType']").on("change", function() {
			switchIpType($(this));
		});
		$("#ipCfgFrom").validate(
				{
					errorPlacement : function(error, element) {
						$(element).parents(".form-group").find(
								"div[for='" + element.attr("name") + "']")
								.append(error);
					},
					submitHandler : function(form) {
						//loading('onloading...');
						form.submit();
					},
					errorContainer : "#messageBox",
				});
	});
</script>
</head>
<body>
	<div class="page-content">
	 	<h3 class="page-title">
			<spring:message code="ip address"></spring:message>
		</h3> 
		<div class="row">
			<div class="col-md-12">
				<div class="portlet box blue">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-gift"></i>
							<c:if test="${empty _cfg.cfgId}">
								<spring:message code="add"></spring:message>
							</c:if>
							<c:if test="${not empty _cfg.cfgId}">
								<spring:message code="edit"></spring:message>
							</c:if>
						</div>
					</div>
					<div class="portlet-body form">
						<!-- BEGIN FORM-->
						<form id="ipCfgFrom" action="${ctx}/cfg/ip/saveOrUpdateCfg"
							method="post" class="form-horizontal">
							<div class="form-body">
								<h3 class="form-section">
									<spring:message code="service configuration" />
								</h3>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><spring:message
													code="config_describe" /></label>
											<div class="col-md-6">
												<input class="form-control" type="text" id="cfgDesc"
													name="cfgDesc" value="${_cfg.cfgDesc}">
											</div>
											<div for="cfgDesc"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="IP类型" /></label>
											<div class="col-md-6">
												<select name="ipType"
													class="selectpicker show-tick form-control required">
													<option value="4"
														<c:if test="${_cfg.ipType==4}">selected</c:if>>V4</option>
													<option value="6"
														<c:if test="${_cfg.ipType==6}">selected</c:if>>V6</option>
												</select>
												<%-- <input class="form-control" type="text"  value="${_cfg.ipType}"> --%>
											</div>
											<div for="ipType"></div>
										</div>
									</div>

								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="源IP格式" /></label>
											<div class="col-md-6">
												<select name="cfgType"
													class="selectpicker show-tick form-control required">
													<option value="1">Subnet</option>
													<option value="2">IP Range</option>
												</select>
												<%-- <input class="form-control" type="text"  value="${_cfg.ipType}"> --%>
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="源IP" /></label>
											<div class="col-md-6">
												<input class="form-control required ipCheck" type="text"
													name="srcIp" id="srcIp" value="0.0.0.0/32">
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="目的IP格式" /></label>
											<div class="col-md-6">
												<select name="cfgType"
													class="selectpicker show-tick form-control required">
													<option value="1">Subnet</option>
													<option value="2">IP Range</option>
												</select>
												<%-- <input class="form-control" type="text"  value="${_cfg.ipType}"> --%>
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="目的IP" /></label>
											<div class="col-md-6">
												<input class="form-control required ipCheck" type="text"
													name="srcIp" id="destIp" value="0.0.0.0/32">
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="源端口" /></label>
											<div class="col-md-6">
												<input class="form-control required ipCheck" type="text"
													name="srcPort" id="srcPort" value="0-65535">
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="目的端口" /></label>
											<div class="col-md-6">
												<input class="form-control required ipCheck" type="text"
													name="destPort" id="destPort" value="0-65535">
											</div>
											<div for="cfgType"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="direction" /></label>
											<div class="col-md-6">
												<select name="direction"
													class="selectpicker show-tick form-control required">
													<option value="0"
														<c:if test="${_cfg.direction==0}">selected</c:if>>both</option>
													<option value="1"
														<c:if test="${_cfg.direction==1}">selected</c:if>><spring:message
															code="ingress to egress" /></option>
													<option value="2"
														<c:if test="${_cfg.direction==1}">selected</c:if>><spring:message
															code="egress to ingress" /></option>
												</select>
											</div>
											<div for="direction"></div>
										</div>
									</div>
									<c:if test="${type eq 'bgp' }">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3"><font
													color="red">*</font>
												<spring:message code="ASN" /></label>
												<div class="col-md-6">
													<input class="form-control" type="text" id="cfgDesc"
													name="cfgDesc" value="${_cfg.cfgDesc}">
												</div>
												<div for="direction"></div>
											</div>
										</div>
									</c:if>
									<div class="col-md-6">

										<div class="form-group ">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="传输层协议" /></label>
											<div class="col-md-6">
												<select name="protocol"
													class="selectpicker show-tick form-control required"
													title="<spring:message code="select"/>">
													<option value="6"
														<c:if test="${_cfg.protocol==6}">selected</c:if>>TCP</option>
													<option value="17"
														<c:if test="${_cfg.protocol==17}">selected</c:if>>UDP</option>
												</select>
												<%-- <input class="form-control" type="text" name="protocol" value="${_cfg.protocol}"> --%>
											</div>
											<div for="protocol"></div>
										</div>
									</div>
									<c:if test="${type ne 'bgp' }">
										<div class="col-md-6">
	
											<div class="form-group ">
												<label class="control-label col-md-3"><font
													color="red">*</font>
												<spring:message code="应用层协议" /></label>
												<div class="col-md-6">
													<select name="protocol"
														class="selectpicker show-tick form-control required"
														title="<spring:message code="select"/>">
														<option value="1"
															<c:if test="${_cfg.protocol==1}">selected</c:if>>HTTP</option>
														<option value="2"
															<c:if test="${_cfg.protocol==2}">selected</c:if>>HTTPS</option>
														<option value="3"
															<c:if test="${_cfg.protocol==3}">selected</c:if>>FTP</option>
														<option value="4"
															<c:if test="${_cfg.protocol==4}">selected</c:if>>FTPS</option>
														<option value="5"
															<c:if test="${_cfg.protocol==5}">selected</c:if>>SMTP</option>
														<option value="6"
															<c:if test="${_cfg.protocol==6}">selected</c:if>>POP3</option>
														<option value="7"
															<c:if test="${_cfg.protocol==7}">selected</c:if>>SSH</option>
														<option value="8"
															<c:if test="${_cfg.protocol==8}">selected</c:if>>SSL</option>
														<option value="9"
															<c:if test="${_cfg.protocol==9}">selected</c:if>>DNS</option>
														<option value="10"
															<c:if test="${_cfg.protocol==10}">selected</c:if>>VPN</option>
														<option value="11"
															<c:if test="${_cfg.protocol==11}">selected</c:if>>PPTP</option>
														<option value="12"
															<c:if test="${_cfg.protocol==12}">selected</c:if>>L2TP</option>
														<option value="13"
															<c:if test="${_cfg.protocol==13}">selected</c:if>>MMS</option>
														<option value="14"
															<c:if test="${_cfg.protocol==14}">selected</c:if>>RTSP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>RTMP</option>
														<option value="16"
															<c:if test="${_cfg.protocol==16}">selected</c:if>>GRE</option>
														<option value="17"
															<c:if test="${_cfg.protocol==17}">selected</c:if>>ICMP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>IKE</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>IMAP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>IMAPS</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>IPSEC</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>XMPP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>NTP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>POP3
															SSL</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>QUIC</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>SIP</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>SMB</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>SMTPS</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>SPDY</option>
														<option value="15"
															<c:if test="${_cfg.protocol==15}">selected</c:if>>SOCKS</option>
													</select>
													<%-- <input class="form-control" type="text" name="protocol" value="${_cfg.protocol}"> --%>
												</div>
												<div for="protocol"></div>
											</div>
										</div>
									</c:if>
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="action" /></label>
											<div class="col-md-6">
												<select name="action"
													class="selectpicker show-tick form-control required">
													<option value="1"
														<c:if test="${_cfg.action==1}">selected</c:if>><spring:message
															code="drop" /></option>
													<option value="2"
														<c:if test="${_cfg.action==2}">selected</c:if>><spring:message
															code="reject" /></option>
													<option value="3"
														<c:if test="${_cfg.action==3}">selected</c:if>><spring:message
															code="redirect" /></option>
													<option value="4"
														<c:if test="${_cfg.action==4}">selected</c:if>><spring:message
															code="mirroring" /></option>
													<option value="5"
														<c:if test="${_cfg.action==5}">selected</c:if>><spring:message
															code="steering" /></option>
													<option value="6"
														<c:if test="${_cfg.action==6}">selected</c:if>><spring:message
															code="ratelimit" /></option>
													<option value="7"
														<c:if test="${_cfg.action==7}">selected</c:if>><spring:message
															code="replace content" /></option>
													<option value="8"
														<c:if test="${_cfg.action==8}">selected</c:if>><spring:message
															code="monitor" /></option>
												</select>
											</div>
											<div for="action"></div>
										</div>
									</div>
								</div>
								<input type="hidden" name="isAreaEffective" value="0"> <input
									type="hidden" name="areaEffectiveIds" value="">
								<div class="row">
									
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="whether_area_block" /></label>
											<div class="col-md-6" id="areaCheck">
												<label class="radio-inline"> <input type="radio"
													name="isAreaEffective" value="1" class="required"<c:if test="${_cfg.isAreaEffective==1}">checked</c:if>
		            			<%-- <c:if test="${action==2}">disabled</c:if> --%>
		            		>
												<spring:message code="yes" />
												</label> <label class="radio-inline"> <input type="radio"
													name="isAreaEffective" value="0" class="required"<c:if test="${_cfg.isAreaEffective==0}">checked</c:if>
		            			<%-- <c:if test="${action==2}">disabled</c:if> --%>
		            		>
												<spring:message code="no" />
												</label>
												<%-- <c:if test="${action==2}">
		            		<input class="form-control" type="hidden" name="isAreaEffective" value="${_cfg.isAreaEffective}">
			            </c:if> --%>
											</div>
											<div for="isAreaEffective"></div>
										</div>
									</div>
									<div class="hidden areaType col-md-6">
										<div class="form-group">
											<label class="control-label col-md-3"><font
												color="red">*</font>
											<spring:message code="area_control_type" /></label>
											<div class="col-md-6">
												<label class="radio-inline"> <input type="radio"
													name="areaType" value="1" class="required">
												<spring:message code="area" /> <spring:message code="isp" />
												</label> <label class="radio-inline"> <input type="radio"
													name="areaType" value="0" class="required">
												<spring:message code="area" /> ip
												</label>
											</div>
											<div for="areaType"></div>
											<input class="form-control" type="hidden"
												name="areaEffectiveIds" value="${_cfg.areaEffectiveIds}">
										</div>
									</div>
								</div>
								<div class="row">
									
								</div>


								<c:if test="${not empty _cfg.cfgId}">
									<input type="hidden" name="cfgId" value="${_cfg.cfgId}">
								</c:if>
								<c:if test="${not empty _cfg.compileId}">
									<input type="hidden" name="compileId" value="${_cfg.compileId}">
								</c:if>
								<input type="hidden" name="tableName" value="${_cfg.tableName}">
								<input type="hidden" name="serviceId" value="${serviceId}">
								<input type="hidden" name="cfgName" value="${cfgName}">
								<input type="hidden" name="action" value="${action}"> <input
									type="hidden" name="protocolId" value="${_cfg.protocolId}">
								<div id="areaInfo">
									<%-- <%@include file="/WEB-INF/include/form/areaDemoInfo.jsp"%> --%>
									<%@include file="/WEB-INF/include/form/basicInfo.jsp"%>
								</div>
								<input type="hidden" name=requestId value="0">
							</div>
							<div class="form-actions">
								<div class="row">
									<div class="col-md-6">
										<div class="row">
											<div class="col-md-offset-3 col-md-8">
												<button id="save" type="submit" class="btn green">
													<spring:message code="submit" />
												</button>
												<button id="cancel" type="button" class="btn default">
													<spring:message code="cancel" />
												</button>
											</div>
										</div>
									</div>
									<div class="col-md-6"></div>
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