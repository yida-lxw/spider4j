package com.yida.spider4j.crawler.pipeline.model;

import java.util.ArrayList;
import java.util.List;

import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: CollectorPageModelPipeline
 * @Description: 用于收集网页数据模型的管道[基于面向对象]
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午3:17:23
 *
 */
public class CollectorPageModelPipeline<T> implements PageModelPipeline<T> {

    private List<T> collected = new ArrayList<T>();

    @Override
    public synchronized void process(T t, Task task) {
        collected.add(t);
    }

    public List<T> getCollected() {
        return collected;
    }
}
