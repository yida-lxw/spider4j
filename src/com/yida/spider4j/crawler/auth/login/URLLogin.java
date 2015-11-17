package com.yida.spider4j.crawler.auth.login;

import java.util.Map;

import com.yida.spider4j.crawler.auth.param.AuthInfo;
import com.yida.spider4j.crawler.auth.param.Feedback;
import com.yida.spider4j.crawler.auth.param.URLAuthInfo;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;
import com.yida.spider4j.crawler.utils.httpclient.Result;

/**
 * @ClassName: URLLogin
 * @Description: URL登录(Get)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 下午2:53:50
 *
 */
public abstract class URLLogin extends WebLogin {
	@Override
	public Feedback doAuthenticate(AuthInfo authInfo) {
		if(null == authInfo) {
			throw new IllegalArgumentException("When invoke the doAuthenticate method of FormLogin class,authInfo MUST not be null");
		}
		if(!(authInfo instanceof URLAuthInfo)) {
			throw new IllegalArgumentException("When invoke the doAuthenticate method of FormLogin class,authInfo MUST not be the type of URLAuthInfo");
		}
		//登录帐号
		String userName = authInfo.getUsername();
		//登录密码
		String password = authInfo.getPassword();
		//判断登录帐号密码是否为空,若为空直接抛参数异常,因为这些参数是必须的
		if(null == userName || "".equals(userName) ||
			null == password || "".equals(password)) {
			throw new IllegalArgumentException("When invoke the doAuthenticate method of FormLogin class,userName and password MUST not be null");
		}
		//登录帐号进行URL编码(防止有中文出现乱码,因为get方式不能直接传递中文参数)
		userName = StringUtils.encodeURIComponent(userName);
		//验证码
		String validateCode = authInfo.getValidateCode();
		
		this.addLoginParam(authInfo.getUsernameParamName(), userName);
		this.addLoginParam(authInfo.getPasswordParamName(), password);
		
		if(null != validateCode && !"".equals(validateCode)) {
			this.addLoginParam(authInfo.getValidateCodeParamName(), validateCode);
		}
		//其他额外的登录验证参数
		Map<String,String> params = setLoginParams();
		//设置请求参数
		addLoginParams(params);
		
		Map<String,String> headers = setLoginRequestHeaders();
		//设置请求头信息
		addRequestHeaderMap(headers);
		
		//获取登录请求URL
		String loginUrl = authInfo.getFinalLoginUrl();
		Result result = null;
		//开始登录
		try {
			result = HttpClientUtils.get(loginUrl, headers, params);
		} catch (Exception e) {
			result = null;
		}
		return buildFeedback(result);
	}
}
