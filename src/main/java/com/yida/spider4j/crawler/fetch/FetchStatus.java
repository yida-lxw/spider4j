package com.yida.spider4j.crawler.fetch;
/**
 * 爬虫页面提取自定义状态码
 * @author  Lanxiaowei
 * @created 2013-09-05 16:10:26
 */
public class FetchStatus {
	/**提取成功*/
	public static final int OK = 1000;
	/**页面字节数过大*/
	public static final int PageTooBig = 1001;
	/**未知页面文档*/	
	public static final int UnknownDocument = 1002;
	/**URL被禁止访问*/
	public static final int URLnotAllowedByURLFilters = 1003;
	/**致命的协议违规*/
	public static final int FatalProtocolViolation = 1004;
	/**致命的传输错误*/
	public static final int FatalTransportError = 1005;
	/**未知错误*/
	public static final int UnknownError = 1006;
	/**页面加载错误*/
	public static final int PageLoadError = 1007;
	/**请求中止*/
	public static final int RequestForTermination = 1008;
	/**已请求过的重定向页面*/
	public static final int RedirectedPageIsSeen = 1010;
	/**空页面*/
	public static final int EMPTYPAGE = 1011;
	/**二进制数据*/
	public static final int PageIsBinary = 1012;
	/**访问的页面地址已转移*/
	public static final int Moved = 1013;
	/**访问的页面已转移到未知地址*/
	public static final int MovedToUnknownLocation = 1014;
}
