package com.yida.spider4j.crawler.auth.param;

import java.net.MalformedURLException;

import javax.swing.text.html.FormSubmitEvent.MethodType;

/**
 * 基本登录验证信息(浏览器会弹出一个通用的登录窗口)
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-9-28上午10:27:58
 * 
 */
public class BasicAuthInfo extends AuthInfo {
	public BasicAuthInfo(String username, String password, String loginUrl) throws MalformedURLException {
		super(AuthenticationType.BASIC_AUTHENTICATION, MethodType.GET, loginUrl, username, password);
	}

	@Override
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getFinalLoginUrl
	 * @Description: 拼接最终的请求URL
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getFinalLoginUrl() {
		return null;
	}
}