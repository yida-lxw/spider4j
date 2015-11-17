package com.yida.spider4j.crawler.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.Constant;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: AbstractMultiPageProcessor
 * @Description: MultiPageProcessor的默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午10:50:13
 *
 */
public abstract class AbstractMultiPageProcessor extends SimplePageProcessor implements MultiPageProcessor {
	public AbstractMultiPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
		if(!(pageProcessorParam instanceof MultiPageProcessorParam)) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + "'s pageProcessorParam MUST be type of MultiPageProcessorParam.");
		}
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determineTotalPage
	 * @Description: 探测总页数
	 * @param @param page
	 * @param pagesize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int determineTotalPage(Page page,int pageSize) {
		return 0;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determinePageSize
	 * @Description: 探测每页显示大小
	 * @param @param page
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int determinePageSize(Page page) {
		return Constant.PAGE_SIZE;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: knownTotalPage
	 * @Description: 告诉PageProcessor,从已有的分页页面内容可否已知总页数
	 *               需要用户实现
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean knownTotalPage() {
		return true;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildTargetRequest
	 * @Description: 构建目标请求(每一页的pageSize个目标URL)
	 * @param @param page
	 * @param @param pageType
	 * @return void
	 * @throws
	 */
	protected void buildTargetRequest(Page page,PageType pageType) {
		//先从当前页提取详情页URL
		List<String> urlList = this.pickUpTargetUrl(page);
		if(null != urlList && urlList.size() > 0) {
			//仅供调试,你懂的
			/*if(page.getPageType().equals(PageType.LIST_PAGE)) {
				for(String url : urlList) {
					System.out.println("detail-url:" + url);
				}
			}*/
			//从当前页提取URL放入队列
			page.addTargetRequests(urlList, this.method(), 1, this.setRequestHeader(), 
					this.setRequestParams(), this.ajax(), this.requestBodyEncoding(),pageType);
		}
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildPagingRequest
	 * @Description: 构建分页请求(每一页的请求URL)
	 * @param @param page
	 * @param @param sourcePageType
	 * @param @param pagingPageType
	 * @return void
	 * @throws
	 */
	protected void buildPagingRequest(Page page,PageType sourcePageType,PageType pagingPageType) {
		if(page.getPageType() != null && sourcePageType.equals(page.getPageType())) {
			//如果种子页不需要分页,那直接结束了
			if(!this.needPaging()) {
				return;
			}
			
			//获取每页显示大小
			int pageSize = this.determinePageSize(page);
			if(pageSize <= 0) {
				pageSize = Constant.PAGE_SIZE;
			}
			this.pageSize = pageSize;
			
			int currentPageNum = this.judgeCurrentPage(page,this.pageSize);
			if(currentPageNum == 1) {
				//如果种子页需要分页,则从当前页提取其他所有页请求URL,然后放去URL调度器
				//这里又要分GET和POST两种情况:
				//1.POST请求的URL不变,一般变化的是POST提交参数
				//2.GET请求一般变化的URL
				if(HttpConstant.Method.GET.equalsIgnoreCase(this.methodPaging())) {
					List<String> pagingUrls = this.pickUpPagingUrl(page);
					if(null != pagingUrls && pagingUrls.size() > 0) {
						page.addTargetRequests(pagingUrls, HttpConstant.Method.GET, 
							page.getRequest().getPriority(), this.setRequestHeaderPaging(), 
							this.setRequestParamsPaging(), this.ajaxPaging(), 
							this.requestBodyEncodingPaging(),pagingPageType);
					}
				} 
				// POST请求
				else {
					List<Map<String,String>> paramList = this.pickUpListParam(page);
					if(null != paramList && !paramList.isEmpty()) {
						String pageRequestUrl = this.buildNextPageUrl(page, currentPageNum, totalPage, pageSize);
						if(StringUtils.isEmpty(pageRequestUrl)) {
							pageRequestUrl = page.getRequest().getUrl();
						}
						if(StringUtils.isEmpty(pageRequestUrl)) {
							return;
						}
						page.addTargetRequests(pageRequestUrl, HttpConstant.Method.POST, 
							page.getRequest().getPriority(), this.setRequestHeaderPaging(), 
							paramList, this.ajaxPaging(),  
							this.requestBodyEncodingPaging(),pagingPageType);
					}
				}
			}
		}
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: pickUpListParam
	 * @Description: 从当前页提取列表请求参数[即下一页的POST提交参数,适用于POST请求]
	 * @param @param page
	 * @param @return
	 * @return List<Map<String,String>>
	 * @throws
	 */
	protected List<Map<String,String>> pickUpListParam(Page page) {
		List<Map<String,String>> paramList = new ArrayList<Map<String,String>>();

		//获取每页显示大小
		int pageSize = this.determinePageSize(page);
		if(pageSize <= 0) {
			pageSize = Constant.PAGE_SIZE;
		}
		this.pageSize = pageSize;
		
		//如果分页总页数已知的情况的下:
		if(this.knownTotalPage()) {
			//获取总页数
			int totalPage = this.determineTotalPage(page,this.pageSize);
			if(totalPage <= 0) {
				return null;
			}
			this.totalPage = totalPage;
			
			for(int i=0; i < this.totalPage; i++) {
				Map<String,String> params = this.nextPagePost(page, this.totalPage, this.pageSize, i+1);
				if(params != null && !params.isEmpty()) {
					paramList.add(params);
				}
			}
		} 
		//总页数无法得知的情况下,从1开始遍历,直到isLastPage()返回false
		else {
			int currentPage = 1;
			Map<String,String> params = null;
			do {
				params = this.nextPagePost(page, this.totalPage, this.pageSize, currentPage++);
				if(params != null && !params.isEmpty()) {
					paramList.add(params);
				}
			} while(!this.isLastPage(page,null,params));
		}
		return paramList;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: pickUpPagingUrl
	 * @Description: 从当前页提取每一页URL[适用于GET请求]
	 * @param @param page
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	protected List<String> pickUpPagingUrl(Page page) {
		List<String> urlList = new ArrayList<String>();
		
		if(this.pageSize <= 0) {
			//获取每页显示大小
			int pageSize = this.determinePageSize(page);
			if(pageSize <= 0) {
				pageSize = Constant.PAGE_SIZE;
			}
			this.pageSize = pageSize;
		}
		
		//如果总页数已知的情况的下:
		if(knownTotalPage()) {
			//获取总页数
			int totalPage = this.determineTotalPage(page,this.pageSize);
			if(totalPage <= 0) {
				return null;
			}
			this.totalPage = totalPage;
			
			for(int i=0; i < this.totalPage; i++) {
				String pageUrl = this.nextPageGet(page, this.totalPage, this.pageSize, i+1);
				if(StringUtils.isNotEmpty(pageUrl) && 
					!pageUrl.equals(page.getRequest().getUrl())) {
					urlList.add(pageUrl);
				}
			}
		} 
		//总页数无法得知的情况下,从1开始遍历,直到isLastPage()返回false
		else {
			int currentPage = 1;
			String pageUrl = null;
			do {
				pageUrl = this.nextPageGet(page, this.totalPage, this.pageSize,currentPage++);
				if(StringUtils.isNotEmpty(pageUrl) && 
						!pageUrl.equals(page.getRequest().getUrl())) {
					urlList.add(pageUrl);
				}
			} while(!this.isLastPage(page,pageUrl,null));
		}
		return urlList;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: nextPageGet
	 * @Description: 获取下一页(GET请求)
	 * @param @param page           当前页
	 * @param @param totalPage      总页数
	 * @param @param pageSize       每页显示大小
	 * @param @param currentPage    当前页码,即第几页,从1开始计算
	 * @return String
	 * @throws
	 */
	protected String nextPageGet(Page page,int totalPage,int pageSize,int currentPage) {
		String nextPageUrl = this.buildNextPageUrl(page, currentPage,totalPage, pageSize);
		//若下一页链接没提取到,那直接跳过不处理,保险起见哈
		if(StringUtils.isEmpty(nextPageUrl)) {
			return null;
		}
		return nextPageUrl;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: nextPagePost
	 * @Description: 获取下一页(POST请求)
	 * @param @param page
	 * @param @param totalPage
	 * @param @param pageSize
	 * @param @param currentPage
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	public Map<String,String> nextPagePost(Page page,int totalPage,int pageSize,int currentPage) {
		//POST请求参数需要用户自己重写实现
		return null;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildNextPageUrl
	 * @Description: 构建下一页的请求URL
	 * @param @param page
	 * @param @param currentPage
	 * @param @param totalPage
	 * @param @param pageSize
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String buildNextPageUrl(Page page, int currentPage, int totalPage,
			int pageSize) {
		MultiPageProcessorParam multiPageProcessorParam = (MultiPageProcessorParam)this.pageProcessorParam;
		//正则表达式
		String nextPageUrlRegex = multiPageProcessorParam.getNextPageUrlRegex();
		if(StringUtils.isNotEmpty(nextPageUrlRegex)) {
			return page.getHtml(ExpressionType.REGEX).regex(nextPageUrlRegex,multiPageProcessorParam.getGroup()).get();
		}
		//Xpath表达式
		String nextPageUrlXpath = multiPageProcessorParam.getNextPageUrlXpath();
		if(StringUtils.isNotEmpty(nextPageUrlXpath)) {
			return page.getHtml(ExpressionType.XPATH).xpath(nextPageUrlXpath, multiPageProcessorParam.getAttributeName()).get();
		}
		//Jsoup表达式
		String nextPageUrlJsoup = multiPageProcessorParam.getNextPageUrlJsoup();
		if(StringUtils.isNotEmpty(nextPageUrlJsoup)) {
			return page.getHtml(ExpressionType.JSOUP).jsoup(nextPageUrlJsoup, multiPageProcessorParam.getAttributeName()).get();
		}
		//若这3个表达式,用户都没提供,那没法提取下一页URL了,只能return null;
		//更复杂的逻辑留给用户去重写实现
		return null;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: pickUpTargetUrl
	 * @Description: 从当前页提取目标URL
	 * @param @param page
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	@Override
	public List<String> pickUpTargetUrl(Page page) {
		List<String> urlList = new ArrayList<String>();
		MultiPageProcessorParam multiPageProcessorParam = (MultiPageProcessorParam)this.pageProcessorParam;
		//正则表达式
		if(StringUtils.isNotEmpty(multiPageProcessorParam.getTargetUrlRegex())) {
			if(page.getHtml(ExpressionType.REGEX) != null) {
				urlList = page.getHtml(ExpressionType.REGEX).regex(multiPageProcessorParam.getTargetUrlRegex(), multiPageProcessorParam.getGroup()).all();
			} else if(page.getJson() != null) {
				urlList = page.getJson().regex(multiPageProcessorParam.getTargetUrlRegex(), multiPageProcessorParam.getGroup()).all();
			}
		} 
		//xpath表达式
		else if(StringUtils.isNotEmpty(multiPageProcessorParam.getTargetUrlXpath())) {
			if(page.getHtml(ExpressionType.XPATH) != null) {
				urlList = page.getHtml(ExpressionType.XPATH).xpath(multiPageProcessorParam.getTargetUrlXpath(), 
						multiPageProcessorParam.getAttributeName(), multiPageProcessorParam.isMulti())
					.all();
			} else if(page.getJson() != null) {
				urlList = page.getJson().xpath(multiPageProcessorParam.getTargetUrlXpath(), 
						multiPageProcessorParam.getAttributeName(), multiPageProcessorParam.isMulti())
						.all();
			}
		}
		//jsoup表达式
		else if(StringUtils.isNotEmpty(multiPageProcessorParam.getTargetUrlJsoup())) {
			if(page.getHtml(ExpressionType.JSOUP) != null) {
				urlList = page.getHtml(ExpressionType.JSOUP).jsoup(multiPageProcessorParam.getTargetUrlJsoup(), 
					multiPageProcessorParam.getAttributeName(), multiPageProcessorParam.isMulti())
					.all();
			} else if(page.getJson() != null) {
				urlList = page.getJson().jsoup(multiPageProcessorParam.getTargetUrlJsoup(), 
						multiPageProcessorParam.getAttributeName(), multiPageProcessorParam.isMulti())
						.all();
			}
		}
		return urlList;
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
	 * @Description: 告诉PageProcessor,请求体的字符编码
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
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,列表页是否需要分页
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public boolean needPaging() {
		return true;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: judgeCurrentPage
	 * @Description: 判定当前页是第几页[页码从1开始计算]
	 * @param @param page
	 * @param pageSize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int judgeCurrentPage(Page page,int pageSize) {
		return 1;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: isLastPage
	 * @Description: 是否最后一页[只有当分页总页数无法得知时才需要重写此方法]
	 * @param page      当前页对象
	 * @param pageUrl   分页URL
	 * @param params    post提交参数
	 * @param @return 
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean isLastPage(Page page,String pageUrl,Map<String ,String> params) {
		return false;
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
	
	/************************针对分页请求每一页beging*******************************/
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestHeaderPaging
	 * @Description: 设置分页请求头信息
	 * @return Map<String,String>
	 * @throws
	 */
	protected abstract Map<String,String> setRequestHeaderPaging();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestParamsPaging
	 * @Description: 设置分页请求参数[POST请求可能会需要设置]
	 * @return Map<String,String>
	 * @throws
	 */
	protected abstract Map<String,String> setRequestParamsPaging();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: requestBodyEncodingPaging
	 * @Description: 告诉PageProcessor,分页请求体的字符编码
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String requestBodyEncodingPaging() {
		return HttpConstant.Charset.UTF_8;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: ajaxPaging
	 * @Description: 告诉PageProcessor,http 分页请求是否为ajax请求
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	protected boolean ajaxPaging() {
		return false;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: methodPaging
	 * @Description: 告诉PageProcessor,http分页请求的method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	protected String methodPaging() {
		return HttpConstant.Method.GET;
	}
	/************************针对分页请求每一页end*******************************/
}
