package com.yida.spider4j.crawler.selector;

/**
 * @ClassName: ExpressionType
 * @Description: 表达式类型,分3种[Regex,Xpath,Jsoup]
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月17日 下午1:12:20
 *
 */
public enum ExpressionType {
	REGEX {             //Regex
		String getValue(){
			return REGEX_VALUE;
		}
	},
	XPATH {             //Xpath
		String getValue(){
			return XPATH_VALUE;
		}
	},
	JSOUP {             //JSoup
		String getValue(){
			return JSOUP_VALUE;
		}
	};
	
	abstract String getValue();
	
	/**正则*/
	public static final String REGEX_VALUE = "regex";
	
	/**Xpath*/
	public static final String XPATH_VALUE = "xpath";
	
	/**JSoup*/
	public static final String JSOUP_VALUE = "jsoup";
}
