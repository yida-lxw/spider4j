package com.yida.spider4j.crawler.auth.login;

import java.util.Map;

import com.yida.spider4j.crawler.auth.param.AuthInfo;
import com.yida.spider4j.crawler.auth.param.Feedback;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;
import com.yida.spider4j.crawler.utils.httpclient.Result;

/**
 * @ClassName: FormLogin
 * @Description: 表单登录(POST)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月30日 下午1:30:17
 *
 */
public abstract class FormLogin extends WebLogin {
	
	
	@Override
	public Feedback doAuthenticate(AuthInfo authInfo) {
		if(null == authInfo) {
			throw new IllegalArgumentException("When invoke the doAuthenticate method of FormLogin class,authInfo MUST not be null");
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
		//登录帐号编码
		userName = encodeUserName(userName);
		//密码加密
		password = encryptPassword(password);
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
		String loginUrl = authInfo.getLoginUrl();
		Result result = null;
		//开始登录
		try {
			result = HttpClientUtils.post(loginUrl, headers, params);
		} catch (Exception e) {
			result = null;
		}
		return buildFeedback(result);
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: method
	 * @Description: Http请求Method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	protected String method() {
		return HttpConstant.Method.POST;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getLoginParamMap
	 * @Description: 返回登录请求提交参数
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	public Map<String,String> getLoginParamMap() {
		return this.loginParams;
	}
}
