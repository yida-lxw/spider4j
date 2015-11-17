package com.yida.spider4j.crawler.utils.collection.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: Beta
 * @Description: 测试版注解,用于提醒用户此接口API为测试非稳定版
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 上午9:46:44
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.TYPE})
@Documented
@Beta
public @interface Beta {}
