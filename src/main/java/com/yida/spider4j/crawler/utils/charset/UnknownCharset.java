package com.yida.spider4j.crawler.utils.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @ClassName: UnknownCharset
 * @Description: 未知的字符集编码
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午2:49:22
 *
 */
public class UnknownCharset extends Charset {
	private static Charset instance;

	private UnknownCharset() {
		super("void", null);
	}

	public static Charset getInstance() {
		if (instance == null) {
			instance = new UnknownCharset();
		}
		return instance;
	}

	public boolean contains(Charset cs) {
		return false;
	}

	public CharsetDecoder newDecoder() {
		throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
	}

	public CharsetEncoder newEncoder() {
		throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
	}
}
