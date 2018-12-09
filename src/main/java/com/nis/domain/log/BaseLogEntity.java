package com.nis.domain.log;

import com.nis.domain.BaseEntity;

public class BaseLogEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 8321337584518281424L;
	
	protected Integer cfgId;
	protected String foundTime; //发现时间，timestamp
	protected String recvTime; //接收时间，timestamp
	protected String transProto; //协议类型，从字典LOG_PROTOCOL取值
	protected Integer addrType; //ip地址类型，从字典IP_TYPE取值
	protected String dIp; //服务端ip地址
	protected String sIp; //客户端ip地址
	protected String dPort; //服务端ip端口
	protected String sPort; //客户端ip端口
	protected Integer service; //配置表的serviceId
	protected Integer entranceId; //出入口编号
	protected Integer deviceId; //串联设备编号
	protected Integer direction; //传输方向   0：域内->域外，1：域外->域内，从字典LOG_DIRECTION取值
	protected Integer streamDir; //流类型  0：c2s；1：s2c；2：double，从字典LOG_STREAMTYPE取值
	protected String capIp; //处理机IP
	protected String addrList; //嵌套地址列表
	protected String serverLocate;// 服务端地址定位信息
	protected String clientLocate;// 客户端地址定位信息
	protected String userRegion; //用户自定义
	protected String sAsn;//客户端ASN
	protected String dAsn;//服务端ASN
	protected String sSubscribeId;//客户端用户名
	protected String dSubscribeId;//服务端用户名
	protected String sceneFile;//现场日志文件地址
	
	protected Integer functionId;
	protected Integer action;
	
	protected String date;//配置界面日志总量查询时间
	//自定义字段
	protected String seltype;//选中类型,页面搜索用
	protected String searchFoundStartTime;//开始时间，格式为yyyy-mm-dd hh24:mi:ss
	protected String searchFoundEndTime;//结束时间，格式同上
	
	protected String isLogTotalSearch;//由配置列表点击日志总量进行查询的标识
	protected String orderBy;//排序参数
	
	//新增字段
	protected Integer encapType;//原始二层封装格式
	protected Integer linkId;//串联设备链路号
	protected String innerSmac;//MAC_IN_MAC的内层源MAC
	protected String innerDmac;//MAC_IN_MAC的内层目标MAC
	
	
	
	public Integer getEncapType() {
		return encapType;
	}
	public void setEncapType(Integer encapType) {
		this.encapType = encapType;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getInnerSmac() {
		return innerSmac;
	}
	public void setInnerSmac(String innerSmac) {
		this.innerSmac = innerSmac;
	}
	public String getInnerDmac() {
		return innerDmac;
	}
	public void setInnerDmac(String innerDmac) {
		this.innerDmac = innerDmac;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getFoundTime() {
		return foundTime;
	}
	public void setFoundTime(String foundTime) {
		this.foundTime = foundTime;
	}
	public String getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}
	public Integer getCfgId() {
		return cfgId;
	}
	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
	}
	public String getTransProto() {
		return transProto;
	}
	public void setTransProto(String transProto) {
		this.transProto = transProto;
	}
	public Integer getAddrType() {
		return addrType;
	}
	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}
	public String getdIp() {
		return dIp;
	}
	public void setdIp(String dIp) {
		this.dIp = dIp;
	}
	public String getsIp() {
		return sIp;
	}
	public void setsIp(String sIp) {
		this.sIp = sIp;
	}
	public String getdPort() {
		return dPort;
	}
	public void setdPort(String dPort) {
		this.dPort = dPort;
	}
	public String getsPort() {
		return sPort;
	}
	public void setsPort(String sPort) {
		this.sPort = sPort;
	}
	public Integer getService() {
		return service;
	}
	public void setService(Integer service) {
		this.service = service;
	}
	public Integer getEntranceId() {
		return entranceId;
	}
	public void setEntranceId(Integer entranceId) {
		this.entranceId = entranceId;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Integer getStreamDir() {
		return streamDir;
	}
	public void setStreamDir(Integer streamDir) {
		this.streamDir = streamDir;
	}
	public String getCapIp() {
		return capIp;
	}
	public void setCapIp(String capIp) {
		this.capIp = capIp;
	}
	public String getAddrList() {
		return addrList;
	}
	public void setAddrList(String addrList) {
		this.addrList = addrList;
	}
	public String getUserRegion() {
		return userRegion;
	}
	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public String getSeltype() {
		return seltype;
	}
	public void setSeltype(String seltype) {
		this.seltype = seltype;
	}
	public String getSearchFoundStartTime() {
		return searchFoundStartTime;
	}
	public void setSearchFoundStartTime(String searchFoundStartTime) {
		this.searchFoundStartTime = searchFoundStartTime;
	}
	public String getSearchFoundEndTime() {
		return searchFoundEndTime;
	}
	public void setSearchFoundEndTime(String searchFoundEndTime) {
		this.searchFoundEndTime = searchFoundEndTime;
	}
	public String getServerLocate() {
		return serverLocate;
	}
	public void setServerLocate(String serverLocate) {
		this.serverLocate = serverLocate;
	}
	public String getClientLocate() {
		return clientLocate;
	}
	public void setClientLocate(String clientLocate) {
		this.clientLocate = clientLocate;
	}
	public String getsAsn() {
		return sAsn;
	}
	public void setsAsn(String sAsn) {
		this.sAsn = sAsn;
	}
	public String getdAsn() {
		return dAsn;
	}
	public void setdAsn(String dAsn) {
		this.dAsn = dAsn;
	}
	public String getsSubscribeId() {
		return sSubscribeId;
	}
	public void setsSubscribeId(String sSubscribeId) {
		this.sSubscribeId = sSubscribeId;
	}
	public String getdSubscribeId() {
		return dSubscribeId;
	}
	public void setdSubscribeId(String dSubscribeId) {
		this.dSubscribeId = dSubscribeId;
	}
	public String getSceneFile() {
		return sceneFile;
	}
	public void setSceneFile(String sceneFile) {
		this.sceneFile = sceneFile;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getIsLogTotalSearch() {
		return isLogTotalSearch;
	}
	public void setIsLogTotalSearch(String isLogTotalSearch) {
		this.isLogTotalSearch = isLogTotalSearch;
	}
	@Override
	public String toString() {
		return "BaseLogEntity [cfgId=" + cfgId + ", foundTime=" + foundTime + ", recvTime=" + recvTime + ", transProto="
				+ transProto + ", addrType=" + addrType + ", dIp=" + dIp + ", sIp=" + sIp + ", dPort=" + dPort
				+ ", sPort=" + sPort + ", service=" + service + ", entranceId=" + entranceId + ", deviceId=" + deviceId
				+ ", direction=" + direction + ", streamDir=" + streamDir + ", capIp=" + capIp + ", addrList="
				+ addrList + ", serverLocate=" + serverLocate + ", clientLocate=" + clientLocate + ", userRegion="
				+ userRegion + ", sAsn=" + sAsn + ", dAsn=" + dAsn + ", sSubscribeId=" + sSubscribeId
				+ ", dSubscribeId=" + dSubscribeId + ", sceneFile=" + sceneFile + ", functionId=" + functionId
				+ ", action=" + action + ", date=" + date + ", seltype=" + seltype + ", searchFoundStartTime="
				+ searchFoundStartTime + ", searchFoundEndTime=" + searchFoundEndTime + ", isLogTotalSearch="
				+ isLogTotalSearch + "]";
	}
	
	
}
