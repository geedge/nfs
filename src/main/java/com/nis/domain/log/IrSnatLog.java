package com.nis.domain.log;

public class IrSnatLog extends BaseLogEntity<IrSnatLog> {

	private static final long serialVersionUID = 5860838446015457328L;
	
	protected String natIp;//	复用的ip地址
	protected String creatTime;//	链接建立时间
	protected String closeTime;//	链接结束时间
	protected String c2sPktNum;//	c2s包数
	protected String s2cPktNum;//	s2c包数
	protected String c2sByteNum;//	c2s字节数
	protected String s2cByteNum;//	s2c字节数
	 
	
	public String getNatIp() {
		return natIp;
	}
	public void setNatIp(String natIp) {
		this.natIp = natIp;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(String c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public String getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(String s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public String getC2sByteNum() {
		return c2sByteNum;
	}
	public void setC2sByteNum(String c2sByteNum) {
		this.c2sByteNum = c2sByteNum;
	}
	public String getS2cByteNum() {
		return s2cByteNum;
	}
	public void setS2cByteNum(String s2cByteNum) {
		this.s2cByteNum = s2cByteNum;
	}
	
	
	












}
