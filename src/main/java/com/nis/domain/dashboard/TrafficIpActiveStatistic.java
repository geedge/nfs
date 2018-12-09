package com.nis.domain.dashboard;

import com.nis.util.excel.ExcelField;

public class TrafficIpActiveStatistic{
	Integer id;
	@ExcelField(title="IP",sort=10)
	String ipAddr;
	@ExcelField(title="Area",sort=20)
	String areaId;
	@ExcelField(title="Number",sort=30)
	Long linkNum;
	@ExcelField(title="C2S(Pkt)",sort=40)
	Long c2sPktNum;
	@ExcelField(title="S2C(Pkt)",sort=50)
	Long s2cPktNum;
	@ExcelField(title="C2S(Byte)",sort=60)
	Long c2sByteLen;
	@ExcelField(title="S2C(Byte)",sort=70)
	Long s2cByteLen;
	@ExcelField(title="Time",sort=80)
	String statTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatTime() {
		return statTime;
	}
	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	public TrafficIpActiveStatistic(){
		super();
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public Long getLinkNum() {
		return linkNum;
	}
	public void setLinkNum(Long linkNum) {
		this.linkNum = linkNum;
	}
	public Long getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(Long c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public Long getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(Long s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public Long getC2sByteLen() {
		return c2sByteLen;
	}
	public void setC2sByteLen(Long c2sByteLen) {
		this.c2sByteLen = c2sByteLen;
	}
	public Long getS2cByteLen() {
		return s2cByteLen;
	}
	public void setS2cByteLen(Long s2cByteLen) {
		this.s2cByteLen = s2cByteLen;
	}
	
}