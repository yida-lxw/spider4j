package com.yida.spider4j.crawler.auth.login;

import java.net.MalformedURLException;
import java.util.Map;

import com.yida.spider4j.crawler.auth.param.AuthInfo;
import com.yida.spider4j.crawler.auth.param.FormAuthInfo;
import com.yida.spider4j.crawler.utils.HttpConstant;

/**
 * @ClassName: SimpleFormLogin
 * @Description: FormLogin的简单实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 下午3:59:56
 *
 */
public class SimpleFormLogin extends FormLogin {
	/**登录帐号*/
	private String userName;
	/**登录密码*/
	private String password;
	/**验证码[登录有时候可能会弹出验证码]*/
	private String validateCode;
	
	/** 帐号表单属性名称 */
	private String usernameParamName;
	/** 密码表单属性名称 */
	private String passwordParamName;
	/** 验证码表单属性名称 */
	private String validateCodeParamName;
	
	/** 登录请求URL */
	private String loginUrl;
	
	public SimpleFormLogin(String userName, String password,
			String usernameParamName, String passwordParamName, String loginUrl) {
		this.userName = userName;
		this.password = password;
		this.usernameParamName = usernameParamName;
		this.passwordParamName = passwordParamName;
		this.loginUrl = loginUrl;
	}

	public SimpleFormLogin(String userName, String password,
			String validateCode, String usernameParamName,
			String passwordParamName, String validateCodeParamName,
			String loginUrl) {
		this.userName = userName;
		this.password = password;
		this.validateCode = validateCode;
		this.usernameParamName = usernameParamName;
		this.passwordParamName = passwordParamName;
		this.validateCodeParamName = validateCodeParamName;
		this.loginUrl = loginUrl;
	}

	public SimpleFormLogin(String userName, String password, String validateCode) {
		this.userName = userName;
		this.password = password;
		this.validateCode = validateCode;
	}
	
	public SimpleFormLogin(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildAuthInfo
	 * @Description: 根据传入的参数构建AuthInfo对象
	 * @param @return
	 * @return AuthInfo
	 * @throws
	 */
	@Override
	public AuthInfo buildAuthInfo() {
		//若是POST请求
		if(HttpConstant.Method.POST.equalsIgnoreCase(method())) {
			FormAuthInfo urlAuthInfo = null;
			try {
				urlAuthInfo = new FormAuthInfo(userName, password, validateCode, loginUrl, usernameParamName, passwordParamName, validateCodeParamName);
			} catch (MalformedURLException e) {
				//e.printStackTrace();
			}
			return urlAuthInfo;
		}
		//其他Http method暂且不支持
		return null;
	}
	
	@Override
	protected Map<String, String> setLoginParams() {
		return null;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
