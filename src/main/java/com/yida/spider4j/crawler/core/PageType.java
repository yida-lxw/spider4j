package com.yida.spider4j.crawler.core;

/**
 * @ClassName: PageType
 * @Description: 页面类型,分4种[种子页,起始页,列表页,详情页]
 *               这也是爬虫的基本抓取流程:[种子页 -->] 起始页 --> 列表页 --> 详情页
 *               种子页不是必须的,可以直接起始页 --> 列表页 --> 详情页,
 *               特殊情况下(比如起始页需要分页,这时候需要引入种子页使用4个层级来抓取)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午2:41:53
 *
 */
public enum PageType {
	SEED_PAGE {             //种子页
		String getValue(){
			return SEED_PAGE_VALUE;
		}
	},
	SEED_PAGING_PAGE {             //种子页-分页URL
		String getValue(){
			return SEED_PAGING_PAGE_VALUE;
		}
	},
	START_PAGE {             //起始页
		String getValue(){
			return START_PAGE_VALUE;
		}
	},
	LIST_PAGE {             //列表页
		String getValue(){
			return LIST_PAGE_VALUE;
		}
	},
	DETAIL_PAGE {             //详情页
		String getValue(){
			return DETAIL_PAGE_VALUE;
		}
	};
	
	abstract String getValue();
	
	/**种子页*/
	public static final String SEED_PAGE_VALUE = "seed";
	
	/**种子页分页URL*/
	public static final String SEED_PAGING_PAGE_VALUE = "seed_paging";
	
	/**起始页*/
	public static final String START_PAGE_VALUE = "start";
	
	/**列表页*/
	public static final String LIST_PAGE_VALUE = "list";
	
	/**详情页*/
	public static final String DETAIL_PAGE_VALUE = "detail";
}
