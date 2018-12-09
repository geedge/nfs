/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.CookieUtil;

/**
 * 分页类
 * @author ThinkGem
 * @version 2013-7-2
 * @param <T>
 */
public class PageLog<T> {
	
	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Configurations.getIntProperty("page.pageSize", 30)); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	
	private long count;// 总记录数，设置为“-1”表示不查询总数
	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引
	
	
	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 8;// 显示页面长度
	private int slider = 1;// 前后显示页面长度
	
	private List<T> list = new ArrayList<T>();
	
	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc
	
	private String fields;//制定资源的字段
	
	private String where;
	private String alias;
	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	
	private String funcParam = ""; // 函数的附加参数，第三个参数值。
	
	private String message = ""; // 设置提示消息，显示在“共n条”之后

	private RequestContext requestContext;
	
	private int maxExportSize=Constants.MAX_EXPORT_SIZE;
	
	public PageLog() {
		this.pageSize = -1;
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public PageLog(HttpServletRequest request, HttpServletResponse response){
		this(request, response, Integer.valueOf(Configurations.getIntProperty("page.pageSize", 30)));
	}
	public PageLog(HttpServletRequest request, HttpServletResponse response,String alias){
		
		this(request, response, Integer.valueOf(Configurations.getIntProperty("page.pageSize", 30)),alias);
	}
	
	public PageLog(HttpServletRequest request, HttpServletResponse response, int defaultPageSize,String alias){
		this.setAlias(alias);
		this.initPage(request, response, defaultPageSize);
	}
	public PageLog(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		this.initPage(request, response, defaultPageSize);
	}
	private void initPage(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		try {
			
			this.requestContext = new RequestContext(request);

			// 设置页码参数（传递repage参数，来记住页码）
			String no = request.getParameter("pageNo");
			if (StringUtils.isNotBlank(no)) {
				if (StringUtils.isNumeric(no)){
					CookieUtil.addCookie(response, "pageNo", no);
					this.setPageNo(Integer.parseInt(no));
				}else if (request.getParameter("repage")!=null){
					no =  CookieUtil.getValue(request, "pageNo");
					if (StringUtils.isNumeric(no)){
						this.setPageNo(Integer.parseInt(no));
					}
				}
			}
			
			// 设置页面大小参数（传递repage参数，来记住页码大小）
			String size = "";
			if(defaultPageSize==-1){
				size = "-1";
			}else{
				size = request.getParameter("pageSize");
				
			}
			if (StringUtils.isNotBlank(size)) {
				
				if (StringUtils.isNumeric(size) || size.equals("-1")){
					CookieUtil.addCookie(response, "pageSize", size);
					this.setPageSize(Integer.parseInt(size));
				} else if (request.getParameter("repage")!=null){
					size =  CookieUtil.getValue(request, "pageSize");
					if (StringUtils.isNumeric(size)){
						this.setPageSize(Integer.parseInt(size));
					}
				} 
			} else {
				this.pageSize = defaultPageSize;
			}
			
			String fields = request.getParameter("fields");
			if (StringUtils.isNotBlank(fields)){
				this.setFields(fields);
			}
			
			// 设置排序参数
			String orderBy = request.getParameter("orderBy");
			if (StringUtils.isNotBlank(orderBy)){
				this.setOrderBy(orderBy);
			}
			
			this.setWhere(getWhere(request));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: getWhere 
	* @Description: TODO(抽取where查询条件) 
	* @param @param request
	* @param @return    入参
	* @return String    返回类型
	* @author （darnell） 
	* @throws
	* @date 2016年8月17日 上午9:28:21 
	* @version V1.0
	* wx:日期格式的数据用日期格式化函数格式化，带空格的数据加上引号
	 */
	private String getWhere(HttpServletRequest request) {
		String format=Constants.SEARCH_DATEFORMAT;
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		Map<String, String[]> requestMap = request.getParameterMap();
		StringBuilder whereBuilder = new StringBuilder(512);
		for(String paramName : request.getParameterMap().keySet()) {
			if (requestMap.get(paramName)!=null&&paramName.startsWith("search_")&&StringUtils.isNotBlank(requestMap.get(paramName)[0])) {
				String clomn=paramName.substring("search_".length());
				String value=requestMap.get(paramName)[0].trim();
				boolean isDate=false;
				try {
					sdf.parse(value);
					isDate=true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(clomn.endsWith("_start")){
					clomn=clomn.substring(0,clomn.lastIndexOf("_start"));
					if(StringUtils.isNotBlank(alias)) whereBuilder.append(alias.trim()+".");
					whereBuilder.append(clomn).append(">=");
					if(isDate){
						whereBuilder.append("date_format('")
						.append(value).append("','%Y-%m-%d %H:%i:%S')");
					}else if(value.indexOf(" ")>-1){
						whereBuilder.append("'").append(requestMap.get(paramName)[0]).append("'");
					}else{
						whereBuilder.append(requestMap.get(paramName)[0]);
					}
					whereBuilder.append(" and ");
				}else if(clomn.endsWith("_end")){
					clomn=clomn.substring(0,clomn.lastIndexOf("_end"));
					if(StringUtils.isNotBlank(alias)) whereBuilder.append(alias.trim()+".");
					whereBuilder.append(clomn).append("<=");
					if(isDate){
						whereBuilder.append("DATE_FORMAT('")
						.append(value).append("','%Y-%m-%d %H:%i:%S')");
					}else if(value.indexOf(" ")>-1){
						whereBuilder.append("'").append(requestMap.get(paramName)[0]).append("'");
					}else{
						whereBuilder.append(requestMap.get(paramName)[0]);
					}
					whereBuilder.append(" and ");
				}else{
					if(StringUtils.isNotBlank(alias)) whereBuilder.append(alias.trim()+".");
					whereBuilder.append(clomn).append("=");
					if(isDate){
						whereBuilder.append("date_format('")
						.append(value).append("','%Y-%m-%d %H:%i:%S')");
					}else if(value.indexOf(" ")>-1){
						whereBuilder.append("'").append(requestMap.get(paramName)[0]).append("'");
					}else{
						whereBuilder.append(requestMap.get(paramName)[0]);
					}
					whereBuilder.append(" and ");
				}
				
			}
		}
		if (whereBuilder.length() > 0) {
			whereBuilder.delete(whereBuilder.lastIndexOf(" and "), whereBuilder.length());
		}
		return whereBuilder.toString();
	}
	
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public PageLog(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public PageLog(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public PageLog(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.list = list;
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		// 控制接口是否启用count last
		if (this.count!=0 && this.last!=0) {
			//this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
			
			/*if (this.count % this.pageSize != 0 || this.last == 0) {
				this.last++;
			}*/

			if (this.last < this.first) {
				this.last = this.first;
			}
			
			if (this.pageNo <= 1) {
				this.pageNo = this.first;
				this.firstPage=true;
			}

			if (this.pageNo >= this.last) {
				this.pageNo = this.last;
				this.lastPage=true;
			}

			if (this.pageNo < this.last - 1) {
				this.next = this.pageNo + 1;
			} else {
				this.next = this.last;
			}

			if (this.pageNo > 1) {
				this.prev = this.pageNo - 1;
			} else {
				this.prev = this.first;
			}
			
			//2
			if (this.pageNo < this.first) {// 如果当前页小于首页
				this.pageNo = this.first;
			}

			if (this.pageNo > this.last) {// 如果当前页大于尾页
				this.pageNo = this.last;
			}
		}else {
		//首页
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		//最后一页
		if(list.size()<this.pageSize){
			this.lastPage=true;
		}

		//下一页
		if (list.size() >= this.pageSize) {
			this.next = this.pageNo + 1;
		} 
//		上一页
		if (pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}
	}
		
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// 控制接口返回count last 切换分页
		if (this.count!=0) {
			if(list != null && list.isEmpty()) {
				return "<div class=\"none-data\"><i class=\"fa fa-warning font-red-flamingo\"></i>&nbsp;&nbsp;"+requestContext.getMessage("noneData")+"</div>";
			}
			if (pageNo == first) {// 如果是首页
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; "+requestContext.getMessage("previousPage")+"</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+",'"+funcParam+"');\">&#171; "+requestContext.getMessage("previousPage")+"</a></li>\n");
			}

			int begin = pageNo - (length / 2);

			if (begin < first) {
				begin = first;
			}

			int end = begin + length - 1;

			if (end >= last) {
				end = last;
				begin = end - length + 1;
				if (begin < first) {
					begin = first;
				}
			}

			if (begin > first) {
				int i = 0;
				for (i = first; i < first + slider && i < begin; i++) {
					sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
							+ (i + 1 - first) + "</a></li>\n");
				}
				if (i < begin) {
					sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
				}
			}

			for (int i = begin; i <= end; i++) {
				if (i == pageNo) {
					sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
							+ "</a></li>\n");
				} else {
					sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
							+ (i + 1 - first) + "</a></li>\n");
				}
			}

			if (last - end > slider) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
				end = last - slider;
			}

			for (int i = end + 1; i <= last; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
						+ (i + 1 - first) + "</a></li>\n");
			}

			if (pageNo == last) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">"+requestContext.getMessage("nextPage")+" &#187;</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+",'"+funcParam+"');\">"
						+ ""+requestContext.getMessage("nextPage")+" &#187;</a></li>\n");
			}

			sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">"+requestContext.getMessage("current")+" ");
			sb.append("<input type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
			sb.append(funcName+"(this.value,"+pageSize+",'"+funcParam+"');\" onclick=\"this.select();\"/> / ");
			/*sb.append(pageNo+" / ");*/
			sb.append(last+" "+requestContext.getMessage("page")+"，");
			/*sb.append("<input type=\"text\" value=\""+last+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
			sb.append(funcName+"("+pageNo+",this.value,'"+funcParam+"');\" onclick=\"this.select();\"/> "+requestContext.getMessage("page")+"，");*/
			sb.append(""+requestContext.getMessage("total")+" <span id='showTotalCount'>" + count + "</span> "+requestContext.getMessage("count")+""+(message!=null?message:"")+"</a></li>\n");

			sb.insert(0,"<ul>\n").append("</ul>\n");
			
			sb.append("<div style=\"clear:both;\"></div>");

//			sb.insert(0,"<div class=\"page\">\n").append("</div>\n");
			
		}else {
		
		if(list != null && list.isEmpty()&&pageNo<=1) {
			return "<div class=\"none-data\"><i class=\"fa fa-warning font-red-flamingo\"></i>&nbsp;&nbsp;"+requestContext.getMessage("noneData")+"</div>";
		}
		

		
		sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+first+","+pageSize+",'"+funcParam+"');\">"+requestContext.getMessage("firstPage")+"</a></li>\n");
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; "+requestContext.getMessage("previousPage")+"</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+",'"+funcParam+"');\">&#171; "+requestContext.getMessage("previousPage")+"</a></li>\n");
		}

		sb.append("<li class=\"disabled\"><a href=\"javascript:\">"+pageNo+"</a></li>\n");
		
