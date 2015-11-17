package com.yida.spider4j.crawler.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;

public class QueueScheduler extends DuplicateFilterScheduler implements MonitorableScheduler {
	private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    @Override
    public void pushWhenNoDuplicate(Request request, Task task) {
        queue.add(request);
    }

    @Override
    public synchronized Request poll(Task task) {
        return queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateFilter().getTotalRequestsCount(task);
    }
}
