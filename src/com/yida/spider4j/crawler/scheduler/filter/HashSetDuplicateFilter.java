package com.yida.spider4j.crawler.scheduler.filter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.collection.Sets;

/**
 * @ClassName: HashSetDuplicateFilter
 * @Description: 基于Hashset实现的URL重复过滤器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 下午4:08:31
 *
 */
public class HashSetDuplicateFilter implements DuplicateFilter {
	private Set<Request> urls = Sets.newSetFromMap(new ConcurrentHashMap<Request, Boolean>());
	
	@Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(request);
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}
