package com.yida.spider4j.crawler.processor;

import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.core.Page;

/**
 * @ClassName: StartPageProcessor
 * @Description: MultiPageProcessor实现(需要分页的情况)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:42:47
 *
 */
public interface MultiPageProcessor extends PageProcessor {
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildRequest
	 * @Description: 构造Request对象
	 * @param page
	 * @param @return
	 * @return void
	 * @throws
	 */
	public void buildRequest(Page page);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: pickUpTargetUrl
	 * @Description: 提取目标URL链接
	 * @param @param page
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> pickUpTargetUrl(Page page);
	
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
	public int determineTotalPage(Page page,int pageSize);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determinePageSize
	 * @Description: 探测每页显示大小
	 * @param @param page
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int determinePageSize(Page page);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: judgeCurrentPage
	 * @Description: 判定当前是第几页[页码从1开始计算]
	 * @param page
	 * @param pageSize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int judgeCurrentPage(Page page,int pageSize);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: isLastPage
	 * @Description: 是否最后一页
	 * @param page         当前页对象
	 * @param pageUrl      分页URL
	 * @param params       post提交参数
	 * @param @return 
	 * @return boolean
	 * @throws
	 */
	public boolean isLastPage(Page page,String pageUrl,Map<String ,String> params);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: knownTotalPage
	 * @Description: 告诉PageProcessor,从已有的分页页面内容可否已知总页数
	 *               需要用户实现
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean knownTotalPage();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,是否需要分页
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean needPaging();
	
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
	public String buildNextPageUrl(Page page,int currentPage,int totalPage,int pageSize);
	
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
	public Map<String,String> nextPagePost(Page page,int totalPage,int pageSize,int currentPage);
}
