package com.nis.domain.log;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:DkBehaviorLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月12日 下午5:09:21
 * @version V1.0
 */
public class DkBehaviorLog  extends BaseLogEntity<DkBehaviorLog> {
	private static final long serialVersionUID = -589390375816690510L;
	
	protected Integer labelProtoId; //协议类型id
	protected Integer labelOsId;//操作系统id
	protected Integer labelBsId; //浏览器id
	protected Integer labelBehavId; //行为id
	protected Integer labelAppId; //应用id
	protected Integer labelProtoSource; //应用id
	protected Integer labelBehavSource; //应用id
	protected Integer labelAppSource; //应用id
	
    @ApiModelProperty(value = "c2s包数", required = true)
    protected String c2sPktNum;
    @ApiModelProperty(value = "s2c包数", required = true)
    protected String s2cPktNum;
    @ApiModelProperty(value = "c2s字节数", required = true)
    protected String c2sByteNum;
    @ApiModelProperty(value = "s2c字节数", required = true)
    protected String s2cByteNum;
    
	
	public Integer getLabelProtoId() {
		return labelProtoId;
	}
	public void setLabelProtoId(Integer labelProtoId) {
		this.labelProtoId = labelProtoId;
	}
	public Integer getLabelOsId() {
		return labelOsId;
	}
	public void setLabelOsId(Integer labelOsId) {
		this.labelOsId = labelOsId;
	}
	public Integer getLabelBsId() {
		return labelBsId;
	}
	public void setLabelBsId(Integer labelBsId) {
		this.labelBsId = labelBsId;
	}
	public Integer getLabelBehavId() {
		return labelBehavId;
	}
	public void setLabelBehavId(Integer labelBehavId) {
		this.labelBehavId = labelBehavId;
	}
	public Integer getLabelAppId() {
		return labelAppId;
	}
	public void setLabelAppId(Integer labelAppId) {
		this.labelAppId = labelAppId;
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
	public Integer getLabelAppSource() {
		return labelAppSource;
	}
	public void setLabelAppSource(Integer labelAppSource) {
		this.labelAppSource = labelAppSource;
	}
	public Integer getLabelProtoSource() {
		return labelProtoSource;
	}
	
	public void setLabelProtoSource(Integer labelProtoSource) {
		this.labelProtoSource = labelProtoSource;
	}
	public Integer getLabelBehavSource() {
		return labelBehavSource;
	}
	public void setLabelBehavSource(Integer labelBehavSource) {
		this.labelBehavSource = labelBehavSource;
	}
 
    
}
