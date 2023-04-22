package com.yida.spider4j.crawler.selector;

import java.util.List;

/**
 * @ClassName: Selectable
 * @Description: 可提取数据抽象接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 上午10:07:30
 *
 */
public interface Selectable {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: xpath
	 * @Description: xpath提取
	 * @param @param xpath
	 * @param @return
	 * @return Selectable
	 * @throws
	 */
    public Selectable xpath(String xpath);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: xpath
     * @Description: xpath提取
     * @param @param xpath     xpath表达式
     * @param @param attrName  要提取的属性名称
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable xpath(String xpath,String attrName);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: xpath
     * @Description: xpath提取
     * @param @param xpath
     * @param @param multi  是否提取匹配到的多个节点
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable xpath(String xpath,boolean multi);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: xpath
     * @Description: xpath提取
     * @param @param xpath
     * @param @param attrName
     * @param @param multi  是否提取匹配到的多个节点
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable xpath(String xpath,String attrName,boolean multi);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsoup
     * @Description: jsoup提取
     * @param @param jsoupExpression
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsoup(String jsoupExpression);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsoup
     * @Description: jsoup提取
     * @param @param jsoupExpression
     * @param @param attrName
     * @param @param firstChild
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsoup(String jsoupExpression, String attrName);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsoup
     * @Description: jsoup提取
     * @param @param jsoupExpression
     * @param @param multi  是否提取匹配到的多个节点
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsoup(String jsoupExpression, boolean multi);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsoup
     * @Description: jsoup提取
     * @param @param jsoupExpression
     * @param @param attrName
     * @param @param multi  是否提取匹配到的多个节点
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsoup(String jsoupExpression, String attrName,boolean multi);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: regex
     * @Description: 正则表达式提取
     * @param @param regex
     * @param @param group
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable regex(String regex, int group);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: regex
     * @Description: 正则表达式提取
     * @param @param regex
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable regex(String regex);
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsonPath
     * @Description: JsonPath表达式提取
     * @param @param jsonPath
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsonPath(String jsonPath);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: smartContent
     * @Description: 页面内容美化
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable smartContent();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: links
     * @Description: 提取url
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable links();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: replace
     * @Description: 根据一个正则表达式替换
     * @param @param regex
     * @param @param replacement
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable replace(String regex, String replacement);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: toString
     * @Description: toString
     * @param @return
     * @return String
     * @throws
     */
    public String toString();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: get
     * @Description: 获取匹配到的第一个元素的文本
     * @param @return
     * @return String
     * @throws
     */
    public String get();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: match
     * @Description: 是否匹配到数据
     * @param @return
     * @return boolean
     * @throws
     */
    public boolean match();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: all
     * @Description: 返回匹配到的多个元素的文本
     * @param @return
     * @return List<String>
     * @throws
     */
    public List<String> all();

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: select
     * @Description: 根据选择器提取数据
     * @param @param selector
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable select(Selector selector);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectList
     * @Description: 根据选择器提取数据,支持多条
     * @param @param selector
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable selectList(Selector selector);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: nodes
     * @Description: 返回匹配到的所有元素
     * @param @return
     * @return List<Selectable>
     * @throws
     */
    public List<Selectable> nodes();
}
