package com.yida.spider4j.crawler.utils.httpclient;

import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * Response响应体包装器
 * @author Lanxiaowei
 *
 */
public class Result {
	/**Cookie*/
	private String cookie;
	/**响应状态码*/
	private int statusCode;
	/**响应头信息*/
	private HashMap<String, Header> headerMap;
	/**响应主体对象*/
	private HttpEntity httpEntity;
	
	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public HashMap<String, Header> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(HashMap<String, Header> headerMap) {
		this.headerMap = headerMap;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public Result setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
		return this;
	}
	
	public Result setHeaders(Header[] headers){
		if(null == headerMap) {
			headerMap = new HashMap<String, Header>();
		}
		
		for (Header header : headers) {
			headerMap.put(header.getName(), header);
		}
		return this;
	}
	
	public Result putHeader(String headerName,String headerValue) {
		if(null != headerMap) {
			if(StringUtils.isNotEmpty(headerName) && 
					StringUtils.isNotEmpty(headerValue)) {
				headerMap.put(headerName, new BasicHeader(headerName, headerValue));
			}
		}
		return this;
	}
	
	public void removeHeader(String headerName) {
		if(null == headerMap) {
			return;
		}
		headerMap.remove(headerName);
	}

	public Result(){}

	public Result(String cookie, int statusCode,HashMap<String, Header> headerMap, HttpEntity httpEntity) {
		this.cookie = cookie;
		this.statusCode = statusCode;
		this.headerMap = headerMap;
		this.httpEntity = httpEntity;
	}
}
