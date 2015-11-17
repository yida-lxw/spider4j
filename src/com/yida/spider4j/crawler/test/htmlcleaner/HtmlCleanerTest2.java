package com.yida.spider4j.crawler.test.htmlcleaner;

import java.util.List;

import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.yida.spider4j.crawler.utils.xml.XMLUtils;

/**
 * @ClassName: HtmlCleanerTest
 * @Description: HTMLCleaner使用测试
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月18日 下午3:36:45
 *
 */
public class HtmlCleanerTest2 {
	public static void main(String[] args) throws Exception {
		HtmlCleaner cleaner = new HtmlCleaner();
		String fragment = "<link rel=\"apple-touch-icon\" href=\"/pics/movie/apple-touch-icon.png\"><data><employee><name id=\"1\">益达</name><name id=\"2\">yida</name>"
				+ "<title>Manager</title></employee></data>";
	    TagNode node = cleaner.clean(fragment);
	    DomSerializer ser = new DomSerializer(cleaner.getProperties());
	    org.w3c.dom.Document document = ser.createDOM(node);
	    XMLUtils utils = XMLUtils.getInstance();
	    List<String> list = utils.getMultiNodeText(document, "//employee/name[@id='1']");
	    if(null == list || list.size() <= 0) {
	    	System.out.println("Nothing.");
	    } else {
	    	for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
	    }
	}
}
