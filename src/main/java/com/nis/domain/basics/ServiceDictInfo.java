package com.nis.domain.basics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysMenu;
import com.nis.domain.SysUser;
import com.nis.util.Configurations;
import com.nis.util.StringUtil;
/**
 * 业务辅助表-业务字典信息表
 * @author zsl
 *
 */
public class ServiceDictInfo extends BaseEntity<ServiceDictInfo>{

	private static final long serialVersionUID = 4680466118906894338L;
	
	private Integer serviceDictId; //service_dict_id	字典ID	int	N	主键，自增
	private Integer itemType; //item_type	数据类型	int	N	1：分类  2：性质  3：标签
	private String itemCode; //item_code编码	varchar(64)	N	
	private String itemValue; //item_value	编码对应值	varchar2(64)	N	
	private String itemDesc; //item_desc	描述信息	varcahr2(128)	Y	
	private ServiceDictInfo parent; //parent_id	父ID	number(9)	N	无父属性，默认填0
	private Integer isLeaf; //is_leaf	是否叶子节点	int	N	0-否 1-是；只有一级填0；
	private Integer isValid; //is_valid	有效标志	int	N	1-有效 0-无效
	private SysUser serviceDictCreator; //creator_id	创建人员	int	N	取自sys_user.id
	private Date createTime; //create_time	配置时间	date	N	
	private SysUser serviceDictEditor; //editor_id	修改人员	int	Y	取自sys_user.id
	private Date editTime; //edit_time	修改时间	date	Y	
	private Integer levelNo; //层级
	private List<ServiceDictInfo> childrenList = new ArrayList<ServiceDictInfo>();//子列表
    private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	private Date editBeginDate;		// 修改开始日期
	private Date editEndDate;		// 修改结束日期
	
	private String showSequence;  //显示序号
	
	private String pNames;//父节点名称
	/**
	 * 封装参数数据类型,
	 * 
	 */
	private List<Integer> conditionType;
	
	@JsonIgnore
	public String getpNames() {
		return pNames;
	}
	public void setpNames(String pNames) {
		this.pNames = pNames;
	}
	public Integer getServiceDictId() {
		return serviceDictId;
	}
	public void setServiceDictId(Integer serviceDictId) {
		this.serviceDictId = serviceDictId;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Integer getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	@JsonBackReference
	public ServiceDictInfo getParent() {
		return parent;
	}
	public void setParent(ServiceDictInfo parent) {
		this.parent = parent;
	}
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public SysUser getServiceDictCreator() {
		return serviceDictCreator;
	}
	public void setServiceDictCreator(SysUser serviceDictCreator) {
		this.serviceDictCreator = serviceDictCreator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public SysUser getServiceDictEditor() {
		return serviceDictEditor;
	}
	public void setServiceDictEditor(SysUser serviceDictEditor) {
		this.serviceDictEditor = serviceDictEditor;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public List<ServiceDictInfo> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<ServiceDictInfo> childrenList) {
		this.childrenList = childrenList;
	}
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
	public Date getEditBeginDate() {
		return editBeginDate;
	}
	public void setEditBeginDate(Date editBeginDate) {
		this.editBeginDate = editBeginDate;
	}
	public Date getEditEndDate() {
		return editEndDate;
	}
	public void setEditEndDate(Date editEndDate) {
		this.editEndDate = editEndDate;
	}
	public String getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(String showSequence) {
		this.showSequence = showSequence;
	}
	public List<Integer> getConditionType() {
		return conditionType;
	}
	public void setConditionType(List<Integer> conditionType) {
		this.conditionType = conditionType;
	}
	@JsonIgnore
	public static void sortList(List<ServiceDictInfo> list, List<ServiceDictInfo> sourcelist, Integer parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			ServiceDictInfo serviceDictInfo = sourcelist.get(i);
			//System.out.println(serviceDictInfo.getServiceDictId()+">>>>>"+serviceDictInfo.getParent().getServiceDictId());
			if (serviceDictInfo.getParent()!=null && serviceDictInfo.getParent().getServiceDictId()!=null
					&& serviceDictInfo.getParent().getServiceDictId().equals(parentId)){
				list.add(serviceDictInfo);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						ServiceDictInfo child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getServiceDictId()!=null
								&& child.getParent().getServiceDictId().equals(serviceDictInfo.getServiceDictId())){
							sortList(list, sourcelist, serviceDictInfo.getServiceDictId(), true);
							break;
						}
					}
				}
			}
		}
	}
	
	@JsonIgnore
	public static void checkList(List<ServiceDictInfo> list,List<ServiceDictInfo> parentList, List<ServiceDictInfo> sourcelist, boolean cascade){
		for(ServiceDictInfo serviceUp:parentList){
			list.add(serviceUp);
			//判断是否有子节点，有则继续获取子节点 
			sortList(list, sourcelist, serviceUp.getServiceDictId(), true);
		}
		
	}
	
	
	//顶层的数量
	
	public static Integer topCount(List<ServiceDictInfo> list,Integer parentId){
		Integer count = 0;
		List<ServiceDictInfo> listTemp = new ArrayList<ServiceDictInfo>();
		for(ServiceDictInfo serviceDictInfo:list){
			if(serviceDictInfo.getParent()!=null&&serviceDictInfo.getParent().getServiceDictId()!=null&&serviceDictInfo.getParent().getServiceDictId().equals(parentId)){
				listTemp.add(serviceDictInfo);
			}
		}
		
		return listTemp.size();
	}
	
	//处理下级序号
	public static void addChildrenSeq(List<ServiceDictInfo> list,Integer parentId){
		    int countNo = 1;
			for(int i=0; i<list.size(); i++){
				ServiceDictInfo serviceDictInfo = list.get(i);
				if(serviceDictInfo.getParent()!=null && serviceDictInfo.getParent().getServiceDictId()!=null
						&& serviceDictInfo.getParent().getServiceDictId().equals(parentId)){
					//找出该父类
					for(ServiceDictInfo se:list){
						if(se.getServiceDictId().equals(parentId)){
							serviceDictInfo.setShowSequence(se.getShowSequence()+Configurations.getStringProperty("childrenMark", ".")+countNo);
							countNo++;
						}
					}
					//继续获取子节点
					for (int j=0; j<list.size(); j++){
						ServiceDictInfo child = list.get(j);
						if (child.getParent()!=null && child.getParent().getServiceDictId()!=null
								&& child.getParent().getServiceDictId().equals(serviceDictInfo.getServiceDictId())){
							addChildrenSeq(list, serviceDictInfo.getServiceDictId());
							break;
						}
					}
				}
				
			}
		
		
		
	}
	//获取所有父节点
	@JsonIgnore
	public static String getPNames(List<ServiceDictInfo> list,Integer pid,String pNames) {
		String pName="";
		for (ServiceDictInfo serviceDictInfo : list) {
			if(pid.equals(serviceDictInfo.getServiceDictId())){
				pid=serviceDictInfo.getParent().getServiceDictId();
				pName=serviceDictInfo.getItemValue();
				pNames="/"+pName+pNames;
			}
		}
		if(pid==0){
			if(!StringUtil.isEmpty(pNames)) pNames=pNames.substring(1);
			return pNames;
		}else{
			return getPNames(list, pid, pNames);
		}
	}
	
}
