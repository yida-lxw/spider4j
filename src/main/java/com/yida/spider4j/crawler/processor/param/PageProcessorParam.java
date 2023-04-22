package com.yida.spider4j.crawler.processor.param;

import java.util.List;

/**
 * @ClassName: PageProcessorParam
 * @Description: PageProcessorParam处理需要的参数
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午5:25:59
 *
 */
public abstract class PageProcessorParam {
	/**属性名称*/
	private String attributeName;
	
	/**组数,从零开始计算(group[0]表示源串)*/
	private int group;
	
	/**是否需要匹配多个,false表示只匹配第一个,即使符合条件的有多个*/
	private boolean multi = true;
	
	/**起始页URL*/
	private String startUrl;
	
	/**起始页URL[应对起始URL可能有多个情况]*/
	private List<String> startUrls;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public List<String> getStartUrls() {
		return startUrls;
	}

	public void setStartUrls(List<String> startUrls) {
		this.startUrls = startUrls;
	}
}
