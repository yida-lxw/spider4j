package com.yida.spider4j.crawler.utils.common;
/**
 * @ClassName: Validate
 * @Description: 数据验证工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 上午10:30:51
 *
 */
public class Validate {
	private Validate() {}

    /**
     * Validates that the obect is not null
     * @param obj object to test
     */
    public static void notNull(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("Object must not be null");
    }

    /**
     * Validates that the object is not null
     * @param obj object to test
     * @param msg message to output if validation fails
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null)
            throw new IllegalArgumentException(msg);
    }

    /**
     * Validates that the value is true
     * @param val object to test
     */
    public static void isTrue(boolean val) {
        if (!val)
            throw new IllegalArgumentException("Must be true");
    }

    /**
     * Validates that the value is true
     * @param val object to test
     * @param msg message to output if validation fails
     */
    public static void isTrue(boolean val, String msg) {
        if (!val)
            throw new IllegalArgumentException(msg);
    }

    /**
     * Validates that the array contains no null elements
     * @param objects the array to test
     */
    public static void noNullElements(Object[] objects) {
        noNullElements(objects, "Array must not contain any null objects");
    }

    /**
     * Validates that the array contains no null elements
     * @param objects the array to test
     * @param msg message to output if validation fails
     */
    public static void noNullElements(Object[] objects, String msg) {
        for (Object obj : objects)
            if (obj == null)
                throw new IllegalArgumentException(msg);
    }

    /**
     * Validates that the string is not empty
     * @param string the string to test
     */
    public static void notEmpty(String string) {
        if (string == null || string.length() == 0)
            throw new IllegalArgumentException("String must not be empty");
    }

    /**
     * Validates that the string is not empty
     * @param string the string to test
     * @param msg message to output if validation fails
     */
    public static void notEmpty(String string, String msg) {
        if (string == null || string.length() == 0)
            throw new IllegalArgumentException(msg);
    }
}
