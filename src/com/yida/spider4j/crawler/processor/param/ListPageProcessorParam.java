package com.yida.spider4j.crawler.processor.param;

public class ListPageProcessorParam extends PageProcessorParam {
	/************Regex*************/
    /**列表页URL正则表达式*/
    private String listUrlRegex;
    /**下一页URL正则表达式*/
	private String nextPageUrlRegex;
    
    
    /************Xpath*************/
    /**列表页URL Xpath表达式*/
    private String listUrlXpath;
    /**下一页URL Xpath表达式*/
	private String nextPageUrlXpath;
    
    
    /************Jsoup*************/
    /**列表页URL Jsoup表达式*/
    private String listUrlJsoup;
    /**下一页URL Jsoup表达式*/
	private String nextPageUrlJsoup;
	
	
	public String getListUrlRegex() {
		return listUrlRegex;
	}
	public ListPageProcessorParam setListUrlRegex(String listUrlRegex) {
		this.listUrlRegex = listUrlRegex;
		return this;
	}
	public String getNextPageUrlRegex() {
		return nextPageUrlRegex;
	}
	public ListPageProcessorParam setNextPageUrlRegex(String nextPageUrlRegex) {
		this.nextPageUrlRegex = nextPageUrlRegex;
		return this;
	}
	public String getListUrlXpath() {
		return listUrlXpath;
	}
	public ListPageProcessorParam setListUrlXpath(String listUrlXpath) {
		this.listUrlXpath = listUrlXpath;
		return this;
	}
	public String getNextPageUrlXpath() {
		return nextPageUrlXpath;
	}
	public ListPageProcessorParam setNextPageUrlXpath(String nextPageUrlXpath) {
		this.nextPageUrlXpath = nextPageUrlXpath;
		return this;
	}
	public String getListUrlJsoup() {
		return listUrlJsoup;
	}
	public ListPageProcessorParam setListUrlJsoup(String listUrlJsoup) {
		this.listUrlJsoup = listUrlJsoup;
		return this;
	}
	public String getNextPageUrlJsoup() {
		return nextPageUrlJsoup;
	}
	public ListPageProcessorParam setNextPageUrlJsoup(String nextPageUrlJsoup) {
		this.nextPageUrlJsoup = nextPageUrlJsoup;
		return this;
	}
}
