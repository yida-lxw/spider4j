package com.yida.spider4j.crawler.test.wangyi.pageprocessor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleAttachedPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.common.GsonUtils;
import com.yida.spider4j.crawler.utils.jdbc.JDBCUtils;

import java.util.Map;

/**
 * @ClassName: DoubanDetailPageProcessor
 * @Description: 网易新闻详情页附属页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class WangyiAttachedPageProcessor extends SimpleAttachedPageProcessor {

	public WangyiAttachedPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	@Override
	public void process(Page page) {
		super.process(page);
		String jsonString = page.getJson().get();
		Map<String, Object> dataMap = GsonUtils.getInstance().jsonStr2Map(jsonString);
		int commentCount = Integer.valueOf(dataMap.get("cmtCount").toString().replace(".0", ""));
		String pageId = dataMap.get("docId").toString();
		page.putField("commentCount", commentCount);
		page.putField("pageId", pageId);
		String updateSQL = "update news set commentCount=? where pageId=?";
		Object[] params = new Object[] {commentCount, pageId};
		JDBCUtils.update(updateSQL, params, true);
	}
}
