package com.yida.spider4j.crawler.pipeline.model;

import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.ReflectionUtils;

/**
 * @ClassName: ConsolePageModelPipeline
 * @Description: 打印网页的数据模型到控制台的管道实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午3:13:51
 *
 */
@SuppressWarnings("rawtypes")
public class ConsolePageModelPipeline implements PageModelPipeline {
    @Override
    public void process(Object o, Task task) {
        if(null == o) {
        	return;
        }
        ReflectionUtils.printClassInfo(o);
    }
}
