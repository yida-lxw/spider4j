package com.yida.spider4j.crawler.selector.regex;

/**
 * @ClassName: RegexResult
 * @Description: 正则匹配结果包装对象
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午2:46:38
 *
 */
class RegexResult {
	/**匹配到的每组结果集*/
    private String[] groups;
    /**空的结果集*/
    public static final RegexResult EMPTY_RESULT = new RegexResult();

    public RegexResult() {}

    public RegexResult(String[] groups) {
        this.groups = groups;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: get
     * @Description: 获取指定组的匹配结果
     * @param @param groupId
     * @param @return
     * @return String
     * @throws
     */
    public String get(int groupId) {
        if (groups == null) {
            return null;
        }
        return groups[groupId];
    }
}
