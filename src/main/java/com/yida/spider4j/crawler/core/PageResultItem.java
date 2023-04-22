package com.yida.spider4j.crawler.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PageResultItem
 * @Description: 从爬取到的页面提取出来的数据集
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 上午9:33:21
 *
 */
@SuppressWarnings("unchecked")
public class PageResultItem {
	/**提取到的数据集*/
	private Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
	
	/**提取到的数据集[可能有多条]*/
	private List<LinkedHashMap<String, Object>> dataMapList = new ArrayList<LinkedHashMap<String, Object>>();

	/**抓取请求对象*/
    private Request request;
    
    /**是否跳过不处理*/
    private boolean skip;

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: get
     * @Description: 根据key从数据集中提取数据
     * @param @param key
     * @param @return
     * @return T
     * @throws
     */
	public <T> T get(String key) {
        Object o = dataMap.get(key);
        if (o == null) {
            return null;
        }
        return (T) dataMap.get(key);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getAll
     * @Description: 返回数据集Map
     * @param @return
     * @return Map<String,Object>
     * @throws
     */
    public Map<String, Object> getAll() {
        return dataMap;
    }

    public Map<String, Object> getDataMap() {
		return dataMap;
	}

	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: put
     * @Description: 往数据集中put数据
     * @param @param key
     * @param @param value
     * @param @return
     * @return PageResultItem
     * @throws
     */
    public <T> PageResultItem put(String key, T value) {
        dataMap.put(key, value);
        return this;
    }
    
    public <T> PageResultItem putMap(LinkedHashMap<String, Object> map) {
        dataMapList.add(map);
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public PageResultItem setRequest(Request request) {
        this.request = request;
        return this;
    }

    public boolean isSkip() {
        return skip;
    }

    public PageResultItem setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }

    public List<LinkedHashMap<String, Object>> getDataMapList() {
		return dataMapList;
	}

	public PageResultItem setDataMapList(List<LinkedHashMap<String, Object>> dataMapList) {
		this.dataMapList = dataMapList;
		return this;
	}

	public PageResultItem setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
		return this;
	}

	@Override
    public String toString() {
        return "PageResultItem{" +
                "fields=" + dataMap +
                ", request=" + request +
                ", skip=" + skip +
                '}';
    }
}
