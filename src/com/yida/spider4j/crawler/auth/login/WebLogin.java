package com.yida.spider4j.crawler.auth.login;

import java.util.HashMap;
import java.util.Map;

import com.yida.spider4j.crawler.auth.param.Feedback;
import com.yida.spider4j.crawler.utils.httpclient.Result;

/**
 * @ClassName: WebLogin
 * @Description: Web登录
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 下午1:22:52
 *
 */
public abstract class WebLogin implements Login {
	/**请求头信息*/
	protected Map<String,String> headerMap;
	/**登录验证所需提交参数*/
	protected Map<String,String> loginParams;
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: method
	 * @Description: Http请求Method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected abstract String method();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: isAjax
	 * @Description: 是否Ajax请求
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	protected boolean isAjax() {
		return false;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addRequestHeader
	 * @Description: 添加请求头信息
	 * @param @param key
	 * @param @param value
	 * @return void
	 * @throws
	 */
	protected void addRequestHeader(String key,String value) {
		if(key == null || value == null) {
			return;
		}
		if(this.headerMap == null) {
			this.headerMap = new HashMap<String, String>();
		}
		this.headerMap.put(key, value);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addRequestHeaderMap
	 * @Description: 批量添加请求头信息
	 * @param @param headers
	 * @return void
	 * @throws
	 */
	protected void addRequestHeaderMap(Map<String,String> headers) {
		if(headers == null || headers.isEmpty()) {
			return;
		}
		if(this.headerMap == null) {
			this.headerMap = new HashMap<String, String>();
		}
		this.headerMap.putAll(headers);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setLoginRequestHeaders
	 * @Description: 填充登录验证请求的头信息
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	protected Map<String,String> setLoginRequestHeaders() {
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.99 Safari/537.36");
		return headers;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addLoginParam
	 * @Description: 添加登录请求所需提交的参数
	 * @param @param paramName
	 * @param @param paramValue
	 * @param @return
	 * @return FormLogin
	 * @throws
	 */
	protected WebLogin addLoginParam(String paramName,String paramValue) {
		if(this.loginParams == null) {
			this.loginParams = new HashMap<String, String>();
		}
		this.loginParams.put(paramName, paramValue);
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addLoginParams
	 * @Description: 批量添加登录请求所需提交的参数
	 * @param @param params
	 * @param @return
	 * @return FormLogin
	 * @throws
	 */
	protected WebLogin addLoginParams(Map<String,String> params) {
		if(null == params || params.isEmpty()) {
			return this;
		}
		if(this.loginParams == null) {
			this.loginParams = new HashMap<String, String>();
		}
		this.loginParams.putAll(params);
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setLoginParams
	 * @Description: 填充登录验证的请求参数
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	protected abstract Map<String,String> setLoginParams();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: encodeUserName
	 * @Description: 登录帐号编码(比如有些网站会对用户的登录帐号进行Base64编码)
	 * @param @param userName
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String encodeUserName(String userName) {
		//这里什么都没做,只是原样返回,留给用户自己去重写实现
		return userName;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: encryptPassword
	 * @Description: 密码加密
	 * @param @param password
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String encryptPassword(String password) {
		//这里什么都没做,只是原样返回,留给用户自己去重写实现
		return password;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getRequestHeader
	 * @Description: 获取请求头信息
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	public Map<String,String> getRequestHeader() {
		return this.headerMap;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildFeedback
	 * @Description: 组装Feedback对象
	 * @param @param result
	 * @param @return
	 * @return Feedback
	 * @throws
	 */
	protected Feedback buildFeedback(Result result) {
		//判断是否登录成功
		boolean loginSuccess = true;
		if(null == result) {
			loginSuccess = false;
		} else {
			int statuCode = result.getStatusCode();
			if(statuCode >= 300) {
				loginSuccess = false;
			}
		}
		//组装登录验证的反馈结果
		Feedback feedback = new Feedback(result,loginSuccess);
		return feedback;
	}
}
