package com.yida.spider4j.crawler.pipeline.model;

import java.util.Collection;

/**
 * @ClassName: MultiPageModel
 * @Description: 多页数据模型[一篇新闻网页内容可能分多页组成,而每页都是一个网页]
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午3:55:02
 *
 */
public interface MultiPageModel {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getPageKey
	 * @Description: 返回网页的唯一标识
	 * @param @return
	 * @return String
	 * @throws
	 */
    public String getPageKey();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getPage
     * @Description: 获取当前网页
     * @param @return
     * @return String
     * @throws
     */
    public String getPage();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getOtherPages
     * @Description: 获取剩下的其他网页
     * @param @return
     * @return Collection<String>
     * @throws
     */
    public Collection<String> getOtherPages();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: combine
     * @Description: 合并多个网页
     * @param @param multiPageModel
     * @param @return
     * @return MultiPageModel
     * @throws
     */
    public MultiPageModel combine(MultiPageModel multiPageModel);
}
