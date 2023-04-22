package com.yida.spider4j.crawler.auth.param;

import java.net.MalformedURLException;

import javax.swing.text.html.FormSubmitEvent.MethodType;

/**
 * 表单登录验证
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-9-28上午10:30:23
 * 
 */
public class FormAuthInfo extends AuthInfo {
	public FormAuthInfo(String username, String password, String validateCode,String loginUrl, String usernameParamName,
			String passwordParamName,String validateCodeParamName) throws MalformedURLException {
		super(AuthenticationType.FORM_AUTHENTICATION, MethodType.POST, loginUrl, username, password,validateCode);
		this.usernameParamName = usernameParamName;
		this.passwordParamName = passwordParamName;
		this.validateCodeParamName = validateCodeParamName;
	}
	
	public FormAuthInfo(String username, String password,String loginUrl, String usernameParamName,
			String passwordParamName) throws MalformedURLException {
		this(username, password, null, loginUrl, usernameParamName, passwordParamName, null);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getFinalLoginUrl
	 * @Description: 拼接最终的请求URL
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String getFinalLoginUrl() {
		return this.loginUrl;
	}
}