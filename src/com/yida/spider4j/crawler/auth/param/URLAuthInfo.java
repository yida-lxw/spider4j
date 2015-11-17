package com.yida.spider4j.crawler.auth.param;

import java.net.MalformedURLException;

import javax.swing.text.html.FormSubmitEvent.MethodType;

/**
 * URL登录验证
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-9-28上午10:30:23
 * 
 */
public class URLAuthInfo extends AuthInfo {
	public URLAuthInfo(String username, String password,String loginUrl, String usernameParamName,
			String passwordParamName) throws MalformedURLException {
		this(username, password, null, loginUrl, usernameParamName, passwordParamName, null);
	}
	
	public URLAuthInfo(String username, String password,String validateCode,String loginUrl, String usernameParamName,
			String passwordParamName,String validateCodeParamName) throws MalformedURLException {
		super(AuthenticationType.FORM_AUTHENTICATION, MethodType.GET, loginUrl, username, password,validateCode);
		
		this.usernameParamName = usernameParamName;
		this.passwordParamName = passwordParamName;
		this.validateCodeParamName = validateCodeParamName;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getFinalLoginUrl
	 * @Description: 拼接最终的请求URL
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getFinalLoginUrl() {
		if(this.loginTarget == null || "".equals(this.loginTarget) || 
			this.host == null || "".equals(this.host)) {
			return null;
		}
		String url = "";
		if(this.port <= 0) {
			if(this.loginTarget.contains("?")) {
				url = this.host + this.loginTarget + "&" + this.usernameParamName + 
						"=" + this.username + "&" + this.passwordParamName + 
						"=" + this.password + "&" + this.validateCodeParamName + 
						"=" + this.validateCode;
			} else {
				url = this.host + this.loginTarget + "?" + this.usernameParamName + 
						"=" + this.username + "&" + this.passwordParamName + 
						"=" + this.password + "&" + this.validateCodeParamName + 
						"=" + this.validateCode;
			}
		} else {
			if(this.loginTarget.contains("?")) {
				url = this.host + ":" + this.port + this.loginTarget + "&" + this.usernameParamName + 
						"=" + this.username + "&" + this.passwordParamName + 
						"=" + this.password + "&" + this.validateCodeParamName + 
						"=" + this.validateCode;
			} else {
				url = this.host + ":" + this.port + this.loginTarget + "?" + this.usernameParamName + 
						"=" + this.username + "&" + this.passwordParamName + 
						"=" + this.password + "&" + this.validateCodeParamName + 
						"=" + this.validateCode;
			}
		}
		return url;
	}
}