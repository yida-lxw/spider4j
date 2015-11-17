package com.yida.spider4j.crawler.test.howbuy.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: ManagerStartPageProcessor
 * @Description: 好买网私募经理起始页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class ManagerStartPageProcessor extends SimpleStartPageProcessor {
	public ManagerStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determineTotalPage
	 * @Description: 探测总页数
	 * @param page
	 * @param pagesize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int determineTotalPage(Page page,int pageSize) {
		if(page == null) {
			return 0;
		}
		String text = page.getHtml().jsoup("div[class=pages]").get();
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		Pattern p = Pattern.compile(".*共(\\d+)页.*");
		Matcher m = p.matcher(text); 
		if(m.find()) { 
			text = m.group(1).trim(); 
		}
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		try {
			return Integer.valueOf(text);
		} catch (NumberFormatException e) {
			return 0;
		}
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
		return true;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: nextPagePost
	 * @Description: 构建下一页请求参数[适用于POST请求]
	 * @param @param page
	 * @param @param totalPage
	 * @param @param pageSize
	 * @param @param currentPage
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	@Override
	public Map<String, String> nextPagePost(Page page, int totalPage,
			int pageSize, int currentPage) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderType", "Desc");
		params.put("sortField", "hb1nscclzyjj");
		params.put("ejflsccl", "");
		params.put("jjjlly", "");
		params.put("skey", "");
		params.put("page", currentPage + "");
		params.put("perPage", pageSize + "");
		params.put("allPage", totalPage + "");
		params.put("targetPage", "");
		return params;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,当前页是第几页
	 * @param page
	 * @param pageSize  每页显示大小
	 * @return int  返回当前页的页码,页码从1开始计算
	 * @throws
	 */
	@Override
	public int judgeCurrentPage(Page page,int pageSize) {
		if(page.getParams() == null || page.getParams().isEmpty()) {
			return 1;
		}
		String val = page.getParams().get("page");
		if(StringUtils.isEmpty(val)) {
			return 1;
		}
		try {
			return Integer.valueOf(val);
		} catch (NumberFormatException e) {
			return 1;
		}
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
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,分页请求的method值,get or post or other else
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	protected String methodPaging() {
		return HttpConstant.Method.POST;
	}
}
