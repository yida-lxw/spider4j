package com.yida.spider4j.crawler.auth.param;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.yida.spider4j.crawler.utils.httpclient.Result;
import com.yida.spider4j.crawler.utils.httpclient.StringEntityHandler;

/**
 * @ClassName: Feedback
 * @Description: 登录验证反馈结果
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 上午11:07:59
 *
 */
public class Feedback {
	/**登录验证返回结果*/
	private Result result;
	
	/**是否登录成功*/
	private boolean loginSuccess;

	public Feedback(Result result) {
		this.result = result;
	}

	public Feedback(Result result, boolean loginSuccess) {
		this.result = result;
		this.loginSuccess = loginSuccess;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getResponseBody
	 * @Description: 获取响应数据
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String getResponseBody() {
		if(null == result) {
			return null;
		}
		HttpEntity entity = result.getHttpEntity();
		if(null == entity) {
			return null;
		}
		StringEntityHandler entityHandler = new StringEntityHandler();
		try {
			return entityHandler.handleEntity(entity);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getStatuCode
	 * @Description: 获取响应状态码
	 * @param @return
	 * @return int
	 * @throws
	 */
	protected int getStatuCode() {
		if(null == result) {
			return -1;
		}
		return result.getStatusCode();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getResponseHeader
	 * @Description: 响应头信息
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	protected Map<String,String> getResponseHeader() {
		if(null == result) {
			return null;
		}
		HashMap<String, Header> headerMap = result.getHeaderMap();
		if(null == headerMap || headerMap.isEmpty()) {
			return null;
		}
		Map<String,String> headers = new HashMap<String, String>();
		for(Map.Entry<String, Header> entry : headerMap.entrySet()) {
			Header header = entry.getValue();
			if(null == header) {
				continue;
			}
			String key = entry.getKey();
			String value = header.getValue();
			headers.put(key, value);
		}
		return headers;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getResponseHeader
	 * @Description: 根据Header Key获取Header Value
	 * @param @param headerName
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String getResponseHeader(String headerName) {
		if(null == result || null == headerName || "".equals(headerName)) {
			return null;
		}
		HashMap<String, Header> headerMap = result.getHeaderMap();
		if(null == headerMap || headerMap.isEmpty()) {
			return null;
		}
		Header header = headerMap.get(headerName);
		if(null == header) {
			return null;
		}
		return header.getValue();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getCookie
	 * @Description: 获取Response返回的Cookie值
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String getCookie() {
		if(null == result) {
			return null;
		}
		HashMap<String, Header> headerMap = result.getHeaderMap();
		if(null == headerMap || headerMap.isEmpty()) {
			return null;
		}
		Header header = headerMap.get("Cookie");
		if(null == header) {
			return null;
		}
		return header.getValue();
	}
	
	
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getContentType
	 * @Description: 获取Response返回的ContentType值
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String getContentType() {
		if(null == result) {
			return null;
		}
		HashMap<String, Header> headerMap = result.getHeaderMap();
		if(null == headerMap || headerMap.isEmpty()) {
			return null;
		}
		Header header = headerMap.get("Content-Type");
		if(null == header) {
			return null;
		}
		return header.getValue();
	}
	
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
}
