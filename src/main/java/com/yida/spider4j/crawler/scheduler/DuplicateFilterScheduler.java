package com.yida.spider4j.crawler.scheduler;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.scheduler.filter.DuplicateFilter;
import com.yida.spider4j.crawler.scheduler.filter.HashSetDuplicateFilter;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: DuplicateFilterScheduler
 * @Description: 带URL去重功能的调度器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 下午6:38:44
 *
 */
public abstract class DuplicateFilterScheduler extends DefaultConfigurable implements Scheduler {

    private DuplicateFilter duplicateFilter = new HashSetDuplicateFilter();

    public DuplicateFilter getDuplicateFilter() {
        return duplicateFilter;
    }

    public DuplicateFilterScheduler setDuplicateFilter(DuplicateFilter duplicateFilter) {
        this.duplicateFilter = duplicateFilter;
        return this;
    }

    @Override
    public void push(Request request, Task task) {
        LogUtils.info("get a candidate url {" + request.getUrl() + "}");
        if (!duplicateFilter.isDuplicate(request, task) || shouldReserved(request)) {
        	LogUtils.debug("push to queue {" + request.getUrl() + "}");
            pushWhenNoDuplicate(request, task);
        }
    }

    protected boolean shouldReserved(Request request) {
        return request.getExtra(Request.CYCLE_TRIED_TIMES) != null;
    }

    protected void pushWhenNoDuplicate(Request request, Task task) {

    }
}
