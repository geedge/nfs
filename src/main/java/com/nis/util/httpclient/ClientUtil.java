package com.nis.util.httpclient;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.nis.util.Constants;


public class ClientUtil {

	private static Client client;//客户端
	private static Logger logger = Logger.getLogger(ClientUtil.class);

	/**
	 * 初始化https client
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static void initClient(){
		try{
			if(client==null){
				PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(); 
				pcm.setDefaultSocketConfig( SocketConfig.custom(). setSoTimeout(Constants.POOLCM_SOCKET_TIMEOUT). build()); 
				pcm.setMaxTotal(Constants.HTTP_MAX_CONNECTION); pcm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PERROUTE);

				ClientConfig clientConfig = new ClientConfig();
				clientConfig.property(ClientProperties.CONNECT_TIMEOUT, Constants.CLIENT_CONNECT_TIMEOUT);//连接超时时间
				clientConfig.property(ClientProperties.READ_TIMEOUT, Constants.CLIENT_READ_TIMEOUT);//读取超时时间
				clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, pcm); 
				clientConfig.register(ClientRequestHeaderFilter.class);//请求过滤器，自动添加header信息
				clientConfig.register(JacksonFeature.class);//json支持
				clientConfig.register(MultiPartFeature.class);//文件上传支持
				client = ClientBuilder.newClient(clientConfig);
				logger.info("客户端初始化成功");
			}
			
		}catch (Exception e) {
			logger.error("初始化客户端失败，请检查证书是否正确！",e);
			System.exit(1);//程序退出
		}
	}
	
	/**
	 *path 以https://或http:// 开始不做处理，否则在path前加上 Constants.BASE_URL + Constants.VERSION
	 * @param path
	 * @return
	 */
	public static WebTarget getWebTarger(String path){
		if(client==null){
			initClient();
		}
		if(StringUtils.isNotBlank(path)){
			path = formatHttpStr(path);//格式化url字符串
			return client.target(path);
		}
		return null;
	}
	
	/**
	 * 格式化url字符串
	 * @param path
	 * @return
	 */
	public static String formatHttpStr(String path){
		if( ! path.startsWith("https://") && ! path.startsWith("http://")){
			path = Constants.SERVICE_URL  + "/" + path;
		}
		path = path.replaceAll("/{2,}", "/");
		int i = path.indexOf(":/") + 2;
		String prefix = path.substring(0, i);
		path =prefix +"/"+ path.substring(i);
		return path;
	}
	
	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		ClientUtil.client = client;
	}
	/**
	 *文件上传header增加Content-fileDesc属性
	 *格式：Content-Filedesc:json格式的字段描述
	 * @param path
	 * @return
	 */
	public static JSONObject formatFileDesc(Map<String, Object> map){
		Map<String,Object> newMap = new HashMap();
		
		return JSONObject.fromObject(newMap);
	}
	public static void main(String[] args) throws ClientProtocolException, IOException {  
		String url = Constants.SERVICE_URL+Constants.CONFIG_ID_SOURCES;
		//创建连接
		WebTarget wt = ClientUtil.getWebTarger(url);
		//获取响应结果
		Builder header = wt.request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON);
		Response response= header.get(); 
		if( response.getStatus() == 200){
			String result= response.readEntity(String.class);
			System.out.println(result);
		}
	}
}
