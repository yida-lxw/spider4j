package com.yida.spider4j.crawler.service;

import com.yida.spider4j.crawler.bean.HotNews;
import com.yida.spider4j.crawler.utils.jdbc.BeanListHandler;
import com.yida.spider4j.crawler.utils.jdbc.JDBCUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotNewsService {
    /**热点总榜*/
    public List<HotNews> getTopNHotNews() {
        String querySQL = "select media,title,pageUrl,pageId,commentCount,channel from news_all_stat limit 0,10";
        return JDBCUtils.queryList(querySQL, new BeanListHandler(HotNews.class), new Object[]{}, true);
    }

    /**年度热点榜*/
    public List<HotNews> getTopNHotNewsOfYear() {
        String querySQL = "select media,title,pageUrl,pageId,commentCount,channel from news_year_stat limit 0,10";
        return JDBCUtils.queryList(querySQL, new BeanListHandler(HotNews.class), new Object[]{}, true);
    }

    /**月度热点榜*/
    public List<HotNews> getTopNHotNewsOfMonth() {
        String querySQL = "select media,title,pageUrl,pageId,commentCount,channel from news_month_stat limit 0,10";
        return JDBCUtils.queryList(querySQL, new BeanListHandler(HotNews.class), new Object[]{}, true);
    }

    /**每日热点榜*/
    public List<HotNews> getTopNHotNewsOfDay() {
        String querySQL = "select media,title,pageUrl,pageId,commentCount,channel from news_day_stat limit 0,10";
        return JDBCUtils.queryList(querySQL, new BeanListHandler(HotNews.class), new Object[]{}, true);
    }
}
