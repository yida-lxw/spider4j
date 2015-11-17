package com.yida.spider4j.crawler.utils.httpclient;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @ClassName: SocksAuthenticator
 * @Description: Socket代理认证对象
 *               当代理需要账号密码时，创建此对象，使用示例如下：
 *               //在创建HttpClient对象之前设置代理账号密码
 *               Authenticator.setDefault(new SocksAuthenticator("your-username", "your-password"));
 * @author Lanxiaowei
 * @date 2013-3-20 下午03:08:49
 */
public class SocksAuthenticator extends Authenticator {
	/**用户名*/
	private String userName;
	/**密码*/
    private String passWord;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

    public SocksAuthenticator(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}
    
	protected PasswordAuthentication getPasswordAuthentication() { 
        if (userName != null && passWord != null) { 
            return new PasswordAuthentication(userName, passWord.toCharArray()); 
        } 
        return null; 
    } 
}
