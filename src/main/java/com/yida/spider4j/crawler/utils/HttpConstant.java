package com.yida.spider4j.crawler.utils;

/**
 * @ClassName: HttpConstant
 * @Description: Http协议相关的常量
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 上午11:26:29
 * 
 */
public abstract class HttpConstant {

	public static abstract class Method {

		public static final String GET = "GET";

		public static final String HEAD = "HEAD";

		public static final String POST = "POST";

		public static final String PUT = "PUT";

		public static final String DELETE = "DELETE";

		public static final String TRACE = "TRACE";

		public static final String CONNECT = "CONNECT";

	}

	public static abstract class Header {
		public static final String REFERER = "Referer";

		public static final String USER_AGENT = "User-Agent";
	}
	
	public static abstract class Charset {
		public static final String UTF_8 = "UTF-8";

		public static final String GBK = "GBK";

		public static final String GB2312 = "GB2312";

		public static final String ISO_8859_1 = "ISO-8859-1";
	}
}
