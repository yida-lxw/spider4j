package com.yida.spider4j.crawler.utils.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 */
public class GsonUtils {
    private static final Logger log = LoggerFactory.getLogger(GsonUtils.class);

    public final TypeToken<Map<String, Object>> mapTypeToken = new TypeToken<Map<String, Object>>(){};

    private Gson gson;

    private GsonUtils() {
        if (gson == null) {
            gson= new GsonBuilder()
                    //当字段值为空或null时，依然对该字段进行转换
                    //.serializeNulls()
                    //防止特殊字符出现乱码
                    .disableHtmlEscaping()
                    //对结果进行换行
                    .setPrettyPrinting()
                    //日期时间转化为指定日期格式
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    //当Map的key为复杂对象时,需要开启该方法
                    .enableComplexMapKeySerialization()
                    .serializeSpecialFloatingPointValues()
                    .create();
        }
    }

    private static class SingletonInstance {
        private static final GsonUtils INSTANCE = new GsonUtils();
    }

    public static GsonUtils getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 将对象转成json格式
     * @param object
     * @return String
     */
    public String beanToString(Object object) {
        String gsonString = null;
        try {
            if (gson != null) {
                gsonString = gson.toJson(object);
            }
        } catch (Exception e) {
            log.error("bean to json string occur exception",e);
        }
        return gsonString;
    }

    /**
     * 将json转成特定的cls的对象
     * @param gsonString
     * @param cls
     * @return
     */
    public <T> T stringToBean(String gsonString, Class<T> cls) {
        T t = null;
        try {
            if (gson != null) {
                t = gson.fromJson(gsonString, cls);
            }
        } catch (JsonSyntaxException e) {
            log.error("invalid json string:[{}]\n,class-name:{}", gsonString, cls.getName(), e);
        }
        return t;
    }

    /**
     * 将JSON字符串转成Java Map
     * @param jsonStr
     * @return
     */
    public <K, V> Map<K, V> jsonStr2Map(String jsonStr, TypeToken<Map<K, V>> typeToken) {
        Map<K, V> resultMap = null;
        try {
            resultMap = gson.fromJson(jsonStr, typeToken);
        } catch (JsonSyntaxException e) {
            log.error("convert json string to Map occur exception,invalid json string:[{}]\n", jsonStr, e);
        }
        return resultMap;
    }

    /**
     * 将JSON字符串转成Java Map
     * @param jsonStr
     * @return
     */
    public Map<String, Object> jsonStr2Map(String jsonStr) {
        Map<String, Object> resultMap = null;
        try {
            resultMap = gson.fromJson(jsonStr, mapTypeToken);
        } catch (JsonSyntaxException e) {
            log.error("convert json string to Map occur exception,invalid json string:[{}]\n", jsonStr, e);
        }
        return resultMap;
    }

    /**
     * json字符串转成list
     * 解决泛型问题
     * 备注：
     * List<T> list=gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
     * 该方法会报泛型类型擦除问题
     * @param gsonString
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> stringToList(String gsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            if(gson!=null){
                JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
                for(final JsonElement elem : array){
                    list.add(gson.fromJson(elem, cls));
                }
            }
        } catch (JsonSyntaxException e) {
            log.error("invalid json string:[{}]\n,class-name:{}", gsonString, cls.getName(), e);
        }
        return list;
    }
}
