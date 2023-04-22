package com.yida.spider4j.crawler.utils.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * @ClassName: UnsupportedCharset
 * @Description: 系统暂不支持的字符集编码
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午2:50:12
 *
 */
public class UnsupportedCharset extends Charset {
	private static Map<String, Charset> singletons = new HashMap<String, Charset>();

	private UnsupportedCharset(String name) {
		super(name, null);
	}

	public static Charset forName(String name) {
		Charset ret = (Charset) singletons.get(name);
		if (ret == null) {
			ret = new UnsupportedCharset(name);
			singletons.put(name, ret);
		}
		return ret;
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

	public String displayName() {
		return super.displayName();
	}

	public String displayName(Locale locale) {
		return super.displayName(locale);
	}
}
