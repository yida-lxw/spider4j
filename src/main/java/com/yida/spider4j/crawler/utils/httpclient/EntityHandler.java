package com.yida.spider4j.crawler.utils.httpclient;

import java.io.IOException;

import org.apache.http.HttpEntity;

/**
 * Response响应体处理接口
 * @author Lanxiaowei
 *
 */
public interface EntityHandler<T> {
    public T handleEntity(HttpEntity entity) throws IOException;
}
