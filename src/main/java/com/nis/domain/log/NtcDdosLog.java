package com.nis.domain.log;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcDdosLog extends BaseLogEntity<NtcDdosLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9080132009178985910L;

	@ApiModelProperty(value = "攻击类型", required = true)
    protected Integer attackType;
    @ApiModelProperty(value = "攻击起始时间", required = true)
    protected Date attackStartTime;
    @ApiModelProperty(value = "最后一次攻击包时间", required = true)
    protected Date lastAttackTime;
    @ApiModelProperty(value = "攻击最大流量, 包数/秒", required = true)
    protected String attackMaxPps;
    @ApiModelProperty(value = "攻击最大流量, bit数/秒", required = true)
    protected String attackMaxBps;
    @ApiModelProperty(value = "攻击累积包数", required = true)
    protected String attackTotalPkt;
    @ApiModelProperty(value = "攻击累积字节数", required = true)
    protected String attackTotalByte;
    @ApiModelProperty(value = "攻击流量是否被丢弃", required = true)
    protected Integer isBlock;
    
    
	public Integer getAttackType() {
		return attackType;
	}
	public void setAttackType(Integer attackType) {
		this.attackType = attackType;
	}
	public Date getAttackStartTime() {
		return attackStartTime;
	}
	public void setAttackStartTime(Date attackStartTime) {
		this.attackStartTime = attackStartTime;
	}
	public Date getLastAttackTime() {
		return lastAttackTime;
	}
	public void setLastAttackTime(Date lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}
	public String getAttackMaxPps() {
		return attackMaxPps;
	}
	public void setAttackMaxPps(String attackMaxPps) {
		this.attackMaxPps = attackMaxPps;
	}
	public String getAttackMaxBps() {
		return attackMaxBps;
	}
	public void setAttackMaxBps(String attackMaxBps) {
		this.attackMaxBps = attackMaxBps;
	}
	public String getAttackTotalPkt() {
		return attackTotalPkt;
	}
	public void setAttackTotalPkt(String attackTotalPkt) {
		this.attackTotalPkt = attackTotalPkt;
	}
	public String getAttackTotalByte() {
		return attackTotalByte;
	}
	public void setAttackTotalByte(String attackTotalByte) {
		this.attackTotalByte = attackTotalByte;
	}
	public Integer getIsBlock() {
		return isBlock;
	}
	public void setIsBlock(Integer isBlock) {
		this.isBlock = isBlock;
	}
    
}
