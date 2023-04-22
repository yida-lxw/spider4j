package com.yida.spider4j.crawler.utils.httpclient;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Response响应对象to字节数组处理器
 * @author Lanxiaowei
 *
 */
public class ByteArrayEntityHandler implements EntityHandler<byte[]>{
	public ByteArrayEntityHandler(){}
	
    public byte[] handleEntity(HttpEntity entity) throws IOException {
    	if(!entity.isStreaming()) {
    		return null;
    	}
    	return EntityUtils.toByteArray(entity);
    }
}
