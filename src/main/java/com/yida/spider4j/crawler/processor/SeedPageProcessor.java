package com.yida.spider4j.crawler.processor;

import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: SeedPageProcessor
 * @Description: 种子页PageProcessor实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午9:49:26
 *
 */
public abstract class SeedPageProcessor extends AbstractMultiPageProcessor {
	public SeedPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
}
