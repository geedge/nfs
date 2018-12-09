package com.nis.interceptor;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.UploadContext;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsFileUploadSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.nis.exceptions.MultiPartNewException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.StringUtil;


public class MultipartFileIntercepter extends CommonsMultipartResolver{
	Logger logger = Logger.getLogger(MultipartFileIntercepter.class);
	private Integer functionId=0;
	
	protected CommonsFileUploadSupport.MultipartParsingResult parseRequest(HttpServletRequest request)throws MultipartException{
	    String encoding = determineEncoding(request);
	    //样例文件URI
	    boolean isSampleFileUpload = request.getRequestURI().toLowerCase().contains(Constants.SAMPLE_UPLOAD_URL_KEYWORD);
	    //摘要文件URI
	    boolean isDigestFileUpload = request.getRequestURI().toLowerCase().contains(Constants.DIGEST_UPLOAD_URL_KEYWORD);
	    //证书文件URI
	    boolean isCertFileUpload = request.getRequestURI().toLowerCase().contains(Constants.CERT_UPLOAD_URL_KEYWORD);
	    FileUpload fileUpload = this.prepareFileUpload(encoding,isSampleFileUpload,isDigestFileUpload,isCertFileUpload);
	    long fileUploadTotalSize=0l;
	    Properties languageProp=getMsgProp();
	    MultipartParsingResult multipartParsingResult=null;
	    try {
	    		if(isSampleFileUpload || isDigestFileUpload || isCertFileUpload){
	    			/*****************预先获取上传文件的总的大小************/
			    	FileUpload fileUploadTotal = fileUpload;
			    	ServletRequestContext ctx = new ServletRequestContext(request);
			    	InputStream input=ctx.getInputStream();
			        @SuppressWarnings("deprecation") // still has to be backward compatible
			        int contentLengthInt = ctx.getContentLength();
			
			        fileUploadTotalSize = UploadContext.class.isAssignableFrom(input.getClass())
			                                 // Inline conditional is OK here CHECKSTYLE:OFF
			                                 ? ((UploadContext) ctx).contentLength()
			                                 : contentLengthInt;
			                                 // CHECKSTYLE:ON
			        logger.info("上传文件总大小为"+fileUploadTotalSize);
			        /*****************预先获取上传文件的总的大小************/
			        //验证文件总大小
			        if(fileUploadTotalSize > fileUpload.getSizeMax() ){
			        	throw new MultiPartNewException("", fileUploadTotalSize, fileUpload.getSizeMax(),languageProp, null);
			        }
			        //设置文件上传SizeMax为总文件的大小，否则无法通过SizeLimit(总的文件大小)校验，就无法进行下面单个文件大小的校验
			        fileUploadTotal.setSizeMax(fileUploadTotalSize);  
			        
					List fileItems = ((ServletFileUpload) fileUploadTotal).parseRequest(request);
					multipartParsingResult=parseFileItems(fileItems, encoding);
					MultiValueMap<String, MultipartFile> files=multipartParsingResult.getMultipartFiles();
					//单个文件验证文件格式和文件大小
					validFileList(files,fileUpload,languageProp,isSampleFileUpload);
	    		}else{
	    			List fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
	    			multipartParsingResult=this.parseFileItems(fileItems, encoding);
	    		}
				
				return multipartParsingResult;
				
			} catch (FileUploadBase.SizeLimitExceededException ex) {
				if(fileUploadTotalSize==0l){
					throw new MultiPartNewException(languageProp, ex);
				}else{
					throw new MultiPartNewException("",fileUploadTotalSize,fileUpload.getSizeMax(),languageProp,ex);
				}
			} catch (FileUploadException ex) {
				throw new MultiPartNewException(languageProp, ex);
			}catch (IOException ex) {
				throw new MultiPartNewException(languageProp, ex);
			}catch (MultiPartNewException ex) {
				throw ex;
			}catch (Exception ex) {
				throw new MultiPartNewException(languageProp, ex);
			}
	   
	  }
	/**
	 * 校验文件大小、类型
	 * @return
	 * @throws FileSizeLimitExceededException 
	 */
	protected void validFileList(MultiValueMap<String, MultipartFile> multipartFile,FileUpload fileUpload,Properties prop,boolean isSampleFileUpload) throws FileSizeLimitExceededException{
		String errorInfo="";
		String fileTypes="";
		long fileMaxSize=0l;
		String privateFileTypes="";
		long privateFileMaxSize=0l;
		if(functionId.equals(52)){ //digest
			fileTypes=Constants.DIGEST_FILE_TYPE;
			fileMaxSize=Constants.DIGEST_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(28)){ //audio
			fileTypes=Constants.AUDIO_FILE_TYPE;
			fileMaxSize=Constants.AUDIO_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(29)){ //video
			fileTypes=Constants.VIDEO_FILE_TYPE;
			fileMaxSize=Constants.VIDEO_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(30)){ //picture
			fileTypes=Constants.PICTURE_FILE_TYPE;
			fileMaxSize=Constants.PICTURE_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(31)){ //voip
			fileTypes=Constants.VOIP_FILE_TYPE;
			fileMaxSize=Constants.VOIP_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(630)){ //说话人识别
			fileTypes=Constants.SPEAKER_FILE_TYPE;
			fileMaxSize=Constants.SPEAKER_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(631)){ //台标识别
			fileTypes=Constants.LOGO_FILE_TYPE;
			fileMaxSize=Constants.LOGO_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(632)){ //人脸识别
			fileTypes=Constants.PICTURE_FILE_TYPE;//同图片类型
			fileMaxSize=Constants.FACE_SINGLE_FILE_MAX_SIZE;
		}else if(functionId.equals(570)){ //证书管理
			fileTypes=Constants.PUBLIC_FILE_TYPE;
			fileMaxSize=Constants.PUBLIC_SINGLE_FILE_MAX_SIZE;
			privateFileTypes=Constants.PRIVATE_FILE_TYPE;
			privateFileMaxSize=Constants.PRIVATE_SINGLE_FILE_MAX_SIZE;
		}else{
			return;
		}
		logger.info("functionId:"+functionId);
		logger.info("fileType:"+fileTypes);
		logger.info("singleFileMaxSize:"+fileMaxSize);
		
		for (String fileName : multipartFile.keySet()) {
			MultipartFile file= multipartFile.getFirst(fileName);
			//私钥文件校验
			if(!StringUtil.isEmpty(fileName) && fileName.toLowerCase().equals("privatekeyfilei")){
				//文件类型错误
				if((!StringUtil.isEmpty(privateFileTypes)) && (privateFileTypes.indexOf(","+FileUtils.getSuffix(file.getOriginalFilename().toLowerCase(), false)+",") == -1)){
					throw new MultiPartNewException(errorInfo,file.getOriginalFilename(),privateFileTypes.substring(0,privateFileTypes.length()-1).substring(1),prop,null);
				}
				if(fileMaxSize >0 && file.getSize() > privateFileMaxSize){
					throw new MultiPartNewException(errorInfo, file.getOriginalFilename(), file.getSize(), privateFileMaxSize,prop, null);
				}
			//其他文件校验
			}else{
				//文件类型错误
				if((!StringUtil.isEmpty(fileTypes)) && fileTypes.indexOf(","+FileUtils.getSuffix(file.getOriginalFilename().toLowerCase(), false)+",") == -1){
					throw new MultiPartNewException(errorInfo,file.getOriginalFilename(),fileTypes.substring(0,fileTypes.length()-1).substring(1),prop,null);
				}
				if(fileMaxSize > 0l && file.getSize() > fileMaxSize){
					throw new MultiPartNewException(errorInfo, file.getOriginalFilename(), file.getSize(), fileMaxSize,prop, null);
				}
			}
			
		}
	}
	/**
	 * 构造FileUpload
	 * @param encoding
	 * @param isSampleFileUpload
	 * @return
	 */
	protected FileUpload prepareFileUpload(String encoding,boolean isSampleFileUpload,boolean isDigestFileUpload,boolean isCertFileUpload) {
		FileUpload fileUpload = getFileUpload();
		FileUpload actualFileUpload = fileUpload;
		if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
			actualFileUpload = newFileUpload(getFileItemFactory());
			actualFileUpload.setHeaderEncoding(encoding);
			if(isSampleFileUpload){	
				actualFileUpload.setSizeMax(Constants.SAMPLE_TOTAL_FILE_MAX_SIZE);
			}else if(isDigestFileUpload){
				actualFileUpload.setSizeMax(Constants.DIGEST_TOTAL_FILE_MAX_SIZE);
			}else if(isCertFileUpload){
				actualFileUpload.setSizeMax(Constants.CERT_TOTAL_FILE_MAX_SIZE);
			}else{
				actualFileUpload.setSizeMax(fileUpload.getSizeMax());
			}
		}
		return actualFileUpload;
	}
	
	/**
	 * 获取国际化配置文件
	 * @return
	 */
	public Properties getMsgProp(){
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
			logger.error("未知i18n消息配置文件,请确定文件是否存在!",e);
		}
		return msgProp;
	}
	protected CommonsFileUploadSupport.MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
		Map<String, String[]> multipartParameters = new HashMap<String, String[]>();
		Map<String, String> multipartParameterContentTypes = new HashMap<String, String>();

		// Extract multipart files and multipart parameters.
		for (FileItem fileItem : fileItems) {
			if (fileItem.isFormField()) {
				String value;
				String partEncoding = determineEncoding(fileItem.getContentType(), encoding);
				if (partEncoding != null) {
					try {
						value = fileItem.getString(partEncoding);
					}
					catch (UnsupportedEncodingException ex) {
						if (logger.isDebugEnabled()) {
							logger.warn("Could not decode multipart item '" + fileItem.getFieldName() +
									"' with encoding '" + partEncoding + "': using platform default");
						}
						value = fileItem.getString();
					}
				}
				else {
					value = fileItem.getString();
				}
				String[] curParam = multipartParameters.get(fileItem.getFieldName());
				if (curParam == null) {
					// simple form field
					multipartParameters.put(fileItem.getFieldName(), new String[] {value});
					if(fileItem.getFieldName().toLowerCase().equals("functionid")){
						functionId=StringUtil.isEmpty(value) ? 0:Integer.parseInt(value);
					}
				}
				else {
					// array of simple form fields
					String[] newParam = StringUtils.addStringToArray(curParam, value);
					multipartParameters.put(fileItem.getFieldName(), newParam);
				}
				multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
			}
			else {
				// multipart file field
				CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
				if(!StringUtils.isEmpty(file.getOriginalFilename())){
					multipartFiles.add(file.getName(), file);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() +
							" bytes with original filename [" + file.getOriginalFilename() + "], stored " +
							file.getStorageDescription());
				}
			}
		}
		return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
	}
	
	private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
		if (!StringUtils.hasText(contentTypeHeader)) {
			return defaultEncoding;
		}
		MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
		Charset charset = contentType.getCharSet();
		return (charset != null ? charset.name() : defaultEncoding);
	}
	
}
