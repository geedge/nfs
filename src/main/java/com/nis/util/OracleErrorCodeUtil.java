package com.nis.util;

import java.util.HashMap;
import java.util.Map;

public class OracleErrorCodeUtil {
	/**
	 * 返回异常信息内容
	 * 
	 * @param e
	 * @return
	 */
	public static String getOraCode(Exception e) {
		if (e == null) {
			return "";
		}

		String errorMessage = e.getMessage();
		if (null != errorMessage && errorMessage.length() > 0) {
			int index = errorMessage.toUpperCase().indexOf("ORA-");
			if (index != -1) {
				return errorMessage.substring(index + 4, index + 9);

			}
		}
		return "";
	}
}
