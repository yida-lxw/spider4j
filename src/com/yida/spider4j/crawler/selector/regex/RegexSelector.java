package com.yida.spider4j.crawler.selector.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.yida.spider4j.crawler.selector.PlainTextSelector;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: RegexSelector
 * @Description: 正则表达式选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午2:50:27
 *
 */
public class RegexSelector implements PlainTextSelector {
	/**正则表达式*/
    private String regexStr;
    
    private Pattern regex;

    /**组数,从零开始计算(group[0]表示源串)*/
    private int group;

    public RegexSelector(String regexStr, int group) {
        if (StringUtils.isEmpty(regexStr)) {
            throw new IllegalArgumentException("regex must not be empty");
        }
        // 判断正则表达式是否有()分组,若没有则两头添加(),保证始终有一个分组,否则group(1)会抛异常
        if (StringUtils.countMatches(regexStr, "(") - StringUtils.countMatches(regexStr, "\\(") ==
                StringUtils.countMatches(regexStr, "(?:") - StringUtils.countMatches(regexStr, "\\(?:")) {
            regexStr = "(" + regexStr + ")";
        }
        this.regexStr = regexStr;
        try {
            regex = Pattern.compile(regexStr, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("invalid regex", e);
        }
        this.group = group;
    }

    public RegexSelector(String regexStr) {
        this(regexStr, 1);
    }

    @Override
    public String select(String text) {
        return selectGroup(text).get(group);
    }

    @Override
    public List<String> selectList(String text) {
        List<String> strings = new ArrayList<String>();
        List<RegexResult> results = selectGroupList(text);
        for (RegexResult result : results) {
            strings.add(result.get(group));
        }
        return strings;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectGroup
     * @Description: 单组匹配(始终只匹配第一组即group[1])
     * @param @param text
     * @param @return
     * @return RegexResult
     * @throws
     */
    public RegexResult selectGroup(String text) {
        Matcher matcher = regex.matcher(text);
        if (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            return new RegexResult(groups);
        }
        return RegexResult.EMPTY_RESULT;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectGroupList
     * @Description: 多组匹配
     * @param @param text
     * @param @return
     * @return List<RegexResult>
     * @throws
     */
    public List<RegexResult> selectGroupList(String text) {
        Matcher matcher = regex.matcher(text);
        List<RegexResult> resultList = new ArrayList<RegexResult>();
        while (matcher.find()) {
        	//group[0]为源串,所以groupCount + 1
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            resultList.add(new RegexResult(groups));
        }
        return resultList;
    }

    @Override
    public String toString() {
        return regexStr;
    }
}
