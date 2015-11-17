package com.yida.spider4j.crawler.utils.charset.core;
/**
 * @ClassName: NsDetector
 * @Description: 
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午3:12:52
 *
 */
public class NsDetector extends NsPSMDetector implements INsCharsetDetector {

	NsICharsetDetectionObserver mObserver = null;

	public NsDetector() {
		super();
	}

	public NsDetector(int langFlag) {
		super(langFlag);
	}

	public void init(NsICharsetDetectionObserver aObserver) {
		mObserver = aObserver;
		return;
	}

	public boolean doIt(byte[] aBuf, int aLen, boolean oDontFeedMe) {
		if (aBuf == null || oDontFeedMe)
			return false;

		this.HandleData(aBuf, aLen);
		return mDone;
	}

	public void report(String charset) {
		if (mObserver != null)
			mObserver.notify(charset);
	}

	public boolean isAscii(byte[] aBuf, int aLen) {
		for (int i = 0; i < aLen; i++) {
			if ((0x0080 & aBuf[i]) != 0) {
				return false;
			}
		}
		return true;
	}
}
