package com.yida.spider4j.crawler.processor.param;

public class StartPageProcessorParam extends PageProcessorParam {
	
	/************Regex*************/
    /**起始页URL正则表达式*/
    private String startUrlRegex;
    /**下一页URL正则表达式*/
	private String nextPageUrlRegex;
    
    
    /************Xpath*************/
    /**起始页URL Xpath表达式*/
    private String startUrlXpath;
    /**下一页URL Xpath表达式*/
	private String nextPageUrlXpath;
    
    
    /************Jsoup*************/
    /**起始页URL Jsoup表达式*/
    private String startUrlJsoup;
    /**下一页URL Jsoup表达式*/
	private String nextPageUrlJsoup;
	
	
	public String getStartUrlRegex() {
		return startUrlRegex;
	}
	public StartPageProcessorParam setStartUrlRegex(String startUrlRegex) {
		this.startUrlRegex = startUrlRegex;
		return this;
	}
	public String getNextPageUrlRegex() {
		return nextPageUrlRegex;
	}
	public StartPageProcessorParam setNextPageUrlRegex(String nextPageUrlRegex) {
		this.nextPageUrlRegex = nextPageUrlRegex;
		return this;
	}
	public String getStartUrlXpath() {
		return startUrlXpath;
	}
	public StartPageProcessorParam setStartUrlXpath(String startUrlXpath) {
		this.startUrlXpath = startUrlXpath;
		return this;
	}
	public String getNextPageUrlXpath() {
		return nextPageUrlXpath;
	}
	public StartPageProcessorParam setNextPageUrlXpath(String nextPageUrlXpath) {
		this.nextPageUrlXpath = nextPageUrlXpath;
		return this;
	}
	public String getStartUrlJsoup() {
		return startUrlJsoup;
	}
	public StartPageProcessorParam setStartUrlJsoup(String startUrlJsoup) {
		this.startUrlJsoup = startUrlJsoup;
		return this;
	}
	public String getNextPageUrlJsoup() {
		return nextPageUrlJsoup;
	}
	public StartPageProcessorParam setNextPageUrlJsoup(String nextPageUrlJsoup) {
		this.nextPageUrlJsoup = nextPageUrlJsoup;
		return this;
	}
}
