package com.yida.spider4j.crawler.fetch;
/**
 * HTTP状态码
 * @author  Lanxiaowei
 * @created 2013-09-05 17:29:13
 */
public class HttpStatus {
	/**客户端继续请求标识*/
	public static final int INFO_CONTINUE = 100;
	/**服务器已准备切换协议标识*/
	public static final int INFO_SWITCHING_PROTOCOL = 101;
	/**客户端请求成功*/
	public static final int SUCCESS_OK = 200;
	/**客户端请求成功并且服务器创建了新的资源*/
	public static final int SUCCESS_CREATED = 201;
	/**服务器已接受请求，准备开始处理请求*/
	public static final int SUCCESS_ACCEPTED = 202;
	/**服务器返回未授权的资源*/
	public static final int SUCCESS_NOT_AUTHORITATIVE = 203;
	/**客户端请求成功但页面无内容*/
	public static final int SUCCESS_NO_CONTENT = 204;
	/**客户端请求成功但页面无内容,并提醒客户端重置其文档视图*/
	public static final int SUCCESS_RESET_CONTENT = 205;
	/**服务器返回部分内容*/
	public static final int SUCCESS_PARTIAL_CONTENT = 206;
	/**服务器返回多种操作选择列表*/
	public static final int REDIRECT_MULTIPLE_CHOICE = 300;
	/**请求的资源已被永久性移动到新地址*/
	public static final int REDIRECT_MOVED_PERMANENTLY = 301;
	/**请求资源已临时移动到新地址但仍使用旧地址不重定向到新地址*/
	public static final int REDIRECT_MOVED_BUT_UNCHANGE = 302;
	/**提醒请求者请求其他资源地址*/
	public static final int REDIRECT_SEE_OTHER = 303;
	/**提醒请求者此资源自最后一次请求后没有被修改过*/
	public static final int REDIRECT_NOT_MODIFIED = 304;
	/**提请请求者需要使用代理访问*/
	public static final int REDIRECT_USE_PROXY = 305;
	/**请求者被临时重定向到新地址*/
	public static final int REDIRECT_TEMPORARY = 307;
	/**错误请求，服务器无法处理*/
	public static final int CLIENT_BAD_REQUEST = 400;
	/**请求要求身份验证*/
	public static final int CLIENT_NOT_AUTHORITATIVE = 401;
	/**提醒请求者，此请求为付费请求*/
	public static final int CLIENT_PAYMENT_REQUIRED = 402;
	/**服务器拒绝客户端请求*/
	public static final int CLIENT_FORBIDDEN = 403;
	/**服务器资源找不到*/
	public static final int CLIENT_NOT_FOUND = 404;
	/**请求的方法服务器不允许*/
	public static final int CLIENT_METHOD_NOT_ALLOWED = 405;
	/**服务器无法接收请求*/
	public static final int CLIENT_CANNOT_ACCEPTABLE = 406;
	/**请求需要使用代理*/
	public static final int CLIENT_REQUIRED_PROXY = 407;
	/**客户端请求超时*/
	public static final int CLIENT_REQUEST_TIMEOUT = 408;
	/**服务器在完成请求之前发生冲突*/
	public static final int CLIENT_CONFLICT_BEFORE_RESPONSE = 409;
	/**请求资源已删除*/
	public static final int CLIENT_HAVE_DELETED = 410;
	/**请求需要有效内容长度请求头*/
	public static final int CLIENT_LENGTH_REQUIRED = 411;
	/**请求不符合客户端设置的前提条件*/
	public static final int CLIENT_PRECONDITION_FAILED = 412;
	/**请求实体过大*/
	public static final int CLIENT_REQUEST_ENTITY_TO_LARGE = 413;
	/**请求URL长度过长*/
	public static final int CLIENT_REQUEST_URL_TO_LONG = 414;
	/**服务器不支持此MIME类型*/
	public static final int CLIENT_UNSPPORTED_MIME = 415;
	/**不符合RANGE请求范围*/
	public static final int CLIENT_UNCONFORM_RANGE_REQUEST = 416;
	/**服务器未满足EXPECTATION请求头*/
	public static final int CLIENT_EXPECTATION_FAILED = 417;
	/**服务器内部错误*/
	public static final int SERVER_ERROR = 500;
	/**服务器尚未实现请求的功能*/
	public static final int SERVER_NOT_IMPLEMENTED = 501;
	/**服务器作为网关，收到无效响应*/
	public static final int SERVER_BAD_GETWAY = 502;
	/**服务器停机维护，暂时不可用*/
	public static final int SERVER_UN_AVAILABLE = 503;
	/**服务器作为网关，接收上级服务器请求超时*/
	public static final int SERVER_GETWAY_TIMEOUT = 504;
	/**服务器不支持此HTTP版本*/
	public static final int SERVER_HTTP_VERSION_NOT_SUPPORTED = 505;
	
	/**
	 * 通过HTTP状态码判断请求是否成功
	 * @param code
	 * @return
	 */
	public static boolean isRequestSuccess(int code) {
    	return code >= 200 && code < 300;
    }
}
