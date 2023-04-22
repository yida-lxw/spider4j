package com.yida.spider4j.crawler.utils.charset;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import com.yida.spider4j.crawler.utils.charset.core.NsDetector;
import com.yida.spider4j.crawler.utils.charset.core.NsICharsetDetectionObserver;
import com.yida.spider4j.crawler.utils.charset.core.NsPSMDetector;

public class CharsetDetectorFacade extends AbstractCharsetDetector implements NsICharsetDetectionObserver {
	private static NsDetector detector;
	private byte[] buf = new byte[4096];

	private Charset codpage = null;

	private int amountOfVerifiers = 0;

	/***************设计成单例 begin******************/
	private CharsetDetectorFacade () {
		detector = new NsDetector(NsPSMDetector.SIMPLIFIED_CHINESE);
		detector.init(this);
		this.amountOfVerifiers = detector.getProbableCharsets().length;
	}
	
	private static class SingletonHolder {  
        private static final CharsetDetectorFacade INSTANCE = new CharsetDetectorFacade();  
    }  

    public static final CharsetDetectorFacade getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  
    /***************设计成单例 end******************/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: detectCharset
     * @Description: 自动探测字符集编码
     * @param @param in
     * @param @return
     * @param @throws IOException
     * @return Charset
     * @throws
     */
    public Charset detectCharset(InputStream in) throws IOException {
    	return detectCharset(in, Integer.MAX_VALUE);
    }
    
    @Override
	public synchronized Charset detectCharset(InputStream in, int length) throws IOException {
		int len;
		reset();

		int read = 0;
		boolean done = false;
		Charset ret = null;
		do {
			len = in.read(this.buf, 0, Math.min(this.buf.length, length - read));
			if (len > 0) {
				read += len;
			}
			if (!(done))
				done = detector.doIt(this.buf, len, false);
		} while ((len > 0) && (!(done)));
		detector.dataEnd();
		if (this.codpage == null) {
			ret = guess();
		} else {
			ret = this.codpage;
		}
		return ret;
	}

	private Charset guess() {
		Charset ret = null;
		String[] possibilities = detector.getProbableCharsets();

		if (possibilities.length == this.amountOfVerifiers) {
			ret = Charset.forName("US-ASCII");
		} else {
			String check = possibilities[0];
			if (check.equalsIgnoreCase("nomatch"))
				ret = UnknownCharset.getInstance();
			else {
				for (int i = 0; (ret == null) && (i < possibilities.length); ++i) {
					try {
						ret = Charset.forName(possibilities[i]);
					} catch (UnsupportedCharsetException uce) {
						ret = UnsupportedCharset.forName(possibilities[i]);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void notify(String charset) {
		this.codpage = Charset.forName(charset);
	}

	public void reset() {
		detector.reset();
		this.codpage = null;
	}
}
