package com.yida.spider4j.crawler.auth.login;

import com.yida.spider4j.crawler.auth.param.AuthInfo;
import com.yida.spider4j.crawler.auth.param.Feedback;

/**
 * @ClassName: Login
 * @Description: 登录接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 上午11:04:15
 *
 */
public interface Login {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: doAuthenticate
	 * @Description: 登录验证
	 * @param @param authInfo
	 * @param @return
	 * @return Feedback
	 * @throws
	 */
	public Feedback doAuthenticate(AuthInfo authInfo);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildAuthInfo
	 * @Description: 构建AuthInfo对象
	 * @param @return
	 * @return AuthInfo
	 * @throws
	 */
	public AuthInfo buildAuthInfo();
}
