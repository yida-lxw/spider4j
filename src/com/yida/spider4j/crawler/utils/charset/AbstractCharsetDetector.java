package com.yida.spider4j.crawler.utils.charset;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
/**
 * @ClassName: AbstractCharsetDetector
 * @Description: ICharsetDetector接口的默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午2:41:06
 *
 */
public abstract class AbstractCharsetDetector implements ICharsetDetector {

	@Override
	public int compareTo(ICharsetDetector o) {
		String other = o.getClass().getName();
		String mine = super.getClass().getName();
		return mine.compareTo(other);
	}

	@Override
	public Reader open(URL url) throws IOException {
		Reader ret = null;
		Charset cs = detectCharset(url);
		if (cs != null) {
			ret = new InputStreamReader(new BufferedInputStream(url.openStream()), cs);
		}
		return ret;
	}

	@Override
	public Charset detectCharset(URL url) throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		Charset result = detectCharset(in, 2147483647);
		in.close();
		return result;
	}
}
