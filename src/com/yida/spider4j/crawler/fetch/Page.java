package com.yida.spider4j.crawler.fetch;


/**
 * 网页
 * @author  Lanxiaowei
 * @created 2013-08-29 19:11:55
 */
public class Page {
	/**当前网页的资源地址*/
	//private WebURL url;
	/**标题*/
	private String title;
	/**网页内容*/
	private String html;
	/**网页内容的字符编码*/
	private String charset;
	/**二进制数据[一般指的是图片,MP3,视频，CSS,JS文件等资源]*/
	private byte[] binaryData;
	/**从当前网页中提取出来的资源地址*/
	/*private List<WebURL> urls;
	public WebURL getUrl() {
		return url;
	}
	public void setUrl(WebURL url) {
		this.url = url;
	}*/
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public byte[] getBinaryData() {
		return binaryData;
	}
	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
	}
	/*public List<WebURL> getUrls() {
		return urls;
	}
	public void setUrls(List<WebURL> urls) {
		this.urls = urls;
	}
	public Page(WebURL url, String title, String html) {
		this.url = url;
		this.title = title;
		this.html = html;
	}
	public Page(WebURL url, String title, String html, String charset) {
		this.url = url;
		this.title = title;
		this.html = html;
		this.charset = charset;
	}*/
}
