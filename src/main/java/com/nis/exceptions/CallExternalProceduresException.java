package com.nis.exceptions;

import java.util.Properties;

import org.springframework.context.i18n.LocaleContextHolder;

import com.nis.util.Configurations;

/**
 * 调用外部程序出错异常
 * @author dell
 *
 */
public class CallExternalProceduresException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String message=getMsgProp().getProperty("call_external_procedures_failed", "Call external procedures failed");
	public CallExternalProceduresException() {
		super(message);
	}
	public CallExternalProceduresException(String message) {
		super(message);
		
	}
	public CallExternalProceduresException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 获取国际化配置文件
	 * @return
	 */
	public static Properties getMsgProp(){
		Properties msgProp = new Properties();
		try {
			String language = LocaleContextHolder.getLocale().getLanguage();
			if(language.equals("zh_cn")||language.equals("zh")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
			}else if(language.equals("ru")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
			}else{
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			}
			
		} catch (Exception e) {
			msgProp = null;
		}
		return msgProp;
	}
}