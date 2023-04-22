package com.yida.spider4j.crawler.pipeline.model;

import com.yida.spider4j.crawler.task.Task;
/**
 * @ClassName: PageModelPipeline
 * @Description: 面向对象的数据模型管道即PageProcessor返回的不再是Map,
 *               而是Java Bean对象,这对于你在使用面向对象的ORM框架时会很有用,
 *               比如你想直接save(javabean)就能将一个对象插入数据库,比如hibernate
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午2:43:14
 *
 */
public interface PageModelPipeline<T> {
	public void process(T t, Task task);
}
