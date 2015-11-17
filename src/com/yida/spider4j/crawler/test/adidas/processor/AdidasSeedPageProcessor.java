package com.yida.spider4j.crawler.test.adidas.processor;

import com.yida.spider4j.crawler.processor.SimpleSeedPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: AdidasSeedPageProcessor
 * @Description: 阿迪达斯种子页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class AdidasSeedPageProcessor extends SimpleSeedPageProcessor {
	public AdidasSeedPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,是否需要分页
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean needPaging() {
		return false;
	}
}
