package com.nis.domain.log;

/**
 * @ClassName:NtcStreamMediaLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (wx)
 * @date 2018年7月16日 下午3:11:42
 * @version V1.0
 */
public class NtcStreamMediaLog  extends BaseLogEntity<NtcStreamMediaLog> {

	private static final long serialVersionUID = 1446933545638550170L;
	protected String url;
	protected String protocol;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
