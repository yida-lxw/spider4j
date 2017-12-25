package com.yida.spider4j.crawler.test.lianjia;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.pipeline.DataBasePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.lianjia.processor.LianJiaDetailPageProcessor;
import com.yida.spider4j.crawler.test.lianjia.processor.LianJiaListPageProcessor;
import com.yida.spider4j.crawler.test.lianjia.processor.LianJiaStartPageProcessor;

/**
 * @author Lanxiaowei(736031305@qq.com)
 * @ClassName: LianJiaSpider
 * @Description: 链家租房爬虫测试类
 * @date 2017年12月22日 下午22:27:58
 */
public class LianJiaSpider {
    public static void main(String[] args) {
        //start
        MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
        LianJiaStartPageProcessor startPageProcessor = new LianJiaStartPageProcessor(startPageProcessorParam);

        //list
        MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
        listPageProcessorParam.setTargetUrlJsoup("div[class=list-wrap] > ul[id=house-lst] > li > div[class=info-panel] > h2 > a");
        //获取href属性值
        listPageProcessorParam.setAttributeName("href");
        LianJiaListPageProcessor listPageProcessor = new LianJiaListPageProcessor(listPageProcessorParam);

        //detail
        DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
        LianJiaDetailPageProcessor detailPageProcessor = new LianJiaDetailPageProcessor(detailPageProcessorParam);

        String startUrl = "https://bj.lianjia.com/zufang";

        DataBasePipeline dataBasePipeline = new DataBasePipeline("houseDB", "rent_house", "lianjiaNO");
        Spider.create(startPageProcessor, listPageProcessor, detailPageProcessor).startUrl(startUrl)
                .addPipeline(new ConsolePipeline())
                .addPipeline(dataBasePipeline)
                .threadNum(20)
                .start();
    }
}
