package com.yida.spider4j.crawler.download;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.core.Site;
import com.yida.spider4j.crawler.selector.Html;
/**
 * @ClassName: AbstractDownloader
 * @Description: Downloader的默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 上午11:14:15
 *
 */
public abstract class AbstractDownloader extends DefaultConfigurable implements
		Downloader {
	public Html download(String url) {
		return download(url, this.config.getPageDefaultEncoding());
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: download
	 * @Description: 根据URL链接下载网页
	 * @param @param url 待抓取的网页链接
	 * @param @param charset 指定网页编码
	 * @param @return
	 * @return Html
	 * @throws
	 */
	public Html download(String url, String charset) {
		Page page = download(new Request(url), Site.me().setCharset(charset)
				.toTask());
		return (Html) page.getHtml();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addToCycleRetry
	 * @Description: 下载失败的网页需要重新添加到URL调度器里
	 * @param @param request
	 * @param @param site
	 * @param @return
	 * @return Page
	 * @throws
	 */
	protected Page addToCycleRetry(Request request, Site site) {
        Page page = new Page();
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        //判断用户是否设置网页下载失败重试次数
        if (cycleTriedTimesObject == null) {
            page.addTargetRequest(request.setPriority(0)
            	.putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = Integer.valueOf(cycleTriedTimesObject.toString());
            cycleTriedTimes++;
            //如果重试次数超过限制,则停止重试
            if (cycleTriedTimes >= site.getCycleRetryTimes()) {
                return null;
            }
            page.addTargetRequest(request.setPriority(0).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
        }
        page.setNeedCycleRetry(true);
        return page;
    }

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: onSuccess
	 * @Description: 抓取成功的回调函数
	 * @param @param request
	 * @return void
	 * @throws
	 */
	protected void onSuccess(Request request) {
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: onError
	 * @Description: 抓取失败的回调函数
	 * @param @param request
	 * @return void
	 * @throws
	 */
	protected void onError(Request request) {
	}
}
