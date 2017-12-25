package com.yida.spider4j.crawler.test.lianjia.processor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.Selectable;
import com.yida.spider4j.crawler.utils.Constant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

import java.util.List;

/**
 * @author Lanxiaowei(736031305@qq.com)
 * @ClassName: LianJiaStartPageProcessor
 * @Description: 链家租房起始页处理器
 * @date 2017年12月22日 下午20:40:10
 */
public class LianJiaStartPageProcessor extends SimpleStartPageProcessor {
    public LianJiaStartPageProcessor(PageProcessorParam pageProcessorParam) {
        super(pageProcessorParam);
    }

    /**
     * @param page
     * @param pageSize 每页显示大小
     * @param @return
     * @return int
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: determineTotalPage
     * @Description: 探测总页数
     */
    @Override
    public int determineTotalPage(Page page, int pageSize) {
        if (page == null) {
            return 0;
        }
        String text = page.getHtml().jsoup("span[class=list-head clear] > h2 > span,div[class=list-head clear] > h2 > span").get();
        if (StringUtils.isEmpty(text)) {
            return 0;
        }
        text = StringUtils.getNumberFromString(text);
        if (StringUtils.isEmpty(text)) {
            return 0;
        }
        try {
            int total = Integer.valueOf(text);
            if (pageSize <= 0) {
                pageSize = Constant.PAGE_SIZE;
            }
            return (total % pageSize == 0) ? (total / pageSize) : (total / pageSize) + 1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * @param @param  page
     * @param @return
     * @return int
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: determinePageSize
     * @Description: 探测每页显示大小
     */
    @Override
    public int determinePageSize(Page page) {
        if (page == null) {
            return Constant.PAGE_SIZE;
        }
        List<Selectable> list = page.getHtml().jsoup("ul#house-lst > li").nodes();
        if (null == list || list.size() <= 0) {
            return Constant.PAGE_SIZE;
        }
        return list.size();
    }

    /**
     * @param @param  page
     * @param @param  currentPage
     * @param @param  totalPage
     * @param @param  pageSize
     * @param @return
     * @return String
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: buildNextPageUrl
     * @Description: 构建下一页的请求URL
     */
    @Override
    public String buildNextPageUrl(Page page, int currentPage, int totalPage,
                                   int pageSize) {
        return "https://bj.lianjia.com/zufang/pg" + currentPage;
    }

    /**
     * @param @return
     * @return boolean
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: knownTotalPage
     * @Description: 告诉PageProcessor, 从已有的分页页面内容可否已知总页数
     * 需要用户实现
     */
    @Override
    public boolean knownTotalPage() {
        return true;
    }

    /**
     * @param @return
     * @return boolean
     * @throws
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: needPaging
     * @Description: 告诉PageProcessor, 列表页是否需要分页
     */
    @Override
    public boolean needPaging() {
        return true;
    }
}
