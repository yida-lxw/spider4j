package com.yida.spider4j.crawler.processor;

import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: StartPageProcessor
 * @Description: 起始页PageProcessor实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:42:47
 *
 */
public abstract class StartPageProcessor extends AbstractMultiPageProcessor {
	public StartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
}
