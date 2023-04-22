package com.yida.spider4j.crawler.auth.param;

import javax.swing.text.html.FormSubmitEvent.MethodType;

import java.net.MalformedURLException;

/**
 * Microsoft Active Directory登录验证
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-9-28上午10:33:43
 * 
 */
public class NTAuthInfo extends AuthInfo {
	/** 域名 */
	private String domain;

	public NTAuthInfo(String username, String password, String loginUrl, String domain) throws MalformedURLException {
		super(AuthenticationType.NT_AUTHENTICATION, MethodType.GET, loginUrl, username, password);
		this.domain = domain;
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
		//暂未实现
		return null;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}