package com.nis.domain;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nis.util.StringUtils;


public class SysLog extends BaseEntity<SysLog>{
	private static final long serialVersionUID = -926412650391800311L;

	private Integer type;//日志类型：运行日志（1000-系统登录 1001-系统退出）、操作（2000-添加 2001-删除 2002-修改 2003-查询） \404 未知操作

    private String title;// 日志标题

	private Date createDate;//创建日期

    private String remoteAddr;// 操作用户的IP地址

    private String userAgent;// 操作用户代理信息

    private String requestUri;// 操作的URI

    private String method;// 操作的方式

    private Integer state;//0-失败 1-成功
    
    private String params; // 操作提交的数据
    
    private String exception; // 异常信息
    
    private Long consumerTime;//消费时间
    
    private Integer functionId;//功能菜单id
    private String compileIds;//配置编译id
    private String cfgIds;//界面配置id
    private Integer compileId;//配置编译id
    private Integer cfgId;//界面配置id
    private String methodName;//功能方法名称
    private Integer auditState;//审核状态
    private Integer action;//GK动作
    private String operation;//操作说明
    private String functionName;//功能名称
    private Integer isValid;//配置有效状态
    
    
    public Long getConsumerTime() {
		return consumerTime;
	}

	public void setConsumerTime(Long consumerTime) {
		this.consumerTime = consumerTime;
	}

	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
    


    public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr == null ? null : remoteAddr.trim();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri == null ? null : requestUri.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
    public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	private String createBy;// 创建者

    public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
    
    public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public Integer getCompileId() {
		return compileId;
	}

	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getCompileIds() {
		return compileIds;
	}

	public void setCompileIds(String compileIds) {
		this.compileIds = compileIds;
	}

	public String getCfgIds() {
		return cfgIds;
	}

	public void setCfgIds(String cfgIds) {
		this.cfgIds = cfgIds;
	}

	public Integer getCfgId() {
		return cfgId;
	}

	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	/**
	 * 设置请求参数
	 * @param paramMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setParams(Map paramMap){
		if (paramMap == null){
			return;
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
			params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
		}
		this.params = params.toString();
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}