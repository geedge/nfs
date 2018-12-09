package com.nis.domain.log;

public class NtcMailLog extends BaseLogEntity<NtcMailLog> {

	private static final long serialVersionUID = -2848016592975339617L;
	
	private String mailProto; //邮件协议类型，不可空  SMTP/POP3/IMAP4
	private String mailFrom;
	private String mailTo;
	private String subject;
	private String emlFile;
	private String emlKey;
	
	
	public String getEmlKey() {
		return emlKey;
	}
	public void setEmlKey(String emlKey) {
		this.emlKey = emlKey;
	}
	public String getMailProto() {
		return mailProto;
	}
	public void setMailProto(String mailProto) {
		this.mailProto = mailProto;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmlFile() {
		return emlFile;
	}
	public void setEmlFile(String emlFile) {
		this.emlFile = emlFile;
	}
	
}
