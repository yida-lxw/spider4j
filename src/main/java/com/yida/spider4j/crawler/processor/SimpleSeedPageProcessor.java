package com.yida.spider4j.crawler.processor;

import java.util.HashMap;
import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: SimpleSeedPageProcessor
 * @Description: 种子页处理器默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午9:52:19
 *
 */
public abstract class SimpleSeedPageProcessor extends SeedPageProcessor {
	public SimpleSeedPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestHeaderPaging
	 * @Description: 设置分页请求头信息
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	protected Map<String,String> setRequestHeaderPaging() {
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
	 * @Title: setRequestParamsPaging
	 * @Description: 设置Post请求参数[POST请求可能会需要设置]
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	protected Map<String,String> setRequestParamsPaging() {
		Map<String,String> params = new HashMap<String,String>();
		//这里只是做个演示
		//params.put("", "");
		return params;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildRequest
	 * @Description: 构造Request对象
	 * @param @param page
	 * @param @return
	 * @return List<Request>
	 * @throws
	 */
	@Override
	public void buildRequest(Page page) {
		boolean paging = needPaging();
		//分页每一页里的pageSize个目标URL
		buildTargetRequest(page, PageType.START_PAGE);
		
		//若需要分页才提取分页请求每一页的URL
		if(paging) {
			//分页每一页的URL
			buildPagingRequest(page, PageType.SEED_PAGE, PageType.SEED_PAGING_PAGE);
		}
	}
}
