package com.homw.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @description HTTP客户端工具
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public class HttpClientUtil {

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		HttpGet request = new HttpGet(url);
		CloseableHttpResponse response = HttpClients.createDefault().execute(request);
		return response(response);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param headerMap
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> headerMap) throws IOException {
		HttpGet request = new HttpGet(url);
		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}

		CloseableHttpResponse response = HttpClients.createDefault().execute(request);
		return response(response);
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param requestBody
	 * @param contentType
	 * @param headerMap   请求头
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String requestBody, ContentType contentType, Map<String, String> headerMap)
			throws IOException {
		HttpPost request = new HttpPost(url);
		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}

		HttpEntity entity = new StringEntity(requestBody, contentType);
		request.setEntity(entity);
		CloseableHttpResponse response = HttpClients.createDefault().execute(request);
		return response(response);
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> paramMap, Map<String, String> headerMap)
			throws IOException {
		HttpPost request = new HttpPost(url);
		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}

		List<org.apache.http.NameValuePair> paramList = new ArrayList<>();
		if (paramMap != null && paramMap.size() > 0) {
			Set<String> keySet = paramMap.keySet();
			for (String key : keySet) {
				paramList.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
		}
		// 解决因请求参数中文乱码导致签名比对失败的问题
		request.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));

		CloseableHttpResponse response = HttpClients.createDefault().execute(request);
		return response(response);
	}

	/**
	 * 处理响应
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private static String response(CloseableHttpResponse response) throws IOException {
		if (response != null && response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity());
		}
		return null;
	}

}
