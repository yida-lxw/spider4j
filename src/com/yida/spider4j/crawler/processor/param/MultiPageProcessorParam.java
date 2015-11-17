package com.yida.spider4j.crawler.processor.param;

public class MultiPageProcessorParam extends PageProcessorParam {
	/**分页每一页POST请求URL,注意是POST请求,不是GET请求*/
	private String pagingPagePostUrl;
	
	/************Regex*************/
    /**目标页URL正则表达式*/
    private String targetUrlRegex;
    /**下一页URL正则表达式*/
	private String nextPageUrlRegex;
    
    
    /************Xpath*************/
    /**目标页URL Xpath表达式*/
    private String targetUrlXpath;
    /**下一页URL Xpath表达式*/
	private String nextPageUrlXpath;
    
    
    /************Jsoup*************/
    /**目标页URL Jsoup表达式*/
    private String targetUrlJsoup;
    /**下一页URL Jsoup表达式*/
	private String nextPageUrlJsoup;
	
	
	public String getPagingPagePostUrl() {
		return pagingPagePostUrl;
	}
	public MultiPageProcessorParam setPagingPagePostUrl(String pagingPagePostUrl) {
		this.pagingPagePostUrl = pagingPagePostUrl;
		return this;
	}
	public String getTargetUrlRegex() {
		return targetUrlRegex;
	}
	public MultiPageProcessorParam setTargetUrlRegex(String targetUrlRegex) {
		this.targetUrlRegex = targetUrlRegex;
		return this;
	}
	public String getNextPageUrlRegex() {
		return nextPageUrlRegex;
	}
	public MultiPageProcessorParam setNextPageUrlRegex(String nextPageUrlRegex) {
		this.nextPageUrlRegex = nextPageUrlRegex;
		return this;
	}
	public String getTargetUrlXpath() {
		return targetUrlXpath;
	}
	public MultiPageProcessorParam setTargetUrlXpath(String targetUrlXpath) {
		this.targetUrlXpath = targetUrlXpath;
		return this;
	}
	public String getNextPageUrlXpath() {
		return nextPageUrlXpath;
	}
	public MultiPageProcessorParam setNextPageUrlXpath(String nextPageUrlXpath) {
		this.nextPageUrlXpath = nextPageUrlXpath;
		return this;
	}
	public String getTargetUrlJsoup() {
		return targetUrlJsoup;
	}
	public MultiPageProcessorParam setTargetUrlJsoup(String targetUrlJsoup) {
		this.targetUrlJsoup = targetUrlJsoup;
		return this;
	}
	public String getNextPageUrlJsoup() {
		return nextPageUrlJsoup;
	}
	public MultiPageProcessorParam setNextPageUrlJsoup(String nextPageUrlJsoup) {
		this.nextPageUrlJsoup = nextPageUrlJsoup;
		return this;
	}
}
