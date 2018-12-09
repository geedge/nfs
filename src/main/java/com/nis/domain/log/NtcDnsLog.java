package com.nis.domain.log;

public class NtcDnsLog extends BaseLogEntity<NtcDnsLog> {

	private static final long serialVersionUID = -3763888115864065019L;

	private Integer qr;
	private Integer rd;
	private Integer ra;
	private String rr;
	private Integer qType;
	private Integer qclass;
	private Integer opcode;
	private String qname;
	private String cname;
	private Integer dnsSub;
	
	/*以下字段无需反馈到界面*/
	private String cheatType;
	private Integer cheatRcode;
	private String cheatStrategy;
	private String cheatRr;
	public Integer getQr() {
		return qr;
	}
	public void setQr(Integer qr) {
		this.qr = qr;
	}
	public Integer getRd() {
		return rd;
	}
	public void setRd(Integer rd) {
		this.rd = rd;
	}
	public Integer getRa() {
		return ra;
	}
	public void setRa(Integer ra) {
		this.ra = ra;
	}
	public String getRr() {
		return rr;
	}
	public void setRr(String rr) {
		this.rr = rr;
	}
	public Integer getqType() {
		return qType;
	}
	public void setqType(Integer qType) {
		this.qType = qType;
	}
	public Integer getQclass() {
		return qclass;
	}
	public void setQclass(Integer qclass) {
		this.qclass = qclass;
	}
	public Integer getOpcode() {
		return opcode;
	}
	public void setOpcode(Integer opcode) {
		this.opcode = opcode;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Integer getDnsSub() {
		return dnsSub;
	}
	public void setDnsSub(Integer dnsSub) {
		this.dnsSub = dnsSub;
	}
	public String getCheatType() {
		return cheatType;
	}
	public void setCheatType(String cheatType) {
		this.cheatType = cheatType;
	}
	public Integer getCheatRcode() {
		return cheatRcode;
	}
	public void setCheatRcode(Integer cheatRcode) {
		this.cheatRcode = cheatRcode;
	}
	public String getCheatStrategy() {
		return cheatStrategy;
	}
	public void setCheatStrategy(String cheatStrategy) {
		this.cheatStrategy = cheatStrategy;
	}
	public String getCheatRr() {
		return cheatRr;
	}
	public void setCheatRr(String cheatRr) {
		this.cheatRr = cheatRr;
	}
	
	
}
