/**
 *@Title: BaseConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 上午11:15:14
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysMenu;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.util.excel.ExcelField;
import com.nis.web.security.UserUtils;

/**
 * @ClassName: BaseConfig.java 
 * @Description: 基础配置
 * @author (wx)
 * @date 2018年2月5日 上午11:15:14
 * @version V1.0
 */
public class BaseCfg<T> extends BaseEntity<T> implements Cloneable{
	protected Integer index;//excel导入时所在的行
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	protected Integer areaType;
	protected List<AreaBean> areaIsp;
	protected List<AreaIpCfg> areaCfg;
	protected String showName;//表在界面上展示的名称
	protected String seltype;//选中类型,页面搜索用
	protected String tableName;
	@ExcelField(title="userregion1",sort=3)
	protected String userRegion1;
	@ExcelField(title="userregion2",sort=4)
	protected String userRegion2;
	@ExcelField(title="userregion3",sort=5)
	protected String userRegion3;
	@ExcelField(title="userregion4",sort=6)
	protected String userRegion4;
	@ExcelField(title="userregion5",sort=7)
	protected String userRegion5;
	
	private Map<Integer,Integer> compileGroupMap;
	protected Integer compileIsIssued;
	public Integer getCompileIsIssued() {
		return compileIsIssued;
	}
	public void setCompileIsIssued(Integer compileIsIssued) {
		this.compileIsIssued = compileIsIssued;
	}
	
	public Map<Integer, Integer> getCompileGroupMap() {
		return compileGroupMap;
	}
	public void setCompileGroupMap(Map<Integer, Integer> compileGroupMap) {
		this.compileGroupMap = compileGroupMap;
	}
	
	/**
	 * 编译id
	 */
	@ExcelField(title="cfg_id",sort=0)
	protected Integer compileId ;
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 3253728127555103620L;
	/**
	 * 配置ID
	 */
	protected Long cfgId;
	/**
	 * 配置描述
	 */
	@ExcelField(title="config_describe",sort=1)
	protected String cfgDesc;
	
	/**
	 * GK类型
	 */
	@Expose
	@ExcelField(title="block_type",dictType="SERVICE_ACTION",sort=2)
	protected Integer action;
	/**
	 * 有效标识
	 */
	@ExcelField(title="valid_identifier",dictType="VALID_IDENTIFIER",sort=90)
	@Expose
	protected Integer isValid;
	/**
	 * 是否审核
	 */
	@ExcelField(title="is_audit",dictType="AUDIT_STATUS",sort=25)
	protected Integer isAudit;
	/**
	 * 创建人员
	 */
	protected Long creatorId;
	/**
	 * 创建人员
	 */
	@ExcelField(title="creator",sort=110)
	protected String creatorName;
	/**
	 * 配置时间
	 */
	@ExcelField(title="config_time",sort=120)
	protected Date createTime;
	/**
	 * 修改人员
	 */
	protected Long editorId;
	/**
	 * 创建人员
	 */
	@ExcelField(title="editor",sort=130)
	protected String editorName;
	/**
	 * 修改时间
	 */
	@ExcelField(title="edit_time",sort=140)
	protected Date editTime;
	/**
	 * 审核人员
	 */
	protected Long auditorId;
	/**
	 * 审核人员
	 */
	@ExcelField(title="auditor",sort=150)
	protected String auditorName;
	/**
	 * 审核时间
	 */
	@Expose
	@ExcelField(title="audit_time",sort=160)
	@SerializedName("opTime")
	protected Date auditTime;
	/**
	 * 业务id
	 */
	@Expose
	@SerializedName("service")
	protected Integer serviceId;
	/**
	 * 来函id
	 */
	protected Integer requestId;
	/**
	 * 取消审核来函id
	 */
	protected Integer cancelRequestId;
	/**
	 * 来函
	 */
	@ExcelField(title="letter",sort=58)
	protected String requestName;
	/**
	 * 是否区域gk
	 */
	@ExcelField(title="whether_area_block",dictType="WHETHER_AREA_BLOCK",sort=40)
	protected Integer isAreaEffective;
	/**
	 * 分类
	 */
	@ExcelField(title="classification",dictType="type",sort=60)
	protected String classify;
	/**
	 * 性质
	 */
	@ExcelField(title="attribute",dictType="attribute",sort=70)
	protected String attribute;
	/**
	 * 标签
	 */
	@ExcelField(title="label",dictType="label",sort=80)
	protected String lable;
	/**
	 * 分类
	 */
	protected String classifyName;
	/**
	 * 性质
	 */
	protected String attributeName;
	/**
	 * 标签
	 */
	protected String lableName;
	/**
	 * 区域生效id
	 */
	protected String areaEffectiveIds ;
	/**
	 * 界面搜索时间条件
	 */
	protected Date search_create_time_start;
	protected Date search_create_time_end;
	protected Date search_edit_time_start;
	protected Date search_edit_time_end;
	protected Date search_audit_time_start;
	protected Date search_audit_time_end;
	/**
	 * 配置域码
	 */
	protected Integer cfgRegionCode;
	/**
	 * 内容类型
	 */
	protected String cfgType;
	/**
	 * 菜单ID
	 */
	protected Integer functionId;
	/**
	 * 菜单权限
	 */
	protected String permission;
	/**
	 * 菜单权限
	 */
	protected String menuNameCode;
	
