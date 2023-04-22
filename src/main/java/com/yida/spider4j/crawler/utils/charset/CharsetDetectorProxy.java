package com.yida.spider4j.crawler.utils.charset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * @ClassName: CharsetDetectorProxy
 * @Description: 字符集编码探测器代理
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午2:56:21
 *
 */
public class CharsetDetectorProxy extends AbstractCharsetDetector {
	
	private Set<ICharsetDetector> detectors = new LinkedHashSet<ICharsetDetector>();

	/***************设计成单例 begin******************/
	private CharsetDetectorProxy () {}
	
	private static class SingletonHolder {  
        private static final CharsetDetectorProxy INSTANCE = new CharsetDetectorProxy();  
    }  

    public static final CharsetDetectorProxy getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  
    /***************设计成单例 end******************/

	public boolean add(ICharsetDetector detector) {
		return this.detectors.add(detector);
	}

	@Override
	public Charset detectCharset(URL url) throws IOException {
		Charset ret = null;
		Iterator<ICharsetDetector> detectorIt = this.detectors.iterator();
		while (detectorIt.hasNext()) {
			ret = ((ICharsetDetector) detectorIt.next()).detectCharset(url);
			if ((ret != null) && (ret != UnknownCharset.getInstance()) && (!(ret instanceof UnsupportedCharset))) {
				break;
			}
		}
		return ret;
	}

	@Override
	public Charset detectCharset(InputStream in, int length) throws IOException, IllegalArgumentException {
		if (!(in.markSupported())) {
			throw new IllegalArgumentException("The given input stream (" + in.getClass().getName()
					+ ") has to support marking.");
		}
		Charset ret = null;
		int markLimit = length;
		Iterator<ICharsetDetector> detectorIt = this.detectors.iterator();
		while (detectorIt.hasNext()) {
			in.mark(markLimit);
			ret = ((ICharsetDetector) detectorIt.next()).detectCharset(in, length);
			try {
				in.reset();
			} catch (IOException ioex) {
				throw new IllegalArgumentException(
						"More than the given length had to be read and the given stream could not be reset. Undetermined state for this detection.");
			}

			if ((ret != null) && (ret != UnknownCharset.getInstance()) && (!(ret instanceof UnsupportedCharset))) {
				break;
			}
		}

		return ret;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		Iterator<ICharsetDetector> it = this.detectors.iterator();
		int i = 1;
		while (it.hasNext()) {
			ret.append(i);
			ret.append(") ");
			ret.append(it.next().getClass().getName());
			ret.append("\n");
			++i;
		}
		return ret.toString();
	}
}
