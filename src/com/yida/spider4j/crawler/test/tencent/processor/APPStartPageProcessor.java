package com.yida.spider4j.crawler.test.tencent.processor;

import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;

/**
 * @ClassName: APPStartPageProcessor
 * @Description: 腾讯应用宝APP起始页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午4:32:09
 *
 */
public class APPStartPageProcessor extends SimpleStartPageProcessor {
	public APPStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
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
		return 20;
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
		return false;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,列表页是否需要分页
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean needPaging() {
		return true;
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
		int start = (currentPage - 1) * pageSize;
		return "http://android.myapp.com/myapp/cate/appList.htm?orgame=1&categoryId=0&pageSize=" + 
			pageSize + "&pageContext=" + start;
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
		buildPagingRequest(page, PageType.START_PAGE, PageType.DETAIL_PAGE);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: isLastPage
	 * @Description: 是否最后一页[只有当分页总页数无法得知时才需要重写此方法]
	 * @param page       当前页对象
	 * @param pageUrl    分页URL
	 * @param params     post提交参数
	 * @param @return 
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean isLastPage(Page page,String pageUrl,Map<String,String> params) {
		String json = null;
		try {
			json = HttpClientUtils.getHTML(pageUrl, null, params);
		} catch (Exception e) {
			return true;
		}
		if(StringUtils.isEmpty(json)) {
			return true;
		}
		//若存在"obj":[] 则表示没有返回数据,说明已经是最后一页了,不用继续分页请求咯
		return json.indexOf("\"obj\":[]") > 0;
	}
}
