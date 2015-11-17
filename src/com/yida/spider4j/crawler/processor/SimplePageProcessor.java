package com.yida.spider4j.crawler.processor;

import java.util.List;

import com.yida.spider4j.crawler.core.Site;
import com.yida.spider4j.crawler.download.HttpClientDownloader;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.url.URLUtils;
/**
 * @ClassName: SimplePageProcessor
 * @Description: PageProcessor接口默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:39:29
 *
 */
public abstract class SimplePageProcessor implements PageProcessor {
	protected Site site;
	protected PageProcessorParam pageProcessorParam;
	
	protected HttpClientDownloader downloader;
	
	/**总页数*/
	protected int totalPage;
	
	/**每页显示大小*/
	protected int pageSize;
	
	public SimplePageProcessor(PageProcessorParam pageProcessorParam) {
		if(pageProcessorParam == null) {
			throw new IllegalArgumentException("pageProcessorParam MUST not be null.");
		}
		this.pageProcessorParam = pageProcessorParam;
		String startUrl = pageProcessorParam.getStartUrl();
		//起始页是否需要分页
		boolean multiPage = pageProcessorParam.isMulti();
		if(StringUtils.isNotEmpty(startUrl)) {
			if(multiPage) {
				this.site = Site.me().addStartUrl(startUrl)
	                .setDomain(URLUtils.getDomain(startUrl))
	                .setShouldStartPagePaging(true);
			} else {
				this.site = Site.me().setStartUrl(startUrl)
	                .setDomain(URLUtils.getDomain(startUrl))
	                .setShouldStartPagePaging(false);
			}
		} else {
			List<String> urls = pageProcessorParam.getStartUrls();
			if(null == urls || urls.size() <= 0) {
				//这里未设置起始URL,需要你在startUrl()函数里返回指定
				String startURL = startUrl();
				if(StringUtils.isNotEmpty(startURL)) {
					this.site = Site.me().setStartUrl(startUrl())
							.setShouldStartPagePaging(multiPage);
				} else {
					urls = startUrls();
					if(null == urls || urls.size() <= 0) {
						this.site = Site.me();
					} else {
						this.site = Site.me().setStartUrls(urls)
			                .setDomain(URLUtils.getDomain(urls.get(0)))
			                .setShouldStartPagePaging(multiPage);
					}
				}
			} else {
				this.site = Site.me().setStartUrls(urls)
	                .setDomain(URLUtils.getDomain(urls.get(0)))
	                .setShouldStartPagePaging(multiPage);
			}
		}
		if(this.downloader == null) {
			this.downloader = new HttpClientDownloader();
		}
	}
	
	@Override
	public Site getSite() {
		return this.site;
	}

	public PageProcessorParam getPageProcessorParam() {
		return pageProcessorParam;
	}

	public void setPageProcessorParam(PageProcessorParam pageProcessorParam) {
		this.pageProcessorParam = pageProcessorParam;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
