package com.nis.domain.basics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysUser;
import com.nis.util.Configurations;
/**
 * 业务辅助表-系统字典信息表
 * @author zsl
 *
 */
public class SysDictInfo extends BaseEntity<SysDictInfo>{

	private static final long serialVersionUID = 893702645278342859L;
	
	private Integer sysDictId; //service_dict_id	字典ID	int	N	主键，自增
	private Integer itemType; //item_type	数据类型	int	N	1：分类  2：性质  3：标签
	private String itemCode; //item_code编码	varchar	N	
	private String itemValue; //item_value	编码对应值	varchar2(64)	N	
	private String itemDesc; //item_desc	描述信息	varcahr2(128)	Y	
	private SysDictInfo parent; //parent_id	父ID	number(9)	N	无父属性，默认填0
	private Integer isLeaf; //is_leaf	是否叶子节点	int	N	0-否 1-是；只有一级填0；
	private Integer isValid; //is_valid	有效标志	int	N	1-有效 0-无效
	private SysUser sysDictCreator; //creator_id	创建人员	int	N	取自sys_user.id
	private Date createTime; //create_time	配置时间	date	N	
	private SysUser sysDictEditor; //editor_id	修改人员	int	Y	取自sys_user.id
	private Date editTime; //edit_time	修改时间	date	Y	
	private Integer levelNo; //层级
	private Integer isInitianlize; //是否为初始化数据 0：否  1：是  
	private List<ServiceDictInfo> ChildrenList = new ArrayList<ServiceDictInfo>();//字列表
	
    private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	private Date editBeginDate;		// 修改开始日期
	private Date editEndDate;		// 修改结束日期
	
	private String showSequence; //显示序号

	/**
	 * 封装参数数据类型,
	 * 
	 */
	private List<Integer> conditionType;
	                                                                             

	public Integer getSysDictId() {
		return sysDictId;
	}
	public void setSysDictId(Integer sysDictId) {
		this.sysDictId = sysDictId;
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
	public SysDictInfo getParent() {
		return parent;
	}
	public void setParent(SysDictInfo parent) {
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
	public SysUser getSysDictCreator() {
		return sysDictCreator;
	}
	public void setSysDictCreator(SysUser sysDictCreator) {
		this.sysDictCreator = sysDictCreator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public SysUser getSysDictEditor() {
		return sysDictEditor;
	}
	public void setSysDictEditor(SysUser sysDictEditor) {
		this.sysDictEditor = sysDictEditor;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public List<ServiceDictInfo> getChildrenList() {
		return ChildrenList;
	}
	public void setChildrenList(List<ServiceDictInfo> childrenList) {
		ChildrenList = childrenList;
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
	public Integer getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
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
	public void setShowSequence(String showSequence) {
		this.showSequence = showSequence;
	}
	public String getShowSequence() {
		return showSequence;
	}
	public List<Integer> getConditionType() {
		return conditionType;
	}
	public void setConditionType(List<Integer> conditionType) {
		this.conditionType = conditionType;
	}
	public Integer getIsInitianlize() {
		return isInitianlize;
	}
	public void setIsInitianlize(Integer isInitianlize) {
		this.isInitianlize = isInitianlize;
	}
	@JsonIgnore
	public static void sortList(List<SysDictInfo> list, List<SysDictInfo> sourcelist, Integer parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			SysDictInfo sysDictInfo = sourcelist.get(i);
			if (sysDictInfo.getParent()!=null && sysDictInfo.getParent().getSysDictId()!=null
					&& sysDictInfo.getParent().getSysDictId().equals(parentId)){
				list.add(sysDictInfo);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						SysDictInfo child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getSysDictId()!=null
								&& child.getParent().getSysDictId().equals(sysDictInfo.getSysDictId())){
							sortList(list, sourcelist, sysDictInfo.getSysDictId(), true);
							break;
						}
					}
				}
			}
		}
	}
	
	//处理下级序号
	public static void addChildrenSeq(List<SysDictInfo> list,Integer parentId){
		    int countNo = 1;
			for(int i=0; i<list.size(); i++){
				SysDictInfo sysDictInfo = list.get(i);
				if(sysDictInfo.getParent()!=null && sysDictInfo.getParent().getSysDictId()!=null
						&& sysDictInfo.getParent().getSysDictId().equals(parentId)){
					//找出该父类
					for(SysDictInfo se:list){
						if(se.getSysDictId()==parentId){
							sysDictInfo.setShowSequence(se.getShowSequence()+Configurations.getStringProperty("childrenMark", ".")+countNo);
							countNo++;
						}
					}
					//继续获取子节点
					for (int j=0; j<list.size(); j++){
						SysDictInfo child = list.get(j);
						if (child.getParent()!=null && child.getParent().getSysDictId()!=null
								&& child.getParent().getSysDictId().equals(sysDictInfo.getSysDictId())){
							addChildrenSeq(list, sysDictInfo.getSysDictId());
							break;
						}
					}
				}
				
			}
	
	}
}
