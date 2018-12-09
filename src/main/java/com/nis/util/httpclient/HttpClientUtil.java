/**
 *@Title: HttpClientBean.java
 *@Package com.nis.util.httpclient 
 *@Description TODO
 *@author dell
 *@date 2016年11月7日 下午2:36:26
 *@version  版本号
 */
package com.nis.util.httpclient;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.Encodes;
import com.nis.util.StringUtil;

import net.sf.json.JSONObject;
/**
 * @ClassName: HttpClientBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年11月7日 下午2:36:26
 * @version V1.0
 */
public class HttpClientUtil {
	protected static Logger logger = Logger.getLogger(HttpClientUtil.class);
	 /** 
     * 处理get请求. 
     * @param url  请求路径 
     * @return  json 
	 * @throws IOException 
	 * @throws ClientProtocolException 
     */  
    public static String get(String destUrl) throws Exception{
        //执行get方法  
        String result = null;
		Response response=null;
		String url = "";
        try {
        	URIBuilder uriBuilder = new URIBuilder(destUrl);
			System.err.println(uriBuilder);
			url=uriBuilder.toString();
			logger.info("流量统计数据请求路径:"+url);
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("getMsg url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			response= header.get();
			int status = response.getStatus();
			if (status ==  HttpStatus.SC_OK) {
				result= response.readEntity(String.class);
				//调用处理数据方法
				logger.info("获取消息成功，相应内容如下： " + result);
			} else {
				logger.error("获取消息失败，相应内容如下： " + result);
				throw new ConnectException("流量统计服务接口连接错误"+result);
			}
			} catch (Exception e) {
				throw e;
		}finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;  
    }  
    
    /** 
     * 处理post请求. 
     * @param url  请求路径 
     * @param params  参数 
     * @return  json 
     * @throws IOException 
     * @throws ClientProtocolException 
     */  
/*    public String post(String url,Map<String, String> params) throws ClientProtocolException, IOException{  
        //实例化httpClient  
        CloseableHttpClient httpclient =null; 
        //结果  
        CloseableHttpResponse response = null;  
        String content="";  
        try {  
        	httpclient = HttpClients.createDefault();  
        	//实例化post方法  
            HttpPost httpPost = new HttpPost(url); 
            //处理参数  
            List<NameValuePair> nvps = new ArrayList <NameValuePair>();    
            Set<String> keySet = params.keySet();    
            for(String key : keySet) {    
                nvps.add(new BasicNameValuePair(key, params.get(key)));    
            }  
            //提交的参数  
            UrlEncodedFormEntity uefEntity  = new UrlEncodedFormEntity(nvps, "UTF-8");  
            //将参数给post方法  
            httpPost.setEntity(uefEntity); 
            requestConfig = RequestConfig.custom()  
                    .setSocketTimeout( Constants.HTTP_SOCKET_TIMEOUT)
                    .setConnectTimeout( Constants.HTTP_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout( Constants.HTTP_CONNECT_REQUEST_TIMEOUT)
                    .build();
        	httpclient =  HttpClients.custom().setDefaultRequestConfig(requestConfig)
    	    		.setRetryHandler(new DefaultHttpRequestRetryHandler( Constants.HTTP_CONNECT_RETRY_TIMES, false))
    	    		.build();
            //执行post方法  
            response = httpclient.execute(httpPost);  
//            if(response.getStatusLine().getStatusCode()==200){  
                content = EntityUtils.toString(response.getEntity(),"utf-8");  
//                System.out.println(content);  
//            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (Exception e) {  
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  
        return content;  
    } */ 
    /** 
     * 处理patch请求. 
     * @param url  请求路径 
     * @param params  参数 
     * @return  json 
     * @throws IOException 
     * @throws ClientProtocolException 
     */  
    public static String patch(String url,String param) throws Exception{  
    	CloseableHttpClient httpclient = null;  

    	//处理参数  
//    	List<NameValuePair> nvps = new ArrayList <NameValuePair>();    
//    	Set<String> keySet = params.keySet();    
//    	for(String key : keySet) {    
//    		nvps.add(new BasicNameValuePair(key, params.get(key)));    
//    	}    
    	//结果  
    	CloseableHttpResponse response = null;  
    	String content="";  
        try {  
        	//实例化httpClient 
        	httpclient = HttpClients.createDefault(); 
        	//实例化patch方法  
        	HttpPatch httpPatch = new HttpPatch(url);   
    		httpPatch.setHeader("Content-type", "application/json");
    		httpPatch.setHeader("Charset", "utf-8");
    		httpPatch.setHeader("Accept", "application/json");
    		httpPatch.setHeader("Accept-Charset", "utf-8");
	    	//提交的参数  
	//    	UrlEncodedFormEntity uefEntity  = new UrlEncodedFormEntity(nvps, "UTF-8");  
	    	StringEntity entity = new StringEntity(param, "utf-8");
	    	//将参数给post方法  
	    	httpPatch.setEntity(entity);  
	    	requestConfig = RequestConfig.custom()  
	                .setSocketTimeout( Constants.HTTP_SOCKET_TIMEOUT)
	                .setConnectTimeout( Constants.HTTP_CONNECT_TIMEOUT)
	                .setConnectionRequestTimeout( Constants.HTTP_CONNECT_REQUEST_TIMEOUT)
	                .build();
	    	httpclient =  HttpClients.custom().setDefaultRequestConfig(requestConfig)
		    		.setRetryHandler(new DefaultHttpRequestRetryHandler( Constants.HTTP_CONNECT_RETRY_TIMES, false))
		    		.build();
	    	//执行post方法  
	    	response = httpclient.execute(httpPatch); 
	    	int status = response.getStatusLine().getStatusCode();
            if(status==200){  
            	content = EntityUtils.toString(response.getEntity());  
            }
        } catch (Exception e) {
            throw e;
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
	            try {
	                httpclient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
            }
        }
    	return content;  
    }  
    
    
	/**
	 * http get 获取消息      
	 * @param destUrl 业务地址
	 * @param params 参数列表
	 * @return 查询结果数据json
	 */
	public static String getMsg(String destUrl, Map<String, Object> params, HttpServletRequest req) throws IOException {
//		RequestContext requestContext = new RequestContext(req);
		
//		CloseableHttpResponse  response = null;
		String result = null;
		Response response=null;
		String url = "";
		try {
			URIBuilder uriBuilder = new URIBuilder(destUrl);
			if(params!=null) {
				for (String param : params.keySet()) {
					if(!StringUtil.isBlank(param)&&params.get(param)!=null) {
						uriBuilder.addParameter(param, params.get(param).toString());
					}
				}
			}
			System.err.println(uriBuilder);
			url=uriBuilder.toString();
			//创建连接
			WebTarget wt = ClientUtil.getWebTarger(url);
			logger.info("getMsg url:"+url);
			//获取响应结果
			Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
			response= header.get();
			
//			HttpGet request = new HttpGet(uriBuilder.build());
//			request.setConfig(requestConfig);
//			URI uri = request.getURI();
//			logger.info(uri);
//			System.err.println(uri);
//			response = getHttpClient().execute(request);
//			int status = response.getStatusLine().getStatusCode();
			int status = response.getStatus();
			if (status ==  HttpStatus.SC_OK) {
				result= response.readEntity(String.class);
//				result = EntityUtils.toString(response.getEntity());
				//调用处理数据方法
				result = galaxyMessageFormat(result);
				if(!StringUtil.isEmpty(result) && result.length() > 501) {
					logger.info("获取消息成功，相应内容如下： " + result.substring(0, 500));
				}else {
					logger.info("获取消息成功，相应内容如下： " + result);
				}
				
			} else {
				logger.error("获取消息失败，相应内容如下： " + result);
				throw new MaatConvertException(status+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取消息失败，相应内容如下： " + result);
			logger.error("获取消息失败 ", e);
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:");
		}finally {
            if (response != null) {
                response.close();
            }
        }
		return result;
	}
	
	/**
	 * 将接口的日志数据格式化包括 “null”,日期为“0”,特殊字符转义
	 * @param recv
	 * @return
	 */
	public static String galaxyMessageFormat(String recv){
		JSONObject jobj = JSONObject.fromObject(recv);
		Map<String, Object> parse = (Map<String, Object>)jobj;
    	  Map map = (Map) parse.get("data");
    	  List reslist=new ArrayList();
    	  List<Map<String,Object>> list= (List)map.get("list");
    	  if(list!=null&&list.size()>0){
    	  for (Map<String,Object> m : list) {
    		  Map recvMap = new HashMap();
    		  Iterator<Entry<String, Object>> iterator = m.entrySet().iterator();
    		  while(iterator.hasNext()){
    			  Entry<String, Object> next = iterator.next();
    			  String key = next.getKey().toString();
    			  Object value = next.getValue();
    			  //处理字段为“null”情况
				if(value!=null&&"null".equals(value)){
					value="";  
				}
				//处理时间字段为“0”情况
			  	if("foundTime".equals(key)||"recvTime".equals(key)){
			  		if(null!=value&&"0".equals(value)){
			  		value="";
			  		}
			  	}
    			  //处理特殊字符转义问题
    			  if(null!=value){
    			  	value = Encodes.escapeHtml(value.toString());
    			  }
    			  recvMap.put(key, value);
    		  }
    		  reslist.add(recvMap);
		}
    	  map.put("list", reslist);
    	  parse.put("data", map);
    	  recv = parse.toString();
    }
    	  return recv;
}
	
	private static CloseableHttpClient httpClient = null;
	private static  RequestConfig requestConfig;
	//http 连接池
	public static CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			//http 连接池管理 需后续实现
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			// 将最大连接数增加到200
			cm.setMaxTotal(200);
		    // 将每个路由基础的连接增加到20
		    cm.setDefaultMaxPerRoute(20);
		    requestConfig = RequestConfig.custom()  
	                .setSocketTimeout( Constants.HTTP_SOCKET_TIMEOUT)
	                .setConnectTimeout( Constants.HTTP_CONNECT_TIMEOUT)
	                .setConnectionRequestTimeout( Constants.HTTP_CONNECT_REQUEST_TIMEOUT)
	                .build();
		    httpClient =  HttpClients.custom()
		    		.setConnectionManager(cm)
		    		.setDefaultRequestConfig(requestConfig)
		    		.setRetryHandler(new DefaultHttpRequestRetryHandler( Constants.HTTP_CONNECT_RETRY_TIMES, false))
		    		.build();
		}  
		return httpClient;
		
	}
    
//    public static void main(String[] args) throws ClientProtocolException, IOException {  
//    	HttpClientUtil hd = new HttpClientUtil();  
////        hd.get("http://10.0.6.115:9200/_sql?sql=select * from dfipportlog-2016-09-07-15 limit 1 10");  
//        Map<String,String> map = new HashMap();  
//        map.put("id","1");  
//        hd.post("http://localhost:8080/springMVC/menu/getChildren.do",map);  
//        
//    }  
  
}
