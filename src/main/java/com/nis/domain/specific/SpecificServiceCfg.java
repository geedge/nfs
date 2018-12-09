package com.nis.domain.specific;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.nis.util.Configurations;

/**
 * 系统特定服务信息表-记录特定服务信息(specific_service_cfg)
 * @author zsl
 *
 */
public class SpecificServiceCfg extends BaseEntity<SpecificServiceCfg>{

	private static final long serialVersionUID = -1133644323636425766L;
	
	private Integer specServiceId;  //spec_service_id	协议ID	int	N	主键，初始化
	private Integer specServiceCode; //协议编码	int	N	暂定，以后可扩展  1000：微信 , 1001：QQ
	private String specServiceName; //spec_service_name	协议名称	varchar(64)	N	
	private String specServiceDesc; //spec_service_desc	协议描述	varchar2(64)	N	
	private Integer isValid; //is_valid	有效标志	int	N	1-有效 0-无效
	private Date opTime; //op_time	操作时间	date	N	
	private SpecificServiceCfg parent; //parent_id	父节点id	int	N	0表示一级节点
	private Integer isLeaf; //is_leaf	是否是叶子节点	int	N	0否，1是，只有一级填0
	private Integer groupId; //group_id	maat端配置分组id	int	N	缺省0，表示未与maat分组同步
	private Integer cfgType;//配置类型，1，app;2,加密隧道；3，基础协议
	private Integer parentType;//父配置类型
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	private String showSequence;  //显示序号
	private String businessType;
	private Integer addFlag;//app界面添加标记
	
	public Integer getAddFlag() {
		return addFlag;
	}
	public void setAddFlag(Integer addFlag) {
		this.addFlag = addFlag;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Integer getParentType() {
		return parentType;
	}
	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}
	public Integer getCfgType() {
		return cfgType;
	}
	public void setCfgType(Integer cfgType) {
		this.cfgType = cfgType;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}
	public Integer getSpecServiceCode() {
		return specServiceCode;
	}
	public void setSpecServiceCode(Integer specServiceCode) {
		this.specServiceCode = specServiceCode;
	}
	public String getSpecServiceName() {
		return specServiceName;
	}
	public void setSpecServiceName(String specServiceName) {
		this.specServiceName = specServiceName;
	}
	public String getSpecServiceDesc() {
		return specServiceDesc;
	}
	public void setSpecServiceDesc(String specServiceDesc) {
		this.specServiceDesc = specServiceDesc;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	public SpecificServiceCfg getParent() {
		return parent;
	}
	public void setParent(SpecificServiceCfg parent) {
		this.parent = parent;
	}
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
	public String getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(String showSequence) {
		this.showSequence = showSequence;
	}
	@JsonIgnore
	public static void sortList(List<SpecificServiceCfg> list, List<SpecificServiceCfg> allList, Integer parentId, boolean cascade) {
	
		for(int i=0;i<allList.size();i++){
			SpecificServiceCfg ss = allList.get(i);
			//System.out.println("处理"+ss.getSpecServiceName()+"id:"+ss.getSpecServiceId()+"父ID:"+ss.getParent().getSpecServiceId()+"条件》》"+parentId);
			if(ss!=null&&ss.getParent()!=null&&ss.getParent().getSpecServiceId()!=null&&ss.getParent().getSpecServiceId().equals(parentId)){
				list.add(ss);
				//System.out.println("list加入"+ss.getSpecServiceName()+"id:"+ss.getSpecServiceId()+"父ID:"+ss.getParent().getSpecServiceId());
				if(cascade){
					for(int j=0;j<allList.size();j++){
						SpecificServiceCfg child = allList.get(j);
						if(child!=null&&child.getParent()!=null&&child.getParent().getSpecServiceId()!=null&&child.getParent().getSpecServiceId().equals(ss.getSpecServiceId())){
							sortList(list,allList,ss.getSpecServiceId(),true);
							break;
						}
					}
				}
			}
			
		}
		
	}
	//处理下级序号
	public static void addChildrenSeq(List<SpecificServiceCfg> list,Integer parentId){
		    int countNo = 1;
			for(int i=0; i<list.size(); i++){
				SpecificServiceCfg ss = list.get(i);
				if(ss.getParent()!=null && ss.getParent().getSpecServiceId()!=null
						&& ss.getParent().getSpecServiceId().equals(parentId)){
					//找出该父类
					for(SpecificServiceCfg se:list){
						if(se.getSpecServiceId()==parentId){
							ss.setShowSequence(se.getShowSequence()+Configurations.getStringProperty("childrenMark", ".")+countNo);
							countNo++;
						}
					}
					//继续获取子节点
					for (int j=0; j<list.size(); j++){
						SpecificServiceCfg child = list.get(j);
						if (child.getParent()!=null && child.getParent().getSpecServiceId()!=null
								&& child.getParent().getSpecServiceId().equals(ss.getSpecServiceId())){
							addChildrenSeq(list, ss.getSpecServiceId());
							break;
						}
					}
				}
				
			}
			
	}

	
	

}
