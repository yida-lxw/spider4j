package com.yida.spider4j.crawler.utils.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: URLUtils
 * @Description: URL工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 下午3:30:30
 *
 */
public class URLUtils {
	private static Pattern patternForHrefWithQuote = Pattern.compile("(<a[^<>]*href\\s*=\\s*)[\"']([^\"'<>]*)[\"']", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    private static Pattern patternForHrefWithoutQuote = Pattern.compile("(<a[^<>]*href\\s*=\\s*)([^\"'<>\\s]+)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
    /**
     * canonicalizeUrl
     * <p/>
     * Borrowed from Jsoup.
     *
     * @param url
     * @param refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            
            if (url.startsWith("?")) {
                url = base.getPath() + url;
            }
            URL abs = new URL(base, url);
            return StringUtils.encodeURI(abs.toExternalForm());
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getHost
     * @Description: 获取主机,如http://www.google.com
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String getHost(String url) {
        String host = url;
        int i = StringUtils.ordinalIndexOf(url, "/", 3);
        if (i > 0) {
            host = StringUtils.substring(url, 0, i);
        }
        return host;
    }

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: removeProtocol
     * @Description: 去除URL中的协议头,如http://
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: removeWWW
     * @Description: 移除URL中的www.(这样做是为了便于获取网站的顶级域名,
     *               比如http://www.jd.com,其顶级域名为jd.com)
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String removeWWW(String url) {
        return url.replace("www.","");
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getDomain
     * @Description: 获取顶级域名
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        if(domain.startsWith("www.")) {
        	domain = removeWWW(domain);
        }
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        if(domain.indexOf(".") == domain.lastIndexOf(".")) {
        	return domain;
        }
        return StringUtils.substring(domain, domain.indexOf(".") + 1);
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getSubDomain
     * @Description: 提取子域名,如http://list
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String getSubDomain(String url) {
    	String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return domain;
	}
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: fixAllRelativeHrefs
     * @Description: 把URL相对路径改成绝对路径
     * @param @param html
     * @param @param url
     * @param @return
     * @return String
     * @throws
     */
    public static String fixAllRelativeHrefs(String html, String url) {
        html = replaceByPattern(html, url, patternForHrefWithQuote);
        html = replaceByPattern(html, url, patternForHrefWithoutQuote);
        return html;
    }

    public static String replaceByPattern(String html, String url, Pattern pattern) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = pattern.matcher(html);
        int lastEnd = 0;
        boolean modified = false;
        while (matcher.find()) {
            modified = true;
            stringBuilder.append(StringUtils.substring(html, lastEnd, matcher.start()));
            stringBuilder.append(matcher.group(1));
            stringBuilder.append("\"").append(canonicalizeUrl(matcher.group(2), url)).append("\"");
            lastEnd = matcher.end();
        }
        if (!modified) {
            return html;
        }
        stringBuilder.append(StringUtils.substring(html, lastEnd));
        return stringBuilder.toString();
    }
    
    public static void main(String[] args) {
    	String url = "http://list.jd.com/list.html?cat=9987%2C653%2C655&ev=exbrand_%E8%8B%B9%E6%9E%9C%EF%BC%88Apple%EF%BC%89%40&page=1&JL=3_%E5%93%81%E7%89%8C_%E8%8B%B9%E6%9E%9C%EF%BC%88Apple%EF%BC%89";
    	String domain = getDomain(url);
    	String subDomain = getSubDomain(url);
    	
    	System.out.println(domain);
    	System.out.println(subDomain);
	}
}
