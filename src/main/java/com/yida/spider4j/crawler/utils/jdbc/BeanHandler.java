package com.yida.spider4j.crawler.utils.jdbc;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 13:47
 * @description Type your description over here.
 */
public class BeanHandler<T> extends AbstractBeanHandler<T> {

    public BeanHandler(Class<T> type) {
        super(type);
    }
}
