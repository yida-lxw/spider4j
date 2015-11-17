package com.yida.spider4j.crawler.selector.regex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.yida.spider4j.crawler.selector.PlainTextSelector;

/**
 * @ClassName: ReplaceSelector
 * @Description: 正则替换选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月8日 下午5:17:05
 *
 */
public class ReplaceSelector implements PlainTextSelector {
	/**正则表达式*/
    private String regexStr;
    /**替换字符串*/
    private String replacement;
    
    private Pattern regex;

    public ReplaceSelector(String regexStr, String replacement) {
        this.regexStr = regexStr;
        this.replacement = replacement;
        try {
            regex = Pattern.compile(regexStr);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("invalid regex expression", e);
        }
    }

    @Override
    public String select(String text) {
        Matcher matcher = regex.matcher(text);
        return matcher.replaceAll(replacement);
    }

    @Override
    public List<String> selectList(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return regexStr + "_" + replacement;
    }

}
