package com.yida.spider4j.crawler.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PlainText
 * @Description: 纯文本(对应MIME:text/plain)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午2:34:24
 * 
 */
public class PlainText extends AbstractSelectable {
	protected List<String> sourceTexts;

	public PlainText(List<String> sourceTexts) {
		this.sourceTexts = sourceTexts;
	}

	public PlainText(String text) {
		this.sourceTexts = new ArrayList<String>();
		sourceTexts.add(text);
	}

	public static PlainText create(String text) {
		return new PlainText(text);
	}

	@Override
	public Selectable xpath(String xpath) {
		throw new UnsupportedOperationException("xpath method don't supporte plain text");
	}

	@Override
	public Selectable xpath(String xpath, boolean multi) {
		throw new UnsupportedOperationException("xpath method don't supporte plain text");
	}

	@Override
	public Selectable xpath(String xpath, String attrName) {
		throw new UnsupportedOperationException("xpath method don't supporte plain text");
	}

	@Override
	public Selectable xpath(String xpath, String attrName, boolean multi) {
		throw new UnsupportedOperationException("xpath method don't supporte plain text");
	}

	@Override
	public Selectable jsoup(String jsoupExpression) {
		throw new UnsupportedOperationException("jsoup method don't supporte plain text");
	}
	
	@Override
	public Selectable jsoup(String jsoupExpression, boolean multi) {
		throw new UnsupportedOperationException("jsoup method don't supporte plain text");
	}

	@Override
	public Selectable jsoup(String jsoupExpression, String attrName) {
		throw new UnsupportedOperationException("jsoup method don't supporte plain text");
	}

	@Override
	public Selectable jsoup(String jsoupExpression, String attrName,
			boolean multi) {
		throw new UnsupportedOperationException("jsoup method don't supporte plain text");
	}

	@Override
	public Selectable smartContent() {
		throw new UnsupportedOperationException("smartContent method don't supporte plain text");
	}

	@Override
	public Selectable links() {
		throw new UnsupportedOperationException("links method don't supporte plain text");
	}
	
	@Override
	public List<Selectable> nodes() {
		List<Selectable> nodes = new ArrayList<Selectable>(getSourceTexts().size());
        for (String string : getSourceTexts()) {
            nodes.add(PlainText.create(string));
        }
        return nodes;
	}

	@Override
	protected List<String> getSourceTexts() {
		return sourceTexts;
	}
}
