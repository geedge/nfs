package com.nis.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.HttpClientUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.log.SearchReport;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.httpclient.ClientUtil;
import com.nis.util.httpclient.HttpClientUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class ConfigServiceUtil {
	private static Logger logger = LoggerFactory.getLogger(ConfigServiceUtil.class);
	/*public static class MapTypeAdapter extends TypeAdapter<Object> {
		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<Object>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;

			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<String, Object>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;

			case STRING:
				return in.nextString();

			case NUMBER:
				String temp = in.nextString();
				BigDecimal dbNum = new BigDecimal(temp);
				BigDecimal maxLong = new BigDecimal(Long.MAX_VALUE);
				BigDecimal maxInteger = new BigDecimal(Integer.MAX_VALUE);
				// 数字超过long的最大值，返回BigDecimal类型
				if (dbNum.compareTo(maxLong)==1) {
					return dbNum;
				}else if(dbNum.compareTo(maxInteger)==1){
					long lngNum = Long.parseLong(temp);
					return lngNum;
				}else{
					int lngNum = Integer.parseInt(temp);
					return lngNum;
				}

			case BOOLEAN:
				return in.nextBoolean();

			case NULL:
				in.nextNull();
				return null;

			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			// 序列化无需实现
		}

	}*/
	/**
	 * 从后台服务获取compileid,groupid,regionid
	 * @param type 1是compileid,2是groupid,3是regionid
	 * @return
	 */
	public static List<Integer> getId(int type,int num) throws MaatConvertException {
		Response response=null;
		List<Integer> list = new ArrayList();
		try {
			logger.warn("get ids start");
			long start=System.currentTimeMillis();
			String result = null;
			String url = "";
			if (type == 1) {
				url = Constants.SERVICE_URL+Constants.CONFIG_ID_SOURCES+"?sourceName=CONFIG_COMPILE&num="+num;
			} else if (type == 2) {
				url = Constants.SERVICE_URL+Constants.CONFIG_ID_SOURCES+"?sourceName=CONFIG_GROUP&num="+num;
			} else if (type == 3) {
				url = Constants.SERVICE_URL+Constants.CONFIG_ID_SOURCES+"?sourceName=CONFIG_REGION&num="+num;
			}
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("getId url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.get();
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				logger.info("get result success");
//				result = "{\"status\":200,\"businessCode\":2000,\"reason\":\"数据获取操作成功\","
//						+ "\"msg\":\"配置ID获取成功\",\"fromuri\":\"/galaxy/service/cfg/v1/configPzIdSources\","
//						+ "\"traceCode\":\"2018052409232108368751\",\"data\":{\"sourceName\":\"CONFIG_COMPILE\",\"num\":1,\"pzIdList\":[22]}}";
				JSONObject resObject = JSONObject.fromObject(result);
				ToMaatResult bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
				if(!StringUtil.isEmpty(bean)){
					if(!StringUtil.isEmpty(bean.getData())){
						list = bean.getData().getPzIdList();
					}else{
						throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
					}
				}else{
					throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
				}
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
			long end=System.currentTimeMillis();
			logger.warn("get ids finish,cost:"+(end-start));
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
                response.close();
            }
		} 
		return list;
	}
	/**
	 * MAAT配置提交
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static ToMaatResult postMaatCfg(String params) throws MaatConvertException{
		Response response=null;
		ToMaatResult bean = null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL+Constants.MAAT_CFG;
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("postMaatCfg url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.post(Entity.entity(params, MediaType.APPLICATION_JSON));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				logger.info("get result success");
				JsonConfig config=new JsonConfig();
				config.setExcludes(new String[]{"configCompileList"});
				JSONObject resObject = JSONObject.fromObject(result,config);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
                response.close();
            }
		} 
		
		return bean;
	}
	/**
	 * 回调配置提交
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static ToMaatResult postCallbackCfg(String params) throws MaatConvertException{
		Response response=null;
		ToMaatResult bean = null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL+Constants.CALLBACK_CFG;
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("postCallbackCfg url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.post(Entity.entity(params, MediaType.APPLICATION_JSON));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				JSONObject resObject = JSONObject.fromObject(result);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
		
	}
	/**
	 * 文件配置提交
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static ToMaatResult postFileCfg(String params,File file,String fileDesc) throws MaatConvertException{
		Response response=null;
		ToMaatResult bean = null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL+Constants.FILE_UPLOAD_CFG;
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("postFileCfg url:"+url);
			FormDataMultiPart formDataMultiPart=new FormDataMultiPart();
			FileDataBodyPart bodyPart=new FileDataBodyPart("file",file);
			formDataMultiPart.bodyPart(bodyPart);
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("File-Desc",fileDesc);
			try {
				response= header.post(Entity.entity(formDataMultiPart, formDataMultiPart.getMediaType()));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				JSONObject resObject = JSONObject.fromObject(result);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>"+result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
		
	}
	/**
	 * 配置删除
	 * @param params
	 * @param type,1表示maat配置取消，2表示回调配置取消
	 * @return
	 */
	public static ToMaatResult delete(String params,Integer type) throws MaatConvertException{
		Response response=null;
		ToMaatResult bean = null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL;
			if(type==1){
				url = url+Constants.MAAT_CFG;
			}else if(type==2){
				url = url+Constants.CALLBACK_CFG;
			}
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("delete url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.delete(); 
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response != null &&  response.getStatus() == 200){
				JSONObject resObject = JSONObject.fromObject(result);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
		
	}
	/**
	 * 配置状态修改
	 * @param params
	 * @param type,1表示maat配置取消，2表示回调配置取消，3,分组复用删除域
	 * @return
	 */
	public static ToMaatResult put(String params,Integer type) throws MaatConvertException{
		ToMaatResult bean = null;
		Response response=null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL;
			if(type==1){
				url = url+Constants.MAAT_CFG;
			}else if(type==2){
				url = url+Constants.CALLBACK_CFG;
			}else if(type==3) {
				url = url+Constants.GROUP_REUSE_SOURCES;
			}
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("put url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.put(Entity.entity(params, MediaType.APPLICATION_JSON)); 
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				JSONObject resObject = JSONObject.fromObject(result);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
	}
	/**
	 * 配置状态修改patch
	 * @param params
	 * @param type,1表示maat配置取消，2表示回调配置取消，3,分组复用删除域
	 * @return
	 */
	public static String patch(String params,Integer type) throws MaatConvertException{
		String result = null;
		try {
			String url = Constants.SERVICE_URL;
			if(type==1){
				url = url+Constants.MAAT_CFG;
			}else if(type==2){
				url = url+Constants.CALLBACK_CFG;
			}else if(type==3) {
				url = url+Constants.GROUP_REUSE_SOURCES;
			}
			logger.info("put url:"+url);
			//创建连接
			try {
				result = HttpClientUtil.patch(url, params);
//				JSONObject resObject = JSONObject.fromObject(result);
//				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
				//获取响应结果
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			} 
		} catch (Exception e) {
			throw e;
		} 
		
		return result;
	}
	/**
	 * 信息获取
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static ToMaatResult getFileDigest(String params,File file,String fileDesc) throws MaatConvertException{
		ToMaatResult bean = null;
		Response response=null;
		try {
			String result = null;
			String url = Constants.SERVICE_URL+Constants.FILE_DIGEST_CFG;
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("getFileDigest url:"+url);
			FormDataMultiPart formDataMultiPart=new FormDataMultiPart();
			FileDataBodyPart bodyPart=new FileDataBodyPart("file",file);
			formDataMultiPart.bodyPart(bodyPart);
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("File-Desc",fileDesc);
			try {
				response= header.post(Entity.entity(formDataMultiPart, formDataMultiPart.getMediaType()));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				logger.info("获取文件摘要响应结果"+result);
				JSONObject resObject = JSONObject.fromObject(result);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
	}
	/**
	 * 分组复用域配置新增
	 * @param params
	 * @return
	 * @throws MaatConvertException
	 */
	public static ToMaatResult postGroupReuseSources(String params) throws MaatConvertException{
		ToMaatResult bean = null;
		Response response=null;
		try {
			logger.warn("postGroupReuseSources start");
			long start=System.currentTimeMillis();
			String result = null;
			String url = Constants.SERVICE_URL+Constants.GROUP_REUSE_SOURCES;
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("postGroupReuseSources url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response=  header.post(Entity.entity(params, MediaType.APPLICATION_JSON));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				JsonConfig config=new JsonConfig();
				config.setExcludes(new String[]{"configCompileList"});
				JSONObject resObject = JSONObject.fromObject(result,config);
				bean = (ToMaatResult) JSONObject.toBean(resObject,ToMaatResult.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
			long end=System.currentTimeMillis();
			logger.warn("postGroupReuseSources end,cost:"+(end-start));
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return bean;
	}
	/**
	 * 
	 * getReport(配置日志总量统计查询)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param type 1-	配置命中总量业务2-	配置报表业务
	 * @param ids
	 * @param serviceIds
	 * @param searchReportStartTime
	 * @param searchReportEndTime
	 * @return
	 * @throws MaatConvertException 
	 *ReportResultLog
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getReport(int type,String ids,String serviceIds,String searchReportStartTime,String searchReportEndTime) throws MaatConvertException{
		String result = null;
		Response response=null;
		try {
			if(StringUtils.isBlank(ids)||StringUtils.isBlank(serviceIds)){
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			UriBuilder builder=UriBuilder.fromPath(Constants.LOG_BASE_URL+Constants.NTC_PZ_REPORT);
			builder.queryParam("searchBusinessType",type);
			builder.queryParam("searchService",serviceIds);
			builder.queryParam("searchCfgId",ids);
			if(StringUtils.isNotBlank(searchReportStartTime)) {
				builder.queryParam("searchReportStartTime",searchReportStartTime);
			}
			if(StringUtils.isNotBlank(searchReportEndTime)) {
				builder.queryParam("searchReportEndTime",searchReportEndTime);
			}
			URI uri=builder.build();
			//创建连接
			ClientUtil.initClient();
			Client client=ClientUtil.getClient();
			WebTarget wt = client.target(uri);
			logger.info("getReport url:"+uri.toString());
			Builder header = wt.request();
			try {
				response=  header.get(); 
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				result= response.readEntity(String.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return result;
	}
	public static String getReport(String reportUrl, SearchReport searchCondition) throws MaatConvertException{
//		if(StringUtils.isBlank(searchCondition.getSearchService())){
//			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
//		}
		Response response=null;
		String result = null;
		try {
			UriBuilder builder=UriBuilder.fromPath(reportUrl);
			builder.queryParam("searchBusinessType",searchCondition.getSearchBusinessType());
			if(StringUtils.isNotBlank(searchCondition.getSearchService())) {
				builder.queryParam("searchService",searchCondition.getSearchService());
			}
			if(searchCondition.getSearchCondition()!=null){
				for(Entry<String, Object> e:searchCondition.getSearchCondition().entrySet()){
					builder.queryParam(e.getKey(),e.getValue());
				}
				
			}
			if(StringUtils.isNotBlank(searchCondition.getSearchReportStartTime())){
				builder.queryParam("searchReportStartTime",searchCondition.getSearchReportStartTime());
			}
			if(StringUtils.isNotBlank(searchCondition.getSearchReportEndTime())){
				builder.queryParam("searchReportEndTime",searchCondition.getSearchReportEndTime());
			}
			if(searchCondition.getPageNo()!=null){
				builder.queryParam("pageNo",searchCondition.getPageNo());
			}
			if(searchCondition.getPageSize()!=null){
				builder.queryParam("pageSize",searchCondition.getPageSize());
			}
			if(StringUtils.isNotBlank(searchCondition.getOrderBy())){
				builder.queryParam("orderBy",searchCondition.getOrderBy());
			}
			if(StringUtils.isNotBlank(searchCondition.getFields())){
				builder.queryParam("fields",searchCondition.getFields());
			}
			URI uri=builder.build();
			logger.info("report url:"+uri.toString());
			//创建连接
			ClientUtil.initClient();
			Client client=ClientUtil.getClient();
			WebTarget wt = client.target(uri);
			Builder header = wt.request();
			try {
				response=  header.get(); 
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				result= response.readEntity(String.class);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return result;
	}
	public static Map getCGIInfo(String url,String cmd,Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		params.put("cmd", cmd);
		String recv = getCGI(url, params);
		if (StringUtils.isNotBlank(recv)) {
			resultMap = (Map<String, Object>) JSON.parse(recv);
		}
		return resultMap;
	}
	public static String getCGI(String url, Map<String, Object> params) throws MaatConvertException{
//		if(StringUtils.isBlank(searchCondition.getSearchService())){
//			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
//		}
		Response response=null;
		String result = null;
		try {
			UriBuilder builder=UriBuilder.fromPath(url);
			if(params!=null) {
				for (String param : params.keySet()) {
					if(!StringUtil.isBlank(param)&&params.get(param)!=null) {
						builder.queryParam(param,params.get(param).toString());
					}
				}
			}
			URI uri=builder.build();
			logger.info("cgi url:"+uri.toString());
			//创建连接
			ClientUtil.initClient();
			Client client=ClientUtil.getClient();
			WebTarget wt = client.target(uri);
			Builder header = wt.request();
			try {
				response=  header.get(); 
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"cgi_service_failed\"/>");
			} 
			if(response !=null && response.getStatus() == 200){
				result= response.readEntity(String.class);
				logger.info("cgi info:"+result);
			}else{
				if(response!= null) {
					throw new MaatConvertException("<spring:message code=\"cgi_service_failed\"/>:"+response.readEntity(String.class));
				}else {
					throw new MaatConvertException("<spring:message code=\"cgi_service_failed\"/>");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (response != null) {
	            response.close();
	        }
		}
		return result;
	}
	
	/**
	 * 配置全量更新指令下发
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static JSONObject configSyncCmd(String params) throws MaatConvertException{
		Response response=null;
		JSONObject bean = null;
		try {
			String result = null;
			String url = DictUtils.getDictLabel("config_sync_url", "sync_cmd");
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("sync_cmd url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON)
										.header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.post(Entity.entity(params, MediaType.APPLICATION_JSON));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				logger.info("get result success");
				bean = JSONObject.fromObject(result);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if (response != null) {
                response.close();
            }
		} 
		return bean;
	}
	/**
	 * 配置全量更新获取当前状态
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static JSONObject configSyncStatus() throws MaatConvertException{
		Response response=null;
		JSONObject bean = null;
		try {
			String result = null;
			String url = DictUtils.getDictLabel("config_sync_url", "get_sync_status");
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("get_sync_status url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON)
										.header("Content-Type", MediaType.APPLICATION_JSON);
			try {
				response= header.get();
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				logger.info("get result success");
				bean= JSONObject.fromObject(result);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
                response.close();
            }
		} 
		return bean;
	}
	/**
	 * 配置全量更新
	 * @param params
	 * @return
	 * @throws MaatConvertException 
	 */
	public static JSONObject configSync(String params,Integer cfgType,Integer serviceId,String tableName,String completeTag) throws MaatConvertException{
		Response response=null;
		JSONObject bean = null;
		try {
			String result = null;
			String url = DictUtils.getDictLabel("config_sync_url", "sync_send");
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("sync_send url:"+url);
			logger.info("Last-Completed-Tag:"+completeTag);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON)
										.header("Content-Type", MediaType.APPLICATION_JSON)
										.header("Config-Type", cfgType)
										.header("Service-Id", serviceId)
										.header("Config-Table", tableName)
										.header("Last-Completed-Tag", completeTag);
			try {
				response= header.post(Entity.entity(params, MediaType.APPLICATION_JSON));
				if(response != null) {
					result=response.readEntity(String.class);
				}
			} catch (Exception e) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}  
			if(response != null && response.getStatus() == 200){
				logger.info("get result success");
				bean = JSONObject.fromObject(result);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+result);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (response != null) {
                response.close();
            }
		} 
		
		return bean;
	}
	public static void main(String[] args) {
		try {
//			getId(1,1);
			System.out.println(IpUtil.convertMask(4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
