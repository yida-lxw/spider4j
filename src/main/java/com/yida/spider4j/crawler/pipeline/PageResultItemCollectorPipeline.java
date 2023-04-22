package com.yida.spider4j.crawler.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;
/**
 * @ClassName: PageResultItemCollectorPipeline
 * @Description: PageResultItem对象收集管道
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午9:14:56
 *
 */
public class PageResultItemCollectorPipeline implements CollectorPipeline<PageResultItem>{

	private List<PageResultItem> collector = new ArrayList<PageResultItem>();

    @Override
    public synchronized void process(PageResultItem pageResultItem, Task task) {
        collector.add(pageResultItem);
    }

    @Override
    public List<PageResultItem> getCollected() {
        return collector;
    }
}
