package com.yida.spider4j.crawler.test.douban.pageprocessor;

import com.yida.spider4j.crawler.processor.SimpleListPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: DoubanDetailPageProcessor
 * @Description: 豆瓣电影Top250列表页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class DoubanListPageProcessor extends SimpleListPageProcessor {
	public DoubanListPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
}
