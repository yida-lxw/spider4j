package com.yida.spider4j.crawler.utils.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-26 08:38
 * @description Type your description over here.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value() default "";
}
