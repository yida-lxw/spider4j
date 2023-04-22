package com.yida.spider4j.crawler.processor.param;
/**
 * @ClassName: SeedPageProcessorParam
 * @Description: 种子页网页处理器的参数
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午9:53:30
 *
 */
public class SeedPageProcessorParam extends PageProcessorParam {
	/************Regex*************/
    /**种子页URL正则表达式*/
    private String seedUrlRegex;
    /**下一页URL正则表达式*/
	private String nextPageUrlRegex;
    
    
    /************Xpath*************/
    /**种子页URL Xpath表达式*/
    private String seedUrlXpath;
    /**下一页URL Xpath表达式*/
	private String nextPageUrlXpath;
    
    
    /************Jsoup*************/
    /**种子页URL Jsoup表达式*/
    private String seedUrlJsoup;
    /**下一页URL Jsoup表达式*/
	private String nextPageUrlJsoup;
	
	
	public String getSeedUrlRegex() {
		return seedUrlRegex;
	}
	public SeedPageProcessorParam setSeedUrlRegex(String seedUrlRegex) {
		this.seedUrlRegex = seedUrlRegex;
		return this;
	}
	public String getNextPageUrlRegex() {
		return nextPageUrlRegex;
	}
	public SeedPageProcessorParam setNextPageUrlRegex(String nextPageUrlRegex) {
		this.nextPageUrlRegex = nextPageUrlRegex;
		return this;
	}
	public String getSeedUrlXpath() {
		return seedUrlXpath;
	}
	public SeedPageProcessorParam setSeedUrlXpath(String seedUrlXpath) {
		this.seedUrlXpath = seedUrlXpath;
		return this;
	}
	public String getNextPageUrlXpath() {
		return nextPageUrlXpath;
	}
	public SeedPageProcessorParam setNextPageUrlXpath(String nextPageUrlXpath) {
		this.nextPageUrlXpath = nextPageUrlXpath;
		return this;
	}
	public String getSeedUrlJsoup() {
		return seedUrlJsoup;
	}
	public SeedPageProcessorParam setSeedUrlJsoup(String seedUrlJsoup) {
		this.seedUrlJsoup = seedUrlJsoup;
		return this;
	}
	public String getNextPageUrlJsoup() {
		return nextPageUrlJsoup;
	}
	public SeedPageProcessorParam setNextPageUrlJsoup(String nextPageUrlJsoup) {
		this.nextPageUrlJsoup = nextPageUrlJsoup;
		return this;
	}
}
