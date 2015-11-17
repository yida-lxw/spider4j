package com.yida.spider4j.crawler.auth.param;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.FormSubmitEvent.MethodType;

/**
 * 登录验证信息
 * @since 1.0
 * @author  Lanxiaowei@citic-finance.com
 * @date    2015-9-28上午10:26:42
 *
 */
public abstract class AuthInfo {
	/**
	 * 身份验证类型-枚举
	 * @since 1.0
	 * @author  Lanxiaowei@citic-finance.com
	 * @date    2015-9-28上午10:25:54
	 *
	 */
	public enum AuthenticationType {
		BASIC_AUTHENTICATION, FORM_AUTHENTICATION, NT_AUTHENTICATION
	}

	protected AuthenticationType authenticationType;
	protected MethodType httpMethod;
	protected String protocol;
	protected String host;
	protected String loginTarget;
	protected String loginUrl;
	protected int port;
	protected String username;
	protected String password;
	/**验证码*/
	protected String validateCode;
	
	/** 帐号表单属性名称 */
	protected String usernameParamName;
	/** 密码表单属性名称 */
	protected String passwordParamName;
	/** 验证码表单属性名称 */
	protected String validateCodeParamName;

	public AuthInfo() {
	}

	protected AuthInfo(AuthenticationType authenticationType, MethodType httpMethod, String loginUrl, String username,
			String password,String validateCode) throws MalformedURLException {
		this.authenticationType = authenticationType;
		this.httpMethod = httpMethod;
		URL url = new URL(loginUrl);
		this.protocol = url.getProtocol();
		this.host = url.getHost();
		this.port = url.getDefaultPort();
		this.loginTarget = url.getFile();
		this.loginUrl = loginUrl;
		this.username = username;
		this.password = password;
		this.validateCode = validateCode;
	}
	
	protected AuthInfo(AuthenticationType authenticationType, MethodType httpMethod, String loginUrl, String username,
			String password) throws MalformedURLException {
		this(authenticationType, httpMethod, loginUrl, username, password, null);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getFinalLoginUrl
	 * @Description: 拼接最终的请求URL
	 * @param @return
	 * @return String
	 * @throws
	 */
	public abstract String getFinalLoginUrl();

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getHttpMethod
	 * @Description: Http method(get/post)
	 * @return
	 * @return MethodType
	 * @throws
	 */
	public MethodType getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(MethodType httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getProtocol
	 * @Description: 协议类型(http / https)
	 * @return
	 * @return String
	 * @throws
	 */
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getHost
	 * @Description: 主域名(如：www.google.com)
	 * @return
	 * @return String
	 * @throws
	 */
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getLoginTarget
	 * @Description: 登录目标URL(主要是除去host剩下的部分,如： /login.html)
	 * @return
	 * @return String
	 * @throws
	 */
	public String getLoginTarget() {
		return loginTarget;
	}

	public void setLoginTarget(String loginTarget) {
		this.loginTarget = loginTarget;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getPort
	 * @Description: 端口号(如：80 / 8080)
	 * @return
	 * @return int
	 * @throws
	 */
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getUsername
	 * @Description: 登录验证时需要的帐号
	 * @return
	 * @return String
	 * @throws
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getPassword
	 * @Description: 登录验证时需要的密码
	 * @return
	 * @return String
	 * @throws
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getUsernameParamName() {
		return usernameParamName;
	}

	public void setUsernameParamName(String usernameParamName) {
		this.usernameParamName = usernameParamName;
	}

	public String getPasswordParamName() {
		return passwordParamName;
	}

	public void setPasswordParamName(String passwordParamName) {
		this.passwordParamName = passwordParamName;
	}

	public String getValidateCodeParamName() {
		return validateCodeParamName;
	}

	public void setValidateCodeParamName(String validateCodeParamName) {
		this.validateCodeParamName = validateCodeParamName;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}