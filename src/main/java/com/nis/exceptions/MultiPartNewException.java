package com.nis.exceptions;

import static java.lang.String.format;

import java.util.Properties;

import org.springframework.web.multipart.MultipartException;

public class MultiPartNewException extends MultipartException {

	private static final long serialVersionUID = 8922896505285617384L;
	
	public MultiPartNewException(String msg) {
		super(msg);
	}
	public MultiPartNewException(Properties prop,Throwable ex) {
		super(prop.get("file_upload_error").toString(),ex);
	}
	public MultiPartNewException(String msg,Throwable ex) {
		
		super(msg,ex);
	}
	
	 /**
     * @param message   The detail message.
     * @param actual    The actual request size.
     * @param permitted The maximum permitted request size.
     */
	public MultiPartNewException(String msg,long actual,long permitted,Properties prop,Throwable ex) {
		
		super(format(prop.get("total_file_upload_size_error").toString(),
				Long.valueOf(actual),Long.valueOf(permitted)),ex);
	}
	
	/**single file
	 * @param fileName
	 * @param message   The detail message.
	 * @param actual    The actual request size.
	 * @param permitted The maximum permitted request size.
	 */
	public MultiPartNewException(String msg,String fileName,long actual,long permitted,Properties prop,Throwable ex) {
		
		super(format(prop.get("single_file_upload_size_error").toString(),
				fileName,Long.valueOf(actual), Long.valueOf(permitted)),ex);
	}
	
	/**single file
	 * @param fileName
	 * @param message   The detail message.
	 * @param permitted fileType.
	 */
	public MultiPartNewException(String msg,String fileName,String fileType,Properties prop,Throwable ex) {
		
		super(format(prop.get("file_upload_type_error").toString(),
				fileName, fileType),ex);
	}
	
	
}
