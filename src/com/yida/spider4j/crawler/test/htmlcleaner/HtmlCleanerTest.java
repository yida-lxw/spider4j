package com.yida.spider4j.crawler.test.htmlcleaner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;

/**
 * @ClassName: HtmlCleanerTest
 * @Description: HTMLCleaner使用测试
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月18日 下午3:36:45
 *
 */
public class HtmlCleanerTest {
	public static void main(String[] args) throws Exception {
		/*HtmlCleaner cleaner = new HtmlCleaner();
		URL url = new URL("http://movie.douban.com/subject/1292052/");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    InputStream in = conn.getInputStream();
	    TagNode node = cleaner.clean(in, "UTF-8");
	    DomSerializer ser = new DomSerializer(cleaner.getProperties());
	    org.w3c.dom.Document document = ser.createDOM(node);
	    XMLUtils utils = XMLUtils.getInstance();
	    List<String> list = utils.getMultiNodeText(document, "//span[contains(text(),'类型')]/following-sibling::span[@property='v:genre'] | "
	    		+ "//span[contains(text(),'制片国家/地区:')]/preceding-sibling::span[@property='v:genre']");
	    if(null == list || list.size() <= 0) {
	    	System.out.println("Nothing.");
	    } else {
	    	for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
	    }*/
	    
		//因为我发现音悦台的MV ID都是7位数字
		int max = 9999999;
		for(int i=0; i < max; i++) {
			String videoId = i + "";
		    String html = HttpClientUtils.getHTML("http://www.yinyuetai.com/insite/get-video-info?flex=true&videoId=" + videoId);
		    //System.out.println(html);
		    if(html == null || "".equals(html) || html.indexOf("找不到编号为") >= 0) {
		    	System.out.println("没有id={" + videoId + "}这个MV");
		    	continue;
		    }
		    
		  //音悦台VIP专享MV
		    Pattern pattern = Pattern.compile(".+(http://sh.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.mp4\\?(?!http).*?&vst=0Y).+");
		    Matcher matcher = pattern.matcher(html);
		    if(matcher.find()) {
		    	String url = matcher.group(1);
		    	System.out.println("id={" + videoId + "}VIP专享MV url:" + url);
		    } else {
		    	//超清MV
			    pattern = Pattern.compile(".+(http://he.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.flv\\?(?!http).*?&vst=0Y).+");
			    matcher = pattern.matcher(html);
			    if(matcher.find()) {
			    	String url = matcher.group(1);
			    	System.out.println("id={" + videoId + "}超清MV url:" + url);
			    } else {
			    	//高清MV
				    //.+(http://hd.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.flv\\?[^?]+&vst=0Y).+
				    pattern = Pattern.compile(".+(http://hd.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.flv\\?(?!http).*?&vst=0Y).+");
				    matcher = pattern.matcher(html);
				    if(matcher.find()) {
				    	String url = matcher.group(1);
				    	System.out.println("id={" + videoId + "}高清MV url:" + url);
				    } else {
				    	//流畅MV
					    //.+(http://hc.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.flv\\?[^?]+&vst=0Y).+
					    pattern = Pattern.compile(".+(http://hc.yinyuetai.com/uploads/videos/common/[a-zA-Z0-9]+\\.flv\\?(?!http).*?&vst=0Y).+");
					    matcher = pattern.matcher(html);
					    if(matcher.find()) {
					    	String url = matcher.group(1);
					    	System.out.println("id={" + videoId + "}流畅MV url:" + url);
					    } else {
					    	continue;
					    }
				    }
			    }
		    }
		    
		}
	    
	}
}
