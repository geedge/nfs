package com.nis.util.httpclient;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import com.nis.util.Constants;
/**
 * 请求过滤器，每次请求时自动配置requestHeader信息
 */
public class ClientRequestHeaderFilter implements ClientRequestFilter{

	@Override
	public void filter(ClientRequestContext context) throws IOException {
		MultivaluedMap<String,Object> header = context.getHeaders();
		Map<String,Object> h = Constants.REQUEST_HEADER;//请求头参数
		if(h != null && h.size() > 0){
			for(Map.Entry<String, Object> temp : h.entrySet()){
				header.add(temp.getKey(), temp.getValue());
			}
		}
	}

}