	/**
	 * do_log属性在界面（do_log:0不需要1记录所有日志2只记录结构化日志。默认是2）
	 */
	@ExcelField(title="do_log",dictType="DO_LOG",sort=30)
	protected Integer doLog = Constants.MAAT_CFG_DOLOG_DEFAULT;
	
	protected Integer groupId;//仅用于copy属性使用
	protected Integer regionId;//仅用于copy属性使用
//	protected Integer numberRegionGroupId;//仅用于copy属性使用
	protected Integer numberRegionRegionId;//仅用于copy属性使用
	
//	public Integer getNumberRegionGroupId() {
//		return numberRegionGroupId;
//	}
//	public void setNumberRegionGroupId(Integer numberRegionGroupId) {
//		this.numberRegionGroupId = numberRegionGroupId;
//	}
	public Integer getNumberRegionRegionId() {
		return numberRegionRegionId;
	}
	public void setNumberRegionRegionId(Integer numberRegionRegionId) {
		this.numberRegionRegionId = numberRegionRegionId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getDoLog() {
		return doLog;
	}
	public void setDoLog(Integer doLog) {
		this.doLog = doLog;
	}
	/**
	 * cfgRegionCode
	 * @return  cfgRegionCode
	*/
	
	public Integer getCfgRegionCode() {
		return cfgRegionCode;
	}
	/**
	 * @param cfgRegionCode the cfgRegionCode to set
	 */
	public void setCfgRegionCode(Integer cfgRegionCode) {
		this.cfgRegionCode = cfgRegionCode;
	}
	/**
	 * cfgType
	 * @return  cfgType
	*/
	
	public String getCfgType() {
		return cfgType;
	}
	/**
	 * @param cfgType the cfgType to set
	 */
	public void setCfgType(String cfgType) {
		this.cfgType = cfgType;
	}
	/**
	 * functionId
	 * @return  functionId
	*/
	
	public Integer getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
		if(!StringUtil.isEmpty(functionId) && StringUtil.isEmpty(this.menuNameCode)){
			List<SysMenu> list = UserUtils.getMenuList();
			if(!StringUtil.isEmpty(list)) {
				for (SysMenu menu :list) {
					if (!StringUtil.isEmpty(menu.getCode()) 
							&&menu.getFunctionId()!=null 
							&& menu.getFunctionId().equals(functionId)
							//&& menu.getFunctionId().equals(functionId)
							) {
						this.setMenuNameCode(menu.getCode());
						break;
					}
					
				}
			}
		}

	}
	
	public String getMenuNameCode() {
		return menuNameCode;
	}
	public void setMenuNameCode(String menuNameCode) {
		this.menuNameCode = menuNameCode;
	}
	
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	/**
	 * cfgId
	 * @return  cfgId
	*/
	public Long getCfgId() {
		return cfgId;
	}
	/**
	 * @param cfgId the cfgId to set
	 */
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	/**
	 * action
	 * @return  action
	*/
	
	public Integer getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(Integer action) {
		this.action = action;
	}
	/**
	 * isValid
	 * @return  isValid
	*/
	
	public Integer getIsValid() {
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	/**
	 * isAudit
	 * @return  isAudit
	*/
	
	public Integer getIsAudit() {
		return isAudit;
	}
	/**
	 * @param isAudit the isAudit to set
	 */
	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}
	/**
	 * creatorId
	 * @return  creatorId
	*/
	
