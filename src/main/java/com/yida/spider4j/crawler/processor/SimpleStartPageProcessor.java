package com.yida.spider4j.crawler.processor;

import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: SimpleStartPageProcessor
 * @Description: 起始页处理器默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午5:35:13
 *
 */
public abstract class SimpleStartPageProcessor extends StartPageProcessor {
	public SimpleStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
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
		//分页每一页的URL
		buildPagingRequest(page, PageType.START_PAGE, PageType.LIST_PAGE);
	}
	
	/************************针对分页请求每一页beging*******************************/
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestHeaderPaging
	 * @Description: 设置分页请求头信息
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	protected Map<String,String> setRequestHeaderPaging() {
		return this.setRequestHeader();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestParamsPaging
	 * @Description: 设置分页请求参数[POST请求可能会需要设置]
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	protected Map<String,String> setRequestParamsPaging() {
		return this.setRequestParams();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: requestBodyEncodingPaging
	 * @Description: 告诉PageProcessor,分页请求体的字符编码
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	protected String requestBodyEncodingPaging() {
		return this.requestBodyEncoding();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: ajaxPaging
	 * @Description: 告诉PageProcessor,http 分页请求是否为ajax请求
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	protected boolean ajaxPaging() {
		return this.ajax();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: methodPaging
	 * @Description: 告诉PageProcessor,http分页请求的method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	protected String methodPaging() {
		return this.method();
	}
	/************************针对分页请求每一页end*******************************/
}
