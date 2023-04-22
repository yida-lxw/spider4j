package com.yida.spider4j.crawler.utils.map;

/**
 * @author code4crafter@gmail.com
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MultiKeyMapBase
 * @Description: 多Key-Map结构
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 下午1:10:07
 *
 */

@SuppressWarnings({"rawtypes"})
public abstract class MultiKeyMapBase {

    protected static final Class<? extends Map> DEFAULT_CLAZZ = HashMap.class;

    private Class<? extends Map> protoMapClass = DEFAULT_CLAZZ;

    public MultiKeyMapBase() {
    }

    public MultiKeyMapBase(Class<? extends Map> protoMapClass) {
        this.protoMapClass = protoMapClass;
    }

    @SuppressWarnings("unchecked")
    protected <K, V2> Map<K, V2> newMap() {
        try {
            return (Map<K, V2>) protoMapClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("wrong proto type map "
                    + protoMapClass);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("wrong proto type map "
                    + protoMapClass);
        }
    }
}