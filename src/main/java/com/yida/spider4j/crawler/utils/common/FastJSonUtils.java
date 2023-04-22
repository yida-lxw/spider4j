/*
 * Copyright (c) 2015, Xinlong.inc and/or its affiliates. All rights reserved.
 */
package com.yida.spider4j.crawler.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Alibaba FastJSon操作工具类
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-6-29上午10:11:02
 * 
 */
public class FastJSonUtils {
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";

	/**
	 * @Author Lanxiaowei
	 * @Title: toJSon
	 * @Description: JSON字符串转换成Java对象
	 * @param jsonString
	 * @param cls
	 * @return
	 * @return T
	 * @throws
	 */
	public static <T> T toObject(String jsonString, Class<T> cls) {
		return JSON.parseObject(jsonString, cls);
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: toObjectList
	 * @Description: JSON字符串转换成Java对象集合
	 * @param jsonString
	 * @param cls
	 * @return
	 * @return List<T>
	 * @throws
	 */
	public static <T> List<T> toObjectList(String jsonString, Class<T> cls) {
		return JSON.parseArray(jsonString, cls);
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: toListMap
	 * @Description: JSon字符串转换成List<Map<String, Object>>类型
	 * @param jsonString
	 * @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public static List<Map<String, Object>> toListMap(String jsonString) {
		return JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
		});
	}
	
	public static Map<String, Object> toMap(String jsonString) {
		return JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: toJSon
	 * @Description: Java对象转换成JSon字符串
	 * @param object             待转换的Java对象
	 * @param returnFieldArray   需要返回的属性数组
	 * @param dateFormat         日期转换格式
	 * @param prettyFormat       是否需要格式化JSon
	 * @param useSingleQuote     是否使用单引号
	 * @param skipTransient      是否忽略被@Transient注解修饰的属性
	 * @return
	 * @return String
	 * @throws
	 */
	public static String toJSon(Object object,String[] returnFieldArray,String dateFormat,
		boolean prettyFormat,boolean useSingleQuote,boolean skipTransient) {
		SerializeFilter[] filters = null;
		if(null != returnFieldArray && returnFieldArray.length > 0) {
			SerializeFilter filter = new SimplePropertyPreFilter(returnFieldArray);
			filters = new SerializeFilter[1];
			filters[0] = filter;
		}
		List<SerializerFeature> features = new ArrayList<SerializerFeature>();
		//是否格式化JSon
		if(prettyFormat) {
			features.add(SerializerFeature.PrettyFormat);
		}
		//是否使用单引号
		if(useSingleQuote) {
			features.add(SerializerFeature.UseSingleQuotes);
		}
		//是否忽略被@Transient注解修饰的属性
		if(skipTransient) {
			features.add(SerializerFeature.SkipTransientField);
		}
		return toJSONStringWithDateFormat(object, dateFormat, filters, features.toArray(new SerializerFeature[] {}));
	}
	
	/**
	 * @Author Lanxiaowei
	 * @Title: toJSONStringWithDateFormat
	 * @Description: Java对象转换成JSon字符串
	 * @param object
	 * @param dateFormat
	 * @param filters
	 * @param features
	 * @return
	 * @return String
	 * @throws
	 */
	public static final String toJSONStringWithDateFormat(Object object, String dateFormat,
		SerializeFilter[] filters,SerializerFeature[] features) {
		SerializeWriter out = new SerializeWriter();

		try {
			JSONSerializer serializer = new JSONSerializer(out);
			for (SerializerFeature feature : features) {
				serializer.config(feature, true);
			}

			serializer.config(SerializerFeature.WriteDateUseDateFormat, true);

			if (dateFormat != null && !"".equals(dateFormat)) {
				serializer.setDateFormat(dateFormat);
			} else {
				serializer.setDateFormat(DEFAULT_DATE_PATTERN);
			}
			setFilter(serializer, filters);
			serializer.write(object);

			return out.toString();
		} finally {
			out.close();
		}
	}
	
	/**
	 * @Author Lanxiaowei
	 * @Title: toJSONStringWithDateFormat
	 * @Description: Java对象转换成JSon字符串
	 * @param object
	 * @param filters
	 * @param features
	 * @return
	 * @return String
	 * @throws
	 */
	public static final String toJSONStringWithDateFormat(Object object,
			SerializeFilter[] filters,SerializerFeature[] features) {
		return toJSONStringWithDateFormat(object, null, filters, features);
	}
	
	/**
	 * @Author Lanxiaowei
	 * @Title: setFilter
	 * @Description: 为JSONSerializer设置SerializeFilter
	 * @param serializer
	 * @param filters
	 * @return void
	 * @throws
	 */
	private static void setFilter(JSONSerializer serializer, SerializeFilter[] filters) {
        if(null == filters || filters.length == 0) {
        	return;
        }
		for (SerializeFilter filter : filters) {
            setFilter(serializer, filter);
        }
    }
	
	/**
	 * @Author Lanxiaowei
	 * @Title: setFilter
	 * @Description: 为JSONSerializer设置SerializeFilter
	 * @param serializer
	 * @param filter
	 * @return void
	 * @throws
	 */
	private static void setFilter(JSONSerializer serializer, SerializeFilter filter) {
        if (filter == null) {
            return;
        }
        
        if (filter instanceof PropertyPreFilter) {
            serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
        }

        if (filter instanceof NameFilter) {
            serializer.getNameFilters().add((NameFilter) filter);
        }

        if (filter instanceof ValueFilter) {
            serializer.getValueFilters().add((ValueFilter) filter);
        }

        if (filter instanceof PropertyFilter) {
            serializer.getPropertyFilters().add((PropertyFilter) filter);
        }

        if (filter instanceof BeforeFilter) {
            serializer.getBeforeFilters().add((BeforeFilter) filter);
        }

        if (filter instanceof AfterFilter) {
            serializer.getAfterFilters().add((AfterFilter) filter);
        }
    }
	
	/*public static void main(String[] args) {
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("id", "1");
		map1.put("name", "john");
		map1.put("sex", "male");
		map1.put("age", "28");
		
		Map<String,Object> map2 = new HashMap<String, Object>();
		map2.put("id", "2");
		map2.put("name", "tom");
		map2.put("sex", "male");
		map2.put("age", "29");
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map1);
		list.add(map2);
		
		String json = toJSon(list, null, null, true, false, true);
		System.out.println(json);
	}*/
}
