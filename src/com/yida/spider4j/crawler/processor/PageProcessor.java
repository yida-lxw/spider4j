package com.yida.spider4j.crawler.processor;

import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.core.Site;

/**
 * @ClassName: PageProcessor
 * @Description: 网页处理器[比如提取URL,提取数据]
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:18:05
 *
 */
public interface PageProcessor {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestHeader
	 * @Description: 设置请求头信息
	 * @return Map<String,String>
	 * @throws
	 */
	public Map<String,String> setRequestHeader();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: setRequestParams
	 * @Description: 设置请求参数[POST请求才可能需要重写设置]
	 * @return Map<String,String>
	 * @throws
	 */
	public Map<String,String> setRequestParams();

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: requestBodyEncoding
	 * @Description: 告诉PageProcessor请求体的字符编码
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String requestBodyEncoding();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: ajax
	 * @Description: 告诉PageProcessor,http请求是否为ajax请求
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean ajax();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: method
	 * @Description: 告诉PageProcessor,http请求的method值:GET or POST or other else
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String method();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getSite
	 * @Description: 获取网站设置
	 * @param @return
	 * @return Site
	 * @throws
	 */
	public Site getSite();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: startUrl
	 * @Description: 告诉PageProcessor,起始页URL是什么
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String startUrl();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: startUrls
	 * @Description: 告诉PageProcessor,起始页URL是什么
	 *               [应对起始URL可能有多个的情况]
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> startUrls();
}
