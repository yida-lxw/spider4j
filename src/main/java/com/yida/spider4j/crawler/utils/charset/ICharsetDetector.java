package com.yida.spider4j.crawler.utils.charset;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @ClassName: ICharsetDetector
 * @Description: 字符集探测接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午2:35:25
 *
 */
public interface ICharsetDetector extends Comparable<ICharsetDetector> {
	Reader open(URL url) throws IOException;

	Charset detectCharset(URL url) throws IOException;

	Charset detectCharset(InputStream inputStream, int length) throws IOException;
}