//		最后一页 未铺满 集合不为空
		if (list != null && !list.isEmpty()&&list.size()<pageSize) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">"+requestContext.getMessage("nextPage")+" &#187;</a></li>\n");
		}else if(pageNo>1&&list.isEmpty()){
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">"+requestContext.getMessage("nextPage")+" &#187;</a></li>\n");
		}else {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+",'"+funcParam+"');\">"
					+ ""+requestContext.getMessage("nextPage")+" &#187;</a></li>\n");
		}


		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");
		}
		return sb.toString();
	}
	
	/**
	 * 获取分页HTML代码
	 * @return
	 */
	@JsonIgnore
	public String getHtml(){
		return toString();
	}
	

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setLast(Integer last) {
		this.last = last;
	}
	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		if (pageSize == -1 || pageSize > 0 ) {
			this.pageSize = pageSize;
		} else {
			this.pageSize = Integer.valueOf(Configurations.getIntProperty("page.pageSize", 30));
		}
	}

	/**
	 * 首页索引
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	public int getLast() {
		return last;
	}
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return getLast();
	}
	/**
	 * 是否为第一页
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}
	

	/** 
	* @return where 
	*/
	@JsonIgnore
	public String getWhere() {
		return where;
	}

	/** 
	* @param where 要设置的 where 
	*/
	public void setWhere(String where) {
		this.where = where;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public PageLog<T> setList(List<T> list) {
		this.list = list;
		initialize();
		return this;
	}

	/**
	 * 获取查询排序字符串
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		// SQL过滤，防止注入 
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
					+ "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(orderBy).find()) {
			return "";
		}
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	

	/** 
	* @return fields  字段属性查询，拼接用,分隔
	*/
	@JsonIgnore
	public String getFields() {
		return fields;
	}

	/** 
	* @param fields 要设置的 fields 
	*/
	public void setFields(String fields) {
		this.fields = fields;
	}

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 获取分页函数的附加参数
	 * @return
	 */
	@JsonIgnore
	public String getFuncParam() {
		return funcParam;
	}
	@JsonIgnore
	public int getMaxExportSize() {
		return maxExportSize;
	}
	public void setMaxExportSize(int maxExportSize) {
		this.maxExportSize = maxExportSize;
	}
	/**
	 * 设置分页函数的附加参数
	 * @return
	 */
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	@JsonIgnore
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	@JsonIgnore
	public int getMaxResults(){
		return getPageSize();
	}

	/**
	 * alias
	 * @return  alias
	*/
	
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