	public Long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * createTime
	 * @return  createTime
	*/
	
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * editorId
	 * @return  editorId
	*/
	
	public Long getEditorId() {
		return editorId;
	}
	/**
	 * @param editorId the editorId to set
	 */
	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}
	/**
	 * editTime
	 * @return  editTime
	*/
	
	public Date getEditTime() {
		return editTime;
	}
	/**
	 * @param editTime the editTime to set
	 */
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	/**
	 * auditorId
	 * @return  auditorId
	*/
	
	public Long getAuditorId() {
		return auditorId;
	}
	/**
	 * @param auditorId the auditorId to set
	 */
	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}
	/**
	 * auditTime
	 * @return  auditTime
	*/
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	public Date getAuditTime() {
		return auditTime;
	}
	/**
	 * @param auditTime the auditTime to set
	 */
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	/**
	 * serviceId
	 * @return  serviceId
	*/
	
	public Integer getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * requestId
	 * @return  requestId
	*/
	
	public Integer getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	/**
	 * compileId
	 * @return  compileId
	*/
	
//	public Integer getCompileId() {
//		return compileId;
//	}
//	/**
//	 * @param compileId the compileId to set
//	 */
//	public void setCompileId(Integer compileId) {
//		this.compileId = compileId;
//	}
	/**
	 * isAreaEffective
	 * @return  isAreaEffective
	*/
	
	public Integer getIsAreaEffective() {
		return isAreaEffective;
	}
	/**
	 * @param isAreaEffective the isAreaEffective to set
	 */
	public void setIsAreaEffective(Integer isAreaEffective) {
		this.isAreaEffective = isAreaEffective;
	}
	/**
	 * classify
	 * @return  classify
	*/
	
	public String getClassify() {
		return classify;
	}
	/**
	 * @param classify the classify to set
	 */
	public void setClassify(String classify) {
		this.classify = classify;
	}
	/**
	 * attribute
	 * @return  attribute
	*/
	
	public String getAttribute() {
		return attribute;
	}
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	/**
	 * lable
	 * @return  lable
	*/
	
	public String getLable() {
		return lable;
	}
	/**
	 * @param lable the lable to set
	 */
	public void setLable(String lable) {
		this.lable = lable;
	}
	/**
	 * areaEffectiveIds
	 * @return  areaEffectiveIds
	*/
	
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	/**
	 * @param areaEffectiveIds the areaEffectiveIds to set
	 */
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
	/**
	 * cfgDesc
	 * @return  cfgDesc
	*/
	
	public String getCfgDesc() {
		return cfgDesc;
	}
	/**
	 * @param cfgDesc the cfgDesc to set
	 */
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	
	/**
	 * tableName
	 * @return  tableName
	*/
	
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void initDefaultValue(){
		
	}
	/**
	 * creatorName
	 * @return  creatorName
	*/
	
	public String getCreatorName() {
		return creatorName;
	}
	/**
	 * @param creatorName the creatorName to set
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/**
	 * editorName
	 * @return  editorName
	*/
	
	public String getEditorName() {
		return editorName;
	}
	/**
	 * @param editorName the editorName to set
	 */
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	/**
	 * auditorName
	 * @return  auditorName
	*/
	
	public String getAuditorName() {
		return auditorName;
	}
	/**
	 * @param auditorName the auditorName to set
	 */
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	/**
	 * classifyName
	 * @return  classifyName
	*/
	
	public String getClassifyName() {
		return classifyName;
	}
	/**
	 * @param classifyName the classifyName to set
	 */
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	/**
	 * attributeName
	 * @return  attributeName
	*/
	
	public String getAttributeName() {
		return attributeName;
	}
	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	/**
	 * lableName
	 * @return  lableName
	*/
	
	public String getLableName() {
		return lableName;
	}
	/**
	 * @param lableName the lableName to set
	 */
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
	/**
	 * requestName
	 * @return  requestName
	*/
	
	public String getRequestName() {
		return requestName;
	}
	/**
	 * @param requestName the requestName to set
	 */
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	/**
	 * search_create_time_start
	 * @return  search_create_time_start
	*/
	
	public Date getSearch_create_time_start() {
		return search_create_time_start;
	}
	/**
	 * @param search_create_time_start the search_create_time_start to set
	 */
	public void setSearch_create_time_start(Date search_create_time_start) {
		this.search_create_time_start = search_create_time_start;
	}
	/**
	 * search_create_time_end
	 * @return  search_create_time_end
	*/
	
	public Date getSearch_create_time_end() {
		return search_create_time_end;
	}
	/**
	 * @param search_create_time_end the search_create_time_end to set
	 */
	public void setSearch_create_time_end(Date search_create_time_end) {
		this.search_create_time_end = search_create_time_end;
	}
	/**
	 * search_edit_time_start
	 * @return  search_edit_time_start
	*/
	
	public Date getSearch_edit_time_start() {
		return search_edit_time_start;
	}
	/**
	 * @param search_edit_time_start the search_edit_time_start to set
	 */
	public void setSearch_edit_time_start(Date search_edit_time_start) {
		this.search_edit_time_start = search_edit_time_start;
	}
	/**
	 * search_edit_time_end
	 * @return  search_edit_time_end
	*/
	
	public Date getSearch_edit_time_end() {
		return search_edit_time_end;
	}
	/**
	 * @param search_edit_time_end the search_edit_time_end to set
	 */
	public void setSearch_edit_time_end(Date search_edit_time_end) {
		this.search_edit_time_end = search_edit_time_end;
	}
	/**
	 * search_audit_time_start
	 * @return  search_audit_time_start
	*/
	
	public Date getSearch_audit_time_start() {
		return search_audit_time_start;
	}
	/**
	 * @param search_audit_time_start the search_audit_time_start to set
	 */
	public void setSearch_audit_time_start(Date search_audit_time_start) {
		this.search_audit_time_start = search_audit_time_start;
	}
	/**
	 * search_audit_time_end
	 * @return  search_audit_time_end
	*/
	
	public Date getSearch_audit_time_end() {
		return search_audit_time_end;
	}
	/**
	 * @param search_audit_time_end the search_audit_time_end to set
	 */
	public void setSearch_audit_time_end(Date search_audit_time_end) {
		this.search_audit_time_end = search_audit_time_end;
	}
	/**
	 * seltype
	 * @return  seltype
	*/
	
	public String getSeltype() {
		return seltype;
	}
	/**
	 * @param seltype the seltype to set
	 */
	public void setSeltype(String seltype) {
		this.seltype = seltype;
	}
	/**
	 * compileId
	 * @return  compileId
	*/
	
	public Integer getCompileId() {
		return compileId;
	}
	/**
	 * @param compileId the compileId to set
	 */
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	/**
	 * areaCfg
	 * @return  areaCfg
	*/
	
	public List<AreaIpCfg> getAreaCfg() {
		return areaCfg;
	}
	/**
	 * @param areaCfg the areaCfg to set
	 */
	public void setAreaCfg(List<AreaIpCfg> areaCfg) {
		this.areaCfg = areaCfg;
	}
	/**
	 * showName
	 * @return  showName
	*/
	
	public String getShowName() {
		return showName;
	}
	/**
	 * @param showName the showName to set
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}
	/**
	 * areaIsp
	 * @return  areaIsp
	*/
	
	public List<AreaBean> getAreaIsp() {
		return areaIsp;
	}
	/**
	 * @param areaIsp the areaIsp to set
	 */
	public void setAreaIsp(List<AreaBean> areaIsp) {
		this.areaIsp = areaIsp;
	}
	/**
	 * areaType
	 * @return  areaType
	*/
	
	public Integer getAreaType() {
		return areaType;
	}
	/**
	 * @param areaType the areaType to set
	 */
	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}
	public String getUserRegion1() {
		return userRegion1;
	}
	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}
	public String getUserRegion2() {
		return userRegion2;
	}
	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}
	public String getUserRegion3() {
		return userRegion3;
	}
	public void setUserRegion3(String userRegion3) {
		this.userRegion3 = userRegion3;
	}
	public String getUserRegion4() {
		return userRegion4;
	}
	public void setUserRegion4(String userRegion4) {
		this.userRegion4 = userRegion4;
	}
	public String getUserRegion5() {
		return userRegion5;
	}
	public void setUserRegion5(String userRegion5) {
		this.userRegion5 = userRegion5;
	}
	public Integer getCancelRequestId() {
		return cancelRequestId;
	}
	public void setCancelRequestId(Integer cancelRequestId) {
		this.cancelRequestId = cancelRequestId;
	}
	
}
