package com.yida.spider4j.crawler.wangyi.pageprocessor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.utils.common.DateUtils;
import com.yida.spider4j.crawler.utils.common.FastJSonUtils;
import com.yida.spider4j.crawler.utils.jdbc.AbstractDAO;
import com.yida.spider4j.crawler.wangyi.bean.WangyiDetailPage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @ClassName: DoubanStartPageProcessor
 * @Description: 豆瓣电影Top250起始页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class WangyiStartPageProcessor extends SimpleStartPageProcessor {
	private static final Logger logger = Logger.getLogger(WangyiStartPageProcessor.class.getName());

	public WangyiStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
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

	@Override
	public List<String> pickUpPagingUrl(Page page) {
		try {
			String htmlBody = page.getHtml().get();
			htmlBody = htmlBody.substring(htmlBody.indexOf("["));
			htmlBody = htmlBody.substring(0, htmlBody.lastIndexOf("]") + 1);
			htmlBody = htmlBody
					.replace("a href=\"", "a href=\\\"")
					.replace("\" class=\"", "\\\" class=\\\"")
					.replace("\">", "\\\">");
			logger.info("JSON数据：\n" + htmlBody);
			List<Map<String,Object>> dataList = FastJSonUtils.toListMap(htmlBody);
			if(null != dataList && dataList.size() > 0) {
				List<String> detailPageUrlList = new ArrayList<>(dataList.size());
				List<WangyiDetailPage> detailPageList = new ArrayList<>(dataList.size());
				for(Map<String,Object> itemMap : dataList) {
					String pageUrl = itemMap.get("docurl").toString();
					detailPageUrlList.add(pageUrl);
					String title = itemMap.get("title").toString();
					String media = itemMap.get("source").toString();
					String channel = itemMap.get("channelname").toString();
					Date publishDate = DateUtils.stringToDate(itemMap.get("time").toString(),
							"MM/dd/yyyy HH:mm:ss");
					String pageId = pageUrl.replace(".html", "");
					pageId = pageId.substring(pageId.lastIndexOf("/") + 1);
					WangyiDetailPage wangyiDetailPage = new WangyiDetailPage();
					wangyiDetailPage.setPageId(pageId);
					wangyiDetailPage.setChannel(channel);
					wangyiDetailPage.setPageUrl(pageUrl);
					wangyiDetailPage.setTitle(title);
					wangyiDetailPage.setMedia(media);
					wangyiDetailPage.setCommentCount(0);
					wangyiDetailPage.setPublishDate(publishDate);
					detailPageList.add(wangyiDetailPage);
				}
				if(null != detailPageList && detailPageList.size() > 0) {
					//Map<String, Object> dataMap = SQLBuilder.buildBatchInsertSQL(detailPageList, true, false);
					//String insertSQL = dataMap.get(SQLBuilder.SQL_KEY).toString();
					//Object[][] params = (Object[][])dataMap.get(SQLBuilder.PARAMS_KEY);
					//boolean success = JDBCUtils.insertBatch(insertSQL, params, true);
					boolean success = new AbstractDAO<Long, WangyiDetailPage>(){}.insertOrUpdateBatch(detailPageList);
					System.out.println("批量插入" + ((success)? "成功" : "失败"));
				}
				return detailPageUrlList;
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return new ArrayList<>();
	}
}
