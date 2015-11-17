package com.yida.spider4j.crawler.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: SimpleStartPageProcessor
 * @Description: 详情页处理器默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午5:35:13
 *
 */
public abstract class SimpleDetailPageProcessor extends SimplePageProcessor implements DetailPageProcessor {
	public SimpleDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
		if(!(pageProcessorParam instanceof DetailPageProcessorParam)) {
			throw new IllegalArgumentException("pageProcessorParam MUST be type of DetailPageProcessorParam.");
		}
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: process
	 * @Description: 网页处理逻辑(解析网页数据)
	 * @param @param page
	 * @return void
	 * @throws
	 */
	@Override
	public void process(Page page) {
		//如果page为null或不是详情页,则直接跳过,什么都不做
		if(null == page || (page.getPageType() != null && !page.getPageType().equals(PageType.DETAIL_PAGE))) {
			return;
		}
		
		//这个具体留给用户去重写实现
		//page.putField("title", page.getHtml().xpath("//div[@class=adf]"));
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: checkValid
	 * @Description: 检测网页是否有效[有时,可能网页是下载成功了,可是网页里并没有我们需要提取的数据,
	 *               这时,我们视之为无效的网页]
	 * @param @param page
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean checkValid(Page page) {
		//默认认为所有下载的网页都是有效,如果你有特殊需求,需要排除一些无效网页,
		//请重写实现自己的检测逻辑
		return true;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestHeader
	 * @Description: 设置请求头信息
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	public Map<String,String> setRequestHeader() {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Language","zh-CN,zh;q=0.8");
		headers.put("Cache-Control","max-age=0");
		headers.put("Connection","keep-alive");
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.99 Safari/537.36");
		return headers;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestParams
	 * @Description: 设置请求参数[POST请求可能会需要设置]
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	public Map<String,String> setRequestParams() {
		Map<String,String> params = new HashMap<String,String>();
		//这里只是做个演示
		//params.put("", "");
		return params;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: requestBodyEncoding
	 * @Description: 告诉PageProcessor请求体的字符编码
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String requestBodyEncoding() {
		return HttpConstant.Charset.UTF_8;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: ajax
	 * @Description: 告诉PageProcessor,http请求是否为ajax请求
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean ajax() {
		return false;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: method
	 * @Description: 告诉PageProcessor,http请求的method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String method() {
		return HttpConstant.Method.GET;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: startUrl
	 * @Description: 告诉PageProcessor,起始页URL是什么
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String startUrl() {
		if(this.site == null) {
			return this.pageProcessorParam.getStartUrl();
		}
		String url = this.site.getStartUrl();
		if(StringUtils.isNotEmpty(url)) {
			return url;
		}
		url = this.pageProcessorParam.getStartUrl();
		this.site.setStartUrl(url);
		return url;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: startUrls
	 * @Description: 告诉PageProcessor,起始页URL是什么(批量指定多个)
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public List<String> startUrls() {
		if(this.site == null) {
			return this.pageProcessorParam.getStartUrls();
		}
		List<String> list = this.site.getStartUrls();
		if(null == list || list.size() <= 0) {
			list = this.pageProcessorParam.getStartUrls();
			this.site.setStartUrls(list);
			return list;
		}
		return list;
	}
}
