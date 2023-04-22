package com.yida.spider4j.crawler.auth.login;

/**
 * @ClassName: LoginFailurelException
 * @Description: 登录验证失败异常
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 下午4:37:38
 *
 */
@SuppressWarnings("serial")
public class LoginFailurelException extends Exception {
	public LoginFailurelException() {
        super();
    }
	
	public LoginFailurelException(String message) {
        super(message);
    }
	
	public LoginFailurelException(String message, Throwable cause) {
        super(message, cause);
    }
}
