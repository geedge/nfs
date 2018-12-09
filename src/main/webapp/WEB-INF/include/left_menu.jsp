<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp"%>



		<c:forEach items="${fns:getLeftMenuTreeList()}" var="menu" varStatus="idxStatus">
     		
			<ul id="menu_${menu.id }" class="hide accordion page-sidebar-menu page-header-fixed page-sidebar-menu-light" 
     			data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200"
     			menu-id="${menu.id }" menu-name="<spring:message code="${menu.code}"></spring:message>" >
     			
     			<c:if test="${not empty menu.children }">
     				<c:forEach  items="${menu.children}" var="secondMenu" varStatus="secondStatus">
     					<c:if test="${secondMenu.isShow==1}">
		     				<li class="nav-item" id="menu_${secondMenu.id }" menu-id="${secondMenu.id }" menu-name="<spring:message code="${secondMenu.code}"></spring:message>" >
			     				
			     				<a class="nav-link nav-toggle"		     					
			     					<c:if test="${secondMenu.href != null && secondMenu.href != ''}" var="secondHref">
			     						 href="javascript:;" onclick="page_turn('${secondMenu.id }','${secondMenu.functionId }','1','','${ctx}${secondMenu.href }',this)" target="mainFrame" >
			     					</c:if>
			     					<c:if test="${!secondHref }">
			     						 href="javascript:;" class="nav-link nav-toggle"> 
			     					</c:if> 
			     					
									<i class="${secondMenu.icon }"></i> 
									<span class="title"> 
									<%-- ${secondMenu.name } --%> <spring:message code="${secondMenu.code}"></spring:message></span>
									<c:if test="${not empty secondMenu.children }" var="secondClid">
										<span class="arrow "></span>
									</c:if>
								</a>
								
								
								<c:if test="${secondClid}">
									<ul class="sub-menu">
									
										<c:forEach  items="${secondMenu.children}" var="thirdMenu" varStatus="thirdStatus">
											
											<c:if test="${thirdMenu.isShow==1}">
												<li class="nav-item" id="menu_${thirdMenu.id }" menu-id="${thirdMenu.id }" menu-name="<spring:message code="${thirdMenu.code}"></spring:message>">
									
								     				<a class="nav-link nav-toggle"		     					
								     					<c:if test="${thirdMenu.href != null && thirdMenu.href != ''}" var="thirdHref">
								     						 href="javascript:;" onclick="page_turn('${thirdMenu.id }','${thirdMenu.functionId }','2','','${ctx}${thirdMenu.href }',this)" target="mainFrame" >
								     						 
								     					</c:if>
								     					<c:if test="${!thirdHref }">
								     						 href="javascript:;" class="nav-link nav-toggle"> 
								     					</c:if> 
				     					
														<span class="title">
														<%-- ${thirdMenu.name } --%><spring:message code="${thirdMenu.code}"></spring:message></span> 
														<c:if test="${not empty thirdMenu.children }" var="thiredClid">
															<span class="arrow "></span>
														</c:if>
													</a>
													
													<c:if test="${thiredClid}">
														<ul class="sub-menu">
															<c:forEach  items="${thirdMenu.children}" var="fourthMenu">
																<c:if test="${fourthMenu.isShow==1}">
																	<li class="nav-item" id="menu_${fourthMenu.id }" menu-id="${fourthMenu.id }" menu-name="<spring:message code="${fourthMenu.code}"></spring:message>"  >
																		<a href="javascript:;" 
																		onclick="page_turn('${fourthMenu.id }','${fourthMenu.functionId }','3','','${ctx}${fourthMenu.href }',this)" target="mainFrame" 
																		class="nav-link ">
																			<%-- ${fourthMenu.name } --%> <spring:message code="${fourthMenu.code}"></spring:message> 
																			<!-- <span class="badge badge-danger">1</span> -->
																		</a>
																	</li>
																</c:if>
															</c:forEach> 
														</ul>
													 </c:if>
												  </li>
											</c:if>
										</c:forEach> 
									 </ul>
								</c:if>
								
								
							</li>
     					</c:if>
     				</c:forEach>
     				
     			</c:if>
     					
				
     		
     		</ul>
     		
		</c:forEach>
		

