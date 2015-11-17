package com.yida.spider4j.crawler.selector.jsonpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.yida.spider4j.crawler.utils.common.FastJSonUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: JsonPathUtils
 * @Description: JsonPath操作工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午2:37:59
 * 
 */
@SuppressWarnings("unchecked")
public class JsonPathUtils {
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private static Configuration conf;
	static {
		conf = Configuration.defaultConfiguration();
		conf = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectMap
	 * @Description: 根据JsonPath表达式返回一个Map
	 * @param @param json
	 * @param @param jsonPath
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public static Map<String, Object> selectMap(String json, String jsonPath) {
		Object obj = null;
		try {
			obj = JsonPath.using(conf).parse(json).read(jsonPath);
		} catch (Exception e) {
			return null;
		}
		if (obj == null) {
			return null;
		}
		if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			if (jsonArray.size() <= 0) {
				return null;
			}
			return (Map<String, Object>) jsonArray.get(0);
		}
		return null;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectMultiMap
	 * @Description: 根据JsonPath表达式返回一个Map集合
	 * @param @param json
	 * @param @param jsonPath
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public static List<Map<String, Object>> selectMultiMap(String json,String jsonPath) {
		Object obj = null;
		try {
			obj = JsonPath.using(conf).parse(json).read(jsonPath);
		} catch (Exception e) {
			return null;
		}
		if (obj == null) {
			return null;
		}
		if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			if (jsonArray.size() <= 0) {
				return null;
			}
			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
			for (Object object : jsonArray) {
				maps.add((Map<String, Object>) object);
			}
			return maps;
		}
		return null;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectList
	 * @Description: 根据JsonPath表达式返回一个字符串集合
	 * @param @param json
	 * @param @param jsonPath
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> selectList(String json, String jsonPath) {
		List<Object> list = null;
		try {
			list = JsonPath.using(conf).parse(json).read(jsonPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null == list || list.size() <= 0) {
			return null;
		}
		List<String> strList = new ArrayList<String>();
		for (Object obj : list) {
			if (null == obj) {
				continue;
			}
			if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				if (jsonArray.size() <= 0) {
					return null;
				}
				strList.add(jsonArray.toString());
			} else if (obj instanceof LinkedHashMap) {
				strList.add(FastJSonUtils.toJSon(obj, null,
						DEFAULT_DATE_FORMAT, true, false, true));
			} else {
				strList.add(obj.toString());
			}
		}
		return strList;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectOne
	 * @Description: 根据JsonPath表达式返回匹配到的第一个字符串
	 * @param @param json
	 * @param @param jsonPath
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String selectOne(String json, String jsonPath) {
		String str = null;
		try {
			Object object = JsonPath.using(conf).parse(json).read(jsonPath);
			if(object == null) {
				return null;
			}
			str = object.toString();
			if("[]".equals(str)) {
				return null;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return str;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: convertMap
	 * @Description: 因为JsonPath返回的Map的value可能是JSONArray类型,
	 *               为了操作简便,所以这里把JSOnArray类型使用分隔符拼接成一个字符串
	 * @param @param sourceMap
	 * @param @param seprator
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 */
	public static Map<String, String> convertMap(Map<String, Object> sourceMap,
			String seprator) {
		if (null == sourceMap || sourceMap.isEmpty()) {
			return null;
		}
		if (null == seprator || "".equals(seprator)) {
			seprator = "#$&#";
		}
		StringBuilder builder = null;
		Map<String, String> destMap = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (null == val) {
				continue;
			}
			if (val instanceof JSONArray) {
				JSONArray array = (JSONArray) val;
				builder = new StringBuilder();
				for (Object obj : array) {
					if (obj == null) {
						continue;
					}
					builder.append(obj.toString()).append(seprator);
				}
				String valStr = builder.toString().replaceAll(
						seprator.replace("$", "\\$") + "$", "");
				destMap.put(key, valStr);
			} else {
				destMap.put(key, val.toString());
			}
		}
		return destMap;
	}
}
